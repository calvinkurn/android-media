package com.tokopedia.tkpd.drawer;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.text.TextUtils;
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
import com.tokopedia.core.database.manager.GlobalCacheManager;
import com.tokopedia.core.deposit.activity.DepositActivity;
import com.tokopedia.core.drawer2.data.factory.ProfileSourceFactory;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerNotification;
import com.tokopedia.core.drawer2.data.viewmodel.DrawerProfile;
import com.tokopedia.core.drawer2.view.DrawerAdapter;
import com.tokopedia.core.drawer2.view.DrawerHelper;
import com.tokopedia.core.drawer2.view.databinder.DrawerHeaderDataBinder;
import com.tokopedia.core.drawer2.view.databinder.DrawerItemDataBinder;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerGroup;
import com.tokopedia.core.drawer2.view.viewmodel.DrawerItem;
import com.tokopedia.core.loyaltysystem.LoyaltyDetail;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.people.activity.PeopleInfoNoDrawerActivity;
import com.tokopedia.core.remoteconfig.FirebaseRemoteConfigImpl;
import com.tokopedia.core.remoteconfig.RemoteConfig;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.discovery.BrowseProductRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.router.wallet.IWalletRouter;
import com.tokopedia.core.router.wallet.WalletRouterUtil;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdCache;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.loyalty.view.activity.TokoPointWebviewActivity;
import com.tokopedia.profile.view.activity.PeopleInfoActivity;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.seller.info.view.activity.SellerInfoActivity;
import com.tokopedia.seller.shopsettings.etalase.activity.EtalaseShopEditor;
import com.tokopedia.tkpd.R;

import java.util.ArrayList;

import static com.tokopedia.core.drawer2.view.DrawerAdapter.IS_INBOX_OPENED;
import static com.tokopedia.core.drawer2.view.DrawerAdapter.IS_PEOPLE_OPENED;
import static com.tokopedia.core.drawer2.view.DrawerAdapter.IS_RESO_OPENED;
import static com.tokopedia.core.drawer2.view.DrawerAdapter.IS_SHOP_OPENED;

/**
 * @author by nisie on 1/11/17.
 */

