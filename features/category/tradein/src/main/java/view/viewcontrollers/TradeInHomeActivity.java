package view.viewcontrollers;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tradein.R;
import com.laku6.tradeinsdk.api.Laku6TradeIn;

import org.json.JSONException;
import org.json.JSONObject;

public class TradeInHomeActivity extends BaseTradeInActivity implements Laku6TradeIn.TradeInListener {

    private Laku6TradeIn laku6TradeIn;
    private final int MY_PERMISSIONS_REQUEST_READ_PHONE_STATE = 123;
    private TextView mTvPriceElligible;
    private ImageView mButtonRemove;
    private TextView mTvModelName;
    private TextView mTvHeaderPrice;
    private TextView mTvInitialPrice;
    private TextView mTvGoToProductDetails;

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

    @Override
    void initView() {
        laku6TradeIn = Laku6TradeIn.getInstance(this);
        laku6TradeIn.setCampaignTradeInId("tokopediaSandbox");
        mTvPriceElligible = findViewById(R.id.tv_price_elligible);
        mButtonRemove = findViewById(R.id.button_remove);
        mTvModelName = findViewById(R.id.tv_model_name);
        mTvHeaderPrice = findViewById(R.id.tv_header_price);
        mTvInitialPrice = findViewById(R.id.tv_initial_price);
        mTvGoToProductDetails = findViewById(R.id.tv_go_to_product_details);
        mTvModelName.setText(new StringBuffer().append(Build.BRAND).append(Build.MODEL).toString());
        requestPermission();
    }

    @Override
    protected void onStart() {
        super.onStart();
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, new IntentFilter("laku6-test-end"));
        LocalBroadcastManager.getInstance(this).registerReceiver(mBackReceiver, new IntentFilter("laku6-back-action"));
    }

    @Override
    int getMenuRes() {
        return 0;
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.layout_activity_tradeinhome;
    }

    @Override
    int getBottomSheetLayoutRes() {
        return 0;
    }

    @Override
    boolean doNeedReattach() {
        return false;
    }

    @Override
    protected Fragment getNewFragment() {
        return null;
    }

    @Override
    public void onFinished(JSONObject jsonObject) {
        /*"min_price":450000,
"max_price":1500000,
"model_id":786,
"brand":"Xiaomi",
"model": "Mi A1",
"model_display_name":"Xiaomi Mi A1"*/
        try {
            hideProgressBar();
            mTvGoToProductDetails.setText(getString(R.string.text_check_functionality));
            mTvGoToProductDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    laku6TradeIn.startGUITest();
                }
            });
            mTvInitialPrice.setText(String.valueOf(jsonObject.getInt("max_price")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onError(JSONObject jsonObject) {
        hideProgressBar();
        try {
            mTvInitialPrice.setText(jsonObject.getString("message"));
            mTvGoToProductDetails.setText(R.string.go_to_product_details);
            mTvGoToProductDetails.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void requestPermission() {
        if (!laku6TradeIn.permissionGranted()) {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.READ_PHONE_STATE},
                    MY_PERMISSIONS_REQUEST_READ_PHONE_STATE);
        } else {
            getMaxPrice();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_PHONE_STATE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    getMaxPrice();
                } else {
                    requestPermission();
                }
            }
        }
    }

    private void getMaxPrice() {
        Toast.makeText(this, "Calling Max Price API", Toast.LENGTH_SHORT).show();
        showProgressBar();
        laku6TradeIn.getMinMaxPrice(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mMessageReceiver);
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mBackReceiver);
    }
}
