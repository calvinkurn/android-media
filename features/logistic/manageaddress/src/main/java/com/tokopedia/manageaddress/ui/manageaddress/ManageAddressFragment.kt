package com.tokopedia.manageaddress.ui.manageaddress

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
import com.tokopedia.localizationchooseaddress.ui.widget.ChooseAddressWidget
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.di.manageaddress.ManageAddressComponent
import com.tokopedia.manageaddress.domain.mapper.AddressModelMapper
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.EDIT_PARAM
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_REF
import com.tokopedia.manageaddress.util.ManageAddressConstant.KERO_TOKEN
import com.tokopedia.manageaddress.util.ManageAddressConstant.LABEL_LAINNYA
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_CREATE
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_EDIT
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_USER_NEW
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.SearchBarUnify
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

    private val viewModel: ManageAddressViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ManageAddressViewModel::class.java]
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
    private var chooseAddressWidget: ChooseAddressWidget? = null

    private var maxItemPosition: Int = -1
    private var isLoading: Boolean = false

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
            performSearch(searchAddress?.searchBarTextField?.text?.toString() ?: "")
        }
    }

    private fun openSoftKeyboard() {
        searchAddress?.searchBarTextField?.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(it, InputMethodManager.SHOW_IMPLICIT)
        }
    }

    private fun performSearch(query: String) {
        clearData()
        maxItemPosition = 0
        viewModel.searchAddress(query)
    }

    private fun initHeader() {
        manageAddressListener?.setAddButtonOnClickListener {
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
        chooseAddressWidget = view?.findViewById(R.id.choose_address_widget)

        ImageHandler.LoadImage(iv_empty_state, EMPTY_STATE_PICT_URL)
        ImageHandler.LoadImage(iv_empty_address, EMPTY_SEARCH_PICT_URL)

        fragmentManager?.let { chooseAddressWidget?.setBindFragmentManager(it, this ) }

        initScrollListener()

    }

    private fun initViewModel() {
        viewModel.addressList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is ManageAddressState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    globalErrorLayout?.gone()
                    if (viewModel.isClearData) clearData()
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
    }

    private fun initScrollListener() {
        addressList?.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adapter = recyclerView.adapter
                val totalItemCount = adapter?.itemCount
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()

                if(maxItemPosition < lastVisibleItemPosition) {
                    maxItemPosition = lastVisibleItemPosition
                }

                if ((maxItemPosition + 1) == totalItemCount && viewModel.canLoadMore && !isLoading) {
                    viewModel.loadMore()
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
        performSearch(searchKey)
    }

    private fun initSearchView() {
        searchAddress?.searchBarTextField?.setOnClickListener {
            searchAddress?.searchBarTextField?.isCursorVisible = true
            openSoftKeyboard()
        }

        searchAddress?.searchBarTextField?.setOnEditorActionListener { _, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                searchAddress?.clearFocus()
                performSearch(searchAddress?.searchBarTextField?.text?.toString() ?: "")
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        searchAddress?.clearListener = {
           performSearch("")
        }

        searchAddress?.searchBarPlaceholder = "Cari Alamat"
    }

    private fun updateData(data: List<RecipientAddressModel>) {
        adapter.addList(data)
    }

    private fun clearData() {
        adapter.clearData()
    }

    override fun onManageAddressEditClicked(peopleAddress: RecipientAddressModel) {
        openFormAddressView(peopleAddress)
    }

    override fun onManageAddressLainnyaClicked(peopleAddress: RecipientAddressModel) {
        openBottomSheetView(peopleAddress)
    }

    private fun openFormAddressView(data: RecipientAddressModel?) {
        val token = viewModel.token
        if (data == null) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, SCREEN_NAME_USER_NEW)
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
            viewModel.searchAddress("")
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

        fun newInstance(): ManageAddressFragment {
            return ManageAddressFragment()
        }
    }
}