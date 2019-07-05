package com.tokopedia.groupchat.room.view.presenter

import com.tokopedia.abstraction.base.view.presenter.BaseDaggerPresenter
import com.tokopedia.groupchat.room.domain.usecase.GetVideoStreamUseCase
import com.tokopedia.groupchat.room.view.listener.PlayActivityContract
import com.tokopedia.groupchat.room.view.viewmodel.VideoStreamViewModel
import rx.Subscriber
import javax.inject.Inject

/**
 * @author : Steven 19/06/19
 */
class PlayActivityPresenter @Inject constructor() : BaseDaggerPresenter<PlayActivityContract.View>(), PlayActivityContract.Presenter {

}