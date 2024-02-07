package com.tokopedia.recommendation_widget_common.infinite.foryou.entity

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.WidgetForYouContentCardBinding
import com.tokopedia.recommendation_widget_common.infinite.foryou.BaseForYouViewHolder
import com.tokopedia.utils.view.binding.viewBinding

class ContentCardViewHolder constructor(
    view: View,
    private val listener: Listener
) : BaseForYouViewHolder<ContentCardModel>(
    view,
    ContentCardModel::class.java
) {

    private val binding: WidgetForYouContentCardBinding? by viewBinding()

    override fun bind(element: ContentCardModel) {
        binding?.view?.setListener(object : ContentCardView.Listener {
            override fun onContentCardClicked(item: ContentCardModel) {
                listener.onContentCardClicked(item, bindingAdapterPosition)
            }

            override fun onContentCardImpressed(item: ContentCardModel) {
                listener.onContentCardImpressed(item, bindingAdapterPosition)
            }
        })

        binding?.view?.setupView(element)
    }

    interface Listener {
        fun onContentCardImpressed(item: ContentCardModel, position: Int)
        fun onContentCardClicked(item: ContentCardModel, position: Int)
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.widget_for_you_content_card
    }
}
