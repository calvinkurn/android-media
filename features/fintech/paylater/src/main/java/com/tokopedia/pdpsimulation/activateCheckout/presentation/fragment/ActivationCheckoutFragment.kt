package com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.CheckoutData
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.PaylaterGetOptimizedModel
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureDetail
import com.tokopedia.pdpsimulation.activateCheckout.domain.model.TenureSelectedModel
import com.tokopedia.pdpsimulation.activateCheckout.helper.DataMapper
import com.tokopedia.pdpsimulation.activateCheckout.listner.TenureSelectListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter.ActivationTenureAdapter
import com.tokopedia.pdpsimulation.activateCheckout.viewmodel.PayLaterActivationViewModel
import com.tokopedia.pdpsimulation.common.constants.PARAM_GATEWAY_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.domain.model.GetProductV3
import com.tokopedia.pdpsimulation.paylater.domain.model.InstallmentDetails
import com.tokopedia.pdpsimulation.paylater.helper.BottomSheetNavigator
import com.tokopedia.pdpsimulation.paylater.presentation.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.product.detail.common.AtcVariantHelper
import com.tokopedia.product.detail.common.view.AtcVariantListener
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

    private var productId: String  =""


    private val gatewayId: Int by lazy {
        arguments?.getInt(PARAM_GATEWAY_ID) ?: -1
    }

    private val tenureSelected: Int by lazy {
        arguments?.getInt(PARAM_PRODUCT_TENURE) ?: 0
    }

    private lateinit var  activationTenureAdapter  : ActivationTenureAdapter
    private lateinit var installmentModel:InstallmentDetails
    private  var listOfTenureDetail:List<TenureDetail> = ArrayList()
    var selectedPosition = 0


    private val bottomSheetNavigator: BottomSheetNavigator by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetNavigator(childFragmentManager)
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
        productId = arguments?.getString(PARAM_PRODUCT_ID,"").toString()
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
                    it.data.price?.let { priceProduct ->
                        payLaterActivationViewModel.getOptimizedCheckoutDetail(productId,
                            priceProduct,gatewayId)
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
                    setSelectedTenure(it.data)
                    setTenureOptionsData(it.data)
                }
                is Fail -> {

                }
            }
        }
    }

    private fun setSelectedTenure(data: PaylaterGetOptimizedModel) {
        for(i in 0 until data.checkoutData[0].tenureDetail.size)
        {
            if(tenureSelected == data.checkoutData[0].tenureDetail[i].tenure)
            {
                selectedPosition = i
                break
            }
        }
        data.checkoutData[0].tenureDetail[selectedPosition].isSelected = true
          DataMapper.mapToInstallationDetail(data.checkoutData[0].tenureDetail[selectedPosition]).installmentDetails?.let {
              installmentModel = it
          }
        DataMapper.mapToInstallationDetail(data.checkoutData[0].tenureDetail[selectedPosition]).tenure?.let {
            paymentDuration.text = it
        }
        DataMapper.mapToInstallationDetail(data.checkoutData[0].tenureDetail[selectedPosition]).priceText?.let {
            amountToPay.text = it
        }
    }

    private fun setTenureOptionsData(data: PaylaterGetOptimizedModel) {
        if(data.checkoutData.isNotEmpty())
        {
            setGatewayProductImage(data.checkoutData[0])
            if(!data.checkoutData[0].gateway_name.isBlank())
                gatewayDetailLayout.getwayBrandName.text = data.checkoutData[0].gateway_name
            else
                gatewayDetailLayout.getwayBrandName.visibility = View.GONE
            if(!data.checkoutData[0].subtitle.isBlank())
                gatewayDetailLayout.subheaderGateway.text = data.checkoutData[0].subtitle
            else
                gatewayDetailLayout.subheaderGateway.visibility = View.GONE
            if(!data.checkoutData[0].subtitle2.isBlank())
                gatewayDetailLayout.subheaderGatewayDetail.text = data.checkoutData[0].subtitle2
            else
                gatewayDetailLayout.subheaderGatewayDetail.visibility = View.GONE
            gatewayDetailLayout.additionalDetail.text = data.footer
            setTenureDetail(data.checkoutData[0].tenureDetail)
        }

    }

    private fun setTenureDetail(tenureDetail: List<TenureDetail>) {
        this.listOfTenureDetail = tenureDetail
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
        addListners()
        gatewayDetailLayout.recyclerTenureDetail.isNestedScrollingEnabled = false
        gatewayDetailLayout.recyclerTenureDetail.layoutManager= LinearLayoutManager(context)
        gatewayDetailLayout.recyclerTenureDetail.adapter = activationTenureAdapter
    }

    private fun addListners() {
        activationTenureAdapter = ActivationTenureAdapter(listOf(),object : TenureSelectListner{
            override fun selectedTenure(tenureSelectedModel: TenureSelectedModel,newPositionToSelect:Int) {
                tenureSelectedModel.installmentDetails?.let{
                    installmentModel = it
                }
                tenureSelectedModel.priceText?.let{
                    amountToPay.text = it
                }
                tenureSelectedModel.tenure?.let {
                    paymentDuration.text = it
                }
                listOfTenureDetail[selectedPosition].isSelected = false
                listOfTenureDetail[newPositionToSelect].isSelected = true
                activationTenureAdapter.updateList(listOfTenureDetail)
                activationTenureAdapter.notifyItemChanged(newPositionToSelect)
                activationTenureAdapter.notifyItemChanged(selectedPosition)
                selectedPosition = newPositionToSelect


            }

        })
        detailHeader.showVariantBottomSheet.setOnClickListener{
            context?.let {
                AtcVariantHelper.goToAtcVariant(it,productId,"",false,"") { data, code ->
                    startActivityForResult(data, code)
                }
            }
        }

        priceBreakdown.setOnClickListener {
            if(this::installmentModel.isInitialized)
            {
                val bundle = Bundle().apply {
                    putParcelable(
                        PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL,
                        installmentModel
                    )
                }
                bottomSheetNavigator.showBottomSheet(PayLaterInstallmentFeeInfo::class.java, bundle)
            }

        }
    }

    override fun getScreenName(): String {
        return "Activation PayLater"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        context?.let {
            AtcVariantHelper.onActivityResultAtcVariant(it, requestCode, data) {
                if(!this.selectedProductId.isBlank() ) {
                    productId = this.selectedProductId
                    payLaterActivationViewModel.getProductDetail(this.selectedProductId)

                }
            }
        }
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
