package com.tokopedia.core.drawer;

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
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppEventTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdActivity;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.drawer.interactor.NetworkInteractor;
import com.tokopedia.core.drawer.interactor.NetworkInteractorImpl;
import com.tokopedia.core.drawer.model.DrawerHeader;
import com.tokopedia.core.drawer.model.DrawerItem;
import com.tokopedia.core.drawer.model.DrawerItemList;
import com.tokopedia.core.drawer.model.DrawerSeparator;
import com.tokopedia.core.drawer.model.LoyaltyItem.LoyaltyItem;
import com.tokopedia.core.drawer.model.topcastItem.TopCashItem;
import com.tokopedia.core.drawer.var.NotificationItem;
import com.tokopedia.core.drawer.var.UserType;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.SessionRouter;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.core.router.home.SimpleHomeRouter;
import com.tokopedia.core.router.transactionmodule.TransactionPurchaseRouter;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.GlobalConfig;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.RecyclerViewItem;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.var.ToolbarVariable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nisie on 5/08/15.
 */

public class DrawerVariable {
    private static final String IS_INBOX_OPENED = "IS_INBOX_OPENED";
    private static final String IS_SHOP_OPENED = "IS_SHOP_OPENED";
    private static final String IS_PEOPLE_OPENED = "IS_PEOPLE_OPENED";

    private static final String CACHE_TOKO_CASH_TEXT = "CACHE_TOKO_CASH_TEXT";
    private static final String CACHE_TOKO_CASH_URL = "CACHE_TOKO_CASH_URL";
    private static final String CACHE_TOKO_CASH_LABEL = "CACHE_TOKO_CASH_LABEL";
    private static final String CACHE_TOKO_CASH_ACTION_TYPE = "CACHE_TOKO_CASH_ACTION_TYPE";
    private static final String CACHE_TOKO_CASH_OTHER_ACTION = "CACHE_TOKO_CASH_OTHER_ACTION";
    private static final String CACHE_TOKO_CASH_LINK = "CACHE_TOKO_CASH_LINK";

    private NetworkInteractor networkInteractor;
    public AppCompatActivity context;
    private ViewHolder holder;
    private Model model;
    private DrawerAdapter adapter;
    private LinearLayoutManager layoutManager;

    private SessionHandler Session;
    private LocalCacheHandler Cache;
    private LocalCacheHandler CacheNotif;
    //   private FacadeNotification facadeNotif;

    private int drawerPosition = 0;
    private DefaultItemAnimator animator;
    private ToolbarVariable toolbar;
    private boolean hasUpdated = false;

    public boolean hasUpdated() {
        return hasUpdated;
    }

    public void setHasUpdated(boolean hasUpdated) {
        this.hasUpdated = hasUpdated;
    }

    public class Model {
        private DrawerHeader header;
        private DrawerItemList peopleMenu;
        private DrawerItemList shopMenu;
        private DrawerItemList inboxMenu;
        private List<RecyclerViewItem> data;

