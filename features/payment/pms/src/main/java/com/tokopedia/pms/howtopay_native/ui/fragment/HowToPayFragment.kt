package com.tokopedia.pms.howtopay_native.ui.fragment

import android.app.Activity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.os.Bundle
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.pms.R
import com.tokopedia.pms.howtopay_native.analytics.HowToPayAnalytics
import com.tokopedia.pms.howtopay_native.data.model.*
import com.tokopedia.pms.howtopay_native.di.HowToPayComponent
import com.tokopedia.pms.howtopay_native.ui.adapter.InstructionAdapter
import com.tokopedia.pms.howtopay_native.ui.adapter.MultiChannelAdapter
import com.tokopedia.pms.howtopay_native.ui.adapter.viewHolder.NonScrollLinerLayoutManager
import com.tokopedia.pms.howtopay_native.ui.viewmodel.HowToPayViewModel
import com.tokopedia.pms.howtopay_native.util.ScreenshotHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.pms_hwp_info.*
import javax.inject.Inject


class HowToPayFragment : BaseDaggerFragment() {

    private lateinit var appLinkPaymentInfo: AppLinkPaymentInfo
    private val HIGHLIGHT_DIGIT_COUNT = 3

    private val TIMBER_CHAR_MAX_LIMIT = 1000


    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSessionInterface: dagger.Lazy<UserSessionInterface>

    @Inject
    lateinit var howToPayAnalytics: dagger.Lazy<HowToPayAnalytics>

    private val screenshotHelper: ScreenshotHelper by lazy(LazyThreadSafetyMode.NONE) {
        ScreenshotHelper { showToast(getString(R.string.pms_hwp_screenshot_success)) }
    }

