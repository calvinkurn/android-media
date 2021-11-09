package com.tokopedia.digital.home.presentation.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewDigitalHomeSearchEmptyBinding

class DigitalHomePageSearchEmptyStateViewHolder (itemView: View?) :
        AbstractViewHolder<EmptyModel>(itemView) {

    override fun bind(element: EmptyModel) {
        val bind = ViewDigitalHomeSearchEmptyBinding.bind(itemView)
    }

    companion object {
        val LAYOUT = R.layout.view_digital_home_search_empty
    }
}