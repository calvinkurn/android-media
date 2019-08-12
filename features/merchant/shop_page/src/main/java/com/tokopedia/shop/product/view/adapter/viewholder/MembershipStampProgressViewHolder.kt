package com.tokopedia.shop.product.view.adapter.viewholder

import android.support.annotation.LayoutRes
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.CircleIndicatorView
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.product.view.model.MembershipStampProgressViewModel

class MembershipStampProgressViewHolder(itemView: View, listener: MembershipStampAdapter.MembershipStampAdapterListener) : AbstractViewHolder<MembershipStampProgressViewModel>(itemView) {

    lateinit var rvMembership: RecyclerView
    lateinit var circleIndicator: CircleIndicatorView
    private val membershipAdapter: MembershipStampAdapter by lazy {
        MembershipStampAdapter(itemView.context, listener)
    }

    companion object {
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.item_rv_membership
    }

    init {
        findViews(itemView)
    }

    override fun bind(element: MembershipStampProgressViewModel) {
        //if user not registered yet hide indicator
        if(!element.membershipData.isUserRegistered) {
            circleIndicator.visibility = View.GONE
        } else{
            circleIndicator.visibility = View.VISIBLE
            circleIndicator.setIndicator(element.membershipData.membershipProgram.membershipQuests.size)
            membershipAdapter.setMembershipData(element.membershipData)
            rvMembership.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    circleIndicator.setCurrentIndicator((rvMembership.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition())
                }
            })
        }
    }

    private fun findViews(itemView: View) {
        circleIndicator = itemView.findViewById(R.id.circle_rv_indicator)
        rvMembership = itemView.findViewById(R.id.rv_membership)
        rvMembership.apply {
            itemView.context?.let {
                layoutManager = LinearLayoutManager(it, LinearLayout.HORIZONTAL, false)
                adapter = membershipAdapter
            }
        }
        PagerSnapHelper().attachToRecyclerView(rvMembership)
    }

}