package com.tokopedia.settingbank.banklist.v2.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.AddBankRequest
import com.tokopedia.settingbank.banklist.v2.domain.AddBankResponse
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.domain.TemplateData
import com.tokopedia.settingbank.banklist.v2.view.activity.ARG_BANK_DATA
import com.tokopedia.settingbank.banklist.v2.view.viewModel.*
import com.tokopedia.settingbank.banklist.v2.view.viewState.*
import com.tokopedia.settingbank.banklist.v2.view.widgets.BankTNCBottomSheet
import com.tokopedia.settingbank.banklist.v2.view.widgets.CloseableBottomSheetFragment
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_bank_v2.*
import javax.inject.Inject


class AddBankFragment : BaseDaggerFragment() {


    private val REQUEST_OTP: Int = 103

    override fun getScreenName(): String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private lateinit var tNCViewModel: SettingBankTNCViewModel
    private lateinit var checkAccountNumberViewModel: CheckAccountNumberViewModel
    private lateinit var textWatcherViewModel: BankNumberTextWatcherViewModel
    private lateinit var accountHolderNameViewModel: AccountHolderNameViewModel
    private lateinit var addAccountViewModel: AddAccountViewModel

    private lateinit var tncBottomSheet: BankTNCBottomSheet
    private lateinit var bankListBottomSheet: CloseableBottomSheetFragment
    private lateinit var confirmationDialog: AlertDialog

    val builder: AddBankRequest.Builder = AddBankRequest.Builder()

    lateinit var bank: Bank

