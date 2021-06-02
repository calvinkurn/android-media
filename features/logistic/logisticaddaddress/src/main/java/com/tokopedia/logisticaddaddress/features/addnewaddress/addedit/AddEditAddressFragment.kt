package com.tokopedia.logisticaddaddress.features.addnewaddress.addedit

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
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
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.LocalCacheHandler
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.logisticCommon.util.getLatLng
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.common.AddressConstants.ANA_NEGATIVE
import com.tokopedia.logisticaddaddress.common.AddressConstants.ANA_POSITIVE
import com.tokopedia.logisticaddaddress.di.addnewaddress.AddNewAddressModule
import com.tokopedia.logisticaddaddress.di.addnewaddress.DaggerAddNewAddressComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.add_address.ContactData
import com.tokopedia.logisticaddaddress.features.addnewaddress.AddNewAddressUtils
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.analytics.AddNewAddressAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddress.pinpoint.PinpointMapActivity
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetFragment
import com.tokopedia.logisticaddaddress.utils.AddEditAddressUtil
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import kotlinx.android.synthetic.main.form_add_new_address_data_item.*
import kotlinx.android.synthetic.main.form_add_new_address_default_item.*
import kotlinx.android.synthetic.main.form_add_new_address_mismatch_data_item.*
import kotlinx.android.synthetic.main.fragment_add_edit_new_address.*
import kotlinx.android.synthetic.main.header_add_new_address_data_item.*
import javax.inject.Inject

/**
 * Created by fwidjaja on 2019-05-22.
 */
