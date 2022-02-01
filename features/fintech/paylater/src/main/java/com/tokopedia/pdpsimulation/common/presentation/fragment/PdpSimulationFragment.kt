package com.tokopedia.pdpsimulation.common.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.pdpsimulation.R
import com.tokopedia.pdpsimulation.common.analytics.PdpSimulationAnalytics
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_ID
import com.tokopedia.pdpsimulation.common.constants.PARAM_PRODUCT_TENURE
import com.tokopedia.pdpsimulation.common.di.component.PdpSimulationComponent
import com.tokopedia.pdpsimulation.common.helper.BottomSheetNavigator
import com.tokopedia.pdpsimulation.common.presentation.adapter.PayLaterAdapterFactoryImpl
import com.tokopedia.pdpsimulation.common.presentation.adapter.PayLaterSimulationAdapter
import com.tokopedia.pdpsimulation.common.utils.Utils
import com.tokopedia.pdpsimulation.paylater.domain.model.*
import com.tokopedia.pdpsimulation.paylater.presentation.detail.adapter.PayLaterSimulationTenureAdapter
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterActionStepsBottomSheet
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterInstallmentFeeInfo
import com.tokopedia.pdpsimulation.paylater.presentation.detail.bottomsheet.PayLaterTokopediaGopayBottomsheet
import com.tokopedia.pdpsimulation.paylater.viewModel.PayLaterViewModel
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.currency.CurrencyFormatUtil
import kotlinx.android.synthetic.main.fragment_pdp_simulation.*
import kotlinx.android.synthetic.main.product_detail.view.*
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import java.util.*
import javax.inject.Inject


class PdpSimulationFragment : BaseDaggerFragment() {

    private var isProductDetailShown: Boolean = false

    @Inject
    lateinit var viewModelFactory: dagger.Lazy<ViewModelProvider.Factory>

    @Inject
    lateinit var pdpSimulationAnalytics: dagger.Lazy<PdpSimulationAnalytics>

    private val payLaterViewModel: PayLaterViewModel by lazy(LazyThreadSafetyMode.NONE) {
        val viewModelProvider = ViewModelProviders.of(requireActivity(), viewModelFactory.get())
        viewModelProvider.get(PayLaterViewModel::class.java)
    }

    private val bottomSheetNavigator: BottomSheetNavigator by lazy(LazyThreadSafetyMode.NONE) {
        BottomSheetNavigator(childFragmentManager)
    }

    private val productId: String by lazy {
        arguments?.getString(PARAM_PRODUCT_ID) ?: ""
    }

    private val defaultTenure: Int by lazy {
        (arguments?.getString(PARAM_PRODUCT_TENURE) ?: "0").toInt()
    }

    private val simulationAdapter: PayLaterSimulationAdapter by lazy {
        PayLaterSimulationAdapter(getAdapterTypeFactory())
    }
    private val tenureAdapter: PayLaterSimulationTenureAdapter by lazy {
        PayLaterSimulationTenureAdapter { payLaterList ->
            simulationAdapter.addAllElements(payLaterList)
            rvPayLaterOption.scrollToPosition(0)
        }
    }

    private fun getAdapterTypeFactory() = PayLaterAdapterFactoryImpl(
        interaction = PayLaterOptionInteraction(
            onCtaClicked = { handleAction(it) },
            installementDetails = { openInstallmentBottomSheet(it) },
            seeMoreOptions = { adapterPosition ->
                if (adapterPosition != RecyclerView.NO_POSITION)
                    simulationAdapter.updateOptionList(adapterPosition)
            }
        )
    )

    private fun handleAction(detail: Detail) {
        Toast.makeText(context, "Cta clicked", Toast.LENGTH_LONG).show()

        Utils.handleClickNavigation(context, detail,
            openHowToUse = {
                bottomSheetNavigator.showBottomSheet(PayLaterActionStepsBottomSheet::class.java, it)
            },
            openGoPay = {
                bottomSheetNavigator.showBottomSheet(PayLaterTokopediaGopayBottomsheet::class.java, it)
            }
        )
    }

    private fun openInstallmentBottomSheet(installment: InstallmentDetails) {
        Toast.makeText(context, "Open Installemt", Toast.LENGTH_LONG).show()
        val bundle = Bundle().apply { putParcelable(PayLaterInstallmentFeeInfo.INSTALLMENT_DETAIL, installment) }
        bottomSheetNavigator.showBottomSheet(PayLaterInstallmentFeeInfo::class.java, bundle)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_pdp_simulation, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        observeViewModel()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initArguments()
        initViews()
        payLaterViewModel.getProductDetail(productId)
    }

