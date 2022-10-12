package com.tokopedia.tkpd.flashsale.presentation.detail.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.seller_tokopedia_flash_sale.databinding.StfsFragmentCampaignProductCriteriaBinding
import com.tokopedia.tkpd.flashsale.di.component.DaggerTokopediaFlashSaleComponent
import com.tokopedia.tkpd.flashsale.presentation.detail.adapter.campaigndetail.ProductCriteriaAdapter
import com.tokopedia.tkpd.flashsale.presentation.detail.uimodel.ProductCriteriaModel
import com.tokopedia.utils.lifecycle.autoClearedNullable

class CampaignProductCriteriaFragment: BaseDaggerFragment() {

    companion object {
        private const val BUNDLE_KEY_CRITERIA_MODEL = "ProductCriteriaModel"
        @JvmStatic
        fun newInstance(productCriterias: List<ProductCriteriaModel>): CampaignProductCriteriaFragment {
            return CampaignProductCriteriaFragment().apply { //TODO: use SaveInstanceCacheManager
                arguments = Bundle().apply {
                    putParcelableArrayList(BUNDLE_KEY_CRITERIA_MODEL, ArrayList(productCriterias))
                }
            }
        }
    }

    private var binding by autoClearedNullable<StfsFragmentCampaignProductCriteriaBinding>()
    private val productCriterias by lazy {
        arguments?.getParcelableArrayList<ProductCriteriaModel>(BUNDLE_KEY_CRITERIA_MODEL)?.toList().orEmpty()
    }

    override fun getScreenName() = ""

    override fun initInjector() {
        DaggerTokopediaFlashSaleComponent.builder()
            .baseAppComponent((activity?.applicationContext as? BaseMainApplication)?.baseAppComponent)
            .build()
            .inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = StfsFragmentCampaignProductCriteriaBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupCriteriaList()
    }

    private fun setupCriteriaList() {
        binding?.rvProductCriteria?.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = ProductCriteriaAdapter().apply {
                setDataList(productCriterias)
            }
        }
    }
}
