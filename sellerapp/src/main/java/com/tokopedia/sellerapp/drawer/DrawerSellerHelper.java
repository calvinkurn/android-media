package com.tokopedia.sellerapp.drawer;

import android.app.Activity;
import android.content.Intent;
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
import com.tokopedia.core.network.constants.TkpdBaseURL;
import com.tokopedia.core.people.activity.PeopleInfoDrawerActivity;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.seller.gmsubscribe.view.activity.GmSubscribeHomeActivity;
import com.tokopedia.seller.goldmerchant.statistic.view.activity.GMStatisticDashboardActivity;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.shopsettings.etalase.activity.EtalaseShopEditor;
import com.tokopedia.seller.topads.dashboard.view.activity.TopAdsDashboardActivity;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

import java.util.ArrayList;

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

        data.add(new DrawerItem(context.getString(R.string.drawer_title_home),
                R.drawable.icon_home,
                TkpdState.DrawerPosition.SELLER_INDEX_HOME,
                true));

        data.add(getSellerMenu());
        data.add(getInboxMenu());
        data.add(getProductMenu());

        data.add(getGoldMerchantMenu());
        data.add(getPaymentAndTopupMenu());
        data.add(new DrawerItem(context.getString(R.string.drawer_title_statistic),
                R.drawable.statistik_icon,
                TkpdState.DrawerPosition.SELLER_GM_STAT,
                true));
        data.add(new DrawerItem(context.getString(R.string.drawer_title_top_ads),
                R.drawable.ic_top_ads,
                TkpdState.DrawerPosition.SELLER_TOP_ADS,
                true));

        data.add(new DrawerItem(context.getString(R.string.drawer_title_setting),
                R.drawable.icon_setting,
                TkpdState.DrawerPosition.SETTINGS,
                true));
        data.add(new DrawerItem(context.getString(R.string.title_activity_contact_us),
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
        shopLayout.setVisibility(View.VISIBLE);
        footerShadow.setVisibility(View.VISIBLE);
        return data;
    }

    private DrawerItem getSellerMenu() {
        DrawerGroup sellerMenu = new DrawerGroup(context.getString(R.string.drawer_title_seller),
                R.drawable.icon_penjualan,
                TkpdState.DrawerPosition.SHOP,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false),
                getTotalSellerNotif());
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
        sellerMenu.add(new DrawerItem(context.getString(R.string.drawer_title_opportunity),
                TkpdState.DrawerPosition.SHOP_OPPORTUNITY_LIST,
                drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false)));
        sellerMenu.add(new DrawerSeparator(drawerCache.getBoolean(DrawerAdapter.IS_SHOP_OPENED, false)));

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
        inboxMenu.add(new DrawerItem(context.getString(R.string.drawer_title_resolution_center),
                TkpdState.DrawerPosition.RESOLUTION_CENTER,
                drawerCache.getBoolean(DrawerAdapter.IS_INBOX_OPENED, false),
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER)));
        return inboxMenu;
    }

    private DrawerItem getPaymentAndTopupMenu() {
        DrawerGroup sellerMenu = new DrawerGroup(context.getResources().getString(R.string.digital_product),
                R.drawable.payment_and_topup,
                TkpdState.DrawerPosition.SELLER_PRODUCT_DIGITAL_EXTEND,
                drawerCache.getBoolean(DrawerAdapter.IS_PRODUCT_DIGITAL_OPENED, false),
                0);

        sellerMenu.add(new DrawerItem(context.getResources().getString(R.string.payment_and_topup),
                TkpdState.DrawerPosition.MANAGE_PAYMENT_AND_TOPUP,
                true));
        sellerMenu.add(new DrawerItem(context.getResources().getString(R.string.digital_transaction_list),
                TkpdState.DrawerPosition.MANAGE_TRANSACTION_DIGITAL,
                true));

        return sellerMenu;
    }

    private DrawerItem getProductMenu() {
        DrawerGroup sellerMenu = new DrawerGroup(context.getString(R.string.drawer_title_product),
                R.drawable.ic_manage_produk,
                TkpdState.DrawerPosition.SELLER_PRODUCT_EXTEND,
                drawerCache.getBoolean(DrawerAdapter.IS_PRODUCT_OPENED, false),
                0);
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
        DrawerGroup gmMenu = new DrawerGroup(context.getString(R.string.drawer_title_gold_merchant),
                R.drawable.ic_goldmerchant_drawer,
                TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE,
                drawerCache.getBoolean(DrawerAdapter.IS_GM_OPENED, false),
                0);
        String gmString = SessionHandler.isGoldMerchant(context) ?
                context.getString(R.string.extend_gold_merchant) :
                context.getString(R.string.upgrade_gold_merchant);

        gmMenu.add(new DrawerItem(gmString,
                TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND,
                drawerCache.getBoolean(DrawerAdapter.IS_GM_OPENED, false),
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
        checkExpand(DrawerAdapter.IS_INBOX_OPENED, TkpdState.DrawerPosition.INBOX);
        checkExpand(DrawerAdapter.IS_PEOPLE_OPENED, TkpdState.DrawerPosition.PEOPLE);
        checkExpand(DrawerAdapter.IS_SHOP_OPENED, TkpdState.DrawerPosition.SHOP);
        checkExpand(DrawerAdapter.IS_PRODUCT_DIGITAL_OPENED, TkpdState.DrawerPosition.SELLER_PRODUCT_DIGITAL_EXTEND);
        checkExpand(DrawerAdapter.IS_PRODUCT_OPENED, TkpdState.DrawerPosition.SELLER_PRODUCT_EXTEND);
        checkExpand(DrawerAdapter.IS_GM_OPENED, TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE);
        adapter.notifyDataSetChanged();
    }

    private void checkExpand(String key, int idPosition) {
        if (drawerCache.getBoolean(key, false)) {
            DrawerGroup group = findGroup(idPosition);
            if (group != null)
                adapter.getData().addAll(group.getPosition() + 1, group.getList());
        }
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
                    UnifyTracking.eventDrawerSellerHome();
                    context.startActivity(SellerHomeActivity.getCallingIntent(context));
                    break;
                case TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND:
                    UnifyTracking.eventClickGoldMerchantViaDrawer();
                    context.startActivity(GmSubscribeHomeActivity.getCallingIntent(context));
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
                    intent = SellerRouter.getActivitySellingTransactionOpportunity(context);
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.MANAGE_PRODUCT:
                    if (context.getApplication() instanceof TkpdCoreRouter) {
                        ((TkpdCoreRouter) context.getApplication()).goToManageProduct(context);
                    }
                    break;
                case TkpdState.DrawerPosition.MANAGE_PAYMENT_AND_TOPUP:
                    UnifyTracking.eventClickPaymentAndTopupOnDrawer();
                    context.startActivity(((IDigitalModuleRouter) context.getApplication())
                            .instanceIntentDigitalCategoryList());
                    break;
                case TkpdState.DrawerPosition.MANAGE_TRANSACTION_DIGITAL:
                    UnifyTracking.eventClickDigitalTransactionListOnDrawer();
                    context.startActivity(((IDigitalModuleRouter) context.getApplication())
                            .instanceIntentDigitalWeb(TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_TRANSACTION_LIST));
                    break;
                case TkpdState.DrawerPosition.DRAFT_PRODUCT:
                    UnifyTracking.eventDrawerClick(AppEventTracking.EventLabel.DRAFT_PRODUCT);
                    context.startActivity(new Intent(context, ProductDraftListActivity.class));
                    break;
                case TkpdState.DrawerPosition.MANAGE_ETALASE:
                    startIntent(context, EtalaseShopEditor.class);
                    sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISPLAY);
                    break;
                case TkpdState.DrawerPosition.SELLER_GM_STAT:
                    intent = new Intent(context, GMStatisticDashboardActivity.class);
                    context.startActivity(intent);
                    UnifyTracking.eventClickGMStat();
                    break;
                case TkpdState.DrawerPosition.SELLER_TOP_ADS:
                    UnifyTracking.eventDrawerTopads();
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
                PeopleInfoDrawerActivity.createInstance(context, SessionHandler.getLoginID(context))
        );
        sendGTMNavigationEvent(AppEventTracking.EventLabel.PROFILE);

    }

    @Override
    public void onGoToProfileCompletion() {
        Intent intent = new Intent(context, ProfileCompletionActivity.class);
        context.startActivity(intent);
    }

    private void onGoToShop() {
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(ShopInfoActivity.createBundle(sessionHandler.getShopID(), ""));
        context.startActivity(intent);
        sendGTMNavigationEvent(AppEventTracking.EventLabel.SHOP_EN);
    }


}
