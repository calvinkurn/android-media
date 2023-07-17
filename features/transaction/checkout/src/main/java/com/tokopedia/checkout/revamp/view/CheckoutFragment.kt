package com.tokopedia.checkout.revamp.view

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.akamai_bot_lib.exception.AkamaiErrorException
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.checkout.databinding.FragmentCheckoutBinding
import com.tokopedia.checkout.databinding.HeaderCheckoutBinding
import com.tokopedia.checkout.databinding.ToastRectangleBinding
import com.tokopedia.checkout.revamp.di.CheckoutModule
import com.tokopedia.checkout.revamp.di.DaggerCheckoutComponent
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapter
import com.tokopedia.checkout.revamp.view.adapter.CheckoutAdapterListener
import com.tokopedia.checkout.revamp.view.adapter.CheckoutDiffUtilCallback
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutOrderModel
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutPageState
import com.tokopedia.checkout.revamp.view.uimodel.CheckoutProductModel
import com.tokopedia.checkout.view.ShipmentFragment
import com.tokopedia.checkout.view.uimodel.ShipmentNewUpsellModel
import com.tokopedia.loaderdialog.LoaderDialog
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddEditAddressSource
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.util.PinpointRolloutHelper
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.exception.CartResponseErrorException
import com.tokopedia.purchase_platform.common.feature.addons.data.model.AddOnProductDataItemModel
import com.tokopedia.purchase_platform.common.feature.checkout.ShipmentFormRequest
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.domain.model.UploadPrescriptionUiModel
import com.tokopedia.purchase_platform.common.feature.ethicaldrug.view.UploadPrescriptionListener
import com.tokopedia.purchase_platform.common.utils.animateGone
import com.tokopedia.purchase_platform.common.utils.animateShow
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoCleared
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CheckoutFragment : BaseDaggerFragment(), CheckoutAdapterListener, UploadPrescriptionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: CheckoutViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[CheckoutViewModel::class.java]
    }

    private var binding by autoCleared<FragmentCheckoutBinding>()

    private var header by autoCleared<HeaderCheckoutBinding>()

    private val adapter: CheckoutAdapter = CheckoutAdapter(this, this)

    private var loader: LoaderDialog? = null

    private val isPlusSelected: Boolean
        get() = arguments?.getBoolean(ShipmentFragment.ARG_IS_PLUS_SELECTED, false) ?: false

    private val deviceId: String
        get() = if (arguments?.getString(ShipmentFormRequest.EXTRA_DEVICE_ID) != null) {
            arguments!!.getString(ShipmentFormRequest.EXTRA_DEVICE_ID)!!
        } else {
            ""
        }

    private val isOneClickShipment: Boolean
        get() = arguments != null && arguments!!.getBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT)

    private val checkoutLeasingId: String
        get() {
            var leasingId = "0"
            if (arguments != null && arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID) != null &&
                !arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID)
                    .equals("null", ignoreCase = true)
            ) {
                leasingId = arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID)!!
            }
            return leasingId
        }

    private val isTradeIn: Boolean
        get() = arguments != null && arguments!!.getString(
            ShipmentFormRequest.EXTRA_DEVICE_ID,
            ""
        ) != null && arguments!!.getString(ShipmentFormRequest.EXTRA_DEVICE_ID, "").isNotEmpty()

    private val checkoutPageSource: String
        get() {
            var pageSource: String = CheckoutConstant.CHECKOUT_PAGE_SOURCE_PDP
            if (arguments != null && arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE) != null) {
                pageSource = arguments!!.getString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE)!!
            }
            return pageSource
        }

    override fun getScreenName(): String {
        return if (isOneClickShipment) {
            ConstantTransactionAnalytics.ScreenName.ONE_CLICK_SHIPMENT
        } else {
            ConstantTransactionAnalytics.ScreenName.CHECKOUT
        }
    }

    override fun initInjector() {
        if (activity != null) {
            val baseMainApplication = activity!!.application as BaseMainApplication
            DaggerCheckoutComponent.builder()
                .baseAppComponent(baseMainApplication.baseAppComponent)
                .checkoutModule(CheckoutModule(this))
                .build()
                .inject(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        activity?.findViewById<View>(com.tokopedia.abstraction.R.id.toolbar)?.isVisible = false
        binding = FragmentCheckoutBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.headerCheckout.setNavigationOnClickListener {
            onBackPressed()
        }
        header = HeaderCheckoutBinding.inflate(LayoutInflater.from(context))
        header.tvCheckoutHeaderAddressHeader.isVisible = false
        header.icCheckoutHeaderAddress.isVisible = false
        header.tvCheckoutHeaderAddressName.isVisible = false
        binding.headerCheckout.customView(header.root)

        binding.rvCheckout.adapter = adapter
        binding.rvCheckout.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        binding.rvCheckout.clearOnScrollListeners()
        binding.rvCheckout.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val linearLayoutManager = recyclerView.layoutManager as? LinearLayoutManager
                if (recyclerView.isVisible &&
                    (linearLayoutManager?.findFirstVisibleItemPosition() ?: -1) > 1
                ) {
                    header.tvCheckoutHeaderText.animateGone()
                    header.tvCheckoutHeaderAddressHeader.animateShow()
                    header.icCheckoutHeaderAddress.animateShow()
                    header.tvCheckoutHeaderAddressName.animateShow()
                } else if (!header.tvCheckoutHeaderText.isVisible) {
                    header.tvCheckoutHeaderText.animateShow()
                    header.tvCheckoutHeaderAddressHeader.animateGone()
                    header.icCheckoutHeaderAddress.animateGone()
                    header.tvCheckoutHeaderAddressName.animateGone()
                }
            }
        })

