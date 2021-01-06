package com.tokopedia.flight.cancellationdetail.presentation.fragment

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationListFragment.EXTRA_INVOICE_ID
import com.tokopedia.flight.cancellationdetail.presentation.adapter.FlightOrderCancellationListAdapterTypeFactory
import com.tokopedia.flight.cancellationdetail.presentation.model.FlightOrderCancellationListModel
import com.tokopedia.flight.cancellationdetail.presentation.viewmodel.FlightOrderCancellationListViewModel
import com.tokopedia.flight.orderdetail.di.FlightOrderDetailComponent
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