package com.tokopedia.digital.cart.fragment;

import android.app.Activity;
import android.support.v7.widget.AppCompatCheckBox;
import android.view.View;

import com.tokopedia.design.component.Dialog;
import com.tokopedia.digital.R;
import com.tokopedia.digital.cart.data.cache.DigitalPostPaidLocalCache;
import com.tokopedia.user.session.UserSession;

public class DigitalPostPaidDialog extends Dialog {
    private AppCompatCheckBox dontshowAgainCheckbox;
    private DigitalPostPaidLocalCache digitalPostPaidLocalCache;
    private String userId;

    public DigitalPostPaidDialog(Activity context,
                                 Type type,
                                 DigitalPostPaidLocalCache digitalPostPaidLocalCache,
                                 String userId) {
        super(context, type);
        this.digitalPostPaidLocalCache = digitalPostPaidLocalCache;
        this.userId = userId;
    }


    @Override
    public int layoutResId() {
        return R.layout.view_digital_post_paid_dialog;
    }

    @Override
    public void initView(View dialogView) {
        super.initView(dialogView);
        dontshowAgainCheckbox = dialogView.findViewById(R.id.cb_dont_show_again);
        dontshowAgainCheckbox.setOnCheckedChangeListener((compoundButton, b) -> {
            digitalPostPaidLocalCache.setDontShowAgain(userId, b);
        });
    }
}
