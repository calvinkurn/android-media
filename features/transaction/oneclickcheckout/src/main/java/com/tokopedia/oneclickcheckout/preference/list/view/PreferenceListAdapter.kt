package com.tokopedia.oneclickcheckout.preference.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel

class PreferenceListAdapter(private val listener: PreferenceListAdapterListener, private val currentProfileId: Int = -1) : RecyclerView.Adapter<PreferenceListViewHolder>() {

    private var list: ArrayList<ProfilesItemModel> = ArrayList()

    fun submitList(newList: List<ProfilesItemModel>?) {
        list.clear()
        if (newList != null) {
            list.addAll(newList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PreferenceListViewHolder {
        return PreferenceListViewHolder(
                LayoutInflater.from(parent.context).inflate(PreferenceListViewHolder.LAYOUT, parent, false),
                listener)
    }

    override fun onBindViewHolder(holder: PreferenceListViewHolder, position: Int) {
        holder.bind(list[position], currentProfileId, itemCount)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface PreferenceListAdapterListener {

        fun onPreferenceSelected(preference: ProfilesItemModel)

        fun onPreferenceEditClicked(preference: ProfilesItemModel, position: Int, profileSize: Int)
    }
}