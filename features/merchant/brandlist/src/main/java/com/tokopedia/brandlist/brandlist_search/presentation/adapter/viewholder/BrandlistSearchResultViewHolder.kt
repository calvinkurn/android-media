package com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_search.presentation.adapter.viewmodel.BrandlistSearchResultViewModel
import kotlinx.android.synthetic.main.item_search_result.view.*


class BrandlistSearchResultViewHolder(view: View): AbstractViewHolder<BrandlistSearchResultViewModel>(view) {

    companion object {
        val LAYOUT = R.layout.item_search_result
    }

    private val context: Context = itemView.context
    private val imgBrandLogo = itemView.iv_brand_logo
    private val imgBrandImage = itemView.iv_brand_image
    private val txtBrandName = itemView.tv_brand_name

    override fun bind(element: BrandlistSearchResultViewModel) {
        bindData(element.name, element.defaultUrl, element.logoUrl)
    }

    private fun bindData(name: String, brandLogoUrl: String, brandImageUrl: String) {
        txtBrandName.text = name
        ImageHandler.loadImage(context, imgBrandLogo, brandLogoUrl, null)
        if(brandImageUrl.isNotBlank()) {
            ImageHandler.loadImage(context, imgBrandImage, brandImageUrl, null)
        } else {
            imgBrandImage.visibility = View.GONE
        }
    }
}