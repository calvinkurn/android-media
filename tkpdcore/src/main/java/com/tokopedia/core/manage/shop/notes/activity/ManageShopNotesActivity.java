package com.tokopedia.core.manage.shop.notes.activity;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.net.Uri;
import android.os.Bundle;

import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.app.BasePresenterActivity;
import com.tokopedia.core.manage.shop.notes.fragment.ManageShopNotesFormFragment;
import com.tokopedia.core.manage.shop.notes.fragment.ManageShopNotesFragment;
import com.tokopedia.core.manage.shop.notes.model.ShopNote;

/**
 * Created by nisie on 10/26/16.
 */

public class ManageShopNotesActivity extends BasePresenterActivity {

    public static final String PARAM_SHOP_ID = "shop_id";
    public static final String PARAM_IS_RETURNABLE_POLICY = "IS_RETURNABLE_POLICY";
    public static final String PARAM_IS_EDIT = "IS_EDIT";
    public static final String PARAM_SHOP_NOTE = "SHOP_NOTE";
    private static final String EXTRA_BUNDLE = "EXTRA_BUNDLE_NOTES";
    private Bundle bundle;

    @Override
    protected void setupURIPass(Uri data) {

    }

    @Override
    protected void setupBundlePass(Bundle extras) {

    }

    @Override
    protected void initialPresenter() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState != null) {
            bundle = new Bundle();
            bundle = savedInstanceState.getBundle(EXTRA_BUNDLE);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBundle(EXTRA_BUNDLE, bundle);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_simple_fragment;
    }

    @Override
    protected void initView() {
        if (getIntent().getExtras() != null && bundle == null) {
            bundle = new Bundle();
            bundle = getIntent().getExtras();
        }

        ManageShopNotesFragment fragment = ManageShopNotesFragment.createInstance(bundle);
        fragment.setOnActionShopNoteListener(onActionShopNoteListener());
        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        if (getFragmentManager().findFragmentById(R.id.container) == null) {
            fragmentTransaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
        } else {
            fragmentTransaction.replace(R.id.container, fragment, fragment.getClass().getSimpleName());
        }
        fragmentTransaction.commit();
    }

    private ManageShopNotesFragment.OnActionShopNoteListener onActionShopNoteListener() {
        return new ManageShopNotesFragment.OnActionShopNoteListener() {
            @Override
            public void onAddShopNote(boolean isReturnablePolicy, ShopNote shopNote) {
                if (bundle == null) {
                    bundle = new Bundle();
                }

                bundle.putBoolean(PARAM_IS_RETURNABLE_POLICY, isReturnablePolicy);
                if (shopNote != null) {
                    bundle.putBoolean(PARAM_IS_EDIT, true);
                    bundle.putParcelable(PARAM_SHOP_NOTE, shopNote);
                }

                ManageShopNotesFormFragment fragment = ManageShopNotesFormFragment.createInstance(bundle);
                fragment.setOnFinishActionListener(onFinishAction());
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(R.animator.slide_in_left, 0, 0, R.animator.slide_out_right);
                transaction.add(R.id.container, fragment, fragment.getClass().getSimpleName());
                transaction.addToBackStack("secondStack");
                transaction.commit();
            }

        };
    }

    private ManageShopNotesFormFragment.FinishActionListener onFinishAction() {
        return new ManageShopNotesFormFragment.FinishActionListener() {
            @Override
            public void onFinishAction() {
                getFragmentManager().popBackStack();
                invalidateOptionsMenu();
                ((ManageShopNotesFragment) getFragmentManager().findFragmentByTag(ManageShopNotesFragment.class.getSimpleName())).refresh();
            }
        };
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
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP_NOTE;
    }

    @Override
    public void onBackPressed() {
        if (getFragmentManager().getBackStackEntryCount() > 0) {
            getFragmentManager().popBackStack();
        } else {
            super.onBackPressed();
        }
    }
}
