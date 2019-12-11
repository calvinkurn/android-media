package com.tokopedia.purchase_platform.features.cart.view.compoundview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tokopedia.design.base.BaseCustomView;
import com.tokopedia.purchase_platform.R;

/**
 * Created by meta on 19/07/18.
 */
public class ToolbarRemoveView extends BaseCustomView {

    public interface ToolbarCartListener {
        void onToolbarRemoveAllCart();
        void onGoToChuck();
    }

    public ToolbarRemoveView(@NonNull Context context) {
        super(context);
        init();
    }

    public ToolbarRemoveView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToolbarRemoveView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private TextView textView;

    private void init() {
        View view = inflate(getContext(), R.layout.toolbar_delete, this);
        textView = view.findViewById(R.id.textview_title);
    }

    public void setTitle(CharSequence title) {
        if (textView != null)
            textView.setText(title);
    }

}
