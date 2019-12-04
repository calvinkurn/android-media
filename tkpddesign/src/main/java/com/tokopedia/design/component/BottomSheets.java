package com.tokopedia.design.component;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.fragment.app.FragmentManager;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.tokopedia.design.R;

/**
 * Created by meta on 15/03/18.
 * <p>
 * Note: to avoid error "Fatal Exception: java.lang.IllegalArgumentException: The view is not a child of CoordinatorLayout"
 * please use `androidx.coordinatorlayout.widget.CoordinatorLayout` as parent layout on your xml
 */

public abstract class BottomSheets extends BottomSheetDialogFragment {

    private View inflatedView;
    private BottomSheetDismissListener dismissListener;
    private BottomSheetBehavior bottomSheetBehavior;

    public abstract int getLayoutResourceId();
    public abstract void initView(View view);

    public interface BottomSheetDismissListener {
        void onDismiss();
    }

    public enum BottomSheetsState {
        NORMAL, FULL, FLEXIBLE
    }

    public int getBaseLayoutResourceId() {
        return R.layout.widget_bottomsheet;
    }

    public BottomSheetBehavior getBottomSheetBehavior() {
        return bottomSheetBehavior;
    }

    public void setDismissListener(BottomSheetDismissListener dismissListener) {
        this.dismissListener = dismissListener;
    }

    protected String title() {
        return getString(R.string.app_name);
    }

    protected String resetButtonTitle() {
        return "";
    }

    protected BottomSheetsState state() {
        return BottomSheetsState.NORMAL;
    }

    protected void configView(final View parentView) {
        TextView textViewTitle = parentView.findViewById(getTv_title());
        textViewTitle.setText(title());

        TextView resetButton = parentView.findViewById(getTv_reset());
        if (resetButton != null) {
            if (!TextUtils.isEmpty(resetButtonTitle())) {
                resetButton.setText(resetButtonTitle());
                resetButton.setVisibility(View.VISIBLE);
            }
            resetButton.setOnClickListener(view -> onResetButtonClicked());
        }

        View layoutTitle = parentView.findViewById(getLayout_title());
        if (layoutTitle != null) {
            layoutTitle.setOnClickListener(v -> onCloseButtonClick());
        }

        View closeButton = parentView.findViewById(getBtn_close());
        if (closeButton != null) {
            closeButton.setOnClickListener(view -> BottomSheets.this.dismiss());
        }

        FrameLayout frameParent = parentView.findViewById(getBottomsheet_container());
        View subView = View.inflate(getContext(), getLayoutResourceId(), null);
        initView(subView);
        frameParent.addView(subView);
    }

    protected int getBottomsheet_container() {
        return R.id.bottomsheet_container;
    }

    protected int getBtn_close() {
        return R.id.btn_close;
    }

    protected int getTv_reset() {
        return R.id.tv_reset;
    }

    protected int getTv_title() {
        return R.id.tv_title;
    }

    protected int getLayout_title() {
        return R.id.layout_title;
    }

    protected void onCloseButtonClick() {
        if (bottomSheetBehavior == null) {
            return;
        }

        if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
            bottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        } else if (bottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
            BottomSheets.this.dismiss();
        }
    }

    protected void onResetButtonClicked() {

    }

    protected void updateHeight() {
        inflatedView.invalidate();
        inflatedView.measure(0, 0);
        if (bottomSheetBehavior != null)
            bottomSheetBehavior.setPeekHeight(inflatedView.getMeasuredHeight());
    }

    protected void updateHeight(int height) {
        ViewGroup.LayoutParams params = inflatedView.getLayoutParams();
        params.height = height;

        inflatedView.setLayoutParams(params);
        inflatedView.setMinimumHeight(height);
        inflatedView.invalidate();
        inflatedView.measure(0, 0);

        if (bottomSheetBehavior != null)
            bottomSheetBehavior.setPeekHeight(height);
    }

    @SuppressLint("RestrictedApi")
    @Override
    public void setupDialog(Dialog dialog, int style) {
        super.setupDialog(dialog, style);

        inflatedView = View.inflate(getContext(), getBaseLayoutResourceId(), null);
        configView(inflatedView);
        dialog.setContentView(inflatedView);

        View parent = (View) inflatedView.getParent();
        parent.setFitsSystemWindows(true);
        bottomSheetBehavior = BottomSheetBehavior.from(parent);

        inflatedView.measure(0, 0);
        int height = inflatedView.getMeasuredHeight();

        if (state() != BottomSheetsState.FLEXIBLE) {
            try {
                ViewGroup.LayoutParams params = ((View) inflatedView.getParent()).getLayoutParams();

                inflatedView.measure(0, 0);
                DisplayMetrics displaymetrics = new DisplayMetrics();
                getActivity().getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
                int screenHeight = displaymetrics.heightPixels;

                if (state() == BottomSheetsState.FULL) {
                    height = screenHeight;
                }

                if (bottomSheetBehavior != null)
                    bottomSheetBehavior.setPeekHeight(height);

                params.height = screenHeight;
                parent.setLayoutParams(params);
            } catch (IllegalArgumentException illegalEx) {
                Log.d(BottomSheets.class.getName(), illegalEx.getMessage());
            } catch (Exception ignored) {
            }
        }
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

    @Override
    public void show(FragmentManager manager, String tag) {
        try {
            super.show(manager, tag);
        } catch (IllegalStateException e) { }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final BottomSheetDialog bottomSheetDialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        if (state() == BottomSheetsState.FLEXIBLE) {
            bottomSheetDialog.setOnShowListener(dialog -> {
                FrameLayout bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);

                if (bottomSheet != null) {
                    BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
                    behavior.setSkipCollapsed(true);
                    behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                }
            });
        }

        return bottomSheetDialog;
    }
}
