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

public class ChannelCloseableBottomSheetDialog extends BottomSheetDialog {

    Context context;
    private CloseClickedListener closeListener;
    private BackHardwareClickedListener backHardwareClickedListener;

    public interface CloseClickedListener {
        void onCloseDialog();
    }
    public interface BackHardwareClickedListener {
        void onBackHardwareClicked();
    }


    private ChannelCloseableBottomSheetDialog(@NonNull Context context) {
        super(context);
        init(context);
    }

    private ChannelCloseableBottomSheetDialog(@NonNull Context context, int theme) {
        super(context, theme);
        init(context);
    }

    protected ChannelCloseableBottomSheetDialog(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    public static ChannelCloseableBottomSheetDialog createInstance(Context context) {
        final ChannelCloseableBottomSheetDialog channelCloseableBottomSheetDialog = new ChannelCloseableBottomSheetDialog
                (context);
        channelCloseableBottomSheetDialog.setListener(new CloseClickedListener() {
            @Override
            public void onCloseDialog() {
                channelCloseableBottomSheetDialog.dismiss();
            }
        });
        return channelCloseableBottomSheetDialog;
    }

    public static ChannelCloseableBottomSheetDialog createInstance(Context context,
                                                                   CloseClickedListener closeListener,
                                                                   BackHardwareClickedListener backHardwareClickedListener) {
        ChannelCloseableBottomSheetDialog channelCloseableBottomSheetDialog = new ChannelCloseableBottomSheetDialog
                (context){
            @Override
            public void onBackPressed() {
                super.onBackPressed();
                if(backHardwareClickedListener != null) {
                    backHardwareClickedListener.onBackHardwareClicked();
                }
            }
        };
        channelCloseableBottomSheetDialog.setListener(closeListener);
        return channelCloseableBottomSheetDialog;
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
            ((TextView)contentView.findViewById(R.id.title_closeable)).setVisibility(View.GONE);
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
                .channel_closeable_bottom_sheet_dialog, null);
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
                    .channel_closeable_bottom_sheet_dialog, null);
            FrameLayout frameLayout = contentView.findViewById(R.id.container);
            View view = inflater.inflate(layoutResId, null);
            frameLayout.addView(view);
            super.setContentView(contentView);
        }

        super.setContentView(layoutResId);
    }
}
