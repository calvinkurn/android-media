package com.tokopedia.product.detail.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadIcon
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.financing.FtCalculationPartnerData
import com.tokopedia.product.detail.data.model.financing.FtTncData
import kotlin.math.roundToLong

class FtPDPInstallmentCalculationAdapter(var productPrice: Float?,
                                         var isOfficialStore: Boolean,
                                         var partnerDataList: ArrayList<FtCalculationPartnerData>,
                                         var getDataFromFragment: GetTncDataFromFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    private var expandedPosition = -1
    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.pdp_installment_layout, parent, false)
        return InstallmentItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return partnerDataList.size
    }

    override fun onBindViewHolder(vHolder: RecyclerView.ViewHolder, position: Int) {

        val mContext = vHolder.itemView.context ?: return

        val item = partnerDataList[position]
        val ftTncData = getDataFromFragment.getTncData(item.tncId)

        if (vHolder is InstallmentItemViewHolder) {
            if (ftTncData.isNullOrEmpty()) {
                vHolder.flInstructionTickerView.hide()
            } else {
                vHolder.flInstructionTickerView.show()
                vHolder.llTncDescription.removeAllViews()
                for (tncData in ftTncData) {
                    val view = LayoutInflater.from(mContext).inflate(R.layout.pdp_installment_tnc_desc, null, false)
                    val tvTncDesc = view.findViewById<TextView>(R.id.tv_tnc_description)
                    tvTncDesc.text = getCompleteString(tncData)
                    vHolder.llTncDescription.addView(view)
                }
            }

            vHolder.ivMainIcon.loadIcon(item.partnerIcon)
            vHolder.tvInstallmentTitle.text = String.format(mContext.getString(R.string.ft_installment_heading), item.partnerName)
            vHolder.llInstallmentContainer.hide()

            vHolder.llInstallmentDetail.removeAllViews()
            for (installmentData in item.creditCardInstallmentList) {

                val monthlyProductPrice = (if (isOfficialStore) installmentData.osMonthlyPrice else installmentData.monthlyPrice)
                val view = LayoutInflater.from(mContext).inflate(R.layout.pdp_installment_data_detail_layout, null, false)

                val monthCountTv = view.findViewById<TextView>(R.id.tv_month_count)
                monthCountTv.text = String.format(mContext.getString(R.string.pdp_installment_price_heading), installmentData.creditCardInstallmentTerm)

                val tvInstallmentPriceExt = view.findViewById<TextView>(R.id.tv_installment_price_ext)
                val priceTv = view.findViewById<TextView>(R.id.tv_installment_price)
                val tvInstallmentMinimumPriceExt = view.findViewById<TextView>(R.id.tv_installment_minimum_price_ext)
                tvInstallmentMinimumPriceExt.hide()
                productPrice?.compareTo(installmentData.minimumAmount)?.let {
                    if (it < 0) {
                        priceTv.text = "-"
                        tvInstallmentMinimumPriceExt.show()
                        tvInstallmentPriceExt.hide()
                        tvInstallmentMinimumPriceExt.text = String.format(mContext.getString(R.string.ft_min_installment_amount),
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(installmentData.minimumAmount, false))
                    } else {
                        if (installmentData.maximumAmount == 0) {
                            tvInstallmentMinimumPriceExt.hide()
                            tvInstallmentPriceExt.show()
                            priceTv.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(monthlyProductPrice.roundToLong(), false)
                        } else {
                            installmentData.maximumAmount.compareTo(monthlyProductPrice).let { result ->
                                if (result < 0) {
                                    priceTv.text = "-"
                                    tvInstallmentMinimumPriceExt.show()
                                    tvInstallmentPriceExt.hide()
                                    tvInstallmentMinimumPriceExt.text = String.format(mContext.getString(R.string.ft_max_installment_amount),
                                            CurrencyFormatUtil.convertPriceValueToIdrFormat(installmentData.maximumAmount, false))
                                } else {
                                    tvInstallmentMinimumPriceExt.hide()
                                    tvInstallmentPriceExt.show()
                                    priceTv.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(monthlyProductPrice.roundToLong(), false)
                                }
                            }
                        }
                    }
                }
                vHolder.llInstallmentDetail.addView(view)
            }

            if (item.expandLayout) {
                vHolder.ivInstallmentToggle.animate().rotation(180f).duration = 300
                vHolder.llInstallmentContainer.show()
            } else {
                vHolder.ivInstallmentToggle.animate().rotation(0f).duration = 300
                vHolder.llInstallmentContainer.hide()
            }

            vHolder.rlMainContainer.setOnClickListener {
                item.expandLayout = !item.expandLayout

                if (item.expandLayout) {
                    vHolder.ivInstallmentToggle.animate().rotation(180f).duration = 300
                    vHolder.llInstallmentContainer.show()
                } else {
                    vHolder.ivInstallmentToggle.animate().rotation(0f).duration = 300
                    vHolder.llInstallmentContainer.hide()
                }

                for (index in 0 until partnerDataList.size) {
                    if (expandedPosition != position && expandedPosition == index) {
                        partnerDataList[index].expandLayout = false
                        notifyItemChanged(index)
                    }
                }
                expandedPosition = position
            }

            inflateInstructionListData(vHolder, position, mContext)
        }
    }

    private fun inflateInstructionListData(vHolder: InstallmentItemViewHolder, position: Int, context: Context) {

        val item = partnerDataList[position]

        if (item.creditCardInstructionList.isEmpty()) {
            vHolder.tvInstallmentDataHeading.hide()
            vHolder.llInstructionDetailContainer.hide()
        } else {
            vHolder.tvInstallmentDataHeading.show()
            vHolder.llInstructionDetailContainer.show()
            vHolder.tvInstallmentDataHeading.text = String.format(context.getString(R.string.ft_installment_data_heading), item.partnerName)
            vHolder.llInstructionDetailContainer.removeAllViews()
            for (instructionData in item.creditCardInstructionList) {
                val view = LayoutInflater.from(context).inflate(R.layout.pdp_installment_instruction_data_layout, null, false)

                val instructionHeading = view.findViewById<TextView>(R.id.tv_instruction_heading)
                instructionHeading.text = String.format(context.getString(R.string.ft_instruction_heading), instructionData.order)

                val instructionDesc = view.findViewById<TextView>(R.id.tv_instruction_description)
                instructionDesc.text = instructionData.description

                val instructionIcon: ImageView = view.findViewById(R.id.iv_instruction_icon)
                instructionIcon.loadIcon(instructionData.insImageUrl)

                vHolder.llInstructionDetailContainer.addView(view)
            }
        }
    }

    private fun getCompleteString(ftTncData: FtTncData): String {
        var tncString = ""
        for (i in 0 until ftTncData.tncOrder) {
            tncString += "*"
        }
        tncString += " "
        tncString += ftTncData.tncDescription
        return tncString
    }

    interface GetTncDataFromFragment {
        fun getTncData(id: Int): ArrayList<FtTncData>?
    }

    inner class InstallmentItemViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal val rlMainContainer: RelativeLayout = view.findViewById(R.id.ll_main_container)
        internal val tvInstallmentTitle: TextView = view.findViewById(R.id.tv_installment_detail_heading)
        internal val tvInstallmentDataHeading = view.findViewById<TextView>(R.id.tv_installment_data_heading)
        internal val ivInstallmentToggle: ImageView = view.findViewById(R.id.iv_installment_toggle)
        internal val llInstallmentContainer: LinearLayout = view.findViewById(R.id.ll_row_container)
        internal val llInstructionDetailContainer: LinearLayout = view.findViewById(R.id.instruction_detail_ll)
        internal val llInstallmentDetail: LinearLayout = view.findViewById(R.id.installment_detail_ll)
        internal val ivMainIcon: ImageView = view.findViewById(R.id.iv_installment_main_icon)
        internal val flInstructionTickerView = view.findViewById<FrameLayout>(R.id.ft_installment_ticker_view)
        internal val llTncDescription: LinearLayout = view.findViewById(R.id.ll_tnc_description)
    }
}
