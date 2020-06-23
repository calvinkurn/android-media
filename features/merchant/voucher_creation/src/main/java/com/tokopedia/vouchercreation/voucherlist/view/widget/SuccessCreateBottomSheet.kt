package com.tokopedia.vouchercreation.voucherlist.view.widget

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.detail.view.component.StartEndVoucher
import com.tokopedia.vouchercreation.voucherlist.model.ui.VoucherUiModel
import kotlinx.android.synthetic.main.bottomsheet_mvc_success_create.view.*

class SuccessCreateBottomSheet(parent: ViewGroup): BottomSheetUnify() {

    companion object {
        val TAG: String = SuccessCreateBottomSheet::class.java.simpleName

        private const val DATE_FORMAT = "dd MMM yyyy"
        private const val HOUR_FORMAT = "HH:mm"

        @JvmStatic
        fun createInstance(parent: ViewGroup,
                           voucherUiModel: VoucherUiModel): SuccessCreateBottomSheet {
            return SuccessCreateBottomSheet(parent).apply {
                this.voucherUiModel = voucherUiModel
            }
        }
    }

    private var voucherUiModel: VoucherUiModel? = null

    private var onShareClickAction: (VoucherUiModel) -> Unit = {}
    private var onDownloadClickAction: (VoucherUiModel) -> Unit = {}

    init {
        val child = LayoutInflater.from(parent.context)
                .inflate(R.layout.bottomsheet_mvc_success_create, parent, false)
        setChild(child)
        setStyle(DialogFragment.STYLE_NORMAL, R.style.DialogStyle)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView(view)
    }

    private fun setupView(view: View) {
        with(view) {
            voucherUiModel?.let { uiModel ->
                successTitle?.text = String.format(context?.getString(R.string.mvc_success_create_title).toBlankOrString(), uiModel.typeFormatted)

                successImage?.loadImageWithoutPlaceholder(uiModel.imageSquare)

                val startDate = DateTimeUtils.reformatUnsafeDateTime(uiModel.startTime, DATE_FORMAT)
                val endDate = DateTimeUtils.reformatUnsafeDateTime(uiModel.finishTime, DATE_FORMAT)
                val startHour = String.format(context?.getString(R.string.mvc_hour_wib).toBlankOrString(), DateTimeUtils.reformatUnsafeDateTime(uiModel.startTime, HOUR_FORMAT))
                val endHour = String.format(context?.getString(R.string.mvc_hour_wib).toBlankOrString(), DateTimeUtils.reformatUnsafeDateTime(uiModel.finishTime, HOUR_FORMAT))
                successDate?.run {
                    setStartTime(StartEndVoucher.Model(startDate, startHour))
                    setEndTime(StartEndVoucher.Model(endDate, endHour))
                }

                successShareButton?.setOnClickListener {
                    voucherUiModel?.run(onShareClickAction)
                }
                successDownloadButton?.setOnClickListener {
                    voucherUiModel?.run(onDownloadClickAction)
                }
            }
        }
    }

    fun show(fm: FragmentManager) {
        showNow(fm, this::class.java.simpleName)
    }

    fun setOnShareClickListener(action: (VoucherUiModel) -> Unit): SuccessCreateBottomSheet {
        onShareClickAction = action
        return this
    }

    fun setOnDownloadClickListener(action: (VoucherUiModel) -> Unit): SuccessCreateBottomSheet {
        onDownloadClickAction = action
        return this
    }

}