package com.tokopedia.design.bottomsheet;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.StyleRes;
import android.view.View;
import android.widget.FrameLayout;

import com.tokopedia.design.R;

public class BottomSheetCustomContentView extends BottomSheetView {

    private FrameLayout customContentLayout;

    public BottomSheetCustomContentView(Context context) {
        super(context);
    }

    public BottomSheetCustomContentView(@NonNull Context context, @StyleRes int theme) {
        super(context, theme);
        init(context);
    }

    public BottomSheetCustomContentView(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        init(context);
    }

    protected void init(Context context) {
        super.init(context);
        customContentLayout = findViewById(R.id.layout_custom_content);
    }

    public void setCustomContentLayout(int customContentLayout) {
        this.customContentLayout.addView(getLayoutInflater().inflate(customContentLayout, null));
    }

    public void setCustomContentLayout(View customContentLayout) {
        this.customContentLayout.addView(customContentLayout);
    }


    protected int getLayout() {
        return R.layout.widget_bottom_sheet_custom_content;
    }
}
