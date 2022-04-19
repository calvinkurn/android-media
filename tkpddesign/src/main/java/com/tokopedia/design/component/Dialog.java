package com.tokopedia.design.component;

import android.app.Activity;
import androidx.appcompat.app.AlertDialog;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseDialog;

/**
 * Created by meyta on 2/14/18.
 */

public class Dialog extends BaseDialog {

    private TextView title, desc;
    private TextView btnCancel, btnOk;

    private Type type = Type.RETORIC;

    public enum Type {
        RETORIC,
        PROMINANCE,
        LONG_PROMINANCE
    }

    public Dialog(Activity context, Type type) {
        super(null);
        this.context = context;
        this.type = type;
        init();
    }

    @Override
    public int layoutResId() {
        if (type == Type.LONG_PROMINANCE)
            return com.tokopedia.design.R.layout.dialog_longprominance;
        return com.tokopedia.design.R.layout.dialog_base;
    }

    @Override
    public void initView(View dialogView) {
        title = dialogView.findViewById(com.tokopedia.design.R.id.tv_title_dialog);
        desc = dialogView.findViewById(com.tokopedia.design.R.id.tv_desc_dialog);
        btnOk = dialogView.findViewById(com.tokopedia.design.R.id.btn_ok_dialog);
        btnCancel = dialogView.findViewById(com.tokopedia.design.R.id.btn_cancel_dialog);

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

    public TextView getTitleTextView () {
        return title;
    }

    public void setTitle(String title) {
        this.title.setText(title);
    }

    public void setDesc(String desc) {
        this.desc.setText(desc);
    }

    public void setDesc(Spanned desc) {
        this.desc.setText(desc);
    }
    public void setDescMovementMethod(){
        this.desc.setMovementMethod(LinkMovementMethod.getInstance());
    }
    public void setBtnOk(String title) {
        this.btnOk.setText(title);
    }

    public void setBtnCancel(String title) {
        this.btnCancel.setText(title);
    }

    public TextView getBtnCancel() {
        return btnCancel;
    }

    public TextView getBtnOk() {
        return btnOk;
    }
}
