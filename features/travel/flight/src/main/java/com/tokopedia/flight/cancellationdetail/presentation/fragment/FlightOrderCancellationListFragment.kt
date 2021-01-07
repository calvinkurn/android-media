package com.tokopedia.flight.cancellationdetail.presentation.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellationdetail.presentation.activity.FlightOrderCancellationListActivity.Companion.EXTRA_INVOICE_ID
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationListAdapterTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.cancellationdetail.presentation.viewmodel.FlightOrderCancellationListViewModel
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import javax.inject.Inject

/**
 * @author by furqan on 06/01/2021
 */
class FlightOrderCancellationListFragment : BaseListFragment<FlightOrderCancellationListModel, FlightOrderCancellationListAdapterTypeFactory>() {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private lateinit var flightOrderCancellationViewModel: FlightOrderCancellationListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewModelProvider = ViewModelProviders.of(this, viewModelFactory)
        flightOrderCancellationViewModel = viewModelProvider.get(FlightOrderCancellationListViewModel::class.java)
        flightOrderCancellationViewModel.orderId = arguments?.getString(EXTRA_INVOICE_ID) ?: ""
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
            inflater.inflate(R.layout.fragment_flight_cancellation_list, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        flightOrderCancellationViewModel.cancellationList.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    renderList(it.data)
                }
                is Fail -> {

                }
            }
        })
    }

    override fun getRecyclerViewResourceId(): Int =
            R.id.rvFlightOrderCancellationList

    override fun getAdapterTypeFactory(): FlightOrderCancellationListAdapterTypeFactory =
            FlightOrderCancellationListAdapterTypeFactory()

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(FlightOrderDetailComponent::class.java).inject(this)
    }

    override fun onItemClicked(data: FlightOrderCancellationListModel) {
        navigateToDetailPage(data)
    }

    override fun loadData(page: Int) {
        adapter.clearAllElements()
        flightOrderCancellationViewModel.fetchCancellationData()
    }

    private fun navigateToDetailPage(cancellationListModel: FlightOrderCancellationListModel) {

    }

    companion object {
        fun getInstance(invoiceId: String): FlightOrderCancellationListFragment =
                FlightOrderCancellationListFragment().also {
                    it.arguments = Bundle().apply {
                        putString(EXTRA_INVOICE_ID, invoiceId)
                    }
                }
    }
}