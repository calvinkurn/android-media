package com.tokopedia.statistic.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.statistic.R

import kotlinx.android.synthetic.main.view_stc_date_text_field.view.*

/**
 * Created By @ilhamsuaib on 16/06/20
 */

class DateTextFieldView(
        context: Context?,
        attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    init {
        View.inflate(context, R.layout.view_stc_date_text_field, this)
    }

    var isActive: Boolean = false
        set(value) {
            field = value
            val color = if (value) {
                context.getResColor(com.tokopedia.unifyprinciples.R.color.light_G400)
            } else {
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Neutral_N150)
            }
            horLineDtf.setBackgroundColor(color)
        }

    var label: String = ""
        set(value) {
            field = value
            tvStcDateLabel.text = value
        }

    var hint: String = ""
        set(value) {
            field = value
            tvStcDateText.text = value
            tvStcDateText.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_32))
        }

    var valueStr: String = ""
        set(value) {
            field = value
            tvStcDateText.text = value
            tvStcDateText.setTextColor(context.getResColor(com.tokopedia.unifyprinciples.R.color.Neutral_N700_96))
        }

    var labelGravity: Int = Gravity.START
        set(value) {
            field = value
            tvStcDateLabel.gravity = value
        }
}