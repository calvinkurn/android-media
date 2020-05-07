package com.tokopedia.withdraw.view.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.ColorRes
import androidx.appcompat.app.AlertDialog
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog
import com.tokopedia.design.intdef.CurrencyEnum
import com.tokopedia.design.text.watcher.AfterTextWatcher
import com.tokopedia.design.text.watcher.CurrencyTextWatcher
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.design.utils.StringUtils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.webview.TkpdWebView
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.WithdrawAnalytics
import com.tokopedia.withdraw.constant.WithdrawConstant
import com.tokopedia.withdraw.di.WithdrawComponent
import com.tokopedia.withdraw.domain.model.BankAccount
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpData
import com.tokopedia.withdraw.domain.model.validatePopUp.ValidatePopUpWithdrawal
import com.tokopedia.withdraw.view.adapter.BankAccountAdapter
import com.tokopedia.withdraw.view.decoration.SpaceItemDecoration
import com.tokopedia.withdraw.view.viewmodel.BankAccountListViewModel
import com.tokopedia.withdraw.view.viewmodel.ValidatePopUpViewModel
import kotlinx.android.synthetic.main.swd_fragment_base_withdrawal.*
import javax.inject.Inject

abstract class WithdrawalBaseFragment : BaseDaggerFragment(), BankAccountAdapter.BankAdapterListener {

    @Inject
    lateinit var userSession: UserSession

    @Inject
    lateinit var analytics: WithdrawAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory


    private lateinit var bankAccountListViewModel: BankAccountListViewModel
    private lateinit var bankAccountAdapter: BankAccountAdapter

    private var balance: Long = 0L

    private lateinit var accountBalanceType: SaldoWithdrawal

    override fun getScreenName(): String? = null

