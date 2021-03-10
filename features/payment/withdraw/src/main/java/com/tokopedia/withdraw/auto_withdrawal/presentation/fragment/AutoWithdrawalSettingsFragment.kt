package com.tokopedia.withdraw.auto_withdrawal.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
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
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSession
import com.tokopedia.webview.TkpdWebView
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.analytics.*
import com.tokopedia.withdraw.auto_withdrawal.di.component.AutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.domain.model.*
import com.tokopedia.withdraw.auto_withdrawal.presentation.activity.AutoWithdrawalActivity
import com.tokopedia.withdraw.auto_withdrawal.presentation.adapter.ScheduleChangeListener
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.AutoWDInfoFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.ExclusiveRekPremFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.dialog.ScheduleTimingFragment
import com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel.AutoWDSettingsViewModel
import com.tokopedia.withdraw.saldowithdrawal.util.WithdrawConstant
import kotlinx.android.synthetic.main.swd_fragment_awd_settings.*
import javax.inject.Inject

const val TAG_AUTO_WITHDRAWAL_TNC_BOTTOM_SHEET = "AUTO_WITHDRAWAL_TNC_BOTTOM_SHEET"
const val MIME_TYPE_TEXT_HTML = "text/html"
const val ENCODING_UTF_8 = "utf-8"

class AutoWithdrawalSettingsFragment : BaseDaggerFragment(), ScheduleChangeListener {

    @Inject
    lateinit var userSession: dagger.Lazy<UserSession>

    @Inject
    lateinit var analytics: AutoWithdrawAnalytics

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    private var getInfoAutoWD: GetInfoAutoWD? = null
    private var autoWDStatusData: AutoWDStatusData? = null
    private var currentSchedule: Schedule? = null
    private var requestedSchedule: Schedule? = null

    private var primaryBankAccount: BankAccount? = null
    private var tncTemplateStr: String? = null
    private var autoWithdrawalUpsertRequest: AutoWithdrawalUpsertRequest? = null

