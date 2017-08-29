package com.tokopedia.posapp.view.service;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;

import com.tokopedia.posapp.domain.usecase.GetProductListUseCase;

/**
 * @author okasurya on 8/28/2017
 */
public class FetcherService extends IntentService {
    private static final String ACTION_START = "com.tokopedia.posapp.view.service.action.START";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.tokopedia.posapp.view.service.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.tokopedia.posapp.view.service.extra.PARAM2";

    GetProductListUseCase getProductListUseCase;

    public FetcherService() {
        super("FetcherService");
    }

    public static Intent getServiceIntent(Context context) {
        Intent intent = new Intent(context, FetcherService.class);
        intent.setAction(ACTION_START);
        intent.putExtra(EXTRA_PARAM1, "");
        intent.putExtra(EXTRA_PARAM2, "");
        return intent;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_START.equals(action)) {
                final String param1 = intent.getStringExtra(EXTRA_PARAM1);
                final String param2 = intent.getStringExtra(EXTRA_PARAM2);
                handleActionStart(param1, param2);
            }
        }
    }

    private void handleActionStart(String param1, String param2) {
        // start the service here
    }
}
