package com.tokopedia.recommendation_widget_common.infinite.foryou.recom

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.productcard.ProductCardClickListener
import com.tokopedia.productcard.ProductCardGridView
import com.tokopedia.recommendation_widget_common.R
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

            }

            override fun onProductImageClicked(v: View) {

            }

            override fun onSellerInfoClicked(v: View) {

            }
        })
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
