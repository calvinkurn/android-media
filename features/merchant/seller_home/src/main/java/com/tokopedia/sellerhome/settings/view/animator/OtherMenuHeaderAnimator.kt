package com.tokopedia.sellerhome.settings.view.animator

import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.widget.NestedScrollView

class OtherMenuHeaderAnimator(
    private val scrollView: NestedScrollView?,
    private val otherMenuHeader: ConstraintLayout?,
    private val toolbarHeight: Int
) {

    companion object {
        private const val TRANSLATE_START_OFFSET = 80
        private const val TRANSLATE_END_OFFSET = 160
    }

    private val translateDivisor by lazy {
        TRANSLATE_END_OFFSET - TRANSLATE_START_OFFSET
    }

    fun init() {
        scrollView?.setOnScrollChangeListener(NestedScrollView.OnScrollChangeListener { sv, _, _, _, _ ->
            translateToolbarView(sv.scrollY)
        })
    }

    private fun translateToolbarView(offset: Int) {
        val translationY =
            when {
                offset <= TRANSLATE_START_OFFSET -> 0f
                offset >= TRANSLATE_END_OFFSET -> toolbarHeight.toFloat()
                else -> calculateToolbarTranslation(offset)
            }
        otherMenuHeader?.translationY = translationY
    }

    private fun calculateToolbarTranslation(offset: Int): Float {
        return (offset - TRANSLATE_START_OFFSET)
            .toFloat()
            .div(translateDivisor)
            .times(toolbarHeight)
    }

}