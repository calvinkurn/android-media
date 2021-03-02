package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.view_pm_registration_footer.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationFooterView : ConstraintLayout {

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_pm_registration_footer, this)
    }

    fun setOnRegisterClickListener(action: (isAgreed: Boolean) -> Unit) {
        btnPmRegister.setOnClickListener {
            action(cbPmRegistrationTnC.isChecked)
        }
    }
}