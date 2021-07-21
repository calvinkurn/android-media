package com.tokopedia.selleronboarding.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.media.loader.loadImage
import com.tokopedia.selleronboarding.R
import com.tokopedia.selleronboarding.model.SobSliderManageUiModel
import kotlinx.android.synthetic.main.sob_slider_manage_view_holder.view.*

/**
 * Created By @ilhamsuaib on 20/07/21
 */

class SliderManageViewHolder(itemView: View) : AbstractViewHolder<SobSliderManageUiModel>(itemView) {

    companion object {
        val RES_LAYOUT = R.layout.sob_slider_manage_view_holder
    }

    override fun bind(element: SobSliderManageUiModel) {
        with(itemView) {
            imgSobManage1.loadImage(R.drawable.onboarding_03_1)
            imgSobManage2.loadImage(R.drawable.onboarding_03_2)
        }
    }
}