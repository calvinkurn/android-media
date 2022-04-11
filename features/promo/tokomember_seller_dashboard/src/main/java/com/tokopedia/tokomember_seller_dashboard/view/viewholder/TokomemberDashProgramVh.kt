package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography

class TokomemberDashProgramVh(itemView: View) : RecyclerView.ViewHolder(itemView) {

    lateinit var ivTime: ImageUnify
    lateinit var ivMemberStatistics: ImageUnify
    lateinit var programStatus: Typography
    lateinit var periodProgram: Typography
    lateinit var programStartDate: Typography
    lateinit var programStartTime: Typography
    lateinit var programEndDate: Typography
    lateinit var programEndTime: Typography
    lateinit var programMemberLabel: Typography
    lateinit var programMemberValue: Typography
    lateinit var programMemberTransaksiLabel: Typography
    lateinit var programMemberTransaksivalue: Typography
    
    fun bind(item: ProgramSellerListItem) {

        ivTime = itemView.findViewById(R.id.ivTime)
        ivMemberStatistics = itemView.findViewById(R.id.ivMemberStatistics)
        programStatus = itemView.findViewById(R.id.programStatus)
        periodProgram = itemView.findViewById(R.id.periodProgram)
        programStartDate = itemView.findViewById(R.id.programStartDate)
        programStartTime = itemView.findViewById(R.id.programStartTime)
        programEndDate = itemView.findViewById(R.id.programEndDate)
        programEndTime = itemView.findViewById(R.id.programEndTime)
        programMemberLabel = itemView.findViewById(R.id.programMemberLabel)
        programMemberValue = itemView.findViewById(R.id.programMemberValue)
        programMemberTransaksiLabel = itemView.findViewById(R.id.programMemberTransaksiLabel)
        programMemberTransaksivalue = itemView.findViewById(R.id.programMemberTransaksivalue)

        programStatus.text = item.statusStr
        programStartDate.text = item.timeWindow?.startTime?.let { getDate(it) }
        programEndDate.text = item.timeWindow?.endTime?.let { getDate(it) }
        programStartTime.text = item.timeWindow?.startTime?.let { getTime(it) }
        programEndTime.text = item.timeWindow?.endTime?.let { getTime(it) }

        programMemberValue.text = item.analytics?.totalNewMember
        programMemberTransaksivalue.text = item.analytics?.trxCount

        ivTime.setImageDrawable(getIconUnifyDrawable(itemView.context, IconUnify.CLOCK))
        ivTime.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_icon)
        ivMemberStatistics.setImageDrawable(getIconUnifyDrawable(itemView.context, IconUnify.PROJECT))
        ivMemberStatistics.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_icon)

        //TODO handle actions on 3 dots icon click

    }

    private fun getDate(time: String): String {
        return time.substringBefore(" ")
    }

    private fun getTime(time: String): String {
        return time.substringAfter(" ")
    }
}