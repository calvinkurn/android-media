package com.tokopedia.logisticaddaddress.features.shopeditaddress

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.shopeditaddress.ShopEditAddressComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetFragment
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography

class ShopEditAddressFragment : BaseDaggerFragment(), OnMapReadyCallback,
        DiscomBottomSheetFragment.ActionListener{

    private var etShopLocationWrapper: TextInputLayout? = null
    private var etShopLocation: TextInputLayout? = null
    private var etKotaKecamatanWrapper: TextInputLayout? = null
    private var etKotaKecamatan: TextInputLayout? = null
    private var etZipCodeWrapper: TextInputLayout? = null
    private var etZipCode: TextInputLayout? = null
    private var etShopDetailWrapper: TextInputLayout? = null
    private var etShopDetail: TextInputLayout? = null
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_shop_edit_addres, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        savedInstanceState?.let {
            getSavedInstanceState = savedInstanceState
        }
        initViews()
        prepareMap()
        prepareLayout()
        setViewListener()
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
//        moveMap(getLatLng(currentLat, currentLong))
    }

    private fun prepareLayout() {
        /*here isi semua edit text with data dr model*/
    }

    private fun setViewListener() {
        /*here text wrapper dsb*/
    }

    private fun showDistrictRecommendationBottomSheet() {
        val districtRecommendationBottomSheetFragment =
                DiscomBottomSheetFragment.newInstance(isLogisticLabel)
        districtRecommendationBottomSheetFragment.setActionListener(this)
        fragmentManager?.run {
            districtRecommendationBottomSheetFragment.show(this, "")
        }
    }

    override fun onGetDistrict(districtAddress: Address) {
        /*see AddEditAddressFragment*/
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}