package com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.address

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
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticcart.shipping.model.RecipientAddressModel
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.checkout.subfeature.address_choice.domain.model.AddressListModel
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.di.PreferenceEditComponent
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.edit.view.shipping.ShippingDurationFragment
import kotlinx.android.synthetic.main.fragment_choose_address.*
import javax.inject.Inject

class AddressListFragment : BaseDaggerFragment(), SearchInputView.Listener, SearchInputView.ResetListener {

    override fun onSearchSubmitted(text: String) {
        performSearch(text)
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    override fun onSearchReset() {
        viewModel.getAddress()
    }

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: AddressListViewModel by lazy {
        ViewModelProviders.of(this, viewModelFactory)[AddressListViewModel::class.java]
    }

    companion object {
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

    val adapter = AddressListItemAdapter()
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

    private fun initViewModel(){
        viewModel.addressList.observe(this, Observer {
            when(it){
                is AddressListModel -> {
                    if(it.listAddress.isEmpty()){
                        empty_state_order_list.visibility = View.VISIBLE
                        address_list_rv.visibility = View.GONE
                    } else {
                        empty_state_order_list.gone()
                        address_list_rv.visible()
                        renderData(it.listAddress)
                    }

                }

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
        viewModel.getAddress()

        if(empty_state_order_list.visibility == View.GONE){
            btn_save_address.text = getString(R.string.label_button_input_address)
            btn_save_address.setOnClickListener {
                if (arguments?.getBoolean(ARG_IS_EDIT) == false) {
                    goToNextStep()
                } else {
                    goBack()
                }
            }
        } else {
            address_list_layout.visibility = View.GONE
            empty_state_order_list.visibility = View.VISIBLE
            btn_save_address.text = getString(R.string.label_button_input_address_empty)
            btn_save_address.setOnClickListener {
                goToPickLocation()
            }
        }

        address_list_rv.adapter = adapter
        address_list_rv.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        loadMore()
        initSearchView()
        onSearchReset()

    }

    private fun goBack() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
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
                    goToPickLocation()
                }
            }
        } else {
            val parent = activity
            if (parent is PreferenceEditActivity) {
                parent.hideDeleteButton()
                parent.hideAddButton()
                parent.showStepper()
                parent.setStepperValue(25, true)
                parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
                parent.setHeaderSubtitle(getString(R.string.activity_subtitle_choose_address))
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

    }

    private fun loadMore(){
        address_list_rv.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val adapter = recyclerView.adapter
                val totalItemCount = adapter!!.itemCount
                val lastVisibleItemPosition = (recyclerView.layoutManager as LinearLayoutManager)
                        .findLastVisibleItemPosition()

                if (maxItemPosition < lastVisibleItemPosition) {
                    maxItemPosition = lastVisibleItemPosition
                }

                if (maxItemPosition + 1 == totalItemCount && !isLoading && dy > 0) {
                    viewModel.loadMore()
                }
            }
        })

    }

    private fun initSearchView(){
        searchAddress.searchTextView.setOnClickListener(onSearchViewClickListener())
        searchAddress.searchTextView.setOnTouchListener(onSearchViewTouchListener())

        searchAddress.setListener(this)
        searchAddress.setResetListener(this)
        searchAddress.setSearchHint(getString(R.string.label_hint_search_address))
    }


    private fun performSearch(query: String){
        if(query.isNotEmpty()){
            viewModel.searchAddress(query)
        } else {
            viewModel.getAddress()
        }
    }

    private fun openSoftKeyboard(){
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

    private fun goToPickLocation(){
        val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
        intent.putExtra(EXTRA_IS_FULL_FLOW, true)
        startActivity(intent)
    }

    override fun onStart() {
        super.onStart()
//        setStep()
    }

    private fun setStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.showStepper()
            parent.setStepperValue(25, true)
            parent.setHeaderTitle(getString(R.string.activity_title_choose_address))
            parent.setHeaderSubtitle(getString(R.string.activity_subtitle_choose_address))
        }
    }

    private fun goToNextStep() {
        val parent = activity
        if (parent is PreferenceEditActivity) {
            parent.addFragment(ShippingDurationFragment.newInstance())
        }
    }

}