package com.tokopedia.vouchercreation.shop.voucherlist.view.widget

import com.tokopedia.imageassets.TokopediaImageUrl

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
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.databinding.BottomsheetMvcBroadcastVoucherBinding
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
        private const val TPD_VOUCHER_IMAGE_URL = TokopediaImageUrl.TPD_VOUCHER_IMAGE_URL
        private const val DATE_FORMAT = "dd MMMM yyyy"
        private const val HOUR_FORMAT = "HH:mm"
        private const val VOUCHER = "voucher"
    }

    private var binding by autoClearedNullable<BottomsheetMvcBroadcastVoucherBinding>()

    private val voucherUiModel by lazy {
        arguments?.getParcelable<VoucherUiModel?>(VOUCHER)
    }

    private var onShareClickAction: (VoucherUiModel) -> Unit = {}
    private var onBroadCastClickAction: (VoucherUiModel) -> Unit = {}

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
        setupView(view)
    }

    private fun initBottomSheet() {
        binding = BottomsheetMvcBroadcastVoucherBinding.inflate(LayoutInflater.from(context))
        setChild(binding?.root)
    }

    private fun setupView(view: View) {
        binding?.apply {
            // load tokopedia voucher image
            iuTkpdVoucher.loadImage(TPD_VOUCHER_IMAGE_URL)
            // voucher period info setup
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
                tgpVoucherPeriodInfo.text = voucherPeriodInfo
            }

            // broad cast button setup
            broadcastButton.setOnClickListener {
                voucherUiModel?.run(onBroadCastClickAction)
            }

            // free text view setup
            voucherUiModel?.run {
                if (isFreeIconVisible) tgpFree.show()
                else tgpFree.hide()
            }

            // social media button setup
            val isPublic = voucherUiModel?.isPublic ?: false
            if (isPublic) socialMediaShareButton.hide()
            socialMediaShareButton.setOnClickListener {
                voucherUiModel?.run(onShareClickAction)
            }
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