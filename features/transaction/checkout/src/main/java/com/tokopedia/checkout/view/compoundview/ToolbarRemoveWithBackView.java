package com.tokopedia.checkout.view.compoundview;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by meta on 19/07/18.
 */
public class ToolbarRemoveWithBackView extends BaseCustomView {

    public ToolbarRemoveWithBackView(@NonNull Context context) {
        super(context);
        init();
    }

    public ToolbarRemoveWithBackView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public ToolbarRemoveWithBackView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private ImageView btnBack;
    private TextView textView;
    private Button btnRemove;

    private void init() {
        View view = inflate(getContext(), R.layout.toolbar_delete_with_back, this);
        btnBack = view.findViewById(R.id.btn_back);
        textView = view.findViewById(R.id.textview_title);
        btnRemove = view.findViewById(R.id.btn_remove);
    }

    public void setTitle(CharSequence title) {
        if (textView != null)
            textView.setText(title);
    }

    public void navigateUp(Activity context) {
        if (btnBack != null)
            btnBack.setOnClickListener(v -> context.finish());
    }

    public void setOnClickRemove(ToolbarRemoveView.OnToolbarRemoveAllCartListener clickRemove) {
        if (btnRemove != null)
            btnRemove.setOnClickListener(v -> clickRemove.onToolbarRemoveAllCart());
    }
}
