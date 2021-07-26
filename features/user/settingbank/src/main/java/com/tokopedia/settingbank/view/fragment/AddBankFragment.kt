package com.tokopedia.settingbank.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.*
import android.text.method.DigitsKeyListener
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.getDimens
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.analytics.BankSettingAnalytics
import com.tokopedia.settingbank.di.SettingBankComponent
import com.tokopedia.settingbank.domain.model.*
import com.tokopedia.settingbank.util.AddBankAccountException
import com.tokopedia.settingbank.util.getBankTypeFromAbbreviation
import com.tokopedia.settingbank.util.textChangedListener
import com.tokopedia.settingbank.view.activity.AddBankActivity
import com.tokopedia.settingbank.view.viewModel.AddAccountViewModel
import com.tokopedia.settingbank.view.viewState.*
import com.tokopedia.settingbank.view.widgets.BankTNCBottomSheet
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.fragment_add_bank.*
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

    private lateinit var addAccountViewModel: AddAccountViewModel

    val builder: AddBankRequest.Builder = AddBankRequest.Builder()

    var isFragmentRestored: Boolean = false

    var checkAccountNameState: CheckAccountNameState? = null

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

    private fun initViewModels() {
        addAccountViewModel = ViewModelProvider(this, viewModelFactory)
                .get(AddAccountViewModel::class.java)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_bank, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRestoredFragmentState()
        initializeTextAreaField()
        setTncText()
        startObservingViewModels()
        setBankName()
        btnPeriksa.setOnClickListener { checkAccountNumber() }
        add_account_button.setOnClickListener { onClickAddBankAccount() }
        if (!::bank.isInitialized) {
            openBankListForSelection()
        }
    }

    private fun initializeTextAreaField() {
        textAreaBankName.textAreaInput.apply {
            isClickable = false
            isFocusable = false
            isSingleLine = true
            setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getDimens(com.tokopedia.unifycomponents.R.dimen.unify_font_16).toFloat())
            setOnClickListener { openBankListForSelection() }
            val paddingEndDimen = getDimens(com.tokopedia.unifycomponents.R.dimen.unify_space_32)
            setPadding(paddingLeft, paddingTop, paddingEndDimen, paddingBottom)
        }
        textAreaBankAccountNumber.textAreaInput.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getDimens(com.tokopedia.unifycomponents.R.dimen.unify_font_16).toFloat())
            inputType = InputType.TYPE_CLASS_NUMBER
            keyListener = DigitsKeyListener.getInstance(false, false)
            textChangedListener(onTextChangeExt = ::onTextChanged)
        }
        textAreaBankAccountHolderName.textAreaInput.apply {
            setTextSize(TypedValue.COMPLEX_UNIT_PX,
                    getDimens(com.tokopedia.unifycomponents.R.dimen.unify_font_16).toFloat())
            inputType = InputType.TYPE_TEXT_VARIATION_PERSON_NAME
            textAreaBankAccountHolderName.textAreaInput.filters = getAlphabetOnlyInputFilter()
        }
        setAccountNumberInputFilter()

    }

    private fun setRestoredFragmentState() {
        if (isFragmentRestored) {
            unregisterObserver()
            builder.getAccountName()?.let {
                if (it.isNotBlank())
                    textAreaBankAccountNumber.textAreaInput.setText(it)
            }
            builder.getAccountName()?.let {
                if (it.isNotBlank()) {
                    if (builder.isManual()) {
                        textAreaBankAccountHolderName.textAreaInput.setText(it)
                        textAreaBankAccountHolderName.visible()
                        groupAccountNameAuto.gone()
                    } else {
                        tvAccountHolderName.text = it
                        groupAccountNameAuto.visible()
                        textAreaBankAccountHolderName.gone()
                    }
                    setPeriksaButtonState(false)
                } else {
                    isFragmentRestored = false
                }
            } ?: kotlin.run {
                isFragmentRestored = false
            }
        }
    }

    private fun unregisterObserver() {
        addAccountViewModel.apply {
            addBankAccountLiveData.removeObservers(viewLifecycleOwner)
            termsAndConditionLiveData.removeObservers(viewLifecycleOwner)
            validateAccountNumberStateLiveData.removeObservers(viewLifecycleOwner)
            checkAccountDataLiveData.removeObservers(viewLifecycleOwner)
            accountNameValidationResult.removeObservers(viewLifecycleOwner)
        }
    }

    private fun startObservingViewModels() {
        addAccountViewModel.validateAccountNumberStateLiveData.observe(viewLifecycleOwner, Observer {
            if (isFragmentRestored) {
                isFragmentRestored = false
                return@Observer
            }
            when (it) {
                is OnNOBankSelected -> {
                    hideAccountHolderNameUI()
                    setAccountNumberError(getString(R.string.sbank_select_bank))
                }
                is ValidateAccountNumberSuccess -> {
                    setAccountNumberError(null)
                    onValidateAccountNumber(it)
                }
            }
        })

        addAccountViewModel.addBankAccountLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    onBankSuccessfullyAdded(it.data)
                }
                is Fail -> {
                    showError(it.throwable) { requestAddBankAccount() }
                }
            }
            progress_bar.gone()
        })

        addAccountViewModel.termsAndConditionLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    openTNCBottomSheet(it.data)
                }
                is Fail -> {
                    showError(it.throwable, null)
                }
            }
        })

        addAccountViewModel.checkAccountDataLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success ->
                    handleCheckAccountState(it.data)
                is Fail -> showError(it.throwable, null)
            }
        })
    }

    private fun handleCheckAccountState(data: CheckAccountNameState) {
        setPeriksaButtonState(false)
        this.checkAccountNameState = data
        when (data) {
            is AccountNameFinalValidationSuccess -> {
                onAccountNumberAndNameValidationSuccess(data)
            }

            is EditableAccountName -> {
                onEditableAccountFound(data)
            }

            is AccountNameCheckError -> {
                onAccountNumberCheckError(data)
            }
        }
    }

    private fun onAccountNumberAndNameValidationSuccess(data: AccountNameFinalValidationSuccess) {
        add_account_button.isEnabled = true
        showManualAccountNameError(null)
        if (data.checkAccountAction == ActionValidateAccountName) {
            builder.setAccountName(data.accountHolderName, true)
            openConfirmationPopUp()
        } else {
            builder.setAccountName(data.accountHolderName, false)
            tvAccountHolderName.text = data.accountHolderName
            textAreaBankAccountHolderName.gone()
            groupAccountNameAuto.visible()
        }
    }

    private fun onEditableAccountFound(data: EditableAccountName) {
        builder.isManual(true)
        add_account_button.isEnabled = true
        groupAccountNameAuto.gone()
        textAreaBankAccountHolderName.visible()
        textAreaBankAccountHolderName.textAreaInput.isEnabled = true
        if (data.accountName.isNotEmpty())
            textAreaBankAccountHolderName.textAreaInput.setText(data.accountName)
        showManualAccountNameError(data.message)
    }

    private fun onAccountNumberCheckError(data: AccountNameCheckError) {
        add_account_button.isEnabled = false
        if (data.accountName.isEmpty()) {
            setAccountNumberError(data.message)
        } else {
            groupAccountNameAuto.gone()
            textAreaBankAccountHolderName.visible()
            textAreaBankAccountHolderName.textAreaInput.setText(data.accountName)
            textAreaBankAccountHolderName.textAreaInput.isEnabled = false
            showManualAccountNameError(data.message)
        }
    }

    private fun checkAccountNumber() {
        if (::bank.isInitialized) {
            if (textAreaBankAccountNumber.textAreaInput.text != null) {
                bankSettingAnalytics.eventOnPericsaButtonClick()
                addAccountViewModel.checkAccountNumber(bank.bankID,
                        textAreaBankAccountNumber.textAreaInput.text.toString())
            }
        } else {
            openBankListForSelection()
        }
    }

    private fun openBankListForSelection() {
        activity?.let {
            SelectBankFragment.showChooseBankBottomSheet(it, it.supportFragmentManager)
        }
    }

    private fun showManualAccountNameError(error: String?) {
        textAreaBankAccountHolderName.textAreaWrapper.error = error
    }

    private fun onValidateAccountNumber(onTextChanged: ValidateAccountNumberSuccess) {
        setPeriksaButtonState(onTextChanged.isCheckEnable)
        add_account_button.isEnabled = onTextChanged.isAddBankButtonEnable
        builder.setAccountNumber(textAreaBankAccountNumber.textAreaInput.text.toString())
        setPeriksaButtonState(onTextChanged.isCheckEnable)
        add_account_button.isEnabled = onTextChanged.isAddBankButtonEnable
        hideAccountHolderNameUI()
    }

    private fun setAccountNumberError(errorStr: String?) {
        textAreaBankAccountNumber.textAreaWrapper.error = errorStr
    }

    private fun hideAccountHolderNameUI() {
        tvAccountHolderName.text = ""
        textAreaBankAccountHolderName.textAreaInput.setText("")
        showManualAccountNameError(null)
        textAreaBankAccountHolderName.gone()
        groupAccountNameAuto.gone()
    }

    fun onBankSelected(selectedBank: Bank) {
        bank = selectedBank
        this.checkAccountNameState = null
        setBankName()
        textAreaBankAccountNumber.textAreaInput.setText("")
        setAccountNumberInputFilter()
        hideAccountHolderNameUI()
    }

    private fun setAccountNumberInputFilter() {
        if (::bank.isInitialized) {
            val abbreviation = bank.abbreviation ?: ""
            val bankAccountNumberCount = getBankTypeFromAbbreviation(abbreviation)
            val filterArray = arrayOfNulls<InputFilter>(1)
            filterArray[0] = InputFilter.LengthFilter(bankAccountNumberCount.count)
            textAreaBankAccountNumber.textAreaInput.filters = filterArray
        }
    }

    private fun setBankName() {
        if (::bank.isInitialized) {
            builder.bank(bank.bankID, bank.bankName)
            textAreaBankName.textAreaInput.setText("${bank.abbreviation ?: ""} (${bank.bankName})")
        }
    }

    private fun onClickAddBankAccount() {
        if (!::bank.isInitialized) {
            openBankListForSelection()
        } else
            try {
                if (checkAccountNameState is AccountNameFinalValidationSuccess) {
                    bankSettingAnalytics.eventOnAutoNameSimpanClick()
                    openConfirmationPopUp()
                } else if (checkAccountNameState is EditableAccountName) {
                    bankSettingAnalytics.eventOnManualNameSimpanClick()
                    val accountHolderName = textAreaBankAccountHolderName.textAreaInput.text.toString()
                    if (isAccountNameLengthValid(accountHolderName)) {
                        if ((checkAccountNameState as EditableAccountName).isValidBankAccount) {
                            builder.setAccountName(accountHolderName, true)
                            openConfirmationPopUp()
                        } else {
                            addAccountViewModel.validateEditedAccountInfo(bank.bankID,
                                    textAreaBankAccountNumber.textAreaInput.text.toString(),
                                    accountHolderName)
                        }
                    } else {
                        showManualAccountNameError(
                                getString(R.string.sbank_name_char_limit_error))
                    }
                }
            } catch (e: Exception) {
            }
    }

    private fun isAccountNameLengthValid(accountHolderName: String): Boolean {
        if (accountHolderName.length in 3..128) {
            return true
        }
        return false
    }

    private fun openConfirmationPopUp() {
        context?.let {context->
            val addBankRequest = builder.build()
            val description = context.getString(R.string.sbank_add_bank_confirm, bank.abbreviation,
                    addBankRequest.accountNo, addBankRequest.accountName)
            DialogUnify(context = context, actionType = DialogUnify.HORIZONTAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.sbank_confirm_add_bank_account))
                setDescription(description)
                setPrimaryCTAText(getString(R.string.sbank_ya_tambah))
                setSecondaryCTAText(getString(R.string.sbank_batal))
                setPrimaryCTAClickListener {
                    bankSettingAnalytics.eventDialogConfirmAddAccountClick()
                    openPinVerification()
                    dismiss()
                }
                setSecondaryCTAClickListener {
                    dismiss()
                }
                show()
            }
        }
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
            REQUEST_OTP -> {
                if (resultCode == Activity.RESULT_OK) {
                    requestAddBankAccount()
                }
            }
        }
    }

    private fun requestAddBankAccount() {
        try {
            progress_bar.visible()
            addAccountViewModel.addBank(builder.build())
        } catch (e: Exception) {
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

    private fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        s?.let {
            addAccountViewModel.validateAccountNumber(bank, s.toString())
        } ?: run {
            addAccountViewModel.validateAccountNumber(bank, "")
        }
    }

    private fun setTncText() {
        val tncSpannableString = createTermsAndConditionSpannable()
        tvAddBankTnc.text = tncSpannableString
        tvAddBankTnc.highlightColor = MethodChecker.getColor(context, android.R.color.transparent)
        tvAddBankTnc.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun createTermsAndConditionSpannable(): SpannableStringBuilder {
        val originalText = getString(R.string.sbank_add_bank_tnc)
        val readMoreText = getString(R.string.sbank_add_bank_tnc_clickable_part)
        val spannableString = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = MethodChecker.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_G400)
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
        addAccountViewModel.loadTermsAndCondition()
    }

    private fun openTNCBottomSheet(templateData: TemplateData?) {
        templateData?.let {
            BankTNCBottomSheet.showBankTNCBottomSheet(templateData, activity)
        }
    }

    private fun showError(throwable: Throwable, retry: (() -> Unit)?) {
        context?.let { context ->
            val errorMessage = when (throwable) {
                is AddBankAccountException -> throwable.errorMessage
                else -> SettingBankErrorHandler.getErrorMessage(context, throwable)
            }
            view?.let { view ->
                retry?.let {
                    Toaster.build(view, errorMessage, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR,
                            getString(R.string.sbank_promo_coba_lagi),
                            View.OnClickListener { retry.invoke() }).show()
                } ?: run {
                    Toaster.build(view, errorMessage,
                            Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun getAlphabetOnlyInputFilter(): Array<InputFilter> {
        val regex = Regex("^[a-zA-Z* ]+$")
        return arrayOf(
                object : InputFilter {
                    override fun filter(
                            cs: CharSequence, start: Int,
                            end: Int, spanned: Spanned?, dStart: Int, dEnd: Int,
                    ): CharSequence? {
                        if (cs == "") {
                            return cs
                        }
                        return if (cs.toString().matches(regex)) {
                            cs
                        } else ""
                    }
                }
        )
    }

    private fun setPeriksaButtonState(isEnable: Boolean) {
        if (isEnable)
            btnPeriksa.buttonVariant = UnifyButton.Variant.GHOST
        else
            btnPeriksa.buttonVariant = UnifyButton.Variant.FILLED
        btnPeriksa.isEnabled = isEnable
    }

}
