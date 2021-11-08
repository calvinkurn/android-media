package com.tokopedia.product.manage.common.view.ongoingpromotion.bottomsheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.cachemanager.SaveInstanceCacheManager
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.common.view.model.OngoingPromotionListWrapper
import com.tokopedia.product.manage.common.view.ongoingpromotion.adapter.OngoingPromotionAdapter
import com.tokopedia.product.manage.common.view.ongoingpromotion.adapter.divider.OngoingPromotionDividerDecoration
import com.tokopedia.product.manage.databinding.BottomSheetProductManageOngoingPromotionBinding
import com.tokopedia.shop.common.data.source.cloud.model.productlist.ProductCampaignType
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.ArrayList

class OngoingPromotionBottomSheet : BottomSheetUnify() {

    companion object {
        private const val TAG = "OngoingPromotionBottomSheet"

        private const val ONGOING_CAMPAIGN_LIST_KEY = "ongoing_campaign_list_key"
        private const val ONGOING_CAMPAIGN_LIST_CACHE_ID = "ongoing_campaign_list_cache_id"

        fun createInstance(
            context: Context,
            campaignTypeList: ArrayList<ProductCampaignType>
        ): OngoingPromotionBottomSheet {
            return OngoingPromotionBottomSheet().apply {
                val cacheManager = SaveInstanceCacheManager(context, true).apply {
                    put(ONGOING_CAMPAIGN_LIST_KEY, OngoingPromotionListWrapper(campaignTypeList))
                }
                arguments = Bundle().apply {
                    putString(ONGOING_CAMPAIGN_LIST_CACHE_ID, cacheManager.id)
                }
            }
        }
    }

    private var campaignTypeList: List<ProductCampaignType>? = null

    private var binding by autoClearedNullable<BottomSheetProductManageOngoingPromotionBinding>()

    private val itemDecoration by lazy {
        context?.let {
            OngoingPromotionDividerDecoration(it)
        }
    }

    fun show(fm: FragmentManager) {
        show(fm, TAG)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTitle(context?.getString(R.string.product_manage_campaign_bottom_sheet_title).orEmpty())
        setChild(binding?.root)

        val cacheManager =
            if (savedInstanceState == null) {
                val cacheId = arguments?.getString(ONGOING_CAMPAIGN_LIST_CACHE_ID)
                context?.let {
                    SaveInstanceCacheManager(it, cacheId)
                }
            } else {
                context?.let {
                    SaveInstanceCacheManager(it, savedInstanceState)
                }
            }
        campaignTypeList = cacheManager?.get<OngoingPromotionListWrapper>(
            ONGOING_CAMPAIGN_LIST_KEY,
            OngoingPromotionListWrapper::class.java
        )?.ongoingPromotionList
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = BottomSheetProductManageOngoingPromotionBinding.inflate(
            inflater,
            container,
            false
        )
        setChild(binding?.root)
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupView()
        super.onViewCreated(view, savedInstanceState)
    }

    private fun setupView() {
        binding?.rvProductManageOngoingPromotion?.run {
            layoutManager = LinearLayoutManager(this@OngoingPromotionBottomSheet.context)
            adapter = OngoingPromotionAdapter(campaignTypeList.orEmpty())
            itemDecoration?.let {
                addItemDecoration(it)
            }
        }
    }

}