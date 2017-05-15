package com.tokopedia.topadsmobilesdk;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.DisplayMode;
import com.tokopedia.topads.sdk.view.TopAdsView;

public class MainActivity extends AppCompatActivity implements
        TopAdsItemClickListener, TopAdsListener {

    private static final String TAG = MainActivity.class.getSimpleName();
    private TopAdsView topAdsView;
    private Button changeGridBtn;
    private Button changeListBtn;
    private Button showShopBtn;
    private Button showProductBtn;
    private EditText pageTxt;
    private TopAdsParams params = new TopAdsParams();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        topAdsView = (TopAdsView) findViewById(R.id.topAds);
        pageTxt = (EditText) findViewById(R.id.page);

        Config topAdsConfig = new Config.Builder()
                .setUserId("5479860")
                .setSessionId("elZDX-HmqSM:APA91bEEF9pYzAkSH8Y_AfuiDd-XEYPSkgyKuZq9FH3nL3BJtXNoiqv3TporXPGbxTQizQdfLQAuudAfjHC9HUKySvrf2RYXbtEm_aB7GtInGE9PQztitsaz6h2uuivyv7eMZE97PH5k")
                .build();
        topAdsView.setAdsItemClickListener(this);
        topAdsView.setAdsListener(this);
        topAdsView.setConfig(topAdsConfig);
        topAdsView.loadTopAds();

        changeListBtn = (Button) findViewById(R.id.listBtn);
        changeGridBtn = (Button) findViewById(R.id.gridBtn);
        showProductBtn = (Button) findViewById(R.id.product);
        showShopBtn = (Button) findViewById(R.id.shop);

        changeListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topAdsView.setDisplayMode(DisplayMode.LIST);
            }
        });

        changeGridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                topAdsView.setDisplayMode(DisplayMode.GRID);
            }
        });

        showShopBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pageTxt.getText().toString().isEmpty()){
                    params.getParam().put(TopAdsParams.KEY_PAGE, pageTxt.getText().toString());
                } else {
                    params.getParam().put(TopAdsParams.KEY_PAGE, "0");
                }
                topAdsView.setTopAdsParams(params);
                topAdsView.showShop();
            }
        });

        showProductBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!pageTxt.getText().toString().isEmpty()){
                    params.getParam().put(TopAdsParams.KEY_PAGE, pageTxt.getText().toString());
                } else {
                    params.getParam().put(TopAdsParams.KEY_PAGE, "0");
                }
                topAdsView.setTopAdsParams(params);
                topAdsView.showProduct();
            }
        });

    }

    @Override
    public void onProductItemClicked(Product product) {
        Toast.makeText(this, "Item TopAds Clicked name "+product.getName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Toast.makeText(this, "Item TopAds Clicked name "+shop.getName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddFavorite(Shop shop) {
        Toast.makeText(this, shop.getName()+" add to favorite ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTopAdsLoaded() {
        Log.d(TAG, "TopAds Loaded");
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        Log.d(TAG, "Error Load TopAds error code "+errorCode+" message "+message);
    }
}