    override fun initInjector() {
        getComponent(WithdrawComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModels()
    }

    private fun initViewModels() {
        var viewModelProvider: ViewModelProvider
        parentFragment?.let {
            viewModelProvider = ViewModelProviders.of(it, viewModelFactory)
            bankAccountListViewModel = viewModelProvider.get(BankAccountListViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_fragment_base_withdrawal,
                container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated(savedInstanceState)
        if (balance > 0) {
            initBankAccountRecycler()
            setCurrencyTextWatcherToSaldoInput()
            withdrawalButton.isEnabled = false
            tvCopyAllSaldoAmount.setOnClickListener { copyAllBalanceToWithdrawalAmount() }
            tv_info.text = createTermsAndConditionSpannable()
            tv_info.movementMethod = LinkMovementMethod.getInstance()
            if (::bankAccountListViewModel.isInitialized)
                observeBaseViewModel()
            observeBaseViewModel()
            addTextWatcherToUpdateWithdrawalState()
            if (savedInstanceState == null)
                etWithdrawalInput.setText("0")
        }
    }

    abstract fun onViewCreated(savedInstanceState: Bundle?)

    abstract fun updateWithdrawalHint(bankAccount: BankAccount?,
                                      withdrawalAmount: Long)

    abstract fun updateWithdrawalButtonState(bankAccount: BankAccount?,
                                             withdrawalAmount: Long)

    private fun observeBaseViewModel() {
        bankAccountListViewModel.bankListResponseMutableData.observe(this, Observer {
            when (it) {
                is Success -> {
                    updateBankAccountAdapter(it.data)
                }
                is Fail -> {
                    updateBankAccountAdapter(arrayListOf())
                }
            }
        })
    }

    private fun addTextWatcherToUpdateWithdrawalState() {
        etWithdrawalInput.addTextChangedListener(object : AfterTextWatcher() {
            override fun afterTextChanged(s: Editable?) {
                val withdrawal: Long = StringUtils.convertToNumeric(s.toString(),
                        false).toLong()
                updateWithdrawalHint(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
                if (withdrawal == 0L) {
                    etWithdrawalInput.setSelection(etWithdrawalInput.length())
                }
                updateWithdrawalButtonState(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
            }
        })
    }

    fun changeHintTextColor(@ColorRes hintColor: Int,
                            @ColorRes underLineColor: Int, hintText: String) {
        etWithdrawalInput.background.mutate().setColorFilter(resources.getColor(underLineColor), PorterDuff.Mode.SRC_ATOP)
        saldoWithdrawHint.setTextColor(resources.getColor(hintColor))
        saldoWithdrawHint.text = hintText
    }

    fun updateWithdrawalButton(canWithdraw: Boolean) {
        withdrawalButton.isEnabled = canWithdraw
        if (canWithdraw) {
            withdrawalButton.setOnClickListener {
                initiateWithdrawal()
            }
        }
    }

    fun setBalanceType(accountBalanceType: SaldoWithdrawal, balance: Long) {
        this.accountBalanceType = accountBalanceType
        this.balance = balance
        when (accountBalanceType) {
            is BuyerSaldoWithdrawal -> tvAccountBalanceType.text = getString(R.string.saldo_refund)
            is SellerSaldoWithdrawal -> tvAccountBalanceType.text = getString(R.string.saldo_seller)
        }
        this.balance = balance
        if (balance > 0L) {
            tvTotalSaldoBalance.text = CurrencyFormatUtil
                    .convertPriceValueToIdrFormat(balance, false)
        } else {
            showBlankState()
        }
    }

    private fun showBlankState() {
        editable_group.visibility = View.GONE
        emptyGroup.visibility = View.VISIBLE
        val message = when (accountBalanceType) {
            is BuyerSaldoWithdrawal -> getString(R.string.swd_refund_empty_msg)
            is SellerSaldoWithdrawal -> getString(R.string.swd_penghasilan_empty_msg)

        }
        emptySaldoDescription.text = message
    }

    private fun initBankAccountRecycler() {
        context?.let {
            recyclerBankList.layoutManager = NonScrollableLinerLayoutManager(context!!)
            bankAccountAdapter = BankAccountAdapter(analytics, this)
            recyclerBankList.adapter = bankAccountAdapter
            val itemDecoration = SpaceItemDecoration(it.resources.getDimension(R.dimen.dp_16).toInt()
                    , MethodChecker.getDrawable(it, R.drawable.swd_divider))
            recyclerBankList.addItemDecoration(itemDecoration)
        }
    }

    private fun updateBankAccountAdapter(data: ArrayList<BankAccount>) {
        recyclerBankList.post {
            bankAccountAdapter.updateBankList(data)
            bankAccountAdapter.notifyDataSetChanged()
            onBankAccountChanged()
        }
    }

    private fun setCurrencyTextWatcherToSaldoInput() {
        val currencyTextWatcher = CurrencyTextWatcher(etWithdrawalInput, CurrencyEnum.RPwithSpace)
        currencyTextWatcher.setMaxLength(WithdrawConstant.MAX_LENGTH)
        currencyTextWatcher.setDefaultValue("")
        etWithdrawalInput.addTextChangedListener(currencyTextWatcher)
    }

    private fun copyAllBalanceToWithdrawalAmount() {
        etWithdrawalInput.setText(balance.toString())
    }

    private fun createTermsAndConditionSpannable(): SpannableStringBuilder? {
        val originalText = getString(R.string.saldo_withdraw_tnc_original)
        val readMoreText = getString(R.string.saldo_withdraw_tnc_clickable)
        val spannableString = SpannableString(readMoreText)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = this.resources.getColor(R.color.tkpd_main_green)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                analytics.eventClickTANDC()
                openTermsAndConditionBottomSheet()
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder.valueOf(originalText).append(" ").append(spannableString)
    }

    private fun openTermsAndConditionBottomSheet() {
        val bottomSheet = CloseableBottomSheetDialog
                .createInstanceRounded(activity)
        val view = layoutInflater.inflate(R.layout.swd_layout_withdraw_tnc,
                null, true)
        val webView: TkpdWebView = view.findViewById(R.id.swd_tnc_webview)
        val closeBtn = view.findViewById<ImageView>(R.id.close_button)
        val titleView: Typography = view.findViewById(R.id.title_closeable)
        webView.loadAuthUrl(WithdrawConstant.WEB_TNC_URL, userSession)
        closeBtn.setOnClickListener { bottomSheet.dismiss() }
        titleView.text = getString(R.string.saldo_withdraw_tnc_title)
        bottomSheet.setCustomContentView(view, getString(R.string.saldo_withdraw_tnc_title), false)
        bottomSheet.show()
    }

    fun lockWithdrawal(isLocked: Boolean) {
        if (isLocked) {
            withdrawalButton.isEnabled = false
            withdrawalButton.isClickable = false
            ivButtonLeft.visible()
        } else {
            withdrawalButton.isEnabled = true
            withdrawalButton.isClickable = true
            ivButtonLeft.gone()
        }
    }

    override fun openAddBankAccount() {
        parentFragment?.let {
            if (it is SaldoWithdrawalFragment) {
                it.openAddBankAccount()
            }
        }
    }

    override fun openBankAccountSetting() {
        parentFragment?.let {
            if (it is SaldoWithdrawalFragment) {
                it.openBankAccountSetting()
            }
        }
    }

    override fun onBankAccountChanged() {
        val withdrawal: Long = StringUtils.convertToNumeric(etWithdrawalInput.text.toString(),
                false).toLong()
        updateWithdrawalHint(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
        if (withdrawal == 0L) {
            etWithdrawalInput.setSelection(etWithdrawalInput.length())
        }
        updateWithdrawalButtonState(bankAccountAdapter.getSelectedBankAccount(), withdrawal)
    }

    private fun initiateWithdrawal() {
        analytics.eventClickWithdrawal()
        bankAccountAdapter.getSelectedBankAccount()?.let { bankAccount ->
            val withdrawalAmount = StringUtils
                    .convertToNumeric(etWithdrawalInput.text.toString(),
                            false).toLong()
            if (parentFragment is SaldoWithdrawalFragment) {
                when (accountBalanceType) {
                    is BuyerSaldoWithdrawal -> {
                        (parentFragment as SaldoWithdrawalFragment)
                                .initiateBuyerWithdrawal(bankAccount, withdrawalAmount)
                    }
                    is SellerSaldoWithdrawal -> {
                        (parentFragment as SaldoWithdrawalFragment)
                                .initiateSellerWithdrawal(bankAccount, withdrawalAmount)
                    }
                }
            }
        }
    }

}


class NonScrollableLinerLayoutManager(context: Context) : LinearLayoutManager(context) {
    override fun canScrollVertically(): Boolean {
        return false
    }
}

sealed class SaldoWithdrawal
object BuyerSaldoWithdrawal : SaldoWithdrawal()
object SellerSaldoWithdrawal : SaldoWithdrawal()