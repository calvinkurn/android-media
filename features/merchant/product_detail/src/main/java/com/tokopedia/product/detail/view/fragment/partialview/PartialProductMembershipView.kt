package com.tokopedia.product.detail.view.fragment.partialview

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.product.detail.R
import com.tokopedia.shop.common.graphql.data.stampprogress.MembershipStampProgress
import com.tokopedia.shop.common.util.CircleIndicatorView
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter

class PartialProductMembershipView private constructor(private var view: View, listener: MembershipStampAdapter.MembershipStampAdapterListener) {

    lateinit var rvMembership: RecyclerView
    lateinit var circleIndicator: CircleIndicatorView
    private val membershipAdapter: MembershipStampAdapter by lazy {
        MembershipStampAdapter(view.context, listener)
    }

    companion object {
        fun build(_view: View, listener: MembershipStampAdapter.MembershipStampAdapterListener) = PartialProductMembershipView(_view, listener)
    }

    init {
        findViews(view)
    }

    fun renderMembershipData(element: MembershipStampProgress) {
        initRecyclerView(view)
        //if user not registered yet hide indicator
        if (!element.membershipStampProgress.isUserRegistered) {
            circleIndicator.visibility = View.GONE
        } else {
            circleIndicator.visibility = View.VISIBLE
            circleIndicator.setIndicator(element.membershipStampProgress.membershipProgram.membershipQuests.size)
        }

        //Hide all membershipview when isShown = false
        if (!element.membershipStampProgress.isShown) {
            view.hide()
            return
        } else view.show()

//        membershipAdapter.setMembershipData(element.membershipStampProgress)
        rvMembership.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                circleIndicator.setCurrentIndicator((rvMembership.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition())
            }
        })
    }

    private fun findViews(view: View) {
        circleIndicator = view.findViewById(R.id.circle_rv_indicator)
        rvMembership = view.findViewById(R.id.rv_membership)
    }

    private fun initRecyclerView(view: View) {
        rvMembership.apply {
            view.context?.let {
                layoutManager = LinearLayoutManager(it, LinearLayout.HORIZONTAL, false)
                adapter = membershipAdapter
            }
        }
        if (rvMembership.onFlingListener == null) {
            PagerSnapHelper().attachToRecyclerView(rvMembership)
        }
    }

}