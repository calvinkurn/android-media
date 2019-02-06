package com.tokopedia.flight.cancellation.view.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationReasonAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationReasonViewHolder;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationChooseReasonContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationChooseReasonPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonViewModel;

import java.util.ArrayList;

import javax.inject.Inject;

/**
 * @author by furqan on 30/10/18.
 */

public class FlightCancellationChooseReasonFragment extends BaseListFragment<FlightCancellationReasonViewModel, FlightCancellationReasonAdapterTypeFactory>
        implements FlightCancellationReasonViewHolder.ReasonListener, FlightCancellationChooseReasonContract.View{

    public static final String EXTRA_SELECTED_REASON = "EXTRA_SELECTED_REASON";

    @Inject
    FlightCancellationChooseReasonPresenter presenter;

    private ArrayList<FlightCancellationReasonViewModel> reasonList;
    private FlightCancellationReasonViewModel selectedReason;

    public static FlightCancellationChooseReasonFragment createInstance(FlightCancellationReasonViewModel selectedReason) {
        FlightCancellationChooseReasonFragment fragment = new FlightCancellationChooseReasonFragment();

        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SELECTED_REASON, selectedReason);
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Bundle args = (savedInstanceState != null) ? savedInstanceState : getArguments();

        selectedReason = args.getParcelable(EXTRA_SELECTED_REASON);

        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);

        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putParcelable(EXTRA_SELECTED_REASON, selectedReason);
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
    public boolean isItemChecked(FlightCancellationReasonViewModel selectedItem) {
        return selectedReason != null && selectedReason.getDetail()
                .equalsIgnoreCase(selectedItem.getDetail());
    }

    @Override
    public void onItemClicked(FlightCancellationReasonViewModel viewModel) {
        selectedReason = viewModel;
        getAdapter().notifyDataSetChanged();

        selectReason(viewModel);
    }

    @Override
    public void loadData(int page) {
        presenter.getReasonList();
    }

    @Override
    protected FlightCancellationReasonAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightCancellationReasonAdapterTypeFactory(this);
    }

    @Override
    public void renderReasonList(ArrayList<FlightCancellationReasonViewModel> reasonViewModelList) {
        reasonList = reasonViewModelList;
        renderList(reasonList);
    }

    private void selectReason(FlightCancellationReasonViewModel selectedReason) {
        Intent intent = new Intent();
        intent.putExtra(EXTRA_SELECTED_REASON, selectedReason);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }
}
