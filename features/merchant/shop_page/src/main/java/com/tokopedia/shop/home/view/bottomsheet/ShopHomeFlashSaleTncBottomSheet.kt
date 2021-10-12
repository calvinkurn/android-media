package com.tokopedia.shop.home.view.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.shop.R
import com.tokopedia.shop.ShopComponentHelper
import com.tokopedia.shop.home.di.module.ShopPageHomeModule
import com.tokopedia.shop.home.view.adapter.ShopHomeFlashSaleTncAdapter
import com.tokopedia.shop.home.view.viewmodel.ShopHomeFlashSaleTncBottomSheetViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class ShopHomeFlashSaleTncBottomSheet : BottomSheetUnify() {

    companion object {
        private const val CAMPAIGN_ID = "campaign_id"
        fun createInstance(campaignId: String) = ShopHomeFlashSaleTncBottomSheet().apply {
            arguments = Bundle().apply {
                putString(CAMPAIGN_ID, campaignId)
            }
        }
    }

    private var campaignId = ""
    private var flashSaleTncView: RecyclerView? = null
    private var flashSaleTncAdapter: ShopHomeFlashSaleTncAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private var viewModel: ShopHomeFlashSaleTncBottomSheetViewModel? = null

    private fun initInjector() {
        activity?.run {
            DaggerShopPageHomeComponent
                .builder()
                .shopPageHomeModule(ShopPageHomeModule())
                .shopComponent(ShopComponentHelper().getComponent(application, this))
                .build()
                .inject(this@ShopHomeFlashSaleTncBottomSheet)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        clearContentPadding = true
        viewModel = ViewModelProviders.of(this, viewModelFactory)
            .get(ShopHomeFlashSaleTncBottomSheetViewModel::class.java)
        arguments?.let {
            val campaignId = it.getString(CAMPAIGN_ID, "")
            viewModel?.getFlashSaleTermsAndConditions(campaignId)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setChild(inflater.inflate(R.layout.fragment_shop_campaign_tnc_bottom_sheet, container, false))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetTitle.text = context?.getString(R.string.shop_page_label_purchase_tnc)
        initView(view)
        observeLiveData()
//        flashSaleTncAdapter?.setTncDescriptions(getFlashSaleTermsAndConditions(context))
    }

    private fun observeLiveData() {
        viewModel?.flashSaleTnc?.observe(viewLifecycleOwner, {
            when(it) {
                is Success -> {
                    val data = it.data
                    bottomSheetTitle.text = data.title
                }
                is Fail -> {

                }
            }
        })
    }

    private fun initView(rootView: View) {
        flashSaleTncView = rootView.findViewById(R.id.rv_flash_sale_tnc)
        flashSaleTncView?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        flashSaleTncAdapter = ShopHomeFlashSaleTncAdapter()
        flashSaleTncView?.adapter = flashSaleTncAdapter
    }
}