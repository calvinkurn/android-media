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
import com.tokopedia.pdp.fintech.listner.WidgetClickListner
import com.tokopedia.pdp_fintech.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.resources.isDarkMode

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
            if (it == 0) {
                holder.dummyView.visibility = View.GONE
                holder.seeMoreIcon.visibility = View.VISIBLE
            } else {
                holder.dummyView.visibility = View.VISIBLE
                holder.seeMoreIcon.visibility = View.GONE
            }
        }

        chipsData[position].header?.let {
            if (it.isNotBlank()) {
                holder.headerPartner.visibility = View.VISIBLE
                holder.headerPartner.text = it.parseAsHtml()
            } else
                removeViewVisibility(holder.headerPartner)
        } ?: run {
            removeViewVisibility(holder.headerPartner)
        }

        chipsData[position].subheaderColor?.let {
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

        chipsData[position].subheader?.let {
            if (it.isNotBlank()) {
                holder.subheaderPartner.visibility = View.VISIBLE
                holder.subheaderPartner.text = it.parseAsHtml()
            } else
                removeViewVisibility(holder.subheaderPartner)
        } ?: run {
            removeViewVisibility(holder.subheaderPartner)

        }

        setIcon(position, holder.partnerIcon)
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
            safeLet(
                chipsData[adapterPosition].cta?.androidUrl,
                chipsData[adapterPosition].cta?.type,
                chipsData[adapterPosition].tenure,
                chipsData[adapterPosition].productCode,
                chipsData[adapterPosition].cta?.bottomsheet,
                chipsData[adapterPosition].gatewayId,
                chipsData[adapterPosition].name,
                chipsData[adapterPosition].userStatus,
                chipsData[adapterPosition].linkingStatus
            ) { url, ctaType, tenure, gatewayCode, bottomSheetWidgetDetail, gatewayId, userStatus, partnerName,linkingStatus ->

                widgetClickListner.clickedWidget(
                    FintechRedirectionWidgetDataClass(
                        cta = ctaType,
                        redirectionUrl = url,
                        tenure = tenure,
                        gatewayId = gatewayId,
                        productUrl = null,
                        gatewayCode = gatewayCode,
                        widgetBottomSheet = bottomSheetWidgetDetail,
                        userStatus = userStatus,
                        linkingStatus = linkingStatus,
                        gatewayPartnerName = partnerName
                    )
                )
            }
        }

    }


    // Null Checker
    inline fun <T1 : Any, T2 : Any, T3 : Any, T4 : Any, T5 : Any, T6 : Any, T7 : Any, T8 : Any,  T9 : Any, R : Any> safeLet(
        p1: T1?,
        p2: T2?,
        p3: T3?,
        p4: T4?,
        p5: T5?,
        p6: T6?,
        p7: T7?,
        p8: T8?,
        p9: T9?,
        block: (T1, T2, T3, T4, T5, T6, T7, T8, T9) -> R?
    ): R? {
        return if (p1 != null && p2 != null && p3 != null && p4 != null && p5 != null && p6 != null && p7 != null && p8 != null && p9 != null) block(
            p1,
            p2,
            p3,
            p4,
            p5,
            p6,
            p7,
            p8,
            p9
        ) else null
    }
}


