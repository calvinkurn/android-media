package com.tokopedia.topupbills.telco.view.widget

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.design.base.BaseCustomView
import com.tokopedia.topupbills.R
import org.jetbrains.annotations.NotNull

/**
 * Created by nabillasabbaha on 24/05/19.
 */
class DigitalAddToMyBillsWidget @JvmOverloads constructor(@NotNull context: Context, attrs: AttributeSet? = null,
                                                          defStyleAttr: Int = 0)
    : BaseCustomView(context, attrs, defStyleAttr) {

    private val checkBox: CheckBox
    private val infoIcon: ImageView
    private val descInfo: TextView

    init {
        val view = View.inflate(context, R.layout.view_digital_add_to_mybills, this)
        checkBox = view.findViewById(R.id.checkbox)
        infoIcon = view.findViewById(R.id.icon_info)
        descInfo = view.findViewById(R.id.desc_info)
    }

    fun renderContent(dateBills: String) {
        descInfo.setText(String.format(context.getString(R.string.digital_desc_add_to_mybills), dateBills))
        infoIcon.setOnClickListener {

        }
    }

}