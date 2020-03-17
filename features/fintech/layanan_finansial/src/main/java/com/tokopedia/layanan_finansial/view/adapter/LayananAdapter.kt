package com.tokopedia.layanan_finansial.view.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.layanan_finansial.R
import com.tokopedia.layanan_finansial.view.models.LayananListItem
import kotlinx.android.synthetic.main.layanan_card_item.view.*

class LayananAdapter(private val list: List<LayananListItem>) : RecyclerView.Adapter<LayananAdapter.LayananViewHolder>() {


   inner class LayananViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
       fun setData(layananListItem: LayananListItem) {
           itemView.apply {
               name.text = layananListItem.name
               category.text = layananListItem.categrory
               desc_1.text = layananListItem.desc_1
               if(!layananListItem.desc_2.isNullOrEmpty()){
                   desc_1.setLines(1)
                   desc_2.text = layananListItem.desc_2
                   desc_2.show()
               } else {
                   desc_1.setLines(2)
                   desc_2.hide()
               }
               if (!layananListItem.cta.isNullOrEmpty()) {
                   cta.show()
                   view.show()
                   arrow.show()
                   cta.text = layananListItem.cta
               } else {
                   cta.hide()
                   view.hide()
                   arrow.hide()
               }
               if(!layananListItem.status.isNullOrEmpty()){
                   status.text = layananListItem.status
                   status.setBackgroundColor(Color.parseColor(layananListItem.status_background_color))
                   status.setTextColor(Color.parseColor(layananListItem.status_text_color))
                   status.show()
               } else {
                   status.hide()
               }
           }
       }

   }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LayananViewHolder {
        return LayananViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layanan_card_item,parent,false))
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: LayananViewHolder, position: Int) {
        holder.setData(list[position])
    }
}