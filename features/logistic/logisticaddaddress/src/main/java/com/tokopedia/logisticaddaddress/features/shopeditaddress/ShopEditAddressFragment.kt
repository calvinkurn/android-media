package com.tokopedia.logisticaddaddress.features.shopeditaddress

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticCommon.data.entity.shoplocation.Warehouse
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.shopeditaddress.ShopEditAddressComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetFragment
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import timber.log.Timber
import java.util.*
import javax.inject.Inject

class ShopEditAddressFragment : BaseDaggerFragment(), OnMapReadyCallback,
        DiscomBottomSheetFragment.ActionListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ShopEditAddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(ShopEditAddressViewModel::class.java)
    }

    private var warehouseModel: Warehouse? = null
    private val zipCodes: List<String> = ArrayList()
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0

    private var etShopLocationWrapper: TextInputLayout? = null
    private var etShopLocation: TextInputEditText? = null
    private var etKotaKecamatanWrapper: TextInputLayout? = null
    private var etKotaKecamatan: TextInputEditText? = null
    private var etZipCodeWrapper: TextInputLayout? = null
    private var etZipCode: TextInputEditText? = null
    private var etShopDetailWrapper: TextInputLayout? = null
    private var etShopDetail: TextInputEditText? = null
    private var tvPinpointText: Typography? = null

    private var mapView: MapView? = null
    private var btnOpenMap: UnifyButton? = null

    private var getSavedInstanceState: Bundle? = null
    private var googleMap: GoogleMap? = null
    private var isLogisticLabel: Boolean = true

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ShopEditAddressComponent::class.java).inject(this)
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
        return inflater.inflate(R.layout.fragment_shop_edit_addres, container, false)
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
        /*if (resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(EXTRA_WAREHOUSE_DATA)) {
                    warehouseModel = data.getParcelableExtra(EXTRA_WAREHOUSE_DATA)
                    warehouseModel?.let {
                        currentLat = it.latLon.substringBefore(",").toDouble()
                        currentLong = it.latLon.substringAfter(",").toDouble()
                    }
                }
            }
        }*/
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

    override fun onDestroy() {
        mapView?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView?.onLowMemory()
    }

    private fun initViews() {
        etShopLocationWrapper = view?.findViewById(R.id.et_nama_lokasi_shop_wrapper)
        etShopLocation = view?.findViewById(R.id.et_nama_lokasi_shop)
        etKotaKecamatanWrapper = view?.findViewById(R.id.et_kota_kecamatan_shop_wrapper)
        etKotaKecamatan = view?.findViewById(R.id.et_kota_kecamatan_shop)
        etZipCodeWrapper = view?.findViewById(R.id.et_kode_pos_shop_wrapper)
        etZipCode = view?.findViewById(R.id.et_kode_pos_shop)
        etShopDetailWrapper = view?.findViewById(R.id.et_detail_alamat_shop_wrapper)
        etShopDetail = view?.findViewById(R.id.et_detail_alamat_shop)
        tvPinpointText = view?.findViewById(R.id.tv_pinpoint_text)

        mapView = view?.findViewById(R.id.map_view_detail)
        btnOpenMap = view?.findViewById(R.id.btn_open_map)
    }

    private fun initViewModel() {
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
                }
                is Fail -> Timber.d(it.throwable)
            }
        })
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

        tvPinpointText?.text = getString(R.string.tv_pinpoint_desc)

    }

    private fun setViewListener() {
        /*here text wrapper dsb*/
        etKotaKecamatan?.apply {
            addTextChangedListener(etKotaKecamatanWrapper?.let { setWrapperWatcher(it) })
            setOnClickListener {
                showDistrictRecommendationBottomSheet()
            }
        }

        etZipCode?.apply {
            setOnClickListener {

            }
        }
    }

    private fun showDistrictRecommendationBottomSheet() {
        val districtRecommendationBottomSheetFragment =
                DiscomBottomSheetFragment.newInstance(isLogisticLabel)
        districtRecommendationBottomSheetFragment.setActionListener(this)
        fragmentManager?.run {
            districtRecommendationBottomSheetFragment.show(this, "")
        }
    }

    private fun showZipCodes() {

    }

    override fun onGetDistrict(districtAddress: Address) {
        /*see AddEditAddressFragment*/
        warehouseModel?.cityId = districtAddress.cityId
        warehouseModel?.districtId = districtAddress.districtId
        warehouseModel?.zipCodes = districtAddress.zipCodes
        viewModel.getAutoCompleteList(districtAddress.districtName)
    }

    private fun setWrapperWatcher(wrapper: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
                //no-op
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
                if (text.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }
        }
    }

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.isErrorEnabled = false
        } else {
            wrapper.isErrorEnabled = true
            wrapper.hint = ""
            wrapper.error = s
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

    companion object {
        const val EXTRA_WAREHOUSE_DATA = "WAREHOUSE_DATA"
        const val EXTRA_LAT = "EXTRA_LAT"
        const val EXTRA_LONG = "EXTRA_LONG"

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