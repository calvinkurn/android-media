package com.tokopedia.payment.setting.add.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.payment.setting.add.view.fragment.AddCreditCardFragment
import com.tokopedia.payment.setting.list.model.PaymentSignature

class AddCreditCardActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        return AddCreditCardFragment.createInstance(intent.extras)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        validateBundleData()
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        runOnUiThread { window.addFlags(WindowManager.LayoutParams.FLAG_SECURE) }
    }

    private fun validateBundleData() {
        intent.extras?.let {
            if (!it.containsKey(ARG_PAYMENT_SIGNATURE))
                finish()
        } ?: run {
            finish()
        }
    }

    companion object {
        const val ARG_PAYMENT_SIGNATURE = "arg_payment_signature"
        fun createIntent(context: Context, paymentSignature: PaymentSignature): Intent {
            return Intent(context, AddCreditCardActivity::class.java).apply {
                putExtra(ARG_PAYMENT_SIGNATURE, paymentSignature)
            }
        }
    }
}