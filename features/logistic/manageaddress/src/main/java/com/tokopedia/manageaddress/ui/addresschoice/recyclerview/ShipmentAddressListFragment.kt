package com.tokopedia.manageaddress.ui.addresschoice.recyclerview

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnTouchListener
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.common.utils.network.ErrorHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.KeyboardHandler
import com.tokopedia.analytics.performance.PerformanceMonitoring
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.toLongOrZero
import com.tokopedia.localizationchooseaddress.domain.model.LocalCacheModel
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils.getLocalizingAddressData
import com.tokopedia.logisticCommon.data.constant.LogisticConstant
import com.tokopedia.logisticCommon.data.entity.address.Destination
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.data.entity.address.Token
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.FragmentShipmentAddressListBinding
import com.tokopedia.manageaddress.di.DaggerAddressChoiceComponent
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper
import com.tokopedia.manageaddress.ui.addresschoice.AddressListContract
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsChangeAddress
import com.tokopedia.purchase_platform.common.analytics.CheckoutAnalyticsMultipleAddress
import com.tokopedia.purchase_platform.common.analytics.ConstantTransactionAnalytics
import com.tokopedia.purchase_platform.common.base.BaseCheckoutFragment
import com.tokopedia.purchase_platform.common.constant.CartConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.EXTRA_REF
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.ArrayList
import javax.inject.Inject

