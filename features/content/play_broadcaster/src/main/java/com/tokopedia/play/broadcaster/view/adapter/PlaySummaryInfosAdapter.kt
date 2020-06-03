package com.tokopedia.play.broadcaster.view.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.ui.model.SummaryUiModel
import kotlinx.android.synthetic.main.item_play_summary_info.view.*

/**
 * @author by jessica on 26/05/20
 */

class PlaySummaryInfosAdapter(private val infos: List<SummaryUiModel.LiveInfo>): RecyclerView.Adapter<PlaySummaryInfosAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
            ViewHolder(LayoutInflater.from(parent.context).inflate(VH_LAYOUT, parent, false))

    override fun getItemCount(): Int = infos.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        with(holder.itemView) {
            iv_item_play_summary_info_icon.loadImage(infos[position].liveInfoIcon)
            tv_item_play_summary_description.text = infos[position].liveInfoDescription
            tv_item_play_summary_count_info.text = infos[position].liveInfoCount
        }
    }

    class ViewHolder(view: View): RecyclerView.ViewHolder(view)

    companion object {
        val VH_LAYOUT = R.layout.item_play_summary_info
    }
}