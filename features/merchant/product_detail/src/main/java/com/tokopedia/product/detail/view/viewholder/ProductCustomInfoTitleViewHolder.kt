package com.tokopedia.product.detail.view.viewholder

import android.view.View
import android.view.ViewGroup.LayoutParams
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.setLayoutHeight
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.util.lazyThreadSafetyNone
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.datamodel.ProductCustomInfoTitleDataModel
import com.tokopedia.product.detail.databinding.ItemDynamicCustomInfoTitleBinding

/**
 * Created by yovi.putra on 24/11/22"
 * Project name: android-tokopedia-core
 **/


class ProductCustomInfoTitleViewHolder(
    private val view: View
) : ProductDetailPageViewHolder<ProductCustomInfoTitleDataModel>(view) {

    companion object {

        val LAYOUT = R.layout.item_dynamic_custom_info_title
    }

    private val binding by lazyThreadSafetyNone { ItemDynamicCustomInfoTitleBinding.bind(view) }

    override fun bind(element: ProductCustomInfoTitleDataModel) {
        when (element.status) {
            ProductCustomInfoTitleDataModel.Status.PLACEHOLDER -> showLoading()
            ProductCustomInfoTitleDataModel.Status.SHOW -> showContent(element)
            else -> hideContent()
        }
    }

    private fun showLoading() = with(binding) {
        root.setLayoutHeight(LayoutParams.WRAP_CONTENT)
        title.gone()
        shimmerTitle.show()
    }

    private fun hideLoading() = with(binding) {
        root.setLayoutHeight(LayoutParams.WRAP_CONTENT)
        shimmerTitle.gone()
        title.show()
    }

    private fun showContent(element: ProductCustomInfoTitleDataModel) = with(binding) {
        title.text = element.title
        hideLoading()
    }

    private fun hideContent() {
        binding.root.setLayoutHeight(0)
    }
}
