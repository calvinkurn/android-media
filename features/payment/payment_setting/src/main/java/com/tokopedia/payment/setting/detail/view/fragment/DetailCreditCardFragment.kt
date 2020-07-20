package com.tokopedia.payment.setting.detail.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.detail.di.DetailCreditCardComponent
import com.tokopedia.payment.setting.detail.view.presenter.DetailCreditCardContract
import com.tokopedia.payment.setting.detail.view.presenter.DetailCreditCardPresenter
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.getExpiredDate
import com.tokopedia.payment.setting.util.getSpacedTextPayment
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_credit_card_detail.*
import kotlinx.android.synthetic.main.fragment_credit_card_detail.view.*
import javax.inject.Inject

class DetailCreditCardFragment : BaseDaggerFragment(), DetailCreditCardContract.View,
        DeleteCreditCardDialogPayment.DeleteCreditCardDialogListener {

    private var settingListPaymentModel: SettingListPaymentModel? = null

    @Inject
    lateinit var detailCreditCardPresenter: DetailCreditCardPresenter

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run { GraphqlClient.init(this) }
        settingListPaymentModel = arguments?.getParcelable(EXTRA_PAYMENT_MODEL)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_credit_card_detail,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progressDialog.setMessage(getString(com.tokopedia.abstraction.R.string.title_loading))
        settingListPaymentModel?.run {
            ImageHandler.LoadImage(view.imageCCBackground, this.backgroundImage)
            creditCardNumber.text = this.maskedNumber?.getSpacedTextPayment()
            creditCardExpiryText.text = this.getExpiredDate()
            buttonDeleteCC.setOnClickListener { showDeleteCcDialog() }
        }
    }

    private fun showDeleteCcDialog() {
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
        tokenId?.let {
            detailCreditCardPresenter.deleteCreditCard(tokenId, resources)
        }
    }

    override fun onDeleteCCResult(success: Boolean?, message: String?, tokenId: String?) {
        if (success == true) {
            message?.let {
                onSuccessFullyDeleted(message)
            }
            activity?.setResult(Activity.RESULT_OK)
            activity?.finish()
        } else {
            message?.let {
                view?.let { view ->
                    Toaster.make(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun onSuccessFullyDeleted(message: String) {
        view?.let { view ->
            Toaster.make(view, message, Toaster.LENGTH_SHORT)
        }
    }

    override fun onErrorDeleteCC(e: Throwable, tokenId: String?) {
        context?.let { context ->
            val errorMessage = ErrorHandler.getErrorMessage(context, e)
            view?.let { view ->
                Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                        getString(R.string.payment_label_coba_lagi), View.OnClickListener {
                    onConfirmDelete(tokenId)
                })
            }
        }
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(DetailCreditCardComponent::class.java).inject(this)
        detailCreditCardPresenter.attachView(this)
    }

    override fun onDestroy() {
        detailCreditCardPresenter.detachView()
        super.onDestroy()
    }

    companion object {
        const val EXTRA_PAYMENT_MODEL = "EXTRA_PAYMENT_MODEL"

        fun createInstance(settingListPaymentModel: SettingListPaymentModel?): DetailCreditCardFragment {
            val detailCreditCardFragment = DetailCreditCardFragment()
            val bundle = Bundle()
            bundle.putParcelable(EXTRA_PAYMENT_MODEL, settingListPaymentModel)
            detailCreditCardFragment.arguments = bundle
            return detailCreditCardFragment
        }
    }

}