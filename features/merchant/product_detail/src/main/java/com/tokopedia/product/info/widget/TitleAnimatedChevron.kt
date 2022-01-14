package com.tokopedia.product.info.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.databinding.LayoutTitleAnimatedChevronBinding

/**
 * Created by Yehezkiel on 13/10/20
 */
class TitleAnimatedChevron @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    companion object {
        private const val ANIMATE_ROTATE_TOP_DEGREE = 180f
        private const val ANIMATE_ROTATE_TOP_DURATION = 300L
        private const val ANIMATE_ROTATE_DOWN_DURATION = 300L
    }

    private val binding = LayoutTitleAnimatedChevronBinding.inflate(LayoutInflater.from(context))

    init {
        addView(binding.root)
    }

    var isExpand: Boolean = false
        set(value) {
            field = value
            if (value) {
                rotateTop()
            } else {
                if (binding.productDetailToggle.rotation != 0F) {
                    rotateDown()
                }
            }
        }

    var titleText: String = ""
        set(value) {
            field = value
            binding.productDetailTitle.text = value
        }

    private fun rotateTop() {
        binding.productDetailToggle.run {
            animate().rotation(ANIMATE_ROTATE_TOP_DEGREE).duration = ANIMATE_ROTATE_TOP_DURATION
        }
    }

    private fun rotateDown() {
        binding.productDetailToggle.run {
            animate().rotation(0F).duration = ANIMATE_ROTATE_DOWN_DURATION
        }
    }
}