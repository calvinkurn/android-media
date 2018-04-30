package com.tokopedia.imagepicker.editor.widget;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.imagepicker.R;

/**
 * Created by hendry on 30/04/18.
 */

public class ImageEditActionMainWidget extends FrameLayout {
    public ImageEditActionMainWidget(@NonNull Context context) {
        super(context);
        init();
    }

    public ImageEditActionMainWidget(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ImageEditActionMainWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public ImageEditActionMainWidget(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    private void init() {
        View view = LayoutInflater.from(getContext()).inflate(R.layout.widget_image_edit_action_main,
                this, true);
    }
}
