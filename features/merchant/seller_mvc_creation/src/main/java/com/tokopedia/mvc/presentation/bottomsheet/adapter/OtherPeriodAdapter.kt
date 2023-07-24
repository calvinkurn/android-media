package com.tokopedia.mvc.presentation.bottomsheet.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.kotlin.extensions.view.ZERO
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.mvc.R
import com.tokopedia.mvc.databinding.SmvcItemOtherPeriodBinding
import com.tokopedia.mvc.domain.entity.Voucher
import com.tokopedia.utils.date.DateUtil

class OtherPeriodAdapter : RecyclerView.Adapter<OtherPeriodAdapter.ViewHolder>() {
    private var data: List<Voucher> = emptyList()
    private var onClickListener: (voucher: Voucher) -> Unit = {}

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = SmvcItemOtherPeriodBinding.inflate(
            LayoutInflater.from(parent.context),
            parent, false
        )
        return ViewHolder(binding, ::onItemClicked)
    }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        data.getOrNull(position)?.let {
            holder.bind(it)
        }
    }

    private fun onItemClicked(position: Int) {
        data.getOrNull(position)?.let {
            onClickListener.invoke(it)
        }
    }

    fun setDataList(newData: List<Voucher>) {
        data = newData
        notifyItemRangeChanged(Int.ZERO, newData.size)
    }

    fun setOnItemClickListerner(listener: (voucher: Voucher) -> Unit) {
        onClickListener = listener
    }

    inner class ViewHolder(
        private val binding: SmvcItemOtherPeriodBinding,
        listener: (position: Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        init {
            binding.iconMore.setOnClickListener {
                listener.invoke(absoluteAdapterPosition)
            }
        }

        fun bind(item: Voucher) {
            val startDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS,
                DateUtil.DEFAULT_VIEW_FORMAT, item.startTime
            )
            val finishDate = DateUtil.formatDate(
                DateUtil.YYYY_MM_DD_T_HH_MM_SS,
                DateUtil.DEFAULT_VIEW_FORMAT, item.finishTime
            )
            with(binding) {
                val context = root.context
                tfText.text = context.getString(
                    R.string.smvc_voucherlist_voucher_date_format,
                    startDate,
                    finishDate
                )
                tfSubTextQuota.text = MethodChecker.fromHtml(
                    context.getString(R.string.smvc_voucherlist_voucher_quota_format, item.quota)
                )
                tfSubTextProgram.apply {
                    showWithCondition(item.subsidyDetail.programDetail.programName.isNotEmpty())
                    text = MethodChecker.fromHtml(
                        context.getString(
                            R.string.smvc_placeholder_recurring_program_name,
                            item.subsidyDetail.programDetail.promotionLabel
                        )
                    )
                }
            }
        }
    }
}
