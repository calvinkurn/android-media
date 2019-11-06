package com.tokopedia.logisticaddaddress.features.autocomplete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.logisticaddaddress.R
import com.tokopedia.logisticaddaddress.di.dropoff_picker.DaggerDropoffPickerComponent
import javax.inject.Inject

class AutoCompleteFragment : Fragment() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(AutoCompleteViewModel::class.java) }

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

        val rvResults = view.findViewById<RecyclerView>(R.id.rv_autocomplete)
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