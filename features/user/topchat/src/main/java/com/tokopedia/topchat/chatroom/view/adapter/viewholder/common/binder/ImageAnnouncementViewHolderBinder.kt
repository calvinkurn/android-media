package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ImageAnnouncementUiModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener

object ImageAnnouncementViewHolderBinder {

    private val imageSizer = DynamicSizeImageRequestListener()

    fun bindBannerImage(
        uiModel: ImageAnnouncementUiModel,
        banner: ImageView?
    ) {
        ImageHandler.loadImageWithListener(banner, uiModel.imageUrl, imageSizer)
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