public class DrawerBuyerHelper extends DrawerHelper
        implements DrawerItemDataBinder.DrawerItemListener,
        DrawerHeaderDataBinder.DrawerHeaderListener {

    private static final String TOP_SELLER_APPLICATION_PACKAGE = "com.tokopedia.sellerapp";
    private static final int VAL_DEFAULT = 0;

    private TextView shopName;
    private TextView shopLabel;

    private ImageView shopIcon;
    private View shopLayout;
    private View footerShadow;

    private SessionHandler sessionHandler;
    private GlobalCacheManager globalCacheManager;

    private RemoteConfig remoteConfig;

    public DrawerBuyerHelper(Activity activity,
                             SessionHandler sessionHandler,
                             LocalCacheHandler drawerCache,
                             GlobalCacheManager globalCacheManager) {
        super(activity);
        this.sessionHandler = sessionHandler;
        this.drawerCache = drawerCache;
        this.globalCacheManager = globalCacheManager;
        shopName = (TextView) activity.findViewById(R.id.label);
        shopLabel = (TextView) activity.findViewById(R.id.sublabel);
        shopIcon = (ImageView) activity.findViewById(R.id.icon);
        shopLayout = activity.findViewById(R.id.drawer_shop);
        footerShadow = activity.findViewById(R.id.drawer_footer_shadow);
    }

    public static DrawerBuyerHelper createInstance(Activity activity,
                                                   SessionHandler sessionHandler,
                                                   LocalCacheHandler drawerCache,
                                                   GlobalCacheManager globalCacheManager) {
        return new DrawerBuyerHelper(activity, sessionHandler, drawerCache, globalCacheManager);
    }

    @Override
    public ArrayList<DrawerItem> createDrawerData() {
        initRemoteConfig();
        ArrayList<DrawerItem> data = new ArrayList<>();

        if (sessionHandler.isV4Login()) {
            createDataLogin(data);
            shopLayout.setVisibility(View.VISIBLE);
            footerShadow.setVisibility(View.VISIBLE);
        } else {
            createDataGuest(data);
            shopLayout.setVisibility(View.GONE);
            footerShadow.setVisibility(View.GONE);
        }
        return data;
    }

    private void initRemoteConfig() {
        remoteConfig = new FirebaseRemoteConfigImpl(context);
    }

    private void createDataGuest(ArrayList<DrawerItem> data) {
        data.add(new DrawerItem(context.getString(R.string.drawer_title_home), TkpdState
                .DrawerPosition.INDEX_HOME, true));
        data.add(new DrawerItem(context.getString(R.string.title_category),
                TkpdState.DrawerPosition.CATEGORY_NAVIGATION, true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_login), TkpdState
                .DrawerPosition.LOGIN, true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_register), TkpdState
                .DrawerPosition.REGISTER, true));
        if (GlobalConfig.isAllowDebuggingTools()) {
            data.add(new DrawerItem(context.getString(R.string.drawer_title_developer_option),
                    android.R.drawable.stat_sys_warning,
                    TkpdState.DrawerPosition.DEVELOPER_OPTIONS, true));
        }

    }

    private void createDataLogin(ArrayList<DrawerItem> data) {
        data.add(new DrawerItem(context.getString(R.string.drawer_title_home),
                R.drawable.icon_home,
                TkpdState.DrawerPosition.INDEX_HOME,
                true));
        data.add(new DrawerItem(context.getString(R.string.title_category),
                R.drawable.ic_category_black_bold,
                TkpdState.DrawerPosition.CATEGORY_NAVIGATION,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_wishlist),
                R.drawable.icon_wishlist,
                TkpdState.DrawerPosition.WISHLIST,
                true));
        data.add(getInboxMenu());
        data.add(getBuyerMenu());

        if (SessionHandler.isUserHasShop(context)) {
            data.add(getSellerMenu());
            data.add(getProductMenu());
            data.add(getResoMenu());
            data.add(getGoldMerchantMenu());
            data.add(new DrawerItem(context.getString(R.string.drawer_title_top_ads),
                    R.drawable.ic_top_ads,
                    TkpdState.DrawerPosition.SELLER_TOP_ADS,
                    true));
        }
        if (!SessionHandler.isUserHasShop(context)) {
            data.add(new DrawerItem(context.getString(R.string.drawer_title_new_reso),
                    R.drawable.ic_reso,
                    TkpdState.DrawerPosition.RESOLUTION_CENTER,
                    true, getTotalResoBuyerNotif()));
        }
        data.add(new DrawerItem(context.getString(R.string.drawer_title_setting),
                R.drawable.icon_setting,
                TkpdState.DrawerPosition.SETTINGS,
                true));

        showAppShareButton(data);

        data.add(new DrawerItem(context.getString(R.string.drawer_title_activity_contact_us),
                R.drawable.ic_contactus,
                TkpdState.DrawerPosition.CONTACT_US,
                true));
        if (!TrackingUtils.getBoolean(AppEventTracking.GTM.CONTACT_US)) {
            data.add(new DrawerItem(context.getString(R.string.drawer_title_help),
                    R.drawable.ic_help,
                    TkpdState.DrawerPosition.HELP,
                    true));
        }
        data.add(new DrawerItem(context.getString(R.string.drawer_title_logout),
                R.drawable.ic_menu_logout,
                TkpdState.DrawerPosition.LOGOUT,
                true));
    }

    private DrawerItem getGoldMerchantMenu() {
        DrawerItem menu;
        boolean isGoldMerchant = SessionHandler.isGoldMerchant(context);
        if (isGoldMerchant) {
            menu = new DrawerGroup(context.getString(R.string.drawer_title_gold_merchant),
                    R.drawable.ic_goldmerchant_drawer,
                    TkpdState.DrawerPosition.GOLD_MERCHANT,
                    drawerCache.getBoolean(DrawerAdapter.IS_GM_OPENED, false),
                    0);
            ((DrawerGroup) menu).add(new DrawerItem(context.getString(R.string.drawer_title_featured_product),
                    TkpdState.DrawerPosition.FEATURED_PRODUCT, false, true));
        } else {
            menu = (new DrawerItem(context.getString(R.string.drawer_title_gold_merchant),
                    R.drawable.ic_goldmerchant_drawer,
                    TkpdState.DrawerPosition.GOLD_MERCHANT,
                    false));
        }
        return menu;
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
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_etalase_list),
                TkpdState.DrawerPosition.MANAGE_ETALASE,
                true));
        return sellerMenu;
    }

    private DrawerGroup getSellerMenu() {
        DrawerGroup sellerMenu = new DrawerGroup(context.getString(R.string.drawer_title_seller),
                R.drawable.icon_penjualan,
                TkpdState.DrawerPosition.SHOP,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                getTotalSellerNotif());
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_opportunity),
                0,
                TkpdState.DrawerPosition.SHOP_OPPORTUNITY_LIST,
                drawerCache.getBoolean(IS_SHOP_OPENED, false)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_new_order),
                TkpdState.DrawerPosition.SHOP_NEW_ORDER,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_confirm_shipping),
                TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_shipping_status),
                TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS,
                drawerCache.getBoolean(IS_SHOP_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS)));
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_list_selling),
                0,
                TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST,
                drawerCache.getBoolean(IS_SHOP_OPENED, false)));
        return sellerMenu;
    }

    private DrawerGroup getResoMenu() {
        DrawerGroup resoMenu = new DrawerGroup(context.getString(R.string.drawer_title_new_reso),
                R.drawable.ic_reso,
                TkpdState.DrawerPosition.RESOLUTION_CENTER,
                drawerCache.getBoolean(IS_RESO_OPENED, false),
                getTotalResoNotif());
        resoMenu.add(new DrawerItem(context.getString(R.string.drawer_title_new_reso_as_buyer),
                TkpdState.DrawerPosition.RESOLUTION_CENTER_BUYER,
                drawerCache.getBoolean(IS_RESO_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_BUYER, VAL_DEFAULT)));
        resoMenu.add(new DrawerItem(context.getString(R.string.drawer_title_new_reso_as_seller),
                TkpdState.DrawerPosition.RESOLUTION_CENTER_SELLER,
                drawerCache.getBoolean(IS_RESO_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, VAL_DEFAULT)));
        return resoMenu;
    }

    private DrawerGroup getBuyerMenu() {
        DrawerGroup buyerMenu = new DrawerGroup(context.getString(R.string.drawer_title_buyer),
                R.drawable.icon_pembelian,
                TkpdState.DrawerPosition.PEOPLE,
                drawerCache.getBoolean(IS_PEOPLE_OPENED, false),
                getTotalBuyerNotif());
        buyerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_payment_status),
                TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS,
                drawerCache.getBoolean(IS_PEOPLE_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM)));
        buyerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_order_status),
                TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS,
                drawerCache.getBoolean(IS_PEOPLE_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_ORDER_STATUS)));
        buyerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_confirm_delivery),
                TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING,
                drawerCache.getBoolean(IS_PEOPLE_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM)));
        buyerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_transaction_canceled),
                TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED,
                drawerCache.getBoolean(IS_PEOPLE_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_REORDER)));
        buyerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_purchase_list),
                TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST,
                drawerCache.getBoolean(IS_PEOPLE_OPENED, false)));
        return buyerMenu;
    }

    private DrawerGroup getInboxMenu() {
        DrawerGroup inboxMenu = new DrawerGroup(context.getString(R.string.drawer_title_inbox),
                R.drawable.icon_inbox,
                TkpdState.DrawerPosition.INBOX,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                getTotalInboxNotif());
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_message),
                TkpdState.DrawerPosition.INBOX_MESSAGE,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE)));
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_discussion),
                TkpdState.DrawerPosition.INBOX_TALK,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK)));
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_review),
                TkpdState.DrawerPosition.INBOX_REVIEW,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW)));
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_inbox_ticket),
                TkpdState.DrawerPosition.INBOX_TICKET,
                drawerCache.getBoolean(IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET)));

        if(SessionHandler.isUserHasShop(context)){
            inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_seller_info),
                    TkpdState.DrawerPosition.SELLER_INFO,
                    drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                    drawerCache.getInt(DrawerNotification.CACHE_INBOX_SELLER_INFO)));
        }
        return inboxMenu;
    }

    private int getTotalInboxNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET, 0);
    }

    private int getTotalBuyerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_DELIVERY_CONFIRM, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_REORDER, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_ORDER_STATUS, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_PURCHASE_PAYMENT_CONFIRM, 0);
    }

    private int getTotalSellerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, 0);
    }

    private int getTotalResoBuyerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_BUYER, 0);
    }

    private int getTotalResoNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_BUYER, 0)  +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, 0);
    }

    @Override
    public boolean isOpened() {
        return drawerLayout.isDrawerOpen(GravityCompat.START);
    }

    private boolean hasShop(DrawerProfile drawerProfile) {
        return !drawerProfile.getShopName().equals("");
    }

    @Override
    public void setFooterData(DrawerProfile profile) {
        if (hasShop(profile)) {
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
        } else {
            shopName.setText(com.tokopedia.core.R.string.title_create_shop);
            shopIcon.setVisibility(View.GONE);
            shopLabel.setVisibility(View.GONE);
            shopLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    globalCacheManager.delete(ProfileSourceFactory.KEY_PROFILE_DATA);
                    onGoToCreateShop();
                }
            });
        }
    }

    @Override
    public void setSelectedPosition(int id) {
        super.setSelectedPosition(id);
        adapter.setSelectedItem(id);
    }

    @Override
    public void initDrawer(Activity activity) {
        this.adapter = DrawerAdapter.createAdapter(activity, this, drawerCache);
        this.adapter.setData(createDrawerData());
        this.adapter.setHeader(new DrawerHeaderDataBinder(adapter, activity, this, drawerCache));
        recyclerView.setLayoutManager(new LinearLayoutManager(activity,
                LinearLayoutManager.VERTICAL, false));
        recyclerView.setAdapter(adapter);
        setExpand();
        closeDrawer();
    }

    public void setExpand() {
        checkExpand(DrawerAdapter.IS_INBOX_OPENED, TkpdState.DrawerPosition.INBOX);
        checkExpand(DrawerAdapter.IS_PEOPLE_OPENED, TkpdState.DrawerPosition.PEOPLE);
        checkExpand(DrawerAdapter.IS_SHOP_OPENED, TkpdState.DrawerPosition.SHOP);
        checkExpand(DrawerAdapter.IS_PRODUCT_OPENED, TkpdState.DrawerPosition.SELLER_PRODUCT_EXTEND);
        checkExpand(DrawerAdapter.IS_RESO_OPENED, TkpdState.DrawerPosition.RESOLUTION_CENTER);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClicked(DrawerItem item) {
        if (item.getId() == selectedPosition) {
            closeDrawer();
        } else {
            Intent intent;
            switch (item.getId()) {
                case TkpdState.DrawerPosition.INDEX_HOME:
                    intent = HomeRouter.getHomeActivity(context);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.CATEGORY_NAVIGATION:
                    UnifyTracking.eventCategoryDrawer();
                    context.startActivity(BrowseProductRouter.getCategoryNavigationIntent(context));
                    break;
                case TkpdState.DrawerPosition.WISHLIST:
                    Intent wishList = SimpleHomeRouter
                            .getSimpleHomeActivityIntent(context, SimpleHomeRouter.WISHLIST_FRAGMENT);

                    context.startActivity(wishList);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.WISHLIST);
                    break;
                case TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS:
                    context.startActivity(TransactionPurchaseRouter.createIntentConfirmPayment(context));
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.PAYMENT_CONFIRMATION);
                    break;
                case TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS:
                    context.startActivity(TransactionPurchaseRouter.createIntentTxStatus(context));
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.ORDER_STATUS);
                    break;
                case TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING:
                    context.startActivity(TransactionPurchaseRouter.createIntentConfirmShipping(context));
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.RECEIVE_CONFIRMATION);
                    break;
                case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED:
                    context.startActivity(TransactionPurchaseRouter.createIntentTxCanceled(context));
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.CANCELLED_ORDER);
                    break;
                case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST:
                    context.startActivity(TransactionPurchaseRouter.createIntentTxAll(context));
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.PURCHASE_LIST);
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
                    intent = SellerRouter.getActivitySellingTransactionOpportunity(context,"");
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.ADD_PRODUCT:
                    intent = new Intent(context, ProductAddActivity.class);
                    context.startActivity(intent);
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
                case TkpdState.DrawerPosition.DRAFT_PRODUCT:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        ((TkpdCoreRouter) context.getApplication()).goToDraftProductList(context);
                    }
                    break;
                case TkpdState.DrawerPosition.FEATURED_PRODUCT:
                case TkpdState.DrawerPosition.GOLD_MERCHANT:
                    Intent launchIntent = context.getPackageManager()
                            .getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);

                    if (launchIntent != null) {
                        context.startActivity(launchIntent);
                        UnifyTracking.eventClickGMSwitcher(AppEventTracking.EventLabel.OPEN_TOP_SELLER + AppEventTracking.EventLabel.OPEN_APP);
                    } else if (context.getApplication() instanceof TkpdCoreRouter) {
                        ((TkpdCoreRouter) context.getApplication()).goToCreateMerchantRedirect(context);
                        UnifyTracking.eventClickGMSwitcher(AppEventTracking.EventLabel.OPEN_GM + AppEventTracking.Category.SWITCHER);
                    }
                    break;
                case TkpdState.DrawerPosition.SELLER_TOP_ADS:
                    Intent topadsIntent = context.getPackageManager()
                            .getLaunchIntentForPackage(TOP_SELLER_APPLICATION_PACKAGE);

                    if (topadsIntent != null) {
                        context.startActivity(topadsIntent);
                        UnifyTracking.eventTopAdsSwitcher(AppEventTracking.EventLabel.OPEN_APP);
                    } else if (context.getApplication() instanceof TkpdCoreRouter) {
                        ((TkpdCoreRouter) context.getApplication()).goToCreateMerchantRedirect(context);
                        UnifyTracking.eventTopAdsSwitcher(AppEventTracking.Category.SWITCHER);
                    }
                    break;
                case TkpdState.DrawerPosition.SELLER_INFO:
                    UnifyTracking.eventClickMenuSellerInfo();
                    intent = new Intent(context, SellerInfoActivity.class);
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.RESOLUTION_CENTER:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        context.startActivity(((TkpdCoreRouter) context.getApplication())
                                .getResolutionCenterIntentBuyer(context));
                        sendGTMNavigationEvent(AppEventTracking.EventLabel.RESOLUTION_CENTER);

                    }
                    break;
                case TkpdState.DrawerPosition.RESOLUTION_CENTER_BUYER:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        context.startActivity(((TkpdCoreRouter) context.getApplication())
                                .getResolutionCenterIntentBuyer(context));
                        sendGTMNavigationEvent(AppEventTracking.EventLabel.RESOLUTION_CENTER);

                    }
                    break;
                case TkpdState.DrawerPosition.RESOLUTION_CENTER_SELLER:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        context.startActivity(((TkpdCoreRouter) context.getApplication())
                                .getResolutionCenterIntentSeller(context));
                        sendGTMNavigationEvent(AppEventTracking.EventLabel.RESOLUTION_CENTER);

                    }
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
        //TODO milhamj
