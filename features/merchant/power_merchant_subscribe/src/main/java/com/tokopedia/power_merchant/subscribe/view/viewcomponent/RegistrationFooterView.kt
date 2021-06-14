package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.R
import kotlinx.android.synthetic.main.view_pm_registration_footer.view.*

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationFooterView : ConstraintLayout {

    private var tncClickListener: (() -> Unit)? = null
    private var checkedListener: ((Boolean) -> Unit)? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        View.inflate(context, R.layout.view_pm_registration_footer, this)

        tvPmRegistrationTnC.setOnClickListener {
            tncClickListener?.invoke()
        }
        cbPmRegistrationTnC.setOnCheckedChangeListener { _, isChecked ->
            checkedListener?.invoke(isChecked)
        }
    }

    fun setOnTncClickListener(action: () -> Unit) {
        this.tncClickListener = action
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

    fun setOnTickboxCheckedListener(callback: (Boolean) -> Unit) {
        this.checkedListener = callback
    }

    fun showLoadingState() {
        btnPmRegister.isLoading = true
    }

    fun hideLoadingState() {
        btnPmRegister.isLoading = false
    }
}