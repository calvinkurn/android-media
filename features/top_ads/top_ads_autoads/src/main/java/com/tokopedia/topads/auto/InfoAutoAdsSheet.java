package com.tokopedia.topads.auto;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Author errysuprayogi on 07,May,2019
 */
public class InfoAutoAdsSheet {

    private BottomSheetDialog dialog;
    private View closeButton;
    private View agreeButton;

    public InfoAutoAdsSheet() {
    }

    public static InfoAutoAdsSheet newInstance(Context context) {
        InfoAutoAdsSheet fragment = new InfoAutoAdsSheet();
        fragment.dialog = new BottomSheetDialog(context);
        fragment.dialog.setContentView(R.layout.layout_info_autoads);
        fragment.closeButton = fragment.dialog.findViewById(R.id.btn_close);
        fragment.agreeButton = fragment.dialog.findViewById(R.id.btn_agree);
        fragment.setupView(context);
        return fragment;
    }

    private void setupView(Context context) {
        dialog.setOnShowListener(dialogInterface -> {
            BottomSheetDialog dialog = (BottomSheetDialog) dialogInterface;
            FrameLayout frameLayout = dialog.findViewById(R.id.design_bottom_sheet);
            if (frameLayout != null) {
                BottomSheetBehavior<FrameLayout> behavior = BottomSheetBehavior.from(frameLayout);
                behavior.setHideable(false);
            }
        });

        agreeButton.setOnClickListener(view -> {
            dismissDialog();
        });

        closeButton.setOnClickListener(view -> dismissDialog());
    }

    public void show() {
        dialog.show();
    }

    public void dismissDialog() {
        dialog.dismiss();
    }
}
