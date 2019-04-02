package com.tokopedia.core;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.network.apiservices.search.HotListService;
import com.tokopedia.core.network.retrofit.response.TkpdResponse;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core2.R;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Response;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


public class MaintenancePage extends Activity {

    private static String IS_NETWORK = "is_network";
    private static String UNDER_MAINTENANCE = "UNDER_MAINTENANCE";
    private ViewHolder holder;
    private String maintenanceMessage;

    public static Intent createIntentFromNetwork(Context context, String msg) {
        Intent intent = new Intent(context, MaintenancePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(IS_NETWORK, true);
        intent.putExtra("message", msg);
        return intent;
    }

    public static Intent createIntentFromNetwork(Context context) {
        Intent intent = new Intent(context, MaintenancePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(IS_NETWORK, true);
        return intent;
    }

    public static Intent createIntent(Context context) {
        Intent intent = new Intent(context, MaintenancePage.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_NO_ANIMATION);
        intent.putExtra(IS_NETWORK, false);
        intent.putExtra("message", getMessage(context));
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
                        Log.i("NETWORK RICO", "dapet respon nehe: " + tkpdResponseResponse.body().getStatus());
                        if (tkpdResponseResponse.body().getStatus().equals(UNDER_MAINTENANCE)) {
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
        startActivity(HomeRouter.getHomeActivityInterfaceRouter(this));
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
