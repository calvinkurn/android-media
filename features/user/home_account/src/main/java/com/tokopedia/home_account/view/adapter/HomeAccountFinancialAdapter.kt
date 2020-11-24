package com.tokopedia.home_account.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.FinancialItemViewHolder

/**
 * Created by Yoris Prayogo on 09/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountFinancialAdapter(val listener: HomeAccountUserListener): RecyclerView.Adapter<FinancialItemViewHolder>() {

    var list: List<CommonDataView> = mutableListOf()

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: FinancialItemViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FinancialItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(FinancialItemViewHolder.LAYOUT, parent, false)

        return FinancialItemViewHolder(view, listener)
    }
}