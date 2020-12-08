package com.tokopedia.home_account.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.CommonViewHolder
import com.tokopedia.home_account.view.viewholder.ErrorItemViewHolder
import com.tokopedia.home_account.view.viewholder.FinancialItemViewHolder

/**
 * Created by Yoris Prayogo on 09/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountFinancialAdapter(val listener: HomeAccountUserListener): RecyclerView.Adapter<BaseViewHolder>() {

    var list: MutableList<CommonDataView> = arrayListOf()

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder){
            is FinancialItemViewHolder -> {
                holder.bind(list[position])
            }
            is ErrorItemViewHolder -> {
                holder.bind(list[position])
            }
        }
    }

    fun addItems(itemList: List<CommonDataView>) {
        this.list.clear()
        this.list.addAll(itemList)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when(viewType){
            CommonViewHolder.TYPE_ERROR -> {
                val view = LayoutInflater.from(parent.context).inflate(ErrorItemViewHolder.LAYOUT, parent, false)
                ErrorItemViewHolder(view, listener)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(FinancialItemViewHolder.LAYOUT, parent, false)
                FinancialItemViewHolder(view, listener)
            }
        }
    }
}