package com.tokopedia.profile.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profile.R
import com.tokopedia.profile.view.viewmodel.NoPostCardViewModel
import com.tokopedia.profile.view.viewmodel.TitleViewModel

class OtherPostTitleViewHolder(val v: View) : AbstractViewHolder<TitleViewModel>(v) {

    companion object {
        //TODO
        @LayoutRes
        val LAYOUT = R.layout.item_profile_title
    }

    override fun bind(element: TitleViewModel?) {

    }

}