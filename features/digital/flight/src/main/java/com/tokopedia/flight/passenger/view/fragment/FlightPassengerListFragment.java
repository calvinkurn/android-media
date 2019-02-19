package com.tokopedia.flight.passenger.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.tokopedia.abstraction.base.view.adapter.Visitable;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.flight.R;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingNewPassengerViewModel;
import com.tokopedia.flight.booking.view.viewmodel.FlightBookingPassengerViewModel;
import com.tokopedia.flight.passenger.di.FlightPassengerComponent;
import com.tokopedia.flight.passenger.view.activity.FlightPassengerUpdateActivity;
import com.tokopedia.flight.passenger.view.adapter.FlightPassengerListAdapterTypeFactory;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightPassengerListViewHolder;
import com.tokopedia.flight.passenger.view.adapter.viewholder.FlightPassengerNewViewHolder;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerListContract;
import com.tokopedia.flight.passenger.view.presenter.FlightPassengerListPresenter;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * @author by furqan on 26/02/18.
 */

public class FlightPassengerListFragment extends BaseListFragment<FlightBookingPassengerViewModel, FlightPassengerListAdapterTypeFactory>
        implements FlightPassengerListViewHolder.ListenerCheckedSavedPassenger, FlightPassengerNewViewHolder.ListenerClickedNewPassenger,
        FlightPassengerListContract.View {

    public static final int REQUEST_EDIT_PASSENGER_CODE = 1;
    public static final String EXTRA_SELECTED_PASSENGER = "EXTRA_SELECTED_PASSENGER";
    public static final String EXTRA_REQUEST_ID = "EXTRA_REQUEST_ID";
    public static final String EXTRA_DEPARTURE_DATE = "EXTRA_DEPARTURE_DATE";
    public static final String EXTRA_IS_DOMESTIC = "EXTRA_IS_DOMESTIC";
    private static final String PASSENGER_IDENTITY_TRACE = "tr_flight_fill_passenger_identity";
    public static final int IS_SELECTING = 1;
    public static final int IS_NOT_SELECTING = 0;

    private String selectedPassengerId;
    private String requestId;
    private String departureDate;
    private boolean isDomestic;
    private FlightBookingPassengerViewModel selectedPassenger;
    @Inject
    FlightPassengerListPresenter presenter;
    List<FlightBookingPassengerViewModel> flightBookingPassengerViewModelList;

    private PerformanceMonitoring performanceMonitoring;
    private boolean isTraceStop = false;

    public static FlightPassengerListFragment createInstance(FlightBookingPassengerViewModel selectedPassenger,
                                                             String requestId, String departureDate, boolean isDomestic) {
        FlightPassengerListFragment flightPassengerListFragment = new FlightPassengerListFragment();
        Bundle bundle = new Bundle();
        bundle.putParcelable(EXTRA_SELECTED_PASSENGER, selectedPassenger);
        bundle.putString(EXTRA_REQUEST_ID, requestId);
        bundle.putString(EXTRA_DEPARTURE_DATE, departureDate);
        bundle.putBoolean(EXTRA_IS_DOMESTIC, isDomestic);
        flightPassengerListFragment.setArguments(bundle);
        return flightPassengerListFragment;
    }

    public FlightPassengerListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        selectedPassenger = getArguments().getParcelable(EXTRA_SELECTED_PASSENGER);
        requestId = getArguments().getString(EXTRA_REQUEST_ID);
        departureDate = getArguments().getString(EXTRA_DEPARTURE_DATE);
        isDomestic = getArguments().getBoolean(EXTRA_IS_DOMESTIC);
        selectedPassengerId = (selectedPassenger.getPassengerId() != null) ? selectedPassenger.getPassengerId() : "";
        flightBookingPassengerViewModelList = new ArrayList<>();

        performanceMonitoring = PerformanceMonitoring.start(PASSENGER_IDENTITY_TRACE);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_passenger_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        presenter.attachView(this);
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onDestroy() {
        presenter.onDestroyView();
        super.onDestroy();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(FlightPassengerComponent.class).inject(this);
    }

    @Override
    public void onItemClicked(FlightBookingPassengerViewModel flightBookingPassengerViewModel) {
        presenter.selectPassenger(flightBookingPassengerViewModel);
    }

    @Override
    public void loadData(int page) {
        getAdapter().clearAllElements();
        showLoading();
        presenter.getSavedPassengerList();
    }

    @Override
    protected FlightPassengerListAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightPassengerListAdapterTypeFactory(this, this);
    }

    @Override
    public List<FlightBookingPassengerViewModel> getPassengerViewModelList() {
        return flightBookingPassengerViewModelList;
    }

    @Override
    public void setPassengerViewModelList(List<FlightBookingPassengerViewModel> passengerViewModelList) {
        this.flightBookingPassengerViewModelList = passengerViewModelList;
    }

    @Override
    public void renderPassengerList() {
        hideLoading();
        super.isLoadingInitialData = true;
        renderList(flightBookingPassengerViewModelList, false);

        if (flightBookingPassengerViewModelList.size() > 0) {
            addNewPassengerElement();
        }

        stopTrace();
    }

    @Override
    public FlightBookingPassengerViewModel getCurrentPassenger() {
        return selectedPassenger;
    }

    @Override
    public void setCurrentPassanger(FlightBookingPassengerViewModel selectedPassenger) {
        this.selectedPassenger = selectedPassenger;
    }

    @Override
    public String getSalutationString(int resId) {
        return getString(resId);
    }

    @Override
    public void onSelectPassengerSuccess(FlightBookingPassengerViewModel selectedPassenger) {
        if (selectedPassenger != null) {
            Intent intent = new Intent();
            intent.putExtra(EXTRA_SELECTED_PASSENGER, selectedPassenger);
            getActivity().setResult(RESULT_OK, intent);
        } else {
            getActivity().setResult(RESULT_OK, null);
        }
        getActivity().finish();
    }

    @Override
    public String getSelectedPassengerId() {
        return selectedPassengerId;
    }

    @Override
    public String getRequestId() {
        return requestId;
    }

    @Override
    public void onGetListError(Throwable throwable) {
        getAdapter().clearAllElements();
        super.showGetListError(throwable);
    }

    @Override
    public String getDepartureDate() {
        return departureDate;
    }

    private void onSelectNewPassenger() {
        presenter.selectPassenger(null);
    }

    @Override
    public void deletePassenger(String passengerId) {
        showDeleteDialog(passengerId);
    }

    @Override
    public void editPassenger(FlightBookingPassengerViewModel passengerViewModel) {
        startActivityForResult(
                FlightPassengerUpdateActivity.getCallingIntent(getActivity(),
                        passengerViewModel, departureDate, requestId, isDomestic),
                REQUEST_EDIT_PASSENGER_CODE
        );
    }

    private void addNewPassengerElement() {
        getAdapter().addElement(new FlightBookingNewPassengerViewModel(
                getString(R.string.flight_list_passenger_add_passenger_label)
        ));

        getAdapter().notifyDataSetChanged();
    }

    @Override
    public View.OnClickListener onNewPassengerClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onSelectNewPassenger();
            }
        };
    }

    @Override
    protected Visitable getEmptyDataViewModel() {
        return new FlightBookingNewPassengerViewModel(
                getString(R.string.flight_list_passenger_add_passenger_label)
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_EDIT_PASSENGER_CODE:
                if (resultCode == RESULT_OK) {
                    presenter.onSuccessUpdatePassengerData();
                }
                break;
        }
    }

    @Override
    public void showSuccessChangePassengerData() {
        showSuccessSnackbar(R.string.flight_passenger_update_success);
    }

    @Override
    public void onSuccessDeletePassengerData() {
        showSuccessSnackbar(R.string.flight_passenger_delete_success);
    }

    @Override
    public void stopTrace() {
        if (!isTraceStop) {
            performanceMonitoring.stopTrace();
            isTraceStop = true;
        }
    }

    private void showSuccessSnackbar(int resId) {
        NetworkErrorHelper.showGreenCloseSnackbar(getActivity(),
                getString(resId));
    }

    private void showDeleteDialog(final String passengerId) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.flight_passenger_delete_dialog_title));
        dialog.setDesc(getString(R.string.flight_passenger_delete_dialog_description));
        dialog.setBtnOk("Hapus");
        dialog.setBtnCancel("Batal");
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.deletePassenger(passengerId);
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

}
