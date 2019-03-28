package com.tokopedia.flight.cancellation.view.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.Menu;

import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.flight.FlightModuleRouter;
import com.tokopedia.flight.R;
import com.tokopedia.flight.cancellation.di.DaggerFlightCancellationComponent;
import com.tokopedia.flight.cancellation.di.FlightCancellationComponent;
import com.tokopedia.flight.cancellation.view.fragment.FlightCancellationReasonAndProofFragment;
import com.tokopedia.flight.cancellation.view.viewmodel.FlightCancellationWrapperViewModel;
import com.tokopedia.flight.common.view.BaseFlightActivity;
import com.tokopedia.imageuploader.di.ImageUploaderModule;

/**
 * @author by alvarisi on 3/26/18.
 */
public class FlightCancellationReasonAndProofActivity extends BaseFlightActivity
        implements HasComponent<FlightCancellationComponent>, FlightCancellationReasonAndProofFragment.OnFragmentInteractionListener{

    private static final String EXTRA_CANCELLATION_VIEW_MODEL = "EXTRA_CANCELLATION_VIEW_MODEL";
    private static final int REQUEST_REVIEW_CODE = 1;
    private FlightCancellationWrapperViewModel cancellationWrapperViewModel;
    private FlightCancellationComponent cancellationComponent;
    private FlightCancellationReasonAndProofFragment fragment;

    public static Intent getCallingIntent(Activity activity, FlightCancellationWrapperViewModel viewModel) {
        Intent intent = new Intent(activity, FlightCancellationReasonAndProofActivity.class);
        intent.putExtra(EXTRA_CANCELLATION_VIEW_MODEL, viewModel);
        return intent;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        cancellationWrapperViewModel = getIntent().getParcelableExtra(EXTRA_CANCELLATION_VIEW_MODEL);
        super.onCreate(savedInstanceState);
        setupToolbar();
    }

    @Override
    protected Fragment getNewFragment() {
        if (fragment == null) {
            fragment = FlightCancellationReasonAndProofFragment.newInstance(cancellationWrapperViewModel);
        }

        return fragment;
    }

    @Override
    public FlightCancellationComponent getComponent() {
        if (cancellationComponent == null) {
            initInjector();
        }
        return cancellationComponent;
    }

    private void initInjector() {
        if (getApplication() instanceof FlightModuleRouter) {
            cancellationComponent = DaggerFlightCancellationComponent.builder()
                    .flightComponent(getFlightComponent())
                    .imageUploaderModule(new ImageUploaderModule())
                    .build();
        } else {
            throw new RuntimeException("Application must implement FlightModuleRouter");
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    private void setupToolbar() {
        toolbar.setContentInsetStartWithNavigation(0);
        toolbar.setSubtitleTextColor(ContextCompat.getColor(this, R.color.grey_500));
        String title = getString(R.string.activity_label_flight_cancellation);
        String subtitle = String.format(
                getString(R.string.flight_cancellation_subtitle_order_id),
                cancellationWrapperViewModel.getInvoice()
        );
        updateTitle(title, subtitle);
    }

    @Override
    public void goToReview(FlightCancellationWrapperViewModel viewModel) {

        startActivityForResult(FlightCancellationReviewActivity.createIntent(this,
                viewModel.getInvoice(), viewModel),
                REQUEST_REVIEW_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_REVIEW_CODE:
                if (resultCode == Activity.RESULT_OK){
                    closeReasonAndProofPage();
                }else {
                    if (getFragment() instanceof FlightCancellationReasonAndProofFragment){
                        ((FlightCancellationReasonAndProofFragment) getFragment()).onEstimateRefundActivityResultCancelled();
                    }
                }
                break;
        }
    }

    private void closeReasonAndProofPage() {
        setResult(Activity.RESULT_OK);
        finish();
    }
}
