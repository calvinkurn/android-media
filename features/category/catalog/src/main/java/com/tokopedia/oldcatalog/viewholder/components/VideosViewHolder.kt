package com.tokopedia.oldcatalog.viewholder.components

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.oldcatalog.analytics.CatalogDetailAnalytics
import com.tokopedia.oldcatalog.listener.CatalogDetailListener
import com.tokopedia.oldcatalog.model.raw.VideoComponentData
import com.tokopedia.kotlin.extensions.view.loadImageWithoutPlaceholder
import kotlinx.android.synthetic.main.item_catalog_video.view.*

class VideosViewHolder(v: View) : RecyclerView.ViewHolder(v) {
    fun bind(model: VideoComponentData, catalogDetailListener: CatalogDetailListener) {
        catalogDetailListener.sendWidgetImpressionEvent(
            CatalogDetailAnalytics.ActionKeys.VIDEO_WIDGET_IMPRESSION,
            CatalogDetailAnalytics.ActionKeys.VIDEO_WIDGET_IMPRESSION_ITEM_NAME,
            adapterPosition)
        itemView.video_title_tv.text = model.title
        itemView.channel_name.text = model.author
        itemView.video_thumbnail_iv.setOnClickListener {
            catalogDetailListener.playVideo(model,adapterPosition)
        }
        model.thumbnail?.let {
            itemView.video_thumbnail_iv.loadImageWithoutPlaceholder(model.thumbnail)
        }
    }
}
