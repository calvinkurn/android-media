package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.gamification.R

class CekTokoButtonContainer @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    val layout = R.layout.gami_cek_toko_btn_container

    var btnReminder: GiftBoxReminderButton
    var btnSecond: GreenGradientButton

    init {
        View.inflate(context, layout, this)
        btnReminder = findViewById(R.id.btnFirst)
        btnSecond = findViewById(R.id.btnSecond)
        val isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false
        if (isTablet) {
            wrapButtons()
        }
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        layoutParams = lp
        orientation = HORIZONTAL
    }

    fun setSecondButtonText(text: String?) {
        btnSecond.setText(text)
    }

    fun toggleReminderVisibility(show: Boolean) {
        btnReminder.visibility = if (show) View.VISIBLE else View.GONE
    }

    fun wrapButtons(){
        this.layoutParams.width = LinearLayout.LayoutParams.WRAP_CONTENT
    }
}