package com.tokopedia.sellerapp.drawer;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.AlertDialog;
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
import com.tokopedia.core.base.domain.RequestParams;
import com.tokopedia.core.deposit.activity.DepositActivity;
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
import com.tokopedia.core.router.digitalmodule.IDigitalModuleRouter;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.shopinfo.models.shopmodel.ShopModel;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.gm.featured.view.activity.GMFeaturedProductActivity;
import com.tokopedia.gm.statistic.view.activity.GMStatisticDashboardActivity;
import com.tokopedia.gm.subscribe.view.activity.GmSubscribeHomeActivity;
import com.tokopedia.mitratoppers.MitraToppersRouter;
import com.tokopedia.profile.view.activity.TopProfileActivity;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.seller.SellerModuleRouter;
import com.tokopedia.seller.product.draft.view.activity.ProductDraftListActivity;
import com.tokopedia.seller.product.edit.view.activity.ProductAddActivity;
import com.tokopedia.seller.seller.info.view.activity.SellerInfoActivity;
import com.tokopedia.seller.shop.common.domain.interactor.GetShopInfoUseCase;
import com.tokopedia.seller.shopsettings.etalase.activity.EtalaseShopEditor;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.dashboard.view.activity.DashboardActivity;
import com.tokopedia.topads.dashboard.view.activity.TopAdsDashboardActivity;

import java.util.ArrayList;

