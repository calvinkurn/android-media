package com.tokopedia.manageaddress.ui

import android.content.Context
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
import com.example.manageaddress.R
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.features.addaddress.AddAddressActivity
import com.tokopedia.logisticdata.data.entity.address.AddressModel
import com.tokopedia.logisticdata.data.entity.address.Token
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.model.PeopleAddress
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_manage_address.*
import javax.inject.Inject

class ManageAddressFragment : BaseDaggerFragment(), SearchInputView.Listener, ManageAddressItemAdapter.ManageAddressItemAdapterListener{

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val adapter = ManageAddressItemAdapter()

    private val viewModel : ManageAddressViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[ManageAddressViewModel::class.java]
    }

    private lateinit var searchAddress: SearchInputView
    private var addressList: RecyclerView? = null
    private var searchInputView: SearchInputView? = null

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
        initSearch()
        initView()
        initViewModel()
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
    }

    private fun initViewModel() {
        viewModel.addressList.observe(this, Observer {
            when (it) {
                is Success -> renderData(it.data.liistAddress)
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

    private fun renderData(data: List<PeopleAddress>) {
        adapter.addressList.clear()
        adapter.addressList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onManageAddressEditClicked(peopleAddress: PeopleAddress) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun openFormAddressView(data: PeopleAddress){
        val token = viewModel.getToken()
        startActivityForResult(activity?.let {
            AddAddressActivity.createInstanceEditAddress(
                    it,
                data as AddressModel,
                    token as Token)
        }, 102)
    }
}