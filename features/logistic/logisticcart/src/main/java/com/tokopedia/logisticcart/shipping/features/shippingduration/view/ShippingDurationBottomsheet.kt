package com.tokopedia.logisticcart.shipping.features.shippingduration.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.res.Resources
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.ratescourierrecommendation.ServiceData
import com.tokopedia.logisticcart.R
import com.tokopedia.logisticcart.databinding.FragmentShipmentDurationChoiceBinding
import com.tokopedia.logisticcart.shipping.features.shippingduration.di.DaggerShippingDurationComponent
import com.tokopedia.logisticcart.shipping.model.ChooseShippingDurationState
import com.tokopedia.logisticcart.shipping.model.LogisticPromoUiModel
import com.tokopedia.logisticcart.shipping.model.Product
import com.tokopedia.logisticcart.shipping.model.RatesParam
import com.tokopedia.logisticcart.shipping.model.RatesViewModelType
import com.tokopedia.logisticcart.shipping.model.ShipmentDetailData
import com.tokopedia.logisticcart.shipping.model.ShippingCourierUiModel
import com.tokopedia.logisticcart.shipping.model.ShippingDurationAnalyticState
import com.tokopedia.logisticcart.shipping.model.ShippingDurationListState
import com.tokopedia.logisticcart.shipping.model.ShippingDurationUiModel
import com.tokopedia.logisticcart.shipping.model.ShopShipment
import com.tokopedia.logisticcart.utils.RatesParamHelper.generateRatesParam
import com.tokopedia.network.utils.ErrorHandler.Companion.getErrorMessage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoCleared
import javax.inject.Inject

/**
 * Created by Irfan Khoirul on 06/08/18.
 */
