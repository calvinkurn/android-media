package com.tokopedia.topads.auto;

import android.content.Context;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.FrameLayout;

/**
 * Author errysuprayogi on 07,May,2019
 */
public class ManualAdsConfirmationSheet {

    private BottomSheetDialog dialog;
    private View closeButton;
    private View startManualAdsButton;
    private View startAutoAdsButton;

    public ManualAdsConfirmationSheet() {
    }

    public static ManualAdsConfirmationSheet newInstance(Context context) {
        ManualAdsConfirmationSheet fragment = new ManualAdsConfirmationSheet();
        fragment.dialog = new BottomSheetDialog(context);
        fragment.dialog.setContentView(R.layout.layout_confirmation_manual_ads);
        fragment.closeButton = fragment.dialog.findViewById(R.id.btn_close);
        fragment.startAutoAdsButton = fragment.dialog.findViewById(R.id.btn_start_auto);
        fragment.startManualAdsButton = fragment.dialog.findViewById(R.id.btn_start_manual);
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

        startAutoAdsButton.setOnClickListener(view -> {

        });

        startManualAdsButton.setOnClickListener(view -> {

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
