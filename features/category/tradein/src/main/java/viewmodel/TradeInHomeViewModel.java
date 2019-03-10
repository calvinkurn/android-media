package viewmodel;

import android.Manifest;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.OnLifecycleEvent;
import android.arch.lifecycle.ViewModel;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.widget.Toast;

import com.laku6.tradeinsdk.api.Laku6TradeIn;

import org.json.JSONObject;

import java.lang.ref.WeakReference;

public class TradeInHomeViewModel extends ViewModel implements LifecycleObserver, Laku6TradeIn.TradeInListener {
    private MutableLiveData<String> eligibileTickerData;
    private MutableLiveData<JSONObject> minPriceData;
    private MutableLiveData<JSONObject> priceFailData;

    private WeakReference<FragmentActivity> activityWeakReference;
    private Laku6TradeIn laku6TradeIn;
    public static final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;

    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String result = intent.getStringExtra("test-result");
        }
    };
    private BroadcastReceiver mBackReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            // Get extra data included in the Intent. DO BACK TO PARENT
            Log.d("receiver", "Do back action to parent");
        }
    };

    public TradeInHomeViewModel(FragmentActivity activity) {
        activityWeakReference = new WeakReference<>(activity);
        eligibileTickerData = new MutableLiveData<>();
        minPriceData = new MutableLiveData<>();
        priceFailData= new MutableLiveData<>();
    }

    public MutableLiveData<String> getEligibileTickerData() {
        return eligibileTickerData;
    }

    public MutableLiveData<JSONObject> getMinPriceData() {
        return minPriceData;
    }

    public MutableLiveData<JSONObject> getPriceFailData() {
        return priceFailData;
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    public void getPriceFromSDK() {
        laku6TradeIn = Laku6TradeIn.getInstance(activityWeakReference.get());
        laku6TradeIn.setCampaignTradeInId("tokopediaSandbox");
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_START)
    public void registerReceivers() {
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            LocalBroadcastManager.getInstance(activityWeakReference.get()).registerReceiver(mMessageReceiver, new IntentFilter("laku6-test-end"));
            LocalBroadcastManager.getInstance(activityWeakReference.get()).registerReceiver(mBackReceiver, new IntentFilter("laku6-back-action"));
            requestPermission();
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    public void unRegisterReceivers() {
        if (activityWeakReference != null && activityWeakReference.get() != null) {
            LocalBroadcastManager.getInstance(activityWeakReference.get()).unregisterReceiver(mMessageReceiver);
            LocalBroadcastManager.getInstance(activityWeakReference.get()).unregisterReceiver(mBackReceiver);
        }
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        minPriceData.setValue(jsonObject);
    }


    @Override
    public void onError(JSONObject jsonObject) {
        priceFailData.setValue(jsonObject);
    }

    public void startGUITest() {
        laku6TradeIn.startGUITest();

    }

    public void requestPermission() {
        if (!laku6TradeIn.permissionGranted()) {
            ActivityCompat.requestPermissions(activityWeakReference.get(),
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            getMaxPrice();
        }
    }

    public void getMaxPrice() {
        Toast.makeText(activityWeakReference.get(), "Calling Max Price API", Toast.LENGTH_SHORT).show();
        laku6TradeIn.getMinMaxPrice(this);
    }
}
