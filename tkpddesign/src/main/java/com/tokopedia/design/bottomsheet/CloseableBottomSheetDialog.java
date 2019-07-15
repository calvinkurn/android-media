package com.tokopedia.design.bottomsheet;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;


/**
 * @author by nisie on 2/23/18.
 */

public class CloseableBottomSheetDialog extends BottomSheetDialog {

    Context context;
    private CloseClickedListener closeListener;
    private boolean isRounded;

    public interface CloseClickedListener {
        void onCloseDialog();
    }

    public interface BackHardwareClickedListener {
        void onBackHardwareClicked();
    }

    private CloseableBottomSheetDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private CloseableBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected CloseableBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public static CloseableBottomSheetDialog createInstance(Context context) {
        final CloseableBottomSheetDialog closeableBottomSheetDialog = new CloseableBottomSheetDialog
                (context);
        closeableBottomSheetDialog.isRounded = false;
        closeableBottomSheetDialog.setListener(new CloseClickedListener() {
            @Override
            public void onCloseDialog() {
                closeableBottomSheetDialog.dismiss();
            }
        });
        return closeableBottomSheetDialog;
    }

    public static CloseableBottomSheetDialog createInstanceRounded(Context context) {
        final CloseableBottomSheetDialog closeableBottomSheetDialog = new CloseableBottomSheetDialog
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

    public static CloseableBottomSheetDialog createInstance(Context context,
                                                            CloseClickedListener closeListener,
                                                            BackHardwareClickedListener backHardwareClickedListener) {
        CloseableBottomSheetDialog closeableBottomSheetDialog =
                new CloseableBottomSheetDialog(context){
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
        } else {
            return inflateCloseableHeader(view, title, isCloseable);
        }
    }

    private View inflateCloseableHeader(View view, String title, boolean isCloseable) {
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .closeable_bottom_sheet_dialog, null);
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
                .rounded_closeable_bottom_sheet_dialog, null);
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
                    .closeable_bottom_sheet_dialog, null);
            FrameLayout frameLayout = contentView.findViewById(R.id.container);
            View view = inflater.inflate(layoutResId, null);
            frameLayout.addView(view);
            super.setContentView(contentView);
        }

        super.setContentView(layoutResId);
    }
}
