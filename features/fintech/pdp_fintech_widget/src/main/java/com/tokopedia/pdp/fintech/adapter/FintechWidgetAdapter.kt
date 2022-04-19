package com.tokopedia.pdp.fintech.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.parseAsHtml
import com.tokopedia.media.loader.loadImage
import com.tokopedia.pdp.fintech.adapter.FintechWidgetAdapter.MyViewHolder
import com.tokopedia.pdp.fintech.domain.datamodel.ChipsData
import com.tokopedia.pdp.fintech.domain.datamodel.FintechRedirectionWidgetDataClass
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

        chipsData[position].gatewayId?.let {
            setSeeMoreLogic(it,holder)
        }
        chipsData[position].header?.let {
            setHeaderData(it,holder)
        } ?: run {
            removeViewVisibility(holder.headerPartner)
        }
        chipsData[position].subheaderColor?.let {
            setSubHeaderColor(it, holder)
        }
        chipsData[position].subheader?.let {
            setSubHeaderData(it,holder)
        } ?: run {
            removeViewVisibility(holder.subheaderPartner)
        }
        setIcon(position, holder.partnerIcon)
    }

    private fun setSeeMoreLogic(it: Int, holder: FintechWidgetAdapter.MyViewHolder) {
        if (it == 0) {
            holder.dummyView.visibility = View.GONE
            holder.seeMoreIcon.visibility = View.VISIBLE
            holder.seeMoreIcon.loadImage(R.drawable.fintechwidget_procced_icon)
        } else {
            holder.dummyView.visibility = View.VISIBLE
            holder.seeMoreIcon.visibility = View.GONE
        }
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
                        com.tokopedia.unifyprinciples.R.color.Unify_G500
                    )
                )
                holder.subheaderPartner.fontType = Typography.BOLD
            }
            else -> {
                holder.subheaderPartner.setTextColor(
                    ContextCompat.getColor(
                        context,
                        com.tokopedia.unifyprinciples.R.color.Unify_N700_68
                    )
                )
                holder.subheaderPartner.fontType = Typography.REGULAR
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
        return chipsData.size
    }

    fun setData(chips: ArrayList<ChipsData>) {
        this.chipsData = chips
        notifyDataSetChanged()
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val partnerIcon = itemView.findViewById<ImageUnify>(R.id.partnerIcon)
        val headerPartner = itemView.findViewById<Typography>(R.id.chipHeader)
        val subheaderPartner = itemView.findViewById<Typography>(R.id.chipSubHeader)
        val seeMoreIcon = itemView.findViewById<ImageUnify>(R.id.seeMore_Icon)
        val dummyView = itemView.findViewById<View>(R.id.dummyViewForMargin)

        init {

            itemView.setOnClickListener {
                clickedWidgetData()

            }
        }


        private fun clickedWidgetData() {
          val listOfAllChecker   = listOf( chipsData[adapterPosition].cta?.androidUrl,chipsData[adapterPosition].cta?.type, chipsData[adapterPosition].tenure,  chipsData[adapterPosition].productCode, chipsData[adapterPosition].cta?.bottomsheet, chipsData[adapterPosition].gatewayId, chipsData[adapterPosition].userStatus,chipsData[adapterPosition].name,  chipsData[adapterPosition].linkingStatus,chipsData[adapterPosition].installmentAmount)
           if(safeLet(listOfAllChecker) == true)
             {
                widgetClickListner.clickedWidget(
                    FintechRedirectionWidgetDataClass(
                        cta = chipsData[adapterPosition].cta?.type!!,
                        redirectionUrl = chipsData[adapterPosition].cta?.androidUrl,
                        tenure =  chipsData[adapterPosition].tenure!!,
                        gatewayId = chipsData[adapterPosition].gatewayId!!,
                        gatewayCode =  chipsData[adapterPosition].productCode,
                        widgetBottomSheet = chipsData[adapterPosition].cta?.bottomsheet,
                        userStatus =  chipsData[adapterPosition].userStatus,
                        linkingStatus = chipsData[adapterPosition].linkingStatus,
                        gatewayPartnerName =  chipsData[adapterPosition].name,
                        installmentAmout = chipsData[adapterPosition].installmentAmount
                    )
                )
            }
        }

    }

      fun safeLet(listOfAllChecker: List<Any?>): Any {
        var counter = 0
        for(i in listOfAllChecker.indices)
        {
            if(listOfAllChecker[i] == null) {
                counter = -1
                break;
            }
        }
        return counter != -1

    }

}


