package com.tokopedia.design.component;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.CoordinatorLayout;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * Created by meta on 15/03/18.
 */

public abstract class BottomSheets extends BottomSheetDialogFragment {

    public abstract int getLayoutResourceId();

    public int getBaseLayoutResourceId(){
        return R.layout.widget_bottomsheet;
    }

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
    private View inflatedView;
    
    public interface BottomSheetDismissListener {
        void onDismiss();
    }

    private BottomSheetDismissListener dismissListener;

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        inflatedView = View.inflate(getContext(), getBaseLayoutResourceId(), null);

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

    protected void configView(final View parentView) {
        TextView textViewTitle = parentView.findViewById(R.id.tv_title);
        textViewTitle.setText(title());

        View layoutTitle = parentView.findViewById(R.id.layout_title);
        layoutTitle.setOnClickListener(v -> onCloseButtonClick());

        FrameLayout frameParent = parentView.findViewById(R.id.bottomsheet_container);
        View subView = View.inflate(getContext(), getLayoutResourceId(), null);
        initView(subView);
        frameParent.addView(subView);
    }

    protected void onCloseButtonClick() {
        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            BottomSheets.this.dismiss();
        }
    }

    public void setDismissListener(BottomSheetDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
        super.onDismiss(dialog);
    }

    @Override
    public void onCancel(DialogInterface dialog) {
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
        super.onCancel(dialog);
    }

    @Override
    public void onDestroy() {
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        if (dismissListener != null) {
            dismissListener.onDismiss();
        }
        super.onDetach();
    }

    protected void updateHeight() {
        inflatedView.invalidate();
        inflatedView.measure(0, 0);
        bottomSheetBehavior.setPeekHeight(inflatedView.getMeasuredHeight());
    }

    protected void updateHeight(int height) {
        ViewGroup.LayoutParams params = inflatedView.getLayoutParams();
        params.height = height;
        inflatedView.setLayoutParams(params);
        inflatedView.setMinimumHeight(height);
        inflatedView.invalidate();
        inflatedView.measure(0, 0);
        bottomSheetBehavior.setPeekHeight(height);
    }
}
