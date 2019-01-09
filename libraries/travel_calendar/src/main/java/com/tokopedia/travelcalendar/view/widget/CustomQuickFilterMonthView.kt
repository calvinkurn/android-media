package com.tokopedia.travelcalendar.view.widget

import android.content.Context
import android.support.v7.widget.AppCompatTextView
import android.util.AttributeSet
import android.view.View

import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.travelcalendar.R

import kotlinx.android.synthetic.main.item_month_quick_filter.view.*
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 28/12/18.
 */
class CustomQuickFilterMonthView @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                           defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val monthName: AppCompatTextView

    init {
        val view = View.inflate(context, R.layout.item_month_quick_filter, this)
        monthName = view.month
    }

    fun setTextMonth(month: String) {
        monthName.text = month
    }
}
