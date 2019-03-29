package com.tokopedia.tokocash.ovoactivation.view;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.tokocash.R;

/**
 * Created by nabillasabbaha on 24/09/18.
 */
public abstract class BaseOvoActivationActivity extends BaseSimpleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCloseButton();
    }

    private void setCloseButton() {
        getSupportActionBar().setHomeAsUpIndicator(ContextCompat.getDrawable(this, com.tokopedia.abstraction.R.drawable.ic_close_default));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.wallet_anim_stay, R.anim.wallet_slide_out_up);
    }
}
