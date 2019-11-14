package com.tokopedia.design.component;

import android.content.Context;
import androidx.annotation.DrawableRes;
import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseBottomSheetView;

/**
 * Created by meyta on 2/13/18.
 */

public class Tooltip extends BaseBottomSheetView {

    private TextView tvTitle, tvDesc;
    private ImageView icon;
    private Button btnAction;

    public Tooltip(@NonNull Context context) {
        super(context);
    }

    public Tooltip(@NonNull Context context, int theme) {
        super(context, theme);
    }

    protected Tooltip(@NonNull Context context, boolean cancelable, OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.widget_tooltip;
    }

    @Override
    protected void initView(View view) {
        tvTitle = view.findViewById(R.id.tv_title_tooltip);
        tvDesc = view.findViewById(R.id.tv_desc_tooltip);
        icon = view.findViewById(R.id.iv_icon_tooltip);
        btnAction = view.findViewById(R.id.btn_action_tooltip);
    }

    public void setTitle(String title) {
        this.tvTitle.setText(title);
    }

    public void setDesc(String tvDesc) {
        this.tvDesc.setText(tvDesc);
    }

    public void setTextButton(String btnAction) {
        this.btnAction.setText(btnAction);
    }

    public void setIcon(@DrawableRes int icon) {
        this.icon.setImageDrawable(ContextCompat.getDrawable(getContext(), icon));
    }

    public void setWithIcon(boolean withIcon) {
        this.icon.setVisibility(withIcon ? View.VISIBLE : View.GONE);

    }

    public TextView getTextViewTitle() {
        return tvTitle;
    }

    public TextView getTextViewDesc() {
        return tvDesc;
    }

    public Button getBtnAction() {
        return btnAction;
    }

    public ImageView getIcon() {
        return icon;
    }
}
