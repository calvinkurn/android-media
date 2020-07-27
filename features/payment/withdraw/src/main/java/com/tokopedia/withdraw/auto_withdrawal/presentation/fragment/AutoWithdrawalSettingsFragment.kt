package com.tokopedia.withdraw.auto_withdrawal.presentation.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.widget.CompoundButtonCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.webview.TkpdWebView
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.di.component.AutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusData
import com.tokopedia.withdraw.auto_withdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.auto_withdrawal.domain.model.GetInfoAutoWD
import com.tokopedia.withdraw.auto_withdrawal.domain.model.Schedule
import com.tokopedia.withdraw.auto_withdrawal.presentation.activity.AutoWithdrawalActivity
import com.tokopedia.withdraw.auto_withdrawal.presentation.adapter.ScheduleChangeListener
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.AutoWDInfoFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.ExclusiveRekPremFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.ScheduleTimingFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel.AutoWDSettingsViewModel
import com.tokopedia.withdraw.saldowithdrawal.presentation.fragment.SaldoWithdrawalFragment
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import kotlinx.android.synthetic.main.swd_fragment_awd_settings.*
import javax.inject.Inject

class AutoWithdrawalSettingsFragment : BaseDaggerFragment(), ScheduleChangeListener {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    var getInfoAutoWD: GetInfoAutoWD? = null
    private var autoWDStatusData: AutoWDStatusData? = null
    private var currentSchedule: Schedule? = null
    private var requestedSchedule: Schedule? = null

    private var primaryBankAccount: BankAccount? = null
    private var tncTemplateStr: String? = null

