package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail

/**
 * Created by Nisie on 2/16/16.
 */
data class ImageUpload(
    var picSrc: String = "",
    var picSrcLarge: String = "",
    var description: String = "",
    var imageId: String = "",
    var fileLoc: String = "",
    var picObj: String = "",
    var position: Int = 0,
    var isSelected: Boolean = false
)