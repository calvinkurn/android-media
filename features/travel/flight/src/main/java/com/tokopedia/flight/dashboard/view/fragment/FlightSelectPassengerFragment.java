package com.tokopedia.flight.dashboard.view.fragment;


import android.content.Context;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.travel.widget.SelectPassengerView;
import com.tokopedia.flight.dashboard.di.FlightDashboardComponent;
import com.tokopedia.flight.dashboard.view.fragment.model.FlightPassengerModel;
import com.tokopedia.flight.dashboard.view.presenter.FlightSelectPassengerPresenterImpl;
import com.tokopedia.flight.dashboard.view.presenter.FlightSelectPassengerView;

import javax.inject.Inject;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightSelectPassengerFragment extends BaseDaggerFragment implements FlightSelectPassengerView {

    private static final String EXTRA_PASS_DATA = "EXTRA_PASS_DATA";
    @Inject
    FlightSelectPassengerPresenterImpl presenter;
    private SelectPassengerView adultPassengerView;
    private SelectPassengerView childrenPassengerView;
    private SelectPassengerView infantPassengerView;
    private Button saveButton;
    private FlightPassengerModel passData;
    private OnFragmentInteractionListener interactionListener;

    public FlightSelectPassengerFragment() {
        // Required empty public constructor
    }

    public static FlightSelectPassengerFragment newInstance(FlightPassengerModel passData) {
        FlightSelectPassengerFragment fragment = new FlightSelectPassengerFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_PASS_DATA, passData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.flight.R.layout.fragment_select_flight_passenger, container, false);
        adultPassengerView = (SelectPassengerView) view.findViewById(com.tokopedia.flight.R.id.spv_passenger_adult);
        childrenPassengerView = (SelectPassengerView) view.findViewById(com.tokopedia.flight.R.id.spv_passenger_children);
        infantPassengerView = (SelectPassengerView) view.findViewById(com.tokopedia.flight.R.id.spv_passenger_infant);
        saveButton = (Button) view.findViewById(com.tokopedia.flight.R.id.button_save);

        adultPassengerView.setMinimalPassenger(1);
        adultPassengerView.setOnPassengerCountChangeListener(new SelectPassengerView.OnPassengerCountChangeListener() {
            @Override
            public boolean onChange(int number) {
                presenter.onAdultPassengerCountChange(number);
                return true;
            }
        });

        childrenPassengerView.setOnPassengerCountChangeListener(new SelectPassengerView.OnPassengerCountChangeListener() {
            @Override
            public boolean onChange(int number) {
                presenter.onChildrenPassengerCountChange(number);
                return true;
            }
        });

        infantPassengerView.setOnPassengerCountChangeListener(new SelectPassengerView.OnPassengerCountChangeListener() {
            @Override
            public boolean onChange(int number) {
                presenter.onInfantPassengerCountChange(number);
                return true;
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onSaveButtonClicked();
            }
        });
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        passData = getArguments().getParcelable(EXTRA_PASS_DATA);
        presenter.initialize();
    }

    @Override
    protected void initInjector() {
        getComponent(FlightDashboardComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void onAttachActivity(Context context) {
        if (context instanceof OnFragmentInteractionListener) {
            interactionListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException("Activity must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public FlightPassengerModel getCurrentPassengerViewModel() {
        return passData;
    }

    @Override
    public void showTotalPassengerErrorMessage(@StringRes int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showInfantGreaterThanAdultErrorMessage(@StringRes int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showInfantMoreThanFourErrorMessage(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showAdultShouldAtleastOneErrorMessage(@StringRes int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void renderPassengerView(FlightPassengerModel passengerPassData) {
        this.passData = passengerPassData;
        adultPassengerView.setValue(passengerPassData.getAdult());
        childrenPassengerView.setValue(passengerPassData.getChildren());
        infantPassengerView.setValue(passengerPassData.getInfant());
    }

    @Override
    public void actionNavigateBack(FlightPassengerModel currentPassengerPassData) {
        if (interactionListener != null) {
            interactionListener.actionSavePassenger(passData);
        }
    }

    public interface OnFragmentInteractionListener {
        void actionSavePassenger(FlightPassengerModel passData);
    }
}
