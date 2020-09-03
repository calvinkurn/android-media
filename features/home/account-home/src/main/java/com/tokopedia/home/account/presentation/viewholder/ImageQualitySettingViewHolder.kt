package com.tokopedia.home.account.presentation.viewholder

import android.view.View
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.viewmodel.ImageQualitySettingItemViewModel
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.unifyprinciples.Typography

class ImageQualitySettingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    private val titleOption: Typography = itemView.findViewById(R.id.text_title_radio)
    private val subtitleOption: Typography = itemView.findViewById(R.id.text_subtitle_radio)
    private val imageOption: ImageView = itemView.findViewById(R.id.image_radio_item)

    fun bindData(item: ImageQualitySettingItemViewModel, position: Int, previousPosition: Int) {
        titleOption.text = item.title
        subtitleOption.text = item.subtitle
        if(previousPosition == position) {
            imageOption.loadImageDrawable(R.drawable.ic_radio_button_selected)
        } else {
            imageOption.loadImageDrawable(R.drawable.ic_radio_button_empty)
        }
    }
}