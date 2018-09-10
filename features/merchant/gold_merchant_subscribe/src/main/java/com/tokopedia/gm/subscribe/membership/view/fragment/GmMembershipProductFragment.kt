package com.tokopedia.gm.subscribe.membership.view.fragment

import com.tokopedia.gm.subscribe.R
import com.tokopedia.gm.subscribe.view.fragment.GmProductFragment


class GmMembershipProductFragment : GmProductFragment(){
    override fun getTitle(): String {
        return getString(R.string.gmsubscribe_extend_product_selector)
    }

    override fun getPackage() {
        presenter.getExtendPackageSelection()
    }


    companion object {
        fun newInstance() = GmMembershipProductFragment()
    }
}