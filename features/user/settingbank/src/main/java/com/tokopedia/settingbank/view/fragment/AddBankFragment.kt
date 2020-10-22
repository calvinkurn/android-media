package com.tokopedia.settingbank.view.fragment

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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.di.SettingBankComponent
import com.tokopedia.settingbank.domain.*
import com.tokopedia.settingbank.view.activity.AddBankActivity
import com.tokopedia.settingbank.view.viewModel.*
import com.tokopedia.settingbank.view.viewState.*
import com.tokopedia.settingbank.view.widgets.BankTNCBottomSheet
import com.tokopedia.settingbank.view.widgets.CloseableBottomSheetFragment
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
    lateinit var bankSettingAnalytics: BankSettingAnalytics

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

    var isFragmentRestored: Boolean = false

    lateinit var bank: Bank

    override fun initInjector() {
        getComponent(SettingBankComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            if (it.containsKey(AddBankActivity.ARG_BANK_DATA))
                arguments?.getParcelable<Bank>(AddBankActivity.ARG_BANK_DATA)?.let { bank ->
                    this.bank = bank
                }
        }
        initViewModels()
        savedInstanceState?.let {
            restoreBuilderAndBank(it)
        }
    }

    private fun restoreBuilderAndBank(bundle: Bundle) {
        if (bundle.containsKey(ARG_OUT_BANK)) {
            isFragmentRestored = true
            bundle.getParcelable<Bank>(ARG_OUT_BANK)?.let { bank ->
                this.bank = bank
            }
            if (bundle.containsKey(ARG_OUT_ACCOUNT_NUMBER)) {
                bundle.getString(ARG_OUT_ACCOUNT_NUMBER)?.let { number ->
                    this.builder.setAccountNumber(number)
                }
                if (bundle.containsKey(ARG_OUT_ACCOUNT_HOLDER_NAME)) {
                    bundle.getString(ARG_OUT_ACCOUNT_HOLDER_NAME)?.let { accHolderName ->
                        val isManual = bundle.getBoolean(ARG_OUT_ACCOUNT_NAME_IS_MANUAL, false)
                        this.builder.setAccountName(accHolderName, isManual)
                    }
                }
            }
        }
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        tNCViewModel = viewModelProvider.get(SettingBankTNCViewModel::class.java)
        textWatcherViewModel = viewModelProvider.get(BankNumberTextWatcherViewModel::class.java)
        checkAccountNumberViewModel = viewModelProvider.get(CheckAccountNumberViewModel::class.java)
        accountHolderNameViewModel = viewModelProvider.get(AccountHolderNameViewModel::class.java)
        addAccountViewModel = viewModelProvider.get(AddAccountViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_bank_v2, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDownArrowBankName()
        setRestoredFragmentState()
        etBankAccountNumber.addTextChangedListener(textWatcherViewModel.getTextWatcher())
        setTncText()
        startObservingViewModels()
        setBankName()
        etBankName.setOnClickListener { openBankListForSelection() }
        btnPeriksa.setOnClickListener { checkAccountNumber() }
        add_account_button.setOnClickListener { onClickAddBankAccount() }
        setAccountNumberInputFilter()
        if (::bank.isInitialized) {
            textWatcherViewModel.onBankSelected(bank)
        } else {
            openBankListForSelection()
        }
    }

    private fun setRestoredFragmentState() {
        if (isFragmentRestored) {
            unregisterObserver()
            builder.getAccountName()?.let {
                if (it.isNotBlank())
                    etBankAccountNumber.setText(it)
            }
            builder.getAccountName()?.let {
                if (it.isNotBlank()) {
                    if (builder.isManual()) {
                        etManualAccountHolderName.setText(it)
                        wrapperManualAccountHolderName.visible()
                        groupAccountNameAuto.gone()
                    } else {
                        tvAccountHolderName.text = it
                        groupAccountNameAuto.visible()
                        wrapperManualAccountHolderName.gone()

                    }
                    btnPeriksa.isEnabled = false
                } else {
                    isFragmentRestored = false
                }
            } ?: kotlin.run {
                isFragmentRestored = false
            }
        }
    }

    private fun unregisterObserver() {
        tNCViewModel.tncPopUpTemplate.removeObservers(this)
        textWatcherViewModel.textWatcherState.removeObservers(this)
        checkAccountNumberViewModel.accountCheckState.removeObservers(this)
        accountHolderNameViewModel.textWatcherState.removeObservers(this)
        addAccountViewModel.addAccountState.removeObservers(this)
    }

    private fun setDownArrowBankName() {
        etBankName.setCompoundDrawablesWithIntrinsicBounds(null, null,
                ContextCompat.getDrawable(context!!, com.tokopedia.design.R.drawable.ic_arrow_down_grey), null)
    }

    private fun startObservingViewModels() {
        tNCViewModel.tncPopUpTemplate.observe(this, Observer {
            when (it) {
                is OnTNCSuccess -> openTNCBottomSheet(it.templateData)
                is OnTNCError -> showErrorOnUI(it.throwable, null)
                null -> {
                }
            }

        })

        textWatcherViewModel.textWatcherState.observe(this, Observer {
            if (isFragmentRestored) {
                isFragmentRestored = false
                return@Observer
            }
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
                is OnCheckAccountError -> showErrorOnUI(it.throwable, null)
                is OnAccountCheckSuccess -> {
                    onAccountCheckSuccess(it.accountHolderName)
                }
                is OnErrorInAccountNumber -> {
                    setAccountNumberError(it.errorMessage)
                }
            }
        })

        accountHolderNameViewModel.textWatcherState.observe(this, Observer {
            when (it) {
                is OnAccountNameError -> showManualAccountNameError(it.error)
                is OnAccountNameValidated -> {
                    builder.setAccountName(it.name, true)
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
                    showErrorOnUI(it.throwable) { requestAddBankAccount() }
                }

            }
        })
    }

    private fun showErrorOnUI(throwable: Throwable, retry: (() -> Unit)?) {
        context?.let { context ->
            view?.let { view ->
                retry?.let {
                    Toaster.make(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi), View.OnClickListener { retry.invoke() })
                } ?: run {
                    Toaster.make(view, SettingBankErrorHandler.getErrorMessage(context, throwable),
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
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
        try {
            val request = builder.build()
            if (request.isManual) {
                bankSettingAnalytics.eventOnManualNameSimpanClick()
                accountHolderNameViewModel.onValidateAccountName(etManualAccountHolderName.text.toString())
            } else {
                bankSettingAnalytics.eventOnAutoNameSimpanClick()
                openPinVerification()
            }
        }catch (e : Exception){}
    }

    private fun checkAccountNumber() {
        if (::bank.isInitialized) {
            bankSettingAnalytics.eventOnPericsaButtonClick()
            checkAccountNumberViewModel.checkAccountNumber(bank.bankID, etBankAccountNumber.text.toString())
        } else {
            openBankListForSelection()
        }
    }

    private fun openBankListForSelection() {
        bankListBottomSheet = CloseableBottomSheetFragment.newInstance(SelectBankFragment(),
                true,
                getString(R.string.sbank_choose_a_bank),
                null,
                CloseableBottomSheetFragment.STATE_FULL)
        bankListBottomSheet.show(activity!!.supportFragmentManager, "")
    }

    private fun showManualAccountNameError(error: String?) {
        wrapperManualAccountHolderName.error = error
    }

    private fun onTextChange(onTextChanged: OnTextChanged) {
        btnPeriksa.isEnabled = onTextChanged.isCheckEnable
        add_account_button.isEnabled = onTextChanged.isAddBankButtonEnable
        if (onTextChanged.clearAccountHolderName) {
            builder.setAccountNumber(etBankAccountNumber.text.toString())
            groupAccountNameAuto.gone()
            tvAccountHolderName.text = ""
            btnPeriksa.isEnabled = onTextChanged.isCheckEnable
            add_account_button.isEnabled = onTextChanged.isAddBankButtonEnable
            etManualAccountHolderName.setText("")
            wrapperManualAccountHolderName.error = null
            wrapperManualAccountHolderName.gone()
        }
    }

    private fun onAccountCheckSuccess(accountHolderName: String?) {
        btnPeriksa.isEnabled = false
        add_account_button.isEnabled = true
        setAccountNumberError(null)
        accountHolderName?.let {
            if (it.isEmpty()) {
                builder.isManual(true)
                openManualNameEntryView()
            } else {
                builder.isManual(false)
                builder.setAccountName(accountHolderName, false)
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
        tvAddBankTnc.highlightColor = resources.getColor(android.R.color.transparent);
        tvAddBankTnc.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun createTermsAndConditionSpannable(): SpannableStringBuilder {
        val originalText = getString(R.string.sbank_add_bank_tnc)
        val readMoreText = getString(R.string.sbank_add_bank_tnc_clickable_part)
        val spannableString = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = this.resources.getColor(com.tokopedia.design.R.color.tkpd_main_green)
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
        bankSettingAnalytics.eventOnTermsAndConditionClick()
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
        notifyAccountNumberWatcher()
        hideAccountHolderName()
        setBankName()
        setAccountNumberInputFilter()
    }

    private fun setAccountNumberInputFilter() {
        if (::bank.isInitialized) {
            val abbreviation = bank.abbreviation?.let { it } ?: ""
            val bankAccountNumberCount = textWatcherViewModel.getBankTypeFromAbbreviation(abbreviation)
            val filterArray = arrayOfNulls<InputFilter>(1)
            filterArray[0] = InputFilter.LengthFilter(bankAccountNumberCount.count)
            etBankAccountNumber.filters = filterArray
        }
    }

    private fun notifyAccountNumberWatcher() {
        etBankAccountNumber.setText("")
    }

    private fun hideAccountHolderName() {
        etManualAccountHolderName.setText("")
        wrapperManualAccountHolderName.gone()
        groupAccountNameAuto.gone()
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
        (dialogView.findViewById(R.id.heading) as TextView).text = getString(R.string.sbank_confirm_add_bank_account)
        val description = context?.resources?.getString(R.string.sbank_add_bank_confirm, bank.abbreviation,
                addBankRequest.accountNo, addBankRequest.accountName)
        (dialogView.findViewById(R.id.description) as TextView).text = description
        dialogView.findViewById<View>(R.id.continue_btn).setOnClickListener {
            bankSettingAnalytics.eventDialogConfirmAddAccountClick()
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
        try {
            addAccountViewModel.addBank(builder.build())
        } catch (e: Exception) {
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        if (::bank.isInitialized) {
            outState.putParcelable(ARG_OUT_BANK, bank)
            if (!builder.getAccountNumber().isNullOrBlank()) {
                outState.putString(ARG_OUT_ACCOUNT_NUMBER, builder.getAccountNumber())
                if (!builder.getAccountName().isNullOrBlank()) {
                    outState.putString(ARG_OUT_ACCOUNT_HOLDER_NAME, builder.getAccountName())
                    outState.putBoolean(ARG_OUT_ACCOUNT_NAME_IS_MANUAL, builder.isManual())
                }
            }
        }
    }

    companion object {
        const val ARG_OUT_BANK = "ARG_OUT_BANK"
        const val ARG_OUT_ACCOUNT_NUMBER = "ARG_OUT_ACCOUNT_NUMBER"
        const val ARG_OUT_ACCOUNT_HOLDER_NAME = "ARG_OUT_ACCOUNT_HOLDER_NAME"
        const val ARG_OUT_ACCOUNT_NAME_IS_MANUAL = "ARG_OUT_ACCOUNT_NAME_IS_MANUAL"
    }

}
