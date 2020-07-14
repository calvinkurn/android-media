package com.tokopedia.flight.cancellation.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter;
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel;
import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationReasonAndProofActivity;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.viewholder.FlightCancellationViewHolder;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationPassengerModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationReasonAndAttachmentModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperModel;
import com.tokopedia.flight.orderlist.view.viewmodel.FlightCancellationJourney;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * @author by furqan on 21/03/18.
 */

public class FlightCancellationFragment extends BaseListFragment<FlightCancellationModel, FlightCancellationAdapterTypeFactory>
        implements FlightCancellationContract.View, FlightCancellationViewHolder.FlightCancellationListener {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";
    public static final String EXTRA_FIRST_CHECK = "EXTRA_FIRST_CHECK";

    public static final int REQUEST_REFUND_CANCELLATION = 1;
    public static final int REQUEST_REASON_AND_PROOF_CANCELLATION = 2;

    private String invoiceId;
    private List<FlightCancellationModel> flightCancellationViewModelList;
    private FlightCancellationWrapperModel flightCancellationWrapperViewModel;
    List<FlightCancellationJourney> flightCancellationJourneyList;
    private Map<String, FlightCancellationPassengerModel> passengerRelationMap;
    private RecyclerView recyclerView;

    @Inject
    FlightCancellationPresenter flightCancellationPresenter;

    private LinearLayout btnContainer;
    private AppCompatButton btnSubmit;

    private boolean isFirstRelationCheck = true;

    public static FlightCancellationFragment createInstance(String invoiceId,
                                                            List<FlightCancellationJourney> flightCancellationJourneyList) {
        FlightCancellationFragment fragment = new FlightCancellationFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        bundle.putParcelableArrayList(EXTRA_CANCEL_JOURNEY, (ArrayList<? extends Parcelable>) flightCancellationJourneyList);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_FIRST_CHECK)) {
            isFirstRelationCheck = savedInstanceState.getBoolean(EXTRA_FIRST_CHECK);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_cancellation, container, false);

        recyclerView = getRecyclerView(view);
        btnContainer = view.findViewById(com.tokopedia.flight.R.id.btn_container);
        btnSubmit = view.findViewById(com.tokopedia.flight.R.id.button_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightCancellationPresenter.onNextButtonClicked();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        initialVariable();

        flightCancellationPresenter.attachView(this);
        flightCancellationPresenter.init();
        super.onViewCreated(view, savedInstanceState);
    }

    @NonNull
    @Override
    protected BaseListAdapter<FlightCancellationModel, FlightCancellationAdapterTypeFactory> createAdapterInstance() {
        BaseListAdapter<FlightCancellationModel, FlightCancellationAdapterTypeFactory> adapter = super.createAdapterInstance();
        ErrorNetworkModel errorNetworkModel = adapter.getErrorNetworkModel();
        errorNetworkModel.setIconDrawableRes(com.tokopedia.flight.R.drawable.ic_flight_empty_state);
        errorNetworkModel.setOnRetryListener(this);
        adapter.setErrorNetworkModel(errorNetworkModel);
        return adapter;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putBoolean(EXTRA_FIRST_CHECK, isFirstRelationCheck);
    }

    private void initialVariable() {
        invoiceId = getArguments().getString(EXTRA_INVOICE_ID);
        flightCancellationJourneyList = getArguments().getParcelableArrayList(EXTRA_CANCEL_JOURNEY);
        flightCancellationWrapperViewModel = new FlightCancellationWrapperModel();
        flightCancellationWrapperViewModel.setInvoice(invoiceId);
        flightCancellationWrapperViewModel.setCancellationReasonAndAttachment(new FlightCancellationReasonAndAttachmentModel());
        passengerRelationMap = new HashMap<>();
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
    public void onItemClicked(FlightCancellationModel viewModel) {

    }

    @Override
    public void loadData(int page) {
        getAdapter().clearAllElements();
        showLoading();
        flightCancellationPresenter.onViewCreated();
    }

    @Override
    protected FlightCancellationAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightCancellationAdapterTypeFactory(this);
    }

    @Override
    public void renderCancelableList() {
        hideLoading();
        hideFullLoading();
        renderList(flightCancellationViewModelList);

        if (flightCancellationViewModelList.size() > 0) {
            btnContainer.setVisibility(View.VISIBLE);
        } else {
            btnContainer.setVisibility(View.GONE);
        }
    }

    @Override
    public void setFlightCancellationViewModel(List<FlightCancellationModel> flightCancellationViewModelList) {
        this.flightCancellationViewModelList = flightCancellationViewModelList;
    }

    @Override
    public void setSelectedCancellationViewModel(List<FlightCancellationModel> flightCancellationViewModelList) {
        this.flightCancellationWrapperViewModel.setGetCancellations(flightCancellationViewModelList);
    }

    @Override
    public void setPassengerRelations(Map<String, FlightCancellationPassengerModel> passengerRelations) {
        this.passengerRelationMap = passengerRelations;
    }

    @Override
    public String getInvoiceId() {
        return invoiceId;
    }

    @Override
    public List<FlightCancellationJourney> getFlightCancellationJourney() {
        return flightCancellationJourneyList;
    }

    @Override
    public List<FlightCancellationModel> getCurrentFlightCancellationViewModel() {
        return flightCancellationViewModelList;
    }

    @Override
    public List<FlightCancellationModel> getSelectedCancellationViewModel() {
        return flightCancellationWrapperViewModel.getGetCancellations();
    }

    @Override
    public Map<String, FlightCancellationPassengerModel> getPassengerRelations() {
        return passengerRelationMap;
    }

    @Override
    public void showShouldChooseAtLeastOnePassengerError() {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(),
                getString(com.tokopedia.flight.R.string.flight_cancellation_should_choose_at_least_one_passenger_error));
    }

    @Override
    public void hideFullLoading() {
        btnContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void showFullLoading() {
        btnContainer.setVisibility(View.GONE);
    }

    @Override
    public void showGetListError(Throwable throwable) {
        hideLoading();
        btnContainer.setVisibility(View.GONE);
        super.showGetListError(throwable);
    }

    @Override
    public void showAutoCheckDialog() {
        isFirstRelationCheck = false;
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.RETORIC);
        dialog.setTitle(getString(com.tokopedia.flight.R.string.flight_cancellation_auto_check_dialog_title));
        dialog.setDesc(getString(
                com.tokopedia.flight.R.string.flight_cancellation_auto_check_dialog_desc));
        dialog.setBtnOk(getString(com.tokopedia.flight.R.string.flight_cancellation_auto_check_dialog_button));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public boolean isFirstRelationCheck() {
        return isFirstRelationCheck;
    }

    @Override
    public void disableNextButton() {
        btnSubmit.setEnabled(false);
    }

    @Override
    public void enableNextButton() {
        btnSubmit.setEnabled(true);
    }

    @Override
    public void onPassengerChecked(FlightCancellationPassengerModel passengerViewModel, int position) {
        flightCancellationPresenter.checkPassenger(passengerViewModel, position);
    }

    @Override
    public void onPassengerUnchecked(FlightCancellationPassengerModel passengerViewModel, int position) {
        flightCancellationPresenter.uncheckPassenger(passengerViewModel, position);
    }

    @Override
    public boolean isChecked(FlightCancellationPassengerModel passengerViewModel) {
        return flightCancellationPresenter.isPassengerChecked(passengerViewModel);
    }

    @Override
    public void navigateToReasonAndProofPage() {
        startActivityForResult(
                FlightCancellationReasonAndProofActivity.getCallingIntent(
                        getActivity(), flightCancellationWrapperViewModel
                ),
                REQUEST_REASON_AND_PROOF_CANCELLATION
        );
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_REFUND_CANCELLATION:
                if (resultCode == RESULT_OK) {
                    closeCancellationPage();
                }
                break;
            case REQUEST_REASON_AND_PROOF_CANCELLATION:
                if (resultCode == RESULT_OK) {
                    closeCancellationPage();
                }
                break;
        }
    }

    @Override
    public void onRetryClicked() {
        showLoading();
        flightCancellationPresenter.onViewCreated();
    }

    @Override
    public int getRecyclerViewResourceId() {
        return R.id.recycler_view;
    }

    private void closeCancellationPage() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    private void notifyChanges() {
        recyclerView.post(new Runnable() {
            @Override
            public void run() {
                getAdapter().notifyDataSetChanged();
            }
        });
    }
}
