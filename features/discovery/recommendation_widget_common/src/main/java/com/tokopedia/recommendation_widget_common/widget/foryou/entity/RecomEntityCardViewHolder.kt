package com.tokopedia.recommendation_widget_common.widget.foryou.entity

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.RecommendationWidgetEntityCardBinding
import com.tokopedia.recommendation_widget_common.widget.foryou.BaseForYouViewHolder
import com.tokopedia.utils.view.binding.viewBinding

class RecomEntityCardViewHolder constructor(
    view: View,
    private val listener: Listener
) : BaseForYouViewHolder<RecomEntityModel>(
    view,
    RecomEntityModel::class.java
) {

    private val binding: RecommendationWidgetEntityCardBinding? by viewBinding()

    override fun bind(element: RecomEntityModel) {
        binding?.view?.setListener(object : RecomEntityCardView.Listener {
            override fun onEntityCardClickListener(item: RecomEntityModel) {
                listener.onEntityCardClickListener(item, bindingAdapterPosition)
            }

            override fun onEntityCardImpressionListener(item: RecomEntityModel) {
                listener.onEntityCardImpressionListener(item, bindingAdapterPosition)
            }
        })

        binding?.view?.setupView(element)
    }

    interface Listener {
        fun onEntityCardImpressionListener(item: RecomEntityModel, position: Int)
        fun onEntityCardClickListener(item: RecomEntityModel, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.recommendation_widget_entity_card
    }
}
