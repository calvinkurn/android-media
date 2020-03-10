package com.tokopedia.centralizedpromo.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralizedpromo.view.model.PostUiModel
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.R.layout.sah_item_centralized_promo_post
import kotlinx.android.synthetic.main.sah_item_centralized_promo_post.view.*

class PostViewHolder(view: View?) : AbstractViewHolder<PostUiModel>(view) {

    companion object {
        val RES_LAYOUT = sah_item_centralized_promo_post
    }

    override fun bind(element: PostUiModel) {
        with(itemView) {
            tvPromotionPostTitle.text = element.title.parseAsHtml()
            tvPromotionPostTitleDescription.text = element.subtitle.parseAsHtml()
            setOnClickListener { openAppLink(element.applink) }
            loadImage(element.featuredMediaUrl)
        }
    }

    private fun loadImage(featuredMediaURL: String) = with(itemView) {
        if (featuredMediaURL.isNotBlank()) {
            ImageHandler.loadImageRounded(context, ivPromotionPost, featuredMediaURL, context.dpToPx(8))
        } else {
            ImageHandler.loadImageRounded2(context, ivPromotionPost, R.drawable.error_drawable, context.dpToPx(8))
        }
    }

    private fun openAppLink(url: String) {
        with(itemView) {
            RouteManager.route(context, url)
        }
    }
}