        public Model() {
            data = new ArrayList<>();
            header = new DrawerHeader(context);
            peopleMenu = new DrawerItemList("Pembelian", 0, R.drawable.icon_pembelian, TkpdState.DrawerPosition.PEOPLE, true);
            shopMenu = new DrawerItemList("Penjualan", 0, R.drawable.icon_penjualan, TkpdState.DrawerPosition.SHOP, true);
            inboxMenu = new DrawerItemList("Kotak Masuk", 0, R.drawable.icon_inbox, TkpdState.DrawerPosition.INBOX, true);
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


    public DrawerVariable(AppCompatActivity context) {
        this.context = context;
    }

    public void createDrawer(boolean withSearchBox) {
        initVar(withSearchBox);
        initView();
        initListener();
        prepareView();
        updateData();
        setDrawer();
    }

    public void createDrawer() {
        createDrawer(false);
    }

    public void setOnSearchClickListener(View.OnClickListener listener) {
        toolbar.setSearchViewClickListener(listener);
    }

    private void initVar(boolean withSearchBox) {
        networkInteractor = new NetworkInteractorImpl();
        Session = new SessionHandler(context);
        Cache = new LocalCacheHandler(context, TkpdState.CacheName.CACHE_USER);
        CacheNotif = new LocalCacheHandler(context, "NOTIFICATION_DATA");
        holder = new ViewHolder();
        model = new Model();
        model.peopleMenu.list = new ArrayList<>();
        model.shopMenu.list = new ArrayList<>();
        model.inboxMenu.list = new ArrayList<>();
        adapter = new DrawerAdapter(context, model.data);
        layoutManager = new LinearLayoutManager(context);
        animator = new DefaultItemAnimator();
        if (withSearchBox) {
            toolbar.createToolbarWithSearchBox();
        } else if (context instanceof TkpdActivity) {
            toolbar.createToolbarWithDrawer();
        }
        initFacade();
        createPeopleMenu();
        createShopMenu();
        createInboxMenu();
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
        holder.drawerLayout.addDrawerListener(onDrawerListener());
    }

    private DrawerAdapter.DrawerListener onDrawer() {
        return new DrawerAdapter.DrawerListener() {
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
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SHOP_EN);
                closeDrawer();
            }
        };
    }

    private DrawerAdapter.GroupClickedListener onGroupClickedListener() {
        return new DrawerAdapter.GroupClickedListener() {
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
            case TkpdState.DrawerPosition.PEOPLE:
                Cache.putBoolean(IS_PEOPLE_OPENED, isExpand);
                break;
            case TkpdState.DrawerPosition.SHOP:
                Cache.putBoolean(IS_SHOP_OPENED, isExpand);
                break;
            default:
                break;
        }
        Cache.applyEditor();
        sendGTMNavigationEvent(group.label);
    }

    private DrawerAdapter.ChildClickedListener onChildClickedListener() {
        return new DrawerAdapter.ChildClickedListener() {
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
        switch (((DrawerItem) model.data.get(position)).id) {
            case TkpdState.DrawerPosition.INDEX_HOME:
                Intent intent = HomeRouter.getHomeActivity(context);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                context.startActivity(intent);
                break;
            case TkpdState.DrawerPosition.LOGIN:
            case TkpdState.DrawerPosition.REGISTER:
                intent = SessionRouter.getLoginActivityIntent(context);
                intent.putExtra(com.tokopedia.core.session.presenter.Session.WHICH_FRAGMENT_KEY, ((DrawerItem) model.data.get(position)).id);
                intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.HOME);
                context.startActivity(intent);
//                context.finish();
                break;
            case TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS:
                goToPeopleConfirmPayment();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PAYMENT_CONFIRMATION);
                break;
            case TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS:
                goToPeopleOrderStatus();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.ORDER_STATUS);
                break;
            case TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING:
                goToPeopleConfirmShipping();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.RECEIVE_CONFIRMATION);
                break;
            case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED:
                goToPeopleTransactionCanceled();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.CANCELLED_ORDER);
                break;
            case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST:
                goToPeopleTransactionList();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PURCHASE_LIST);
                break;
            case TkpdState.DrawerPosition.SHOP_NEW_ORDER:
                goToShopNewOrder();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.NEW_ORDER);
                break;
            case TkpdState.DrawerPosition.SHOP_OPPORTUNITY_LIST:
                goToOpportunityList();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.OPPORTUNIT_LIST
                );
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
                goToManageProduct();
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_LIST);
                break;
            case TkpdState.DrawerPosition.MANAGE_ETALASE:
                startIntent(EtalaseShopEditor.class);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.PRODUCT_DISPLAY);
                break;
            case TkpdState.DrawerPosition.INBOX_MESSAGE:
                intent = InboxRouter.getInboxMessageActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.MESSAGE);
                break;
            case TkpdState.DrawerPosition.INBOX_TALK:
                intent = InboxRouter.getInboxTalkActivityIntent(context);
                context.startActivity(intent);
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
                intent = InboxRouter.getInboxResCenterActivityIntent(context);
                context.startActivity(intent);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.RESOLUTION_CENTER);
                break;
            case TkpdState.DrawerPosition.DEVELOPER_OPTIONS:
                startIntent(DeveloperOptions.class);
                break;
            case TkpdState.DrawerPosition.WISHLIST:
