package com.tokopedia.mvc.common.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import com.tokopedia.campaign.utils.constant.DateConstant
import com.tokopedia.kotlin.extensions.view.formatTo
import com.tokopedia.mvc.R
import com.tokopedia.unifycomponents.BaseCustomView
import com.tokopedia.unifyprinciples.Typography
import java.text.SimpleDateFormat
import java.util.*

class VerticalDateTimeView: BaseCustomView {
    var dateTime: String = ""
        set(value) {
            field = value
            refreshViews()
        }

    var tpgDate: Typography? = null
    var tpgTime: Typography? = null

    constructor(context: Context) : super(context) {
        init(null)
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init(attrs)
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(attrs)
    }

    private fun init(attrs: AttributeSet?) {
        val view = View.inflate(context, R.layout.smvc_customview_vertical_date_time_view, this)
        setupViews(view)
        defineCustomAttributes(attrs)
        refreshViews()
    }

    private fun setupViews(view: View) {
        tpgDate = view.findViewById(R.id.tpg_date)
        tpgTime = view.findViewById(R.id.tpg_time)
    }

    private fun defineCustomAttributes(attrs: AttributeSet?) {
        if (attrs != null) {
            val styledAttributes = context.obtainStyledAttributes(attrs, R.styleable.VerticalDateTimeView, 0, 0)
            try {
                dateTime = styledAttributes.getString(R.styleable.VerticalDateTimeView_date_time_placeholder_text).orEmpty()
            } finally {
                styledAttributes.recycle()
            }
        }
    }

    private fun refreshViews() {
        try {
            val format = SimpleDateFormat(
                DateConstant.DATE_WITH_SECOND_PRECISION_ISO_8601,
                Locale.getDefault()
            )
            val dateTime = format.parse(dateTime)
            val dateString = dateTime?.formatTo(DateConstant.DATE_YEAR_PRECISION)
            val timeString = dateTime?.formatTo(DateConstant.TIME_MINUTE_PRECISION_WITH_TIMEZONE)
            tpgDate?.text = dateString
            tpgTime?.text = timeString
        } catch (t: Throwable) {}
    }
}
