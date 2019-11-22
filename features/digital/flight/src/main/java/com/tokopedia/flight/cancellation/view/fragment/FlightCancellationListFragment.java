package com.tokopedia.flight.cancellation.view.fragment;

import android.os.Bundle;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationDetailActivity;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationListAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationListContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationListPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationListViewModel;

import java.util.List;

import javax.inject.Inject;

/**
 * @author by furqan on 30/04/18.
 */

public class FlightCancellationListFragment extends BaseListFragment<FlightCancellationListViewModel, FlightCancellationListAdapterTypeFactory>
        implements FlightCancellationListContract.View {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";

    private List<FlightCancellationListViewModel> cancellationListViewModelList;
    private String invoiceId;

    @Inject
    FlightCancellationListPresenter presenter;

    public static FlightCancellationListFragment createInstance(String invoiceId) {
        FlightCancellationListFragment fragment = new FlightCancellationListFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_cancellation_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        invoiceId = getArguments().getString(EXTRA_INVOICE_ID);

        presenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightCancellationComponent.class).inject(this);
    }

    @Override
    public void onItemClicked(FlightCancellationListViewModel flightCancellationListViewModel) {
        navigateToDetailPage(flightCancellationListViewModel);
    }

    @Override
    public void loadData(int page) {
        getAdapter().clearAllElements();
        presenter.onViewCreated();
    }

    @Override
    protected FlightCancellationListAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightCancellationListAdapterTypeFactory();
    }

    @Override
    public void navigateToDetailPage(FlightCancellationListViewModel passData) {
        startActivity(FlightCancellationDetailActivity.createIntent(getContext(), passData));
    }

    @Override
    public void renderList() {
        renderList(cancellationListViewModelList);
    }

    @Override
    public List<FlightCancellationListViewModel> getFlightCancellationList() {
        return cancellationListViewModelList;
    }

    @Override
    public String getInvoiceId() {
        return invoiceId;
    }

    @Override
    public void setFlightCancellationList(List<FlightCancellationListViewModel> cancellationList) {
        this.cancellationListViewModelList = cancellationList;
    }

    @Override
    public int getRecyclerViewResourceId() {
        return R.id.recycler_view;
    }
}
