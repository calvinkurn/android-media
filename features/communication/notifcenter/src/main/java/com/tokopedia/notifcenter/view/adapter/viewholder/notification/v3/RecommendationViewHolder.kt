package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationViewHolder(
    itemView: View?,
    private val recommendationListener: RecommendationListener?
) : AbstractViewHolder<RecommendationUiModel>(itemView), AppLogRecTriggerInterface {

    private val productCard: ProductCardGridView? = itemView?.findViewById(
        R.id.notification_product_card
    )

    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(element: RecommendationUiModel, payloads: MutableList<Any>) {
        val isWishlisted = payloads.getOrNull(0) as? Boolean ?: return
        element.recommendationItem.isWishlist = isWishlisted
        productCard?.setThreeDotsOnClickListener {
            recommendationListener?.onThreeDotsClick(element.recommendationItem, adapterPosition)
        }
    }

    override fun bind(element: RecommendationUiModel) {
        setRecTriggerObject(element.recommendationItem)
        bindProductCardUi(element)
        bindProductCardImpression(element)
        bindProductCardClick(element)
        bindProductCardThreeDotsClick(element)
        productCard?.setVisibilityPercentListener(element.recommendationItem.isTopAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
            override fun onShow() {
                element.recommendationItem.sendShowAdsByteIo(itemView.context)
            }

            override fun onShowOver(maxPercentage: Int) {
                element.recommendationItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
            }
        })
    }

    private fun setRecTriggerObject(model: RecommendationItem) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName,
        )
    }

    private fun bindProductCardUi(element: RecommendationUiModel) {
        productCard?.setProductModel(element.recommendationItem.toProductCardModel())
    }

    private fun bindProductCardImpression(element: RecommendationUiModel) {
        productCard?.setImageProductViewHintListener(
            element.recommendationItem,
            object : ViewHintListener {
                override fun onViewHint() {
                    recommendationListener?.onProductImpression(element.recommendationItem)
                }
            }
        )

        productCard?.addOnImpression1pxListener(element.recommendationItem.appLogImpressHolder) {
            AppLogRecommendation.sendProductShowAppLog(
                element.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
            )
        }
    }

    private fun bindProductCardClick(element: RecommendationUiModel) {
        productCard?.setOnClickListener(object : ProductCardClickListener {
            override fun onClick(v: View) {
                AppLogRecommendation.sendProductClickAppLog(
                    element.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                )
                recommendationListener?.onProductClick(
                    element.recommendationItem, null
                )
            }

            override fun onAreaClicked(v: View) {
                element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                element.recommendationItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
            }
        })
    }

    private fun bindProductCardThreeDotsClick(element: RecommendationUiModel) {
        productCard?.setThreeDotsOnClickListener {
            recommendationListener?.onThreeDotsClick(
                element.recommendationItem, adapterPosition
            )
        }
    }

    companion object {
        val LAYOUT = R.layout.item_notifcenter_recommendation
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }
}
