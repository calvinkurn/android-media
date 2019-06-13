package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.TextView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.ResultCallback
import com.google.android.gms.location.LocationSettingsResult
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapsInitializer
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.base.BaseToaster
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetListener
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation.DistrictRecommendationBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapListener
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.add_address.AddAddressDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autofill.AutofillDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_recommendation.DistrictRecommendationItemUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.save_address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.form_add_new_address_data_item.*
import kotlinx.android.synthetic.main.form_add_new_address_mismatch_data_item.*
import kotlinx.android.synthetic.main.form_add_new_address_mismatch_solved_data_item.*
import kotlinx.android.synthetic.main.fragment_add_edit_new_address.*
import kotlinx.android.synthetic.main.header_add_new_address_data_item.*
import kotlinx.android.synthetic.main.header_add_new_address_mismatch_data_item.*
import kotlinx.android.synthetic.main.header_add_new_address_mismatch_solved_data_item.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressFragment: BaseDaggerFragment(), GoogleApiClient.ConnectionCallbacks,
                              GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
                              ResultCallback<LocationSettingsResult>, AddEditAddressListener,
                              DistrictRecommendationBottomSheetFragment.ActionListener,
                              AutocompleteBottomSheetListener,
                              PinpointMapListener,
                              ZipCodeChipsAdapter.ActionListener{

    private var googleMap: GoogleMap? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var token: Token? = null
    private var currentLat: Double? = 0.0
    private var currentLong: Double? = 0.0
    private var labelRumah: String? = "Rumah"
    private var isMismatch: Boolean = false
    private var isMismatchSolved: Boolean = false
    private var districtId: Int? = 0
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
    private lateinit var zipCodeChipsAdapter: ZipCodeChipsAdapter
    private lateinit var zipCodes: List<String>

    @Inject
    lateinit var presenter: AddEditAddressPresenter

    @Inject
    lateinit var autoCompletePresenter: AutocompleteBottomSheetPresenter

    @Inject
    lateinit var pinpointMapPresenter: PinpointMapPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        @Suppress("RECEIVER_NULLABILITY_MISMATCH_BASED_ON_JAVA_ANNOTATIONS")
        @JvmStatic
        fun newInstance(extra: Bundle): AddEditAddressFragment {
            return AddEditAddressFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(AddressConstants.EXTRA_IS_MISMATCH, extra.getBoolean(AddressConstants.EXTRA_IS_MISMATCH))
                    putParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL))
                    putParcelable(AddressConstants.KERO_TOKEN, extra.getParcelable(AddressConstants.KERO_TOKEN))
                    putBoolean(AddressConstants.EXTRA_IS_MISMATCH_SOLVED, extra.getBoolean(AddressConstants.EXTRA_IS_MISMATCH_SOLVED))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (arguments != null) {
            isMismatch = arguments?.getBoolean(AddressConstants.EXTRA_IS_MISMATCH)!!
            saveAddressDataModel = arguments?.getParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL)
            token = arguments?.getParcelable(AddressConstants.KERO_TOKEN)
            currentLat = saveAddressDataModel?.latitude?.toDouble()
            currentLong = saveAddressDataModel?.longitude?.toDouble()
            isMismatchSolved = arguments?.getBoolean(AddressConstants.EXTRA_IS_MISMATCH_SOLVED)!!
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_new_address, container, false)
    }

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        presenter.connectGoogleApi(this)
        arrangeLayout(isMismatch, isMismatchSolved, savedInstanceState)

        back_button_detail.setOnClickListener {
            if (!isMismatch && !isMismatchSolved) {
                map_view_detail?.onPause()
            } else {
                if (isMismatch && !isMismatchSolved) {
                    map_view_detail_mismatch?.onPause()
                } else if (!isMismatch && isMismatchSolved) {
                    map_view_detail_mismatch_solved?.onPause()
                }
            }

            presenter.disconnectGoogleApi()
            activity?.finish()
        }
    }

    @SuppressLint("SetTextI18n")
    private fun arrangeLayout(isMismatch: Boolean, isMismatchSolved: Boolean, savedInstanceState: Bundle?) {
        // !isMismatch && isMismatchSolved : header normal, form mismatch + show ll_detail_alamat_layout
        // isMismatch && !isMismatchSolved : header mismatch, form mismatch : hide ll_detail_alamat_layout

        if (!isMismatch && !isMismatchSolved) {
            ll_mismatch.visibility = View.GONE
            ll_mismatch_solved.visibility = View.GONE
            ll_normal.visibility = View.VISIBLE

            showNormalMapHeader(savedInstanceState)
            showNormalForm()

            btn_save_address.setOnClickListener {
                setSaveAddressModel()
                presenter.saveAddress(saveAddressDataModel)
            }

        } else {
            if (isMismatch && !isMismatchSolved) {
                ll_normal.visibility = View.GONE
                ll_mismatch_solved.visibility = View.GONE
                ll_mismatch.visibility = View.VISIBLE

                showMismatchMapHeader(savedInstanceState)
                showMismatchForm()

                btn_save_address_mismatch.setOnClickListener {
                    setSaveAddressModel()
                    presenter.saveAddress(saveAddressDataModel)
                }

            } else if (!isMismatch && isMismatchSolved) {
                ll_normal.visibility = View.GONE
                ll_mismatch.visibility = View.GONE
                ll_mismatch_solved.visibility = View.VISIBLE

                showMismatchSolvedMapHeader(savedInstanceState)
                showMismatchSolvedForm()

                btn_save_address_mismatch_solved.setOnClickListener {
                    setSaveAddressModel()
                    presenter.saveAddress(saveAddressDataModel)
                }
            }
        }
    }

    private fun showNormalMapHeader(savedInstanceState: Bundle?) {
        map_view_detail.onCreate(savedInstanceState)
        map_view_detail.getMapAsync(this)
        btn_map.apply {
            text = getString(R.string.change_pinpoint)
            setOnClickListener {
                presenter.changePinpoint(currentLat, currentLong, token, false, districtId, isMismatchSolved)
            }
        }
    }

    private fun showMismatchMapHeader(savedInstanceState: Bundle?) {
        map_view_detail_mismatch.onCreate(savedInstanceState)
        map_view_detail_mismatch.getMapAsync(this)
        btn_map_mismatch.apply {
            text = getString(R.string.define_pinpoint)
            setOnClickListener {
                if (et_kota_kecamatan_mismatch.text.isEmpty()) {
                    hideKeyboard()
                    showToastError(getString(R.string.choose_district_first))
                } else {
                    presenter.changePinpoint(currentLat, currentLong, token, true, districtId, isMismatchSolved)
                }
            }
        }
    }

    private fun showMismatchSolvedMapHeader(savedInstanceState: Bundle?) {
        map_view_detail_mismatch_solved.onCreate(savedInstanceState)
        map_view_detail_mismatch_solved.getMapAsync(this)
        btn_map_mismatch_solved.apply {
            text = getString(R.string.change_pinpoint)
            setOnClickListener {
                presenter.changePinpoint(currentLat, currentLong, token, false, districtId, isMismatchSolved)
            }
        }
    }

    private fun showNormalForm() {
        tv_address_based_on_pinpoint.text = "${this.saveAddressDataModel?.title}, ${this.saveAddressDataModel?.formattedAddress}"
        et_detail_address.setText(saveAddressDataModel?.editDetailAddress)
        et_label_address.setText(labelRumah)
        et_receiver_name.setText(userSession.name)
        et_phone.setText(userSession.phoneNumber)
    }

    private fun showMismatchForm() {
        et_kota_kecamatan_mismatch.setOnClickListener {
            showDistrictRecommendationBottomSheet()
        }
        et_kode_pos_mismatch.setOnClickListener {
            showZipCodes()
        }
        et_label_address_mismatch.setText(labelRumah)
        et_receiver_name_mismatch.setText(userSession.name)
        et_phone_mismatch.setText(userSession.phoneNumber)
    }

    private fun showMismatchSolvedForm() {
        et_kota_kecamatan_mismatch_solved.setOnClickListener {
            showDistrictRecommendationBottomSheet()
        }
        et_kode_pos_mismatch_solved.setOnClickListener {
            showZipCodes()
        }
        et_kota_kecamatan_mismatch_solved.setText(this.saveAddressDataModel?.formattedAddress)
        et_alamat_mismatch_solved.setText(this.saveAddressDataModel?.title)
        et_detail_alamat_mismatch_solved.setText(this.saveAddressDataModel?.editDetailAddress)
        et_label_address_mismatch_solved.setText(labelRumah)
        et_receiver_name_mismatch_solved.setText(userSession.name)
        et_phone_mismatch_solved.setText(userSession.phoneNumber)
    }

    private fun showDistrictRecommendationBottomSheet() {
        val districtRecommendationBottomSheetFragment =
                DistrictRecommendationBottomSheetFragment.newInstance()
        districtRecommendationBottomSheetFragment.setActionListener(this)
        districtRecommendationBottomSheetFragment.show(fragmentManager, "")
    }

    private fun showZipCodes() {
        val zipCodeList = zipCodes
        val chipsLayoutManager = ChipsLayoutManager.newBuilder(view?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        val staticDimen8dp = view?.context?.resources?.getDimensionPixelOffset(R.dimen.dp_8)
        ViewCompat.setLayoutDirection(rv_kodepos_chips_mismatch, ViewCompat.LAYOUT_DIRECTION_LTR)
        val zipCodesChipsAdapter = ZipCodeChipsAdapter(context, this)
        zipCodesChipsAdapter.zipCodes = zipCodeList.toMutableList()

        if (isMismatch && !isMismatchSolved) {
            et_kode_pos_input_layout_mismatch.visibility = View.GONE
            rv_kodepos_chips_mismatch.visibility = View.VISIBLE
            rv_kodepos_chips_mismatch.apply {
                addItemDecoration(staticDimen8dp?.let { ChipsItemDecoration(staticDimen8dp) })
                layoutManager = chipsLayoutManager
                adapter = zipCodesChipsAdapter
            }

        } else if (!isMismatch && isMismatchSolved) {
            et_kode_pos_input_layout_mismatch_solved.visibility = View.GONE
            rv_kodepos_chips_mismatch_solved.visibility = View.VISIBLE
            rv_kodepos_chips_mismatch_solved.apply {
                addItemDecoration(staticDimen8dp?.let { ChipsItemDecoration(staticDimen8dp) })
                layoutManager = chipsLayoutManager
                adapter = zipCodesChipsAdapter
            }
        }
    }

    private fun setSaveAddressModel() {
        var detailAddress = ""
        if (!isMismatch && !isMismatchSolved) {
            detailAddress = et_detail_address.text.toString()

            saveAddressDataModel?.addressName = et_label_address.text.toString()
            saveAddressDataModel?.receiverName = et_receiver_name.text.toString()
            saveAddressDataModel?.phone = et_phone.text.toString()

            saveAddressDataModel?.address1 = "${saveAddressDataModel?.title} ${detailAddress}, ${saveAddressDataModel?.formattedAddress}"
            saveAddressDataModel?.address2 = "$currentLat,$currentLong"

        } else {
            if (isMismatch && !isMismatchSolved) {
                detailAddress = et_alamat_mismatch.text.toString()


                saveAddressDataModel?.addressName = et_label_address_mismatch.text.toString()
                saveAddressDataModel?.receiverName = et_receiver_name_mismatch.text.toString()
                saveAddressDataModel?.phone = et_phone_mismatch.text.toString()

                saveAddressDataModel?.address1 = "${detailAddress} ${saveAddressDataModel?.selectedDistrict}"
                saveAddressDataModel?.address2 = ""

            } else if (!isMismatch && isMismatchSolved) {
                detailAddress = et_detail_alamat_mismatch_solved.text.toString()

                saveAddressDataModel?.addressName = et_label_address_mismatch_solved.text.toString()
                saveAddressDataModel?.receiverName = et_receiver_name_mismatch_solved.text.toString()
                saveAddressDataModel?.phone = et_phone_mismatch_solved.text.toString()

                saveAddressDataModel?.address1 = "${saveAddressDataModel?.title} ${detailAddress}, ${saveAddressDataModel?.formattedAddress}"
                saveAddressDataModel?.address2 = "$currentLat,$currentLong"
            }
        }
    }

    override fun onSuccessAddAddress(addAddressDataUiModel: AddAddressDataUiModel, saveAddressDataModel: SaveAddressDataModel) {
        finishActivity(saveAddressDataModel)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        MapsInitializer.initialize(activity!!)
        moveMap(PinpointMapUtils.generateLatLng(currentLat, currentLong))
    }

    override fun moveMap(latLng: LatLng) {
        val cameraPosition = CameraPosition.Builder()
                .target(latLng)
                .zoom(15f)
                .bearing(0f)
                .build()

        googleMap?.moveCamera(CameraUpdateFactory.newCameraPosition(cameraPosition))
    }

    override fun onResult(locationSettingsResult: LocationSettingsResult) {
    }

    override fun getScreenName(): String {
        return AddEditAddressFragment::class.java.simpleName
    }

    override fun initInjector() {
        activity?.run {
            DaggerAddNewAddressComponent.builder()
                    .baseAppComponent((application as BaseMainApplication).baseAppComponent)
                    .addNewAddressModule(AddNewAddressModule())
                    .build()
                    .inject(this@AddEditAddressFragment)
            presenter.attachView(this@AddEditAddressFragment)
            autoCompletePresenter.attachView(this@AddEditAddressFragment)
            pinpointMapPresenter.attachView(this@AddEditAddressFragment)
        }
    }

    override fun onResume() {
        if (!this.isMismatch && !this.isMismatchSolved) {
            map_view_detail?.onResume()
        } else {
            if (this.isMismatch && !this.isMismatchSolved) {
                map_view_detail_mismatch?.onResume()
            } else if (!this.isMismatch && this.isMismatchSolved) {
                map_view_detail_mismatch_solved?.onResume()
            }
        }

        super.onResume()
    }

    override fun onPause() {
        if (!this.isMismatch && !this.isMismatchSolved) {
            map_view_detail?.onPause()
        } else {
            if (this.isMismatch && !this.isMismatchSolved) {
                map_view_detail_mismatch?.onPause()
            } else if (!this.isMismatch && this.isMismatchSolved) {
                map_view_detail_mismatch_solved?.onPause()
            }
        }

        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.disconnectGoogleApi()
    }

    override fun onDestroy() {
        if (!this.isMismatch && !this.isMismatchSolved) {
            map_view_detail?.onDestroy()
        } else {
            if (this.isMismatch && !this.isMismatchSolved) {
                map_view_detail_mismatch?.onDestroy()
            } else if (!this.isMismatch && this.isMismatchSolved) {
                map_view_detail_mismatch_solved?.onDestroy()
            }
        }

        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        if (!this.isMismatch && !this.isMismatchSolved) {
            map_view_detail?.onLowMemory()
        } else {
            if (this.isMismatch && !this.isMismatchSolved) {
                map_view_detail_mismatch?.onLowMemory()
            } else if (!this.isMismatch && this.isMismatchSolved) {
                map_view_detail_mismatch_solved?.onLowMemory()
            }
        }
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel) {
        val intent = activity?.intent
        intent?.putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun onGetDistrict(districtRecommendationItemUiModel: DistrictRecommendationItemUiModel) {
        val provinceName = districtRecommendationItemUiModel.provinceName
        val cityName = districtRecommendationItemUiModel.cityName
        val districtName = districtRecommendationItemUiModel.districtName
        val districtSelected = "$provinceName, $cityName, $districtName"
        zipCodes = districtRecommendationItemUiModel.zipCodes

        et_kota_kecamatan_mismatch.setText(districtSelected)
        saveAddressDataModel?.selectedDistrict = districtSelected
        saveAddressDataModel?.cityId = districtRecommendationItemUiModel.cityId
        saveAddressDataModel?.provinceId = districtRecommendationItemUiModel.provinceId
        saveAddressDataModel?.districtId = districtRecommendationItemUiModel.districtId
        saveAddressDataModel?.latitude = ""
        saveAddressDataModel?.longitude = ""
        autoCompletePresenter.getAutocomplete(districtName)
        this.districtId = districtRecommendationItemUiModel.districtId
    }

    override fun hideListPointOfInterest() {
    }

    override fun onSuccessGetAutocompleteGeocode(dataUiModel: AutocompleteGeocodeDataUiModel) {
    }

    override fun onSuccessGetAutocomplete(dataUiModel: AutocompleteDataUiModel) {
        pinpointMapPresenter.getDistrict(dataUiModel.listPredictions[0].placeId)
    }

    override fun onSuccessPlaceGetDistrict(getDistrictDataUiModel: GetDistrictDataUiModel) {
        currentLat = getDistrictDataUiModel.latitude.toDouble()
        currentLong = getDistrictDataUiModel.longitude.toDouble()
        moveMap(PinpointMapUtils.generateLatLng(currentLat, currentLong))
    }

    override fun onSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
    }

    override fun showFailedDialog() {
    }

    override fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean) {
    }


    private fun showToastError(message: String) {
        var msg = message
        if (view != null && activity != null) {
            if (message.isEmpty()) {
                msg = getString(R.string.default_request_error_unknown)
            }
            val snackbar = Snackbar.make(view!!, msg, BaseToaster.LENGTH_SHORT)
            val snackbarTextView = snackbar.view.findViewById<TextView>(android.support.design.R.id.snackbar_text)
            val snackbarActionButton = snackbar.view.findViewById<Button>(android.support.design.R.id.snackbar_action)
            snackbar.view.background = ContextCompat.getDrawable(view!!.context, com.tokopedia.design.R.drawable.bg_snackbar_error)
            snackbarTextView.setTextColor(ContextCompat.getColor(view!!.context, R.color.font_black_secondary_54))
            snackbarActionButton.setTextColor(ContextCompat.getColor(view!!.context, R.color.font_black_primary_70))
            snackbarTextView.maxLines = 5
            snackbar.setAction(getString(R.string.label_action_snackbar_close)) { }.show()
        }
    }

    override fun onSuccessGetDistrictBoundary(districtBoundaryGeometryUiModel: DistrictBoundaryGeometryUiModel) {
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE);
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0);
    }

    override fun onZipCodeClicked(zipCode: String) {
        if (this.isMismatch && !this.isMismatchSolved) {
            rv_kodepos_chips_mismatch.visibility = View.GONE
            et_kode_pos_input_layout_mismatch.visibility = View.VISIBLE
            et_kode_pos_mismatch.setText(zipCode)

        } else if (!this.isMismatch && this.isMismatchSolved) {
            rv_kodepos_chips_mismatch_solved.visibility = View.GONE
            et_kode_pos_input_layout_mismatch_solved.visibility = View.VISIBLE
            et_kode_pos_mismatch_solved.setText(zipCode)
        }
        saveAddressDataModel?.postalCode = zipCode
    }
}