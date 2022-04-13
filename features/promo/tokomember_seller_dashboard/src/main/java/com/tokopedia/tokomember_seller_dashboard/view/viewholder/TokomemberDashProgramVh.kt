package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE_OLDER
import com.tokopedia.tokomember_seller_dashboard.util.BUNDLE_OPTION_MENU
import com.tokopedia.tokomember_seller_dashboard.util.CANCELED
import com.tokopedia.tokomember_seller_dashboard.util.DRAFT
import com.tokopedia.tokomember_seller_dashboard.util.ENDED
import com.tokopedia.tokomember_seller_dashboard.util.WAITING
import com.tokopedia.tokomember_seller_dashboard.view.fragment.TokomemberOptionsMenuBottomsheet
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class TokomemberDashProgramVh(itemView: View, val fragmentManager: FragmentManager) : RecyclerView.ViewHolder(itemView) {

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
    lateinit var optionMenu: ImageUnify

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
        optionMenu = itemView.findViewById(R.id.optionMenu)

        programStatus.text = item.statusStr
        programStartDate.text = item.timeWindow?.startTime?.let { getDate(it) }
        programEndDate.text = item.timeWindow?.endTime?.let { getDate(it) }
        programStartTime.text = item.timeWindow?.startTime?.let { getTime(it) }
        programEndTime.text = item.timeWindow?.endTime?.let { getTime(it) }

        programMemberValue.text = item.analytics?.totalNewMember
        programMemberTransaksivalue.text = item.analytics?.trxCount

        if(item.actions?.tripleDots.isNullOrEmpty()){
            optionMenu.hide()
        }

        optionMenu.setOnClickListener {
            val bundle = Bundle()
            bundle.putString(BUNDLE_OPTION_MENU, Gson().toJson(item.actions))
            TokomemberOptionsMenuBottomsheet.show(bundle, fragmentManager)
        }

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