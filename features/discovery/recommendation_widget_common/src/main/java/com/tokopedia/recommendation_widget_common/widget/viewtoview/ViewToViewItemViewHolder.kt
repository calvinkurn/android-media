package com.tokopedia.recommendation_widget_common.widget.viewtoview

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.media.loader.loadImage
import com.tokopedia.recommendation_widget_common.R
import com.tokopedia.recommendation_widget_common.databinding.ItemRecomViewToViewBinding
import com.tokopedia.unifycomponents.CardUnify2
import com.tokopedia.utils.view.binding.viewBinding

sealed class ViewToViewItemViewHolder(
    view: View,
    protected val listener: ViewToViewItemListener,
) : AbstractViewHolder<ViewToViewItemData>(view) {

    class Big(
        view: View,
        listener: ViewToViewItemListener,
    ) : ViewToViewItemViewHolder(view, listener) {

        private var binding: ItemRecomViewToViewBinding? by viewBinding()

        init {
            binding?.root?.animateOnPress = CardUnify2.ANIMATE_OVERLAY
        }

        override fun bind(element: ViewToViewItemData?) {
            val element = element ?: return
            val binding = binding ?: return
            with(binding.card) {
                imageItem.loadImage(element.imageUrl)
                tgPrice.text = element.price
                tgName.text = element.name
            }
            setUpListener(element)
        }

        private fun setUpListener(element: ViewToViewItemData) {
            itemView.addOnImpressionListener(element.recommendationData) {
                listener.onViewToViewItemImpressed(element, bindingAdapterPosition)
            }
            val binding = binding ?: return
            binding.root.setOnClickListener {
                listener.onViewToViewItemClicked(element, bindingAdapterPosition)
            }
        }
        companion object {
            val LAYOUT = R.layout.item_recom_view_to_view_big
        }
    }

    class Regular(
        view: View,
        listener: ViewToViewItemListener,
    ) : ViewToViewItemViewHolder(view, listener) {

        private var binding: ItemRecomViewToViewBinding? by viewBinding()

        init {
            binding?.root?.animateOnPress = CardUnify2.ANIMATE_OVERLAY
        }


        override fun bind(element: ViewToViewItemData?) {
            val element = element ?: return
            val binding = binding ?: return
            with(binding.card) {
                imageItem.loadImage(element.imageUrl)
                tgPrice.text = element.price
                tgName.text = element.name
            }
            setUpListener(element)
        }

        private fun setUpListener(element: ViewToViewItemData) {
            val binding = binding ?: return
            binding.root.setOnClickListener {
                listener.onViewToViewItemClicked(element, bindingAdapterPosition)
            }
        }

        companion object {
            val LAYOUT = R.layout.item_recom_view_to_view
        }
    }
}
