package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import android.view.View
import android.widget.ImageView
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener
import com.tokopedia.media.loader.loadImage

object ImageAnnouncementViewHolderBinder {

    fun bindBannerImage(
        uiModel: ImageAnnouncementUiModel,
        banner: ImageView?
    ) {
        banner?.loadImage(uiModel.imageUrl) {
            adaptiveImageSizeRequest(true)
            fitCenter()
        }
    }

    fun bindBannerClick(
        uiModel: ImageAnnouncementUiModel,
        view: View?,
        listener: ImageAnnouncementListener
    ) {
        view?.setOnClickListener {
            listener.onImageAnnouncementClicked(uiModel)
        }
    }

}