package com.tokopedia.product.info.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.product.detail.R
import kotlinx.android.synthetic.main.layout_title_animated_chevron.view.*

/**
 * Created by Yehezkiel on 13/10/20
 */
class TitleAnimatedChevron @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    init {
        View.inflate(context, R.layout.layout_title_animated_chevron, this)
    }

    var isExpand: Boolean = false
        set(value) {
            field = value
            if (value) {
                rotateTop()
            } else {
                if (product_detail_toggle?.rotation != 0F) {
                    rotateDown()
                }
            }
        }

    var titleText: String = ""
        set(value) {
            field = value
            product_detail_title?.text = value
        }

    private fun rotateTop() {
        product_detail_toggle?.run {
            animate().rotation(180F).duration = 300
        }
    }

    private fun rotateDown() {
        product_detail_toggle?.run {
            animate().rotation(0F).duration = 300
        }
    }
}