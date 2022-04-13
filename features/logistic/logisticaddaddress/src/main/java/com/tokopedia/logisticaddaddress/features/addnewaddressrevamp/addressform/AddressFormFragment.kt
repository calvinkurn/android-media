package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Typeface
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.*
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.StyleSpan
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_EDIT_ADDRESS
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.mapper.AddAddressMapper
import com.tokopedia.logisticCommon.data.response.DistrictItem
import com.tokopedia.logisticCommon.data.response.KeroGetAddressResponse
import com.tokopedia.logisticCommon.util.LogisticUserConsentHelper
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
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

class AddressFormFragment : BaseDaggerFragment(), LabelAlamatChipsAdapter.ActionListener,
        DiscomBottomSheetRevamp.DiscomRevampListener {


    private var bottomSheetInfoPenerima: BottomSheetUnify? = null
    private var saveDataModel: SaveAddressDataModel? = null
    private var formattedAddress: String = ""
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentDistrictName: String?  = ""
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
    private var pinpointKotaKecamatan: String = ""

    private var isEdit: Boolean = false
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

    override fun getScreenName(): String  = ""

    override fun initInjector() {
        getComponent(AddNewAddressRevampComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
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
        }
        permissionCheckerHelper = PermissionCheckerHelper()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        prepareData()
        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_CONTACT_PICKER) {
            val contactURI = data?.data
            var contact: ContactData? = null
            if (contactURI != null) {
                contact = context?.let { AddEditAddressUtil.convertContactUriToData(it.contentResolver, contactURI) }
            }
            val phoneNumberOnly = removeSpecialChars(contact?.contactNumber.toString())
            binding?.formAccount?.etNomorHp?.textFieldInput?.setText(phoneNumberOnly)
            view?.let { view -> Toaster.build(view, getString(R.string.success_add_phone_number), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
        } else if (requestCode == REQUEST_PINPONT_PAGE && resultCode == Activity.RESULT_OK) {
            val isResetToSearchPage = data?.getBooleanExtra(EXTRA_RESET_TO_SEARCH_PAGE, false) ?: false
            if (isResetToSearchPage) {
                activity?.run {
                    setResult(Activity.RESULT_OK, Intent())
                    finish()
                }
            }

            var addressDataFromPinpoint = data?.getParcelableExtra<SaveAddressDataModel>(EXTRA_SAVE_DATA_UI_MODEL)
            if (addressDataFromPinpoint == null) {
                addressDataFromPinpoint = data?.getParcelableExtra(EXTRA_ADDRESS_NEW)
            }
            val kotaKecamatanFromEditPinpoint = data?.getStringExtra(EXTRA_KOTA_KECAMATAN)

            // if user make any changes from pinpoint page, then update data in this page
            if (addressDataFromPinpoint != null) {
                if (isEdit) {
                    if (addressDataFromPinpoint.latitude != saveDataModel?.latitude || addressDataFromPinpoint.longitude != saveDataModel?.longitude) {
                        if (kotaKecamatanFromEditPinpoint != currentKotaKecamatan) {
                            view?.let { view -> Toaster.build(view, getString(R.string.change_pinpoint_outside_district), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                        } else {
                            view?.let { view -> Toaster.build(view, getString(R.string.change_pinpoint_edit_address), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
                        }
                        focusOnDetailAddress()
                    }
                    if (kotaKecamatanFromEditPinpoint != null) {
                        pinpointKotaKecamatan = kotaKecamatanFromEditPinpoint
                    }
                }
                saveDataModel = addressDataFromPinpoint
                currentKotaKecamatan = kotaKecamatanFromEditPinpoint
                binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.setText(currentKotaKecamatan)
                binding?.cardAddressPinpoint?.addressDistrict?.text = currentKotaKecamatan
                saveDataModel?.let {
                    if (it.latitude.isNotEmpty() || it.longitude.isNotEmpty()) {
                        currentLat = it.latitude.toDouble()
                        currentLong = it.longitude.toDouble()
                        binding?.cardAddressNegative?.icLocation?.setImage(IconUnify.LOCATION)
                        binding?.cardAddressNegative?.addressDistrict?.text = if (isEdit) getString(R.string.tv_pinpoint_defined_edit) else context?.let {
                            HtmlLinkHelper(
                                it,
                                getString(R.string.tv_pinpoint_defined)
                            ).spannedString
                        }
                        binding?.cardAddressNegative?.btnChangeNegative?.text = getString(R.string.change_pinpoint_positive_text)
                        if (saveDataModel?.postalCode?.isEmpty() == true) saveDataModel?.postalCode =
                            currentPostalCode
                    }
                }
            }
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
        return s.replace("[^A-Za-z0-9 ]".toRegex(), "").replace(" ","")
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
        viewModel.districtDetail.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    prepareLayout(it.data.district[0])
                }

                is Fail -> {
                    it.throwable.printStackTrace()
                }
            }
        })

        viewModel.saveAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == 1) {
                        saveDataModel?.id = it.data.addrId
                        saveDataModel?.warehouseId = it.data.tokonow.warehouseId
                        saveDataModel?.shopId = it.data.tokonow.shopId
                        saveDataModel?.warehouses = AddAddressMapper.mapWarehouses(it.data.tokonow.warehouses)
                        saveDataModel?.serviceType = it.data.tokonow.serviceType
                        if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickSimpanPositive(userSession.userId, SUCCESS)
                        else AddNewAddressRevampAnalytics.onClickSimpanNegative(userSession.userId, SUCCESS)
                        onSuccessAddAddress()
                    }
                }

                is Fail -> {
                    if (isPositiveFlow) {
                        AddNewAddressRevampAnalytics.onClickSimpanErrorPositive(userSession.userId, "")
                        AddNewAddressRevampAnalytics.onClickSimpanPositive(userSession.userId, NOT_SUCCESS)
                    }
                    else {
                        AddNewAddressRevampAnalytics.onClickSimpanErrorNegative(userSession.userId, "")
                        AddNewAddressRevampAnalytics.onClickSimpanNegative(userSession.userId, NOT_SUCCESS)
                    }
                    val msg = it.throwable.message.toString()
                    view?.let { view -> Toaster.build(view, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }
            }
        })

        viewModel.editAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.isSuccess == 1) {
                        onSuccessEditAddress()
                        EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, true)
                    } else {
                        EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    }
                }

                is Fail -> {
                    EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                    val msg = it.throwable.message.toString()
                    view?.let { view -> Toaster.build(view, msg, Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }
            }
        })

        viewModel.defaultAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.addressId != 0L) {
                        binding?.layoutCbDefaultLoc?.visibility = View.VISIBLE
                    } else {
                        binding?.layoutCbDefaultLoc?.visibility = View.GONE
                    }
                }
            }
        })

        viewModel.addressDetail.observe(viewLifecycleOwner, Observer {
            binding?.loaderAddressForm?.visibility = View.GONE
            when (it) {
                is Success -> {
                    it.data.keroGetAddress.data.firstOrNull()?.let { detailAddress ->
                        saveDataModel = AddAddressMapper.mapAddressDetailToSaveAddressDataModel(detailAddress)
                        isLatitudeNotEmpty = saveDataModel?.latitude?.isNotEmpty()
                        isLatitudeNotEmpty?.let { notEmpty ->
                            if (notEmpty) currentLat = saveDataModel?.latitude?.toDouble() ?: 0.0
                        }

                        isLongitudeNotEmpty = saveDataModel?.longitude?.isNotEmpty()
                        isLongitudeNotEmpty?.let { notEmpty ->
                            if (notEmpty) currentLong = saveDataModel?.longitude?.toDouble() ?: 0.0
                        }
                        isPositiveFlow = isLatitudeNotEmpty == true && isLongitudeNotEmpty == true
                        currentKotaKecamatan = "${detailAddress.districtName}, ${detailAddress.cityName}, ${detailAddress.provinceName}"
                        prepareEditLayout(detailAddress)
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun prepareLayout(data: DistrictItem?) {
        setupLabelChips("Rumah")
        binding?.run {
            if (userSession.name.isNotEmpty() && !userSession.name.contains(toppers, ignoreCase = true)) {
                formAccount.etNamaPenerima.textFieldInput.setText(userSession.name)
                formAccount.infoNameLayout.visibility = View.GONE
            } else if (userSession.name.contains(toppers, ignoreCase = true)) {
                formAccount.etNamaPenerima.textFieldWrapper.helperText = getString(R.string.helper_nama_penerima)
            }
            setupFormAccount()
//            formAccount.etNamaPenerima.textFieldInput.addTextChangedListener(setWrapperWatcher(formAccount.etNamaPenerima.textFieldWrapper, null))
            formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)
//            formAccount.etNomorHp.setFirstIcon(R.drawable.ic_contact_black)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickIconPhoneBookPositive(userSession.userId)
                else AddNewAddressRevampAnalytics.onClickIconPhoneBookNegative(userSession.userId)
                onNavigateToContact()
            }
//            formAccount.etNomorHp.textFieldInput.addTextChangedListener(setWrapperWatcherPhone(formAccount.etNomorHp.textFieldWrapper, getString(R.string.validate_no_ponsel_new)))
            formAccount.btnInfo.setOnClickListener {
                if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickIconNamaPenerimaPositive(userSession.userId)
                else AddNewAddressRevampAnalytics.onClickIconNamaPenerimaNegative(userSession.userId)
                showBottomSheetInfoPenerima()
            }
        }

        if (!isPositiveFlow) {
            setOnTouchLabelAddress(ANA_NEGATIVE)
            setupRvLabelAlamatChips()
            setTextListener()
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
                            AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(userSession.userId)
                            showDistrictRecommendationBottomSheet(false)
                        }
                    }
                    setOnClickListener {
                        AddNewAddressRevampAnalytics.onClickFieldKotaKecamatanNegative(userSession.userId)
                        showDistrictRecommendationBottomSheet(false)
                    }
                }
                formAddressNegative.etLabel.textFieldInput.setText("Rumah")
                formAddressNegative.etLabel.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddressNegative.etLabel.textFieldWrapper, null))
                formAddressNegative.etAlamat.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddressNegative.etAlamat.textFieldWrapper, null))
                currentAlamat = formAddressNegative.etAlamat.textFieldInput.text.toString()
            }
        } else {
            setOnTouchLabelAddress(ANA_POSITIVE)
            setupRvLabelAlamatChips()
            setTextListener()
            binding?.run {
                formattedAddress = "${data?.districtName}, ${data?.cityName}, ${data?.provinceName}"
                showPositiveLayout()

                cardAddressPinpoint.addressDistrict.text = formattedAddress

                formAddress.etLabel.textFieldInput.setText("Rumah")
                formAddress.etLabel.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddress.etLabel.textFieldWrapper, null))
                formAddress.etAlamatNew.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddress.etAlamatNew.textFieldWrapper, null))
            }
        }

        LogisticUserConsentHelper.displayUserConsent(activity as Context, userSession.userId, binding?.userConsent, getString(R.string.btn_simpan), if(isPositiveFlow) LogisticUserConsentHelper.ANA_REVAMP_POSITIVE else LogisticUserConsentHelper.ANA_REVAMP_NEGATIVE)

        binding?.btnSaveAddressNew?.setOnClickListener {
            if (validateForm()) {
                doSaveAddress()
            }
        }
    }

    private fun setupLabelChips(currentLabel: String) {
        labelAlamatList = resources.getStringArray(R.array.labelAlamatList).map { Pair(it, it.equals(currentLabel, ignoreCase = true)) }.toTypedArray()
        labelAlamatChipsAdapter = LabelAlamatChipsAdapter(this)
        labelAlamatChipsLayoutManager = ChipsLayoutManager.newBuilder(view?.context)
            .setOrientation(ChipsLayoutManager.HORIZONTAL)
            .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
            .build()
        staticDimen8dp = context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)
    }

    @SuppressLint("SetTextI18n")
    private fun prepareEditLayout(data: KeroGetAddressResponse.Data.KeroGetAddress.DetailAddressResponse) {
        setupLabelChips(data.addrName)
//        if (data.latitude.isNotEmpty() && data.longitude.isNotEmpty() ) {
            isPositiveFlow = data.latitude.isNotEmpty() && data.longitude.isNotEmpty()
//        }
        binding?.run {
//            if (userSession.name.isNotEmpty() && !userSession.name.contains(toppers, ignoreCase = true)) {
                formAccount.etNamaPenerima.textFieldInput.setText(data.receiverName)
                formAccount.infoNameLayout.visibility = View.GONE
//            } else if (userSession.name.contains(toppers, ignoreCase = true)) {
//                formAccount.etNamaPenerima.textFieldWrapper.helperText = getString(R.string.helper_nama_penerima)
//            }
            setupFormAccount()
//            formAccount.etNamaPenerima.textFieldInput.addTextChangedListener(setWrapperWatcher(formAccount.etNamaPenerima.textFieldWrapper, null))
            formAccount.etNomorHp.textFieldInput.setText(data.phone)
//            formAccount.etNomorHp.setFirstIcon(R.drawable.ic_contact_black)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                EditAddressRevampAnalytics.onClickIconPhoneBook(userSession.userId)
                onNavigateToContact()
            }
//            formAccount.etNomorHp.textFieldInput.addTextChangedListener(setWrapperWatcherPhone(formAccount.etNomorHp.textFieldWrapper, getString(R.string.validate_no_ponsel_new)))
            formAccount.btnInfo.setOnClickListener {
//                if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickIconNamaPenerimaPositive(userSession.userId)
//                else AddNewAddressRevampAnalytics.onClickIconNamaPenerimaNegative(userSession.userId)
                showBottomSheetInfoPenerima()
            }
        }

        val addressDetail = if (data.addressDetailNotes.isEmpty()) data.address1 else data.addressDetailStreet

        if (!isPositiveFlow) {
            setOnTouchLabelAddress(ANA_NEGATIVE)
            setupRvLabelAlamatChips()
            setTextListener()
            showNegativeLayout()
            setupNegativePinpointCard()
            binding?.run {
                cardAddressNegative.root.setOnClickListener {
                    EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                    checkKotaKecamatan()
                }
                cardAddressNegative.let {
                    it.btnChangeNegative.visibility = View.VISIBLE
                    it.btnArrow.visibility = View.GONE
                }

                    formAddressNegative.etKotaKecamatan.textFieldInput.setText(currentKotaKecamatan)
                    formAddressNegative.etKotaKecamatan.textFieldInput.apply {
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
                    formAddressNegative.etLabel.textFieldInput.setText(data.addrName)
                    formAddressNegative.rvLabelAlamatChips.visibility = View.GONE
                    formAddressNegative.etAlamat.textFieldInput.setText(addressDetail)
                    formAddressNegative.etCourierNote.textFieldInput.setText(data.addressDetailNotes)
                    formAddressNegative.etLabel.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddressNegative.etLabel.textFieldWrapper, null))
                    formAddressNegative.etAlamat.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddressNegative.etAlamat.textFieldWrapper, null))
                    currentAlamat = formAddressNegative.etAlamat.textFieldInput.text.toString()
            }
        } else {
            setOnTouchLabelAddress(ANA_POSITIVE)
            setupRvLabelAlamatChips()
            setTextListener()
            showPositiveLayout()
            binding?.run {
                formattedAddress = "${data.districtName}, ${data.cityName}, ${data.provinceName}"
                cardAddressPinpoint.btnChange.visibility = View.VISIBLE
                cardAddressPinpoint.btnChange.setOnClickListener {
                    goToPinpointPage()
                    EditAddressRevampAnalytics.onClickAturPinPoint(userSession.userId)
                }
                cardAddressPinpoint.tvPinpointTitle.visibility = View.VISIBLE
                cardAddressPinpoint.addressDistrict.text = formattedAddress
                formAddress.etAlamatNew.textFieldInput.setText(addressDetail)
                formAddress.etCourierNote.textFieldInput.setText(data.addressDetailNotes)
                formAddress.etLabel.textFieldInput.setText(data.addrName)
                formAddress.rvLabelAlamatChips.visibility = View.GONE
                formAddress.etLabel.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddress.etLabel.textFieldWrapper, null))
                formAddress.etAlamatNew.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddress.etAlamatNew.textFieldWrapper, null))
            }
        }

        LogisticUserConsentHelper.displayUserConsent(activity as Context, userSession.userId, binding?.userConsent, getString(R.string.btn_simpan), EditAddressRevampAnalytics.CATEGORY_EDIT_ADDRESS_PAGE)

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
        val viewBinding = BottomsheetLocationUnmatchedBinding.inflate(LayoutInflater.from(context), null, false)
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
            tvLocationUnmatchedDetail.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_desc_nama_penerima_bottomsheet)).spannedString }
            btnClose.setOnClickListener {
                bottomSheetInfoPenerima?.dismiss()
            }
        }
    }

    private fun validateForm(): Boolean {
        validated = true
        var field = ""
        binding?.run {
            if (isPositiveFlow) {
                if (formAddress.etLabel.textFieldInput.text.toString().isEmpty() || formAddress.etLabel.textFieldInput.text.toString() == " ") {
                    validated = false
                    field += getString(R.string.field_label_alamat)
                    setWrapperError(formAddress.etLabel.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddress.etAlamatNew.textFieldInput.text.toString().isEmpty() || formAddress.etAlamatNew.textFieldInput.text.toString() == " ") {
                    validated = false
                    field += getString(R.string.field_alamat)
                    setWrapperError(formAddress.etAlamatNew.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddress.etLabel.textFieldInput.text.toString().length < MINIMUM_CHAR) {
                    validated = false
                    field += getString(R.string.field_label_alamat)
                    view?.let { Toaster.build(it, getString(R.string.error_label_address), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }

                if (formAddress.etAlamatNew.textFieldInput.text.toString().length < MINIMUM_CHAR) {
                    validated = false
                    field += getString(R.string.field_alamat)
                    view?.let { Toaster.build(it, getString(R.string.error_alamat), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }
            } else {
                if (formAddressNegative.etLabel.textFieldInput.text.toString().isEmpty() || formAddressNegative.etLabel.textFieldInput.text.toString() == " ") {
                    validated = false
                    field += getString(R.string.field_label_alamat)
                    setWrapperError(formAddressNegative.etLabel.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddressNegative.etAlamat.textFieldInput.text.toString().isEmpty() || formAddressNegative.etAlamat.textFieldInput.text.toString() == " ") {
                    validated = false
                    field += getString(R.string.field_alamat)
                    setWrapperError(formAddressNegative.etAlamat.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddressNegative.etLabel.textFieldInput.text.toString().length < MINIMUM_CHAR) {
                    validated = false
                    field += getString(R.string.field_label_alamat)
                    view?.let { Toaster.build(it, getString(R.string.error_label_address), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }


                if (formAddressNegative.etAlamat.textFieldInput.text.toString().length < MINIMUM_CHAR) {
                    validated = false
                    field += getString(R.string.field_alamat)
                    view?.let { Toaster.build(it, getString(R.string.error_alamat), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }
            }

            if (formAccount.etNamaPenerima.textFieldInput.text.toString().isEmpty() || formAccount.etNamaPenerima.textFieldInput.text.toString() == " ") {
                validated = false
                field += getString(R.string.field_nama_penerima)
                setWrapperError(formAccount.etNamaPenerima.textFieldWrapper, getString(R.string.tv_error_field))
            }

            if (formAccount.etNomorHp.textFieldInput.text.toString().isEmpty()  || formAccount.etNomorHp.textFieldInput.text.toString() == " ") {
                validated = false
                field += getString(R.string.field_nomor_hp)
                setWrapperError(formAccount.etNomorHp.textFieldWrapper, getString(R.string.tv_error_field))
            }

            if (formAccount.etNamaPenerima.textFieldInput.text.toString().length < 2) {
                validated = false
                field += getString(R.string.field_nama_penerima)
                view?.let { Toaster.build(it, getString(R.string.error_nama_penerima), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
            }
        }

        if (isEdit && !isPositiveFlow) {
            if (pinpointKotaKecamatan.isNotEmpty() && currentKotaKecamatan != pinpointKotaKecamatan) {
                validated = false
                view?.let {
                    Toaster.build(
                        it,
                        getString(R.string.error_district_pinpoint_mismatch),
                        Toaster.LENGTH_SHORT,
                        Toaster.TYPE_ERROR
                    ).show()
                }
            }
        }
        if (!isEdit) {
            if (!validated && isPositiveFlow) {
                AddNewAddressRevampAnalytics.onClickSimpanErrorPositive(userSession.userId, field)
            } else if (!validated && !isPositiveFlow) {
                AddNewAddressRevampAnalytics.onClickSimpanErrorNegative(userSession.userId, field)
            }
        } else {
            if (!validated) {
                EditAddressRevampAnalytics.onClickButtonSimpan(userSession.userId, false)
                EditAddressRevampAnalytics.onClickSimpanError(userSession.userId, field)
            }
        }
        return validated
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

    private fun setWrapperWatcherPhone(wrapper: TextInputLayout, textWatcher: String?): TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                if (s.isNotEmpty() && s.length < 9) {
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

    private fun showDistrictRecommendationBottomSheet(isPinpoint: Boolean) {
        districtBottomSheet = DiscomBottomSheetRevamp(isPinpoint = isPinpoint, isEdit = isEdit)
        districtBottomSheet?.setListener(this)
        districtBottomSheet?.show(this.childFragmentManager)
    }

    private fun checkKotaKecamatan() {
        if (binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.text?.toString()?.isEmpty() ?: true) {
            showDistrictRecommendationBottomSheet(true)
        } else {
            goToPinpointPage()
        }
    }

    private fun goToPinpointPage() {
        val bundle = Bundle()
        bundle.putDouble(EXTRA_LAT, currentLat)
        bundle.putDouble(EXTRA_LONG, currentLong)
//        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, false)
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, isPositiveFlow)
        bundle.putString(EXTRA_DISTRICT_NAME, currentDistrictName)
        bundle.putString(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
        bundle.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, saveDataModel)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, true)
        bundle.putBoolean(EXTRA_IS_EDIT, isEdit)
        bundle.putString(EXTRA_POSTAL_CODE, currentPostalCode)
        if (!isPositiveFlow && !isEdit) bundle.putBoolean(EXTRA_IS_POLYGON, true)
        startActivityForResult(context?.let { PinpointNewPageActivity.createIntent(it, bundle) }, REQUEST_PINPONT_PAGE)
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

    private fun setupRvLabelAlamatChips() {
        if (isPositiveFlow) {
            binding?.formAddress?.rvLabelAlamatChips?.apply {
                staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
                layoutManager = labelAlamatChipsLayoutManager
                adapter = labelAlamatChipsAdapter
            }
        } else {
            binding?.formAddressNegative?.rvLabelAlamatChips?.apply {
                staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
                layoutManager = labelAlamatChipsLayoutManager
                adapter = labelAlamatChipsAdapter
            }
        }

    }

    private fun setupFormAccount() {
        binding?.run {
            formAccount.etNamaPenerima.textFieldInput.addTextChangedListener(setWrapperWatcher(formAccount.etNamaPenerima.textFieldWrapper, null))
            formAccount.etNomorHp.setFirstIcon(R.drawable.ic_contact_black)
            formAccount.etNomorHp.textFieldInput.addTextChangedListener(setWrapperWatcherPhone(formAccount.etNomorHp.textFieldWrapper, getString(R.string.validate_no_ponsel_new)))
        }
    }

    private fun setupNegativePinpointCard() {
        binding?.run {
            if (!isPinpoint) {
                cardAddressNegative.icLocation.setImage(IconUnify.LOCATION_OFF)
                cardAddressNegative.addressDistrict.text =  if (isEdit) getString(R.string.tv_pinpoint_not_defined_edit) else context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_not_defined)).spannedString }
            }
            else {
                cardAddressNegative.icLocation.setImage(IconUnify.LOCATION)
                cardAddressNegative.addressDistrict.text = if (isEdit) getString(R.string.tv_pinpoint_defined_edit) else context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_defined)).spannedString }
                cardAddressNegative.btnChangeNegative.text = getString(R.string.change_pinpoint_positive_text)
            }
        }
    }

    private fun setTextListener() {
        binding?.run {
            formAccount.etNomorHp.textFieldInput.apply {
                addTextChangedListener(object: TextWatcher {
                    override fun afterTextChanged(s: Editable?) {
                        //
                    }

                    override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                        //
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                        filters = arrayOf(InputFilter.LengthFilter(15))
                    }

                })
            }

            cbDefaultLoc.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (!isEdit) {
                        if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaPositive(userSession.userId)
                        else AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaNegative(userSession.userId)
                    }
                    saveDataModel?.setAsPrimaryAddresss = true
                } else saveDataModel?.setAsPrimaryAddresss = false
            }

            formAccount.etNamaPenerima.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaPositive(userSession.userId)
                        else  AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickFieldNamaPenerima(userSession.userId)
                    }
                }
            }

            formAccount.etNomorHp.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (!isEdit) {
                        if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickFieldNomorHpPositive(userSession.userId)
                        else  AddNewAddressRevampAnalytics.onClickFieldNomorHpNegative(userSession.userId)
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
    private fun setOnTouchLabelAddress(type: String) {
        if (isPositiveFlow) {
            binding?.formAddress?.etLabel?.textFieldInput?.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        eventShowListLabelAlamat()
                    } else {
                        binding?.formAddress?.rvLabelAlamatChips?.visibility = View.GONE
                    }
                }
                setOnClickListener {
                    eventShowListLabelAlamat()
                }
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                                   after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                               count: Int) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val currentLabel = labelAlamatList
                        labelAlamatList = currentLabel.map { item -> item.copy(second = item.first.equals(s.toString(), ignoreCase = true)) }.toTypedArray()
                        val filterList = labelAlamatList.filter {
                            it.first.contains("$s", true)
                        }
                        if (!isEdit) {
                            labelAlamatChipsAdapter.submitList(filterList)
                        }
                        else {
                            if (s.toString().isNotEmpty() && filterList.isEmpty()) {
                                binding?.formAddress?.rvLabelAlamatChips?.visibility = View.GONE
                            } else {
                                binding?.formAddress?.rvLabelAlamatChips?.visibility = View.VISIBLE
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
        } else {
            binding?.formAddressNegative?.etLabel?.textFieldInput?.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        eventShowListLabelAlamat()
                    } else {
                        binding?.formAddressNegative?.rvLabelAlamatChips?.visibility = View.GONE
                    }
                }
                setOnClickListener {
                    eventShowListLabelAlamat()
                }
                addTextChangedListener(object : TextWatcher {
                    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int,
                                                   after: Int) {
                    }

                    override fun onTextChanged(s: CharSequence, start: Int, before: Int,
                                               count: Int) {
                    }

                    override fun afterTextChanged(s: Editable) {
                        val currentLabel = labelAlamatList
                        labelAlamatList = currentLabel.map { item -> item.copy(second = item.first.equals(s.toString(), ignoreCase = true)) }.toTypedArray()
                        val filterList = labelAlamatList.filter {
                            it.first.contains("$s", true)
                        }
                        if (!isEdit) {
                            labelAlamatChipsAdapter.submitList(filterList)
                        }
                        else {
                            if (s.toString().isNotEmpty() && filterList.isEmpty()) {
                                binding?.formAddressNegative?.rvLabelAlamatChips?.visibility = View.GONE
                            } else {
                                binding?.formAddressNegative?.rvLabelAlamatChips?.visibility = View.VISIBLE
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
//        val res: Resources = resources
//        labelAlamatList = res.getStringArray(R.array.labelAlamatList).map { Pair(it, false) }.toTypedArray()

        if (isPositiveFlow) binding?.formAddress?.rvLabelAlamatChips?.visibility = View.VISIBLE
        else binding?.formAddressNegative?.rvLabelAlamatChips?.visibility = View.VISIBLE
        binding?.formAddress?.rvLabelAlamatChips?.let { ViewCompat.setLayoutDirection(it, ViewCompat.LAYOUT_DIRECTION_LTR) }
        labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
    }

    private fun doSaveAddress() {
        setSaveAddressDataModel()
        saveDataModel?.let { viewModel.saveAddress(it) }
    }

    private fun doSaveEditAddress() {
        setSaveAddressDataModel()
        saveDataModel?.let { viewModel.saveEditAddress(it) }
    }

    private fun setSaveAddressDataModel() {
        if (currentLat != 0.0 && currentLong != 0.0) saveDataModel?.address2 = "$currentLat,$currentLong"
        binding?.run {
            saveDataModel?.receiverName = formAccount.etNamaPenerima.textFieldInput.text.toString()
            saveDataModel?.phone = formAccount.etNomorHp.textFieldInput.text.toString()
            if (isPositiveFlow) {
                if (formAddress.etCourierNote.textFieldInput.text.isNotEmpty()) {
                    saveDataModel?.address1 = "${formAddress.etAlamatNew.textFieldInput.text} (${formAddress.etCourierNote.textFieldInput.text})"
                } else {
                    saveDataModel?.address1 = "${formAddress.etAlamatNew.textFieldInput.text}"
                }
                saveDataModel?.addressName =  formAddress.etLabel.textFieldInput.text.toString()
                saveDataModel?.isAnaPositive = PARAM_ANA_POSITIVE
            } else {
                if (formAddressNegative.etCourierNote.textFieldInput.text.isNotEmpty()) {
                    saveDataModel?.address1 = "${formAddressNegative.etAlamat.textFieldInput.text} (${formAddressNegative.etCourierNote.textFieldInput.text})"
                } else {
                    saveDataModel?.address1 = "${formAddressNegative.etAlamat.textFieldInput.text}"
                }
                saveDataModel?.addressName =  formAddressNegative.etLabel.textFieldInput.text.toString()
                saveDataModel?.isAnaPositive = PARAM_ANA_NEGATIVE
            }   
        }


        if (userSession.name.isNotEmpty() && userSession.name.contains(toppers, ignoreCase = true)) {
            saveDataModel?.applyNameAsNewUserFullname = true
        }
    }

    private fun onSuccessAddAddress() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_ADDRESS_NEW, saveDataModel)
            })
            finish()
        }
    }

    private fun onSuccessEditAddress() {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(EXTRA_EDIT_ADDRESS, saveDataModel)
            })
            finish()
        }
    }

    companion object {

        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        const val REQUEST_CODE_CONTACT_PICKER = 99

        const val REQUEST_PINPONT_PAGE = 1998
        const val PARAM_ANA_POSITIVE = "1"
        const val PARAM_ANA_NEGATIVE = "0"

        const val SUCCESS = "success"
        const val NOT_SUCCESS = "not success"

        const val MINIMUM_CHAR = 3

        fun newInstance(extra: Bundle): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, extra.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putString(EXTRA_KOTA_KECAMATAN, extra.getString(EXTRA_KOTA_KECAMATAN))
                    putBoolean(EXTRA_IS_EDIT, false)
                }
            }
        }

        fun newInstance(addressId: String?): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putString(EXTRA_ADDRESS_ID, addressId)
                    putBoolean(EXTRA_IS_EDIT, true)
                }
            }
        }
    }

    override fun onLabelAlamatChipClicked(labelAlamat: String) {
        binding?.run {
            if (isPositiveFlow) {
                if (!isEdit) {
                    formAddress.rvLabelAlamatChips.visibility = View.GONE
                }
                formAddress.etLabel.textFieldInput.run {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickChipsLabelAlamatPositive(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickChipsLabelAlamat(userSession.userId)
                    }
                    setText(labelAlamat)
                    setSelection(formAddress.etLabel.textFieldInput.text.length)
                }
            } else {
                if (!isEdit) {
                    formAddressNegative.rvLabelAlamatChips.visibility = View.GONE
                }
                formAddressNegative.etLabel.textFieldInput.run {
                    if (!isEdit) {
                        AddNewAddressRevampAnalytics.onClickChipsLabelAlamatNegative(userSession.userId)
                    } else {
                        EditAddressRevampAnalytics.onClickChipsLabelAlamat(userSession.userId)
                    }
                    setText(labelAlamat)
                    setSelection(formAddressNegative.etLabel.textFieldInput.text.length)
                }
            }
        }
    }

    override fun onGetDistrict(districtAddress: Address) {
        districtBottomSheet?.getDistrict(districtAddress)
    }

    override fun onChooseZipcode(districtAddress: Address, postalCode: String, isPinpoint: Boolean) {
        val kotaKecamatanText = "${districtAddress.districtName}, ${districtAddress.cityName}, ${districtAddress.provinceName}"
        formattedAddress = kotaKecamatanText
        currentDistrictName = districtAddress.districtName.toString()
        binding?.formAddressNegative?.etKotaKecamatan?.textFieldInput?.run {
            setText(kotaKecamatanText)
            currentKotaKecamatan = kotaKecamatanText
        }

        val selectedDistrict = "${districtAddress.provinceName}, ${districtAddress.cityName}, ${districtAddress.districtName}"
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
            saveDataModel?.latitude = "0.0"
            saveDataModel?.longitude = "0.0"
            this.isPinpoint = false
            binding?.run {
                cardAddressNegative.icLocation.setImage(IconUnify.LOCATION_OFF)
                cardAddressNegative.addressDistrict.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_not_defined)).spannedString }
            }
        } else {
            view?.let { view -> Toaster.build(view, getString(R.string.district_changed_success), Toaster.LENGTH_SHORT, Toaster.TYPE_NORMAL).show() }
            focusOnDetailAddress()
        }

        if (isPinpoint) goToPinpointPage()
    }

}