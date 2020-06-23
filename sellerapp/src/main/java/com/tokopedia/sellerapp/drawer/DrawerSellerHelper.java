package com.tokopedia.sellerapp.drawer;

import android.app.Activity;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.core.view.GravityCompat;

import com.tkpd.library.ui.view.LinearLayoutManager;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerAdapter;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerSellerHeaderDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.gm.featured.view.activity.GMFeaturedProductActivity;
import com.tokopedia.gm.resource.GMConstant;
import com.tokopedia.gm.statistic.view.activity.GMStatisticDashboardActivity;
import com.tokopedia.power_merchant.subscribe.tracking.GMTracking;
import com.tokopedia.profile.view.activity.ProfileActivity;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerhome.view.activity.SellerHomeActivity;
import com.tokopedia.track.TrackApp;
import com.tokopedia.url.TokopediaUrl;

import java.util.ArrayList;

/**
 * Created by nisie on 5/6/17.
 */

public class DrawerSellerHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerSellerHeaderDataBinder.DrawerHeaderListener {

    private static final String DIGITAL_PATH_MITRA = "https://pulsa.tokopedia.com/mitra/";
    private TextView shopName;
    private TextView shopLabel;
    private ImageView shopIcon;
    private View shopLayout;
    private View footerShadow;
    private DrawerItem powerMerchantDrawerItem;

    private SessionHandler sessionHandler;

