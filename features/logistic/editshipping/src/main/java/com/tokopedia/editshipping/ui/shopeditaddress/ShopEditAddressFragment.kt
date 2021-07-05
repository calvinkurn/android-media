package com.tokopedia.editshipping.ui.shopeditaddress

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.editshipping.R
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
import com.tokopedia.editshipping.util.ShopEditAddressUtils
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
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

    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var etShopLocationWrapper: TextInputLayout? = null
    private var etShopLocation: TextInputEditText? = null
    private var etKotaKecamatanWrapper: TextInputLayout? = null
    private var etKotaKecamatan: TextInputEditText? = null
    private var etZipCodeWrapper: TextInputLayout? = null
    private var etZipCode: AutoCompleteTextView? = null
    private var etShopDetailWrapper: TextInputLayout? = null
    private var etShopDetail: TextInputEditText? = null
    private var tvPinpointText: Typography? = null
    private var btnSave: UnifyButton? = null
    private var helperShopDetail: Typography? = null
    private var txtShopLocationWatcher: Typography? = null

    private var mapView: MapView? = null
    private var btnOpenMap: UnifyButton? = null

    private var getSavedInstanceState: Bundle? = null
    private var googleMap: GoogleMap? = null
    private var validate: Boolean = true
    private var uncoveredCourierFlag: Boolean = false

    override fun getScreenName(): String = ""

    override fun initInjector() {
        DaggerShopEditAddressComponent.builder().baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_edit_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            getSavedInstanceState = savedInstanceState
        }
        initViews()
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
                    val address = data?.getParcelableExtra<DistrictRecommendationAddress>(RESULT_INTENT_DISTRICT_RECOMMENDATION)
                    etKotaKecamatan?.setText(address?.districtName + ", " + address?.cityName)
                    etZipCode?.setText("")

                    if (address?.zipCodes != null) {
                        zipCodes = ArrayList(address.zipCodes)
                        initZipCode()
                    }

                    address?.let {
                        viewModel.getAutoCompleteList(it.districtName)
                        warehouseModel?.districtId = it.districtId
                    }
                }

                OPEN_MAP_REQUEST_CODE -> {
                   val addressModel = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_ADDRESS_MODEL)
                    addressModel?.let {
                        warehouseModel?.districtId = it.districtId
                        detailAddressHelper = it.formattedAddress
                        adjustMap(it.latitude.toDouble(), it.longitude.toDouble())
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
                zipCodes)
        }

        etZipCode?.setAdapter(zipCodeAdapter)
    }

    override fun onResume() {
        super.onResume()
        mapView?.onResume()
    }

    override fun onStart() {
        super.onStart()
        mapView?.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView?.onStop()
    }

    override fun onPause() {
        mapView?.onPause()
        super.onPause()
    }

    override fun onDestroyView() {
        mapView?.onDestroy()
        mapView = null
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    private fun initViews() {
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
        etShopLocationWrapper = view?.findViewById(R.id.et_nama_lokasi_shop_wrapper)
        etShopLocation = view?.findViewById(R.id.et_nama_lokasi_shop)
        etKotaKecamatanWrapper = view?.findViewById(R.id.et_kota_kecamatan_shop_wrapper)
        etKotaKecamatan = view?.findViewById(R.id.et_kota_kecamatan_shop)
        etZipCodeWrapper = view?.findViewById(R.id.et_kode_pos_shop_wrapper)
        etZipCode = view?.findViewById(R.id.et_kode_pos_shop)
        etShopDetailWrapper = view?.findViewById(R.id.et_detail_alamat_shop_wrapper)
        etShopDetail = view?.findViewById(R.id.et_detail_alamat_shop)
        tvPinpointText = view?.findViewById(R.id.tv_pinpoint_text)
        btnSave = view?.findViewById(R.id.btn_save_warehouse)
        helperShopDetail = view?.findViewById(R.id.tv_detail_alamat_helper)
        txtShopLocationWatcher = view?.findViewById(R.id.tv_nama_lokasi_watcher)

        mapView = view?.findViewById(R.id.map_view_detail)
        btnOpenMap = view?.findViewById(R.id.btn_open_map)
    }

    private fun initViewModel() {
        viewModel.zipCodeList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    zipCodes = ArrayList(it.data.district[0].zipCode)
                    initZipCode()
                    if (zipCodes.isEmpty()) {
                        etZipCode?.apply {
                            isFocusableInTouchMode = true
                            isFocusable = true
                            setOnClickListener(null)
                        }
                    }
                }
                is Fail -> zipCodes = arrayListOf()
            }
        })

        viewModel.autoCompleteList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> viewModel.getDistrictLocation(it.data.data.first().placeId)
                is Fail -> Timber.d(it.throwable)
            }
        })

        viewModel.districtLocation.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val lat = it.data.latitude.toDouble()
                    val long = it.data.longitude.toDouble()
                    adjustMap(lat, long)
                    detailAddressHelper = it.data.formattedAddress
                    val addressDetailUser = etShopDetail?.text.toString()
                    checkValidateAddressDetail(detailAddressHelper, addressDetailUser)
                }
                is Fail -> Timber.d(it.throwable)
            }
        })

        viewModel.districtGeocode.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> detailAddressHelper = it.data.data.formattedAddress
                is Fail -> detailAddressHelper = ""
            }
        })

        viewModel.saveEditShop.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopEditAddressState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    view?.let { view -> Toaster.build(view, getString(R.string.save_edit_shop_success), Toaster.LENGTH_SHORT, type = Toaster.TYPE_NORMAL).show() }
                    if (!uncoveredCourierFlag) {
                        activity?.setResult(Activity.RESULT_OK)

                    } else {
                        startActivity(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_SHIPPING))
                    }
                    activity?.finish()
                }
                is ShopEditAddressState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    view?.let { view -> Toaster.build(view, DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show() }
                }

                else -> swipeRefreshLayout?.isRefreshing = true
            }
        })

        viewModel.checkCouriers.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ShopEditAddressState.Success -> {
                    checkCouriersCoverage(it.data.data.isCovered)
                }

                is ShopEditAddressState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    view?.let { view -> Toaster.build(view, DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show() }
                }

                else -> swipeRefreshLayout?.isRefreshing = true
            }
        })

    }

    private fun checkValidateAddressDetail(addressHelper: String, userAddress: String) {
        val normalizeAddressHelper = ShopEditAddressUtils.normalize(addressHelper)
        val normalizeUserAddress = ShopEditAddressUtils.normalize(userAddress)
        if (ShopEditAddressUtils.validateAddressSimilarity(normalizeAddressHelper, normalizeUserAddress)) {
            validate = true
            helperShopDetail?.text = ""
        } else {
            validate = false
            helperShopDetail?.text = getString(R.string.detail_alamat_error_helper, detailAddressHelper)
        }
    }

    private fun checkCouriersCoverage(isCoverage: Boolean) {
        if (isCoverage) {
            val latLong = "$currentLat,$currentLong"
            warehouseModel?.let {
                viewModel.saveEditShopLocation(userSession.shopId.toLong(), it.warehouseId, etShopLocation?.text.toString(),
                        it.districtId, latLong, userSession.email, etShopDetail?.text.toString(),
                        etZipCode?.text.toString(), userSession.phoneNumber)
            }
        } else {
            swipeRefreshLayout?.isRefreshing = false
            showDialog()
        }
    }

    private fun showDialog() {
        val dialog = context?.let { DialogUnify(it, DialogUnify.VERTICAL_ACTION, DialogUnify.NO_IMAGE) }
        dialog?.apply {
            setTitle(getString(R.string.title_save_dialog))
            setDescription(getString(R.string.desc_save_dialog))
            setPrimaryCTAText(getString(R.string.primary_button_save_dialog))
            setPrimaryCTAClickListener {
                uncoveredCourierFlag = true
                val latLong = "$currentLat,$currentLong"
                warehouseModel?.let {
                    viewModel.saveEditShopLocation(userSession.shopId.toLong(), it.warehouseId, etShopLocation?.text.toString(),
                            it.districtId, latLong, userSession.email, etShopDetail?.text.toString(),
                            etZipCode?.text.toString(), userSession.phoneNumber)
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
        mapView?.onCreate(getSavedInstanceState)
        mapView?.getMapAsync(this)
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
        etShopLocation?.setText(warehouseModel?.warehouseName)
        etKotaKecamatan?.setText(warehouseModel?.districtName)
        etZipCode?.setText(warehouseModel?.postalCode)
        etShopDetail?.setText(warehouseModel?.addressDetail)

        tvPinpointText?.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_desc)).spannedString }

        btnOpenMap?.setOnClickListener {
            goToPinpointActivity(currentLat, currentLong, warehouseModel)
        }

        btnSave?.setOnClickListener {
            warehouseModel?.let { it ->
                if(validate) {
                    viewModel.checkCouriersAvailability(userSession.shopId.toLong(), it.districtId)
                }
            }
        }

        viewModel.getZipCode(warehouseModel?.districtId.toString())

        if (warehouseModel?.latLon?.isNotEmpty() == true) {
            viewModel.getDistrictGeocode(warehouseModel?.latLon)
        } else viewModel.getDistrictGeocode("$DEFAULT_LAT,$DEFAULT_LONG")

    }

    private fun setViewListener() {
        etShopLocation?.apply {
            addTextChangedListener(setShopLocationWatcher())
        }

        etKotaKecamatan?.apply {
            setOnClickListener {
                val intent = RouteManager.getIntent(activity, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS)
                startActivityForResult(intent, GET_DISTRICT_RECCOMENDATION_REQUEST_CODE)
            }
        }

        etZipCode?.apply {
            setOnTouchListener(View.OnTouchListener { _, _ ->
                if (etZipCode?.isPopupShowing == false) {
                    etZipCode?.showDropDown()
                }
                false
            })
        }

        etShopDetail?.apply {
            addTextChangedListener(setAlamatWatcher())
        }
    }

    private fun setShopLocationWatcher(): TextWatcher {
        return object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no-op
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val strLength = s.toString().length
                val info = "$strLength/25"
                txtShopLocationWatcher?.text = info
            }

            override fun afterTextChanged(s: Editable?) {
                //no-op
            }

        }
    }

    private fun setAlamatWatcher(): TextWatcher {
        return object: TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                //no-op
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    val strLength = s.toString().length
                    when {
                        strLength < 20 -> {
                            validate = false
                            helperShopDetail?.text = getString(R.string.helper_shop_detail)
                        }
                        else -> {
                            checkValidateAddressDetail(s.toString(), detailAddressHelper)
                        }
                    }
                }
            }

            override fun afterTextChanged(s: Editable?) {
                //no-op
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
                .zoom(15f)
                .bearing(0f)
                .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    private fun goToPinpointActivity(lat: Double?, long: Double?, warehouseDataModel: Warehouse?) {
        val intent = RouteManager.getIntent(
                activity, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
        intent.putExtra(EXTRA_IS_FULL_FLOW, false)
        intent.putExtra(EXTRA_LAT, lat)
        intent.putExtra(EXTRA_LONG, long)
        intent.putExtra(EXTRA_WAREHOUSE_DATA, warehouseDataModel)
        intent.putExtra(EXTRA_IS_EDIT_WAREHOUSE, true)
        startActivityForResult(intent, OPEN_MAP_REQUEST_CODE)
    }

    companion object {
        private const val GET_DISTRICT_RECCOMENDATION_REQUEST_CODE = 100
        private const val OPEN_MAP_REQUEST_CODE = 200
        private const val RESULT_INTENT_DISTRICT_RECOMMENDATION = "district_recommendation_address"
        private const val EXTRA_ADDRESS_MODEL = "EXTRA_ADDRESS_MODEL";
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