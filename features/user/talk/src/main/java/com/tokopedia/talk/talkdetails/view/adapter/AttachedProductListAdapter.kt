package com.tokopedia.talk.talkdetails.view.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tokopedia.design.bottomsheet.BottomSheetCallAction
import com.tokopedia.talk.common.adapter.viewmodel.TalkProductAttachmentViewModel

class AttachedProductListAdapter(val data: List<TalkProductAttachmentViewModel>) : RecyclerView.Adapter<AttachedProductViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AttachedProductViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.talk_product_attachment, parent, false)
        return AttachedProductViewHolder(itemView)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun onBindViewHolder(holder: AttachedProductViewHolder, position: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class AttachedProductViewHolder(itemView:View) : RecyclerView.ViewHolder(itemView) {

    fun bind(element:TalkProductAttachmentViewModel) {

    }
}