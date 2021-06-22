package com.tokopedia.flight.airport.presentation.bottomsheet

import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.flight.FlightComponentInstance
import com.tokopedia.flight.R
import com.tokopedia.flight.airport.di.DaggerFlightAirportComponent
import com.tokopedia.flight.airport.di.FlightAirportComponent
import com.tokopedia.flight.airport.presentation.adapter.FlightAirportAdapterTypeFactory
import com.tokopedia.flight.airport.presentation.adapter.viewholder.FlightAirportClickListener
import com.tokopedia.flight.airport.presentation.model.FlightAirportModel
import com.tokopedia.flight.airport.presentation.viewmodel.FlightAirportPickerViewModel
import com.tokopedia.flight.common.view.model.EmptyResultModel
import com.tokopedia.flight.search.presentation.adapter.viewholder.EmptyResultViewHolder
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_flight_airport_picker.*
import kotlinx.android.synthetic.main.bottom_sheet_flight_airport_picker.view.*
import java.util.*
import java.util.concurrent.TimeUnit
import javax.inject.Inject

/**
 * @author by furqan on 19/05/2020
 */
class FlightAirportPickerBottomSheet : BottomSheetUnify(),
        FlightAirportClickListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightAirportPickerViewModel: FlightAirportPickerViewModel
    private lateinit var flightAirportComponent: FlightAirportComponent
    private lateinit var flightAirportAdapter: BaseAdapter<FlightAirportAdapterTypeFactory>

    private lateinit var mChildView: View

    lateinit var listener: Listener

    private val emptyResultModel: EmptyResultModel
        get() {
            val emptyModel = EmptyResultModel()
            if (getFilterText().length < 3) {
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

    private val errorResultModel: EmptyResultModel
        get() {
            val emptyModel = EmptyResultModel()
            emptyModel.iconRes = com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection
            emptyModel.title = getString(R.string.flight_airport_connection_title_error)
            emptyModel.content = getString(R.string.flight_airport_connection_description_error)
            emptyModel.buttonTitle = getString(R.string.flight_booking_action_retry)
            emptyModel.callback = object : EmptyResultViewHolder.Callback {
                override fun onEmptyButtonClicked() {
                    onSearchAirportTextChanged(getFilterText())
                }
            }
            return emptyModel
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initInjector()
        activity?.run {
            val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
            flightAirportPickerViewModel = viewModelProvider.get(FlightAirportPickerViewModel::class.java)
        }

        initBottomSheet()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetHeader.setPadding(
                resources.getDimensionPixelSize(com.tokopedia.unifyprinciples.R.dimen.layout_lvl2),
                bottomSheetHeader.top,
                bottomSheetWrapper.right,
                bottomSheetHeader.bottom)
        bottomSheetWrapper.setPadding(0,
                bottomSheetWrapper.paddingTop,
                0,
                bottomSheetWrapper.paddingBottom)
        bottomSheetWrapper.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)

        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        flightAirportPickerViewModel.airportList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {
            hideLoading()
            when (it) {
                is Success -> {
                    renderList(it.data)
                }
                is Fail -> {
                    showError()
                }
            }
        })

        flightAirportPickerViewModel.fetchAirport(getFilterText())
    }

    override fun airportClicked(airportViewModel: FlightAirportModel) {
        if (::listener.isInitialized) {
            listener.onAirportSelected(airportViewModel)
            dismiss()
        }
    }

    override fun getFilterText(): String = mChildView.flightAirportSearchBar.searchBarTextField.text.toString()

    private fun initInjector() {
        activity?.let {
            if (!::flightAirportComponent.isInitialized) {
                flightAirportComponent = DaggerFlightAirportComponent.builder()
                        .flightComponent(FlightComponentInstance.getFlightComponent(it.application))
                        .build()
            }
            flightAirportComponent.inject(this)
        }
    }

    private fun initBottomSheet() {
        showCloseIcon = true
        isFullpage = true
        setTitle(getString(R.string.flight_search_airport_arrival_title))

        mChildView = View.inflate(requireContext(), R.layout.bottom_sheet_flight_airport_picker, null)
        setChild(mChildView)
    }

    private fun initView() {
        flightAirportSearchBar.searchBarTextField.setOnEditorActionListener { textView, actionId, keyEvent ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                flightAirportSearchBar.clearFocus()
                onSearchAirportTextChanged(getFilterText())
                true
            } else {
                false
            }
        }
        flightAirportSearchBar.searchBarTextField.addTextChangedListener(getSearchTextWatcher())

        createAdapterInstance()
        rvFlightAirport.layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        rvFlightAirport.adapter = flightAirportAdapter
        showLoading()
    }

    private fun showLoading() {
        flightAirportAdapter.clearAllElements()
        flightAirportAdapter.showLoading()
    }

    private fun hideLoading() {
        flightAirportAdapter.hideLoading()
    }

    private fun renderList(visitableList: List<Visitable<*>>) {
        hideLoading()
        flightAirportAdapter.clearAllElements()
        if (visitableList.isNotEmpty()) {
            flightAirportAdapter.addElement(visitableList)
        } else {
            flightAirportAdapter.addElement(emptyResultModel)
        }
    }

    private fun showError() {
        flightAirportAdapter.clearAllElements()
        flightAirportAdapter.addElement(errorResultModel)
    }

    private fun onSearchAirportTextChanged(airportKeyword: String) {
        flightAirportPickerViewModel.fetchAirport(airportKeyword)
        showLoading()
    }

    private fun createAdapterInstance() {
        val adapterTypeFactory = FlightAirportAdapterTypeFactory(this)
        flightAirportAdapter = BaseAdapter(adapterTypeFactory)
    }

    private fun getSearchTextWatcher(): TextWatcher =
            object : TextWatcher {
                var timer: Timer = Timer()

                override fun afterTextChanged(editable: Editable) {
                    runTimer(editable.toString())
                }

                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    timer.cancel()
                }

                private fun runTimer(keyword: String) {
                    timer = Timer()
                    timer.schedule(object : TimerTask() {
                        override fun run() {
                            updateListener(keyword)
                        }
                    }, DEFAULT_DELAY_TEXT_CHANGED)
                }

                private fun updateListener(keyword: String) {
                    val mainHandler = Handler(mChildView.flightAirportSearchBar.searchBarTextField.context.mainLooper)
                    val myRunnable = Runnable {
                        onSearchAirportTextChanged(keyword)
                    }
                    mainHandler.post(myRunnable)
                }
            }

    interface Listener {
        fun onAirportSelected(selectedAirport: FlightAirportModel)
    }

    companion object {
        const val TAG_FLIGHT_AIRPORT_PICKER = "TAG_FLIGHT_AIRPORT_PICKER"

        private val DEFAULT_DELAY_TEXT_CHANGED = TimeUnit.MILLISECONDS.toMillis(300)

        fun getInstance(): FlightAirportPickerBottomSheet =
                FlightAirportPickerBottomSheet()
    }

}