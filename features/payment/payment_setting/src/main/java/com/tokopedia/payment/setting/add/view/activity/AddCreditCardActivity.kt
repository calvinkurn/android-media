package com.tokopedia.payment.setting.add.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.payment.setting.add.view.fragment.AddCreditCardFragment
import com.tokopedia.payment.setting.list.model.PaymentSignature

class AddCreditCardActivity : BaseSimpleActivity() {

    override fun getNewFragment(): Fragment? {
        val bundle = Bundle()
        intent.extras?.let {
            if (it.containsKey(ARG_PAYMENT_SIGNATURE))
                bundle.putAll(it)
        }
        return AddCreditCardFragment.createInstance(bundle)
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        validateBundleData()
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