package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.app.Activity
import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.core.view.ViewCompat
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
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetListener
import com.tokopedia.logisticaddaddress.features.addnewaddress.bottomsheets.autocomplete_geocode.AutocompleteBottomSheetPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapListener
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapPresenter
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutocompleteDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete_geocode.AutocompleteGeocodeDataUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.district_boundary.DistrictBoundaryGeometryUiModel
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.get_district.GetDistrictDataUiModel
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetFragment
import com.tokopedia.logisticaddaddress.utils.getLatLng
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.logisticdata.data.entity.response.Data
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
        DiscomBottomSheetFragment.ActionListener,
        AutocompleteBottomSheetListener,
        PinpointMapListener,
        ZipCodeChipsAdapter.ActionListener, IOnBackPressed,
        LabelAlamatChipsAdapter.ActionListener {

    private var googleMap: GoogleMap? = null
    private var saveAddressDataModel: SaveAddressDataModel? = null
    private var token: Token? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var labelRumah: String? = "Rumah"
    private var isMismatch: Boolean = false
    private var isMismatchSolved: Boolean = false
    private var isUnnamedRoad: Boolean = false
    private var isNullZipcode: Boolean = false
    private val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
    private val EXTRA_DETAIL_ADDRESS_LATEST = "EXTRA_DETAIL_ADDRESS_LATEST"
    private lateinit var zipCodeChipsAdapter: ZipCodeChipsAdapter
    private lateinit var chipsLayoutManager: ChipsLayoutManager
    private lateinit var labelAlamatChipsLayoutManager: ChipsLayoutManager
    private var staticDimen8dp: Int? = 0
    private lateinit var labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    private val FINISH_PINPOINT_FLAG = 8888
    private val MINIMUM_CHARACTER = 8
    private var getView: View? = null
    private var getSavedInstanceState: Bundle? = null
    private var labelAlamatList: Array<String> = emptyArray()
    private var isLatitudeNotEmpty: Boolean? = false
    private var isLongitudeNotEmpty: Boolean? = false

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
                    putBoolean(AddressConstants.EXTRA_IS_UNNAMED_ROAD, extra.getBoolean(AddressConstants.EXTRA_IS_UNNAMED_ROAD))
                    putBoolean(AddressConstants.EXTRA_IS_NULL_ZIPCODE, extra.getBoolean(AddressConstants.EXTRA_IS_NULL_ZIPCODE, false))
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isMismatch = it.getBoolean(AddressConstants.EXTRA_IS_MISMATCH)
            saveAddressDataModel = it.getParcelable(AddressConstants.EXTRA_SAVE_DATA_UI_MODEL)
            token = it.getParcelable(AddressConstants.KERO_TOKEN)
            isLatitudeNotEmpty = saveAddressDataModel?.latitude?.isNotEmpty()
            isLatitudeNotEmpty?.let {
                if (it) currentLat = saveAddressDataModel?.latitude?.toDouble() ?: 0.0
            }

            isLongitudeNotEmpty = saveAddressDataModel?.longitude?.isNotEmpty()
            isLongitudeNotEmpty?.let {
                if (it) currentLong = saveAddressDataModel?.longitude?.toDouble() ?: 0.0
            }

            isMismatchSolved = it.getBoolean(AddressConstants.EXTRA_IS_MISMATCH_SOLVED)
            isUnnamedRoad = it.getBoolean(AddressConstants.EXTRA_IS_UNNAMED_ROAD, false)
            isNullZipcode = it.getBoolean(AddressConstants.EXTRA_IS_NULL_ZIPCODE, false)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_new_address, container, false)
    }

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

        et_label_address.setText(labelRumah)
        et_receiver_name.setText(userSession.name)
        et_kode_pos_mismatch.setText(saveAddressDataModel?.postalCode ?: "")
        et_phone.setText(userSession.phoneNumber)

        if (!isMismatch && !isMismatchSolved) {
            et_detail_address.clearFocus()
            AddNewAddressUtils.hideKeyboard(et_detail_address, context)
        }
        et_label_address.clearFocus()
        et_receiver_name.clearFocus()
        et_phone.clearFocus()
    }

    private fun setViewListener() {
        btn_save_address.setOnClickListener {
            resetErrorFormDefault()
            if (!isMismatch && !isMismatchSolved) {
                resetErrorForm()
                if (validateForm()) doSaveAddress()
            } else {
                resetErrorFormMismatch()
                if (validateFormMismatch()) doSaveAddress()
            }
        }

        back_button_detail.setOnClickListener {
            map_view_detail?.onPause()

            presenter.disconnectGoogleApi()

            if (!isMismatch && !isMismatchSolved) {
                AddNewAddressAnalytics.eventClickBackArrowOnPositivePageChangeAddressPositive(eventLabel = LOGISTIC_LABEL)
            }

            activity?.finish()
        }

        if (!isMismatch && !isMismatchSolved) {
            et_detail_address.apply {
                addTextChangedListener(setWrapperWatcher(et_detail_address_wrapper))
                setOnClickListener { AddNewAddressAnalytics.eventClickFieldDetailAlamatChangeAddressPositive(eventLabel = LOGISTIC_LABEL) }
                addTextChangedListener(setDetailAlamatWatcher())

                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }
            }

            setOnTouchLabelAddress(ANA_POSITIVE)

            et_receiver_name.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldNamaPenerimaChangeAddressPositive(eventLabel = LOGISTIC_LABEL)
                    }
                }

                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }
            }

            et_phone.apply {
                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldNoPonselChangeAddressPositive(eventLabel = LOGISTIC_LABEL)
                    }
                }
            }

        } else {
            if (isMismatch) {
                et_kota_kecamatan_mismatch.apply {
                    addTextChangedListener(setWrapperWatcher(et_kota_kecamatan_mismatch_wrapper))
                    setOnClickListener {
                        showDistrictRecommendationBottomSheet()
                        AddNewAddressAnalytics.eventClickFieldKotaKecamatanChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
                    }
                }
            }

            et_alamat_mismatch.apply {
                addTextChangedListener(setWrapperWatcher(et_alamat_mismatch_wrapper))
                addTextChangedListener(setAlamatWatcher())
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldAlamatChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
                    }
                }
                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }
            }

            et_kode_pos_mismatch.setOnClickListener { eventShowZipCodes() }

            setOnTouchLabelAddress(ANA_NEGATIVE)

            et_receiver_name.apply {
                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldNamaPenerimaChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
                    }
                }
            }

            et_phone.apply {
                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldNoPonselChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
                    }
                }
            }
        }

        et_receiver_name.apply {
            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
            addTextChangedListener(setWrapperWatcher(et_receiver_name_wrapper))
        }

        et_phone.apply {
            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
            addTextChangedListener(setWrapperWatcher(et_phone_wrapper))
        }
    }

    private fun setOnTouchLabelAddress(type: String) {
        et_label_address.apply {
            setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    eventShowListLabelAlamat(type)
                } else {
                    rv_label_alamat_chips.visibility = View.GONE
                }
            }
            setOnClickListener {
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
            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
            addTextChangedListener(setWrapperWatcher(et_label_address_wrapper))
        }
    }

    private fun eventShowListLabelAlamat(type: String) {
        showLabelAlamatList()
        if (type.equals("positive", true)) {
            AddNewAddressAnalytics.eventClickFieldLabelAlamatChangeAddressPositive(eventLabel = LOGISTIC_LABEL)
        } else {
            AddNewAddressAnalytics.eventClickFieldLabelAlamatChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
        }
    }

    private fun doSaveAddress() {
        setSaveAddressModel()
        saveAddressDataModel?.let {
            if (isMismatch) {
                presenter.saveAddress(it, ANA_NEGATIVE)
            } else {
                presenter.saveAddress(it, ANA_POSITIVE)
            }
        }
    }

    private fun validateForm(): Boolean {
        var validated = true

        var field = ""
        if (et_detail_address?.text.isNullOrEmpty()) {
            validated = false
            rl_detail_address_info_counter.visibility = View.GONE
            setWrapperError(et_detail_address_wrapper, getString(R.string.validate_detail_alamat))
            if (field.isNotEmpty()) field += ", "
            field += "detail alamat"
        }

        if (!validated) {
            AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(field, eventLabel = LOGISTIC_LABEL)
        }

        if (!validateFormDefault(field)) validated = false
        return validated
    }

    private fun validateFormMismatch(): Boolean {
        var validated = true

        var field = ""
        if (et_kota_kecamatan_mismatch?.text.isNullOrEmpty()) {
            validated = false
            setWrapperError(et_kota_kecamatan_mismatch_wrapper, getString(R.string.validate_kota_kecamatan))
            if (field.isNotEmpty()) field += ", "
            field += "kota kecamatan"
        }
        if (et_kode_pos_mismatch?.text.isNullOrEmpty()) {
            validated = false
            setWrapperError(et_kode_pos_mismatch_wrapper, getString(R.string.validate_kode_pos))
            if (field.isNotEmpty()) field += ", "
            field += "kode pos"
        }
        if (et_kode_pos_mismatch?.text?.length ?: 0 < 5) {
            validated = false
            setWrapperError(et_kode_pos_mismatch_wrapper, getString(R.string.validate_kode_pos_length))
            if (field.isNotEmpty()) field += ", "
            field += "kode pos"
        }
        if (et_alamat_mismatch?.text.isNullOrEmpty()) {
            validated = false
            setWrapperError(et_alamat_mismatch_wrapper, getString(R.string.validate_alamat))
            if (field.isNotEmpty()) field += ", "
            field += "alamat"
        }
        if (et_alamat_mismatch?.text?.length ?: 0 < 5) {
            validated = false
            setWrapperError(et_alamat_mismatch_wrapper, getString(R.string.validate_alamat_length))
            if (field.isNotEmpty()) field += ", "
            field += "alamat"
        }

        if (!validated) {
            AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(field, eventLabel = LOGISTIC_LABEL)
        }

        if (!validateFormDefault(field)) validated = false
        return validated
    }

    private fun validateFormDefault(errorField: String): Boolean {
        var validated = true

        var field = errorField
        if (et_label_address?.text.isNullOrEmpty()) {
            validated = false
            setWrapperError(et_label_address_wrapper, getString(R.string.validate_label_alamat))
            if (field.isNotEmpty()) field += ", "
            field += "label alamat"

        }

        if (et_receiver_name?.text.isNullOrEmpty()) {
            validated = false
            setWrapperError(et_receiver_name_wrapper, getString(R.string.validate_nama_penerima))
            if (field.isNotEmpty()) field += ", "
            field += "nama penerima"

        }
        if (et_phone.text.isNullOrEmpty()) {
            validated = false
            setWrapperError(et_phone_wrapper, getString(R.string.validate_no_ponsel))
            if (field.isNotEmpty()) field += ", "
            field += "no ponsel"
        }

        if (et_phone?.text?.length ?: 0 < MINIMUM_CHARACTER) {
            validated = false
            setWrapperError(et_phone_wrapper, getString(R.string.validate_no_ponsel_less_char))
            if (field.isNotEmpty()) field += ", "
            field += "no ponsel"
        }

        if (!validated) {
            if (isMismatch) {
                AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(field, eventLabel = LOGISTIC_LABEL)
            } else {
                AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(field, eventLabel = LOGISTIC_LABEL)
            }
        }
        return validated
    }

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
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
        setWrapperError(et_detail_address_wrapper, null)
        rl_detail_address_info_counter.visibility = View.VISIBLE
    }

    private fun resetErrorFormMismatch() {
        setWrapperError(et_kota_kecamatan_mismatch_wrapper, null)
        setWrapperError(et_kode_pos_mismatch_wrapper, null)
        setWrapperError(et_alamat_mismatch_wrapper, null)
    }

    private fun resetErrorFormDefault() {
        setWrapperError(et_label_address_wrapper, null)
        setWrapperError(et_receiver_name_wrapper, null)
        setWrapperError(et_phone_wrapper, null)
    }

    private fun setWrapperWatcher(wrapper: TextInputLayout): TextWatcher {
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
                    val countCharLeft: Int
                    var info = ""
                    val strLength = s.toString().length
                    when {
                        strLength < 5 -> {
                            countCharLeft = 5 - strLength
                            info = "$countCharLeft karakter lagi diperlukan"
                        }
                        strLength > 4 -> {
                            countCharLeft = 175 - strLength
                            info = "$countCharLeft karakter tersisa"

                        }
                        strLength == 175 -> info = ""
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
        }
        setupRvLabelAlamatChips()
    }

    private fun eventShowZipCodes() {
        showZipCodes()
        AddNewAddressAnalytics.eventClickFieldKodePosChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
    }

    private fun setupRvKodePosChips() {
        rv_kodepos_chips_mismatch.apply {
            staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
            layoutManager = chipsLayoutManager
            adapter = zipCodeChipsAdapter
        }
    }

    private fun setupRvLabelAlamatChips() {
        rv_label_alamat_chips.apply {
            staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
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
                hideKeyboard()
                saveAddressDataModel?.editDetailAddress = et_detail_address.text.toString()
                goToPinpointActivity(currentLat, currentLong, false, token, false,
                        isMismatchSolved, isMismatch, saveAddressDataModel)
                AddNewAddressAnalytics.eventClickButtonUbahPinPointChangeAddressPositive(eventLabel = LOGISTIC_LABEL)
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
                hideKeyboard()
                if (et_kota_kecamatan_mismatch?.text.isNullOrEmpty()) {
                    view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.choose_district_first), it1, it2) } }
                    AddNewAddressAnalytics.eventViewToasterPilihKotaDanKodePosTerlebihDahulu(eventLabel = LOGISTIC_LABEL)
                } else {
                    goToPinpointActivity(currentLat, currentLong, false, token, true,
                            isMismatchSolved, isMismatch, saveAddressDataModel)
                    AddNewAddressAnalytics.eventClickButtonPilihLokasiIni(eventLabel = LOGISTIC_LABEL)
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
                hideKeyboard()
                saveAddressDataModel?.editDetailAddress = tv_detail_alamat_mismatch.text.toString()
                goToPinpointActivity(currentLat, currentLong, false, token, true,
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
        et_alamat_mismatch.clearFocus()

        if (isUnnamedRoad) {
            et_kota_kecamatan_mismatch.setText(this.saveAddressDataModel?.formattedAddress)
            et_kode_pos_mismatch.setText(this.saveAddressDataModel?.postalCode)
        } else if (isNullZipcode) {
            et_kota_kecamatan_mismatch.setText(this.saveAddressDataModel?.formattedAddress)
            presenter.getZipCodes(saveAddressDataModel?.districtId.toString())
        }
    }

    private fun setMismatchSolvedForm() {
        et_kota_kecamatan_mismatch.setText(this.saveAddressDataModel?.formattedAddress)

        val isDetailAddressEmpty = this.saveAddressDataModel?.editDetailAddress?.isEmpty()
        isDetailAddressEmpty?.let {
            if (it) {
                ll_detail_alamat.visibility = View.GONE
            } else {
                ll_detail_alamat.visibility = View.VISIBLE
                tv_detail_alamat_mismatch.text = this.saveAddressDataModel?.editDetailAddress
            }
        }
        et_kode_pos_mismatch.setText(this.saveAddressDataModel?.postalCode)
    }

    private fun showDistrictRecommendationBottomSheet() {
        val districtRecommendationBottomSheetFragment =
                DiscomBottomSheetFragment.newInstance()
        districtRecommendationBottomSheetFragment.setActionListener(this)
        fragmentManager?.run {
            districtRecommendationBottomSheetFragment.show(this, "")
        }
    }

    private fun showZipCodes() {
        ViewCompat.setLayoutDirection(rv_kodepos_chips_mismatch, ViewCompat.LAYOUT_DIRECTION_LTR)
        saveAddressDataModel?.zipCodes?.let {
            zipCodeChipsAdapter.zipCodes = it.toMutableList()
            zipCodeChipsAdapter.notifyDataSetChanged()
            rv_kodepos_chips_mismatch.visibility = View.VISIBLE
        }
    }

    private fun showLabelAlamatList() {
        val res: Resources = resources
        labelAlamatList = res.getStringArray(R.array.labelAlamatList)

        rv_label_alamat_chips.visibility = View.VISIBLE
        ViewCompat.setLayoutDirection(rv_label_alamat_chips, ViewCompat.LAYOUT_DIRECTION_LTR)
        labelAlamatChipsAdapter.labelAlamatList = labelAlamatList.toMutableList()
        labelAlamatChipsAdapter.notifyDataSetChanged()
    }

    override fun showLoading() {
        // no op
    }

    private fun setSaveAddressModel() {
        var address1 = ""
        val detailAddress: String
        if (!isMismatch && !isMismatchSolved) {
            detailAddress = et_detail_address.text.toString()

            address1 = "${saveAddressDataModel?.title}, ${saveAddressDataModel?.formattedAddress}"
            if (detailAddress.isNotEmpty()) address1 += " [Tokopedia Note: ${detailAddress}]"

            saveAddressDataModel?.address1 = address1
            saveAddressDataModel?.address2 = "$currentLat,$currentLong"

        } else {
            val etAlamat = et_alamat_mismatch.text.toString()
            if (etAlamat.isNotEmpty()) address1 = "$etAlamat, "
            if (isMismatch) {
                address1 += "${saveAddressDataModel?.selectedDistrict}"
                saveAddressDataModel?.address1 = address1
                if (isNullZipcode) {
                    saveAddressDataModel?.address2 = "$currentLat,$currentLong"
                } else {
                    saveAddressDataModel?.address2 = ""
                }
            } else {
                address1 += "${saveAddressDataModel?.formattedAddress}"
                saveAddressDataModel?.address1 = address1
                saveAddressDataModel?.address2 = "$currentLat,$currentLong"
            }
        }

        saveAddressDataModel?.addressName = et_label_address.text.toString()
        saveAddressDataModel?.receiverName = et_receiver_name.text.toString()
        saveAddressDataModel?.phone = et_phone.text.toString()
        saveAddressDataModel?.postalCode = et_kode_pos_mismatch.text.toString()
    }

    override fun onSuccessAddAddress(saveAddressDataModel: SaveAddressDataModel) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveAddressDataModel)
            })
            finish()
        }
    }

    override fun showZipCodes(zipcodes: List<String>) {
        saveAddressDataModel?.zipCodes = zipcodes
        showZipCodes()
    }

    override fun showManualZipCodes() {
        et_kode_pos_mismatch.isFocusableInTouchMode = true
        et_kode_pos_mismatch.isFocusable = true
        et_kode_pos_mismatch.setOnClickListener(null)
    }

    override fun onMapReady(googleMap: GoogleMap?) {
        this.googleMap = googleMap
        this.googleMap?.uiSettings?.isMapToolbarEnabled = false
        this.googleMap?.uiSettings?.isMyLocationButtonEnabled = true
        this.googleMap?.uiSettings?.setAllGesturesEnabled(false)
        activity?.let { MapsInitializer.initialize(activity) }
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
        arrangeLayout(isMismatch, isMismatchSolved)
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

    override fun onConnected(p0: Bundle?) {}

    override fun onConnectionSuspended(p0: Int) {}

    override fun onConnectionFailed(p0: ConnectionResult) {}

    override fun onGetDistrict(districtAddress: Address) {
        val provinceName = districtAddress.provinceName
        val cityName = districtAddress.cityName
        val districtName = districtAddress.districtName
        val districtSelected = "$provinceName, $cityName, $districtName"

        et_kota_kecamatan_mismatch.setText(districtSelected)
        saveAddressDataModel?.selectedDistrict = districtSelected
        saveAddressDataModel?.cityId = districtAddress.cityId
        saveAddressDataModel?.provinceId = districtAddress.provinceId
        saveAddressDataModel?.districtId = districtAddress.districtId
        saveAddressDataModel?.latitude = ""
        saveAddressDataModel?.longitude = ""
        saveAddressDataModel?.zipCodes = districtAddress.zipCodes
        autoCompletePresenter.getAutocomplete(districtName)
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
        moveMap(getLatLng(currentLat, currentLong))
    }

    override fun onSuccessAutofill(autofillDataUiModel: Data, errMsg: String) {
    }

    override fun showFailedDialog() {
    }

    override fun goToAddEditActivity(isMismatch: Boolean, isMismatchSolved: Boolean, isUnnamedRoad: Boolean, isZipCodeNull: Boolean) {
    }

    override fun onSuccessGetDistrictBoundary(districtBoundaryGeometryUiModel: DistrictBoundaryGeometryUiModel) {
    }

    override fun showOutOfReachDialog() {
    }

    override fun showUndetectedDialog() {
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE)
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onZipCodeClicked(zipCode: String) {
        rv_kodepos_chips_mismatch.visibility = View.GONE
        et_kode_pos_mismatch.apply {
            setText(zipCode)
            AddNewAddressAnalytics.eventClickFieldKodePosChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
        }
        saveAddressDataModel?.postalCode = zipCode
        AddNewAddressAnalytics.eventClickChipsKodePosChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
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
        et_label_address.run {
            setText(labelAlamat)
            setSelection(et_label_address?.text?.length ?: 0)
        }
        if (!isMismatch && !isMismatchSolved) {
            AddNewAddressAnalytics.eventClickChipsLabelAlamatChangeAddressPositive(eventLabel = LOGISTIC_LABEL)
        } else {
            AddNewAddressAnalytics.eventClickChipsLabelAlamatChangeAddressNegative(eventLabel = LOGISTIC_LABEL)
        }
    }

    private fun goToPinpointActivity(lat: Double?, long: Double?, isShowAutocomplete: Boolean, token: Token?, isPolygon: Boolean,
                                     isMismatchSolved: Boolean, isMismatch: Boolean, saveAddressDataModel: SaveAddressDataModel?) {
        startActivityForResult(context?.let {
            PinpointMapActivity.newInstance(it, lat, long, isShowAutocomplete, token, isPolygon,
                    isMismatchSolved, isMismatch, saveAddressDataModel, true)
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
        } else {
            // this solves issue when positif ANA changed into negatif ANA
            if (data == null) {
                isMismatch = true
                isMismatchSolved = false
                createFragment()
            }
        }
    }

    override fun finishBackToAddEdit(isMismatch: Boolean, isMismatchSolved: Boolean) {
    }

    private fun setDetailAlamatWatcher(): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                rl_detail_address_info_counter.visibility = View.VISIBLE
                if (s.isNotEmpty()) {
                    val countCharLeft: Int
                    var info = ""
                    val strLength = s.toString().length
                    when {
                        strLength > 0 -> {
                            countCharLeft = 60 - strLength
                            info = "$countCharLeft/60"
                        }
                        strLength == 0 -> info = "60/60"
                        strLength == 60 -> info = "0/60"
                    }
                    tv_detail_address_counter.text = info
                } else {
                    tv_detail_address_counter.text = "60/60"
                }
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
    }

    override fun showError(t: Throwable) {
        Toast.makeText(context, getString(R.string.something_wrong_happened), Toast.LENGTH_SHORT).show()
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
    }
}