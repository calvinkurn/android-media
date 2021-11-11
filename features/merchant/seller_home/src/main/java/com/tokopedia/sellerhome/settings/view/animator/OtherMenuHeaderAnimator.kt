package com.tokopedia.sellerhome.settings.view.animator

import android.widget.LinearLayout
import androidx.core.widget.NestedScrollView

class OtherMenuHeaderAnimator(
    private val scrollView: NestedScrollView?,
    private val otherMenuHeader: LinearLayout?,
    private val headerMaxHeight: Int
) {

    companion object {
        private const val TRANSLATE_START_OFFSET = 80
        private const val TRANSLATE_END_OFFSET = 160
        private const val INITIAL_TRANSLATION_Y = 0f
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
                offset <= TRANSLATE_START_OFFSET -> INITIAL_TRANSLATION_Y
                offset >= TRANSLATE_END_OFFSET -> headerMaxHeight.toFloat()
                else -> calculateToolbarTranslation(offset)
            }
        otherMenuHeader?.translationY = translationY
    }

    private fun calculateToolbarTranslation(offset: Int): Float {
        return (offset - TRANSLATE_START_OFFSET)
            .toFloat()
            .div(translateDivisor)
            .times(headerMaxHeight)
    }

}