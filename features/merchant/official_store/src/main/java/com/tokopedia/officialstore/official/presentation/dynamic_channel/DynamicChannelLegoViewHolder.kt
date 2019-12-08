package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatTextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Channel
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Header
import com.tokopedia.unifyprinciples.Typography

class DynamicChannelLegoViewHolder(
        view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelViewModel>(view) {

    private val mainContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_lego_main_container)
    private val headerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_header_main_container)
    private val headerTitle = itemView.findViewById<Typography>(R.id.dc_header_title)
    private val headerCountDown = itemView.findViewById<CountDownView>(R.id.dc_header_count_down)
    private val headerActionText = itemView.findViewById<AppCompatTextView>(R.id.dc_header_action_text)
    private val contentList = itemView.findViewById<RecyclerView>(R.id.dc_lego_rv)

    override fun bind(element: DynamicChannelViewModel?) {
        element?.run {
            dcEventHandler.legoImpression(dynamicChannelData)
            setupHeader(dynamicChannelData.header)
            setupContent(dynamicChannelData)
        }
    }

    private fun setupHeader(header: Header?) {
        if (header != null && header.name.isNotEmpty()) {
            headerContainer.visibility = View.VISIBLE
            headerTitle.text = header.name
            headerCountDown.visibility = View.GONE

            if (header.applink.isNotEmpty()) {
                headerActionText.apply {
                    visibility = View.VISIBLE
                    setOnClickListener(dcEventHandler.onClickLegoHeaderActionText(header.applink))
                }
            } else {
                headerActionText.visibility = View.GONE
            }
        } else {
            headerContainer.visibility = View.GONE
        }
    }

    private fun setupContent(channelData: Channel) {
        if (!channelData.grids.isNullOrEmpty()) {
            mainContainer.visibility = View.VISIBLE

            contentList.apply {
                layoutManager = GridLayoutManager(
                        itemView.context,
                        columnNum,
                        GridLayoutManager.VERTICAL,
                        false
                )
                adapter = LegoListAdapter(channelData, dcEventHandler)
            }
        } else {
            mainContainer.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_lego_main

        private const val columnNum = 3
    }
}
