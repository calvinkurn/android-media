package com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.addressform

import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.res.Resources
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.response.DistrictItem
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
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetFragment
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant
import com.tokopedia.logisticaddaddress.utils.AddEditAddressUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoCleared
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject

class AddressFormFragment : BaseDaggerFragment(), LabelAlamatChipsAdapter.ActionListener,
        DiscomBottomSheetFragment.ActionListener {


    private var bottomSheetInfoPenerima: BottomSheetUnify? = null
    private var saveDataModel: SaveAddressDataModel? = null
    private var formattedAddress: String = ""
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentDistrictName: String?  = ""
    private var labelAlamatList: Array<String> = emptyArray()
    private var staticDimen8dp: Int? = 0
    private var isPositiveFlow: Boolean = true
    /*To differentiate user pinpoint on ANA Negative*/
    private var isPinpoint: Boolean = false
    /*To differentiate flow from logistic or not*/
    private var isLogisticLabel: Boolean = true
    private var validated: Boolean = true
    private val toppers: String = "Toppers-"
    private var currentKotaKecamatan: String? = ""
    private var currentAlamat: String = ""
    private var currentPostalCode: String = ""
    private var isLatitudeNotEmpty: Boolean? = false
    private var isLongitudeNotEmpty: Boolean? = false

    private lateinit var labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    private lateinit var labelAlamatChipsLayoutManager: ChipsLayoutManager
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private lateinit var districtRecommendationBottomSheetFragment: DiscomBottomSheetFragment

    private var binding by autoCleared<FragmentAddressFormBinding>()

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
        return binding.root
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
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
            binding.formAccount.etNomorHp.textFieldInput.setText(phoneNumberOnly)
        } else if (requestCode == REQUEST_PINPONT_PAGE && resultCode == Activity.RESULT_OK) {
            val isNegativeFullFlow = data?.getBooleanExtra(EXTRA_NEGATIVE_FULL_FLOW, false)
            val isFromAddressForm = data?.getBooleanExtra(EXTRA_FROM_ADDRESS_FORM, false)
            saveDataModel = data?.getParcelableExtra(EXTRA_SAVE_DATA_UI_MODEL)
            if (saveDataModel == null) {
                saveDataModel = data?.getParcelableExtra(EXTRA_ADDRESS_NEW)
            }
            if (isNegativeFullFlow == true && isFromAddressForm == false) {
                finishActivity(saveDataModel)
            } else {
                currentKotaKecamatan = data?.getStringExtra(EXTRA_KOTA_KECAMATAN)
                binding.formAddressNegative.etKotaKecamatan.textFieldInput.setText(currentKotaKecamatan)
                saveDataModel?.let {
                    if (it.latitude.isNotEmpty() || it.longitude.isNotEmpty()) {
                        currentLat = it.latitude.toDouble()
                        currentLong = it.longitude.toDouble()
                        binding.cardAddressNegative.icLocation.setImage(IconUnify.LOCATION)
                        binding.cardAddressNegative.addressDistrict.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_defined)).spannedString }
                        if (saveDataModel?.postalCode?.isEmpty() == true) saveDataModel?.postalCode = currentPostalCode
                    }
                }
            }

        }
    }

    private fun finishActivity(data: SaveAddressDataModel?) {
        activity?.run {
            setResult(Activity.RESULT_OK, Intent().apply {
                putExtra(LogisticConstant.EXTRA_ADDRESS_NEW, data)
            })
            finish()
        }
    }

    private fun removeSpecialChars(s: String): String {
        return s.replace("[^A-Za-z0-9 ]".toRegex(), "").replace(" ","")
    }

    private fun prepareData() {
        viewModel.getDefaultAddress("address")
        if (isPositiveFlow) {
            viewModel.getDistrictDetail(saveDataModel?.districtId.toString())
        } else {
            prepareLayout(null)
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

        viewModel.defaultAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.addressId != 0) {
                        binding.layoutCbDefaultLoc.visibility = View.VISIBLE
                    } else {
                        binding.layoutCbDefaultLoc.visibility = View.GONE
                    }
                }
            }
        })
    }

    @SuppressLint("SetTextI18n")
    private fun prepareLayout(data: DistrictItem?) {

        labelAlamatChipsAdapter = LabelAlamatChipsAdapter(this)
        labelAlamatChipsLayoutManager = ChipsLayoutManager.newBuilder(view?.context)
                .setOrientation(ChipsLayoutManager.HORIZONTAL)
                .setRowStrategy(ChipsLayoutManager.STRATEGY_DEFAULT)
                .build()
        staticDimen8dp = context?.resources?.getDimensionPixelOffset(com.tokopedia.unifyprinciples.R.dimen.unify_space_8)

        binding.run {
            if (userSession.name.isNotEmpty() && !userSession.name.contains(toppers, ignoreCase = true)) {
                formAccount.etNamaPenerima.textFieldInput.setText(userSession.name)
                formAccount.infoNameLayout.visibility = View.GONE
            } else if (userSession.name.contains(toppers, ignoreCase = true)) {
                formAccount.etNamaPenerima.textFieldWrapper.helperText = getString(R.string.helper_nama_penerima)
            }
            formAccount.etNamaPenerima.textFieldInput.addTextChangedListener(setWrapperWatcher(formAccount.etNamaPenerima.textFieldWrapper, null))
            formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)
            formAccount.etNomorHp.setFirstIcon(R.drawable.ic_contact_black)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickIconPhoneBookPositive(userSession.userId)
                else AddNewAddressRevampAnalytics.onClickIconPhoneBookNegative(userSession.userId)
                onNavigateToContact()
            }
            formAccount.etNomorHp.textFieldInput.addTextChangedListener(setWrapperWatcherPhone(formAccount.etNomorHp.textFieldWrapper, getString(R.string.validate_no_ponsel_new)))
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
            binding.run {
                cardAddress.root.gone()
                formAddress.root.gone()
                formAddressNegative.root.visible()
                cardAddressNegative.root.visible()

                if (!isPinpoint) {
                    cardAddressNegative.icLocation.setImage(IconUnify.LOCATION_OFF)
                    cardAddressNegative.addressDistrict.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_not_defined)).spannedString }
                }
                else {
                    cardAddressNegative.icLocation.setImage(IconUnify.LOCATION)
                    cardAddressNegative.addressDistrict.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_defined)).spannedString }
                }

                cardAddressNegative.root.setOnClickListener {
                    AddNewAddressRevampAnalytics.onClickAturPinpointNegative(userSession.userId)
                    checkKotaKecamatan()
                }

                formAddressNegative.etKotaKecamatan.textFieldInput.setText(currentKotaKecamatan)
                formAddressNegative.etKotaKecamatan.textFieldInput.apply {
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
            binding.run {
                formattedAddress = "${data?.districtName}, ${data?.cityName}, ${data?.provinceName}"
                cardAddress.root.visible()
                formAddress.root.visible()
                formAddressNegative.root.gone()
                cardAddressNegative.root.gone()

                cardAddress.addressDistrict.text = formattedAddress

                formAddress.etLabel.textFieldInput.setText("Rumah")
                formAddress.etLabel.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddress.etLabel.textFieldWrapper, null))
                formAddress.etAlamat.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddress.etAlamat.textFieldWrapper, null))
            }
        }

        binding.btnSaveAddress.setOnClickListener {
            if (validateForm()) {
                doSaveAddress()
            }
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
        binding.run {
            if (isPositiveFlow) {
                if (formAddress.etLabel.textFieldInput.text.toString().isEmpty() || formAddress.etLabel.textFieldInput.text.toString() == " ") {
                    validated = false
                    field += getString(R.string.field_label_alamat)
                    setWrapperError(formAddress.etLabel.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddress.etAlamat.textFieldInput.text.toString().isEmpty() || formAddress.etAlamat.textFieldInput.text.toString() == " ") {
                    validated = false
                    field += getString(R.string.field_alamat)
                    setWrapperError(formAddress.etAlamat.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddress.etLabel.textFieldInput.text.toString().length < 3) {
                    validated = false
                    field += getString(R.string.field_label_alamat)
                    view?.let { Toaster.build(it, getString(R.string.error_label_address), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }

                if (formAddress.etAlamat.textFieldInput.text.toString().length < 3) {
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

                if (formAddressNegative.etLabel.textFieldInput.text.toString().length < 3) {
                    validated = false
                    field += getString(R.string.field_label_alamat)
                    view?.let { Toaster.build(it, getString(R.string.error_label_address), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR).show() }
                }


                if (formAddressNegative.etAlamat.textFieldInput.text.toString().length < 3) {
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


        if (!validated && isPositiveFlow) {
            AddNewAddressRevampAnalytics.onClickSimpanErrorPositive(userSession.userId, field)
        } else if (!validated && !isPositiveFlow) {
            AddNewAddressRevampAnalytics.onClickSimpanErrorNegative(userSession.userId, field)
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
        districtRecommendationBottomSheetFragment = DiscomBottomSheetFragment.newInstance(isLogisticLabel, true, isPinpoint)
        districtRecommendationBottomSheetFragment.setActionListener(this)
        childFragmentManager?.run {
            districtRecommendationBottomSheetFragment.show(this, "")
        }
    }

    private fun checkKotaKecamatan() {
        if (binding.formAddressNegative.etKotaKecamatan.textFieldInput.text.toString().isEmpty()) {
            showDistrictRecommendationBottomSheet(true)
        } else {
            goToPinpointPage()
        }
    }

    private fun goToPinpointPage() {
        val bundle = Bundle()
        bundle.putDouble(EXTRA_LAT, currentLat)
        bundle.putDouble(EXTRA_LONG, currentLong)
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, false)
        bundle.putString(EXTRA_DISTRICT_NAME, currentDistrictName)
        bundle.putString(EXTRA_KOTA_KECAMATAN, currentKotaKecamatan)
        bundle.putParcelable(EXTRA_SAVE_DATA_UI_MODEL, saveDataModel)
        bundle.putBoolean(EXTRA_FROM_ADDRESS_FORM, true)
        bundle.putString(EXTRA_POSTAL_CODE, currentPostalCode)
        if (!isPositiveFlow) bundle.putBoolean(EXTRA_IS_POLYGON, true)
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
            binding.formAddress.rvLabelAlamatChips.apply {
                staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
                layoutManager = labelAlamatChipsLayoutManager
                adapter = labelAlamatChipsAdapter
            }
        } else {
            binding.formAddressNegative.rvLabelAlamatChips.apply {
                staticDimen8dp?.let { ChipsItemDecoration(it) }?.let { addItemDecoration(it) }
                layoutManager = labelAlamatChipsLayoutManager
                adapter = labelAlamatChipsAdapter
            }
        }

    }

    private fun setTextListener() {
        binding.run {
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

            binding.cbDefaultLoc.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaPositive(userSession.userId)
                    else AddNewAddressRevampAnalytics.onClickBoxJadikanAlamatUtamaNegative(userSession.userId)
                    saveDataModel?.setAsPrimaryAddresss = true
                } else saveDataModel?.setAsPrimaryAddresss = false
            }

            formAccount.etNamaPenerima.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaPositive(userSession.userId)
                    else  AddNewAddressRevampAnalytics.onClickFieldNamaPenerimaNegative(userSession.userId)
                }
            }

            formAccount.etNomorHp.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    if (isPositiveFlow) AddNewAddressRevampAnalytics.onClickFieldNomorHpPositive(userSession.userId)
                    else  AddNewAddressRevampAnalytics.onClickFieldNomorHpNegative(userSession.userId)
                }
            }

            formAddress.etLabel.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    eventShowListLabelAlamat()
                    AddNewAddressRevampAnalytics.onClickFieldLabelAlamatPositive(userSession.userId)
                }
            }

            formAddress.etAlamat.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    AddNewAddressRevampAnalytics.onClickFieldAlamatPositive(userSession.userId)
                }
            }

            formAddress.etCourierNote.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    AddNewAddressRevampAnalytics.onClickFieldCatatanKurirPositive(userSession.userId)
                }
            }

            formAddressNegative.etLabel.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    eventShowListLabelAlamat()
                    AddNewAddressRevampAnalytics.onClickFieldLabelAlamatNegative(userSession.userId)
                }
            }

            formAddressNegative.etAlamat.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    AddNewAddressRevampAnalytics.onClickFieldAlamatNegative(userSession.userId)
                }
            }

            formAddressNegative.etCourierNote.textFieldInput.setOnFocusChangeListener { _, hasFocus ->
                if (hasFocus) {
                    AddNewAddressRevampAnalytics.onClickFieldCatatanKurirNegative(userSession.userId)
                }
            }
        }

    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setOnTouchLabelAddress(type: String) {
        if (isPositiveFlow) {
            binding.formAddress.etLabel.textFieldInput.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        eventShowListLabelAlamat()
                    } else {
                        binding.formAddress.rvLabelAlamatChips.visibility = View.GONE
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
            }
        } else {
            binding.formAddressNegative.etLabel.textFieldInput.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        eventShowListLabelAlamat()
                    } else {
                        binding.formAddressNegative.rvLabelAlamatChips.visibility = View.GONE
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
            }
        }

    }

    private fun eventShowListLabelAlamat() {
        showLabelAlamatList()
    }

    private fun showLabelAlamatList() {
        val res: Resources = resources
        labelAlamatList = res.getStringArray(R.array.labelAlamatList)

        if (isPositiveFlow) binding.formAddress.rvLabelAlamatChips.visibility = View.VISIBLE
        else binding.formAddressNegative.rvLabelAlamatChips.visibility = View.VISIBLE
        ViewCompat.setLayoutDirection(binding.formAddress.rvLabelAlamatChips, ViewCompat.LAYOUT_DIRECTION_LTR)
        labelAlamatChipsAdapter.submitList(labelAlamatList.toList())
    }

    private fun doSaveAddress() {
        setSaveAddressDataModel()
        saveDataModel?.let {
            viewModel.saveAddress(it)
        }
    }

    private fun setSaveAddressDataModel() {
        if (currentLat != 0.0 && currentLong != 0.0) saveDataModel?.address2 = "$currentLat,$currentLong"
        saveDataModel?.receiverName = binding.formAccount.etNamaPenerima.textFieldInput.text.toString()
        saveDataModel?.phone = binding.formAccount.etNomorHp.textFieldInput.text.toString()
        if (isPositiveFlow) {
            if (binding.formAddress.etCourierNote.textFieldInput.text.isNotEmpty()) {
                saveDataModel?.address1 = "${binding.formAddress.etAlamat.textFieldInput.text} (${binding.formAddress.etCourierNote.textFieldInput.text})"
            } else {
                saveDataModel?.address1 = "${binding.formAddress.etAlamat.textFieldInput.text}"
            }
            saveDataModel?.addressName =  binding.formAddress.etLabel.textFieldInput.text.toString()
            saveDataModel?.isAnaPositive = PARAM_ANA_POSITIVE
        } else {
            if (binding.formAddressNegative.etCourierNote.textFieldInput.text.isNotEmpty()) {
                saveDataModel?.address1 = "${binding.formAddressNegative.etAlamat.textFieldInput.text} (${binding.formAddressNegative.etCourierNote.textFieldInput.text})"
            } else {
                saveDataModel?.address1 = "${binding.formAddressNegative.etAlamat.textFieldInput.text}"
            }
            saveDataModel?.addressName =  binding.formAddressNegative.etLabel.textFieldInput.text.toString()
            saveDataModel?.isAnaPositive = PARAM_ANA_NEGATIVE
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

    companion object {

        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        const val REQUEST_CODE_CONTACT_PICKER = 99

        const val REQUEST_PINPONT_PAGE = 1998
        const val PARAM_ANA_POSITIVE = "1"
        const val PARAM_ANA_NEGATIVE = "0"

        const val SUCCESS = "success"
        const val NOT_SUCCESS = "not success"

        fun newInstance(extra: Bundle): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, extra.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                    putString(EXTRA_KOTA_KECAMATAN, extra.getString(EXTRA_KOTA_KECAMATAN))
                }
            }
        }
    }

    override fun onLabelAlamatChipClicked(labelAlamat: String) {
        if (isPositiveFlow) {
            binding.formAddress.rvLabelAlamatChips.visibility = View.GONE
            binding.formAddress.etLabel.textFieldInput.run {
                AddNewAddressRevampAnalytics.onClickChipsLabelAlamatPositive(userSession.userId)
                setText(labelAlamat)
                setSelection(binding.formAddress.etLabel.textFieldInput.text.length)
            }
        } else {
            binding.formAddressNegative.rvLabelAlamatChips.visibility = View.GONE
            binding.formAddressNegative.etLabel.textFieldInput.run {
                AddNewAddressRevampAnalytics.onClickChipsLabelAlamatNegative(userSession.userId)
                setText(labelAlamat)
                setSelection(binding.formAddressNegative.etLabel.textFieldInput.text.length)
            }
        }

    }

    override fun onGetDistrict(districtAddress: Address) {
       districtRecommendationBottomSheetFragment.getDistrict(districtAddress)
    }

    override fun onChooseZipcode(districtAddress: Address, postalCode: String, isPinpoint: Boolean) {
        val kotaKecamatanText = "${districtAddress.districtName}, ${districtAddress.cityName} $postalCode"
        formattedAddress = "${districtAddress.districtName}, ${districtAddress.cityName}, ${districtAddress.provinceName}"
        currentDistrictName = districtAddress.districtName.toString()
        binding.formAddressNegative.etKotaKecamatan.textFieldInput.run {
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

        if (isPinpoint) goToPinpointPage()
    }

}