package com.tokopedia.product.detail.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.AppLogRecTriggerInterface
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.RecommendationTriggerObject
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ComponentTrackDataModel
import com.tokopedia.product.detail.data.model.datamodel.PdpRecommendationWidgetDataModel
import com.tokopedia.product.detail.databinding.ItemRecomViewHolderBinding
import com.tokopedia.product.detail.view.listener.ProductDetailListener
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Created by frenzel on 27/03/23
 */
class PdpRecommendationWidgetViewHolder(
    view: View,
    private val listener: ProductDetailListener
) : AbstractViewHolder<PdpRecommendationWidgetDataModel>(view), AppLogRecTriggerInterface {

    companion object {
        val LAYOUT = R.layout.item_recom_view_holder
    }

    private val binding: ItemRecomViewHolderBinding? by viewBinding()

    override fun bind(element: PdpRecommendationWidgetDataModel) {

        val recommendationWidgetModel = element.recommendationWidgetModel.copy(
            listener = object : RecommendationWidgetListener {
                override fun onAreaClicked(position: Int, item: RecommendationItem) {
                    item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.AREA)
                }

                override fun onProductImageClicked(position: Int, item: RecommendationItem) {
                    item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.COVER)
                }

                override fun onSellerInfoClicked(position: Int, item: RecommendationItem) {
                    item.sendRealtimeClickAdsByteIo(itemView.context, AdsLogConst.Refer.SELLER_NAME)
                }

                override fun onViewAttachedToWindow(position: Int, item: RecommendationItem) {
                    item.sendShowAdsByteIo(itemView.context)
                }

                override fun onViewDetachedFromWindow(position: Int, item: RecommendationItem, visiblePercentage: Int) {
                    item.sendShowOverAdsByteIo(itemView.context, visiblePercentage)
                }
            }
        )

        binding?.recomWidget?.bind(recommendationWidgetModel)
        binding?.root?.addOnImpressionListener(element.impressHolder) {
            listener.onImpressComponent(
                ComponentTrackDataModel(
                    componentType = element.type(),
                    componentName = element.name(),
                    adapterPosition = bindingAdapterPosition
                )
            )
        }
    }

    override fun bind(element: PdpRecommendationWidgetDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.recomWidget?.recycle()
    }

    override fun getRecommendationTriggerObject(): RecommendationTriggerObject? {
        return binding?.recomWidget?.getRecommendationTriggerObject()
    }

    override fun isEligibleToTrack(): Boolean {
        return binding?.recomWidget?.isEligibleToTrack().orFalse()
    }
}
