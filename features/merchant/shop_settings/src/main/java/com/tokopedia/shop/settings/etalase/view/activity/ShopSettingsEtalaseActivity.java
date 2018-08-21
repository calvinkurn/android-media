package com.tokopedia.shop.settings.etalase.view.activity;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.settings.R;
import com.tokopedia.shop.settings.etalase.data.ShopEtalaseViewModel;
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseListFragment;
import com.tokopedia.shop.settings.etalase.view.fragment.ShopSettingsEtalaseReorderFragment;
import com.tokopedia.shop.settings.notes.data.ShopNoteViewModel;

import java.util.ArrayList;
import java.util.List;

public class ShopSettingsEtalaseActivity extends BaseSimpleActivity
        implements ShopSettingsEtalaseListFragment.OnShopSettingsEtalaseFragmentListener,
        ShopSettingsEtalaseReorderFragment.OnShopSettingsEtalaseReorderFragmentListener {

    private TextView tvSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        tvSave = findViewById(R.id.tvSave);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShopSettingsEtalaseReorderFragment fragment = getReorderFragment();
                if (fragment!= null) {
                    fragment.saveReorder();
                }
            }
        });
        tvSave.setVisibility(View.GONE);
    }

    private ShopSettingsEtalaseReorderFragment getReorderFragment(){
        return (ShopSettingsEtalaseReorderFragment) getSupportFragmentManager()
                .findFragmentByTag(ShopSettingsEtalaseReorderFragment.TAG);
    }

    @Override
    protected Fragment getNewFragment() {
        return ShopSettingsEtalaseListFragment.newInstance();
    }

    @Override
    public void onBackPressed() {
        if (getSupportFragmentManager().getBackStackEntryCount() != 0) {
            tvSave.setVisibility(View.GONE);
            getSupportFragmentManager().popBackStack();
        }
        else {
            super.onBackPressed();
        }
    }

    @Override
    protected int getLayoutRes() {
        return R.layout.activity_shop_setting_etalase;
    }

    public View getSaveButton(){
        return tvSave;
    }

    @Override
    public void goToReorderFragment(ArrayList<ShopEtalaseViewModel> models) {
        ShopSettingsEtalaseReorderFragment fragment = ShopSettingsEtalaseReorderFragment.newInstance(models);
        replaceAndHideOldFragment(fragment,true, ShopSettingsEtalaseReorderFragment.TAG);
        tvSave.setVisibility(View.VISIBLE);
        invalidateOptionsMenu();
    }

    @Override
    public void onSuccessReorderEtalase() {
        onBackPressed();
        if (showFragment(getTagFragment())) {
            ShopSettingsEtalaseListFragment fragment = (ShopSettingsEtalaseListFragment) getFragment();
            if (fragment != null) {
                fragment.refreshData();
            }
        }
    }

    public void replaceAndHideOldFragment(Fragment fragment, boolean addToBackstack, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction ft= fragmentManager.beginTransaction();
        Fragment currentVisibileFragment = getCurrentVisibleFragment(fragmentManager);
        if (currentVisibileFragment!= null) {
            ft.hide(currentVisibileFragment);
        }
        if (!addToBackstack) {
            ft.add(R.id.parent_view, fragment, tag).show(fragment).commit();
        }
        else {
            ft.add(R.id.parent_view, fragment, tag).addToBackStack(tag).show(fragment).commit();
        }
    }

    public boolean showFragment(String tag) {
        Fragment f = getSupportFragmentManager().findFragmentByTag(tag);
        if (f == null) {
            return false;
        } else {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction ft= fragmentManager.beginTransaction();
            Fragment currentVisibileFragment = getCurrentVisibleFragment(fragmentManager);
            if (currentVisibileFragment!= null) {
                ft.hide(currentVisibileFragment);
            }
            ft.show(f).commit();
            return true;
        }
    }

    // assume only 1 fragment visible
    private Fragment getCurrentVisibleFragment(FragmentManager fragmentManager){
        List<Fragment> fragmentList = fragmentManager.getFragments();
        for (int i =0, sizei = fragmentList.size(); i<sizei; i++) {
            Fragment f = fragmentList.get(i);
            if (f!= null && f.isVisible()) {
                return f;
            }
        }
        return null;
    }

}
