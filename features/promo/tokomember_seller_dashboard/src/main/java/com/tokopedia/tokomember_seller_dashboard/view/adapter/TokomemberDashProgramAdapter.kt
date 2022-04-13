package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.model.ProgramSellerListItem
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberDashProgramVh

class TokomemberDashProgramAdapter(
    var programSellerList: ArrayList<ProgramSellerListItem>,
    val fragmentManager: FragmentManager
) :
    RecyclerView.Adapter<TokomemberDashProgramVh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TokomemberDashProgramVh {
        return TokomemberDashProgramVh(
            View.inflate(
                parent.context,
                R.layout.tm_dash_program_item,
                null
            ),
            fragmentManager
        )
    }

    override fun onBindViewHolder(holder: TokomemberDashProgramVh, position: Int) {
        holder.bind(programSellerList[position])
    }

    override fun getItemCount() = programSellerList.size

}