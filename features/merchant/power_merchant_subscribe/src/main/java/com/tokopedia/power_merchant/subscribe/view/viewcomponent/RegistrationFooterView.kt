package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.applink.RouteManager
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import com.tokopedia.power_merchant.subscribe.common.constant.Constant
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

        tvPmRegistrationTnC.setOnClickListener {
            RouteManager.route(context, Constant.Url.POWER_MERCHANT_TERMS_AND_CONDITION)
        }
    }

    fun setOnCtaClickListener(action: (isAgreed: Boolean) -> Unit) {
        btnPmRegister.setOnClickListener {
            action(cbPmRegistrationTnC.isChecked)
        }
    }

    fun setCtaText(ctaText: String) {
        btnPmRegister.text = ctaText
    }

    fun setTnCVisibility(isVisible: Boolean) {
        if (isVisible) {
            tvPmRegistrationTnC.visible()
            cbPmRegistrationTnC.visible()
        } else {
            tvPmRegistrationTnC.gone()
            cbPmRegistrationTnC.gone()
        }
    }

    fun showRegistrationProgress() {
        btnPmRegister.isLoading = true
    }

    fun hideRegistrationProgress() {
        btnPmRegister.isLoading = false
    }
}