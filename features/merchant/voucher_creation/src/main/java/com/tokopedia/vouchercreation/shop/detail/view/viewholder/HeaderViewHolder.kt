package com.tokopedia.vouchercreation.shop.detail.view.viewholder

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.core.content.res.ResourcesCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.toBlankOrString
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifycomponents.ContainerUnify
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.common.consts.VoucherStatusConst
import com.tokopedia.vouchercreation.common.utils.DateTimeUtils
import com.tokopedia.vouchercreation.common.utils.TimerRunnable
import com.tokopedia.vouchercreation.shop.detail.model.VoucherHeaderUiModel
import com.tokopedia.vouchercreation.shop.detail.view.component.StartEndVoucher
import kotlinx.android.synthetic.main.item_mvc_voucher_header.view.*

/**
 * Created By @ilhamsuaib on 30/04/20
 */

class HeaderViewHolder(
        itemView: View?,
        private val onDownloadClick: () -> Unit
) : AbstractViewHolder<VoucherHeaderUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_voucher_header

        private const val DATE_FORMAT = "dd MMM yyyy"
        private const val HOUR_FORMAT = "HH:mm"

        private const val DEFAULT_RADIUS = 8.0f
    }

    override fun bind(element: VoucherHeaderUiModel) {
        with(itemView) {
            var containerColor: Int? = null

            val startDate = DateTimeUtils.reformatUnsafeDateTime(element.startTime, DATE_FORMAT)
            val endDate = DateTimeUtils.reformatUnsafeDateTime(element.finishTime, DATE_FORMAT)
            val startHour = String.format(context?.getString(R.string.mvc_hour_wib).toBlankOrString(), DateTimeUtils.reformatUnsafeDateTime(element.startTime, HOUR_FORMAT))
            val endHour = String.format(context?.getString(R.string.mvc_hour_wib).toBlankOrString(), DateTimeUtils.reformatUnsafeDateTime(element.finishTime, HOUR_FORMAT))

            val statusResId: Int
            val textColorResId: Int

            headerContainer?.gone()
            label_vps.gone()
            tpg_package_name.gone()
            label_subsidy.gone()

            when(element.status) {
                VoucherStatusConst.NOT_STARTED -> {
                    statusResId = R.string.mvc_future
                    textColorResId = com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
                }
                VoucherStatusConst.ONGOING -> {
                    lblMvcRemainingTime?.visible()

                    val endDateTime = DateTimeUtils.convertUnsafeDateTime(element.finishTime)

                    val diffMillis = endDateTime.time - System.currentTimeMillis()

                    val timer = TimerRunnable(diffMillis, ::onCountdownTick)
                    timer.start()

                    headerContainer?.visible()
                    statusResId = R.string.mvc_ongoing
                    containerColor = ContainerUnify.GREEN
                    textColorResId = com.tokopedia.unifyprinciples.R.color.Green_G500
                }
                VoucherStatusConst.ENDED -> {
                    headerContainer?.visible()
                    statusResId = R.string.mvc_ended
                    containerColor = ContainerUnify.GREY
                    textColorResId = com.tokopedia.unifyprinciples.R.color.Neutral_N700_96
                }
                VoucherStatusConst.STOPPED -> {
                    statusResId = R.string.mvc_stopped
                    textColorResId = com.tokopedia.unifyprinciples.R.color.Red_R500
                    startEndVoucher?.gone()
                    element.cancelTime?.let { time ->
                        val cancelDate = DateTimeUtils.reformatUnsafeDateTime(time, DATE_FORMAT)
                        canceledDate?.run {
                            text = String.format(context?.getString(R.string.mvc_stopped_at).toBlankOrString(), cancelDate)
                            visible()
                        }
                    }
                }
                VoucherStatusConst.DELETED -> {
                    statusResId = R.string.mvc_deleted
                    textColorResId = com.tokopedia.unifyprinciples.R.color.Red_R500
                    startEndVoucher?.gone()
                }
                else -> {
                    statusResId = R.string.mvc_ongoing
                    textColorResId = com.tokopedia.unifyprinciples.R.color.Neutral_N700_68
                }
            }

            startEndVoucher?.run {
                setStartTime(StartEndVoucher.Model(startDate, startHour))
                setEndTime(StartEndVoucher.Model(endDate, endHour))
            }

            imgMvcVoucherDetail?.loadImageRounded(element.voucherImageUrl, resources?.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl1)?.toFloat() ?: DEFAULT_RADIUS)
            voucherStatus?.run {
                setTextColor(resources?.run { ResourcesCompat.getColor(this, textColorResId, null) } ?: Color.BLACK)
                text = context?.getString(statusResId).toBlankOrString()
            }
            containerColor?.run {
                headerContainer?.setContainerColor(this)
            }

            if (element.isVps) {
                label_vps.visible()
                tpg_package_name.visible()
                label_subsidy.gone()
                tpg_package_name.text = element.packageName
            }

            if (!element.isVps && element.isSubsidy) {
                label_vps.gone()
                tpg_package_name.gone()
                label_subsidy.visible()
            }

            btnMvcDownload.setOnClickListener {
                onDownloadClick()
            }
        }
    }

    private fun onCountdownTick(time: String) {
        itemView.lblMvcRemainingTime?.text = time
    }
}