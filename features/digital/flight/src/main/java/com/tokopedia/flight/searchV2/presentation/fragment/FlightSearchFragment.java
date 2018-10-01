package com.tokopedia.flight.searchV2.presentation.fragment;

import android.os.Bundle;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.flight.FlightComponentInstance;
import com.tokopedia.flight.search.view.model.FlightSearchPassDataViewModel;
import com.tokopedia.flight.searchV2.di.DaggerFlightSearchComponent;
import com.tokopedia.flight.searchV2.di.FlightSearchComponent;
import com.tokopedia.flight.searchV2.presentation.contract.FlightSearchContract;
import com.tokopedia.flight.searchV2.presentation.presenter.FlightSearchPresenter;

import javax.inject.Inject;

import static com.tokopedia.flight.searchV2.presentation.activity.FlightSearchActivity.EXTRA_PASS_DATA;

/**
 * @author by furqan on 01/10/18.
 */

public class FlightSearchFragment extends BaseDaggerFragment
        implements FlightSearchContract.View {

    @Inject
    public FlightSearchPresenter flightSearchPresenter;
    FlightSearchComponent flightSearchComponent;


    FlightSearchPassDataViewModel passDataViewModel;

    public static FlightSearchFragment newInstance(FlightSearchPassDataViewModel passDataViewModel) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASS_DATA, passDataViewModel);

        FlightSearchFragment fragment = new FlightSearchFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        flightSearchComponent = DaggerFlightSearchComponent.builder()
                .flightComponent(FlightComponentInstance.getFlightComponent(getActivity().getApplication()))
                .build();

        flightSearchComponent.inject(this);
    }

    public void loadData() {

    }
}
