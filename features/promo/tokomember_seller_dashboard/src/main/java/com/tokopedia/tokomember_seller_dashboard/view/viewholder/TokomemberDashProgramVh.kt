package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE_OLDER
import com.tokopedia.tokomember_seller_dashboard.util.CANCELED
import com.tokopedia.tokomember_seller_dashboard.util.DRAFT
import com.tokopedia.tokomember_seller_dashboard.util.ENDED
import com.tokopedia.tokomember_seller_dashboard.util.WAITING
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TokomemberDashProgramVh(itemView: View) : RecyclerView.ViewHolder(itemView) {

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
    lateinit var btn_edit: UnifyButton
    lateinit var view_status: View

    @SuppressLint("ResourcePackage")
    fun bind(item: ProgramSellerListItem) {
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
        btn_edit = itemView.findViewById(R.id.btn_edit)
        view_status = itemView.findViewById(R.id.view_status)

        programStatus.text = item.statusStr
        programStartDate.text = item.timeWindow?.startTime?.let { getDate(it) }
        programEndDate.text = item.timeWindow?.endTime?.let { getDate(it) }
        programStartTime.text = item.timeWindow?.startTime?.let { getTime(it) }
        programEndTime.text = item.timeWindow?.endTime?.let { getTime(it) }

        programMemberValue.text = item.analytics?.totalNewMember
        programMemberTransaksivalue.text = item.analytics?.trxCount

//        ivTime.setImageDrawable(getIconUnifyDrawable(itemView.context, IconUnify.CLOCK))
//        ivTime.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_icon)
//        ivTime.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_GN500))
//        ivMemberStatistics.setImageDrawable(getIconUnifyDrawable(itemView.context, IconUnify.PROJECT))
//        ivMemberStatistics.background = ContextCompat.getDrawable(itemView.context, R.drawable.bg_icon)
//        ivMemberStatistics.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_GN500))

        //TODO handle actions on 3 dots icon click

        when(item.status){
            DRAFT ->{
                btn_edit.hide()
            }
            WAITING ->{
                // Allow editing
                programStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_YN400)))
                view_status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_YN400))
                btn_edit.show()
            }
            ACTIVE ->{
                btn_edit.hide()
            }
            ACTIVE_OLDER ->{
                btn_edit.hide()
            }
            ENDED ->{
                btn_edit.hide()
            }
            CANCELED ->{
                programStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN400)))
                view_status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, R.color.Unify_NN400))
                btn_edit.hide()
            }
        }
    }

    private fun getDate(time: String): String {
        return time.substringBefore(" ")
    }

    private fun getTime(time: String): String {
        return time.substringAfter(" ")
    }
}