//                Bundle bundle = new Bundle();
//                bundle.putInt(SimpleHomeActivity.FRAGMENT_TYPE, SimpleHomeActivity.WISHLIST_FRAGMENT);
//                Intent wishList = new Intent(context, SimpleHomeActivity.class);
//                wishList.putExtras(bundle);

                Intent wishList = SimpleHomeRouter
                        .getSimpleHomeActivityIntent(context, SimpleHomeRouter.WISHLIST_FRAGMENT);

                context.startActivity(wishList);
                sendGTMNavigationEvent(AppEventTracking.EventLabel.WISHLIST);
                break;
            case TkpdState.DrawerPosition.GOLD_MERCHANT:
                if (context.getApplication() instanceof TkpdCoreRouter) {
                    ((TkpdCoreRouter) context.getApplication())
                            .goToMerchantRedirect(context);
                }
                break;
            case TkpdState.DrawerPosition.SETTINGS:
                context.startActivity(new Intent(context, ManageGeneral.class));
                sendGTMNavigationEvent(AppEventTracking.EventLabel.SETTING);
                break;
            case TkpdState.DrawerPosition.CONTACT_US:
                intent = InboxRouter.getContactUsActivityIntent(context);
                if(TrackingUtils.getBoolean(AppEventTracking.GTM.CREATE_TICKET)) {
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

    private void goToOpportunityList() {
        Intent intent = SellerRouter.getActivitySellingTransaction(context);
        intent.putExtra("tab", 5);
        context.startActivity(intent);
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
        Intent intent = SellerRouter.getActivitySellingTransaction(context);
        Bundle bundle = new Bundle();
        bundle.putInt("tab", 1);
        bundle.putString("user_id", SessionHandler.getLoginID(context));
        intent.putExtras(bundle);
        context.startActivity(intent);
    }

    private void goToPeopleTransactionCanceled() {
        context.startActivity(TransactionPurchaseRouter.createIntentTxCanceled(context));
    }

    private void goToPeopleTransactionList() {
        context.startActivity(TransactionPurchaseRouter.createIntentTxAll(context));
    }

    private void goToPeopleConfirmShipping() {
        context.startActivity(TransactionPurchaseRouter.createIntentConfirmShipping(context));
    }

    private void goToPeopleOrderStatus() {
        context.startActivity(TransactionPurchaseRouter.createIntentTxStatus(context));
    }

    private void goToPeopleConfirmPayment() {
        context.startActivity(TransactionPurchaseRouter.createIntentConfirmPayment(context));
    }

    private void goToManageProduct() {
        if(context.getApplication() instanceof TkpdCoreRouter){
            ((TkpdCoreRouter)context.getApplication()).goToManageProduct(context);
        }
    }

    private void startIntent(Class<?> cls) {
        context.startActivity(new Intent(context, cls));

    }


    private void setDrawer() {
        holder.recyclerView.setAdapter(adapter);
    }


    private boolean isLogin() {
        return Session.getLoginID() != null && Session.isV4Login();
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
        if (GlobalConfig.isAllowDebuggingTools()) {
            model.data.add(new DrawerItem("Developer Options", 0, 0, TkpdState.DrawerPosition.DEVELOPER_OPTIONS, true));
        }
        //      model.data.add(new DrawerItem("Developer Options", 0, 0, TkpdState.DrawerPosition.DEVELOPER_OPTIONS, true));
        adapter.notifyDataSetChanged();


    }

    private void clearData() {
        model.data.clear();
    }

    private void setExpand() {
        if (Cache.getBoolean(IS_INBOX_OPENED, false)) {
            expandGroup(getDrawerListPosition(TkpdState.DrawerPosition.INBOX));
        }
        if (Cache.getBoolean(IS_PEOPLE_OPENED, false)) {
            expandGroup(getDrawerListPosition(TkpdState.DrawerPosition.PEOPLE));
        }
        if (Cache.getBoolean(IS_SHOP_OPENED, false)) {
            expandGroup(getDrawerListPosition(TkpdState.DrawerPosition.SHOP));
        }
    }

    private void setSelected() {
        adapter.setItemSelected(drawerPosition);
    }


    public void updateData() {
        clearData();
        hasUpdated = true;

        if (isLogin()) {
            setCache();
            Long curr_time = System.currentTimeMillis() / 1000;
            Long time = Long.parseLong(Cache.getString("timestamp", "0"));
            Long interval = curr_time - time;

            if (interval > 18000 || model.header.userName.equals("") || model.header.shopIcon.equals("")) {
                getUserInfo();
            }

            getDeposit();
            getNotification();
            getTokoCash();

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

        adapter.notifyDataSetChanged();
        holder.recyclerView.smoothScrollToPosition(0);
    }

    public void updateBalance() {
        getLoyalty();
        updateTokoCash();
    }

    private void setCache() {
        setCacheHeader();
        setCachePeopleMenu();
        setCacheShopMenu();
        setCacheInboxMenu();
        setCacheFooter();
        adapter.notifyDataSetChanged();
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

        setTokoCashValueFromCache();
    }

    private void setCachePeopleMenu() {
        model.peopleMenu.isExpanded = CacheNotif.getBoolean("IS_PEOPLE_OPENED", false);
        model.peopleMenu.list.get(0).notif = CacheNotif.getInt("payment_conf", 0);
        model.peopleMenu.list.get(1).notif = CacheNotif.getInt("order_status", 0);
        model.peopleMenu.list.get(2).notif = CacheNotif.getInt("delivery_confirm", 0);
        model.peopleMenu.list.get(3).notif = CacheNotif.getInt("reorder", 0);

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

    private void createPeopleMenu() {
        model.peopleMenu.list.add(new DrawerItem("Status Pembayaran", 0, 0, TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS, false));
        model.peopleMenu.list.add(new DrawerItem("Status Pemesanan", 0, 0, TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS, false));
        model.peopleMenu.list.add(new DrawerItem("Konfirmasi Penerimaan", 0, 0, TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING, false));
        model.peopleMenu.list.add(new DrawerItem("Transaksi Dibatalkan", 0, 0, TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED, false));
        model.peopleMenu.list.add(new DrawerItem("Daftar Pembelian", 0, 0, TkpdState.DrawerPosition.PEOPLE_TRANSACTION_LIST, false));
    }

    private void createShopMenu() {
        model.shopMenu.list.add(new DrawerItem("Order Baru", 0, 0, TkpdState.DrawerPosition.SHOP_NEW_ORDER, false));
        model.shopMenu.list.add(new DrawerItem("Konfirmasi Pengiriman", 0, 0, TkpdState.DrawerPosition.SHOP_CONFIRM_SHIPPING, false));
        model.shopMenu.list.add(new DrawerItem("Status Pengiriman", 0, 0, TkpdState.DrawerPosition.SHOP_SHIPPING_STATUS, false));
        model.shopMenu.list.add(new DrawerItem("Daftar Penjualan", 0, 0, TkpdState.DrawerPosition.SHOP_TRANSACTION_LIST, false));
        model.shopMenu.list.add(new DrawerItem("Peluang", 0, 0, TkpdState.DrawerPosition.SHOP_OPPORTUNITY_LIST, false));
        model.shopMenu.list.add(new DrawerSeparator());
        model.shopMenu.list.add(new DrawerItem("Daftar Produk", 0, 0, TkpdState.DrawerPosition.MANAGE_PRODUCT, true));
        model.shopMenu.list.add(new DrawerItem("Etalase Toko", 0, 0, TkpdState.DrawerPosition.MANAGE_ETALASE, true));
    }

    private void createInboxMenu() {
        model.inboxMenu.list.add(new DrawerItem("Pesan", 0, 0, TkpdState.DrawerPosition.INBOX_MESSAGE, false));
        model.inboxMenu.list.add(new DrawerItem("Diskusi Produk", 0, 0, TkpdState.DrawerPosition.INBOX_TALK, false));
        model.inboxMenu.list.add(new DrawerItem("Ulasan", 0, 0, TkpdState.DrawerPosition.INBOX_REVIEW, false));
        model.inboxMenu.list.add(new DrawerItem("Layanan Pengguna", 0, 0, TkpdState.DrawerPosition.INBOX_TICKET, false));
        model.inboxMenu.list.add(new DrawerItem("Pusat Resolusi", 0, 0, TkpdState.DrawerPosition.RESOLUTION_CENTER, false));
    }

    private void addMenuToDrawer() {
        model.data.add(model.header);
        model.data.add(new DrawerItem("Beranda", 0, R.drawable.icon_home, TkpdState.DrawerPosition.INDEX_HOME, false));
        model.data.add(new DrawerItem("Wishlist", 0, R.drawable.icon_wishlist, TkpdState.DrawerPosition.WISHLIST, false));
        model.data.add(model.inboxMenu);
        model.data.add(model.peopleMenu);
        if (!Session.getShopID().equals("0") && !Session.getShopID().equals("")) {
            model.data.add(model.shopMenu);
            model.data.add(new DrawerItem("Gold Merchant", 0, R.drawable.ic_goldmerchant_drawer,
                    TkpdState.DrawerPosition.GOLD_MERCHANT,false));
        }
        model.data.add(new DrawerItem("Pengaturan", 0, R.drawable.icon_setting, TkpdState.DrawerPosition.SETTINGS, false));
        if (!TrackingUtils.getBoolean(AppEventTracking.GTM.CONTACT_US)) {
            model.data.add(new DrawerItem("Hubungi Kami", 0, R.drawable.ic_contact_us, TkpdState.DrawerPosition.CONTACT_US, false));
        }
        model.data.add(new DrawerItem("Keluar", 0, R.drawable.ic_menu_logout, TkpdState.DrawerPosition.LOGOUT, false));
        if (BuildConfig.DEBUG & MainApplication.isDebug()) {
            model.data.add(new DrawerItem("Developer Options", 0, android.R.drawable.stat_sys_warning, TkpdState.DrawerPosition.DEVELOPER_OPTIONS, false));
        }
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

    public void getNotification() {
        if (!isLogin()) {
            return;
        }
        networkInteractor.getNotification(context, new NetworkInteractor.NotificationListener() {
            @Override
            public void onSuccess(NotificationItem notificationItem) {
                setNotificationPeople(notificationItem);
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


    private void setNotificationPeople(NotificationItem notificationItem) {
        model.peopleMenu.notif = notificationItem.getNotifPurchase();
        for (DrawerItem item : model.peopleMenu.list) {
            switch (item.id) {
                case TkpdState.DrawerPosition.PEOPLE_PAYMENT_STATUS:
                    item.notif = notificationItem.payment_confirmed;
                    break;
                case TkpdState.DrawerPosition.PEOPLE_ORDER_STATUS:
                    item.notif = notificationItem.order_status;
                    break;
                case TkpdState.DrawerPosition.PEOPLE_CONFIRM_SHIPPING:
                    item.notif = notificationItem.delivery_confirm;
                    break;
                case TkpdState.DrawerPosition.PEOPLE_TRANSACTION_CANCELED:
                    item.notif = notificationItem.reorder;
                    break;
                default:
                    item.notif = 0;
            }
        }
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
                sendGTMNavigationEvent(AppEventTracking.EventLabel.NAVIGATION_DRAWER);
            }
        };
    }

    public void closeDrawer() {
        holder.drawerLayout.closeDrawer(GravityCompat.START);
    }

    public void openDrawer() {
        holder.drawerLayout.openDrawer(GravityCompat.START);
    }

    public void setToolbar(ToolbarVariable toolbar) {
        this.toolbar = toolbar;
    }

    public void setEnabled(Boolean isEnabled) {
        if (isEnabled) {
            holder.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        } else {
            holder.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }
    }

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

    public boolean isOpened() {
        return holder.drawerLayout.isDrawerOpen(GravityCompat.START);
    }


    public void unsubscribe() {
        networkInteractor.unsubscribe();
    }

    private void sendGTMNavigationEvent(String label) {
        UnifyTracking.eventDrawerClick(label);
    }

    public static void startIntent(Context context, Class<?> cls) {
        context.startActivity(new Intent(context, cls));
    }

    private void getTokoCash() {
        networkInteractor.getTokoCash(context.getApplicationContext(),
                onTokoCashRenderedListener());
    }

    private void updateTokoCash() {
        networkInteractor.updateTokoCash(context.getApplicationContext(),
                onTokoCashRenderedListener());
    }

    private NetworkInteractor.TopCashListener onTokoCashRenderedListener() {
        return new NetworkInteractor.TopCashListener() {
            @Override
            public void onSuccess(TopCashItem topCashItem) {
                populateTokoCashData(topCashItem);
                putTokoCashValueOnCache();
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {

            }

            @Override
            public void onTokenExpire() {
                Intent intent = new Intent();
                intent.setAction("com.tokopedia.tkpd.FORCE_LOGOUT");
                MainApplication.getAppContext().sendBroadcast(intent);
            }
        };
    }

    private void populateTokoCashData(TopCashItem topCashItem) {
        model.header.tokoCashURL = topCashItem.getData().getRedirectUrl();
        model.header.tokoCashLink = topCashItem.getData().getLink();
        if(model.header.tokoCashLink == 1) {
            model.header.tokoCashValue = topCashItem.getData().getBalance();
            model.header.tokoCashText = topCashItem.getData().getText();
            model.header.tokoCashToWallet = true;
        } else {
            model.header.tokoCashText = topCashItem.getData().getText();
            model.header.tokoCashToWallet = false;
            if(topCashItem.getData().getAction() != null)
                model.header.tokoCashOtherAction = true;
        }
    }

    private void setTokoCashValueFromCache() {
        model.header.tokoCashValue = Cache.getString(CACHE_TOKO_CASH_TEXT);
        model.header.tokoCashURL = Cache.getString(CACHE_TOKO_CASH_URL);
        model.header.tokoCashText = Cache.getString(CACHE_TOKO_CASH_LABEL);
        model.header.tokoCashLink = Cache.getInt(CACHE_TOKO_CASH_LINK);
        model.header.tokoCashToWallet = Cache.getBoolean(CACHE_TOKO_CASH_ACTION_TYPE);
        model.header.tokoCashOtherAction = Cache.getBoolean(CACHE_TOKO_CASH_OTHER_ACTION);
    }

    private void putTokoCashValueOnCache() {
        Cache.putString(CACHE_TOKO_CASH_TEXT, model.header.tokoCashValue);
        Cache.putString(CACHE_TOKO_CASH_URL, model.header.tokoCashURL);
        Cache.putString(CACHE_TOKO_CASH_LABEL, model.header.tokoCashText);
        Cache.putInt(CACHE_TOKO_CASH_LINK, model.header.tokoCashLink);
        Cache.putBoolean(CACHE_TOKO_CASH_ACTION_TYPE,model.header.tokoCashToWallet);
        model.header.tokoCashOtherAction = Cache.getBoolean(CACHE_TOKO_CASH_OTHER_ACTION);
    }
}
