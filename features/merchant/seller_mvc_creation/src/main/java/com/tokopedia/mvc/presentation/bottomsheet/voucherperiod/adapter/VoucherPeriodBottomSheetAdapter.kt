package com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.campaign.utils.constant.DateConstant.DATE_YEAR_PRECISION
import com.tokopedia.campaign.utils.extension.toDate
import com.tokopedia.mvc.databinding.SmvcItemPeriodListBinding
import com.tokopedia.mvc.presentation.bottomsheet.voucherperiod.DateStartEndData
import com.tokopedia.mvc.util.DateTimeUtils.DASH_DATE_FORMAT
import com.tokopedia.mvc.util.formatTo

class VoucherPeriodBottomSheetAdapter :
    RecyclerView.Adapter<VoucherPeriodBottomSheetAdapter.VoucherPeriodBottomSheetViewHolder>() {

    private val dateList = mutableListOf<DateStartEndData>()

    inner class VoucherPeriodBottomSheetViewHolder(itemView: SmvcItemPeriodListBinding) : RecyclerView.ViewHolder(itemView.root) {
        private val dateText: com.tokopedia.unifyprinciples.Typography = itemView.dateText
        fun bind(item: DateStartEndData) {
            dateText.text = item.formatTextToDisplayDate()
        }

        private fun DateStartEndData.formatTextToDisplayDate(): String {
            return "${dateStart.formatDateToDisplayDate()}, $hourStart - ${dateEnd.formatDateToDisplayDate()}, $hourEnd"
        }

        private fun String.formatDateToDisplayDate(): String {
            return this.toDate(DASH_DATE_FORMAT).formatTo(DATE_YEAR_PRECISION)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): VoucherPeriodBottomSheetViewHolder {
        val view = SmvcItemPeriodListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VoucherPeriodBottomSheetViewHolder(view)
    }

    override fun onBindViewHolder(holder: VoucherPeriodBottomSheetViewHolder, position: Int) {
        holder.bind(dateList[position])
    }

    override fun getItemCount(): Int {
        return dateList.size
    }

    fun setList(list: List<DateStartEndData>) {
        this.dateList.clear()
        this.dateList.addAll(list)
    }
}
