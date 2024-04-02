package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.os.Bundle
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogAnalytics
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
import com.tokopedia.analytics.byteio.topads.provider.IAdsViewHolderTrackListener
import com.tokopedia.analytics.byteio.topads.util.getVisibleHeightPercentage
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationProductItemBinding
import com.tokopedia.inbox.universalinbox.util.UniversalInboxValueUtil.WISHLIST_STATUS_IS_WISHLIST
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationUiModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.kotlin.extensions.view.orZero
import com.tokopedia.productcard.ProductCardGridView
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

    private var recommendationItem: RecommendationItem? = null

    override fun bind(uiModel: UniversalInboxRecommendationUiModel) {
        recommendationItem = uiModel.recommendationItem

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

            if (recommendationItem?.isTopAds == true) {
                setProductInfoClickListener()
                setImageProductClickListener()
                setShopTypeLocationOnClickListener()
            }


            setOnClickListener {

                if (recommendationItem?.isTopAds == false) {
                    AppLogRecommendation.sendProductClickAppLog(
                        uiModel.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                    )
                }

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

    private fun ProductCardGridView.setProductInfoClickListener() {

        setProductInfoOnClickListener {
            AppLogTopAds.sendEventRealtimeClick(
                itemView.context,
                PageName.INBOX,
                AdsLogRealtimeClickModel(
                    AdsLogConst.Refer.AREA,
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    System.currentTimeMillis().toString(),
                    AdsLogRealtimeClickModel.AdExtraData(
                        "",
                        productId = recommendationItem?.productId.orZero().toString()
                    )
                )
            )
        }
    }

    private fun ProductCardGridView.setImageProductClickListener() {

        setImageProductClickListener {
            AppLogTopAds.sendEventRealtimeClick(
                itemView.context,
                PageName.INBOX,
                AdsLogRealtimeClickModel(
                    AdsLogConst.Refer.COVER,
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    System.currentTimeMillis().toString(),
                    AdsLogRealtimeClickModel.AdExtraData(
                        getChannelName(),
                        productId = recommendationItem?.productId.orZero().toString()
                    )
                )
            )
        }
    }

    private fun ProductCardGridView.setShopTypeLocationOnClickListener() {

        setShopTypeLocationOnClickListener {
            AppLogTopAds.sendEventRealtimeClick(
                itemView.context,
                PageName.INBOX,
                AdsLogRealtimeClickModel(
                    AdsLogConst.Refer.SELLER_NAME,
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    System.currentTimeMillis().toString(),
                    AdsLogRealtimeClickModel.AdExtraData(
                        channel = getChannelName(),
                        productId = recommendationItem?.productId.orZero().toString()
                    )
                )
            )
        }
    }

    override fun onViewRecycled() {
        binding?.inboxProductRecommendation?.recycle()
    }

    override fun onViewAttachedToWindow(recyclerView: RecyclerView?) {
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventShow(
                itemView.context,
                PageName.INBOX,
                AdsLogShowModel(
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    System.currentTimeMillis().toString(),
                    AdsLogShowModel.AdExtraData(
                        channel = getChannelName(),
                        productId = recommendationItem?.productId.orZero().toString(),
                    )
                )
            )
        }
    }

    override fun onViewDetachedToWindow(recyclerView: RecyclerView?) {
        if (recommendationItem?.isTopAds == true) {

            val visiblePercentage = getVisibleHeightPercentage(recyclerView, binding?.inboxProductRecommendation)

            AppLogTopAds.sendEventShowOver(
                itemView.context,
                PageName.INBOX,
                AdsLogShowOverModel(
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    //todo this value from BE
                    "",
                    System.currentTimeMillis().toString(),
                    AdsLogShowOverModel.AdExtraData(
                        channel = getChannelName(),
                        productId = recommendationItem?.productId.orZero().toString(),
                        sizePercent = visiblePercentage,
                        rit = ""
                    )
                )
            )
        }
    }

    //need to confirm
    private fun getChannelName() = AppLogAnalytics.getTwoLastPage().orEmpty()

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_product_item
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject = recTriggerObject
}
