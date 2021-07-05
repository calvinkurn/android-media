package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateAtfModel
import kotlinx.android.synthetic.main.home_error_state_atf_position.view.*

class HomeAtfErrorViewHolder(itemView: View, private val listener: HomeCategoryListener?)
    : AbstractViewHolder<ErrorStateAtfModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_error_state_atf_position
    }

    override fun bind(element: ErrorStateAtfModel) {
        itemView.localload_error_state_atf.progressState = false

        itemView.localload_error_state_atf.refreshBtn?.setOnClickListener {
            listener?.refreshHomeData(forceRefresh = true)
            itemView.localload_error_state_atf.progressState = true
        }
    }
}