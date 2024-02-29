package com.tokopedia.recommendation_widget_common.infinite.foryou.recom

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.kotlin.extensions.view.ViewHintListener
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouRecomListBinding
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseRecommendationViewHolder
import com.tokopedia.recommendation_widget_common.infinite.foryou.ParentRecommendationListener
import com.tokopedia.utils.view.binding.viewBinding

class RecommendationCardListViewHolder constructor(
    view: View,
    private val listener: ParentRecommendationListener
) : BaseRecommendationViewHolder<RecommendationCardModel>(
    view,
    RecommendationCardModel::class.java
), AppLogRecTriggerInterface {

    private val binding: WidgetForYouRecomListBinding? by viewBinding()

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

    private fun setLayout(
        element: RecommendationCardModel
    ) {
        binding?.productCardView?.setProductModel(element.productCardModel)
    }

    private fun productCardImpressionListener(element: RecommendationCardModel) {
        binding?.productCardView?.setImageProductViewHintListener(
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
        binding?.productCardView?.setOnClickListener {
            listener.onProductCardClicked(
                element,
                bindingAdapterPosition
            )
        }
    }

    private fun setItemThreeDotsClickListener(element: RecommendationCardModel) {
        binding?.productCardView?.setThreeDotsOnClickListener {
            listener.onProductCardThreeDotsClicked(
                element,
                bindingAdapterPosition
            )
        }
    }

    private fun setRecTriggerObject(model: RecommendationCardModel) {
        recTriggerObject = RecommendationTriggerObject(
            sessionId = model.appLog.sessionId,
            requestId = model.appLog.requestId,
            moduleName = model.pageName,
        )
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_recom_list
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject {
        return recTriggerObject
    }
}
