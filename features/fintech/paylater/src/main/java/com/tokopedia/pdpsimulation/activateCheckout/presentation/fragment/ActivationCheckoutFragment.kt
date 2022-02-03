package com.tokopedia.pdpsimulation.activateCheckout.presentation.fragment

import android.content.Intent
import android.os.Bundle
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
import com.tokopedia.pdpsimulation.activateCheckout.listner.ActivationListner
import com.tokopedia.pdpsimulation.activateCheckout.presentation.adapter.ActivationTenureAdapter
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet
import com.tokopedia.pdpsimulation.activateCheckout.presentation.bottomsheet.SelectGateWayBottomSheet.Companion.GATEWAY_LIST
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
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode
import kotlinx.android.synthetic.main.fragment_activation_checkout.*
import kotlinx.android.synthetic.main.paylater_activation_gateway_detail.view.*
import kotlinx.android.synthetic.main.paylater_activation_product_detail.view.*
import kotlinx.android.synthetic.main.product_detail.view.*
import javax.inject.Inject


class ActivationCheckoutFragment : BaseDaggerFragment(), ActivationListner {

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    private val payLaterActivationViewModel: PayLaterActivationViewModel by lazy(
        LazyThreadSafetyMode.NONE
    ) {
        val viewModelProvider = ViewModelProvider(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(PayLaterActivationViewModel::class.java)
    }

    private var productId: String = ""

    private val gatewayId: Int by lazy {
        arguments?.getInt(PARAM_GATEWAY_ID) ?: -1
    }

    private val tenureSelected: Int by lazy {
        arguments?.getInt(PARAM_PRODUCT_TENURE) ?: 0
    }

    private lateinit var activationTenureAdapter: ActivationTenureAdapter
    private lateinit var installmentModel: InstallmentDetails
    private var listOfTenureDetail: List<TenureDetail> = ArrayList()
    private lateinit var listOfGateway: PaylaterGetOptimizedModel
    private var selectedTenurePosition = 0
    private var selectedGateway = 0
    var quantity = 1
    var isDisabled= false


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
        productId = arguments?.getString(PARAM_PRODUCT_ID, "").toString()
        observerProductData()
        observerOtherDetail()
        initView()

        startAllLoaders()
        payLaterActivationViewModel.getProductDetail(productId)

    }

    private fun startAllLoaders() {
        productInfoActivationShimmer.visibility = View.VISIBLE
        detailHeader.visibility = View.GONE
        amountBottomDetailLoader.visibility = View.VISIBLE
        gatewayDetailShimmer.visibility = View.VISIBLE
        gatewayDetailLayout.visibility = View.GONE
    }

    fun updateSelectedTenure(gatewaySelected: Int)
    {
        this.selectedGateway = gatewaySelected
    }

