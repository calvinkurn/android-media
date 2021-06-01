package com.tokopedia.pms.bankaccount.view.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.config.GlobalConfig
import com.tokopedia.pms.bankaccount.data.model.BankListModel
import com.tokopedia.pms.bankaccount.view.bottomsheet.BankDestinationBottomSheet
import com.tokopedia.pms.bankaccount.view.fragment.ChangeBankAccountFragment
import com.tokopedia.pms.paymentlist.di.DaggerPmsComponent
import com.tokopedia.pms.paymentlist.di.PmsComponent
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel

/**
 * Created by zulfikarrahman on 6/25/18.
 */
class ChangeBankAccountActivity : BaseSimpleActivity(), HasComponent<PmsComponent>,
    ChangeBankListener {

    private lateinit var component: PmsComponent

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
        return ChangeBankAccountFragment.createInstance(paymentListModel)
    }

    override fun getComponent(): PmsComponent {
        if (!::component.isInitialized)
            component = DaggerPmsComponent.builder()
                .baseAppComponent(
                    (applicationContext as BaseMainApplication)
                        .baseAppComponent
                ).build()
        return component
    }

    override fun onBankSelected(bank: BankListModel) {
        (fragment as ChangeBankAccountFragment).onBankSelected(bank)
    }

    override fun openDestinationBankBottomSheet() {
        BankDestinationBottomSheet.show(supportFragmentManager)
    }

    companion object {
        const val PAYMENT_LIST_MODEL_EXTRA = "payment_list_model_extra"

        fun createIntent(context: Context?, paymentListModel: BasePaymentModel?): Intent {
            val intent = Intent(context, ChangeBankAccountActivity::class.java)
            intent.putExtra(PAYMENT_LIST_MODEL_EXTRA, paymentListModel)
            return intent
        }
    }
}

interface ChangeBankListener {
    fun openDestinationBankBottomSheet()
    fun onBankSelected(bank: BankListModel)
}