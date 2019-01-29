package com.tokopedia.tokopoints.notification.view;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.tokopoints.notification.R;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.tokopoints.notification.model.PopupNotification;
import com.tokopedia.tokopoints.notification.utils.AnalyticsTrackerUtil;

public class TokoPointsPopupNotificationBottomSheet extends BottomSheets {

    PopupNotification mData;
    private String couponTitle;

    @Override
    public int getLayoutResourceId() {
        return R.layout.tp_layout_popup_notification;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        if (getActivity() == null) {
            return;
        }

        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;
        if (getBottomSheetBehavior() != null)
            getBottomSheetBehavior().setPeekHeight(screenHeight / 2);
    }


    @Override
    public void initView(View view) {
        TextView count = view.findViewById(R.id.text_quota_count);
        TextView title = view.findViewById(R.id.text_title);
        TextView desc = view.findViewById(R.id.text_desc);
        TextView notes = view.findViewById(R.id.text_notes);
        TextView sender = view.findViewById(R.id.text_sender);
        ImageView banner = view.findViewById(R.id.img_banner);
        Button action = view.findViewById(R.id.button_action);

        action.setText(mData.getButtonText());
        if (mData.getCatalog() == null || mData.getCatalog().getTitle() == null) {
            couponTitle=mData.getTitle();
            title.setGravity(Gravity.CENTER_HORIZONTAL);
        } else {
            couponTitle=mData.getCatalog().getTitle() + " " + mData.getCatalog().getSubTitle();
        }
        title.setText(couponTitle);

        if (!TextUtils.isEmpty(mData.getText())) {
            desc.setVisibility(View.VISIBLE);
            desc.setText(mData.getText());
        }

        if (!TextUtils.isEmpty(mData.getNotes())) {
            notes.setVisibility(View.VISIBLE);
            notes.setText("\"" + mData.getNotes() + "\"");
        }

        if (!TextUtils.isEmpty(mData.getSender())) {
            sender.setVisibility(View.VISIBLE);
            sender.setText("-" + mData.getSender());
        }

        if (mData.getCatalog() == null
                || mData.getCatalog().getExpired() == null
                || mData.getCatalog().getExpired().isEmpty()) {
            count.setVisibility(View.GONE);
        } else {
            count.setVisibility(View.VISIBLE);
            count.setText(mData.getCatalog().getExpired());
        }

        if (mData.getImageURL() != null && !mData.getImageURL().isEmpty()) {
            ImageHandler.loadImageFitCenter(banner.getContext(), banner, mData.getImageURL());
        } else {
            if (mData.getCatalog() != null
                    && mData.getCatalog().getImageUrlMobile() != null
                    && !mData.getCatalog().getImageUrlMobile().isEmpty()) {
                ImageHandler.loadImageFitCenter(banner.getContext(), banner, mData.getCatalog().getImageUrlMobile());
            }
        }

        action.setOnClickListener(view1 -> {
            RouteManager.route(action.getContext(), mData.getAppLink());
            dismiss();
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_TERIMA_HADIAH,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN_KUPON,
                    couponTitle);
        });
    }

    @Override
    protected String title() {
        return mData.getTitle();
    }

    public void setData(PopupNotification data) {
        this.mData = data;
    }

    @Override
    protected void onCloseButtonClick() {
        super.onCloseButtonClick();
        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.POPUP_TERIMA_HADIAH,
                AnalyticsTrackerUtil.ActionKeys.CLICK_CLOSE_BUTTON,
                couponTitle);
    }
}
