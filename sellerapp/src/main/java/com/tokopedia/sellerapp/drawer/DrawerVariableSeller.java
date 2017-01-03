package com.tokopedia.sellerapp.drawer;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.DeveloperOptions;
import com.tokopedia.core.EtalaseShopEditor;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.drawer.DrawerVariable;
import com.tokopedia.core.drawer.interactor.NetworkInteractor;
import com.tokopedia.core.drawer.interactor.NetworkInteractorImpl;
import com.tokopedia.core.drawer.model.DrawerHeader;
import com.tokopedia.core.drawer.model.DrawerItem;
import com.tokopedia.core.drawer.model.DrawerItemList;
import com.tokopedia.core.drawer.model.DrawerSeparator;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.drawer.var.UserType;
import com.tokopedia.core.inboxmessage.activity.InboxMessageActivity;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.myproduct.ManageProduct;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.inbox.inboxtalk.activity.InboxTalkActivity;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.var.ToolbarVariable;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.gmsubscribe.GMSubscribeActivity;
import com.tokopedia.sellerapp.home.view.SellerHomeActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 5/08/15.
 */

public class DrawerVariableSeller extends DrawerVariable {
    private static final String IS_INBOX_OPENED = "IS_INBOX_OPENED";
    private static final String IS_SHOP_OPENED = "IS_SHOP_OPENED";
    private static final String IS_GM_SUBSCRIBE_OPENED = "IS_GM_SUBSCRIBE_OPENED";

    private NetworkInteractor networkInteractor;
    public AppCompatActivity context;
    private ViewHolder holder;
    private Model model;
    private DrawerSellerAdapter adapter;
    private LinearLayoutManager layoutManager;

    private SessionHandler Session;
    private LocalCacheHandler Cache;
    private LocalCacheHandler CacheNotif;
    //   private FacadeNotification facadeNotif;

    private int drawerPosition = 0;
    private DefaultItemAnimator animator;
    private ToolbarVariable toolbar;
    private boolean hasUpdated = false;

    @Override
    public boolean hasUpdated() {
        return hasUpdated;
    }

    @Override
    public void setHasUpdated(boolean hasUpdated) {
        this.hasUpdated = hasUpdated;
    }

    public class Model {
        private DrawerHeader header;
        private DrawerItem sellerHome;
        private DrawerItemList shopMenu;
        private DrawerItemList inboxMenu;
        private DrawerItemList gmSubscribeMenu;
        private List<RecyclerViewItem> data;

        public Model() {
            data = new ArrayList<>();
            header = new DrawerHeader(context);
            sellerHome = new DrawerItem("Beranda", 0, R.drawable.icon_home, TkpdState.DrawerPosition.SELLER_INDEX_HOME, false);
            shopMenu = new DrawerItemList("Penjualan", 0, R.drawable.icon_penjualan, TkpdState.DrawerPosition.SHOP, true);
            inboxMenu = new DrawerItemList("Kotak Masuk", 0, R.drawable.icon_inbox, TkpdState.DrawerPosition.INBOX, true);
            gmSubscribeMenu = new DrawerItemList("Gold Merchant", 0, R.drawable.ic_goldmerchant_drawer, TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE, true);
        }

    }

    private class ViewHolder {
        DrawerLayout drawerLayout;
        RecyclerView recyclerView;
        TextView shopName;
        ImageView shopIcon;
        FrameLayout shopLayout;
        TextView footerShadow;
        TextView shopLabel;
    }


    public DrawerVariableSeller(AppCompatActivity context) {
        super(context);
        this.context = context;
    }

    @Override
    public void createDrawer() {
        initVar();
        initView();
        initListener();
        prepareView();
        updateData();
        setDrawer();
    }

    private void initVar() {
        networkInteractor = new NetworkInteractorImpl();
        Session = new SessionHandler(context);
        Cache = new LocalCacheHandler(context, TkpdState.CacheName.CACHE_USER);
        CacheNotif = new LocalCacheHandler(context, "NOTIFICATION_DATA");
        holder = new ViewHolder();
        model = new Model();
        model.shopMenu.list = new ArrayList<>();
        model.inboxMenu.list = new ArrayList<>();
        model.gmSubscribeMenu.list = new ArrayList<>();
        adapter = new DrawerSellerAdapter(context, model.data);
        layoutManager = new LinearLayoutManager(context);
        animator = new DefaultItemAnimator();
        initFacade();
        createShopMenu();
        createInboxMenu();
        createGoldMerchantMenu();
    }

