package com.tokopedia.home;

import android.content.Intent;
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

import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.R;
import com.tokopedia.core.R2;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.app.TActivity;
import com.tokopedia.home.fragment.ProductHistoryFragment;
import com.tokopedia.home.presenter.SimpleHomeImpl;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SimpleHomeActivity extends TActivity
        implements com.tokopedia.home.presenter.SimpleHomeView {

    public static final String FRAGMENT_TYPE = "FRAGMENT_TYPE";
    public static final int INVALID_FRAGMENT = 0;
    public static final int WISHLIST_FRAGMENT = 1;
    public static final int PRODUCT_HISTORY_FRAGMENT = 2;

    @Bind(R2.id.simple_home_toolbar)
    Toolbar toolbar;

    com.tokopedia.home.presenter.SimpleHome simpleHome;

    FragmentManager supportFragmentManager;

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
        ButterKnife.bind(this);

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
        initToolbar();
    }

    @Override
    public void setTitle(int fragmentType) {
        switch (fragmentType){
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
        switch (fragmentType){
            case WISHLIST_FRAGMENT:
                if (isFragmentCreated(com.tokopedia.home.fragment.WishListFragment.FRAGMENT_TAG)) {
                    Log.d(TAG, messageTAG + com.tokopedia.home.fragment.WishListFragment.class.getSimpleName() + " is created !!!");
                    Fragment wishListFragment = com.tokopedia.home.fragment.WishListFragment.newInstance();
                    moveToFragment(wishListFragment, true, com.tokopedia.home.fragment.WishListFragment.FRAGMENT_TAG);
                }else {
                    Log.d(TAG, messageTAG + com.tokopedia.home.fragment.WishListFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
            case PRODUCT_HISTORY_FRAGMENT:
                if(isFragmentCreated(ProductHistoryFragment.FRAGMENT_TAG)) {
                    Log.d(TAG, messageTAG + ProductHistoryFragment.class.getSimpleName() + " is created !!!");
                    Fragment productHistory = ProductHistoryFragment.newInstance();
                    moveToFragment(productHistory, true, ProductHistoryFragment.FRAGMENT_TAG);
                }else {
                    Log.d(TAG, messageTAG + ProductHistoryFragment.class.getSimpleName() + " is not created !!!");
                }
                break;
        }
    }

    @Override
    public void moveToFragment(Fragment fragment, boolean isAddToBackStack, String TAG) {
        FragmentTransaction fragmentTransaction = supportFragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.simple_home_container, fragment, TAG);
        if(isAddToBackStack)
            fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * @param tag
     * @return true means fragment is null
     */
    @Override
    public boolean isFragmentCreated(String tag) {
        return supportFragmentManager.findFragmentByTag(tag)==null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        sendNotifLocalyticsCallback();
        invalidateOptionsMenu();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
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
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart_only, menu);
        LocalCacheHandler Cache = new LocalCacheHandler(getBaseContext(), "NOTIFICATION_DATA");
        int CartCache = Cache.getInt("is_has_cart");
        if (CartCache > 0) {
            menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart_active);
        } else {
            menu.findItem(R.id.action_cart).setIcon(R.drawable.ic_new_action_cart);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R2.id.home:
                Log.d(TAG, messageTAG + " R.id.home !!!");
                return true;
            case android.R.id.home:
                Log.d(TAG, messageTAG+" android.R.id.home !!!");
                getSupportFragmentManager().popBackStack();
                return true;
            case R2.id.action_cart:
                return TActivity.onCartOptionSelected(this);
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        sendNotifLocalyticsCallback();
    }

    private void sendNotifLocalyticsCallback() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            if (bundle.containsKey(AppEventTracking.LOCA.NOTIFICATION_BUNDLE)){
                TrackingUtils.eventLocaNotificationCallback(getIntent());
            }
        }
    }
}
