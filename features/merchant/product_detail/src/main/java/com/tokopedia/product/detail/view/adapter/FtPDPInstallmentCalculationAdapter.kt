package com.tokopedia.product.detail.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.design.utils.CurrencyFormatUtil
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.product.detail.data.model.financing.FtCalculationPartnerData
import com.tokopedia.product.detail.data.model.financing.FtTncData
import kotlin.math.roundToLong

class FtPDPInstallmentCalculationAdapter(var mContext: Context?, var productPrice: Float?,
                                         var isOfficialStore: Boolean,
                                         var data: ArrayList<FtCalculationPartnerData>,
                                         var getDataFromFragment: GetTncDataFromFragment) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, p1: Int): RecyclerView.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.pdp_installment_layout, parent, false)
        return InstallmentItemViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(vHolder: RecyclerView.ViewHolder, position: Int) {

        if (mContext == null) {
            return
        }

        val item = data[position]
        val ftTncData = getDataFromFragment.getTncData(item.tncId)
        var finalTncString = ""
        ftTncData?.let {
            finalTncString = prepareTncString(it)
        }


        if (vHolder is InstallmentItemViewHolder) {

            if (finalTncString.isEmpty()) {
                vHolder.flInstructionTickerView.hide()
            } else {
                vHolder.flInstructionTickerView.show()
                vHolder.tvTncDescription.text = finalTncString
            }

            ImageHandler.loadImage(mContext!!, vHolder.ivMainIcon, item.partnerIcon, R.drawable.ic_loading_image)

            vHolder.tvInstallmentTitle.text = String.format(mContext!!.getString(R.string.ft_installment_heading), item.partnerName)

            vHolder.llInstallmentContainer.hide()
            /*vHolder.ivInstallmentToggle.setOnClickListener {
                if (expandLayout) {
                    vHolder.llInstallmentContainer.show()
                } else {
                    vHolder.llInstallmentContainer.hide()
                }
                expandLayout = !expandLayout
            }*/

            vHolder.llInstallmentDetail.removeAllViews()
            for (installmentData in item.creditCardInstallmentList) {
                val view = LayoutInflater.from(mContext).inflate(R.layout.pdp_installment_data_detail_layout, null, false)

                val monthCountTv = view.findViewById<TextView>(R.id.tv_month_count)
                monthCountTv.text = String.format(mContext!!.getString(R.string.pdp_installment_price_heading), installmentData.creditCardInstallmentTerm)

                val tvInstallmentPriceExt = view.findViewById<TextView>(R.id.tv_installment_price_ext)
                val priceTv = view.findViewById<TextView>(R.id.tv_installment_price)
                val tvInstallmentMinimumPriceExt = view.findViewById<TextView>(R.id.tv_installment_minimum_price_ext)
                tvInstallmentMinimumPriceExt.hide()
                productPrice?.compareTo(installmentData.minimumAmount)?.let {
                    if (it < 0) {

                        tvInstallmentMinimumPriceExt.show()
                        tvInstallmentPriceExt.hide()
                        tvInstallmentMinimumPriceExt.text = String.format("\nMin Pembelanjaan %s",
                                CurrencyFormatUtil.convertPriceValueToIdrFormat(installmentData.monthlyPrice.roundToLong(), false))
                        priceTv.text = "-"

                    } else {
                        tvInstallmentMinimumPriceExt.hide()
                        tvInstallmentPriceExt.show()
                        priceTv.text = CurrencyFormatUtil.convertPriceValueToIdrFormat(
                                (if (isOfficialStore) installmentData.osMonthlyPrice else installmentData.monthlyPrice).roundToLong(), false)
                    }
                }

                vHolder.llInstallmentDetail.addView(view)
            }

            if (item.creditCardInstructionList.isEmpty()) {
                vHolder.tvInstallmentDataHeading.hide()
                vHolder.llInstructionDetailContainer.hide()
            } else {
                vHolder.tvInstallmentDataHeading.show()
                vHolder.llInstructionDetailContainer.show()
                vHolder.tvInstallmentDataHeading.text = String.format(mContext!!.getString(R.string.ft_installment_data_heading), item.partnerName)
                vHolder.llInstructionDetailContainer.removeAllViews()
                for (instructionData in item.creditCardInstructionList) {
                    val view = LayoutInflater.from(mContext).inflate(R.layout.pdp_installment_instruction_data_layout, null, false)

                    val instructionHeading = view.findViewById<TextView>(R.id.tv_instruction_heading)
                    instructionHeading.text = String.format(mContext!!.getString(R.string.ft_instruction_heading), instructionData.order)

                    val instructionDesc = view.findViewById<TextView>(R.id.tv_instruction_description)
                    instructionDesc.text = instructionData.description

                    val instructionIcon: ImageView = view.findViewById(R.id.iv_instruction_icon)
                    ImageHandler.loadImage(mContext!!, instructionIcon, instructionData.insImageUrl, R.drawable.ic_loading_image)

                    vHolder.llInstructionDetailContainer.addView(view)
                }
            }

        }
    }

    private fun prepareTncString(ftTncDataList: ArrayList<FtTncData>): String {
        var finalTNCString = ""
        for (index in 0 until  ftTncDataList.size) {
            finalTNCString += getCompleteString(ftTncDataList[index])
            if(index != ftTncDataList.size - 1) {
                finalTNCString += "\n"
            }
        }
        return finalTNCString
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
        internal val tvInstallmentTitle: TextView = view.findViewById(R.id.tv_installment_detail_heading)
        internal val tvInstallmentDataHeading = view.findViewById<TextView>(R.id.tv_installment_data_heading)
        internal val ivInstallmentToggle: ImageView = view.findViewById(R.id.iv_installment_toggle)
        internal val llInstallmentContainer: LinearLayout = view.findViewById(R.id.ll_row_container)
        internal val llInstructionDetailContainer: LinearLayout = view.findViewById(R.id.instruction_detail_ll)
        internal val llInstallmentDetail: LinearLayout = view.findViewById(R.id.installment_detail_ll)
        internal val ivMainIcon: ImageView = view.findViewById(R.id.iv_installment_main_icon)
        internal val flInstructionTickerView = view.findViewById<FrameLayout>(R.id.ft_installment_ticker_view)
        internal val tvTncDescription: TextView = view.findViewById(R.id.tv_tnc_description)

        private var expandLayout = true

        init {
            ivInstallmentToggle.setOnClickListener {
                if (expandLayout) {
                    llInstallmentContainer.show()
                } else {
                    llInstallmentContainer.hide()
                }
                expandLayout = !expandLayout
            }
        }
    }
}