    private void createGoldMerchantMenu() {
        String gmString = isGoldMerchant() ? context.getString(R.string.extend_gold_merchant) : context.getString(R.string.upgrade_gold_merchant);
        model.gmSubscribeMenu.list.add(new DrawerItem(gmString, 0, 0, TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND, false));
    }

    private boolean isGoldMerchant() {
        return Session.isGoldMerchant(context);
    }

    private void initView() {
        holder.drawerLayout = (DrawerLayout) context.findViewById(R.id.drawer_layout_nav);
        holder.recyclerView = (RecyclerView) context.findViewById(R.id.left_drawer);
        holder.shopName = (TextView) context.findViewById(R.id.label);
        holder.shopLabel = (TextView) context.findViewById(R.id.sublabel);
        holder.shopIcon = (ImageView) context.findViewById(R.id.icon);
        holder.shopLayout = (FrameLayout) context.findViewById(R.id.drawer_shop);
        holder.footerShadow = (TextView) context.findViewById(R.id.drawer_footer_shadow);
    }


    private void initFacade() {

    }

    private void prepareView() {
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        holder.recyclerView.setLayoutManager(layoutManager);
        animator.setAddDuration(100);
        holder.recyclerView.setItemAnimator(animator);
        closeDrawer();
        setEnabled(false);
    }

    @Override
    public void setDrawerPosition(int drawerPosition) {
        this.drawerPosition = drawerPosition;
        adapter.setItemSelected(drawerPosition);
        adapter.notifyDataSetChanged();
    }

    private void initListener() {
        adapter.setGroupClickedListener(onGroupClickedListener());
        adapter.setChildClickedListener(onChildClickedListener());
        adapter.setDrawerListener(onDrawer());
        toolbar.setOnDrawerToggleClick(onToggleClickedListener());
        holder.shopLayout.setOnClickListener(onShopClicked());
        holder.drawerLayout.setDrawerListener(onDrawerListener());
    }

    private DrawerSellerAdapter.DrawerListener onDrawer() {
        return new DrawerSellerAdapter.DrawerListener() {
            @Override
            public void OnClosed() {
                closeDrawer();
            }
        };
    }


    private DrawerLayout.DrawerListener onDrawerListener() {
        return new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                InputMethodManager imm = (InputMethodManager) context
                        .getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(drawerView.getWindowToken(), 0);
            }

            @Override
            public void onDrawerOpened(View drawerView) {

            }

