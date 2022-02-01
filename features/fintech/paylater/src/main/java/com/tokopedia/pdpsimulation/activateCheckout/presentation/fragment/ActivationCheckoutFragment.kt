package com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter.ActivationTenureAdapter
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.paylater.domain.model.GetProductV3
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_activation_checkout.*
import kotlinx.android.synthetic.main.paylater_activation_gateway_detail.view.*
import kotlinx.android.synthetic.main.paylater_activation_product_detail.view.*
import kotlinx.android.synthetic.main.product_detail.view.*
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

    private val activationTenureAdapter = ActivationTenureAdapter(listOf())


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
                    setProductData(it.data)
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
                    setTenureOptionsData(it.data)
                }
                is Fail -> {

                }
            }
        }
    }

    private fun setTenureOptionsData(data: PaylaterGetOptimizedModel) {
        if(data.checkoutData.isNotEmpty())
        {
            setGatewayProductImage(data.checkoutData[0])
            gatewayDetailLayout.getwayBrandName.text = data.checkoutData[0].gateway_name
            gatewayDetailLayout.subheaderGateway.text = data.checkoutData[0].subtitle
            gatewayDetailLayout.subheaderGatewayDetail.text = data.checkoutData[0].subtitle2
            gatewayDetailLayout.additionalDetail.text = data.footer
            setTenureDetail(data.checkoutData[0].tenureDetail)
        }

    }

    private fun setTenureDetail(tenureDetail: List<TenureDetail>) {
        activationTenureAdapter.updateList(tenureDetail)
    }

    private fun setGatewayProductImage(checkoutData: CheckoutData) {
        if(context.isDarkMode())
            gatewayDetailLayout.gatewayImg.setImageUrl(checkoutData.dark_img_url)
        else
            gatewayDetailLayout.gatewayImg.setImageUrl(checkoutData.light_img_url)

    }

    private fun setProductData(productData: GetProductV3) {
        productData.pictures?.get(0)?.let { pictures ->
            pictures.urlThumbnail?.let { urlThumbnail ->
                detailHeader.productDetailWidget.productImage.setImageUrl(
                    urlThumbnail
                )
            }
        }
        productData.productName.let {
            detailHeader.productDetailWidget.productName.text = it
        }

        detailHeader.productDetailWidget.productPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(productData.price ?: 0.0, false)

        showVariantProductHeader(productData)
    }

    private fun showVariantProductHeader(data: GetProductV3)
    {
        data.variant?.let { variant ->
            if (variant.products.isNotEmpty() && variant.selections.isNotEmpty()) {
                var combination = -1
                for (i in variant.products.indices) {
                    if (productId == variant.products[i].productID) {
                        combination = variant.products[i].combination[0] ?: -1
                        break
                    }
                }
                if (combination != -1)
                    detailHeader.productDetailWidget.productVariant.text =
                        variant.selections[0].options[combination]?.value ?: ""
            } else {
                detailHeader.productDetailWidget.productVariant.gone()
            }
        }
    }



    private fun initView() {
        gatewayDetailLayout.recyclerTenureDetail.layoutManager= LinearLayoutManager(context)
        gatewayDetailLayout.recyclerTenureDetail.adapter = activationTenureAdapter
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
