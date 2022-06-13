package com.tokopedia.autocompletecomponent.initialstate.curatedcampaign

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocompletecomponent.R
import com.tokopedia.autocompletecomponent.databinding.LayoutAutocompleteCuratedCampaignCardBinding
import com.tokopedia.autocompletecomponent.initialstate.BaseItemInitialStateSearch
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.utils.view.binding.viewBinding

class CuratedCampaignViewHolder(
    itemView: View,
    private val listener: CuratedCampaignListener,
): AbstractViewHolder<CuratedCampaignDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_curated_campaign_card
    }

    private var binding: LayoutAutocompleteCuratedCampaignCardBinding? by viewBinding()

    override fun bind(element: CuratedCampaignDataView) {
        bindImage(element.baseItemInitialState)
        bindTitle(element.baseItemInitialState)
        bindSubtitle(element.baseItemInitialState)
        bindListener(element)
    }

    private fun bindImage(element: BaseItemInitialStateSearch) {
        val image = binding?.autocompleteCuratedCampaignImage ?: return

        image.shouldShowWithAction(element.imageUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(
                itemView.context,
                image,
                element.imageUrl
            )
        }
    }

    private fun bindTitle(element: BaseItemInitialStateSearch) {
        binding?.autocompleteCuratedCampaignTitle?.setTextAndCheckShow(
            MethodChecker.fromHtml(element.title).toString()
        )
    }

    private fun bindSubtitle(element: BaseItemInitialStateSearch) {
        binding?.autocompleteCuratedCampaignSubtitle?.setTextAndCheckShow(
            MethodChecker.fromHtml(element.subtitle).toString()
        )
    }

    private fun bindListener(element: CuratedCampaignDataView) {
        binding?.autocompleteCuratedCampaignCard?.setOnClickListener {
            listener.onCuratedCampaignCardClicked(element)
        }
    }
}