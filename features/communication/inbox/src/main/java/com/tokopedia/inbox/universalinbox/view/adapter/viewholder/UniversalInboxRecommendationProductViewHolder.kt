package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.analytics.byteio.topads.models.AdsLogRealtimeClickModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowModel
import com.tokopedia.analytics.byteio.topads.models.AdsLogShowOverModel
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationProductItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogRealtimeClickModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowOverModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationProductViewHolder(
    itemView: View,
    private val recommendationListener: RecommendationListener
) : AbstractViewHolder<UniversalInboxRecommendationUiModel>(itemView), AppLogRecTriggerInterface {

    private val binding: UniversalInboxRecommendationProductItemBinding? by viewBinding()
    private var recommendationItem: RecommendationItem? = null
    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(uiModel: UniversalInboxRecommendationUiModel) {
        this.recommendationItem = recommendationItem
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

            setOnClickListener(object : ProductCardClickListener {
                override fun onClick(v: View) {
                    AppLogRecommendation.sendProductClickAppLog(
                        uiModel.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                    )

                    recommendationListener.onProductClick(
                        uiModel.recommendationItem,
                        null,
                        bindingAdapterPosition
                    )
                }

                override fun onAreaClicked(v: View) {
                    AppLogTopAds.sendEventRealtimeClick(
                        itemView.context,
                        PageName.CART,
                        uiModel.recommendationItem.asAdsLogRealtimeClickModel(AdsLogConst.Refer.AREA)
                    )
                }

                override fun onProductImageClicked(v: View) {
                    AppLogTopAds.sendEventRealtimeClick(
                        itemView.context,
                        PageName.CART,
                        uiModel.recommendationItem.asAdsLogRealtimeClickModel(AdsLogConst.Refer.COVER)
                    )
                }

                override fun onSellerInfoClicked(v: View) {
                    AppLogTopAds.sendEventRealtimeClick(
                        itemView.context,
                        PageName.CART,
                        uiModel.recommendationItem.asAdsLogRealtimeClickModel(AdsLogConst.Refer.SELLER_NAME)
                    )
                }
            })

            setThreeDotsOnClickListener {
                recommendationListener.onThreeDotsClick(
                    uiModel.recommendationItem,
                    bindingAdapterPosition
                )
            }
        }
    }

    override fun onViewAttachedToWindow() {
        recommendationItem?.let { item ->
            if (item.isTopAds) AppLogTopAds.sendEventShow(itemView.context, PageName.INBOX,
                item.asAdsLogShowModel())
        }
    }

    override fun onViewDetachedFromWindow(visiblePercentage: Int) {
        recommendationItem?.let { item ->
            if (item.isTopAds) AppLogTopAds.sendEventShowOver(itemView.context, PageName.INBOX,
                item.asAdsLogShowOverModel(visiblePercentage))
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
            moduleName = model.pageName,
        )
    }

    override fun onViewRecycled() {
        binding?.inboxProductRecommendation?.recycle()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_product_item
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject = recTriggerObject
}