//        context.startActivity(
//                PeopleInfoNoDrawerActivity.createInstance(context, sessionHandler.getLoginID())
//        );
        context.startActivity(
                PeopleInfoActivity.newInstance(context)
        );
        sendGTMNavigationEvent(AppEventTracking.EventLabel.PROFILE);

    }

    @Override
    public void onGoToTopPoints(String topPointsUrl) {
        if (topPointsUrl != null && !topPointsUrl.equals("")) {
            Bundle bundle = new Bundle();
            bundle.putString("url", URLGenerator.generateURLLucky(topPointsUrl, context));
            Intent intent = new Intent(context, LoyaltyDetail.class);
            intent.putExtras(bundle);
            context.startActivity(intent);
            sendGTMNavigationEvent(AppEventTracking.EventLabel.TOPPOINTS);
        }
    }

    @Override
    public void onGoToProfileCompletion() {
        Intent intent = new Intent(context, ProfileCompletionActivity.class);
        context.startActivity(intent);
    }

    @Override
    public void onWalletBalanceClicked(String redirectUrlBalance, String appLinkBalance) {
        WalletRouterUtil.navigateWallet(
                context.getApplication(),
                context,
                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                appLinkBalance,
                redirectUrlBalance,
                new Bundle()
        );
    }

    @Override
    public void onWalletActionButtonClicked(String redirectUrlActionButton, String appLinkActionButton) {
        WalletRouterUtil.navigateWallet(
                context.getApplication(),
                context,
                IWalletRouter.DEFAULT_WALLET_APPLINK_REQUEST_CODE,
                appLinkActionButton,
                redirectUrlActionButton,
                new Bundle()
        );
    }

    @Override
    public void onTokoPointActionClicked(String mainPageUrl, String title) {
        if (TextUtils.isEmpty(title))
            context.startActivity(TokoPointWebviewActivity.getIntent(context, mainPageUrl));
        else
            context.startActivity(TokoPointWebviewActivity.getIntentWithTitle(context, mainPageUrl, title));
    }

    private void onGoToCreateShop() {
        Intent intent = SellerRouter.getActivityShopCreateEdit(context);
        context.startActivity(intent);
        sendGTMNavigationEvent(AppEventTracking.EventLabel.SHOP_EN);
    }

    private void onGoToShop() {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(sessionHandler.getShopID(), ""));
        context.startActivity(intent);
        sendGTMNavigationEvent(AppEventTracking.EventLabel.SHOP_EN);
    }

    private void showAppShareButton(ArrayList<DrawerItem> data) {
        if (remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.MAINAPP_SHOW_APP_SHARE_BUTTON)) {
            if(remoteConfig.getBoolean(TkpdCache.RemoteConfigKey.APP_SHOW_REFERRAL_BUTTON)){
                data.add(new DrawerItem(remoteConfig.getString(TkpdCache.RemoteConfigKey.APP_REFERRAL_TITLE, context.getString(R.string.drawer_title_referral_appshare)),
                        R.drawable.share_ke_teman, TkpdState.DrawerPosition.APPSHARE,
                        true, true));
            }else{
                data.add(new DrawerItem(context.getString(R.string.drawer_title_appshare),
                        R.drawable.share_ke_teman, TkpdState.DrawerPosition.APPSHARE,
                        true, true));
            }
        }
    }
}
