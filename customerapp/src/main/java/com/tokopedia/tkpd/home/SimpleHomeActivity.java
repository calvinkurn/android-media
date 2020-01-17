package com.tokopedia.tkpd.home;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import android.util.Log;
import android.view.MenuItem;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.wishlist.WishListFragment;
import com.tokopedia.tkpd.home.presenter.SimpleHome;
import com.tokopedia.tkpd.home.presenter.SimpleHomeImpl;
import com.tokopedia.tkpd.home.presenter.SimpleHomeView;


public class SimpleHomeActivity extends TActivity
        implements SimpleHomeView {

    public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public static final int INVALID_FRAGMENT = 0;
    public static final int WISHLIST_FRAGMENT = 1;
    SimpleHome simpleHome;

    FragmentManager supportFragmentManager;

    @DeepLink({Constants.Applinks.WISHLIST, ApplinkConst.WISHLIST})
    public static Intent getWishlistApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return newWishlistInstance(context)
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent newWishlistInstance(Context context) {
        Intent intent = new Intent(context, SimpleHomeActivity.class);
        intent.putExtra(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.WISHLIST_FRAGMENT);
        return intent;
    }

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_HOME_WISHLIST;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }
        setContentView(R.layout.activity_simple_home);
        initToolbar();
        initGraphqlLib();

        supportFragmentManager = getSupportFragmentManager();
        supportFragmentManager.addOnBackStackChangedListener(new FragmentManager.OnBackStackChangedListener() {
            public void onBackStackChanged() {
                int backCount = getSupportFragmentManager().getBackStackEntryCount();
                Log.d(TAG, "back stack changed [count : " + backCount);
                if (backCount == 0) {
                    finish();
                }
            }
        });

        simpleHome = new SimpleHomeImpl(this);
        simpleHome.fetchExtras(getIntent());
        simpleHome.fetchDataAfterRotate(savedInstanceState);

    }

    private void initGraphqlLib() {
        GraphqlClient.init(this);
    }

    @Override
    public void setTitle(int fragmentType) {
        switch (fragmentType) {
            case WISHLIST_FRAGMENT:
                toolbar.setTitle(getString(R.string.title_wishlist));
                break;
        }
    }

    @Override
    public void initFragment(int fragmentType) {
        switch (fragmentType) {
            case WISHLIST_FRAGMENT:
                if (isFragmentCreated(WishListFragment.FRAGMENT_TAG)) {
                    Log.d(TAG, messageTAG + WishListFragment.class.getSimpleName() + " is created !!!");
                    Fragment wishListFragment = WishListFragment.newInstance();
                    if (getIntent().hasExtra(Constants.FROM_APP_SHORTCUTS)) {
                        boolean isFromAppShortCut = getIntent().getBooleanExtra(WishListFragment.FROM_APP_SHORTCUTS, false);
                        Bundle args = new Bundle();
                        args.putBoolean(WishListFragment.FROM_APP_SHORTCUTS, isFromAppShortCut);
                        wishListFragment.setArguments(args);
                    }
                    moveToFragment(wishListFragment, true, WishListFragment.FRAGMENT_TAG);
                } else {
                    Log.d(TAG, messageTAG + WishListFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
        }
    }

    @Override
    public void moveToFragment(Fragment fragment, boolean isAddToBackStack, String TAG) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.simple_home_container, fragment, TAG);
        if (isAddToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * @param tag
     * @return true means fragment is null
     */
    @Override
    public boolean isFragmentCreated(String tag) {
        return supportFragmentManager.findFragmentByTag(tag) == null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        invalidateOptionsMenu();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        // Do not put super, avoid crash transactionTooLarge
    }

    @Override
    public void initToolbar() {
        setupToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            Log.d(TAG, messageTAG + " R.id.home !!!");
            return true;
        } else if (item.getItemId() == android.R.id.home) {
            Log.d(TAG, messageTAG + " android.R.id.home !!!");
            getSupportFragmentManager().popBackStack();
            return true;
        } else if (item.getItemId() == R.id.action_cart) {
            return onCartOptionSelected(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    @Override
    protected boolean isLightToolbarThemes() {
        return true;
    }
}
