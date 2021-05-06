package com.tokopedia.payment.setting.detail.view.fragment

import android.app.Activity
import android.app.ProgressDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.graphql.data.GraphqlClient
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.payment.setting.R
import com.tokopedia.payment.setting.detail.model.DataResponseDeleteCC
import com.tokopedia.payment.setting.detail.view.viewmodel.DetailCreditCardViewModel
import com.tokopedia.payment.setting.di.SettingPaymentComponent
import com.tokopedia.payment.setting.list.model.SettingListPaymentModel
import com.tokopedia.payment.setting.util.getExpiredDate
import com.tokopedia.payment.setting.util.getSpacedTextPayment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_credit_card_detail.*
import kotlinx.android.synthetic.main.fragment_credit_card_detail.view.*
import javax.inject.Inject

class DetailCreditCardFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val viewModel: DetailCreditCardViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(DetailCreditCardViewModel::class.java)
    }
    private var settingListPaymentModel: SettingListPaymentModel? = null

    private val progressDialog: ProgressDialog by lazy { ProgressDialog(context) }

    override fun initInjector() {
        getComponent(SettingPaymentComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        activity?.run { GraphqlClient.init(this) }
        settingListPaymentModel = arguments?.getParcelable(EXTRA_PAYMENT_MODEL)
    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
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
        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.creditCardDeleteResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onDeleteCCSuccess(it.data)
                is Fail -> onDeleteCCError(it.throwable)
            }
        })
    }

    private fun onDeleteCCSuccess(data: DataResponseDeleteCC) {
        hideProgressDialog()
        onDeleteCCResult(data.removeCreditCard?.isSuccess, data.removeCreditCard?.message, viewModel.tokenId)
    }

    private fun onDeleteCCError(throwable: Throwable) {
        hideProgressDialog()
        onErrorDeleteCC(throwable, viewModel.tokenId)
    }

    private fun showDeleteCcDialog() {
        context?.let {
            val creditCardDialog = DialogUnify(it, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE)
            creditCardDialog.apply {
                setTitle(getString(R.string.payment_title_delete_credit_card))
                setDescription(getString(R.string.payment_label_forever_delete_credit_card))
                setPrimaryCTAText(getString(R.string.payment_label_yes))
                setPrimaryCTAClickListener {
                    onConfirmDelete(settingListPaymentModel?.tokenId ?: "")
                }
                setSecondaryCTAText(getString(R.string.payment_label_no))
                setSecondaryCTAClickListener { dismiss() }
                show()
            }
        }
    }

    private fun showProgressDialog() {
        progressDialog.show()
    }

    private fun hideProgressDialog() {
        progressDialog.hide()
    }

    fun onConfirmDelete(tokenId: String?) {
        tokenId?.let {
            showProgressDialog()
            viewModel.deleteCreditCard(tokenId)
        }
    }

    private fun onDeleteCCResult(success: Boolean?, message: String?, tokenId: String?) {
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

    private fun onErrorDeleteCC(e: Throwable, tokenId: String?) {
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