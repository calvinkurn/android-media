package com.tokopedia.tokopoints.view.fragment;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.view.model.PopupNotification;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;

public class PopupNotificationBottomSheet extends BottomSheets {

    PopupNotification mData;

    @Override
    public int getLayoutResourceId() {
        return R.layout.tp_layout_popup_notification;
    }

    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
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
        TextView notes = view.findViewById(R.id.text_notes);
        TextView sender = view.findViewById(R.id.text_sender);
        ImageView banner = view.findViewById(R.id.img_banner);
        Button action = view.findViewById(R.id.button_action);

        action.setText(mData.getButtonText());

        if (mData.getCatalog() != null && mData.getCatalog().getTitle() != null) {
            title.setText(mData.getCatalog().getTitle() + " " + mData.getCatalog().getSubTitle());
        } else {
            title.setText(mData.getText());
        }

        if (mData.getNotes() != null && !mData.getNotes().isEmpty()) {
            notes.setVisibility(View.VISIBLE);
            notes.setText("\"" + mData.getNotes() + "\"");
        }

        if (mData.getSender() != null && !mData.getSender().isEmpty()) {
            sender.setVisibility(View.VISIBLE);
            sender.setText("-" + mData.getSender());
        }

        if (mData.getCatalog() != null
                && mData.getCatalog().getExpired() == null
                && mData.getCatalog().getExpired().isEmpty()) {
            count.setVisibility(View.VISIBLE);
            count.setText(mData.getCatalog().getExpired());
        } else {
            count.setVisibility(View.GONE);
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

        action.setOnClickListener(view1 -> RouteManager.route(action.getContext(), mData.getAppLink()));
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
                AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                AnalyticsTrackerUtil.ActionKeys.CLICK_CLOSE_BUTTON,
                AnalyticsTrackerUtil.EventKeys.TOKOPOINTS_LUCKY_EGG_CLOSE_LABEL);
    }
}
