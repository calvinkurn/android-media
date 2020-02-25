package com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import kotlinx.android.synthetic.main.card_preference.view.*

class PreferenceListViewHolder(itemView: View, private val listener: PreferenceListAdapter.PreferenceListAdapterListener) : RecyclerView.ViewHolder(itemView) {

    companion object {
        val LAYOUT = R.layout.card_preference
    }

    private val tvChoosePreference = itemView.tv_choose_preference
    private val ivEditPreference = itemView.iv_edit_preference

    fun bind(preference: Preference) {
        tvChoosePreference.setOnClickListener {
            listener.onPreferenceSelected(preference)
        }
        ivEditPreference.setOnClickListener {
            listener.onPreferenceEditClicked(preference)
        }
    }
}