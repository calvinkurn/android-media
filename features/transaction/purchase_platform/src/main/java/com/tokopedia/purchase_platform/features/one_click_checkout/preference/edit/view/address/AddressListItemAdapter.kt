package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference

class AddressListItemAdapter : ListAdapter<Preference, AddressListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object: DiffUtil.ItemCallback<Preference>(){
            override fun areItemsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddressListViewHolder {
        return AddressListViewHolder(LayoutInflater.from(parent.context).inflate(AddressListViewHolder.Layout, parent, false))
    }

    override fun onBindViewHolder(holder: AddressListViewHolder, position: Int) {

    }

}