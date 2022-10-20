package com.tokopedia.review.feature.inbox.buyerreview.domain.model.inboxdetail

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.tokopedia.kotlin.extensions.view.ZERO

data class InboxReputationResponseWrapper(
    @SerializedName("data")
    @Expose
    val data: Data
) {
    data class Data(
        @SerializedName("inboxReviewReputationListV2")
        @Expose
        val response: Response
    ) {
        data class Response(
            @SerializedName("reputationList")
            @Expose
            val reputationList: List<Reputation> = listOf(),
            @SerializedName("hasNext")
            @Expose
            val hasNext: Boolean = false,
        ) {
            data class Reputation(
                @SerializedName("shopIdStr")
                @Expose
                val shopIdStr: String = "",
                @SerializedName("userIdStr")
                @Expose
                val userIdStr: String = "",
                @SerializedName("reputationIdStr")
                @Expose
                val reputationIdStr: String = "",
                @SerializedName("orderData")
                @Expose
                val orderData: OrderData = OrderData(),
                @SerializedName("revieweeData")
                @Expose
                val revieweeData: RevieweeData = RevieweeData(),
                @SerializedName("reputationData")
                @Expose
                val reputationData: ReputationData = ReputationData(),
            ) {
                data class OrderData(
                    @SerializedName("invoiceRefNum")
                    @Expose
                    val invoiceRefNum: String = "",
                    @SerializedName("createTimeFmt")
                    @Expose
                    val createTimeFmt: String = "",
                )

                data class RevieweeData(
                    @SerializedName("name")
                    @Expose
                    val name: String = "",
                    @SerializedName("roleId")
                    @Expose
                    val roleId: String = "0",
                    @SerializedName("picture")
                    @Expose
                    val picture: String = "",
                    @SerializedName("buyerBadge")
                    @Expose
                    val buyerBadge: BuyerBadge = BuyerBadge(),
                    @SerializedName("shopBadge")
                    @Expose
                    val shopBadge: ShopBadge = ShopBadge(),
                ) {
                    data class BuyerBadge(
                        @SerializedName("positive")
                        @Expose
                        val positive: Int = Int.ZERO,
                        @SerializedName("neutral")
                        @Expose
                        val neutral: Int = Int.ZERO,
                        @SerializedName("negative")
                        @Expose
                        val negative: Int = Int.ZERO,
                        @SerializedName("positivePercentage")
                        @Expose
                        val positivePercentage: String = "",
                        @SerializedName("noReputation")
                        @Expose
                        val noReputation: Int = Int.ZERO,
                    )

                    data class ShopBadge(
                        @SerializedName("tooltip")
                        @Expose
                        val tooltip: String = "",
                        @SerializedName("reputationScore")
                        @Expose
                        val reputationScore: String = "",
                        @SerializedName("score")
                        @Expose
                        val score: Int = Int.ZERO,
                        @SerializedName("minBadgeScore")
                        @Expose
                        val minBadgeScore: Int = Int.ZERO,
                        @SerializedName("reputationBadgeUrl")
                        @Expose
                        val reputationBadgeUrl: String = "",
                        @SerializedName("isFavorited")
                        @Expose
                        var isFavorited: Int = Int.ZERO,
                    )
                }

                data class ReputationData(
                    @SerializedName("revieweeScore")
                    @Expose
                    val revieweeScore: Int = Int.ZERO,
                    @SerializedName("revieweeScoreStatus")
                    @Expose
                    val revieweeScoreStatus: Int = Int.ZERO,
                    @SerializedName("showRevieweeScore")
                    @Expose
                    val showRevieweeScore: Boolean = false,
                    @SerializedName("reviewerScore")
                    @Expose
                    val reviewerScore: Int = Int.ZERO,
                    @SerializedName("reviewerScoreStatus")
                    @Expose
                    val reviewerScoreStatus: Int = Int.ZERO,
                    @SerializedName("isEditable")
                    @Expose
                    val isEditable: Boolean = false,
                    @SerializedName("isInserted")
                    @Expose
                    val isInserted: Boolean = false,
                    @SerializedName("isLocked")
                    @Expose
                    val isLocked: Boolean = false,
                    @SerializedName("isAutoScored")
                    @Expose
                    val isAutoScored: Boolean = false,
                    @SerializedName("isCompleted")
                    @Expose
                    val isCompleted: Boolean = false,
                    @SerializedName("showLockingDeadline")
                    @Expose
                    val showLockingDeadline: Boolean = false,
                    @SerializedName("lockingDeadlineDays")
                    @Expose
                    val lockingDeadlineDays: Int = Int.ZERO,
                    @SerializedName("showBookmark")
                    @Expose
                    val showBookmark: Boolean = false,
                    @SerializedName("actionMessage")
                    @Expose
                    val actionMessage: String = "",
                )
            }
        }
    }
}