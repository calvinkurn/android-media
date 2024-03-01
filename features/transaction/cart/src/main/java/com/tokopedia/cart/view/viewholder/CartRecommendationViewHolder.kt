package com.tokopedia.cart.view.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecommendationBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.recommendation_widget_common.extension.asProductTrackModel
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartRecommendationViewHolder(private val binding: ItemCartRecommendationBinding, val actionListener: ActionListener?) :
    RecyclerView.ViewHolder(binding.root), AppLogRecTriggerInterface {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_cart_recommendation
    }

    internal var isTopAds = false

    private var recTriggerObject = RecommendationTriggerObject()

    fun bind(element: CartRecommendationItemHolderData) {
        setRecTriggerObject(element.recommendationItem)
        binding.productCardView.apply {
            setProductModel(
                element.recommendationItem.toProductCardModel(true, UnifyButton.Type.MAIN)
            )
            setOnClickListener {
                AppLogRecommendation.sendProductClickAppLog(
                    element.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                )
                actionListener?.onRecommendationProductClicked(
                    element.recommendationItem
                )
            }
            setAddToCartOnClickListener {
                actionListener?.onButtonAddToCartClicked(element)
            }

            setImageProductViewHintListener(
                element.recommendationItem,
                object : ViewHintListener {
                    override fun onViewHint() {
                        actionListener?.onRecommendationProductImpression(
                            element.recommendationItem
                        )
                    }
                }
            )

            addOnImpression1pxListener(element.recommendationItem.appLogImpressHolder) {
                AppLogRecommendation.sendProductShowAppLog(
                    element.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                )
            }
        }

        if (!element.hasSentImpressionAnalytics) {
            actionListener?.onRecommendationImpression(element)
            element.hasSentImpressionAnalytics = true
        }

        isTopAds = element.recommendationItem.isTopAds
    }

    private fun setRecTriggerObject(model: RecommendationItem) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName,
        )
    }

    fun clearImage() {
        binding.productCardView.recycle()
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }
}
