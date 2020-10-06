package com.tokopedia.play.widget.ui

import android.view.View
import android.widget.FrameLayout
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.elyeproj.loaderviewlibrary.LoaderImageView
import com.tokopedia.play.widget.PlayWidgetViewHolder
import com.tokopedia.play.widget.R


/**
 * Created by mzennis on 06/10/20.
 */
class PlayWidgetMediumView(itemView: View) : PlayWidgetViewHolder(itemView) {

    private val background: LoaderImageView = itemView.findViewById(R.id.play_widget_medium_bg_loader)
    private val shimmering: FrameLayout = itemView.findViewById(R.id.play_widget_medium_shimmering)

    private val title: Typography = itemView.findViewById(R.id.play_widget_medium_title)
    private val actionTitle: Typography = itemView.findViewById(R.id.play_widget_medium_action)

    private val itemContainer: FrameLayout = itemView.findViewById(R.id.play_widget_container)
    private val overlay: FrameLayout = itemView.findViewById(R.id.play_widget_overlay)
    private val overlayBackground: AppCompatImageView = itemView.findViewById(R.id.play_widget_overlay_bg)
    private val overlayImage: AppCompatImageView = itemView.findViewById(R.id.play_widget_overlay_image)

    private val recyclerViewItem: RecyclerView = itemView.findViewById(R.id.play_widget_recycler_view)

}