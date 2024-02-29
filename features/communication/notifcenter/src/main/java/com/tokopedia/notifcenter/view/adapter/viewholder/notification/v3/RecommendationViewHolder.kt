package com.tokopedia.notifcenter.view.adapter.viewholder.notification.v3

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.notifcenter.R
import com.tokopedia.notifcenter.data.uimodel.RecommendationUiModel
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.extension.asProductTrackModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem

class RecommendationViewHolder constructor(
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
        productCard?.setOnClickListener {
            AppLogRecommendation.sendProductClickAppLog(
                element.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
            )
            recommendationListener?.onProductClick(
                    element.recommendationItem, null
            )
        }
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
