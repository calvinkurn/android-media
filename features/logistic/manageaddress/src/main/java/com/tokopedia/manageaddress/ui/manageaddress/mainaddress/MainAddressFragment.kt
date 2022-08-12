package com.tokopedia.manageaddress.ui.manageaddress.mainaddress

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.localizationchooseaddress.domain.mapper.TokonowWarehouseMapper
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.constant.AddressConstant.ANA_REVAMP_FEATURE_ID
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EDIT_ADDRESS_REVAMP_FEATURE_ID
import com.tokopedia.logisticCommon.data.constant.AddressConstant.EXTRA_EDIT_ADDRESS
import com.tokopedia.logisticCommon.data.constant.LogisticConstant.EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.ui.shareaddress.ShareAddressBottomSheet
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.data.analytics.ManageAddressAnalytics
import com.tokopedia.manageaddress.databinding.BottomsheetActionAddressBinding
import com.tokopedia.manageaddress.databinding.FragmentMainAddressBinding
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressFragment
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressItemAdapter
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressViewModel
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
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

/**
 * MainAddressFragment
 * fragment inside viewPager of ManageAddressFragment
 */
class MainAddressFragment : BaseDaggerFragment(), ManageAddressItemAdapter.MainAddressItemAdapterListener,
    ShareAddressConfirmationBottomSheet.Listener{

    companion object {

        private const val EMPTY_STATE_PICT_URL = "https://ecs7.tokopedia.net/android/others/pilih_alamat_pengiriman3x.png"
        private const val EMPTY_SEARCH_PICT_URL = "https://ecs7.tokopedia.net/android/others/address_not_found3x.png"
        private const val IS_SUCCESS = "success"
        private const val IS_NOT_SUCCESS = "not success"

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
    private var editedChosenAddress: RecipientAddressModel? = null

    private var maxItemPosition: Int = -1
    private var isLoading: Boolean = false
    private var isFromCheckoutChangeAddress: Boolean? = false
    private var isFromCheckoutSnippet: Boolean? = false
    private var isLocalization: Boolean? = false
    private var typeRequest: Int? = -1
    private var prevState: Int = -1
    private var localChosenAddr: LocalCacheModel? = null
    private var isFromEditAddress: Boolean? = false
    var isFromEditChosenAddress: Boolean? = null
    private var isFromDeleteAddress: Boolean? = false
    private var isStayOnPageState: Boolean? = false
    private var source: String = ""

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ManageAddressComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentMainAddressBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initArguments()
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
        isFromCheckoutChangeAddress = arguments?.getBoolean(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS)
        isFromCheckoutSnippet = arguments?.getBoolean(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_SNIPPET)
        isLocalization = arguments?.getBoolean(ManageAddressConstant.EXTRA_IS_LOCALIZATION)
        typeRequest = arguments?.getInt(CheckoutConstant.EXTRA_TYPE_REQUEST)
        prevState = arguments?.getInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS) ?: -1
        localChosenAddr = context?.let { ChooseAddressUtils.getLocalizingAddressData(it) }
        viewModel.savedQuery = arguments?.getString(ManageAddressConstant.EXTRA_QUERY) ?: ""
        viewModel.receiverUserId = arguments?.getString(ManageAddressConstant.QUERY_RECEIVER_USER_ID)
        viewModel.senderUserId = arguments?.getString(ManageAddressConstant.QUERY_SENDER_USER_ID)
        source = arguments?.getString(ApplinkConstInternalLogistic.PARAM_SOURCE) ?: ""
    }

    private fun initAdapter() {
        adapter.apply {
            setMainAddressListener(viewModel.isNeedToShareAddress, this@MainAddressFragment)
        }

        binding?.addressList?.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
    }

    private fun initView() {
        setButtonEnabled(false)
        updateButton(getString(R.string.btn_share_adddress))

        binding?.apply {
            emptyStateManageAddress?.ivEmptyState?.loadImage(EMPTY_STATE_PICT_URL)
            ivEmptyAddress?.loadImage(EMPTY_SEARCH_PICT_URL)
        }
        initScrollListener()
    }

    private fun observerSetChosenAddress() {
        viewModel.setChosenAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (binding?.btnChooseAddress?.text == getString((R.string.pilih_alamat))) ChooseAddressTracking.onClickButtonPilihAlamat(
                        userSession.userId,
                        IS_SUCCESS
                    )
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
                            putExtra(CheckoutConstant.EXTRA_SELECTED_ADDRESS_DATA, data)
                        }
                        activity?.setResult(CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_CHANGE_ADDRESS, resultIntent)
                    } else if (isFromCheckoutSnippet == true) {
                        activity?.setResult(CheckoutConstant.RESULT_CODE_ACTION_CHECKOUT_SELECT_ADDRESS_FOR_SNIPPET)
                    }
                    activity?.finish()
                }

                is Fail -> {
                    if (binding?.btnChooseAddress?.text == getString(R.string.pilih_alamat)) ChooseAddressTracking.onClickButtonPilihAlamat(
                        userSession.userId,
                        IS_NOT_SUCCESS
                    )
                    view?.let { view ->
                        Toaster.build(
                            view, it.throwable.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
    }

    private fun observerGetChosenAddress() {
        viewModel.getChosenAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val data = it.data
                    context?.let { context ->
                        if (isFromEditAddress == true) {
                            val newRecipientAddressModel = RecipientAddressModel()
                            newRecipientAddressModel.apply {
                                id = data.addressId.toString()
                                addressStatus = data.status
                                recipientName = data.receiverName
                                addressName = data.addressName
                                latitude = data.latitude
                                longitude = data.longitude
                                destinationDistrictId = data.districtId.toString()
                                postalCode = data.postalCode
                            }
                            _selectedAddressItem = newRecipientAddressModel
                            if (isFromEditChosenAddress == true) {
                                editedChosenAddress = newRecipientAddressModel
                            }
                        }
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

                        if (isFromDeleteAddress == true) {
                            context?.let {
                                viewModel.searchAddress("", prevState, data.addressId, true)
                            }
                        }
                    }
                }

                is Fail -> {
                    view?.let { view ->
                        Toaster.build(
                            view, it.throwable.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
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
                    if (it.data.listAddress.isNotEmpty()) {
                        updateTicker(it.data.pageInfo?.ticker)
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

    private fun observerSetDefault() {
        viewModel.setDefault.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManageAddressState.Success ->
                    if (isLocalization == true || isFromCheckoutChangeAddress == true || isFromCheckoutSnippet == true) {
                        bottomSheetLainnya?.dismiss()
                        setChosenAddress()
                    } else {
                        bottomSheetLainnya?.dismiss()
                        viewModel.getStateChosenAddress("address")
                    }

                is ManageAddressState.Fail -> {
                    view?.let { view ->
                        Toaster.build(
                            view, it.throwable?.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }

                else -> {
                    //no-op
                }
            }
        })
    }

    private fun observerRemovedAddress() {
        viewModel.resultRemovedAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManageAddressState.Success ->
                    Toaster.build(requireView(), getString(R.string.toaster_remove_address_success), Toaster.TYPE_NORMAL).show()
                else -> {
                    //no-op
                }
            }
        })
    }

    private fun observerEligibleForAddressFeature() {
        viewModel.eligibleForAddressFeature.observe(viewLifecycleOwner, Observer {
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
                    view?.let { view ->
                        Toaster.build(
                            view, it.throwable.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PARAM_CREATE) {
            val addressDataModel = data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
            if (addressDataModel != null) {
                setChosenAddressANA(addressDataModel)
                view?.let { Toaster.build(it, getString(R.string.add_address_success), Toaster.LENGTH_SHORT, type = Toaster.TYPE_NORMAL).show() }
            } else {
                performSearch(viewModel.savedQuery, null)
            }
        } else if (requestCode == REQUEST_CODE_PARAM_EDIT) {
            isFromEditAddress = true
            isFromEditChosenAddress = data?.getBooleanExtra(EXTRA_IS_STATE_CHOSEN_ADDRESS_CHANGED, false)

            performSearch(viewModel.savedQuery, null)
            viewModel.getStateChosenAddress("address")
            setButtonEnabled(true)
            val addressData = data?.getStringExtra(EXTRA_EDIT_ADDRESS)
            if (addressData != null) {
                view?.let {
                    Toaster.build(it, getString(R.string.edit_address_success), Toaster.LENGTH_SHORT, type = Toaster.TYPE_NORMAL)
                        .show()
                }
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
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, screenName)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        } else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, screenName)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        }
    }

    private fun goToEditAddress(eligibleForEditRevamp: Boolean, data: RecipientAddressModel) {
        if (eligibleForEditRevamp) {
            val intent = RouteManager.getIntent(context, "${ApplinkConstInternalLogistic.EDIT_ADDRESS_REVAMP}${data.id}")
            intent.putExtra(ApplinkConstInternalLogistic.PARAM_SOURCE, source)
            startActivityForResult(intent, REQUEST_CODE_PARAM_EDIT)
        } else {
            val token = viewModel.token
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V1)
            val mapper = AddressModelMapper()
            intent.putExtra(EDIT_PARAM, mapper.transform(data))
            intent.putExtra(KERO_TOKEN, token)
            startActivityForResult(intent, REQUEST_CODE_PARAM_EDIT)
        }
    }

    private fun initScrollListener() {
        binding?.addressList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adapter = recyclerView.adapter
                val totalItemCount = adapter?.itemCount
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
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
        })
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

    private fun updateTicker(ticker: String?) {
        ticker?.let {
            binding?.tickerInfo?.run {
                if (it.isEmpty()) {
                    gone()
                } else {
                    visible()
                    setHtmlDescription(ticker)
                }
            }
        }
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
        val viewBottomSheetLainnya = BottomsheetActionAddressBinding.inflate(LayoutInflater.from(context), null, false).apply {
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
                viewModel.setDefaultPeopleAddress(data.id, false, prevState, data.id.toLong(), true)
                bottomSheetLainnya?.dismiss()
            }
            btnHapusAlamat.setOnClickListener {
                viewModel.deletePeopleAddress(data.id)
                bottomSheetLainnya?.dismiss()
                isFromDeleteAddress = true
            }
            btnAlamatUtamaChoose.setOnClickListener {
                isStayOnPageState = false
                context?.let {
                    viewModel.setDefaultPeopleAddress(data.id, true, prevState, data.id.toLong(), true)
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

        fragmentManager?.let {
            bottomSheetLainnya?.show(it, "show")
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
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(
                        it, throwable.message
                            ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR
                    ).show()
                }
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
        setButtonEnabled(true)
        _selectedAddressItem = peopleAddress
        if (isLocalization == true) ChooseAddressTracking.onClickAvailableAddressAddressList(userSession.userId)
    }

    private fun setChosenAddress(isClickBackButton: Boolean = false) {
        val addressData = if (isClickBackButton) editedChosenAddress else _selectedAddressItem
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
                    showShareAddressBottomSheet(id)
                }
            } else {
                addressData?.let { viewModel.setStateChosenAddress(it) }
            }
        } else if (isFromCheckoutChangeAddress == true) {
            addressData?.let { viewModel.setStateChosenAddress(it) }
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
        } else {
            performSearch("", addressDataModel)
        }
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

    fun setAddressDataOnBackButton() {
        setChosenAddress(true)
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
        showShareAddressBottomSheet(peopleAddress.id)
    }

    private fun showShareAddressBottomSheet(senderAddressId: String) {
        val shareAddressListener = object : ShareAddressBottomSheet.ShareAddressListener {
            override fun onClickShareAddress(receiverPhoneNumberOrEmail: String) {
                bottomSheetShareAddress?.dismiss()
                showShareAddressConfirmationBottomSheet(receiverPhoneNumberOrEmail, senderAddressId)
            }
        }

        bottomSheetShareAddress = ShareAddressBottomSheet.newInstance(
            isRequestAddress = false,
            shareAddressListener = shareAddressListener
        )
        bottomSheetShareAddress?.show(
            parentFragmentManager,
            ShareAddressBottomSheet.TAG_SHARE_ADDRESS
        )
    }

    private fun showShareAddressConfirmationBottomSheet(
        senderAddressId: String,
        receiverPhoneNumberOrEmail: String
    ) {
        bottomSheetConfirmationShareAddress = ShareAddressConfirmationBottomSheet.newInstance(
            senderAddressId = senderAddressId,
            receiverPhoneNumberOrEmail = receiverPhoneNumberOrEmail,
            listener = this
        )
        bottomSheetConfirmationShareAddress?.show(
            parentFragmentManager,
            ShareAddressConfirmationBottomSheet.TAG_SHARE_ADDRESS_CONFIRMATION
        )
    }

    override fun onSuccessShareAddress() {
        bottomSheetConfirmationShareAddress?.dismiss()
        showToaster(getString(R.string.success_share_address))
    }

    override fun onFailedShareAddress(errorMessage: String) {
        bottomSheetConfirmationShareAddress?.dismiss()
        showToaster(errorMessage, Toaster.TYPE_ERROR)
    }

    private fun showToaster(message: String, toastType: Int = Toaster.TYPE_NORMAL) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, toastType).show()
        }
    }
}