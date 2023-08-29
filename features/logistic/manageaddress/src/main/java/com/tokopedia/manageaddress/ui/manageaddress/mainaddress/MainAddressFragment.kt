package com.tokopedia.manageaddress.ui.manageaddress.mainaddress

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.config.GlobalConfig
import com.tokopedia.dialog.DialogUnify
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntSafely
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.ChosenAddressModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.domain.model.LocalWarehouseModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddressConstant
import com.tokopedia.logisticCommon.data.constant.AddressConstant.ANA_REVAMP_FEATURE_ID
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EDIT_ADDRESS_REVAMP_FEATURE_ID
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.response.KeroAddrStateChosenAddressData
import com.tokopedia.logisticCommon.data.response.KeroAddressRespTokonow
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.data.analytics.ManageAddressAnalytics
import com.tokopedia.manageaddress.data.analytics.ShareAddressAnalytics
import com.tokopedia.manageaddress.databinding.BottomsheetActionAddressBinding
import com.tokopedia.manageaddress.databinding.FragmentMainAddressBinding
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.domain.response.SetDefaultPeopleAddressResponse
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressFragment
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressItemAdapter
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressViewModel
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressBottomSheet
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressConfirmationBottomSheet
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.EDIT_PARAM
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_REF
import com.tokopedia.manageaddress.util.ManageAddressConstant.KERO_TOKEN
import com.tokopedia.manageaddress.util.ManageAddressConstant.LABEL_LAINNYA
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_CREATE
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_EDIT
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_CART_EXISTING_USER
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_CHOOSE_ADDRESS_EXISTING_USER
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_USER_NEW
import com.tokopedia.media.loader.loadImage
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.userconsent.domain.collection.ConsentCollectionParam
import com.tokopedia.usercomponents.userconsent.ui.UserConsentWidget
import com.tokopedia.utils.lifecycle.autoClearedNullable
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * MainAddressFragment
 * fragment inside viewPager of ManageAddressFragment
 */
