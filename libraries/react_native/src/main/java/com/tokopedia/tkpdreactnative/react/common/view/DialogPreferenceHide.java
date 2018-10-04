package com.tokopedia.tkpdreactnative.react.common.view;

import android.app.Activity;
import android.view.View;
import android.widget.CheckBox;

import com.tokopedia.design.component.Dialog;
import com.tokopedia.tkpdreactnative.R;

/**
 * Created by zulfikarrahman on 4/24/18.
 */

public class DialogPreferenceHide extends Dialog {

    private CheckBox checkBoxHideDialog;

    public DialogPreferenceHide(Activity context, Type type) {
        super(context, type);
    }

    @Override
    public int layoutResId() {
        return R.layout.dialog_preference_hide;
    }

    @Override
    public void initView(View dialogView) {
        super.initView(dialogView);
        checkBoxHideDialog = dialogView.findViewById(R.id.checkbox_dialog_hide);
    }

    public boolean isCheckedBoxHideDialog(){
        return checkBoxHideDialog.isChecked();
    }
}
