package com.tokopedia.inbox.universalinbox.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.analytics.byteio.PageName
import com.tokopedia.analytics.byteio.topads.AdsLogConst
import com.tokopedia.inbox.R
import com.tokopedia.inbox.databinding.UniversalInboxRecommendationWidgetItemBinding
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxRecommendationWidgetUiModel
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.recommendation_widget_common.byteio.sendRealtimeClickAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowAdsByteIo
import com.tokopedia.recommendation_widget_common.byteio.sendShowOverAdsByteIo
import com.tokopedia.recommendation_widget_common.presentation.model.RecommendationItem
import com.tokopedia.recommendation_widget_common.widget.global.RecommendationWidgetListener
import com.tokopedia.utils.view.binding.viewBinding

class UniversalInboxRecommendationWidgetViewHolder(
    itemView: View
) : AbstractViewHolder<UniversalInboxRecommendationWidgetUiModel>(itemView) {

    private var binding: UniversalInboxRecommendationWidgetItemBinding? by viewBinding()

    override fun bind(uiModel: UniversalInboxRecommendationWidgetUiModel) {
        val recommendationWidgetModel = uiModel.recommendationWidgetModel.copy(
            listener = object: RecommendationWidgetListener {
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
        binding?.inboxRecommendationWidget?.bind(recommendationWidgetModel)
    }

    override fun onViewRecycled() {
        super.onViewRecycled()
        binding?.inboxRecommendationWidget?.recycle()
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.universal_inbox_recommendation_widget_item
    }
}
