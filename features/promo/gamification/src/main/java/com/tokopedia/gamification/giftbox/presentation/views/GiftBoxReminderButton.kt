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

        val paddingTop = context.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_green_gradient_btn_top_padding)?.toInt() ?: 0
        val paddingSide = context.resources?.getDimension(com.tokopedia.gamification.R.dimen.gami_green_gradient_btn_side_padding)?.toInt() ?: 0
        val isTablet = context.resources?.getBoolean(com.tokopedia.gamification.R.bool.gami_is_tablet) ?: false

        val lp = LinearLayout.LayoutParams(if (isTablet) LinearLayout.LayoutParams.WRAP_CONTENT else LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT)
        lp.apply {
            setPadding(paddingSide, paddingTop, paddingSide, paddingTop)
        }
        layoutParams = lp
        background = ContextCompat.getDrawable(context, R.drawable.gami_bg_reminder)

        imageBell.layoutParams.apply {
            if (isTablet) {
                width = dpToPx(28).toInt()
                height = dpToPx(28).toInt()
            }
        }
    }

    fun performLoading() {
        loaderReminder.visibility = View.VISIBLE
        tvReminderBtn.visibility = View.INVISIBLE
        imageBell.visibility = View.INVISIBLE
    }

    fun stopLoading() {
        loaderReminder.visibility = View.GONE
        tvReminderBtn.visibility = View.VISIBLE
        imageBell.visibility = View.VISIBLE
    }

    fun setText(text: String?) {
        tvReminderBtn.text = text
    }

    fun setIcon(isReminderSet: Boolean) {
        if (isReminderSet) {
            imageBell.setImageResource(R.drawable.gami_bell_ring)
        } else {
            imageBell.setImageResource(R.drawable.gami_bell_1)
        }
    }
}