package com.tokopedia.product.manage.feature.campaignstock.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.product.manage.ProductManageInstance
import com.tokopedia.product.manage.R
import com.tokopedia.product.manage.feature.campaignstock.di.DaggerCampaignStockComponent
import com.tokopedia.product.manage.feature.campaignstock.ui.CampaignStockActivity
import com.tokopedia.product.manage.feature.campaignstock.ui.viewmodel.CampaignStockViewModel
import javax.inject.Inject

class CampaignStockFragment: BaseDaggerFragment() {

    companion object {
        @JvmStatic
        fun createInstance(): CampaignStockFragment = CampaignStockFragment()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val mViewModel: CampaignStockViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(CampaignStockViewModel::class.java)
    }

    private val shopId by lazy {
        activity?.intent?.getStringExtra(CampaignStockActivity.SHOP_ID)
    }

    private val productId by lazy {
        activity?.intent?.getStringArrayExtra(CampaignStockActivity.PRODUCT_ID)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_campaign_stock, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupView()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        activity?.run {
            DaggerCampaignStockComponent.builder()
                    .productManageComponent(ProductManageInstance.getComponent(application))
                    .build()
                    .inject(this@CampaignStockFragment)
        }
    }

    private fun setupView() {
        shopId?.let { shopId ->
            productId?.let { productId ->
                mViewModel.getStockAllocation(shopId, productId)
            }
        }
    }

    private fun applyLayout() {

    }
}