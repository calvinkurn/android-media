package com.tokopedia.tkpd.home.thankyou.view;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.home.thankyou.di.component.DaggerThanksAnalyticsComponent;
import com.tokopedia.tkpd.home.thankyou.view.viewmodel.ThanksAnalyticsData;

import javax.inject.Inject;


public class ThanksAnalyticsService extends IntentService {
    public static final String DATA = "ThanksAnalyticsData";

    @Inject
    ThanksAnalytics.Presenter presenter;

    public static void start(Context context, ThanksAnalyticsData data) {
        Intent intent = new Intent(context, ThanksAnalyticsService.class);
        intent.putExtra(DATA, data);
        context.startService(intent);
    }

    public ThanksAnalyticsService() {
        super("ThanksAnalyticsService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        initInjection();

        if(intent != null) {
            ThanksAnalyticsData data = intent.getParcelableExtra(DATA);
            if(data != null) {
                presenter.doAnalytics(data);
            }
        }
    }

    private void initInjection() {
        AppComponent appComponent = ((MainApplication) getApplication()).getAppComponent();
        DaggerThanksAnalyticsComponent component =
                (DaggerThanksAnalyticsComponent) DaggerThanksAnalyticsComponent.builder()
                        .appComponent(appComponent)
                        .build();

        component.inject(this);
    }
}
