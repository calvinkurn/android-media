package com.tokopedia.sellerapp.drawer;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.deposit.activity.DepositActivity;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerAdapter;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerSellerHeaderDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerSeparator;
import com.tokopedia.core.people.activity.PeopleInfoDrawerActivity;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.seller.gmsubscribe.view.activity.GmSubscribeHomeActivity;
import com.tokopedia.seller.shopsettings.etalase.activity.EtalaseShopEditor;
import com.tokopedia.seller.topads.view.activity.TopAdsDashboardActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmstat.activities.GMStatActivity;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

import java.util.ArrayList;

import static com.tokopedia.core.drawer2.view.DrawerAdapter.IS_GM_OPENED;
import static com.tokopedia.core.drawer2.view.DrawerAdapter.IS_INBOX_OPENED;
import static com.tokopedia.core.drawer2.view.DrawerAdapter.IS_SHOP_OPENED;

/**
 * Created by nisie on 5/6/17.
 */

public class DrawerSellerHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerSellerHeaderDataBinder.DrawerHeaderListener {

    private TextView shopName;
    private TextView shopLabel;
    private ImageView shopIcon;
    private View shopLayout;
    private View footerShadow;

    private SessionHandler sessionHandler;


    public DrawerSellerHelper(Activity activity,
                              SessionHandler sessionHandler,
                              LocalCacheHandler drawerCache) {
        super(activity);
        this.sessionHandler = sessionHandler;
        this.drawerCache = drawerCache;
        shopName = (TextView) activity.findViewById(R.id.label);
        shopLabel = (TextView) activity.findViewById(R.id.sublabel);
        shopIcon = (ImageView) activity.findViewById(R.id.icon);
        shopLayout = activity.findViewById(R.id.drawer_shop);
        footerShadow = activity.findViewById(R.id.drawer_footer_shadow);
    }

    public static DrawerSellerHelper createInstance(Activity activity,
                                                    SessionHandler sessionHandler,
                                                    LocalCacheHandler drawerCache) {
        return new DrawerSellerHelper(activity, sessionHandler, drawerCache);
    }

    @Override
    public ArrayList<DrawerItem> createDrawerData() {
        ArrayList<DrawerItem> data = new ArrayList<>();

        data.add(new DrawerItem("Beranda",
                R.drawable.icon_home,
                TkpdState.DrawerPosition.SELLER_INDEX_HOME,
                true));
        data.add(getGoldMerchantMenu());
        data.add(new DrawerItem("Statistik",
                R.drawable.statistik_icon,
                TkpdState.DrawerPosition.SELLER_GM_STAT,
                true));
        data.add(new DrawerItem(context.getString(R.string.title_top_ads),
                R.drawable.ic_top_ads,
                TkpdState.DrawerPosition.SELLER_TOP_ADS,
                true));

        data.add(getInboxMenu());
        data.add(getSellerMenu());

        data.add(new DrawerItem("Pengaturan",
                R.drawable.icon_setting,
                TkpdState.DrawerPosition.SETTINGS,
                true));
        data.add(new DrawerItem("Hubungi Kami",
                R.drawable.ic_contactus,
                TkpdState.DrawerPosition.CONTACT_US,
                true));
        if (!TrackingUtils.getBoolean(AppEventTracking.GTM.CONTACT_US)) {
            data.add(new DrawerItem("Bantuan",
                    R.drawable.ic_help,
                    TkpdState.DrawerPosition.HELP,
                    true));
        }
        data.add(new DrawerItem("Keluar",
                R.drawable.ic_menu_logout,
                TkpdState.DrawerPosition.LOGOUT,
                true));
        if (GlobalConfig.isAllowDebuggingTools()) {
            data.add(new DrawerItem("Developer Options",
                    0,
                    TkpdState.DrawerPosition.DEVELOPER_OPTIONS,
                    true));
        }

        shopLayout.setVisibility(View.VISIBLE);
        footerShadow.setVisibility(View.VISIBLE);
        return data;
    }

