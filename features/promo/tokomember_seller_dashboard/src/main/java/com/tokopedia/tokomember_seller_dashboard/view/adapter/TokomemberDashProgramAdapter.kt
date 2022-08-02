package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberDashProgramVh

class TokomemberDashProgramAdapter(
    var programSellerList: ArrayList<ProgramSellerListItem>,
    val fragmentManager: FragmentManager,
    val shopId: Int,
    val programActions: ProgramActions,
    val homeFragmentCallback: TmProgramDetailCallback
) :
    RecyclerView.Adapter<TokomemberDashProgramVh>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokomemberDashProgramVh {
        return TokomemberDashProgramVh(
            LayoutInflater.from(parent.context).inflate(R.layout.tm_dash_program_item,parent,false), fragmentManager
        )
    }

    override fun onBindViewHolder(holder: TokomemberDashProgramVh, position: Int) {
        holder.bind(programSellerList[position], shopId, programActions, homeFragmentCallback)
    }

    override fun getItemCount() = programSellerList.size

}