package com.tokopedia.logisticorder.view;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.logisticorder.R;
import com.tokopedia.logisticorder.adapter.EmptyTrackingNotesAdapter;
import com.tokopedia.logisticorder.adapter.TrackingHistoryAdapter;
import com.tokopedia.logisticorder.di.DaggerTrackingPageComponent;
import com.tokopedia.logisticorder.di.TrackingPageComponent;
import com.tokopedia.logisticorder.di.TrackingPageModule;
import com.tokopedia.logisticorder.presenter.ITrackingPagePresenter;
import com.tokopedia.logisticorder.uimodel.AdditionalInfoUiModel;
import com.tokopedia.logisticorder.uimodel.TrackingUiModel;
import com.tokopedia.logisticorder.utils.DateUtil;
import com.tokopedia.logisticorder.view.livetracking.LiveTrackingActivity;
import com.tokopedia.network.utils.ErrorHandler;
import com.tokopedia.unifycomponents.UnifyButton;
import com.tokopedia.unifycomponents.ticker.Ticker;
import com.tokopedia.unifycomponents.ticker.TickerCallback;
import com.tokopedia.unifycomponents.ticker.TickerData;
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by kris on 5/9/18. Tokopedia
 */

public class TrackingPageFragment extends BaseDaggerFragment implements ITrackingPageFragment {

    private static final int PER_SECOND = 1000;
    private static final int LIVE_TRACKING_VIEW_REQ = 1;
    private static final String ARGUMENTS_ORDER_ID = "ARGUMENTS_ORDER_ID";
    private static final String ARGUMENTS_TRACKING_URL = "ARGUMENTS_TRACKING_URL";
    private static final String ARGUMENTS_CALLER = "ARGUMENTS_CALLER";

    private String mOrderId;
    private String mTrackingUrl;
    private String mCaller;

    private ProgressBar loadingScreen;
    private TextView referenceNumber;
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
    private Ticker tickerInfoCourier;
    private LinearLayout tickerInfoLayout;

    @Inject
    ITrackingPagePresenter presenter;
    @Inject
    DateUtil dateUtil;
    @Inject
    OrderAnalyticsOrderTracking mAnalytics;

    public static TrackingPageFragment createFragment(String orderId, String liveTrackingUrl, String caller) {
        TrackingPageFragment fragment = new TrackingPageFragment();
        Bundle bundle = new Bundle();
        bundle.putString(ARGUMENTS_ORDER_ID, orderId);
        bundle.putString(ARGUMENTS_TRACKING_URL, liveTrackingUrl);
        bundle.putString(ARGUMENTS_CALLER, caller);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mOrderId = getArguments().getString(ARGUMENTS_ORDER_ID);
            mTrackingUrl = getArguments().getString(ARGUMENTS_TRACKING_URL);
            mCaller = getArguments().getString(ARGUMENTS_CALLER);
        }
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

        rootView = view.findViewById(R.id.root_view);
        referenceNumber = view.findViewById(R.id.reference_number);
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

        liveTrackingButton = view.findViewById(R.id.live_tracking_button);

