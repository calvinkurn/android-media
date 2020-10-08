package com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature

import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.presentation.adapter.FeatureListingAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.model.FeatureListItem
import kotlinx.android.synthetic.main.thank_item_feature_list.view.*

class FeatureListViewModel(val view: View, val listener : FeatureListingAdapterListener)
    : AbstractViewHolder<FeatureListItem>(view) {

    private val ivFeatureItem = itemView.ivFeatureItem
    private val tvFeatureItemTitle = itemView.tvFeatureItemTitle
    private val tvFeatureItemDescription = itemView.tvFeatureItemDescription

    override fun bind(element: FeatureListItem?) {
        element?.apply {
            ivFeatureItem.scaleType =  ImageView.ScaleType.CENTER_INSIDE
            ivFeatureItem.setImageUrl(image)
            tvFeatureItemTitle.text = title
            tvFeatureItemDescription.text = description
            view.setOnClickListener {
                urlApp?.let {
                    listener.openAppLink(urlApp)
                }?: run {
                    url?.let {
                        listener.openWebUrl(url)
                    }
                }
            }
        }
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_item_feature_list
    }
}
