package com.tokopedia.home.account.presentation.viewholder

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.home.account.R
import com.tokopedia.home.account.presentation.uimodel.MediaQualityUIModel
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.unifyprinciples.Typography

class ImageQualitySettingViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {

    private val titleOption: Typography = itemView.findViewById(R.id.text_title_radio)
    private val subtitleOption: Typography = itemView.findViewById(R.id.text_subtitle_radio)
    private val imageOption: ImageView = itemView.findViewById(R.id.image_radio_item)

    private val context by lazy { itemView.context }

    fun bind(item: MediaQualityUIModel, position: Int, previousPosition: Int) {
        titleOption.text = context.getString(item.title)
        subtitleOption.text = context.getString(item.subtitle)

        if (previousPosition == position) {
            imageOption.loadImageDrawable(R.drawable.ic_radio_button_selected)
        } else {
            imageOption.loadImageDrawable(R.drawable.ic_radio_button_empty)
        }
    }

    companion object {
        fun viewHolder(parent: ViewGroup): ImageQualitySettingViewHolder {
            return ImageQualitySettingViewHolder(LayoutInflater
                    .from(parent.context)
                    .inflate(
                            R.layout.item_image_quality_option,
                            parent,
                            false
                    ))
        }
    }

}