            @Override
            public void onDrawerClosed(View drawerView) {

            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        };
    }

    private View.OnClickListener onShopClicked() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Session.getShopID().equals("0") || Session.getShopID().equals("")) {
                    Intent intent = SellerRouter.getAcitivityShopCreateEdit(context);
                    intent.putExtra(SellerRouter.ShopSettingConstant.FRAGMENT_TO_SHOW,
                            SellerRouter.ShopSettingConstant.CREATE_SHOP_FRAGMENT_TAG);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ShopInfoActivity.class);
                    intent.putExtras(ShopInfoActivity.createBundle(Session.getShopID(), "", model.header.shopName, model.header.shopIcon, 0));
                    context.startActivity(intent);
                }
                closeDrawer();
            }
        };
    }

    private DrawerSellerAdapter.GroupClickedListener onGroupClickedListener() {
        return new DrawerSellerAdapter.GroupClickedListener() {
            @Override
            public void OnExpanded(int position) {
                expandGroup(position);

            }

            @Override
            public void OnCollapsed(int position) {
                collapseGroup(position);
            }


        };
    }

    private void collapseGroup(int position) {
        if (position != 0) {
            DrawerItemList group = ((DrawerItemList) model.data.get(position));
            ((DrawerItemList) model.data.get(position)).isExpanded = false;
            for (int i = position + 1; i <= position + group.list.size(); i++) {
                model.data.remove(position + 1);
            }
            setExpandCache(group, false);
            adapter.notifyDataSetChanged();
            holder.recyclerView.scrollToPosition(position);
            CommonUtils.dumper("DrawerTag : Removed " + group.list.size() + " on " + position);
        }
    }

    private void expandGroup(int position) {
        if (position != 0) {
            DrawerItemList group = ((DrawerItemList) model.data.get(position));
            ((DrawerItemList) model.data.get(position)).isExpanded = true;
            model.data.addAll(position + 1, group.list);
            setExpandCache(group, true);
            adapter.notifyDataSetChanged();
            holder.recyclerView.scrollToPosition(position + group.list.size());
            CommonUtils.dumper("DrawerTag : Added " + group.list.size() + " on " + position);
        }
    }

    private void setExpandCache(DrawerItemList group, boolean isExpand) {
        switch (group.id) {
            case TkpdState.DrawerPosition.INBOX:
                Cache.putBoolean(IS_INBOX_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.SHOP:
                Cache.putBoolean(IS_SHOP_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE:
                Cache.putBoolean(IS_GM_SUBSCRIBE_OPENED, isExpand);
                break;
            default:
                break;
        }
        Cache.applyEditor();
    }

    private DrawerSellerAdapter.ChildClickedListener onChildClickedListener() {
        return new DrawerSellerAdapter.ChildClickedListener() {
            @Override
            public void OnClicked(int position) {
                CommonUtils.dumper("DRAWERTAG : START ACTION AT :" + position + " shopId : " + ((DrawerItem) model.data.get(position)).id + "Drawer Position : " + drawerPosition);
                if (((DrawerItem) model.data.get(position)).id == drawerPosition) {
                    closeDrawer();
                } else {
                    startAction(position);
                }

            }
        };
    }

    private void startAction(int position) {
        Boolean isFinish = true;
        Intent intent;
        switch (((DrawerItem) model.data.get(position)).id) {
            case TkpdState.DrawerPosition.SELLER_INDEX_HOME:
                context.startActivity(new Intent(context, SellerHomeActivity.class));
                break;
            case TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE_EXTEND:
                context.startActivity(new Intent(context, GMSubscribeActivity.class));
                break;
            case TkpdState.DrawerPosition.LOGIN:
            case TkpdState.DrawerPosition.REGISTER:
                intent = SessionRouter.getLoginActivityIntent(context);
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, ((DrawerItem) model.data.get(position)).id);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                context.startActivity(intent);
//                context.finish();
                break;
            case TkpdState.DrawerPosition.SHOP_NEW_ORDER:
                goToShopNewOrder();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.NEW_ORDER);
                break;
            case TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING:
                goToShopConfirmShipping();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_CONFIRMATION);
                break;
            case TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS:
                goToShopShippingStatus();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.DELIVERY_STATUS);
                break;
            case TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST:
                goToShopTransactionList();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SALES_LIST);
                break;
            case TkpdState.DrawerPosition.MANAGE_PRODUCT:
                startIntent(ManageProduct.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_LIST);
                break;
            case TkpdState.DrawerPosition.MANAGE_ETALASE:
                startIntent(EtalaseShopEditor.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISPLAY);
                break;
            case TkpdState.DrawerPosition.INBOX_MESSAGE:
                startIntent(InboxMessageActivity.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.MESSAGE);
                break;
            case TkpdState.DrawerPosition.INBOX_TALK:
                startIntent(InboxTalkActivity.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISCUSSION);
                break;
            case TkpdState.DrawerPosition.INBOX_REVIEW:
                startIntent(InboxReputationActivity.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.REVIEW);
                break;
            case TkpdState.DrawerPosition.INBOX_TICKET:
                intent = InboxRouter.getInboxTicketActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.HELP);
                break;
            case TkpdState.DrawerPosition.RESOLUTION_CENTER:
                context.startActivity(InboxRouter.getInboxResCenterActivityIntent(context));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.RESOLUTION_CENTER);
                break;
            case TkpdState.DrawerPosition.DEVELOPER_OPTIONS:
                startIntent(DeveloperOptions.class);
                break;
            case TkpdState.DrawerPosition.SETTINGS:
                context.startActivity(new Intent(context, ManageGeneral.class));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SETTING);
                break;
            case TkpdState.DrawerPosition.CONTACT_US:
                intent = InboxRouter.getContactUsActivityIntent(context);
                if (TrackingUtils.getBoolean(AppEventTracking.GTM.CREATE_TICKET)) {
                    intent.putExtra("link", "https://tokopedia.com/contact-us-android");
                }
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.LOGOUT:
                isFinish = false;
                SessionHandler session = new SessionHandler(context);
                session.Logout(context);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SIGN_OUT);
                break;
            default:
                break;
        }
        if (isFinish && drawerPosition != TkpdState.DrawerPosition.INDEX_HOME) {
            context.finish();
        }
