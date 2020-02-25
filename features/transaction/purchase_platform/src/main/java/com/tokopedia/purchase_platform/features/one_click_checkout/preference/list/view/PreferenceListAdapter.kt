package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference

class PreferenceListAdapter(private val listener: PreferenceListAdapterListener) : ListAdapter<Preference, PreferenceListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Preference>() {
            override fun areItemsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Preference, newItem: Preference): Boolean {
                return oldItem == newItem
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferenceListViewHolder {
        return PreferenceListViewHolder(
                LayoutInflater.from(parent.context).inflate(PreferenceListViewHolder.LAYOUT, parent, false),
                listener)
    }

    override fun onBindViewHolder(holder: PreferenceListViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    interface PreferenceListAdapterListener {

        fun onPreferenceSelected(preference: Preference)

        fun onPreferenceEditClicked(preference: Preference)
    }
}