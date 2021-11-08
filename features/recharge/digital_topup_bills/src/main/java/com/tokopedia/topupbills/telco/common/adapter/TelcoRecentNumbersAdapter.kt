package com.tokopedia.topupbills.telco.common.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.common.topupbills.data.TopupBillsRecommendation
import com.tokopedia.topupbills.R

class TelcoRecentNumbersAdapter(private val digitalRecentNumbers: List<TopupBillsRecommendation>) :
        RecyclerView.Adapter<TelcoRecentNumbersAdapter.RecentNumbersItemViewHolder>() {

    private lateinit var listener: ActionListener

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNumbersItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_telco_recent_numbers, parent, false)
        return RecentNumbersItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return digitalRecentNumbers.size
    }

    override fun onBindViewHolder(holder: RecentNumbersItemViewHolder, position: Int) {
        holder.bind(digitalRecentNumbers[position])
    }

    inner class RecentNumbersItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconOperator: ImageView = itemView.findViewById(R.id.telco_recent_icon_operator)
        private val textClientNumber: TextView = itemView.findViewById(R.id.telco_recent_client_number)
        private val textProductName: TextView = itemView.findViewById(R.id.telco_recent_product_name)

        private lateinit var topupBillsRecommendation: TopupBillsRecommendation

        init {
            itemView.setOnClickListener {
                listener.onClickRecentNumber(topupBillsRecommendation, adapterPosition)
            }
        }

        fun bind(topupBillsRecommendation: TopupBillsRecommendation) {
            this.topupBillsRecommendation = topupBillsRecommendation
            ImageHandler.loadImageWithoutPlaceholder(iconOperator, topupBillsRecommendation.iconUrl,
                    ContextCompat.getDrawable(itemView.context, com.tokopedia.abstraction.R.drawable.status_no_result)
            )
            if (topupBillsRecommendation.description.isEmpty()) {
                textClientNumber.text = topupBillsRecommendation.clientNumber
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