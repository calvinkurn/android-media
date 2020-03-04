package com.tokopedia.centralized_promo.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.centralized_promo.view.model.PostUiModel
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhome.R.layout.sah_item_centralized_promo_post
import kotlinx.android.synthetic.main.sah_item_centralized_promo_post.view.*

class PostViewHolder(view: View?) : AbstractViewHolder<PostUiModel>(view) {

    companion object {
        val RES_LAYOUT = sah_item_centralized_promo_post
    }

    override fun bind(element: PostUiModel) {
        with(itemView) {
            ImageHandler.loadImageThumbs(context, ivPromotionPost, element.featuredMediaUrl)
            tvPromotionPostTitle.text = element.title.parseAsHtml()
            tvPromotionPostTitleDescription.text = element.subtitle.parseAsHtml()
            setOnClickListener { openApplink(element.applink) }
        }
    }

    private fun openApplink(url: String) {
        with(itemView) {
            RouteManager.route(context, url)
        }
    }
}