package com.tokopedia.play.view.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.kotlin.extensions.coroutines.launchCatchError
import com.tokopedia.play.PARTNER_TYPE_ADMIN
import com.tokopedia.play.PARTNER_TYPE_INFLUENCER
import com.tokopedia.play.PARTNER_TYPE_SHOP
import com.tokopedia.play.data.Like
import com.tokopedia.play.domain.GetShopInfoUseCase
import com.tokopedia.play.domain.GetTotalLikeUseCase
import com.tokopedia.play.domain.PostFollowShopUseCase
import com.tokopedia.play.domain.PostLikeUseCase
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

    private val _observableTotalLikes = MutableLiveData<Result<Like>>()
    val observableTotalLikes: LiveData<Result<Like>> = _observableTotalLikes

    private fun getPeopleInfo(peopleId: String) {
        Log.wtf("Meyta", "getPeopleInfo $peopleId")
    }

    private fun getShopInfo(shopId: String, partnerType: String)  {
        launchCatchError(block = {
            val response = withContext(Dispatchers.IO) {
                getShopInfoUseCase.params = GetShopInfoUseCase.createParam(shopId)
                getShopInfoUseCase.executeOnBackground()
            }

            val titleToolbar = TitleToolbar(
                    shopId,
                    response.result[0].shopCore.name,
                    partnerType,
                    response.result[0].favoriteData.alreadyFavorited == 1)
            _observableToolbarInfo.value = Success(titleToolbar)
        }) {
            _observableToolbarInfo.value = Fail(it)
        }
    }

    fun getToolbarInfo(partnerType: String, partnerId: String) {
        if (partnerType == PARTNER_TYPE_ADMIN)
            return
        if (partnerType == PARTNER_TYPE_SHOP)
            getShopInfo(partnerId, partnerType)
        else if (partnerId == PARTNER_TYPE_INFLUENCER)
            getPeopleInfo(partnerId)
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

//    private val _observableChatList = MutableLiveData<PlayChat>()
//    val observableChatList: LiveData<PlayChat> = _observableChatList
//
//    private val listOfUser = listOf(
//            "Rifqi",
//            "Meyta",
//            "IJ",
//            "Yehez"
//    )
//
//    private val listOfMessage = listOf(
//            "Great product!",
//            "I watched all of that till te end and i decided i will buy this dress.",
//            "Great, wellspoken review. Such a wonderful information. Thanks a lot!"
//    )

//    fun startObservingChatList() {
//        launch {
//            var id = 0
//            while (job.isActive) {
//                _observableChatList.value =
//                        PlayChat(
//                                ++id,
//                                listOfUser.random(),
//                                listOfMessage.random()
//                        )
//                delay(5000)
//            }
//        }

//    }

    override fun onCleared() {
        super.onCleared()
        job.cancelChildren()
    }
}