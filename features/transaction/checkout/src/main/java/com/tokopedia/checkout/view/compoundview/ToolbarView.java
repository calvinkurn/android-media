package com.tokopedia.checkout.view.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;

import com.tokopedia.checkout.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by meta on 19/07/18.
 */
public class ToolbarView  extends BaseCustomView {

    public ToolbarView(@NonNull Context context) {
        super(context);
        init();
    }

    public ToolbarView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToolbarView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        inflate(getContext(), R.layout.toolbar_delete, this);
    }
}
