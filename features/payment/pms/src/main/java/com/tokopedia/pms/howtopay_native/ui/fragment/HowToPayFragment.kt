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
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pms.R
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
import kotlinx.android.synthetic.main.pms_hwp_info.*
import org.json.JSONObject
import javax.inject.Inject


class HowToPayFragment : BaseDaggerFragment() {

    private var appLinkPaymentInfo: AppLinkPaymentInfo? = null
    private val HIGHLIGHT_DIGIT_COUNT = 3

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

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
        if (arguments != null) {
            appLinkPaymentInfo = getAppLinkPaymentInfoData(arguments!!)
            if (appLinkPaymentInfo == null)
                activity?.finish()
        } else
            activity?.finish()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.pms_hwp_info, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeLiveData()
        howToPayViewModel.getHowToPayInstruction(appLinkPaymentInfo!!)
    }

    private fun observeLiveData() {
        howToPayViewModel.mutableLiveData.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> onInstructionLoaded(it.data)
                is Fail -> onInstructionLoadingFailed(it.throwable)
            }
        })
    }

    private fun onInstructionLoadingFailed(throwable: Throwable) {

    }

    private fun onInstructionLoaded(data: HowToPayInstruction) {
        when (data) {
            is MultiChannelGatewayResult -> {
                when (data.type) {
                    is VirtualAccount -> addVirtualAccountPayment(data)
                    is Syariah -> addSyariahPayment(data)
                }

                addMultipleChannelAdapter(data.gateway.paymentChannels)
            }
            is SingleChannelGatewayResult -> {
                when (data.type) {
                    is BankTransfer -> addBankTransferPayment(data)
                    is Store -> addStoreTransferPayment(data)
                    is KlickBCA -> addKlikBCAPayment(data)
                }
                addSingleChannelInstruction(data.instructions)
            }
        }
    }

    private fun addVirtualAccountPayment(data: MultiChannelGatewayResult) {
        tvGateWayName.text = appLinkPaymentInfo?.gateway_name
        tvPaymentAccountTitle.text = getString(R.string.pms_hwp_VA_code)
        tvPaymentAccountNumber.text = appLinkPaymentInfo?.payment_code
        tvTotalPaymentAmount.text = getString(R.string.pms_hwp_rp, appLinkPaymentInfo?.total_amount)
        tvPaymentInfoAction.text = getString(R.string.pms_hwp_copy)
        appLinkPaymentInfo?.gateway_logo?.let {
            ivGateWayImage.setImageUrl(it)
        }
        ivTakeScreenshot.setOnClickListener {
            copyTOClipBoard(context, appLinkPaymentInfo?.payment_code ?: "")
            showToast("Nomor virtual account berhasil disalin.")
        }
        tvPaymentInfoAction.setOnClickListener {
            copyTOClipBoard(context, appLinkPaymentInfo?.payment_code ?: "")
            showToast("Nomor virtual account berhasil disalin.")
        }
        ivTakeScreenshot.setImage(IconUnify.COPY)
        tickerAmountNote.gone()
        tvAccountName.gone()
        tvPaymentNote.gone()
    }

    private fun addSyariahPayment(data: MultiChannelGatewayResult) {
        tvGateWayName.text = appLinkPaymentInfo?.gateway_name
        tvPaymentAccountTitle.text = getString(R.string.pms_hwp_payment_code)
        tvPaymentAccountNumber.text = appLinkPaymentInfo?.payment_code
        tvTotalPaymentAmount.text = getString(R.string.pms_hwp_rp, appLinkPaymentInfo?.total_amount)
        tvPaymentInfoAction.text = getString(R.string.pms_hwp_copy)
        appLinkPaymentInfo?.gateway_logo?.let {
            ivGateWayImage.setImageUrl(it, ivGateWayImage.heightRatio)
        }
        ivTakeScreenshot.setImage(IconUnify.COPY)
        ivTakeScreenshot.setOnClickListener {
            copyTOClipBoard(context, appLinkPaymentInfo?.payment_code ?: "")
            showToast("Kode Pembayaran berhasil disalin.")
        }
        tvPaymentInfoAction.setOnClickListener {
            copyTOClipBoard(context, appLinkPaymentInfo?.payment_code ?: "")
            showToast("Kode Pembayaran berhasil disalin.")
        }
        tickerAmountNote.gone()
        tvAccountName.gone()
        tvPaymentNote.gone()
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
        recyclerView.adapter = InstructionAdapter(instructions)
        recyclerView.post {
            recyclerView.adapter?.notifyDataSetChanged()
        }
    }

    private fun addBankTransferPayment(data: SingleChannelGatewayResult) {
        tvGateWayName.text = appLinkPaymentInfo?.gateway_name
        tvPaymentAccountTitle.text = getString(R.string.pms_hwp_account_number)
        tvPaymentAccountNumber.text = appLinkPaymentInfo?.bank_num
        tvAccountName.visible()
        tvAccountName.text = getString(R.string.pms_hwp_bank_info,
                appLinkPaymentInfo?.bank_name, appLinkPaymentInfo?.bank_info)
        tvTotalPaymentAmount.text = highlightLastThreeDigits(getString(R.string.pms_hwp_rp,
                appLinkPaymentInfo?.total_amount))
        tvPaymentInfoAction.text = getString(R.string.pms_hwp_copy)
        appLinkPaymentInfo?.gateway_logo?.let {
            ivGateWayImage.setImageUrl(it)
        }
        ivTakeScreenshot.setImage(IconUnify.COPY)
        ivTakeScreenshot.setOnClickListener {
            copyTOClipBoard(context, appLinkPaymentInfo?.bank_num ?: "")
            showToast("Nomor rekening berhasil disalin.")
        }
        tvPaymentInfoAction.setOnClickListener {
            copyTOClipBoard(context, appLinkPaymentInfo?.bank_num ?: "")
            showToast("Nomor rekening berhasil disalin.")
        }
        tickerAmountNote.visible()
        tickerAmountNote.setTextDescription(getString(R.string.pms_hwp_transfer_3_digit_info))
        tvPaymentNote.gone()
    }

    private fun addStoreTransferPayment(data: SingleChannelGatewayResult) {
        tvGateWayName.text = appLinkPaymentInfo?.gateway_name
        tvPaymentAccountTitle.text = getString(R.string.pms_hwp_payment_code)
        tvPaymentAccountNumber.text = appLinkPaymentInfo?.payment_code
        tvTotalPaymentAmount.text = getString(R.string.pms_hwp_rp, appLinkPaymentInfo?.total_amount)
        tvPaymentInfoAction.text = getString(R.string.pms_hwp_screenshot)
        appLinkPaymentInfo?.gateway_logo?.let {
            ivGateWayImage.setImageUrl(it)
        }
        ivTakeScreenshot.setImage(IconUnify.DOWNLOAD)
        tickerAmountNote.gone()
        tvAccountName.gone()
        tvPaymentNote.visible()
        tvPaymentInfoAction.setOnClickListener { screenshotHelper.takeScreenShot(view, this) }
        ivTakeScreenshot.setOnClickListener { screenshotHelper.takeScreenShot(view, this) }
        context?.let {
            tvPaymentNote.movementMethod = LinkMovementMethod.getInstance()
            tvPaymentNote.text = getSpannableStoreNote(it)
        }
    }

    private fun addKlikBCAPayment(data: SingleChannelGatewayResult) {
        tvGateWayName.text = appLinkPaymentInfo?.gateway_name
        tvPaymentAccountTitle.text = getString(R.string.pms_hwp_user_id)
        tvPaymentAccountNumber.text = appLinkPaymentInfo?.payment_code
        tvTotalPaymentAmount.text = getString(R.string.pms_hwp_rp, appLinkPaymentInfo?.total_amount)
        appLinkPaymentInfo?.gateway_logo?.let {
            ivGateWayImage.setImageUrl(it)
        }
        tvPaymentInfoAction.gone()
        ivTakeScreenshot.gone()
        tickerAmountNote.gone()
        tvAccountName.gone()
        tvPaymentNote.gone()
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
                    StoreListBottomSheet().show(it.supportFragmentManager, "StoreListBottomSheet")
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

    private fun getAppLinkPaymentInfoData(bundle: Bundle): AppLinkPaymentInfo? {
        return try {
            val json = JSONObject()
            val keys = bundle.keySet()
            for (key in keys)
                json.put(key, bundle.getString(key, ""))
            val appLinkPaymentInfo: AppLinkPaymentInfo = Gson().fromJson(json.toString(), AppLinkPaymentInfo::class.java)
            if (appLinkPaymentInfo.bank_code.isNullOrBlank())
                appLinkPaymentInfo.bank_code = null
            return appLinkPaymentInfo
        } catch (e: Exception) {
            null
        }
    }

    private fun copyTOClipBoard(context: Context?, dataStr: String) {
        context?.let {
            val extraSpaceRegexStr = "\\s+".toRegex()
            val clipboard = context.getSystemService(Activity.CLIPBOARD_SERVICE)
                    as ClipboardManager
            val clip = ClipData.newPlainText(COPY_BOARD_LABEL,
                    dataStr.replace(extraSpaceRegexStr, ""))
            clipboard.setPrimaryClip(clip)
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