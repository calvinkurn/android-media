package com.tokopedia.tokomember_seller_dashboard.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.tokomember_seller_dashboard.R
import com.tokopedia.tokomember_seller_dashboard.view.viewholder.TmMemberListVh

class TmMemberListAdapter(private val list:List<String>,private val context:Context) : RecyclerView.Adapter<TmMemberListVh>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TmMemberListVh {
        val inflater = LayoutInflater.from(context)
        val itemView = inflater.inflate(layout,parent,false)
        return TmMemberListVh(itemView, context)
    }

    override fun onBindViewHolder(holder: TmMemberListVh, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }

    companion object{
        private val layout = R.layout.tm_member_list_item
    }
}