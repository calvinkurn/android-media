package com.tokopedia.power_merchant.subscribe.view.viewcomponent

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.power_merchant.subscribe.databinding.ViewPmRegistrationFooterBinding

/**
 * Created By @ilhamsuaib on 02/03/21
 */

class RegistrationFooterView : ConstraintLayout {

    private var binding: ViewPmRegistrationFooterBinding? = null
    private var tncClickListener: (() -> Unit)? = null
    private var checkedListener: ((Boolean) -> Unit)? = null

    constructor (context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    init {
        binding = ViewPmRegistrationFooterBinding.inflate(LayoutInflater.from(context), this, true)
        binding?.run {
            tvPmRegistrationTnC.setOnClickListener {
                tncClickListener?.invoke()
            }
            cbPmRegistrationTnC.setOnCheckedChangeListener { _, isChecked ->
                checkedListener?.invoke(isChecked)
            }
        }
    }

    fun setOnTncClickListener(action: () -> Unit) {
        this.tncClickListener = action
    }

    fun setOnCtaClickListener(action: (isAgreed: Boolean) -> Unit) {
        binding?.run {
            btnPmRegister.setOnClickListener {
                action(cbPmRegistrationTnC.isChecked)
            }
        }
    }

    fun hideCtaButton() {
        binding?.btnPmRegister?.hide()
    }

    fun showCtaButton() {
        binding?.btnPmRegister?.show()
    }

    fun setCtaText(ctaText: String) = binding?.run {
        btnPmRegister.text = ctaText
    }

    fun setTnCVisibility(isVisible: Boolean) = binding?.run {
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

    fun showLoadingState() = binding?.run {
        btnPmRegister.isLoading = true
    }

    fun hideLoadingState() = binding?.run {
        btnPmRegister.isLoading = false
    }
}