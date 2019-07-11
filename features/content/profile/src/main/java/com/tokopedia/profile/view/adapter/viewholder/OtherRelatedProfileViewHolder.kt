package com.tokopedia.profile.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profile.R
import com.tokopedia.profile.view.viewmodel.OtherRelatedProfileViewModel

class OtherRelatedProfileViewHolder(val v: View) : AbstractViewHolder<OtherRelatedProfileViewModel>(v) {

    companion object {
        //TODO
        @LayoutRes
        val LAYOUT = R.layout.item_other_post
    }

    override fun bind(element: OtherRelatedProfileViewModel?) {

    }

}