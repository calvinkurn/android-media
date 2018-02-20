package com.tokopedia.design.dialog;

import android.app.Activity;
import android.view.View;

import com.tokopedia.design.R;

/**
 * Created by meyta on 2/14/18.
 *
 * How to use?
 * setTitle("Title Goes Here");
 * setDesc("Make the copy compact to communicate what you want, approx 50 character like this");
 * setBtnOk("Long Text CTA Here");
 * setBtnCancel("CTA Here");
 * setOnOkClickListener(View.OnClickListener());
 * setOnCancelClickListener(View.OnClickListener());
 */

public class LongProminanceDialog extends ProminanceDialog {

    public LongProminanceDialog(Activity context) {
        super(context);
    }

    @Override
    public int layoutResId() {
        return R.layout.dialog_longprominance;
    }

    @Override
    public void initView(View dialogView) {
        super.initView(dialogView);
    }
}
