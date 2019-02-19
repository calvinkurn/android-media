package com.tokopedia.topads.sdk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.analytics.TopAdsGtmTracker;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.domain.interactor.OpenTopAdsUseCase;
import com.tokopedia.topads.sdk.di.DaggerTopAdsComponent;
import com.tokopedia.topads.sdk.di.TopAdsComponent;
import com.tokopedia.topads.sdk.domain.model.Data;
import com.tokopedia.topads.sdk.domain.model.Product;
import com.tokopedia.topads.sdk.listener.LocalAdsClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemClickListener;
import com.tokopedia.topads.sdk.listener.TopAdsItemImpressionListener;
import com.tokopedia.topads.sdk.presenter.TopAdsPresenter;
import com.tokopedia.topads.sdk.utils.GridSpaceItemDecoration;
import com.tokopedia.topads.sdk.view.TopAdsInfoBottomSheetDynamicChannel;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;
import java.util.List;

import javax.inject.Inject;

public class TopAdsDynamicChannelView extends LinearLayout implements View.OnClickListener,
        LocalAdsClickListener {

    private static final String TAG = TopAdsDynamicChannelView.class.getSimpleName();
    public static final int SPAN_COUNT = 3;
    private RecyclerView recyclerView;
    private AdsItemAdapter itemAdapter;
    private TextView titleTxt;
    private ImageView infoCta;
    private TopAdsInfoBottomSheetDynamicChannel infoBottomSheet;
    private TopAdsItemClickListener adsItemClickListener;
    private TopAdsItemImpressionListener impressionListener;
    private OpenTopAdsUseCase openTopAdsUseCase;


    @Inject
    TopAdsPresenter presenter;

    public TopAdsDynamicChannelView(Context context) {
        super(context);
        inflateView(context, null, 0);
        initInjector();
    }

    public TopAdsDynamicChannelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
        initInjector();
    }

    public TopAdsDynamicChannelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
        initInjector();
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_ads_dynamic_channel, this);
        recyclerView = findViewById(R.id.list);
        infoCta = findViewById(R.id.info_cta);
        titleTxt = findViewById(R.id.channel_title);
        openTopAdsUseCase = new OpenTopAdsUseCase(context);
        itemAdapter = new AdsItemAdapter(getContext());
        itemAdapter.setItemClickListener(this);
        itemAdapter.setAdsItemImpressionListener(new TopAdsItemImpressionListener() {
            @Override
            public void onImpressionProductAdsItem(int position, Product product) {
                if(impressionListener!=null){
                    impressionListener.onImpressionProductAdsItem(position, product);
                }
            }
        });
        recyclerView.setAdapter(itemAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(SPAN_COUNT,
                getResources().getDimensionPixelSize(R.dimen.dp_8), true));
        infoCta.setOnClickListener(this);
        infoBottomSheet = TopAdsInfoBottomSheetDynamicChannel.newInstance(getContext());
    }

    private void initInjector() {
        BaseMainApplication application = ((BaseMainApplication) getContext().getApplicationContext());
        TopAdsComponent component = DaggerTopAdsComponent.builder()
                .baseAppComponent(application.getBaseAppComponent())
                .build();
        component.inject(this);
        component.inject(presenter);
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info_cta) {
            infoBottomSheet.show();
        }
    }

    public void setData(String title, List<Item> data) {
        if(!data.isEmpty()){
            titleTxt.setText(title);
            itemAdapter.setList(data);
            setVisibility(VISIBLE);
        } else {
            setVisibility(GONE);
        }
    }

    public void setAdsItemImpressionListener(TopAdsItemImpressionListener impressionListener) {
        this.impressionListener = impressionListener;
    }

    public void setAdsItemClickListener(TopAdsItemClickListener adsItemClickListener) {
        this.adsItemClickListener = adsItemClickListener;
    }

    @Override
    public void onShopItemClicked(int position, Data data) {
        if(adsItemClickListener!=null){
            adsItemClickListener.onShopItemClicked(position, data.getShop());
        }
    }

    @Override
    public void onProductItemClicked(int position, Data data) {
        if(adsItemClickListener!=null){
            adsItemClickListener.onProductItemClicked(position, data.getProduct());
            openTopAdsUseCase.execute(data.getProductClickUrl());
            TopAdsGtmTracker.eventHomeProductClick(getContext(), data.getProduct(), position);
        }
    }

    @Override
    public void onAddFavorite(int position, Data data) {
        if(adsItemClickListener!=null){
            adsItemClickListener.onAddFavorite(position, data);
        }
    }

    @Override
    public void onAddWishLish(int position, Data data) {
        if(data.getProduct().isWishlist()){
            presenter.removeWishlist(data);
        } else {
            presenter.addWishlist(data);
        }
    }

}
