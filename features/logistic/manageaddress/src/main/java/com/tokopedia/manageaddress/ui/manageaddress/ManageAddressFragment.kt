package com.tokopedia.manageaddress.ui.manageaddress

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.ui.preference.ChooseAddressSharePref
import com.tokopedia.localizationchooseaddress.util.ChooseAddressConstant
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.EDIT_PARAM
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_REF
import com.tokopedia.manageaddress.util.ManageAddressConstant.KERO_TOKEN
import com.tokopedia.manageaddress.util.ManageAddressConstant.LABEL_LAINNYA
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_CREATE
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_EDIT
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_USER_NEW
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.bottomsheet_action_address.view.*
import kotlinx.android.synthetic.main.empty_manage_address.*
import kotlinx.android.synthetic.main.fragment_manage_address.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ManageAddressFragment : BaseDaggerFragment(), SearchInputView.Listener, ManageAddressItemAdapter.ManageAddressItemAdapterListener {

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = ManageAddressItemAdapter(this)

    private val viewModel: ManageAddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ManageAddressViewModel::class.java]
    }

    private var searchAddress: SearchBarUnify? = null
    private var addressList: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var bottomSheetLainnya: BottomSheetUnify? = null
    private var emptySearchLayout: LinearLayout? = null

    private var buttonAddEmpty: UnifyButton? = null
    private var emptyStateLayout: LinearLayout? = null

    private var globalErrorLayout: GlobalError? = null

    private var manageAddressListener: ManageAddressListener? = null

    private var llButtonChooseAddress: LinearLayout? = null
    private var buttonChooseAddress: UnifyButton? = null
    private var chooseAddressPref: ChooseAddressSharePref? = null
    private var _selectedAddressItem: RecipientAddressModel? = null
    private var tickerInfo: Ticker? = null

    private var maxItemPosition: Int = -1
    private var isLoading: Boolean = false
    private var isFromCheckoutChangeAddress: Boolean? = false
    private var isFromCheckoutSnippet: Boolean? = false
    private var isLocalization: Boolean? = false
    private var typeRequest: Int? = -1
    private var prevState: Int = -1
    private var localChosenAddr: LocalCacheModel? = null
    private var isFromEditAddress: Boolean? = false
    private var isFromDeleteAddress: Boolean? = false
    private var isStayOnPageState: Boolean? = false

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ManageAddressComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_manage_address, container, false)
        searchAddress = view.findViewById(R.id.search_input_view)
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initView()
        initViewModel()
        address_list.adapter = adapter
        address_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        isFromCheckoutChangeAddress = arguments?.getBoolean(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS)
        isFromCheckoutSnippet = arguments?.getBoolean(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_SNIPPET)
        isLocalization = arguments?.getBoolean(ManageAddressConstant.EXTRA_IS_LOCALIZATION)
        typeRequest = arguments?.getInt(CheckoutConstant.EXTRA_TYPE_REQUEST)
        prevState = arguments?.getInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS) ?: -1
        localChosenAddr = context?.let { ChooseAddressUtils.getLocalizingAddressData(it) }
        initSearch()
        initSearchView()
    }

    override fun onSearchSubmitted(text: String) {
        performSearch(text, null)
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PARAM_CREATE) {
            val addressDataModel = data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
            if (addressDataModel != null) {
                setChosenAddressANA(addressDataModel)
            }
        } else if (requestCode == REQUEST_CODE_PARAM_EDIT) {
            isFromEditAddress = true
            performSearch(searchAddress?.searchBarTextField?.text?.toString() ?: "", null)
            viewModel.getStateChosenAddress("address")
            setButtonEnabled(true)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomSheetLainnya = null
    }

    private fun openSoftKeyboard() {
        searchAddress?.searchBarTextField?.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun performSearch(query: String, saveAddressDataModel: SaveAddressDataModel?) {
        clearData()
        maxItemPosition = 0
        val addrId = saveAddressDataModel?.id ?: getChosenAddrId()
        context?.let {
            viewModel.searchAddress(query, prevState, addrId, ChooseAddressUtils.isRollOutUser(it))
        }
    }

    private fun initHeader() {
        manageAddressListener?.setAddButtonOnClickListener {
            if (isLocalization == true) ChooseAddressTracking.onClickButtonTambahAlamat(userSession.userId)
            openFormAddressView(null)
        }
    }

    private fun initView() {
        addressList = view?.findViewById(R.id.address_list)
        searchAddress = view?.findViewById(R.id.search_input_view)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
        emptySearchLayout = view?.findViewById(R.id.empty_search)
        emptyStateLayout = view?.findViewById(R.id.empty_state_manage_address)
        globalErrorLayout = view?.findViewById(R.id.global_error)
        buttonAddEmpty = view?.findViewById(R.id.btn_add_empty)
        tickerInfo = view?.findViewById(R.id.ticker_info)

        chooseAddressPref = ChooseAddressSharePref(context)

        llButtonChooseAddress = view?.findViewById(R.id.ll_btn)
        buttonChooseAddress = view?.findViewById(R.id.btn_choose_address)
        setButtonEnabled(false)

        ImageHandler.LoadImage(iv_empty_state, EMPTY_STATE_PICT_URL)
        ImageHandler.LoadImage(iv_empty_address, EMPTY_SEARCH_PICT_URL)

        initScrollListener()

    }

    private fun initViewModel() {
        viewModel.addressList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManageAddressState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    if (viewModel.isClearData) clearData()
                    if (it.data.listAddress.isNotEmpty()) {
                        if (context?.let { context -> ChooseAddressUtils.isRollOutUser(context) } == true) {
                            updateTicker(it.data.pageInfo?.ticker)
                            updateButton(it.data.pageInfo?.buttonLabel)
                            updateStateForCheckoutSnippet(it.data.listAddress)
                        }
                    }
                    updateData(it.data.listAddress)
                    setEmptyState()
                    isLoading = false

                }

                is ManageAddressState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                    isLoading = false
                }

                else -> {
                    swipeRefreshLayout?.isRefreshing = true
                    isLoading = true
                }
            }
        })

        viewModel.setDefault.observe(viewLifecycleOwner, Observer {
            when(it) {
                is ManageAddressState.Success ->
                    if (isLocalization == true || isFromCheckoutChangeAddress ==  true || isFromCheckoutSnippet == true) {
                        bottomSheetLainnya?.dismiss()
                        setChosenAddress()
                    } else {
                        bottomSheetLainnya?.dismiss()
                        viewModel.getStateChosenAddress("address")
                    }

                is ManageAddressState.Fail -> {
                    view?.let { view ->
                        Toaster.build(view, it.throwable?.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    }
                }

                else -> {
                    //no-op
                }
            }
        })

        viewModel.getChosenAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    val data = it.data
                    context?.let {
                        context ->
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
                        }
                        ChooseAddressUtils.updateLocalizingAddressDataFromOther(context, data.addressId.toString(), data.cityId.toString(),
                                data.districtId.toString(), data.latitude, data.longitude, ChooseAddressUtils.setLabel(data), data.postalCode)

                        if (isFromDeleteAddress == true) {
                            context?.let {
                                viewModel.searchAddress("", prevState, data.addressId, ChooseAddressUtils.isRollOutUser(it))
                            }
                        }
                    }
                }

                is Fail -> {
                    view?.let { view ->
                        Toaster.build(view, it.throwable.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    }
                }
            }
        })

        viewModel.setChosenAddress.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    if (buttonChooseAddress?.text == getString(R.string.pilih_alamat)) ChooseAddressTracking.onClickButtonPilihAlamat(userSession.userId, IS_SUCCESS)
                    val data = it.data
                    context?.let {
                        context ->
                        ChooseAddressUtils.updateLocalizingAddressDataFromOther(context, data.addressId.toString(), data.cityId.toString(),
                                data.districtId.toString(), data.latitude, data.longitude, ChooseAddressUtils.setLabel(data), data.postalCode)
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
                    if (buttonChooseAddress?.text == getString(R.string.pilih_alamat)) ChooseAddressTracking.onClickButtonPilihAlamat(userSession.userId, IS_NOT_SUCCESS)
                    view?.let { view ->
                        Toaster.build(view, it.throwable.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                    }
                }
            }
        })
    }

    private fun initScrollListener() {
        addressList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
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
                        viewModel.loadMore(prevState, getChosenAddrId(), ChooseAddressUtils.isRollOutUser(it))
                    }
                }
            }
        })
    }

    private fun setEmptyState() {
        if (adapter.addressList.isNotEmpty()) {
            emptyStateLayout?.gone()
            searchAddress?.visible()
            addressList?.visible()
            emptySearchLayout?.gone()
        } else if (viewModel.savedQuery.isEmpty()) {
            buttonAddEmpty?.setOnClickListener {
                openFormAddressView(null)
            }
            emptyStateLayout?.visible()
            searchAddress?.gone()
            addressList?.gone()
            emptySearchLayout?.gone()
        } else {
            emptySearchLayout?.visible()
            emptyStateLayout?.gone()
            searchAddress?.visible()
            addressList?.gone()
        }
    }

    private fun initSearch() {
        val searchKey = viewModel.savedQuery
        searchAddress?.searchBarTextField?.setText(searchKey)
        performSearch(searchKey, null)
        if (isLocalization == true) ChooseAddressTracking.impressAddressListPage(userSession.userId)
    }

    private fun initSearchView() {
        searchAddress?.searchBarTextField?.setOnClickListener {
            searchAddress?.searchBarTextField?.isCursorVisible = true
            openSoftKeyboard()
        }

        searchAddress?.searchBarTextField?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAddress?.clearFocus()
                performSearch(searchAddress?.searchBarTextField?.text?.toString() ?: "", null)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        searchAddress?.clearListener = {
            performSearch("", null)
        }

        searchAddress?.searchBarPlaceholder = "Cari Alamat"
    }

    private fun updateData(data: List<RecipientAddressModel>) {
        adapter.addList(data)
    }

    private fun updateTicker(ticker: String?) {
        ticker?.let {
            if (ticker.isEmpty()) {
                tickerInfo?.gone()
            } else {
                tickerInfo?.visible()
                tickerInfo?.setHtmlDescription(ticker)
            }
        }
    }

    private fun updateButton(btnLabel: String?) {
        btnLabel?.let {
            llButtonChooseAddress?.visible()
            if (btnLabel.isEmpty()) {
                buttonChooseAddress?.text = getString(R.string.pilih_alamat)
            } else {
                buttonChooseAddress?.text = btnLabel
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
        val token = viewModel.token
        if (data == null) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, SCREEN_NAME_USER_NEW)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        } else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            val mapper = AddressModelMapper()
            intent.putExtra(EDIT_PARAM, mapper.transform(data))
            intent.putExtra(KERO_TOKEN, token)
            startActivityForResult(intent, REQUEST_CODE_PARAM_EDIT)
        }
    }

    private fun openBottomSheetView(data: RecipientAddressModel) {
        bottomSheetLainnya = BottomSheetUnify()
        val viewBottomSheetLainnya = View.inflate(context, R.layout.bottomsheet_action_address, null).apply {
            if (data.addressStatus == 2) {
                btn_alamat_utama?.gone()
                divider?.gone()
                btn_alamat_utama_choose?.gone()
                divider_utama_choose?.gone()
            } else {
                if (!data.isStateChosenAddress) {
                    btn_alamat_utama?.gone()
                    divider?.gone()
                    btn_alamat_utama_choose?.visible()
                    divider_utama_choose?.visible()
                } else {
                    btn_alamat_utama?.visible()
                    divider?.visible()
                    btn_alamat_utama_choose?.gone()
                    divider_utama_choose?.gone()
                }
            }
            btn_alamat_utama?.setOnClickListener {
                if (isFromCheckoutChangeAddress == true || isLocalization == true) {
                    _selectedAddressItem = data
                }
                isStayOnPageState = true
                viewModel.setDefaultPeopleAddress(data.id, false, prevState, data.id.toInt(), ChooseAddressUtils.isRollOutUser(context))
                bottomSheetLainnya?.dismiss()
            }
            btn_hapus_alamat?.setOnClickListener {
                viewModel.deletePeopleAddress(data.id, prevState, getChosenAddrId(), ChooseAddressUtils.isRollOutUser(context))
                bottomSheetLainnya?.dismiss()
                isFromDeleteAddress = true
            }
            btn_alamat_utama_choose?.setOnClickListener {
                isStayOnPageState = false
                context?.let {
                    viewModel.setDefaultPeopleAddress(data.id,true, prevState, data.id.toInt(), ChooseAddressUtils.isRollOutUser(it))
                }
                _selectedAddressItem = data
            }
        }

        bottomSheetLainnya?.apply {
            setTitle(LABEL_LAINNYA)
            setCloseClickListener { dismiss() }
            setChild(viewBottomSheetLainnya)
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
                    Toaster.build(it, throwable.message
                            ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            context?.let {
                viewModel.searchAddress("", prevState, getChosenAddrId(), ChooseAddressUtils.isRollOutUser(it))
            }
        }
        searchAddress?.gone()
        addressList?.gone()
        emptyStateLayout?.gone()
        globalErrorLayout?.visible()
        emptySearchLayout?.gone()
    }

    fun setListener(listener: ManageAddressListener) {
        this.manageAddressListener = listener
    }

    interface ManageAddressListener {
        fun setAddButtonOnClickListener(onClick: () -> Unit)
    }

    companion object {

        private const val EMPTY_STATE_PICT_URL = "https://ecs7.tokopedia.net/android/others/pilih_alamat_pengiriman3x.png"
        private const val EMPTY_SEARCH_PICT_URL = "https://ecs7.tokopedia.net/android/others/address_not_found3x.png"
        private const val IS_SUCCESS = "success"
        private const val IS_NOT_SUCCESS = "not success"

        fun newInstance(bundle: Bundle): ManageAddressFragment {
            return ManageAddressFragment().apply {
                arguments = bundle
            }
        }
    }

    override fun onAddressItemSelected(peopleAddress: RecipientAddressModel) {
        setButtonEnabled(true)
        _selectedAddressItem = peopleAddress
        if (isLocalization == true) ChooseAddressTracking.onClickAvailableAddressAddressList(userSession.userId)
    }

    private fun setChosenAddress() {
        if (isStayOnPageState == false) {
            if (isLocalization == true) {
                val resultIntent = Intent().apply {
                    putExtra(ChooseAddressConstant.EXTRA_SELECTED_ADDRESS_DATA, _selectedAddressItem)
                }
                activity?.setResult(Activity.RESULT_OK, resultIntent)
                activity?.finish()
            } else {
                _selectedAddressItem?.let { viewModel.setStateChosenAddress(it) }
            }
        } else if (isFromCheckoutChangeAddress == true) {
            _selectedAddressItem?.let { viewModel.setStateChosenAddress(it) }
        }
    }

    private fun setChosenAddressANA(addressDataModel: SaveAddressDataModel) {
        context?.let {
            ChooseAddressUtils.updateLocalizingAddressDataFromOther(it,
                    addressDataModel.id.toString(), addressDataModel.cityId.toString(), addressDataModel.districtId.toString(),
                    addressDataModel.latitude, addressDataModel.longitude, "${addressDataModel.addressName} ${addressDataModel.receiverName}", addressDataModel.postalCode)
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

    private fun getChosenAddrId(): Int {
        var chosenAddrId = 0
        localChosenAddr?.address_id?.let { localAddrId ->
            if (localAddrId.isNotEmpty()) {
                localChosenAddr?.address_id?.toInt()?.let { id ->
                    chosenAddrId = id
                }
            }
        }
        return chosenAddrId
    }


    private fun setButtonEnabled(isEnabled: Boolean) {
        if (isEnabled) {
            isStayOnPageState = false
            buttonChooseAddress?.apply {
                setEnabled(true)
                setOnClickListener { setChosenAddress() }
            }
        } else {
            buttonChooseAddress?.isEnabled = false
        }
    }

}