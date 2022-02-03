package com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.GyroAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroTokomemberItem
import kotlinx.android.synthetic.main.thank_item_feature_tokomember.view.*

class GyroTokomemberViewHolder(val view: View, val listener: GyroAdapterListener)
    : AbstractViewHolder<GyroTokomemberItem>(view) {

    private val ivFeatureItem = itemView.ivFeatureItemToko
    private val tvFeatureItemTitle = itemView.tvFeatureItemTitleToko
    private val tvFeatureItemDescription = itemView.tvFeatureItemDescriptionToko
    private val viewFlipperItemContainer = itemView.containerViewFlipper

    override fun bind(element: GyroTokomemberItem?) {
        showShimmer()
        element?.apply {
            showItem()
            itemView.tag = element
            ivFeatureItem.scaleType = ImageView.ScaleType.CENTER_INSIDE

            imageURL?.let {
                ivFeatureItem.visible()
                ivFeatureItem.setImageUrl(imageURL)
            } ?: run { ivFeatureItem.gone() }

            tvFeatureItemTitle.text = title ?: ""
            tvFeatureItemDescription.text = description ?: ""

            view.setOnClickListener {
                if (element.isOpenBottomSheet) {
                    listener.onItemClicked(element, adapterPosition)
                } else {
                    if (!urlApp.isNullOrBlank()) {
                        listener.openAppLink(urlApp)
                    } else if (!url.isNullOrBlank()) {
                        listener.openWebUrl(url)
                    }
                }
            }
        }
    }

    private fun showShimmer() {
        viewFlipperItemContainer.displayedChild = SHIMMER
    }

    private fun showItem() {
        viewFlipperItemContainer.displayedChild = DATA
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_item_feature_tokomember
        const val SHIMMER = 0
        const val DATA = 1
        const val ERROR = 2
    }
}
