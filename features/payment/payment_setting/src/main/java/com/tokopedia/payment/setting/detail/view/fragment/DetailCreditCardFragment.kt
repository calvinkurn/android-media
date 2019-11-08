package com.tokopedia.payment.setting.detail.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.detail.di.DaggerDetailCreditCardComponent
import com.tokopedia.payment.setting.detail.di.DetailCreditCardModule
import com.tokopedia.payment.setting.detail.view.presenter.DetailCreditCardContract
import com.tokopedia.payment.setting.detail.view.presenter.DetailCreditCardPresenter
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.*
import kotlinx.android.synthetic.main.fragment_credit_card_detail.*
import kotlinx.android.synthetic.main.fragment_credit_card_detail.view.*
import javax.inject.Inject

class DetailCreditCardFragment : BaseDaggerFragment(), DetailCreditCardContract.View, DeleteCreditCardDialogPayment.DeleteCreditCardDialogListener {

    var settingListPaymentModel: SettingListPaymentModel? = null
    @Inject
    lateinit var detailCreditCardPresenter: DetailCreditCardPresenter
    val progressDialog : ProgressDialog by lazy { ProgressDialog(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run { GraphqlClient.init(this) }
        settingListPaymentModel = arguments?.getParcelable(EXTRA_PAYMENT_MODEL)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_credit_card_detail, container, false)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        settingListPaymentModel?.run {
            ImageHandler.LoadImage(view.imageCCBackground, this.backgroundImage)
            creditCardNumber.setText(this.maskedNumber?.getSpacedTextPayment())
            creditCardExpiryText.setText(this.getExpiredDate())
            buttonDeleteCC.setOnClickListener { showDeleteCcDialog() }
        }
    }

    fun showDeleteCcDialog() {
        fragmentManager?.run {
            val creditCardDialog = DeleteCreditCardDialogPayment.newInstance(
                    settingListPaymentModel?.tokenId ?: "",
                    settingListPaymentModel?.maskedNumber ?: "")
            creditCardDialog.setListener(this@DetailCreditCardFragment)
            creditCardDialog.show(this,
                    "")
        }
    }

    override fun showProgressDialog() {
        progressDialog.show()
    }

    override fun hideProgressDialog() {
        progressDialog.hide()
    }

    override fun onConfirmDelete(tokenId: String?) {
        detailCreditCardPresenter.deleteCreditCard(tokenId, resources)
    }

    override fun onDeleteCCResult(success: Boolean?, message: String?, tokenId: String?) {
        if(success?:false){
            NetworkErrorHelper.showGreenSnackbar(activity, message)
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        }else{
            showErrorSnackbar(message, tokenId)
        }
    }

    private fun showErrorSnackbar(message: String?, tokenId: String?) {
        NetworkErrorHelper.createSnackbarRedWithAction(activity, message, { onConfirmDelete(tokenId) }).showRetrySnackbar()
    }

    override fun onErrorDeleteCC(e: Throwable, tokenId: String?) {
        showErrorSnackbar(ErrorHandler.getErrorMessage(activity, e), tokenId)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        DaggerDetailCreditCardComponent.builder()
                .baseAppComponent((activity?.application as BaseMainApplication).baseAppComponent)
                .detailCreditCardModule(DetailCreditCardModule())
                .build()
                .inject(this)
        detailCreditCardPresenter.attachView(this)
    }

    override fun onDestroy() {
        detailCreditCardPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        val EXTRA_PAYMENT_MODEL = "EXTRA_PAYMENT_MODEL"

        fun createInstance(settingListPaymentModel: SettingListPaymentModel?) : DetailCreditCardFragment {
            val detailCreditCardFragment = DetailCreditCardFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PAYMENT_MODEL, settingListPaymentModel)
            detailCreditCardFragment.arguments = bundle
            return detailCreditCardFragment
        }
    }

}