package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 27/05/19.
 */
class DigitalTelcoBillsResultWidget @JvmOverloads constructor(@NotNull context: Context,
                                                              attrs: AttributeSet? = null,
                                                              defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val label: TextView
    private val value: TextView

    init {
        val view = View.inflate(context, R.layout.item_digital_bills_result, this)
        label = view.findViewById(R.id.label)
        value = view.findViewById(R.id.value)
    }

    fun setLabel(labelText: String) {
        label.setText(labelText)
    }

    fun setValue(valueText: String) {
        value.setText(valueText)
    }
}