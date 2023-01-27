package com.tokopedia.pdp.fintech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter.MyViewHolder
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
import com.tokopedia.pdp.fintech.helper.Utils.returnRouteObject
import com.tokopedia.pdp.fintech.helper.Utils.safeLet
import com.tokopedia.pdp.fintech.helper.Utils.setListOfData
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode
import java.util.*
import kotlin.collections.ArrayList

class FintechWidgetAdapter(val context: Context, var widgetClickListner: WidgetClickListner) :
    RecyclerView.Adapter<MyViewHolder>() {

    private var chipsData: ArrayList<ChipsData> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.fintech_invidual_whole_chip, parent, false)
        )
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        if (chipsData[position].gatewayId != GATEWAY_ID_SEE_MORE) {
            chipsData[position].gatewayId?.let {
                holder.dummyView.visibility = View.VISIBLE
            }
            chipsData[position].header?.let {
                setHeaderData(it, holder)
            } ?: run {
                removeViewVisibility(holder.headerPartner)
            }
            chipsData[position].subheaderColor?.let {
                setSubHeaderColor(it, holder)
            }
            chipsData[position].subheader?.let {
                setSubHeaderData(it, holder)
            } ?: run {
                removeViewVisibility(holder.subheaderPartner)
            }
            setIcon(position, holder.partnerIcon)
        }
        else
            holder.cardContainer.visibility = View.GONE
    }

    private fun setHeaderData(it: String,
                              holder: MyViewHolder)
    {
        if (it.isNotBlank()) {
            holder.headerPartner.visibility = View.VISIBLE
            holder.headerPartner.text = it.parseAsHtml()
        } else
            removeViewVisibility(holder.headerPartner)
    }

    private fun setSubHeaderData( it: String,
                                  holder: MyViewHolder)
    {
        if (it.isNotBlank()) {
            holder.subheaderPartner.visibility = View.VISIBLE
            holder.subheaderPartner.text = it.parseAsHtml()
        } else
            removeViewVisibility(holder.subheaderPartner)
    }

    private fun setSubHeaderColor(
        it: String,
        holder: MyViewHolder
    ) {
        when {
            it.equals("green", true) -> {
                holder.subheaderPartner.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_GN500
                    )
                )
                holder.subheaderPartner.weightType = Typography.BOLD
            }
            it.equals("blue", true) -> {
                holder.subheaderPartner.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_BN500
                    )
                )
                holder.subheaderPartner.weightType = Typography.BOLD
            }
            else -> {
                holder.subheaderPartner.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_NN600
                    )
                )
                holder.subheaderPartner.weightType = Typography.REGULAR
            }
        }
    }

    private fun setIcon(
        position: Int,
        iconImageView: ImageUnify
    ) {
        if (!context.isDarkMode()) {
            chipsData[position].productIconLight?.let {
                if (it.isNotBlank()) {
                    iconImageView.visibility = View.VISIBLE
                    iconImageView.setImageUrl(it)
                } else
                    iconImageView.visibility = View.GONE
            } ?: run {
                iconImageView.visibility = View.GONE
            }
        } else {
            chipsData[position].productIconDark?.let {
                if (it.isNotBlank()) {
                    iconImageView.visibility = View.VISIBLE

                    iconImageView.setImageUrl(it)
                } else
                    iconImageView.visibility = View.GONE
            } ?: run {
                iconImageView.visibility = View.GONE
            }
        }
    }

    private fun removeViewVisibility(view: View) {
        view.visibility = View.GONE
    }

    override fun getItemCount(): Int {
        // since the last element of the list is Lihat semu which no needed to inflate
        return chipsData.size-1
    }

    fun setData(chips: ArrayList<ChipsData>) {
        this.chipsData = chips
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val partnerIcon = itemView.findViewById<ImageUnify>(R.id.partnerIcon)
        val headerPartner = itemView.findViewById<Typography>(R.id.chipHeader)
        val subheaderPartner = itemView.findViewById<Typography>(R.id.chipSubHeader)
        val dummyView = itemView.findViewById<View>(R.id.dummyViewForMargin)
        val cardContainer = itemView.findViewById<View>(R.id.pdp_fintech_card_container)

        init {

            itemView.setOnClickListener {
                clickedWidgetData()

            }
        }


        private fun clickedWidgetData() {
          val listOfAllChecker   = setListOfData(chipsData[adapterPosition])
           if(safeLet(listOfAllChecker) == true)
             {
                widgetClickListner.clickedWidget(
                    returnRouteObject(chipsData[adapterPosition])
                )
            }
        }

    }


    companion object {
        // Do not inflate if gatway id is 0
        const val GATEWAY_ID_SEE_MORE = "0"
    }

}


