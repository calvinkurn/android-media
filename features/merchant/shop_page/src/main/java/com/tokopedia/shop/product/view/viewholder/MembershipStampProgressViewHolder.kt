package com.tokopedia.shop.product.view.viewholder

import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.widget.LinearLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.shop.R
import com.tokopedia.shop.common.util.CircleIndicatorView
import com.tokopedia.shop.common.view.adapter.MembershipStampAdapter
import com.tokopedia.shop.product.view.datamodel.MembershipStampProgressUiModel

class MembershipStampProgressViewHolder(itemView: View, listener: MembershipStampAdapter.MembershipStampAdapterListener?) : AbstractViewHolder<MembershipStampProgressUiModel>(itemView) {

    lateinit var rvMembership: RecyclerView
    lateinit var circleIndicator: CircleIndicatorView
    private val membershipAdapter: MembershipStampAdapter? by lazy {
        listener?.let { MembershipStampAdapter(itemView.context, it) }
    }

    companion object {
        @JvmStatic
        @LayoutRes
        val LAYOUT = R.layout.item_rv_membership
    }

    init {
        findViews(itemView)
    }

    override fun bind(element: MembershipStampProgressUiModel) {
        circleIndicator.setIndicator(element.listOfData.size)

        itemView.show()
        membershipAdapter?.setMembershipData(element.listOfData)
        rvMembership.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                circleIndicator.setCurrentIndicator((rvMembership.layoutManager as LinearLayoutManager).findFirstCompletelyVisibleItemPosition())
            }
        })
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