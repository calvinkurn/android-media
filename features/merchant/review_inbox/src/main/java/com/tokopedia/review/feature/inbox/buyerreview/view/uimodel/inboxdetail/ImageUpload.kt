package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

/**
 * Created by Nisie on 2/16/16.
 */
data class ImageUpload(
    var imageId: String = "",
    var fileLoc: String = "",
    var picSrc: String = "",
    var picSrcLarge: String = "",
    var picObj: String = "",
    var position: Int = 0,
    var description: String = "",
    var isSelected: Boolean = false
)