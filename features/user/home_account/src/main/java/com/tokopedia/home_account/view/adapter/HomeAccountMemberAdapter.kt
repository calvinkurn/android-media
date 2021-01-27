package com.tokopedia.home_account.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.home_account.AccountConstants
import com.tokopedia.home_account.data.model.MemberItemDataView
import com.tokopedia.home_account.view.listener.HomeAccountUserListener
import com.tokopedia.home_account.view.viewholder.ErrorItemViewHolder
import com.tokopedia.home_account.view.viewholder.MemberItemViewHolder
import com.tokopedia.unifycomponents.CardUnify
import kotlinx.android.synthetic.main.home_account_item_error.view.*

/**
 * Created by Yoris Prayogo on 19/10/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class HomeAccountMemberAdapter(val listener: HomeAccountUserListener): RecyclerView.Adapter<BaseViewHolder>() {

    var list: ArrayList<MemberItemDataView> = arrayListOf()

    override fun getItemCount(): Int = list.size

    fun addItems(itemList: List<MemberItemDataView>) {
        this.list.clear()
        this.list.addAll(itemList)
        notifyDataSetChanged()
    }

    override fun getItemViewType(position: Int): Int {
        return this.list[position].type
    }

    override fun onBindViewHolder(holder: BaseViewHolder, position: Int) {
        when(holder) {
            is MemberItemViewHolder -> {
                holder.bind(list[position])
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseViewHolder {
        return when (viewType) {
            AccountConstants.LAYOUT.TYPE_ERROR -> {
                val view = LayoutInflater.from(parent.context).inflate(ErrorItemViewHolder.LAYOUT, parent, false)
                view.home_account_item_error_container?.cardType = CardUnify.TYPE_CLEAR
                view.setOnClickListener { listener.onMemberErrorClicked() }
                ErrorItemViewHolder(view, listener)
            }
            else -> {
                val view = LayoutInflater.from(parent.context).inflate(MemberItemViewHolder.LAYOUT, parent, false)
                return MemberItemViewHolder(view, listener)
            }
        }
    }
}