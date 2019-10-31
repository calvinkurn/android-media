package com.tokopedia.officialstore.official.presentation.dynamic_channel

import android.view.View
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.countdown.CountDownView
import com.tokopedia.officialstore.R
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Grid
import com.tokopedia.officialstore.official.data.model.dynamic_channel.Header
import com.tokopedia.unifyprinciples.Typography

class DynamicChannelSprintSaleViewHolder(
        private val view: View?,
        private val dcEventHandler: DynamicChannelEventHandler
) : AbstractViewHolder<DynamicChannelViewModel>(view) {

    private val columnNum = 3
    private val mainContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_sprintsale_main_container)
    private val headerContainer = itemView.findViewById<ConstraintLayout>(R.id.dc_header_main_container)
    private val headerTitle = itemView.findViewById<Typography>(R.id.dc_header_title)
    private val headerCountDown = itemView.findViewById<CountDownView>(R.id.dc_header_count_down)
    private val headerActionText = itemView.findViewById<Typography>(R.id.dc_header_action_text)
    private val contentList = itemView.findViewById<RecyclerView>(R.id.dc_sprintsale_rv)

    override fun bind(element: DynamicChannelViewModel?) {
        element?.run {
            setupHeader(dynamicChannelData.header)
            setupContent(dynamicChannelData.grids)
        }
    }

    private fun setupHeader(header: Header?) {
        if (header != null && header.name.isNotEmpty()) {
            headerContainer.visibility = View.VISIBLE
            headerTitle.text = header.name

            if (header.expiredTime.isNotEmpty()) {
                val expiredTime = OfficialStoreDateHelper.getExpiredTime(header.expiredTime)

                headerCountDown.setup(
                        OfficialStoreDateHelper.getServerTimeOffset(header.serverTime),
                        expiredTime,
                        dcEventHandler
                )
                headerCountDown.visibility = View.VISIBLE
            } else {
                headerCountDown.visibility = View.GONE
            }

            if (header.applink.isNotEmpty()) {
                headerActionText.apply {
                    visibility = View.VISIBLE
                    setOnClickListener {
                        RouteManager.route(view?.context, header.applink)
                    }
                }
            } else {
                headerActionText.visibility = View.GONE
            }
        } else {
            headerContainer.visibility = View.GONE
        }
    }

    private fun setupContent(grids: MutableList<Grid?>?) {
        if (!grids.isNullOrEmpty()) {
            mainContainer.visibility = View.VISIBLE

            contentList.apply {
                layoutManager = GridLayoutManager(
                        itemView.context,
                        columnNum,
                        GridLayoutManager.VERTICAL,
                        false
                )
                adapter = SprintSaleListAdapter(view?.context, grids)
            }
        } else {
            mainContainer.visibility = View.GONE
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.dynamic_channel_sprintsale_main
    }
}
