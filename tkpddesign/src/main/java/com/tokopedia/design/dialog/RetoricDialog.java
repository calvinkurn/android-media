package com.tokopedia.design.dialog;

import android.app.Activity;
import android.view.View;

/**
 * Created by meyta on 2/14/18.
 *
 * How to use?
 * setTitle("Title Goes Here");
 * setDesc("Make the copy compact to communicate what you want, approx 50 character like this");
 * setBtnOk("CTA Here");
 * setOnOkClickListener(View.OnClickListener());
 *
 * important! cancel button will invisible
 */

public class RetoricDialog extends ProminanceDialog {

    public RetoricDialog(Activity context) {
        super(context);
    }

    @Override
    public void initView(View dialogView) {
        super.initView(dialogView);
        getBtnCancel().setVisibility(View.GONE);
    }
}