package com.tokopedia.search.result.presentation.view.adapter.viewholder.product

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.search.R
import com.tokopedia.search.databinding.SearchResultProductBannerLayoutBinding
import com.tokopedia.search.result.presentation.model.BannerDataView
import com.tokopedia.search.result.presentation.view.listener.BannerListener
import com.tokopedia.utils.view.binding.viewBinding

class BannerViewHolder(itemView: View, private val bannerListener: BannerListener): AbstractViewHolder<BannerDataView>(itemView) {

    companion object {
        @LayoutRes
        @JvmField
        val LAYOUT = R.layout.search_result_product_banner_layout
    }

    private var binding: SearchResultProductBannerLayoutBinding? by viewBinding()

    override fun bind(element: BannerDataView?) {
        element ?: return

        bindBannerText(element)
        bindBannerImage(element)
    }

    private fun bindBannerText(element: BannerDataView) {
        binding?.searchProductBannerText?.let {
            it.shouldShowWithAction(element.text.isNotEmpty()) {
                it.text = element.text
            }
        }
    }

    private fun bindBannerImage(element: BannerDataView) {
        binding?.searchProductBannerImage?.let {
            it.shouldShowWithAction(element.imageUrl.isNotEmpty()) {
                it.setImageUrl(element.imageUrl)

                if (element.applink.isNotEmpty()) {
                    it.setOnClickListener {
                        bannerListener.onBannerClicked(element)
                    }
                } else {
                    it.setOnClickListener(null)
                }
            }
        }
    }
}