package com.tokopedia.shop.open.view.activity;

import android.content.Context;
import android.content.Intent;
import androidx.fragment.app.Fragment;

import com.tokopedia.abstraction.common.utils.GlobalConfig;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.core.base.di.component.HasComponent;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.design.component.Dialog;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.base.view.activity.BaseSimpleActivity;
import com.tokopedia.shop.open.R;
import com.tokopedia.shop.open.di.component.DaggerShopOpenDomainComponent;
import com.tokopedia.shop.open.di.component.ShopOpenDomainComponent;
import com.tokopedia.shop.open.di.module.ShopOpenDomainModule;
import com.tokopedia.shop.open.view.fragment.ShopOpenReserveDomainFragment;

/**
 * Created by Nathaniel on 3/16/2017.
 */

public class ShopOpenDomainActivity extends BaseSimpleActivity
        implements HasComponent<ShopOpenDomainComponent> {

    public static Intent getIntent(Context context) {
        Intent intent = new Intent(context, ShopOpenDomainActivity.class);
        return intent;
    }

    @Override
    protected Fragment getNewFragment() {

        if (getIntent() != null) {
            boolean isFromAppShortCut = getIntent().getBooleanExtra(Constants.FROM_APP_SHORTCUTS, false);
            return ShopOpenReserveDomainFragment.newInstance(isFromAppShortCut);
        } else {
            return ShopOpenReserveDomainFragment.newInstance();
        }
    }

    @Override
    public ShopOpenDomainComponent getComponent() {
        return DaggerShopOpenDomainComponent
                .builder()
                .shopOpenDomainModule(new ShopOpenDomainModule())
                .shopComponent(((SellerModuleRouter) getApplication()).getShopComponent())
                .build();
    }

    @Override
    public void onBackPressed() {
        if (GlobalConfig.isSellerApp() && isTaskRoot()) {
            Dialog dialog = new Dialog(this, Dialog.Type.PROMINANCE);
            dialog.setTitle(getString(R.string.open_shop_logout_title));
            dialog.setDesc(getString(R.string.open_shop_logout_confirm));
            dialog.setBtnOk(getString(R.string.open_shop_logout_button));
            dialog.setBtnCancel(getString(R.string.open_shop_cancel));
            dialog.setOnOkClickListener(v -> {
                dialog.dismiss();
                RouteManager.route(this, ApplinkConstInternalGlobal.LOGOUT);
                finish();
            });
            dialog.setOnCancelClickListener(v -> dialog.dismiss());
            dialog.show();
        } else {
            super.onBackPressed();
        }
    }
}