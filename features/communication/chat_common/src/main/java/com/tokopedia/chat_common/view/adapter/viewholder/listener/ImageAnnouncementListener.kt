package com.tokopedia.chat_common.view.adapter.viewholder.listener

import com.tokopedia.chat_common.data.ImageAnnouncementUiModel

/**
 * @author by nisie on 28/11/18.
 */
interface ImageAnnouncementListener{
    fun onImageAnnouncementClicked(uiModel : ImageAnnouncementUiModel)
    fun onCtaBroadcastClicked(uiModel: ImageAnnouncementUiModel)
}
