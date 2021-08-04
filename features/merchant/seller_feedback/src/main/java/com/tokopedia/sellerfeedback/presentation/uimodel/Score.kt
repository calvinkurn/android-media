package com.tokopedia.sellerfeedback.presentation.uimodel

import com.tokopedia.sellerfeedback.R

sealed class Score {
    abstract val value: Int
    abstract val colorId: Int
    abstract val drawableActiveId: Int
    abstract val drawableInactiveId: Int

    data class Bad(
            override val value: Int = R.string.feedback_form_score_bad,
            override val colorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_Y500,
            override val drawableActiveId: Int = R.drawable.ic_emoji_bad_active,
            override val drawableInactiveId: Int = R.drawable.ic_emoji_bad_inactive
    ) : Score()

    data class Neutral(
            override val value: Int = R.string.feedback_form_score_neutral,
            override val colorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_Y400,
            override val drawableActiveId: Int = R.drawable.ic_emoji_neutral_active,
            override val drawableInactiveId: Int = R.drawable.ic_emoji_neutral_inactive
    ) : Score()

    data class Good(
            override val value: Int = R.string.feedback_form_score_good,
            override val colorId: Int = com.tokopedia.unifyprinciples.R.color.Unify_G500,
            override val drawableActiveId: Int = R.drawable.ic_emoji_good_active,
            override val drawableInactiveId: Int = R.drawable.ic_emoji_good_inactive
    ) : Score()
}