    override fun initInjector() {
        getComponent(SettingBankComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(ARG_BANK_DATA))
                arguments?.getParcelable<Bank>(ARG_BANK_DATA)?.let { bank ->
                    this.bank = bank
                }
        }
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        tNCViewModel = viewModelProvider.get(SettingBankTNCViewModel::class.java)
        textWatcherViewModel = viewModelProvider.get(BankNumberTextWatcherViewModel::class.java)
        checkAccountNumberViewModel = viewModelProvider.get(CheckAccountNumberViewModel::class.java)
        accountHolderNameViewModel = viewModelProvider.get(AccountHolderNameViewModel::class.java)
        addAccountViewModel = viewModelProvider.get(AddAccountViewModel::class.java)
        if (::bank.isInitialized)
            textWatcherViewModel.onBankSelected(bank)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_bank_v2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setTncText()
        startObservingViewModels()
        setBankName()
        etBankName.setOnClickListener { openBankListForSelection() }
        btnPeriksa.setOnClickListener { checkAccountNumber() }
        add_account_button.setOnClickListener { onClickAddBankAccount() }
        etBankAccountNumber.addTextChangedListener(textWatcherViewModel.getTextWatcher())
    }


    private fun startObservingViewModels() {
        tNCViewModel.tncPopUpTemplate.observe(this, Observer {
            openTNCBottomSheet(it)
        })

        textWatcherViewModel.textWatcherState.observe(this, Observer {
            when (it) {
                is OnNOBankSelected -> setAccountNumberError(getString(R.string.sbank_select_bank))
                is OnTextChanged -> {
                    setAccountNumberError(null)
                    onTextChange(it)
                }
            }
        })

        checkAccountNumberViewModel.accountCheckState.observe(this, Observer {
            when (it) {
                is OnNetworkError -> {

                }
                is OnAccountCheckSuccess -> onAccountCheckSuccess(it.accountHolderName)
                is OnErrorInAccountNumber -> {
                    setAccountNumberError(it.errorMessage)
                }
            }
        })

        accountHolderNameViewModel.textWatcherState.observe(this, Observer {
            when (it) {
                is OnAccountNameError -> showManualAccountNameError(it.error)
                is OnAccountNameValidated -> {
                    builder.accountName(it.name, true)
                    showManualAccountNameError(null)
                    openConfirmationPopUp()
                }
            }
        })

        addAccountViewModel.addAccountState.observe(this, Observer {
            when (it) {
                is OnAddBankRequestStarted -> {
                    progress_bar.visible()
                }
                is OnAddBankRequestEnded -> {
                    progress_bar.gone()
                }
                is OnSuccessfullyAdded -> {
                    onBankSuccessfullyAdded(it.addBankResponse)
                }
                is OnAccountAddingError -> {
                    showErrorOnUI(it.message) { requestAddBankAccount() }
                }

            }
        })
    }

    private fun showErrorOnUI(errorMessage: String?, retry: (() -> Unit)?) {
        errorMessage?.let {
            view?.let { view ->
                retry?.let {
                    Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi), View.OnClickListener { retry.invoke() })
                } ?: run {
                    Toaster.make(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
                }

            }
        }
    }

    private fun onBankSuccessfullyAdded(addBankResponse: AddBankResponse) {
        val intent = Intent()
        val bundle = Bundle()
        val data = addBankResponse.data
        bundle.putString(ApplinkConstInternalGlobal.PARAM_ACCOUNT_ID, data.accountId.toString())
        bundle.putString(ApplinkConstInternalGlobal.PARAM_ACCOUNT_NAME, data.accountName)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_ACCOUNT_NO, data.accountNumber)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_BANK_ID, data.bankId.toString())
        bundle.putString(ApplinkConstInternalGlobal.PARAM_BANK_NAME, builder.build().bankName)
        intent.putExtras(bundle)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    private fun onClickAddBankAccount() {
        val request = builder.build()
        if (!request.isManual) {
            openPinVerification()
        } else {
            accountHolderNameViewModel.onValidateAccountName(etManualAccountHolderName.text.toString())
        }
    }

    private fun checkAccountNumber() {
        checkAccountNumberViewModel.checkAccountNumber(bank.bankID, etBankAccountNumber.text.toString())
    }

    private fun openBankListForSelection() {
        bankListBottomSheet = CloseableBottomSheetFragment.newInstance(SelectBankFragment(),
                true,
                getString(R.string.sbank_choose_a_bank),
                null,
                CloseableBottomSheetFragment.STATE_FULL)
        bankListBottomSheet.showNow(activity!!.supportFragmentManager, "")
    }

    private fun showManualAccountNameError(error: String?) {
        wrapperManualAccountHolderName.error = error
    }

    private fun onTextChange(onTextChanged: OnTextChanged) {
        btnPeriksa.isEnabled = onTextChanged.isCheckEnable
        add_account_button.isEnabled = onTextChanged.isAddBankButtonEnable
        if (onTextChanged.clearAccountHolderName) {
            builder.accountNumber(onTextChanged.newAccountNumber)
            groupAccountNameAuto.gone()
            tvAccountHolderName.text = ""
            btnPeriksa.isEnabled = onTextChanged.isCheckEnable
            add_account_button.isEnabled = onTextChanged.isAddBankButtonEnable
            etManualAccountHolderName.setText("")
            wrapperManualAccountHolderName.error = null
            wrapperManualAccountHolderName.gone()
        }
        if (onTextChanged.isTextUpdateRequired) {
            etBankAccountNumber.setText(onTextChanged.newAccountNumber)
            etBankAccountNumber.text?.let {
                etBankAccountNumber.setSelection(it.length)
            }
        }
    }

    private fun onAccountCheckSuccess(accountHolderName: String?) {
        btnPeriksa.isEnabled = false
        add_account_button.isEnabled = true
        accountHolderName?.let {
            if (it.isEmpty()) {
                builder.isManual(true)
                openManualNameEntryView()
            } else {
                builder.accountName(accountHolderName, false)
                tvAccountHolderName.text = accountHolderName
                wrapperManualAccountHolderName.gone()
                groupAccountNameAuto.visible()
            }
        } ?: run {
            openManualNameEntryView()
        }
    }

    private fun openManualNameEntryView() {
        groupAccountNameAuto.gone()
        wrapperManualAccountHolderName.visible()
    }

    private fun setAccountNumberError(errorStr: String?) {
        wrapperBankAccountNumber.error = errorStr
    }

    private fun setTncText() {
        val tncSpannableString = createTermsAndConditionSpannable()
        tvAddBankTnc.text = tncSpannableString
        tvAddBankTnc.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun createTermsAndConditionSpannable(): SpannableStringBuilder {
        val originalText = getString(R.string.sbank_add_bank_tnc)
        val readMoreText = getString(R.string.sbank_add_bank_tnc_clickable_part)
        val spannableString = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = this.resources.getColor(R.color.tkpd_main_green)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                loadTncForAddBank()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder.valueOf(originalText).append(" ").append(spannableString)
    }

    private fun loadTncForAddBank() {
        tNCViewModel.loadTNCPopUpTemplate()
    }

    private fun openTNCBottomSheet(templateData: TemplateData?) {
        templateData?.let {
            if (::tncBottomSheet.isInitialized) {
                tncBottomSheet.templateData = it
                activity?.let {
                    tncBottomSheet.show(templateData)
                }
            } else {
                tncBottomSheet = BankTNCBottomSheet(activity!!)
                tncBottomSheet.show(templateData)
            }
        }
    }

    fun closeBottomSheet() {
        bankListBottomSheet.dismiss()
    }

    fun onBankSelected(selectedBank: Bank) {
        bank = selectedBank
        textWatcherViewModel.onBankSelected(bank)
        setBankName()
    }

    private fun setBankName() {
        if (::bank.isInitialized) {
            builder.bank(bank.bankID, bank.bankName)
            etBankName.setText("${bank.abbreviation ?: ""} (${bank.bankName})")
        }
    }

    private fun openConfirmationPopUp() {
        val addBankRequest = builder.build()
        val dialogBuilder = AlertDialog.Builder(activity!!)
        val inflater = activity!!.layoutInflater
        val dialogView = inflater.inflate(R.layout.sbank_confirmation_dialog, null)
        (dialogView.findViewById(R.id.heading) as TextView).text = getString(R.string.sbank_add_bank_account)
        (dialogView.findViewById(R.id.description) as TextView).text = "Kamu akan menambahkan rekening ${bank?.abbreviation
                ?: ""} ${addBankRequest.accountNo} a.n ${addBankRequest.accountName}."
        dialogView.findViewById<View>(R.id.continue_btn).setOnClickListener {
            confirmationDialog.dismiss()
            openPinVerification()
        }
        dialogView.findViewById<View>(R.id.back_btn).setOnClickListener {
            if (::confirmationDialog.isInitialized)
                confirmationDialog.dismiss()
        }
        confirmationDialog = dialogBuilder.setView(dialogView).show()
    }

    private fun openPinVerification() {
        val OTP_TYPE_ADD_BANK_ACCOUNT = 12
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.email)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.phoneNumber)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_ADD_BANK_ACCOUNT)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_OTP)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_OTP -> onResultRequestOtp(resultCode)
        }
    }

    private fun onResultRequestOtp(resultCode: Int) {
        if (resultCode == Activity.RESULT_OK) {
            requestAddBankAccount()
        }
    }

    private fun requestAddBankAccount() {
        addAccountViewModel.addBank(builder.build())

    }
}
