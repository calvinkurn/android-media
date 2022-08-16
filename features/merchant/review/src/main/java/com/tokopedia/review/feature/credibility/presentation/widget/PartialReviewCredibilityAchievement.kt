package com.tokopedia.review.feature.credibility.presentation.widget

import androidx.core.content.ContextCompat
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

    fun hide() {
        binding.root.invisible()
    }

    fun showData(data: ReviewCredibilityAchievementBoxUiModel.ReviewCredibilityAchievementUiModel?) {
        if (data == null) {
            hide()
        } else {
            binding.ivReviewCredibilityAchievementAvatarBorder.setImageDrawable(
                ContextCompat.getDrawable(
                    binding.root.context, R.drawable.border_review_credibility_achievement_avatar
                )
            )
            binding.ivReviewCredibilityAchievementAvatar.urlSrc = data.avatar
            binding.tvReviewCredibilityAchievementName.text = data.name
            binding.tvReviewCredibilityAchievementCounter.text = data.counter
            binding.containerReviewCredibilityAchievementDetail.setBackgroundColor(
                data.color.toColorInt(com.tokopedia.unifyprinciples.R.color.Unify_TN600)
            )
            binding.cardReviewCredibilityAchievement.setOnClickListener {
                listener?.onClickAchievementSticker(data.mementoLink, data.name)
            }
            binding.ivReviewCredibilityAchievementAvatar.setOnClickListener {
                listener?.onClickAchievementSticker(data.mementoLink, data.name)
            }
            binding.root.visible()
        }
    }

    fun setListener(newListener: ReviewCredibilityAchievementBoxWidget.Listener?) {
        listener = newListener
    }
}