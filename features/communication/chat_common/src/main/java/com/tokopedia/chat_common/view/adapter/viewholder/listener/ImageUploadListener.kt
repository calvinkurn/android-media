package com.tokopedia.chat_common.view.adapter.viewholder.listener

import com.tokopedia.chat_common.data.ImageUploadUiModel

/**
 * @author by nisie on 28/11/18.
 */
interface ImageUploadListener {
    fun onImageUploadClicked(imageUrl: String, replyTime: String, isSecure: Boolean)
    fun onRetrySendImage(element: ImageUploadUiModel)
    fun onImageUploadCancelClicked(image : ImageUploadUiModel){}
}