package com.tokopedia.expresscheckout.view.profile.adapter

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.tokopedia.expresscheckout.view.profile.CheckoutProfileActionListener
import com.tokopedia.expresscheckout.view.profile.viewholder.ProfileViewHolder
import com.tokopedia.expresscheckout.view.profile.viewmodel.ProfileViewModel

/**
 * Created by Irfan Khoirul on 01/01/19.
 */

class CheckoutProfileAdapter(val data: ArrayList<ProfileViewModel>, val listener: CheckoutProfileActionListener) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    public fun setData(newData: ArrayList<ProfileViewModel>) {
        data.clear()
        data.addAll(newData)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(viewType, parent, false)
        return ProfileViewHolder(view, listener)
    }

    override fun getItemCount(): Int {
        return data.size
    }

    override fun getItemViewType(position: Int): Int {
        return ProfileViewHolder.LAYOUT
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is ProfileViewHolder) {
            holder.bind(data[position])
        }
    }


}