package com.tokopedia.sellerhomecommon.presentation.view.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.kotlin.extensions.view.getResDrawable
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcItemPostImageEmphasizedBinding
import com.tokopedia.sellerhomecommon.presentation.model.PostItemUiModel
import com.tokopedia.sellerhomecommon.utils.clearUnifyDrawableEnd
import com.tokopedia.sellerhomecommon.utils.setUnifyDrawableEnd
import com.tokopedia.unifycomponents.timer.TimerUnifySingle
import timber.log.Timber
import java.util.*

/**
 * Created By @ilhamsuaib on 20/05/20
 */

class PostImageEmphasizedViewHolder(
    view: View?
) : AbstractViewHolder<PostItemUiModel.PostImageEmphasizedUiModel>(view) {

    companion object {
        val RES_LAYOUT = R.layout.shc_item_post_image_emphasized
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

            setupPin(element.isPinned)
            setupTimer(element)
        }
    }

    private fun setupPin(isPinned: Boolean) {
        with(binding) {
            if (isPinned) {
                val dp12 = root.context.resources.getDimension(
                    com.tokopedia.unifyprinciples.R.dimen.unify_space_12
                )
                tvPostDescription.setUnifyDrawableEnd(
                    iconId = IconUnify.PUSH_PIN_FILLED,
                    colorIcon = root.context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_NN500),
                    width = dp12,
                    height = dp12
                )
            } else {
                tvPostDescription.clearUnifyDrawableEnd()
            }
        }
    }

    private fun setupTimer(element: PostItemUiModel.PostImageEmphasizedUiModel) {
        binding.run {
            val deadLine = element.countdownDate
            if (deadLine != null && deadLine.time >= Date().time) {
                val timerBackground = root.context.getResColor(
                    com.tokopedia.unifyprinciples.R.color.Unify_T100
                )
                timerShcPostItemTimer.visible()
                timerShcPostItemTimer.setBackgroundColor(timerBackground)
                timerShcPostItemTimer.timerVariant = TimerUnifySingle.VARIANT_INFORMATIVE
                timerShcPostItemTimer.timerFormat = TimerUnifySingle.FORMAT_DAY
                timerShcPostItemTimer.targetDate = Calendar.getInstance().apply {
                    time = element.countdownDate
                }
            } else {
                timerShcPostItemTimer.gone()
            }
        }
    }
}
