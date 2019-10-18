package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Color
import android.support.annotation.LayoutRes
import android.support.v7.widget.AppCompatImageView
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.officialstore.R
import com.tokopedia.unifyprinciples.Typography

// TODO: Update data mapping when back-end finished
class DynamicChannelThematicViewHolder(
        private val view: View?
) : AbstractViewHolder<DynamicChannelViewModel>(view) {

    private val bannerImageCornerRadius: Float = 16f
    private val bannerTitle = itemView.findViewById<Typography>(R.id.dc_thematic_banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.dc_thematic_banner_description)
    private val bannerImage = itemView.findViewById<AppCompatImageView>(R.id.dc_thematic_banner_image)
    private val contentList = itemView.findViewById<RecyclerView>(R.id.dc_thematic_rv)

    override fun bind(element: DynamicChannelViewModel?) {
        contentList.apply {
            layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ThematicListAdapter()
        }

        bannerTitle.apply {
            text = "Speaker"
            setTextColor(Color.WHITE)
        }

        bannerDescription.apply {
            text = "di bawah Rp 499.000"
            setTextColor(Color.WHITE)
        }

        Glide.with(itemView.context)
                .load("https://ecs7.tokopedia.net/img/attachment/2019/10/14/20723472/20723472_b1e575fc-1522-4a44-9100-d2652aa8d7ae.jpg")
                .asBitmap()
                .centerCrop()
                .dontAnimate()
                .into(getRoundedImageViewTarget(bannerImage, bannerImageCornerRadius))
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_thematic_main
    }
}
