package com.tokopedia.digital_product_detail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.digital_product_detail.databinding.FragmentDigitalPdpDataPlanBinding
import com.tokopedia.digital_product_detail.di.DigitalPDPComponent
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPDataPlanViewModel
import com.tokopedia.digital_product_detail.presentation.viewmodel.DigitalPDPPulsaViewModel
import com.tokopedia.recharge_component.listener.RechargeRecommendationCardListener
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

        binding?.let {
            it.widgetRecommendationCard.renderRecommendationLayout(recommendationListener = object : RechargeRecommendationCardListener{
                override fun onProductRecommendationCardClicked(applinkUrl: String) {
                    context?.let {
                        RouteManager.route(it, applinkUrl)
                    }
                }
            },
                "Paling sering kamu beli",
                listOf(
                    RecommendationCardWidgetModel(
                        RecommendationCardEnum.SMALL,
                        "https://ecs7.tokopedia.net/img/attachment/2021/11/18/59205941/59205941_4206fd77-877d-46aa-a4f7-3ddb752da681.png",
                        "Token Listrik 100ribu",
                        "Rp101.500"
                    ),
                    RecommendationCardWidgetModel(
                        RecommendationCardEnum.SMALL,
                        "https://ecs7.tokopedia.net/img/attachment/2021/11/18/59205941/59205941_4206fd77-877d-46aa-a4f7-3ddb752da681.png",
                        "Token Listrik 20 ribu",
                        "Rp20.500"
                    ),
                    RecommendationCardWidgetModel(
                        RecommendationCardEnum.SMALL,
                        "https://ecs7.tokopedia.net/img/attachment/2021/11/18/59205941/59205941_4206fd77-877d-46aa-a4f7-3ddb752da681.png",
                        "Token Listrik 30 ribu",
                        "Rp30.500"
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