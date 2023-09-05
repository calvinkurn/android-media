package com.tokopedia.tokomember_seller_dashboard.view.viewholder

import android.annotation.SuppressLint
import android.content.res.ColorStateList
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE
import com.tokopedia.tokomember_seller_dashboard.util.ACTIVE_OLDER
import com.tokopedia.tokomember_seller_dashboard.util.CANCELED
import com.tokopedia.tokomember_seller_dashboard.util.DRAFT
import com.tokopedia.tokomember_seller_dashboard.util.EDIT
import com.tokopedia.tokomember_seller_dashboard.util.ENDED
import com.tokopedia.tokomember_seller_dashboard.util.EXTEND
import com.tokopedia.tokomember_seller_dashboard.util.TmDateUtil
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
    fun bind(
        item: ProgramSellerListItem,
        shopId: Int,
        programActions: ProgramActions,
        homeFragmentCallback: TmProgramDetailCallback,
        tmTracker: TmTracker?
    ) {

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
        btn_edit = itemView.findViewById(com.tokopedia.tokomember_common_widget.R.id.btn_edit)
        view_status = itemView.findViewById(R.id.view_status)
        optionMenu = itemView.findViewById(com.tokopedia.tokomember_common_widget.R.id.optionMenu)

        programStatus.text = item.statusStr
        programStartDate.text = item.timeWindow?.startTime?.let { getDate(it) }
        programEndDate.text = item.timeWindow?.endTime?.let { getDate(it) }
        programStartTime.text = item.timeWindow?.startTime?.let { getTime(it) }
        programEndTime.text = item.timeWindow?.endTime?.let { getTime(it) }

        programMemberValue.text = if(item.analytics?.totalNewMember.isNullOrEmpty()) "-"
                                  else item.analytics?.totalNewMember
        programMemberTransaksivalue.text = if(item.analytics?.trxCount.isNullOrEmpty()) "-"
                                           else item.analytics?.trxCount

        if(item.actions?.tripleDots.isNullOrEmpty()){
            optionMenu.hide()
        }
        else{
            optionMenu.show()
        }

        itemView.setOnClickListener {
            item.id?.toIntOrZero()?.let { it1 ->
            tmTracker?.clickProgramItemButton(shopId.toString(), it1.toString())
                homeFragmentCallback.openDetailFragment(shopId, it1)
            }
//            val intent = Intent(itemView.context, TokomemberDashProgramDetailActivity::class.java)
//            intent.putExtra(BUNDLE_PROGRAM_ID, item.id?.toInt())
//            intent.putExtra(BUNDLE_SHOP_ID, shopId)
//            itemView.context.startActivity(intent)
        }

        optionMenu.setOnClickListener {
            item.id?.toIntOrZero()?.let { it1 ->
                if(item.status == WAITING){
                    tmTracker?.clickProgramWaitingThreeDot(shopId.toString(), it1.toString())
                }
                else {
                    tmTracker?.clickProgramActiveThreeDot(shopId.toString(), it1.toString())
                }
                TokomemberOptionsMenuBottomsheet.show(Gson().toJson(item.actions), shopId,
                    it1, fragmentManager, programActions, tmTracker)
            }
        }

        btn_edit.setOnClickListener {
            item.id?.toIntOrZero()?.let { it1 ->
                tmTracker?.clickProgramEdit(shopId.toString(), it1.toString())
                programActions.option(EDIT, programId = it1, shopId = shopId)
            }
        }
         /*   val intent = Intent(itemView.context, TokomemberDashCreateProgramActivity::class.java)
            intent.putExtra(BUNDLE_EDIT_PROGRAM, true)
            intent.putExtra(BUNDLE_SHOP_ID, shopId)
            intent.putExtra(BUNDLE_PROGRAM_ID, item.id)
            intent.putExtra(BUNDLE_PROGRAM_TYPE, ProgramType.EDIT)
            itemView.context.startActivity(intent)*/

        when(item.status){
            DRAFT ->{
            }
            WAITING ->{
                // Allow editing
                programStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_YN400)))
                view_status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_YN400))
                btn_edit.show()
            }
            ACTIVE ->{
                programStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)))
                view_status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                btn_edit.hide()
            }
            ACTIVE_OLDER ->{
                programStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500)))
                view_status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_GN500))
                btn_edit.show()
                btn_edit.text = "Perpanjang Program"
                btn_edit.setOnClickListener {
                    item.id?.toIntOrZero()?.let { it1 ->
                        tmTracker?.clickProgramExtensionButton(it1.toString(), shopId.toString())
                        programActions.option(EXTEND, programId = it1, shopId = shopId)
                    }
                }
            }
            ENDED ->{
                btn_edit.hide()
                programStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN400)))
                view_status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
            }
            CANCELED ->{
                btn_edit.hide()
                programStatus.setTextColor(ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN400)))
                view_status.backgroundTintList = ColorStateList.valueOf(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN400))
            }
            else ->{
                btn_edit.hide()
            }
        }
    }

    private fun getDate(time: String): String {
        return TmDateUtil.setDatePreview(time)
    }

    private fun getTime(time: String): String {
        return time.substringAfter(" ")
    }
    companion object{
        val LAYOUT = R.layout.tm_dash_program_item
    }
}
