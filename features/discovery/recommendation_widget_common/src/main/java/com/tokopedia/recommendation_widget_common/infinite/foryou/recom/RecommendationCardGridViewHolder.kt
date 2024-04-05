package com.tokopedia.recommendation_widget_common.infinite.foryou.recom

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.analytics.byteio.topads.AppLogTopAds
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogRealtimeClickModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowModel
import com.tokopedia.recommendation_widget_common.byteio.TrackRecommendationMapper.asAdsLogShowOverModel
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ParentRecommendationListener

class RecommendationCardGridViewHolder constructor(
    view: View,
    private val listener: ParentRecommendationListener
) : BaseRecommendationViewHolder<RecommendationCardModel>(
    view,
    RecommendationCardModel::class.java
), AppLogRecTriggerInterface {

    private val productCardView by lazy { itemView.findViewById<ProductCardGridView>(R.id.productCardView) }

    private var recTriggerObject = RecommendationTriggerObject()

    override fun bind(element: RecommendationCardModel) {
        setRecTriggerObject(element)
        setLayout(element)
        productCardImpressionListener(element)
        setItemProductCardClickListener(element)
        setItemThreeDotsClickListener(element)
    }

    override fun bindPayload(newItem: RecommendationCardModel?) {
        newItem?.let {
            setItemThreeDotsClickListener(it)
        }
    }

    override fun onViewAttachedToWindow(element: RecommendationCardModel?) {
        element?.let {
            if (it.recommendationProductItem.isTopAds) {
                AppLogTopAds.sendEventShow(
                    itemView.context,
                    PageName.HOME,
                    it.recommendationProductItem.asAdsLogShowModel()
                )
            }
        }
    }

    override fun onViewDetachedFromWindow(element: RecommendationCardModel?, visiblePercentage: Int) {
        element?.let {
            if (it.recommendationProductItem.isTopAds) {
                AppLogTopAds.sendEventShowOver(
                    itemView.context,
                    PageName.HOME,
                    it.recommendationProductItem.asAdsLogShowOverModel(visibilityPercentage)
                )
            }
        }
    }

    private fun setLayout(element: RecommendationCardModel) {
        productCardView?.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener(element: RecommendationCardModel) {
        productCardView?.setImageProductViewHintListener(
            element,
            object : ViewHintListener {
                override fun onViewHint() {
                    listener.onProductCardImpressed(
                        element,
                        bindingAdapterPosition
                    )
                }
            }
        )
    }

    private fun setItemProductCardClickListener(element: RecommendationCardModel) {
        productCardView?.setOnClickListener(object : ProductCardClickListener{
            override fun onClick(v: View) {
                listener.onProductCardClicked(
                    element,
                    bindingAdapterPosition
                )
            }

            override fun onAreaClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationProductItem, AdsLogConst.Refer.AREA)
            }

            override fun onProductImageClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationProductItem, AdsLogConst.Refer.COVER)
            }

            override fun onSellerInfoClicked(v: View) {
                sendEventRealtimeClickAdsByteIo(itemView.context, element.recommendationProductItem, AdsLogConst.Refer.SELLER_NAME)
            }
        })
    }

    internal fun sendEventRealtimeClickAdsByteIo(context: Context, element: RecommendationCardModel.ProductItem?, refer: String) {
        element?.let {
            if (it.isTopAds) {
                AppLogTopAds.sendEventRealtimeClick(
                    context,
                    PageName.HOME,
                    it.asAdsLogRealtimeClickModel(refer)
                )
            }
        }
    }

    private fun setItemThreeDotsClickListener(productCardItem: RecommendationCardModel) {
        productCardView?.setThreeDotsOnClickListener {
            listener.onProductCardThreeDotsClicked(
                productCardItem,
                bindingAdapterPosition
            )
        }
    }

    private fun setRecTriggerObject(model: RecommendationCardModel) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName,
            listName = model.tabName,
            listNum = model.tabIndex,
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_recom_grid
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }
}
