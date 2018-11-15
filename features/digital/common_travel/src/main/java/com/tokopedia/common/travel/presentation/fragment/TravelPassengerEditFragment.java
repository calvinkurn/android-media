package com.tokopedia.common.travel.presentation.fragment;

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
import android.widget.Toast;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.DividerItemDecoration;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.common.travel.R;
import com.tokopedia.common.travel.di.CommonTravelComponent;
import com.tokopedia.common.travel.presentation.activity.TravelPassengerUpdateActivity;
import com.tokopedia.common.travel.presentation.adapter.TravelPassengerEditAdapter;
import com.tokopedia.common.travel.presentation.contract.TravelPassengerEditContract;
import com.tokopedia.common.travel.presentation.model.TravelPassenger;
import com.tokopedia.common.travel.presentation.presenter.TravelPassengerEditPresenter;
import com.tokopedia.common.travel.utils.CommonTravelUtils;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.graphql.data.GraphqlClient;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by nabillasabbaha on 13/11/18.
 */
public class TravelPassengerEditFragment extends BaseDaggerFragment implements TravelPassengerEditContract.View {

    private RecyclerView recyclerViewPassenger;
    private ProgressBar progressBar;
    private TravelPassengerEditAdapter adapter;

    @Inject
    TravelPassengerEditPresenter presenter;

    public static Fragment newInstance() {
        Fragment fragment = new TravelPassengerEditFragment();
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

        presenter.getPassengerList();
        renderPassengerList();
    }

    private void renderPassengerList() {
        adapter = new TravelPassengerEditAdapter();
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerViewPassenger.getContext());
        dividerItemDecoration.setUsePaddingLeft(true);
        recyclerViewPassenger.addItemDecoration(dividerItemDecoration);
        adapter.setListener(new TravelPassengerEditAdapter.ActionListener() {
            @Override
            public void onEditPassenger(TravelPassenger travelPassenger) {
                startActivity(TravelPassengerUpdateActivity.callingIntent(getActivity(), travelPassenger, TravelPassengerUpdateActivity.EDIT_PASSENGER_TYPE));
            }

            @Override
            public void onDeletePassenger(String id, int travelId) {
                showDeleteDialog(id, travelId);
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
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), getString(resId));
    }

    @Override
    public void showMessageErrorInSnackBar(String message) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), message);
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

    private void showDeleteDialog(String id, int travelId) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.dialog_delete_passenger_title));
        dialog.setDesc(getString(R.string.dialog_delete_passenger_question));
        dialog.setBtnOk(getString(R.string.dialog_delete_passenger_btn_delete));
        dialog.setBtnCancel(getString(R.string.dialog_delete_passenger_btn_cancel));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showSuccessSnackbar(getString(R.string.snackbar_message_success_delete));
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
