package com.tokopedia.pms.paymentlist.presentation.activity

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.pms.bankaccount.view.ChangeBankAccountActivity
import com.tokopedia.pms.clickbca.view.ChangeClickBcaActivity
import com.tokopedia.pms.paymentlist.di.DaggerPaymentListComponent
import com.tokopedia.pms.paymentlist.di.PaymentListComponent
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.extractValues
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment
import com.tokopedia.pms.paymentlist.presentation.listeners.PaymentListActionListener
import com.tokopedia.pms.proof.view.UploadProofPaymentActivity

class PaymentListActivity : BaseSimpleActivity(), HasComponent<PaymentListComponent>,
    PaymentListActionListener {

    private lateinit var component: PaymentListComponent

    override fun getNewFragment(): Fragment {
        return DeferredPaymentListFragment.createInstance()
    }

    override fun getComponent(): PaymentListComponent {
        if (!::component.isInitialized)
            component = DaggerPaymentListComponent.builder()
                .baseAppComponent(
                    (applicationContext as BaseMainApplication)
                        .baseAppComponent
                ).build()
        return component
    }

    override fun cancelSingleTransaction(
        transactionId: String,
        merchantCode: String,
        productName: String?
    ) {
        (fragment as DeferredPaymentListFragment).invokeCancelSingleTransaction(
            transactionId,
            merchantCode, productName
        )
    }

    override fun cancelCombinedTransaction(model: BasePaymentModel) {
        (fragment as DeferredPaymentListFragment).showCombinedTransactionDetail(model)
    }

    override fun changeAccountDetail(model: BasePaymentModel) {
        val intent = ChangeBankAccountActivity.createIntent(this, model)
        startActivityForResult(intent, REQUEST_CODE_CHANGE_BANK_ACCOUNT)
    }

    override fun uploadPaymentProof(model: BasePaymentModel) {
        val intent: Intent = UploadProofPaymentActivity.createIntent(this, model)
        startActivityForResult(intent, REQUEST_CODE_UPLOAD_PROOF)
    }

    override fun changeBcaUserId(model: BasePaymentModel) {
        val codePair = model.extractValues()
        val intent = ChangeClickBcaActivity.createIntent(
            this, codePair.first,
            codePair.second, model.paymentCode
        )
        startActivityForResult(intent, REQUEST_CODE_CHANGE_BCA_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when(requestCode) {
                REQUEST_CODE_CHANGE_BANK_ACCOUNT, REQUEST_CODE_UPLOAD_PROOF,
                REQUEST_CODE_CHANGE_BCA_ID -> (fragment as DeferredPaymentListFragment).loadInitialDeferredTransactions()
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    companion object {
        const val REQUEST_CODE_CHANGE_BANK_ACCOUNT = 1
        const val REQUEST_CODE_UPLOAD_PROOF = 2
        const val REQUEST_CODE_CHANGE_BCA_ID = 3
    }
}