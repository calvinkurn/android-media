package com.tokopedia.flight.cancellation.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationTermsAndConditionsActivity;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachementAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentAdapter;
import com.tokopedia.flight.cancellation.view.adapter.FlightCancellationAttachmentTypeFactory;
import com.tokopedia.flight.cancellation.view.adapter.FlightReviewCancellationAdapterTypeFactory;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationReviewContract;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationReviewPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationViewModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.util.FlightErrorUtil;

import javax.inject.Inject;

/**
 * @author by furqan on 29/03/18.
 */

public class FlightCancellationReviewFragment extends BaseListFragment<FlightCancellationViewModel, FlightReviewCancellationAdapterTypeFactory>
        implements FlightCancellationReviewContract.View, FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";

    private static final int REQUEST_CANCELLATION_TNC = 1;

    private LinearLayout containerAdditionalData;
    private LinearLayout containerAdditionalReason;
    private LinearLayout containerAdditionalDocuments;
    private AppCompatButton btnSubmit;
    private AppCompatTextView txtReason;
    private AppCompatTextView txtTotalRefund;
    private RecyclerView rvAttachments;
    private FlightCancellationAttachmentAdapter attachmentAdapter;
    private NestedScrollView reviewContainer;
    private LinearLayout loadingContainer;

    @Inject
    FlightCancellationReviewPresenter presenter;
    private String invoiceId;
    private FlightCancellationWrapperViewModel flightCancellationPassData;

    public static FlightCancellationReviewFragment createInstance(String invoiceId,
                                                                  FlightCancellationWrapperViewModel flightCancellationPassData) {
        FlightCancellationReviewFragment fragment = new FlightCancellationReviewFragment();
        Bundle bundle = new Bundle();
        bundle.putString(EXTRA_INVOICE_ID, invoiceId);
        bundle.putParcelable(EXTRA_CANCEL_JOURNEY, flightCancellationPassData);
        fragment.setArguments(bundle);
        return fragment;
    }

    @SuppressLint("StringFormatMatches")
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_flight_cancellation_review, container, false);
        reviewContainer = view.findViewById(R.id.sv_review_container);
        loadingContainer = view.findViewById(R.id.full_page_loading);
        rvAttachments = view.findViewById(R.id.rv_attachments);
        containerAdditionalData = view.findViewById(R.id.container_additional_data);
        containerAdditionalReason = view.findViewById(R.id.container_additional_reason);
        containerAdditionalDocuments = view.findViewById(R.id.container_additional_documents);
        txtReason = view.findViewById(R.id.txt_cancellation_reason);
        txtTotalRefund = view.findViewById(R.id.txt_total_refund);
        btnSubmit = view.findViewById(R.id.button_submit);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToTermsAndConditionsPage();
            }
        });

        FlightCancellationAttachmentTypeFactory adapterTypeFactory = new FlightCancellationAttachementAdapterTypeFactory(this, false);
        attachmentAdapter = new FlightCancellationAttachmentAdapter(adapterTypeFactory);
        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        rvAttachments.setLayoutManager(layoutManager);
        rvAttachments.setHasFixedSize(true);
        rvAttachments.setNestedScrollingEnabled(false);
        rvAttachments.setAdapter(attachmentAdapter);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        invoiceId = getArguments().getString(EXTRA_INVOICE_ID);
        flightCancellationPassData = getArguments().getParcelable(EXTRA_CANCEL_JOURNEY);

        presenter.attachView(this);
        presenter.onViewCreated();
        renderView();
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
    public void onItemClicked(FlightCancellationViewModel flightCancellationViewModel) {

    }

    @Override
    public void loadData(int page) {
    }

    @Override
    protected FlightReviewCancellationAdapterTypeFactory getAdapterTypeFactory() {
        return new FlightReviewCancellationAdapterTypeFactory();
    }

    @Override
    public void showSuccessDialog(int resId) {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.RETORIC);
        dialog.setTitle(getString(R.string.flight_cancellation_review_dialog_success_title));
        dialog.setDesc(Html.fromHtml(getString(resId)));
        dialog.setBtnOk("OK");
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                closeReviewCancellationPage();
            }
        });
        dialog.show();
    }

    @Override
    public void showLoading() {
        reviewContainer.setVisibility(View.GONE);
        loadingContainer.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        reviewContainer.setVisibility(View.VISIBLE);
        loadingContainer.setVisibility(View.GONE);
    }

    @Override
    public String getInvoiceId() {
        return invoiceId;
    }

    @Override
    public FlightCancellationWrapperViewModel getCancellationWrapperViewModel() {
        return flightCancellationPassData;
    }

    @Override
    public void setCancellationWrapperViewModel(FlightCancellationWrapperViewModel viewModel) {
        this.flightCancellationPassData = viewModel;
    }

    @Override
    public void showCancellationError(Throwable throwable) {
        NetworkErrorHelper.showRedCloseSnackbar(getActivity(), FlightErrorUtil
                .getMessageFromException(getContext(), throwable));
    }

    private void renderView() {
        renderList(flightCancellationPassData.getGetCancellations());

        if (flightCancellationPassData.getCancellationReasonAndAttachment().getReason() != null &&
                !flightCancellationPassData.getCancellationReasonAndAttachment().getReason().isEmpty()) {
            txtReason.setText(flightCancellationPassData.getCancellationReasonAndAttachment().getReason());
        } else {
            containerAdditionalReason.setVisibility(View.GONE);
        }

        if (flightCancellationPassData.getCancellationReasonAndAttachment().getAttachments() != null &&
                flightCancellationPassData.getCancellationReasonAndAttachment().getAttachments().size() > 0) {
            attachmentAdapter.addElement(flightCancellationPassData.getCancellationReasonAndAttachment().getAttachments());
        } else {
            containerAdditionalDocuments.setVisibility(View.GONE);
        }

        if ((flightCancellationPassData.getCancellationReasonAndAttachment().getReason() == null &&
                flightCancellationPassData.getCancellationReasonAndAttachment().getAttachments() == null) ||
                (flightCancellationPassData.getCancellationReasonAndAttachment().getReason().isEmpty() &&
                flightCancellationPassData.getCancellationReasonAndAttachment().getAttachments().size() == 0)) {
            containerAdditionalData.setVisibility(View.GONE);
        }

        txtTotalRefund.setText(flightCancellationPassData.getCancellationReasonAndAttachment().getEstimateFmt());
    }

    @Override
    public void onUploadAttachmentButtonClicked(int positionIndex) {

    }

    @Override
    public void deleteAttachement(FlightCancellationAttachmentViewModel element) {

    }

    private void closeReviewCancellationPage() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void navigateToTermsAndConditionsPage() {
        startActivityForResult(FlightCancellationTermsAndConditionsActivity.createIntent(getContext()), REQUEST_CANCELLATION_TNC);
    }

    private void showConfirmationDialog() {
        final Dialog dialog = new Dialog(getActivity(), Dialog.Type.PROMINANCE);
        dialog.setTitle(getString(R.string.flight_cancellation_dialog_title));
        dialog.setDesc(getString(R.string.flight_cancellation_review_dialog_description));
        dialog.setBtnOk(getString(R.string.flight_cancellation_dialog_back_button_text));
        dialog.setOnOkClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.setBtnCancel(getString(R.string.flight_booking_submit_button_label));
        dialog.setOnCancelClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.requestCancellation();
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CANCELLATION_TNC :
                if (resultCode == Activity.RESULT_OK) {
                    showConfirmationDialog();
                }
                break;
        }
    }
}
