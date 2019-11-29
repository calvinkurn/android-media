package com.tokopedia.settingbank.banklist.v2.view.fragment

import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.settingbank.R
import com.tokopedia.settingbank.banklist.v2.di.SettingBankComponent
import com.tokopedia.settingbank.banklist.v2.domain.Bank
import com.tokopedia.settingbank.banklist.v2.domain.TemplateData
import com.tokopedia.settingbank.banklist.v2.view.activity.ARG_BANK_DATA
import com.tokopedia.settingbank.banklist.v2.view.viewModel.BankNumberTextWatcherViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewModel.SettingBankTNCViewModel
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnNoBankSelected
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnTextWatcherError
import com.tokopedia.settingbank.banklist.v2.view.viewState.OnTextWatcherSuccess
import com.tokopedia.settingbank.banklist.v2.view.widgets.BankTNCBottomSheet
import com.tokopedia.settingbank.banklist.v2.view.widgets.CloseableBottomSheetFragment
import kotlinx.android.synthetic.main.fragment_add_bank_v2.*
import javax.inject.Inject

class AddBankFragment : BaseDaggerFragment() {

    override fun getScreenName(): String? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private lateinit var tNCViewModel: SettingBankTNCViewModel
    private lateinit var textWatcherViewModel: BankNumberTextWatcherViewModel

    private lateinit var tncBottomSheet: BankTNCBottomSheet
    private lateinit var bankListBottomSheet: CloseableBottomSheetFragment

    lateinit var bank: Bank

    override fun initInjector() {
        getComponent(SettingBankComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        savedInstanceState?.let {
            if (savedInstanceState.containsKey(ARG_BANK_DATA))
                savedInstanceState.getParcelable<Bank>(ARG_BANK_DATA)?.let {
                    bank = it
                }
        }
        initViewModels()
    }

    private fun initViewModels() {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        tNCViewModel = viewModelProvider.get(SettingBankTNCViewModel::class.java)
        textWatcherViewModel = viewModelProvider.get(BankNumberTextWatcherViewModel::class.java)
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
        etBankAccountNumber.addTextChangedListener(textWatcherViewModel.getTextWatcher())
    }

    private fun openBankListForSelection() {
        bankListBottomSheet = CloseableBottomSheetFragment.newInstance(SelectBankFragment(),
                true,
                getString(R.string.sbank_choose_a_bank),
                null,
                CloseableBottomSheetFragment.STATE_FULL)
        bankListBottomSheet.showNow(activity!!.supportFragmentManager, "")
    }

    private fun startObservingViewModels() {
        tNCViewModel.tncPopUpTemplate.observe(this, Observer {
            openTNCBottomSheet(it)
        })

        textWatcherViewModel.textWatcherState.observe(this, Observer {
            when (it) {
                is OnTextWatcherSuccess -> setAccountNumberError(null)
                is OnNoBankSelected -> setAccountNumberError("Please Nama Bank")
                is OnTextWatcherError -> setAccountNumberError(it.error)
            }
        })
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
            etBankName.setText("${bank.abbreviation ?: ""} (${bank.bankName})")
        }
    }

    override fun onDestroy() {
        tNCViewModel.tncPopUpTemplate.removeObservers(this)
        textWatcherViewModel.textWatcherState.removeObservers(this)
        super.onDestroy()
    }
}