package com.tokopedia.withdraw.auto_withdrawal.presentation.fragment

import android.content.Context
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.text.*
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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.withdraw.R
import com.tokopedia.withdraw.auto_withdrawal.di.component.AutoWithdrawalComponent
import com.tokopedia.withdraw.auto_withdrawal.domain.model.AutoWDStatusData
import com.tokopedia.withdraw.auto_withdrawal.domain.model.BankAccount
import com.tokopedia.withdraw.auto_withdrawal.domain.model.GetInfoAutoWD
import com.tokopedia.withdraw.auto_withdrawal.domain.model.Schedule
import com.tokopedia.withdraw.auto_withdrawal.presentation.activity.AutoWithdrawalActivity
import com.tokopedia.withdraw.auto_withdrawal.presentation.viewModel.AutoWDSettingsViewModel
import kotlinx.android.synthetic.main.swd_fragment_awd_settings.*
import javax.inject.Inject

class AutoWithdrawalSettingsFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>
    var getInfoAutoWD: GetInfoAutoWD? = null
    var autoWDStatusData: AutoWDStatusData? = null
    var currentSchedule: Schedule? = null
    var requestedSchedule: Schedule? = null

    var primaryBankAccount: BankAccount? = null

    private val autoWDSettingsViewModel: AutoWDSettingsViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory.get())
        viewModelProvider.get(AutoWDSettingsViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.swd_fragment_awd_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setLoaderViewVisibility(true)
        observeViewModel()
        autoWDSettingsViewModel.getAutoWDInfo()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        getComponent(AutoWithdrawalComponent::class.java).inject(this)
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
    }

    private fun onAutoWithdrawalStatusLoaded(autoWDStatusData: AutoWDStatusData) {
        setLoaderViewVisibility(false)
        groupAutoWDSaveSetting.gone()
        this.autoWDStatusData = autoWDStatusData
        scrollViewAutoWDContent.visible()
        autoWDStatusData.apply {
            if (isOwner) {
                tickerAutoWD.gone()
                if (status == 0)
                    setBannerViewVisibility(true)
                else
                    setBannerViewVisibility(false)
            } else {
                tickerAutoWD.visible()
                tickerAutoWD.setTextDescription(getString(R.string.swd_auto_wd_user_not_owner))
            }
            setPrimaryBankAccount(this)
            setScheduleData(this)
        }
        checkboxAutoWD.setOnCheckedChangeListener { _, isChecked ->
            setPrimaryBankAccount(autoWDStatusData)
            enableScheduleSection(isChecked)
            showSaveButton()
        }
    }

    private fun showSaveButton() {
        if (primaryBankAccount != null && currentSchedule != null) {
            if ((checkboxAutoWD.isChecked && currentSchedule?.status != 1)
                    || (!checkboxAutoWD.isChecked && currentSchedule?.status == 1)
            || (requestedSchedule != currentSchedule))
                setSaveSettingBottomViewVisibility(true)
            else
                setSaveSettingBottomViewVisibility(false)
        } else {
            setSaveSettingBottomViewVisibility(false)
        }
    }

    private fun setScheduleData(autoWDStatusData: AutoWDStatusData) {
        currentSchedule = autoWDStatusData.schedule[0]
        //todo commented for testing purpose
        /*autoWDStatusData.schedule.forEach {
            if (it.status == 1) {
                currentSchedule = it
            }
        }*/
        currentSchedule?.apply {
            tvAutoWDScheduleType.text = title
            tvScheduleTiming.text = desc
            checkboxAutoWD.isChecked = status == 1
            enableScheduleSection(status == 1 && autoWDStatusData.isOwner)
            enableBankAccountSection(status == 1 && autoWDStatusData.isOwner)
        }
    }

    private fun onBankAccountLoaded(accountList: ArrayList<BankAccount>) {
        accountList.forEach lit@{ bankAccount ->
            if (bankAccount.isDefaultBank == 1) {
                primaryBankAccount = bankAccount
                return@lit
            }
        }
    }

    private fun setPrimaryBankAccount(autoWDStatusData: AutoWDStatusData) {
        primaryBankAccount?.apply {
            tvAutoWDBankName.text = bankName
            tvAutoWdBankAccountDetail.text = "$accountNo - $accountName"
            if (status == 2 || status == 3 || status == 5) {
                enableBankAccountData(false)
                btnAutoWDSaveSettings.isEnabled = false
            } else {
                enableBankAccountSection(autoWDStatusData.isOwner && checkboxAutoWD.isChecked)
            }
            tvAutoWdBankNote.text = copyWriting ?: ""
        } ?: run {
            //todo no bank account case...
        }
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

    private fun setActionMenu() {
        val headerUnify = (activity as AutoWithdrawalActivity).getHeader()
        headerUnify.actionText = getString(R.string.swd_info)
        headerUnify.actionTextView?.setOnClickListener { openInfoBottomSheet() }
    }

    private fun setSaveSettingBottomViewVisibility(isVisible: Boolean) {
        if (isVisible) {
            groupAutoWDSaveSetting.visible()
            context?.let { context ->
                setTermsAndConditionSpannable(context)
            }
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

    private fun enableAutoWDSettingSection(isEnable: Boolean, isAutoWdOn: Boolean) {
        context?.let { context ->
            checkboxAutoWD.setChecked(true)
            CompoundButtonCompat.getButtonDrawable(checkboxAutoWD)
            checkboxAutoWD.isEnabled = false
            if (isEnable) {
                val textHeadingColor = ContextCompat.getColor(context, R.color.Neutral_N700_96)
                val textBodyColor = ContextCompat.getColor(context, R.color.Neutral_N700_68)
                tvAutoWDTitle.setTextColor(textHeadingColor)
                tvAutoWDAllBalanceAccording.setTextColor(textBodyColor)
                tvScheduleTiming.setTextColor(textBodyColor)
                tvChangeAutoWDSchedule.visible()
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
                //todo openTermsAndConditionBottomSheet()

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
                //todo openTermsAndConditionBottomSheet()

            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        tvAutoWDTermsAndCond.text = SpannableStringBuilder.valueOf(originalText).append(" ")
                .append(spannableString)
    }

    private fun openInfoBottomSheet() {

    }

    companion object {

        fun getInstance(bundle: Bundle): AutoWithdrawalSettingsFragment = AutoWithdrawalSettingsFragment().apply {
            arguments = bundle
        }
    }
}