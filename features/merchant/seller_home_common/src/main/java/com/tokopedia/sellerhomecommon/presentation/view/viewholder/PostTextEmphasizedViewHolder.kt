package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemPostTextEmphasizedBinding
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostTextEmphasizedViewHolder(
    view: View?
) : AbstractViewHolder<PostItemUiModel.PostTextEmphasizedUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_post_text_emphasized
    }

    private val binding by lazy {
        ShcItemPostTextEmphasizedBinding.bind(itemView)
    }

    override fun bind(element: PostItemUiModel.PostTextEmphasizedUiModel) {
        with(binding) {
            try {
                parentViewShcItemPostTextEmphasized.background = root.context.getResDrawable(
                    R.drawable.shc_bg_ripple_radius_4dp
                )
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
            if (element.isPinned) {
                tvShcPostTextDescription.setUnifyDrawableEnd(
                    iconId = IconUnify.PUSH_PIN_FILLED,
                    colorIcon = root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN500),
                    width = root.context.dpToPx(12),
                    height = root.context.dpToPx(12)
                )
            } else {
                tvShcPostTextDescription.clearUnifyDrawableEnd()
            }
        }
    }
}