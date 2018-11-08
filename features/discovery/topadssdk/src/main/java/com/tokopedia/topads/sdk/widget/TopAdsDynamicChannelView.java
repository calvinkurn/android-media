package com.tokopedia.topads.sdk.widget;

import android.content.Context;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.topads.sdk.R;
import com.tokopedia.topads.sdk.base.adapter.Item;
import com.tokopedia.topads.sdk.utils.GridSpaceItemDecoration;
import com.tokopedia.topads.sdk.view.TopAdsInfoBottomSheetDynamicChannel;
import com.tokopedia.topads.sdk.view.adapter.AdsItemAdapter;
import java.util.List;

public class TopAdsDynamicChannelView extends LinearLayout implements View.OnClickListener {

    public static final int SPAN_COUNT = 3;
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
        recyclerView.setLayoutManager(new GridLayoutManager(context, SPAN_COUNT));
        recyclerView.addItemDecoration(new GridSpaceItemDecoration(SPAN_COUNT,
                getResources().getDimensionPixelSize(R.dimen.dp_8), true));
        infoCta.setOnClickListener(this);
        infoBottomSheet = TopAdsInfoBottomSheetDynamicChannel.newInstance(getContext());
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
