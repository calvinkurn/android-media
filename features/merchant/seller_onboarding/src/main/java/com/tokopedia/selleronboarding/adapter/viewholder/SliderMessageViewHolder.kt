package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderMessageUiModel
import kotlinx.android.synthetic.main.sob_slider_message_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderMessageViewHolder(itemView: View) : AbstractViewHolder<SobSliderMessageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_message_view_holder
    }

    override fun bind(element: SobSliderMessageUiModel) {
        with(itemView) {
            imgSobMessage1.loadImage(R.drawable.onboarding_02_1)
            imgSobMessage2.loadImage(R.drawable.onboarding_02_2)
            imgSobMessage3.loadImage(R.drawable.onboarding_02_3)
        }
    }
}