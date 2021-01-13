package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.unifyprinciples.Typography

class GreenGradientButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val layout = R.layout.gami_green_gradient_button
    var btn: Typography

    init {
        View.inflate(context, layout, this)
        btn = findViewById(R.id.cta)
        val padding = dpToPx(11).toInt()
        val lp = FrameLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        lp.apply {
            setPadding(padding, padding, padding, padding)
        }
        layoutParams = lp
        background = ContextCompat.getDrawable(context, R.drawable.gami_green_grad_btn)
    }

    fun setText(text: String?) {
        btn.text = text
    }
}