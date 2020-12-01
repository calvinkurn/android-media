package com.tokopedia.flight.airport.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.view.activity.FlightAirportPickerActivity
import com.tokopedia.flight.airport.view.adapter.FlightAirportClickListener
import com.tokopedia.flight.airport.view.model.FlightAirportModel
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerContract
import com.tokopedia.flight.airport.view.presenter.FlightAirportPickerPresenterImpl
import com.tokopedia.flight.airportv2.di.DaggerFlightAirportComponent
import com.tokopedia.flight.airportv2.di.FlightAirportModule
import com.tokopedia.flight.airportv2.presentation.adapter.FlightAirportAdapterTypeFactory
import com.tokopedia.flight.common.di.component.FlightComponent
import com.tokopedia.flight.common.view.model.EmptyResultModel
import com.tokopedia.unifycomponents.SearchBarUnify
import javax.inject.Inject

/**
 * @author by jessica on 12/11/20
 */

class FlightAirportPickerFragment: BaseListFragment<Visitable<*>, FlightAirportAdapterTypeFactory>(),
        FlightAirportPickerContract.View, FlightAirportClickListener {

    @Inject
    lateinit var flightAirportPickerPresenter: FlightAirportPickerPresenterImpl
    private var isFirstTime = true

    lateinit var searchBarUnify: SearchBarUnify

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_flight_airport_picker, container, false)
        view.requestFocus()
        return view
    }

    override fun renderList(list: List<Visitable<*>?>) {
        if (isFirstTime) {
            searchBarUnify.visibility = View.VISIBLE
            isFirstTime = false
        }
        super.renderList(list)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        searchBarUnify = view.findViewById(R.id.search_input_view)
        getSearchBarEditText().hint = getString(com.tokopedia.common.travel.R.string.flight_label_search_hint_airport)
        getSearchBarEditText().addTextChangedListener(object : TextWatcher {

            override fun afterTextChanged(p0: Editable?) {}
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (TextUtils.isEmpty(getSearchBarEditText().text.toString())) {
                    searchBarUnify.searchBarIcon.visibility = View.GONE
                } else {
                    searchBarUnify.searchBarIcon.visibility = View.VISIBLE
                }
                flightAirportPickerPresenter.getSuggestionAirport(getSearchBarEditText().text.toString())
            }
        })
    }

    private fun getSearchBarEditText(): EditText = searchBarUnify.searchBarTextField

    override fun getAdapterTypeFactory(): FlightAirportAdapterTypeFactory {
        return FlightAirportAdapterTypeFactory(this)
    }

    override fun getScreenName(): String? = null

    override fun initInjector() {
        DaggerFlightAirportComponent.builder()
                .flightAirportModule(FlightAirportModule())
                .flightComponent(getComponent(FlightComponent::class.java))
                .build()
                .inject(this)
        flightAirportPickerPresenter.attachView(this)
    }

    override fun onItemClicked(t: Visitable<*>?) { }

    override fun onDestroy() {
        super.onDestroy()
        flightAirportPickerPresenter.detachView()
    }

    override fun loadData(page: Int) {
        if (isFirstTime) searchBarUnify.visibility = View.GONE
        flightAirportPickerPresenter.getPopularCityAirport()
    }

    override fun showGetAirportListLoading() {
        showLoading()
    }

    override fun hideGetAirportListLoading() {
        hideLoading()
    }

    override fun showLoading() {
        adapter.setElement(loadingModel)
    }

    override fun airportClicked(airportViewModel: FlightAirportModel?) {
        val intent = Intent()
        intent.putExtra(EXTRA_SELECTED_AIRPORT, airportViewModel)
        activity?.setResult(Activity.RESULT_OK, intent)
        activity?.finish()
    }

    override fun getFilterText(): String {
        return getSearchBarEditText().text.toString()
    }

    override fun createAdapterInstance(): BaseListAdapter<Visitable<*>, FlightAirportAdapterTypeFactory> {
        val adapter = super.createAdapterInstance()
        val errorNetworkModel = adapter.errorNetworkModel
        errorNetworkModel.iconDrawableRes = com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
        errorNetworkModel.errorMessage = getString(R.string.flight_airport_connection_title_error)
        errorNetworkModel.subErrorMessage = getString(R.string.flight_airport_connection_description_error)
        errorNetworkModel.onRetryListener = ErrorNetworkModel.OnRetryListener { loadData(0) }
        adapter.errorNetworkModel = errorNetworkModel
        return adapter
    }

    override fun getEmptyDataViewModel(): Visitable<*> {
        val emptyModel = EmptyResultModel()
        if (getSearchBarEditText().text.toString().length < 3) {
            emptyModel.iconRes = R.drawable.ic_flight_airport_search_not_complete
            emptyModel.title = getString(R.string.flight_airport_less_than_three_keyword_title_error)
            emptyModel.content = getString(R.string.flight_airport_less_than_three_keyword_error)
        } else {
            emptyModel.iconRes = com.tokopedia.globalerror.R.drawable.unify_globalerrors_404
            emptyModel.title = getString(R.string.flight_airport_not_found_title_error)
            emptyModel.content = getString(R.string.flight_airport_not_found_description_error)
        }
        return emptyModel
    }

    override fun getRecyclerViewResourceId(): Int {
        return R.id.recycler_view
    }

    companion object {
        const val EXTRA_SELECTED_AIRPORT = "extra_selected_aiport"

        fun getInstance(searchHint: String): FlightAirportPickerFragment {
            val fragment = FlightAirportPickerFragment()
            val bundle = Bundle()
            bundle.putString(FlightAirportPickerActivity.EXTRA_TOOLBAR_TITLE, searchHint)
            fragment.arguments = bundle
            return fragment
        }
    }

}