    public DrawerSellerHelper(Activity activity,
                              SessionHandler sessionHandler,
                              LocalCacheHandler drawerCache) {
        super(activity);
        this.sessionHandler = sessionHandler;
        this.drawerCache = drawerCache;
        shopName = activity.findViewById(R.id.label);
        shopLabel = activity.findViewById(R.id.sublabel);
        shopIcon = activity.findViewById(R.id.icon);
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
        initRemoteConfig();
        ArrayList<DrawerItem> data = new ArrayList<>();
        powerMerchantDrawerItem = getInstance();

        data.add(new DrawerItem(context.getString(R.string.drawer_title_home),
                R.drawable.icon_home,
                TkpdState.DrawerPosition.SELLER_INDEX_HOME,
                true));

        data.add(getSellerMenu());
        data.add(getInboxMenu());
        data.add(getProductMenu());
        data.add(new DrawerItem(context.getString(R.string.drawer_title_top_ads),
                R.drawable.ic_top_ads,
                TkpdState.DrawerPosition.SELLER_TOP_ADS,
                true));
        if (!((SellerDrawerAdapter) adapter).isOfficialStore()) {
            data.add(powerMerchantDrawerItem);
        } else {
            data.remove(powerMerchantDrawerItem);
        }
        data.add(new DrawerItem(context.getString(R.string.drawer_title_new_reso_seller),
                R.drawable.ic_reso,
                TkpdState.DrawerPosition.RESOLUTION_CENTER,
                true,
                getTotalResoNotif()));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_mitra_toppers),
                R.drawable.ic_mitra_toppers,
                TkpdState.DrawerPosition.SELLER_MITRA_TOPPERS,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_statistic),
                R.drawable.statistik_icon,
                TkpdState.DrawerPosition.SELLER_GM_STAT,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_setting),
                R.drawable.icon_setting,
                TkpdState.DrawerPosition.SETTINGS,
                true));
        data.add(new DrawerItem(context.getString(R.string.title_activity_contact_us_drawer),
                R.drawable.ic_contactus,
                TkpdState.DrawerPosition.CONTACT_US,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_logout),
                R.drawable.ic_menu_logout,
                TkpdState.DrawerPosition.LOGOUT,
                true));
        shopLayout.setVisibility(View.VISIBLE);
        footerShadow.setVisibility(View.VISIBLE);

        return data;
    }

    private void initRemoteConfig() {
    }

    private DrawerItem getSellerMenu() {
        DrawerGroup sellerMenu = new DrawerGroup(context.getString(R.string.drawer_title_seller),
                R.drawable.icon_penjualan,
                TkpdState.DrawerPosition.SHOP,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false),
                getTotalSellerNotif());
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_opportunity),
                TkpdState.DrawerPosition.SHOP_OPPORTUNITY_LIST,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_new_order),
                TkpdState.DrawerPosition.SHOP_NEW_ORDER,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_confirm_shipping),
                TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_shipping_status),
                TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_list_selling),
                TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false)));
        return sellerMenu;
    }

    private DrawerItem getInboxMenu() {
        DrawerGroup inboxMenu = new DrawerGroup(context.getString(R.string.drawer_title_inbox),
                R.drawable.icon_inbox,
                TkpdState.DrawerPosition.INBOX,
                drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                getTotalInboxNotif());
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_message),
                TkpdState.DrawerPosition.INBOX_MESSAGE,
                drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE)));
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_discussion),
                TkpdState.DrawerPosition.INBOX_TALK,
                drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK)));
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_review),
                TkpdState.DrawerPosition.INBOX_REVIEW,
                drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW)));
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_ticket),
                TkpdState.DrawerPosition.INBOX_TICKET,
                drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET)));
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_seller_info),
                TkpdState.DrawerPosition.SELLER_INFO,
                drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_SELLER_INFO)));
        return inboxMenu;
    }

    private DrawerItem getProductMenu() {
        DrawerGroup sellerMenu = new DrawerGroup(context.getString(R.string.drawer_title_product),
                R.drawable.ic_manage_produk,
                TkpdState.DrawerPosition.SELLER_PRODUCT_EXTEND,
                drawerCache.getBoolean(DrawerAdapter.IS_PRODUCT_OPENED, false),
                0);
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_add_product),
                TkpdState.DrawerPosition.ADD_PRODUCT,
                true));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_product_list),
                TkpdState.DrawerPosition.MANAGE_PRODUCT,
                true));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_draft_list),
                TkpdState.DrawerPosition.DRAFT_PRODUCT,
                true));
        sellerMenu.add(new DrawerItem(context.getString(com.tokopedia.seller.R.string.featured_product_title),
                TkpdState.DrawerPosition.FEATURED_PRODUCT,
                true));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_etalase_list),
                TkpdState.DrawerPosition.MANAGE_ETALASE,
                true));

        return sellerMenu;
    }

    private int getTotalInboxNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_SELLER_INFO, 0);
    }

    private int getTotalSellerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, 0);
    }

    private int getTotalResoNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, 0);
    }

    @Override
    public void initDrawer(Activity activity) {
        this.adapter = new SellerDrawerAdapter(activity, this, drawerCache);
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
        checkExpand(DrawerAdapter.IS_INBOX_OPENED, TkpdState.DrawerPosition.INBOX);
        checkExpand(DrawerAdapter.IS_PEOPLE_OPENED, TkpdState.DrawerPosition.PEOPLE);
        checkExpand(DrawerAdapter.IS_SHOP_OPENED, TkpdState.DrawerPosition.SHOP);
        checkExpand(DrawerAdapter.IS_PRODUCT_DIGITAL_OPENED, TkpdState.DrawerPosition.SELLER_PRODUCT_DIGITAL_EXTEND);
        checkExpand(DrawerAdapter.IS_PRODUCT_OPENED, TkpdState.DrawerPosition.SELLER_PRODUCT_EXTEND);
        checkExpand(DrawerAdapter.IS_GM_OPENED, TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(DrawerItem item) {
        if (item.getId() == selectedPosition) {
            closeDrawer();
        } else {
            Intent intent;
            boolean isNeedToCloseActivity = true;
            switch (item.getId()) {
                case TkpdState.DrawerPosition.INDEX_HOME:
                    eventDrawerClick(AppEventTracking.EventLabel.SELLER_HOME);
                    context.startActivity(SellerHomeActivity.createIntent(context));
                    break;
                case TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND:
                    if (context.getApplication() instanceof AbstractionRouter) {
                        new GMTracking().sendClickHamburgerMenuEvent(item.label);
                    }
                    eventClickGoldMerchantViaDrawer();
                    RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE);
                    break;
                case TkpdState.DrawerPosition.SHOP_NEW_ORDER:
                    intent = SellerRouter.getActivitySellingTransactionNewOrder(context);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.NEW_ORDER);
                    break;
                case TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING:
                    intent = SellerRouter.getActivitySellingTransactionConfirmShipping(context);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_CONFIRMATION);
                    break;
                case TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS:
                    intent = SellerRouter.getActivitySellingTransactionShippingStatus(context);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_STATUS);
                    break;
                case TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST:
                    intent = SellerRouter.getActivitySellingTransactionList(context);
                    context.startActivity(intent);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.SALES_LIST);
                    break;
                case TkpdState.DrawerPosition.SHOP_OPPORTUNITY_LIST:
                    intent = SellerRouter.getActivitySellingTransactionOpportunity(context, "");
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.ADD_PRODUCT:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        Intent manageProductIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_MANAGE_LIST);
                        Intent addProductIntent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.PRODUCT_ADD_ITEM);
                        TaskStackBuilder.create(context)
                                .addNextIntent(manageProductIntent)
                                .addNextIntent(addProductIntent)
                                .startActivities();
                    }
                    break;
                case TkpdState.DrawerPosition.MANAGE_PRODUCT:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        ((TkpdCoreRouter) context.getApplication()).goToManageProduct(context);
                    }
                    break;
                case TkpdState.DrawerPosition.MANAGE_PAYMENT_AND_TOPUP:
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, DIGITAL_PATH_MITRA);
                    eventClickPaymentAndTopupOnDrawer();
                    break;
                case TkpdState.DrawerPosition.MANAGE_TRANSACTION_DIGITAL:
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, DIGITAL_PATH_MITRA);
                    eventClickDigitalTransactionListOnDrawer();
                    break;
                case TkpdState.DrawerPosition.DRAFT_PRODUCT:
                    eventDrawerClick(AppEventTracking.EventLabel.DRAFT_PRODUCT);
                    context.startActivity(new Intent(context, ProductDraftListActivity.class));
                    break;
                case TkpdState.DrawerPosition.MANAGE_ETALASE:
                    RouteManager.route(context, ApplinkConstInternalMarketplace.SHOP_SETTINGS_ETALASE);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISPLAY);
                    break;
                case TkpdState.DrawerPosition.SELLER_GM_STAT:
                    intent = new Intent(context, GMStatisticDashboardActivity.class);
                    context.startActivity(intent);
                    eventClickGMStat(AppEventTracking.Category.HAMBURGER,
                            AppEventTracking.EventLabel.STATISTIC);
                    break;
                case TkpdState.DrawerPosition.SELLER_MITRA_TOPPERS:
                    RouteManager.route(context, ApplinkConstInternalGlobal.WEBVIEW, TokopediaUrl.getInstance().getWEB() + "mitra-toppers/");
                    break;
                case TkpdState.DrawerPosition.SELLER_TOP_ADS:
                    eventDrawerClick(AppEventTracking.EventLabel.TOPADS);
                    RouteManager.route(context, ApplinkConst.SellerApp.TOPADS_AUTOADS);
                    break;
                case TkpdState.DrawerPosition.SELLER_FLASH_SALE:
                    RouteManager.route(context, ApplinkConst.SellerApp.FLASHSALE_MANAGEMENT);
                    break;
                case TkpdState.DrawerPosition.FEATURED_PRODUCT:
                    eventFeaturedProduct(AppEventTracking.EventLabel.FEATURED_PRODUCT);
                    intent = new Intent(context, GMFeaturedProductActivity.class);
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.SELLER_INFO:
                    eventSellerInfo(AppEventTracking.Action.CLICK_HAMBURGER_ICON, AppEventTracking.EventLabel.SELLER_INFO);
                    RouteManager.route(context, ApplinkConst.SELLER_INFO);
                    break;
                default:
                    super.onItemClicked(item);
            }

            if (selectedPosition != TkpdState.DrawerPosition.INDEX_HOME
                    && item.getId() != TkpdState.DrawerPosition.LOGOUT && isNeedToCloseActivity) {
                context.finish();
            }
            closeDrawer();
        }
    }

    public void eventSellerInfo(String eventAction, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.SELLER_INFO,
                AppEventTracking.Category.SELLER_INFO_HOMEPAGE,
                eventAction,
                eventLabel);
    }

    public void eventClickGoldMerchantViaDrawer() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.CLICK_NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.BUY_GM);
    }

    public void eventFeaturedProduct(String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.SHOP_MANAGE,
                AppEventTracking.Category.FEATURED_PRODUCT,
                AppEventTracking.Action.CLICK,
                eventLabel);
    }

    public void eventDrawerClick(String label) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.NAVIGATION_DRAWER,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                label);
    }

    public void eventClickPaymentAndTopupOnDrawer() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.PAYMENT_AND_TOPUP);
    }

    public static void eventClickDigitalTransactionListOnDrawer() {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.USER_INTERACTION_HOMEPAGE,
                AppEventTracking.Category.HAMBURGER,
                AppEventTracking.Action.CLICK,
                AppEventTracking.EventLabel.DIGITAL_TRANSACTION_LIST);
    }

    private void eventClickGMStat(String eventCategory, String eventLabel) {
        TrackApp.getInstance().getGTM().sendGeneralEvent(
                AppEventTracking.Event.CLICK_GM_STAT,
                eventCategory,
                AppEventTracking.Action.CLICK,
                eventLabel);
    }

    private void showDialogActionGoToGMSubscribe() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.featured_product_title);
        alertDialog.setMessage(context.getString(R.string.featured_product_desc_should_gold_merchant,
                context.getString(GMConstant.getGMTitleResource(context))));
        alertDialog.setPositiveButton(R.string.label_subscribe, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendGMAnalyticDialogEvent(true);
                RouteManager.route(context, ApplinkConstInternalMarketplace.POWER_MERCHANT_SUBSCRIBE);
            }
        });
        alertDialog.setNegativeButton(R.string.title_cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                sendGMAnalyticDialogEvent(false);
            }
        });
        alertDialog.show();
    }

    private void sendGMAnalyticDialogEvent(boolean isSubscribing) {
        if (context.getApplication() instanceof AbstractionRouter) {
            new GMTracking().sendClickManageProductDialogEvent(isSubscribing);
        }
    }

    @Override
    public void onGoToDeposit() {
        if (context.getApplicationContext() instanceof SellerModuleRouter) {
            SellerModuleRouter sellerModuleRouter = (SellerModuleRouter) context.getApplicationContext();
            sellerModuleRouter.startSaldoDepositIntent(context);
            sendGTMNavigationEvent(AppEventTracking.EventLabel.SHOP_EN);
        }
    }

    @Override
    public void onGoToProfile() {
        context.startActivity(
                ProfileActivity.Companion.createIntent(context, SessionHandler.getLoginID(context))
        );
        sendGTMNavigationEvent(AppEventTracking.EventLabel.PROFILE);
    }

    @Override
    public void onGoToProfileCompletion() {
        Intent intent = new Intent(context, ProfileCompletionActivity.class);
        context.startActivity(intent);
    }

    private void onGoToShop() {
        if (context.getApplicationContext() instanceof SellerModuleRouter) {
            SellerModuleRouter sellerModuleRouter = (SellerModuleRouter) context.getApplicationContext();
            Intent intent = sellerModuleRouter.getShopPageIntent(context, sessionHandler.getShopID());
            context.startActivity(intent);
            sendGTMNavigationEvent(AppEventTracking.EventLabel.SHOP_EN);
        }
    }

    private DrawerItem getInstance() {
        if (powerMerchantDrawerItem == null) {
            powerMerchantDrawerItem = new DrawerItem(context.getString(R.string.pm_title),
                    R.drawable.ic_pm_badge_shop_regular,
                    TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND,
                    true);
        }
        return powerMerchantDrawerItem;
    }
}
