package com.tokopedia.tokopoints.view.fragment;

import android.app.Dialog;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.view.adapter.CouponInStackBaseAdapter;
import com.tokopedia.tokopoints.view.adapter.CouponListStackedBaseAdapter;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LobItem;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

public class CouponInStackBottomSheet extends BottomSheets implements AdapterCallback {

    private String mData;

    @Override
    public int getLayoutResourceId() {
        return R.layout.tp_bottosheet_coupon_in_stack;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        if (getBottomSheetBehavior() != null)
            getBottomSheetBehavior().setPeekHeight(screenHeight / 3);
    }


    @Override
    public void initView(View view) {
        RecyclerView recyclerView = view.findViewById(R.id.rv_coupon_in_stack);
        CouponInStackBaseAdapter adapter = new CouponInStackBaseAdapter( this, getContext(), mData);
        recyclerView.setAdapter(adapter);
        adapter.startDataLoading();
    }

    @Override
    protected String title() {
        return "Coupon in Stack";
    }

    public void setData(String data) {
        this.mData = data;
    }

    @Override
    protected void onCloseButtonClick() {
        super.onCloseButtonClick();

        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                AnalyticsTrackerUtil.ActionKeys.CLICK_CLOSE_BUTTON,
                AnalyticsTrackerUtil.EventKeys.TOKOPOINTS_LUCKY_EGG_CLOSE_LABEL);
    }

    @Override
    public void onRetryPageLoad(int pageNumber) {

    }

    @Override
    public void onEmptyList(Object rawObject) {

    }

    @Override
    public void onStartFirstPageLoad() {

    }

    @Override
    public void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject) {

    }

    @Override
    public void onStartPageLoad(int pageNumber) {

    }

    @Override
    public void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject) {

    }

    @Override
    public void onError(int pageNumber) {

    }
}
