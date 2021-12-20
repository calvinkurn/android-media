package com.tokopedia.tkpd.thankyou.view;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.core.app.JobIntentService;

import com.tokopedia.app.common.MainApplication;
import com.tokopedia.app.common.di.CommonAppComponent;
import com.tokopedia.tkpd.thankyou.di.component.DaggerThanksTrackerComponent;
import com.tokopedia.tkpd.thankyou.view.viewmodel.ThanksTrackerData;

import javax.inject.Inject;


public class ThanksTrackerService extends JobIntentService {
    public static final String DATA = "ThanksTrackerData";
    private static final int THANKSTRACKER_JOB_ID = 1000;

    @Inject
    ThanksTracker.Presenter presenter;

    public static void start(Context context, ThanksTrackerData data) {
        Intent intent = new Intent(context, ThanksTrackerService.class);
        intent.putExtra(DATA, data);

        enqueueWork(context, ThanksTrackerService.class, THANKSTRACKER_JOB_ID, intent);
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    protected void onHandleWork(@NonNull Intent intent) {
        if (intent != null) {
            ThanksTrackerData data = intent.getParcelableExtra(DATA);
            if (isDataValid(data)) {
                initInjection();
                presenter.doAnalytics(data);
            }
        }
    }

    private boolean isDataValid(ThanksTrackerData data) {
        if (data != null && data.getPlatform() != null && data.getPlatform().equals("marketplace")) {
            return data.getId() != null
                    && !data.getId().isEmpty()
                    && !data.getPlatform().isEmpty()
                    && data.getShopTypes() != null;
        } else if (data != null && data.getPlatform() != null && data.getPlatform().equals("digital")) {
            return data.getId() != null
                    && !data.getId().isEmpty()
                    && !data.getPlatform().isEmpty();
        } else {
            return false;
        }
    }

    private void initInjection() {
        CommonAppComponent appComponent = ((MainApplication) getApplication()).getAppComponent();
        DaggerThanksTrackerComponent component =
                (DaggerThanksTrackerComponent) DaggerThanksTrackerComponent.builder()
                        .commonAppComponent(appComponent)
                        .build();

        component.inject(this);
    }
}
