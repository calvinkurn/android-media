package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.PostUiModel
import kotlinx.android.synthetic.main.shc_item_post.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostViewHolder(view: View?) : AbstractViewHolder<PostUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_post
    }

    override fun bind(element: PostUiModel) {
        with(itemView) {
            try {
                parentViewShcItemPost.background = context.getResDrawable(R.drawable.shc_bg_ripple_radius_4dp)
            } catch (e: Exception) {
                Timber.e(e)
            }

            tvPostTitle.text = element.title.parseAsHtml()
            tvPostDescription.text = element.subtitle.parseAsHtml()
            loadImage(element.featuredMediaURL)
        }
    }

    private fun loadImage(featuredMediaURL: String) = with(itemView) {
        if (featuredMediaURL.isNotEmpty()) {
            ImageHandler.loadImageRounded(context, imgPost, featuredMediaURL, context.dpToPx(8))
        } else {
            ImageHandler.loadImageRounded2(context, imgPost, com.tokopedia.abstraction.R.drawable.error_drawable, context.dpToPx(8))
        }
    }
}