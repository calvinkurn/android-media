package com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.GyroAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroRecommendationListItem
import kotlinx.android.synthetic.main.thank_item_feature_list.view.*

class GyroListItemViewHolder(val view: View, val listener: GyroAdapterListener)
    : AbstractViewHolder<GyroRecommendationListItem>(view) {

    private val ivFeatureItem = itemView.ivFeatureItem
    private val tvFeatureItemTitle = itemView.tvFeatureItemTitle
    private val tvFeatureItemDescription = itemView.tvFeatureItemDescription


    override fun bind(element: GyroRecommendationListItem?) {
        element?.apply {

            itemView.tag = element
            ivFeatureItem.scaleType = ImageView.ScaleType.CENTER_INSIDE

            image?.let {
                ivFeatureItem.visible()
                ivFeatureItem.setImageUrl(image)
            } ?: run { ivFeatureItem.gone() }

            tvFeatureItemTitle.text = title ?: ""
            tvFeatureItemDescription.text = description ?: ""

            view.setOnClickListener {
                listener.onItemClicked(element, adapterPosition)
                if (!urlApp.isNullOrBlank()) {
                    listener.openAppLink(urlApp)
                } else if (!url.isNullOrBlank()) {
                    listener.openWebUrl(url)
                }
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_item_feature_list
    }
}
