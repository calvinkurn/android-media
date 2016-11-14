package com.tokopedia.core.shopinfo.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.shopinfo.fragment.ShopFavoritedFragment;
import com.tokopedia.core.shopinfo.presenter.ShopFavoritedFragmentPresenterImpl;


/**
 * @author  by Alifa on 10/5/2016.
 */
public class ShopFavoritedActivity extends BasePresenterActivity {

    private static final String TAG = "SHOP_FAVORITED_FRAGMENT";

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_FAVORITER;
    }

    public static Intent createInstance(Context context) {
        return new Intent(context, ShopFavoritedActivity.class);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {
        //presenter = new BasePresenterActivity();
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_shop_favorited;
    }

    @Override
    protected void initView() {
        ShopFavoritedFragment fragment = ShopFavoritedFragment.createInstance(getIntent().getExtras().getString("shop_id"));
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentTransaction.add(R.id.container, fragment, TAG);
        fragmentTransaction.commit();
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
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == ShopFavoritedFragmentPresenterImpl.REQUEST_FAVORITEE_CODE && resultCode == Activity.RESULT_OK){
            ((ShopFavoritedFragment)getFragmentManager().findFragmentByTag(TAG)).refresh();
        }
    }
}
