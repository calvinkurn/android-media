package com.tokopedia.pdp.fintech.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter.MyViewHolder
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp_fintech.R

class FintechWidgetAdapter(var widgetClickListner: WidgetClickListner) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fintech_invidual_whole_chip, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {}
    override fun getItemCount(): Int {
        return 5
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val widegetCard = itemView.findViewById<ConstraintLayout>(R.id.cardContent)
        init {
            itemView.setOnClickListener {
                widgetClickListner.clickedWidget(adapterPosition)
            }
        }


    }
}

