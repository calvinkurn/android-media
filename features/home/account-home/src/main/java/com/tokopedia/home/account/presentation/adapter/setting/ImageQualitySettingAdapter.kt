package com.tokopedia.home.account.presentation.adapter.setting

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.listener.ImageQualitySettingListener
import com.tokopedia.home.account.presentation.viewholder.ImageQualitySettingViewHolder
import com.tokopedia.home.account.presentation.viewmodel.ImageQualitySettingItemViewModel

class ImageQualitySettingAdapter(private val listener: ImageQualitySettingListener): RecyclerView.Adapter<ImageQualitySettingViewHolder>() {

    var itemList = listOf<ImageQualitySettingItemViewModel>()

    var previousPosition = 0
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageQualitySettingViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_image_quality_option, parent, false)
        return ImageQualitySettingViewHolder(view)
    }

    override fun getItemCount(): Int = itemList.size

    override fun onBindViewHolder(holder: ImageQualitySettingViewHolder, position: Int) {
        val item = itemList[position]
        holder.bindData(item, position, previousPosition)
        holder.itemView.setOnClickListener {
            if(previousPosition != position) {
                previousPosition = position
                notifyDataSetChanged()
                listener.onOptionClicked(position)
            }
        }
    }
}