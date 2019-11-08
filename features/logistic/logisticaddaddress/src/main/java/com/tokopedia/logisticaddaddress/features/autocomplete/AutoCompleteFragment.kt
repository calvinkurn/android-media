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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.dropoff_picker.DaggerDropoffPickerComponent
import com.tokopedia.logisticaddaddress.features.addnewaddress.uimodel.autocomplete.AutoCompleteResultUi
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AutoCompleteFragment : Fragment(),
        SearchInputView.Listener, AutoCompleteAdapter.ActionListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AutoCompleteViewModel::class.java) }

    private val adapter: AutoCompleteAdapter = AutoCompleteAdapter()

    lateinit var searchInput: SearchInputView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_autocomplete, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        searchInput = view.findViewById(R.id.search_input_autocomplete)
        searchInput.setDelayTextChanged(400)
        searchInput.setListener(this)

        val rvResults = view.findViewById<RecyclerView>(R.id.rv_autocomplete)
        rvResults.layoutManager = LinearLayoutManager(context)
        rvResults.hasFixedSize()
        rvResults.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        rvResults.adapter = adapter
        adapter.setActionListener(this)
        viewModel.getSavedAddress()

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
    }

    override fun onSearchSubmitted(text: String?) {
        text?.let { fetchData(it) }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let { fetchData(it) }
    }

    override fun onResultClicked(data: AutoCompleteResultUi) {
        viewModel.getLatlng(data.placeId)
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
        viewModel.getAutoCompleteList(keyword)
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

    companion object {
        fun newInstance(): Fragment = AutoCompleteFragment().apply {
            arguments = Bundle()
        }
    }
}