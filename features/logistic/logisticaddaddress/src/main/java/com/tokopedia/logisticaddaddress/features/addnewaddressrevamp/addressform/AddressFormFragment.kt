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
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_EDIT_ADDRESS
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.response.DistrictItem
import com.tokopedia.logisticCommon.util.LogisticUserConsentHelper
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POLYGON
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_KOTA_KECAMATAN
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_RESET_TO_SEARCH_PAGE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentAddressFormBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
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

    private var labelAlamatList: Array<Pair<String, Boolean>> = emptyArray()
    private var staticDimen8dp: Int? = 0

    var isBackDialogClicked: Boolean = false
    private var backDialog: DialogUnify? = null

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

    @Inject
    lateinit var saveAddressMapper: SaveAddressMapper

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
            viewModel.setDataFromArguments(
                isEdit = it.getBoolean(EXTRA_IS_EDIT, false),
                saveDataModel = it.getParcelable(EXTRA_SAVE_DATA_UI_MODEL),
                isPositiveFlow = it.getBoolean(EXTRA_IS_POSITIVE_FLOW),
                addressId = it.getString(EXTRA_ADDRESS_ID, ""),
                source = it.getString(PARAM_SOURCE, ""),
                onViewEditAddressPageNew = {
                    EditAddressRevampAnalytics.onViewEditAddressPageNew(userSession.userId)
                }
            )
            checkMapsAvailability()
        }
        permissionCheckerHelper = PermissionCheckerHelper()
    }

    private fun checkMapsAvailability() {
        val gmsAvailable = if (viewModel.isEdit) {
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

        // if user make any changes from pinpoint page, then update data in this page
        if (addressDataFromPinpoint != null) {
            if (viewModel.isEdit) {
                if (addressDataFromPinpoint.latitude != viewModel.saveDataModel?.latitude || addressDataFromPinpoint.longitude != viewModel.saveDataModel?.longitude) {
                    if (addressDataFromPinpoint.districtId != viewModel.saveDataModel?.districtId && !viewModel.isPositiveFlow) {
                        showToaster(
                            getString(R.string.change_pinpoint_outside_district),
                            Toaster.TYPE_NORMAL
                        )
                    } else {
                        showToaster(
                            getString(R.string.change_pinpoint_edit_address),
                            Toaster.TYPE_NORMAL
                        )
                    }
                    focusOnDetailAddress()
                }
            }

            addressDataFromPinpoint.apply {
                viewModel.saveDataModel = this
                binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.setText(
                    formattedAddress
                )
                binding?.cardAddressPinpoint?.addressDistrict?.text =
                    formattedAddress
            }

            if (viewModel.isHaveLatLong) {
                binding?.cardAddressNegative?.icLocation?.setImage(IconUnify.LOCATION)
                binding?.cardAddressNegative?.addressDistrict?.text = if (viewModel.isEdit) {
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
            val phoneNumberOnly = viewModel.removeSpecialChars(contact?.contactNumber.toString())
            binding?.formAccount?.etNomorHp?.textFieldInput?.setText(phoneNumberOnly)
            showToaster(getString(R.string.success_add_phone_number), Toaster.TYPE_NORMAL)
        }
    }

    private fun showToaster(
        message: String,
        toasterType: Int,
    ) {
        view?.let { view ->
            Toaster.build(view, message, Toaster.LENGTH_SHORT, toasterType).show()
        }
    }

    private fun focusOnDetailAddress() {
        if (!viewModel.isPositiveFlow) {
            binding?.formAddressNegative?.etAlamat?.textFieldInput?.requestFocus()
        } else {
            binding?.formAddress?.etAlamatNew?.textFieldInput?.requestFocus()
        }
    }

    private fun prepareData() {
        if (viewModel.isEdit) {
            binding?.loaderAddressForm?.visibility = View.VISIBLE
            viewModel.getAddressDetail()
        } else {
            viewModel.getDefaultAddress("address")
            if (viewModel.isPositiveFlow) {
                viewModel.getDistrictDetail()
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
                    if (viewModel.isPositiveFlow) {
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

                is Fail -> {
                    if (viewModel.isPositiveFlow) {
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
                    showToaster(
                        message = it.throwable.message.toString(),
                        toasterType = Toaster.TYPE_ERROR
                    )
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
                    showToaster(
                        message = it.throwable.message.toString(),
                        toasterType = Toaster.TYPE_ERROR
                    )
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
                    prepareEditLayout(it.data)
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.toString(),
                        toasterType = Toaster.TYPE_ERROR
                    )
                }
            }
        }

        viewModel.pinpointValidation.observe(viewLifecycleOwner) {
            binding?.loaderAddressForm?.visibility = View.GONE
            when (it) {
                is Success -> {
                    if (it.data.result.not()) {
                        showToaster(
                            message = getString(R.string.error_district_pinpoint_mismatch),
                            toasterType = Toaster.TYPE_ERROR
                        )
                        EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    }
                }
                is Fail -> {
                    EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    showToaster(
                        message = it.throwable.message.toString(),
                        toasterType = Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun prepareLayout(data: DistrictItem?) {
        setupLabelChips("Rumah")
        binding?.run {
            if (userSession.name.isNotEmpty() && !userSession.name.contains(
                    TOPPERS,
                    ignoreCase = true
                )
            ) {
                formAccount.etNamaPenerima.textFieldInput.setText(userSession.name)
                formAccount.infoNameLayout.visibility = View.GONE
            } else if (userSession.name.contains(TOPPERS, ignoreCase = true)) {
                formAccount.etNamaPenerima.textFieldWrapper.helperText =
                    getString(R.string.helper_nama_penerima)
            }
            setupFormAccount()
            formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                if (viewModel.isPositiveFlow) {
                    AddNewAddressRevampAnalytics.onClickIconPhoneBookPositive(userSession.userId)
                } else {
                    AddNewAddressRevampAnalytics.onClickIconPhoneBookNegative(userSession.userId)
                }
                onNavigateToContact()
            }
            formAccount.btnInfo.setOnClickListener {
                if (viewModel.isPositiveFlow) {
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
        if (!viewModel.isPositiveFlow) {
            showNegativeLayout()
            setupNegativePinpointCard()
            binding?.run {
                cardAddressNegative.root.setOnClickListener {
                    AddNewAddressRevampAnalytics.onClickAturPinpointNegative(userSession.userId)
                    checkKotaKecamatan()
                }

                formAddressNegative.etKotaKecamatan.textFieldInput.setText(viewModel.saveDataModel?.formattedAddress)
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
            }
        } else {
            binding?.run {
                showPositiveLayout()

                cardAddressPinpoint.addressDistrict.text = viewModel.saveDataModel?.formattedAddress

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
            if (viewModel.isPositiveFlow) LogisticUserConsentHelper.ANA_REVAMP_POSITIVE else LogisticUserConsentHelper.ANA_REVAMP_NEGATIVE
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
    private fun prepareEditLayout(data: SaveAddressDataModel) {
        setupLabelChips(data.addressName)
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

        setOnTouchLabelAddress()
        setupRvLabelAlamatChips()
        setTextListener()

        if (!viewModel.isPositiveFlow) {
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
                        setText(viewModel.saveDataModel?.formattedAddress)
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
                        textFieldInput.setText(data.addressName)
                        textFieldInput.addTextChangedListener(
                            setWrapperWatcher(
                                textFieldWrapper,
                                null
                            )
                        )
                    }
                    rvLabelAlamatChips.visibility = View.GONE
                    etAlamat.run {
                        textFieldInput.setText(data.address1)
                        if (data.address1.length > MAX_CHAR_ALAMAT) {
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
                        textFieldInput.setText(data.address1Notes)
                        if (data.address1Notes.length > MAX_CHAR_NOTES) {
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
                }
            }
        } else {
            showPositiveLayout()
            binding?.run {
                cardAddressPinpoint.run {
                    context?.let {
                        btnChange.visible()
                        btnChange.setOnClickListener {
                            goToPinpointPage()
                            EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                        }
                    }
                    addressDistrict.text = viewModel.saveDataModel?.formattedAddress
                    tvPinpointTitle.visibility = View.VISIBLE
                }
                formAddress.run {
                    etAlamatNew.run {
                        textFieldInput.setText(data.address1)
                        if (data.address1.length > MAX_CHAR_ALAMAT) {
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
                        textFieldInput.setText(data.address1Notes)
                        if (data.address1Notes.length > MAX_CHAR_NOTES) {
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
                        textFieldInput.setText(data.addressName)
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
        viewModel.validated = true
        val field = mutableListOf<String>()
        if (viewModel.isPositiveFlow) {
            if (!validatePhoneNumber()) {
                field.add(getString(R.string.field_nomor_hp))
                viewModel.validated = false
            }
            if (!validateReceiverName()) {
                field.add(getString(R.string.field_nama_penerima))
                viewModel.validated = false
            }
            if (!validateCourierNote()) {
                field.add(getString(R.string.field_catatan_kurir))
                viewModel.validated = false
            }
            if (!validateAlamat()) {
                field.add(getString(R.string.field_alamat))
                viewModel.validated = false
            }
            if (!validateLabel()) {
                field.add(getString(R.string.field_label_alamat))
                viewModel.validated = false
            }
        } else {
            if (!validateCourierNote()) {
                field.add(getString(R.string.field_catatan_kurir))
                viewModel.validated = false
            }
            if (!validateAlamat()) {
                field.add(getString(R.string.field_alamat))
                viewModel.validated = false
            }
            if (!validateLabel()) {
                field.add(getString(R.string.field_label_alamat))
                viewModel.validated = false
            }
            if (!validatePhoneNumber()) {
                field.add(getString(R.string.field_nomor_hp))
                viewModel.validated = false
            }
            if (!validateReceiverName()) {
                field.add(getString(R.string.field_nama_penerima))
                viewModel.validated = false
            }
        }

        if (!viewModel.isEdit) {
            if (!viewModel.validated && viewModel.isPositiveFlow) {
                AddNewAddressRevampAnalytics.onClickSimpanErrorPositive(
                    userSession.userId,
                    field.joinToString(",")
                )
            } else if (!viewModel.validated && !viewModel.isPositiveFlow) {
                AddNewAddressRevampAnalytics.onClickSimpanErrorNegative(
                    userSession.userId,
                    field.joinToString(",")
                )
            }
        } else {
            if (!viewModel.validated) {
                EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                EditAddressRevampAnalytics.onClickSimpanError(
                    userSession.userId,
                    field.joinToString(",")
                )
            }
        }
        return viewModel.validated
    }

    private fun validateReceiverName(): Boolean {
        binding?.formAccount?.etNamaPenerima?.let { field ->
            val receiverName = field.textFieldInput.text.toString()
            return if (receiverName.length < MIN_CHAR_RECEIVER_NAME) {
                if (receiverName.isEmpty() || receiverName == " ") {
                    setWrapperError(field.textFieldWrapper, getString(R.string.tv_error_field))
                }
                showToaster(
                    message = getString(R.string.error_nama_penerima),
                    toasterType = Toaster.TYPE_ERROR
                )
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
                showToaster(
                    message = getString(R.string.error_min_char_phone_number),
                    toasterType = Toaster.TYPE_ERROR
                )
                false
            } else if (!viewModel.isPhoneNumberValid(phoneNumber)) {
                showToaster(
                    message = getString(R.string.error_invalid_format_phone_number),
                    toasterType = Toaster.TYPE_ERROR
                )
                false
            } else {
                true
            }
        }
        return false
    }

    private fun validateCourierNote(): Boolean {
        binding?.run {
            return if (viewModel.isPositiveFlow) {
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
                if (viewModel.isPositiveFlow) formAddress.etAlamatNew else formAddressNegative.etAlamat
            val alamat = field.textFieldInput.text.toString()
            return if (alamat.length < MIN_CHAR_ADDRESS_LABEL) {
                if (alamat.isEmpty() || alamat == " ") {
                    setWrapperError(field.textFieldWrapper, getString(R.string.tv_error_field))
                }
                showToaster(
                    message = getString(R.string.error_alamat),
                    toasterType = Toaster.TYPE_ERROR
                )
                false
            } else {
                field.textFieldWrapper.error == null
            }
        }
        return false
    }

    private fun validateLabel(): Boolean {
        binding?.run {
            val field =
                if (viewModel.isPositiveFlow) formAddress.etLabel else formAddressNegative.etLabel
            val label = field.textFieldInput.text.toString()
            return if (label.length < MIN_CHAR_ADDRESS_LABEL) {
                if (label.isEmpty() || label == " ") {
                    setWrapperError(field.textFieldWrapper, getString(R.string.tv_error_field))
                }
                showToaster(
                    message = getString(R.string.error_label_address),
                    toasterType = Toaster.TYPE_ERROR
                )
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
                } else if (s.isEmpty() && viewModel.isEdit) {
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
        districtBottomSheet = DiscomBottomSheetRevamp(
            isPinpoint = isPinpoint,
            isEdit = viewModel.isEdit,
            isGmsAvailable = viewModel.isGmsAvailable
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
        viewModel.saveDataModel?.run {
            bundle.putDouble(EXTRA_LAT, latitude.toDoubleOrZero())
            bundle.putDouble(EXTRA_LONG, longitude.toDoubleOrZero())
        }
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, viewModel.isPositiveFlow)
        bundle.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, viewModel.saveDataModel)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, true)
        bundle.putBoolean(EXTRA_IS_EDIT, viewModel.isEdit)
        bundle.putBoolean(EXTRA_GMS_AVAILABILITY, viewModel.isGmsAvailable)
        if (!viewModel.isPositiveFlow && !viewModel.isEdit) bundle.putBoolean(
            EXTRA_IS_POLYGON,
            true
        )
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
            showToaster(
                message = getString(R.string.contact_not_found),
                toasterType = Toaster.TYPE_ERROR
            )
        }
    }

    private fun setupRvLabelAlamatChips() {
        binding?.run {
            val field =
                if (viewModel.isPositiveFlow) formAddress.rvLabelAlamatChips else formAddressNegative.rvLabelAlamatChips
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
            if (viewModel.saveDataModel?.hasPinpoint() != true) {
                cardAddressNegative.icLocation.setImage(IconUnify.LOCATION_OFF)
                cardAddressNegative.addressDistrict.text =
                    if (viewModel.isEdit) {
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
                    if (viewModel.isEdit) {
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
                    if (!viewModel.isEdit) {
                        if (viewModel.isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaPositive(
                                userSession.userId
                            )
                        } else {
                            AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaNegative(
                                userSession.userId
                            )
                        }
                    }
                    viewModel.saveDataModel?.setAsPrimaryAddresss = true
                } else {
                    viewModel.saveDataModel?.setAsPrimaryAddresss = false
                }
            }

            formAccount.etNamaPenerima.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!viewModel.isEdit) {
                        if (viewModel.isPositiveFlow) {
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
                    if (!viewModel.isEdit) {
                        if (viewModel.isPositiveFlow) {
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
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldLabelAlamatPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldLabelAlamat(userSession.userId)
                    }
                }
            }

            formAddress.etAlamatNew.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldAlamatPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldAlamat(userSession.userId)
                    }
                }
            }

            formAddress.etCourierNote.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldCatatanKurirPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldCatatanKurir(userSession.userId)
                    }
                }
            }

            formAddressNegative.etLabel.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    eventShowListLabelAlamat()
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldLabelAlamatNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldLabelAlamat(userSession.userId)
                    }
                }
            }

            formAddressNegative.etAlamat.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldAlamatNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldAlamat(userSession.userId)
                    }
                }
            }

            formAddressNegative.etCourierNote.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!viewModel.isEdit) {
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
            val field =
                if (viewModel.isPositiveFlow) formAddress.etLabel else formAddressNegative.etLabel
            val rvChips =
                if (viewModel.isPositiveFlow) formAddress.rvLabelAlamatChips else formAddressNegative.rvLabelAlamatChips

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
                        if (!viewModel.isEdit) {
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
        if (viewModel.isPositiveFlow) {
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
        viewModel.saveAddress()
    }

    private fun doSaveEditAddress() {
        setSaveAddressDataModel()
        viewModel.saveDataModel?.let {
            if (it.hasPinpoint()) {
                binding?.loaderAddressForm?.visibility = View.VISIBLE
                viewModel.validatePinpoint(it)
            } else {
                viewModel.saveEditAddress(it)
            }
        }
    }

    private fun setSaveAddressDataModel() {
        if (viewModel.saveDataModel?.hasPinpoint() == true) {
            viewModel.saveDataModel?.address2 =
                "${viewModel.saveDataModel?.latitude},${viewModel.saveDataModel?.longitude}"
        }
        binding?.run {
            val address1: String
            val address1Notes: String
            val addressName: String
            val isAnaPositive: String

            if (viewModel.isPositiveFlow) {
                address1 = "${formAddress.etAlamatNew.textFieldInput.text}"
                address1Notes =
                    formAddress.etCourierNote.textFieldInput.text.toString()
                addressName = formAddress.etLabel.textFieldInput.text.toString()
                isAnaPositive = PARAM_ANA_POSITIVE
            } else {
                address1 = "${formAddressNegative.etAlamat.textFieldInput.text}"
                address1Notes =
                    formAddressNegative.etCourierNote.textFieldInput.text.toString()
                addressName =
                    formAddressNegative.etLabel.textFieldInput.text.toString()
                isAnaPositive = PARAM_ANA_NEGATIVE
            }

            viewModel.updateDataSaveModel(
                receiverName = formAccount.etNamaPenerima.textFieldInput.text.toString(),
                phoneNo = formAccount.etNomorHp.textFieldInput.text.toString(),
                address1 = address1,
                address1Notes = address1Notes,
                addressName = addressName,
                isAnaPositive = isAnaPositive
            )
        }

        if (userSession.name.isNotEmpty() && userSession.name.contains(
                TOPPERS,
                ignoreCase = true
            )
        ) {
            viewModel.saveDataModel?.applyNameAsNewUserFullname = true
        }
    }

    private fun onSuccessAddAddress() {
        activity?.run {
            setResult(
                Activity.RESULT_OK,
                Intent().apply {
                    putExtra(EXTRA_ADDRESS_NEW, viewModel.saveDataModel)
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
                    putExtra(EXTRA_EDIT_ADDRESS, viewModel.saveDataModel?.id?.toString())
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
        private const val TOPPERS = "Toppers-"
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
                if (viewModel.isPositiveFlow) formAddress.rvLabelAlamatChips else formAddressNegative.rvLabelAlamatChips
            val field =
                if (viewModel.isPositiveFlow) formAddress.etLabel else formAddressNegative.etLabel
            if (!viewModel.isEdit) {
                rvChips.visibility = View.GONE
            }
            field.textFieldInput.run {
                if (!viewModel.isEdit) {
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
        viewModel.saveDataModel = saveAddressMapper.mapAddressModeltoSaveAddressDataModel(
            districtAddress,
            postalCode,
            viewModel.saveDataModel
        )
        binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.run {
            setText(viewModel.saveDataModel?.formattedAddress)
        }
        // reset lat long
        if (!viewModel.isEdit) {
            viewModel.clearLatLong()
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
            showToaster(getString(R.string.district_changed_success), Toaster.TYPE_NORMAL)
            focusOnDetailAddress()
        }

        if (isPinpoint) goToPinpointPage()
    }
}
