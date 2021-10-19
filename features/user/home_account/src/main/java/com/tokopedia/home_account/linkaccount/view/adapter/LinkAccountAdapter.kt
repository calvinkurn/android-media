package com.tokopedia.home_account.linkaccount.view.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.linkaccount.data.UserAccountDataView
import com.tokopedia.home_account.linkaccount.listener.AccountItemListener
import com.tokopedia.home_account.linkaccount.view.adapter.viewholder.AccountViewHolder

/**
 * Created by Yoris on 04/08/21.
 */

class LinkAccountAdapter(private val listener: AccountItemListener): RecyclerView.Adapter<AccountViewHolder>() {

    private var internalList = arrayListOf<UserAccountDataView>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AccountViewHolder {
        return AccountViewHolder(listener,
            LayoutInflater.from(parent.context).inflate(AccountViewHolder.LAYOUT, parent, false))
    }

    override fun onBindViewHolder(holder: AccountViewHolder, position: Int) {
        holder.bind(internalList[position])
    }

    override fun getItemCount(): Int = internalList.size

    fun setItems(list: List<UserAccountDataView>){
        internalList.clear()
        internalList.addAll(list)
        notifyDataSetChanged()
    }
}