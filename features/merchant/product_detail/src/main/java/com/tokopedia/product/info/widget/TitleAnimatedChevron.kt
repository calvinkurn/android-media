package com.tokopedia.product.info.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import kotlinx.android.synthetic.main.widget_slider_counter_view.view.*

/**
 * Created by Yehezkiel on 13/10/20
 */
class TitleAnimatedChevron  @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    private var count: Int = 0

    init {
        View.inflate(context, R.layout.layout_title_animated_chevron, this)
    }

    fun setView(startCounter: Int = 1, count: Int) {
        this.count = count

        if (count == 0) {
            sliderText.hide()
        } else {
            sliderText.show()
            sliderText?.text = context.getString(R.string.slider_counter_builder, startCounter, count)
        }
    }

    fun setCurrentCounter(startCounter: Int) {
        sliderText?.text = context.getString(R.string.slider_counter_builder, startCounter, count)
    }
}