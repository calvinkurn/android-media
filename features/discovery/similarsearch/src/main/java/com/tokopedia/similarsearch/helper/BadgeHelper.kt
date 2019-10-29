package com.tokopedia.similarsearch.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.similarsearch.R

class BadgeHelper {
    companion object {
        @JvmStatic
        fun loadShopBadgesIcon(url: String, linearLayoutShopBadges: LinearLayout, context: Context) {
            if (!TextUtils.isEmpty(url)) {
                val view = LayoutInflater.from(context).inflate(R.layout.similar_search_product_item_badge_layout, null)
                ImageHandler.loadImageBitmap2(context, url, object : CustomTarget<Bitmap>() {
                    override fun onLoadCleared(placeholder: Drawable?) {

                    }

                    override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                        loadShopBadgeSuccess(view, linearLayoutShopBadges, resource)
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        super.onLoadFailed(errorDrawable)
                        loadShopBadgeFailed(view)
                    }
                })
            }
        }

        private fun loadShopBadgeSuccess(view: View, linearLayoutShopBadges: LinearLayout, bitmap: Bitmap) {
            val image = view.findViewById<ImageView>(R.id.badge)

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
