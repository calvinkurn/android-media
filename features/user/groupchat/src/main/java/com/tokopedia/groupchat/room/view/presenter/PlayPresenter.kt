package com.tokopedia.groupchat.room.view.presenter

import android.util.Log
import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.groupchat.chatroom.view.viewmodel.ChannelInfoViewModel
import com.tokopedia.groupchat.room.domain.usecase.GetPlayInfoUseCase
import com.tokopedia.groupchat.room.view.listener.PlayContract
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 13/02/19
 */

class PlayPresenter @Inject constructor(
        var getPlayInfoUseCase: GetPlayInfoUseCase)
    : BaseDaggerPresenter<PlayContract.View>(), PlayContract.Presenter{


    fun getPlayInfo(channelId: String?, onSuccessGetInfo: (ChannelInfoViewModel) -> Unit) {
        getPlayInfoUseCase.execute(
                GetPlayInfoUseCase.createParams(channelId),
                object : Subscriber<ChannelInfoViewModel>() {
                    override fun onNext(t: ChannelInfoViewModel?) {
                        t?.let { onSuccessGetInfo(it) }
                    }

                    override fun onCompleted() {
                    }

                    override fun onError(e: Throwable?) {
                        Log.d("tevplay", e.toString())
                    }

                })
    }
}