package com.tokopedia.common.travel.presentation.fragment;

import android.app.Activity;
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
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.network.ErrorHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerEditActivity;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerUpdateActivity;
import com.tokopedia.common.travel.presentation.adapter.TravelPassengerEditAdapter;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerEditContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.presentation.model.TravelTrip;
import com.tokopedia.common.travel.presentation.presenter.TravelPassengerEditPresenter;
import com.tokopedia.common.travel.utils.CommonTravelUtils;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.design.component.ToasterError;
import com.tokopedia.graphql.data.GraphqlClient;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class TravelPassengerEditFragment extends BaseDaggerFragment implements TravelPassengerEditContract.View {

    private static final int EDIT_PASSENGER_REQUEST_CODE = 177;

    private RecyclerView recyclerViewPassenger;
    private ProgressBar progressBar;
    private TravelPassengerEditAdapter adapter;
    private TravelTrip travelTrip;
    private Dialog dialog;

    @Inject
    TravelPassengerEditPresenter presenter;

    public static Fragment newInstance(TravelTrip travelTrip) {
        Fragment fragment = new TravelPassengerEditFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(TravelPassengerEditActivity.TRAVEL_TRIP, travelTrip);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_travel_passenger_edit, container, false);
        recyclerViewPassenger = view.findViewById(R.id.recycler_view_passenger);
        progressBar = view.findViewById(R.id.progress_bar);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        travelTrip = getArguments().getParcelable(TravelPassengerEditActivity.TRAVEL_TRIP);

        renderPassengerList();
        presenter.getPassengerList();
    }

    private void renderPassengerList() {
        adapter = new TravelPassengerEditAdapter(travelTrip.getTravelPassengerBooking());
        adapter.setListener(new TravelPassengerEditAdapter.ActionListener() {
            @Override
            public void onEditPassenger(TravelPassenger travelPassenger) {
                travelTrip.setTravelPassengerBooking(travelPassenger);
                startActivityForResult(TravelPassengerUpdateActivity.callingIntent(getActivity(), travelTrip,
                        TravelPassengerUpdateActivity.EDIT_PASSENGER_TYPE), EDIT_PASSENGER_REQUEST_CODE);
            }

            @Override
            public void onDeletePassenger(String idPassenger, String id, int travelId) {
                showDeleteDialog(idPassenger, id, travelId);
            }
        });
        recyclerViewPassenger.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewPassenger.setAdapter(adapter);
    }

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

    @Override
    public void showMessageErrorInSnackBar(int resId) {
        ToasterError.showClose(getActivity(), getString(resId));
    }

    @Override
    public void showMessageErrorInSnackBar(String message) {
        ToasterError.showClose(getActivity(), message);
    }

    @Override
    public void showMessageErrorInSnackBar(Throwable throwable) {
        String message = ErrorHandler.getErrorMessage(getActivity(), throwable);
        ToasterError.showClose(getActivity(), message);
    }

    @Override
    public void showSuccessSnackbar(String message) {
        NetworkErrorHelper.showGreenSnackbar(getActivity(), message);
    }

    @Override
    public void navigateToBookingPassenger(TravelPassenger trainPassengerViewModel) {

    }

    @Override
    public void renderPassengerList(List<TravelPassenger> travelPassengerList) {
        adapter.addAll(travelPassengerList);
    }

    @Override
    public void showProgressBar() {
        if (progressBar != null) progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgressBar() {
        if (progressBar != null) progressBar.setVisibility(View.GONE);
    }

    @Override
    public int getTravelPlatformType() {
        return travelTrip.getTravelPlatformType();
    }

    @Override
    public void successDeletePassenger() {
        showSuccessSnackbar(getString(R.string.travel_snackbar_msg_success_delete));
        presenter.getPassengerList();
    }

    @Override
    public TravelPassenger getTravelPassengerBooking() {
        return travelTrip.getTravelPassengerBooking();
    }

    private void showDeleteDialog(String idPassenger, String id, int travelId) {
        dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.travel_dialog_delete_passenger_title));
        dialog.setDesc(getString(R.string.travel_dialog_delete_passenger_question));
        dialog.setBtnOk(getString(R.string.travel_dialog_delete_passenger_btn_delete));
        dialog.setBtnCancel(getString(R.string.travel_dialog_delete_passenger_btn_cancel));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deletePassenger(idPassenger, id, travelId);
                dialog.dismiss();
            }
        });
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == EDIT_PASSENGER_REQUEST_CODE) {
                NetworkErrorHelper.showGreenCloseSnackbar(getActivity(), getString(R.string.travel_success_edit_passenger_msg));
            }
            presenter.getPassengerList();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDestroyView();
    }
}
