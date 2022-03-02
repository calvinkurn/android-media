package com.tokopedia.vouchercreation.shop.voucherlist.view.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.shop.voucherlist.model.ui.VoucherUiModel

class BroadCastVoucherBottomSheet : BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(voucherUiModel: VoucherUiModel): BroadCastVoucherBottomSheet {
            return BroadCastVoucherBottomSheet().apply {
                setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)

                arguments = Bundle().apply {
                    putParcelable(VOUCHER, voucherUiModel)
                }
            }
        }
        const val TAG: String = "SuccessCreateBottomSheet"
        private const val TPD_VOUCHER_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/mvc/mvc_voucher.png"
        private const val DATE_FORMAT = "dd MMMM yyyy"
        private const val HOUR_FORMAT = "HH:mm"
        private const val VOUCHER = "voucher"
    }

    private val voucherUiModel by lazy {
        arguments?.getParcelable<VoucherUiModel?>(VOUCHER)
    }

    private var tkpdVoucherView: ImageUnify? = null
    private var voucherPeriodInfoView: Typography? = null
    private var broadCastButton: View? = null
    private var socialMediaShareButton: View? = null
    private var freeTextView: Typography? = null

    private var onShareClickAction: (VoucherUiModel) -> Unit = {}
    private var onBroadCastClickAction: (VoucherUiModel) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet(container)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun initBottomSheet(container: ViewGroup?) {
        val child = LayoutInflater.from(context)
            .inflate(R.layout.bottomsheet_mvc_broadcast_voucher, container, false)
        setChild(child)
    }

    private fun setupView(view: View) {
        // load tokopedia voucher image
        tkpdVoucherView = view.findViewById(R.id.iu_tkpd_voucher)
        tkpdVoucherView?.loadImage(TPD_VOUCHER_IMAGE_URL)
        // voucher period info setup
        voucherPeriodInfoView = view.findViewById(R.id.tgp_voucher_period_info)
        voucherUiModel?.let { uiModel ->
            val startDate = DateTimeUtils.reformatUnsafeDateTime(uiModel.startTime, DATE_FORMAT)
            val endDate = DateTimeUtils.reformatUnsafeDateTime(uiModel.finishTime, DATE_FORMAT)
            val startHour = String.format(
                context?.getString(R.string.mvc_hour_wib).toBlankOrString(),
                DateTimeUtils.reformatUnsafeDateTime(uiModel.startTime, HOUR_FORMAT)
            )
            val endHour = String.format(
                context?.getString(R.string.mvc_hour_wib).toBlankOrString(),
                DateTimeUtils.reformatUnsafeDateTime(uiModel.finishTime, HOUR_FORMAT)
            )
            val startPeriod = "$startDate, $startHour"
            val endPeriod = "$endDate, $endHour"
            val voucherPeriod = "$startPeriod - $endPeriod"
            val voucherPeriodInfo = String.format(
                context?.getString(R.string.mvc_voucher_period_info).toBlankOrString(),
                uiModel.name,
                voucherPeriod
            )
            voucherPeriodInfoView?.text = voucherPeriodInfo
        }

        // broad cast button setup
        broadCastButton = view.findViewById(R.id.broadcast_button)
        broadCastButton?.setOnClickListener {
            voucherUiModel?.run(onBroadCastClickAction)
        }

        // free text view setup
        freeTextView = view.findViewById(R.id.tgp_free)
        voucherUiModel?.run {
            if (isFreeIconVisible) freeTextView?.show()
            else freeTextView?.hide()
        }

        // social media button setup
        socialMediaShareButton = view.findViewById(R.id.social_media_share_button)
        val isPublic = voucherUiModel?.isPublic ?: false
        if (isPublic) socialMediaShareButton?.hide()
        socialMediaShareButton?.setOnClickListener {
            voucherUiModel?.run(onShareClickAction)
        }
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }

    fun setOnShareClickListener(action: (VoucherUiModel) -> Unit): BroadCastVoucherBottomSheet {
        onShareClickAction = action
        return this
    }

    fun setOnBroadCastClickListener(action: (VoucherUiModel) -> Unit): BroadCastVoucherBottomSheet {
        onBroadCastClickAction = action
        return this
    }
}