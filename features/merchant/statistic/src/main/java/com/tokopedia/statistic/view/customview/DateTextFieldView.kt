package com.tokopedia.statistic.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.statistic.databinding.ViewStcDateTextFieldBinding

/**
 * Created By @ilhamsuaib on 16/06/20
 */

class DateTextFieldView(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    private val binding: ViewStcDateTextFieldBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewStcDateTextFieldBinding.inflate(inflater, this, true)
    }

    var isActive: Boolean = false
        set(value) {
            field = value
            val color = if (value) {
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_G400)
            } else {
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N150)
            }
            binding.horLineDtf.setBackgroundColor(color)
        }

    var label: String = ""
        set(value) {
            field = value
            binding.tvStcDateLabel.text = value
        }

    var hint: String = binding.tvStcDateText.text.toString()
        set(value) {
            field = value
            binding.tvStcDateText.text = value
            binding.tvStcDateText.setTextColor(
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
            )
        }

    var valueStr: String = ""
        set(value) {
            field = value
            binding.tvStcDateText.text = value
            binding.tvStcDateText.setTextColor(
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
            )
        }

    var labelGravity: Int = Gravity.START
        set(value) {
            field = value
            binding.tvStcDateLabel.gravity = value
        }
}