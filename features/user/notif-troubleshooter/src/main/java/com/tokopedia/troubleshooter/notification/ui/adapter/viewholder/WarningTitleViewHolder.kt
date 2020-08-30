package com.tokopedia.troubleshooter.notification.ui.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.troubleshooter.notification.R
import com.tokopedia.troubleshooter.notification.ui.uiview.WarningTitleUIVIew
import com.tokopedia.unifyprinciples.Typography

class WarningTitleViewHolder(view: View): AbstractViewHolder<WarningTitleUIVIew>(view) {

    private val txtTitle: Typography = view.findViewById(R.id.txtTitle)

    override fun bind(element: WarningTitleUIVIew?) {
        if (element == null) return
        txtTitle.text = element.title
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_warning_title
    }

}