package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel

class PreferenceListAdapter(private val listener: PreferenceListAdapterListener) : ListAdapter<ProfilesItemModel, PreferenceListViewHolder>(DIFF_CALLBACK) {

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ProfilesItemModel>() {
            override fun areItemsTheSame(oldItem: ProfilesItemModel, newItem: ProfilesItemModel): Boolean {
                return oldItem.profileId == newItem.profileId
            }

            override fun areContentsTheSame(oldItem: ProfilesItemModel, newItem: ProfilesItemModel): Boolean {
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

        fun onPreferenceSelected(preference: ProfilesItemModel)

        fun onPreferenceEditClicked(preference: ProfilesItemModel)
    }
}