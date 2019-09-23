package com.tokopedia.imagesearch.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.request.animation.GlideAnimation
import com.bumptech.glide.request.target.SimpleTarget
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.imagesearch.R

class BadgeHelper {
    companion object {
        @JvmStatic
        fun loadShopBadgesIcon(url: String, linearLayoutShopBadges: LinearLayout, context: Context) {
            if (!TextUtils.isEmpty(url)) {
                val view = LayoutInflater.from(context).inflate(R.layout.image_search_product_item_badge_layout, null)
                ImageHandler.loadImageBitmap2(context, url, object : SimpleTarget<Bitmap>() {
                    override fun onResourceReady(bitmap: Bitmap, glideAnimation: GlideAnimation<in Bitmap>) {
                        loadShopBadgeSuccess(view, linearLayoutShopBadges, bitmap)
                    }

                    override fun onLoadFailed(e: Exception?, errorDrawable: Drawable?) {
                        loadShopBadgeFailed(view)
                    }
                })
            }
        }

        private fun loadShopBadgeSuccess(view: View, linearLayoutShopBadges: LinearLayout, bitmap: Bitmap) {
            val image = view.findViewById<ImageView>(com.tokopedia.topads.sdk.R.id.badge)

            if (bitmap.height <= 1 && bitmap.width <= 1) {
                view.visibility = View.GONE
            } else {
                image.setImageBitmap(bitmap)
                view.visibility = View.VISIBLE
                linearLayoutShopBadges?.addView(view)
            }
        }

        private fun loadShopBadgeFailed(view: View) {
            view.visibility = View.GONE
        }
    }
}
