package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.uiview.FooterUIView

class FooterViewHolder(view: View): AbstractViewHolder<FooterUIView>(view) {

    override fun bind(element: FooterUIView?) {}

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_footer_message
    }

}