class ShipmentAddressListFragment : BaseCheckoutFragment(), AddressListContract.View,
        SearchInputView.Listener, SearchInputView.ResetListener,
        ShipmentAddressListAdapter.ActionListener {
    @Inject
    lateinit var mPresenter: AddressListContract.Presenter

    @Inject
    lateinit var checkoutAnalyticsChangeAddress: CheckoutAnalyticsChangeAddress

    @Inject
    lateinit var checkoutAnalyticsMultipleAddress: CheckoutAnalyticsMultipleAddress

    private var binding by autoClearedNullable<FragmentShipmentAddressListBinding>()

    private val mAdapter: ShipmentAddressListAdapter = ShipmentAddressListAdapter(this)

    private var mActivityListener: ICartAddressChoiceActivityListener? = null
    private var maxItemPosition = 0
    private var isLoading = false
    private var requestType = 0
    private var mCurrentAddress: RecipientAddressModel? = null
    private var chooseAddressTracePerformance: PerformanceMonitoring? = null
    private var isChooseAddressTraceStopped = false
    private var token: Token? = null
    private var originDirectionType = 0
    private var prevState = 0
    private var localChosenAddr: LocalCacheModel? = null
    private var mInputMethodManager: InputMethodManager? = null

    override fun showList(list: MutableList<RecipientAddressModel>) {
        maxItemPosition = 0
        val selectedId = mCurrentAddress?.getId() ?: ""
        mAdapter.setAddressList(list, selectedId)
        binding?.rvAddressList?.visibility = View.VISIBLE
        binding?.llNetworkErrorView?.visibility = View.GONE
        binding?.llNoResult?.visibility = View.GONE
    }

    override fun updateList(list: MutableList<RecipientAddressModel>) {
        mAdapter.updateAddressList(list)
        binding?.rvAddressList?.visibility = View.VISIBLE
    }

    override fun showListEmpty() {
        mAdapter.setAddressList(ArrayList(), null)
        binding?.rvAddressList?.visibility = View.GONE
        binding?.llNetworkErrorView?.visibility = View.GONE
        binding?.llNoResult?.visibility = View.VISIBLE
    }

    override fun showError(e: Throwable) {
        binding?.rlContent?.visibility = View.GONE
        binding?.llNetworkErrorView?.visibility = View.VISIBLE
        binding?.llNoResult?.visibility = View.GONE
        val message = ErrorHandler.getErrorMessage(context, e)
        NetworkErrorHelper.showEmptyState(activity, binding?.llNetworkErrorView, message
        ) {
            val keyword: String = binding?.svAddressSearchBox?.searchText ?: ""
            performSearch(keyword, true)
        }
    }

    override fun showLoading() {
        isLoading = true
        binding?.swipeRefreshLayout?.isRefreshing = true
    }

    override fun hideLoading() {
        isLoading = false
        binding?.swipeRefreshLayout?.isRefreshing = false
    }

    override fun resetPagination() {
        maxItemPosition = 0
    }

    override fun setToken(token: Token?) {
        this.token = token
    }

    override fun navigateToCheckoutPage(recipientAddressModel: RecipientAddressModel) {
        onAddressContainerClicked(recipientAddressModel, -1)
    }

    override fun stopTrace() {
        if (!isChooseAddressTraceStopped) {
            chooseAddressTracePerformance?.stopTrace()
            isChooseAddressTraceStopped = true
        }
    }

    override fun onAddressContainerClicked(model: RecipientAddressModel?, position: Int) {
        mAdapter.updateSelected(position)
        if (mActivityListener != null && activity != null) {
            KeyboardHandler.hideSoftKeyboard(activity)
            sendAnalyticsOnAddressSelectionClicked()
            mActivityListener?.finishAndSendResult(model)
        }
    }

    private fun sendAnalyticsOnAddressSelectionClicked() {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickChecklistAlamatFromPilihAlamatLainnya()
        checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickRadioButtonFromPilihAlamatLainnya()
    }

    override fun onEditClick(model: RecipientAddressModel?) {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickUbahFromPilihAlamatLainnya()
        activity?.let {
            val mapper = AddressModelMapper()
            val intent = if (originDirectionType == ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM) {
                RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V1,
                        LogisticConstant.INSTANCE_TYPE_EDIT_ADDRESS_FROM_MULTIPLE_CHECKOUT)
            } else {
                RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V1,
                        LogisticConstant.INSTANCE_TYPE_EDIT_ADDRESS_FROM_SINGLE_CHECKOUT)
            }
            intent.apply {
                putExtra(PARAM_ADDRESS_MODEL, mapper.transform(model))
                putExtra(PARAM_TOKEN, token)
            }
            startActivityForResult(intent, LogisticConstant.REQUEST_CODE_PARAM_EDIT)
        }
    }

    override fun onAddAddressButtonClicked() {
        activity?.let {
            if (originDirectionType == ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM) {
                checkoutAnalyticsMultipleAddress.eventClickAddressCartMultipleAddressClickPlusFromMultiple()
            } else {
                checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressClickTambahAlamatBaruFromGantiAlamat()
                checkoutAnalyticsChangeAddress.eventClickShippingCartChangeAddressClickTambahFromAlamatPengiriman()
            }
            val intent = RouteManager.getIntent(it, ApplinkConstInternalLogistic.ADD_ADDRESS_V2).apply {
                putExtra(PARAM_TOKEN, token)
                putExtra(EXTRA_REF, CartConstant.SCREEN_NAME_CART_EXISTING_USER)
            }
            startActivityForResult(intent, LogisticConstant.ADD_NEW_ADDRESS_CREATED)
        }
    }

    override fun initInjector() {
        DaggerAddressChoiceComponent.builder()
                .baseAppComponent((activity?.applicationContext as BaseMainApplication).baseAppComponent)
                .build()
                .inject(this)

    }

    override fun initView(view: View) {
        checkoutAnalyticsChangeAddress.eventViewAtcCartChangeAddressImpressionChangeAddress()
        binding?.btChangeSearch?.setOnClickListener {
            binding?.svAddressSearchBox?.searchTextView?.setText("")
            binding?.svAddressSearchBox?.searchTextView?.requestFocus()
        }
        binding?.swipeRefreshLayout?.setOnRefreshListener {
            isLoading = true
            binding?.svAddressSearchBox?.searchTextView?.setText("")
            maxItemPosition = 0
            onSearchReset()
        }
        binding?.rvAddressList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adapter = recyclerView.adapter
                val totalItemCount = adapter?.itemCount
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
                if (maxItemPosition < lastVisibleItemPosition) {
                    maxItemPosition = lastVisibleItemPosition
                }

                if ((maxItemPosition + 1) == totalItemCount && !isLoading && dy > 0 && context != null) {
                    mPresenter.loadMore(prevState, getChosenAddrId(), true)
                }
            }
        })

        mPresenter.attachView(this)
    }

    private fun getChosenAddrId(): Long {
        return localChosenAddr?.address_id?.toLongOrZero() ?: 0
    }

    override fun onSearchSubmitted(text: String?) {
        performSearch(text ?: "", true)
    }

    private fun performSearch(query: String, resetPage: Boolean) {
        checkoutAnalyticsChangeAddress.eventClickAtcCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya()
        checkoutAnalyticsChangeAddress.eventClickAddressCartChangeAddressCartChangeAddressSubmitSearchFromPilihAlamatLainnya()
        context?.let {
            if (query.isNotEmpty()) {
                mPresenter.searchAddress(query, prevState, getChosenAddrId(), true)
            } else {
                mPresenter.getAddress(prevState, getChosenAddrId(),true)
            }
        }
    }

    override fun onSearchTextChanged(text: String?) {
      openSoftKeyboard()
    }

    override fun onSearchReset() {
       context?.let {
           mPresenter.getAddress(prevState, getChosenAddrId(), true)
       }
    }

    override fun getScreenName(): String {
        return ConstantTransactionAnalytics.ScreenName.ADDRESS_LIST_PAGE
    }

    override fun getOptionsMenuEnable(): Boolean {
        return true
    }

    override fun getFragmentLayout(): Int {
        return R.layout.fragment_shipment_address_list
    }

    override fun onAttach(activity: Activity) {
        super.onAttach(activity)
        mActivityListener = activity as ICartAddressChoiceActivityListener
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        chooseAddressTracePerformance = PerformanceMonitoring.start(CHOOSE_ADDRESS_TRACE)
        arguments?.let {
            mCurrentAddress = it.getParcelable(EXTRA_CURRENT_ADDRESS)
            requestType = it.getInt(CheckoutConstant.EXTRA_TYPE_REQUEST, 0)
            originDirectionType = it.getInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_DEFAULT)
            prevState = it.getInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS, -1)
        }
        context?.let {
            localChosenAddr = getLocalizingAddressData(it)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentShipmentAddressListBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        activity?.let {
            mInputMethodManager = it.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        }
        binding?.rvAddressList?.layoutManager = LinearLayoutManager(activity)
        binding?.rvAddressList?.adapter = mAdapter
        initSearchView()
        onSearchReset()
    }

    private fun initSearchView() {
        binding?.svAddressSearchBox?.searchTextView?.setOnClickListener(onSearchViewClickListener())
        binding?.svAddressSearchBox?.searchTextView?.setOnTouchListener(onSearchViewTouchListener())
        binding?.svAddressSearchBox?.setListener(this)
        binding?.svAddressSearchBox?.setResetListener(this)
        binding?.svAddressSearchBox?.setSearchHint(getString(com.tokopedia.purchase_platform.common.R.string.label_hint_search_address))
    }

    private fun onSearchViewTouchListener(): OnTouchListener? {
        return OnTouchListener { view, motionEvent ->
            binding?.svAddressSearchBox?.searchTextView?.isCursorVisible = true
            openSoftKeyboard()
            false
        }
    }

    private fun openSoftKeyboard() {
        mInputMethodManager?.showSoftInput(
                binding?.svAddressSearchBox?.searchTextView, InputMethodManager.SHOW_IMPLICIT
        )
    }

    private fun onSearchViewClickListener(): View.OnClickListener? {
        return View.OnClickListener { view: View? ->
            binding?.svAddressSearchBox?.searchTextView?.isCursorVisible = true
            openSoftKeyboard()
        }
    }

    override fun onDetach() {
        super.onDetach()
        mPresenter.detachView()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                LogisticConstant.REQUEST_CODE_PARAM_EDIT, LogisticConstant.REQUEST_CODE_PARAM_CREATE -> {
                    var address: RecipientAddressModel? = null
                    if (data != null && data.hasExtra(LogisticConstant.EXTRA_ADDRESS)) {
                        val intentModel: Destination? = data.getParcelableExtra(LogisticConstant.EXTRA_ADDRESS)
                        address = RecipientAddressModel()
                        intentModel?.let {
                            address.setId(it.addressId)
                            address.setAddressName(it.addressName)
                            address.setDestinationDistrictId(it.districtId)
                            address.setCityId(it.cityId)
                            address.setProvinceId(it.provinceId)
                            address.setRecipientName(it.receiverName)
                            address.setRecipientPhoneNumber(it.receiverPhone)
                            address.setStreet(it.addressStreet)
                            address.setPostalCode(it.postalCode)
                        }
                    }
                    if (requestType == CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN) {
                        if (context != null) {
                            mPresenter.getAddress(prevState, getChosenAddrId(), true)
                        }
                        mCurrentAddress = address
                    } else mActivityListener?.finishAndSendResult(address)
                }
                LogisticConstant.ADD_NEW_ADDRESS_CREATED_FROM_EMPTY, LogisticConstant.ADD_NEW_ADDRESS_CREATED -> {
                    val newAddress = RecipientAddressModel()
                    if (data != null && data.hasExtra(EXTRA_ADDRESS_NEW)) {
                        val intentModel: SaveAddressDataModel? = data.getParcelableExtra(EXTRA_ADDRESS_NEW)
                        intentModel?.let {
                            newAddress.setId(it.id.toString())
                            newAddress.setAddressName(it.addressName)
                            newAddress.setDestinationDistrictId(it.districtId.toString())
                            newAddress.setCityId(it.cityId.toString())
                            newAddress.setProvinceId(it.provinceId.toString())
                            newAddress.setRecipientName(it.receiverName)
                            newAddress.setRecipientPhoneNumber(it.phone)
                            newAddress.setStreet(it.formattedAddress)
                            newAddress.setPostalCode(it.postalCode)
                        }
                    }
                    if (requestType == CheckoutConstant.TYPE_REQUEST_SELECT_ADDRESS_FROM_COMPLETE_LIST_FOR_MONEY_IN) {
                        if (context != null) {
                            mPresenter.getAddress(prevState, getChosenAddrId(), true)
                        }
                        mCurrentAddress = newAddress
                    } else mActivityListener?.finishAndSendResult(newAddress)
                }
                else -> {
                    // no ops
                }
            }
        }
    }

    companion object {
        const val ARGUMENT_ORIGIN_DIRECTION_TYPE = "ARGUMENT_ORIGIN_DIRECTION_TYPE"
        const val ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM = 1
        const val EXTRA_CURRENT_ADDRESS = "CURRENT_ADDRESS"
        const val ORIGIN_DIRECTION_TYPE_DEFAULT = 0
        private const val CHOOSE_ADDRESS_TRACE = "mp_choose_another_address"
        const val EXTRA_ADDRESS_NEW = "EXTRA_ADDRESS_NEW"
        private const val PARAM_ADDRESS_MODEL = "EDIT_PARAM"
        private const val PARAM_TOKEN = "token"

        fun newInstance(currentAddress: RecipientAddressModel, prevState: Int) : ShipmentAddressListFragment {
            return ShipmentAddressListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress)
                    putInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_DEFAULT)
                    putInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS, prevState)
                }
            }
        }

        fun newInstance(currentAddress: RecipientAddressModel, requestType: Int, prevState: Int) : ShipmentAddressListFragment {
            return ShipmentAddressListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress)
                    putInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_DEFAULT)
                    putInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS, prevState)
                    putInt(CheckoutConstant.EXTRA_TYPE_REQUEST, requestType)
                }
            }
        }

        fun newInstanceFromMultipleAddressForm(currentAddress: RecipientAddressModel, prevState: Int) : ShipmentAddressListFragment {
            return ShipmentAddressListFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(EXTRA_CURRENT_ADDRESS, currentAddress)
                    putInt(ARGUMENT_ORIGIN_DIRECTION_TYPE, ORIGIN_DIRECTION_TYPE_FROM_MULTIPLE_ADDRESS_FORM)
                    putInt(CheckoutConstant.EXTRA_PREVIOUS_STATE_ADDRESS, prevState)
                }
            }
        }
    }

    interface ICartAddressChoiceActivityListener {
        fun finishAndSendResult(selectedAddressResult: RecipientAddressModel?)
    }
}