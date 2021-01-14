package com.tokopedia.gamification.giftbox.presentation.views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.content.ContextCompat
import com.tokopedia.gamification.R
import com.tokopedia.gamification.giftbox.presentation.helpers.dpToPx
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography

class GiftBoxReminderButton @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr) {

    val LAYOUT_ID = R.layout.gami_giftbox_reminder_button
    val loaderReminder: LoaderUnify
    val tvReminderBtn: Typography
    val imageBell: AppCompatImageView

    init {
        View.inflate(context, LAYOUT_ID, this)
        loaderReminder = findViewById(R.id.loaderReminder1)
        tvReminderBtn = findViewById(R.id.tvReminderBtn1)
        imageBell = findViewById(R.id.imageBell)

        val padding = dpToPx(11).toInt()
        val lp = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.apply {
            setPadding(padding, padding, padding, padding)
        }
        layoutParams = lp
        background = ContextCompat.getDrawable(context, R.drawable.gami_bg_reminder)
    }

    fun performLoading() {
        loaderReminder.visibility = View.VISIBLE
        tvReminderBtn.visibility = View.GONE
        imageBell.visibility = View.GONE
    }

    fun stopLoading() {
        loaderReminder.visibility = View.GONE
        tvReminderBtn.visibility = View.VISIBLE
        imageBell.visibility = View.VISIBLE
    }

    fun setText(text: String?) {
        tvReminderBtn.text = text
    }

    fun setIcon(isEnabled: Boolean) {
        if (isEnabled) {
            imageBell.setImageResource(R.drawable.gami_bell_ring)
        } else {
            imageBell.setImageResource(R.drawable.gami_bell_1)
        }
    }
}