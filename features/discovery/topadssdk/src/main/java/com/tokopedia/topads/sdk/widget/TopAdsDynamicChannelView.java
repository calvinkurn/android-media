package com.tokopedia.topads.sdk.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.view.TopAdsInfoBottomSheetDynamicChannel;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;
import com.tokopedia.topads.sdk.view.adapter.viewmodel.home.ProductDynamicChannelViewModel;

import java.util.ArrayList;
import java.util.List;

public class TopAdsDynamicChannelView extends LinearLayout implements View.OnClickListener {

    private RecyclerView recyclerView;
    private AdsItemAdapter itemAdapter;
    private TextView titleTxt;
    private ImageView infoCta;
    private TopAdsInfoBottomSheetDynamicChannel infoBottomSheet;

    public TopAdsDynamicChannelView(Context context) {
        super(context);
        inflateView(context, null, 0);
    }

    public TopAdsDynamicChannelView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        inflateView(context, attrs, 0);
    }

    public TopAdsDynamicChannelView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        inflateView(context, attrs, defStyleAttr);
    }

    private void inflateView(Context context, AttributeSet attrs, int defStyle) {
        inflate(getContext(), R.layout.layout_ads_dynamic_channel, this);
        recyclerView = findViewById(R.id.list);
        infoCta = findViewById(R.id.info_cta);
        titleTxt = findViewById(R.id.channel_title);
        itemAdapter = new AdsItemAdapter(getContext());
        recyclerView.setAdapter(itemAdapter);
        infoCta.setOnClickListener(this);
        infoBottomSheet = TopAdsInfoBottomSheetDynamicChannel.newInstance(getContext());

        //Dummy Data
        List<Item> items = new ArrayList<>();
        for (int i = 0; i < 6; i++) {
            ProductDynamicChannelViewModel viewModel = new ProductDynamicChannelViewModel();
            viewModel.setProductName("Produk "+i);
            viewModel.setImageUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2018/6/24/1374800/1374800_14e091c8-284f-4725-8421-eeb82ebe1f40_507_440.jpg");
            viewModel.setImpressionUrl("https://ecs7.tokopedia.net/img/cache/200-square/product-1/2018/6/24/1374800/1374800_14e091c8-284f-4725-8421-eeb82ebe1f40_507_440.jpg");
            viewModel.setProductCashback("Cashback 5%");
            viewModel.setProductPrice("Rp 1.000.000");
            items.add(viewModel);
        }
        setData("Produk Pilihan", "", items);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.info_cta) {
            infoBottomSheet.show();
        }
    }

    public void setData(String title, String cta, List<Item> data) {
        titleTxt.setText(title);
        itemAdapter.setList(data);
    }
}
