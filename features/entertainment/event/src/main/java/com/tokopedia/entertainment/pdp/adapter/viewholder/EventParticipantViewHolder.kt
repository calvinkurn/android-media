package com.tokopedia.entertainment.pdp.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.entertainment.pdp.uimodel.ParticipantUiModel
import com.tokopedia.entertainment.R
import com.tokopedia.utils.view.binding.viewBinding
import com.tokopedia.entertainment.databinding.ItemEventPdpRedeemBinding
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show

/**
 * Author firmanda on 17,Nov,2022
 */

class EventParticipantViewHolder(
    private val listener: ParticipantListener,
    itemView: View
): AbstractViewHolder<ParticipantUiModel>(itemView) {

    private var binding: ItemEventPdpRedeemBinding? by viewBinding()

    override fun bind(element: ParticipantUiModel) {
        binding?.run {
            if (element.title.isNotEmpty()){
                tgParticipantTitle.show()
                tgParticipantTitle.text = element.title
            } else {
                tgParticipantTitle.hide()
            }

            if (element.isDisabled) {
                cbParticipant.isEnabled = false
                cbParticipant.isChecked = true

                if (element.redeemTime.isNotEmpty()) {
                    tgParticipantRedeemTime.show()
                    tgParticipantRedeemTime.text = element.redeemTime
                } else {
                    tgParticipantRedeemTime.hide()
                }
            } else {
                tgParticipantRedeemTime.hide()
                cbParticipant.isEnabled = true
                cbParticipant.setOnCheckedChangeListener(null)
                cbParticipant.isChecked = element.isChecked
                cbParticipant.setOnCheckedChangeListener { _, isChecked ->
                    listener.onCheckListener(element, isChecked, adapterPosition)
                }
            }

            if (element.subTitle.isNotEmpty()) {
                tgParticipantSubTitle.show()
                tgParticipantSubTitle.text = element.subTitle
            } else {
                tgParticipantSubTitle.hide()
            }
        }
    }

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.item_event_pdp_redeem
    }

    fun interface ParticipantListener {
        fun onCheckListener(element: ParticipantUiModel, isChecked: Boolean, position: Int)
    }
}
