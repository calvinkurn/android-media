package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.sellerhomecommon.R
import com.tokopedia.sellerhomecommon.databinding.ShcLastUpdatedInfoViewBinding
import com.tokopedia.sellerhomecommon.utils.DateTimeUtil
import java.util.*

/**
 * Created by @ilhamsuaib on 28/01/22.
 */

class LastUpdatedView : LinearLayout {

    private var binding: ShcLastUpdatedInfoViewBinding? = null

    constructor(context: Context) : super(context) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initView(context)
    }

    fun setLastUpdated(lastUpdated: Long) {
        binding?.run {
            val lastUpdatedStr = getLastUpdatedInfo(root.context, lastUpdated)
            val lastUpdatedFmt = "<i>$lastUpdatedStr</i>".parseAsHtml()
            tvShcLastUpdated.text = lastUpdatedFmt
        }
    }

    fun setRefreshButtonVisibility(shouldShow: Boolean) {
        binding?.icShcRefreshLastUpdated?.isVisible = shouldShow
    }

    fun setRefreshButtonClickListener(callback: () -> Unit) {
        binding?.icShcRefreshLastUpdated?.setOnClickListener {
            callback()
        }
    }

    private fun initView(context: Context) {
        binding = ShcLastUpdatedInfoViewBinding.inflate(
            LayoutInflater.from(context), this, true
        )

        showLastUpdatedInfo()
    }

    private fun showLastUpdatedInfo() {
        binding?.viewShcLastUpdatedContainer?.run {
            visible()
            /*scaleX = 0f
            scaleY = 0f*/
        }
        /*Handler(Looper.getMainLooper()).postDelayed({
            binding?.viewShcLastUpdatedContainer?.run {
                animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
            }
        }, INITIAL_SHOW_DELAY)*/
    }

    private fun getLastUpdatedInfo(context: Context, timeInMillis: Long): String {
        val nowMillis = Date().time
        val nowCal = getCalendar(nowMillis)
        val lastUpdatedCal = getCalendar(timeInMillis)
        val isSameDay = lastUpdatedCal.time == nowCal.time
        val isYesterday = lastUpdatedCal.timeInMillis
            .minus(nowCal.timeInMillis) < DateTimeUtil.ONE_DAY_MILLIS
        return when {
            isSameDay -> {
                context.getString(
                    R.string.shc_last_updated_same_day,
                    DateTimeUtil.format(timeInMillis, DateTimeUtil.FORMAT_HH_MM)
                )
            }
            isYesterday -> {
                context.getString(R.string.shc_last_updated_yesterday)
            }
            else -> {
                context.getString(
                    R.string.shc_last_updated_more_then_yesterday,
                    DateTimeUtil.format(timeInMillis, DateTimeUtil.FORMAT_DD_MMM_YYYY)
                )
            }
        }
    }

    private fun getCalendar(timeInMillis: Long): Calendar {
        return Calendar.getInstance().apply {
            time = Date(timeInMillis)
            set(Calendar.HOUR_OF_DAY, DateTimeUtil.ZERO)
            set(Calendar.MINUTE, DateTimeUtil.ZERO)
            set(Calendar.SECOND, DateTimeUtil.ZERO)
            set(Calendar.MILLISECOND, DateTimeUtil.ZERO)
        }
    }
}