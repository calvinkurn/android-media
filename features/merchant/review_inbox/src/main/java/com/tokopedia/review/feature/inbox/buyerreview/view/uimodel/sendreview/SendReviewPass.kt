package com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.sendreview

import com.tokopedia.review.feature.inbox.buyerreview.view.uimodel.inboxdetail.ImageUpload
import java.util.*

/**
 * @author by nisie on 9/4/17.
 */
class SendReviewPass constructor(
    var reviewId: String, var productId: String, var reputationId: String, var shopId: String,
    var rating: String, var reviewMessage: String, var listImage: ArrayList<ImageUpload>,
    var listDeleted: List<ImageUpload>, var isShareFb: Boolean, var isAnonymous: Boolean
) {
    fun setListDeleted(listDeleted: ArrayList<ImageUpload>) {
        this.listDeleted = listDeleted
    }
}