package com.tokopedia.checkout.view.compoundview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.checkout.R;
import com.tokopedia.design.base.BaseCustomView;

/**
 * Created by meta on 19/07/18.
 */
public class ToolbarRemoveView extends BaseCustomView {

    public interface OnToolbarRemoveAllCartListener {
        void onToolbarRemoveAllCart();
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
    private Button btnRemove;

    private void init() {
        View view = inflate(getContext(), R.layout.toolbar_delete, this);
        textView = view.findViewById(R.id.textview_title);
        btnRemove = view.findViewById(R.id.btn_remove);
    }

    public void setTitle(CharSequence title) {
        if (textView != null)
            textView.setText(title);
    }

    public void setOnClickRemove(OnToolbarRemoveAllCartListener clickRemove) {
        if (btnRemove != null)
            btnRemove.setOnClickListener(v -> clickRemove.onToolbarRemoveAllCart());
    }

    public void setVisibilityRemove(boolean state) {
        if (btnRemove != null) {
            btnRemove.setVisibility(state ? VISIBLE : GONE);
        }
    }
}
