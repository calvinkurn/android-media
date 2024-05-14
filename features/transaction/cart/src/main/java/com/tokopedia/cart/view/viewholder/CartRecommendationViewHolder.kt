package com.tokopedia.cart.view.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.cart.R
import com.tokopedia.cart.databinding.ItemCartRecommendationBinding
import com.tokopedia.cart.view.ActionListener
import com.tokopedia.cart.view.uimodel.CartRecommendationItemHolderData
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.unifycomponents.UnifyButton

/**
 * Created by Irfan Khoirul on 2019-05-29.
 */

class CartRecommendationViewHolder(
    private val binding: ItemCartRecommendationBinding,
    val actionListener: ActionListener?
) : RecyclerView.ViewHolder(binding.root),
    AppLogRecTriggerInterface {

    companion object {
        @JvmStatic
        val LAYOUT = R.layout.item_cart_recommendation
    }

    internal var isTopAds = false
    private var recTriggerObject = RecommendationTriggerObject()
    private var recommendationItem: RecommendationItem? = null

    fun bind(element: CartRecommendationItemHolderData) {
        this.recommendationItem = element.recommendationItem
        setRecTriggerObject(element.recommendationItem)
        binding.productCardView.apply {
            setProductModel(
                element.recommendationItem.toProductCardModel(true, UnifyButton.Type.MAIN)
                    .copy(isInBackground = true)
            )
            setOnClickListener(object : ProductCardClickListener {
                override fun onClick(v: View) {
                    AppLogRecommendation.sendProductClickAppLog(
                        element.recommendationItem.asProductTrackModel(entranceForm = EntranceForm.PURE_GOODS_CARD)
                    )
                    actionListener?.onRecommendationProductClicked(
                        element.recommendationItem
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

            setVisibilityPercentListener(
                isTopAds = element.recommendationItem.isTopAds,
                eventListener = object : ProductConstraintLayout.OnVisibilityPercentChanged {
                    override fun onShow() {
                        element.recommendationItem.sendShowAdsByteIo(itemView.context)
                    }

                    override fun onShowOver(maxPercentage: Int) {
                        element.recommendationItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
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
