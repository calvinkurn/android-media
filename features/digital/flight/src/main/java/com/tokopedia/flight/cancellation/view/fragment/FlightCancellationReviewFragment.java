package com.tokopedia.flight.cancellation.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
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
import com.tokopedia.flight.cancellation.view.fragment.customview.FlightCancellationRefundBottomSheet;
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
    private AppCompatTextView tvDescription;
    private AppCompatTextView tvRefundDetail;
    private LinearLayout containerEstimateRefund;
    private LinearLayout containerEstimateNotes;

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
        txtTotalRefund = view.findViewById(R.id.tv_total_refund);
        btnSubmit = view.findViewById(R.id.button_submit);
        tvDescription = view.findViewById(R.id.tv_description_refund);
        tvRefundDetail = view.findViewById(R.id.tv_refund_detail);
        containerEstimateRefund = view.findViewById(R.id.container_estimate_refund);
        containerEstimateNotes = view.findViewById(R.id.container_estimate_notes);

        tvDescription.setText(setDescriptionText());
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
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
    public void showEstimateValue() {
        containerEstimateRefund.setVisibility(View.VISIBLE);
        containerEstimateNotes.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEstimateValue() {
        containerEstimateRefund.setVisibility(View.GONE);
        containerEstimateNotes.setVisibility(View.GONE);
    }

    @Override
    public void showRefundDetail(int resId) {
        tvRefundDetail.setText(getString(resId));
        tvRefundDetail.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideRefundDetail() {
        tvRefundDetail.setVisibility(View.GONE);
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

    @Override
    public void showErrorFetchEstimateRefund(String messageFromException) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), messageFromException, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.onRetryFetchEstimate();
            }
        });
    }

    @Override
    public void renderView() {
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

    @Override
    public void viewImage(String filePath) {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_CANCELLATION_TNC:
                if (resultCode == Activity.RESULT_OK) {
                    presenter.requestCancellation();
                }
                break;
        }
    }

    private void closeReviewCancellationPage() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    private void navigateToTermsAndConditionsPage() {
        startActivityForResult(FlightCancellationTermsAndConditionsActivity.createIntent(getContext()), REQUEST_CANCELLATION_TNC);
    }

    private SpannableString setDescriptionText() {
        final int color = getContext().getResources().getColor(R.color.green_500);
        int startIndex = getString(R.string.flight_cancellation_refund_description).indexOf("Pelajari");
        int stopIndex = getString(R.string.flight_cancellation_refund_description).length();
        SpannableString description = new SpannableString(getContext().getString(
                R.string.flight_cancellation_refund_description));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                FlightCancellationRefundBottomSheet bottomSheet = new FlightCancellationRefundBottomSheet();
                bottomSheet.show(getChildFragmentManager(), getString(R.string.flight_cancellation_refund_bottom_sheet_tag));
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                super.updateDrawState(ds);
                ds.setUnderlineText(false);
                ds.setColor(color);
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.NORMAL));
            }
        };
        description.setSpan(clickableSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return description;
    }
}
