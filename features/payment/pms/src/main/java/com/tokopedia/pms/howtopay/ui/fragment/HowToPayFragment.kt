package com.tokopedia.pms.howtopay.ui.fragment

import android.annotation.SuppressLint
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
import com.tokopedia.kotlin.extensions.view.EMPTY
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.pms.R
import com.tokopedia.pms.databinding.PmsHwpInfoBinding
import com.tokopedia.pms.howtopay.analytics.HowToPayAnalytics
import com.tokopedia.pms.howtopay.data.model.AppLinkPaymentInfo
import com.tokopedia.pms.howtopay.data.model.HowToPayData
import com.tokopedia.pms.howtopay.data.model.HtpPaymentChannel
import com.tokopedia.pms.howtopay.ui.adapter.InstructionAdapter
import com.tokopedia.pms.howtopay.ui.adapter.MultiChannelAdapter
import com.tokopedia.pms.howtopay.ui.adapter.viewHolder.NonScrollLinerLayoutManager
import com.tokopedia.pms.howtopay.ui.viewmodel.HowToPayViewModel
import com.tokopedia.pms.howtopay.util.ScreenshotHelper
import com.tokopedia.pms.paymentlist.di.PmsComponent
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.htmltags.HtmlUtil
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HowToPayFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var userSessionInterface: dagger.Lazy<UserSessionInterface>

    @Inject
    lateinit var howToPayAnalytics: dagger.Lazy<HowToPayAnalytics>

    private var binding by autoClearedNullable<PmsHwpInfoBinding>()

    private lateinit var appLinkPaymentInfo: AppLinkPaymentInfo

    private val screenshotHelper: ScreenshotHelper by lazy(LazyThreadSafetyMode.NONE) {
        ScreenshotHelper { showToast(getString(R.string.pms_hwp_screenshot_success)) }
    }

    private val howToPayViewModel: HowToPayViewModel by lazy(LazyThreadSafetyMode.NONE) {
        ViewModelProvider(this, viewModelFactory.get()).get(HowToPayViewModel::class.java)
    }

    override fun getScreenName() = ""
    override fun initInjector() =
        getComponent(PmsComponent::class.java).inject(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments == null || !userSessionInterface.get().isLoggedIn) {
            activity?.finish()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = PmsHwpInfoBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            observeLiveData()
            binding?.loaderViewHowToPay?.visible()
            howToPayViewModel.getAppLinkPaymentInfoData(it)
        }
    }

    private fun observeLiveData() {
        howToPayViewModel.appLinkPaymentLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onAppLinkPaymentInfoLoaded(it.data)
                    is Fail -> onAppLinkParsingError()
                }
            }
        )

        howToPayViewModel.howToPayLiveData.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> onInstructionLoaded(it.data)
                    is Fail -> onInstructionLoadingFailed()
                }
            }
        )
    }

    private fun onAppLinkParsingError() {
        binding?.loaderViewHowToPay?.gone()
        activity?.finish()
        ServerLogger.log(
            Priority.P2,
            "HOW_TO_PAY",
            mapOf(
                "type" to "validation",
                "reason" to "bundle_app_link_parsing_error",
                "data" to arguments.toString().take(TIMBER_CHAR_MAX_LIMIT)
            )
        )
    }

    private fun onAppLinkPaymentInfoLoaded(appLinkPaymentInfo: AppLinkPaymentInfo) {
        this.appLinkPaymentInfo = appLinkPaymentInfo
        howToPayViewModel.getGqlHtpInstructions(appLinkPaymentInfo)
    }

    private fun onInstructionLoadingFailed() {
        binding?.run {
            loaderViewHowToPay.gone()
            globalErrorHowToPay.visible()
            globalErrorHowToPay.errorAction.setOnClickListener { activity?.finish() }
            ServerLogger.log(
                Priority.P2,
                "HOW_TO_PAY",
                mapOf(
                    "type" to "validation",
                    "reason" to "page_not_found",
                    "data" to appLinkPaymentInfo.toString().take(TIMBER_CHAR_MAX_LIMIT)
                )
            )
        }
    }

    private fun onInstructionLoaded(data: HowToPayData) {
        binding?.run {
            loaderViewHowToPay.gone()
            scrollViewHowToPay.visible()
            howToPayAnalytics.get().eventOnScreenOpen(data.gatewayCode)
            setHeaderData(data)
            setInstructionList(data)
        }
    }

    private fun setInstructionList(data: HowToPayData) {
        data.helpPageData?.channelList?.let { channelList ->
            if (data.isOfflineStore || data.isManualTransfer) {
                addSingleChannelInstruction(
                    channelList.getOrNull(0)?.channelSteps
                        ?: arrayListOf()
                )
            } else {
                addMultipleChannelAdapter(channelList)
            }
        }
    }

    private fun setHeaderData(data: HowToPayData) {
        val paymentCodeHeading = getPaymentCodeHeading(data.paymentCodeHint)

        val totalAmount = CurrencyFormatUtil.convertPriceValueToIdrFormat(
            howToPayViewModel.getTotalTransactionAmount(data),
            false
        )

        val displayAmount = if (data.isManualTransfer) {
            highlightLastThreeDigits(totalAmount)
        } else {
            totalAmount
        }

        setCommonPaymentDetails(
            data.gatewayName,
            data.gatewayImage,
            paymentCodeHeading,
            data.transactionCode,
            displayAmount
        )
        when {
            data.isManualTransfer -> addBankTransferPayment(data)
            data.isOfflineStore -> addStoreTransferPayment(data)
            else -> {
                setActionInfo(data, getString(R.string.pms_hwp_copy), IconUnify.COPY) {
                    copyToClipBoard(context, data.transactionCode, data.gatewayCode)
                    showToast(getString(R.string.pms_hwp_common_copy_success, paymentCodeHeading))
                }

                if (data.helpPageData?.isVA() == true) addVATicker()
            }
        }
    }

    private fun setCommonPaymentDetails(
        gatewayName: String,
        gatewayImage: String,
        paymentCodeHeading: String,
        paymentCode: String,
        displayAmount: CharSequence?
    ) {
        binding?.run {
            tvPaymentAccountTitle.text = paymentCodeHeading
            tvGateWayName.text = gatewayName
            tvPaymentAccountNumber.text = paymentCode
            tvTotalPaymentAmount.text = displayAmount

            ivGateWayImage.scaleType = ImageView.ScaleType.CENTER_INSIDE
            when {
                gatewayImage.isNotBlank() ->
                    ivGateWayImage.setImageUrl(gatewayImage, ivGateWayImage.heightRatio)
                else -> ivGateWayImage.gone()
            }
        }
    }

    private fun addBankTransferPayment(data: HowToPayData) {
        binding?.run {
            val bankInfo = getString(
                R.string.pms_hwp_bank_info,
                data.destAccountName,
                data.destBankBranch
            )
            tvAccountName.visible()
            tvAccountName.text = bankInfo
            tickerAmountNote.visible()
            tickerAmountNote.tickerTitle = getString(R.string.pms_htp_pending)
            tickerAmountNote.setTextDescription(
                HtmlUtil
                    .fromHtml(getString(R.string.pms_manual_transfer_instruction)).trim()
            )

            setActionInfo(data, getString(R.string.pms_hwp_copy), IconUnify.COPY) {
                copyToClipBoard(context, data.transactionCode, data.gatewayCode)
                showToast(getString(R.string.pms_hwp_common_copy_success, getPaymentCodeHeading(data.paymentCodeHint)))
            }
        }
    }

    private fun addVATicker() {
        binding?.run {
            tickerAmountNote.visible()
            tickerAmountNote.tickerTitle = String.EMPTY
            tickerAmountNote.setTextDescription(getString(R.string.pms_va_ticker_description))
        }
    }

    private fun addStoreTransferPayment(data: HowToPayData) {
        binding?.run {
            setActionInfo(data, getString(R.string.pms_hwp_screenshot), IconUnify.DOWNLOAD) {
                takeScreenShot(data.gatewayCode)
            }
            tvStorePaymentNote.visible()
            context?.let {
                tvStorePaymentNote.movementMethod = LinkMovementMethod.getInstance()
                tvStorePaymentNote.text = getSpannableStoreNote(it)
            }
        }
    }

    private fun takeScreenShot(gatewayCode: String) {
        if (::appLinkPaymentInfo.isInitialized) {
            howToPayAnalytics.get().eventOnScreenShotClick(gatewayCode)
        }
        screenshotHelper.takeScreenShot(view, this)
    }

    private fun setActionInfo(
        howToPayData: HowToPayData,
        actionText: String?,
        actionIcon: Int,
        clickAction: (() -> Unit)
    ) {
        binding?.run {
            // for account number copy
            if (howToPayData.isOfflineStore || !howToPayData.hideCopyAccountNum) {
                ivTakeScreenshot.setImage(actionIcon)
                ivTakeScreenshot.setOnClickListener { clickAction.invoke() }

                tvPaymentInfoAction.text = actionText
                tvPaymentInfoAction.setOnClickListener { clickAction.invoke() }
            } else {
                ivTakeScreenshot.gone()
                tvPaymentInfoAction.gone()
            }
            // for amount copy
            if (!howToPayData.hideCopyAmount) {
                val totalAmount = howToPayViewModel.getTotalTransactionAmount(howToPayData)
                tvPaymentAmountAction.text = getString(R.string.pms_hwp_copy)
                ivAmountAction.setImage(IconUnify.COPY)
                tvPaymentAmountAction.setOnClickListener {
                    copyToClipBoard(context, totalAmount.toLong().toString(), howToPayData.gatewayCode)
                    showToast(getString(R.string.pms_hwp_amount_copy_success))
                }
                ivAmountAction.setOnClickListener {
                    copyToClipBoard(context, totalAmount.toLong().toString(), howToPayData.gatewayCode)
                    showToast(getString(R.string.pms_hwp_amount_copy_success))
                }
            } else {
                tvPaymentAmountAction.gone()
                ivAmountAction.gone()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addMultipleChannelAdapter(paymentChannels: ArrayList<HtpPaymentChannel>?) {
        binding?.run {
            if (!paymentChannels.isNullOrEmpty()) {
                recyclerView.layoutManager = NonScrollLinerLayoutManager(activity as Context)
                recyclerView.adapter = MultiChannelAdapter(paymentChannels)
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun addSingleChannelInstruction(instructions: ArrayList<String>) {
        binding?.run {
            recyclerView.layoutManager = NonScrollLinerLayoutManager(activity as Context)
            recyclerView.adapter = InstructionAdapter(instructions, null)
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun highlightLastThreeDigits(amountStr: String): SpannableString {
        val spannable = SpannableString(amountStr)
        context?.let {
            if (amountStr.length > HIGHLIGHT_DIGIT_COUNT) {
                val startIndex = spannable.length - HIGHLIGHT_DIGIT_COUNT
                spannable.setSpan(
                    ForegroundColorSpan(
                        ContextCompat.getColor(
                            it,
                            unifyprinciplesR.color.Unify_GN500
                        )
                    ),
                    startIndex,
                    spannable.length,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )
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
        val color =
            ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)
        spannableString.setSpan(color, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        spannableString.setSpan(
            object : ClickableSpan() {
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
            },
            startIndex,
            endIndex,
            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return SpannableStringBuilder.valueOf(storeNote).append(" ").append(spannableString)
    }

    private fun copyToClipBoard(context: Context?, dataStr: String, gatewayCode: String) {
        context?.let {
            try {
                val extraSpaceRegexStr = "\\s+".toRegex()
                val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE)
                    as ClipboardManager
                val clip = ClipData.newPlainText(
                    COPY_BOARD_LABEL,
                    dataStr.replace(extraSpaceRegexStr, "")
                )
                clipboard.setPrimaryClip(clip)
            } catch (e: Exception) {
            }
            if (::appLinkPaymentInfo.isInitialized) {
                howToPayAnalytics.get().eventOnCopyCodeClick(gatewayCode)
            }
        }
    }

    private fun showToast(message: String) {
        view?.let { Toaster.build(it, message, Toaster.LENGTH_SHORT).show() }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        screenshotHelper.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
    }

    private fun getPaymentCodeHeading(paymentCodeHint: String) =
        if (paymentCodeHint.isEmpty()) {
            getString(R.string.pms_hwp_account_number)
        } else {
            paymentCodeHint
        }

    override fun onDestroyView() {
        screenshotHelper.cancel()
        super.onDestroyView()
    }

    companion object {
        private const val COPY_BOARD_LABEL = "Tokopedia"
        private const val HIGHLIGHT_DIGIT_COUNT = 3
        private const val TIMBER_CHAR_MAX_LIMIT = 1000

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