class ShippingDurationBottomsheet : ShippingDurationAdapterListener,
    BottomSheetUnify() {

    private var binding by autoCleared<FragmentShipmentDurationChoiceBinding>()

    private var shippingDurationBottomsheetListener: ShippingDurationBottomsheetListener? = null

    private var chooseCourierTracePerformance: PerformanceMonitoring? = null
    private var isChooseCourierTraceStopped = false

    private var isDisableOrderPrioritas = false
    private var isOcc = false
    private var mCartPosition = -1
    private var ratesParam: RatesParam? = null
    private var selectedSpId: Int = 0
    private var selectedServiceId: Int = 0
    private var isRatesTradeInApi: Boolean = false
    private var mRecipientAddress: RecipientAddressModel? = null

    @JvmField
    @Inject
    var shippingDurationAdapter: ShippingDurationAdapter? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ShippingDurationViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ShippingDurationViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initializeInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        initBottomSheet()
        initObserver()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initObserver() {
        viewModel.loading.observe(viewLifecycleOwner) {
            if (it) showLoading() else hideLoading()
        }

        viewModel.shipmentData.observe(viewLifecycleOwner) {
            stopTrace()
            when (it) {
                is ShippingDurationListState.ShowList -> {
                    showData(it.list)
                }

                is ShippingDurationListState.NoShipmentAvailable -> {
                    showNoCourierAvailable(it.message)
                }

                is ShippingDurationListState.RatesError -> {
                    showErrorPage(it.e)
                }
            }
        }

        viewModel.chosenDuration.observe(viewLifecycleOwner) {
            when (it) {
                is ChooseShippingDurationState.NormalShipping -> {
                    onShippingDurationAndRecommendCourierChosen(
                        it.shippingCourierUiModelList,
                        it.courierData,
                        it.cartPosition,
                        it.selectedServiceId,
                        it.serviceData,
                        it.flagNeedToSetPinpoint
                    )
                }

                is ChooseShippingDurationState.FreeShipping -> {
                    onLogisticPromoChosen(
                        it.shippingCourierViewModelList,
                        it.courierData,
                        it.serviceData,
                        it.needToSetPinpoint,
                        it.data.promoCode,
                        it.data.serviceId,
                        it.data
                    )
                }

                is ChooseShippingDurationState.CourierNotAvailable -> {
                    showPromoCourierNotAvailable()
                }
            }
        }

        viewModel.shipmentAnalytic.observe(viewLifecycleOwner) {
            when (it) {
                is ShippingDurationAnalyticState.AnalyticCourierPromo -> {
                    sendAnalyticCourierPromo(it.shippingDurationUiModelList)
                }

                is ShippingDurationAnalyticState.AnalyticPromoLogistic -> {
                    sendAnalyticPromoLogistic(it.promoViewModelList)
                }
            }
        }
    }

    private fun getRates() {
        ratesParam?.let {
            viewModel.loadDuration(
                selectedSpId = selectedSpId,
                selectedServiceId = selectedServiceId,
                ratesParam = it,
                isRatesTradeInApi = isRatesTradeInApi,
                isOcc = isOcc
            )
        }
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        context?.run {
            setTitle(getString(R.string.title_bottomsheet_shipment_duration))
        }

        clearContentPadding = true
        customPeekHeight = Resources.getSystem().displayMetrics.heightPixels / 2
        isDragable = false
        isHideable = true

        setShowListener {
            chooseCourierTracePerformance = PerformanceMonitoring.start(CHOOSE_COURIER_TRACE)
            getRates()
        }
        setOnDismissListener {
        }
        setCloseClickListener {
            shippingDurationBottomsheetListener?.onShippingDurationButtonCloseClicked()
            dismiss()
        }
        initView()
    }

    private fun initializeInjector() {
        val baseMainApplication = activity?.application as BaseMainApplication
        val component = DaggerShippingDurationComponent.builder()
            .baseAppComponent(baseMainApplication.baseAppComponent)
            .build()
        component.inject(this)
    }

    @SuppressLint("InflateParams")
    private fun initView() {
        binding =
            FragmentShipmentDurationChoiceBinding.inflate(LayoutInflater.from(context), null, false)
        setupRecyclerView(mCartPosition)
        setChild(binding.root)
    }

    private fun setupRecyclerView(cartPosition: Int) {
        shippingDurationAdapter?.setShippingDurationAdapterListener(this)
        shippingDurationAdapter?.setCartPosition(cartPosition)
        val linearLayoutManager = LinearLayoutManager(
            activity,
            LinearLayoutManager.VERTICAL,
            false
        )
        binding.rvDuration.layoutManager = linearLayoutManager
        binding.rvDuration.adapter = shippingDurationAdapter
    }

    /*
    Section: Shipping Duration View
     */

    private fun showLoading() {
        binding.llContent.visibility = View.GONE
        binding.llNetworkErrorView.visibility = View.GONE
        binding.pbLoading.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.pbLoading.visibility = View.GONE
        binding.llNetworkErrorView.visibility = View.GONE
        binding.llContent.visibility = View.VISIBLE
    }

    private fun showErrorPage(message: String) {
        binding.pbLoading.visibility = View.GONE
        binding.llContent.visibility = View.GONE
        binding.llNetworkErrorView.visibility = View.VISIBLE
        NetworkErrorHelper.showEmptyState(
            activity,
            binding.llNetworkErrorView,
            message
        ) { getRates() }
    }

    private fun showErrorPage(error: Throwable) {
        showErrorPage(getErrorMessage(activity, error))
    }

    private fun showData(uiModelList: MutableList<RatesViewModelType>) {
        shippingDurationAdapter?.setShippingDurationViewModels(uiModelList, isDisableOrderPrioritas)
    }

    private fun sendAnalyticCourierPromo(shippingDurationUiModelList: List<ShippingDurationUiModel>) {
        for (shippingDurationUiModel in shippingDurationUiModelList) {
            shippingDurationBottomsheetListener?.onShowDurationListWithCourierPromo(
                shippingDurationUiModel.serviceData.isPromo == 1,
                shippingDurationUiModel.serviceData.serviceName
            )
        }
    }

    private fun sendAnalyticPromoLogistic(promoViewModelList: List<LogisticPromoUiModel>) {
        shippingDurationBottomsheetListener?.onShowLogisticPromo(promoViewModelList)
    }

    private fun showNoCourierAvailable(message: String?) {
        shippingDurationBottomsheetListener?.onNoCourierAvailable(
            message ?: context?.getString(R.string.label_no_courier_bottomsheet_message)
        )
        dismiss()
    }

    private fun stopTrace() {
        if (!isChooseCourierTraceStopped) {
            chooseCourierTracePerformance?.stopTrace()
            isChooseCourierTraceStopped = true
        }
    }

    private fun onShippingDurationAndRecommendCourierChosen(
        shippingCourierUiModelList: List<ShippingCourierUiModel>,
        courierData: ShippingCourierUiModel?,
        cartPosition: Int,
        selectedServiceId: Int,
        serviceData: ServiceData,
        flagNeedToSetPinpoint: Boolean
    ) {
        shippingDurationBottomsheetListener?.let {
            try {
                it.onShippingDurationChoosen(
                    shippingCourierUiModelList,
                    courierData,
                    mRecipientAddress, cartPosition, selectedServiceId, serviceData,
                    flagNeedToSetPinpoint, isDurationClick = true, isClearPromo = true
                )
                dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun onLogisticPromoChosen(
        shippingCourierViewModelList: List<ShippingCourierUiModel>,
        courierData: ShippingCourierUiModel,
        serviceData: ServiceData,
        needToSetPinpoint: Boolean,
        promoCode: String,
        serviceId: Int,
        data: LogisticPromoUiModel
    ) {
        try {
            shippingDurationBottomsheetListener?.onLogisticPromoChosen(
                shippingCourierViewModelList, courierData,
                mRecipientAddress, mCartPosition,
                serviceData, false, promoCode, serviceId, data
            )
        } catch (e: Exception) {
            e.printStackTrace()
        }
        dismiss()
    }

    private fun showPromoCourierNotAvailable() {
        activity?.let {
            showErrorPage(it.getString(R.string.logistic_promo_serviceid_mismatch_message))
        }
    }

    /*
    Section: Adapter Listener
     */

    override fun onShippingDurationChoosen(
        shippingCourierUiModelList: List<ShippingCourierUiModel>,
        cartPosition: Int,
        serviceData: ServiceData
    ) {
        viewModel.onChooseDuration(shippingCourierUiModelList, cartPosition, serviceData, isOcc)
    }

    override fun onLogisticPromoClicked(data: LogisticPromoUiModel) {
        viewModel.onLogisticPromoClicked(data)
    }

    companion object {
        private const val CHOOSE_COURIER_TRACE = "mp_choose_courier"
        private const val TAG = "Shipping Duration Bottom Sheet"

        fun show(
            fragmentManager: FragmentManager,
            shippingDurationBottomsheetListener: ShippingDurationBottomsheetListener?,
            ratesParam: RatesParam,
            selectedSpId: Int,
            selectedServiceId: Int,
            isRatesTradeInApi: Boolean,
            isDisableOrderPrioritas: Boolean,
            recipientAddressModel: RecipientAddressModel?,
            cartPosition: Int,
            isOcc: Boolean
        ) {
            val bottomSheet = ShippingDurationBottomsheet()
            bottomSheet.shippingDurationBottomsheetListener = shippingDurationBottomsheetListener
            bottomSheet.isDisableOrderPrioritas = isDisableOrderPrioritas
            bottomSheet.mRecipientAddress = recipientAddressModel
            bottomSheet.mCartPosition = cartPosition
            bottomSheet.isOcc = isOcc
            bottomSheet.ratesParam = ratesParam
            bottomSheet.selectedSpId = selectedSpId
            bottomSheet.selectedServiceId = selectedServiceId
            bottomSheet.isRatesTradeInApi = isRatesTradeInApi
            fragmentManager.apply {
                bottomSheet.show(this, TAG)
            }
        }

        fun show(
            activity: Activity,
            fragmentManager: FragmentManager,
            shippingDurationBottomsheetListener: ShippingDurationBottomsheetListener?,
            shipmentDetailData: ShipmentDetailData,
            selectedServiceId: Int,
            shopShipmentList: List<ShopShipment>,
            recipientAddressModel: RecipientAddressModel? = null,
            cartPosition: Int,
            codHistory: Int = -1,
            isLeasing: Boolean = false,
            pslCode: String = "",
            products: ArrayList<Product>,
            cartString: String,
            isDisableOrderPrioritas: Boolean,
            isTradeInDropOff: Boolean = false,
            isFulFillment: Boolean = false,
            preOrderTime: Int = -1,
            mvc: String = "",
            cartData: String,
            isOcc: Boolean,
            warehouseId: String
        ) {
            var selectedSpId = 0
            shipmentDetailData.selectedCourier?.let { selectedCourier ->
                selectedSpId = selectedCourier.shipperProductId
            }
            val ratesParam = generateRatesParam(
                shipmentDetailData,
                shopShipmentList,
                recipientAddressModel,
                codHistory,
                isLeasing,
                pslCode,
                products,
                cartString,
                isTradeInDropOff,
                mvc,
                cartData,
                isOcc,
                warehouseId
            )
            show(
                fragmentManager,
                shippingDurationBottomsheetListener,
                ratesParam,
                selectedSpId,
                selectedServiceId,
                isTradeInDropOff,
                isDisableOrderPrioritas,
                recipientAddressModel,
                cartPosition,
                isOcc
            )
        }
    }
}
