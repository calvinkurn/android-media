package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.constant.TelcoCategoryType
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.common.topupbills.view.model.TopupBillsExtraParam
import com.tokopedia.digital_product_detail.R
import com.tokopedia.digital_product_detail.data.model.data.DigitalPDPConstant
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpDataPlanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.bottomsheet.SummaryPulsaBottomsheet
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomData
import com.tokopedia.recharge_component.model.denom.DenomWidgetEnum
import com.tokopedia.recharge_component.result.RechargeNetworkResult
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by firmanda on 04/01/21
 */

class DigitalPDPDataPlanFragment :
    BaseDaggerFragment(),
    RechargeDenomGridListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPDataPlanViewModel

    private var clientNumber = ""
    private var productId =  0
    private var menuId = 0
    private var categoryId = TelcoCategoryType.CATEGORY_PAKET_DATA

    private var binding by autoClearedNullable<FragmentDigitalPdpDataPlanBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPDataPlanViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDigitalPdpDataPlanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getDataFromBundle()
        getInitalData()
        observeData()
    }

    private fun getDataFromBundle() {
        arguments?.run {
            val digitalTelcoExtraParam = this.getParcelable(DigitalPDPConstant.EXTRA_PARAM)
                ?: TopupBillsExtraParam()
            clientNumber = digitalTelcoExtraParam.clientNumber
            productId = digitalTelcoExtraParam.productId.toIntOrNull() ?: 0
            if (digitalTelcoExtraParam.categoryId.isNotEmpty()) {
                categoryId = digitalTelcoExtraParam.categoryId.toInt()
            }
            if (digitalTelcoExtraParam.menuId.isNotEmpty()) {
                menuId = digitalTelcoExtraParam.menuId.toIntOrNull() ?: 0
            }
        }
    }

    private fun observeData() {
        viewModel.observableDenomMCCMData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> {
                    it.data
                }
                is RechargeNetworkResult.Fail -> {
                    it.error
                }
                is RechargeNetworkResult.Loading -> {
                }
            }
        })

        viewModel.menuDetailData.observe(viewLifecycleOwner, {
            when (it) {
                is RechargeNetworkResult.Success -> {
                    it.data.recommendations
                }
                is RechargeNetworkResult.Fail -> {
                    it.error
                }
                is RechargeNetworkResult.Loading -> {
                }
            }
        })
    }

    private fun getInitalData() {
        //viewModel.addFilter("filter_tag_kuota", arrayListOf("1157"))
        viewModel.getMenuDetail(menuId, true)
        viewModel.getRechargeCatalogInputMultiTab(menuId, "17", "085")
    }

    /**
     * DenomGrid Listener
     */
    override fun onDenomGridClicked(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int, productListTitle: String, isShowBuyWidget: Boolean) {

    }

    override fun onDenomGridImpression(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int) {

    }


    companion object {
        fun newInstance(telcoExtraParam: TopupBillsExtraParam) = DigitalPDPDataPlanFragment().also {
            val bundle = Bundle()
            bundle.putParcelable(DigitalPDPConstant.EXTRA_PARAM, telcoExtraParam)
            it.arguments = bundle
        }
    }
}