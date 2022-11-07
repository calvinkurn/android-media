package com.tokopedia.review.feature.credibility.presentation.widget

import android.content.Context
import android.graphics.ColorFilter
import android.graphics.LightingColorFilter
import android.graphics.drawable.Drawable
import androidx.annotation.ColorInt
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.review.R
import com.tokopedia.review.databinding.PartialReviewCredibilityAchievementBinding
import com.tokopedia.review.feature.credibility.presentation.uimodel.ReviewCredibilityAchievementBoxUiModel
import com.tokopedia.reviewcommon.extension.toColorInt

class PartialReviewCredibilityAchievement(
    private val binding: PartialReviewCredibilityAchievementBinding,
    private var listener: ReviewCredibilityAchievementBoxWidget.Listener?
) {

    companion object {
        private val DEFAULT_BACKGROUND_COLOR = com.tokopedia.unifyprinciples.R.color.Unify_TN600
    }

    private fun createAchievementAvatarBorder(context: Context, @ColorInt color: Int): Drawable? {
        val drawable = MethodChecker.getDrawable(context, R.drawable.border_review_credibility_achievement_avatar)
        val filter: ColorFilter = LightingColorFilter(ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Static_Black), color)
        drawable.colorFilter = filter
        return drawable
    }

    private fun PartialReviewCredibilityAchievementBinding.hide() {
        root.invisible()
    }

    private fun PartialReviewCredibilityAchievementBinding.bindAchievementAvatarBorder(colorHex: String) {
        ivReviewCredibilityAchievementAvatarBorder.setImageDrawable(
            createAchievementAvatarBorder(
                context = binding.root.context,
                color = colorHex.toColorInt(DEFAULT_BACKGROUND_COLOR)
            )
        )
    }

    private fun PartialReviewCredibilityAchievementBinding.bindAchievementAvatar(avatar: String) {
        ivReviewCredibilityAchievementAvatar.urlSrc = avatar
    }

    private fun PartialReviewCredibilityAchievementBinding.bindAchievementName(name: String) {
        tvReviewCredibilityAchievementName.text = name
    }

    private fun PartialReviewCredibilityAchievementBinding.bindAchievementCard(color: String) {
        containerReviewCredibilityAchievementDetail.setBackgroundColor(
            color.toColorInt(DEFAULT_BACKGROUND_COLOR)
        )
    }

    private fun PartialReviewCredibilityAchievementBinding.bindListeners(mementoLink: String, name: String) {
        cardReviewCredibilityAchievement.setOnClickListener {
            listener?.onClickAchievementSticker(mementoLink, name)
        }
        ivReviewCredibilityAchievementAvatar.setOnClickListener {
            listener?.onClickAchievementSticker(mementoLink, name)
        }
    }

    fun showData(data: ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel?) {
        with(binding) {
            if (data == null) {
                hide()
            } else {
                bindAchievementAvatarBorder(data.color)
                bindAchievementAvatar(data.avatar)
                bindAchievementName(data.name)
                bindAchievementCard(data.color)
                bindListeners(data.mementoLink, data.name)
                root.visible()
            }
        }
    }

    fun setListener(newListener: ReviewCredibilityAchievementBoxWidget.Listener?) {
        listener = newListener
    }
}
