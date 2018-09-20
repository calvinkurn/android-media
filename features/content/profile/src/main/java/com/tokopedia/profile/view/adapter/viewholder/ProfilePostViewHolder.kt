package com.tokopedia.profile.view.adapter.viewholder

import android.view.View
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.profile.R
import com.tokopedia.profile.view.viewmodel.ProfileHeaderViewModel

/**
 * @author by milhamj on 9/20/18.
 */
class ProfilePostViewHolder(val v: View) : AbstractViewHolder<ProfileHeaderViewModel>(v) {

    companion object {
        val LAYOUT = R.layout.item_affiliate_post
    }

    override fun bind(element: ProfileHeaderViewModel) {

    }
}