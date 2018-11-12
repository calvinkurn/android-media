package com.tokopedia.common.travel.presentation;

import android.content.Context;
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
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.adapter.TravelPassengerListAdapter;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.utils.CommonTravelUtils;
import com.tokopedia.graphql.data.GraphqlClient;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 23/10/18.
 */
public class TravelPassengerBookingListFragment extends BaseDaggerFragment
        implements TravelPassengerBookingListContract.View {

    private LinearLayout addNewPassengerLayout;
    private RecyclerView passengerListRecyclerView;
    private TravelPassengerListAdapter adapter;
    private ActionListener listener;
    private TravelPassenger passengerSelected;
    private ProgressBar progressBar;
    private LinearLayout layoutPassngerList;
    private boolean resetPassengerListSelected;

    @Inject
    TravelPassengerBookingListPresenter presenter;

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

    public static Fragment newInstance(TravelPassenger travelPassenger, boolean resetPassengerListSelected) {
        Fragment fragment = new TravelPassengerBookingListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TravelPassengerBookingListActivity.PASSENGER_DATA, travelPassenger);
        bundle.putBoolean(TravelPassengerBookingListActivity.RESET_PASSENGER_LIST_SELECTED, resetPassengerListSelected);
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

        passengerSelected = getArguments().getParcelable(TravelPassengerBookingListActivity.PASSENGER_DATA);
        resetPassengerListSelected = getArguments().getBoolean(TravelPassengerBookingListActivity.RESET_PASSENGER_LIST_SELECTED);

        initRecyclerView();
        presenter.getPassengerList(resetPassengerListSelected);

        addNewPassengerLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initRecyclerView() {
        adapter = new TravelPassengerListAdapter(passengerSelected);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(passengerListRecyclerView.getContext());
        dividerItemDecoration.setUsePaddingLeft(true);
        passengerListRecyclerView.addItemDecoration(dividerItemDecoration);
        adapter.setListener(new TravelPassengerListAdapter.ActionListener() {
            @Override
            public void onClickChoosePassenger(TravelPassenger travelPassenger) {
                travelPassenger.setIdLocal(passengerSelected.getIdLocal());
                listener.onClickPassenger(travelPassenger);
            }

            @Override
            public void onUpdatePassenger(String idPassenger, boolean isSelected) {
                presenter.updatePassenger(idPassenger, isSelected);
            }

            @Override
            public void onShowErrorCantPickPassenger() {
                showMessageErrorInSnackBar(R.string.error_message_choose_passenger);
            }
        });
        passengerListRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        passengerListRecyclerView.setAdapter(adapter);

    }

    @SuppressWarnings("Range")
    @Override
    public void showMessageErrorInSnackBar(int resId) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showMessageErrorInSnackBar(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
    }

    @Override
    public void navigateToBookingPassenger(TravelPassenger travelPassenger) {

    }

    @Override
    public void renderPassengerList(List<TravelPassenger> travelPassengerList) {
        adapter.addAll(travelPassengerList);
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
    protected void onAttachActivity(Context context) {
        super.onAttachActivity(context);
        if (context instanceof ActionListener) {
            listener = (ActionListener) context;
        }
    }

    public interface ActionListener {
        void onClickPassenger(TravelPassenger travelPassenger);
    }
}
