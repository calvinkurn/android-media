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
import android.widget.AutoCompleteTextView
import androidx.core.view.ViewCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager
import com.google.android.material.textfield.TextInputLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.response.DistrictItem
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.AddressConstants
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
import com.tokopedia.logisticaddaddress.databinding.BottomsheetLocationUnmatchedBinding
import com.tokopedia.logisticaddaddress.databinding.FragmentAddressFormBinding
import com.tokopedia.logisticaddaddress.di.addnewaddressrevamp.AddNewAddressRevampComponent
import com.tokopedia.logisticaddaddress.domain.model.Address
import com.tokopedia.logisticaddaddress.domain.model.add_address.ContactData
import com.tokopedia.logisticaddaddress.features.addnewaddress.ChipsItemDecoration
import com.tokopedia.logisticaddaddress.features.addnewaddress.addedit.LabelAlamatChipsAdapter
import com.tokopedia.logisticaddaddress.features.addnewaddressrevamp.pinpointnew.PinpointNewPageActivity
import com.tokopedia.logisticaddaddress.features.district_recommendation.DiscomBottomSheetFragment
import com.tokopedia.logisticaddaddress.utils.AddAddressConstant
import com.tokopedia.logisticaddaddress.utils.AddEditAddressUtil
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.HtmlLinkHelper
import com.tokopedia.unifycomponents.TextFieldUnify
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
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var currentDistrictName: String  = ""
    private var labelAlamatList: Array<String> = emptyArray()
    private var staticDimen8dp: Int? = 0
    private var isPositiveFlow: Boolean = true
    /*To differentiate user pinpoint on ANA Negative*/
    private var isPinpoint: Boolean = false
    /*To differentiate flow from logistic or not*/
    private var isLogisticLabel: Boolean = true
    private var validated: Boolean = true
    private val toppers: String = "Toppers"
    private var currentKotaKecamatan: String = ""

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
            currentLat = saveDataModel?.latitude?.toDouble() ?: 0.0
            currentLong = saveDataModel?.longitude?.toDouble() ?: 0.0
            isPositiveFlow = it.getBoolean(EXTRA_IS_POSITIVE_FLOW)
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
        } else if (requestCode == 1998 && resultCode == Activity.RESULT_OK) {
            saveDataModel = data?.getParcelableExtra(EXTRA_SAVE_DATA_UI_MODEL)

            binding.formAddressNegative.etKotaKecamatan.textFieldInput.setText(currentKotaKecamatan)
            binding.cardAddressNegative.icLocation.setImage(IconUnify.LOCATION)
            binding.cardAddressNegative.addressDistrict.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_pinpoint_defined)).spannedString }
            saveDataModel?.let {
                currentLat = it.latitude.toDouble()
                currentLong = it.longitude.toDouble()
            }
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
                        onSuccessAddAddress()
                    }
                }

                is Fail -> {
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
                formAccount.etNamaPenerima.textFieldWrapper.error = ""
                formAccount.infoNameLayout.visibility = View.GONE
            }
            formAccount.etNamaPenerima.textFieldInput.addTextChangedListener(setWrapperWatcher(formAccount.etNamaPenerima.textFieldWrapper, null))
            formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)
            formAccount.etNomorHp.setFirstIcon(R.drawable.ic_contact_black)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                onNavigateToContact()
            }
            formAccount.etNomorHp.textFieldInput.addTextChangedListener(setWrapperWatcherPhone(formAccount.etNomorHp.textFieldWrapper, getString(R.string.validate_no_ponsel_less_char)))
            formAccount.btnInfo.setOnClickListener {
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
                    checkKotaKecamatan()
                }

                formAddressNegative.etKotaKecamatan.textFieldInput.apply {
                    setOnFocusChangeListener { _, hasFocus ->
                        if (hasFocus) {
                            showDistrictRecommendationBottomSheet()
                        }
                    }
                    setOnClickListener {
                        showDistrictRecommendationBottomSheet()
                    }
                }
                formAddressNegative.etLabel.textFieldInput.setText("Rumah")
                formAddressNegative.etLabel.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddressNegative.etLabel.textFieldWrapper, null))
                formAddressNegative.etAlamat.textFieldInput.addTextChangedListener(setWrapperWatcher(formAddressNegative.etAlamat.textFieldWrapper, null))
            }
        } else {
            setOnTouchLabelAddress(ANA_POSITIVE)
            setupRvLabelAlamatChips()
            setTextListener()
            binding.run {
                cardAddress.root.visible()
                formAddress.root.visible()
                formAddressNegative.root.gone()
                cardAddressNegative.root.gone()

                cardAddress.addressDistrict.text = "${data?.districtName}, ${data?.cityName}, ${data?.provinceName}"

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
            tvInfoCourier.text = getString(R.string.tv_title_nama_penerima_bottomsheet)
            tvInfoCourierDetail.text = context?.let { HtmlLinkHelper(it, getString(R.string.tv_desc_nama_penerima_bottomsheet)).spannedString }
            btnClose.setOnClickListener {
                bottomSheetInfoPenerima?.dismiss()
            }
        }
    }

    private fun validateForm(): Boolean {
        validated = true
        binding.run {
            if (isPositiveFlow) {
                if (formAddress.etLabel.textFieldInput.text.toString().isEmpty() || formAddress.etLabel.textFieldInput.text.toString() == " ") {
                    validated = false
                    setWrapperError(formAddress.etLabel.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddress.etAlamat.textFieldInput.text.toString().isEmpty() || formAddress.etAlamat.textFieldInput.text.toString() == " ") {
                    validated = false
                    setWrapperError(formAddress.etAlamat.textFieldWrapper, getString(R.string.tv_error_field))
                }
            } else {
                if (formAddressNegative.etLabel.textFieldInput.text.toString().isEmpty() || formAddressNegative.etLabel.textFieldInput.text.toString() == " ") {
                    validated = false
                    setWrapperError(formAddressNegative.etLabel.textFieldWrapper, getString(R.string.tv_error_field))
                }

                if (formAddressNegative.etAlamat.textFieldInput.text.toString().isEmpty() || formAddressNegative.etAlamat.textFieldInput.text.toString() == " ") {
                    validated = false
                    setWrapperError(formAddressNegative.etAlamat.textFieldWrapper, getString(R.string.tv_error_field))
                }
            }

            if (formAccount.etNamaPenerima.textFieldInput.text.toString().isEmpty() || formAccount.etNamaPenerima.textFieldInput.text.toString() == " ") {
                validated = false
                setWrapperError(formAccount.etNamaPenerima.textFieldWrapper, getString(R.string.tv_error_field))
            }

            if (formAccount.etNomorHp.textFieldInput.text.toString().isEmpty()  || formAccount.etNomorHp.textFieldInput.text.toString() == " ") {
                validated = false
                setWrapperError(formAccount.etNomorHp.textFieldWrapper, getString(R.string.tv_error_field))
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

    private fun setWrapperError(wrapper: TextInputLayout, s: String?) {
        if (s.isNullOrBlank()) {
            wrapper.error = s
            wrapper.setErrorEnabled(false)
        } else {
            wrapper.setErrorEnabled(true)
            wrapper.error = s
        }
    }

    private fun showDistrictRecommendationBottomSheet() {
        districtRecommendationBottomSheetFragment = DiscomBottomSheetFragment.newInstance(isLogisticLabel, true)
        districtRecommendationBottomSheetFragment.setActionListener(this)
        childFragmentManager?.run {
            districtRecommendationBottomSheetFragment.show(this, "")
        }
    }

    private fun checkKotaKecamatan() {
        if (binding.formAddressNegative.etKotaKecamatan.textFieldInput.text.toString().isEmpty()) {
            showDistrictRecommendationBottomSheet()
        } else {
            goToPinpointPage()
        }
    }

    private fun goToPinpointPage() {
        val bundle = Bundle()
        saveDataModel?.latitude?.toDouble()?.let { bundle.putDouble(AddAddressConstant.EXTRA_LATITUDE, it) }
        saveDataModel?.longitude?.toDouble()?.let { bundle.putDouble(AddAddressConstant.EXTRA_LONGITUDE, it) }
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, false)
        bundle.putString(EXTRA_DISTRICT_NAME, currentDistrictName)
        startActivityForResult(context?.let { PinpointNewPageActivity.createIntent(it, bundle) }, 1998)
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
        binding.formAccount.etNomorHp.textFieldInput.apply {
            addTextChangedListener(object: TextWatcher {
                override fun afterTextChanged(s: Editable?) {
                    //
                }

                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                    //
                }

                override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                    if (s.contains("0")) {
                           filters = arrayOf(InputFilter.LengthFilter(14))
                    } else {
                        filters = arrayOf(InputFilter.LengthFilter(15))
                    }
                }

            })
        }
    }

    private fun setOnTouchLabelAddress(type: String) {
        if (isPositiveFlow) {
            binding.formAddress.etLabel.textFieldInput.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        eventShowListLabelAlamat(type)
                    } else {
                        binding.formAddress.rvLabelAlamatChips.visibility = View.GONE
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
            }
        } else {
            binding.formAddressNegative.etLabel.textFieldInput.apply {
                setOnFocusChangeListener { _, hasFocus ->
                    if (hasFocus) {
                        eventShowListLabelAlamat(type)
                    } else {
                        binding.formAddressNegative.rvLabelAlamatChips.visibility = View.GONE
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
            }
        }

    }

    private fun eventShowListLabelAlamat(type: String) {
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
        saveDataModel?.let { viewModel.saveAddress(it) }
    }

    private fun setSaveAddressDataModel() {
        saveDataModel?.address2 = "$currentLat,$currentLong"
        saveDataModel?.receiverName = binding.formAccount.etNamaPenerima.textFieldInput.text.toString()
        saveDataModel?.phone = binding.formAccount.etNomorHp.textFieldInput.text.toString()
        if (isPositiveFlow) {
            saveDataModel?.address1 = "${binding.formAddress.etAlamat.textFieldInput.text} (${binding.formAddress.etCourierNote.textFieldInput.text})"
            saveDataModel?.addressName =  binding.formAddress.etLabel.textFieldInput.text.toString()
        } else {
            saveDataModel?.address1 = "${binding.formAddressNegative.etAlamat.textFieldInput.text} (${binding.formAddressNegative.etCourierNote.textFieldInput.text})"
            saveDataModel?.addressName =  binding.formAddressNegative.etLabel.textFieldInput.text.toString()
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

        fun newInstance(extra: Bundle): AddressFormFragment {
            return AddressFormFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_SAVE_DATA_UI_MODEL, extra.getParcelable(EXTRA_SAVE_DATA_UI_MODEL))
                    putBoolean(EXTRA_IS_POSITIVE_FLOW, extra.getBoolean(EXTRA_IS_POSITIVE_FLOW))
                }
            }
        }
    }

    override fun onLabelAlamatChipClicked(labelAlamat: String) {
        if (isPositiveFlow) {
            binding.formAddress.rvLabelAlamatChips.visibility = View.GONE
            binding.formAddress.etLabel.textFieldInput.run {
                setText(labelAlamat)
                setSelection(binding.formAddress.etLabel.textFieldInput.text.length)
            }
        } else {
            binding.formAddressNegative.rvLabelAlamatChips.visibility = View.GONE
            binding.formAddressNegative.etLabel.textFieldInput.run {
                setText(labelAlamat)
                setSelection(binding.formAddressNegative.etLabel.textFieldInput.text.length)
            }
        }

    }

    override fun onGetDistrict(districtAddress: Address) {
       districtRecommendationBottomSheetFragment.getDistrict(districtAddress)
    }

    override fun onChooseZipcode(districtAddress: Address, zipCode: String) {
        val kotaKecamatanText = "${districtAddress?.districtName}, ${districtAddress?.cityName} $zipCode"
        currentDistrictName = districtAddress?.districtName.toString()
        binding.formAddressNegative.etKotaKecamatan.textFieldInput.run {
            setText(kotaKecamatanText)
            currentKotaKecamatan = kotaKecamatanText
        }

        val selectedDistrict = "${districtAddress?.provinceName}, ${districtAddress?.cityName}, ${districtAddress?.districtName}"
        saveDataModel?.selectedDistrict = selectedDistrict
        saveDataModel?.cityId = districtAddress.cityId
        saveDataModel?.provinceId = districtAddress.provinceId
        saveDataModel?.districtId = districtAddress.districtId
        saveDataModel?.zipCodes = districtAddress.zipCodes
    }

}