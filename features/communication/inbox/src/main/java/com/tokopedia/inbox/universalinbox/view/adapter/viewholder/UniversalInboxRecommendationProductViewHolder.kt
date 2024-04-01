package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.provider.IAdsViewHolderTrackListener
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationProductItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationProductViewHolder(
    itemView: View,
    private val recommendationListener: RecommendationListener
) : AbstractViewHolder<UniversalInboxRecommendationUiModel>(itemView), AppLogRecTriggerInterface, IAdsViewHolderTrackListener {

    private val binding: UniversalInboxRecommendationProductItemBinding? by viewBinding()

    private var recTriggerObject = RecommendationTriggerObject()

    private var isProductAds = false

    override fun bind(uiModel: UniversalInboxRecommendationUiModel) {
        isProductAds = uiModel.recommendationItem.isTopAds

        setRecTriggerObject(uiModel.recommendationItem)
        binding?.inboxProductRecommendation?.run {
            setProductModel(uiModel.recommendationItem.toProductCardModel(hasThreeDots = true))
            setImageProductViewHintListener(
                uiModel.recommendationItem,
                object : ViewHintListener {
                    override fun onViewHint() {
                        recommendationListener.onProductImpression(uiModel.recommendationItem)
                    }
                }
            )

            addOnImpression1pxListener(uiModel.recommendationItem.appLogImpressHolder) {
                AppLogRecommendation.sendProductShowAppLog(
                    uiModel.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                )
            }

            setProductInfoOnClickListener {
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.INBOX,
                    AdsLogRealtimeClickModel(
                        "",
                        "",
                        "",
                        "",
                        System.currentTimeMillis().toString(),
                        AdsLogRealtimeClickModel.AdExtraData(
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                )
            }

            setImageProductClickListener {
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.INBOX,
                    AdsLogRealtimeClickModel(
                        "",
                        "",
                        "",
                        "",
                        System.currentTimeMillis().toString(),
                        AdsLogRealtimeClickModel.AdExtraData(
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                )
            }

            setShopTypeLocationOnClickListener {
                AppLogTopAds.sendEventRealtimeClick(
                    itemView.context,
                    PageName.INBOX,
                    AdsLogRealtimeClickModel(
                        "",
                        "",
                        "",
                        "",
                        System.currentTimeMillis().toString(),
                        AdsLogRealtimeClickModel.AdExtraData(
                            "",
                            "",
                            "",
                            ""
                        )
                    )
                )
            }

            setOnClickListener {
                AppLogRecommendation.sendProductClickAppLog(
                    uiModel.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                )

                recommendationListener.onProductClick(
                    uiModel.recommendationItem,
                    null,
                    bindingAdapterPosition
                )
            }

            setThreeDotsOnClickListener {
                recommendationListener.onThreeDotsClick(
                    uiModel.recommendationItem,
                    bindingAdapterPosition
                )
            }
        }
    }

    fun bind(uiModel: RecommendationItem, payloads: Bundle) {
        val isWishListed = payloads.getBoolean(WISHLIST_STATUS_IS_WISHLIST) as? Boolean ?: return
        uiModel.isWishlist = isWishListed
        binding?.inboxProductRecommendation?.setThreeDotsOnClickListener {
            recommendationListener.onThreeDotsClick(uiModel, layoutPosition)
        }
    }

    private fun setRecTriggerObject(model: RecommendationItem) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName
        )
    }

    override fun onViewRecycled() {
        binding?.inboxProductRecommendation?.recycle()
    }

    override fun onViewAttachedToWindow(recyclerView: RecyclerView?) {
        if (isProductAds) {
        }
    }

    override fun onViewDetachedToWindow(recyclerView: RecyclerView?) {
        if (isProductAds) {
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_product_item
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject = recTriggerObject
}
