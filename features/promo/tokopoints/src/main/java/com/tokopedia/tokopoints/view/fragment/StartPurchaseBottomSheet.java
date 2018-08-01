package com.tokopedia.tokopoints.view.fragment;

import android.app.Dialog;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tkpd.library.utils.ImageHandler;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.BottomSheets;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LobItem;
import com.tokopedia.tokopoints.view.util.CommonConstant;

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
        getBottomSheetBehavior().setPeekHeight(screenHeight / 3);
    }


    @Override
    public void initView(View view) {
        TextView desc = view.findViewById(R.id.text_description);
        desc.setText(mLobDetails.getDescription());
        LinearLayout linearLayout = view.findViewById(R.id.container_lob);
        LayoutInflater inflater = LayoutInflater.from(view.getContext());

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
                icon.setImageResource(R.drawable.ic_tp_buy);
            } else if (item.getText().equalsIgnoreCase("Kereta")) {
                icon.setImageResource(R.drawable.ic_tp_train);
            } else if (item.getText().equalsIgnoreCase("Pesawat")) {
                icon.setImageResource(R.drawable.ic_tp_pesawat);
            } else if (item.getText().equalsIgnoreCase("Bayar")) {
                icon.setImageResource(R.drawable.ic_tp_bayar);
            }
        }
    }

    @Override
    protected String title() {
        return mLobDetails.getTitle();
    }

    public void setData(LobDetails lobDetails) {
        this.mLobDetails = lobDetails;
    }
}
