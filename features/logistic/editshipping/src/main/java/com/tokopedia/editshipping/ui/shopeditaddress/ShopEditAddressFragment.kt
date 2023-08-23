package com.tokopedia.editshipping.ui.shopeditaddress

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.editshipping.R
import com.tokopedia.editshipping.databinding.FragmentShopEditAddressBinding
import com.tokopedia.editshipping.di.shopeditaddress.DaggerShopEditAddressComponent
import com.tokopedia.editshipping.domain.model.shopeditaddress.ShopEditAddressState
import com.tokopedia.editshipping.util.EditShippingConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.editshipping.util.EditShippingConstant.DEFAULT_LAT
import com.tokopedia.editshipping.util.EditShippingConstant.DEFAULT_LONG
import com.tokopedia.editshipping.util.EditShippingConstant.EXTRA_IS_EDIT_WAREHOUSE
import com.tokopedia.editshipping.util.EditShippingConstant.EXTRA_IS_FULL_FLOW
import com.tokopedia.editshipping.util.EditShippingConstant.EXTRA_LAT
import com.tokopedia.editshipping.util.EditShippingConstant.EXTRA_LONG
import com.tokopedia.editshipping.util.EditShippingConstant.EXTRA_WAREHOUSE_DATA
import com.tokopedia.editshipping.util.ShopEditAddressLevenshteinUtils
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_WH_DISTRICT_ID
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.logisticCommon.util.LogisticUserConsentHelper
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import timber.log.Timber
import javax.inject.Inject

class ShopEditAddressFragment : BaseDaggerFragment(), OnMapReadyCallback {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var userSession: UserSessionInterface

