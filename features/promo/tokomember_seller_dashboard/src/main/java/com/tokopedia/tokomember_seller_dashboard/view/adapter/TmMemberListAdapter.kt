package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.model.DataModel
import com.tokopedia.tokomember_seller_dashboard.model.UserCardMemberModel
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmMemberListVh
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmMemberLoaderVh

class TmMemberListAdapter(private var members:List<DataModel>,private val context:Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun getItemViewType(position: Int): Int {
        return when(members[position]){
            is UserCardMemberModel -> TmMemberListVh.LAYOUT_TYPE
            else -> TmMemberLoaderVh.LAYOUT_TYPE
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
       return when(viewType){
            TmMemberListVh.LAYOUT_TYPE -> {
                val layout = inflater.inflate(TmMemberListVh.LAYOUT_TYPE,parent,false)
                TmMemberListVh(layout,context)
            }
           else -> {
               val layout = inflater.inflate(TmMemberLoaderVh.LAYOUT_TYPE,parent,false)
               TmMemberLoaderVh(layout)
           }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val data = members[position]
        when(holder){
            is TmMemberListVh -> {
                holder.bind(data as UserCardMemberModel)
            }
        }
    }

    override fun getItemCount() = members.size

    fun updateList(newList : List<DataModel>){
         members = newList
    }
}