package com.tokopedia.topupbills.telco.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.data.TelcoRecommendation

/**
 * Created by nabillasabbaha on 23/04/19.
 */
class DigitalRecentNumbersAdapter(val digitalRecentNumbers: List<TelcoRecommendation>) :
        RecyclerView.Adapter<DigitalRecentNumbersAdapter.RecentNumbersItemViewHolder>() {

    private lateinit var listener: ActionListener

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecentNumbersItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_digital_recent_numbers, parent, false)
        return RecentNumbersItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return digitalRecentNumbers.size
    }

    override fun onBindViewHolder(holder: RecentNumbersItemViewHolder, position: Int) {
        holder.bind(digitalRecentNumbers[position])
    }

    inner class RecentNumbersItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        private val iconOperator: ImageView = itemView.findViewById(R.id.icon_operator)
        private val textClientNumber: TextView = itemView.findViewById(R.id.text_client_number)
        private val textProductName: TextView = itemView.findViewById(R.id.text_product_name)

        private lateinit var telcoRecommendation: TelcoRecommendation

        init {
            itemView.setOnClickListener {
                listener.onClickRecentNumber(telcoRecommendation, adapterPosition)
            }
        }

        fun bind(telcoRecommendation: TelcoRecommendation) {
            this.telcoRecommendation = telcoRecommendation
            ImageHandler.loadImageWithoutPlaceholder(iconOperator, telcoRecommendation.iconUrl,
                    ContextCompat.getDrawable(itemView.context, R.drawable.status_no_result)
            )
            textClientNumber.text = telcoRecommendation.clientNumber
            textProductName.text = telcoRecommendation.title
        }

    }

    interface ActionListener {
        fun onClickRecentNumber(telcoRecommendation: TelcoRecommendation, position: Int)
    }

}