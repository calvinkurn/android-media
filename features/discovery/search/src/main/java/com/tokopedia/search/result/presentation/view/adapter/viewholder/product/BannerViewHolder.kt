package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.result.presentation.model.BannerDataView
import com.tokopedia.search.result.presentation.view.listener.BannerListener
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class BannerViewHolder(itemView: View, private val bannerListener: BannerListener): AbstractViewHolder<BannerDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_banner_layout
    }

    private var bannerText: Typography? = null
    private var bannerImage: ImageUnify? = null

    init {
        bannerText = itemView.findViewById(R.id.searchProductBannerText)
        bannerImage = itemView.findViewById(R.id.searchProductBannerImage)
    }

    override fun bind(element: BannerDataView?) {
        element ?: return

        bindBannerText(element)
        bindBannerImage(element)
    }

    private fun bindBannerText(element: BannerDataView) {
        bannerText?.shouldShowWithAction(element.text.isNotEmpty()) {
            bannerText?.text = element.text
        }
    }

    private fun bindBannerImage(element: BannerDataView) {
        bannerImage?.shouldShowWithAction(element.imageUrl.isNotEmpty()) {
            bannerImage?.setImageUrl(element.imageUrl)

            if (element.applink.isNotEmpty()) {
                bannerImage?.setOnClickListener {
                    bannerListener.onBannerClicked(element)
                }
            } else {
                bannerImage?.setOnClickListener(null)
            }
        }
    }
}