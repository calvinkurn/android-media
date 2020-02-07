package com.tokopedia.sellerhome.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.util.parseAsHtml
import com.tokopedia.sellerhome.view.model.PostUiModel
import kotlinx.android.synthetic.main.sah_item_post.view.*

class PostViewHolder(view: View?) : AbstractViewHolder<PostUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.sah_item_post
    }

    override fun bind(element: PostUiModel) {
        with(element) {
            itemView.tv_post_title.text = title.parseAsHtml()
            itemView.tv_post_description.text = subtitle.parseAsHtml()
            loadImage(featuredMediaURL)
        }
    }

    private fun loadImage(featuredMediaURL: String) = with(itemView) {
        if (featuredMediaURL.isNotEmpty()) {
            ImageHandler.loadImageRounded(context, iv_post, featuredMediaURL, 20f)
        } else {
            ImageHandler.loadImageRounded2(context, iv_post, R.drawable.error_drawable, 20f)
        }
    }
}