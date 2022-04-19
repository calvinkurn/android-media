package com.tokopedia.sellerhome.settings.view.animator

import androidx.appcompat.widget.AppCompatImageView
import com.tokopedia.unifyprinciples.UnifyMotion

class OtherMenuShareButtonAnimator(private val shareButton: AppCompatImageView?) {

    companion object {
        private const val INITIAL_TRANSLATION_X = 48f
        private const val TARGET_TRANSLATION_X = -48f
        private const val DEFAULT_TRANSLATION_X = 0f
        private const val INITIAL_APLHA = 0f
        private const val FINAL_ALPHA = 1f
    }

    fun setInitialButtonState() {
        shareButton?.run {
            alpha = INITIAL_APLHA
            translationX = INITIAL_TRANSLATION_X
        }
    }

    fun animateShareButtonSlideIn() {
        shareButton?.run {
            alpha = FINAL_ALPHA
            animate()
                ?.translationXBy(TARGET_TRANSLATION_X)
                ?.setInterpolator(UnifyMotion.EASE_OUT)
                ?.setDuration(UnifyMotion.T3)
        }
    }

    fun isShareButtonShowing(): Boolean {
        return shareButton?.alpha == FINAL_ALPHA && shareButton.translationX == DEFAULT_TRANSLATION_X
    }

}