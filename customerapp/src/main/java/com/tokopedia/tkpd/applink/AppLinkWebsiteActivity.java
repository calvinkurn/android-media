package com.tokopedia.tkpd.applink;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.TaskStackBuilder;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.SellerAppRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.webview.fragment.FragmentGeneralWebView;
import com.tokopedia.digital.product.activity.DigitalWebActivity;
import com.tokopedia.tkpd.R;

/**
 * @author anggaprasetiyo on 7/20/17.
 */

public class AppLinkWebsiteActivity extends BasePresenterActivity
        implements FragmentGeneralWebView.OnFragmentInteractionListener {
    private static final String EXTRA_URL = "EXTRA_URL";
    private static final String KEY_APP_LINK_QUERY_URL = "url";

    private String url;

    public static Intent newInstance(Context context, String url) {
        return new Intent(context, DigitalWebActivity.class)
                .putExtra(EXTRA_URL, url);
    }

    @SuppressWarnings("unused")
    @DeepLink({Constants.Applinks.WEBVIEW})
    public static Intent getInstanceIntentAppLink(Context context, Bundle extras) {
        TaskStackBuilder taskStackBuilder = TaskStackBuilder.create(context);
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        if (extras.getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false)) {
            Intent homeIntent;
            if (GlobalConfig.isSellerApp()) {
                homeIntent = SellerAppRouter.getSellerHomeActivity(context);
            } else {
                homeIntent = HomeRouter.getHomeActivity(context);
            }
            homeIntent.putExtra(HomeRouter.EXTRA_INIT_FRAGMENT,
                    HomeRouter.INIT_STATE_FRAGMENT_HOME);
            taskStackBuilder.addNextIntent(homeIntent);
        }
        String webUrl = extras.getString(
                KEY_APP_LINK_QUERY_URL, TkpdBaseURL.DEFAULT_TOKOPEDIA_WEBSITE_URL
        );
        Intent destination = AppLinkWebsiteActivity.newInstance(context, webUrl);
        taskStackBuilder.addNextIntent(destination);
        return destination;
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {
        url = extras.getString(EXTRA_URL);
    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_app_link_website;
    }

    @Override
    protected void initView() {
        Fragment fragment = getFragmentManager().findFragmentById(com.tokopedia.digital.R.id.container);
        if (fragment == null || !(fragment instanceof FragmentGeneralWebView))
            getFragmentManager().beginTransaction().replace(com.tokopedia.digital.R.id.container,
                    FragmentGeneralWebView.createInstance(url, true)).commit();
    }

    @Override
    protected void setViewListener() {

    }

    @Override
    protected void initVar() {

    }

    @Override
    protected void setActionVar() {

    }

    @Override
    public void onWebViewSuccessLoad() {

    }

    @Override
    public void onWebViewErrorLoad() {

    }

    @Override
    public void onWebViewProgressLoad() {

    }
}
