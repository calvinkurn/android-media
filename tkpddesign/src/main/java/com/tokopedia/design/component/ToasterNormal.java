package com.tokopedia.design.component;

import android.support.design.widget.Snackbar;
import android.view.View;

import com.tokopedia.design.R;
import com.tokopedia.design.base.BaseToaster;

/**
 * @author okasurya on 1/30/18.
 */

public class ToasterNormal extends BaseToaster {

    public static Snackbar make(View view,
                                String snackbarText,
                                @Snackbar.Duration int duration) {
        return new Builder(view, snackbarText, duration)
                .setBackgroundDrawable(R.drawable.bg_snackbar_normal)
                .setTextColor(R.color.font_black_secondary_54)
                .setActionTextColor(R.color.font_black_primary_70)
                .build();
    }
}
