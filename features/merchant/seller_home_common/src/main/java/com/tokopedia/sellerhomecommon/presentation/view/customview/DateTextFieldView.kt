package com.tokopedia.sellerhomecommon.presentation.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.getResColor
import com.tokopedia.sellerhomecommon.databinding.ViewShcDateTextFieldBinding

/**
 * Created by @ilhamsuaib on 09/02/22.
 */

class DateTextFieldView(
    context: Context?,
    attrs: AttributeSet?
) : LinearLayout(context, attrs) {

    private val binding: ViewShcDateTextFieldBinding

    init {
        val inflater = LayoutInflater.from(context)
        binding = ViewShcDateTextFieldBinding.inflate(inflater, this, true)
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
            binding.tvShcDateLabel.text = value
        }

    var hint: String = binding.tvShcDateText.text.toString()
        set(value) {
            field = value
            binding.tvShcDateText.text = value
            binding.tvShcDateText.setTextColor(
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
            )
        }

    var valueStr: String = ""
        set(value) {
            field = value
            binding.tvShcDateText.text = value
            binding.tvShcDateText.setTextColor(
                context.getResColor(com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
            )
        }

    var labelGravity: Int = Gravity.START
        set(value) {
            field = value
            binding.tvShcDateLabel.gravity = value
        }
}