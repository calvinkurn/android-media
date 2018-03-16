package com.tokopedia.design.component;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseDialog;

/**
 * Created by meyta on 2/14/18.
 *
 * How to use?
 * setTitle("Title Goes Here");
 * setDesc("Make the copy compact to communicate what you want, approx 50 character like this");
 * setBtnOk("CTA Here");
 * setBtnCancel("CTA Here");
 * setOnOkClickListener(View.OnClickListener());
 * setOnCancelClickListener(View.OnClickListener());
 */

public class Dialog extends BaseDialog {

    private TextView title, desc;
    private Button btnCancel, btnOk;

    private Type type = Type.RETORIC;

    public enum Type {
        RETORIC,
        PROMINANCE,
        LONG_PROMINANCE
    }

    public Dialog(Activity context, Type type) {
        super(context);
        this.type = type;
    }

    @Override
    public int layoutResId() {
        if (type == Type.LONG_PROMINANCE)
            return R.layout.dialog_longprominance;
        return R.layout.dialog_base;

    }

    @Override
    public void initView(View dialogView) {
        title = dialogView.findViewById(R.id.tv_title_dialog);
        desc = dialogView.findViewById(R.id.tv_desc_dialog);
        btnOk = dialogView.findViewById(R.id.btn_ok_dialog);
        btnCancel = dialogView.findViewById(R.id.btn_cancel_dialog);

        if (type == Type.RETORIC)
            btnCancel.setVisibility(View.GONE);
    }

    @Override
    public void initListener(AlertDialog dialog) {
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
    }

    public void setOnOkClickListener(View.OnClickListener onOkClickListener) {
        btnOk.setOnClickListener(onOkClickListener);
    }

    public void setOnCancelClickListener(View.OnClickListener onCancelClickListener) {
        btnCancel.setOnClickListener((type == Type.RETORIC) ? null : onCancelClickListener);
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDesc(String desc) {
        this.desc.setText(desc);
    }

    public void setBtnOk(String title) {
        this.btnOk.setText(title);
    }

    public void setBtnCancel(String title) {
        this.btnCancel.setText(title);
    }

    public Button getBtnCancel() {
        return btnCancel;
    }

    public Button getBtnOk() {
        return btnOk;
    }
}
