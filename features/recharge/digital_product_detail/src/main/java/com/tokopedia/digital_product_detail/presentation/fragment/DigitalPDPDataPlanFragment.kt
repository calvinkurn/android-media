package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.common.topupbills.data.product.CatalogProduct
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpDataPlanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.recharge_component.listener.RechargeBuyWidgetListener
import com.tokopedia.recharge_component.listener.RechargeDenomFullListener
import com.tokopedia.recharge_component.listener.RechargeDenomGridListener
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
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
        observeData()
        initalView()
        viewModel.getDelayedResponse()
    }

    private fun initalView(){
        binding?.let {
        }
    }

    private fun observeData() {
        viewModel.dummy.observe(viewLifecycleOwner, {
            binding?.let {
                it.widgetBuyWidget.showBuyWidget(CatalogProduct(
                    "1"
                    ,attributes = CatalogProduct.Attributes(
                        price = "Rp35.000",
                        promo = CatalogProduct.Promo(
                        )
                    )
                ), listener = object: RechargeBuyWidgetListener{
                    override fun onClickedChevron(product: CatalogProduct) {

                    }

                    override fun onClickedButtonLanjutkan(product: CatalogProduct) {

                    }
                })
            }
        })
    }



    companion object {
        fun newInstance(): DigitalPDPDataPlanFragment {
            val fragment = DigitalPDPDataPlanFragment()
            return fragment
        }
    }
}