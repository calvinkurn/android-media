package com.tokopedia.shop.setting.view.adapter.viewholder

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.setting.view.adapter.ShopPageSettingAdapter

class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private var changeProfileView: TextView? = null
    private var changeNoteView: TextView? = null
    private var editSchedulerView: TextView? = null

    init {
        changeProfileView = itemView.findViewById(R.id.tv_change_profile)
        changeNoteView = itemView.findViewById(R.id.tv_change_note)
        editSchedulerView = itemView.findViewById(R.id.tv_edit_schedule)
    }

    fun bind(clickListener: ShopPageSettingAdapter.ProfileItemClickListener) {
        changeProfileView?.setOnClickListener{clickListener.onChangeProfileClicked()}
        changeNoteView?.setOnClickListener{clickListener.onChangeShopNoteClicked()}
        editSchedulerView?.setOnClickListener{clickListener.onEditShopScheduleClicked()}
    }
}