package com.tokopedia.shop.common.view.viewholder

import android.view.View
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.constant.GQLQueryNamedConstant.GO_TO_MEMBERSHIP_DETAIL
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipQuests
import com.tokopedia.shop.common.view.BaseMembershipViewHolder
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.common.widget.MembershipStampView

class MembershipItemRegisteredViewHolder(private val view: View, private val dataSize: Int, private val listener: MembershipStampAdapter.MembershipStampAdapterListener, private val url: String) : BaseMembershipViewHolder<MembershipQuests>(view) {

    private var membershipView: MembershipStampView = view.findViewById(R.id.membership_stamp_view)

    override fun bind(element: MembershipQuests) {
        membershipView.setMembershipModel(element, dataSize)
        membershipView.setOnClickListener {
            listener.goToVoucherOrRegister(url, GO_TO_MEMBERSHIP_DETAIL)
            RouteManager.route(view.context, String.format("%s?url=%s", ApplinkConst.WEBVIEW, url))
        }
        membershipView.btnClaim.setOnClickListener {
            listener.onButtonClaimClicked(element.questUserID)
        }
    }

}