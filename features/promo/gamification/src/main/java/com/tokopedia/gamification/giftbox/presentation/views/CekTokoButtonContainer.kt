package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.gamification.R
import com.tokopedia.unifycomponents.toPx

class CekTokoButtonContainer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val layout = R.layout.gami_cek_toko_btn_container

    var btnReminder: GiftBoxReminderButton
    var btnSecond: GreenGradientButton
    var viewDivider: View
    var isTablet = false

    init {
        View.inflate(context, layout, this)
        btnReminder = findViewById(R.id.btnFirst)
        btnSecond = findViewById(R.id.btnSecond)
        viewDivider = findViewById(R.id.view_divider)
        isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false
        val lp = LayoutParams(if (isTablet) LayoutParams.WRAP_CONTENT else LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT)
        layoutParams = lp
        orientation = HORIZONTAL
    }

    fun setSecondButtonText(text: String?) {
        btnSecond.setText(text)
    }

    fun toggleReminderVisibility(show: Boolean) {
        btnReminder.visibility = if (show) View.VISIBLE else View.GONE
        viewDivider.visibility = if (show) View.VISIBLE else View.GONE
        if (!show && !isTablet) {
            val paddingSide = 48.toPx()
            btnSecond.setPadding(paddingSide, btnSecond.paddingTop, paddingSide, btnSecond.paddingBottom)
            wrapButtons()
        }
    }

    fun wrapButtons() {
        this.layoutParams.width = LayoutParams.WRAP_CONTENT
    }
}