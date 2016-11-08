package com.tokopedia.core.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.tkpd.library.utils.data.DataManagerImpl;
import com.tkpd.library.utils.data.DataReceiver;
import com.tokopedia.core.database.model.Bank;
import com.tokopedia.core.database.model.CategoryDB;
import com.tokopedia.core.database.model.City;
import com.tokopedia.core.database.model.District;
import com.tokopedia.core.database.model.Province;
import com.tokopedia.core.rxjava.RxUtils;
import com.tokopedia.core.service.constant.HadesConstant;

import java.util.List;

import rx.subscriptions.CompositeSubscription;

/**
 * Created by ricoharisin on 4/28/16.
 */
public class HadesService extends IntentService implements DataReceiver {

    public static final int STATUS_RUNNING = 0;
    public static final int STATUS_FINISHED = 1;
    public static final int STATUS_TIMEOUT = 2;
    public static final int STATUS_NO_CONNECTION = 3;

    public static final String TYPE = "type";
    public static final String RECEIVER = "receiver";
    public static final String STATUS = "status";

    public static final String ACTION_FETCH_DEPARTMENT = "fetch_department";

    public static Boolean getIsHadesRunning() {
        return IS_HADES_RUNNING;
    }


    private static Boolean IS_HADES_RUNNING = false;

    private CompositeSubscription compositeSubscription = new CompositeSubscription();

    protected int State = 0;

    public HadesService() {
        super("hades service");
    }

    public static void startDownload(Context context, int state) {
        Intent intent = new Intent(Intent.ACTION_SYNC, null, context, HadesService.class);
        intent.putExtra(TYPE, state);

        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        IS_HADES_RUNNING = true;
        State = intent.getIntExtra(TYPE, 0);
        Log.i("HADES TAG", "LAGI JALAAAN NEEEH");
        switch(State) {
            case HadesConstant.STATE_DEPARTMENT:
                Bundle bundle = new Bundle();
                bundle.putInt(TYPE, State);
                Intent localIntent = new Intent(ACTION_FETCH_DEPARTMENT).putExtra(STATUS, STATUS_RUNNING);
                LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
                DataManagerImpl.getDataManager()
                        .getListDepartment2(this.getApplication(),
                                this, 0, true);
                break;

        }
    }

    @Override
    public CompositeSubscription getSubscription() {
        return RxUtils.getNewCompositeSubIfUnsubscribed(compositeSubscription);
    }

    @Override
    public void setDistricts(List<District> districts) {

    }

    @Override
    public void setCities(List<City> cities) {

    }

    @Override
    public void setProvinces(List<Province> provinces) {

    }

    @Override
    public void setBank(List<Bank> banks) {

    }

    @Override
    public void setDepartments(List<CategoryDB> departments) {
        Bundle bundle = new Bundle();
        bundle.putInt("type", State);
        Intent localIntent = new Intent(ACTION_FETCH_DEPARTMENT).putExtra(STATUS, STATUS_FINISHED);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
        Log.i("HADES TAG", "KELAR FETCH DEPARTMENTNYAA");
    }

    @Override
    public void setShippingCity(List<District> districts) {

    }

    @Override
    public void onNetworkError(String message) {
        Intent localIntent = new Intent(ACTION_FETCH_DEPARTMENT).putExtra(STATUS, STATUS_NO_CONNECTION);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    @Override
    public void onMessageError(String message) {
        Log.i("HADES TAG", "ERRORRRR: "+message);
    }

    @Override
    public void onUnknownError(String message) {

    }

    @Override
    public void onTimeout() {
        Intent localIntent = new Intent(ACTION_FETCH_DEPARTMENT).putExtra(STATUS, STATUS_TIMEOUT);
        LocalBroadcastManager.getInstance(this).sendBroadcast(localIntent);
    }

    @Override
    public void onFailAuth() {

    }

    @Override
    public void onDestroy() {
        IS_HADES_RUNNING = false;
    }
}
