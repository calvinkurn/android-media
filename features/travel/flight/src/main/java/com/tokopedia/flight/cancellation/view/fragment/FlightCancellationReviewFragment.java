package com.tokopedia.flight.cancellation.view.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.core.widget.NestedScrollView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.fragment.BaseListFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.dialog.DialogUnify;
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
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationAttachmentModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationModel;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperModel;
import com.tokopedia.flight.cancellationV2.presentation.adapter.FlightCancellationReviewEstimationNotesAdapter;
import com.tokopedia.flight.orderlist.util.FlightErrorUtil;
import com.tokopedia.unifycomponents.UnifyButton;

import java.util.List;

import javax.inject.Inject;

import kotlin.Unit;

/**
 * @author by furqan on 29/03/18.
 */

public class FlightCancellationReviewFragment extends BaseListFragment<FlightCancellationModel, FlightReviewCancellationAdapterTypeFactory>
        implements FlightCancellationReviewContract.View, FlightCancellationAttachementAdapterTypeFactory.OnAdapterInteractionListener {

    public static final String EXTRA_INVOICE_ID = "EXTRA_INVOICE_ID";
    public static final String EXTRA_CANCEL_JOURNEY = "EXTRA_CANCEL_JOURNEY";

    private static final int REQUEST_CANCELLATION_TNC = 1;

    private FlightCancellationReviewEstimationNotesAdapter estimationNotesAdapter;

    private LinearLayout containerAdditionalData;
    private LinearLayout containerAdditionalReason;
    private LinearLayout containerAdditionalDocuments;
    private UnifyButton btnSubmit;
    private AppCompatTextView txtReason;
    private AppCompatTextView txtTotalRefund;
    private RecyclerView rvAttachments;
    private FlightCancellationAttachmentAdapter attachmentAdapter;
    private NestedScrollView reviewContainer;
    private LinearLayout loadingContainer;
    private AppCompatTextView tvDescription;
    private AppCompatTextView tvRefundDetail;
    private LinearLayout containerEstimateRefund;
    private RecyclerView rvEstimationNotes;

    @Inject
    FlightCancellationReviewPresenter presenter;
    private String invoiceId;
    private FlightCancellationWrapperModel flightCancellationPassData;

    public static FlightCancellationReviewFragment createInstance(String invoiceId,
                                                                  FlightCancellationWrapperModel flightCancellationPassData) {
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
        View view = inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_cancellation_review, container, false);
        reviewContainer = view.findViewById(com.tokopedia.flight.R.id.sv_review_container);
        loadingContainer = view.findViewById(com.tokopedia.flight.R.id.full_page_loading);
        rvAttachments = view.findViewById(com.tokopedia.flight.R.id.rv_attachments);
        containerAdditionalData = view.findViewById(com.tokopedia.flight.R.id.container_additional_data);
        containerAdditionalReason = view.findViewById(com.tokopedia.flight.R.id.container_additional_reason);
        containerAdditionalDocuments = view.findViewById(com.tokopedia.flight.R.id.container_additional_documents);
        txtReason = view.findViewById(com.tokopedia.flight.R.id.txt_cancellation_reason);
        txtTotalRefund = view.findViewById(com.tokopedia.flight.R.id.tv_total_refund);
        btnSubmit = view.findViewById(com.tokopedia.flight.R.id.button_submit);
        tvDescription = view.findViewById(com.tokopedia.flight.R.id.tv_description_refund);
        tvRefundDetail = view.findViewById(com.tokopedia.flight.R.id.tv_refund_detail);
        containerEstimateRefund = view.findViewById(com.tokopedia.flight.R.id.container_estimate_refund);
        rvEstimationNotes = view.findViewById(com.tokopedia.flight.R.id.rvEstimationNotes);

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
                = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
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
    public int getRecyclerViewResourceId() {
        return R.id.recycler_view;
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
    public void onItemClicked(FlightCancellationModel flightCancellationViewModel) {

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
        final DialogUnify dialog = new DialogUnify(getActivity(), DialogUnify.HORIZONTAL_ACTION, DialogUnify.NO_IMAGE);
        dialog.setTitle(getString(com.tokopedia.flight.R.string.flight_cancellation_review_dialog_success_title));
        dialog.setDescription(Html.fromHtml(getString(resId)));
        dialog.setPrimaryCTAText("OK");
        dialog.setPrimaryCTAClickListener(() -> {
            dialog.dismiss();
            closeReviewCancellationPage();
            return Unit.INSTANCE;
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
    public void showEstimateValue(List<String> estimationNotes) {
        containerEstimateRefund.setVisibility(View.VISIBLE);

        if (estimationNotesAdapter == null) {
            estimationNotesAdapter = new FlightCancellationReviewEstimationNotesAdapter();
        }
        estimationNotesAdapter.setData(estimationNotes);

        rvEstimationNotes.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
        rvEstimationNotes.setHasFixedSize(true);
        rvEstimationNotes.setAdapter(estimationNotesAdapter);
        rvEstimationNotes.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideEstimateValue() {
        containerEstimateRefund.setVisibility(View.GONE);
        rvEstimationNotes.setVisibility(View.GONE);
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
    public FlightCancellationWrapperModel getCancellationWrapperViewModel() {
        return flightCancellationPassData;
    }

    @Override
    public void setCancellationWrapperViewModel(FlightCancellationWrapperModel viewModel) {
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
                flightCancellationPassData.getCancellationReasonAndAttachment().getReason().length() > 0) {
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
                (flightCancellationPassData.getCancellationReasonAndAttachment().getReason().length() == 0 &&
                        flightCancellationPassData.getCancellationReasonAndAttachment().getAttachments().size() == 0)) {
            containerAdditionalData.setVisibility(View.GONE);
        }

        txtTotalRefund.setText(flightCancellationPassData.getCancellationReasonAndAttachment().getEstimateFmt());
    }

    @Override
    public void onUploadAttachmentButtonClicked(int positionIndex) {

    }

    @Override
    public void deleteAttachement(FlightCancellationAttachmentModel element) {

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
        final int color = getContext().getResources().getColor(com.tokopedia.unifyprinciples.R.color.Unify_G500);
        int startIndex = getString(com.tokopedia.flight.R.string.flight_cancellation_refund_description).indexOf("Pelajari");
        int stopIndex = getString(com.tokopedia.flight.R.string.flight_cancellation_refund_description).length();
        SpannableString description = new SpannableString(getContext().getString(
                com.tokopedia.flight.R.string.flight_cancellation_refund_description));
        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                FlightCancellationRefundBottomSheet bottomSheet = new FlightCancellationRefundBottomSheet();
                bottomSheet.show(getChildFragmentManager(), getString(com.tokopedia.flight.R.string.flight_cancellation_refund_bottom_sheet_tag));
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
