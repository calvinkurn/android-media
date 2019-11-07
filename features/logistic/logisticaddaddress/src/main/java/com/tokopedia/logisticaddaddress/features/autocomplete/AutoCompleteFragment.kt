package com.tokopedia.logisticaddaddress.features.autocomplete

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
import com.tokopedia.logisticaddaddress.di.dropoff_picker.DaggerDropoffPickerComponent
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

class AutoCompleteFragment : Fragment(), SearchInputView.Listener {

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
        rvResults.adapter = adapter

        viewModel.autoCompleteList.observe(this, Observer {
            when(it) {
                is Success -> adapter.setData(it.data)
            }
        })
    }

    override fun onSearchSubmitted(text: String?) {
        text?.let { viewModel.getAutoCompleteList(it) }
    }

    override fun onSearchTextChanged(text: String?) {
        text?.let { viewModel.getAutoCompleteList(it) }
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

    companion object {
        fun newInstance(): Fragment = AutoCompleteFragment().apply {
            arguments = Bundle()
        }
    }
}