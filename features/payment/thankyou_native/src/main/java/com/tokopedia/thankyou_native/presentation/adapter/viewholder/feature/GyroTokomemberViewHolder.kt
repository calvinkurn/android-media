package com.tokopedia.thankyou_native.presentation.adapter.viewholder.feature

import android.text.Html
import android.view.View
import android.widget.ImageView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.thankyou_native.R
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.CLOSE_MEMBERSHIP
import com.tokopedia.thankyou_native.analytics.GyroTrackingKeys.OPEN_MEMBERSHIP
import com.tokopedia.thankyou_native.presentation.adapter.GyroAdapterListener
import com.tokopedia.thankyou_native.presentation.adapter.model.GyroTokomemberItem
import kotlinx.android.synthetic.main.thank_item_feature_tokomember.view.*
import kotlinx.android.synthetic.main.thank_item_feature_tokomember.view.tvFeatureItemIconToko
import kotlinx.android.synthetic.main.thank_item_tokomember_error.view.*

class GyroTokomemberViewHolder(val view: View, val listener: GyroAdapterListener) :
    AbstractViewHolder<GyroTokomemberItem>(view) {

    private val ivFeatureItem = itemView.ivFeatureItemToko
    private val tvFeatureItemTitle = itemView.tvFeatureItemTitleToko
    private val tvFeatureItemDescription = itemView.tvFeatureItemDescriptionToko
    private val viewFlipperItemContainer = itemView.containerViewFlipper
    private val iconStar = itemView.tvFeatureItemIconToko
    private val iconError = itemView.tvFeatureItemIconTokoError

    override fun bind(element: GyroTokomemberItem?) {
        showShimmer()
        element?.apply {
            if (element.failRegister) {
                showError()
                iconError?.setOnClickListener {
                    if (!urlApp.isNullOrBlank()) {
                        listener.openAppLink(urlApp)
                    } else if (!url.isNullOrBlank()) {
                        listener.openWebUrl(url)
                    }
                }
            } else {
                showItem()
                itemView.tag = element
                ivFeatureItem.scaleType = ImageView.ScaleType.CENTER_INSIDE

                imageURL?.let {
                    ivFeatureItem.visible()
                    ivFeatureItem.setImageUrl(imageURL)
                } ?: run { ivFeatureItem.gone() }

                tvFeatureItemTitle.text = Html.fromHtml(title ?: "")
                tvFeatureItemDescription.text = Html.fromHtml(description ?: "")

                if (this.successRegister && membershipType == OPEN_MEMBERSHIP) {
                    iconStar.hide()
                } else if (membershipType == CLOSE_MEMBERSHIP) {
                    iconStar.hide()
                } else {
                    iconStar.show()
                }

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
        }

    private fun showShimmer() {
        viewFlipperItemContainer.displayedChild = SHIMMER
    }

    private fun showItem() {
        viewFlipperItemContainer.displayedChild = DATA
    }

    private fun showError() {
        viewFlipperItemContainer.displayedChild = ERROR
    }

    companion object {
        val LAYOUT_ID = R.layout.thank_item_feature_tokomember
        const val SHIMMER = 0
        const val DATA = 1
        const val ERROR = 2
    }
}
