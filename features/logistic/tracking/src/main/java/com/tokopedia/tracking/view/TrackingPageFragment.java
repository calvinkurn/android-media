package com.tokopedia.tracking.view;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.tracking.R;
import com.tokopedia.tracking.adapter.EmptyTrackingNotesAdapter;
import com.tokopedia.tracking.adapter.TrackingHistoryAdapter;
import com.tokopedia.tracking.di.DaggerTrackingPageComponent;
import com.tokopedia.tracking.di.TrackingPageComponent;
import com.tokopedia.tracking.di.TrackingPageModule;
import com.tokopedia.tracking.presenter.ITrackingPagePresenter;
import com.tokopedia.tracking.utils.DateUtil;
import com.tokopedia.tracking.viewmodel.TrackingViewModel;
import com.tokopedia.transactionanalytics.OrderAnalyticsOrderTracking;
import com.tokopedia.transactionanalytics.listener.ITransactionAnalyticsTrackingOrder;
import com.tokopedia.unifycomponents.UnifyButton;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static com.tokopedia.tracking.view.TrackingPageActivity.ORDER_ID_KEY;
import static com.tokopedia.tracking.view.TrackingPageActivity.URL_LIVE_TRACKING;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPageFragment extends BaseDaggerFragment implements
        ITrackingPageFragment, ITransactionAnalyticsTrackingOrder {

    private static final String ADDITIONAL_INFO_URL = "https://m.tokopedia.com/bantuan/217217126-agen-logistik-di-tokopedia";
    private static final int PER_SECOND = 1000;
    private static final String INVALID_REFERENCE_STATUS = "resi tidak valid";

    private ProgressBar loadingScreen;
    private ProgressDialog progressDialog;

    private TextView referenceNumber;
    private ImageView courierLogo;
    private TextView deliveryDate;
    private TextView storeName;
    private TextView storeAddress;
    private TextView serviceCode;
    private TextView buyerName;
    private TextView buyerLocation;
    private TextView currentStatus;
    private RecyclerView trackingHistory;
    private LinearLayout emptyUpdateNotification;
    private TextView notificationText;
    private RecyclerView notificationHelpStep;
    private UnifyButton liveTrackingButton;
    private ViewGroup rootView;
    private LinearLayout descriptionLayout;
    private UnifyButton retryButton;
    private TextView retryStatus;
    private CountDownTimer mCountDownTimer;

    @Inject
    ITrackingPagePresenter presenter;
    @Inject
    DateUtil dateUtil;
    @Inject
    OrderAnalyticsOrderTracking orderAnalyticsOrderTracking;

    public static TrackingPageFragment createFragment(String orderId, String liveTrackingUrl) {
        TrackingPageFragment fragment = new TrackingPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ORDER_ID_KEY, orderId);
        bundle.putString(URL_LIVE_TRACKING, liveTrackingUrl);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.tracking_page_layout, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        loadingScreen = view.findViewById(R.id.main_progress_bar);

        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage(getString(R.string.title_loading));
        progressDialog.setCancelable(false);

        rootView = view.findViewById(R.id.root_view);
        referenceNumber = view.findViewById(R.id.reference_number);
        courierLogo = view.findViewById(R.id.courier_logo);
        deliveryDate = view.findViewById(R.id.delivery_date);
        storeName = view.findViewById(R.id.store_name);
        storeAddress = view.findViewById(R.id.store_address);
        serviceCode = view.findViewById(R.id.service_code);
        buyerName = view.findViewById(R.id.buyer_name);
        buyerLocation = view.findViewById(R.id.buyer_location);
        currentStatus = view.findViewById(R.id.currenct_status);
        trackingHistory = view.findViewById(R.id.tracking_history);
        trackingHistory.setNestedScrollingEnabled(false);
        emptyUpdateNotification = view.findViewById(R.id.empty_update_notification);
        notificationText = view.findViewById(R.id.notification_text);
        notificationHelpStep = view.findViewById(R.id.notification_help_step);
        retryButton = view.findViewById(R.id.retry_pickup_button);
        descriptionLayout = view.findViewById(R.id.description_layout);
        retryStatus = view.findViewById(R.id.tv_retry_status);
        TextView furtherInformationText = view.findViewById(R.id.further_information_text);
        furtherInformationText.setText(Html
                        .fromHtml(getString(R.string.further_information_text_html)),
                TextView.BufferType.SPANNABLE);
        furtherInformationText.setOnClickListener(onFurtherInformationClicked());
        liveTrackingButton = view.findViewById(R.id.live_tracking_button);
        presenter.onGetTrackingData(getArguments().getString(ORDER_ID_KEY));
        presenter.onGetRetryAvailability(getArguments().getString(ORDER_ID_KEY));
    }


    @Override
    public void populateView(TrackingViewModel model) {
        referenceNumber.setText(model.getReferenceNumber());
        ImageHandler.LoadImage(courierLogo, model.getCourierLogoUrl());
        if (TextUtils.isEmpty(model.getServiceCode())) descriptionLayout.setVisibility(View.GONE);
        if (!TextUtils.isEmpty(model.getDeliveryDate()))
            deliveryDate.setText(dateUtil.getFormattedDate(model.getDeliveryDate()));

        storeName.setText(model.getSellerStore());
        storeAddress.setText(model.getSellerAddress());
        serviceCode.setText(model.getServiceCode());
        buyerName.setText(model.getBuyerName());
        buyerLocation.setText(model.getBuyerAddress());
        currentStatus.setText(model.getStatus());
        initialHistoryView();
        setHistoryView(model);
        setEmptyHistoryView(model);
        setLiveTrackingButton();
        sendAnalyticsOnViewTrackingRendered();
    }

    public void sendAnalyticsOnViewTrackingRendered() {
        orderAnalyticsOrderTracking.eventViewOrderTrackingImpressionButtonLiveTracking();
    }

    public void sendAnalyticsOnButtonLiveTrackingClicked() {
        orderAnalyticsOrderTracking.eventClickOrderTrackingClickButtonLiveTracking();
    }

    @Override
    public void showMainLoadingPage() {
        loadingScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void closeMainLoadingPage() {
        loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public void showLoading() {
        if (progressDialog != null && !progressDialog.isShowing()) progressDialog.show();
    }

    @Override
    public void hideLoading() {
        if (progressDialog != null && progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void showError(Throwable error) {
        String message = ErrorHandler.getErrorMessage(getContext(), error);
        // currently, message is not being used
        NetworkErrorHelper.showEmptyState(getActivity(), rootView,
                () -> {
                    presenter.onGetTrackingData(getArguments().getString(ORDER_ID_KEY));
                    presenter.onGetRetryAvailability(getArguments().getString(ORDER_ID_KEY));
                });
    }

    @Override
    public void setRetryButton(boolean active, long deadline) {
        if (active) {
            retryButton.setVisibility(View.VISIBLE);
            retryButton.setText("Cari Driver Baru");
            retryButton.setEnabled(true);
            retryButton.setOnClickListener(view -> {
                        retryButton.setEnabled(false);
                        presenter.onRetryPickup(getArguments().getString(ORDER_ID_KEY));
                    }
            );
            retryStatus.setVisibility(View.GONE);
        } else {
            retryButton.setVisibility(View.GONE);
            if (deadline > 0) {
                // when retry button available but need to wait until deadline
                retryStatus.setVisibility(View.VISIBLE);
                long now = System.currentTimeMillis() / 1000L;
                long remainingTime = deadline - now;
                initTimer(remainingTime);
            } else {
                retryStatus.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public void startSuccessCountdown() {
        retryButton.setText("Mencari driver baru...");
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong -> {
                    presenter.onGetTrackingData(getArguments().getString(ORDER_ID_KEY));
                    presenter.onGetRetryAvailability(getArguments().getString(ORDER_ID_KEY));
                });
    }

    private void initialHistoryView() {
        trackingHistory.setVisibility(View.GONE);
        emptyUpdateNotification.setVisibility(View.GONE);
        liveTrackingButton.setVisibility(View.GONE);
    }

    private void setHistoryView(TrackingViewModel model) {
        if (model.isInvalid() || model.getStatusNumber() == TrackingViewModel.ORDER_STATUS_WAITING ||
                model.isInvalid() || model.getChange() == 0 || model.getHistoryList().isEmpty()) {
            trackingHistory.setVisibility(View.GONE);
        } else {
            trackingHistory.setVisibility(View.VISIBLE);
            trackingHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
            trackingHistory.setAdapter(new TrackingHistoryAdapter(model.getHistoryList(), dateUtil));
        }
    }

    private void setEmptyHistoryView(TrackingViewModel model) {
        if (model.isInvalid()) {
            emptyUpdateNotification.setVisibility(View.VISIBLE);
            notificationText.setText(getString(R.string.warning_courier_invalid));
            notificationHelpStep.setVisibility(View.VISIBLE);
            notificationHelpStep.setLayoutManager(new LinearLayoutManager(getActivity()));
            notificationHelpStep.setAdapter(new EmptyTrackingNotesAdapter());
        } else if (model.getStatusNumber() == TrackingViewModel.ORDER_STATUS_WAITING
                || model.getChange() == 0 || model.getHistoryList().size() == 0) {
            emptyUpdateNotification.setVisibility(View.VISIBLE);
            notificationText.setText(getString(R.string.warning_no_courier_change));
            notificationHelpStep.setVisibility(View.GONE);
        } else {
            emptyUpdateNotification.setVisibility(View.GONE);
            notificationHelpStep.setVisibility(View.GONE);
        }
    }

    private void setLiveTrackingButton() {
        if (TextUtils.isEmpty(getArguments().getString(URL_LIVE_TRACKING)))
            liveTrackingButton.setVisibility(View.GONE);
        else {
            liveTrackingButton.setVisibility(View.VISIBLE);
            liveTrackingButton.setOnClickListener(onLiveTrackingClickedListener());
        }
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {

        TrackingPageComponent component = DaggerTrackingPageComponent
                .builder()
                .baseAppComponent(
                        ((BaseMainApplication) getActivity().getApplication())
                                .getBaseAppComponent()
                )
                .trackingPageModule(new TrackingPageModule(this))
                .build();
        component.inject(this);
    }

    private void initTimer(long remainingSeconds) {
        if (remainingSeconds <= 0) return;
        long timeInMillis = remainingSeconds * 1000;
        String strFormat = (getContext() != null) ?
                getContext().getString(R.string.retry_dateline_info) : "";
        mCountDownTimer = new CountDownTimer(timeInMillis, PER_SECOND) {
            @Override
            public void onTick(long millsUntilFinished) {
                if (getContext() != null) {
                    String info = String.format(strFormat,
                            DateUtils.formatElapsedTime(millsUntilFinished / 1000));
                    retryStatus.setText(MethodChecker.fromHtml(info));
                }
            }

            @Override
            public void onFinish() {
                presenter.onGetTrackingData(getArguments().getString(ORDER_ID_KEY));
                presenter.onGetRetryAvailability(getArguments().getString(ORDER_ID_KEY));
            }
        };
        mCountDownTimer.start();
    }

    private View.OnClickListener onFurtherInformationClicked() {
        return view -> startActivity(SimpleWebViewActivity.createIntent(getActivity(), ADDITIONAL_INFO_URL));
    }

    private View.OnClickListener onLiveTrackingClickedListener() {
        return view -> {
            sendAnalyticsOnButtonLiveTrackingClicked();
            startActivity(
                    SimpleWebViewActivity.createIntent(getActivity(),
                            getArguments().getString(URL_LIVE_TRACKING)));
        };
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        presenter.onDetach();
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
    }
}