        tickerInfoCourier = view.findViewById(R.id.ticker_info_courier);
        tickerInfoLayout = view.findViewById(R.id.ticker_info_layout);
        fetchData();
    }

    @Override
    public void populateView(TrackingUiModel model) {
        referenceNumber.setText(model.getReferenceNumber());
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
        setLiveTrackingButton(model);
        setTickerInfoCourier(model);
        mAnalytics.eventViewOrderTrackingImpressionButtonLiveTracking();
    }

    @Override
    public void showLoading() {
        loadingScreen.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingScreen.setVisibility(View.GONE);
    }

    @Override
    public void showError(Throwable error) {
        String message = ErrorHandler.getErrorMessage(getContext(), error);
        // currently, message is not being used
        NetworkErrorHelper.showEmptyState(getActivity(), rootView, this::fetchData);
    }

    @Override
    public void showSoftError(Throwable error) {
        String message = ErrorHandler.getErrorMessage(getContext(), error);
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void setRetryButton(boolean active, long deadline) {
        if (active) {
            retryButton.setVisibility(View.VISIBLE);
            retryButton.setText(getContext().getString(R.string.find_new_driver));
            retryButton.setEnabled(true);
            retryButton.setOnClickListener(view -> {
                        retryButton.setEnabled(false);
                        presenter.onRetryPickup(mOrderId);
                        mAnalytics.eventClickButtonCariDriver(mOrderId);
                    }
            );
            retryStatus.setVisibility(View.GONE);
            mAnalytics.eventViewButtonCariDriver(mOrderId);
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
        retryButton.setText(getContext().getString(R.string.finding_new_driver));
        Observable.timer(5, TimeUnit.SECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        showError(e);
                    }

                    @Override
                    public void onNext(Long aLong) {
                        fetchData();
                    }
                });
    }

    private void initialHistoryView() {
        trackingHistory.setVisibility(View.GONE);
        emptyUpdateNotification.setVisibility(View.GONE);
        liveTrackingButton.setVisibility(View.GONE);
    }

    private void setHistoryView(TrackingUiModel model) {
        if (model.isInvalid() || model.getStatusNumber() == TrackingUiModel.ORDER_STATUS_WAITING
                || model.getChange() == 0 || model.getHistoryList().isEmpty()) {
            trackingHistory.setVisibility(View.GONE);
        } else {
            trackingHistory.setVisibility(View.VISIBLE);
            trackingHistory.setLayoutManager(new LinearLayoutManager(getActivity()));
//            trackingHistory.setAdapter(new TrackingHistoryAdapter(model.getHistoryList(), dateUtil));
        }
    }

    private void setTickerInfoCourier(TrackingUiModel trackingUiModel) {
        if (trackingUiModel.getAdditionalInfoList() != null) {
            List<AdditionalInfoUiModel> additionalInfoUiModelList = trackingUiModel.getAdditionalInfoList();
            if (additionalInfoUiModelList.isEmpty()) {
                tickerInfoLayout.setVisibility(View.GONE);
            } else {
                tickerInfoLayout.setVisibility(View.VISIBLE);
                if (additionalInfoUiModelList.size() > 1) {
                    List<TickerData> tickerDataList = new ArrayList<>();
                    for (int i=0; i<additionalInfoUiModelList.size(); i++) {
                        AdditionalInfoUiModel additionalInfoUiModel = additionalInfoUiModelList.get(i);
                        String formattedDesc = formatTitleHtml(additionalInfoUiModel.getNotes(), additionalInfoUiModel.getUrlDetail(), additionalInfoUiModel.getUrlText());
                        TickerData tickerData = new TickerData(additionalInfoUiModel.getTitle(), formattedDesc, Ticker.TYPE_ANNOUNCEMENT, true);
                        tickerDataList.add(tickerData);
                    }
                    TickerPagerAdapter tickerPagerAdapter = new TickerPagerAdapter(getContext(), tickerDataList);
                    tickerPagerAdapter.setPagerDescriptionClickEvent((charSequence, o) -> {
                        RouteManager.route(getContext(), String.format("%s?url=%s", ApplinkConst.WEBVIEW, charSequence));
                    });
                    tickerInfoCourier.addPagerView(tickerPagerAdapter, tickerDataList);

                } else {
                    AdditionalInfoUiModel additionalInfoUiModel = additionalInfoUiModelList.get(0);
                    String formattedDesc = formatTitleHtml(additionalInfoUiModel.getNotes(), additionalInfoUiModel.getUrlDetail(), additionalInfoUiModel.getUrlText());
                    tickerInfoCourier.setHtmlDescription(formattedDesc);
                    tickerInfoCourier.setTickerTitle(additionalInfoUiModel.getTitle());
                    tickerInfoCourier.setTickerType(Ticker.TYPE_ANNOUNCEMENT);
                    tickerInfoCourier.setTickerShape(Ticker.SHAPE_LOOSE);
                    tickerInfoCourier.setDescriptionClickEvent(new TickerCallback() {
                        @Override
                        public void onDescriptionViewClick(@NotNull CharSequence charSequence) {
                            RouteManager.route(getContext(), String.format("%s?url=%s", ApplinkConst.WEBVIEW, charSequence));
                        }

                        @Override
                        public void onDismiss() {

                        }
                    });
                }
            }
        } else {
            tickerInfoLayout.setVisibility(View.GONE);
        }
    }

    private String formatTitleHtml(String desc, String urlText, String url) {
        return String.format("%s <a href=\"%s\">%s</a>", desc, urlText, url);
    }

    private void setEmptyHistoryView(TrackingUiModel model) {
        if (model.isInvalid()) {
            emptyUpdateNotification.setVisibility(View.VISIBLE);
            notificationText.setText(getString(R.string.warning_courier_invalid));
            notificationHelpStep.setVisibility(View.VISIBLE);
            notificationHelpStep.setLayoutManager(new LinearLayoutManager(getActivity()));
            notificationHelpStep.setAdapter(new EmptyTrackingNotesAdapter());
        } else if (model.getStatusNumber() == TrackingUiModel.ORDER_STATUS_WAITING
                || model.getChange() == 0 || model.getHistoryList().size() == 0) {
            emptyUpdateNotification.setVisibility(View.VISIBLE);
            notificationText.setText(getString(R.string.warning_no_courier_change));
            notificationHelpStep.setVisibility(View.GONE);
        } else {
            emptyUpdateNotification.setVisibility(View.GONE);
            notificationHelpStep.setVisibility(View.GONE);
        }
    }

    private void setLiveTrackingButton(TrackingUiModel model) {
        if (TextUtils.isEmpty(mTrackingUrl) && TextUtils.isEmpty(model.getTrackingUrl()))
            liveTrackingButton.setVisibility(View.GONE);
        else {
            liveTrackingButton.setVisibility(View.VISIBLE);
            liveTrackingButton.setOnClickListener(onLiveTrackingClickedListener(model));
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
                .trackingPageModule(new TrackingPageModule(this, getContext()))
                .build();
        component.inject(this);
    }

    private void initTimer(long remainingSeconds) {
        if (remainingSeconds <= 0) return;
        long timeInMillis = remainingSeconds * 1000;
        String strFormat = (getContext() != null) ?
                getContext().getString(R.string.retry_dateline_info) : "";
        mAnalytics.eventViewLabelTungguRetry(
                DateUtils.formatElapsedTime(timeInMillis / 1000), mOrderId);
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
                fetchData();
            }
        };
        mCountDownTimer.start();
    }

    private View.OnClickListener onLiveTrackingClickedListener(TrackingUiModel model) {
        return view -> {
            mAnalytics.eventClickOrderTrackingClickButtonLiveTracking();
            if (getContext() != null) {
                String trackingUrl = mTrackingUrl;
                if (TextUtils.isEmpty(trackingUrl)) {
                    trackingUrl = model.getTrackingUrl();
                }
                startActivityForResult(LiveTrackingActivity.Companion.createIntent(getContext(), trackingUrl), LIVE_TRACKING_VIEW_REQ);
            }
        };
    }

    private void fetchData() {
        presenter.onGetTrackingData(mOrderId);
        if (mTrackingUrl != null && !mTrackingUrl.isEmpty()
                && mCaller != null && mCaller.equalsIgnoreCase("seller")) {
            presenter.onGetRetryAvailability(mOrderId);
        }
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
