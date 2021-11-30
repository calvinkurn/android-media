package com.tokopedia.payment.setting.add.view.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.payment.setting.add.view.fragment.AddCreditCardFragment
import com.tokopedia.payment.setting.list.model.PaymentSignature

class AddCreditCardActivity : BaseSimpleActivity() {

    override fun getNewFragment() = AddCreditCardFragment.createInstance(intent.extras)

    override fun onCreate(savedInstanceState: Bundle?) {
        validateBundleData()
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        if(GlobalConfig.APPLICATION_TYPE== GlobalConfig.CONSUMER_APPLICATION|| GlobalConfig.APPLICATION_TYPE== GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread {
                window.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    override fun onBackPressed() {
        setResult(Activity.RESULT_OK)
        super.onBackPressed()
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