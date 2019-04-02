package com.tokopedia.common.travel.presentation.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerListActivity;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerUpdateActivity;
import com.tokopedia.common.travel.presentation.adapter.TravelPassengerListAdapter;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerListContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.presentation.model.TravelTrip;
import com.tokopedia.common.travel.presentation.presenter.TravelPassengerListPresenter;
import com.tokopedia.common.travel.utils.CommonTravelUtils;
import com.tokopedia.design.base.BaseToaster;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.design.component.ToasterNormal;
import com.tokopedia.graphql.data.GraphqlClient;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 23/10/18.
 */
public class TravelPassengerListFragment extends BaseDaggerFragment
        implements TravelPassengerListContract.View {

    private static final int ADD_PASSENGER_REQUEST_CODE = 175;
    private static final int EDIT_PASSENGER_REQUEST_CODE = 176;

    private LinearLayout addNewPassengerLayout;
    private RecyclerView passengerListRecyclerView;
    private TravelPassengerListAdapter adapter;
    private ActionListener listener;
    private TravelPassenger passengerBooking;
    private ProgressBar progressBar;
    private LinearLayout layoutPassngerList;
    private boolean resetPassengerListSelected;
    private TravelTrip travelTrip;

    @Inject
    TravelPassengerListPresenter presenter;

    @Override
    protected void initInjector() {
        GraphqlClient.init(getActivity());
        CommonTravelComponent commonTravelComponent = CommonTravelUtils.getTrainComponent(getActivity().getApplication());
        commonTravelComponent.inject(this);
        presenter.attachView(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    public static Fragment newInstance(TravelTrip travelTrip,
                                       boolean resetPassengerListSelected) {
        Fragment fragment = new TravelPassengerListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TravelPassengerListActivity.TRAVEL_TRIP, travelTrip);
        bundle.putBoolean(TravelPassengerListActivity.RESET_PASSENGER_LIST_SELECTED, resetPassengerListSelected);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_passenger_list, container, false);
        addNewPassengerLayout = view.findViewById(R.id.add_new_passenger_layout);
        passengerListRecyclerView = view.findViewById(R.id.recycler_view_passenger);
        progressBar = view.findViewById(R.id.progress_bar);
        layoutPassngerList = view.findViewById(R.id.layout_passenger_list);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getDataFromBundle();
        initRecyclerView();
        setActionListener();
    }

    private void getDataFromBundle() {
        travelTrip = getArguments().getParcelable(TravelPassengerListActivity.TRAVEL_TRIP);
        resetPassengerListSelected = getArguments().getBoolean(TravelPassengerListActivity.RESET_PASSENGER_LIST_SELECTED);
        passengerBooking = travelTrip.getTravelPassengerBooking();
    }

    private void setActionListener() {
        addNewPassengerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(TravelPassengerUpdateActivity.callingIntent(getActivity(),
                        travelTrip, TravelPassengerUpdateActivity.ADD_PASSENGER_TYPE), ADD_PASSENGER_REQUEST_CODE);
            }
        });
    }

    private void initRecyclerView() {
        adapter = new TravelPassengerListAdapter(passengerBooking);
        adapter.setListener(new TravelPassengerListAdapter.ActionListener() {
            @Override
            public void onClickChoosePassenger(TravelPassenger travelPassenger) {
                presenter.selectPassenger(passengerBooking, travelPassenger);
            }
        });
        passengerListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        passengerListRecyclerView.setAdapter(adapter);

    }

    @SuppressWarnings("Range")
    @Override
    public void showMessageErrorInSnackBar(int resId) {
        ToasterError.showClose(getActivity(), getString(resId));
    }

    @Override
    public void showActionErrorInSnackBar(TravelPassenger travelPassenger, int resId) {
        ToasterError.make(getActivity().findViewById(android.R.id.content),
                getString(resId), BaseToaster.LENGTH_LONG)
                .setAction(getActivity().getString(R.string.travel_action_snackbar_error_edit), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        travelTrip.setTravelPassengerBooking(travelPassenger);
                        startActivityForResult(TravelPassengerUpdateActivity.callingIntent(getActivity(), travelTrip,
                                TravelPassengerUpdateActivity.EDIT_PASSENGER_TYPE), EDIT_PASSENGER_REQUEST_CODE);
                    }
                }).show();
    }

    @Override
    public void showMessageErrorInSnackBar(Throwable throwable) {
        String errorMessage = ErrorHandler.getErrorMessage(getActivity(), throwable);
        ToasterError.showClose(getActivity(), errorMessage);
    }

    @Override
    public void renderPassengerList(List<TravelPassenger> travelPassengerList) {
        adapter.addAll(travelPassengerList);
    }

    @Override
    public void updatePassengerSelected(TravelPassenger travelPassengerSelected) {
        listener.onBackPressedActivity(travelPassengerSelected);
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.VISIBLE);
            layoutPassngerList.setVisibility(View.GONE);
        }
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) {
            progressBar.setVisibility(View.GONE);
            layoutPassngerList.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClickSelectPassenger(TravelPassenger travelPassenger) {
        listener.onClickPassenger(travelPassenger);
        presenter.updatePassenger(passengerBooking.getIdPassenger(), false);
        presenter.updatePassenger(travelPassenger.getIdPassenger(), true);
    }

    @Override
    public void successUpdatePassengerDb() {

    }

    @Override
    public void failedUpdatePassengerDb() {

    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.getPassengerList(resetPassengerListSelected, travelTrip.getTravelPassengerBooking().getIdLocal(),
                travelTrip.getTravelPassengerBooking().getIdPassenger());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == ADD_PASSENGER_REQUEST_CODE) {
                ToasterNormal.show(getActivity(), getString(R.string.travel_success_add_passenger_msg));
            } else if (requestCode == EDIT_PASSENGER_REQUEST_CODE) {
                ToasterNormal.show(getActivity(), getString(R.string.travel_success_edit_passenger_msg));
            }
            resetPassengerListSelected = false;
        }
    }

    @Override
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActionListener) {
            listener = (ActionListener) context;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }

    public interface ActionListener {
        void onClickPassenger(TravelPassenger travelPassenger);

        void onBackPressedActivity(TravelPassenger travelPassenger);
    }
}
