package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.home.R
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.dynamic_channel.ErrorStateAtfModel
import com.tokopedia.home.databinding.HomeErrorStateAtfPositionBinding
import com.tokopedia.utils.view.binding.viewBinding

class HomeAtfErrorViewHolder(itemView: View, private val listener: HomeCategoryListener?)
    : AbstractViewHolder<ErrorStateAtfModel>(itemView) {

    private var binding: HomeErrorStateAtfPositionBinding? by viewBinding()
    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_error_state_atf_position
    }

    override fun bind(element: ErrorStateAtfModel) {
        binding?.localloadErrorStateAtf?.progressState = false

        binding?.localloadErrorStateAtf?.refreshBtn?.setOnClickListener {
            listener?.refreshHomeData(forceRefresh = true)
            binding?.localloadErrorStateAtf?.progressState = true
        }
    }
}