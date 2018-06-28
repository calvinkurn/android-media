package com.tokopedia.reksadana.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.core.home.fragment.SimpleWebViewWithFilePickerFragment;
import com.tokopedia.reksadana.R;
import com.tokopedia.reksadana.view.fragment.ReksaDanaHomeFragment;
import com.tokopedia.reksadana.view.utils.Constants;

public class ReksaDanaHomeActivity extends BaseSimpleActivity {
    @Override
    protected Fragment getNewFragment() {
        return SimpleWebViewWithFilePickerFragment.createInstanceWithWebClient(Constants.BASE_URL,new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                if(url.equals(Constants.FORM_URL)){
                    getSupportFragmentManager()
                            .beginTransaction()
                            .replace(R.id.parent_view, new ReksaDanaHomeFragment(), ReksaDanaHomeFragment.class.getSimpleName()).addToBackStack(null)
                            .commit();
                    return true;
                } else {
                    return false;
                }
            }
        });
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
        toolbar.setBackgroundColor(getResources().getColor(R.color.white));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.black));
        }
        toolbar.setTitleTextAppearance(this, R.style.ToolbarText_SansSerifMedium);

    }
}
