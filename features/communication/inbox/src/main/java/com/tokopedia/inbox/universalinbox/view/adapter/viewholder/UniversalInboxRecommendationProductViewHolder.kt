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

    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(uiModel: UniversalInboxRecommendationUiModel) {
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
                    sendClickAdsByteIO(uiModel.recommendationItem, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    sendClickAdsByteIO(uiModel.recommendationItem, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(v: View) {
                    sendClickAdsByteIO(uiModel.recommendationItem, AdsLogConst.Refer.SELLER_NAME)
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

    private fun sendClickAdsByteIO(recommendationItem: RecommendationItem?, refer: String) {
        if (recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventRealtimeClick(
                itemView.context,
                PageName.INBOX,
                AdsLogRealtimeClickModel(
                    refer,
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogRealtimeClickModel.AdExtraData(
                        productId = recommendationItem?.productId.orZero().toString()
                    )
                )
            )
        }
    }

    override fun onViewAttachedToWindow(element: UniversalInboxRecommendationUiModel?) {
        if (element?.recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventShow(
                itemView.context,
                PageName.INBOX,
                AdsLogShowModel(
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogShowModel.AdExtraData(
                        productId = element.recommendationItem.productId.orZero().toString()
                    )
                )
            )
        }
    }

    override fun onViewDetachedFromWindow(
        element: UniversalInboxRecommendationUiModel?,
        visiblePercentage: Int
    ) {
        if (element?.recommendationItem?.isTopAds == true) {
            AppLogTopAds.sendEventShowOver(
                itemView.context,
                PageName.INBOX,
                AdsLogShowOverModel(
                    // todo this value from BE
                    0,
                    // todo this value from BE
                    0,
                    AdsLogShowOverModel.AdExtraData(
                        productId = element.recommendationItem.productId.orZero().toString(),
                        sizePercent = visiblePercentage.toString()
                    )
                )
            )
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
