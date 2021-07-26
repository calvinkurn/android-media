package com.tokopedia.core.common.ui;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.constant.TkpdCache;
import com.tokopedia.core.TkpdCoreRouter;
import com.tokopedia.core.network.R;
import com.tokopedia.core.network.apiservices.search.HotListService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.logger.ServerLogger;
import com.tokopedia.logger.utils.Priority;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;
import timber.log.Timber;

/**
 * This class need to be validated
 */
@Deprecated
public class MaintenancePage extends Activity {

    private static String IS_NETWORK = "is_network";
    private static String UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    private ViewHolder holder;
    private String maintenanceMessage;

    public static Intent createIntentFromNetwork(Context context) {
        Intent intent = new Intent(context, MaintenancePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(IS_NETWORK, true);
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "createIntentFromNetwork");
        messageMap.put("data", "");
        ServerLogger.log(Priority.P1, "MAINTENANCE_PAGE", messageMap);
        return intent;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MaintenancePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(IS_NETWORK, false);
        intent.putExtra("message", getMessage(context));
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "createIntent");
        messageMap.put("data", "");
        ServerLogger.log(Priority.P1, "MAINTENANCE_PAGE", messageMap);
        return intent;
    }

    private static String getMessage(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.MAINTENANCE);
        return cache.getString(TkpdCache.Key.MESSAGE, "");
    }

    public static boolean isMaintenance(Context context) {
        LocalCacheHandler cache = new LocalCacheHandler(context, TkpdCache.MAINTENANCE);
        return cache.getBoolean(TkpdCache.Key.STATUS2, false);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maintenance_page);
        initVar();
        initView();
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "onCreateShow");
        messageMap.put("data", "");
        ServerLogger.log(Priority.P1, "MAINTENANCE_PAGE", messageMap);
    }

    private void initVar() {
        holder = new ViewHolder();
        maintenanceMessage = getIntent().getExtras().getString("message", "");
    }

    private void initView() {
        holder.notif = (TextView) findViewById(R.id.notification);
        holder.progress = (ProgressBar) findViewById(R.id.checking);
        if (!maintenanceMessage.equals("")) holder.notif.setText(maintenanceMessage);
    }

    private void setApplicationToMaintenance() {
        LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.MAINTENANCE);
        cache.putBoolean(TkpdCache.Key.STATUS2, true);
        cache.putString(TkpdCache.Key.MESSAGE, maintenanceMessage);
        cache.applyEditor();
    }

    private void checkServerMaintenanceStatus() {
        Map<String, String> messageMap = new HashMap<>();
        messageMap.put("type", "checkServerMaintenanceStatusLoad");
        messageMap.put("data", "");
        ServerLogger.log(Priority.P1, "MAINTENANCE_PAGE", messageMap);
        Map<String, String> param = new HashMap<>();
        new HotListService().getApi().getHotList(AuthUtil.generateParams(this, param))
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.newThread())
                .subscribe(new Subscriber<Response<TkpdResponse>>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Response<TkpdResponse> tkpdResponseResponse) {
                        String status = "";
                        if (tkpdResponseResponse.body() != null) {
                            status = tkpdResponseResponse.body().getStatus();
                        }
                        Map<String, String> messageMap = new HashMap<>();
                        messageMap.put("type", "checkServerMaintenanceStatusResult");
                        messageMap.put("data", status);
                        ServerLogger.log(Priority.P1, "MAINTENANCE_PAGE", messageMap);

                        if (status.equals(UNDER_MAINTENANCE)) {
                            hideProgressBar();
                        } else {
                            setMaintenanceDone();
                            goToIndexHome();
                        }
                    }
                });

    }

    private void setMaintenanceDone() {
        LocalCacheHandler cache = new LocalCacheHandler(this, TkpdCache.MAINTENANCE);
        cache.putBoolean(TkpdCache.Key.STATUS2, false);
        cache.applyEditor();
    }

    private void goToIndexHome() {
        startActivity(((TkpdCoreRouter) getApplication()).getHomeIntent(this));
        finish();
    }

    private void hideProgressBar() {
        holder.progress.setVisibility(View.GONE);
        holder.notif.setVisibility(View.VISIBLE);
    }

    private void showProgressBar() {
        holder.progress.setVisibility(View.VISIBLE);
        holder.notif.setVisibility(View.GONE);
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (getIntent().getBooleanExtra(IS_NETWORK, false)) {
            hideProgressBar();
            setApplicationToMaintenance();
            getIntent().putExtra(IS_NETWORK, false);
        } else {
            showProgressBar();
            checkServerMaintenanceStatus();
        }
    }

    private class ViewHolder {
        TextView notif;
        ProgressBar progress;
    }

}
