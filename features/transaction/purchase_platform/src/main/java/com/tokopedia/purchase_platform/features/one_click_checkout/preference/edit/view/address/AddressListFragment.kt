package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel
import com.tokopedia.network.constant.ResponseStatus
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.view.CartAddressChoiceActivity.KERO_TOKEN
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccState
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationFragment
import com.tokopedia.unifycomponents.Toaster
import kotlinx.android.synthetic.main.fragment_choose_address.*
import kotlinx.android.synthetic.main.fragment_detail_product_page.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class AddressListFragment : BaseDaggerFragment(), SearchInputView.Listener, AddressListItemAdapter.onSelectedListener {

    override fun onSelect(addressId: String) {
        viewModel.setSelectedAddress(addressId)
    }

    override fun onSearchSubmitted(text: String) {
        performSearch(text)
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddressListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AddressListViewModel::class.java]
    }

    companion object {
        const val REQUEST_FIRST_CREATE = 1
        const val REQUEST_CREATE = 2

        const val EXTRA_IS_FULL_FLOW = "EXTRA_IS_FULL_FLOW"

        private const val ARG_IS_EDIT = "is_edit"

        fun newInstance(isEdit: Boolean = false): AddressListFragment {
            val addressListFragment = AddressListFragment()
            val bundle = Bundle()
            bundle.putBoolean(ARG_IS_EDIT, isEdit)
            addressListFragment.arguments = bundle
            return addressListFragment
        }
    }

    val adapter = AddressListItemAdapter(this)
    private var maxItemPosition: Int = 0
    private var isLoading: Boolean = false
    private lateinit var inputMethodManager: InputMethodManager
    private lateinit var searchAddress: SearchInputView

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PreferenceEditComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_choose_address, container, false)
        searchAddress = view.findViewById(R.id.search_input_view)
        return view
    }

    private fun initViewModel() {
        viewModel.addressList.observe(this, Observer {
            when (it) {
                is OccState.Success -> {
                    swipe_refresh_layout.isRefreshing = false
                    global_error.gone()
                    content_layout.visible()
                    if (it.data.listAddress.isEmpty()) {
                        text_search_error.visible()
                        content_layout.gone()
                    } else {
                        text_search_error.gone()
                        address_list_rv.visible()
                        renderData(it.data.listAddress)
                    }
                }

                is OccState.Fail -> {
                    if (!it.isConsumed) {
                        swipe_refresh_layout.isRefreshing = false
                        if (it.throwable != null) {
                            handleError(it.throwable)
                        }
                    }
                }

                else -> swipe_refresh_layout.isRefreshing = true

            }
        })

    }

    private fun renderData(data: List<RecipientAddressModel>) {
        adapter.addressList.clear()
        adapter.addressList.addAll(data)
        adapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initViewModel()
        initSearch()
        initView()
        address_list_rv.adapter = adapter
        address_list_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        initSearchView()

    }

    private fun initView() {
        if (empty_state_order_list.visibility == View.GONE) {
            btn_save_address.text = getString(R.string.label_button_input_address)
            btn_save_address.setOnClickListener {
                if (arguments?.getBoolean(ARG_IS_EDIT) == false) {
                    goToNextStep()
                } else {
                    goBack()
                }
            }
        } else {
            empty_state_order_list.visibility = View.VISIBLE
            btn_save_address.text = getString(R.string.label_button_input_address_empty)
            btn_save_address.setOnClickListener {
                goToPickLocation(REQUEST_FIRST_CREATE)
            }
        }
    }

    private fun initSearch(){
        val searchKey = viewModel.savedQuery
        search_input_view.searchText = searchKey

        performSearch(searchKey)
    }

    private fun goBack() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.addressId = adapter.addresspositionId
            Log.d("address_fragment", parent.addressId.toString())
            parent.goBack()
        }
    }

    private fun initHeader() {
        if (arguments?.getBoolean(ARG_IS_EDIT) == true) {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.hideStepper()
                parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
                parent.showAddButton()
                parent.setAddButtonOnClickListener {
                    if (viewModel.token != null) {
                        goToPickLocation(REQUEST_CREATE)
                    } else {
                        view?.let {
                            Toaster.make(it, "Failed", type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
        } else {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.hideDeleteButton()
                parent.hideAddButton()
                parent.showAddButton()
                parent.setAddButtonOnClickListener {
                    goToPickLocation(REQUEST_CREATE)
                }
                parent.showStepper()
                parent.setStepperValue(25, true)
                parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
                parent.setHeaderSubtitle(getString(R.string.activity_subtitle_choose_address))
            }
        }
    }

    /*OnActivityResult utk flow dari ana*/
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_FIRST_CREATE) {
            performSearch("")
            goToNextStep()
        } else if (requestCode == REQUEST_CREATE) {
            performSearch(searchAddress.searchTextView.text.toString())
        }

    }

    private fun initSearchView() {
        searchAddress.searchTextView.setOnClickListener(onSearchViewClickListener())
        searchAddress.searchTextView.setOnTouchListener(onSearchViewTouchListener())
        searchAddress.setResetListener {
            performSearch("")
        }
        searchAddress.setListener(this)
        searchAddress.setSearchHint(getString(R.string.label_hint_search_address))

    }


    private fun performSearch(query: String) {
        viewModel.searchAddress(query)
    }

    private fun openSoftKeyboard() {
        (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)?.showSoftInput(
                searchAddress.searchTextView, InputMethodManager.SHOW_IMPLICIT)
    }

    private fun onSearchViewTouchListener(): View.OnTouchListener {
        return View.OnTouchListener { view, motionEvent ->
            searchAddress.searchTextView.isCursorVisible = true
            openSoftKeyboard()
            false
        }
    }

    private fun onSearchViewClickListener(): View.OnClickListener {
        return View.OnClickListener { view ->
            searchAddress.searchTextView.isCursorVisible = true
            openSoftKeyboard()
        }
    }

    private fun goToPickLocation(requestCode: Int) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
        intent.putExtra(EXTRA_IS_FULL_FLOW, true)
        intent.putExtra(KERO_TOKEN, viewModel.token)
        startActivityForResult(intent, requestCode)
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            val selectedId = viewModel.selectedId.toIntOrZero()
            if(selectedId > 0) {
                parent.addressId = selectedId
                parent.addFragment(ShippingDurationFragment())
            }
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
                            Toaster.make(it, "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    Toaster.make(it, throwable.message
                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                }
            }
        }
        viewModel.consumeSearchAddressFail()
    }

    private fun showGlobalError(type: Int) {
        global_error.setType(type)
        global_error.setActionClickListener {
            viewModel.searchAddress("")
        }
        content_layout.gone()
        global_error.visible()
    }

}