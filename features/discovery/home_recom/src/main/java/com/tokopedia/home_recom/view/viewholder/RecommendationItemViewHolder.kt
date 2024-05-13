package com.tokopedia.home_recom.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.EntranceForm
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.recommendation.AppLogRecommendation
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.home_recom.R
import com.tokopedia.home_recom.model.datamodel.RecommendationItemDataModel
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.kotlin.extensions.view.addOnImpression1pxListener
import com.tokopedia.productcard.ATCNonVariantListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.productcard.layout.ProductConstraintLayout
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asProductTrackModel
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.extension.toProductCardModel
import com.tokopedia.recommendation_widget_common.listener.RecommendationListener
import com.tokopedia.recommendation_widget_common.listener.RecommendationTokonowListener
import com.tokopedia.topads.sdk.utils.TopAdsUrlHitter

/**
 * Created by lukas on 21/05/2019
 *
 * A class for holder view Recommendation Item
 */
class RecommendationItemViewHolder(
    private val view: View,
    val listener: RecommendationListener,
    val tokonowListener: RecommendationTokonowListener? = null
) : AbstractViewHolder<RecommendationItemDataModel>(view), AppLogRecTriggerInterface {

    companion object {
        private const val RECOM_ITEM = "recom_item"
    }

    private val productCardView: ProductCardGridView by lazy { view.findViewById<ProductCardGridView>(R.id.product_item) }

    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(element: RecommendationItemDataModel) {
        setupCard(element)
        setRecTriggerObject(element)
    }

    override fun bind(element: RecommendationItemDataModel, payloads: MutableList<Any>) {
        if (payloads.isNotEmpty() && payloads.first() is Boolean) {
            setupCard(element.copy(productItem = element.productItem.copy(isWishlist = payloads.first() as Boolean)))
        }
    }

    private fun setupCard(element: RecommendationItemDataModel) {
        productCardView.run {
            setProductModel(element.productItem.toProductCardModel(hasThreeDots = true))

            setImageProductViewHintListener(element.productItem, object : ViewHintListener {
                override fun onViewHint() {
                    if (element.productItem.isTopAds) {
                        TopAdsUrlHitter(itemView.context).hitImpressionUrl(
                            this.javaClass.simpleName,
                            element.productItem.trackerImageUrl,
                            element.productItem.productId.toString(),
                            element.productItem.name,
                            element.productItem.imageUrl,
                            RECOM_ITEM
                        )
                    }
                    listener.onProductImpression(element.productItem)
                }
            })
            setVisibilityPercentListener(element.productItem.isTopAds, object : ProductConstraintLayout.OnVisibilityPercentChanged {
                override fun onShow() {
                    element.productItem.sendShowAdsByteIo(itemView.context)
                }

                override fun onShowOver(maxPercentage: Int) {
                    element.productItem.sendShowOverAdsByteIo(itemView.context, maxPercentage)
                }
            })
            setOnClickListener(object : ProductCardClickListener {
                override fun onClick(v: View) {
                    listener.onProductClick(element.productItem, element.productItem.type, adapterPosition)
                    if (element.productItem.isTopAds) TopAdsUrlHitter(itemView.context).hitClickUrl(
                        this.javaClass.simpleName,
                        element.productItem.clickUrl,
                        element.productItem.productId.toString(),
                        element.productItem.name,
                        element.productItem.imageUrl,
                        RECOM_ITEM
                    )
                    AppLogRecommendation.sendProductClickAppLog(
                        element.productItem.asProductTrackModel(
                            entranceForm = EntranceForm.PURE_GOODS_CARD,
                        )
                    )
                }

                override fun onAreaClicked(v: View) {
                    element.productItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(v: View) {
                    element.productItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(v: View) {
                    element.productItem.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
                }
            })

            setThreeDotsOnClickListener {
                listener.onThreeDotsClick(element.productItem, adapterPosition)
            }

            setAddToCartNonVariantClickListener(object : ATCNonVariantListener {
                override fun onQuantityChanged(quantity: Int) {
                    element.productItem.onCardQuantityChanged(quantity)
                    tokonowListener?.onProductTokonowNonVariantQuantityChanged(
                        recomItem = element.productItem,
                        adapterPosition = adapterPosition,
                        quantity = quantity
                    )
                }
            })

            setAddVariantClickListener {
                tokonowListener?.onProductTokonowVariantClicked(
                    recomItem = element.productItem,
                    adapterPosition = adapterPosition
                )
            }

            addOnImpression1pxListener(element.productItem.appLogImpressHolder) {
                AppLogRecommendation.sendProductShowAppLog(
                    element.productItem.asProductTrackModel(
                        entranceForm = EntranceForm.PURE_GOODS_CARD,
                    )
                )
            }
        }
    }

    private fun setRecTriggerObject(model: RecommendationItemDataModel) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.productItem.appLog.sessionId,
            requestId = model.productItem.appLog.requestId,
            moduleName = model.productItem.pageName,
        )
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject? {
        return recTriggerObject
    }
}
