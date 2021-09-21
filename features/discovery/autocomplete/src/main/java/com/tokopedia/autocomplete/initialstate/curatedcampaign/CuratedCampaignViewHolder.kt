package com.tokopedia.autocomplete.initialstate.curatedcampaign

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.autocomplete.R
import com.tokopedia.autocomplete.initialstate.InitialStateItemClickListener
import com.tokopedia.kotlin.extensions.view.setTextAndCheckShow
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.layout_autocomplete_curated_campaign_card.view.*

class CuratedCampaignViewHolder(
        itemView: View,
        private val clickListener: InitialStateItemClickListener
): AbstractViewHolder<CuratedCampaignDataView>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.layout_autocomplete_curated_campaign_card
    }

    override fun bind(element: CuratedCampaignDataView) {
        bindImage(element)
        bindTitle(element)
        bindSubtitle(element)
        bindListener(element)
    }

    private fun bindImage(element: CuratedCampaignDataView) {
        itemView.autocompleteCuratedCampaignImage?.shouldShowWithAction(element.imageUrl.isNotEmpty()) {
            ImageHandler.loadImageFitCenter(itemView.context, itemView.autocompleteCuratedCampaignImage, element.imageUrl)
        }
    }

    private fun bindTitle(element: CuratedCampaignDataView) {
        itemView.autocompleteCuratedCampaignTitle?.setTextAndCheckShow(MethodChecker.fromHtml(element.title).toString())
    }

    private fun bindSubtitle(element: CuratedCampaignDataView) {
        itemView.autocompleteCuratedCampaignSubtitle?.setTextAndCheckShow(MethodChecker.fromHtml(element.subtitle).toString())
    }

    private fun bindListener(element: CuratedCampaignDataView) {
        itemView.autocompleteCuratedCampaignCard?.setOnClickListener {
            clickListener.onCuratedCampaignCardClicked(element)
        }
    }
}