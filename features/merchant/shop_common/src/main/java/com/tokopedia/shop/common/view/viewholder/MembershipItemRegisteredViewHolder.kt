package com.tokopedia.shop.common.view.viewholder

import android.view.View
import com.tokopedia.shop.common.R
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipQuests
import com.tokopedia.shop.common.view.BaseMembershipViewHolder
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.common.widget.MembershipStampView

class MembershipItemRegisteredViewHolder(view: View, private val dataSize: Int, private val listener: MembershipStampAdapter.MembershipStampAdapterListener) : BaseMembershipViewHolder<MembershipQuests>(view) {

    private var membershipView: MembershipStampView = view.findViewById(R.id.membership_stamp_view)

    override fun bind(element: MembershipQuests) {
        membershipView.setMembershipModel(element, dataSize)
        membershipView.btnClaim.setOnClickListener {
            listener.onButtonClaimClicked()
        }
    }

}