package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.content.ContextCompat
import android.support.v4.view.ViewCompat
import android.text.Editable
import android.text.TextWatcher
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
import com.tokopedia.design.text.TkpdHintTextInputLayout
import com.tokopedia.logisticaddaddress.AddressConstants
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetListener
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.district_recommendation.DistrictRecommendationBottomSheetFragment
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapListener
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapPresenter
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
import kotlinx.android.synthetic.main.form_add_new_address_default_item.*
import kotlinx.android.synthetic.main.form_add_new_address_mismatch_data_item.*
import kotlinx.android.synthetic.main.fragment_add_edit_new_address.*
import kotlinx.android.synthetic.main.header_add_new_address_data_item.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressFragment : BaseDaggerFragment(), GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback,
        ResultCallback<LocationSettingsResult>, AddEditAddressListener,
        DistrictRecommendationBottomSheetFragment.ActionListener,
        AutocompleteBottomSheetListener,
        PinpointMapListener,
        ZipCodeChipsAdapter.ActionListener, IOnBackPressed,
        LabelAlamatChipsAdapter.ActionListener {

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
    private val EXTRA_DETAIL_ADDRESS_LATEST = "EXTRA_DETAIL_ADDRESS_LATEST"
    private lateinit var zipCodeChipsAdapter: ZipCodeChipsAdapter
    private lateinit var chipsLayoutManager: ChipsLayoutManager
    private lateinit var labelAlamatChipsLayoutManager: ChipsLayoutManager
    private var staticDimen8dp: Int? = 0
    private lateinit var labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    private val FINISH_PINPOINT_FLAG = 8888
    private var getView: View? = null
    private var getSavedInstanceState: Bundle? = null
    private var labelAlamatList: Array<String> = emptyArray()

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
        getView = view
        savedInstanceState?.let {
            getSavedInstanceState = savedInstanceState
        }
        createFragment()
    }

    private fun createFragment() {
        prepareMap()
        prepareLayout()
        setViewListener()
    }

    private fun prepareMap() {
        presenter.connectGoogleApi(this)
        map_view_detail.onCreate(getSavedInstanceState)
        map_view_detail.getMapAsync(this)
    }

    private fun prepareLayout() {
        zipCodeChipsAdapter = ZipCodeChipsAdapter(context, this)
        labelAlamatChipsAdapter = LabelAlamatChipsAdapter(context, this)
        chipsLayoutManager = ChipsLayoutManager.newBuilder(getView?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .setScrollingEnabled(true)
                .build()
        labelAlamatChipsLayoutManager = ChipsLayoutManager.newBuilder(getView?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        staticDimen8dp = context?.resources?.getDimensionPixelOffset(R.dimen.dp_8)

        arrangeLayout(isMismatch, isMismatchSolved)

        et_label_address.setText(labelRumah)
        et_receiver_name.setText(userSession.name)
        et_phone.setText(userSession.phoneNumber)
    }

    private fun setViewListener() {
        btn_save_address.setOnClickListener {
            val errorField = "Field = "
            resetErrorFormDefault()
            if (!isMismatch && !isMismatchSolved) {
                resetErrorForm()
                if (validateForm(errorField)) doSaveAddress()
            } else {
                resetErrorFormMismatch()
                if (validateFormMismatch(errorField)) doSaveAddress()
            }
        }

        back_button_detail.setOnClickListener {
            map_view_detail?.onPause()

            presenter.disconnectGoogleApi()

            if (!isMismatch && !isMismatchSolved) {
                AddNewAddressAnalytics.eventClickBackArrowOnPositivePageChangeAddressPositive()
            }

            activity?.finish()
        }

        if (!isMismatch && !isMismatchSolved) {
            et_detail_address.apply {
                addTextChangedListener(setWrapperWatcher(et_detail_address_wrapper))
                setOnClickListener { AddNewAddressAnalytics.eventClickFieldDetailAlamatChangeAddressPositive() }
            }

            setOnTouchLabelAddress("positive")

            et_receiver_name.setOnClickListener {
                AddNewAddressAnalytics.eventClickFieldNamaPenerimaChangeAddressPositive()
            }
            et_phone.setOnClickListener {
                AddNewAddressAnalytics.eventClickFieldNoPonselChangeAddressPositive()
            }

        } else {
            if (isMismatch) {
                // et_detail_alamat_mismatch.addTextChangedListener(setWrapperWatcher(et_detail_alamat_mismatch_wrapper))

                et_kota_kecamatan_mismatch.apply {
                    addTextChangedListener(setWrapperWatcher(et_kota_kecamatan_mismatch_wrapper))
                    setOnClickListener {
                        showDistrictRecommendationBottomSheet()
                        AddNewAddressAnalytics.eventClickFieldKotaKecamatanChangeAddressNegative()
                    }
                }
            }

            et_alamat_mismatch.apply {
                addTextChangedListener(setWrapperWatcher(et_alamat_mismatch_wrapper))
                addTextChangedListener(setAlamatWatcher())
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressUtils.scrollUpLayout(scroll_view_layout)
                    }
                }
                AddNewAddressAnalytics.eventClickFieldAlamatChangeAddressNegative()
            }

            et_kode_pos_mismatch.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        // scrollUpLayout()
                        AddNewAddressUtils.scrollUpLayout(scroll_view_layout)
                        eventShowZipCodes()
                    }
                }
                setOnClickListener {
                    eventShowZipCodes()
                }
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                                   after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                               count: Int) {
                        if (s.isNotEmpty()) {
                            val input = "$s"
                            val zipCodesDisplay = mutableListOf<String>()

                            saveAddressDataModel?.zipCodes?.forEach {
                                if (it.contains(input, ignoreCase = true)) {
                                    zipCodesDisplay.add(it)
                                }
                            }
                            zipCodeChipsAdapter.zipCodes = zipCodesDisplay
                            zipCodeChipsAdapter.notifyDataSetChanged()
                        }
                    }

                    override fun afterTextChanged(s: Editable) {
                    }
                })
                //addTextChangedListener(setWrapperWatcher(et_kode_pos_mismatch_wrapper))
            }

            setOnTouchLabelAddress("negative")

            et_receiver_name.setOnClickListener {
                AddNewAddressAnalytics.eventClickFieldNamaPenerimaChangeAddressNegative()
            }

            et_phone.setOnClickListener {
                AddNewAddressAnalytics.eventClickFieldNoPonselChangeAddressNegative()
            }
        }

        et_label_address.addTextChangedListener(setWrapperWatcher(et_label_address_wrapper))
        et_receiver_name.addTextChangedListener(setWrapperWatcher(et_receiver_name_wrapper))
        et_phone.addTextChangedListener(setWrapperWatcher(et_phone_wrapper))
    }

    private fun setOnTouchLabelAddress(type: String) {
        et_label_address.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    AddNewAddressUtils.scrollUpLayout(scroll_view_layout)
                    eventShowListLabelAlamat(type)
                }
            }
            setOnClickListener {
                AddNewAddressUtils.scrollUpLayout(scroll_view_layout)
                eventShowListLabelAlamat(type)
            }
            addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                               after: Int) {
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                           count: Int) {
                    if (s.isNotEmpty()) {
                        val input = "$s"
                        val labelAlamatDisplay = mutableListOf<String>()

                        labelAlamatList.forEach {
                            if (it.contains(input, ignoreCase = true)) {
                                labelAlamatDisplay.add(it)
                            }
                        }
                        labelAlamatChipsAdapter.labelAlamatList = labelAlamatDisplay
                        labelAlamatChipsAdapter.notifyDataSetChanged()
                    }
                }

                override fun afterTextChanged(s: Editable) {
                }
            })
        }
    }

    private fun eventShowListLabelAlamat(type: String) {
        // showLabelAlamatList()
        if (type.equals("positive", true)) {
            AddNewAddressAnalytics.eventClickFieldLabelAlamatChangeAddressPositive()
        } else {
            AddNewAddressAnalytics.eventClickFieldLabelAlamatChangeAddressNegative()
        }
    }

    private fun doSaveAddress() {
        setSaveAddressModel()
        presenter.saveAddress(saveAddressDataModel)

        if (isMismatch) AddNewAddressAnalytics.eventClickButtonSimpanNegativeSuccess()
        else AddNewAddressAnalytics.eventClickButtonSimpanSuccess()
    }

    private fun validateForm(errorField: String): Boolean {
        var validated = true

        var field = errorField
        if (et_detail_address.text.isEmpty()) {
            validated = false
            setWrapperError(et_label_address_wrapper, getString(R.string.validate_detail_alamat))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "detail alamat"
        }

        if (!validateFormDefault(errorField)) validated = false
        return validated
    }

    private fun validateFormMismatch(errorField: String): Boolean {
        var validated = true

        var field = errorField
        if (et_kota_kecamatan_mismatch.text.isEmpty()) {
            validated = false
            setWrapperError(et_kota_kecamatan_mismatch_wrapper, getString(R.string.validate_kota_kecamatan))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "kota kecamatan"
        }
        if (et_kode_pos_mismatch.text.isEmpty()) {
            validated = false
            setWrapperError(et_kode_pos_mismatch_wrapper, getString(R.string.validate_kode_pos))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "kode pos"
        }
        if (et_kode_pos_mismatch.text.length < 5) {
            validated = false
            setWrapperError(et_kode_pos_mismatch_wrapper, getString(R.string.validate_kode_pos_length))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "kode pos"
        }
        if (et_alamat_mismatch.text.isEmpty()) {
            validated = false
            setWrapperError(et_alamat_mismatch_wrapper, getString(R.string.validate_alamat))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "alamat"
        }

        /*if (isMismatchSolved) {
            if (et_detail_alamat_mismatch.text.isEmpty()) {
                validated = false
                setWrapperError(et_detail_alamat_mismatch_wrapper, getString(R.string.validate_detail_alamat))
                if (!isErrorFieldEmpty(field)) field += ", "
                field += "detail alamat"
            }
        }*/


        if (!validateFormDefault(errorField)) validated = false
        return validated
    }

    private fun validateFormDefault(errorField: String): Boolean {
        var validated = true

        var field = errorField
        if (et_label_address.text.isEmpty()) {
            validated = false
            setWrapperError(et_label_address_wrapper, getString(R.string.validate_label_alamat))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "label alamat"

        }
        if (et_receiver_name.text.isEmpty()) {
            validated = false
            setWrapperError(et_receiver_name_wrapper, getString(R.string.validate_nama_penerima))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "nama penerima"

        }
        if (et_phone.text.isEmpty()) {
            validated = false
            setWrapperError(et_phone_wrapper, getString(R.string.validate_no_ponsel))
            if (!isErrorFieldEmpty(field)) field += ", "
            field += "no ponsel"
        }

        if (!validated) {
            if (isMismatch) AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(field)
            else AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(field)
        }
        return validated
    }

    private fun isErrorFieldEmpty(errorField: String): Boolean {
        var isEmpty = true
        if (!errorField.equals("Field = ", true)) isEmpty = false
        return isEmpty
    }

    private fun setWrapperError(wrapper: TkpdHintTextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.setErrorEnabled(false)
        } else {
            wrapper.setErrorEnabled(true)
            wrapper.setHint("")
            wrapper.error = s
        }
    }

    private fun resetErrorForm() {
        setWrapperError(et_label_address_wrapper, null)
    }

    private fun resetErrorFormMismatch() {
        setWrapperError(et_kota_kecamatan_mismatch_wrapper, null)
        setWrapperError(et_kode_pos_mismatch_wrapper, null)
        setWrapperError(et_alamat_mismatch_wrapper, null)
        // setWrapperError(et_detail_alamat_mismatch_wrapper, null)
    }

    private fun resetErrorFormDefault() {
        setWrapperError(et_label_address_wrapper, null)
        setWrapperError(et_receiver_name_wrapper, null)
        setWrapperError(et_phone_wrapper, null)
    }

    private fun setWrapperWatcher(wrapper: TkpdHintTextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

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

    private fun setAlamatWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    var countCharLeft: Int
                    var info = ""
                    when {
                        count < 5 -> {
                            countCharLeft = 5 - count
                            info = "$countCharLeft karakter lagi diperlukan"
                        }
                        count > 4 -> {
                            countCharLeft = 175 - count
                            info = "$countCharLeft karakter tersisa"

                        }
                        count == 175 -> info = ""
                    }
                    tv_alamat_info_mismatch.text = info
                }
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
    }

    private fun arrangeLayout(isMismatch: Boolean, isMismatchSolved: Boolean) {
        if (!isMismatch && !isMismatchSolved) {
            ll_mismatch.visibility = View.GONE
            ll_normal.visibility = View.VISIBLE

            setNormalMapHeader()
            setNormalForm()

        } else {
            ll_normal.visibility = View.GONE
            ll_mismatch.visibility = View.VISIBLE

            if (isMismatch) {
                setMismatchMapHeader()
                setMismatchForm()

            } else {
                setMismatchSolvedMapHeader()
                setMismatchSolvedForm()
            }
            setupRvKodePosChips()
            setupRvLabelAlamatChips()
        }
    }

    private fun eventShowZipCodes() {
        showZipCodes()
        AddNewAddressAnalytics.eventClickFieldKodePosChangeAddressNegative()
    }

    private fun setOnClickLabelAlamat() {
        et_label_address.setOnClickListener {
            // showLabelAlamatList()
        }
    }

    private fun setupRvKodePosChips() {
        rv_kodepos_chips_mismatch.apply {
            addItemDecoration(staticDimen8dp?.let { ChipsItemDecoration(it) })
            layoutManager = chipsLayoutManager
            adapter = zipCodeChipsAdapter
        }
    }

    private fun setupRvLabelAlamatChips() {
        rv_label_alamat_chips.apply {
            addItemDecoration(staticDimen8dp?.let { ChipsItemDecoration(it) })
            layoutManager = labelAlamatChipsLayoutManager
            adapter = labelAlamatChipsAdapter
        }
    }

    private fun setNormalMapHeader() {
        disable_map_layout.visibility = View.GONE
        icon_pointer.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_green_pointer))
        icon_pointer.colorFilter = null
        btn_map.apply {
            text = getString(R.string.change_pinpoint)
            val params = btn_map.layoutParams
            params.width = 450
            btn_map.layoutParams = params
            setOnClickListener {
                saveAddressDataModel?.editDetailAddress = et_detail_address.text.toString()
                goToPinpointActivity(currentLat, currentLong, false, token, false, districtId,
                        isMismatchSolved, isMismatch, saveAddressDataModel)
                AddNewAddressAnalytics.eventClickButtonUbahPinPointChangeAddressPositive()
            }
        }
    }

    private fun setMismatchMapHeader() {
        disable_map_layout.visibility = View.VISIBLE
        icon_pointer.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_green_pointer))
        context?.resources?.getColor(R.color.separator_color)?.let { icon_pointer.setColorFilter(it) }
        btn_map.apply {
            text = getString(R.string.define_pinpoint)
            val params = btn_map.layoutParams
            params.width = 550
            btn_map.layoutParams = params
            setOnClickListener {
                if (et_kota_kecamatan_mismatch.text.isEmpty()) {
                    hideKeyboard()
                    view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.choose_district_first), it1, it2) } }
                    AddNewAddressAnalytics.eventViewToasterPilihKotaDanKodePosTerlebihDahulu()
                } else {
                    goToPinpointActivity(currentLat, currentLong, false, token, true, districtId,
                            isMismatchSolved, isMismatch, saveAddressDataModel)
                    AddNewAddressAnalytics.eventClickButtonPilihLokasiIni()
                }
            }
        }
    }

    private fun setMismatchSolvedMapHeader() {
        disable_map_layout.visibility = View.GONE
        icon_pointer.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_green_pointer))
        icon_pointer.colorFilter = null
        btn_map.apply {
            text = getString(R.string.change_pinpoint)
            val params = btn_map.layoutParams
            params.width = 450
            btn_map.layoutParams = params
            setOnClickListener {
                saveAddressDataModel?.editDetailAddress = et_detail_alamat_mismatch.text.toString()
                goToPinpointActivity(currentLat, currentLong, false, token, true, districtId,
                        isMismatchSolved, isMismatch, saveAddressDataModel)
            }
        }
    }

    private fun setNormalForm() {
        tv_address_based_on_pinpoint.text = "${this.saveAddressDataModel?.title}, ${this.saveAddressDataModel?.formattedAddress}"
        et_detail_address.setText(saveAddressDataModel?.editDetailAddress)
    }

    private fun setMismatchForm() {
        ll_detail_alamat.visibility = View.GONE
    }

    private fun setMismatchSolvedForm() {
        ll_detail_alamat.visibility = View.VISIBLE
        et_kota_kecamatan_mismatch.setText(this.saveAddressDataModel?.formattedAddress)
        // et_alamat_mismatch.setText(this.saveAddressDataModel?.title)
        et_detail_alamat_mismatch.setText(this.saveAddressDataModel?.editDetailAddress)
        et_kode_pos_mismatch.setText(this.saveAddressDataModel?.postalCode)
    }

    private fun showDistrictRecommendationBottomSheet() {
        val districtRecommendationBottomSheetFragment =
                DistrictRecommendationBottomSheetFragment.newInstance()
        districtRecommendationBottomSheetFragment.setActionListener(this)
        districtRecommendationBottomSheetFragment.show(fragmentManager, "")
    }

    private fun showZipCodes() {
        ViewCompat.setLayoutDirection(rv_kodepos_chips_mismatch, ViewCompat.LAYOUT_DIRECTION_LTR)
        zipCodeChipsAdapter.zipCodes = saveAddressDataModel?.zipCodes!!.toMutableList()

        rv_kodepos_chips_mismatch.visibility = View.VISIBLE
    }

    private fun showLabelAlamatList() {
        val res: Resources = resources
        labelAlamatList = res.getStringArray(R.array.labelAlamatList)

        ViewCompat.setLayoutDirection(rv_label_alamat_chips, ViewCompat.LAYOUT_DIRECTION_LTR)
        labelAlamatChipsAdapter.labelAlamatList = labelAlamatList.toMutableList()

        rv_label_alamat_chips.visibility = View.VISIBLE
    }

    private fun setSaveAddressModel() {
        var detailAddress = ""
        if (!isMismatch && !isMismatchSolved) {
            detailAddress = et_detail_address.text.toString()

            saveAddressDataModel?.address1 = "${saveAddressDataModel?.title} ${detailAddress}, ${saveAddressDataModel?.formattedAddress}"
            saveAddressDataModel?.address2 = "$currentLat,$currentLong"

        } else {
            detailAddress = et_detail_alamat_mismatch.text.toString()
            if (isMismatch) {
                saveAddressDataModel?.address1 = "${detailAddress} ${saveAddressDataModel?.selectedDistrict}"
                saveAddressDataModel?.address2 = ""

            } else {
                saveAddressDataModel?.address1 = "${saveAddressDataModel?.title} ${detailAddress}, ${saveAddressDataModel?.formattedAddress}"
                saveAddressDataModel?.address2 = "$currentLat,$currentLong"
            }
        }

        saveAddressDataModel?.addressName = et_label_address.text.toString()
        saveAddressDataModel?.receiverName = et_receiver_name.text.toString()
        saveAddressDataModel?.phone = et_phone.text.toString()
    }

    override fun onSuccessAddAddress(addAddressDataUiModel: AddAddressDataUiModel, saveAddressDataModel: SaveAddressDataModel) {
        finishActivity(saveAddressDataModel)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        this.googleMap?.uiSettings?.setAllGesturesEnabled(false)
        activity?.let { MapsInitializer.initialize(activity) }
        moveMap(AddNewAddressUtils.generateLatLng(currentLat, currentLong))

    }

    private fun moveMap(latLng: LatLng) {
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
        map_view_detail?.onResume()
        super.onResume()
    }

    override fun onPause() {
        map_view_detail?.onPause()
        super.onPause()
    }

    override fun onStop() {
        super.onStop()
        presenter.disconnectGoogleApi()
    }

    override fun onDestroy() {
        map_view_detail?.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        map_view_detail?.onLowMemory()
        super.onLowMemory()
    }

    override fun onConnected(p0: Bundle?) {
    }

    override fun onConnectionSuspended(p0: Int) {
    }

    override fun onConnectionFailed(p0: ConnectionResult) {
    }

    private fun finishActivity(saveAddressDataModel: SaveAddressDataModel) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
            })
            finish()
        }
    }

    override fun onGetDistrict(districtRecommendationItemUiModel: DistrictRecommendationItemUiModel) {
        val provinceName = districtRecommendationItemUiModel.provinceName
        val cityName = districtRecommendationItemUiModel.cityName
        val districtName = districtRecommendationItemUiModel.districtName
        val districtSelected = "$provinceName, $cityName, $districtName"

        et_kota_kecamatan_mismatch.setText(districtSelected)
        saveAddressDataModel?.selectedDistrict = districtSelected
        saveAddressDataModel?.cityId = districtRecommendationItemUiModel.cityId
        saveAddressDataModel?.provinceId = districtRecommendationItemUiModel.provinceId
        saveAddressDataModel?.districtId = districtRecommendationItemUiModel.districtId
        saveAddressDataModel?.latitude = ""
        saveAddressDataModel?.longitude = ""
        saveAddressDataModel?.zipCodes = districtRecommendationItemUiModel.zipCodes
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
        moveMap(AddNewAddressUtils.generateLatLng(currentLat, currentLong))
    }

    override fun onSuccessAutofill(autofillDataUiModel: AutofillDataUiModel) {
    }

    override fun showFailedDialog() {
    }

    override fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean) {
    }

    override fun onSuccessGetDistrictBoundary(districtBoundaryGeometryUiModel: DistrictBoundaryGeometryUiModel) {
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE);
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0);
    }

    override fun onZipCodeClicked(zipCode: String) {
        rv_kodepos_chips_mismatch.visibility = View.GONE
        et_kode_pos_mismatch.apply {
            setText(zipCode)
            AddNewAddressAnalytics.eventClickFieldKodePosChangeAddressNegative()
        }
        saveAddressDataModel?.postalCode = zipCode
        AddNewAddressAnalytics.eventClickChipsKodePosChangeAddressNegative()
    }

    override fun showAutoComplete(lat: Double, long: Double) {
    }

    override fun onBackPressed(): Boolean {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_DETAIL_ADDRESS_LATEST, et_detail_address.text.toString())
            })
        }
        return false
    }

    override fun onLabelAlamatChipClicked(labelAlamat: String) {
        rv_label_alamat_chips.visibility = View.GONE
        et_label_address.setText(labelAlamat)
        if (!isMismatch && !isMismatchSolved) {
            AddNewAddressAnalytics.eventClickChipsLabelAlamatChangeAddressPositive()
        } else {
            AddNewAddressAnalytics.eventClickChipsLabelAlamatChangeAddressNegative()
        }
    }

    private fun goToPinpointActivity(lat: Double?, long: Double?, isShowAutocomplete: Boolean, token: Token?, isPolygon: Boolean, districtId: Int?,
                                     isMismatchSolved: Boolean, isMismatch: Boolean, saveAddressDataModel: SaveAddressDataModel?) {
        startActivityForResult(context?.let {
            PinpointMapActivity.newInstance(it, lat, long, isShowAutocomplete, token, isPolygon,
                    districtId, isMismatchSolved, isMismatch, saveAddressDataModel, true)
        }, FINISH_PINPOINT_FLAG)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == FINISH_PINPOINT_FLAG && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                if (data.hasExtra(AddressConstants.EXTRA_IS_MISMATCH)) {
                    isMismatch = data.getBooleanExtra(AddressConstants.EXTRA_IS_MISMATCH, false)
                }

                if (data.hasExtra(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL)) {
                    saveAddressDataModel = data.getParcelableExtra<SaveAddressDataModel>(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL)
                    saveAddressDataModel?.let {
                        currentLat = it.latitude.toDouble()
                        currentLong = it.longitude.toDouble()
                    }
                }

                if (data.hasExtra(AddressConstants.EXTRA_IS_MISMATCH_SOLVED)) {
                    isMismatchSolved = data.getBooleanExtra(AddressConstants.EXTRA_IS_MISMATCH_SOLVED, true)
                }
                createFragment()
            }
        }
    }

    override fun finishBackToAddEdit(isMismatch: Boolean, isMismatchSolved: Boolean) {
    }
}