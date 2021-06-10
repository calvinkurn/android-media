package com.tokopedia.pms.paymentlist.presentation.activity

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.pms.analytics.PmsAnalytics
import com.tokopedia.pms.analytics.PmsEvents
import com.tokopedia.pms.bankaccount.view.activity.ChangeBankAccountActivity
import com.tokopedia.pms.clickbca.view.ChangeClickBcaActivity
import com.tokopedia.pms.paymentlist.di.DaggerPmsComponent
import com.tokopedia.pms.paymentlist.di.PmsComponent
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.pms.paymentlist.domain.data.extractValues
import com.tokopedia.pms.paymentlist.presentation.fragment.DeferredPaymentListFragment
import com.tokopedia.pms.paymentlist.presentation.listener.PaymentListActionListener
import com.tokopedia.pms.proof.view.UploadProofPaymentActivity
import javax.inject.Inject

class PaymentListActivity : BaseSimpleActivity(), HasComponent<PmsComponent>,
    PaymentListActionListener {

    private lateinit var component: PmsComponent

    @Inject
    lateinit var pmsAnalytics: dagger.Lazy<PmsAnalytics>

    override fun getNewFragment(): Fragment {
        return DeferredPaymentListFragment.createInstance()
    }

    override fun getComponent(): PmsComponent {
        if (!::component.isInitialized) {
            component = DaggerPmsComponent.builder()
                .baseAppComponent(
                    (applicationContext as BaseMainApplication)
                        .baseAppComponent
                ).build()
            component.inject(this)
        }
        return component
    }

    override fun cancelSingleTransaction(
        transactionId: String,
        merchantCode: String,
        productName: String?,
        event: PmsEvents
    ) {
        sendEventToAnalytics(event)
        (fragment as DeferredPaymentListFragment).invokeCancelSingleTransaction(
            transactionId,
            merchantCode, productName
        )
    }

    override fun cancelCombinedTransaction(model: BasePaymentModel) {
        sendEventToAnalytics(PmsEvents.InvokeCancelTransactionBottomSheetEvent(3))
        (fragment as DeferredPaymentListFragment).showCombinedTransactionDetail(model)
    }

    override fun showInvoiceDetail(invoiceUrl: String?) {
        sendEventToAnalytics(PmsEvents.ShowTransactionDetailEvent(6))
        RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, invoiceUrl)
    }

    override fun changeAccountDetail(model: BasePaymentModel) {
        sendEventToAnalytics(PmsEvents.InvokeChangeAccountDetailsEvent(9))
        val intent = ChangeBankAccountActivity.createIntent(this, model)
        startActivityForResult(intent, REQUEST_CODE_CHANGE_BANK_ACCOUNT)
    }

    override fun uploadPaymentProof(model: BasePaymentModel) {
        sendEventToAnalytics(PmsEvents.UploadPaymentProofEvent(11))
        val intent: Intent = UploadProofPaymentActivity.createIntent(this, model)
        startActivityForResult(intent, REQUEST_CODE_UPLOAD_PROOF)
    }

    override fun changeBcaUserId(model: BasePaymentModel) {
        sendEventToAnalytics(PmsEvents.InvokeEditBcaUserIdEvent(7))
        val codePair = model.extractValues()
        val intent = ChangeClickBcaActivity.createIntent(
            this, codePair.first,
            codePair.second, model.paymentCode
        )
        startActivityForResult(intent, REQUEST_CODE_CHANGE_BCA_ID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_CODE_CHANGE_BANK_ACCOUNT, REQUEST_CODE_UPLOAD_PROOF,
                REQUEST_CODE_CHANGE_BCA_ID -> {
                    (fragment as DeferredPaymentListFragment).onRefresh()
                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    fun sendEventToAnalytics(event: PmsEvents) {
        if (::pmsAnalytics.isInitialized) {
            pmsAnalytics.get().sendPmsAnalyticsEvent(event)
        }
    }

    companion object {
        const val REQUEST_CODE_CHANGE_BANK_ACCOUNT = 1
        const val REQUEST_CODE_UPLOAD_PROOF = 2
        const val REQUEST_CODE_CHANGE_BCA_ID = 3
    }
}