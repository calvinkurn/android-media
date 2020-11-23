package com.tokopedia.home_account.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.data.model.MemberItemDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountMemberAdapter(val listener: HomeAccountUserListener): RecyclerView.Adapter<MemberItemViewHolder>() {

    var list: List<MemberItemDataView> = mutableListOf()

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: MemberItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MemberItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(MemberItemViewHolder.LAYOUT, parent, false)

        return MemberItemViewHolder(view, listener)
    }
}