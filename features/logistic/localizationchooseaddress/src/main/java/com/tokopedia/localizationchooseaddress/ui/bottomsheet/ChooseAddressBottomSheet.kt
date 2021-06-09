package com.tokopedia.localizationchooseaddress.ui.bottomsheet

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.location.FusedLocationProviderClient
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.R
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.localizationchooseaddress.di.ChooseAddressComponent
import com.tokopedia.localizationchooseaddress.di.DaggerChooseAddressComponent
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressList
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.EXTRA_IS_FROM_ANA
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant.Companion.EXTRA_SELECTED_ADDRESS_DATA
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.entity.address.DistrictRecommendationAddress
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.permission.PermissionCheckerHelper
import javax.inject.Inject


class ChooseAddressBottomSheet : BottomSheetUnify(), HasComponent<ChooseAddressComponent>,
        AddressListItemAdapter.AddressListItemAdapterListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ChooseAddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ChooseAddressViewModel::class.java]
    }

    private val adapter = AddressListItemAdapter(this)
    private var chooseAddressLayout: ConstraintLayout? = null
    private var buttonLogin: ConstraintLayout? = null
    private var buttonAddAddress: ConstraintLayout? = null
    private var addressList: RecyclerView? = null
    private var listener: ChooseAddressBottomSheetListener? = null

    private var txtLocalization: Typography? = null
    private var divider: View? = null
    private var txtSnippet: Typography? = null
    private var contentLayout: FrameLayout? = null
    private var buttonSnippet: ConstraintLayout? = null
    private var errorLayout: ConstraintLayout? = null
    private var imageError: ImageView? = null
    private var progressBar: LoaderUnify? = null
    private var source: String = ""

    private var fm: FragmentManager? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null
    private var permissionCheckerHelper: PermissionCheckerHelper? = null
    private var fusedLocationClient: FusedLocationProviderClient? = null
    // flag variable to ask permission
    private var shouldShowGpsPopUp: Boolean = false
    // flag variable for asking permission after bottom sheet is shown
    private var hasBottomSheetShown: Boolean = false
    // flag variable to differentiate login and GPS flow
    private var isLoginFlow: Boolean = false
    // flag variable to prevent multiple times asking permission
    private var hasAskedPermission: Boolean = false
    //flag variable to differentiate setState from snippet or not
    private var isSnippetAddressFlow: Boolean = false
    //flag variable to differentiate setState from address bottomsheet or not
    private var isCardAddressClicked: Boolean = false
    //flag variable to differentiate setState from address list or not
    private var isAddressListFlow: Boolean = false
    //flag variable to support warehous location, ex: for tokonow
    private var isSupportWarehouseLoc: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
        permissionCheckerHelper = PermissionCheckerHelper()
        hasAskedPermission = savedInstanceState?.getBoolean(HAS_ASKED_PERMISSION_KEY) ?: false
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putBoolean(HAS_ASKED_PERMISSION_KEY, hasAskedPermission)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        initLayout()
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        changeCloseButtonSize()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setInitialViewState()
        initObserver()
        if (userSession.isLoggedIn) {
            initData()
        } else {
            setViewState(false)
        }
    }

    private fun getPermissions(): Array<String> {
        return arrayOf(
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_FINE_LOCATION,
                PermissionCheckerHelper.Companion.PERMISSION_ACCESS_COARSE_LOCATION)
    }

    @SuppressLint("MissingPermission")
    private fun getLocation() {
        if (ChooseAddressUtils.isGpsEnabled(context)) {
            fusedLocationClient = context?.let { FusedLocationProviderClient(it) }
            fusedLocationClient?.lastLocation?.addOnSuccessListener { location: Location? ->
                if (location != null) {
                    setStateWithLocation(location)
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            REQUEST_CODE_ADD_ADDRESS -> {
                val saveAddressDataModel = data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
                if (saveAddressDataModel != null) {
                    viewModel.setStateChosenAddress(
                            status = 2,
                            addressId = saveAddressDataModel.id.toString(),
                            receiverName = saveAddressDataModel.receiverName,
                            addressName = saveAddressDataModel.addressName,
                            latitude = saveAddressDataModel.latitude,
                            longitude = saveAddressDataModel.longitude,
                            districtId = saveAddressDataModel.districtId.toString(),
                            postalCode = saveAddressDataModel.postalCode,
                            isTokonow = isSupportWarehouseLoc
                    )
                    isSnippetAddressFlow = false
                    isAddressListFlow = false
                }
            }
            REQUEST_CODE_GET_DISTRICT_RECOM -> {
                val discomModel = data?.getParcelableExtra<DistrictRecommendationAddress>("district_recommendation_address")
                val latitude = data?.getStringExtra("latitude")
                val longitude = data?.getStringExtra("longitude")
                if (discomModel != null) {
                    viewModel.setStateChosenAddress(
                            status = 4,
                            addressId = null,
                            addressName = "",
                            receiverName = "",
                            districtId = discomModel.districtId.toString(),
                            latitude = latitude,
                            longitude = longitude,
                            postalCode = "",
                            isTokonow = isSupportWarehouseLoc
                    )
                    isSnippetAddressFlow = true
                    isAddressListFlow = false
                }
            }
            REQUEST_CODE_ADDRESS_LIST -> {
                val recipientAddress = data?.getParcelableExtra<RecipientAddressModel>(EXTRA_SELECTED_ADDRESS_DATA)
                val isFromANA = data?.getBooleanExtra(EXTRA_IS_FROM_ANA, false)
                if (recipientAddress != null && isFromANA == false) {
                    viewModel.setStateChosenAddress(
                            status = recipientAddress.addressStatus,
                            addressId = recipientAddress.id.toString(),
                            receiverName = recipientAddress.recipientName,
                            addressName = recipientAddress.addressName,
                            latitude = recipientAddress.latitude,
                            longitude = recipientAddress.longitude,
                            districtId = recipientAddress.destinationDistrictId.toString(),
                            postalCode = recipientAddress.postalCode,
                            isTokonow = isSupportWarehouseLoc
                    )
                    isSnippetAddressFlow = false
                    isAddressListFlow = true
                } else if (isFromANA == true) {
                    listener?.onAddressDataChanged()
                    dismissBottomSheet()
                } else {
                    dismissBottomSheet()
                }
            }
            REQUEST_CODE_LOGIN_PAGE -> {
                if (resultCode == Activity.RESULT_OK) {
                    isLoginFlow = true
                    setInitialViewState()
                    val chooseAddressPref = context?.getSharedPreferences(CHOOSE_ADDRESS_PREF, Context.MODE_PRIVATE)
                    chooseAddressPref?.edit()?.clear()?.apply()
                    viewModel.getDefaultChosenAddress("", source, isSupportWarehouseLoc)
                }
            }
        }
    }

    private fun initInjector() {
        component.inject(this)
    }

    private fun initLayout() {
        clearContentPadding = true
        val view = View.inflate(context, R.layout.bottomsheet_choose_address, null)
        setupView(view)
        setChild(view)
        setCloseClickListener {
            ChooseAddressTracking.onClickCloseBottomSheet(userSession.userId)
            dismissBottomSheet()
        }
        setOnDismissListener {
            listener?.onDismissChooseAddressBottomSheet()
        }
        setShowListener {
            onBottomSheetShown()
        }
    }

    private fun setupView(child: View) {
        chooseAddressPref = ChooseAddressSharePref(context)
        chooseAddressLayout = child.findViewById(R.id.choose_address_layout)
        buttonAddAddress = child.findViewById(R.id.no_address_layout)
        buttonLogin = child.findViewById(R.id.login_layout)
        addressList = child.findViewById(R.id.rv_address_card)

        divider = child.findViewById(R.id.divider)
        txtSnippet = child.findViewById(R.id.txt_snippet_location)
        txtLocalization = child.findViewById(R.id.txt_bottomsheet_localization)
        contentLayout = child.findViewById(R.id.frame_content_layout)
        buttonSnippet = child.findViewById(R.id.pilih_kota_kecamatan_layout)
        errorLayout = child.findViewById(R.id.error_state_layout)
        imageError = child.findViewById(R.id.img_info_error)
        progressBar = child.findViewById(R.id.progress_bar)

        addressList?.adapter = adapter
        addressList?.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
    }

    private fun initData() {
        source = listener?.getLocalizingAddressHostSourceBottomSheet().toString()
        isSupportWarehouseLoc = listener?.isSupportWarehouseLoc()?: false
        viewModel.getChosenAddressList(source)
    }

    private fun initObserver() {
        viewModel.chosenAddressList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    adapter.updateData(it.data)
                    setViewState(true)
                }

                is Fail -> {
                    setErrorViewState()
                }
            }
        })

        viewModel.setChosenAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (isCardAddressClicked) ChooseAddressTracking.onClickAvailableAddress(userSession.userId, IS_SUCCESS)
                    else if (isAddressListFlow) ChooseAddressTracking.onClickButtonPilihAlamat(userSession.userId, IS_SUCCESS)
                    val data = it.data
                    var localData = LocalCacheModel()
                    if (isSnippetAddressFlow) {
                        localData = ChooseAddressUtils.setLocalizingAddressData(
                                addressId = data.addressId.toString(),
                                cityId = data.cityId.toString(),
                                districtId = data.districtId.toString(),
                                lat = data.latitude,
                                long = data.longitude,
                                label = "${data.districtName}, ${data.cityName}",
                                postalCode = data.postalCode,
                                warehouseId = data.tokonowModel.warehouseId.toString(),
                                shopId = data.tokonowModel.shopId.toString()
                        )
                    } else {
                        localData = ChooseAddressUtils.setLocalizingAddressData(
                                addressId = data.addressId.toString(),
                                cityId = data.cityId.toString(),
                                districtId = data.districtId.toString(),
                                lat = data.latitude,
                                long = data.longitude,
                                label = "${data.addressName} ${data.receiverName}",
                                postalCode = data.postalCode,
                                warehouseId = data.tokonowModel.warehouseId.toString(),
                                shopId = data.tokonowModel.shopId.toString()
                        )
                    }

                    chooseAddressPref?.setLocalCache(localData)
                    listener?.onAddressDataChanged()
                    dismissBottomSheet()
                }

                is Fail -> {
                    if (isCardAddressClicked) ChooseAddressTracking.onClickAvailableAddress(userSession.userId, IS_NOT_SUCCESS)
                    else if (isAddressListFlow) ChooseAddressTracking.onClickButtonPilihAlamat(userSession.userId, IS_NOT_SUCCESS)
                    listener?.onLocalizingAddressServerDown()
                    dismissBottomSheet()
                }

            }
        })

        viewModel.getDefaultAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (it.data.keroAddrError.detail.isNotEmpty()) {
                        showToaster(getString(R.string.toaster_failed_chosen_address), Toaster.TYPE_ERROR)
                        if (isLoginFlow) {
                            initData()
                        } else {
                            setViewState(userSession.isLoggedIn)
                        }
                    } else if (it.data.addressData.cityId != 0){
                        val data = it.data.addressData
                        val localData = ChooseAddressUtils.setLocalizingAddressData(
                                addressId = data.addressId.toString(),
                                cityId = data.cityId.toString(),
                                districtId = data.districtId.toString(),
                                lat = data.latitude,
                                long = data.longitude,
                                label = ChooseAddressUtils.setLabel(ChosenAddressModel(
                                        addressName = data.addressName,
                                        receiverName = data.receiverName,
                                        districtName = data.districtName,
                                        cityName = data.cityName
                                )),
                                postalCode = data.postalCode,
                                warehouseId = it.data.tokonow.warehouseId.toString(),
                                shopId = it.data.tokonow.warehouseId.toString()
                        )
                        chooseAddressPref?.setLocalCache(localData)
                        if (isLoginFlow) {
                            listener?.onLocalizingAddressLoginSuccessBottomSheet()
                            listener?.onAddressDataChanged()
                            dismissBottomSheet()
                        } else {
                            listener?.onAddressDataChanged()
                            dismissBottomSheet()
                        }
                    } else {
                        chooseAddressPref?.setLocalCache(ChooseAddressConstant.defaultAddress)
                        listener?.onAddressDataChanged()
                        dismissBottomSheet()
                    }
                }

                is Fail -> {
                    listener?.onLocalizingAddressServerDown()
                    dismissBottomSheet()
                }
            }
        })
    }

    private fun showToaster(message: String, type: Int) {
        val toaster = Toaster
        view?.rootView?.let { v ->
            toaster.build(v, message, Toaster.LENGTH_SHORT, type, "").show()
        }
    }

    private fun setInitialViewState() {
        progressBar?.visible()
        txtLocalization?.gone()
        contentLayout?.gone()
        divider?.gone()
        txtSnippet?.gone()
        buttonSnippet?.gone()
        errorLayout?.gone()
        setTitle("")
    }

    private fun setViewState(loginState: Boolean) {
        if (!loginState) {
            chooseAddressLayout?.gone()
            buttonAddAddress?.gone()
            buttonLogin?.visible()
            shouldShowGpsPopUp = true
            showGpsPopUp()
        } else {
            if (adapter.containsChosenAddress()) {
                buttonLogin?.gone()
                buttonAddAddress?.gone()
                chooseAddressLayout?.visible()
            } else {
                chooseAddressLayout?.gone()
                buttonLogin?.gone()
                buttonAddAddress?.visible()
                shouldShowGpsPopUp = true
                showGpsPopUp()
            }
        }
        errorLayout?.gone()
        progressBar?.gone()
        txtLocalization?.visible()
        contentLayout?.visible()
        divider?.visible()
        txtSnippet?.visible()
        buttonSnippet?.visible()

        setTitle(getString(R.string.bottomsheet_choose_address_title))
        renderButton()
        setCloseClickListener {
            ChooseAddressTracking.onClickCloseBottomSheet(userSession.userId)
            dismissBottomSheet()
        }
    }

    private fun renderButton() {
        buttonLogin?.setOnClickListener {
            ChooseAddressTracking.onClickMasukBottomSheet(userSession.userId)
            startActivityForResult(RouteManager.getIntent(context, ApplinkConst.LOGIN), REQUEST_CODE_LOGIN_PAGE)
        }

        buttonAddAddress?.setOnClickListener {
            ChooseAddressTracking.onClickButtonTambahAlamatBottomSheet(userSession.userId)
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
                putExtra(EXTRA_IS_FULL_FLOW, true)
                putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
            }, REQUEST_CODE_ADD_ADDRESS)
        }

        buttonSnippet?.setOnClickListener {
            ChooseAddressTracking.onClickPilihKotaKecamatan(userSession.userId)
            startActivityForResult(RouteManager.getIntent(context, ApplinkConstInternalMarketplace.DISTRICT_RECOMMENDATION_SHOP_SETTINGS).apply {
                putExtra(IS_LOCALIZATION, true)
            }, REQUEST_CODE_GET_DISTRICT_RECOM)
        }
    }

    private fun setErrorViewState() {
        progressBar?.gone()
        txtLocalization?.gone()
        contentLayout?.gone()
        buttonSnippet?.gone()
        errorLayout?.visible()
        imageError?.setImageDrawable(context?.let { ContextCompat.getDrawable(it, R.drawable.ic_service_error) })
        setTitle("")
        listener?.onLocalizingAddressServerDown()
        setCloseClickListener {
            ChooseAddressTracking.onClickCloseBottomSheet(userSession.userId)
            dismissBottomSheet()
        }
    }

    fun show(fm: FragmentManager?) {
        this.fm = fm
        fm?.let {
            show(it, "")
        }
    }

    fun setListener(listener: ChooseAddressBottomSheetListener) {
        this.listener = listener
    }

    override fun getComponent(): ChooseAddressComponent {
        return DaggerChooseAddressComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
    }

    companion object {
        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
        const val EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL"
        const val EXTRA_IS_LOCALIZATION = "EXTRA_IS_LOCALIZATION"
        const val IS_LOCALIZATION = "is_localization"
        const val IS_SUCCESS = "success"
        const val IS_NOT_SUCCESS = "not success"
        const val REQUEST_CODE_ADD_ADDRESS = 199
        const val REQUEST_CODE_GET_DISTRICT_RECOM = 299
        const val REQUEST_CODE_ADDRESS_LIST = 399
        const val REQUEST_CODE_LOGIN_PAGE = 499

        private const val HAS_ASKED_PERMISSION_KEY = "has_asked_permission"
        private const val CHOOSE_ADDRESS_PREF = "local_choose_address"
    }

    override fun onItemClicked(address: ChosenAddressList) {
        isCardAddressClicked = true
        viewModel.setStateChosenAddress(
                status = address.status,
                addressId = address.addressId,
                receiverName =  address.receiverName,
                addressName = address.addressname,
                latitude = address.latitude,
                longitude = address.longitude,
                districtId = address.districtId,
                postalCode = address.postalCode,
                isTokonow = isSupportWarehouseLoc
        )
    }

    override fun onOtherAddressClicked() {
        ChooseAddressTracking.onClickCekAlamatLainnya(userSession.userId)
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.MANAGE_ADDRESS)
        intent.putExtra(EXTRA_IS_LOCALIZATION, true)
        startActivityForResult(intent, REQUEST_CODE_ADDRESS_LIST)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        permissionCheckerHelper?.onRequestPermissionsResult(context, requestCode, permissions, grantResults)
    }

    private fun setStateWithLocation(location: Location) {
        isLoginFlow = false
        setInitialViewState()
        viewModel.getDefaultChosenAddress("${location.latitude},${location.longitude}", source, isSupportWarehouseLoc)
    }

    private fun showGpsPopUp() {
        if (shouldShowGpsPopUp && hasBottomSheetShown && !hasAskedPermission) {
            hasAskedPermission = true
            if (permissionCheckerHelper?.hasPermission(requireContext(), getPermissions()) == false) {
                permissionCheckerHelper?.checkPermissions(this, getPermissions(), object : PermissionCheckerHelper.PermissionCheckListener {
                    override fun onPermissionDenied(permissionText: String) {
                        ChooseAddressTracking.onClickDontAllowLocation(userSession.userId)
                    }

                    override fun onNeverAskAgain(permissionText: String) {
                        //no op
                    }

                    override fun onPermissionGranted() {
                        ChooseAddressTracking.onClickAllowLocation(userSession.userId)
                        getLocation()
                    }
                })
            }
        }
    }

    private fun dismissBottomSheet() {
        listener?.onDismissChooseAddressBottomSheet()
        this.dismiss()
    }

    // This is a workaround to make sure Permissions Dialog is shown after the bottom sheet is shown
    private fun onBottomSheetShown() {
        hasBottomSheetShown = true
        showGpsPopUp()
    }

    private fun changeCloseButtonSize() {
        context?.also { context ->
            bottomSheetClose.apply {
                setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_bottomsheet_close_choose_address))
                layoutParams.apply {
                    width = context.resources.getDimension(R.dimen.choose_address_close).toInt()
                    height = context.resources.getDimension(R.dimen.choose_address_close).toInt()
                }
            }
        }
    }

    interface ChooseAddressBottomSheetListener {
        /**
         * this listen if we get server down on widget/bottomshet.
         * Host mandatory to GONE LocalizingAddressWidget
         */
        fun onLocalizingAddressServerDown()

        /**
         * Only use by bottomsheet, to notify every changes in address data
         */
        fun onAddressDataChanged()

        /**
         * String Source of Host Page
         */
        fun getLocalizingAddressHostSourceBottomSheet(): String

        /**
         * this listen is use to notify host/fragment if login is success from bottomshet
         */
        fun onLocalizingAddressLoginSuccessBottomSheet()

        fun onDismissChooseAddressBottomSheet()

        /**
         * To differentiate feature that need warehouse loc or not
         */
        fun isSupportWarehouseLoc(): Boolean {
            return true
        }
    }
}