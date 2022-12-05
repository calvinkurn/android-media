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

class EventParticipantTitleViewHolder (
    val listener: ParticipantTitleListener,
    itemView: View
):
    AbstractViewHolder<ParticipantTitleUiModel>(itemView) {

    private var binding: ItemEventPdpRedeemTitleBinding? by viewBinding()

    override fun bind(element: ParticipantTitleUiModel) {
        binding?.run {
            tgDayParticipant.text = element.title
            if (element.isDisabled) {
                cbTitleParticipant.isEnabled = false
                cbTitleParticipant.isChecked = true
            } else {
                cbTitleParticipant.isEnabled = true
                cbTitleParticipant.setOnCheckedChangeListener(null)
                cbTitleParticipant.isChecked = element.isChecked
                cbTitleParticipant.setOnCheckedChangeListener { _, isChecked ->
                    listener.onCheckTitleListener(element, isChecked, adapterPosition)
                }
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_event_pdp_redeem_title
    }

    fun interface ParticipantTitleListener {
        fun onCheckTitleListener(element: ParticipantTitleUiModel, isChecked: Boolean, position: Int)
    }
}

