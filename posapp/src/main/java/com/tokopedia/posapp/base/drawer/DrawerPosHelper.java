package com.tokopedia.posapp.base.drawer;

import android.app.Activity;
import android.content.Intent;
import android.view.View;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerAdapter;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerPosHeaderDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.posapp.PosSessionHandler;
import com.tokopedia.posapp.R;
import com.tokopedia.posapp.outlet.view.activity.OutletActivity;
import com.tokopedia.posapp.product.management.view.activity.ProductManagementActivity;
import com.tokopedia.posapp.product.productlist.view.activity.ProductListActivity;
import com.tokopedia.posapp.transaction.TransactionHistoryActivity;
import com.tokopedia.posapp.auth.validatepassword.view.fragment.ValidatePasswordFragment;

import java.util.ArrayList;

/**
 * Created by Herdi_WORK on 07.09.17.
 */

public class DrawerPosHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerPosHeaderDataBinder.DrawerHeaderListener {

    private View shopLayout;
    private View footerShadow;
    private SessionHandler sessionHandler;

    public DrawerPosHelper(Activity activity,
                           SessionHandler sessionHandler,
                           LocalCacheHandler drawerCache) {
        super(activity);
        this.sessionHandler = sessionHandler;
        this.drawerCache = drawerCache;
        shopLayout = activity.findViewById(R.id.drawer_shop);
        footerShadow = activity.findViewById(R.id.drawer_footer_shadow);
        shopLayout.setVisibility(View.GONE);
        footerShadow.setVisibility(View.GONE);
    }

    public static DrawerPosHelper createInstance(Activity activity,
                                                 SessionHandler sessionHandler,
                                                 LocalCacheHandler drawerCache) {
        return new DrawerPosHelper(activity, sessionHandler, drawerCache);
    }

    @Override
    public ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();

        data.add(new DrawerItem(context.getString(R.string.drawer_title_home),
                R.drawable.icon_home,
                TkpdState.DrawerPosition.INDEX_HOME,
                true));
        if(PosSessionHandler.getOutletId(context) != null && !PosSessionHandler.getOutletId(context).isEmpty()) {
            data.add(new DrawerItem(context.getString(R.string.drawer_title_pos_riwayat_tx),
                    R.drawable.ic_hourglass,
                    TkpdState.DrawerPosition.POS_TRANSACTION_HISTORY,
                    true));
        }
        data.add(new DrawerItem(context.getString(R.string.drawer_title_pos_choose_outlet),
                R.drawable.ic_store,
                TkpdState.DrawerPosition.POS_OUTLET,
                true));
        if(PosSessionHandler.getOutletId(context) != null && !PosSessionHandler.getOutletId(context).isEmpty()) {
            data.add(new DrawerItem(context.getString(R.string.drawer_title_pos_product_management),
                    R.drawable.ic_product_management,
                    TkpdState.DrawerPosition.POS_PRODUCT_MANAGEMENET,
                    true));
        }
        data.add(new DrawerItem(context.getString(R.string.drawer_title_logout),
                R.drawable.icon_logout,
                TkpdState.DrawerPosition.LOGOUT,
                true));

        if (GlobalConfig.isAllowDebuggingTools()) {
            data.add(new DrawerItem(context.getString(R.string.drawer_title_developer_option),
                    TkpdState.DrawerPosition.DEVELOPER_OPTIONS,
                    true));
        }

        shopLayout.setVisibility(View.GONE);
        footerShadow.setVisibility(View.GONE);
        return data;
    }

    @Override
    public void initDrawer(Activity activity) {
        this.adapter = DrawerAdapter.createAdapter(activity, this, drawerCache);
        this.adapter.setData(createDrawerData());
        this.adapter.setHeader(new DrawerPosHeaderDataBinder(adapter, activity, this, drawerCache));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        setExpand();
        closeDrawer();
    }

    @Override
    public boolean isOpened() {
        return false;
    }

    @Override
    public void setFooterData(DrawerProfile profile) {

    }

    @Override
    public void setExpand() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(DrawerItem item) {
        if (item.getId() == selectedPosition) {
            closeDrawer();
        } else {
            PosSessionHandler posSessionHandler = new PosSessionHandler(context);
            switch (item.getId()) {
                case TkpdState.DrawerPosition.INDEX_HOME:
                    Intent intent;
                    if(PosSessionHandler.getOutletId(context) != null
                            && !PosSessionHandler.getOutletId(context).isEmpty()) {
                        intent = ProductListActivity.newTopIntent(context);
                    } else {
                        intent = OutletActivity.newTopIntent(context);
                    }
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.POS_TRANSACTION_HISTORY:
                    posSessionHandler.showPasswordDialog(
                            context.getString(R.string.drawer_title_pos_riwayat_tx),
                            new ValidatePasswordFragment.PasswordListener() {
                                @Override
                                public void onSuccess(ValidatePasswordFragment dialog) {
                                    dialog.dismiss();
                                    startIntent(context, TransactionHistoryActivity.class);
                                }

                                @Override
                                public void onError(String message) {

                                }
                            }
                    );
                    break;
                case TkpdState.DrawerPosition.POS_OUTLET:
                    posSessionHandler.showPasswordDialog(
                            context.getString(R.string.drawer_title_pos_choose_outlet),
                            new ValidatePasswordFragment.PasswordListener() {
                                @Override
                                public void onSuccess(ValidatePasswordFragment dialog) {
                                    dialog.dismiss();
                                    startIntent(context, OutletActivity.class);
                                    context.finish();
                                }

                                @Override
                                public void onError(String message) {

                                }
                            }
                    );
                    break;
                case TkpdState.DrawerPosition.POS_PRODUCT_MANAGEMENET:
                    posSessionHandler.showPasswordDialog(
                            context.getString(R.string.drawer_title_pos_product_management),
                            new ValidatePasswordFragment.PasswordListener() {
                                @Override
                                public void onSuccess(ValidatePasswordFragment dialog) {
                                    dialog.dismiss();
                                    startIntent(context, ProductManagementActivity.class);
                                }

                                @Override
                                public void onError(String message) {

                                }
                            }
                    );
                    break;
                default:
                    super.onItemClicked(item);
            }

            closeDrawer();
        }
    }
}
