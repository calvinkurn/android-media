package com.tokopedia.manageaddress.ui.manageaddress

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
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
import com.tokopedia.logisticdata.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticdata.data.entity.address.SaveAddressDataModel
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.data.analytics.ManageAddressAnalytics
import com.tokopedia.manageaddress.di.manageaddress.ManageAddressComponent
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.EDIT_PARAM
import com.tokopedia.manageaddress.util.ManageAddressConstant.KERO_TOKEN
import com.tokopedia.manageaddress.util.ManageAddressConstant.LABEL_LAINNYA
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_CREATE
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_EDIT
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_USER_NEW
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottomsheet_action_address.view.*
import kotlinx.android.synthetic.main.empty_manage_address.*
import kotlinx.android.synthetic.main.fragment_manage_address.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ManageAddressFragment : BaseDaggerFragment(), SearchInputView.Listener, ManageAddressItemAdapter.ManageAddressItemAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = ManageAddressItemAdapter(this)

    private val viewModel : ManageAddressViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ManageAddressViewModel::class.java]
    }

    private lateinit var searchAddress: SearchInputView
    private var addressList: RecyclerView? = null
    private var searchInputView: SearchInputView? = null
    private var swipeRefreshLayout: SwipeRefreshLayout? = null
    private var bottomSheetLainnya: BottomSheetUnify? = null
    private var emptySearchLayout: LinearLayout? = null

    private var buttonAddEmpty: UnifyButton? = null
    private var emptyStateLayout: LinearLayout? = null

    private var globalErrorLayout: GlobalError? = null

    private var manageAddressListener: ManageAddressListener? = null

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
        initSearch()
        address_list.adapter = adapter
        address_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initSearchView()
    }

    override fun onSearchSubmitted(text: String) {
        performSearch(text)
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_PARAM_CREATE) {
            val addressDataModel = data?.getParcelableExtra<SaveAddressDataModel>("EXTRA_ADDRESS_NEW")
            if (addressDataModel != null) {
                performSearch("")
            }
        } else if (requestCode == REQUEST_CODE_PARAM_EDIT) {
            performSearch(searchAddress.searchTextView.text.toString())
        }
    }

    private fun openSoftKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                searchAddress.searchTextView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun performSearch(query: String) {
        viewModel.searchAddress(query)
    }

    private fun initHeader() {
        manageAddressListener?.setAddButtonOnClickListener {
            openFormAddressView(null)
        }
    }

    private fun initView() {
        addressList = view?.findViewById(R.id.address_list)
        searchInputView = view?.findViewById(R.id.search_input_view)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
        emptySearchLayout = view?.findViewById(R.id.empty_search)
        emptyStateLayout = view?.findViewById(R.id.empty_state_manage_address)
        globalErrorLayout = view?.findViewById(R.id.global_error)
        buttonAddEmpty = view?.findViewById(R.id.btn_add_empty)

        ImageHandler.LoadImage(iv_empty_state, EMPTY_STATE_PICT_URL)
        ImageHandler.LoadImage(iv_empty_address, EMPTY_SEARCH_PICT_URL)
    }

    private fun initViewModel() {
        viewModel.addressList.observe(this, Observer {
            when (it) {
                is ManageAddressState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    setEmptyState(it.data.listAddress.isEmpty(), viewModel.savedQuery.isNullOrEmpty())
                    renderData(it.data.listAddress)
                }

                is ManageAddressState.Fail -> {
                    swipeRefreshLayout?.isRefreshing = false
                    if(it.throwable != null) {
                        handleError(it.throwable)
                    }
                }

                else -> swipeRefreshLayout?.isRefreshing = true
            }
        })
    }

    private fun setEmptyState(isEmpty: Boolean, isFirstLoad: Boolean) {
        if(!isEmpty) {
            emptyStateLayout?.gone()
            searchAddress.visible()
            addressList?.visible()
            emptySearchLayout?.gone()
        }
        else if (isFirstLoad) {
            buttonAddEmpty?.setOnClickListener {
                openFormAddressView(null)
            }
            emptyStateLayout?.visible()
            searchAddress.gone()
            addressList?.gone()
            emptySearchLayout?.gone()
        }
        else {
            emptySearchLayout?.visible()
            emptyStateLayout?.gone()
            searchAddress.visible()
            addressList?.gone()
        }
    }

    private fun initSearch() {
        val searchKey = viewModel.savedQuery
        searchInputView?.searchText = searchKey
        performSearch(searchKey)
    }

    private fun initSearchView() {
        searchAddress.searchTextView.setOnClickListener(onSearchViewClickListener())
        searchAddress.searchTextView.setOnTouchListener(onSearchViewTouchListener())
        searchAddress.setResetListener {
            performSearch("")
        }
        searchAddress.setListener(this)
        searchAddress.setSearchHint("Cari Alamat")
    }

    private fun onSearchViewClickListener(): View.OnClickListener {
        return View.OnClickListener { view ->
            searchAddress.searchTextView.isCursorVisible = true
            openSoftKeyboard()
        }
    }

    private fun onSearchViewTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { view, motionEvent ->
            searchAddress.searchTextView.isCursorVisible = true
            openSoftKeyboard()
            false
        }
    }

    private fun renderData(data: List<RecipientAddressModel>) {
        adapter.addList(data)
    }

    override fun onManageAddressEditClicked(peopleAddress: RecipientAddressModel) {
        openFormAddressView(peopleAddress)
    }

    override fun onManageAddressLainnyaClicked(peopleAddress: RecipientAddressModel) {
        openBottomSheetView(peopleAddress)
    }

    private fun openFormAddressView(data: RecipientAddressModel?) {
        val token = viewModel.token
        if(data == null) {
            activity?.let { ManageAddressAnalytics.sendScreenName(it, SCREEN_NAME_USER_NEW) }
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            intent.putExtra(KERO_TOKEN, token)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        } else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V1)
            val mapper = AddressModelMapper()
            intent.putExtra(EDIT_PARAM, mapper.transform(data))
            intent.putExtra(KERO_TOKEN, token)
            startActivityForResult(intent, REQUEST_CODE_PARAM_EDIT)
        }
    }

    private fun openBottomSheetView(data: RecipientAddressModel) {
        bottomSheetLainnya = BottomSheetUnify()
        val viewBottomSheetLainnya = View.inflate(context, R.layout.bottomsheet_action_address, null).apply {
            if (data.addressStatus == 2) layout_utama.gone() else layout_utama.visible()
            btn_alamat_utama.setOnClickListener {
                viewModel.setDefaultPeopleAddress(data.id)
                bottomSheetLainnya?.dismiss()
            }
            btn_hapus_alamat.setOnClickListener {
                viewModel.deletePeopleAddress(data.id)
                bottomSheetLainnya?.dismiss()
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
                            Toaster.make(it, DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable.message
                            ?: DEFAULT_ERROR_MESSAGE, type = Toaster.TYPE_ERROR)
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalErrorLayout?.setType(type)
        globalErrorLayout?.setActionClickListener {
            viewModel.searchAddress("")
        }
        searchAddress.gone()
        addressList?.gone()
        emptyStateLayout?.gone()
        globalErrorLayout?.visible()
        emptySearchLayout?.gone()
    }

    fun setListener(listener: ManageAddressListener) {
        this.manageAddressListener = listener
    }

    interface ManageAddressListener{
        fun setAddButtonOnClickListener(onClick: () -> Unit)
    }

    companion object {

        private const val EMPTY_STATE_PICT_URL = "https://ecs7.tokopedia.net/android/others/pilih_alamat_pengiriman3x.png"
        private const val EMPTY_SEARCH_PICT_URL = "https://ecs7.tokopedia.net/android/others/address_not_found3x.png"

        fun newInstance() : ManageAddressFragment {
            return ManageAddressFragment()
        }
    }
}