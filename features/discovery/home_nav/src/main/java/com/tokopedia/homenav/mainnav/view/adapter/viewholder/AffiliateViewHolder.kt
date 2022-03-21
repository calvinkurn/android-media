package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.view.View
import androidx.annotation.LayoutRes
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileAffiliateDataModel

/**
 * Created by dhaba
 */
class AffiliateViewHolder (
    itemView: View
) : AbstractViewHolder<ProfileAffiliateDataModel>(itemView){

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.home_nav_item_affiliate
    }

    override fun bind(element: ProfileAffiliateDataModel?) {

    }
}