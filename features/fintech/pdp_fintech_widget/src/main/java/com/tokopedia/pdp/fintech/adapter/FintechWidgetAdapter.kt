package com.tokopedia.pdp.fintech.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter.MyViewHolder
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class FintechWidgetAdapter(var widgetClickListner: WidgetClickListner) : RecyclerView.Adapter<MyViewHolder>() {

    private  var chipsData: ArrayList<ChipsData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fintech_invidual_whole_chip, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        chipsData[position].header?.let {
            if(it.isNotBlank())
                holder.headerPartner.text = it
            else
                holder.headerPartner.visibility = View.GONE
        }?:run {
            holder.headerPartner.visibility = View.GONE
        }
        chipsData[position].subheader?.let {
            if(it.isNotBlank())
                holder.subheaderPartner.text = it
            else
                holder.subheaderPartner.visibility = View.GONE
        }?:run {
            holder.subheaderPartner.visibility = View.GONE
        }
        chipsData[position].productIconLight?.let {
            if(it.isNotBlank())
                holder.partnerIcon.setImageUrl(it)
            else
                holder.partnerIcon.visibility = View.GONE
        }?:run {
            holder.partnerIcon.visibility = View.GONE
        }
    }
    override fun getItemCount(): Int {
        return chipsData.size
    }

    fun setData(chips: ArrayList<ChipsData>) {
        this.chipsData = chips
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        val widegetCard = itemView.findViewById<ConstraintLayout>(R.id.cardContent)
        val partnerIcon = itemView.findViewById<ImageUnify>(R.id.partnerIcon)
        val headerPartner = itemView.findViewById<Typography>(R.id.chipHeader)
        val subheaderPartner = itemView.findViewById<Typography>(R.id.chipSubHeader)
        init {


            itemView.setOnClickListener {
                clickedWidgetData()

            }
        }


        private fun clickedWidgetData() {
            chipsData[adapterPosition].cta?.bottomsheet?.buttons?.get(0)?.buttonUrl?.let { url ->
                chipsData[adapterPosition].cta?.type?.let { ctaType ->
                    widgetClickListner.clickedWidget(
                        ctaType, url
                    )
                }
            } ?: kotlin.run {
                chipsData[adapterPosition].cta?.androidUrl?.let { url ->
                    chipsData[adapterPosition].cta?.type?.let { ctaType ->
                        widgetClickListner.clickedWidget(
                            ctaType, url
                        )
                    }
                }
            }
        }


    }
}

