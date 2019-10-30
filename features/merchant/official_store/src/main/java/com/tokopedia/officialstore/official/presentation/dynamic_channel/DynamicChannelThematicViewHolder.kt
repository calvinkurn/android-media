package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.graphics.Color
import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Banner
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Header
import com.tokopedia.unifyprinciples.Typography

class DynamicChannelThematicViewHolder(
        private val view: View?
) : AbstractViewHolder<DynamicChannelViewModel>(view) {

    private val bannerImageCornerRadius: Float = 16f
    private val mainContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_thematic_main_container)
    private val headerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_header_main_container)
    private val headerTitle = itemView.findViewById<Typography>(R.id.dc_header_title)
    private val headerCountDown = itemView.findViewById<CountDownView>(R.id.dc_header_count_down)
    private val headerActionText = itemView.findViewById<Typography>(R.id.dc_header_action_text)
    private val bannerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_thematic_banner_container)
    private val bannerTitle = itemView.findViewById<Typography>(R.id.dc_thematic_banner_title)
    private val bannerDescription = itemView.findViewById<Typography>(R.id.dc_thematic_banner_description)
    private val bannerImage = itemView.findViewById<AppCompatImageView>(R.id.dc_thematic_banner_image)
    private val contentList = itemView.findViewById<RecyclerView>(R.id.dc_thematic_rv)

    override fun bind(element: DynamicChannelViewModel?) {
        element?.run {
            setupHeader(dynamicChannelData.header)
            setupBanner(dynamicChannelData.banner)
            setupContent(dynamicChannelData.grids)
        }
    }

    private fun setupHeader(header: Header?) {
        if (header != null && header.name.isNotEmpty()) {
            headerContainer.visibility = View.VISIBLE
            headerTitle.text = header.name
            headerCountDown.visibility = View.GONE

            if (header.applink.isNotEmpty()) {
                headerActionText.visibility = View.VISIBLE
                headerActionText.setOnClickListener {
                    RouteManager.route(view?.context, header.applink)
                }
            } else {
                headerActionText.visibility = View.GONE
            }

        } else {
            headerContainer.visibility = View.GONE
        }
    }

    private fun setupBanner(banner: Banner?) {
        if (banner != null && banner.imageUrl.isNotEmpty()) {
            bannerContainer.visibility = View.VISIBLE

            bannerTitle.apply {
                text = banner.title
                setTextColor(Color.parseColor(banner.textColor))
            }

            bannerDescription.apply {
                text = banner.description
                setTextColor(Color.parseColor(banner.textColor))
            }

            Glide.with(itemView.context)
                    .asBitmap()
                    .load(banner.imageUrl)
                    .centerCrop()
                    .dontAnimate()
                    .into(OfficialStoreImageHelper.getRoundedImageViewTarget(bannerImage, bannerImageCornerRadius))
        } else {
            bannerContainer.visibility = View.GONE
        }
    }

    private fun setupContent(grids: MutableList<Grid?>?) {
        if (!grids.isNullOrEmpty()) {
            mainContainer.visibility = View.VISIBLE

            contentList.apply {
                layoutManager = LinearLayoutManager(itemView.context, LinearLayoutManager.HORIZONTAL, false)
                adapter = ThematicListAdapter(view?.context, grids)
            }
        } else {
            mainContainer.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_thematic_main
    }
}
