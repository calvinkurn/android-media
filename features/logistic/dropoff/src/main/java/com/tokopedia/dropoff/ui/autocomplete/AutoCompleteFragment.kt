package com.tokopedia.dropoff.ui.autocomplete

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.dropoff.R
import com.tokopedia.dropoff.databinding.FragmentAutocompleteBinding
import com.tokopedia.dropoff.di.DaggerDropoffPickerComponent
import com.tokopedia.dropoff.ui.dropoff_picker.DropOffAnalytics
import com.tokopedia.dropoff.util.SimpleVerticalDivider
import com.tokopedia.kotlin.extensions.view.hideKeyboard
import com.tokopedia.logisticCommon.domain.model.AutoCompleteVisitable
import com.tokopedia.logisticCommon.domain.model.SavedAddress
import com.tokopedia.logisticCommon.domain.model.SuggestedPlace
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

const val DEBOUNCE_DELAY = 400L

class AutoCompleteFragment :
    Fragment(),
    AutoCompleteAdapter.ActionListener {

    @Inject
    lateinit var tracker: DropOffAnalytics

    @Inject
    lateinit var factory: ViewModelProvider.Factory

    private var binding by autoClearedNullable<FragmentAutocompleteBinding>()

    private val viewModel by lazy {
        ViewModelProvider(this, factory)[AutoCompleteViewModel::class.java]
    }

    private val adapter: AutoCompleteAdapter = AutoCompleteAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initInjector()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAutocompleteBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding?.searchInputAutocomplete?.apply {
            addDebouncedTextChangedListener(::onSearchTextChanged, DEBOUNCE_DELAY)
            searchBarTextField.setOnEditorActionListener { textView, actionId, _ ->
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    textView.hideKeyboard()
                    onSearchSubmitted(textView.text.toString())
                }
                return@setOnEditorActionListener actionId == EditorInfo.IME_ACTION_SEARCH
            }
        }

        binding?.rvAutocomplete?.apply {
            layoutManager = LinearLayoutManager(context)
            setHasFixedSize(true)
            addItemDecoration(SimpleVerticalDivider(context, R.layout.item_autocomplete_result))
            adapter = this@AutoCompleteFragment.adapter
        }
        adapter.setActionListener(this)

        fetchSavedAddress()
        setObservers()
    }

    override fun onResultClicked(data: AutoCompleteVisitable) {
        if (data is SuggestedPlace) {
            tracker.trackSelectLandmarkFromKeyword(
                binding?.searchInputAutocomplete?.searchBarTextField?.text?.toString().orEmpty(),
                data.mainText,
                data.secondaryText
            )
            if (!data.hasPinpoint()) {
                viewModel.getLatLng(data.placeId)
            } else {
                sendResult(data.lat, data.long)
            }
        } else if (data is SavedAddress) {
            sendResult(data.latitude, data.longitude)
        }
    }

    private fun onSearchSubmitted(text: String?) {
        text?.let { fetchData(it) }
    }

    private fun onSearchTextChanged(text: String?) {
        text?.let {
            if (it.isBlank()) {
                fetchSavedAddress()
            } else {
                fetchData(it)
            }
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
        viewModel.autoCompleteList.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> adapter.setData(it.data)
                    is Fail -> when (it.throwable) {
                        is MessageErrorException -> adapter.setNoResult()
                    }
                }
            }
        )
        viewModel.validatedDistrict.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> sendResult(it.data.latitude, it.data.longitude)
                    is Fail -> Toast.makeText(
                        context,
                        "Oops.. something went wrong",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
        )
        viewModel.savedAddress.observe(
            viewLifecycleOwner,
            Observer {
                when (it) {
                    is Success -> adapter.setEmptyData(it.data)
                    is Fail -> when (it.throwable) {
                        is MessageErrorException -> adapter.setNoResult()
                    }
                }
            }
        )
    }

    companion object {
        fun newInstance(): Fragment = AutoCompleteFragment().apply {
            arguments = Bundle()
        }
    }
}
