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
import com.tokopedia.logisticaddaddress.common.AddressConstants.*
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

    private var saveDataModel: SaveAddressDataModel? = null
    private var currentLat: Double = 0.0
    private var currentLong: Double = 0.0
    private var labelAlamatList: Array<String> = emptyArray()
    private var staticDimen8dp: Int? = 0
    private var isPositiveFlow: Boolean = true
    /*To differentiate user pinpoint on ANA Negative*/
    private var isPinpoint: Boolean = false
    /*To differentiate flow from logistic or not*/
    private var isLogisticLabel: Boolean = true
    private var validated: Boolean = true
    private lateinit var labelAlamatChipsAdapter: LabelAlamatChipsAdapter
    private lateinit var labelAlamatChipsLayoutManager: ChipsLayoutManager

    private var permissionCheckerHelper: PermissionCheckerHelper? = null

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
            binding.formAccount.etNomorHp.textFieldInput.setText(contact?.contactNumber)
        } else if (requestCode == 1998 && resultCode == Activity.RESULT_OK) {

        }
    }

    private fun prepareData() {
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
            formAccount.etNamaPenerima.textFieldInput.setText(userSession.name)
            formAccount.etNomorHp.setFirstIcon(R.drawable.ic_contact_black)
            formAccount.etNomorHp.getFirstIcon().setOnClickListener {
                onNavigateToContact()
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
                    goToPinpointPage()
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

                formAccount.etNamaPenerima.textFieldInput.addTextChangedListener(setWrapperWatcher(formAccount.etNamaPenerima.textFieldWrapper, null))
                formAccount.etNomorHp.textFieldInput.setText(userSession.phoneNumber)
                formAccount.etNomorHp.textFieldInput.addTextChangedListener(setWrapperWatcherPhone(formAccount.etNomorHp.textFieldWrapper, getString(R.string.validate_no_ponsel_less_char)))
            }
        }

        binding.btnSaveAddress.setOnClickListener {
            if (validateForm()) {
                doSaveAddress()
            }
        }
    }

    private fun validateForm(): Boolean {
        validated = true
        binding.run {
            if (formAddress.etLabel.textFieldInput.text.toString().isEmpty()) {
                validated = false
                setWrapperError(formAddress.etLabel.textFieldWrapper, getString(R.string.tv_error_field))
            }

            if (formAddress.etAlamat.textFieldInput.text.toString().isEmpty()) {
                validated = false
                setWrapperError(formAddress.etAlamat.textFieldWrapper, getString(R.string.tv_error_field))
            }

            if (formAccount.etNamaPenerima.textFieldInput.text.toString().isEmpty()) {
                validated = false
                setWrapperError(formAccount.etNamaPenerima.textFieldWrapper, getString(R.string.tv_error_field))
            }

            if (formAccount.etNomorHp.textFieldInput.text.toString().isEmpty()) {
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
        val districtRecommendationBottomSheetFragment =
                DiscomBottomSheetFragment.newInstance(isLogisticLabel, true)
        districtRecommendationBottomSheetFragment.setActionListener(this)
        childFragmentManager?.run {
            districtRecommendationBottomSheetFragment.show(this, "")
        }
    }

    private fun goToPinpointPage() {
        val bundle = Bundle()
        saveDataModel?.latitude?.toDouble()?.let { bundle.putDouble(AddAddressConstant.EXTRA_LATITUDE, it) }
        saveDataModel?.longitude?.toDouble()?.let { bundle.putDouble(AddAddressConstant.EXTRA_LONGITUDE, it) }
        bundle.putBoolean(EXTRA_IS_POSITIVE_FLOW, false)
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
                           filters = arrayOf(InputFilter.LengthFilter(15))
                    } else {
                        filters = arrayOf(InputFilter.LengthFilter(16))
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
        saveDataModel?.address1 = "${binding.formAddress.etAlamat.textFieldInput.text} (${binding.formAddress.etCourierNote.textFieldInput.text})"
        saveDataModel?.address2 = "$currentLat,$currentLong"
        saveDataModel?.addressName =  binding.formAddress.etLabel.textFieldInput.text.toString()
        saveDataModel?.receiverName = binding.formAccount.etNamaPenerima.textFieldInput.text.toString()
        saveDataModel?.phone = binding.formAccount.etNomorHp.textFieldInput.text.toString()
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
        binding.formAddress.rvLabelAlamatChips.visibility = View.GONE
        binding.formAddress.etLabel.textFieldInput.run {
            setText(labelAlamat)
            setSelection(binding.formAddress.etLabel.textFieldInput.text.length)
        }
    }

    override fun onGetDistrict(districtAddress: Address) {
       //no-op
    }

}