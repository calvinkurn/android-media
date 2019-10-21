package com.tokopedia.nps.presentation.view.dialog;

import android.view.View;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.nps.NpsAnalytics;
import com.tokopedia.nps.R;
import com.tokopedia.nps.presentation.di.DaggerFeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackComponent;
import com.tokopedia.nps.presentation.di.FeedbackModule;
import com.tokopedia.nps.presentation.presenter.FeedbackPresenter;
import com.tokopedia.remoteconfig.RemoteConfigKey;

import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

public class AppFeedbackDialog extends BottomSheets {

    protected static final String HIDE_FEEDBACK_RATING = "hide_feedback_rating";
    protected static final String LABEL_CLICK_ADVANCED_APP_RATING = "ClickAdvancedAppRating: ";
    protected static final String LABEL_CANCEL_ADVANCED_APP_RATING = "CancelAdvancedAppRating";
    protected static final String DEFAULT_CACHE_VALUE = "1";
    protected static final long EXPIRED_DURATION = TimeUnit.DAYS.toMillis(7); // expired in 7 days

    @Inject
    NpsAnalytics npsAnalytics;

    @Inject
    FeedbackPresenter presenter;

    @Override
    public int getBaseLayoutResourceId() {
        return R.layout.dialog_feedback_base;
    }

    @Override
    protected BottomSheetsState state() {
        return BottomSheetsState.FLEXIBLE;
    }

    @Override
    protected String title() {
        return "";
    }

    @Override
    public int getLayoutResourceId() {
        return 0;
    }

    @Override
    public void initView(View view) {
        initInjector();
    }

    protected String getConfigKey() {
        return GlobalConfig.isSellerApp()
                ? RemoteConfigKey.SELLERAPP_SHOW_ADVANCED_APP_RATING
                : RemoteConfigKey.MAINAPP_SHOW_ADVANCED_APP_RATING;
    }

    protected String getAppType() {
        return GlobalConfig.isSellerApp()
                ? GlobalConfig.PACKAGE_SELLER_APP
                : GlobalConfig.PACKAGE_CONSUMER_APP;
    }

    private void initInjector() {
        FeedbackComponent component = DaggerFeedbackComponent.builder()
                .feedbackModule(new FeedbackModule(getContext()))
                .build();
        component.inject(this);
    }
}
