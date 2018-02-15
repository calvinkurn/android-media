package com.tokopedia.design.dialog;

import android.app.Activity;
import android.view.View;

/**
 * Created by meyta on 2/14/18.
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