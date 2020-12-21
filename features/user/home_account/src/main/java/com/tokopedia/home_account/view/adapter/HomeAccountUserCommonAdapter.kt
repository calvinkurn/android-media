package com.tokopedia.home_account.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.CommonViewHolder

/**
 * Created by Yoris Prayogo on 16/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */
class HomeAccountUserCommonAdapter(val listener: HomeAccountUserListener, val type: Int): RecyclerView.Adapter<CommonViewHolder>() {

    var list: MutableList<CommonDataView> = mutableListOf()

    override fun getItemCount(): Int = list.size

    private var mExpandedPosition = -1

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(type, parent, false)
        return CommonViewHolder(view, listener)
    }
}