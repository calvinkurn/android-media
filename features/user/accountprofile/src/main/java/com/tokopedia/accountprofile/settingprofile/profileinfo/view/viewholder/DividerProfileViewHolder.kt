package com.tokopedia.accountprofile.settingprofile.profileinfo.view.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.accountprofile.R
import com.tokopedia.accountprofile.settingprofile.profileinfo.view.uimodel.DividerProfileUiModel

class DividerProfileViewHolder(val view: View) : AbstractViewHolder<DividerProfileUiModel>(view) {

    override fun bind(element: DividerProfileUiModel?) {}

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.divider_profile_item
    }
}