    private DrawerItem getSellerMenu() {
        DrawerGroup sellerMenu = new DrawerGroup("Penjualan",
                R.drawable.icon_penjualan,
                TkpdState.DrawerPosition.SHOP,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                getTotalSellerNotif());
        sellerMenu.add(new DrawerItem("Order Baru",
                0,
                TkpdState.DrawerPosition.SHOP_NEW_ORDER,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER)));
        sellerMenu.add(new DrawerItem("Konfirmasi Pengiriman",
                0,
                TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION)));
        sellerMenu.add(new DrawerItem("Status Pengiriman",
                0,
                TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS)));
        sellerMenu.add(new DrawerItem("Daftar Penjualan",
                0,
                TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST,
                drawerCache.getBoolean(IS_SHOP_OPENED, false)));
        sellerMenu.add(new DrawerSeparator(drawerCache.getBoolean(IS_SHOP_OPENED, false)));
        sellerMenu.add(new DrawerItem("Daftar Produk",
                0,
                TkpdState.DrawerPosition.MANAGE_PRODUCT,
                drawerCache.getBoolean(IS_SHOP_OPENED, false)));
        sellerMenu.add(new DrawerItem("Etalase Toko",
                0,
                TkpdState.DrawerPosition.MANAGE_ETALASE,
                drawerCache.getBoolean(IS_SHOP_OPENED, false)));

        return sellerMenu;
    }

    private DrawerItem getInboxMenu() {
        DrawerGroup inboxMenu = new DrawerGroup("Kotak Masuk",
                R.drawable.icon_inbox,
                TkpdState.DrawerPosition.INBOX,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                getTotalInboxNotif());
        inboxMenu.add(new DrawerItem("Pesan",
                0,
                TkpdState.DrawerPosition.INBOX_MESSAGE,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE)));
        inboxMenu.add(new DrawerItem("Diskusi Produk",
                0,
                TkpdState.DrawerPosition.INBOX_TALK,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK)));
        inboxMenu.add(new DrawerItem("Ulasan",
                0,
                TkpdState.DrawerPosition.INBOX_REVIEW,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW)));
        inboxMenu.add(new DrawerItem("Layanan Pengguna",
                0,
                TkpdState.DrawerPosition.INBOX_TICKET,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET)));
        inboxMenu.add(new DrawerItem("Pusat Resolusi",
                0,
                TkpdState.DrawerPosition.RESOLUTION_CENTER,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER)));
        return inboxMenu;
    }

    private int getTotalInboxNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET, 0);
    }

    private int getTotalSellerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, 0);
    }

    private DrawerItem getGoldMerchantMenu() {
        DrawerGroup gmMenu = new DrawerGroup("Gold Merchant",
                R.drawable.ic_goldmerchant_drawer,
                TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE,
                drawerCache.getBoolean(IS_GM_OPENED, false),
                0);
        String gmString = sessionHandler.isGoldMerchant(context) ?
                context.getString(R.string.extend_gold_merchant) :
                context.getString(R.string.upgrade_gold_merchant);

        gmMenu.add(new DrawerItem(gmString,
                0,
                TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND,
                drawerCache.getBoolean(IS_GM_OPENED, false),
                0));
        return gmMenu;
    }

    @Override
    public void initDrawer(Activity activity) {
        this.adapter = DrawerAdapter.createAdapter(activity, this, drawerCache);
        this.adapter.setData(createDrawerData());
        this.adapter.setHeader(new DrawerSellerHeaderDataBinder(adapter, activity, this, drawerCache));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        setExpand();
        closeDrawer();
    }

    @Override
    public boolean isOpened() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    @Override
    public void setFooterData(DrawerProfile profile) {
        shopName.setText(profile.getShopName());
        shopIcon.setVisibility(View.VISIBLE);
        shopLabel.setVisibility(View.VISIBLE);
        ImageHandler.LoadImage(shopIcon, profile.getShopAvatar());
        shopLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onGoToShop();
            }
        });
    }

    @Override
    public void setSelectedPosition(int id) {
        super.setSelectedPosition(id);
        adapter.setSelectedItem(id);
    }

    @Override
    public void setExpand() {
        if (drawerCache.getBoolean(IS_INBOX_OPENED, false)) {
            DrawerGroup group = findGroup(TkpdState.DrawerPosition.INBOX);
            if (group != null)
                adapter.getData().addAll(group.getPosition() + 1, group.getList());
        }

        if (drawerCache.getBoolean(DrawerAdapter.IS_PEOPLE_OPENED, false)) {
            DrawerGroup group = findGroup(TkpdState.DrawerPosition.PEOPLE);
            if (group != null)
                adapter.getData().addAll(group.getPosition() + 1, group.getList());
        }
        if (drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false)) {
            DrawerGroup group = findGroup(TkpdState.DrawerPosition.SHOP);
            if (group != null)
                adapter.getData().addAll(group.getPosition() + 1, group.getList());
        }
        adapter.notifyDataSetChanged();
    }

    private DrawerGroup findGroup(int id) {
        for (int i = 0; i < adapter.getData().size(); i++) {
            if (adapter.getData().get(i) instanceof DrawerGroup
                    && adapter.getData().get(i).getId() == id) {
                adapter.getData().get(i).setPosition(i);
                return (DrawerGroup) adapter.getData().get(i);
            }
        }
        return null;
    }

    @Override
    public void onItemClicked(DrawerItem item) {
        if (item.getId() == selectedPosition) {
            closeDrawer();
        } else {
            Intent intent;
            switch (item.getId()) {
                case TkpdState.DrawerPosition.INDEX_HOME:
                    context.startActivity(new Intent(context, SellerHomeActivity.class));
                    break;
                case TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND:
                    context.startActivity(new Intent(context, GmSubscribeHomeActivity.class));
                    break;
                case TkpdState.DrawerPosition.SHOP_NEW_ORDER:
                    intent = SellerRouter.getActivitySellingTransaction(context);
                    Bundle bundle = new Bundle();
                    bundle.putInt("tab", 1);
                    bundle.putString("user_id", SessionHandler.getLoginID(context));
                    intent.putExtras(bundle);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.NEW_ORDER);
                    break;
                case TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING:
                    intent = SellerRouter.getActivitySellingTransaction(context);
                    intent.putExtra("tab", 2);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_CONFIRMATION);
                    break;
                case TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS:
                    intent = SellerRouter.getActivitySellingTransaction(context);
                    intent.putExtra("tab", 3);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_STATUS);
                    break;
                case TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST:
                    intent = SellerRouter.getActivitySellingTransaction(context);
                    intent.putExtra("tab", 4);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.SALES_LIST);
                    break;
                case TkpdState.DrawerPosition.MANAGE_PRODUCT:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        ((TkpdCoreRouter) context.getApplication()).goToManageProduct(context);
                    }
                    break;
                case TkpdState.DrawerPosition.MANAGE_ETALASE:
                    startIntent(context, EtalaseShopEditor.class);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISPLAY);
                    break;
                case TkpdState.DrawerPosition.SELLER_GM_STAT:
                    intent = new Intent(context, GMStatActivity.class);
                    intent.putExtra(GMStatActivity.SHOP_ID, SessionHandler.getShopID(context));
                    intent.putExtra(GMStatActivity.IS_GOLD_MERCHANT, SessionHandler.isGoldMerchant(context));
                    context.startActivity(intent);
                    UnifyTracking.eventClickGMStat();
                    break;
                case TkpdState.DrawerPosition.SELLER_TOP_ADS:
                    intent = new Intent(context, TopAdsDashboardActivity.class);
                    context.startActivity(intent);
                    break;
                default:
                    super.onItemClicked(item);
            }

            if (selectedPosition != TkpdState.DrawerPosition.INDEX_HOME
                    && item.getId() != TkpdState.DrawerPosition.LOGOUT) {
                context.finish();
            }
            closeDrawer();
        }
    }

    @Override
    public void onGoToDeposit() {
        Intent intent = new Intent(context, DepositActivity.class);
        context.startActivity(intent);
        sendGTMNavigationEvent(AppEventTracking.EventLabel.DEPOSIT);
    }

    @Override
    public void onGoToProfile() {
        context.startActivity(
                PeopleInfoDrawerActivity.createInstance(context, sessionHandler.getLoginID(context))
        );
        sendGTMNavigationEvent(AppEventTracking.EventLabel.PROFILE);

    }

    private void onGoToShop() {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(sessionHandler.getShopID(), ""));
        context.startActivity(intent);
        sendGTMNavigationEvent(AppEventTracking.EventLabel.SHOP_EN);
    }


}
