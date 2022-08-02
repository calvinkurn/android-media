package com.tokopedia.topupbills.telco.postpaid.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.FrameLayout
import android.widget.TextView
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

class DigitalSignalOperatorResultWidget @JvmOverloads constructor(@NotNull context: Context,
                                                              attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0)
    : FrameLayout(context, attrs, defStyleAttr) {

    private val label: TextView
    private val value: TextView

    init {
        val view = View.inflate(context, R.layout.item_signal_operator_result, this)
        label = view.findViewById(R.id.signal_operator_label)
        value = view.findViewById(R.id.signal_operator_value)
    }

    fun setLabel(labelText: String) {
        label.text = labelText
    }

    fun setValue(valueText: String) {
        value.text = valueText
    }
}