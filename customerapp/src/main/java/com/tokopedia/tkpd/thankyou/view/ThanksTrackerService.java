package com.tokopedia.tkpd.thankyou.view;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;

import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.base.di.component.AppComponent;
import com.tokopedia.tkpd.thankyou.di.component.DaggerThanksTrackerComponent;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;

import javax.inject.Inject;


public class ThanksTrackerService extends IntentService {
    public static final String DATA = "ThanksTrackerData";

    @Inject
    ThanksTracker.Presenter presenter;

    public static void start(Context context, ThanksTrackerData data) {
        Intent intent = new Intent(context, ThanksTrackerService.class);
        intent.putExtra(DATA, data);

        context.startService(intent);
    }

    public ThanksTrackerService() {
        super("ThanksTrackerService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if(intent != null) {
            ThanksTrackerData data = intent.getParcelableExtra(DATA);
            if(isDataValid(data)) {
                initInjection();
                presenter.doAnalytics(data);
            }
        }
    }

    private boolean isDataValid(ThanksTrackerData data) {
        if (data != null && data.getPlatform() != null && data.getPlatform().equals("marketplace")){
            return data.getId() != null
                    && !data.getId().isEmpty()
                    && !data.getPlatform().isEmpty()
                    && data.getShopTypes() != null;
        } else if (data != null && data.getPlatform() != null && data.getPlatform().equals("digital")){
            return data.getId() != null
                    && !data.getId().isEmpty()
                    && !data.getPlatform().isEmpty();
        } else {
            return false;
        }
    }

    private void initInjection() {
        AppComponent appComponent = ((MainApplication) getApplication()).getAppComponent();
        DaggerThanksTrackerComponent component =
                (DaggerThanksTrackerComponent) DaggerThanksTrackerComponent.builder()
                        .appComponent(appComponent)
                        .build();

        component.inject(this);
    }
}
