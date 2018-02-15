package com.tokopedia.design.dialog;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.tokopedia.design.R;

/**
 * Created by meyta on 2/14/18.
 */

public class ProminanceDialog extends Dialog {

    private TextView title, desc;
    private Button btnCancel, btnOk;

    public ProminanceDialog(Activity context) {
        super(context);
    }

    @Override
    public int layoutResId() {
        return R.layout.dialog_base;
    }

    @Override
    public void initView(View dialogView) {
        title = dialogView.findViewById(R.id.tv_title_dialog);
        desc = dialogView.findViewById(R.id.tv_desc_dialog);
        btnOk = dialogView.findViewById(R.id.btn_ok_dialog);
        btnCancel = dialogView.findViewById(R.id.btn_cancel_dialog);
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
        btnCancel.setOnClickListener(onCancelClickListener);
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
