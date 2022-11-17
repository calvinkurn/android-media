package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.R
import com.tokopedia.entertainment.databinding.ItemEventPdpRedeemTitleBinding
import com.tokopedia.entertainment.pdp.uimodel.ParticipantTitleUiModel
import com.tokopedia.utils.view.binding.viewBinding

/**
 * Author firmanda on 17,Nov,2022
 */

class EventParticipantTitleViewHolder (itemView: View):
    AbstractViewHolder<ParticipantTitleUiModel>(itemView) {

    private var binding: ItemEventPdpRedeemTitleBinding? by viewBinding()

    override fun bind(element: ParticipantTitleUiModel) {
        binding?.run {
            tgDayParticipant.text = element.title
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_event_pdp_redeem_title
    }
}

