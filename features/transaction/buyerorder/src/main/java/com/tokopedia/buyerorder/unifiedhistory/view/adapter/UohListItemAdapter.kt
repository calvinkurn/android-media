package com.tokopedia.buyerorder.unifiedhistory.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.buyerorder.R
import kotlinx.android.synthetic.main.uoh_list_item.view.*

/**
 * Created by fwidjaja on 02/07/20.
 */

class UohListItemAdapter : RecyclerView.Adapter<UohListItemAdapter.ViewHolder>() {

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.uoh_list_item, parent, false))
    }

    override fun getItemCount(): Int {
        return 3
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemView.tv_uoh_categories?.text = "Tokopedia Print"
        holder.itemView.tv_uoh_date?.text = "6 Mei 2020"
        holder.itemView.label_uoh_order?.text = "Butuh Revisi"

        holder.itemView.ticker_info_inside_card?.tickerTitle = "Jika kamu tidak memilih lanjut print atau upload ulang, pesanan otomatis akan di-print."
        holder.itemView.tv_uoh_product_name?.text = "Kartu Nama Standar"
        holder.itemView.tv_uoh_product_desc?.text = "1 Barang (0.100 kg)"

        holder.itemView.tv_uoh_total_belanja?.text = "Total Belanja:"
        holder.itemView.tv_uoh_total_belanja_value?.text = "Rp30.000"
        holder.itemView.uoh_btn_action?.text = "Revisi File"
    }
}