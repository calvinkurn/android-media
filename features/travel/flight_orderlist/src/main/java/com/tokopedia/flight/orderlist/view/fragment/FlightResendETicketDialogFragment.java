package com.tokopedia.flight.orderlist.view.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.flight.orderlist.R;
import com.tokopedia.flight.orderlist.di.DaggerFlightOrderComponent;
import com.tokopedia.flight.orderlist.di.FlightOrderComponent;
import com.tokopedia.flight.orderlist.view.contract.FlightResendETicketContract;
import com.tokopedia.flight.orderlist.view.presenter.FlightResendETicketPresenter;
import com.tokopedia.unifycomponents.TextFieldUnify;
import com.tokopedia.unifyprinciples.Typography;

import javax.inject.Inject;

/**
 * @author by furqan on 08/02/18.
 */

public class FlightResendETicketDialogFragment extends DialogFragment implements FlightResendETicketContract.View {

    private static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    private static final String EXTRA_USER_ID = "EXTRA_USER_ID";
    private static final String EXTRA_USER_EMAIL = "EXTRA_USER_EMAIL";
    private boolean isProgramaticallyDismissed = false;

    private Typography txtSend;
    private Typography txtCancel;
    private TextFieldUnify edtEmail;

    @Inject
    FlightResendETicketPresenter flightResendETicketPresenter;

    private String userId, invoiceId, userEmail;

    public FlightResendETicketDialogFragment() {
    }

    public static FlightResendETicketDialogFragment newInstace(String invoiceId, String userId, String userEmail) {
        FlightResendETicketDialogFragment fragment = new FlightResendETicketDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        bundle.putString(EXTRA_USER_ID, userId);
        bundle.putString(EXTRA_USER_EMAIL, userEmail);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            invoiceId = getArguments().getString(EXTRA_INVOICE_ID);
            userId = getArguments().getString(EXTRA_USER_ID);
            userEmail = getArguments().getString(EXTRA_USER_EMAIL);
        }

        FlightOrderComponent component = DaggerFlightOrderComponent.builder().baseAppComponent(
                ((BaseMainApplication) getActivity().getApplication()).getBaseAppComponent())
                .build();

        component.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_resend_eticket_dialog, container, false);
        edtEmail = view.findViewById(R.id.tfResendEticketEmailAddress);
        txtSend = view.findViewById(R.id.tgResendETicketSend);
        txtCancel = view.findViewById(R.id.tgResendETicketCancel);

        edtEmail.getTextFieldInput().setText(userEmail);
        txtSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                flightResendETicketPresenter.onSendButtonClicked();
            }
        });

        txtCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isProgramaticallyDismissed = true;
                getTargetFragment().onActivityResult(
                        getTargetRequestCode(),
                        Activity.RESULT_CANCELED,
                        null
                );
                dismiss();
            }
        });

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        flightResendETicketPresenter.attachView(this);
    }

    @Override
    public void dismiss() {
        if (isProgramaticallyDismissed) {
            super.dismiss();
        } else {
            getTargetFragment().onActivityResult(
                    getTargetRequestCode(),
                    Activity.RESULT_CANCELED,
                    null
            );

            super.dismiss();
        }
    }

    @Override
    public String getInvoiceId() {
        return invoiceId;
    }

    @Override
    public String getUserId() {
        return userId;
    }

    @Override
    public String getEmail() {
        return edtEmail.getTextFieldInput().getText().toString();
    }

    @Override
    public void showEmailEmptyError(int resId) {
        showError(resId);
    }

    @Override
    public void showEmailInvalidError(int resId) {
        showError(resId);
    }

    @Override
    public void showEmailInvalidSymbolError(int resId) {
        showError(resId);
    }

    @Override
    public void onResendETicketSuccess() {
        isProgramaticallyDismissed = true;
        getTargetFragment().onActivityResult(
                getTargetRequestCode(),
                Activity.RESULT_OK,
                null
        );
        dismiss();
    }

    @Override
    public void onResendETicketError(String errorMsg) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), errorMsg);
        dismiss();
    }

    private void showError(int resId) {
        edtEmail.setError(true);
    }
}
