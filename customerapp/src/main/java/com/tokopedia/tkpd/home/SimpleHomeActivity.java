package com.tokopedia.tkpd.home;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.tkpd.R;
import com.tokopedia.tkpd.home.fragment.ProductHistoryFragment;
import com.tokopedia.tkpd.home.fragment.WishListFragment;
import com.tokopedia.tkpd.home.presenter.SimpleHome;
import com.tokopedia.tkpd.home.presenter.SimpleHomeImpl;
import com.tokopedia.tkpd.home.presenter.SimpleHomeView;


public class SimpleHomeActivity extends TActivity
        implements SimpleHomeView {

    public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public static final int INVALID_FRAGMENT = 0;
    public static final int WISHLIST_FRAGMENT = 1;
    public static final int PRODUCT_HISTORY_FRAGMENT = 2;
    SimpleHome simpleHome;

    FragmentManager supportFragmentManager;

    @DeepLink(Constants.Applinks.WISHLIST)
    public static Intent getWishlistApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return newWishlistInstance(context)
                .setData(uri.build())
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.RECENT_VIEW)
    public static Intent getRecentViewApplinkIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return newRecentViewInstance(context)
                .setData(uri.build())
                .putExtras(extras);
    }

    public static Intent newWishlistInstance(Context context) {
        Intent intent = new Intent(context, SimpleHomeActivity.class);
        intent.putExtra(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.WISHLIST_FRAGMENT);
        return intent;
    }

    public static Intent newRecentViewInstance(Context context) {
        Intent intent = new Intent(context, SimpleHomeActivity.class);
        intent.putExtra(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.PRODUCT_HISTORY_FRAGMENT);
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

    @Override
    public void setTitle(int fragmentType) {
        switch (fragmentType) {
            case WISHLIST_FRAGMENT:
                toolbar.setTitle(getString(R.string.title_wishlist));
                break;
            case PRODUCT_HISTORY_FRAGMENT:
                toolbar.setTitle(getString(R.string.title_activity_product_history));
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
                    moveToFragment(wishListFragment, true, WishListFragment.FRAGMENT_TAG);
                } else {
                    Log.d(TAG, messageTAG + WishListFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case PRODUCT_HISTORY_FRAGMENT:
                if (isFragmentCreated(ProductHistoryFragment.FRAGMENT_TAG)) {
                    Log.d(TAG, messageTAG + ProductHistoryFragment.class.getSimpleName() + " is created !!!");
                    Fragment productHistory = ProductHistoryFragment.newInstance();
                    moveToFragment(productHistory, true, ProductHistoryFragment.FRAGMENT_TAG);
                } else {
                    Log.d(TAG, messageTAG + ProductHistoryFragment.class.getSimpleName() + " is not created !!!");
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

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        simpleHome.saveDataBeforeRotate(outState);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        simpleHome.saveDataBeforeRotate(outState);
    }

    @Override
    public void initToolbar() {
        setupToolbar();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Log.d(TAG, messageTAG + " R.id.home !!!");
                return true;
            case android.R.id.home:
                Log.d(TAG, messageTAG + " android.R.id.home !!!");
                getSupportFragmentManager().popBackStack();
                return true;
            case R.id.action_cart:
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
