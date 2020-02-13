package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandViewModel
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible

class AllBrandViewHolder(itemView: View?) : AbstractViewHolder<AllBrandViewModel>(itemView) {

    private var context: Context? = null
    private var brandNewBadgeView: AppCompatTextView? = null
    private var brandLogoView: ImageView? = null
    private var brandImageView: ImageView? = null
    private var brandNameView: AppCompatTextView? = null

    init {
        brandNewBadgeView = itemView?.findViewById(R.id.tv_new_badge)
        brandLogoView = itemView?.findViewById(R.id.iv_brand_logo)
        brandImageView = itemView?.findViewById(R.id.iv_brand_image)
        brandNameView = itemView?.findViewById(R.id.tv_brand_name)

        itemView?.context?.let {
            context = it
        }
    }

    override fun bind(element: AllBrandViewModel?) {

        val brand = element?.brand

        brandNewBadgeView?.let {
            if (brand != null) {
                val isNewBrand = brand.isNew
                when (isNewBrand) {
                    1 -> it.visible()
                    else -> it.hide()
                }
            }
        }

        brandLogoView?.let {
            if (brand != null) {
                loadImageToImageView(context, brand.logoUrl, it)
            }
        }
        brandImageView?.let {
            if (brand != null) {
                loadImageToImageView(context, brand.exclusiveLogoURL, it)
            }
        }
        brandNameView?.let {
            if (brand != null) {
                it.text = brand.name
            }
        }
    }

    private fun loadImageToImageView(context: Context?, imageUrl: String, brandView: ImageView) {
        context?.let {
            Glide.with(it)
                    .load(imageUrl)
                    .dontAnimate()
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.RESOURCE)
                    .into(brandView)
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.brandlist_all_brand_item
    }
}