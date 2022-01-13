package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpDataPlanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.model.denom.DenomWidgetModel
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardEnum
import com.tokopedia.recharge_component.model.recommendation_card.RecommendationCardWidgetModel
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * @author by firmanda on 04/01/21
 */

class DigitalPDPDataPlanFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: DigitalPDPDataPlanViewModel

    private var binding by autoClearedNullable<FragmentDigitalPdpDataPlanBinding>()

    override fun getScreenName(): String  = ""

    override fun initInjector() {
        getComponent(DigitalPDPComponent::class.java).inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val viewModelProvider = ViewModelProvider(this, viewModelFactory)
        viewModel = viewModelProvider.get(DigitalPDPDataPlanViewModel::class.java)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentDigitalPdpDataPlanBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showInitalView()
        observeData()
        viewModel.getDelayedResponse()
    }

    private fun observeData() {
        viewModel.dummy.observe(viewLifecycleOwner, {
//            showDenomGrid()
//            showMCCM()
//            showFlashSale()
            showDenomFull()
            showMCCMFull()
            showFlashSaleFull()
        })
    }

    private fun showInitalView(){
        binding?.let {
            it.widgetDenomFull.renderDenomFullShimmering("Halo data")
        }
    }

    private fun showDenomFull(){
        binding?.let {
            it.widgetDenomFull.renderDenomFullLayout(denomFullListener = object : RechargeDenomFullListener{
                override fun onChevronDenomClicked(denomGrid: DenomWidgetModel, position: Int) {

                }

                override fun onDenomFullClicked(denomGrid: DenomWidgetModel, position: Int) {

                }
            }, "Diskon Rp15.000 buat pengguna baru, nih!",
                listOf(
                    DenomWidgetModel(
                        title="15 ribu",
                        description = "The description related to the product, max 1 line",
                        specialLabel = "Any campaign label",
                        price = "Rp500",
                        quotaInfo = "3 GB",
                        expiredDays = "30 Hari",
                        discountLabel = "10%",
                        slashPrice = "Rp16.500",
                        appLink = "tokopedia://deals",
                        expiredDate = "December 2021",
                        flashSaleLabel = "Segera Habis",
                        flashSalePercentage = 80,
                        isShowChevron = true
                    )
                )
            )
        }
    }

    private fun showMCCMFull(){
        binding?.let {
            it.widgetMccmFull.renderMCCMFull(denomFullListener = object : RechargeDenomFullListener{
                override fun onChevronDenomClicked(denomGrid: DenomWidgetModel, position: Int) {

                }

                override fun onDenomFullClicked(denomGrid: DenomWidgetModel, position: Int) {

                }
            }, "Diskon Rp15.000 buat pengguna baru, nih!",
                listOf(
                    DenomWidgetModel(
                        title="15 ribu",
                        description = "The description related to the product, max 1 line",
                        specialLabel = "Any campaign label",
                        price = "Rp500",
                        quotaInfo = "3 GB",
                        expiredDays = "30 Hari",
                        discountLabel = "10%",
                        slashPrice = "Rp16.500",
                        appLink = "tokopedia://deals",
                        expiredDate = "December 2021",
                        flashSaleLabel = "Segera Habis",
                        flashSalePercentage = 80,
                        isShowChevron = true
                    )
                )
            )
        }
    }

    private fun showFlashSaleFull(){
        binding?.let {
            it.widgetFlashSaleFull.renderFlashSaleFull(denomFullListener = object : RechargeDenomFullListener{
                override fun onChevronDenomClicked(denomGrid: DenomWidgetModel, position: Int) {

                }

                override fun onDenomFullClicked(denomGrid: DenomWidgetModel, position: Int) {

                }
            }, "Diskon Rp15.000 buat pengguna baru, nih!",
                "Sampai dengan",
                listOf(
                    DenomWidgetModel(
                        title="15 ribu",
                        description = "The description related to the product, max 1 line",
                        specialLabel = "Any campaign label",
                        price = "Rp500",
                        quotaInfo = "3 GB",
                        expiredDays = "30 Hari",
                        discountLabel = "10%",
                        slashPrice = "Rp16.500",
                        appLink = "tokopedia://deals",
                        expiredDate = "December 2021",
                        flashSaleLabel = "Segera Habis",
                        flashSalePercentage = 80,
                        isShowChevron = true
                    )
                )
            )
        }
    }

    companion object {
        fun newInstance(): DigitalPDPDataPlanFragment {
            val fragment = DigitalPDPDataPlanFragment()
            return fragment
        }
    }
}