package com.tokopedia.design.tooltip;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.bottomsheet.BaseBottomSheetView;

/**
 * Created by meyta on 2/13/18.
 *
 * How to use?
 * Tooltip tooltip = new Tooltip(this);
 * tooltip.setTitle("Title Goes Here");
 * tooltip.setDesc("Type your information about the hint in a compact way, don’t take it to long.");
 * tooltip.setTextButton("CTA Here");
 * tooltip.setIcon(R.drawable.ic_search_icon);

 * tooltip.getBtnAction().setOnClickListener(new View.OnClickListener() {
 *  @Override
 *  public void onClick(View view) {
 *      Toast.makeText(MainActivity.this, "Hello World!", Toast.LENGTH_SHORT).show();
 *  }
 * });
 * tooltip.show();
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
    protected void initView() {
        tvTitle = findViewById(R.id.tv_title_tooltip);
        tvDesc = findViewById(R.id.tv_desc_tooltip);
        icon = findViewById(R.id.iv_icon_tooltip);
        btnAction = findViewById(R.id.btn_action_tooltip);
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
