package com.tokopedia.manageaddress.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.example.manageaddress.R
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressActivity
import com.tokopedia.logisticdata.data.entity.address.AddressModel
import com.tokopedia.manageaddress.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.model.ManageAddressState
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_manage_address.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class ManageAddressFragment : BaseDaggerFragment(), SearchInputView.Listener, ManageAddressItemAdapter.ManageAddressItemAdapterListener{

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
        initView()
        initViewModel()
        initSearch()
        address_list.adapter = adapter
        address_list.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initSearchView()
    }

    override fun onSearchSubmitted(text: String?) {
        performSearch(text)
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    /*flow from edit address*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        performSearch("")
    }

    private fun openSoftKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                searchAddress.searchTextView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun performSearch(query: String?) {
        query?.let { viewModel.searchAddress(it) }
    }

    private fun initView() {
        addressList = view?.findViewById(R.id.address_list)
        searchInputView = view?.findViewById(R.id.search_input_view)
        swipeRefreshLayout = view?.findViewById(R.id.swipe_refresh)
    }

    private fun initViewModel() {
        viewModel.addressList.observe(this, Observer {
            when (it) {
                is ManageAddressState.Success -> {
                    swipeRefreshLayout?.isRefreshing = false
                    renderData(it.data.listAddress)
                }

                is ManageAddressState.Fail -> {
                    if(!it.isConsumed) {
                        swipeRefreshLayout?.isRefreshing = false
                        if(it.throwable != null) {
                            handleError(it.throwable)
                        }
                    }
                }

                else -> swipeRefreshLayout?.isRefreshing = true
            }
        })
    }

    private fun initSearch() {
        val searchKey = ""
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

    private fun renderData(data: List<AddressModel>) {
        adapter.addressList.clear()
        adapter.addressList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onManageAddressEditClicked(peopleAddress: AddressModel) {
       openFormAddressView(peopleAddress)
    }

    private fun openFormAddressView(data: AddressModel){
        val token = viewModel.getToken()
        startActivityForResult(activity?.let {
            AddAddressActivity.createInstanceEditAddress(it, data, token)
        }, 102)
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
        viewModel.consumeSearchAddressFail()
    }

    private fun showGlobalError(type: Int) {
//        ToDo:
    }
}