    private var isAutoWDStatusLoadedFirstTime = false

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
        loaderView.visible()
        observeViewModel()
        autoWDSettingsViewModel.getAutoWDInfo()
    }

    override fun getScreenName() = ""

    private fun setActionMenu() {
        val headerUnify = (activity as AutoWithdrawalActivity).getHeader()
        headerUnify.actionText = getString(R.string.swd_info)
        headerUnify.actionTextView?.setOnClickListener {
            openInfoBottomSheet()
            analytics.onClickInfoMenu()
        }
    }

    private fun observeViewModel() {
        autoWDSettingsViewModel.infoAutoWDResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onWithdrawalInfoLoaded(it.data)
                is Fail -> showGlobalError(it.throwable, autoWDSettingsViewModel::getAutoWDInfo)
            }
        })

        autoWDSettingsViewModel.bankListResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onBankAccountLoaded(it.data)
                is Fail -> showGlobalError(it.throwable, autoWDSettingsViewModel::getBankAccount)
            }
        })

        autoWDSettingsViewModel.autoWDStatusDataResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onAutoWithdrawalStatusLoaded(it.data)
                is Fail -> showGlobalError(it.throwable, autoWDSettingsViewModel::getAutoWDStatus)
            }
        })

        autoWDSettingsViewModel.autoWDTNCResultLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onTermsAndConditionLoaded(it.data)
                is Fail -> {
                    showErrorMessage(it.throwable)
                }
            }
        })

        autoWDSettingsViewModel.upsertResponseLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.code == 200) {
                        onAutoWDUpsertComplete(it.data)
                    } else {
                        onAutoWithdrawalUpsertFailed(it.data.message)
                    }
                }
                is Fail -> {
                    showErrorMessage(it.throwable)
                    loaderView.gone()
                }
            }
        })
    }

    private fun showErrorMessage(throwable: Throwable) {
        context?.let {
            val message = ErrorHandler.getErrorMessage(it, throwable)
            view?.let { view ->
                Toaster.make(view, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
            }
        }
    }

    private fun onAutoWDUpsertComplete(data: UpsertResponse) {
        view?.let { it ->
            Toaster.make(it, data.message, Toaster.LENGTH_SHORT)
            autoWDSettingsViewModel.getAutoWDStatus()
            autoWithdrawalUpsertRequest?.let { autoWithdrawalUpsertRequest ->
                if (autoWithdrawalUpsertRequest.isQuit)
                    analytics.onViewAutoWdSuccessToaster(EVENT_LABEL_AUTO_WD_SCH_DELETE)
                else if (autoWithdrawalUpsertRequest.isUpdating)
                    analytics.onViewAutoWdSuccessToaster(EVENT_LABEL_AUTO_WD_SCH_UPDATED)
                else
                    analytics.onViewAutoWdSuccessToaster(EVENT_LABEL_AUTO_WD_SCH_INSERTED)
            }
        }
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
        loaderView.gone()
        val errorType = when (throwable) {
            is MessageErrorException -> GlobalError.SERVER_ERROR
            else -> GlobalError.NO_CONNECTION
        }
        globalError.visible()
        globalError.setType(errorType)
        globalError.errorAction.visible()
        globalError.errorAction.setOnClickListener {
            globalError.gone()
            loaderView.visible()
            retryAction.invoke()
        }
    }

    private fun onBankAccountLoaded(accountList: ArrayList<BankAccount>) {
        accountList.forEach lit@{ bankAccount ->
            if (bankAccount.defaultBankAccount) {
                primaryBankAccount = bankAccount
                return@lit
            }
        }
        primaryBankAccount?.apply {
            tvAutoWDBankName.visible()
            tvAutoWdBankAccountDetail.visible()
            btnAutoWDAddBankAccount.gone()
            tvAutoWdBankNote.visible()
            tvAutoWDBankName.text = bankName
            tvAutoWdBankAccountDetail.text = "$accountNo - $accountName"
            tvAutoWdBankNote.text = copyWriting ?: ""
        } ?: run {
            btnAutoWDAddBankAccount.visible()
            tvAutoWDBankName.gone()
            tvAutoWdBankAccountDetail.gone()
            btnAutoWDAddBankAccount.setOnClickListener { openAddBankAccount() }
            tvAutoWdBankNote.text = getString(R.string.swd_awd_no_saved_account)
        }
    }

    private fun resetOldRequest() {
        requestedSchedule = null
    }

    private fun onAutoWithdrawalStatusLoaded(autoWDStatusData: AutoWDStatusData) {
        resetOldRequest()
        groupAutoWDSaveSetting.gone()
        this.autoWDStatusData = autoWDStatusData
        autoWDStatusData.apply {
            setCurrentWithdrawalSchedule(this)
            if (isOwner) {
                setAutoWdCheckBoxListener(this)
                tickerAutoWD.gone()
                setBannerViewVisibility(status == AUTO_WITHDRAWAL_STATUS_NEW)
            } else {
                tickerAutoWD.visible()
                tickerAutoWD.setTextDescription(getString(R.string.swd_auto_wd_user_not_owner))
            }
            enableAutoWDSettingSection(isOwner)
            enableScheduleSection(status == AUTO_WITHDRAWAL_STATUS_ENABLE && isOwner)
            enableBankAccountSection(status == AUTO_WITHDRAWAL_STATUS_ENABLE && isOwner)
            updateBankAccountSectionState()
            scrollViewAutoWDContent.visible()
            ivAWBannerClose.setOnClickListener { groupAutoWDBannerViews.gone() }
        }
        loaderView.gone()
        onAutoWithdrawalStatusLoadedEvent()
    }

    private fun onAutoWithdrawalStatusLoadedEvent() {
        if (!isAutoWDStatusLoadedFirstTime) {
            analytics.onAutoSettingLoaded(autoWDStatusData?.status
                    == AUTO_WITHDRAWAL_STATUS_ENABLE)
            analytics.isUserRekeningPremiumEvent(autoWDStatusData?.isPowerWd ?: false)
            isAutoWDStatusLoadedFirstTime = true
        }
    }

    private fun setScheduleData(isFromSelectSchedule: Boolean) {
        requestedSchedule?.apply {
            tvAutoWDScheduleType.text = title
            tvScheduleTiming.text = desc
        } ?: run {
            currentSchedule?.apply {
                tvAutoWDScheduleType.text = title
                tvScheduleTiming.text = desc
                if(!isFromSelectSchedule)
                    checkboxAutoWD.isChecked = autoWDStatusData?.status == AUTO_WITHDRAWAL_STATUS_ENABLE
            }
        }
    }

    private fun setCurrentWithdrawalSchedule(autoWDStatusData: AutoWDStatusData) {
        currentSchedule = autoWDStatusData.scheduleList[0]
        autoWDStatusData.scheduleList.forEach {
            if (it.status == AUTO_WITHDRAWAL_STATUS_ENABLE) {
                currentSchedule = it
            }
        }
        setScheduleData(false)
    }

    private fun setAutoWdCheckBoxListener(autoWDStatusData: AutoWDStatusData) {
        checkboxAutoWD.setOnCheckedChangeListener { _, isChecked ->
            updateBankAccountSectionState()
            enableScheduleSection(isChecked && autoWDStatusData.isOwner)
            showSaveButton()
            analytics.onSettingCheckBoxToggled(isChecked)
        }
    }

    private fun showSaveButton() {
        if (currentSchedule != null
                && autoWDStatusData?.isOwner == true) {
            if ((checkboxAutoWD.isChecked && autoWDStatusData?.status != AUTO_WITHDRAWAL_STATUS_ENABLE)
                    || (!checkboxAutoWD.isChecked && autoWDStatusData?.status == AUTO_WITHDRAWAL_STATUS_ENABLE)
                    || (checkboxAutoWD.isChecked && requestedSchedule != null
                            && currentSchedule?.equals(requestedSchedule) == false)) {
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
            return !(status == BANK_STATUS_REJECTED_PENDING
                    || status == BANK_STATUS_REJECTED_FRAUD
                    || status == BANK_STATUS_PENDING_FRAUD_VERIFICATION)
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
                val textHeadingColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
                val textBodyColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                tvAutoWDTitle.setTextColor(textHeadingColor)
                tvAutoWDAllBalanceAccording.setTextColor(textBodyColor)
            } else {
                val textDisabledColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
                tvAutoWDTitle.setTextColor(textDisabledColor)
                tvAutoWDAllBalanceAccording.setTextColor(textDisabledColor)
            }
        }
    }

    private fun enableScheduleSection(isEnable: Boolean) {
        context?.let { context ->
            if (isEnable) {
                ivAutoWDSchedule.isEnabled = isEnable
                val imageEnableColor = tintDrawable(context, R.drawable.swd_ic_time, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ivAutoWDSchedule.setImageDrawable(imageEnableColor)
                val textHeadingColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
                val textBodyColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                tvAutoWDScheduleTitle.setTextColor(textHeadingColor)
                tvAutoWDScheduleType.setTextColor(textBodyColor)
                tvScheduleTiming.setTextColor(textBodyColor)
                tvChangeAutoWDSchedule.visible()
            } else {
                val imageDisableColor = tintDrawable(context, R.drawable.swd_ic_time, com.tokopedia.unifyprinciples.R.color.Unify_N100)
                ivAutoWDSchedule.setImageDrawable(imageDisableColor)
                val textDisabledColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
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
                ScheduleTimingFragment.show(it, it.supportFragmentManager,
                        scheduleList, requestedSchedule
                        ?: currentSchedule)
            }
        }
    }

    private fun enableBankAccountSection(isEnable: Boolean) {
        context?.let { context ->
            if (isEnable) {
                val rpLogo = tintDrawable(context, R.drawable.swd_ic_transaction, com.tokopedia.unifyprinciples.R.color.Unify_G500)
                ivDestinationAccount.setImageDrawable(rpLogo)
                val textHeadingColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)
                tvDestinationAccountTitle.setTextColor(textHeadingColor)
            } else {
                val rpLogo = tintDrawable(context, R.drawable.swd_ic_transaction, com.tokopedia.unifyprinciples.R.color.Unify_N100)
                ivDestinationAccount.setImageDrawable(rpLogo)
                val textDisabledColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
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
                val textBodyColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)
                tvAutoWdBankAccountDetail.setTextColor(textBodyColor)
                tvAutoWDBankName.setTextColor(textBodyColor)
                tvAutoWdBankNote.setTextColor(textBodyColor)
            } else {
                val textDisabledColor = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N700_32)
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
        val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
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
        val color = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_G500)
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
                val view = layoutInflater.inflate(R.layout.swd_layout_withdraw_tnc, null,
                        true)
                val webView: TkpdWebView = view.findViewById(R.id.swd_tnc_webview)
                bottomSheetUnify.setChild(view)
                bottomSheetUnify.setShowListener {
                    webView.loadData(tncTemplateStr, MIME_TYPE_TEXT_HTML, ENCODING_UTF_8)
                }
                bottomSheetUnify.show(activity.supportFragmentManager, TAG_AUTO_WITHDRAWAL_TNC_BOTTOM_SHEET)
                analytics.onClickViewTermsCondition()
            }
        }
    }

    private fun openInfoBottomSheet() {
        activity?.let {
            getInfoAutoWD?.apply {
                AutoWDInfoFragment.show(context, it.supportFragmentManager, this)
            }
        }
    }

    override fun onScheduleSelected(schedule: Schedule) {
        if (currentSchedule?.equals(schedule) == false)
            requestedSchedule = schedule
        else {
            requestedSchedule = null
        }
        setScheduleData(true)
        showSaveButton()
    }

    private fun onSaveAutoWDSettingsClick() {
        autoWDStatusData?.apply {
            if (isPowerWd) {
                if (status == AUTO_WITHDRAWAL_STATUS_NEW || status == AUTO_WITHDRAWAL_STATUS_DISABLE) {
                    autoWithdrawalUpsertRequest = AutoWithdrawalUpsertRequest(this,
                            null, requestedSchedule ?: currentSchedule,
                            primaryBankAccount,
                            false, null, false)
                    verifyUserUsingOTP()
                } else if (status == AUTO_WITHDRAWAL_STATUS_ENABLE) {
                    if (checkboxAutoWD.isChecked) {
                        updateAutoWithdrawalSchedule()
                    } else {
                        openOptOutAutoWDSettingDialog()
                    }
                }
                analytics.onSimpanButtonClick(true)
            } else {
                openJoinRPProgramBottomSheet()
                analytics.onSimpanButtonClick(false)
            }
        }
    }

    private fun openJoinRPProgramBottomSheet() {
        activity?.apply {
            ExclusiveRekPremFragment.show(supportFragmentManager)
        }
    }

    private fun openAddBankAccount() {
        activity?.apply {
            val intent = RouteManager.getIntent(this, ApplinkConstInternalGlobal.ADD_BANK)
            startActivityForResult(intent, BANK_SETTING_REQUEST_CODE)
        }
    }

    private fun verifyUserUsingOTP() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalGlobal.COTP)
        val bundle = Bundle()
        bundle.putString(ApplinkConstInternalGlobal.PARAM_EMAIL, userSession.get().email)
        bundle.putString(ApplinkConstInternalGlobal.PARAM_MSISDN, userSession.get().phoneNumber)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_CAN_USE_OTHER_METHOD, true)
        bundle.putInt(ApplinkConstInternalGlobal.PARAM_OTP_TYPE, OTP_TYPE_ADD_BANK_ACCOUNT)
        bundle.putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SHOW_CHOOSE_METHOD, true)
        intent.putExtras(bundle)
        startActivityForResult(intent, REQUEST_OTP_CODE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_OTP_CODE -> {
                    data?.let {
                        registerForAutoWithdrawal(data)
                    }
                }
            }
        }
    }

    fun refreshBankAccountList() {
        scrollViewAutoWDContent.visible()
        loaderView.visible()
        autoWDSettingsViewModel.getBankAccount()
    }

    private fun registerForAutoWithdrawal(data: Intent) {
        if (data.hasExtra(ApplinkConstInternalGlobal.PARAM_UUID)) {
            val uuid: String = data.getStringExtra(ApplinkConstInternalGlobal.PARAM_UUID)
            autoWithdrawalUpsertRequest?.let {
                it.token = uuid
                upsertAutoWithdrawalSchedule(it)
            }
        }
    }

    private fun openOptOutAutoWDSettingDialog() {
        context?.apply {
            DialogUnify(context = this,
                    actionType = DialogUnify.HORIZONTAL_ACTION,
                    imageType = DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.swd_awd_disable_autowd))
                setDescription(getString(R.string.swd_awd_auto_withdrawal_will_not_work))
                setPrimaryCTAText(getString(R.string.swd_deactivate))
                setSecondaryCTAText(getString(R.string.swd_back))
                setPrimaryCTAClickListener {
                    optOutFromAutoWithdrawal()
                    this.dismiss()
                    analytics.onOptOutDialogAction(EVENT_LABEL_CONTINUE)

                }
                setSecondaryCTAClickListener {
                    this.dismiss()
                    analytics.onOptOutDialogAction(EVENT_LABEL_DISMISS)
                }
                show()
            }
            analytics.onViewOptOutDialog()
        }
    }

    private fun optOutFromAutoWithdrawal() {
        autoWDStatusData?.let {
            upsertAutoWithdrawalSchedule(AutoWithdrawalUpsertRequest(it,
                    currentSchedule, null, primaryBankAccount,
                    false, null, true))
        }
    }

    private fun updateAutoWithdrawalSchedule() {
        autoWDStatusData?.let {
            upsertAutoWithdrawalSchedule(AutoWithdrawalUpsertRequest(
                    it,
                    currentSchedule, requestedSchedule, primaryBankAccount,
                    true, null, false
            ))
        }
    }

    private fun upsertAutoWithdrawalSchedule(autoWithdrawalUpsertRequest: AutoWithdrawalUpsertRequest) {
        loaderView.visible()
        autoWDSettingsViewModel.upsertAutoWithdrawal(autoWithdrawalUpsertRequest)
    }

    private fun onAutoWithdrawalUpsertFailed(message: String) {
        loaderView.gone()
        view?.apply {
            Toaster.make(this, message, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR)
        }
    }

    companion object {
        private const val AUTO_WITHDRAWAL_STATUS_NEW = 0
        private const val AUTO_WITHDRAWAL_STATUS_ENABLE = 1
        private const val AUTO_WITHDRAWAL_STATUS_DISABLE = 2

        private const val BANK_STATUS_REJECTED_PENDING = 2
        private const val BANK_STATUS_REJECTED_FRAUD = 3
        private const val BANK_STATUS_PENDING_FRAUD_VERIFICATION = 5

        private val OTP_TYPE_ADD_BANK_ACCOUNT = 146
        private const val REQUEST_OTP_CODE = 131
        const val BANK_SETTING_REQUEST_CODE = 132

        fun getInstance(bundle: Bundle): AutoWithdrawalSettingsFragment = AutoWithdrawalSettingsFragment()
                .apply {
                    arguments = bundle
                }
    }

}
