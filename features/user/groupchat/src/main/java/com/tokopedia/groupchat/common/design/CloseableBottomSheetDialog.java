package com.tokopedia.groupchat.common.design;

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

import com.tokopedia.groupchat.R;

/**
 * @author by nisie on 2/23/18.
 */

public class CloseableBottomSheetDialog extends BottomSheetDialog {

    Context context;
    private CloseClickedListener closeListener;

    public interface CloseClickedListener {
        void onCloseDialog();
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
        closeableBottomSheetDialog.setListener(new CloseClickedListener() {
            @Override
            public void onCloseDialog() {
                closeableBottomSheetDialog.dismiss();
            }
        });
        return closeableBottomSheetDialog;
    }

    public static CloseableBottomSheetDialog createInstance(Context context, CloseClickedListener
            closeListener) {
        CloseableBottomSheetDialog closeableBottomSheetDialog = new CloseableBottomSheetDialog
                (context);
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
        View contentView = inflateCustomView(view, "", true);
        super.setContentView(contentView);
    }

    public void setContentView(View view, String title) {
        View contentView = inflateCustomView(view, title, true);
        super.setContentView(contentView);
    }

    public void setCustomContentView(View view, String title, boolean isCloseable) {
        View contentView = inflateCustomView(view, title, isCloseable);
        super.setContentView(contentView);
    }

    private View inflateCustomView(View view, String title, boolean isCloseable) {
        View contentView = inflateBasicView(view, title);
        ImageView closeButton = contentView.findViewById(R.id.close_button);
        if (!isCloseable) {
            closeButton.setVisibility(View.GONE);
            contentView.findViewById(R.id.view_separator).setVisibility(View.GONE);
            ((TextView)closeButton.findViewById(R.id.title_closeable)).setVisibility(View.GONE);
        } else {
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                    closeListener.onCloseDialog();
                }
            });
        }

        return contentView;
    }

    private View inflateBasicView(View view, String title) {
        View contentView = ((Activity) context).getLayoutInflater().inflate(R.layout
                .closeable_bottom_sheet_dialog, null);
        FrameLayout frameLayout = contentView.findViewById(R.id.container);
        frameLayout.addView(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                closeListener.onCloseDialog();
            }
        });

        if(!TextUtils.isEmpty(title)){
            contentView.findViewById(R.id.title_closeable).setVisibility(View.VISIBLE);
            ((TextView)contentView.findViewById(R.id.title_closeable)).setText(title);
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
