package com.tokopedia.oneclickcheckout.preference.edit.view.address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.EndlessRecyclerViewScrollListener
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.logisticCommon.domain.model.AddressListModel
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.DEFAULT_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.DEFAULT_LOCAL_ERROR_MESSAGE
import com.tokopedia.oneclickcheckout.common.view.model.OccState
import com.tokopedia.oneclickcheckout.preference.analytics.PreferenceListAnalytics
import com.tokopedia.oneclickcheckout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditParent
import com.tokopedia.oneclickcheckout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant.Companion.KERO_TOKEN
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AddressListFragment : BaseDaggerFragment(), AddressListItemAdapter.OnSelectedListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var preferenceListAnalytics: PreferenceListAnalytics

    private val viewModel: AddressListViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[AddressListViewModel::class.java]
    }

    private var adapter: AddressListItemAdapter? = null
    private var endlessScrollListener: EndlessRecyclerViewScrollListener? = null

    private var searchAddress: SearchBarUnify? = null
    private var addressListRv: RecyclerView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var buttonSaveAddress: UnifyButton? = null
    private var bottomLayout: FrameLayout? = null

    private var emptyStateLayout: LinearLayout? = null
    private var ivEmptyState: ImageUnify? = null

    private var textSearchError: Typography? = null
    private var globalErrorLayout: GlobalError? = null

    companion object {
        const val REQUEST_FIRST_CREATE = 1
        const val REQUEST_CREATE = 2

        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"
        const val EXTRA_IS_LOGISTIC_LABEL = "EXTRA_IS_LOGISTIC_LABEL"

        private const val EMPTY_STATE_PICT_URL = "https://ecs7.tokopedia.net/android/others/pilih_alamat_pengiriman3x.png"
        private const val ARG_IS_EDIT = "is_edit"
        private const val ARGS_ADDRESS_STATE = "ARGS_ADDRESS_STATE"

        fun newInstance(isEdit: Boolean = false, addressState: Int): AddressListFragment {
            val addressListFragment = AddressListFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            bundle.putInt(ARGS_ADDRESS_STATE, addressState)
            addressListFragment.arguments = bundle
            return addressListFragment
        }
    }

    private fun getAddressState(): Int {
        return arguments?.getInt(ARGS_ADDRESS_STATE) ?: 0
    }

    private fun getLocalCacheAddressId(): String {
        context?.let {
            return ChooseAddressUtils.getLocalizingAddressData(it)?.address_id ?: "0"
        }

        return "0"
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_choose_address, container, false)
    }

    private fun initViewModel() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            if (parent.getAddressId() > 0) {
                viewModel.selectedId = parent.getAddressId().toString()
            }
            val shippingParam = parent.getShippingParam()
            if (shippingParam != null) {
                viewModel.destinationLongitude = shippingParam.destinationLongitude
                viewModel.destinationLatitude = shippingParam.destinationLatitude
                viewModel.destinationDistrict = shippingParam.destinationDistrictId
                viewModel.destinationPostalCode = shippingParam.destinationPostalCode
            }
        }

        viewModel.addressList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is OccState.FirstLoad -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    setEmptyState(it.data.listAddress.isEmpty(), viewModel.savedQuery.isEmpty())
                    addressListRv?.scrollToPosition(0)
                    adapter?.setData(it.data.listAddress, it.data.hasNext ?: false)
                    endlessScrollListener?.resetState()
                    endlessScrollListener?.setHasNextPage(it.data.hasNext ?: false)
                    validateButton()
                }

                is OccState.Success -> {
                    adapter?.setData(it.data.listAddress, it.data.hasNext ?: false)
                    endlessScrollListener?.updateStateAfterGetData()
                    endlessScrollListener?.setHasNextPage(it.data.hasNext ?: false)
                    validateButton()
                }

                is OccState.Failed -> {
                    swipeRefreshLayout?.isRefreshing = false
                    it.getFailure()?.let { failure ->
                        handleError(failure.throwable)
                    }
                }

                is OccState.Loading -> swipeRefreshLayout?.isRefreshing = true
            }
        })
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initView()
        initRecyclerView()
        initSearchView()
        initViewModel()
        initSearch()
    }

    private fun setEmptyState(isEmpty: Boolean, isFirstLoad: Boolean) {
        if (!isEmpty) {
            buttonSaveAddress?.text = getString(R.string.label_button_input_address)
            buttonSaveAddress?.setOnClickListener {
                if (arguments?.getBoolean(ARG_IS_EDIT) == false) {
                    goToNextStep()
                } else {
                    goBack()
                }
            }
            emptyStateLayout?.gone()
            textSearchError?.gone()
            searchAddress?.visible()
            addressListRv?.visible()
            bottomLayout?.visible()
        } else if (isFirstLoad) {
            buttonSaveAddress?.text = getString(R.string.label_button_input_address_empty)
            buttonSaveAddress?.setOnClickListener {
                goToPickLocation(REQUEST_FIRST_CREATE)
            }
            addressListRv?.gone()
            searchAddress?.gone()
            textSearchError?.gone()
            emptyStateLayout?.visible()
            bottomLayout?.visible()
        } else {
            addressListRv?.gone()
            bottomLayout?.gone()
            emptyStateLayout?.gone()
            searchAddress?.visible()
            textSearchError?.visible()
        }
    }

    private fun validateButton() {
        buttonSaveAddress?.isEnabled = viewModel.selectedId.toIntOrZero() > 0
    }

    private fun initSearch() {
        val searchKey = viewModel.savedQuery
        searchAddress?.searchBarTextField?.setText(searchKey)

        context?.let {
            viewModel.searchAddress(searchKey, getAddressState(), getLocalCacheAddressId(), ChooseAddressUtils.isRollOutUser(it))
        }
    }

    private fun initView() {
        context?.let {
            activity?.window?.decorView?.setBackgroundColor(ContextCompat.getColor(it, com.tokopedia.unifyprinciples.R.color.Unify_N0))
        }
        addressListRv = view?.findViewById(R.id.address_list_rv)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh_layout)
        searchAddress = view?.findViewById(R.id.search_input_view)
        buttonSaveAddress = view?.findViewById(R.id.btn_save_address)
        bottomLayout = view?.findViewById(R.id.bottom_layout_address)
        emptyStateLayout = view?.findViewById(R.id.empty_state_order_list)
        ivEmptyState = view?.findViewById(R.id.iv_empty_state)
        textSearchError = view?.findViewById(R.id.text_search_error)
        globalErrorLayout = view?.findViewById(R.id.global_error)

        ivEmptyState?.setImageUrl(EMPTY_STATE_PICT_URL)
    }

    private fun initRecyclerView() {
        adapter = AddressListItemAdapter(this)
        addressListRv?.adapter = adapter
        val linearLayoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        addressListRv?.layoutManager = linearLayoutManager
        addressListRv?.clearOnScrollListeners()
        endlessScrollListener = object : EndlessRecyclerViewScrollListener(linearLayoutManager) {
            override fun onLoadMore(page: Int, totalItemsCount: Int) {
                context?.let {
                    viewModel.loadMore(getAddressState(), getLocalCacheAddressId(), ChooseAddressUtils.isRollOutUser(it))
                }
            }
        }
        endlessScrollListener?.let {
            addressListRv?.addOnScrollListener(it)
        }
    }

    private fun goBack() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            val selectedId = viewModel.selectedId.toIntOrZero()
            if (selectedId > 0) {
                preferenceListAnalytics.eventClickSimpanAlamatInPilihAlamatPage()
                parent.setAddressId(selectedId)
                viewModel.selectedAddressModel?.let {
                    parent.setNewlySelectedAddressModel(it)
                }
                setShippingParam()
                parent.goBack()
            }
        }
    }

    private fun initHeader() {
        if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
            val parent = activity
            if (parent is PreferenceEditParent) {
                parent.hideStepper()
                parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
                parent.showAddButton()
                parent.setAddButtonOnClickListener {
                    if (viewModel.token != null) {
                        goToPickLocation(REQUEST_CREATE)
                    } else {
                        view?.let {
                            Toaster.build(it, DEFAULT_LOCAL_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
        } else {
            val parent = activity
            if (parent is PreferenceEditParent) {
                parent.showAddButton()
                parent.setAddButtonOnClickListener {
                    goToPickLocation(REQUEST_CREATE)
                }
                parent.showStepper()
                parent.setStepperValue(25)
                parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
                parent.setHeaderSubtitle(getString(R.string.activity_subtitle_choose_address))
            }
        }
    }

    /*OnActivityResult utk flow dari ana*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        preferenceListAnalytics.eventClickPilihDurasiInANAFlow()
        if (requestCode == REQUEST_FIRST_CREATE) {
            val saveAddressDataModel = data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
            if (saveAddressDataModel != null) {
                viewModel.selectedId = saveAddressDataModel.id.toString()
                viewModel.destinationLongitude = saveAddressDataModel.longitude
                viewModel.destinationLatitude = saveAddressDataModel.latitude
                viewModel.destinationPostalCode = saveAddressDataModel.postalCode
                viewModel.destinationDistrict = saveAddressDataModel.districtId.toString()
                context?.let {
                    viewModel.searchAddress("", getAddressState(), getLocalCacheAddressId(), ChooseAddressUtils.isRollOutUser(it))
                }
                goToNextStep()
            }
        } else if (requestCode == REQUEST_CREATE) {
            context?.let {
                viewModel.searchAddress(searchAddress?.searchBarTextField?.text?.toString() ?: "", getAddressState(), getLocalCacheAddressId(), ChooseAddressUtils.isRollOutUser(it))
            }
        }

    }

    private fun initSearchView() {
        searchAddress?.searchBarTextField?.setOnClickListener {
            searchAddress?.searchBarTextField?.isCursorVisible = true
            openSoftKeyboard()
        }
        searchAddress?.searchBarTextField?.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAddress?.clearFocus()
                context?.let {
                    viewModel.searchAddress(searchAddress?.searchBarTextField?.text?.toString() ?: "", getAddressState(), getLocalCacheAddressId(), ChooseAddressUtils.isRollOutUser(it))
                }
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }
        searchAddress?.clearListener = {
            context?.let {
                viewModel.searchAddress("", getAddressState(), getLocalCacheAddressId(), ChooseAddressUtils.isRollOutUser(it))
            }
        }
        searchAddress?.searchBarPlaceholder = getString(com.tokopedia.purchase_platform.common.R.string.label_hint_search_address)
    }

    override fun onSelect(addressModel: RecipientAddressModel) {
        preferenceListAnalytics.eventClickAddressOptionInPilihAlamatPage()
        viewModel.setSelectedAddress(addressModel)
    }

    private fun openSoftKeyboard() {
        searchAddress?.searchBarTextField?.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun goToPickLocation(requestCode: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
        intent.putExtra(EXTRA_IS_FULL_FLOW, true)
        intent.putExtra(EXTRA_IS_LOGISTIC_LABEL, false)
        intent.putExtra(KERO_TOKEN, viewModel.token)
        startActivityForResult(intent, requestCode)
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            val selectedId = viewModel.selectedId.toIntOrZero()
            if (selectedId > 0) {
                viewModel.selectedAddressModel?.let {
                    parent.setNewlySelectedAddressModel(it)
                }
                preferenceListAnalytics.eventClickSimpanAlamatInPilihAlamatPage()
                parent.setAddressId(selectedId)
                setShippingParam()
                parent.addFragment(ShippingDurationFragment())
            }
        }
    }

    private fun setShippingParam() {
        val parent = activity
        if (parent is PreferenceEditParent) {
            val shippingParam = parent.getShippingParam()
            if (shippingParam != null) {
                shippingParam.destinationDistrictId = viewModel.destinationDistrict
                shippingParam.addressId = viewModel.selectedId
                shippingParam.destinationLatitude = viewModel.destinationLatitude
                shippingParam.destinationLongitude = viewModel.destinationLongitude
                shippingParam.destinationPostalCode = viewModel.destinationPostalCode
                shippingParam.let { parent.setShippingParam(it) }
            }
        }
    }

    private fun handleError(throwable: Throwable?) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            Toaster.build(it, DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.build(it, throwable?.message
                            ?: DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR).show()
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            searchAddress?.searchBarTextField?.setText("")
            context?.let {
                viewModel.searchAddress("", getAddressState(), getLocalCacheAddressId(), ChooseAddressUtils.isRollOutUser(it))
            }
        }
        searchAddress?.gone()
        textSearchError?.gone()
        addressListRv?.gone()
        bottomLayout?.gone()
        emptyStateLayout?.gone()
        globalErrorLayout?.visible()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        searchAddress = null
        addressListRv = null
        swipeRefreshLayout = null
        buttonSaveAddress = null
        bottomLayout = null
        emptyStateLayout = null
        textSearchError = null
        globalErrorLayout = null
    }
}