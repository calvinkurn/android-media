package com.tokopedia.design.component;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * Created by meta on 15/03/18.
 */

public abstract class BottomSheets extends BottomSheetDialogFragment {

    public abstract int getLayoutResourceId();

    public abstract void initView(View view);

    public enum BottomSheetsState {
        NORMAL, FULL
    }

    protected String title() {
        return getString(R.string.app_name);
    }

    protected BottomSheetsState state() {
        return BottomSheetsState.NORMAL;
    }

    private BottomSheetBehavior bottomSheetBehavior;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View inflatedView = View.inflate(getContext(), R.layout.widget_bottomsheet, null);

        configView(inflatedView);

        dialog.setContentView(inflatedView);

        View parent = (View) inflatedView.getParent();
        parent.setFitsSystemWindows(true);

        inflatedView.measure(0, 0);
        int height = inflatedView.getMeasuredHeight();

        bottomSheetBehavior = BottomSheetBehavior.from(parent);

        CoordinatorLayout.LayoutParams params = (CoordinatorLayout.LayoutParams) ((View) inflatedView.getParent()).getLayoutParams();

        inflatedView.measure(0, 0);
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;

        if (state() == BottomSheetsState.FULL) {
            height = screenHeight;
        }
        bottomSheetBehavior.setPeekHeight(height);

        params.height = screenHeight;
        parent.setLayoutParams(params);
    }

    public BottomSheetBehavior getBottomSheetBehavior() {
        return bottomSheetBehavior;
    }

    private void configView(final View parentView) {
        TextView textViewTitle = parentView.findViewById(R.id.tv_title);
        textViewTitle.setText(title());

        ImageButton btnClose = parentView.findViewById(R.id.btn_close);
        btnClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    BottomSheets.this.dismiss();
                }
            }
        });

        FrameLayout frameParent = parentView.findViewById(R.id.bottomsheet_container);
        View subView = View.inflate(getContext(), getLayoutResourceId(), null);
        initView(subView);
        frameParent.addView(subView);
    }
}