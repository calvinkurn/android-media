package com.tokopedia.shop.settings.setting.view.adapter.viewholder

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.settings.databinding.ItemShopPageSettingProfileBinding
import com.tokopedia.shop.settings.setting.view.adapter.ShopPageSettingAdapter
import com.tokopedia.utils.view.binding.viewBinding

class ProfileViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    private val binding: ItemShopPageSettingProfileBinding? by viewBinding()

    fun bind(clickListener: ShopPageSettingAdapter.ProfileItemClickListener) {
        binding?.apply {
            tvChangeProfile.setOnClickListener{clickListener.onChangeProfileClicked()}
            tvChangeNote.setOnClickListener{clickListener.onChangeShopNoteClicked()}
            tvEditSchedule.setOnClickListener{clickListener.onEditShopScheduleClicked()}
        }
    }
}