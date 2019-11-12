package com.tokopedia.logisticaddaddress.features.autocomplete

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.common.SimpleVerticalDivider
import com.tokopedia.logisticaddaddress.di.dropoff_picker.DaggerDropoffPickerComponent
import com.tokopedia.logisticaddaddress.features.autocomplete.model.AutoCompleteVisitable
import com.tokopedia.logisticaddaddress.features.autocomplete.model.SavedAddress
import com.tokopedia.logisticaddaddress.features.autocomplete.model.SuggestedPlace
import com.tokopedia.logisticaddaddress.features.dropoff_picker.DropOffAnalytics
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

const val DEBOUNCE_DELAY = 400L

class AutoCompleteFragment : Fragment(),
        SearchInputView.Listener, AutoCompleteAdapter.ActionListener {

    @Inject
    lateinit var tracker: DropOffAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AutoCompleteViewModel::class.java) }

    private lateinit var searchTextView: SearchInputView
    private val adapter: AutoCompleteAdapter = AutoCompleteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_autocomplete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchTextView = (view.findViewById<SearchInputView>(R.id.search_input_autocomplete)).apply {
            setDelayTextChanged(DEBOUNCE_DELAY)
            setListener(this@AutoCompleteFragment)
        }
        with(view.findViewById<RecyclerView>(R.id.rv_autocomplete)) {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(SimpleVerticalDivider(context, R.layout.item_autocomplete_result))
            adapter = this@AutoCompleteFragment.adapter
        }
        adapter.setActionListener(this)

        fetchSavedAddress()
        setObservers()
    }

    override fun onSearchSubmitted(text: String?) {
        text?.let { fetchData(it) }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let {
            if (it.isBlank()) {
                fetchSavedAddress()
            } else {
                fetchData(it)
            }
        }
    }

    override fun onResultClicked(data: AutoCompleteVisitable) {
        if (data is SuggestedPlace) {
            tracker.trackSelectLandmarkFromKeyword(
                    searchTextView.searchText, data.mainText, data.secondaryText)
            viewModel.getLatLng(data.placeId)
        } else if (data is SavedAddress) {
            sendResult(data.latitude, data.longitude)
        }
    }

    private fun initInjector() {
        activity?.application?.let {
            if (it is BaseMainApplication) {
                DaggerDropoffPickerComponent.builder()
                        .baseAppComponent(it.baseAppComponent)
                        .build().inject(this)
            }
        }
    }

    private fun fetchData(keyword: String) {
        tracker.trackSearchKeyword(keyword)
        viewModel.getAutoCompleteList(keyword)
        adapter.setLoading()
    }

    private fun fetchSavedAddress() {
        viewModel.getSavedAddress()
        adapter.setLoading()
    }

    private fun sendResult(lat: String, long: String) {
        val resultIntent = Intent().apply {
            putExtra("BUNDLE_LATITUDE", lat)
            putExtra("BUNDLE_LONGITUDE", long)
        }
        activity?.let {
            it.setResult(Activity.RESULT_OK, resultIntent)
            it.finish()
        }
    }

    private fun setObservers() {
        viewModel.autoCompleteList.observe(this, Observer {
            when (it) {
                is Success -> adapter.setData(it.data)
                is Fail -> when (it.throwable) {
                    is MessageErrorException -> adapter.setNoResult()
                }
            }
        })
        viewModel.validatedDistrict.observe(this, Observer {
            when (it) {
                is Success -> sendResult(it.data.latitude, it.data.longitude)
            }
        })
        viewModel.savedAddress.observe(this, Observer {
            when (it) {
                is Success -> adapter.setEmptyData(it.data)
                is Fail -> when (it.throwable) {
                    is MessageErrorException -> adapter.setNoResult()
                }
            }
        })
    }


    companion object {
        fun newInstance(): Fragment = AutoCompleteFragment().apply {
            arguments = Bundle()
        }
    }
}