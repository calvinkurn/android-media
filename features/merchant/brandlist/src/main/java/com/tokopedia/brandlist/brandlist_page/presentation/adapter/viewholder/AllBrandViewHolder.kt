package com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewholder

import android.annotation.SuppressLint
import android.content.Context
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.brandlist.R
import com.tokopedia.brandlist.brandlist_page.data.model.Brand
import com.tokopedia.brandlist.brandlist_page.presentation.adapter.viewmodel.AllBrandUiModel
import com.tokopedia.brandlist.common.listener.BrandlistPageTrackingListener
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.visible

@SuppressLint("NewApi")
class AllBrandViewHolder(itemView: View?) : AbstractViewHolder<AllBrandUiModel>(itemView) {

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

        itemView?.setOnClickListener {

            val view = it
            val brandObj = it.getTag(R.id.brand)

            brandObj?.let {

                val brand = view.getTag(R.id.brand) as Brand
                val index = view.getTag(R.id.position) as Int
                val listener = view.getTag(R.id.listener) as BrandlistPageTrackingListener

                listener.clickBrand(
                        (brand.id).toString(),
                        index.toString(),
                        brand.name,
                        brand.exclusiveLogoURL)

                RouteManager.route(context, brand.appsUrl)
            }
        }
    }

    override fun bind(element: AllBrandUiModel?) {

        val index = element?.index
        val brand = element?.brand

        brand?.let {

            itemView.setTag(R.id.brand, brand)
            itemView.setTag(R.id.position, index)
            itemView.setTag(R.id.listener, element.listener)

            element.listener.impressionBrand(
                    (brand.id).toString(),
                    "",
                    brand.name,
                    brand.exclusiveLogoURL)
        }

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