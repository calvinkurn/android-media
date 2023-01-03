package com.tokopedia.play.view.viewholder

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.mediarouter.app.MediaRouteButton
import com.google.android.gms.cast.framework.CastButtonFactory
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.play.R
import com.tokopedia.play.view.type.PlayMoreActionType
import com.tokopedia.play.view.uimodel.PlayMoreActionUiModel

/**
 * Created by jegul on 10/12/19
 */
class PlayMoreActionViewHolder(itemView: View) : BaseViewHolder(itemView) {

    private val ivIcon = itemView.findViewById<ImageView>(R.id.iv_icon)
    private val tvSubtitle = itemView.findViewById<TextView>(R.id.tv_subtitle)
    private val btnCast = itemView.findViewById<MediaRouteButton>(R.id.btn_play_cast)

    fun bind(item: PlayMoreActionUiModel) {
        ivIcon.shouldShowWithAction(item.icon != null){
            ivIcon.setImageDrawable(item.icon)
        }
        tvSubtitle.text = getString(item.subtitleRes)
        itemView.setOnClickListener { item.onClick(item) }

        setupType(item.type)
    }

    private fun setupType(type: PlayMoreActionType) {
        when (type) {
            PlayMoreActionType.Chromecast -> {
                CastButtonFactory.setUpMediaRouteButton(btnCast.context, btnCast)
                btnCast.visible()
            }
            else -> btnCast.gone()
        }
    }
}
