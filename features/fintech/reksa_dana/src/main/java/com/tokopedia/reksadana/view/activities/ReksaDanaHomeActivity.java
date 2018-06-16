package com.tokopedia.reksadana.view.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.view.fragment.ReksaDanaHomeFragment;

public class ReksaDanaHomeActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return ReksaDanaHomeFragment.newInstance();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ReksaDanaHomeActivity.class);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle arg) {
        super.onCreate(arg);
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        }
        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);
    }
}
