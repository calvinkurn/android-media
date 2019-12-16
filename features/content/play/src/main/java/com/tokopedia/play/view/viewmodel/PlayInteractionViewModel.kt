package com.tokopedia.play.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.PARTNER_NAME_ADMIN
import com.tokopedia.play.data.TotalLike
import com.tokopedia.play.domain.GetShopInfoUseCase
import com.tokopedia.play.domain.GetTotalLikeUseCase
import com.tokopedia.play.domain.PostFollowShopUseCase
import com.tokopedia.play.domain.PostLikeUseCase
import com.tokopedia.play.ui.toolbar.model.PartnerType
import com.tokopedia.play.ui.toolbar.model.TitleToolbar
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Result
import com.tokopedia.usecase.coroutines.Success
import kotlinx.coroutines.*
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

/**
 * Created by jegul on 29/11/19
 */
class PlayInteractionViewModel @Inject constructor(
        private val getShopInfoUseCase: GetShopInfoUseCase,
        private val getTotalLikeUseCase: GetTotalLikeUseCase,
        private val postLikeUseCase: PostLikeUseCase,
        private val postFollowShopUseCase: PostFollowShopUseCase,
        dispatchers: CoroutineDispatcher
) : BaseViewModel(dispatchers), CoroutineScope {

    private val job = SupervisorJob()

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + job

    private val _observableToolbarInfo = MutableLiveData<Result<TitleToolbar>>()
    val observableToolbarInfo: LiveData<Result<TitleToolbar>> = _observableToolbarInfo

    private val _observableTotalLikes = MutableLiveData<Result<TotalLike>>()
    val observableTotalLikes: LiveData<Result<TotalLike>> = _observableTotalLikes

    private fun getShopInfo(shopId: Long, partnerType: PartnerType)  {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getShopInfoUseCase.params = GetShopInfoUseCase.createParam(shopId.toString())
                getShopInfoUseCase.executeOnBackground()
            }

            val titleToolbar = TitleToolbar(
                    shopId,
                    response.shopCore.name,
                    partnerType,
                    response.favoriteData.alreadyFavorited == 1)
            _observableToolbarInfo.value = Success(titleToolbar)
        }) {
            _observableToolbarInfo.value = Fail(it)
        }
    }

    fun getToolbarInfo(partnerType: PartnerType, partnerId: Long) {
        if (partnerType == PartnerType.ADMIN) {
            val titleToolbar = TitleToolbar(
                    partnerId,
                    PARTNER_NAME_ADMIN,
                    partnerType,
                    true)
            _observableToolbarInfo.value = Success(titleToolbar)
            return
        }

        if (partnerType == PartnerType.INFLUENCER) {
            val titleToolbar = TitleToolbar(
                    partnerId,
                    "",
                    partnerType,
                    true)
            _observableToolbarInfo.value = Success(titleToolbar)
            return
        }

        if (partnerType == PartnerType.SHOP)
            getShopInfo(partnerId, partnerType)
    }

    fun getTotalLikes(channelId: String) {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getTotalLikeUseCase.channelId = channelId
                getTotalLikeUseCase.executeOnBackground()
            }
            _observableTotalLikes.value = Success(response)
        }) {
            _observableTotalLikes.value = Fail(it)
        }
    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}