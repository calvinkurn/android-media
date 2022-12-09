package com.tokopedia.mvc.presentation.bottomsheet

import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_LONGMONTH_YEAR_WITH_TIME
import com.tokopedia.kotlin.extensions.toFormattedString
import com.tokopedia.kotlin.extensions.view.isMoreThanZero
import com.tokopedia.media.loader.loadImage
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcBottomsheetSuccessUploadVoucherBinding
import com.tokopedia.mvc.domain.entity.VoucherConfiguration
import com.tokopedia.mvc.util.constant.BundleConstant.BUNDLE_KEY_VOUCHER_CONFIGURATION
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class SuccessUploadBottomSheet: BottomSheetUnify() {

    companion object {
        @JvmStatic
        fun createInstance(voucherConfiguration: VoucherConfiguration): SuccessUploadBottomSheet {
            return SuccessUploadBottomSheet().apply {
                arguments = Bundle().apply {
                    putParcelable(BUNDLE_KEY_VOUCHER_CONFIGURATION, voucherConfiguration)
                }
            }
        }
        private const val TPD_VOUCHER_IMAGE_URL = "https://images.tokopedia.net/img/android/campaign/mvc/mvc_voucher.png"
    }

    private var binding by autoClearedNullable<SmvcBottomsheetSuccessUploadVoucherBinding>()

    private val voucherConfiguration by lazy {
        arguments?.getParcelable<VoucherConfiguration?>(BUNDLE_KEY_VOUCHER_CONFIGURATION)
    }

    private var onBroadCastClickAction: (voucherConfiguration: VoucherConfiguration) -> Unit = {}
    private var setOnAdsClickListener: (voucherConfiguration: VoucherConfiguration) -> Unit = {}

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    private fun initBottomSheet() {
        clearContentPadding = true
        binding = SmvcBottomsheetSuccessUploadVoucherBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
    }

    private fun setupView() {
        binding?.apply {
            setupLayoutInfoVoucher()
            voucherConfiguration?.let { voucher ->
                setupDescWording(voucher)
                setupLayoutActions(voucher)
            }
        }
    }

    private fun SmvcBottomsheetSuccessUploadVoucherBinding.setupLayoutInfoVoucher() {
        val greenDark = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_Background)
        val greenLight = MethodChecker.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_GN50)
        val drawable = GradientDrawable(
            GradientDrawable.Orientation.TOP_BOTTOM, intArrayOf(greenDark, greenLight)
        )
        layoutInfoVoucher.background = drawable
        imgSuccessIlustration.loadImage(TPD_VOUCHER_IMAGE_URL)
    }

    private fun SmvcBottomsheetSuccessUploadVoucherBinding.setupLayoutActions(
        voucherConfiguration: VoucherConfiguration
    ) {
        layoutAds.setOnClickListener {
            setOnAdsClickListener(voucherConfiguration)
        }
        layoutBroadcast.setOnClickListener {
            onBroadCastClickAction(voucherConfiguration)
        }
    }

    private fun SmvcBottomsheetSuccessUploadVoucherBinding.setupDescWording(voucher: VoucherConfiguration) {
        voucher.apply {
            val startPeriod = startPeriod.toFormattedString(DATE_LONGMONTH_YEAR_WITH_TIME)
            val endPeriod = endPeriod.toFormattedString(DATE_LONGMONTH_YEAR_WITH_TIME)
            val voucherPeriodInfo = if (totalPeriod.isMoreThanZero()) {
                context?.getString(R.string.smvc_success_upload_multiperiod_desc, voucherName, totalPeriod)
            } else {
                context?.getString(R.string.smvc_success_upload_desc, voucherName, startPeriod, endPeriod)
            }
            tpgVoucherPeriodInfo.text = voucherPeriodInfo
        }
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }

    fun setOnAdsClickListener(
        action: (voucherConfiguration: VoucherConfiguration) -> Unit
    ): SuccessUploadBottomSheet {
        setOnAdsClickListener = action
        return this
    }

    fun setOnBroadCastClickListener(
        action: (voucherConfiguration: VoucherConfiguration) -> Unit
    ): SuccessUploadBottomSheet {
        onBroadCastClickAction = action
        return this
    }
}