    private fun observerProductData() {
        payLaterActivationViewModel.productDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    productInfoActivationShimmer.visibility = View.GONE
                    detailHeader.visibility = View.VISIBLE
                    setProductData(it.data)
                    payLaterActivationViewModel.getOptimizedCheckoutDetail(
                        productId,
                        payLaterActivationViewModel.price * quantity, gatewayId
                    )

                }
                is Fail -> {

                }
            }


        }
    }


    private fun observerOtherDetail() {
        payLaterActivationViewModel.payLaterActivationDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    amountBottomDetailLoader.visibility = View.GONE
                    if (it.data.checkoutData.isNotEmpty()) {
                        gatewayDetailShimmer.visibility = View.GONE
                        gatewayDetailLayout.visibility = View.VISIBLE
                        amountBottomDetailLoader.visibility = View.GONE
                        checkDisableLogic(it.data.checkoutData[selectedGateway].disable)
                        listOfGateway = it.data
                        setSelectedTenure(it.data)
                        setTenureOptionsData(it.data)
                    } else {

                    }
                }
                is Fail -> {

                }
            }
        }
    }

    private fun checkDisableLogic(disable: Boolean) {
        isDisabled = disable
        this.isDisable()
    }



    private fun setSelectedTenure(data: PaylaterGetOptimizedModel) {
        for (i in 0 until data.checkoutData[selectedGateway].tenureDetail.size) {
            if (tenureSelected == data.checkoutData[selectedGateway].tenureDetail[i].tenure) {
                selectedTenurePosition = i
                break
            }
        }
        data.checkoutData[selectedGateway].tenureDetail[selectedTenurePosition].isSelected = true
        DataMapper.mapToInstallationDetail(data.checkoutData[selectedGateway].tenureDetail[selectedTenurePosition]).installmentDetails?.let {
            installmentModel = it
        }
        DataMapper.mapToInstallationDetail(data.checkoutData[selectedGateway].tenureDetail[selectedTenurePosition]).tenure?.let {
            paymentDuration.text = it
        }
        DataMapper.mapToInstallationDetail(data.checkoutData[selectedGateway].tenureDetail[selectedTenurePosition]).priceText?.let {
            amountToPay.text = it
        }
    }

    private fun setTenureOptionsData(data: PaylaterGetOptimizedModel) {
        if (data.checkoutData.isNotEmpty()) {
            setGatewayProductImage(data.checkoutData[selectedGateway])
            if (!data.checkoutData[selectedGateway].gateway_name.isBlank())
                gatewayDetailLayout.getwayBrandName.text =
                    data.checkoutData[selectedGateway].gateway_name
            else
                gatewayDetailLayout.getwayBrandName.visibility = View.GONE
            if (!data.checkoutData[selectedGateway].subtitle.isBlank())
                gatewayDetailLayout.subheaderGateway.text =
                    data.checkoutData[selectedGateway].subtitle
            else
                gatewayDetailLayout.subheaderGateway.visibility = View.GONE
            if (!data.checkoutData[selectedGateway].subtitle2.isBlank())
                gatewayDetailLayout.subheaderGatewayDetail.text =
                    data.checkoutData[selectedGateway].subtitle2
            else
                gatewayDetailLayout.subheaderGatewayDetail.visibility = View.GONE
            gatewayDetailLayout.additionalDetail.text = data.footer
            setTenureDetail(data.checkoutData[selectedGateway].tenureDetail)
        }

    }

    private fun setTenureDetail(tenureDetail: List<TenureDetail>) {
        this.listOfTenureDetail = tenureDetail
        activationTenureAdapter.updateList(tenureDetail)
    }

    private fun setGatewayProductImage(checkoutData: CheckoutData) {
        if (context.isDarkMode())
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

    private fun showVariantProductHeader(data: GetProductV3) {
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
        addListeners()
        gatewayDetailLayout.recyclerTenureDetail.isNestedScrollingEnabled = false
        gatewayDetailLayout.recyclerTenureDetail.layoutManager = LinearLayoutManager(context)
        gatewayDetailLayout.recyclerTenureDetail.adapter = activationTenureAdapter
    }

    private fun addListeners() {

        gatewayDetailLayout.changePayLaterPartner.setOnClickListener {
            if (this::listOfGateway.isInitialized) {
                val bundle = Bundle().apply {
                    putParcelable(
                        GATEWAY_LIST,
                        listOfGateway
                    )
                }
                bottomSheetNavigator.showBottomSheet(SelectGateWayBottomSheet::class.java, bundle)
            }
        }

        detailHeader.quantityEditor.setValueChangedListener { newValue, _, _ ->
            quantity = newValue
            amountBottomDetailLoader.visibility = View.VISIBLE
            gatewayDetailShimmer.visibility = View.VISIBLE
            gatewayDetailLayout.visibility = View.GONE
            payLaterActivationViewModel.getOptimizedCheckoutDetail(
                productId,
                payLaterActivationViewModel.price * quantity,
                gatewayId
            )
        }

        activationTenureAdapter = ActivationTenureAdapter(listOf(), this)
        detailHeader.showVariantBottomSheet.setOnClickListener {
            context?.let {
                AtcVariantHelper.goToAtcVariant(it, productId, "", false, "") { data, code ->
                    startActivityForResult(data, code)
                }
            }
        }

        priceBreakdown.setOnClickListener {
            if (this::installmentModel.isInitialized) {
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
                if (this.selectedProductId.isNotBlank()) {
                    productId = this.selectedProductId

                   startAllLoaders()
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

    override fun isDisable(): Boolean {
        return isDisabled
    }


    override fun selectedTenure(
        tenureSelectedModel: TenureSelectedModel,
        newPositionToSelect: Int
    ) {
        tenureSelectedModel.installmentDetails?.let {
            installmentModel = it
        }
        tenureSelectedModel.priceText?.let {
            amountToPay.text = it
        }
        tenureSelectedModel.tenure?.let {
            paymentDuration.text = it
        }
        listOfTenureDetail[selectedTenurePosition].isSelected = false
        listOfTenureDetail[newPositionToSelect].isSelected = true
        activationTenureAdapter.updateList(listOfTenureDetail)
        activationTenureAdapter.notifyItemChanged(newPositionToSelect)
        activationTenureAdapter.notifyItemChanged(selectedTenurePosition)
        selectedTenurePosition = newPositionToSelect
    }



}