//        binding.tvCheckoutTesting.setOnClickListener {
//            if (header.tvCheckoutHeaderText.isVisible) {
//                header.tvCheckoutHeaderText.animateGone()
//                header.tvCheckoutHeaderAddressHeader.animateShow()
//                header.tvCheckoutHeaderAddressName.animateShow()
//            } else {
//                header.tvCheckoutHeaderText.animateShow()
//                header.tvCheckoutHeaderAddressHeader.animateGone()
//                header.tvCheckoutHeaderAddressName.animateGone()
//            }
//            viewModel.test()
//        }

        initViewModel()
    }

    private fun initViewModel() {
        viewModel.isOneClickShipment = isOneClickShipment
        viewModel.isTradeIn = isTradeIn
        viewModel.deviceId = deviceId
        viewModel.checkoutLeasingId = checkoutLeasingId
        viewModel.isPlusSelected = isPlusSelected
        observeData()

        viewModel.loadSAF(
            skipUpdateOnboardingState = true,
            isReloadData = false,
            isReloadAfterPriceChangeHigher = false
        )
    }

    @SuppressLint("SetTextI18n")
    private fun observeData() {
        viewModel.listData.observe(viewLifecycleOwner) {
            val diffResult = DiffUtil.calculateDiff(CheckoutDiffUtilCallback(it, adapter.list))
            adapter.list = it
            diffResult.dispatchUpdatesTo(adapter)

            it.address()?.recipientAddressModel?.also {address ->
                header.tvCheckoutHeaderAddressName.text = "${address.addressName} • ${address.recipientName}"
            }
        }

        viewModel.pageState.observe(viewLifecycleOwner) {
            when (it) {
                is CheckoutPageState.CacheExpired -> {
                }

                is CheckoutPageState.CheckNoAddress -> {
                }

                CheckoutPageState.EmptyData -> {
                }

                is CheckoutPageState.Error -> {
                    hideLoading()
                    var errorMessage = it.throwable.message ?: ""
                    if (!(it.throwable is CartResponseErrorException || it.throwable is AkamaiErrorException)) {
                        errorMessage = ErrorHandler.getErrorMessage(context, it.throwable)
                    }
                    if (errorMessage.isEmpty()) {
                        errorMessage =
                            getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_error_global_message)
                    }
                    Toaster.build(binding.root, errorMessage, type = Toaster.TYPE_ERROR).show()
                }

                CheckoutPageState.Loading -> {
                    showLoading()
                }

                is CheckoutPageState.NoAddress -> {
                    val token = Token()
                    token.ut = it.cartShipmentAddressFormData.keroUnixTime
                    token.districtRecommendation = it.cartShipmentAddressFormData.keroDiscomToken
                    if (it.eligible) {
                        val intent =
                            RouteManager.getIntent(activity, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
                        intent.putExtra(CheckoutConstant.KERO_TOKEN, token)
                        intent.putExtra(
                            ChooseAddressBottomSheet.EXTRA_REF,
                            CartConstant.SCREEN_NAME_CART_NEW_USER
                        )
                        intent.putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, AddEditAddressSource.CART.source)
                        startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY)
                    } else {
                        val intent =
                            RouteManager.getIntent(activity, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
                        intent.putExtra(CheckoutConstant.KERO_TOKEN, token)
                        intent.putExtra(
                            ChooseAddressBottomSheet.EXTRA_REF,
                            CartConstant.SCREEN_NAME_CART_NEW_USER
                        )
                        startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY)
                    }
                }

                is CheckoutPageState.NoMatchedAddress -> {
                }

                is CheckoutPageState.Success -> {
                    hideLoading()
                    binding.rvCheckout.isVisible = true
                }

                is CheckoutPageState.Normal -> {
                    hideLoading()
                }
            }
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.commonToaster.map {
                var message = it.toasterMessage
                if (message.isEmpty()) {
                    message = it.throwable?.message ?: ""
                    if (!(it.throwable is CartResponseErrorException || it.throwable is AkamaiErrorException)) {
                        message = ErrorHandler.getErrorMessage(context, it.throwable)
                    }
                    if (message.isEmpty()) {
                        message =
                            getString(com.tokopedia.purchase_platform.common.R.string.checkout_flow_error_global_message)
                    }
                }
                Toaster.build(binding.root, message, type = it.toasterType).show()
            }
        }
    }

    fun onBackPressed(): Boolean {
        activity?.finish()
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS -> {
                onResultFromRequestCodeAddressOptions(resultCode, data)
            }

            LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY -> {
                onResultFromAddNewAddress(resultCode, data)
            }

            REQUEST_CODE_COURIER_PINPOINT -> {
                onResultFromCourierPinpoint(resultCode, data)
            }
        }
    }

    private fun onResultFromRequestCodeAddressOptions(resultCode: Int, data: Intent?) {
        when (resultCode) {
            CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS -> {
                val currentAddress = viewModel.listData.value.address()?.recipientAddressModel
                val chosenAddressModel =
                    data!!.getParcelableExtra<ChosenAddressModel>(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA)
                if (currentAddress != null && chosenAddressModel != null) {
                    viewModel.changeAddress(
                        currentAddress,
                        chosenAddressModel,
                        false
                    )
                }
            }

            Activity.RESULT_CANCELED -> if (activity != null && data == null && viewModel.listData.value.isEmpty()) {
                activity!!.finish()
            }

            else -> viewModel.loadSAF(
                isReloadData = false,
                skipUpdateOnboardingState = false,
                isReloadAfterPriceChangeHigher = false
            )
        }
    }

    private fun onResultFromAddNewAddress(resultCode: Int, data: Intent?) {
        val activity: Activity? = activity
        if (activity != null) {
            if (resultCode == Activity.RESULT_CANCELED) {
                activity.finish()
            } else {
                if (data != null) {
                    val addressDataModel = data.getParcelableExtra<SaveAddressDataModel>(
                        LogisticConstant.EXTRA_ADDRESS_NEW
                    )
                    addressDataModel?.let { updateLocalCacheAddressData(it) }
                }
                viewModel.loadSAF(
                    isReloadData = false,
                    skipUpdateOnboardingState = false,
                    isReloadAfterPriceChangeHigher = false
                )
            }
        }
    }

    @Suppress("ImplicitDefaultLocale")
    private fun updateLocalCacheAddressData(saveAddressDataModel: SaveAddressDataModel) {
        val activity: Activity? = activity
        if (activity != null) {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                activity,
                saveAddressDataModel.id.toString(),
                saveAddressDataModel.cityId.toString(),
                saveAddressDataModel.districtId.toString(),
                saveAddressDataModel.latitude,
                saveAddressDataModel.longitude,
                String.format(
                    "%s %s",
                    saveAddressDataModel.addressName,
                    saveAddressDataModel.receiverName
                ),
                saveAddressDataModel.postalCode,
                saveAddressDataModel.shopId.toString(),
                saveAddressDataModel.warehouseId.toString(),
                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(saveAddressDataModel.warehouses),
                saveAddressDataModel.serviceType,
                ""
            )
        }
    }

    private fun onResultFromCourierPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK && data!!.extras != null) {
            val locationPass = getLocationPassFromIntent(data)
            if (locationPass != null) {
                viewModel.editAddressPinpoint(
                    locationPass.latitude,
                    locationPass.longitude,
                    locationPass
                )
            }
        } else {
//            shipmentAdapter.lastServiceId = 0
        }
    }

    private fun getLocationPassFromIntent(data: Intent): LocationPass? {
        var locationPass =
            data.extras!!.getParcelable<LocationPass>(LogisticConstant.EXTRA_EXISTING_LOCATION)
        if (locationPass == null) {
            val addressData =
                data.getParcelableExtra<SaveAddressDataModel>(AddressConstant.EXTRA_SAVE_DATA_UI_MODEL)
            if (addressData != null) {
                locationPass = LocationPass()
                locationPass.latitude = addressData.latitude
                locationPass.longitude = addressData.longitude
                locationPass.districtName = addressData.districtName
                locationPass.cityName = addressData.cityName
            }
        }
        return locationPass
    }

    fun showLoading() {
        if (context != null && loader?.dialog?.isShowing != true) {
            loader = LoaderDialog(context!!)
            loader!!.show()
        }
    }

    fun hideLoading() {
        if (loader != null && loader!!.dialog.isShowing) {
            loader!!.dismiss()
        }
    }

    fun navigateToSetPinpoint(message: String, locationPass: LocationPass?) {
//        sendAnalyticsOnClickEditPinPointErrorValidation(message)
        if (view != null) {
            val toastRectangleBinding = ToastRectangleBinding.inflate(layoutInflater, null, false)
            toastRectangleBinding.tvMessage.text = message
            val toast = Toast(activity)
            toast.duration = Toast.LENGTH_LONG
            toast.view = toastRectangleBinding.root
            toast.show()
        } else {
            Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
        }
        if (activity != null) {
            navigateToPinpointActivity(locationPass)
        }
    }

    private fun navigateToPinpointActivity(locationPass: LocationPass?) {
        val activity: Activity? = activity
        if (activity != null) {
            if (PinpointRolloutHelper.eligibleForRevamp(activity, true)) {
                val bundle = Bundle()
                bundle.putBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY, true)
                if (locationPass?.latitude != null &&
                    locationPass.latitude.isNotEmpty() && locationPass.longitude != null &&
                    locationPass.longitude.isNotEmpty()
                ) {
                    bundle.putDouble(AddressConstant.EXTRA_LAT, locationPass.latitude.toDouble())
                    bundle.putDouble(
                        AddressConstant.EXTRA_LONG,
                        locationPass.longitude.toDouble()
                    )
                }
                bundle.putString(AddressConstant.EXTRA_CITY_NAME, locationPass?.cityName)
                bundle.putString(AddressConstant.EXTRA_DISTRICT_NAME, locationPass?.districtName)
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.PINPOINT)
                intent.putExtra(AddressConstant.EXTRA_BUNDLE, bundle)
                startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
            } else {
                val intent =
                    RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
                val bundle = Bundle()
                bundle.putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
                bundle.putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
                intent.putExtras(bundle)
                startActivityForResult(intent, REQUEST_CODE_COURIER_PINPOINT)
            }
        }
    }

    companion object {

        private const val REQUEST_CODE_COURIER_PINPOINT = 13

        fun newInstance(
            isOneClickShipment: Boolean,
            leasingId: String,
            pageSource: String,
            isPlusSelected: Boolean,
            bundle: Bundle?
        ): CheckoutFragment {
            val b = bundle ?: Bundle()
            b.putString(ShipmentFragment.ARG_CHECKOUT_LEASING_ID, leasingId)
            if (leasingId.isNotEmpty()) {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, true)
            } else {
                b.putBoolean(ShipmentFragment.ARG_IS_ONE_CLICK_SHIPMENT, isOneClickShipment)
            }
            b.putString(ShipmentFragment.ARG_CHECKOUT_PAGE_SOURCE, pageSource)
            b.putBoolean(ShipmentFragment.ARG_IS_PLUS_SELECTED, isPlusSelected)
            val checkoutFragment = CheckoutFragment()
            checkoutFragment.arguments = b
            return checkoutFragment
        }
    }

    // region adapter listener

    override fun onChangeAddress() {
        val intent = RouteManager.getIntent(activity, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        intent.putExtra(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS, true)
        intent.putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, ManageAddressSource.CHECKOUT.source)
        startActivityForResult(intent, CheckoutConstant.REQUEST_CODE_CHECKOUT_ADDRESS)
    }

    override fun onViewNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        // todo
    }

    override fun onClickApplyNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        TODO("Not yet implemented")
    }

    override fun onClickCancelNewUpsellCard(shipmentUpsellModel: ShipmentNewUpsellModel) {
        TODO("Not yet implemented")
    }

    override fun onViewFreeShippingPlusBadge() {
        // todo
    }

    override fun onCheckboxAddonProductListener(
        isChecked: Boolean,
        addOnProductDataItemModel: AddOnProductDataItemModel,
        product: CheckoutProductModel,
        bindingAdapterPosition: Int
    ) {
        TODO("Not yet implemented")
    }

    override fun onClickAddonProductInfoIcon(addOnDataInfoLink: String) {
        TODO("Not yet implemented")
    }

    override fun onClickAddOnsProductWidget(addonType: Int, productId: String, isChecked: Boolean) {
        TODO("Not yet implemented")
    }

    override fun onClickSeeAllAddOnProductService(product: CheckoutProductModel) {
        TODO("Not yet implemented")
    }

    override fun onImpressionAddOnProductService(addonType: Int, productId: String) {
        TODO("Not yet implemented")
    }

    override fun onClickLihatSemuaAddOnProductWidget() {
        TODO("Not yet implemented")
    }

    override fun onClickAddOnGiftingProductLevel(
        product: CheckoutProductModel
    ) {
        TODO("Not yet implemented")
    }

    override fun onImpressionAddOnGiftingProductLevel(productId: String) {
        TODO("Not yet implemented")
    }

    override fun openAddOnGiftingOrderLevelBottomSheet(order: CheckoutOrderModel) {
        TODO("Not yet implemented")
    }

    override fun addOnGiftingOrderLevelImpression(products: List<CheckoutProductModel>) {
        TODO("Not yet implemented")
    }

    override fun onChangeShippingDuration() {

    }

    // endregion

    override fun uploadPrescriptionAction(
        uploadPrescriptionUiModel: UploadPrescriptionUiModel,
        buttonText: String,
        buttonNotes: String
    ) {
        TODO("Not yet implemented")
    }
}
