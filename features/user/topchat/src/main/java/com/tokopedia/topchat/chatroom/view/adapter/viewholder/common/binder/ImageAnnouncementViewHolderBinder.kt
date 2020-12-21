package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel
import com.tokopedia.chat_common.view.adapter.viewholder.listener.ImageAnnouncementListener

object ImageAnnouncementViewHolderBinder {

    private val imageSizer = DynamicSizeImageRequestListener()

    fun bindBannerImage(
            viewModel: ImageAnnouncementViewModel,
            banner: ImageView?
    ) {
        ImageHandler.loadImageWithListener(banner, viewModel.imageUrl, imageSizer)
    }

    fun bindBannerClick(
            viewModel: ImageAnnouncementViewModel,
            view: View?,
            listener: ImageAnnouncementListener
    ) {
        view?.setOnClickListener {
            listener.onImageAnnouncementClicked(viewModel)
        }
    }

}