    private val autoWDSettingsViewModel: AutoWDSettingsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(AutoWDSettingsViewModel::class.java)
    }

    override fun initInjector() {
        getComponent(AutoWithdrawalComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_fragment_awd_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        context?.let { context ->
            setTermsAndConditionSpannable(context)
        }
        scrollViewAutoWDContent.gone()
        setLoaderViewVisibility(true)
        observeViewModel()
        autoWDSettingsViewModel.getAutoWDInfo()
    }

    override fun getScreenName() = ""

    private fun setActionMenu() {
        val headerUnify = (activity as AutoWithdrawalActivity).getHeader()
        headerUnify.actionText = getString(R.string.swd_info)
        headerUnify.actionTextView?.setOnClickListener { openInfoBottomSheet() }
    }

    private fun observeViewModel() {
        autoWDSettingsViewModel.infoAutoWDResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onWithdrawalInfoLoaded(it.data)
                is Fail -> showGlobalError(it.throwable, autoWDSettingsViewModel::getAutoWDInfo)
            }
        })
        autoWDSettingsViewModel.bankListResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onBankAccountLoaded(it.data)
                is Fail -> showGlobalError(it.throwable, autoWDSettingsViewModel::getBankAccount)
            }
        })
        autoWDSettingsViewModel.autoWDStatusDataResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onAutoWithdrawalStatusLoaded(it.data)
                is Fail -> showGlobalError(it.throwable, autoWDSettingsViewModel::getAutoWDStatus)
            }
        })
        autoWDSettingsViewModel.autoWDTNCResultLiveData.observe(this, Observer {
            when (it) {
                is Success -> onTermsAndConditionLoaded(it.data)
                is Fail -> {
                    //todo tnc load fail
                }
            }
        })
    }

    private fun onTermsAndConditionLoaded(template: String) {
        tncTemplateStr = template
        openTNCBottomSheet()
    }

    private fun onWithdrawalInfoLoaded(data: GetInfoAutoWD) {
        getInfoAutoWD = data
        setActionMenu()
    }

    private fun showGlobalError(throwable: Throwable, retryAction: () -> Unit) {
        val errorType = when (throwable) {
            is MessageErrorException -> GlobalError.SERVER_ERROR
            else -> GlobalError.NO_CONNECTION
        }
        globalError.visible()
        globalError.setType(errorType)
        globalError.errorAction.visible()
        globalError.errorAction.setOnClickListener {
            setLoaderViewVisibility(false)
            retryAction.invoke()
        }
    }

    private fun setLoaderViewVisibility(isVisible: Boolean) {
        if (isVisible)
            loaderView.visible()
        else
            loaderView.gone()
    }

    private fun onBankAccountLoaded(accountList: ArrayList<BankAccount>) {
        accountList.forEach lit@{ bankAccount ->
            if (bankAccount.isDefaultBank == 1) {
                primaryBankAccount = bankAccount
                return@lit
            }
        }
        primaryBankAccount?.apply {
            tvAutoWDBankName.text = bankName
            tvAutoWdBankAccountDetail.text = "$accountNo - $accountName"
            tvAutoWdBankNote.text = copyWriting ?: ""
            btnAutoWDAddBankAccount.gone()
        } ?: run {
            btnAutoWDAddBankAccount.visible()
            tvAutoWDBankName.gone()
            tvAutoWdBankAccountDetail.gone()
            btnAutoWDAddBankAccount.setOnClickListener { openAddBankAccount() }
            tvAutoWdBankNote.text = "Belum ada rekening tersimpan."
        }
    }

    private fun onAutoWithdrawalStatusLoaded(autoWDStatusData: AutoWDStatusData) {
        groupAutoWDSaveSetting.gone()
        autoWDStatusData.isOwner = false
        this.autoWDStatusData = autoWDStatusData
        autoWDStatusData.apply {
            setCurrentWithdrawalSchedule(this)
            if (isOwner) {
                setAutoWdCheckBoxListener(this)
                tickerAutoWD.gone()
                setBannerViewVisibility(status == 0)
            } else {
                tickerAutoWD.visible()
                tickerAutoWD.setTextDescription(getString(R.string.swd_auto_wd_user_not_owner))
            }
            enableAutoWDSettingSection(isOwner)
            enableScheduleSection(autoWDStatusData.status == 1 && autoWDStatusData.isOwner)
            enableBankAccountSection(autoWDStatusData.status == 1 && autoWDStatusData.isOwner)
            updateBankAccountSectionState()
            setLoaderViewVisibility(false)
            scrollViewAutoWDContent.visible()
        }
    }

    private fun setScheduleData() {
        requestedSchedule?.apply {
            tvAutoWDScheduleType.text = title
            tvScheduleTiming.text = desc
        } ?: run {
            currentSchedule?.apply {
                tvAutoWDScheduleType.text = title
                tvScheduleTiming.text = desc
                checkboxAutoWD.isChecked = autoWDStatusData?.status == 1
            }
        }
    }

    private fun setCurrentWithdrawalSchedule(autoWDStatusData: AutoWDStatusData) {
        currentSchedule = autoWDStatusData.scheduleList[0]
        autoWDStatusData.scheduleList.forEach {
            if (it.status == 1) {
                currentSchedule = it
            }
        }
        setScheduleData()
    }

    private fun setAutoWdCheckBoxListener(autoWDStatusData: AutoWDStatusData) {
        checkboxAutoWD.setOnCheckedChangeListener { _, isChecked ->
            updateBankAccountSectionState()
            enableScheduleSection(isChecked && autoWDStatusData.isOwner)
            showSaveButton()
        }
    }

    private fun showSaveButton() {
        if (currentSchedule != null
                && autoWDStatusData?.isOwner == true) {
            if ((checkboxAutoWD.isChecked && autoWDStatusData?.status != 1)
                    || (!checkboxAutoWD.isChecked && autoWDStatusData?.status == 1)
                    || (checkboxAutoWD.isChecked && requestedSchedule != null && currentSchedule?.equals(requestedSchedule) == false)) {
                setSaveSettingBottomViewVisibility(true)
                btnAutoWDSaveSettings.isEnabled = isPrimaryAccountActive()
            } else
                setSaveSettingBottomViewVisibility(false)
        } else {
            setSaveSettingBottomViewVisibility(false)
        }
    }

    private fun isPrimaryAccountActive(): Boolean {
        primaryBankAccount?.apply {
            return !(status == 2 || status == 3 || status == 5)
        }
        return false
    }

    private fun updateBankAccountSectionState() {
        if (!checkboxAutoWD.isChecked)
            enableBankAccountSection(false)
        else if (!isPrimaryAccountActive()) {
            enableBankAccountSection(autoWDStatusData?.isOwner == true)
            enableBankAccountData(false)
        } else
            enableBankAccountSection(autoWDStatusData?.isOwner == true)
    }

    private fun setSaveSettingBottomViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            groupAutoWDSaveSetting.visible()
            btnAutoWDSaveSettings.setOnClickListener { onSaveAutoWDSettingsClick() }
        } else
            groupAutoWDSaveSetting.gone()
    }

    private fun setBannerViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            context?.let { context ->
                setExclusiveRekeningBannerSpannable(context)
            }
            groupAutoWDBannerViews.visible()
        } else
            groupAutoWDBannerViews.gone()

    }

    private fun enableAutoWDSettingSection(isEnable: Boolean) {
        context?.let { context ->
            CompoundButtonCompat.getButtonDrawable(checkboxAutoWD)
            checkboxAutoWD.isEnabled = isEnable
            if (isEnable) {
                val textHeadingColor = ContextCompat.getColor(context, R.color.Neutral_N700_96)
                val textBodyColor = ContextCompat.getColor(context, R.color.Neutral_N700_68)
                tvAutoWDTitle.setTextColor(textHeadingColor)
                tvAutoWDAllBalanceAccording.setTextColor(textBodyColor)
            } else {
                val textDisabledColor = ContextCompat.getColor(context, R.color.Neutral_N700_32)
                tvAutoWDTitle.setTextColor(textDisabledColor)
                tvAutoWDAllBalanceAccording.setTextColor(textDisabledColor)
            }
        }
    }

    private fun enableScheduleSection(isEnable: Boolean) {
        context?.let { context ->
            if (isEnable) {
                ivAutoWDSchedule.isEnabled = isEnable
                val imageEnableColor = tintDrawable(context, R.drawable.swd_ic_time, R.color.Green_G500)
                ivAutoWDSchedule.setImageDrawable(imageEnableColor)
                val textHeadingColor = ContextCompat.getColor(context, R.color.Neutral_N700_96)
                val textBodyColor = ContextCompat.getColor(context, R.color.Neutral_N700_68)
                tvAutoWDScheduleTitle.setTextColor(textHeadingColor)
                tvAutoWDScheduleType.setTextColor(textBodyColor)
                tvScheduleTiming.setTextColor(textBodyColor)
                tvChangeAutoWDSchedule.visible()
            } else {
                val imageDisableColor = tintDrawable(context, R.drawable.swd_ic_time, R.color.Neutral_N100)
                ivAutoWDSchedule.setImageDrawable(imageDisableColor)
                val textDisabledColor = ContextCompat.getColor(context, R.color.Neutral_N700_32)
                tvAutoWDScheduleTitle.setTextColor(textDisabledColor)
                tvAutoWDScheduleType.setTextColor(textDisabledColor)
                tvScheduleTiming.setTextColor(textDisabledColor)
                tvChangeAutoWDSchedule.gone()
            }
            tvChangeAutoWDSchedule.setOnClickListener { openSchedulingTimings() }
        }
    }

    private fun openSchedulingTimings() {
        autoWDStatusData?.apply {
            activity?.let {
                val bottomSheet = ScheduleTimingFragment.getInstance(scheduleList, requestedSchedule
                        ?: currentSchedule)
                bottomSheet.setTitle(getString(R.string.swd_withdrawal_schedule))
                bottomSheet.show(it.supportFragmentManager, "ScheduleTimingFragment")
            }
        }
    }

    private fun enableBankAccountSection(isEnable: Boolean) {
        context?.let { context ->
            if (isEnable) {
                val rpLogo = tintDrawable(context, R.drawable.swd_ic_transaction, R.color.Green_G500)
                ivDestinationAccount.setImageDrawable(rpLogo)
                val textHeadingColor = ContextCompat.getColor(context, R.color.Neutral_N700_96)
                tvDestinationAccountTitle.setTextColor(textHeadingColor)
            } else {
                val rpLogo = tintDrawable(context, R.drawable.swd_ic_transaction, R.color.Neutral_N100)
                ivDestinationAccount.setImageDrawable(rpLogo)
                val textDisabledColor = ContextCompat.getColor(context, R.color.Neutral_N700_32)
                tvDestinationAccountTitle.setTextColor(textDisabledColor)
            }
            enableBankAccountData(isEnable)
            if (btnAutoWDAddBankAccount.visibility == View.VISIBLE) {
                btnAutoWDAddBankAccount.isEnabled = isEnable
                if (isEnable)
                    tvAutoWdBankNote.visibility = View.INVISIBLE
                else
                    tvAutoWdBankNote.visible()

            }
        }
    }

    private fun enableBankAccountData(isEnable: Boolean) {
        context?.let { context ->
            if (isEnable) {
                val textBodyColor = ContextCompat.getColor(context, R.color.Neutral_N700_68)
                tvAutoWdBankAccountDetail.setTextColor(textBodyColor)
                tvAutoWDBankName.setTextColor(textBodyColor)
                tvAutoWdBankNote.setTextColor(textBodyColor)
            } else {
                val textDisabledColor = ContextCompat.getColor(context, R.color.Neutral_N700_32)
                tvAutoWdBankAccountDetail.setTextColor(textDisabledColor)
                tvAutoWDBankName.setTextColor(textDisabledColor)
                tvAutoWdBankNote.setTextColor(textDisabledColor)
            }
        }
    }

    private fun tintDrawable(context: Context, drawableRes: Int, colorInt: Int): Drawable {
        val color = ContextCompat.getColor(context, colorInt)
        val mDrawable = MethodChecker.getDrawable(context, drawableRes)
        var mWrappedDrawable = mDrawable.mutate()
        mWrappedDrawable = DrawableCompat.wrap(mWrappedDrawable)
        DrawableCompat.setTint(mWrappedDrawable, color)
        DrawableCompat.setTintMode(mWrappedDrawable, PorterDuff.Mode.SRC_IN)
        return mWrappedDrawable
    }

    private fun setExclusiveRekeningBannerSpannable(context: Context) {
        val originalText = getString(R.string.swd_exclusive_for_premium_acc)
        val rekeningAccount = getString(R.string.swd_rekening_premium)
        val spannableString = SpannableString(rekeningAccount)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = ContextCompat.getColor(context, R.color.Green_G500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                WithdrawConstant.openRekeningAccountInfoPage(context)
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAutoWDBannerBody.movementMethod = LinkMovementMethod.getInstance()
        tvAutoWDBannerBody.text = SpannableStringBuilder.valueOf(originalText).append(" ")
                .append(spannableString)
    }

    private fun setTermsAndConditionSpannable(context: Context) {
        val originalText = getString(R.string.swd_awd_terms_and_condition)
        val termCondition = getString(R.string.swd_awd_terms_and_condition_clickable)
        val spannableString = SpannableString(termCondition)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = ContextCompat.getColor(context, R.color.Green_G500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                tncTemplateStr?.let {
                    openTNCBottomSheet()
                } ?: run {
                    autoWDSettingsViewModel.getAutoWDTNC()
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)

        tvAutoWDTermsAndCond.movementMethod = LinkMovementMethod.getInstance()
        tvAutoWDTermsAndCond.text = SpannableStringBuilder.valueOf(originalText).append(" ")
                .append(spannableString)
    }

    private fun openTNCBottomSheet() {
        activity?.let { activity ->
            tncTemplateStr?.let {
                val bottomSheetUnify = BottomSheetUnify()
                val view = layoutInflater.inflate(R.layout.swd_layout_withdraw_tnc,
                        null, true)
                val webView: TkpdWebView = view.findViewById(R.id.swd_tnc_webview)
                webView.loadData(tncTemplateStr, "text/html", "utf-8")
                bottomSheetUnify.setChild(view)
                bottomSheetUnify.show(activity.supportFragmentManager, "")
            }
        }
    }

    private fun openInfoBottomSheet() {
        activity?.let {
            getInfoAutoWD?.apply {
                val bottomSheet = AutoWDInfoFragment.getInstance(this)
                bottomSheet.setTitle(getString(R.string.swd_auto_wd_info_title))
                bottomSheet.show(it.supportFragmentManager, "AutoWDInfoFragment")
            }
        }
    }

    companion object {
        fun getInstance(bundle: Bundle): AutoWithdrawalSettingsFragment = AutoWithdrawalSettingsFragment()
                .apply {
                    arguments = bundle
                }
    }

    override fun onScheduleSelected(schedule: Schedule) {
        if (currentSchedule?.equals(schedule) == false)
            requestedSchedule = schedule
        else {
            requestedSchedule = null
        }
        setScheduleData()
        showSaveButton()
    }

    private fun onSaveAutoWDSettingsClick() {
        autoWDStatusData?.apply {
            if (isPowerWd) {
            } else {
                openJoinRPProgramBottomSheet()
            }
        }
    }

    private fun openJoinRPProgramBottomSheet() {
        activity?.apply {
            val bottomSheet = ExclusiveRekPremFragment.getInstance()
            bottomSheet.show(supportFragmentManager, "ExclusiveRekPremFragment")
        }
    }

    private fun openAddBankAccount() {
        activity?.apply {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.ADD_BANK)
            startActivityForResult(intent, SaldoWithdrawalFragment.BANK_SETTING_REQUEST_CODE)
        }
    }
}
