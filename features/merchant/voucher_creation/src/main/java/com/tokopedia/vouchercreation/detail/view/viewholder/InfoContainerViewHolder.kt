package com.tokopedia.vouchercreation.detail.view.viewholder

import android.content.Context
import android.view.View
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.detail.model.InfoContainerUiModel
import com.tokopedia.vouchercreation.detail.view.adapter.SubInfoAdapter
import kotlinx.android.synthetic.main.item_mvc_info_container.view.*

/**
 * Created By @ilhamsuaib on 05/05/20
 */

class InfoContainerViewHolder(itemView: View?) : AbstractViewHolder<InfoContainerUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.item_mvc_info_container
    }

    private val subInfoAdapter by lazy { SubInfoAdapter() }

    override fun bind(element: InfoContainerUiModel) {
        with(itemView) {
            tvMvcInfoTitle.text = element.title
            rvMvcSubInfo.layoutManager = getLinearLayoutManager(context)
            rvMvcSubInfo.adapter = subInfoAdapter
        }
        subInfoAdapter.setSubInfoItems(element.informationList)
    }

    private fun getLinearLayoutManager(context: Context): LinearLayoutManager {
        return object : LinearLayoutManager(context) {
            override fun canScrollVertically(): Boolean = false
        }
    }
}