    private val viewModel: ShopEditAddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopEditAddressViewModel::class.java)
    }

    private var warehouseModel: Warehouse? = null
    private var zipCodes: List<String> = ArrayList()
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var detailAddressHelper: String = ""

    private var getSavedInstanceState: Bundle? = null
    private var googleMap: GoogleMap? = null
    private var validate: Boolean = true
    private var uncoveredCourierFlag: Boolean = false

    private val pinpointPageResult = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val addressModel =
                it.data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_MODEL)
                    ?: it.data?.getParcelableExtra<SaveAddressDataModel>(
                        AddressConstant.EXTRA_SAVE_DATA_UI_MODEL
                    )
            addressModel?.let { address ->
                warehouseModel?.districtId = address.districtId
                detailAddressHelper = address.formattedAddress
                adjustMap(address.latitude.toDouble(), address.longitude.toDouble())
            }
        }
    }

    private var binding by autoClearedNullable<FragmentShopEditAddressBinding>()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerShopEditAddressComponent.builder()
            .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
            .build().inject(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            warehouseModel = it.getParcelable(EXTRA_WAREHOUSE_DATA)
            currentLat = it.getDouble(EXTRA_LAT)
            currentLong = it.getDouble(EXTRA_LONG)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentShopEditAddressBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            getSavedInstanceState = savedInstanceState
        }
        initMaps()
        initViewModel()
        prepareMap()
        prepareLayout()
        setViewListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                GET_DISTRICT_RECCOMENDATION_REQUEST_CODE -> {
                    val address = data?.getParcelableExtra<DistrictRecommendationAddress>(
                        RESULT_INTENT_DISTRICT_RECOMMENDATION
                    )
                    binding?.etKotaKecamatanShop?.setText(address?.districtName + ", " + address?.cityName)
                    binding?.etKodePosShop?.setText("")

                    if (address?.zipCodes != null) {
                        zipCodes = ArrayList(address.zipCodes)
                        initZipCode()
                    }

                    address?.let {
                        warehouseModel?.districtId = it.districtId
                    }
                }
            }
        }
    }

    private fun initZipCode() {
        val zipCodeAdapter = context?.let {
            ArrayAdapter(
                it,
                com.tokopedia.design.R.layout.item_autocomplete_text_double_row,
                com.tokopedia.design.R.id.item,
                zipCodes
            )
        }

        binding?.etKodePosShop?.setAdapter(zipCodeAdapter)
    }

    override fun onResume() {
        super.onResume()
        binding?.layoutMapsPreview?.mapViewDetail?.onResume()
    }

    override fun onStart() {
        super.onStart()
        binding?.layoutMapsPreview?.mapViewDetail?.onStart()
    }

    override fun onStop() {
        super.onStop()
        binding?.layoutMapsPreview?.mapViewDetail?.onStop()
    }

    override fun onPause() {
        binding?.layoutMapsPreview?.mapViewDetail?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        binding?.layoutMapsPreview?.mapViewDetail?.onDestroy()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        binding?.layoutMapsPreview?.mapViewDetail?.onLowMemory()
    }

    private fun initMaps() {
        context?.let {
            if (!MapsAvailabilityHelper.isMapsAvailable(it)) {
                binding?.layoutMapsPreview?.mapViewDetail?.gone()
            }
        }
    }

    private fun initViewModel() {
        viewModel.zipCodeList.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    zipCodes = ArrayList(it.data.district[0].zipCode)
                    initZipCode()
                    if (zipCodes.isEmpty()) {
                        binding?.etKodePosShop?.apply {
                            isFocusableInTouchMode = true
                            isFocusable = true
                            setOnClickListener(null)
                        }
                    }
                }
                is Fail -> zipCodes = arrayListOf()
            }
        }

        viewModel.districtLocation.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val lat = it.data.latitude.toDouble()
                    val long = it.data.longitude.toDouble()
                    adjustMap(lat, long)
                    detailAddressHelper = it.data.formattedAddress
                    val addressDetailUser = binding?.etDetailAlamatShop?.text.toString()
                    checkValidateAddressDetail(detailAddressHelper, addressDetailUser)
                }
                is Fail -> Timber.d(it.throwable)
            }
        }

        viewModel.districtGeocode.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> detailAddressHelper = it.data.data.formattedAddress
                is Fail -> detailAddressHelper = ""
            }
        }

        viewModel.saveEditShop.observe(viewLifecycleOwner) {
            when (it) {
                is ShopEditAddressState.Success -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    view?.let { view ->
                        Toaster.build(
                            view,
                            getString(R.string.save_edit_shop_success),
                            Toaster.LENGTH_SHORT,
                            type = Toaster.TYPE_NORMAL
                        ).show()
                    }
                    if (!uncoveredCourierFlag) {
                        activity?.setResult(Activity.RESULT_OK)
                    } else {
                        startActivity(
                            RouteManager.getIntent(
                                context,
                                ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING
                            )
                        )
                    }
                    activity?.finish()
                }
                is ShopEditAddressState.Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    view?.let { view ->
                        Toaster.build(
                            view,
                            DEFAULT_ERROR_MESSAGE,
                            Toaster.LENGTH_SHORT,
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }

                else -> binding?.swipeRefresh?.isRefreshing = true
            }
        }

        viewModel.checkCouriers.observe(viewLifecycleOwner) {
            when (it) {
                is ShopEditAddressState.Success -> {
                    checkCouriersCoverage(it.data.data.isCovered)
                }

                is ShopEditAddressState.Fail -> {
                    binding?.swipeRefresh?.isRefreshing = false
                    view?.let { view ->
                        Toaster.build(
                            view,
                            DEFAULT_ERROR_MESSAGE,
                            Toaster.LENGTH_SHORT,
                            type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }

                else -> binding?.swipeRefresh?.isRefreshing = true
            }
        }
    }

    private fun checkValidateAddressDetail(addressHelper: String, userAddress: String) {
        val normalizeAddressHelper = ShopEditAddressLevenshteinUtils.normalize(addressHelper)
        val normalizeUserAddress = ShopEditAddressLevenshteinUtils.normalize(userAddress)
        if (ShopEditAddressLevenshteinUtils.validateAddressSimilarity(
                normalizeAddressHelper,
                normalizeUserAddress
            )
        ) {
            validate = true
            binding?.tvDetailAlamatHelper?.text = ""
        } else {
            validate = false
            binding?.tvDetailAlamatHelper?.text =
                getString(R.string.detail_alamat_error_helper, detailAddressHelper)
        }
    }

    private fun checkCouriersCoverage(isCoverage: Boolean) {
        if (isCoverage) {
            val latLong = "$currentLat,$currentLong"
            warehouseModel?.let {
                viewModel.saveEditShopLocation(
                    userSession.shopId.toLong(),
                    it.warehouseId,
                    binding?.etNamaLokasiShop?.text.toString(),
                    it.districtId,
                    latLong,
                    binding?.etDetailAlamatShop?.text.toString(),
                    binding?.etKodePosShop?.text.toString()
                )
            }
        } else {
            binding?.swipeRefresh?.isRefreshing = false
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog =
            context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.apply {
            setTitle(getString(R.string.title_save_dialog))
            setDescription(getString(R.string.desc_save_dialog))
            setPrimaryCTAText(getString(R.string.primary_button_save_dialog))
            setPrimaryCTAClickListener {
                uncoveredCourierFlag = true
                val latLong = "$currentLat,$currentLong"
                warehouseModel?.let {
                    viewModel.saveEditShopLocation(
                        userSession.shopId.toLong(),
                        it.warehouseId,
                        binding?.etNamaLokasiShop?.text.toString(),
                        it.districtId,
                        latLong,
                        binding?.etDetailAlamatShop?.text.toString(),
                        binding?.etKodePosShop?.text.toString()
                    )
                }
            }
            setSecondaryCTAText(getString(R.string.secondary_button_save_dialog))
            setSecondaryCTAClickListener {
                dialog.hide()
            }
            show()
        }
    }

    private fun prepareMap() {
        binding?.layoutMapsPreview?.mapViewDetail?.onCreate(getSavedInstanceState)
        binding?.layoutMapsPreview?.mapViewDetail?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        this.googleMap?.uiSettings?.setAllGesturesEnabled(false)
        activity?.let { MapsInitializer.initialize(activity) }
        moveMap(getLatLng(currentLat, currentLong))
    }

    private fun prepareLayout() {
        binding?.etNamaLokasiShop?.setText(warehouseModel?.warehouseName)
        binding?.etKotaKecamatanShop?.setText(warehouseModel?.districtName)
        binding?.etKodePosShop?.setText(warehouseModel?.postalCode)
        binding?.etDetailAlamatShop?.setText(warehouseModel?.addressDetail)

        binding?.tvPinpointText?.text =
            context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_desc)).spannedString }

        binding?.layoutMapsPreview?.btnOpenMap?.setOnClickListener {
            goToPinpointActivity(currentLat, currentLong, warehouseModel)
        }

        LogisticUserConsentHelper.displayUserConsent(
            activity as Context,
            userSession.userId,
            binding?.tvUserConsent,
            getString(R.string.save_changes)
        )

        binding?.btnSaveWarehouse?.setOnClickListener {
            warehouseModel?.let { it ->
                if (validate) {
                    viewModel.checkCouriersAvailability(userSession.shopId.toLong(), it.districtId)
                }
            }
        }

        viewModel.getZipCode(warehouseModel?.districtId.toString())

        if (warehouseModel?.latLon?.isNotEmpty() == true) {
            viewModel.getDistrictGeocode(warehouseModel?.latLon)
        } else {
            viewModel.getDistrictGeocode("$DEFAULT_LAT,$DEFAULT_LONG")
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setViewListener() {
        binding?.etNamaLokasiShop?.apply {
            addTextChangedListener(setShopLocationWatcher())
        }

        binding?.etKotaKecamatanShop?.apply {
            setOnClickListener {
                val intent = RouteManager.getIntent(
                    activity,
                    ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS
                )
                startActivityForResult(intent, GET_DISTRICT_RECCOMENDATION_REQUEST_CODE)
            }
        }

        binding?.etKodePosShop?.apply {
            setOnTouchListener(
                View.OnTouchListener { _, _ ->
                    if (binding?.etKodePosShop?.isPopupShowing == false) {
                        binding?.etKodePosShop?.showDropDown()
                    }
                    false
                }
            )
        }

        binding?.etDetailAlamatShop?.apply {
            addTextChangedListener(setAlamatWatcher())
        }
    }

    private fun setShopLocationWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val strLength = s.toString().length
                val info = "$strLength/25"
                binding?.tvNamaLokasiWatcher?.text = info
            }

            override fun afterTextChanged(s: Editable?) {
                // no-op
            }
        }
    }

    private fun setAlamatWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // no-op
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    val strLength = s.toString().length
                    when {
                        strLength < 20 -> {
                            validate = false
                            binding?.tvDetailAlamatHelper?.text = getString(R.string.helper_shop_detail)
                        }
                        else -> {
                            checkValidateAddressDetail(s.toString(), detailAddressHelper)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                // no-op
            }
        }
    }

    private fun adjustMap(lat: Double, long: Double) {
        currentLat = lat
        currentLong = long
        moveMap(getLatLng(currentLat, currentLong))
    }

    private fun moveMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
            .target(latLng)
            .zoom(MAP_ZOOM)
            .bearing(MAP_BEARING)
            .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun goToPinpointActivity(lat: Double?, long: Double?, warehouseDataModel: Warehouse?) {
        context?.let {
            // go to pinpoint
            val bundle = Bundle().apply {
                putBoolean(AddressConstant.EXTRA_IS_GET_PINPOINT_ONLY, true)
                if (lat != null && long != null) {
                    putDouble(AddressConstant.EXTRA_LAT, lat)
                    putDouble(AddressConstant.EXTRA_LONG, long)
                    putBoolean(EXTRA_IS_EDIT_WAREHOUSE, true)
                    warehouseDataModel?.districtId?.let { districtId ->
                        putLong(
                            EXTRA_WH_DISTRICT_ID,
                            districtId
                        )
                    }
                }
            }
            RouteManager.getIntent(activity, ApplinkConstInternalLogistic.PINPOINT).apply {
                putExtra(AddressConstant.EXTRA_BUNDLE, bundle)
                pinpointPageResult.launch(this)
            }
        }
    }

    companion object {
        private const val GET_DISTRICT_RECCOMENDATION_REQUEST_CODE = 100
        private const val OPEN_MAP_REQUEST_CODE = 200
        private const val MAP_ZOOM = 15f
        private const val MAP_BEARING = 0f
        private const val RESULT_INTENT_DISTRICT_RECOMMENDATION = "district_recommendation_address"
        private const val EXTRA_ADDRESS_MODEL = "EXTRA_ADDRESS_MODEL"
        const val EXTRA_SAVE_DATA_UI_MODEL = "EXTRA_SAVE_DATA_UI_MODEL"

        fun newInstance(extra: Bundle): ShopEditAddressFragment {
            return ShopEditAddressFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_WAREHOUSE_DATA, extra.getParcelable(EXTRA_WAREHOUSE_DATA))
                    putDouble(EXTRA_LAT, extra.getDouble(EXTRA_LAT))
                    putDouble(EXTRA_LONG, extra.getDouble(EXTRA_LONG))
                }
            }
        }
    }
}
