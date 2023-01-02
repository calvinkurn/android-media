package com.tokopedia.tokofood.feature.merchant.presentation.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseMultiFragment
import com.tokopedia.abstraction.base.view.fragment.enums.BaseMultiFragmentLaunchMode
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalTokoFood
import com.tokopedia.applink.tokofood.DeeplinkMapperTokoFood
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.observe
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.response.GetStateChosenAddressResponse
import com.tokopedia.localizationchooseaddress.ui.bottomsheet.ChooseAddressBottomSheet
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddEditAddressSource
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.geolocation.autocomplete.LocationPass
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokofood.R
import com.tokopedia.tokofood.common.util.TokofoodErrorLogger
import com.tokopedia.tokofood.common.util.TokofoodRouteManager
import com.tokopedia.tokofood.databinding.FragmentManageLocationLayoutBinding
import com.tokopedia.tokofood.feature.merchant.di.DaggerMerchantPageComponent
import com.tokopedia.tokofood.feature.merchant.presentation.viewmodel.ManageLocationViewModel
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import java.util.*
import javax.inject.Inject

@FlowPreview
@ExperimentalCoroutinesApi
class ManageLocationFragment : BaseMultiFragment(), ChooseAddressBottomSheet.ChooseAddressBottomSheetListener {

    companion object {

        // keys
        private const val BUNDLE_KEY_NEGATIVE_CASE_ID = "negativeCaseId"
        private const val BUNDLE_KEY_MERCHANT_ID = "merchantId"
        private const val NEW_ADDRESS_PARCELABLE = "EXTRA_ADDRESS_NEW"

        // constant values
        private const val REQUEST_CODE_SET_PINPOINT = 112
        private const val REQUEST_CODE_ADD_ADDRESS = 113
        private const val TOTO_LATITUDE = "-6.2216771"
        private const val TOTO_LONGITUDE = "106.8184023"
        const val SOURCE = "tokofood"

        // image resource url
        const val IMG_STATIC_URI_NO_PIN_POIN = "https://images.tokopedia.net/img/ic-tokofood_home_no_pin_poin.png"
        const val IMG_STATIC_URI_NO_ADDRESS = "https://images.tokopedia.net/img/ic_tokofood_home_no_address.png"
        const val IMG_STATIC_URI_OUT_OF_COVERAGE = "https://images.tokopedia.net/img/ic_tokofood_home_out_of_coverage.png"

        // negative case ids
        const val EMPTY_STATE_OUT_OF_COVERAGE = "2"
        const val EMPTY_STATE_NO_PIN_POINT = "3"
        const val EMPTY_STATE_NO_ADDRESS = "4"

        @JvmStatic
        fun createInstance(negativeCaseId: String = "", merchantId: String) = ManageLocationFragment().apply {
            this.arguments = Bundle().apply {
                putString(BUNDLE_KEY_NEGATIVE_CASE_ID, negativeCaseId)
                putString(BUNDLE_KEY_MERCHANT_ID, merchantId)
            }
        }
    }

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }

    private val viewModel by lazy {
        viewModelProvider.get(ManageLocationViewModel::class.java)
    }

    private var binding: FragmentManageLocationLayoutBinding? = null

    private var localCacheModel: LocalCacheModel? = null

    override fun getFragmentToolbar(): Toolbar? {
        return binding?.toolbar
    }

    override fun getFragmentTitle(): String {
        return ""
    }

    override fun getLaunchMode(): BaseMultiFragmentLaunchMode {
        return BaseMultiFragmentLaunchMode.STANDARD
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun initInjector() {
        activity?.let {
            DaggerMerchantPageComponent
                    .builder()
                    .baseAppComponent((it.applicationContext as BaseMainApplication).baseAppComponent)
                    .build()
                    .inject(this)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val viewBinding = FragmentManageLocationLayoutBinding.inflate(inflater)
        binding = viewBinding
        return viewBinding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupBackgroundColor()
        (activity as AppCompatActivity).setSupportActionBar(binding?.toolbar)
        viewModel.merchantId = arguments?.getString(BUNDLE_KEY_MERCHANT_ID).orEmpty()
        context?.run {
            localCacheModel = ChooseAddressUtils.getLocalizingAddressData(this)
            when (arguments?.getString(BUNDLE_KEY_NEGATIVE_CASE_ID) ?: "") {
                EMPTY_STATE_NO_PIN_POINT -> bindNoPinPoint(this)
                EMPTY_STATE_NO_ADDRESS -> bindNoAddress(this)
                EMPTY_STATE_OUT_OF_COVERAGE -> bindOutOfCoverage(this)
            }
        }
        observeLiveData()
    }

    private fun setupBackgroundColor() {
        activity?.let {
            it.window.decorView.setBackgroundColor(
                ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_Background)
            )
        }
    }

    private fun observeLiveData() {
        observe(viewModel.errorMessage) { message ->
            showToaster(message)
        }
        observe(viewModel.eligibleForAnaRevamp) {
            when (it) {
                is Success -> {
                    if (it.data.eligible) {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_REF, ChooseAddressBottomSheet.SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
                        intent.putExtra(PARAM_SOURCE, AddEditAddressSource.TOKOFOOD.source)
                        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
                    } else {
                        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_REF, ChooseAddressBottomSheet.SCREEN_NAME_CHOOSE_ADDRESS_NEW_USER)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_FULL_FLOW, true)
                        intent.putExtra(ChooseAddressBottomSheet.EXTRA_IS_LOGISTIC_LABEL, false)
                        startActivityForResult(intent, REQUEST_CODE_ADD_ADDRESS)
                    }
                }
                is Fail -> {
                    showToaster(it.throwable.message)
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_ELIGIBLE_FOR_ADDRESS,
                        TokofoodErrorLogger.ErrorDescription.ERROR_ELIGIBLE_FOR_ADDRESS
                    )
                }
            }
        }
        observe(viewModel.updatePinPointState) { isSuccess ->
            if (isSuccess) {
                getChooseAddress()
            }
        }
        observe(viewModel.chooseAddress) {
            when (it) {
                is Success -> {
                    setupChooseAddress(it.data)
                }
                is Fail -> {
                    showToaster(it.throwable.message)
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_CHOOSE_ADDRESS,
                        TokofoodErrorLogger.ErrorDescription.ERROR_CHOOSE_ADDRESS_MANAGE_LOCATION
                    )
                }
            }
        }
        observe(viewModel.checkDeliveryCoverageResult) {
            when (it) {
                is Success -> {
                    val isDeliverable = it.data.tokofoodGetMerchantData.merchantProfile.deliverable
                    if (isDeliverable) navigateToMerchantPage(viewModel.merchantId)
                    else context?.run { bindOutOfCoverage(this) }
                }
                is Fail -> {
                    showToaster(it.throwable.message)
                    logExceptionToServerLogger(
                        it.throwable,
                        TokofoodErrorLogger.ErrorType.ERROR_CHECK_DELIVERY_COVERAGE,
                        TokofoodErrorLogger.ErrorDescription.ERROR_CHECK_DELIVERY_COVERAGE
                    )
                }
            }
        }
    }

    private fun logExceptionToServerLogger(
        throwable: Throwable,
        errorType: String,
        errorDesc: String
    ) {
        TokofoodErrorLogger.logExceptionToServerLogger(
            TokofoodErrorLogger.PAGE.MERCHANT,
            throwable,
            errorType,
            userSession.deviceId.orEmpty(),
            errorDesc
        )
    }

    private fun showToaster(message: String?) {
        view?.let {
            if (!message.isNullOrEmpty()) {
                Toaster.build(it, message, Toaster.LENGTH_LONG).show()
            }
        }
    }

    private fun bindNoPinPoint(context: Context) {
        val title = context.getString(R.string.home_no_pin_poin_title)
        val desc = context.getString(R.string.home_no_pin_poin_desc)
        binding?.tpgLocationBsToHome?.hide()
        binding?.iuLocationBsImage?.loadImage(IMG_STATIC_URI_NO_PIN_POIN)
        binding?.tpgLocationBsTitle?.text = title
        binding?.tpgLocationBsDesc?.text = desc
        binding?.btnLocationBsCta?.text = context.getString(R.string.home_no_pin_poin_button)
        binding?.btnLocationBsCta?.setOnClickListener { navigateToSetPinpoint() }
    }

    private fun bindNoAddress(context: Context) {
        val title = context.getString(R.string.home_no_address_title)
        val desc = context.getString(R.string.home_no_address_desc)
        binding?.tpgLocationBsToHome?.show()
        binding?.iuLocationBsImage?.loadImage(IMG_STATIC_URI_NO_ADDRESS)
        binding?.tpgLocationBsTitle?.text = title
        binding?.tpgLocationBsDesc?.text = desc
        binding?.btnLocationBsCta?.text = context.getString(R.string.home_no_address_button)
        binding?.btnLocationBsCta?.setOnClickListener {
            viewModel.checkUserEligibilityForAnaRevamp()
        }
        binding?.tpgLocationBsToHome?.setOnClickListener { navigateToHome() }
    }

    private fun bindOutOfCoverage(context: Context) {
        val title = context.getString(R.string.home_out_of_coverage_title)
        val desc = context.getString(R.string.home_out_of_coverage_desc)
        binding?.tpgLocationBsToHome?.show()
        binding?.iuLocationBsImage?.loadImage(IMG_STATIC_URI_OUT_OF_COVERAGE)
        binding?.tpgLocationBsTitle?.text = title
        binding?.tpgLocationBsDesc?.text = desc
        binding?.btnLocationBsCta?.text = context.getString(R.string.home_out_of_coverafe_button)
        binding?.btnLocationBsCta?.setOnClickListener { showChooseAddressBottomSheet() }
        binding?.tpgLocationBsToHome?.setOnClickListener { navigateToHome() }
    }

    private fun navigateToHome() {
        RouteManager.route(context, ApplinkConst.HOME)
    }

    private fun navigateToMerchantPage(merchantId: String) {
        val merchantPageUri = Uri.parse(ApplinkConstInternalTokoFood.MERCHANT)
                .buildUpon()
                .appendQueryParameter(DeeplinkMapperTokoFood.PARAM_MERCHANT_ID, merchantId)
                .build()
        TokofoodRouteManager.routePrioritizeInternal(context, merchantPageUri.toString(), isFinishCurrent = true)
    }

    private fun navigateToSetPinpoint() {
        view?.let {
            MapsAvailabilityHelper.onMapsAvailableState(it) {
                val locationPass = LocationPass().apply {
                    latitude = TOTO_LATITUDE
                    longitude = TOTO_LONGITUDE
                }
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.GEOLOCATION)
                val bundle = Bundle().apply {
                    putParcelable(LogisticConstant.EXTRA_EXISTING_LOCATION, locationPass)
                    putBoolean(LogisticConstant.EXTRA_IS_FROM_MARKETPLACE_CART, true)
                }
                intent.putExtras(bundle)
                startActivityForResult(intent, REQUEST_CODE_SET_PINPOINT)
            }
        }
    }

    private fun onResultFromAddAddress(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let {
                val addressDataModel = data.getParcelableExtra<SaveAddressDataModel>(NEW_ADDRESS_PARCELABLE)
                addressDataModel?.let {
                    setupChooseAddress(it)
                }
            }
        }
    }

    private fun onResultFromSetPinpoint(resultCode: Int, data: Intent?) {
        if (resultCode == Activity.RESULT_OK) {
            data?.let { intent ->
                val locationPass = intent.getParcelableExtra(LogisticConstant.EXTRA_EXISTING_LOCATION) as? LocationPass
                locationPass?.let { it ->
                    localCacheModel?.address_id?.let { addressId ->
                        viewModel.updatePinPoint(addressId, it.latitude, it.longitude)
                    }
                }
            }
        }
    }

    private fun setupChooseAddress(data: GetStateChosenAddressResponse) {
        data.let { chooseAddressData ->
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    context = requireContext(),
                    addressId = chooseAddressData.data.addressId.toString(),
                    cityId = chooseAddressData.data.cityId.toString(),
                    districtId = chooseAddressData.data.districtId.toString(),
                    lat = chooseAddressData.data.latitude,
                    long = chooseAddressData.data.longitude,
                    label = String.format(
                            "%s %s",
                            chooseAddressData.data.addressName,
                            chooseAddressData.data.receiverName
                    ),
                    postalCode = chooseAddressData.data.postalCode,
                    warehouseId = chooseAddressData.tokonow.warehouseId.toString(),
                    shopId = chooseAddressData.tokonow.shopId.toString(),
                    warehouses = TokonowWarehouseMapper.mapWarehousesResponseToLocal(chooseAddressData.tokonow.warehouses),
                    serviceType = chooseAddressData.tokonow.serviceType,
                    lastUpdate = chooseAddressData.tokonow.tokonowLastUpdate
            )
        }
        checkIfChooseAddressWidgetDataUpdated()
        navigateToMerchantPage(viewModel.merchantId)
    }

    private fun setupChooseAddress(addressDataModel: SaveAddressDataModel) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(it,
                    addressDataModel.id.toString(), addressDataModel.cityId.toString(), addressDataModel.districtId.toString(),
                    addressDataModel.latitude, addressDataModel.longitude, "${addressDataModel.addressName} ${addressDataModel.receiverName}",
                    addressDataModel.postalCode, addressDataModel.shopId.toString(), addressDataModel.warehouseId.toString(),
                    TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressDataModel.warehouses), addressDataModel.serviceType)
        }
        checkIfChooseAddressWidgetDataUpdated()
        context?.run {
            ChooseAddressUtils.getLocalizingAddressData(this)
                    .let { addressData ->
                        viewModel.checkDeliveryCoverage(
                                merchantId = viewModel.merchantId,
                                latlong = addressData.latLong,
                                timezone = TimeZone.getDefault().id
                        )
                    }
        }
    }

    private fun checkIfChooseAddressWidgetDataUpdated() {
        if (isChooseAddressWidgetDataUpdated()) {
            updateCurrentPageLocalCacheModelData()
        }
    }

    private fun isChooseAddressWidgetDataUpdated(): Boolean {
        localCacheModel?.let {
            context?.apply {
                return ChooseAddressUtils.isLocalizingAddressHasUpdated(
                        this,
                        it
                )
            }
        }
        return false
    }

    private fun updateCurrentPageLocalCacheModelData() {
        context?.let { localCacheModel = ChooseAddressUtils.getLocalizingAddressData(it) }
    }

    private fun getChooseAddress() {
        viewModel.getChooseAddress(SOURCE)
    }

    private fun showChooseAddressBottomSheet() {
        val chooseAddressBottomSheet = ChooseAddressBottomSheet()
        chooseAddressBottomSheet.setListener(this)
        chooseAddressBottomSheet.show(childFragmentManager, "")
    }

    override fun onLocalizingAddressServerDown() {}

    override fun onAddressDataChanged() {
        navigateToMerchantPage(viewModel.merchantId)
    }

    override fun getLocalizingAddressHostSourceBottomSheet(): String {
        return SOURCE
    }

    override fun onLocalizingAddressLoginSuccessBottomSheet() {}

    override fun onDismissChooseAddressBottomSheet() {}

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_ADD_ADDRESS -> onResultFromAddAddress(resultCode, data)
            REQUEST_CODE_SET_PINPOINT -> onResultFromSetPinpoint(resultCode, data)
        }
    }
}
