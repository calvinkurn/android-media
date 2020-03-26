package com.tokopedia.settingnotif.usersetting.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.settingnotif.R
import com.tokopedia.settingnotif.usersetting.domain.pojo.ChangeSection
import com.tokopedia.unifyprinciples.Typography

class ChangeItemViewHolder(
        itemView: View?
): AbstractViewHolder<ChangeSection>(itemView) {

    private val txtTitle = itemView?.findViewById<Typography>(R.id.txtTitle)
    private val txtChangeType = itemView?.findViewById<Typography>(R.id.txtChangeType)

    override fun bind(element: ChangeSection?) {
        val context = itemView.context
        element?.let {
            txtTitle?.text = context.getString(it.description)
            txtChangeType?.text = it.changeItem
        }
    }

    companion object {
        @LayoutRes val LAYOUT = R.layout.item_change_email
    }

}