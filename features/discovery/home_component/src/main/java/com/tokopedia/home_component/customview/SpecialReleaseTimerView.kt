package com.tokopedia.home_component.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.annotation.AttrRes
import com.tokopedia.home_component.R
import com.tokopedia.home_component.util.DateHelper
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import java.util.*

class SpecialReleaseTimerView: FrameLayout {
    private var itemView: View? = null
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, @AttrRes defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private val timerText: Typography? by lazy { itemView?.findViewById(R.id.special_release_timer_text) }
    private val timerIcon: IconUnify? by lazy { itemView?.findViewById(R.id.special_release_timer_icon) }

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.home_custom_view_special_release_timer_view, this)
        this.itemView = view
    }

    fun setTimer(serverTimeOffset: Long = 0L, expiredTime: String) {
        val expiredTimeDate = DateHelper.getExpiredTime(expiredTime)
        if (expiredTime.isNotEmpty() && !DateHelper.isExpired(serverTimeOffset, expiredTimeDate)) {
            val timerCopy = SpecialReleaseTimerCopyGenerator.getCopy(
                expiredTimeDate = expiredTimeDate,
                currentTimeDate = Date(),
                offset = serverTimeOffset
            )
            if (timerCopy.isNotEmpty()) {
                timerText?.text = timerCopy
                showTimer()
            } else {
                hideTimer()
            }
        } else {
            hideTimer()
        }
    }

    private fun hideTimer() {
        timerText?.invisible()
        timerIcon?.invisible()
    }

    private fun showTimer() {
        timerText?.visible()
        timerIcon?.visible()
    }
}