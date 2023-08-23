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
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.RecyclerView
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toDoubleOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_EDIT_ADDRESS
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED
import com.tokopedia.logisticCommon.data.constant.ManageAddressSource
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.uimodel.AddressUiState
import com.tokopedia.logisticCommon.uimodel.isAdd
import com.tokopedia.logisticCommon.uimodel.isEdit
import com.tokopedia.logisticCommon.uimodel.toAddressUiState
import com.tokopedia.logisticCommon.util.MapsAvailabilityHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_ID
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_ADDRESS_STATE
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_FROM_ADDRESS_FORM
import com.tokopedia.logisticaddaddress.common.AddressConstants.EXTRA_GMS_AVAILABILITY
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
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.analytics.AddNewAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.analytics.EditAddressRevampAnalytics
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform.widget.BaseFormAddressWidget
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.uimodel.FieldType
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomActivity.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetRevamp
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.Companion.ARGUMENT_ADDRESS_STATE
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.Companion.ARGUMENT_IS_PINPOINT
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_IS_PINPOINT
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomFragment.Companion.INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODE
import com.tokopedia.logisticaddaddress.features.pinpoint.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.utils.AddEditAddressUtil
import com.tokopedia.logisticaddaddress.utils.TextInputUtil.setWrapperError
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.url.Env
import com.tokopedia.url.TokopediaUrl
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class AddressFormFragment :
    BaseDaggerFragment(),
    LabelAlamatChipsAdapter.ActionListener {

    companion object {
        private const val SOURCE_ADDRESS = "address"
        const val LABEL_HOME = "Rumah"
        const val TOPPERS = "Toppers-"
        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        const val MAX_CHAR_PHONE_NUMBER = 15
        const val MAX_CHAR_ALAMAT = 200
        const val MAX_CHAR_NOTES = 45

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
                    putString(EXTRA_ADDRESS_STATE, extra.getString(EXTRA_ADDRESS_STATE))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, extra.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putBoolean(EXTRA_GMS_AVAILABILITY, extra.getBoolean(EXTRA_GMS_AVAILABILITY))
                    putString(PARAM_SOURCE, extra.getString(PARAM_SOURCE, ""))
                }
            }
        }

        fun newInstance(addressId: String?, extra: Bundle?): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ADDRESS_ID, addressId)
                    putString(EXTRA_ADDRESS_STATE, AddressUiState.EditAddress.name)

                    if (extra != null) {
                        putString(PARAM_SOURCE, extra.getString(PARAM_SOURCE, ""))
                    }
                }
            }
        }
    }

    private var binding by autoClearedNullable<FragmentAddressFormBinding>()
    private var bottomSheetInfoPenerima: BottomSheetUnify? = null
    private var districtBottomSheet: DiscomBottomSheetRevamp? = null

    // edit address
    private var isBackDialogClicked: Boolean = false
    private var backDialog: DialogUnify? = null
    private var addressId: String = ""

    // address label
    private var labelAlamatList: Array<Pair<String, Boolean>> = emptyArray()
    private var staticDimen8dp: Int? = 0
    private var labelAlamatChipsAdapter: LabelAlamatChipsAdapter? = null
    private var labelAlamatChipsLayoutManager: ChipsLayoutManager? = null

    // use current location
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var isGmsAvailable: Boolean = true

    private var source: String = ""
    private val isTokonow: Boolean
        get() = source == ManageAddressSource.TOKONOW.source
    private val sourceValue: String
        get() = if (isTokonow) {
            ManageAddressSource.LOCALIZED_ADDRESS_WIDGET.source
        } else {
            source
        }

    // page state
    private var isPositiveFlow: Boolean = true
    private var addressUiState: AddressUiState = AddressUiState.AddAddress

    // address validations
    private val validateFields: ArrayList<FieldType>
        get() = if (isPositiveFlow) {
            validatePositiveFlow
        } else {
            validateNegativeFlow
        }

    private val validatePositiveFlow = arrayListOf(
        FieldType.PHONE_NUMBER,
        FieldType.RECEIVER_NAME,
        FieldType.COURIER_NOTE,
        FieldType.ADDRESS,
        FieldType.LABEL
    )

    private val validateNegativeFlow = arrayListOf(
        FieldType.COURIER_NOTE,
        FieldType.ADDRESS,
        FieldType.LABEL,
        FieldType.PHONE_NUMBER,
        FieldType.RECEIVER_NAME
    )

    private val collectionId: String
        get() = if (TokopediaUrl.getInstance().TYPE == Env.STAGING) {
            if (addressUiState.isEdit()) {
                AddressConstants.EDIT_ADDRESS_COLLECTION_ID_STAGING
            } else {
                AddressConstants.ADD_ADDRESS_COLLECTION_ID_STAGING
            }
        } else {
            if (addressUiState.isEdit()) {
                AddressConstants.EDIT_ADDRESS_COLLECTION_ID_PRODUCTION
            } else {
                AddressConstants.ADD_ADDRESS_COLLECTION_ID_PRODUCTION
            }
        }

    private val viewModel: AddressFormViewModel by lazy {
        ViewModelProvider(this, viewModelFactory).get(AddressFormViewModel::class.java)
    }

    // activity results
    private val pinpointPageContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onPinpointResult(it.data)
        }
    }

    private val contactPickerContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            onContactPickerResult(it.data)
        }
    }

    private val discomContract = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK) {
            val address = it.data?.getParcelableExtra<Address>(INTENT_DISTRICT_RECOMMENDATION_ADDRESS)
            val postalCode = it.data?.getStringExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_ZIPCODE)
            val isPinpoint = it.data?.getBooleanExtra(INTENT_DISTRICT_RECOMMENDATION_ADDRESS_IS_PINPOINT, false)
            if (address != null && postalCode != null && isPinpoint != null) {
                onChooseZipcode(address, postalCode, isPinpoint)
            }
        }
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
        initData()
        checkMapsAvailability()
        permissionCheckerHelper = PermissionCheckerHelper()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareData(savedInstanceState)
        initObserver()
        setOnBackPressed()
    }

    override fun onPause() {
        super.onPause()
        dismissDistrictRecommendationBottomSheet()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        setSaveAddressDataModel()
        outState.putParcelable(KEY_SAVE_INSTANCE_SAVE_ADDRESS_DATA_MODEL, viewModel.saveDataModel)
        outState.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, viewModel.saveDataModel)
        outState.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        outState.putString(EXTRA_ADDRESS_ID, addressId)
        outState.putString(EXTRA_ADDRESS_STATE, addressUiState.name)
        outState.putString(PARAM_SOURCE, source)

        super.onSaveInstanceState(outState)
    }

    override fun onLabelAlamatChipClicked(labelAlamat: String) {
        binding?.addressForm?.run {
            if (addressUiState.isAdd()) {
                addressLabelChips?.visibility = View.GONE
            }
            addressLabelField?.textFieldInput?.run {
                if (addressUiState.isAdd()) {
                    AddNewAddressRevampAnalytics.onClickChipsLabelAlamatPositive(userSession.userId)
                } else {
                    EditAddressRevampAnalytics.onClickChipsLabelAlamat(userSession.userId)
                }
                setText(labelAlamat)
                setSelection(text.length)
            }
        }
    }

    private fun onChooseZipcode(
        districtAddress: Address,
        postalCode: String,
        isPinpoint: Boolean
    ) {
        viewModel.saveDataModel = SaveAddressMapper.mapAddressModeltoSaveAddressDataModel(
            districtAddress,
            postalCode,
            viewModel.saveDataModel
        )
        binding?.formAddressNegativeWidget?.setDistrict(
            viewModel.saveDataModel?.formattedAddress
        )
        // reset lat long
        when (addressUiState) {
            AddressUiState.EditAddress -> {
                showToaster(getString(R.string.district_changed_success), Toaster.TYPE_NORMAL)
                focusOnDetailAddress()
            }

            AddressUiState.AddAddress -> {
                viewModel.clearLatLong()
                binding?.cardAddressNegativeWidget?.setNotYetPinPoint()
            }

            else -> {
                // no op
            }
        }

        if (isPinpoint) goToPinpointPage()
    }

    private fun setOnBackPressed() {
        activity?.onBackPressedDispatcher?.addCallback(
            viewLifecycleOwner,
            object : OnBackPressedCallback(true) {
                override fun handleOnBackPressed() {
                    when (addressUiState) {
                        AddressUiState.AddAddress -> {
                            if (isPositiveFlow) {
                                AddNewAddressRevampAnalytics.onClickBackPositive(userSession.userId)
                            } else {
                                AddNewAddressRevampAnalytics.onClickBackNegative(userSession.userId)
                            }
                            activity?.finish()
                        }

                        AddressUiState.EditAddress -> {
                            EditAddressRevampAnalytics.onClickBackArrowEditAddress(userSession.userId)
                            if (isBackDialogClicked) {
                                activity?.finish()
                            } else {
                                showDialogBackButton()
                            }
                        }

                        else -> {
                            // no op
                        }
                    }
                }
            }
        )
    }

    private fun initData() {
        arguments?.apply {
            addressUiState = getString(EXTRA_ADDRESS_STATE).toAddressUiState()
            source = getString(PARAM_SOURCE, "")

            when (addressUiState) {
                AddressUiState.EditAddress -> {
                    EditAddressRevampAnalytics.onViewEditAddressPageNew(userSession.userId)
                    addressId = getString(EXTRA_ADDRESS_ID, "")
                }

                AddressUiState.AddAddress -> {
                    val addressModel = viewModel.generateSaveDataModel(
                        saveDataModel = getParcelable(EXTRA_SAVE_DATA_UI_MODEL),
                        defaultName = userSession.name,
                        defaultPhone = userSession.phoneNumber
                    )
                    viewModel.saveDraftAddress(addressModel)
                    isPositiveFlow = getBoolean(EXTRA_IS_POSITIVE_FLOW)
                }

                else -> {
                    // no op
                }
            }
        }
    }

    private fun checkMapsAvailability() {
        val gmsAvailable = if (addressUiState.isEdit()) {
            context?.let { ctx -> MapsAvailabilityHelper.isMapsAvailable(ctx) } ?: true
        } else {
            arguments?.getBoolean(EXTRA_GMS_AVAILABILITY, true) ?: true
        }
        isGmsAvailable = gmsAvailable
    }

    private fun onPinpointResult(data: Intent?) {
        val isResetToSearchPage = data?.getBooleanExtra(EXTRA_RESET_TO_SEARCH_PAGE, false) ?: false
        if (isResetToSearchPage) {
            activity?.run {
                setResult(Activity.RESULT_OK, Intent())
                finish()
            }
        }

        val addressDataFromPinpoint =
            data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_SAVE_DATA_UI_MODEL)
                ?: data?.getParcelableExtra(EXTRA_ADDRESS_NEW)

        // if user make any changes from pinpoint page, then update data in this page
        if (addressDataFromPinpoint != null) {
            if (addressUiState.isEdit()) {
                if (viewModel.isDifferentLatLong(
                        addressDataFromPinpoint.latitude,
                        addressDataFromPinpoint.longitude
                    )
                ) {
                    if (viewModel.isDifferentDistrictId(addressDataFromPinpoint.districtId) && !isPositiveFlow) {
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
                binding?.cardAddressNegativeWidget?.updateView(addressUiState.isEdit())
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
            addressForm.setFocusEtAddress()
        }
    }

    private fun prepareData(savedInstanceState: Bundle?) {
        val draftAddress = savedInstanceState?.getParcelable<SaveAddressDataModel>(
            KEY_SAVE_INSTANCE_SAVE_ADDRESS_DATA_MODEL
        )

        when (addressUiState) {
            AddressUiState.EditAddress -> {
                binding?.loaderAddressForm?.visible()
                viewModel.getAddressDetail(
                    addressId = addressId,
                    sourceValue = sourceValue,
                    draftAddressDataModel = draftAddress
                )
            }

            AddressUiState.AddAddress -> {
                viewModel.getDefaultAddress(SOURCE_ADDRESS)
                draftAddress?.let { viewModel.saveDraftAddress(it) }
            }

            else -> {
                // no op
            }
        }
    }

    private fun observeAddAddress() {
        viewModel.saveAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
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

        when (addressUiState) {
            AddressUiState.EditAddress -> {
                observeEditAddress()
            }

            AddressUiState.AddAddress -> {
                observeAddAddress()
            }

            else -> {
                // no op
            }
        }
    }

    private fun observeAddressDetail() {
        viewModel.addressDetail.observe(viewLifecycleOwner) {
            binding?.loaderAddressForm?.gone()
            when (it) {
                is Success -> {
                    if (addressUiState.isEdit()) {
                        isPositiveFlow = it.data.hasPinpoint().orFalse()
                    }
                    prepareLayout(it.data)
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
        setupLabelChips(if (addressUiState.isEdit()) addressData.addressName else LABEL_HOME)
        renderFormAccount(isEdit = addressUiState.isEdit(), data = addressData)
        setupCbDefaultLocListener()
        renderForm(isEdit = addressUiState.isEdit(), data = addressData)
        setUserConsent()

        binding?.btnSaveAddressNew?.setOnClickListener {
            if (validateForm()) {
                when (addressUiState) {
                    AddressUiState.AddAddress -> {
                        doSaveAddress()
                    }

                    AddressUiState.EditAddress -> {
                        doSaveEditAddress()
                    }

                    else -> {
                        // no op
                    }
                }
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
                    } else if (isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickIconPhoneBookPositive(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickIconPhoneBookNegative(userSession.userId)
                    }

                    onNavigateToContact()
                },
                onClickBtnInfo = {
                    if (isEdit.not()) {
                        if (isPositiveFlow) {
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
                    if (isEdit) {
                        EditAddressRevampAnalytics.onClickFieldNamaPenerima(userSession.userId)
                    } else if (isPositiveFlow) {
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
                    if (isEdit) {
                        EditAddressRevampAnalytics.onClickFieldNomorHp(userSession.userId)
                    } else if (isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickFieldNomorHpPositive(userSession.userId)
                    } else {
                        AddNewAddressRevampAnalytics.onClickFieldNomorHpNegative(userSession.userId)
                    }
                }
            )
        }
    }

    private fun renderForm(isEdit: Boolean, data: SaveAddressDataModel) {
        setOnTouchLabelAddress()
        setupRvLabelAddressChips()
        setupOnTextFocusListener()
        if (isPositiveFlow) {
            setupPositiveLayout(isEdit, data)
        } else {
            setupNegativeLayout(isEdit, data)
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

    private fun setupPositiveLayout(isEdit: Boolean, data: SaveAddressDataModel) {
        binding?.run {
            cardAddressPinpointWidget.visible()
            formAddressPositiveWidget.visible()
            formAddressNegativeWidget.gone()
            cardAddressNegativeWidget.gone()

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
            addressForm.renderView(
                data = data,
                formattedAddress = null,
                hasFocusEtDistrict = null,
                onClickEtDistrictListener = null
            )
        }
    }

    private fun setupNegativeLayout(isEdit: Boolean, data: SaveAddressDataModel) {
        binding?.run {
            cardAddressPinpointWidget.gone()
            formAddressPositiveWidget.gone()
            formAddressNegativeWidget.visible()
            cardAddressNegativeWidget.visible()

            setupNegativePinpointCard()

            val onFocusDistrict = {
                if (isEdit) {
                    EditAddressRevampAnalytics.onClickFieldKotaKecamatan(userSession.userId)
                } else {
                    AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(
                        userSession.userId
                    )
                }
                showDistrictRecommendationBottomSheet(false)
            }
            val onClickDistrict = {
                if (isEdit) {
                    EditAddressRevampAnalytics.onClickFieldKotaKecamatan(userSession.userId)
                } else {
                    AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(
                        userSession.userId
                    )
                }
                showDistrictRecommendationBottomSheet(false)
            }
            addressForm.renderView(
                formattedAddress = viewModel.saveDataModel?.formattedAddress,
                data = data,
                hasFocusEtDistrict = onFocusDistrict,
                onClickEtDistrictListener = onClickDistrict
            )
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

        validateFields.forEach {
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
            when (addressUiState) {
                AddressUiState.EditAddress -> {
                    EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    EditAddressRevampAnalytics.onClickSimpanError(
                        userSession.userId,
                        field.joinToString(",")
                    )
                }

                AddressUiState.AddAddress -> {
                    if (isPositiveFlow) {
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

                else -> {
                    // no op
                }
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
            if (isPositiveFlow) {
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
        val field = binding?.addressForm?.addressDetailField

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
        val field = binding?.addressForm?.addressLabelField

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

    private fun dismissDistrictRecommendationBottomSheet() {
        if (districtBottomSheet != null) {
            districtBottomSheet?.dismiss()
            districtBottomSheet = null
        }
    }

    private fun showDistrictRecommendationBottomSheet(isPinpoint: Boolean) {
        context?.let {
            val bundle = Bundle()
            bundle.putString(ARGUMENT_ADDRESS_STATE, addressUiState.name)
            bundle.putBoolean(ARGUMENT_IS_PINPOINT, isPinpoint)
            val intent = RouteManager.getIntent(it, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS)
            intent.putExtras(bundle)

            discomContract.launch(intent)
        }
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
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        bundle.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, viewModel.saveDataModel)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, true)
        bundle.putBoolean(EXTRA_GMS_AVAILABILITY, isGmsAvailable)
        bundle.putString(EXTRA_ADDRESS_STATE, addressUiState.name)
        if (!isPositiveFlow && addressUiState.isAdd()) {
            bundle.putBoolean(
                EXTRA_IS_POLYGON,
                true
            )
        }
        pinpointPageContract.launch(
            context?.let { PinpointNewPageActivity.createIntent(it, bundle) }
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
            contactPickerContract.launch(contactPickerIntent)
        } catch (@Suppress("SwallowedException") e: ActivityNotFoundException) {
            showToaster(
                message = getString(R.string.contact_not_found),
                toasterType = Toaster.TYPE_ERROR
            )
        }
    }

    private fun setupRvLabelAddressChips() {
        binding?.addressForm?.setupRvLabelAddressChips(
            staticDimen8dp = staticDimen8dp,
            labelAlamatChipsLayoutManager = labelAlamatChipsLayoutManager,
            labelAlamatChipsAdapter = labelAlamatChipsAdapter
        )
    }

    private fun setupNegativePinpointCard() {
        binding?.cardAddressNegativeWidget?.setupNegativePinpointCard(
            hasPinPoint = viewModel.saveDataModel?.hasPinpoint(),
            isEdit = addressUiState.isEdit()
        )
        binding?.run {
            cardAddressNegativeWidget.showBtnChangeNegative {
                when (addressUiState) {
                    AddressUiState.AddAddress -> {
                        AddNewAddressRevampAnalytics.onClickAturPinpointNegative(userSession.userId)
                    }

                    AddressUiState.EditAddress -> {
                        EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                    }

                    else -> {
                        // no op
                    }
                }
                checkKotaKecamatan()
            }
        }
    }

    private fun setupCbDefaultLocListener() {
        binding?.apply {
            cbDefaultLoc.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (addressUiState.isAdd()) {
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
                    viewModel.saveDataModel?.setAsPrimaryAddresss = true
                } else {
                    viewModel.saveDataModel?.setAsPrimaryAddresss = false
                }
            }
        }
    }

    private fun setupOnTextFocusListener() {
        binding?.addressForm?.setOnTextFocusListener(
            hasFocusEtLabel = {
                eventShowListLabelAlamat()
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        EditAddressRevampAnalytics.onClickFieldLabelAlamat(userSession.userId)
                    }

                    AddressUiState.AddAddress -> {
                        if (isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickFieldLabelAlamatPositive(userSession.userId)
                        } else {
                            AddNewAddressRevampAnalytics.onClickFieldLabelAlamatNegative(userSession.userId)
                        }
                    }

                    else -> {
                        // no op
                    }
                }
            },
            hasFocusEtAddress = {
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        EditAddressRevampAnalytics.onClickFieldAlamat(userSession.userId)
                    }

                    AddressUiState.AddAddress -> {
                        if (isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickFieldAlamatPositive(userSession.userId)
                        } else {
                            AddNewAddressRevampAnalytics.onClickFieldAlamatNegative(userSession.userId)
                        }
                    }

                    else -> {
                        // no op
                    }
                }
            },
            hasFocusEtCourierNote = {
                when (addressUiState) {
                    AddressUiState.EditAddress -> {
                        EditAddressRevampAnalytics.onClickFieldCatatanKurir(userSession.userId)
                    }

                    AddressUiState.AddAddress -> {
                        if (isPositiveFlow) {
                            AddNewAddressRevampAnalytics.onClickFieldCatatanKurirPositive(userSession.userId)
                        } else {
                            AddNewAddressRevampAnalytics.onClickFieldCatatanKurirNegative(userSession.userId)
                        }
                    }

                    else -> {
                        // no op
                    }
                }
            }
        )
    }

    private fun setOnTouchLabelAddress() {
        binding?.addressForm?.setOnTouchLabelAddress(
            showListLabelAlamat = {
                eventShowListLabelAlamat()
            },
            onAfterTextChanged = { text, rvChips ->
                doUpdateLabel(text, rvChips)
            }
        )
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
        if (addressUiState.isAdd()) {
            labelAlamatChipsAdapter?.submitList(filterList)
        } else {
            if (text.isNotEmpty() && filterList.isEmpty()) {
                rvChips?.gone()
            } else {
                rvChips?.visible()
                labelAlamatChipsAdapter?.submitList(labelAlamatList.toList())
            }
        }
    }

    private fun eventShowListLabelAlamat() {
        binding?.addressForm?.showLabelAddressList()
        binding?.formAddressPositiveWidget?.setLayoutDirection()
        labelAlamatChipsAdapter?.submitList(labelAlamatList.toList())
    }

    private fun doSaveAddress() {
        setSaveAddressDataModel()
        viewModel.saveAddress(
            consentJson = binding?.userConsentWidget?.generatePayloadData().orEmpty(),
            sourceValue = sourceValue
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
            val address1: String = addressForm.addressDetail
            val address1Notes: String = addressForm.courierNote
            val addressName: String = addressForm.label
            val isAnaPositive: String =
                if (isPositiveFlow) PARAM_ANA_POSITIVE else PARAM_ANA_NEGATIVE

            viewModel.updateDataSaveModel(
                receiverName = formAccountWidget.receiverName,
                phoneNo = formAccountWidget.phoneNumber,
                address1 = viewModel.removeUnprintableCharacter(address1),
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
                    putExtra(EXTRA_ADDRESS_NEW, viewModel.saveDataModel)
                }
            )
            finish()
        }
    }

    private fun showDialogBackButton() {
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
            viewModel.saveEditAddress(addressData, sourceValue)
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
                    viewModel.saveEditAddress(addressData, sourceValue)
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
            setBtnSaveAddressEnable(addressUiState.isEdit())
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
                collectionId = collectionId
            )
        )
    }

    private fun setBtnSaveAddressEnable(isEnabled: Boolean) {
        binding?.btnSaveAddressNew?.isEnabled = isEnabled
    }

    private val FragmentAddressFormBinding.addressForm: BaseFormAddressWidget
        get() = if (isPositiveFlow) this.formAddressPositiveWidget else this.formAddressNegativeWidget
}
