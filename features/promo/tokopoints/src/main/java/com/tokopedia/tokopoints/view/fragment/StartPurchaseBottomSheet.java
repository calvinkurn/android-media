package com.tokopedia.tokopoints.view.fragment;

import android.app.Dialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LobItem;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;

public class StartPurchaseBottomSheet extends BottomSheets {

    LobDetails mLobDetails;

    @Override
    public int getLayoutResourceId() {
        return R.layout.tp_bottosheet_container;
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
        if(mLobDetails==null){
            dismiss();
            return;
        }
        TextView desc = view.findViewById(R.id.text_description);
        if(!TextUtils.isEmpty(mLobDetails.getDescription())){
            desc.setText(mLobDetails.getDescription());
        }
        LinearLayout linearLayout = view.findViewById(R.id.container_lob);
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

        if(mLobDetails.getLobs()!=null) {
            for (LobItem item : mLobDetails.getLobs()) {
                View itemView = inflater.inflate(R.layout.tp_bottomsheet_lob_item, null, false);
                TextView title = itemView.findViewById(R.id.text_title);
                title.setText(item.getText());
                linearLayout.addView(itemView);

                itemView.setOnClickListener(view1 -> {
                    String uri = item.getAppLink().isEmpty() ? item.getUrl() : item.getAppLink();
                    if (uri.startsWith(CommonConstant.TickerMapKeys.TOKOPEDIA)) {
                        RouteManager.route(itemView.getContext(), uri);
                    } else {
                        ((TokopointRouter) itemView.getContext().getApplicationContext()).openTokoPoint(itemView.getContext(), uri);
                    }
                });

                ImageView icon = itemView.findViewById(R.id.img_lob);
                if (item.getText().equalsIgnoreCase("Beli")) {
                    icon.setImageDrawable(MethodChecker.getDrawable(getActivity(),R.drawable.ic_tp_buy));

                    AnalyticsTrackerUtil.sendEvent(view.getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_BELI,
                            "");
                } else if (item.getText().equalsIgnoreCase("Kereta")) {
                    icon.setImageDrawable(MethodChecker.getDrawable(getActivity(),(R.drawable.ic_tp_train)));

                    AnalyticsTrackerUtil.sendEvent(view.getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_KARETA,
                            "");
                } else if (item.getText().equalsIgnoreCase("Pesawat")) {
                    icon.setImageDrawable(MethodChecker.getDrawable(getActivity(),R.drawable.ic_tp_pesawat));

                    AnalyticsTrackerUtil.sendEvent(view.getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_PESAWAT,
                            "");
                } else if (item.getText().equalsIgnoreCase("Bayar")) {
                    icon.setImageDrawable(MethodChecker.getDrawable(getActivity(),R.drawable.ic_tp_bayar));

                    AnalyticsTrackerUtil.sendEvent(view.getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_BAYAR,
                            "");
                }
            }
        }
    }

    @Override
    protected String title() {
        return mLobDetails!=null ? mLobDetails.getTitle() : "";
    }

    public void setData(LobDetails lobDetails) {
        this.mLobDetails = lobDetails;
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
