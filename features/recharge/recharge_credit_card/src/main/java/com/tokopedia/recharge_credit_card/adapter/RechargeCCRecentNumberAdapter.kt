package com.tokopedia.recharge_credit_card.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.common.topupbills.R
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.media.loader.loadImageWithoutPlaceholder

class RechargeCCRecentNumberAdapter(private val digitalRecentNumbers: List<TopupBillsRecommendation>) :
    RecyclerView.Adapter<RechargeCCRecentNumberAdapter.RecentNumbersItemViewHolder>() {

    private var listener: ActionListener? = null

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNumbersItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(com.tokopedia.common.topupbills.R.layout.item_vertical_recent_numbers, parent, false)
        return RecentNumbersItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return digitalRecentNumbers.size
    }

    override fun onBindViewHolder(holder: RecentNumbersItemViewHolder, position: Int) {
        holder.bind(digitalRecentNumbers[position])
    }

    inner class RecentNumbersItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconOperator: ImageView = itemView.findViewById(R.id.common_topup_bills_recent_icon_operator)
        private val textClientNumber: TextView = itemView.findViewById(R.id.common_topup_bills_recent_label)
        private val textProductName: TextView = itemView.findViewById(R.id.common_topup_bills_recent_product_name)

        private var topupBillsRecommendation: TopupBillsRecommendation? = null

        init {
            itemView.setOnClickListener {
                topupBillsRecommendation?.let {
                    listener?.onClickRecentNumber(it, adapterPosition)
                }
            }
        }

        fun bind(topupBillsRecommendation: TopupBillsRecommendation) {
            this.topupBillsRecommendation = topupBillsRecommendation
            iconOperator.loadImageWithoutPlaceholder(topupBillsRecommendation.iconUrl) {
                setErrorDrawable(com.tokopedia.abstraction.R.drawable.status_no_result)
            }
            if (topupBillsRecommendation.description.isEmpty()) {
                textClientNumber.text = topupBillsRecommendation.label
            } else {
                textClientNumber.text = topupBillsRecommendation.description
            }

            textProductName.text = topupBillsRecommendation.title
        }
    }

    interface ActionListener {
        fun onClickRecentNumber(topupBillsRecommendation: TopupBillsRecommendation, position: Int)
    }
}