    private fun initArguments() {
        payLaterViewModel.defaultTenure = defaultTenure
    }

    private fun initViews() {
        rvPayLaterSimulation.adapter = tenureAdapter
        rvPayLaterSimulation.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        rvPayLaterOption.adapter = simulationAdapter
        rvPayLaterOption.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        simulationAdapter.showLoadingInAdapter()
    }

    private fun observeViewModel() {
        payLaterViewModel.productDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> productDetailSuccess(it.data)
                is Fail -> productDetailFail(it.throwable)
            }
        }

        payLaterViewModel.payLaterOptionsDetailLiveData.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> setSimulationView(it.data)
                is Fail -> simulationFailed()
            }
        }
    }


    private fun setSimulationView(data: ArrayList<SimulationUiModel>) {
        // hide loading
        showSimulationViews()
        setSimulationAdapter(data)
    }

    private fun setSimulationAdapter(data: ArrayList<SimulationUiModel>) {
        val defaultSelectedSimulation = payLaterViewModel.defaultSelectedSimulation
        tenureAdapter.setData(data)
        tenureAdapter.lastSelectedPosition = defaultSelectedSimulation
        if (defaultSelectedSimulation >= 0) {
            rvPayLaterSimulation.scrollToPosition(defaultSelectedSimulation)
            simulationAdapter.addAllElements(
                data[defaultSelectedSimulation].simulationList ?: arrayListOf()
            )
        }
    }

    private fun simulationFailed() {
        // show product detail only after simulation has been done
        if (isProductDetailShown)
            productDetail.visible()
    }

    private fun productDetailFail(throwable: Throwable) {
        hideSimulationViews()
        when (throwable) {
            is UnknownHostException, is SocketTimeoutException -> setGlobalErrors(GlobalError.NO_CONNECTION)
            is IllegalStateException -> setGlobalErrors(GlobalError.PAGE_FULL)
            else -> setGlobalErrors(GlobalError.SERVER_ERROR)
        }
    }

    private fun showSimulationViews() {
        productInfoShimmer.gone()
        // show product detail only after simulation has been done
        if (isProductDetailShown)
            productDetail.visible()
        rvPayLaterSimulation.visible()
        rvPayLaterOption.visible()
        payLaterBorder.visible()
    }

    private fun hideSimulationViews() {
        productInfoShimmer.gone()
        rvPayLaterSimulation.gone()
        rvPayLaterOption.gone()
        payLaterBorder.gone()
    }

    private fun setGlobalErrors(errorType: Int) {
        payLaterSimulationGlobalError.setType(errorType)
        payLaterSimulationGlobalError.visible()
        payLaterSimulationGlobalError.setActionClickListener {
            payLaterSimulationGlobalError.gone()
            payLaterViewModel.getProductDetail(productId)
        }
    }

    /**
     * THis method called on product detail api success
     */
    private fun productDetailSuccess(data: GetProductV3) {
        payLaterViewModel.getPayLaterAvailableDetail(data.price ?: 0.0, productId)
        if (data.pictures?.size == 0 || data.productName.isNullOrEmpty() || data.price?.equals(0.0) == true)
            productDetail.gone()
        else
            setProductDetailView(data)
    }

    /**
     * This method called to set view for the product image,price and variant
     */
    private fun setProductDetailView(data: GetProductV3) {
        isProductDetailShown = true
        data.pictures?.get(0)?.let { pictures ->
            pictures.urlThumbnail?.let { urlThumbnail ->
                productDetail.productImage.setImageUrl(
                    urlThumbnail
                )
            }
        }
        data.productName.let {
            productDetail.productName.text = it
        }

        productDetail.productPrice.text =
            CurrencyFormatUtil.convertPriceValueToIdrFormat(data.price ?: 0.0, false)

        showProductVariant(data)
    }

    /**
     * THis method set data for the product variant
     * @param data  is the product detail data
     */
    private fun showProductVariant(data: GetProductV3) {
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
                    productDetail.productVariant.text =
                        variant.selections[0].options[combination]?.value ?: ""
            } else {
                productDetail.productVariant.gone()
            }
        }

    }

    override fun getScreenName() = "PayLater & Cicilan"
    override fun initInjector() = getComponent(PdpSimulationComponent::class.java).inject(this)

    companion object {

        @JvmStatic
        fun newInstance(bundle: Bundle): PdpSimulationFragment {
            val fragment = PdpSimulationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}