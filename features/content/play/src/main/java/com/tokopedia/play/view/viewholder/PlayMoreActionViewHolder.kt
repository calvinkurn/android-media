package com.tokopedia.play.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.play.R
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val ivIcon = itemView.findViewById<ImageView>(R.id.iv_icon)
    private val tvSubtitle = itemView.findViewById<TextView>(R.id.tv_subtitle)

    fun bind(item: PlayMoreActionUiModel) {
        ivIcon.shouldShowWithAction(item.isIconAvailable){
            ivIcon.setImageResource(item.iconRes)
        }
        tvSubtitle.text = getString(item.subtitleRes)
        itemView.setOnClickListener { item.onClick(item) }
    }
}