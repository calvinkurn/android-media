package com.tokopedia.travel.country_code.presentation.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.abstraction.base.view.fragment.BaseSearchListFragment
import com.tokopedia.abstraction.common.utils.GraphqlHelper
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.travel.country_code.R
import com.tokopedia.travel.country_code.di.TravelCountryCodeComponent
import com.tokopedia.travel.country_code.presentation.adapter.PhoneCodePickerAdapterTypeFactory
import com.tokopedia.travel.country_code.presentation.model.TravelCountryPhoneCode
import com.tokopedia.travel.country_code.presentation.viewmodel.PhoneCodePickerViewModel
import com.tokopedia.travel.country_code.util.TravelCountryCodeGqlQuery
import com.tokopedia.unifycomponents.SearchBarUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 23/12/2019
 */
class PhoneCodePickerFragment : BaseListFragment<TravelCountryPhoneCode, PhoneCodePickerAdapterTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    lateinit var viewModel: PhoneCodePickerViewModel

    lateinit var searchBarUnify: SearchBarUnify

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            viewModel = viewModelProvider.get(PhoneCodePickerViewModel::class.java)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_phone_code_picker, container, false)
        view.requestFocus()
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBarUnify = view.findViewById(R.id.search_input_view)
        searchBarUnify.searchBarTextField.addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(text: CharSequence?, p1: Int, p2: Int, p3: Int) {
                viewModel.filterCountryList(text.toString().trim())
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        viewModel.filteredCountryList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    isLoadingInitialData = true
                    renderList(it.data)
                }
                is Fail -> {
                    view?.run {
                        Toaster.make(this, ErrorHandler.getErrorMessage(context, it.throwable), Toaster.LENGTH_SHORT, Toaster.TYPE_ERROR, getString(R.string.general_label_ok))
                    }
                }
            }
        })
    }

    override fun getAdapterTypeFactory(): PhoneCodePickerAdapterTypeFactory =
            PhoneCodePickerAdapterTypeFactory()

    override fun onItemClicked(item: TravelCountryPhoneCode) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_PHONE_CODE, item)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun getScreenName(): String = ""

    override fun initInjector() = getComponent(TravelCountryCodeComponent::class.java)
            .inject(this)

    override fun loadData(page: Int) {
        viewModel.getCountryList(TravelCountryCodeGqlQuery.ALL_COUNTRY)
    }

    companion object {
        const val EXTRA_SELECTED_PHONE_CODE = "EXTRA_SELECTED_PHONE_CODE"
    }

}