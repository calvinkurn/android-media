package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.dpToPx
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemPostImageEmphasizedBinding
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.presentation.view.adapter.PostListPagerAdapter
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import timber.log.Timber

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostImageEmphasizedViewHolder(
    view: View?,
    private val listener: PostListPagerAdapter.Listener,
    private val isCheckingMode: Boolean
) : AbstractViewHolder<PostItemUiModel.PostImageEmphasizedUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_post_image_emphasized
        private const val DP_12 = 12
    }

    private val binding by lazy {
        ShcItemPostImageEmphasizedBinding.bind(itemView)
    }

    override fun bind(element: PostItemUiModel.PostImageEmphasizedUiModel) {
        with(binding) {
            try {
                parentViewShcItemPost.background = root.context.getResDrawable(
                    R.drawable.shc_bg_ripple_radius_4dp
                )
            } catch (e: Exception) {
                Timber.e(e)
            }

            tvPostTitle.text = element.title.parseAsHtml()
            tvPostDescription.text = element.subtitle.parseAsHtml()
            imgPost.loadImage(element.featuredMediaUrl) {
                setErrorDrawable(com.tokopedia.abstraction.R.drawable.error_drawable)
            }
            cbShcItemPostImageEmphasize.isVisible = isCheckingMode
            cbShcItemPostImageEmphasize.isChecked = element.isChecked
            cbShcItemPostImageEmphasize.setOnCheckedChangeListener { _, isChecked ->
                element.isChecked = isChecked
                listener.onCheckedListener(isChecked)
            }
            if (element.isPinned) {
                tvPostDescription.setUnifyDrawableEnd(
                    iconId = IconUnify.PUSH_PIN_FILLED,
                    colorIcon = root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN500),
                    width = root.context.dpToPx(DP_12),
                    height = root.context.dpToPx(DP_12)
                )
            } else {
                tvPostDescription.clearUnifyDrawableEnd()
            }

            root.setOnClickListener {
                if (isCheckingMode) {
                    cbShcItemPostImageEmphasize.isChecked = !cbShcItemPostImageEmphasize.isChecked
                } else {
                    listener.onItemClicked(element)
                }
            }
        }
    }
}