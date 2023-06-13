package com.tokopedia.digital.home.presentation.adapter.viewholder

import com.tokopedia.imageassets.TokopediaImageUrl

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.digital.home.R
import com.tokopedia.digital.home.databinding.ViewDigitalHomeSearchEmptyBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.media.loader.loadImage

class DigitalHomePageSearchEmptyStateViewHolder (itemView: View?, val emptyListener: DigitalHomepageSearchEmptyListener) :
        AbstractViewHolder<EmptyModel>(itemView) {

    override fun bind(element: EmptyModel) {
        val bind = ViewDigitalHomeSearchEmptyBinding.bind(itemView)
        with(bind){
            geEmptyState.apply {
                errorTitle.text = getString(R.string.recharge_home_search_empty_state)
                errorDescription.text = getString(R.string.recharge_home_search_empty_state_desc)
                errorAction.text = getString(R.string.recharge_home_search_empty_state_btn)
                errorSecondaryAction.hide()
                errorAction.setOnClickListener {
                    emptyListener.clearEmptyStateListener()
                }
                errorIllustration.loadImage(IMG_LINK)
                errorIllustration.adjustViewBounds = true
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.view_digital_home_search_empty
        const val IMG_LINK = TokopediaImageUrl.IMG_LINK
    }

    interface DigitalHomepageSearchEmptyListener {
        fun clearEmptyStateListener()
    }
}