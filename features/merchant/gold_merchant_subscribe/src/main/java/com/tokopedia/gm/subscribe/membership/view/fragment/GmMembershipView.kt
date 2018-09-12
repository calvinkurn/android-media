package com.tokopedia.gm.subscribe.membership.view.fragment

import com.tokopedia.abstraction.base.view.listener.CustomerView
import com.tokopedia.gm.subscribe.membership.data.model.GetMembershipData

interface GmMembershipView : CustomerView {

    fun onSuccessGetGmSubscribeMembershipData(membershipData : GetMembershipData)

    fun onErrorGetGmSubscribeMembershipData(throwable: Throwable)

    fun onSuccessSetGmSubscribeMembershipData()

    fun onErrorSetGmSubscribeMembershipData(throwable: Throwable)
}