    private val howToPayViewModel: HowToPayViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory.get()).get(HowToPayViewModel::class.java)
    }

    override fun getScreenName(): String {
        return ""
    }

    override fun initInjector() {
        getComponent(HowToPayComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments == null || !userSessionInterface.get().isLoggedIn) {
            activity?.finish()
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pms_hwp_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            observeLiveData()
            loaderViewHowToPay.visible()
            howToPayViewModel.getAppLinkPaymentInfoData(it)
        }
    }

    private fun observeLiveData() {
        howToPayViewModel.appLinkPaymentLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onAppLinkPaymentInfoLoaded(it.data)
                is Fail -> onAppLinkParsingError()
            }
        })

        howToPayViewModel.howToPayLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onInstructionLoaded(it.data)
                is Fail -> onInstructionLoadingFailed()
            }
        })
    }

    private fun onAppLinkParsingError() {
        loaderViewHowToPay.gone()
        activity?.finish()
        ServerLogger.log(Priority.P2, "HOW_TO_PAY", mapOf("type" to "validation",
                "reason" to "bundle_app_link_parsing_error",
                "data" to arguments.toString().take(TIMBER_CHAR_MAX_LIMIT)
        ))
    }

    private fun onAppLinkPaymentInfoLoaded(appLinkPaymentInfo: AppLinkPaymentInfo) {
        this.appLinkPaymentInfo = appLinkPaymentInfo
        howToPayAnalytics.get().eventOnScreenOpen(appLinkPaymentInfo.payment_type)
        howToPayViewModel.getHowToPayInstruction(appLinkPaymentInfo)
    }

    private fun onInstructionLoadingFailed() {
        loaderViewHowToPay.gone()
        globalErrorHowToPay.visible()
        globalErrorHowToPay.errorAction.setOnClickListener { activity?.finish() }
        ServerLogger.log(Priority.P2, "HOW_TO_PAY", mapOf("type" to "validation",
                "reason" to "page_not_found",
                "data" to appLinkPaymentInfo.toString().take(TIMBER_CHAR_MAX_LIMIT)
        ))
    }

    private fun onInstructionLoaded(data: HowToPayInstruction) {
        loaderViewHowToPay.gone()
        scrollViewHowToPay.visible()
        when (data) {
            is MultiChannelGatewayResult -> {
                when (data.type) {
                    is VirtualAccount -> addVirtualAccountPayment()
                    is Syariah -> addSyariahPayment()
                }
                addMultipleChannelAdapter(data.gateway.paymentChannels)
            }
            is SingleChannelGatewayResult -> {
                when (data.type) {
                    is BankTransfer -> addBankTransferPayment()
                    is Store -> addStoreTransferPayment(data)
                    is KlickBCA -> addKlikBCAPayment()
                }
                addSingleChannelInstruction(data.instructions)
            }
        }
    }

    private fun addVirtualAccountPayment() {
        setPaymentInfo(getString(R.string.pms_hwp_VA_code),
                appLinkPaymentInfo.payment_code,
                getString(R.string.pms_hwp_rp, appLinkPaymentInfo.total_amount),
                getString(R.string.pms_hwp_copy),
                IconUnify.COPY) {
            copyTOClipBoard(context, appLinkPaymentInfo.payment_code)
            showToast(getString(R.string.pms_hwp_va_copy_success))
        }
    }

    private fun addSyariahPayment() {
        setPaymentInfo(getString(R.string.pms_hwp_payment_code),
                appLinkPaymentInfo.payment_code,
                getString(R.string.pms_hwp_rp, appLinkPaymentInfo.total_amount),
                getString(R.string.pms_hwp_copy),
                IconUnify.COPY) {
            copyTOClipBoard(context, appLinkPaymentInfo.payment_code)
            showToast(getString(R.string.pms_hwp_code_copy_success))
        }
    }

    private fun addBankTransferPayment() {
        setPaymentInfo(getString(R.string.pms_hwp_account_number),
                appLinkPaymentInfo.bank_num,
                highlightLastThreeDigits(getString(R.string.pms_hwp_rp,
                        appLinkPaymentInfo.total_amount)),
                getString(R.string.pms_hwp_copy),
                IconUnify.COPY) {
            copyTOClipBoard(context, appLinkPaymentInfo.payment_code)
            showToast(getString(R.string.pms_hwp_bank_account_number_copy))
        }
        tvAccountName.visible()
        tvAccountName.text = getString(R.string.pms_hwp_bank_info,
                appLinkPaymentInfo.bank_name, appLinkPaymentInfo.bank_info)
        tickerAmountNote.visible()
        tickerAmountNote.setTextDescription(getString(R.string.pms_hwp_transfer_3_digit_info))
    }

    private fun addStoreTransferPayment(data: SingleChannelGatewayResult) {

        setPaymentInfo(getString(R.string.pms_hwp_payment_code),
                appLinkPaymentInfo.payment_code,
                getString(R.string.pms_hwp_rp, appLinkPaymentInfo.total_amount),
                getString(R.string.pms_hwp_screenshot),
                IconUnify.DOWNLOAD) {
            takeScreenShot()
        }
        tvStorePaymentNote.visible()
        context?.let {
            tvStorePaymentNote.movementMethod = LinkMovementMethod.getInstance()
            tvStorePaymentNote.text = getSpannableStoreNote(it)
        }
    }


    private fun takeScreenShot() {
        if (::appLinkPaymentInfo.isInitialized)
            howToPayAnalytics.get().eventOnScreenShotClick(appLinkPaymentInfo.payment_type)
        screenshotHelper.takeScreenShot(view, this)
    }

    private fun addKlikBCAPayment() {
        setPaymentInfo(getString(R.string.pms_hwp_user_id),
                appLinkPaymentInfo.payment_code,
                getString(R.string.pms_hwp_rp, appLinkPaymentInfo.total_amount),
                null, null, null)
    }

    private fun setPaymentInfo(accountTitle: String,
                               accountNumber: String,
                               amountStr: CharSequence,
                               actionText: String?,
                               actionIcon: Int?, clickAction: (() -> Unit)?) {
        tvPaymentAccountTitle.text = accountTitle

        tvGateWayName.text = appLinkPaymentInfo.gateway_name
        tvPaymentAccountNumber.text = accountNumber
        tvTotalPaymentAmount.text = amountStr

        ivGateWayImage.scaleType = ImageView.ScaleType.CENTER_INSIDE

        val bankLogo = appLinkPaymentInfo.bank_logo ?: ""
        val gatewayLogo = appLinkPaymentInfo.gateway_logo ?: ""
        when {
            bankLogo.isNotBlank() -> {
                ivGateWayImage.setImageUrl(bankLogo, ivGateWayImage.heightRatio)
            }
            gatewayLogo.isNotBlank() -> {
                ivGateWayImage.setImageUrl(gatewayLogo, ivGateWayImage.heightRatio)
            }
            else -> {
                ivGateWayImage.gone()
            }
        }

        actionIcon?.let {
            ivTakeScreenshot.setImage(actionIcon)
            ivTakeScreenshot.setOnClickListener { clickAction?.invoke() }
        } ?: run {
            ivTakeScreenshot.gone()
        }

        actionText?.let {
            tvPaymentInfoAction.text = actionText
            tvPaymentInfoAction.setOnClickListener { clickAction?.invoke() }
        } ?: run {
            tvPaymentInfoAction.gone()
        }
    }

    private fun addMultipleChannelAdapter(paymentChannels: ArrayList<PaymentChannel>?) {
        if (!paymentChannels.isNullOrEmpty()) {
            recyclerView.layoutManager = NonScrollLinerLayoutManager(activity as Context)
            recyclerView.adapter = MultiChannelAdapter(paymentChannels)
            recyclerView.post { recyclerView.adapter?.notifyDataSetChanged() }
        }
    }

    private fun addSingleChannelInstruction(instructions: ArrayList<String>) {
        recyclerView.layoutManager = NonScrollLinerLayoutManager(activity as Context)
        recyclerView.adapter = InstructionAdapter(instructions, null)
        recyclerView.post {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun highlightLastThreeDigits(amountStr: String): SpannableString {
        val spannable = SpannableString(amountStr)
        context?.let {
            if (amountStr.length > HIGHLIGHT_DIGIT_COUNT) {
                val startIndex = spannable.length - HIGHLIGHT_DIGIT_COUNT
                spannable.setSpan(ForegroundColorSpan(ContextCompat.getColor(it, com.tokopedia.unifycomponents.R.color.Unify_G500)),
                        startIndex, spannable.length,
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            }
        }
        return spannable
    }

    private fun getSpannableStoreNote(context: Context): SpannableStringBuilder {
        val storeNote = getString(R.string.pms_hwp_pay_other_store_info)
        val viewStore = getString(R.string.pms_hwp_view_store_list)
        val spannableString = SpannableString(viewStore)
        val startIndex = 0
        val endIndex = spannableString.length
        val color = ContextCompat.getColor(context, com.tokopedia.unifycomponents.R.color.Unify_G500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(object : ClickableSpan() {
            override fun onClick(widget: View) {
                activity?.let {
                    StoreListBottomSheet.showStoreList(it.supportFragmentManager)
                }
            }

            override fun updateDrawState(ds: TextPaint) {
                super.updateDrawState(ds)
                ds.isUnderlineText = false
                ds.color = color
            }
        }, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        return SpannableStringBuilder.valueOf(storeNote).append(" ").append(spannableString)
    }

    private fun copyTOClipBoard(context: Context?, dataStr: String) {
        context?.let {
            try {
                val extraSpaceRegexStr = "\\s+".toRegex()
                val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE)
                        as ClipboardManager
                val clip = ClipData.newPlainText(COPY_BOARD_LABEL,
                        dataStr.replace(extraSpaceRegexStr, ""))
                clipboard.setPrimaryClip(clip)
            }catch (e : Exception){}
            if (::appLinkPaymentInfo.isInitialized)
                howToPayAnalytics.get().eventOnCopyCodeClick(appLinkPaymentInfo.payment_type)
        }
    }

    private fun showToast(message: String) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT).show()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenshotHelper.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
    }

    override fun onDestroyView() {
        screenshotHelper.cancel()
        super.onDestroyView()
    }

    companion object {
        private val COPY_BOARD_LABEL = "Tokopedia"
        fun getInstance(activity: Activity, bundle: Bundle?): HowToPayFragment? {
            if (bundle == null) {
                activity.finish()
                return null
            }
            return HowToPayFragment().apply {
                arguments = bundle
            }
        }
    }

}
