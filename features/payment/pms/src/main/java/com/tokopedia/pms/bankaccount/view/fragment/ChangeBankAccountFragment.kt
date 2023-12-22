package com.tokopedia.pms.bankaccount.view.fragment

import android.app.Activity
import android.os.Bundle
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.pms.analytics.PmsAnalytics
import com.tokopedia.pms.analytics.PmsEvents
import com.tokopedia.pms.bankaccount.data.model.BankListModel
import com.tokopedia.pms.bankaccount.domain.BankListDataUseCase
import com.tokopedia.pms.bankaccount.view.activity.ChangeBankAccountActivity.Companion.PAYMENT_LIST_MODEL_EXTRA
import com.tokopedia.pms.bankaccount.view.activity.ChangeBankListener
import com.tokopedia.pms.bankaccount.viewmodel.ChangeBankAccountViewModel
import com.tokopedia.pms.databinding.FragmentChangeBankAccountBinding
import com.tokopedia.pms.paymentlist.di.PmsComponent
import com.tokopedia.pms.paymentlist.domain.data.BankTransferPaymentModel
import com.tokopedia.pms.paymentlist.domain.data.BasePaymentModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifycomponents.R as unifycomponentsR

/**
 * Created by zulfikarrahman on 6/25/18.
 */
class ChangeBankAccountFragment : BaseDaggerFragment() {
    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var bankListDataUseCase: BankListDataUseCase

    @Inject
    lateinit var pmsAnalytics: dagger.Lazy<PmsAnalytics>

    private var binding by autoClearedNullable<FragmentChangeBankAccountBinding>()

    private val viewModel: ChangeBankAccountViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(ChangeBankAccountViewModel::class.java)
    }

    private var paymentListModel: BankTransferPaymentModel? = null
    private var loaderDialog: LoaderDialog? = null
    private lateinit var bankList: List<BankListModel>

    override fun getScreenName() = null

    override fun initInjector() =
        getComponent(PmsComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        arguments?.let {
            val model: BasePaymentModel? = it.getParcelable(PAYMENT_LIST_MODEL_EXTRA)
            if (model != null && model is BankTransferPaymentModel) paymentListModel = model
        }
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChangeBankAccountBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (paymentListModel != null) {
            bankList = bankListDataUseCase.bankList
            initializeTextFields()
        }
        binding?.buttonUse?.setOnClickListener(
            View.OnClickListener {
                changeBankAccountDetails()
            }
        )
        viewModel.saveDetailLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onResultEditDetailAccount(it.data.isSuccess, it.data.message)
                    is Fail -> onErrorEditDetailAccount(it.throwable)
                }
            }
        )
    }

    private fun initializeTextFields() {
        binding?.run {
            inputDestBankAccount.textAreaInput.apply {
                isClickable = false
                isFocusable = false
                isSingleLine = true
                setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    getDimens(unifycomponentsR.dimen.unify_font_16).toFloat()
                )
                setOnClickListener { openBankListForSelection() }
            }

            inputNoteOptional.textAreaInput.minLines = 5
            inputNoteOptional.textAreaInput.gravity = Gravity.TOP

            inputAccountNumber.textFieldInput.setText(paymentListModel!!.senderBankInfo.accountNumber)
            inputAccountName.textFieldInput.setText(paymentListModel!!.senderBankInfo.accountName)

            val bankName =
                bankListDataUseCase.getBankNameFromBankId(paymentListModel!!.senderBankInfo.bankId)
            inputDestBankAccount.textAreaInput.setText(bankName)
        }
    }

    private fun openBankListForSelection() {
        (activity as ChangeBankListener).openDestinationBankBottomSheet()
    }

    fun onBankSelected(bankListModel: BankListModel) {
        binding?.inputDestBankAccount?.textAreaInput?.setText(bankListModel.bankName)
    }

    fun changeBankAccountDetails() {
        pmsAnalytics.get().sendPmsAnalyticsEvent(PmsEvents.ConfirmAccountDetailsEvent(10))
        showLoadingDialog()
        binding?.run {
            viewModel.saveDetailAccount(
                transactionId = paymentListModel?.transactionId ?: "",
                merchantCode = paymentListModel?.merchantCode ?: "",
                accountName = inputAccountName.getEditableValue().toString(),
                accountNumber = inputAccountNumber.getEditableValue().toString(),
                notes = inputNoteOptional.textAreaInput.text.toString(),
                destinationBankId = bankListDataUseCase.getBankIdFromBankName(
                    inputDestBankAccount.textAreaInput.text.toString()
                )
            )
        }
    }

    private fun onErrorEditDetailAccount(e: Throwable) {
        hideLoadingDialog()
        showToaster(ErrorHandler.getErrorMessage(context, e), Toaster.TYPE_ERROR)
    }

    private fun onResultEditDetailAccount(success: Boolean, message: String) {
        hideLoadingDialog()
        activity?.let {
            if (success) {
                showToaster(message)
                it.setResult(Activity.RESULT_OK)
                it.finish()
            } else {
                showToaster(message, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun showToaster(message: String, type: Int = Toaster.TYPE_NORMAL) {
        view?.let {
            Toaster.build(
                it,
                message,
                Toaster.LENGTH_LONG,
                type
            ).show()
        }
    }

    private fun showLoadingDialog() {
        context?.let {
            loaderDialog = LoaderDialog(it)
            loaderDialog?.dialog?.setOverlayClose(false)
            loaderDialog?.show()
        }
    }

    private fun hideLoadingDialog() {
        loaderDialog?.dialog?.dismiss()
        loaderDialog = null
    }

    companion object {

        fun createInstance(paymentListModel: BasePaymentModel?): Fragment {
            val changeBankAccountFragment = ChangeBankAccountFragment()
            val bundle = Bundle()
            bundle.putParcelable(PAYMENT_LIST_MODEL_EXTRA, paymentListModel)
            changeBankAccountFragment.arguments = bundle
            return changeBankAccountFragment
        }
    }
}
