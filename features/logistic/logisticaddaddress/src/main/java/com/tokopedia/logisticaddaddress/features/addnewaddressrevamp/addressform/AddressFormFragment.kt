package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.InputFilter
import android.text.InputType
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_EDIT_ADDRESS
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.mapper.AddAddressMapper
import com.tokopedia.logisticCommon.data.response.DistrictItem
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.util.LogisticUserConsentHelper
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_DISTRICT_NAME
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POLYGON
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_KOTA_KECAMATAN
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_POSTAL_CODE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_RESET_TO_SEARCH_PAGE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentAddressFormBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.add_address.ContactData
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.LabelAlamatChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetRevamp
import com.tokopedia.logisticaddaddress.utils.AddEditAddressUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class AddressFormFragment :
    BaseDaggerFragment(),
    LabelAlamatChipsAdapter.ActionListener,
    DiscomBottomSheetRevamp.DiscomRevampListener {

    private var bottomSheetInfoPenerima: BottomSheetUnify? = null
    private var saveDataModel: SaveAddressDataModel? = null
    private var formattedAddress: String = ""
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentDistrictName: String? = ""
    private var labelAlamatList: Array<Pair<String, Boolean>> = emptyArray()
    private var staticDimen8dp: Int? = 0
    private var isPositiveFlow: Boolean = true

    /*To differentiate user pinpoint on ANA Negative*/
    private var isPinpoint: Boolean = false
    private var validated: Boolean = true
    private val toppers: String = "Toppers-"
    private var currentKotaKecamatan: String? = ""
    private var currentAlamat: String = ""
    private var currentPostalCode: String = ""
    private var isLatitudeNotEmpty: Boolean? = false
    private var isLongitudeNotEmpty: Boolean? = false

    private var isEdit: Boolean = false
    var isBackDialogClicked: Boolean = false
    private var backDialog: DialogUnify? = null
    private var addressId: String = ""

    private lateinit var labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    private lateinit var labelAlamatChipsLayoutManager: ChipsLayoutManager
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var districtBottomSheet: DiscomBottomSheetRevamp? = null

    private var binding by autoClearedNullable<FragmentAddressFormBinding>()

    private val viewModel: AddressFormViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddressFormViewModel::class.java)
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddressFormBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            isEdit = it.getBoolean(EXTRA_IS_EDIT, false)
            if (!isEdit) {
                saveDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
                isLatitudeNotEmpty = saveDataModel?.latitude?.isNotEmpty()
                isLatitudeNotEmpty?.let {
                    if (it) currentLat = saveDataModel?.latitude?.toDouble() ?: 0.0
                }

                isLongitudeNotEmpty = saveDataModel?.longitude?.isNotEmpty()
                isLongitudeNotEmpty?.let {
                    if (it) currentLong = saveDataModel?.longitude?.toDouble() ?: 0.0
                }
                isPositiveFlow = it.getBoolean(EXTRA_IS_POSITIVE_FLOW)
                currentKotaKecamatan = it.getString(EXTRA_KOTA_KECAMATAN)
            } else {
                EditAddressRevampAnalytics.onViewEditAddressPageNew(userSession.userId)
                addressId = it.getString(EXTRA_ADDRESS_ID, "")
            }
            checkMapsAvailability()
            viewModel.source = it.getString(PARAM_SOURCE, "")
        }
        permissionCheckerHelper = PermissionCheckerHelper()
    }

    private fun checkMapsAvailability() {
        val gmsAvailable = if (isEdit) {
            context?.let { ctx -> MapsAvailabilityHelper.isMapsAvailable(ctx) } ?: true
        } else {
            arguments?.getBoolean(EXTRA_GMS_AVAILABILITY, true) ?: true
        }
        viewModel.isGmsAvailable = gmsAvailable
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareData()
        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
            onContactPickerResult(data)
        } else if (requestCode == REQUEST_PINPONT_PAGE && resultCode == Activity.RESULT_OK) {
            onPinpointResult(data)
        }
    }

    private fun onPinpointResult(data: Intent?) {
        val isResetToSearchPage = data?.getBooleanExtra(EXTRA_RESET_TO_SEARCH_PAGE, false) ?: false
        if (isResetToSearchPage) {
            activity?.run {
                setResult(Activity.RESULT_OK, Intent())
                finish()
            }
        }

        var addressDataFromPinpoint =
            data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_SAVE_DATA_UI_MODEL)
        if (addressDataFromPinpoint == null) {
            addressDataFromPinpoint = data?.getParcelableExtra(EXTRA_ADDRESS_NEW)
        }
        val kotaKecamatanFromEditPinpoint = data?.getStringExtra(EXTRA_KOTA_KECAMATAN)

        // if user make any changes from pinpoint page, then update data in this page
        if (addressDataFromPinpoint != null) {
            if (isEdit) {
                if (addressDataFromPinpoint.latitude != saveDataModel?.latitude || addressDataFromPinpoint.longitude != saveDataModel?.longitude) {
                    if (addressDataFromPinpoint.districtId != saveDataModel?.districtId && !isPositiveFlow) {
                        showToasterInfo(getString(R.string.change_pinpoint_outside_district))
                    } else {
                        showToasterInfo(getString(R.string.change_pinpoint_edit_address))
                    }
                    focusOnDetailAddress()
                }
            }
            saveDataModel = addressDataFromPinpoint
            currentKotaKecamatan = kotaKecamatanFromEditPinpoint
            binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.setText(
                currentKotaKecamatan
            )
            binding?.cardAddressPinpoint?.addressDistrict?.text = currentKotaKecamatan
            saveDataModel?.let {
                if (it.latitude.isNotEmpty() || it.longitude.isNotEmpty()) {
                    currentLat = it.latitude.toDouble()
                    currentLong = it.longitude.toDouble()
                    binding?.cardAddressNegative?.icLocation?.setImage(IconUnify.LOCATION)
                    binding?.cardAddressNegative?.addressDistrict?.text = if (isEdit) {
                        getString(R.string.tv_pinpoint_defined_edit)
                    } else {
                        context?.let {
                            HtmlLinkHelper(
                                it,
                                getString(R.string.tv_pinpoint_defined)
                            ).spannedString
                        }
                    }
                    binding?.cardAddressNegative?.btnChangeNegative?.text =
                        getString(R.string.change_pinpoint_positive_text)
                    if (saveDataModel?.postalCode?.isEmpty() == true) {
                        saveDataModel?.postalCode =
                            currentPostalCode
                    }
                }
            }
        }
    }

    private fun onContactPickerResult(data: Intent?) {
        val contactURI = data?.data
        var contact: ContactData? = null
        if (contactURI != null) {
            contact = context?.let {
                AddEditAddressUtil.convertContactUriToData(
                    it.contentResolver,
                    contactURI
                )
            }
            val phoneNumberOnly = removeSpecialChars(contact?.contactNumber.toString())
            binding?.formAccount?.etNomorHp?.textFieldInput?.setText(phoneNumberOnly)
            showToasterInfo(getString(R.string.success_add_phone_number))
        }
    }

    private fun showToasterInfo(info: String) {
        view?.let { view ->
            Toaster.build(view, info, Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show()
        }
    }

    private fun focusOnDetailAddress() {
        if (!isPositiveFlow) {
            binding?.formAddressNegative?.etAlamat?.textFieldInput?.requestFocus()
        } else {
            binding?.formAddress?.etAlamatNew?.textFieldInput?.requestFocus()
        }
    }

    private fun removeSpecialChars(s: String): String {
        return s.replace("[^A-Za-z0-9 ]".toRegex(), "").replace(" ", "")
    }

    private fun isPhoneNumberValid(phone: String): Boolean {
        val phoneRule = Regex("^(^62\\d{7,13}|^0\\d{8,14})$")
        return phoneRule.matches(phone)
    }

    private fun prepareData() {
        if (isEdit) {
            binding?.loaderAddressForm?.visibility = View.VISIBLE
            viewModel.getAddressDetail(addressId)
        } else {
            viewModel.getDefaultAddress("address")
            if (isPositiveFlow) {
                viewModel.getDistrictDetail(saveDataModel?.districtId.toString())
            } else {
                prepareLayout(null)
            }
        }
    }

    private fun initObserver() {
        viewModel.districtDetail.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    prepareLayout(it.data.district.getOrNull(0))
                }

                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        }

        viewModel.saveAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == 1) {
                        saveDataModel?.id = it.data.addrId
                        saveDataModel?.warehouseId = it.data.tokonow.warehouseId
                        saveDataModel?.shopId = it.data.tokonow.shopId
                        saveDataModel?.warehouses =
                            AddAddressMapper.mapWarehouses(it.data.tokonow.warehouses)
                        saveDataModel?.serviceType = it.data.tokonow.serviceType
                        if (isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickSimpanPositive(
                                userSession.userId,
                                SUCCESS
                            )
                        } else {
                            AddNewAddressRevampAnalytics.onClickSimpanNegative(
                                userSession.userId,
                                SUCCESS
                            )
                        }
                        onSuccessAddAddress()
                    }
                }

                is Fail -> {
                    if (isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickSimpanErrorPositive(
                            userSession.userId,
                            ""
                        )
                        AddNewAddressRevampAnalytics.onClickSimpanPositive(
                            userSession.userId,
                            NOT_SUCCESS
                        )
                    } else {
                        AddNewAddressRevampAnalytics.onClickSimpanErrorNegative(
                            userSession.userId,
                            ""
                        )
                        AddNewAddressRevampAnalytics.onClickSimpanNegative(
                            userSession.userId,
                            NOT_SUCCESS
                        )
                    }
                    val msg = it.throwable.message.toString()
                    view?.let { view ->
                        Toaster.build(
                            view,
                            msg,
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        }

        viewModel.editAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == 1) {
                        onSuccessEditAddress(it.data.isStateChosenAddressChanged)
                        EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, true)
                    } else {
                        EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    }
                }

                is Fail -> {
                    EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    val msg = it.throwable.message.toString()
                    view?.let { view ->
                        Toaster.build(
                            view,
                            msg,
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        }

        viewModel.defaultAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.addressId != 0L) {
                        binding?.layoutCbDefaultLoc?.visibility = View.VISIBLE
                    } else {
                        binding?.layoutCbDefaultLoc?.visibility = View.GONE
                    }
                }
            }
        }

        viewModel.addressDetail.observe(viewLifecycleOwner) {
            binding?.loaderAddressForm?.visibility = View.GONE
            when (it) {
                is Success -> {
                    it.data.keroGetAddress.data.firstOrNull()?.let { detailAddress ->
                        saveDataModel =
                            AddAddressMapper.mapAddressDetailToSaveAddressDataModel(detailAddress)
                        isLatitudeNotEmpty = saveDataModel?.latitude?.isNotEmpty()
                        isLatitudeNotEmpty?.let { notEmpty ->
                            if (notEmpty) currentLat = saveDataModel?.latitude?.toDouble() ?: 0.0
                        }

                        isLongitudeNotEmpty = saveDataModel?.longitude?.isNotEmpty()
                        isLongitudeNotEmpty?.let { notEmpty ->
                            if (notEmpty) currentLong = saveDataModel?.longitude?.toDouble() ?: 0.0
                        }
                        isPositiveFlow = isLatitudeNotEmpty == true && isLongitudeNotEmpty == true
                        currentKotaKecamatan =
                            "${detailAddress.districtName}, ${detailAddress.cityName}, ${detailAddress.provinceName}"
                        prepareEditLayout(detailAddress)
                    }
                }
            }
        }

        viewModel.pinpointValidation.observe(viewLifecycleOwner) {
            binding?.loaderAddressForm?.visibility = View.GONE
            when (it) {
                is Success -> {
                    if (it.data.result) {
                        saveDataModel?.let { addressData -> viewModel.saveEditAddress(addressData) }
                    } else {
                        view?.let { v ->
                            Toaster.build(
                                v,
                                getString(R.string.error_district_pinpoint_mismatch),
                                Toaster.LENGTH_SHORT,
                                Toaster.TYPE_ERROR
                            ).show()
                        }
                        EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    }
                }
                is Fail -> {
                    EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    val msg = it.throwable.message.toString()
                    view?.let { view ->
                        Toaster.build(
                            view,
                            msg,
                            Toaster.LENGTH_SHORT,
                            Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun prepareLayout(data: DistrictItem?) {
        setupLabelChips("Rumah")
        binding?.run {
            if (userSession.name.isNotEmpty() && !userSession.name.contains(
                    toppers,
                    ignoreCase = true
                )
            ) {
                formAccount.etNamaPenerima.textFieldInput.setText(userSession.name)
                formAccount.infoNameLayout.visibility = View.GONE
            } else if (userSession.name.contains(toppers, ignoreCase = true)) {
                formAccount.etNamaPenerima.textFieldWrapper.helperText =
                    getString(R.string.helper_nama_penerima)
            }
            setupFormAccount()
            formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                if (isPositiveFlow) {
                    AddNewAddressRevampAnalytics.onClickIconPhoneBookPositive(userSession.userId)
                } else {
                    AddNewAddressRevampAnalytics.onClickIconPhoneBookNegative(userSession.userId)
                }
                onNavigateToContact()
            }
            formAccount.btnInfo.setOnClickListener {
                if (isPositiveFlow) {
                    AddNewAddressRevampAnalytics.onClickIconNamaPenerimaPositive(userSession.userId)
                } else {
                    AddNewAddressRevampAnalytics.onClickIconNamaPenerimaNegative(userSession.userId)
                }
                showBottomSheetInfoPenerima()
            }
        }

        setOnTouchLabelAddress()
        setupRvLabelAlamatChips()
        setTextListener()
        if (!isPositiveFlow) {
            showNegativeLayout()
            setupNegativePinpointCard()
            binding?.run {
                cardAddressNegative.root.setOnClickListener {
                    AddNewAddressRevampAnalytics.onClickAturPinpointNegative(userSession.userId)
                    checkKotaKecamatan()
                }

                formAddressNegative.etKotaKecamatan.textFieldInput.setText(currentKotaKecamatan)
                formAddressNegative.etKotaKecamatan.textFieldInput.apply {
                    inputType = InputType.TYPE_NULL
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(
                                userSession.userId
                            )
                            showDistrictRecommendationBottomSheet(false)
                        }
                    }
                    setOnClickListener {
                        AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(userSession.userId)
                        showDistrictRecommendationBottomSheet(false)
                    }
                }
                formAddressNegative.etLabel.textFieldInput.setText("Rumah")
                formAddressNegative.etLabel.textFieldInput.addTextChangedListener(
                    setWrapperWatcher(
                        formAddressNegative.etLabel.textFieldWrapper,
                        null
                    )
                )
                formAddressNegative.etAlamat.textFieldInput.addTextChangedListener(
                    setWrapperWatcher(
                        formAddressNegative.etAlamat.textFieldWrapper,
                        null
                    )
                )
                currentAlamat = formAddressNegative.etAlamat.textFieldInput.text.toString()
            }
        } else {
            binding?.run {
                formattedAddress = "${data?.districtName}, ${data?.cityName}, ${data?.provinceName}"
                showPositiveLayout()

                cardAddressPinpoint.addressDistrict.text = formattedAddress

                formAddress.etLabel.textFieldInput.setText("Rumah")
                formAddress.etLabel.textFieldInput.addTextChangedListener(
                    setWrapperWatcher(
                        formAddress.etLabel.textFieldWrapper,
                        null
                    )
                )
                formAddress.etAlamatNew.textFieldInput.addTextChangedListener(
                    setWrapperWatcher(
                        formAddress.etAlamatNew.textFieldWrapper,
                        null
                    )
                )
            }
        }

        LogisticUserConsentHelper.displayUserConsent(
            activity as Context,
            userSession.userId,
            binding?.userConsent,
            getString(R.string.btn_simpan),
            if (isPositiveFlow) LogisticUserConsentHelper.ANA_REVAMP_POSITIVE else LogisticUserConsentHelper.ANA_REVAMP_NEGATIVE
        )

        binding?.btnSaveAddressNew?.setOnClickListener {
            if (validateForm()) {
                doSaveAddress()
            }
        }
    }

    private fun setupLabelChips(currentLabel: String) {
        labelAlamatList = context?.resources?.getStringArray(R.array.labelAlamatList)
            ?.map { Pair(it, it.equals(currentLabel, ignoreCase = true)) }?.toTypedArray()
            ?: emptyArray()
        labelAlamatChipsAdapter = LabelAlamatChipsAdapter(this)
        labelAlamatChipsLayoutManager = ChipsLayoutManager.newBuilder(view?.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()
        staticDimen8dp =
            context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    }

    @SuppressLint("SetTextI18n")
    private fun prepareEditLayout(data: KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse) {
        setupLabelChips(data.addrName)
        binding?.formAccount?.run {
            etNamaPenerima.textFieldInput.setText(data.receiverName)
            infoNameLayout.visibility = View.GONE
            setupFormAccount()
            etNomorHp.let { nomorHpField ->
                nomorHpField.textFieldInput.setText(data.phone)
                nomorHpField.getFirstIcon().setOnClickListener {
                    EditAddressRevampAnalytics.onClickIconPhoneBook(userSession.userId)
                    onNavigateToContact()
                }
            }
            btnInfo.setOnClickListener {
                showBottomSheetInfoPenerima()
            }
        }

        val addressDetail = data.addressDetailStreet.ifEmpty { data.address1 }

        setOnTouchLabelAddress()
        setupRvLabelAlamatChips()
        setTextListener()

        if (!isPositiveFlow) {
            showNegativeLayout()
            setupNegativePinpointCard()
            binding?.run {
                cardAddressNegative.run {
                    root.setOnClickListener {
                        EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                        checkKotaKecamatan()
                    }
                    btnChangeNegative.visibility = View.VISIBLE
                    btnArrow.visibility = View.GONE
                }

                formAddressNegative.run {
                    etKotaKecamatan.textFieldInput.apply {
                        setText(currentKotaKecamatan)
                        inputType = InputType.TYPE_NULL
                        setOnFocusChangeListener { _, hasFocus ->
                            if (hasFocus) {
                                EditAddressRevampAnalytics.onClickFieldKotaKecamatan(userSession.userId)
                                showDistrictRecommendationBottomSheet(false)
                            }
                        }
                        setOnClickListener {
                            EditAddressRevampAnalytics.onClickFieldKotaKecamatan(userSession.userId)
                            showDistrictRecommendationBottomSheet(false)
                        }
                    }
                    etLabel.run {
                        textFieldInput.setText(data.addrName)
                        textFieldInput.addTextChangedListener(
                            setWrapperWatcher(
                                textFieldWrapper,
                                null
                            )
                        )
                    }
                    rvLabelAlamatChips.visibility = View.GONE
                    etAlamat.run {
                        textFieldInput.setText(addressDetail)
                        if (addressDetail.length > MAX_CHAR_ALAMAT) {
                            this.textFieldWrapper.let { wrapper ->
                                wrapper.error =
                                    context.getString(R.string.error_alamat_exceed_max_char)
                                wrapper.isErrorEnabled = true
                            }
                        }
                        textFieldInput.addTextChangedListener(
                            setWrapperWatcher(
                                textFieldWrapper,
                                null
                            )
                        )
                    }
                    etCourierNote.run {
                        textFieldInput.setText(data.addressDetailNotes)
                        if (data.addressDetailNotes.length > MAX_CHAR_NOTES) {
                            textFieldWrapper.let { wrapper ->
                                wrapper.error =
                                    context.getString(R.string.error_notes_exceed_max_char)
                                wrapper.isErrorEnabled = true
                            }
                            textFieldInput.addTextChangedListener(
                                setNotesWrapperWatcher(
                                    textFieldWrapper
                                )
                            )
                        }
                    }
                    currentAlamat = etAlamat.textFieldInput.text.toString()
                }
            }
        } else {
            showPositiveLayout()
            binding?.run {
                formattedAddress = "${data.districtName}, ${data.cityName}, ${data.provinceName}"
                cardAddressPinpoint.run {
                    context?.let {
                        btnChange.visible()
                        btnChange.setOnClickListener {
                            goToPinpointPage()
                            EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                        }
                    }
                    tvPinpointTitle.visibility = View.VISIBLE
                    addressDistrict.text = formattedAddress
                }
                formAddress.run {
                    etAlamatNew.run {
                        textFieldInput.setText(addressDetail)
                        if (addressDetail.length > MAX_CHAR_ALAMAT) {
                            textFieldWrapper.let { wrapper ->
                                wrapper.error =
                                    context.getString(R.string.error_alamat_exceed_max_char)
                                wrapper.isErrorEnabled = true
                            }
                        }
                        textFieldInput.addTextChangedListener(
                            setWrapperWatcher(
                                textFieldWrapper,
                                null
                            )
                        )
                    }
                    etCourierNote.run {
                        textFieldInput.setText(data.addressDetailNotes)
                        if (data.addressDetailNotes.length > MAX_CHAR_NOTES) {
                            this.textFieldWrapper.let { wrapper ->
                                wrapper.error =
                                    context.getString(R.string.error_notes_exceed_max_char)
                                wrapper.isErrorEnabled = true
                            }
                            textFieldInput.addTextChangedListener(
                                setNotesWrapperWatcher(
                                    textFieldWrapper
                                )
                            )
                        }
                    }
                    etLabel.run {
                        textFieldInput.setText(data.addrName)
                        textFieldInput.addTextChangedListener(
                            setWrapperWatcher(
                                textFieldWrapper,
                                null
                            )
                        )
                    }
                    rvLabelAlamatChips.visibility = View.GONE
                }
            }
        }

        LogisticUserConsentHelper.displayUserConsent(
            activity as Context,
            userSession.userId,
            binding?.userConsent,
            getString(R.string.btn_simpan),
            EditAddressRevampAnalytics.CATEGORY_EDIT_ADDRESS_PAGE
        )

        binding?.btnSaveAddressNew?.setOnClickListener {
            if (validateForm()) {
                doSaveEditAddress()
            }
        }
    }

    private fun showPositiveLayout() {
        binding?.run {
            cardAddressPinpoint.root.visible()
            formAddress.root.visible()
            formAddressNegative.root.gone()
            cardAddressNegative.root.gone()
        }
    }

    private fun showNegativeLayout() {
        binding?.run {
            cardAddressPinpoint.root.gone()
            formAddress.root.gone()
            formAddressNegative.root.visible()
            cardAddressNegative.root.visible()
        }
    }

    private fun showBottomSheetInfoPenerima() {
        bottomSheetInfoPenerima = BottomSheetUnify()
        val viewBinding =
            BottomsheetLocationUnmatchedBinding.inflate(LayoutInflater.from(context), null, false)
        setupBottomShetInfoPenerima(viewBinding)

        bottomSheetInfoPenerima?.apply {
            setCloseClickListener { dismiss() }
            setChild(viewBinding.root)
            setOnDismissListener { dismiss() }
        }

        childFragmentManager.let {
            bottomSheetInfoPenerima?.show(it, "")
        }
    }

    private fun setupBottomShetInfoPenerima(viewBinding: BottomsheetLocationUnmatchedBinding) {
        viewBinding.run {
            tvLocationUnmatched.text = getString(R.string.tv_title_nama_penerima_bottomsheet)
            tvLocationUnmatchedDetail.text =
                context?.let {
                    HtmlLinkHelper(
                        it,
                        getString(R.string.tv_desc_nama_penerima_bottomsheet)
                    ).spannedString
                }
            btnClose.setOnClickListener {
                bottomSheetInfoPenerima?.dismiss()
            }
        }
    }

    private fun validateForm(): Boolean {
        validated = true
        val field = mutableListOf<String>()
        if (isPositiveFlow) {
            if (!validatePhoneNumber()) {
                field.add(getString(R.string.field_nomor_hp))
                validated = false
            }
            if (!validateReceiverName()) {
                field.add(getString(R.string.field_nama_penerima))
                validated = false
            }
            if (!validateCourierNote()) {
                field.add(getString(R.string.field_catatan_kurir))
                validated = false
            }
            if (!validateAlamat()) {
                field.add(getString(R.string.field_alamat))
                validated = false
            }
            if (!validateLabel()) {
                field.add(getString(R.string.field_label_alamat))
                validated = false
            }
        } else {
            if (!validateCourierNote()) {
                field.add(getString(R.string.field_catatan_kurir))
                validated = false
            }
            if (!validateAlamat()) {
                field.add(getString(R.string.field_alamat))
                validated = false
            }
            if (!validateLabel()) {
                field.add(getString(R.string.field_label_alamat))
                validated = false
            }
            if (!validatePhoneNumber()) {
                field.add(getString(R.string.field_nomor_hp))
                validated = false
            }
            if (!validateReceiverName()) {
                field.add(getString(R.string.field_nama_penerima))
                validated = false
            }
        }

        if (!isEdit) {
            if (!validated && isPositiveFlow) {
                AddNewAddressRevampAnalytics.onClickSimpanErrorPositive(
                    userSession.userId,
                    field.joinToString(",")
                )
            } else if (!validated && !isPositiveFlow) {
                AddNewAddressRevampAnalytics.onClickSimpanErrorNegative(
                    userSession.userId,
                    field.joinToString(",")
                )
            }
        } else {
            if (!validated) {
                EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                EditAddressRevampAnalytics.onClickSimpanError(
                    userSession.userId,
                    field.joinToString(",")
                )
            }
        }
        return validated
    }

    private fun validateReceiverName(): Boolean {
        binding?.formAccount?.etNamaPenerima?.let { field ->
            val receiverName = field.textFieldInput.text.toString()
            return if (receiverName.length < MIN_CHAR_RECEIVER_NAME) {
                if (receiverName.isEmpty() || receiverName == " ") {
                    setWrapperError(field.textFieldWrapper, getString(R.string.tv_error_field))
                }
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.error_nama_penerima),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
                false
            } else {
                true
            }
        }
        return false
    }

    private fun validatePhoneNumber(): Boolean {
        binding?.formAccount?.etNomorHp?.let { field ->
            val phoneNumber = field.textFieldInput.text.toString()
            return if (phoneNumber.length < MIN_CHAR_PHONE_NUMBER) {
                if (phoneNumber.isEmpty() || phoneNumber == " ") {
                    setWrapperError(field.textFieldWrapper, getString(R.string.tv_error_field))
                }
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.error_min_char_phone_number),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
                false
            } else if (!isPhoneNumberValid(phoneNumber)) {
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.error_invalid_format_phone_number),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
                false
            } else {
                true
            }
        }
        return false
    }

    private fun validateCourierNote(): Boolean {
        binding?.run {
            return if (isPositiveFlow) {
                formAddress.etCourierNote.textFieldWrapper.error == null
            } else {
                formAddressNegative.etCourierNote.textFieldWrapper.error == null
            }
        }
        return false
    }

    private fun validateAlamat(): Boolean {
        binding?.run {
            val field =
                if (isPositiveFlow) formAddress.etAlamatNew else formAddressNegative.etAlamat
            val alamat = field.textFieldInput.text.toString()
            return if (alamat.length < MIN_CHAR_ADDRESS_LABEL) {
                if (alamat.isEmpty() || alamat == " ") {
                    setWrapperError(field.textFieldWrapper, getString(R.string.tv_error_field))
                }
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.error_alamat),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
                false
            } else {
                field.textFieldWrapper.error == null
            }
        }
        return false
    }

    private fun validateLabel(): Boolean {
        binding?.run {
            val field = if (isPositiveFlow) formAddress.etLabel else formAddressNegative.etLabel
            val label = field.textFieldInput.text.toString()
            return if (label.length < MIN_CHAR_ADDRESS_LABEL) {
                if (label.isEmpty() || label == " ") {
                    setWrapperError(field.textFieldWrapper, getString(R.string.tv_error_field))
                }
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.error_label_address),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
                false
            } else {
                true
            }
        }
        return false
    }

    private fun setWrapperWatcher(wrapper: TextInputLayout, text: String?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty()) {
                    setWrapperError(wrapper, text)
                } else {
                    setWrapperError(wrapper, getString(R.string.tv_error_field))
                }
            }

            override fun afterTextChanged(text: Editable) {
                if (text.isNotEmpty()) {
                    setWrapperError(wrapper, null)
                }
            }
        }
    }

    private fun setNotesWrapperWatcher(wrapper: TextInputLayout): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                wrapper.error = null
                wrapper.setErrorEnabled(false)
            }

            override fun afterTextChanged(text: Editable) {
                wrapper.error = null
                wrapper.setErrorEnabled(false)
            }
        }
    }

    private fun setWrapperWatcherPhone(
        wrapper: TextInputLayout,
        textWatcher: String?
    ): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length < MIN_CHAR_PHONE_NUMBER) {
                    setWrapperError(wrapper, textWatcher)
                } else if (s.isEmpty() && isEdit) {
                    setWrapperError(wrapper, getString(R.string.tv_error_field))
                } else {
                    setWrapperError(wrapper, null)
                }
            }

            override fun afterTextChanged(text: Editable) {
            }
        }
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

    override fun onPause() {
        super.onPause()
        dismissDistrictRecommendationBottomSheet()
    }

    private fun dismissDistrictRecommendationBottomSheet() {
        if (districtBottomSheet != null) {
            districtBottomSheet?.dismiss()
            districtBottomSheet = null
        }
    }

    private fun showDistrictRecommendationBottomSheet(isPinpoint: Boolean) {
        districtBottomSheet = DiscomBottomSheetRevamp()
        districtBottomSheet?.setData(
            isPinpoint = isPinpoint,
            isEdit = isEdit,
            gmsAvailable = viewModel.isGmsAvailable
        )
        districtBottomSheet?.setListener(this)
        districtBottomSheet?.show(this.childFragmentManager)
    }

    private fun checkKotaKecamatan() {
        if (binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.text?.toString()
            ?.isEmpty() ?: true
        ) {
            showDistrictRecommendationBottomSheet(true)
        } else {
            goToPinpointPage()
        }
    }

    private fun goToPinpointPage() {
        val bundle = Bundle()
        bundle.putDouble(EXTRA_LAT, currentLat)
        bundle.putDouble(EXTRA_LONG, currentLong)
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        bundle.putString(EXTRA_DISTRICT_NAME, currentDistrictName)
        bundle.putString(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
        bundle.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, saveDataModel)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, true)
        bundle.putBoolean(EXTRA_IS_EDIT, isEdit)
        bundle.putString(EXTRA_POSTAL_CODE, currentPostalCode)
        bundle.putBoolean(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
        if (!isPositiveFlow && !isEdit) bundle.putBoolean(EXTRA_IS_POLYGON, true)
        startActivityForResult(
            context?.let { PinpointNewPageActivity.createIntent(it, bundle) },
            REQUEST_PINPONT_PAGE
        )
    }

    private fun onNavigateToContact() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionCheckerHelper?.checkPermissions(
                this,
                getPermissions(),
                object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        // no-op
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        // no-op
                    }

                    override fun onPermissionGranted() {
                        openContactPicker()
                    }
                },
                this.getString(R.string.rationale_need_contact)
            )
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
            PermissionCheckerHelper.Companion.PERMISSION_READ_CONTACT
        )
    }

    private fun openContactPicker() {
        val contactPickerIntent = Intent(
            Intent.ACTION_PICK,
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI
        )
        try {
            startActivityForResult(contactPickerIntent, REQUEST_CODE_CONTACT_PICKER)
        } catch (e: ActivityNotFoundException) {
            view?.let {
                Toaster.build(
                    it,
                    getString(R.string.contact_not_found),
                    Toaster.LENGTH_LONG,
                    Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    private fun setupRvLabelAlamatChips() {
        binding?.run {
            val field =
                if (isPositiveFlow) formAddress.rvLabelAlamatChips else formAddressNegative.rvLabelAlamatChips
            field.apply {
                staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
                layoutManager = labelAlamatChipsLayoutManager
                adapter = labelAlamatChipsAdapter
            }
        }
    }

    private fun setupFormAccount() {
        binding?.run {
            formAccount.etNamaPenerima.textFieldInput.addTextChangedListener(
                setWrapperWatcher(
                    formAccount.etNamaPenerima.textFieldWrapper,
                    null
                )
            )
            formAccount.etNomorHp.setFirstIcon(R.drawable.ic_contact_black)
            formAccount.etNomorHp.textFieldInput.addTextChangedListener(
                setWrapperWatcherPhone(
                    formAccount.etNomorHp.textFieldWrapper,
                    getString(R.string.validate_no_ponsel_new)
                )
            )
        }
    }

    private fun setupNegativePinpointCard() {
        binding?.run {
            if (!isPinpoint) {
                cardAddressNegative.icLocation.setImage(IconUnify.LOCATION_OFF)
                cardAddressNegative.addressDistrict.text =
                    if (isEdit) {
                        getString(R.string.tv_pinpoint_not_defined_edit)
                    } else {
                        context?.let {
                            HtmlLinkHelper(
                                it,
                                getString(R.string.tv_pinpoint_not_defined)
                            ).spannedString
                        }
                    }
            } else {
                cardAddressNegative.icLocation.setImage(IconUnify.LOCATION)
                cardAddressNegative.addressDistrict.text =
                    if (isEdit) {
                        getString(R.string.tv_pinpoint_defined_edit)
                    } else {
                        context?.let {
                            HtmlLinkHelper(
                                it,
                                getString(R.string.tv_pinpoint_defined)
                            ).spannedString
                        }
                    }
                cardAddressNegative.btnChangeNegative.text =
                    getString(R.string.change_pinpoint_positive_text)
            }
        }
    }

    private fun setTextListener() {
        binding?.run {
            formAccount.etNomorHp.textFieldInput.apply {
                addTextChangedListener(object : TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        //
                    }

                    override fun beforeTextChanged(
                        s: CharSequence?,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                        //
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                        filters = arrayOf(InputFilter.LengthFilter(MAX_CHAR_PHONE_NUMBER))
                    }
                })
            }

            cbDefaultLoc.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (!isEdit) {
                        if (isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaPositive(
                                userSession.userId
                            )
                        } else {
                            AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaNegative(
                                userSession.userId
                            )
                        }
                    }
                    saveDataModel?.setAsPrimaryAddresss = true
                } else {
                    saveDataModel?.setAsPrimaryAddresss = false
                }
            }

            formAccount.etNamaPenerima.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        if (isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaPositive(
                                userSession.userId
                            )
                        } else {
                            AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaNegative(
                                userSession.userId
                            )
                        }
                    } else {
                        EditAddressRevampAnalytics.onClickFieldNamaPenerima(userSession.userId)
                    }
                }
            }

            formAccount.etNomorHp.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        if (isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickFieldNomorHpPositive(userSession.userId)
                        } else {
                            AddNewAddressRevampAnalytics.onClickFieldNomorHpNegative(userSession.userId)
                        }
                    } else {
                        EditAddressRevampAnalytics.onClickFieldNomorHp(userSession.userId)
                    }
                }
            }

            formAddress.etLabel.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    eventShowListLabelAlamat()
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldLabelAlamatPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldLabelAlamat(userSession.userId)
                    }
                }
            }

            formAddress.etAlamatNew.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldAlamatPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldAlamat(userSession.userId)
                    }
                }
            }

            formAddress.etCourierNote.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldCatatanKurirPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldCatatanKurir(userSession.userId)
                    }
                }
            }

            formAddressNegative.etLabel.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    eventShowListLabelAlamat()
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldLabelAlamatNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldLabelAlamat(userSession.userId)
                    }
                }
            }

            formAddressNegative.etAlamat.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldAlamatNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldAlamat(userSession.userId)
                    }
                }
            }

            formAddressNegative.etCourierNote.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldCatatanKurirNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldCatatanKurir(userSession.userId)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchLabelAddress() {
        binding?.run {
            val field = if (isPositiveFlow) formAddress.etLabel else formAddressNegative.etLabel
            val rvChips =
                if (isPositiveFlow) formAddress.rvLabelAlamatChips else formAddressNegative.rvLabelAlamatChips

            field.textFieldInput.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        eventShowListLabelAlamat()
                    } else {
                        rvChips.visibility = View.GONE
                    }
                }
                setOnClickListener {
                    eventShowListLabelAlamat()
                }
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(
                        s: CharSequence,
                        start: Int,
                        count: Int,
                        after: Int
                    ) {
                    }

                    override fun onTextChanged(
                        s: CharSequence,
                        start: Int,
                        before: Int,
                        count: Int
                    ) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val currentLabel = labelAlamatList
                        labelAlamatList = currentLabel.map { item ->
                            item.copy(
                                second = item.first.equals(
                                    s.toString(),
                                    ignoreCase = true
                                )
                            )
                        }.toTypedArray()
                        val filterList = labelAlamatList.filter {
                            it.first.contains("$s", true)
                        }
                        if (!isEdit) {
                            labelAlamatChipsAdapter.submitList(filterList)
                        } else {
                            if (s.toString().isNotEmpty() && filterList.isEmpty()) {
                                rvChips.visibility = View.GONE
                            } else {
                                rvChips.visibility = View.VISIBLE
                                labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
                            }
                        }
                    }
                })
                setOnTouchListener { view, event ->
                    view.parent.requestDisallowInterceptTouchEvent(true)
                    if ((event.action and MotionEvent.ACTION_MASK) == MotionEvent.ACTION_UP) {
                        view.parent.requestDisallowInterceptTouchEvent(false)
                    }
                    return@setOnTouchListener false
                }
            }
        }
    }

    private fun eventShowListLabelAlamat() {
        showLabelAlamatList()
    }

    private fun showLabelAlamatList() {
        if (isPositiveFlow) {
            binding?.formAddress?.rvLabelAlamatChips?.visibility = View.VISIBLE
        } else {
            binding?.formAddressNegative?.rvLabelAlamatChips?.visibility = View.VISIBLE
        }
        binding?.formAddress?.rvLabelAlamatChips?.let {
            ViewCompat.setLayoutDirection(
                it,
                ViewCompat.LAYOUT_DIRECTION_LTR
            )
        }
        labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
    }

    private fun doSaveAddress() {
        setSaveAddressDataModel()
        saveDataModel?.let { viewModel.saveAddress(it) }
    }

    private fun doSaveEditAddress() {
        setSaveAddressDataModel()
        saveDataModel?.let {
            if (currentLat != 0.0 && currentLong != 0.0) {
                binding?.loaderAddressForm?.visibility = View.VISIBLE
                viewModel.validatePinpoint(it)
            } else {
                viewModel.saveEditAddress(it)
            }
        }
    }

    private fun setSaveAddressDataModel() {
        if (currentLat != 0.0 && currentLong != 0.0) {
            saveDataModel?.address2 =
                "$currentLat,$currentLong"
        }
        binding?.run {
            saveDataModel?.receiverName = formAccount.etNamaPenerima.textFieldInput.text.toString()
            saveDataModel?.phone = formAccount.etNomorHp.textFieldInput.text.toString()
            saveDataModel?.isTokonowRequest = viewModel.isTokonow
            if (isPositiveFlow) {
                if (formAddress.etCourierNote.textFieldInput.text.isNotEmpty()) {
                    saveDataModel?.address1 = "${formAddress.etAlamatNew.textFieldInput.text}"
                    saveDataModel?.address1Notes =
                        formAddress.etCourierNote.textFieldInput.text.toString()
                } else {
                    saveDataModel?.address1 = "${formAddress.etAlamatNew.textFieldInput.text}"
                }
                saveDataModel?.addressName = formAddress.etLabel.textFieldInput.text.toString()
                saveDataModel?.isAnaPositive = PARAM_ANA_POSITIVE
            } else {
                if (formAddressNegative.etCourierNote.textFieldInput.text.isNotEmpty()) {
                    saveDataModel?.address1 = "${formAddressNegative.etAlamat.textFieldInput.text}"
                    saveDataModel?.address1Notes =
                        formAddressNegative.etCourierNote.textFieldInput.text.toString()
                } else {
                    saveDataModel?.address1 = "${formAddressNegative.etAlamat.textFieldInput.text}"
                }
                saveDataModel?.addressName =
                    formAddressNegative.etLabel.textFieldInput.text.toString()
                saveDataModel?.isAnaPositive = PARAM_ANA_NEGATIVE
            }
        }

        if (userSession.name.isNotEmpty() && userSession.name.contains(
                toppers,
                ignoreCase = true
            )
        ) {
            saveDataModel?.applyNameAsNewUserFullname = true
        }
    }

    private fun onSuccessAddAddress() {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_ADDRESS_NEW, saveDataModel)
                }
            )
            finish()
        }
    }

    private fun onSuccessEditAddress(isEditChosenAddress: Boolean) {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_EDIT_ADDRESS, saveDataModel?.id?.toString())
                    putExtra(EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED, isEditChosenAddress)
                }
            )
            finish()
        }
    }

    fun showDialogBackButton() {
        if (backDialog?.isShowing != true) {
            if (backDialog != null) {
                backDialog?.show()
            } else {
                backDialog = context?.let {
                    DialogUnify(
                        it,
                        DialogUnify.HORIZONTAL_ACTION,
                        DialogUnify.NO_IMAGE
                    )
                }
                backDialog?.apply {
                    setOverlayClose(false)
                    setCancelable(false)
                    setTitle(getString(R.string.editaddress_back_dialog_title))
                    setDescription(getString(R.string.editaddress_back_dialog_description))
                    setPrimaryCTAText(getString(R.string.editaddress_back_dialog_cta))
                    setPrimaryCTAClickListener {
                        isBackDialogClicked = true
                        activity?.onBackPressed()
                        backDialog?.dismiss()
                    }
                    setSecondaryCTAText(getString(R.string.editaddress_back_dialog_secondary_cta))
                    setSecondaryCTAClickListener {
                        isBackDialogClicked = false
                        backDialog?.dismiss()
                    }
                    show()
                }
            }
        }
    }

    companion object {

        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        const val REQUEST_CODE_CONTACT_PICKER = 99
        private const val MIN_CHAR_PHONE_NUMBER = 9
        private const val MAX_CHAR_PHONE_NUMBER = 15
        private const val MIN_CHAR_ADDRESS_LABEL = 3
        private const val MIN_CHAR_RECEIVER_NAME = 2
        private const val MAX_CHAR_ALAMAT = 200
        private const val MAX_CHAR_NOTES = 45

        const val REQUEST_PINPONT_PAGE = 1998
        const val PARAM_ANA_POSITIVE = "1"
        const val PARAM_ANA_NEGATIVE = "0"

        const val SUCCESS = "success"
        const val NOT_SUCCESS = "not success"

        fun newInstance(extra: Bundle): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(
                        EXTRA_SAVE_DATA_UI_MODEL,
                        extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL)
                    )
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, extra.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putString(EXTRA_KOTA_KECAMATAN, extra.getString(EXTRA_KOTA_KECAMATAN))
                    putBoolean(EXTRA_IS_EDIT, false)
                    putBoolean(EXTRA_GMS_AVAILABILITY, extra.getBoolean(EXTRA_GMS_AVAILABILITY))
                    putString(PARAM_SOURCE, extra.getString(PARAM_SOURCE, ""))
                }
            }
        }

        fun newInstance(addressId: String?, extra: Bundle?): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ADDRESS_ID, addressId)
                    putBoolean(EXTRA_IS_EDIT, true)

                    if (extra != null) {
                        putString(PARAM_SOURCE, extra.getString(PARAM_SOURCE, ""))
                    }
                }
            }
        }
    }

    override fun onLabelAlamatChipClicked(labelAlamat: String) {
        binding?.run {
            val rvChips =
                if (isPositiveFlow) formAddress.rvLabelAlamatChips else formAddressNegative.rvLabelAlamatChips
            val field = if (isPositiveFlow) formAddress.etLabel else formAddressNegative.etLabel
            if (!isEdit) {
                rvChips.visibility = View.GONE
            }
            field.textFieldInput.run {
                if (!isEdit) {
                    AddNewAddressRevampAnalytics.onClickChipsLabelAlamatPositive(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickChipsLabelAlamat(userSession.userId)
                }
                setText(labelAlamat)
                setSelection(field.textFieldInput.text.length)
            }
        }
    }

    override fun onGetDistrict(districtAddress: Address) {
        districtBottomSheet?.getDistrict(districtAddress)
    }

    override fun onChooseZipcode(
        districtAddress: Address,
        postalCode: String,
        isPinpoint: Boolean
    ) {
        val kotaKecamatanText =
            "${districtAddress.districtName}, ${districtAddress.cityName}, ${districtAddress.provinceName}"
        formattedAddress = kotaKecamatanText
        currentDistrictName = districtAddress.districtName.toString()
        binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.run {
            setText(kotaKecamatanText)
            currentKotaKecamatan = kotaKecamatanText
        }

        val selectedDistrict =
            "${districtAddress.provinceName}, ${districtAddress.cityName}, ${districtAddress.districtName}"
        saveDataModel?.selectedDistrict = selectedDistrict
        saveDataModel?.cityId = districtAddress.cityId
        saveDataModel?.provinceId = districtAddress.provinceId
        saveDataModel?.districtId = districtAddress.districtId
        saveDataModel?.zipCodes = districtAddress.zipCodes
        saveDataModel?.postalCode = postalCode
        currentPostalCode = postalCode

        // reset lat long
        if (!isEdit) {
            currentLat = 0.0
            currentLong = 0.0
            saveDataModel?.latitude = ""
            saveDataModel?.longitude = ""
            this.isPinpoint = false
            binding?.run {
                cardAddressNegative.icLocation.setImage(IconUnify.LOCATION_OFF)
                cardAddressNegative.addressDistrict.text = context?.let {
                    HtmlLinkHelper(
                        it,
                        getString(R.string.tv_pinpoint_not_defined)
                    ).spannedString
                }
            }
        } else {
            showToasterInfo(getString(R.string.district_changed_success))
            focusOnDetailAddress()
        }

        if (isPinpoint) goToPinpointPage()
    }
}
