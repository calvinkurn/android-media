package com.tokopedia.recentview.view.presenter

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.recentview.domain.usecase.RecentViewUseCase
import com.tokopedia.recentview.view.viewmodel.RecentViewDetailProductDataModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlistcommon.domain.AddToWishlistV2UseCase
import com.tokopedia.wishlistcommon.domain.DeleteWishlistV2UseCase
import com.tokopedia.wishlistcommon.listener.WishlistV2ActionListener
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

/**
 * @author by yoasfs on 13/08/20
 */

@SuppressLint("SyntheticAccessor")
@ExperimentalCoroutinesApi
open class RecentViewViewModel @Inject constructor(
        private val baseDispatcher: CoroutineDispatchers,
        private val userSession: UserSessionInterface,
        private val addToWishlistV2UseCase: AddToWishlistV2UseCase,
        private val deleteWishlistV2UseCase: DeleteWishlistV2UseCase,
        private val recentViewUseCase: RecentViewUseCase
): BaseViewModel(baseDispatcher.io) {


    val recentViewDetailProductDataResp : LiveData<Result<ArrayList<RecentViewDetailProductDataModel>>>
        get() = _recentViewDetailProductDataResp
    private val _recentViewDetailProductDataResp : MutableLiveData<Result<ArrayList<RecentViewDetailProductDataModel>>> = MutableLiveData()

    val addWishlistResponse: LiveData<Result<String>> get() = _addWishlistResponse
    private val _addWishlistResponse = MutableLiveData<Result<String>>()

    val removeWishlistResponse: LiveData<Result<String>> get() = _removeWishlistResponse
    private val _removeWishlistResponse = MutableLiveData<Result<String>>()

    fun getRecentView() {
        recentViewUseCase.apply {
            getParam(userSession.userId)
        }.execute({
            _recentViewDetailProductDataResp.postValue(Success(it))
        }, {
            _recentViewDetailProductDataResp.postValue(Fail(it))
        })
    }

    fun addToWishlistV2(productId: String, wishlistV2ActionListener: WishlistV2ActionListener) {
        launch(baseDispatcher.main) {
            addToWishlistV2UseCase.setParams(productId, userSession.userId)
            val result = withContext(baseDispatcher.io) { addToWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistV2ActionListener.onSuccessAddWishlist(result.data, productId)
            } else if (result is Fail) {
                wishlistV2ActionListener.onErrorAddWishList(result.throwable, productId)
            }
        }
    }

    fun removeFromWishlistV2(productId: String, wishlistV2ActionListener: WishlistV2ActionListener) {
        launch(baseDispatcher.main) {
            deleteWishlistV2UseCase.setParams(productId, userSession.userId)
            val result = withContext(baseDispatcher.io) { deleteWishlistV2UseCase.executeOnBackground() }
            if (result is Success) {
                wishlistV2ActionListener.onSuccessRemoveWishlist(result.data, productId)
            } else if (result is Fail) {
                wishlistV2ActionListener.onErrorRemoveWishlist(result.throwable, productId)
            }
        }
    }
}