package com.tokopedia.home_account.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.data.model.CommonDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.ErrorFinancialItemViewHolder
import com.tokopedia.home_account.view.viewholder.ErrorFinancialViewHolder
import com.tokopedia.home_account.view.viewholder.FinancialItemViewHolder
import java.util.*

/**
 * Created by Yoris Prayogo on 09/11/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountFinancialAdapter(val listener: HomeAccountUserListener) : RecyclerView.Adapter<BaseViewHolder>() {

    var list: MutableList<CommonDataView> = arrayListOf()

    override fun getItemCount(): Int = list.size

    override fun getItemViewType(position: Int): Int {
        return list[position].type
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when (holder) {
            is FinancialItemViewHolder -> {
                holder.bind(list[position])
            }
            is ErrorFinancialItemViewHolder -> {
                holder.bind(list[position])
            }
            is ErrorFinancialViewHolder -> {
                holder.bind()
            }
        }
    }

    fun addItems(itemList: List<CommonDataView>) {
        this.list.clear()
        this.list.addAll(itemList)
        orderItem()
    }

    fun addSingleItem(item: CommonDataView) {
        this.list.add(item)
        orderItem()
    }

    fun removeByType(type: Int) {
        this.list.find { it.type == type }?.let {
            this.list.remove(it)
        }
    }

    private fun orderItem() {
        val indexOvoTokopoints = this.list.indexOfFirst { it.type == FinancialItemViewHolder.TYPE_OVO_TOKOPOINTS }
        val indexSaldo = this.list.indexOfFirst { it.type == FinancialItemViewHolder.TYPE_SALDO }
        val indexErrorOvo = this.list.indexOfFirst { it.type == ErrorFinancialItemViewHolder.TYPE_ERROR_OVO }
        val indexErrorTokopoints = this.list.indexOfFirst { it.type == ErrorFinancialItemViewHolder.TYPE_ERROR_TOKOPOINTS }
        val indexErrorSaldo = this.list.indexOfFirst { it.type == ErrorFinancialItemViewHolder.TYPE_ERROR_SALDO }
        when {
            indexSaldo >= 0 -> {
                when {
                    indexOvoTokopoints > indexSaldo -> {
                        Collections.swap(this.list, indexOvoTokopoints, indexSaldo)
                    }
                    indexErrorTokopoints > indexSaldo -> {
                        Collections.swap(this.list, indexErrorTokopoints, indexSaldo)
                    }
                    indexErrorOvo > indexSaldo -> {
                        Collections.swap(this.list, indexErrorOvo, indexSaldo)
                    }
                }
            }
            indexErrorSaldo >= 0 -> {
                when {
                    indexOvoTokopoints > indexErrorSaldo -> {
                        Collections.swap(this.list, indexOvoTokopoints, indexErrorSaldo)
                    }
                    indexErrorTokopoints > indexErrorSaldo -> {
                        Collections.swap(this.list, indexErrorTokopoints, indexErrorSaldo)
                    }
                    indexErrorOvo > indexErrorSaldo -> {
                        Collections.swap(this.list, indexErrorOvo, indexErrorSaldo)
                    }
                }
            }
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            ErrorFinancialItemViewHolder.TYPE_ERROR_TOKOPOINTS,
            ErrorFinancialItemViewHolder.TYPE_ERROR_OVO,
            ErrorFinancialItemViewHolder.TYPE_ERROR_SALDO -> {
                createErrorFinancialItemViewHolder(parent)
            }
            ErrorFinancialViewHolder.ERROR_TYPE -> {
                createErrorFinancialViewHolder(parent)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(FinancialItemViewHolder.LAYOUT, parent, false)
                FinancialItemViewHolder(view, listener)
            }
        }
    }

    private fun createErrorFinancialItemViewHolder(parent: ViewGroup): ErrorFinancialItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(ErrorFinancialItemViewHolder.LAYOUT, parent, false)
        return ErrorFinancialItemViewHolder(view, listener)
    }

    private fun createErrorFinancialViewHolder(parent: ViewGroup): ErrorFinancialViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(ErrorFinancialViewHolder.LAYOUT, parent, false)
        return ErrorFinancialViewHolder(view, listener)
    }

    fun showError() {
        this.list.clear()
        this.list.add(CommonDataView(type = ErrorFinancialViewHolder.ERROR_TYPE))
        notifyDataSetChanged()
    }
}