package com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.binder

import android.widget.ImageView
import com.tokopedia.abstraction.common.utils.image.DynamicSizeImageRequestListener
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.chat_common.data.ImageAnnouncementViewModel

object ImageAnnouncementViewHolderBinder {

    private val imageSizer = DynamicSizeImageRequestListener()

    fun bindBannerImage(
            viewModel: ImageAnnouncementViewModel,
            banner: ImageView?
    ) {
        ImageHandler.loadImageWithListener(banner, viewModel.imageUrl, imageSizer)
    }

}