class MainAddressFragment :
    BaseDaggerFragment(),
    ManageAddressItemAdapter.MainAddressItemAdapterListener,
    ShareAddressConfirmationBottomSheet.Listener {

    companion object {

        private const val EMPTY_STATE_PICT_URL =
            "https://images.tokopedia.net/android/others/pilih_alamat_pengiriman3x.png"
        private const val EMPTY_SEARCH_PICT_URL =
            "https://images.tokopedia.net/android/others/address_not_found3x.png"
        private const val IS_SUCCESS = "success"
        private const val IS_NOT_SUCCESS = "not success"
        private const val TOAST_SHOWING_TIME = 3000L

        fun newInstance(bundle: Bundle): MainAddressFragment {
            return MainAddressFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = ManageAddressItemAdapter()

    private val viewModel: ManageAddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ManageAddressViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentMainAddressBinding>()
    private var bottomSheetLainnya: BottomSheetUnify? = null
    private var bottomSheetShareAddress: BottomSheetUnify? = null
    private var bottomSheetConfirmationShareAddress: BottomSheetUnify? = null

    private var _selectedAddressItem: RecipientAddressModel? = null

    private var maxItemPosition: Int = -1
    private var isLoading: Boolean = false
    private var isFromCheckoutChangeAddress: Boolean? = false
    private var isFromCheckoutSnippet: Boolean? = false
    private var isLocalization: Boolean? = false
    private var typeRequest: Int? = -1
    private var prevState: Int = -1
    private var localChosenAddr: LocalCacheModel? = null
    private var isStayOnPageState: Boolean? = false
    private var mainAddressListener: MainAddressListener? = null
    private var leavePageJob: Job? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ManageAddressComponent::class.java).inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainAddressBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
        initAddAddressBtnListener()
        initView()
        initAdapter()
        initSearch()
        observerListAddress()
        observerSetDefault()
        observerGetChosenAddress()
        observerSetChosenAddress()
        observerRemovedAddress()
        observerEligibleForAddressFeature()
    }

    private fun initArguments() {
        isFromCheckoutChangeAddress =
            arguments?.getBoolean(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS)
        isFromCheckoutSnippet =
            arguments?.getBoolean(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_SNIPPET)
        isLocalization = arguments?.getBoolean(ManageAddressConstant.EXTRA_IS_LOCALIZATION)
        typeRequest = arguments?.getInt(CheckoutConstant.EXTRA_TYPE_REQUEST)
        prevState = arguments?.getInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS) ?: -1
        localChosenAddr = context?.let { ChooseAddressUtils.getLocalizingAddressData(it) }
        viewModel.savedQuery = arguments?.getString(ManageAddressConstant.EXTRA_QUERY) ?: ""
        viewModel.receiverUserId = arguments?.getString(ManageAddressConstant.QUERY_PARAM_RUID)
        viewModel.senderUserId = arguments?.getString(ManageAddressConstant.QUERY_PARAM_SUID)
        viewModel.receiverUserName =
            arguments?.getString(ManageAddressConstant.EXTRA_RECEIVER_USER_NAME)
        viewModel.source = arguments?.getString(PARAM_SOURCE) ?: ""
    }

    private fun initAddAddressBtnListener() {
        mainAddressListener?.setAddButtonOnClickListener {
            if (isLocalization == true) ChooseAddressTracking.onClickButtonTambahAlamat(userSession.userId)
            openFormAddressView(null)
        }
    }

    private fun initAdapter() {
        adapter.apply {
            setMainAddressListener(
                isNeedToShareAddress = viewModel.isNeedToShareAddress,
                listener = this@MainAddressFragment
            )
        }

        binding?.addressList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initView() {
        setButtonEnabled(false)
        updateButton(
            if (viewModel.isNeedToShareAddress) {
                getString(R.string.btn_share_adddress)
            } else {
                getString(R.string.pilih_alamat)
            }
        )

        binding?.apply {
            emptyStateManageAddress?.ivEmptyState?.loadImage(EMPTY_STATE_PICT_URL)
            ivEmptyAddress?.loadImage(EMPTY_SEARCH_PICT_URL)
        }
        initScrollListener()
    }

    private fun observerSetChosenAddress() {
        viewModel.setChosenAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    if (binding?.btnChooseAddress?.text == getString((R.string.pilih_alamat))) {
                        ChooseAddressTracking.onClickButtonPilihAlamat(
                            userSession.userId,
                            IS_SUCCESS
                        )
                    }
                    val data = it.data
                    context?.let { context ->
                        ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                            context,
                            data.addressId.toString(),
                            data.cityId.toString(),
                            data.districtId.toString(),
                            data.latitude,
                            data.longitude,
                            ChooseAddressUtils.setLabel(data),
                            data.postalCode,
                            data.tokonowModel.shopId.toString(),
                            data.tokonowModel.warehouseId.toString(),
                            TokonowWarehouseMapper.mapWarehousesModelToLocal(data.tokonowModel.warehouses),
                            data.tokonowModel.serviceType
                        )
                    }
                    if (isFromCheckoutChangeAddress == true) {
                        val resultIntent = Intent().apply {
                            if (viewModel.isFromMoneyIn) {
                                putExtra(
                                    CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA,
                                    _selectedAddressItem
                                )
                            } else {
                                putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, data)
                            }
                        }
                        activity?.setResult(
                            CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS,
                            resultIntent
                        )
                    } else if (isFromCheckoutSnippet == true) {
                        activity?.setResult(CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_SELECT_ADDRESS_FOR_SNIPPET)
                    }
                    activity?.finish()
                }

                is Fail -> {
                    if (binding?.btnChooseAddress?.text == getString(R.string.pilih_alamat)) {
                        ChooseAddressTracking.onClickButtonPilihAlamat(
                            userSession.userId,
                            IS_NOT_SUCCESS
                        )
                    }
                    showToaster(
                        message = it.throwable.message ?: DEFAULT_ERROR_MESSAGE,
                        toastType = Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observerGetChosenAddress() {
        viewModel.getChosenAddress.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    val data = it.data
                    context?.let { context ->
                        ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                            context,
                            data.addressId.toString(),
                            data.cityId.toString(),
                            data.districtId.toString(),
                            data.latitude,
                            data.longitude,
                            ChooseAddressUtils.setLabel(data),
                            data.postalCode,
                            data.tokonowModel.shopId.toString(),
                            data.tokonowModel.warehouseId.toString(),
                            TokonowWarehouseMapper.mapWarehousesModelToLocal(data.tokonowModel.warehouses),
                            data.tokonowModel.serviceType,
                            data.tokonowModel.lastUpdate
                        )
                    }
                }

                is Fail -> {
                    showToaster(
                        message = it.throwable.message ?: DEFAULT_ERROR_MESSAGE,
                        toastType = Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun observerListAddress() {
        viewModel.addressList.observe(viewLifecycleOwner) {
            when (it) {
                is ManageAddressState.Success -> {
                    binding?.run {
                        swipeRefresh.isRefreshing = false
                        globalError.gone()
                    }
                    if (viewModel.isClearData) clearData()

                    setupTicker(
                        if (it.data.listAddress.isNotEmpty()) {
                            it.data.pageInfo?.ticker
                        } else {
                            null
                        }
                    )

                    if (it.data.listAddress.isNotEmpty()) {
                        updateStateForCheckoutSnippet(it.data.listAddress)
                        if (viewModel.isNeedToShareAddress.not()) {
                            updateButton(it.data.pageInfo?.buttonLabel)
                        }
                    }
                    updateData(it.data.listAddress)
                    setEmptyState()
                    isLoading = false
                }

                is ManageAddressState.Fail -> {
                    setupTicker()
                    binding?.swipeRefresh?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                    isLoading = false
                }

                else -> {
                    binding?.swipeRefresh?.isRefreshing = true
                    isLoading = true
                }
            }
        }
    }

    private fun setupTicker(firstTicker: String? = null) {
        if (viewModel.page == 1) {
            mainAddressListener?.setupTicker(firstTicker)
        }
    }

    private fun observerSetDefault() {
        viewModel.setDefault.observe(viewLifecycleOwner) {
            when (it) {
                is ManageAddressState.Success ->
                    if (isLocalization == true || isFromCheckoutChangeAddress == true || isFromCheckoutSnippet == true) {
                        bottomSheetLainnya?.dismiss()
                        setChosenAddressFromDefaultAddress(it.data)
                    } else {
                        bottomSheetLainnya?.dismiss()
                        setChosenAddressFromChosenAddressResponse(it.data.data.isStateChosenAddressChanged, it.data.data.chosenAddressData, it.data.data.tokonow)
                    }

                is ManageAddressState.Fail -> {
                    bottomSheetLainnya?.dismiss()
                    showToaster(
                        message = it.throwable?.message ?: DEFAULT_ERROR_MESSAGE,
                        toastType = Toaster.TYPE_ERROR
                    )
                }

                else -> {
                    // no-op
                }
            }
        }
    }

    private fun observerRemovedAddress() {
        viewModel.resultRemovedAddress.observe(viewLifecycleOwner) {
            when (it) {
                is ManageAddressState.Success -> {
                    setChosenAddressFromChosenAddressResponse(it.data.isStateChosenAddressChanged, it.data.chosenAddressData, it.data.tokonow)
                    showToaster(getString(R.string.toaster_remove_address_success))
                    viewModel.searchAddress(viewModel.savedQuery, prevState, it.data.chosenAddressData.addressId, true)
                }

                else -> {
                    // no-op
                }
            }
        }
    }

    private fun observerEligibleForAddressFeature() {
        viewModel.eligibleForAddressFeature.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    when (it.data.featureId) {
                        ANA_REVAMP_FEATURE_ID -> {
                            goToAddAddress(it.data.eligible)
                        }
                        EDIT_ADDRESS_REVAMP_FEATURE_ID -> {
                            it.data.data?.let { recipientAddressModel ->
                                goToEditAddress(
                                    it.data.eligible,
                                    recipientAddressModel
                                )
                            }
                        }
                    }
                }
                is Fail -> {
                    showToaster(
                        message = it.throwable.message ?: DEFAULT_ERROR_MESSAGE,
                        toastType = Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PARAM_CREATE) {
            val addressDataModel =
                data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
            if (addressDataModel != null) {
                showToaster(getString(R.string.add_address_success))
                setChosenAddressANA(addressDataModel)
            } else {
                performSearch(viewModel.savedQuery, null)
            }
        } else if (requestCode == REQUEST_CODE_PARAM_EDIT) {
            performSearch(viewModel.savedQuery, null)
            setButtonEnabled(true)
            val addressData = data?.getStringExtra(AddressConstant.EXTRA_EDIT_ADDRESS)
            if (addressData != null) {
                val isEditChosenAddress =
                    data.getBooleanExtra(LogisticConstant.EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED, false)
                if (isEditChosenAddress) {
                    val addressDataModel =
                        data.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
                    if (addressDataModel != null) {
                        context?.let {
                            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                                it,
                                addressDataModel.id.toString(),
                                addressDataModel.cityId.toString(),
                                addressDataModel.districtId.toString(),
                                addressDataModel.latitude,
                                addressDataModel.longitude,
                                "${addressDataModel.addressName} ${addressDataModel.receiverName}",
                                addressDataModel.postalCode,
                                addressDataModel.shopId.toString(),
                                addressDataModel.warehouseId.toString(),
                                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressDataModel.warehouses),
                                addressDataModel.serviceType
                            )
                        }
                    }
                }
                showToaster(message = getString(R.string.edit_address_success))
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomSheetLainnya = null
    }

    fun performSearch(query: String, saveAddressDataModel: SaveAddressDataModel?) {
        clearData()
        maxItemPosition = 0
        val addrId = saveAddressDataModel?.id ?: getChosenAddrId()
        context?.let {
            viewModel.searchAddress(query, prevState, addrId, true)
        }
    }

    private fun goToAddAddress(eligible: Boolean) {
        val token = viewModel.token
        val screenName = if (isFromCheckoutChangeAddress == true && isLocalization == false) {
            SCREEN_NAME_CART_EXISTING_USER
        } else if (isFromCheckoutChangeAddress == false && isLocalization == true) {
            SCREEN_NAME_CHOOSE_ADDRESS_EXISTING_USER
        } else {
            SCREEN_NAME_USER_NEW
        }
        if (eligible) {
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, screenName)
            intent.putExtra(PARAM_SOURCE, viewModel.source)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        } else {
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, screenName)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        }
    }

    private fun goToEditAddress(eligibleForEditRevamp: Boolean, data: RecipientAddressModel) {
        if (eligibleForEditRevamp) {
            val intent = RouteManager.getIntent(
                context,
                "${ApplinkConstInternalLogistic.EDIT_ADDRESS_REVAMP}${data.id}"
            )
            intent.putExtra(PARAM_SOURCE, viewModel.source)
            startActivityForResult(intent, REQUEST_CODE_PARAM_EDIT)
        } else {
            val token = viewModel.token
            val intent =
                RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V1)
            val mapper = AddressModelMapper()
            intent.putExtra(EDIT_PARAM, mapper.transform(data))
            intent.putExtra(KERO_TOKEN, token)
            startActivityForResult(intent, REQUEST_CODE_PARAM_EDIT)
        }
    }

    private fun initScrollListener() {
        binding?.addressList?.addOnScrollListener(
            object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val adapter = recyclerView.adapter
                    val totalItemCount = adapter?.itemCount
                    val lastVisibleItemPosition =
                        (recyclerView.layoutManager as LinearLayoutManager)
                            .findLastVisibleItemPosition()

                    if (maxItemPosition < lastVisibleItemPosition) {
                        maxItemPosition = lastVisibleItemPosition
                    }

                    if ((maxItemPosition + 1) == totalItemCount && viewModel.canLoadMore && !isLoading) {
                        context?.let {
                            viewModel.loadMore(prevState, getChosenAddrId(), true)
                        }
                    }
                }
            }
        )
    }

    /**
     * have trigger to search in Manage Address Fragment
     */
    private fun setEmptyState() {
        binding?.run {
            if (adapter.addressList.isNotEmpty()) {
                emptyStateManageAddress.root.gone()
                getManageAddressFragment()?.searchInputVisibility(true)

                this.addressList.visible()
                emptySearch.gone()
            } else if (viewModel.savedQuery.isEmpty()) {
                emptyStateManageAddress.btnAddEmpty.setOnClickListener {
                    openFormAddressView(null)
                }
                emptyStateManageAddress.root.visible()
                getManageAddressFragment()?.searchInputVisibility(false)

                addressList.gone()
                emptySearch.gone()
            } else {
                emptySearch.visible()
                emptyStateManageAddress.root.gone()
                getManageAddressFragment()?.searchInputVisibility(true)

                addressList.gone()
            }
        }
    }

    /**
     * its called when first open main address
     */
    private fun initSearch() {
        val searchKey = viewModel.savedQuery
        performSearch(searchKey, null)
        if (isLocalization == true) ChooseAddressTracking.impressAddressListPage(userSession.userId)
    }

    private fun updateData(data: List<RecipientAddressModel>) {
        adapter.addList(data)
    }

    private fun updateButton(btnLabel: String?) {
        btnLabel?.let {
            binding?.run {
                llBtn.visible()
                btnChooseAddress.text = if (it.isEmpty()) getString(R.string.pilih_alamat) else it
            }
        }
    }

    private fun updateStateForCheckoutSnippet(addressList: List<RecipientAddressModel>) {
        if (isFromCheckoutSnippet == true && _selectedAddressItem == null) {
            val peopleAddress = addressList.firstOrNull { it.isStateChosenAddress }
            if (peopleAddress != null) {
                setButtonEnabled(true)
                _selectedAddressItem = peopleAddress
            }
        }
    }

    private fun clearData() {
        adapter.clearData()
    }

    override fun onManageAddressEditClicked(peopleAddress: RecipientAddressModel) {
        if (isLocalization == true) ChooseAddressTracking.onClickButtonUbahAlamat(userSession.userId)
        openFormAddressView(peopleAddress)
    }

    override fun onManageAddressLainnyaClicked(peopleAddress: RecipientAddressModel) {
        openBottomSheetView(peopleAddress)
    }

    private fun openFormAddressView(data: RecipientAddressModel?) {
        if (data == null) {
            viewModel.checkUserEligibilityForAnaRevamp()
        } else {
            ManageAddressAnalytics.sendClickButtonUbahAlamatEvent()
            viewModel.checkUserEligibilityForEditAddressRevamp(data)
        }
    }

    private fun openBottomSheetView(data: RecipientAddressModel) {
        bottomSheetLainnya = BottomSheetUnify()
        val viewBottomSheetLainnya =
            BottomsheetActionAddressBinding.inflate(LayoutInflater.from(context), null, false)
                .apply {
                    if (data.addressStatus == 2) {
                        btnAlamatUtama.gone()
                        divider.gone()
                        btnAlamatUtamaChoose.gone()
                        dividerUtamaChoose.gone()
                    } else {
                        if (!data.isStateChosenAddress) {
                            btnAlamatUtama.gone()
                            divider.gone()
                            btnAlamatUtamaChoose.visible()
                            dividerUtamaChoose.visible()
                        } else {
                            btnAlamatUtama.visible()
                            divider.visible()
                            btnAlamatUtamaChoose.gone()
                            dividerUtamaChoose.gone()
                        }
                    }
                    btnAlamatUtama.setOnClickListener {
                        if (isFromCheckoutChangeAddress == true || isLocalization == true) {
                            _selectedAddressItem = data
                        }
                        isStayOnPageState = true
                        viewModel.setDefaultPeopleAddress(
                            data.id,
                            false,
                            prevState,
                            data.id.toLong(),
                            true
                        )
                        bottomSheetLainnya?.dismiss()
                    }
                    btnHapusAlamat.setOnClickListener {
                        bottomSheetLainnya?.dismiss()
                        showDeleteAddressDialog(data.id)
                    }
                    btnAlamatUtamaChoose.setOnClickListener {
                        isStayOnPageState = false
                        context?.let {
                            viewModel.setDefaultPeopleAddress(
                                data.id,
                                true,
                                prevState,
                                data.id.toLong(),
                                true
                            )
                        }
                        _selectedAddressItem = data
                    }
                }

        bottomSheetLainnya?.apply {
            setTitle(LABEL_LAINNYA)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetLainnya.root)
            setOnDismissListener { dismiss() }
        }

        activity?.apply {
            bottomSheetLainnya?.show(supportFragmentManager, "show")
        }
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                        GlobalError.NO_CONNECTION
                    )
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        showGlobalError(GlobalError.SERVER_ERROR)
                        showToaster(
                            message = DEFAULT_ERROR_MESSAGE,
                            toastType = Toaster.TYPE_ERROR
                        )
                    }
                }
            }
            else -> {
                showGlobalError(GlobalError.SERVER_ERROR)
                showToaster(
                    message = throwable.message ?: DEFAULT_ERROR_MESSAGE,
                    toastType = Toaster.TYPE_ERROR
                )
            }
        }
    }

    private fun showGlobalError(type: Int) {
        binding?.run {
            globalError.setType(type)
            globalError.setActionClickListener {
                context?.let {
                    viewModel.searchAddress("", prevState, getChosenAddrId(), true)
                }
            }
            getManageAddressFragment()?.searchInputVisibility(false)
            addressList.gone()
            emptyStateManageAddress.root.gone()
            globalError.visible()
            emptySearch.gone()
        }
    }

    override fun onAddressItemSelected(peopleAddress: RecipientAddressModel) {
        ShareAddressAnalytics.onChooseAddressList()
        setButtonEnabled(true)
        _selectedAddressItem = peopleAddress
        if (isLocalization == true) {
            ChooseAddressTracking.onClickAvailableAddressAddressList(
                userSession.userId
            )
        }
    }

    private fun setChosenAddress() {
        val addressData = _selectedAddressItem
        if (isStayOnPageState == false) {
            if (isLocalization == true) {
                val resultIntent = Intent().apply {
                    putExtra(ChooseAddressConstant.EXTRA_SELECTED_ADDRESS_DATA, addressData)
                }
                activity?.let {
                    it.setResult(Activity.RESULT_OK, resultIntent)
                    it.finish()
                }
            } else if (viewModel.isNeedToShareAddress) {
                addressData?.apply {
                    ShareAddressAnalytics.onClickShareAddress()
                    showShareAddressConfirmationBottomSheet(
                        senderAddressId = id,
                        receiverUserId = viewModel.receiverUserId,
                        receiverUserName = viewModel.receiverUserName
                    )
                }
            } else {
                addressData?.let { viewModel.setStateChosenAddress(it) }
            }
        } else if (isFromCheckoutChangeAddress == true) {
            addressData?.let { viewModel.setStateChosenAddress(it) }
        }
    }

    private fun setChosenAddressFromDefaultAddress(data: SetDefaultPeopleAddressResponse) {
        val addressData = _selectedAddressItem
        if (isStayOnPageState == false) {
            if (isLocalization == true) {
                val resultIntent = Intent().apply {
                    putExtra(ChooseAddressConstant.EXTRA_SELECTED_ADDRESS_DATA, addressData)
                }
                activity?.let {
                    it.setResult(Activity.RESULT_OK, resultIntent)
                    it.finish()
                }
            } else if (viewModel.isNeedToShareAddress) {
                addressData?.apply {
                    ShareAddressAnalytics.onClickShareAddress()
                    showShareAddressConfirmationBottomSheet(
                        senderAddressId = id,
                        receiverUserId = viewModel.receiverUserId,
                        receiverUserName = viewModel.receiverUserName
                    )
                }
            } else {
                setChosenAddressFromChosenAddressResponse(data.data.isStateChosenAddressChanged, data.data.chosenAddressData, data.data.tokonow)
            }
        } else if (isFromCheckoutChangeAddress == true) {
            setChosenAddressFromChosenAddressResponse(data.data.isStateChosenAddressChanged, data.data.chosenAddressData, data.data.tokonow)
        }
    }

    private fun setChosenAddressFromChosenAddressResponse(
        isStateChosenAddressChanged: Boolean,
        chosenAddress: KeroAddrStateChosenAddressData,
        tokonowAddress: KeroAddressRespTokonow
    ) {
        if (isStateChosenAddressChanged) {
            context?.let { ctx ->
                ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                    context = ctx,
                    addressId = chosenAddress.addressId.toString(),
                    cityId = chosenAddress.cityId.toString(),
                    districtId = chosenAddress.districtId.toString(),
                    lat = chosenAddress.latitude,
                    long = chosenAddress.longitude,
                    label = "${chosenAddress.addressName} ${chosenAddress.receiverName}",
                    postalCode = chosenAddress.postalCode,
                    shopId = tokonowAddress.shopId.toString(),
                    warehouseId = tokonowAddress.warehouseId.toString(),
                    warehouses = tokonowAddress.warehouses.map { LocalWarehouseModel(warehouse_id = it.warehouseId, service_type = it.serviceType) },
                    serviceType = tokonowAddress.serviceType
                )
            }
        }
    }

    private fun setChosenAddressANA(addressDataModel: SaveAddressDataModel) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(
                it,
                addressDataModel.id.toString(),
                addressDataModel.cityId.toString(),
                addressDataModel.districtId.toString(),
                addressDataModel.latitude,
                addressDataModel.longitude,
                "${addressDataModel.addressName} ${addressDataModel.receiverName}",
                addressDataModel.postalCode,
                addressDataModel.shopId.toString(),
                addressDataModel.warehouseId.toString(),
                TokonowWarehouseMapper.mapWarehousesAddAddressModelToLocal(addressDataModel.warehouses),
                addressDataModel.serviceType
            )
        }

        if (isLocalization == true) {
            val resultIntent = Intent().apply {
                putExtra(ChooseAddressConstant.EXTRA_IS_FROM_ANA, true)
            }
            activity?.setResult(Activity.RESULT_OK, resultIntent)
            activity?.finish()
        } else if (isFromCheckoutChangeAddress == true) {
            val resultIntent = Intent().apply {
                putExtra(ChooseAddressConstant.EXTRA_IS_FROM_ANA, true)
                putExtra(
                    ChooseAddressConstant.EXTRA_SELECTED_ADDRESS_DATA,
                    addressDataModel.toChosenAddressModel()
                )
            }
            activity?.setResult(
                CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS,
                resultIntent
            )
            activity?.finish()
        } else {
            performSearch("", addressDataModel)
        }
    }

    private fun SaveAddressDataModel.toChosenAddressModel(): ChosenAddressModel {
        return ChosenAddressModel(
            addressId = this.id,
            receiverName = this.receiverName,
            addressName = this.addressName,
            latitude = this.latitude,
            longitude = this.longitude,
            postalCode = this.postalCode,
            districtId = this.districtId.toString().toIntSafely(),
            cityId = this.cityId.toString().toIntSafely()
        )
    }

    private fun getChosenAddrId(): Long {
        var chosenAddrId: Long = 0
        localChosenAddr?.address_id?.let { localAddrId ->
            if (localAddrId.isNotEmpty()) {
                localChosenAddr?.address_id?.toLong()?.let { id ->
                    chosenAddrId = id
                }
            }
        }
        return chosenAddrId
    }

    private fun setButtonEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            isStayOnPageState = false
            binding?.btnChooseAddress?.run {
                setEnabled(true)
                setOnClickListener { setChosenAddress() }
            }
        } else {
            binding?.btnChooseAddress?.isEnabled = false
        }
    }

    private fun getManageAddressFragment(): ManageAddressFragment? {
        val fragments = activity?.supportFragmentManager?.fragments
        fragments?.forEach { currentFragment ->
            if (currentFragment != null && currentFragment.isVisible && currentFragment is ManageAddressFragment) {
                return currentFragment
            }
        }
        return null
    }

    override fun onShareAddressClicked(peopleAddress: RecipientAddressModel) {
        ShareAddressAnalytics.onClickDirectShareButton()
        showShareAddressBottomSheet(peopleAddress.id)
    }

    private fun showShareAddressBottomSheet(senderAddressId: String) {
        val shareAddressListener = object : ShareAddressBottomSheet.ShareAddressListener {
            override fun onClickShareAddress(receiverPhoneNumberOrEmail: String) {
                bottomSheetShareAddress?.dismiss()
                showShareAddressConfirmationBottomSheet(
                    senderAddressId = senderAddressId,
                    receiverPhoneNumberOrEmail = receiverPhoneNumberOrEmail
                )
            }
        }

        bottomSheetShareAddress = ShareAddressBottomSheet.newInstance(
            isRequestAddress = false,
            shareAddressListener = shareAddressListener,
            source = viewModel.getSourceValue(),
            senderAddressId = senderAddressId
        )
        bottomSheetShareAddress?.show(
            parentFragmentManager,
            ShareAddressBottomSheet.TAG_SHARE_ADDRESS
        )
    }

    private fun showShareAddressConfirmationBottomSheet(
        senderAddressId: String,
        receiverPhoneNumberOrEmail: String? = null,
        receiverUserId: String? = null,
        receiverUserName: String? = null
    ) {
        bottomSheetConfirmationShareAddress = ShareAddressConfirmationBottomSheet.newInstance(
            senderAddressId = senderAddressId,
            receiverPhoneNumberOrEmail = receiverPhoneNumberOrEmail,
            receiverUserId = receiverUserId,
            receiverUserName = receiverUserName,
            source = viewModel.getSourceValue(),
            listener = this
        )
        bottomSheetConfirmationShareAddress?.show(
            parentFragmentManager,
            ShareAddressConfirmationBottomSheet.TAG_SHARE_ADDRESS_CONFIRMATION
        )
    }

    override fun showToast(isError: Boolean, msg: String) {
        val type = if (isError) Toaster.TYPE_ERROR else Toaster.TYPE_NORMAL
        showToaster(msg, type)
    }

    override fun leavePage() {
        leavePageJob?.cancel()
        leavePageJob = CoroutineScope(Dispatchers.Main).launch {
            delay(TOAST_SHOWING_TIME)
            gotoHome()
        }
    }

    private fun gotoHome() {
        activity?.let {
            val intentHome = RouteManager.getIntent(activity, ApplinkConst.HOME)
            intentHome.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            it.startActivity(intentHome)
            it.finish()
        }
    }

    private fun showToaster(message: String, toastType: Int = Toaster.TYPE_NORMAL) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, toastType).show()
        }
    }

    private fun generateUserConsentWidget(): UserConsentWidget? {
        return try {
            val userConsent = UserConsentWidget(requireContext())
            userConsent.load(
                ConsentCollectionParam(
                    collectionId = viewModel.deleteCollectionId
                )
            )
            userConsent
        } catch (e: Exception) {
            logToCrashlytics(e)
            null
        }
    }

    private fun logToCrashlytics(exception: Exception) {
        if (!GlobalConfig.DEBUG) {
            FirebaseCrashlytics.getInstance().recordException(exception)
        } else {
            exception.printStackTrace()
        }
    }

    private fun showDeleteAddressDialog(addressId: String) {
        context?.apply {
            val userConsent = generateUserConsentWidget()

            DialogUnify(this, DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE).apply {
                setTitle(getString(R.string.title_delete_address_dialog))
                setSecondaryCTAText(getString(R.string.action_cancel_delete_address))
                setPrimaryCTAText(getString(R.string.btn_delete))
                setSecondaryCTAClickListener {
                    dismiss()
                }
                setPrimaryCTAClickListener {
                    dismiss()
                    viewModel.deletePeopleAddress(
                        id = addressId,
                        consentJson = userConsent?.generatePayloadData().orEmpty()
                    )
                }
            }.show()
        }
    }

    fun setListener(listener: MainAddressListener) {
        this.mainAddressListener = listener
    }

    interface MainAddressListener {
        fun setAddButtonOnClickListener(onClick: () -> Unit)

        fun setupTicker(firstTicker: String? = null)
    }
}
