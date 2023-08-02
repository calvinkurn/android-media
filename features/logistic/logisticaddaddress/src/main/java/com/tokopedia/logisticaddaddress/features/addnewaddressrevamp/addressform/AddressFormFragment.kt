package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_EDIT_ADDRESS
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_EDIT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POLYGON
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_IS_POSITIVE_FLOW
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LAT
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_LONG
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_RESET_TO_SEARCH_PAGE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_SAVE_DATA_UI_MODEL
import com.tokopedia.logisticaddaddress.common.AddressConstants.KEY_SAVE_INSTANCE_SAVE_ADDRESS_DATA_MODEL
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentAddressFormBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.mapper.SaveAddressMapper
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.add_address.ContactData
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.LabelAlamatChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.FieldType
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetRevamp
import com.tokopedia.logisticaddaddress.utils.AddEditAddressUtil
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setWrapperError
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
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
                name = userSession.name,
                phone = userSession.phoneNumber,
                onViewEditAddressPageNew = {
                    EditAddressRevampAnalytics.onViewEditAddressPageNew(userSession.userId)
                }
            )
            checkMapsAvailability()
        }
        permissionCheckerHelper = PermissionCheckerHelper()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        setSaveAddressDataModel()
        outState.putParcelable(KEY_SAVE_INSTANCE_SAVE_ADDRESS_DATA_MODEL, viewModel.saveDataModel)

        super.onSaveInstanceState(outState)
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
        prepareData(savedInstanceState)
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
                if (viewModel.isDifferentLatLong(
                        addressDataFromPinpoint.latitude,
                        addressDataFromPinpoint.longitude
                    )
                ) {
                    if (viewModel.isDifferentDistrictId(addressDataFromPinpoint.districtId) && !viewModel.isPositiveFlow) {
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
                binding?.apply {
                    formAddressNegativeWidget.setDistrict(formattedAddress)
                    cardAddressPinpointWidget.setAddressDistrict(formattedAddress)
                }
            }

            if (viewModel.isHaveLatLong) {
                binding?.cardAddressNegativeWidget?.updateView(viewModel.isEdit)
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
            binding?.formAccountWidget?.setPhoneNumber(phoneNumberOnly)
            showToaster(getString(R.string.success_add_phone_number), Toaster.TYPE_NORMAL)
        }
    }

    private fun showToaster(
        message: String,
        toasterType: Int
    ) {
        view?.let { view ->
            Toaster.build(view, message, Toaster.LENGTH_SHORT, toasterType).show()
        }
    }

    private fun focusOnDetailAddress() {
        binding?.apply {
            if (viewModel.isPositiveFlow) {
                binding?.formAddressPositiveWidget?.setFocusEtAddress()
            } else {
                binding?.formAddressNegativeWidget?.setFocusEtAddress()
            }
        }
    }

    private fun prepareData(savedInstanceState: Bundle?) {
        val draftAddress = savedInstanceState?.getParcelable<SaveAddressDataModel>(
            KEY_SAVE_INSTANCE_SAVE_ADDRESS_DATA_MODEL
        )
        if (viewModel.isEdit) {
            binding?.loaderAddressForm?.visible()
            viewModel.getAddressDetail(draftAddress)
        } else {
            viewModel.getDefaultAddress(SOURCE_ADDRESS)
            draftAddress?.let { viewModel.saveDraftAddress(it) }
        }
    }

    private fun observeAddAddress() {
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

        viewModel.defaultAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (it.data.addressId != 0L) {
                        binding?.layoutCbDefaultLoc?.visible()
                    } else {
                        binding?.layoutCbDefaultLoc?.gone()
                    }
                }
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun observeEditAddress() {
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

        viewModel.pinpointValidation.observe(viewLifecycleOwner) {
            binding?.loaderAddressForm?.gone()
            when (it) {
                is Success -> {
                    if (it.data.result) {
                        viewModel.saveDataModel?.let { addressData ->
                            checkLocation(addressData)
                        }
                    } else {
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
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun initObserver() {
        observeAddressDetail()
        if (viewModel.isEdit) {
            observeEditAddress()
        } else {
            observeAddAddress()
        }
    }

    private fun observeAddressDetail() {
        viewModel.addressDetail.observe(viewLifecycleOwner) {
            binding?.loaderAddressForm?.gone()
            when (it) {
                is Success -> {
                    if (viewModel.isEdit) {
                        prepareEditLayout(it.data)
                    } else {
                        prepareLayout(it.data)
                    }
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message.toString(),
                        toasterType = Toaster.TYPE_ERROR
                    )
                }
                else -> {
                    // no-op
                }
            }
        }
    }

    private fun prepareLayout(addressData: SaveAddressDataModel) {
        setupLabelChips(LABEL_HOME)
        renderFormAccount(isEdit = false, data = addressData)
        setupCbDefaultLocListener()

        if (viewModel.isPositiveFlow) {
            renderPositiveFlow(data = addressData, isEdit = false)
        } else {
            renderNegativeFlow(data = addressData, isEdit = false)
        }

        setUserConsent()

        binding?.btnSaveAddressNew?.setOnClickListener {
            if (validateForm()) {
                doSaveAddress()
            }
        }
    }

    private fun renderFormAccount(isEdit: Boolean, data: SaveAddressDataModel) {
        binding?.apply {
            val receiverName = data.receiverName
            val phoneNumber = data.phone

            formAccountWidget.renderView(
                receiverName = receiverName,
                phoneNumber = phoneNumber,
                onClickPhoneNumberFirstIcon = {
                    if (isEdit) {
                        EditAddressRevampAnalytics.onClickIconPhoneBook(userSession.userId)
                    } else if (viewModel.isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickIconPhoneBookPositive(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickIconPhoneBookNegative(userSession.userId)
                    }

                    onNavigateToContact()
                },
                onClickBtnInfo = {
                    if (isEdit.not()) {
                        if (viewModel.isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickIconNamaPenerimaPositive(userSession.userId)
                        } else {
                            AddNewAddressRevampAnalytics.onClickIconNamaPenerimaNegative(userSession.userId)
                        }
                    }

                    showBottomSheetInfoPenerima()
                },
                isEdit = isEdit
            )

            formAccountWidget.setupOnTextChangeListener(
                hasFocusInputReceiverName = {
                    if (viewModel.isEdit) {
                        EditAddressRevampAnalytics.onClickFieldNamaPenerima(userSession.userId)
                    } else if (viewModel.isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaPositive(
                            userSession.userId
                        )
                    } else {
                        AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaNegative(
                            userSession.userId
                        )
                    }
                },
                hasFocusInputPhoneNumber = {
                    if (viewModel.isEdit) {
                        EditAddressRevampAnalytics.onClickFieldNomorHp(userSession.userId)
                    } else if (viewModel.isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickFieldNomorHpPositive(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickFieldNomorHpNegative(userSession.userId)
                    }
                }
            )
        }
    }

    private fun renderPositiveFlow(
        isEdit: Boolean,
        data: SaveAddressDataModel
    ) {
        binding?.run {
            setOnTouchLabelAddressPositiveFlow()
            setupRvLabelAddressChipsPositiveFlow()
            setupOnTextFocusListenerPositiveFlow()
            showPositiveLayout()
            if (isEdit) {
                cardAddressPinpointWidget.renderView(
                    formattedAddress = viewModel.saveDataModel?.formattedAddress
                ) {
                    goToPinpointPage()
                    EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                }
            } else {
                cardAddressPinpointWidget.setAddressDistrict(viewModel.saveDataModel?.formattedAddress)
            }
            formAddressPositiveWidget.renderView(
                data = data
            )
        }
    }

    private fun renderNegativeFlow(
        isEdit: Boolean,
        data: SaveAddressDataModel
    ) {
        binding?.run {
            setOnTouchLabelAddressNegativeFlow()
            setupRvLabelAddressChipsNegativeFlow()
            setupOnTextFocusListenerNegativeFlow()
            showNegativeLayout()
            setupNegativePinpointCard()

            binding?.apply {
                if (isEdit) {
                    cardAddressNegativeWidget.showBtnChangeNegative {
                        EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                        checkKotaKecamatan()
                    }
                    formAddressNegativeWidget.bindView(
                        formattedAddress = viewModel.saveDataModel?.formattedAddress,
                        data = data,
                        hasFocusEtDistrict = {
                            EditAddressRevampAnalytics.onClickFieldKotaKecamatan(userSession.userId)
                            showDistrictRecommendationBottomSheet(false)
                        },
                        onClickEtDistrictListener = {
                            EditAddressRevampAnalytics.onClickFieldKotaKecamatan(userSession.userId)
                            showDistrictRecommendationBottomSheet(false)
                        }
                    )
                } else {
                    val onFocusDistrict = {
                        AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(
                            userSession.userId
                        )
                        showDistrictRecommendationBottomSheet(false)
                    }
                    val onClickDistrict = {
                        AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(
                            userSession.userId
                        )
                        showDistrictRecommendationBottomSheet(false)
                    }

                    cardAddressNegativeWidget.setOnClickListener {
                        AddNewAddressRevampAnalytics.onClickAturPinpointNegative(userSession.userId)
                        checkKotaKecamatan()
                    }
                    formAddressNegativeWidget.bindView(
                        formattedAddress = viewModel.saveDataModel?.formattedAddress,
                        data = data,
                        hasFocusEtDistrict = onFocusDistrict,
                        onClickEtDistrictListener = onClickDistrict
                    )
                }
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

    private fun prepareEditLayout(data: SaveAddressDataModel) {
        setupLabelChips(data.addressName)
        renderFormAccount(isEdit = true, data = data)
        setupCbDefaultLocListener()

        if (viewModel.isPositiveFlow) {
            renderPositiveFlow(isEdit = true, data = data)
        } else {
            renderNegativeFlow(isEdit = true, data = data)
        }

        setUserConsent()

        binding?.btnSaveAddressNew?.setOnClickListener {
            if (validateForm()) {
                doSaveEditAddress()
            }
        }
    }

    private fun showPositiveLayout() {
        binding?.run {
            cardAddressPinpointWidget.visible()
            formAddressPositiveWidget.visible()
            formAddressNegativeWidget.gone()
            cardAddressNegativeWidget.gone()
        }
    }

    private fun showNegativeLayout() {
        binding?.run {
            cardAddressPinpointWidget.gone()
            formAddressPositiveWidget.gone()
            formAddressNegativeWidget.visible()
            cardAddressNegativeWidget.visible()
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
        var validated = true
        val field = arrayListOf<String>()

        viewModel.validateFields.forEach {
            when (it) {
                FieldType.PHONE_NUMBER -> {
                    validatePhoneNumber {
                        field.add(getString(R.string.field_nomor_hp))
                        validated = false
                    }
                }
                FieldType.RECEIVER_NAME -> {
                    validateReceiverName {
                        field.add(getString(R.string.field_nama_penerima))
                        validated = false
                    }
                }
                FieldType.COURIER_NOTE -> {
                    validateCourierNote {
                        field.add(getString(R.string.field_catatan_kurir))
                        validated = false
                    }
                }
                FieldType.ADDRESS -> {
                    validateAddress {
                        field.add(getString(R.string.field_alamat))
                        validated = false
                    }
                }
                FieldType.LABEL -> {
                    validateLabel {
                        field.add(getString(R.string.field_label_alamat))
                        validated = false
                    }
                }
            }
        }

        if (!validated) {
            if (viewModel.isEdit) {
                EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                EditAddressRevampAnalytics.onClickSimpanError(
                    userSession.userId,
                    field.joinToString(",")
                )
            } else if (viewModel.isPositiveFlow) {
                AddNewAddressRevampAnalytics.onClickSimpanErrorPositive(
                    userSession.userId,
                    field.joinToString(",")
                )
            } else {
                AddNewAddressRevampAnalytics.onClickSimpanErrorNegative(
                    userSession.userId,
                    field.joinToString(",")
                )
            }
        }

        return validated
    }

    private fun validatePhoneNumber(onError: () -> Unit) {
        viewModel.validatePhoneNumber(
            phoneNumber = binding?.formAccountWidget?.phoneNumber,
            onError = onError,
            onEmptyPhoneNumber = {
                binding?.formAccountWidget?.setWrapperErrorPhoneNumber()
            },
            onBelowMinCharacter = {
                showToaster(
                    message = getString(R.string.error_min_char_phone_number),
                    toasterType = Toaster.TYPE_ERROR
                )
            },
            onInvalidPhoneNumber = {
                showToaster(
                    message = getString(R.string.error_invalid_format_phone_number),
                    toasterType = Toaster.TYPE_ERROR
                )
            }
        )
    }

    private fun validateReceiverName(onError: () -> Unit) {
        viewModel.validateReceiverName(
            receiverName = binding?.formAccountWidget?.receiverName,
            onError = onError,
            onEmptyReceiverName = {
                binding?.formAccountWidget?.setWrapperErrorReceiverName()
            },
            onBelowMinCharacter = {
                showToaster(
                    message = getString(R.string.error_nama_penerima),
                    toasterType = Toaster.TYPE_ERROR
                )
            }
        )
    }

    private fun validateCourierNote(onError: () -> Unit) {
        binding?.run {
            if (viewModel.isPositiveFlow) {
                if (formAddressPositiveWidget.isErrorCourierNote) {
                    onError.invoke()
                }
            } else {
                if (formAddressNegativeWidget.isErrorCourierNote) {
                    onError.invoke()
                }
            }
        }
    }

    private fun validateAddress(onError: () -> Unit) {
        val field =
            if (viewModel.isPositiveFlow) binding?.formAddressPositiveWidget?.etAddressNew else binding?.formAddressNegativeWidget?.etAddress

        viewModel.validateAddress(
            address = field?.textFieldInput?.text.toString(),
            onError = onError,
            onEmptyAddress = {
                field?.apply {
                    setWrapperError(textFieldWrapper, getString(R.string.tv_error_field))
                }
            },
            onBelowMinCharacter = {
                showToaster(
                    message = getString(R.string.error_alamat),
                    toasterType = Toaster.TYPE_ERROR
                )
            },
            isErrorTextField = field?.textFieldWrapper?.error != null
        )
    }

    private fun validateLabel(onError: () -> Unit) {
        val field =
            if (viewModel.isPositiveFlow) binding?.formAddressPositiveWidget?.etLabel else binding?.formAddressNegativeWidget?.etLabel

        viewModel.validateLabel(
            label = field?.textFieldInput?.text.toString(),
            onError = onError,
            onEmptyLabel = {
                field?.apply {
                    setWrapperError(textFieldWrapper, getString(R.string.tv_error_field))
                }
            },
            onBelowMinCharacter = {
                showToaster(
                    message = getString(R.string.error_label_address),
                    toasterType = Toaster.TYPE_ERROR
                )
            }
        )
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
            isEdit = viewModel.isEdit,
            gmsAvailable = viewModel.isGmsAvailable
        )
        districtBottomSheet?.setListener(this)
        districtBottomSheet?.show(this.childFragmentManager)
    }

    private fun checkKotaKecamatan() {
        if (binding?.formAddressNegativeWidget?.isEmptyDistrict != false) {
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
        if (!viewModel.isPositiveFlow && !viewModel.isEdit) {
            bundle.putBoolean(
                EXTRA_IS_POLYGON,
                true
            )
        }
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

    private fun setupRvLabelAddressChipsPositiveFlow() {
        binding?.apply {
            formAddressPositiveWidget.setupRvLabelAddressChips(
                staticDimen8dp = staticDimen8dp,
                labelAlamatChipsLayoutManager = labelAlamatChipsLayoutManager,
                labelAlamatChipsAdapter = labelAlamatChipsAdapter
            )
        }
    }

    private fun setupRvLabelAddressChipsNegativeFlow() {
        binding?.apply {
            formAddressNegativeWidget.setupRvLabelAddressChips(
                staticDimen8dp = staticDimen8dp,
                labelAlamatChipsLayoutManager = labelAlamatChipsLayoutManager,
                labelAlamatChipsAdapter = labelAlamatChipsAdapter
            )
        }
    }

    private fun setupNegativePinpointCard() {
        binding?.cardAddressNegativeWidget?.setupNegativePinpointCard(
            hasPinPoint = viewModel.saveDataModel?.hasPinpoint(),
            isEdit = viewModel.isEdit
        )
    }

    private fun setupCbDefaultLocListener() {
        binding?.apply {
            cbDefaultLoc.setOnCheckedChangeListener { _, isChecked ->
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
        }
    }

    private fun setupOnTextFocusListenerPositiveFlow() {
        binding?.apply {
            formAddressPositiveWidget.setOnTextFocusListener(
                hasFocusEtLabel = {
                    eventShowListLabelAlamat()
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldLabelAlamatPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldLabelAlamat(userSession.userId)
                    }
                },
                hasFocusEtAddressNew = {
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldAlamatPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldAlamat(userSession.userId)
                    }
                },
                hasFocusEtCourierNote = {
                    if (!viewModel.isEdit) {
                        AddNewAddressRevampAnalytics.onClickFieldCatatanKurirPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldCatatanKurir(userSession.userId)
                    }
                }
            )
        }
    }

    private fun setupOnTextFocusListenerNegativeFlow() {
        binding?.apply {
            formAddressNegativeWidget.setOnTextFocusListener(
                hasFocusEtLabel = {
                    eventShowListLabelAlamat()
                    if (viewModel.isEdit) {
                        EditAddressRevampAnalytics.onClickFieldLabelAlamat(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickFieldLabelAlamatNegative(userSession.userId)
                    }
                },
                hasFocusEtAddress = {
                    if (viewModel.isEdit) {
                        EditAddressRevampAnalytics.onClickFieldAlamat(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickFieldAlamatNegative(userSession.userId)
                    }
                },
                hasFocusEtCourierNote = {
                    if (viewModel.isEdit) {
                        EditAddressRevampAnalytics.onClickFieldCatatanKurir(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickFieldCatatanKurirNegative(userSession.userId)
                    }
                }
            )
        }
    }

    private fun setOnTouchLabelAddressPositiveFlow() {
        binding?.run {
            formAddressPositiveWidget.setOnTouchLabelAddress(
                showListLabelAlamat = {
                    eventShowListLabelAlamat()
                },
                onAfterTextChanged = { text, rvChips ->
                    doUpdateLabel(text, rvChips)
                }
            )
        }
    }

    private fun setOnTouchLabelAddressNegativeFlow() {
        binding?.run {
            formAddressNegativeWidget.setOnTouchLabelAddress(
                showListLabelAlamat = {
                    eventShowListLabelAlamat()
                },
                onAfterTextChanged = { text, rvChips ->
                    doUpdateLabel(text, rvChips)
                }
            )
        }
    }

    private fun doUpdateLabel(text: String, rvChips: RecyclerView?) {
        val currentLabel = labelAlamatList
        labelAlamatList = currentLabel.map { item ->
            item.copy(
                second = item.first.equals(
                    text,
                    ignoreCase = true
                )
            )
        }.toTypedArray()
        val filterList = labelAlamatList.filter {
            it.first.contains(text, true)
        }
        if (!viewModel.isEdit) {
            labelAlamatChipsAdapter.submitList(filterList)
        } else {
            if (text.isNotEmpty() && filterList.isEmpty()) {
                rvChips?.gone()
            } else {
                rvChips?.visible()
                labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
            }
        }
    }

    private fun eventShowListLabelAlamat() {
        if (viewModel.isPositiveFlow) {
            binding?.formAddressPositiveWidget?.showLabelAddressList()
        } else {
            binding?.formAddressNegativeWidget?.showLabelAddressList()
        }
        binding?.formAddressPositiveWidget?.setLayoutDirection()
        labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
    }

    private fun doSaveAddress() {
        setSaveAddressDataModel()
        viewModel.saveAddress(
            consentJson = binding?.userConsentWidget?.generatePayloadData().orEmpty()
        )
    }

    private fun doSaveEditAddress() {
        setSaveAddressDataModel()
        viewModel.saveDataModel?.let {
            if (it.hasPinpoint()) {
                binding?.loaderAddressForm?.visible()
                viewModel.validatePinpoint(it)
            } else {
                checkLocation(it)
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
                address1 = formAddressPositiveWidget.address
                address1Notes = formAddressPositiveWidget.courierNote
                addressName = formAddressPositiveWidget.label
                isAnaPositive = PARAM_ANA_POSITIVE
            } else {
                address1 = formAddressNegativeWidget.address
                address1Notes = formAddressNegativeWidget.courierNote
                addressName = formAddressNegativeWidget.label
                isAnaPositive = PARAM_ANA_NEGATIVE
            }

            viewModel.updateDataSaveModel(
                receiverName = formAccountWidget.receiverName,
                phoneNo = formAccountWidget.phoneNumber,
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

    private fun checkLocation(addressData: SaveAddressDataModel) {
        if (viewModel.isDifferentLocation(
                address1 = addressData.address1,
                address2 = addressData.address2
            )
        ) {
            showDifferentLocationDialog(addressData)
        } else {
            viewModel.saveEditAddress(addressData)
        }
    }

    private fun showDifferentLocationDialog(
        addressData: SaveAddressDataModel
    ) {
        activity?.apply {
            DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.title_edit_address_confirmation_dialog))
                setDescription(getString(R.string.description_edit_address_confirmation_dialog))
                setPrimaryCTAText(getString(R.string.btn_simpan))
                setPrimaryCTAClickListener {
                    dismiss()
                    viewModel.saveEditAddress(addressData)
                }
                setSecondaryCTAText(getString(R.string.btn_back))
                setSecondaryCTAClickListener { dismiss() }
                show()
            }
        }
    }

    private fun setUserConsent() {
        binding?.userConsentWidget?.visible()

        binding?.userConsentWidget?.apply {
            setBtnSaveAddressEnable(viewModel.isEdit)
            setOnCheckedChangeListener { isChecked ->
                setBtnSaveAddressEnable(isChecked)
            }
            setOnFailedGetCollectionListener {
                setBtnSaveAddressEnable(true)
            }
        }?.load(
            viewLifecycleOwner,
            this,
            ConsentCollectionParam(
                collectionId = viewModel.getCollectionId()
            )
        )
    }

    private fun setBtnSaveAddressEnable(isEnabled: Boolean) {
        binding?.btnSaveAddressNew?.isEnabled = isEnabled
    }

    companion object {
        private const val SOURCE_ADDRESS = "address"
        const val LABEL_HOME = "Rumah"
        const val TOPPERS = "Toppers-"
        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        const val REQUEST_CODE_CONTACT_PICKER = 99
        const val MAX_CHAR_PHONE_NUMBER = 15
        const val MAX_CHAR_ALAMAT = 200
        const val MAX_CHAR_NOTES = 45

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
                if (viewModel.isPositiveFlow) formAddressPositiveWidget.rvLabelAddressChips else formAddressNegativeWidget.rvLabelAddressChips
            val field =
                if (viewModel.isPositiveFlow) formAddressPositiveWidget.etLabel else formAddressNegativeWidget.etLabel
            if (!viewModel.isEdit) {
                rvChips?.visibility = View.GONE
            }
            field?.textFieldInput?.run {
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
        binding?.formAddressNegativeWidget?.setDistrict(
            viewModel.saveDataModel?.formattedAddress
        )
        // reset lat long
        if (!viewModel.isEdit) {
            viewModel.clearLatLong()
            binding?.cardAddressNegativeWidget?.setNotYetPinPoint()
        } else {
            showToaster(getString(R.string.district_changed_success), Toaster.TYPE_NORMAL)
            focusOnDetailAddress()
        }

        if (isPinpoint) goToPinpointPage()
    }
}
