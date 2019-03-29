package com.tokopedia.sellerapp.dashboard.view.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.tokopedia.abstraction.base.view.appupdate.AppUpdateDialogBuilder;
import com.tokopedia.abstraction.base.view.appupdate.ApplicationUpdate;
import com.tokopedia.abstraction.base.view.appupdate.model.DetailUpdate;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.app.DrawerPresenterActivity;
import com.tokopedia.core.gcm.FCMCacheManager;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.gcm.NotificationModHandler;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.graphql.data.GraphqlClient;
import com.tokopedia.product.manage.item.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.view.fragment.DashboardFragment;
import com.tokopedia.sellerapp.dashboard.view.presenter.SellerDashboardDrawerPresenter;
import com.tokopedia.sellerapp.drawer.SellerDrawerAdapter;
import com.tokopedia.sellerapp.fcm.appupdate.FirebaseRemoteAppUpdate;

/**
 * Created by nathan on 9/5/17.
 */

public class DashboardActivity extends DrawerPresenterActivity
        implements GCMHandlerListener, SellerDashboardDrawerPresenter.SellerDashboardView {

    public static final String TAG = DashboardActivity.class.getSimpleName();
    private SellerDashboardDrawerPresenter presenter;

    public static Intent createInstance(Context context) {
        return new Intent(context, DashboardActivity.class);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        GraphqlClient.init(this);
        GetShopInfoUseCase getShopInfoUseCase = null;
        if (getApplicationContext() instanceof SellerModuleRouter) {
            SellerModuleRouter sellerModuleRouter = (SellerModuleRouter) getApplicationContext();
            getShopInfoUseCase = sellerModuleRouter.getShopInfo();
        }
        presenter = new SellerDashboardDrawerPresenter(this, getShopInfoUseCase);
        inflateView(R.layout.activity_simple_fragment);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, DashboardFragment.newInstance(), TAG)
                    .commit();
        }
        checkAppUpdate();
    }

    private void checkAppUpdate() {
        ApplicationUpdate appUpdate = new FirebaseRemoteAppUpdate(this);
        appUpdate.checkApplicationUpdate(new ApplicationUpdate.OnUpdateListener() {
            @Override
            public void onNeedUpdate(final DetailUpdate detail) {
                if (!isFinishing()) {
                    new AppUpdateDialogBuilder(
                        DashboardActivity.this,
                        detail,
                        new AppUpdateDialogBuilder.Listener() {
                            @Override
                            public void onPositiveButtonClicked(DetailUpdate detail) {

                            }

                            @Override
                            public void onNegativeButtonClicked(DetailUpdate detail) {

                            }
                        }
                    ).getAlertDialog().show();
                }
            }

            @Override
            public void onError(Exception e) {
                e.printStackTrace();
            }

            @Override
            public void onNotNeedUpdate() {

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        FCMCacheManager.checkAndSyncFcmId(getApplicationContext());
        NotificationModHandler.showDialogNotificationIfNotShowing(this,
                ManageGeneral.getCallingIntent(this, ManageGeneral.TAB_POSITION_MANAGE_APP)
        );
    }

    // will done in onresume
    @Override
    protected void updateDrawerData() {
        if (sessionHandler.isV4Login()) {
            setDataDrawer();

            getDrawerSellerAttrUseCase(sessionHandler);
            presenter.getFlashsaleSellerStatus(sessionHandler.getShopID());
            presenter.isGoldMerchantAsync();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.unsubscribe();
    }

    @Override
    public void onSuccessGetFlashsaleSellerStatus(Boolean isVisible) {
        SellerDrawerAdapter sellerDrawerAdapter = ((SellerDrawerAdapter) drawerHelper.getAdapter());
        if (isVisible != sellerDrawerAdapter.isFlashSaleVisible()) {
            sellerDrawerAdapter.setFlashSaleVisible(isVisible);
            drawerHelper.getRecyclerView().post(new Runnable() {
                @Override
                public void run() {
                    sellerDrawerAdapter.renderFlashSaleDrawer();
                }
            });
        }
    }

    @Override
    public void onSuccessGetShopInfo(ShopModel shopModel) {
        SellerDrawerAdapter sellerDrawerAdapter = ((SellerDrawerAdapter) drawerHelper.getAdapter());
        if (sellerDrawerAdapter.isGoldMerchant()!= shopModel.info.isGoldMerchant()) {
            sellerDrawerAdapter.setGoldMerchant(shopModel.info.isGoldMerchant());
            drawerHelper.getRecyclerView().post(new Runnable() {
                @Override
                public void run() {
                    sellerDrawerAdapter.renderGMDrawer();
                }
            });
        }
    }

    @Override
    public void onGCMSuccess(String gcmId) {
        
    }

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
    protected int getLayoutId() {
        return 0;
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
    protected int setDrawerPosition() {
        return TkpdState.DrawerPosition.SELLER_INDEX_HOME;
    }

    @Override
    public Context getContext() {
        return this;
    }
}