import rx.Subscriber;

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

    private GetShopInfoUseCase getShopInfoUseCase;

    private boolean isGoldMerchant;

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

        if (activity.getApplicationContext() instanceof SellerModuleRouter) {
            SellerModuleRouter sellerModuleRouter = (SellerModuleRouter) activity.getApplicationContext();
            getShopInfoUseCase = sellerModuleRouter.getShopInfo();
        }

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

        data.add(getGoldMerchantMenu(false));
        data.add(getPaymentAndTopupMenu());
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

        isGoldMerchantAsync();

        return data;
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

    private int getTotalInboxNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_MESSAGE, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TALK, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_REVIEW, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_INBOX_TICKET, 0);
    }

    private int getTotalSellerNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_STATUS, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_SHIPPING_CONFIRMATION, 0) +
                drawerCache.getInt(DrawerNotification.CACHE_SELLING_NEW_ORDER, 0);
    }

    private int getTotalResoNotif() {
        return drawerCache.getInt(DrawerNotification.CACHE_INBOX_RESOLUTION_CENTER_SELLER, 0);
    }

    private DrawerGroup getGoldMerchantMenu(boolean isGoldMerchant) {
        DrawerGroup gmMenu = new DrawerGroup(context.getString(R.string.drawer_title_gold_merchant),
                R.drawable.ic_goldmerchant_drawer,
                TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE,
                drawerCache.getBoolean(DrawerAdapter.IS_GM_OPENED, false),
                0);

        String gmString = isGoldMerchant ?
                context.getString(R.string.extend_gold_merchant) :
                context.getString(R.string.upgrade_gold_merchant);

        gmMenu.add(new DrawerItem(gmString,
                TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND,
                drawerCache.getBoolean(DrawerAdapter.IS_GM_OPENED, false),
                0));
        gmMenu.add(new DrawerItem(context.getString(com.tokopedia.seller.R.string.featured_product_title),
                TkpdState.DrawerPosition.FEATURED_PRODUCT,
                true
        ));
        return gmMenu;
    }

    private void isGoldMerchantAsync() {
        getShopInfoUseCase.execute(RequestParams.EMPTY, new Subscriber<ShopModel>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(ShopModel shopModel) {
                DrawerGroup goldMerchantMenu = getGoldMerchantMenu(shopModel.info.isGoldMerchant());
                goldMerchantMenu.setExpanded(false);

                // update gold merchant
                isGoldMerchant = shopModel.info.isGoldMerchant();

                // find gold merchant index based on drawerposition
                int goldMerchantIndex = -1;
                for (int i = 0; i < adapter.getData().size(); i++) {
                    if (adapter.getData().get(i).getId() == TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE) {
                        goldMerchantIndex = i;
                    }
                }

                DrawerGroup drawerGroup = (DrawerGroup) adapter.getData().get(goldMerchantIndex);
                adapter.getData().removeAll(drawerGroup.getList());
                adapter.getData().remove(goldMerchantIndex);

                adapter.getData().add(goldMerchantIndex, goldMerchantMenu);

                if (drawerGroup.isExpanded()) {
                    adapter.getData().addAll(goldMerchantIndex + 1, goldMerchantMenu.getList());
                    goldMerchantMenu.setExpanded(true);
                }

                adapter.notifyDataSetChanged();
            }
        });
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

    @Override
    public void onItemClicked(DrawerItem item) {
        if (item.getId() == selectedPosition) {
            closeDrawer();
        } else {
            Intent intent;
            boolean isNeedToCloseActivity = true;
            switch (item.getId()) {
                case TkpdState.DrawerPosition.INDEX_HOME:
                    UnifyTracking.eventDrawerSellerHome();
                    context.startActivity(DashboardActivity.createInstance(context));
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
                    intent = SellerRouter.getActivitySellingTransactionOpportunity(context, "");
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
                case TkpdState.DrawerPosition.MANAGE_PAYMENT_AND_TOPUP:
                    context.startActivity(((IDigitalModuleRouter) context.getApplication())
                            .instanceIntentDigitalCategoryList());
                    UnifyTracking.eventClickPaymentAndTopupOnDrawer();
                    break;
                case TkpdState.DrawerPosition.MANAGE_TRANSACTION_DIGITAL:
                    context.startActivity(((IDigitalModuleRouter) context.getApplication())
                            .instanceIntentDigitalWeb(TkpdBaseURL.DIGITAL_WEBSITE_DOMAIN
                                    + TkpdBaseURL.DigitalWebsite.PATH_TRANSACTION_LIST));
                    UnifyTracking.eventClickDigitalTransactionListOnDrawer();
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
                case TkpdState.DrawerPosition.SELLER_MITRA_TOPPERS:
                    Intent mitraToppersIntent = ((MitraToppersRouter) context.getApplication())
                            .getMitraToppersActivityIntent(context);
                    context.startActivity(mitraToppersIntent);
                    break;
                case TkpdState.DrawerPosition.SELLER_TOP_ADS:
                    UnifyTracking.eventDrawerTopads();
                    intent = new Intent(context, TopAdsDashboardActivity.class);
                    context.startActivity(intent);
                    break;
                case TkpdState.DrawerPosition.FEATURED_PRODUCT:
                    if (isGoldMerchant) {
                        UnifyTracking.eventClickMenuFeaturedProduct();
                        intent = new Intent(context, GMFeaturedProductActivity.class);
                        context.startActivity(intent);
                    } else {
                        showDialogActionGoToGMSubscribe();
                        isNeedToCloseActivity = false;
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
                                .getResolutionCenterIntentSeller(context));
                        sendGTMNavigationEvent(AppEventTracking.EventLabel.RESOLUTION_CENTER);
                    }
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

    private void showDialogActionGoToGMSubscribe() {
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(context);
        alertDialog.setTitle(R.string.featured_product_title);
        alertDialog.setMessage(R.string.featured_product_desc_should_gold_merchant);
        alertDialog.setPositiveButton(R.string.label_subscribe, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (context.getApplication() instanceof SellerModuleRouter) {
                    ((SellerModuleRouter) context.getApplication()).goToGMSubscribe(context);
                }
            }
        });
        alertDialog.setNegativeButton(R.string.title_cancel, null);
        alertDialog.show();
    }

    @Override
    public void onGoToDeposit() {
        Intent intent = new Intent(context, DepositActivity.class);
        context.startActivity(intent);
        sendGTMNavigationEvent(AppEventTracking.EventLabel.DEPOSIT);
    }

    @Override
    public void onGoToProfile() {
        context.startActivity(TopProfileActivity.newInstance(context, SessionHandler.getLoginID(context)));
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
