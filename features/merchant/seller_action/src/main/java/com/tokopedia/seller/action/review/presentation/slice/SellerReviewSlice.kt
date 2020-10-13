package com.tokopedia.seller.action.review.presentation.slice

import android.app.PendingIntent
import android.content.Context
import android.net.Uri
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.core.graphics.drawable.IconCompat
import androidx.slice.Slice
import androidx.slice.builders.*
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.seller.action.R
import com.tokopedia.seller.action.common.presentation.slices.SellerSuccessSlice
import com.tokopedia.seller.action.review.domain.model.InboxReviewList

class SellerReviewSlice(context: Context,
                        sliceUri: Uri,
                        private val reviewStarsList: List<InboxReviewList>): SellerSuccessSlice<InboxReviewList>(reviewStarsList, context, sliceUri) {

    @RequiresApi(Build.VERSION_CODES.KITKAT)
    override fun getSuccessSlice(): Slice =
            list(context, sliceUri, ListBuilder.INFINITY) {
                header {
                    title = context.getString(R.string.seller_action_review_title)
                }
                reviewStarsList.forEach {
                    row {
                        val pendingIntent =
                                RouteManager.getIntent(context, ApplinkConstInternalMarketplace.INBOX_REPUTATION)?.let { intent ->
                                    PendingIntent.getActivity(
                                            context,
                                            0,
                                            intent,
                                            0
                                    )
                                }
                        pendingIntent?.let { intent ->
                            primaryAction = SliceAction.create(
                                    intent,
                                    IconCompat.createWithResource(context, R.drawable.ic_sellerapp_slice),
                                    ListBuilder.ICON_IMAGE,
                                    it.product.productName.orEmpty())
                        }
                        title = context.getString(R.string.seller_action_review_item_title, it.user.userName, it.product.productName)
                        subtitle = context.getString(R.string.seller_action_review_item_desc, it.rating.toString(), it.reviewText)
                    }
                }
            }

}