package com.tokopedia.oneclickcheckout.preference.list.view

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel.Companion.MAIN_PROFILE_STATUS

class PreferenceListAdapter(private val listener: PreferenceListAdapterListener, private val currentProfileId: Int = -1) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var list: ArrayList<ProfilesItemModel> = ArrayList()

    private var isNewLayout: Boolean = false

    fun submitList(newList: List<ProfilesItemModel>?, isNewLayout: Boolean) {
        this.isNewLayout = isNewLayout
        list.clear()
        if (newList != null) {
            list.addAll(newList)
        }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            NewMainPreferenceListViewHolder.LAYOUT -> NewMainPreferenceListViewHolder(
                    LayoutInflater.from(parent.context).inflate(NewMainPreferenceListViewHolder.LAYOUT, parent, false),
                    listener)
            NewPreferenceListViewHolder.LAYOUT -> NewPreferenceListViewHolder(
                    LayoutInflater.from(parent.context).inflate(NewPreferenceListViewHolder.LAYOUT, parent, false),
                    listener)
            else -> PreferenceListViewHolder(
                    LayoutInflater.from(parent.context).inflate(PreferenceListViewHolder.LAYOUT, parent, false),
                    listener)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is PreferenceListViewHolder -> {
                holder.bind(list[position], currentProfileId, itemCount)
            }
            is NewMainPreferenceListViewHolder -> {
                holder.bind(list[position], currentProfileId, itemCount)
            }
            is NewPreferenceListViewHolder -> {
                holder.bind(list[position], currentProfileId, itemCount)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        if (!isNewLayout) {
            return PreferenceListViewHolder.LAYOUT
        }
        val mainProfile = list[position].status == MAIN_PROFILE_STATUS
        return if (mainProfile) NewMainPreferenceListViewHolder.LAYOUT else NewPreferenceListViewHolder.LAYOUT
    }

    override fun getItemCount(): Int {
        return list.size
    }

    interface PreferenceListAdapterListener {

        fun onPreferenceSelected(preference: ProfilesItemModel, isMainProfile: Boolean)

        fun onPreferenceEditClicked(preference: ProfilesItemModel, position: Int, profileSize: Int)
    }
}