package com.tokopedia.design.dialog;

import android.app.Activity;
import android.view.View;

import com.tokopedia.design.R;

/**
 * Created by meyta on 2/14/18.
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
