package com.tokopedia.pms.proof.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.config.GlobalConfig
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel

/**
 * Created by zulfikarrahman on 7/6/18.
 */
class UploadProofPaymentActivity : BaseSimpleActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setSecureWindowFlag()
    }

    private fun setSecureWindowFlag() {
        if (GlobalConfig.APPLICATION_TYPE == GlobalConfig.CONSUMER_APPLICATION || GlobalConfig.APPLICATION_TYPE == GlobalConfig.SELLER_APPLICATION) {
            runOnUiThread {
                val window = window
                window?.addFlags(WindowManager.LayoutParams.FLAG_SECURE)
            }
        }
    }

    override fun getNewFragment(): Fragment {
        val paymentListModel: BasePaymentModel =
            intent.getParcelableExtra(PAYMENT_LIST_MODEL_EXTRA)
        return UploadProofPaymentFragment.createInstance(paymentListModel)
    }

    companion object {
        const val PAYMENT_LIST_MODEL_EXTRA = "payment_list_model_extra"

        fun createIntent(context: Context?, paymentListModel: BasePaymentModel?): Intent {
            val intent = Intent(context, UploadProofPaymentActivity::class.java)
            intent.putExtra(PAYMENT_LIST_MODEL_EXTRA, paymentListModel)
            return intent
        }
    }
}