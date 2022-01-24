package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.digital_product_detail.R
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
        getInitalData()
        observeData()
    }

    private fun observeData() {
        binding?.let {
            it.widgetBuyWidget.showBuyWidget(
                DenomData(
                price = "Rp35.000",
                    pricePlain = 35000,
                    slashPrice = "Rp50.000",
                    slashPricePlain = 50000
            ), listener = object : RechargeBuyWidgetListener {
                override fun onClickedChevron(product: DenomData) {
                    fragmentManager?.let {
                        SummaryPulsaBottomsheet(getString(R.string.summary_transaction), product).show(it, "")
                    }
                }

                override fun onClickedButtonLanjutkan(product: DenomData) {

                }
            })
        }

        viewModel.observableDenomData.observe(viewLifecycleOwner, { denomData ->
            when (denomData) {
                is RechargeNetworkResult.Success -> {
                    binding?.let {
                        it.widgetDenomGrid.renderDenomGridLayout(this, denomData.data)
                    }
                }

                is RechargeNetworkResult.Fail -> {
                    view?.let {
                        binding?.let {
                            it.widgetDenomGrid.renderFailDenomGrid()
                        }
                        Toaster.build(it, denomData.error.message ?: "Nei", Toaster.LENGTH_LONG)
                            .show()
                    }
                }

                is RechargeNetworkResult.Loading -> {
                    binding?.let {
                        it.widgetDenomGrid.renderDenomGridShimmering()
                    }
                }
            }

        })
    }

    private fun getInitalData() {
        viewModel.setInital()
        viewModel.getRechargeCatalogInput(148, "17")
    }

    /**
     * DenomGrid Listener
     */
    override fun onDenomGridClicked(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int, isShowBuyWidget: Boolean) {

    }

    override fun onDenomGridImpression(denomGrid: DenomData, layoutType: DenomWidgetEnum, position: Int) {

    }


    companion object {
        fun newInstance(): DigitalPDPDataPlanFragment {
            val fragment = DigitalPDPDataPlanFragment()
            return fragment
        }
    }
}