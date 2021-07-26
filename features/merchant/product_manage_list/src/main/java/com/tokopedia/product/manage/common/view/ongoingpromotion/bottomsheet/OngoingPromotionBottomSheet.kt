package com.tokopedia.product.manage.common.view.ongoingpromotion.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.View
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.view.ongoingpromotion.adapter.OngoingPromotionAdapter
import com.tokopedia.product.manage.common.view.ongoingpromotion.adapter.divider.OngoingPromotionDividerDecoration
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.unifycomponents.BottomSheetUnify
import java.util.ArrayList

class OngoingPromotionBottomSheet: BottomSheetUnify() {

    companion object {
        private const val TAG = "OngoingPromotionBottomSheet"

        private const val CAMPAIGN_LIST_KEY = "campaignList"

        fun createInstance(context: Context,
                           campaignTypeList: ArrayList<ProductCampaignType>): OngoingPromotionBottomSheet {
            return OngoingPromotionBottomSheet().apply {
                val view = View.inflate(context, R.layout.bottom_sheet_product_manage_ongoing_promotion, null)
                setTitle(context.getString(R.string.product_manage_campaign_bottom_sheet_title))
                setChild(view)
                arguments = Bundle().apply {
                    putParcelableArrayList(CAMPAIGN_LIST_KEY, campaignTypeList)
                }
            }
        }
    }

    private var campaignListRecyclerView: RecyclerView? = null

    private val campaignTypeList by lazy {
        arguments?.getParcelableArrayList<ProductCampaignType>(CAMPAIGN_LIST_KEY)
    }

    private val itemDecoration by lazy {
        context?.let {
            OngoingPromotionDividerDecoration(it)
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView(view)
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupView(view: View) {
        campaignListRecyclerView = view.findViewById(R.id.rv_product_manage_ongoing_promotion)
        campaignListRecyclerView?.run {
            layoutManager = LinearLayoutManager(this@OngoingPromotionBottomSheet.context)
            adapter = OngoingPromotionAdapter(campaignTypeList.orEmpty())
            itemDecoration?.let {
                addItemDecoration(it)
            }
        }
    }

}