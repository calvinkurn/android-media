package com.tokopedia.gm.subscribe.membership.view.fragment

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.gm.subscribe.membership.data.model.MembershipData

interface GmMembershipView : CustomerView {

    fun showProgressDialog()

    fun dismissProgressDialog()

    fun onSuccessGetGmSubscribeMembershipData(membershipData : MembershipData)

    fun onErrorGetGmSubscribeMembershipData(error : String)

    fun onSuccessSetGmSubscribeMembershipData()

    fun onErrorSetGmSubscribeMembershipData(error : String)
}
