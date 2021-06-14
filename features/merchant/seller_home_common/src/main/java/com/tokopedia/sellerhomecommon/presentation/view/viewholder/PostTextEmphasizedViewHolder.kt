package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import kotlinx.android.synthetic.main.shc_item_post_text_emphasized.view.*
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostTextEmphasizedViewHolder(view: View?) : AbstractViewHolder<PostItemUiModel.PostTextEmphasizedUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_post_text_emphasized
    }

    override fun bind(element: PostItemUiModel.PostTextEmphasizedUiModel) {
        with(itemView) {
            try {
                parentViewShcItemPostTextEmphasized.background = context.getResDrawable(R.drawable.shc_bg_ripple_radius_4dp)
            } catch (e: Exception) {
                Timber.e(e)
            }

            tvShcPostTextTitle.text = element.title.parseAsHtml()
            tvShcPostTextDescription.text = element.subtitle.parseAsHtml()
            tvShcPostState.text = element.stateText
            imgShcPostMedia.loadImage(element.featuredMediaUrl) {
                setErrorDrawable(com.tokopedia.abstraction.R.drawable.error_drawable)
            }
            imgShcPostState.loadImage(element.stateMediaUrl)
            lineShcItemPost.isVisible = element.shouldShowUnderLine
        }
    }
}