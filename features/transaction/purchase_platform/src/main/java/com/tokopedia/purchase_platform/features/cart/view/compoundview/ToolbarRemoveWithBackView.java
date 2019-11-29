package com.tokopedia.purchase_platform.features.cart.view.compoundview;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.config.GlobalConfig;
import com.tokopedia.purchase_platform.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by meta on 19/07/18.
 */
public class ToolbarRemoveWithBackView extends BaseCustomView {

    private ImageView btnBack;
    private TextView textView;
    private Button btnRemove;
    private Button btnOpenChuck;

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

    private void init() {
        View view = inflate(getContext(), R.layout.toolbar_delete_with_back, this);
        btnBack = view.findViewById(R.id.btn_back);
        textView = view.findViewById(R.id.textview_title);
        btnRemove = view.findViewById(R.id.btn_remove);
        btnOpenChuck = view.findViewById(R.id.btn_chuck);
        if (GlobalConfig.isAllowDebuggingTools()) {
            btnOpenChuck.setVisibility(VISIBLE);
        } else {
            btnOpenChuck.setVisibility(GONE);
        }
        btnOpenChuck.setVisibility(VISIBLE);
    }

    public void setTitle(CharSequence title) {
        if (textView != null)
            textView.setText(title);
    }

    public void navigateUp(Activity context) {
        if (btnBack != null)
            btnBack.setOnClickListener(v -> context.finish());
    }

    public void setOnClickRemove(ToolbarRemoveView.ToolbarCartListener listener) {
        if (btnRemove != null)
            btnRemove.setOnClickListener(v -> listener.onToolbarRemoveAllCart());
    }

    public void setOnClickGoToChuck(ToolbarRemoveView.ToolbarCartListener listener) {
        if (btnOpenChuck != null) {
            btnOpenChuck.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onGoToChuck();
                }
            });
        }
    }

    public void setVisibilityRemove(boolean state) {
        if (btnRemove != null) {
            btnRemove.setVisibility(state ? VISIBLE : GONE);
        }
    }
}
