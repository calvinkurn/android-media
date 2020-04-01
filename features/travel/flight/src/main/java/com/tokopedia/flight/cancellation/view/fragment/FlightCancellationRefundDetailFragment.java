package com.tokopedia.flight.cancellation.view.fragment;


import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatTextView;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.activity.FlightCancellationReviewActivity;
import com.tokopedia.flight.cancellation.view.contract.FlightCancellationRefundDetailContract;
import com.tokopedia.flight.cancellation.view.fragment.customview.FlightCancellationRefundBottomSheet;
import com.tokopedia.flight.cancellation.view.presenter.FlightCancellationRefundDetailPresenter;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;

import javax.inject.Inject;

import static android.app.Activity.RESULT_OK;

/**
 * A simple {@link Fragment} subclass.
 */
public class FlightCancellationRefundDetailFragment extends BaseDaggerFragment implements FlightCancellationRefundDetailContract.View {

    private static final String PARAM_CANCELLATION = "PARAM_CANCELLATION";
    private static final String PARAM_STEP_NUMBER = "PARAM_STEP_NUMBER";
    private static final int REQUEST_REVIEW_CODE = 1;

    private FlightCancellationWrapperViewModel wrapperViewModel;
    private int stepsNumber;

    public static FlightCancellationRefundDetailFragment newInstance(FlightCancellationWrapperViewModel wrapperViewModel,
                                                                     int stepNumber) {
        FlightCancellationRefundDetailFragment fragment = new FlightCancellationRefundDetailFragment();
        Bundle args = new Bundle();
        args.putParcelable(PARAM_CANCELLATION, wrapperViewModel);
        args.putInt(PARAM_STEP_NUMBER, stepNumber);
        fragment.setArguments(args);
        return fragment;
    }

    private AppCompatTextView tvStepTitle;
    private ProgressBar progressBar;
    private LinearLayout container;
    private AppCompatTextView tvTotalRefund;
    private AppCompatButton btnNext;
    private AppCompatTextView tvDescription;

    @Inject
    FlightCancellationRefundDetailPresenter presenter;

    public FlightCancellationRefundDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            wrapperViewModel = getArguments().getParcelable(PARAM_CANCELLATION);
            stepsNumber = getArguments().getInt(PARAM_STEP_NUMBER);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(com.tokopedia.flight.R.layout.fragment_flight_cancellation_refund_detail, container, false);
        setupView(view);
        return view;
    }

    private void setupView(View view) {
        progressBar = (ProgressBar) view.findViewById(com.tokopedia.flight.R.id.progress_bar);
        container = (LinearLayout) view.findViewById(com.tokopedia.flight.R.id.container);
        tvStepTitle = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_step_title);
        tvTotalRefund = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_total_refund);
        tvDescription = (AppCompatTextView) view.findViewById(com.tokopedia.flight.R.id.tv_description_refund);
        tvDescription.setText(setDescriptionText());
        tvDescription.setMovementMethod(LinkMovementMethod.getInstance());
        btnNext = (AppCompatButton) view.findViewById(com.tokopedia.flight.R.id.btn_next);
        btnNext.setOnClickListener(getNextButtonClickListener());

        tvStepTitle.setText(String.format(
                getString(com.tokopedia.flight.R.string.flight_cancellation_step_3_header_title), stepsNumber));
    }

    private View.OnClickListener getNextButtonClickListener() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivityForResult(FlightCancellationReviewActivity.createIntent(getActivity(),
                        wrapperViewModel.getInvoice(), wrapperViewModel),
                        REQUEST_REVIEW_CODE);
            }
        };
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        presenter.initialize();
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
    public void showLoading() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public FlightCancellationWrapperViewModel getCancellationViewModel() {
        return wrapperViewModel;
    }

    @Override
    public void hideLoading() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showErrorFetchEstimateRefund(String message) {
        NetworkErrorHelper.showEmptyState(getActivity(), getView(), message, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                presenter.onRetryFetchEstimate();
            }
        });
    }

    @Override
    public void renderTotalRefund(String totalRefund) {
        tvTotalRefund.setText(totalRefund);
    }

    @Override
    public void showFullPageContainer() {
        container.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideFullPageContainer() {
        container.setVisibility(View.GONE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQUEST_REVIEW_CODE:
                if (resultCode == RESULT_OK) {
                    closeRefundPage();
                }
                break;
        }
    }

    private void closeRefundPage() {
        getActivity().setResult(RESULT_OK);
        getActivity().finish();
    }

    private SpannableString setDescriptionText() {
        final int color = getContext().getResources().getColor(com.tokopedia.design.R.color.green_500);
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
                ds.setTypeface(Typeface.create(Typeface.DEFAULT, Typeface.BOLD));
            }
        };
        description.setSpan(clickableSpan, startIndex, stopIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        return description;
    }
}
