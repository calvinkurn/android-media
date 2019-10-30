package com.tokopedia.feedplus.view.util;

import android.app.Activity;
import com.google.android.material.bottomsheet.BottomSheetDialog;

/**
 * Created by stevenfredian on 5/22/17.
 */

public class InfoTopAdsBottomDialog {

    BottomSheetDialog dialog;

    public InfoTopAdsBottomDialog(Activity activity, int layoutResId) {
        this.dialog = new BottomSheetDialog(activity);
        this.dialog.setContentView(layoutResId);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
