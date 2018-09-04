package com.tokopedia.reksadana.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.di.ReksaDanaComponent;
import com.tokopedia.reksadana.view.fragment.DashBoardFragment;
import com.tokopedia.reksadana.view.fragment.RegisterFragment;
import com.tokopedia.reksadana.view.utils.Constants;

public class ReksaDanaHomeActivity extends BaseSimpleActivity implements HasComponent<ReksaDanaComponent>{
    @Override
    protected Fragment getNewFragment() {
        return DashBoardFragment.createInstance();
    }

    public static Intent createIntent(Context context) {
        return new Intent(context, ReksaDanaHomeActivity.class);
    }

    @DeepLink(Constants.DEEPLINK)
    public static Intent getReksaDanaIntent(Context context, Bundle bundle){
        Uri.Builder uri = Uri.parse(bundle.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ReksaDanaHomeActivity.class)
                .setData(uri.build())
                .putExtras(bundle);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
    @Override
    protected void onCreate(Bundle arg) {
        super.onCreate(arg);
        updateTitle("Tokopedia Reksa Dana");
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        }
        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);
    }

    public void moveToDashboard() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.parent_view, DashBoardFragment.createInstance(), DashBoardFragment.class.getSimpleName())
                .commit();
    }
    public void navigateToRegistration() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.parent_view, RegisterFragment.newInstance(), RegisterFragment.class.getSimpleName())
                .addToBackStack(null)
                .commit();
    }
    @Override
    public ReksaDanaComponent getComponent() {
        return null;
    }
}
