package com.tokopedia.localizationchooseaddress.ui.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ChooseAddressWidget: ConstraintLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    private var textChosenAddress: Typography? = null
    private var buttonChooseAddress: UnifyButton? = null

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.choose_address_widget, this, true)
        textChosenAddress = view.findViewById(R.id.text_chosen_address)
        buttonChooseAddress = view.findViewById(R.id.btn_arrow)
    }
}