//        updateData();
        closeDrawer();
    }

    private void goToShopTransactionList() {
        Intent intent = SellerRouter.getActivitySellingTransaction(context);
        intent.putExtra("tab", 4);
        context.startActivity(intent);
    }

    private void goToShopShippingStatus() {
        Intent intent = SellerRouter.getActivitySellingTransaction(context);
        intent.putExtra("tab", 3);
        context.startActivity(intent);
    }

    private void goToShopConfirmShipping() {
        Intent intent = SellerRouter.getActivitySellingTransaction(context);
        intent.putExtra("tab", 2);
        context.startActivity(intent);
    }

    private void goToShopNewOrder() {
        goToShopNewOrder(context);
    }

    public static void goToShopNewOrder(Context context) {
        Intent intent = SellerRouter.getActivitySellingTransaction(context);
        Bundle bundle = new Bundle();
        bundle.putInt("tab", 1);
        bundle.putString("user_id", SessionHandler.getLoginID(context));
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void startIntent(Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    public static void startIntent(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }


    private void setDrawer() {
        holder.recyclerView.setAdapter(adapter);
    }


    private boolean isLogin() {
        return Session.getLoginID() != null && Session.isV4Login();
    }

    private void clearData() {
        model.data.clear();
    }

    private void setExpand() {
        if (Cache.getBoolean(IS_INBOX_OPENED, false)) {
            expandGroup(getDrawerListPosition(TkpdState.DrawerPosition.INBOX));
        }
        if (Cache.getBoolean(IS_SHOP_OPENED, false)) {
            expandGroup(getDrawerListPosition(TkpdState.DrawerPosition.SHOP));
        }
        if (Cache.getBoolean(IS_GM_SUBSCRIBE_OPENED, false)){
            expandGroup(getDrawerListPosition(TkpdState.DrawerPosition.SELLER_GM_SUBSCRIBE));
        }
    }

    private void setSelected() {
        adapter.setItemSelected(drawerPosition);
    }

    @Override
    public void updateData() {
        clearData();

        if (isLogin()) {
            setCache();
            Long curr_time = System.currentTimeMillis() / 1000;
            Long time = Long.parseLong(Cache.getString("timestamp", "0"));
            Long interval = curr_time - time;

            if (interval > 18000 || model.header.userName.equals("") || model.header.shopIcon.equals("")) {
                getUserInfo();
            }

            getDeposit();

            if (interval > 9000 || model.header.Loyalty.equals("")) {
                getLoyalty();
            }

            setSelected();
            addMenuToDrawer();
            setExpand();
            holder.shopLayout.setVisibility(View.VISIBLE);
            holder.footerShadow.setVisibility(View.VISIBLE);
            setShop();
        } else {
            createDataGuest();
            holder.shopLayout.setVisibility(View.GONE);
            holder.footerShadow.setVisibility(View.GONE);
        }

        getNotification();
        adapter.notifyDataSetChanged();
        holder.recyclerView.smoothScrollToPosition(0);
    }

    private void createDataGuest() {
        clearData();
        CommonUtils.dumper("DrawerTag : CreateDataGuest");

        model.data.add(new DrawerHeader());
        model.data.add(new DrawerItem("Beranda", 0, 0, TkpdState.DrawerPosition.INDEX_HOME, true));
        model.data.add(new DrawerSeparator());
        model.data.add(new DrawerItem("Masuk", 0, 0, TkpdState.DrawerPosition.LOGIN, true));
        model.data.add(new DrawerSeparator());
        model.data.add(new DrawerItem("Daftar", 0, 0, TkpdState.DrawerPosition.REGISTER, true));
        model.data.add(new DrawerSeparator());
        if (TrackingUtils.getBoolean(AppEventTracking.GTM.REPORT)) {
            model.data.add(new DrawerItem("Laporkan", 0, 0, TkpdState.DrawerPosition.REPORT, true));
        }
        if (BuildConfig.DEBUG) {
            model.data.add(new DrawerItem("Developer Options", 0, 0, TkpdState.DrawerPosition.DEVELOPER_OPTIONS, true));
        }
        adapter.notifyDataSetChanged();


    }

    private void setCache() {
        setCacheHeader();
        setCacheShopMenu();
        setCacheInboxMenu();
        setCacheGMSubscribeMenu();
        setCacheFooter();
        adapter.notifyDataSetChanged();
    }

    private void setCacheGMSubscribeMenu() {
        model.gmSubscribeMenu.isExpanded = CacheNotif.getBoolean(IS_GM_SUBSCRIBE_OPENED, false);
    }

    private void setCacheFooter() {
        if (!Cache.getString("user_shop", "").equals("")) {
            holder.shopLabel.setVisibility(View.VISIBLE);
            holder.shopName.setVisibility(View.VISIBLE);
            holder.shopName.setText(Cache.getString("user_shop", ""));
            holder.shopIcon.setVisibility(View.VISIBLE);
            ImageHandler.loadImage2(holder.shopIcon,
                    Cache.getString("shop_pic_uri", ""), R.drawable.ic_default_shop_ava);
        }
    }

    private void setCacheHeader() {
        model.header.userIcon = Cache.getString("user_pic_uri", "");
        model.header.deposit = Cache.getString("deposit", "");
        model.header.userType = UserType.TYPE_PEOPLE;
        model.header.userName = Cache.getString("user_name", "");
        model.header.shopName = Cache.getString("user_shop", "");
        model.header.shopCover = Cache.getString("shop_cover_uri", "");
        model.header.shopIcon = Cache.getString("shop_pic_uri", "");
        model.header.Loyalty = Cache.getString("loyalty", "");
        model.header.LoyaltyUrl = Cache.getString("loyalty_url", "");
    }

    private void setCacheShopMenu() {
        model.shopMenu.isExpanded = CacheNotif.getBoolean("IS_SHOP_OPENED", false);
        model.shopMenu.list.get(0).notif = CacheNotif.getInt("new_order", 0);
        model.shopMenu.list.get(1).notif = CacheNotif.getInt("shipping_confirm", 0);
        model.shopMenu.list.get(2).notif = CacheNotif.getInt("shipping_status", 0);

    }

    private void setCacheInboxMenu() {
        model.inboxMenu.isExpanded = CacheNotif.getBoolean("IS_INBOX_OPENED", false);
        model.inboxMenu.list.get(0).notif = CacheNotif.getInt("message", 0);
        model.inboxMenu.list.get(1).notif = CacheNotif.getInt("talk", 0);
        model.inboxMenu.list.get(2).notif = CacheNotif.getInt("reputation", 0);
        model.inboxMenu.list.get(3).notif = CacheNotif.getInt("ticket", 0);
        model.inboxMenu.list.get(4).notif = CacheNotif.getInt("resolution", 0);

    }

    private void createShopMenu() {
        model.shopMenu.list.add(new DrawerItem("Order baru", 0, 0, TkpdState.DrawerPosition.SHOP_NEW_ORDER, false));
        model.shopMenu.list.add(new DrawerItem("Konfirmasi pengiriman", 0, 0, TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING, false));
        model.shopMenu.list.add(new DrawerItem("Status pengiriman", 0, 0, TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS, false));
        model.shopMenu.list.add(new DrawerItem("Daftar penjualan", 0, 0, TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST, false));
        model.shopMenu.list.add(new DrawerSeparator());
        model.shopMenu.list.add(new DrawerItem("Daftar Produk", 0, 0, TkpdState.DrawerPosition.MANAGE_PRODUCT, true));
        model.shopMenu.list.add(new DrawerItem("Etalase Toko", 0, 0, TkpdState.DrawerPosition.MANAGE_ETALASE, true));
    }

    private void createInboxMenu() {
        model.inboxMenu.list.add(new DrawerItem("Pesan", 0, 0, TkpdState.DrawerPosition.INBOX_MESSAGE, false));
        model.inboxMenu.list.add(new DrawerItem("Diskusi produk", 0, 0, TkpdState.DrawerPosition.INBOX_TALK, false));
        model.inboxMenu.list.add(new DrawerItem("Ulasan", 0, 0, TkpdState.DrawerPosition.INBOX_REVIEW, false));
        model.inboxMenu.list.add(new DrawerItem("Layanan pengguna", 0, 0, TkpdState.DrawerPosition.INBOX_TICKET, false));
        model.inboxMenu.list.add(new DrawerItem("Pusat resolusi", 0, 0, TkpdState.DrawerPosition.RESOLUTION_CENTER, false));
    }

    private void addMenuToDrawer() {
        if (Session.getShopID().isEmpty() || Session.getShopID().equals("0")) {
            // Means it is a buyer so inflate seller navigation menu
            model.data.add(model.header);
            model.data.add(new DrawerItem("Beranda", 0, R.drawable.icon_home, TkpdState.DrawerPosition.INDEX_HOME,
                    false));
            model.data.add(new DrawerItem("Wishlist", 0, R.drawable.icon_wishlist, TkpdState.DrawerPosition.WISHLIST,
                    false));
            model.data.add(model.inboxMenu);
            model.data.add(new DrawerItem("Pengaturan", 0, R.drawable.icon_setting, TkpdState.DrawerPosition.SETTINGS,
                    false));
            model.data.add(new DrawerItem("Keluar", 0, R.drawable.ic_menu_logout, TkpdState.DrawerPosition.LOGOUT,
                    false));
        } else {
            model.data.add(model.header);
            model.data.add(model.sellerHome);
            model.data.add(model.gmSubscribeMenu);
            model.data.add(model.inboxMenu);
            model.data.add(model.shopMenu);
            model.data.add(new DrawerItem("Pengaturan", 0, R.drawable.icon_setting, TkpdState.DrawerPosition.SETTINGS,
                    false));
            if (!TrackingUtils.getBoolean(AppEventTracking.GTM.CONTACT_US)) {
                model.data.add(new DrawerItem("Hubungi Kami", 0, R.drawable.ic_contact_us, TkpdState.DrawerPosition.CONTACT_US, false));
            }
            model.data.add(new DrawerItem("Keluar", 0, R.drawable.ic_menu_logout, TkpdState.DrawerPosition.LOGOUT,
                    false));
        }
//                if (BuildConfig.DEBUG & MainApplication.isDebug()) {
//            model.data.add(new DrawerItem("Developer Options", 0, android.R.drawable.stat_sys_warning,
//                    TkpdState.DrawerPosition.DEVELOPER_OPTIONS, false));
//        }
    }

    private void getLoyalty() {
        networkInteractor.getLoyalty(context, new NetworkInteractor.LoyaltyListener() {
            @Override
            public void onSuccess(LoyaltyItem data) {
                model.header.LoyaltyUrl = URLGenerator.generateURLLucky(data.getUri(), context);
                model.header.Loyalty = data.getLoyaltyPoint().getAmount();
                Cache.putString("loyalty", model.header.Loyalty);
                Cache.putString("loyalty_url", model.header.LoyaltyUrl);
                Cache.applyEditor();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });


    }

    private void getUserInfo() {
        networkInteractor.getProfileInfo(context, new NetworkInteractor.ProfileInfoListener() {
            @Override
            public void onSuccess(DrawerHeader profile) {
                if (profile.shopIcon == null || profile.shopIcon.equals("")) {
                    Uri uri = Uri.parse("android.resource://" + BuildConfig.APPLICATION_ID
                            + "/drawable/ic_default_shop_ava");
                    profile.shopIcon = uri.toString();
                }
                model.header.userIcon = profile.userIcon;
                model.header.deposit = profile.getDeposit(context);
                model.header.userType = UserType.TYPE_PEOPLE;
                model.header.userName = profile.userName;
                model.header.shopName = profile.shopName;
                model.header.shopCover = profile.shopCover;
                model.header.shopIcon = profile.shopIcon;
                model.header.timestamp = profile.timestamp;
                SessionHandler.setUserAvatarUri(context, profile.userIcon);
                model.header.setDataToCache(context);
                adapter.notifyDataSetChanged();
                setShop();
            }

            @Override
            public void onError(String message) {
                Log.d("Eror", "WOI ERROR WOI!!! " + message);
            }
        });
    }

    @Override
    public void getDeposit() {
        networkInteractor.getDeposit(context, new NetworkInteractor.DepositListener() {
            @Override
            public void onSuccess(String deposit) {
                model.header.deposit = deposit;
                Cache.putString("deposit", deposit);
                Cache.applyEditor();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setShop() {
        holder.shopLayout.setVisibility(View.VISIBLE);
        if (!model.header.shopName.equals("")) {
            holder.shopName.setText(model.header.shopName);
            holder.shopIcon.setVisibility(View.VISIBLE);
            holder.shopLabel.setVisibility(View.VISIBLE);
            ImageHandler.LoadImage(holder.shopIcon, model.header.shopIcon);
        } else {
            holder.shopName.setText(R.string.title_create_shop);
            holder.shopIcon.setVisibility(View.GONE);
            holder.shopLabel.setVisibility(View.GONE);
        }
        adapter.notifyDataSetChanged();
    }

    @Override
    public void getNotification() {
        if (!isLogin()) {
            return;
        }
        networkInteractor.getNotification(context, new NetworkInteractor.NotificationListener() {
            @Override
            public void onSuccess(NotificationItem notificationItem) {
                setNotificationShop(notificationItem);
                setNotificationInbox(notificationItem);
                toolbar.updateToolbar(notificationItem);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }
        });
    }

    private void setNotificationShop(NotificationItem notificationItem) {
        model.shopMenu.notif = notificationItem.getNotifShop();
        for (DrawerItem item : model.shopMenu.list) {
            switch (item.id) {
                case TkpdState.DrawerPosition.SHOP_NEW_ORDER:
                    item.notif = notificationItem.new_order;
                    break;
                case TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING:
                    item.notif = notificationItem.shipping_confirm;
                    break;
                case TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS:
                    item.notif = notificationItem.shipping_status;
                    break;
                default:
                    item.notif = 0;
            }
        }

    }

    private void setNotificationInbox(NotificationItem notificationItem) {
        model.inboxMenu.notif = notificationItem.getNotifMessage();
        for (DrawerItem item : model.inboxMenu.list) {
            switch (item.id) {
                case TkpdState.DrawerPosition.INBOX_MESSAGE:
                    item.notif = notificationItem.message;
                    break;
                case TkpdState.DrawerPosition.INBOX_TALK:
                    item.notif = notificationItem.talk;
                    break;
                case TkpdState.DrawerPosition.INBOX_REVIEW:
                    item.notif = notificationItem.reputation;
                    break;
                case TkpdState.DrawerPosition.INBOX_TICKET:
                    item.notif = notificationItem.ticket;
                    break;
                case TkpdState.DrawerPosition.RESOLUTION_CENTER:
                    item.notif = notificationItem.resolution;
                    break;
                default:
                    item.notif = 0;
            }
        }
    }


    private ToolbarVariable.OnDrawerToggleClickListener onToggleClickedListener() {
        return new ToolbarVariable.OnDrawerToggleClickListener() {
            @Override
            public void onDrawerToggleClick() {
                if (holder.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    closeDrawer();
                } else {
                    openDrawer();
                }
            }
        };
    }

    @Override
    public void closeDrawer() {
        if (holder != null && holder.drawerLayout != null)
            holder.drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void openDrawer() {
        holder.drawerLayout.openDrawer(GravityCompat.START);
    }

    @Override
    public void setToolbar(ToolbarVariable toolbar) {
        this.toolbar = toolbar;
    }

    @Override
    public void setEnabled(Boolean isEnabled) {
        if (isEnabled) {
            holder.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            holder.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

    @Override
    public int getDrawerListPosition(int id) {
        int position = 0;
        for (int i = 1; i < model.data.size(); i++) {
            if (model.data.get(i).getType() == TkpdState.DrawerItem.TYPE_LIST) {
                if (((DrawerItemList) model.data.get(i)).id == id) {
                    position = i;
                }
            }
        }
        return position;
    }

    @Override
    public boolean isOpened() {
        return holder.drawerLayout.isDrawerOpen(GravityCompat.START);
    }


    @Override
    public void unsubscribe() {
        networkInteractor.unsubscribe();
    }

    private void sendGTMNavigationEvent(String label) {
        UnifyTracking.eventDrawerClick(label);
    }
}