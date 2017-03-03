package com.tokopedia.core.snapshot.customview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.AttributeSet;

import com.tokopedia.core.R;
import com.tokopedia.core.product.customview.BaseView;
import com.tokopedia.core.snapshot.listener.SnapShotFragmentView;

/**
 * Created by hangnadi on 3/1/17.
 */

public class ButtonView extends BaseView<Object, SnapShotFragmentView> {

    public ButtonView(Context context) {
        super(context);
    }

    public ButtonView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void setListener(SnapShotFragmentView listener) {

    }

    @Override
    protected int getLayoutView() {
        return R.layout.layout_snapshot_button;
    }

    @Override
    protected void parseAttribute(Context context, AttributeSet attrs) {

    }

    @Override
    protected void setViewListener() {

    }

    @Override
    public void renderData(@NonNull Object data) {

    }
}
