package com.tokopedia.home_account.account_settings.presentation.adapter.setting

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home_account.account_settings.presentation.listener.ImageQualitySettingListener
import com.tokopedia.home_account.account_settings.presentation.uimodel.MediaQualityUIModel
import com.tokopedia.home_account.account_settings.presentation.viewholder.ImageQualitySettingViewHolder

class ImageQualitySettingAdapter(
        private val itemList: List<MediaQualityUIModel>,
        private val listener: ImageQualitySettingListener
): RecyclerView.Adapter<ImageQualitySettingViewHolder>() {

    var previousPosition = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
    ): ImageQualitySettingViewHolder {
        return ImageQualitySettingViewHolder.viewHolder(parent)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ImageQualitySettingViewHolder, position: Int) {
        val item = itemList[position]
        holder.bind(item, position, previousPosition)
        holder.itemView.setOnClickListener {
            if(previousPosition != position) {
                previousPosition = position
                notifyDataSetChanged()
                listener.onOptionClicked(position)
            }
        }
    }

}