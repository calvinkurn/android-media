package com.tokopedia.promotionstarget.presentation.ui.bottomsheet;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.tokopedia.promotionstarget.R;


/**
 * @author by nisie on 2/23/18.
 */

@SuppressLint("UnifyComponentUsage")
public class PromotionCloseableBottomSheetDialog extends BottomSheetDialog {

    Context context;
    private CloseClickedListener closeListener;
    private boolean isRounded;
    private boolean isRoundedAndCloseable = false;

    public interface CloseClickedListener {
        void onCloseDialog();
    }

    public interface BackHardwareClickedListener {
        void onBackHardwareClicked();
    }

    private PromotionCloseableBottomSheetDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private PromotionCloseableBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected PromotionCloseableBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public static PromotionCloseableBottomSheetDialog createInstance(Context context) {
        final PromotionCloseableBottomSheetDialog closeableBottomSheetDialog = new PromotionCloseableBottomSheetDialog
                (context, R.style.TransparentBottomSheetDialogTheme);
        closeableBottomSheetDialog.isRounded = false;
        closeableBottomSheetDialog.setListener(new CloseClickedListener() {
            @Override
            public void onCloseDialog() {
                closeableBottomSheetDialog.dismiss();
            }
        });
        return closeableBottomSheetDialog;
    }

    public static PromotionCloseableBottomSheetDialog createInstanceRounded(Context context) {
        final PromotionCloseableBottomSheetDialog closeableBottomSheetDialog = new PromotionCloseableBottomSheetDialog
                (context, R.style.TransparentBottomSheetDialogTheme);
        closeableBottomSheetDialog.isRounded = true;
        closeableBottomSheetDialog.setListener(new CloseClickedListener() {
            @Override
            public void onCloseDialog() {
                closeableBottomSheetDialog.dismiss();
            }
        });
        return closeableBottomSheetDialog;
    }

    public static PromotionCloseableBottomSheetDialog createInstanceCloseableRounded(Context context,
                                                                            CloseClickedListener closeListener) {
        final PromotionCloseableBottomSheetDialog closeableBottomSheetDialog = new PromotionCloseableBottomSheetDialog
                (context, R.style.TransparentBottomSheetDialogTheme);
        closeableBottomSheetDialog.isRoundedAndCloseable = true;
        closeableBottomSheetDialog.setListener(closeListener);
        return closeableBottomSheetDialog;
    }

    public static PromotionCloseableBottomSheetDialog createInstance(Context context,
                                                            CloseClickedListener closeListener,
                                                            BackHardwareClickedListener backHardwareClickedListener) {
        PromotionCloseableBottomSheetDialog closeableBottomSheetDialog =
                new PromotionCloseableBottomSheetDialog(context){
                    @Override
                    public void onBackPressed() {
                        super.onBackPressed();
                        if(backHardwareClickedListener!= null){
                            backHardwareClickedListener.onBackHardwareClicked();
                        }
                    }
                };

        closeableBottomSheetDialog.setListener(closeListener);
        return closeableBottomSheetDialog;
    }

    private void setListener(CloseClickedListener closeListener) {
        this.closeListener = closeListener;
    }

    private void init(Context context) {
        this.context = context;
    }

    @Override
    public void setContentView(View view) {
        setContentView(view, "");
    }

    public void setContentView(View view, String title) {
        setCustomContentView(view, title, true);
    }

    public void setCustomContentView(View view, String title, boolean isCloseable) {
        View contentView = inflateCustomView(view, title, isCloseable);
        super.setContentView(contentView);
    }

    private View inflateCustomView(View view, String title, boolean isCloseable) {
        if(isRounded){
            return inflateRoundedHeader(view, isCloseable);
        } else if (isRoundedAndCloseable) {
            return inflateRoundedCloseableHeader(view, title, isCloseable);
        } else {
            return inflateCloseableHeader(view, title, isCloseable);
        }
    }

    @SuppressLint("MissingInflatedId")
    private View inflateRoundedCloseableHeader(View view, String title, boolean isCloseable) {
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .promo_rounded_closeable_bottom_sheet, null);
        FrameLayout frameLayout = contentView.findViewById(R.id.container);
        frameLayout.addView(view);
        ImageView closeButton = contentView.findViewById(R.id.close_button_rounded);
        TextView headerTitle = contentView.findViewById(R.id.title_closeable_rounded);

        if (!isCloseable) {
            closeButton.setVisibility(View.GONE);
            contentView.findViewById(R.id.view_separator).setVisibility(View.GONE);
            contentView.findViewById(R.id.title_closeable).setVisibility(View.GONE);
        } else {
            closeButton.setOnClickListener(v -> {
                dismiss();
                closeListener.onCloseDialog();
            });
        }

        if (!TextUtils.isEmpty(title)) {
            headerTitle.setVisibility(View.VISIBLE);
            headerTitle.setText(title);
        }

        return contentView;
    }

    private View inflateCloseableHeader(View view, String title, boolean isCloseable) {
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .promo_closeable_bottom_sheet_dialog, null);
        FrameLayout frameLayout = contentView.findViewById(R.id.container);
        frameLayout.addView(view);
        ImageView closeButton = contentView.findViewById(R.id.close_button);

        if (!isCloseable) {
            closeButton.setVisibility(View.GONE);
            contentView.findViewById(R.id.view_separator).setVisibility(View.GONE);
            contentView.findViewById(R.id.title_closeable).setVisibility(View.GONE);
        } else {
            closeButton.setOnClickListener(v -> {
                dismiss();
                closeListener.onCloseDialog();
            });
        }

        if(!TextUtils.isEmpty(title)){
            contentView.findViewById(R.id.title_closeable).setVisibility(View.VISIBLE);
            ((TextView)contentView.findViewById(R.id.title_closeable)).setText(title);
        }

        return contentView;
    }


    private View inflateRoundedHeader(View view, boolean isCloseable) {
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .promo_rounded_closeable_bottom_sheet_dialog, null);
        FrameLayout frameLayout = contentView.findViewById(R.id.container);
        frameLayout.addView(view);
        View trayClose = contentView.findViewById(R.id.tray_close);
        if (isCloseable) {
            trayClose.setVisibility(View.VISIBLE);
        } else {
            trayClose.setVisibility(View.GONE);
        }
        return contentView;
    }

    @Override
    public void setContentView(int layoutResId) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (inflater != null) {
            View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                    .promo_closeable_bottom_sheet_dialog, null);
            FrameLayout frameLayout = contentView.findViewById(R.id.container);
            View view = inflater.inflate(layoutResId, null);
            frameLayout.addView(view);
            super.setContentView(contentView);
        }

        super.setContentView(layoutResId);
    }
}
