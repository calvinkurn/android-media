package com.tokopedia.topadsmobilesdk;

import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.tokopedia.topads.sdk.base.Config;
import com.tokopedia.topads.sdk.domain.TopAdsParams;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.domain.model.Shop;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsListener;
import com.tokopedia.topads.sdk.view.adapter.TopAdsRecyclerAdapter;

import java.util.ArrayList;

/**
 * @author by errysuprayogi on 4/10/17.
 */

public class ListActivity extends AppCompatActivity implements TopAdsItemClickListener, TopAdsListener {


    private static final String TAG = ListActivity.class.getSimpleName();
    private RecyclerView recyclerView;
    private TestAdapter adapter;
    private Button changeGridBtn;
    private Button changeListBtn;
    private static final int PORTRAIT_COLUMN = 1;

    private RecyclerView.LayoutManager layoutManager;
    private LinearLayoutManager linearLayoutManager;
    private GridLayoutManager gridLayoutManager;
    private TopAdsRecyclerAdapter adsRecyclerAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        linearLayoutManager = new LinearLayoutManager(this);
        gridLayoutManager = new GridLayoutManager(ListActivity.this, 2, GridLayoutManager.VERTICAL, false);

        changeGridBtn = (Button) findViewById(R.id.gridBtn);
        changeListBtn = (Button) findViewById(R.id.listBtn);
        recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        adapter = new TestAdapter(this, new ArrayList<RecyclerViewItem>());
        adapter.setHasStableIds(true);

        adsRecyclerAdapter = new TopAdsRecyclerAdapter(this, adapter);
        adsRecyclerAdapter.setTopAdsListener(this);
        adsRecyclerAdapter.setAdsItemClickListener(this);
        adsRecyclerAdapter.setSpanSizeLookup(onSpanSizeLookup());

        TopAdsParams adsParams = new TopAdsParams();
        adsParams.getParam().put(TopAdsParams.KEY_DEPARTEMENT_ID, "66");
        adsRecyclerAdapter.setTopAdsParams(adsParams);
        Config topAdsConfig = new Config.Builder()
                .setSessionId("cmqKKiZDXdw:APA91bEegZcC1X2BXAwDg6fbc5yrkU7sb2Kw1JGi8kfqUObXUY1pzU29PtTeIE3DJBX5YIeYoHhchw30N9_3VynqCexuVei8Gxs2lFyO11BoRBm9FJM6ixiNY5UPzVJhoc59Acmrb0oT")
                .setUserId("3589675")
                .withPreferedCategory()
                .build();
        adsRecyclerAdapter.loadTopAds(topAdsConfig);

        adsRecyclerAdapter.setOnLoadListener(new TopAdsRecyclerAdapter.OnLoadListener() {
            @Override
            public void onLoad(int page, int totalCount) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Log.d(TAG, "Unset Loading..");
                        adsRecyclerAdapter.hideLoading();
                        adapter.addData(provideData());
                    }
                }, 1000);
            }
        });
        recyclerView.setAdapter(adsRecyclerAdapter);

        adsRecyclerAdapter.setHasHeader(true);
        adapter.addHeader();
        adapter.setDatas(provideData());


        changeGridBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                int pos = linearLayoutManager.findFirstVisibleItemPosition();
//                adsRecyclerAdapter.setLayoutManager(gridLayoutManager);
//                recyclerView.scrollToPosition(pos);
                x = 0;
                adapter.addHeader();
                adapter.setDatas(provideData());
            }
        });
        changeListBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int pos = gridLayoutManager.findFirstVisibleItemPosition();
                adsRecyclerAdapter.setLayoutManager(linearLayoutManager);
                recyclerView.scrollToPosition(pos);
            }
        });

    }

    public boolean isLoading() {
        return adapter.getItemViewType(linearLayoutManager.findLastCompletelyVisibleItemPosition()) == TkpdState.RecyclerView.VIEW_LOADING;
    }

    int spanCount = 2;

    private GridLayoutManager.SpanSizeLookup onSpanSizeLookup() {
        return new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (adsRecyclerAdapter.isTopAdsViewHolder(position)
                        || adsRecyclerAdapter.isLoading(position)
                        || position == 0) {
                    return 2;
                } else {
                    return 1;
                }
            }
        };
    }

    @Override
    public void onProductItemClicked(Product product) {
        Toast.makeText(this, "Item TopAds Clicked name " + product.getName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onShopItemClicked(Shop shop) {
        Toast.makeText(this, "Item TopAds Clicked name " + shop.getName(),
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAddFavorite(Shop shop) {
        Toast.makeText(this, shop.getName() + " add to favorite ", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onTopAdsLoaded() {
        Log.d(TAG, "TopAds Loaded");
    }

    @Override
    public void onTopAdsFailToLoad(int errorCode, String message) {
        Log.d(TAG, "Error Load TopAds error code " + errorCode + " message " + message);
    }

    int x = 0;

    private ArrayList<DummyData> provideData() {
        ArrayList<DummyData> datas = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            datas.add(new DummyData("Content Item #" + x, TkpdState.RecyclerView.VIEW_PRODUCT));
            x++;
        }
        return datas;
    }
}
