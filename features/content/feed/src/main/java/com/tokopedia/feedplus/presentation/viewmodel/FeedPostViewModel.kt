package com.tokopedia.feedplus.presentation.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.abstraction.common.network.exception.MessageErrorException
import com.tokopedia.abstraction.common.network.exception.ResponseErrorException
import com.tokopedia.atc_common.domain.usecase.coroutine.AddToCartUseCase
import com.tokopedia.feedcomponent.presentation.utils.FeedResult
import com.tokopedia.feedplus.domain.usecase.FeedXHomeUseCase
import com.tokopedia.feedplus.presentation.model.FeedModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created By : Muhammad Furqan on 23/02/23
 */
class FeedPostViewModel @Inject constructor(
    private val feedXHomeUseCase: FeedXHomeUseCase,
    private val addToCartUseCase: AddToCartUseCase,
    private val userSession: UserSessionInterface,
    private val dispatchers: CoroutineDispatchers
) : ViewModel() {

    var source: String = ""

    private val _feedHome = MutableLiveData<Result<FeedModel>>()
    val feedHome: LiveData<Result<FeedModel>>
        get() = _feedHome

    private val _atcResp = MutableLiveData<FeedResult<Boolean>>()
    val atcRespData: LiveData<FeedResult<Boolean>>
        get() = _atcResp

    fun fetchFeedPosts(
        isNewData: Boolean = false,
        postId: String? = null,
    ) {
        if (isNewData) _feedHome.value = null

        viewModelScope.launch {
            val relevantPostsDeferred = async {
                try {
                    requireNotNull(postId)
                    require(isNewData)

                    getRelevantPosts(postId)
                } catch (e: Throwable) {
                    FeedModel.Empty
                }.items
            }

            val feedPostsDeferred = async {
                getFeedPosts(cursor = _feedHome.value?.cursor.orEmpty())
            }

            _feedHome.value = try {
                val feedPosts = feedPostsDeferred.await()
                Success(
                    feedPosts.copy(
                        items = relevantPostsDeferred.await() + feedPosts.items
                    )
                )
            } catch (e: Throwable) {
                Fail(e)
            }
        }
    }

    fun addToCart(
        productId: String,
        productName: String,
        price: String,
        shopId: String
    ) {
        _atcResp.value = FeedResult.Loading
        viewModelScope.launchCatchError(block = {
            val isSuccess = addToCartImplementation(productId, productName, price, shopId)
            _atcResp.value = FeedResult.Success(isSuccess)
        }) {
            _atcResp.value = FeedResult.Failure(it)
        }
    }

    private suspend fun getRelevantPosts(postId: String): FeedModel {
        return feedXHomeUseCase(
            feedXHomeUseCase.createPostDetailParams(postId)
        )
    }

    private suspend fun getFeedPosts(
        source: String = this.source,
        cursor: String = "",
    ): FeedModel {
        return feedXHomeUseCase(
            feedXHomeUseCase.createParams(
                source,
                cursor
            )
        )
    }

    private suspend fun addToCartImplementation(
        productId: String,
        productName: String,
        price: String,
        shopId: String,
    ): Boolean {
        val params = AddToCartUseCase.getMinimumParams(
            productId,
            shopId,
            productName = productName,
            price = price,
            userId = userSession.userId
        )
        try {
            addToCartUseCase.setParams(params)
            val response = addToCartUseCase.executeOnBackground()
            if (response.isDataError()) throw MessageErrorException(response.getAtcErrorMessage())
            return !response.isStatusError()
        } catch (e: Throwable) {
            if (e is ResponseErrorException) throw MessageErrorException(e.localizedMessage)
            else throw e
        }
    }

    private val Result<FeedModel>.cursor: String
        get() = when (this) {
            is Success -> data.pagination.cursor
            else -> ""
        }

}