class AddEditAddressFragment : BaseDaggerFragment(), OnMapReadyCallback, AddEditView,
        DiscomBottomSheetFragment.ActionListener,
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
    private val EXTRA_DETAIL_ADDRESS_LATEST = "EXTRA_DETAIL_ADDRESS_LATEST"
    private lateinit var zipCodeChipsAdapter: ZipCodeChipsAdapter
    private lateinit var chipsLayoutManager: ChipsLayoutManager
    private lateinit var labelAlamatChipsLayoutManager: ChipsLayoutManager
    private var staticDimen8dp: Int? = 0
    private lateinit var labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    private val FINISH_PINPOINT_FLAG = 8888
    private val MINIMUM_CHARACTER = 9
    private var getView: View? = null
    private var getSavedInstanceState: Bundle? = null
    private var labelAlamatList: Array<String> = emptyArray()
    private var isLatitudeNotEmpty: Boolean? = false
    private var isLongitudeNotEmpty: Boolean? = false
    private var isFullFlow: Boolean = true
    private var isLogisticLabel: Boolean = true
    private var isCircuitBreaker: Boolean = false

    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private lateinit var localCacheHandler: LocalCacheHandler

    lateinit var mapView: MapView

    @Inject
    lateinit var presenter: AddEditAddressPresenter

    @Inject
    lateinit var userSession: UserSessionInterface

    companion object {
        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        const val REQUEST_CODE_CONTACT_PICKER = 99
        const val PREFERENCES_NAME = "add_address_preferences"
        const val ADDRESS_CONTACT_HAS_SHOWN = "address_show_coach_mark"

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
                    putBoolean(AddressConstants.EXTRA_IS_LOGISTIC_LABEL, extra.getBoolean(AddressConstants.EXTRA_IS_LOGISTIC_LABEL, true))
                    putBoolean(AddressConstants.EXTRA_IS_CIRCUIT_BREAKER, extra.getBoolean(AddressConstants.EXTRA_IS_CIRCUIT_BREAKER, false))
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
            isLogisticLabel = it.getBoolean(AddressConstants.EXTRA_IS_LOGISTIC_LABEL, true)
            isCircuitBreaker = it.getBoolean(AddressConstants.EXTRA_IS_CIRCUIT_BREAKER, false)
        }
        permissionCheckerHelper = PermissionCheckerHelper()
        localCacheHandler = LocalCacheHandler(context, PREFERENCES_NAME)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add_edit_new_address, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getView = view
        mapView = view.findViewById(R.id.map_view_detail)
        savedInstanceState?.let {
            getSavedInstanceState = savedInstanceState
        }
        createFragment()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
        }
    }

    private fun createFragment() {
        prepareMap()
        prepareLayout()
        setViewListener()
        showOnBoarding()
    }

    private fun prepareMap() {
        map_view_detail.onCreate(getSavedInstanceState)
        map_view_detail.getMapAsync(this)
    }

    private fun prepareLayout() {
        zipCodeChipsAdapter = ZipCodeChipsAdapter(context, this)
        labelAlamatChipsAdapter = LabelAlamatChipsAdapter(this)
        chipsLayoutManager = ChipsLayoutManager.newBuilder(getView?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .setScrollingEnabled(true)
                .build()
        labelAlamatChipsLayoutManager = ChipsLayoutManager.newBuilder(getView?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        staticDimen8dp = context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)

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
            if (!isMismatch && !isMismatchSolved) {
                AddNewAddressAnalytics.eventClickBackArrowOnPositivePageChangeAddressPositive(isFullFlow, isLogisticLabel)
            }
            activity?.finish()
        }

        if (!isMismatch && !isMismatchSolved) {
            et_detail_address.apply {
                addTextChangedListener(setWrapperWatcher(et_detail_address_wrapper, null))
                setOnClickListener { AddNewAddressAnalytics.eventClickFieldDetailAlamatChangeAddressPositive(isFullFlow, isLogisticLabel) }
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
                        AddNewAddressAnalytics.eventClickFieldNamaPenerimaChangeAddressPositive(isFullFlow, isLogisticLabel)
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

                addTextChangedListener(setWrapperWatcher(et_phone_wrapper, getString(R.string.validate_no_ponsel_less_char)))

                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldNoPonselChangeAddressPositive(isFullFlow, isLogisticLabel)
                    }
                }
            }

        } else {
            if (isMismatch) {
                et_kota_kecamatan_mismatch.apply {
                    addTextChangedListener(setWrapperWatcher(et_kota_kecamatan_mismatch_wrapper, null))
                    setOnClickListener {
                        showDistrictRecommendationBottomSheet()
                        AddNewAddressAnalytics.eventClickFieldKotaKecamatanChangeAddressNegative(isFullFlow, isLogisticLabel)
                    }
                }
            }

            et_alamat_mismatch.apply {
                addTextChangedListener(setWrapperWatcher(et_alamat_mismatch_wrapper, null))
                addTextChangedListener(setAlamatWatcher())
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldAlamatChangeAddressNegative(isFullFlow, isLogisticLabel)
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
                addTextChangedListener(setWrapperWatcher(et_detail_address_wrapper, null))
                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }

                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        AddNewAddressAnalytics.eventClickFieldNamaPenerimaChangeAddressNegative(isFullFlow, isLogisticLabel)
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
                        AddNewAddressAnalytics.eventClickFieldNoPonselChangeAddressNegative(isFullFlow, isLogisticLabel)
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
            addTextChangedListener(setWrapperWatcher(et_receiver_name_wrapper, null))
        }

        et_phone.apply {
            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
            addTextChangedListener(setWrapperWatcher(et_phone_wrapper, getString(R.string.validate_no_ponsel_less_char)))
        }

        btn_contact_picker.setOnClickListener {
            onNavigateToContact()
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
                }

                override fun afterTextChanged(s: Editable) {
                    val filterList = labelAlamatList.filter {
                        it.contains("$s", true)
                    }
                    labelAlamatChipsAdapter.submitList(filterList)
                }
            })
            setOnTouchListener { view, event ->
                view.parent.requestDisallowInterceptTouchEvent(true)
                if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                    view.parent.requestDisallowInterceptTouchEvent(false)
                }
                return@setOnTouchListener false
            }
            addTextChangedListener(setWrapperWatcher(et_label_address_wrapper, null))
        }
    }

    private fun eventShowListLabelAlamat(type: String) {
        showLabelAlamatList()
        if (type.equals("positive", true)) {
            AddNewAddressAnalytics.eventClickFieldLabelAlamatChangeAddressPositive(isFullFlow, isLogisticLabel)
        } else {
            AddNewAddressAnalytics.eventClickFieldLabelAlamatChangeAddressNegative(isFullFlow, isLogisticLabel)
        }
    }

    private fun doSaveAddress() {
        setSaveAddressModel()
        saveAddressDataModel?.let {
            if (isMismatch) {
                presenter.saveAddress(it, ANA_NEGATIVE, isFullFlow, isLogisticLabel)
            } else {
                presenter.saveAddress(it, ANA_POSITIVE, isFullFlow, isLogisticLabel)
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
            AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(field, isFullFlow, isLogisticLabel)
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
            AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(field, isFullFlow, isLogisticLabel)
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
                AddNewAddressAnalytics.eventClickButtonSimpanNegativeNotSuccess(field, isFullFlow, isLogisticLabel)
            } else {
                AddNewAddressAnalytics.eventClickButtonSimpanNotSuccess(field, isFullFlow, isLogisticLabel)
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

    private fun setWrapperWatcher(wrapper: TextInputLayout, textWatcher: String?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.length < 9 && s.isNotEmpty()) {
                    setWrapperError(wrapper, textWatcher)
                } else {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
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

    private fun arrangeLayout(isMismatch: Boolean, isMismatchSolved: Boolean, isCircuitBreaker: Boolean) {
        if (!isMismatch && !isMismatchSolved) {
            ll_mismatch.visibility = View.GONE
            ll_normal.visibility = View.VISIBLE

            setNormalMapHeader()
            setNormalForm()

        } else {
            ll_normal.visibility = View.GONE
            ll_mismatch.visibility = View.VISIBLE

            when {
                isCircuitBreaker -> {
                    setCircuitBreakerOnHeader()
                    setMismatchForm()
                }
                isMismatch -> {
                    setMismatchMapHeader()
                    setMismatchForm()
                }
                else -> {
                    setMismatchSolvedMapHeader()
                    setMismatchSolvedForm()
                }
            }
            setupRvKodePosChips()
        }
        setupRvLabelAlamatChips()
    }

    private fun eventShowZipCodes() {
        showZipCodes()
        AddNewAddressAnalytics.eventClickFieldKodePosChangeAddressNegative(isFullFlow, isLogisticLabel)
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
                AddNewAddressAnalytics.eventClickButtonUbahPinPointChangeAddressPositive(isFullFlow, isLogisticLabel)
            }
        }
    }

    private fun setMismatchMapHeader() {
        disable_map_layout.visibility = View.VISIBLE
        icon_pointer.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_green_pointer))
        context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N100)?.let { icon_pointer.setColorFilter(it) }
        btn_map.apply {
            text = getString(R.string.define_pinpoint)
            val params = btn_map.layoutParams
            params.width = 550
            btn_map.layoutParams = params
            setOnClickListener {
                hideKeyboard()
                if (et_kota_kecamatan_mismatch?.text.isNullOrEmpty()) {
                    view?.let { it1 -> activity?.let { it2 -> AddNewAddressUtils.showToastError(getString(R.string.choose_district_first), it1, it2) } }
                    AddNewAddressAnalytics.eventViewToasterPilihKotaDanKodePosTerlebihDahulu(isFullFlow, isLogisticLabel)
                } else {
                    goToPinpointActivity(currentLat, currentLong, false, token, true,
                            isMismatchSolved, isMismatch, saveAddressDataModel)
                    AddNewAddressAnalytics.eventClickButtonPilihLokasiIni(isFullFlow, isLogisticLabel)
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

    private fun setCircuitBreakerOnHeader() {
        disable_map_layout.visibility = View.VISIBLE
        icon_pointer.setImageDrawable(context?.resources?.getDrawable(R.drawable.ic_green_pointer))
        context?.resources?.getColor(com.tokopedia.unifyprinciples.R.color.Unify_N100)?.let { icon_pointer.setColorFilter(it) }
        btn_map.isClickable = false
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

    private fun showOnBoarding() {
        val coachMarkHasShown = localCacheHandler.getBoolean(ADDRESS_CONTACT_HAS_SHOWN, false)
        if (coachMarkHasShown) {
            return
        }

        val coachMarkItem = ArrayList<CoachMark2Item>()
        coachMarkItem.add(CoachMark2Item(btn_contact_picker,
            getString(R.string.contact_title_coachmark),
            getString(R.string.contact_desc_coachmark)))

        val coachMark = context?.let { CoachMark2(it) }
        coachMark?.showCoachMark(coachMarkItem)

        localCacheHandler.apply {
            putBoolean(ADDRESS_CONTACT_HAS_SHOWN, true)
            applyEditor()
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
        ViewCompat.setLayoutDirection(rv_kodepos_chips_mismatch, ViewCompat.LAYOUT_DIRECTION_LTR)
        saveAddressDataModel?.zipCodes?.let {
            rv_kodepos_chips_mismatch.visibility = View.VISIBLE
            zipCodeChipsAdapter.zipCodes = it.toMutableList()
            zipCodeChipsAdapter.notifyDataSetChanged()
        }
    }

    private fun showLabelAlamatList() {
        val res: Resources = resources
        labelAlamatList = res.getStringArray(R.array.labelAlamatList)

        rv_label_alamat_chips.visibility = View.VISIBLE
        ViewCompat.setLayoutDirection(rv_label_alamat_chips, ViewCompat.LAYOUT_DIRECTION_LTR)
        labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
    }

    private fun onNavigateToContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.checkPermissions(this,
                    getPermissions(),
                    object : PermissionCheckerHelper.PermissionCheckListener {
                        override fun onPermissionDenied(permissionText: String) {
                            //no-op
                        }

                        override fun onNeverAskAgain(permissionText: String) {
                            //no-op
                        }

                        override fun onPermissionGranted() {
                            openContactPicker()
                        }
                    }, this.getString(R.string.rationale_need_contact))
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT
        )
    }

    private fun openContactPicker() {
        val contactPickerIntent = Intent(
                Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI)
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            view?.let {
                Toaster.build(it, getString(R.string.contact_not_found), Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show()
            }
        }
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
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
        arrangeLayout(isMismatch, isMismatchSolved, isCircuitBreaker)
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView.onStop()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onDestroy() {
        mapView.onDestroy()
        super.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

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
        presenter.getAutoComplete(districtName)
    }

    override fun moveMap(latitude: Double, longitude: Double) {
        currentLat = latitude
        currentLong = longitude
        moveMap(getLatLng(currentLat, currentLong))
    }

    private fun hideKeyboard() {
        val inputMethodManager = context?.getSystemService(Activity.INPUT_METHOD_SERVICE)
        (inputMethodManager as InputMethodManager).hideSoftInputFromWindow(view?.windowToken, 0)
    }

    override fun onZipCodeClicked(zipCode: String) {
        rv_kodepos_chips_mismatch.visibility = View.GONE
        et_kode_pos_mismatch.apply {
            setText(zipCode)
            AddNewAddressAnalytics.eventClickFieldKodePosChangeAddressNegative(isFullFlow, isLogisticLabel)
        }
        saveAddressDataModel?.postalCode = zipCode
        AddNewAddressAnalytics.eventClickChipsKodePosChangeAddressNegative(isFullFlow, isLogisticLabel)
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
            AddNewAddressAnalytics.eventClickChipsLabelAlamatChangeAddressPositive(isFullFlow, isLogisticLabel)
        } else {
            AddNewAddressAnalytics.eventClickChipsLabelAlamatChangeAddressNegative(isFullFlow, isLogisticLabel)
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
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == FINISH_PINPOINT_FLAG) {
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
            } else if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
                val contactURI = data?.data
                var contact: ContactData? = null
                if (contactURI != null) {
                    contact = context?.let { AddEditAddressUtil.convertContactUriToData(it.contentResolver, contactURI) }
                }
                val contactNumber = contact?.contactNumber
                val phoneNumberOnly = removeSpecialChars(contactNumber.toString())
                et_phone.setText(phoneNumberOnly)
            } else {
                // this solves issue when positif ANA changed into negatif ANA
                if (data == null) {
                    isMismatch = true
                    isMismatchSolved = false
                    createFragment()
                }
            }
        }
    }

    private fun removeSpecialChars(s: String): String {
        return s.replace("[^A-Za-z0-9 ]".toRegex(), "").replace(" ","")
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

    override fun showError(t: Throwable?) {
        val message = ErrorHandler.getErrorMessage(context, t)
        view?.let { Toaster.build(it, message, Toaster.LENGTH_LONG, Toaster.TYPE_ERROR).show() }
    }

    override fun onDetach() {
        super.onDetach()
        presenter.detachView()
    }
}