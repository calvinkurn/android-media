package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.callbacks.ProgramActions
import com.tokopedia.tokomember_seller_dashboard.callbacks.TmProgramDetailCallback
import com.tokopedia.tokomember_seller_dashboard.model.LayoutType
import com.tokopedia.tokomember_seller_dashboard.model.ProgramItem
import com.tokopedia.tokomember_seller_dashboard.tracker.TmTracker
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmMemberLoaderVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TokomemberDashProgramVh

class TokomemberDashProgramAdapter(
    var programSellerList: MutableList<ProgramItem>,
    val fragmentManager: FragmentManager,
    val shopId: Int,
    val programActions: ProgramActions,
    val homeFragmentCallback: TmProgramDetailCallback
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var tmTracker : TmTracker? = null

    init {
        tmTracker = TmTracker()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return when(viewType){
            TokomemberDashProgramVh.LAYOUT -> {
                TokomemberDashProgramVh(
                    inflater.inflate(R.layout.tm_dash_program_item,parent,false), fragmentManager
                )
            }
            else -> {
                val layout = inflater.inflate(TmMemberLoaderVh.LAYOUT_TYPE,parent,false)
                TmMemberLoaderVh(layout)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is TokomemberDashProgramVh)
            holder.bind(programSellerList[position].programSellerListItem, shopId, programActions, homeFragmentCallback, tmTracker)
    }
    override fun getItemViewType(position: Int): Int {
        return when(programSellerList[position].layoutType){
            LayoutType.SHOW_CARD -> TokomemberDashProgramVh.LAYOUT
            LayoutType.LOADER -> TmMemberLoaderVh.LAYOUT_TYPE
        }
    }

    override fun getItemCount() = programSellerList.size

    fun submitList(list:List<ProgramItem>){
        programSellerList.clear()
        programSellerList.addAll(list)
    }
}
