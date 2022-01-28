package com.tokopedia.pdpsimulation.activateCheckout.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.presentation.fragment.PdpSimulationFragment
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject


class ActivationCheckoutFragment : BaseDaggerFragment() {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterActivationViewModel: PayLaterActivationViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(PayLaterActivationViewModel::class.java)
    }

    private val productId: String by lazy {
        arguments?.getString(PARAM_PRODUCT_ID) ?: ""
    }
    private val gatewayId: Int by lazy {
        arguments?.getInt(PARAM_GATEWAY_ID) ?: -1
    }


    override fun initInjector() = getComponent(PdpSimulationComponent::class.java).inject(this)


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_activation_checkout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        payLaterActivationViewModel.getProductDetail(productId)
        observerProductData()
        observerOtherDetail()
    }

    private fun observerProductData() {
        payLaterActivationViewModel.productDetailLiveData.observe(viewLifecycleOwner){
            when(it)
            {
                is Success -> {
                    setProductData()
                    it.data.price?.let { productPrice ->
                        payLaterActivationViewModel.getOptimizedCheckoutDetail(productId,
                            productPrice,gatewayId)
                    }
                }
                is Fail -> {

                }
            }


        }
    }


    private fun observerOtherDetail(){
        payLaterActivationViewModel.payLaterActivationDetailLiveData.observe(viewLifecycleOwner){
            when(it)
            {
                is Success -> {
                    setTenureOptionsData()
                }
                is Fail -> {

                }
            }
        }
    }

    private fun setTenureOptionsData() {
        TODO("Not yet implemented")
    }

    private fun setProductData() {

    }

    private fun initView() {

    }

    override fun getScreenName(): String {
        return "Activation PayLater"
    }




    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle): ActivationCheckoutFragment {
            val fragment = ActivationCheckoutFragment()
            fragment.arguments = bundle
            return fragment
        }
    }


}
