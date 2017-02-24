package com.tokopedia.sellerapp.home.view;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatDrawableManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ClickableSpan;
import android.text.style.URLSpan;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tkpd.library.utils.image.ImageHandler;
import com.tkpd.library.utils.network.ManyRequestErrorException;
import com.tkpd.library.utils.network.MessageErrorException;
import com.tokopedia.core.EtalaseShopEditor;
import com.tokopedia.core.ManageGeneral;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.analytics.appsflyer.Jordan;
import com.tokopedia.core.analytics.nishikino.Nishikino;
import com.tokopedia.core.analytics.nishikino.model.Authenticated;
import com.tokopedia.core.deposit.activity.DepositActivity;
import com.tokopedia.core.gcm.GCMHandler;
import com.tokopedia.core.gcm.GCMHandlerListener;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.inboxreputation.activity.InboxReputationActivity;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.network.SnackbarRetry;
import com.tokopedia.core.network.apiservices.shop.MyShopOrderService;
import com.tokopedia.core.network.apiservices.shop.ShopService;
import com.tokopedia.core.network.apiservices.transaction.DepositService;
import com.tokopedia.core.network.apiservices.user.InboxResCenterService;
import com.tokopedia.core.network.apiservices.user.NotificationService;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.session.presenter.SessionView;
import com.tokopedia.core.shopinfo.ShopInfoActivity;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SelectableSpannedMovementMethod;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.welcome.WelcomeActivity;
import com.tokopedia.seller.myproduct.ManageProduct;
import com.tokopedia.sellerapp.R;
import com.tokopedia.sellerapp.drawer.DrawerVariableSeller;
import com.tokopedia.sellerapp.gmsubscribe.GMSubscribeActivity;
import com.tokopedia.sellerapp.home.boommenu.BoomMenuButton;
import com.tokopedia.sellerapp.home.boommenu.SquareMenuButton;
import com.tokopedia.sellerapp.home.boommenu.Types.BoomType;
import com.tokopedia.sellerapp.home.boommenu.Types.ButtonType;
import com.tokopedia.sellerapp.home.boommenu.Types.PlaceType;
import com.tokopedia.sellerapp.home.boommenu.Util;
import com.tokopedia.sellerapp.home.di.SellerHomeDependencyInjection;
import com.tokopedia.sellerapp.home.fragment.CloseAppsDialogFragment;
import com.tokopedia.sellerapp.home.model.Ticker;
import com.tokopedia.sellerapp.home.model.deposit.DepositModel;
import com.tokopedia.sellerapp.home.model.notification.Notification;
import com.tokopedia.sellerapp.home.model.orderShipping.OrderShippingData;
import com.tokopedia.sellerapp.home.model.orderShipping.OrderShippingList;
import com.tokopedia.sellerapp.home.model.rescenter.ResCenterInboxData;
import com.tokopedia.sellerapp.home.model.shopmodel.ShopModel;
import com.tokopedia.sellerapp.home.utils.CollapsingToolbarLayoutCust;
import com.tokopedia.sellerapp.home.utils.DepositNetworkController;
import com.tokopedia.sellerapp.home.utils.InboxResCenterNetworkController;
import com.tokopedia.sellerapp.home.utils.NotifNetworkController;
import com.tokopedia.sellerapp.home.utils.ShopController;
import com.tokopedia.sellerapp.home.utils.ShopNetworkController;
import com.tokopedia.sellerapp.home.utils.ShopTransactionController;
import com.tokopedia.sellerapp.home.view.model.ShopScoreViewModel;
import com.tokopedia.sellerapp.home.view.presenter.SellerHomePresenterImpl;
import com.tokopedia.sellerapp.home.view.widget.ShopScoreWidget;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;
import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;

import static com.tokopedia.sellerapp.drawer.DrawerVariableSeller.goToShopNewOrder;

public class SellerHomeActivity
        extends AppCompatActivity
        implements GCMHandlerListener,
        SessionHandler.onLogoutListener,
        SellerHomeView {
    public static final String messageTAG = SellerHomeActivity.class.getSimpleName();
    public static final String STUART = "STUART";
    private static final String ARG_TRUECALLER_PACKAGE = "com.truecaller";
    ImageHandler imageHandler;
    ShopController shopController;

    @BindView(R.id.announcement_ticker)
    LinearLayout announcementTicker;

    @BindView(R.id.seller_home_transaction_view)
    TransactionView3 sellerHomeTransactionView;

    @BindView(R.id.activity_seller_home)
    CoordinatorLayout activitySellerHome;

    @BindView(R.id.seller_home_product_discussion)
    TextView sellerProductDiscussion;

    @BindView(R.id.seller_home_new_messages)
    TextView sellerHomeNewMessages;

    @BindView(R.id.seller_home_buyer_complain)
    TextView sellerHomeBuyerComplain;

    @BindView(R.id.seller_home_shop_cover)
    ImageView sellerHomeShopCover;

    @BindView(R.id.seller_home_shop_icon)
    CircleImageView sellerHomeShopIcon;

    @BindView(R.id.seller_home_shop_name)
    TextView sellerHomeShopname;

    @BindView(R.id.seller_home_shop_place)
    TextView sellerHomeShopPlace;

    @BindView(R.id.seller_home_badge_container)
    BadgeContainer sellerHomeBadgeContainer;

    @BindView(R.id.seller_home_deposit)
    TextView sellerHomeDeposit;

    @BindView(R.id.seller_home_reputation_view)
    ReputationView sellerHomeReputationView;

    SellerHomeNewOrderAdapter sellerHomeNewOrderAdapter;

    @BindView(R.id.seller_home_toolbar)
    Toolbar sellerHomeToolbar;

    @BindView(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayoutCust collapsingToolbar;

    @BindView(R.id.smooth_app_bar_layout)
    AppBarLayout smoothAppBarLayout;

    @BindView(R.id.nested_scroll_view)
    NestedScrollView nestedScrollView;
    @BindView(R.id.seller_home_boom)
    SquareMenuButton sellerHomeBoom;
    @BindView(R.id.hide_layout)
    View hideLayout;
    @BindView(R.id.card_new_order_container)
    RelativeLayout cardNewOrderContainer;
    boolean isBoomMenuShown = false;
    @BindView(R.id.seller_home_blank_space)
    LinearLayout sellerHomeBlankSpace;
    @BindView(R.id.gold_merchant_announcement_image)
    ImageView goldMerchantAnnouncementImage;
    @BindView(R.id.gold_merchant_announcement_text)
    TextView goldMerchantAnnouncementText;
    @Nullable
    ShopModel shopModel;
    @BindView(R.id.drawer_layout_nav)
    DrawerLayout drawerLayoutNav;
    @BindView(R.id.seller_home_linlay_container)
    LinearLayout sellerHomeLinLayContainer;
    String userId;
    String shopId;
    DrawerVariableSeller drawer;
    ActionBarDrawerToggle mDrawerToggle;
    SellerToolbarVariable toolbar;
    SnackbarRetry snackbarRetry;
    SnackbarRetry snackbarRetryUndefinite;
    @BindView(R.id.widget_shop_score)
    ShopScoreWidget shopScoreWidget;
    private boolean isInit = false;
    private SellerHomePresenterImpl presenter;

    @OnClick({R.id.discussion_see_more, R.id.discussion_container})
    public void discussionSeeMore() {
        startActivity(InboxRouter.getInboxTalkActivityIntent(this));
    }

    @OnClick({R.id.message_see_more, R.id.message__card_view_container})
    public void messageSeeMore() {
        this.startActivity(InboxRouter.getInboxMessageActivityIntent(this));
    }

    @OnClick({R.id.complain_see_more, R.id.complain_container})
    public void complainSeeMore() {
        startActivity(InboxRouter.getInboxResCenterActivityIntent(this));
    }

    @OnClick(R.id.seller_home_container)
    public void newOrderSeeMore() {
        goToShopNewOrder(this);
    }

    @OnClick(R.id.seller_home_deposit_container)
    public void moveToDeposit() {
        Intent intent = new Intent(this, DepositActivity.class);
        startActivity(intent);
    }

    @OnClick({R.id.seller_home_shop_icon, R.id.seller_home_shop_name})
    public void moveToShopSetting() {
        Intent intent = new Intent(this, ShopInfoActivity.class);
        Bundle bundle = ShopInfoActivity.createBundle(new SessionHandler(this).getShopID(), "");
        intent.putExtras(bundle);
        startActivity(intent);
    }

    @OnClick(R.id.gold_merchant_announcement)
    public void goToGoldMerchant(){
//        Toast.makeText(this, "Please implement !!!", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(this, GMSubscribeActivity.class));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
        }
        setContentView(R.layout.activity_seller_home);
        ButterKnife.bind(this);

        ViewTreeObserver vto = smoothAppBarLayout.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

                TypedValue tv = new TypedValue();
                SellerHomeActivity.this.getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true);
                int actionBarHeight = getResources().getDimensionPixelSize(tv.resourceId);

                if (Build.VERSION.SDK_INT < 16) {
                    smoothAppBarLayout.getViewTreeObserver().removeGlobalOnLayoutListener(this);
                }else{
                    smoothAppBarLayout.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                int width = smoothAppBarLayout.getMeasuredWidth();
                int height = smoothAppBarLayout.getMeasuredHeight() - actionBarHeight / 3;

                int heightDp = pxToDp(height);

                sellerHomeLinLayContainer.setPadding(0, height, 0, 0);
                sellerHomeLinLayContainer.requestLayout();
            }
        });

        snackbarRetry = new SnackbarRetry(Snackbar.make(activitySellerHome, getString(R.string.msg_network_error_1), Snackbar.LENGTH_LONG),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        shopController.init(SellerHomeActivity.this);
                    }
                });

        snackbarRetryUndefinite = new SnackbarRetry(Snackbar.make(activitySellerHome,
                getString(R.string.msg_network_error_1), Snackbar.LENGTH_INDEFINITE),
                new NetworkErrorHelper.RetryClickedListener() {
                    @Override
                    public void onRetryClicked() {
                        shopController.init(SellerHomeActivity.this);
                    }
                });

        initToolbar();
        initDrawer();
        setMinimumHeight(sellerHomeLinLayContainer);
        setDisplayNewOrder();

        userId = SessionHandler.getLoginID(this);
        shopId = SessionHandler.getShopID(this);
        imageHandler = new ImageHandler(this);

        drawer.setDrawerPosition(TkpdState.DrawerPosition.SELLER_INDEX_HOME);

        GCMHandler gcmHandler = new GCMHandler(this);
        Gson gson = new GsonBuilder().create();
        ShopService shopService = new ShopService();
        ShopNetworkController shopNetworkController = new ShopNetworkController(this, shopService, gson);
        NotificationService notificationService = new NotificationService();
        NotifNetworkController notifNetworkController = new NotifNetworkController(this, notificationService, gson);
        InboxResCenterService inboxResCenterService = new InboxResCenterService();
        InboxResCenterNetworkController inboxResCenterNetworkController
                = new InboxResCenterNetworkController(this, gson, inboxResCenterService);
        DepositService depositService = new DepositService();
        DepositNetworkController depositNetworkController
                = new DepositNetworkController(this, gson, depositService);
        MyShopOrderService myShopOrderService = new MyShopOrderService();
        ShopTransactionController shopTransactionController
                = new ShopTransactionController(myShopOrderService, this, gson);
        shopController = new ShopController(gcmHandler, shopNetworkController, notifNetworkController,
                inboxResCenterNetworkController, depositNetworkController, shopTransactionController
                , gson);
        shopController.subscribe();


        presenter = SellerHomeDependencyInjection.getPresenter(this);
        presenter.attachView(this);
    }

    public int pxToDp(int px) {
        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
        int dp = Math.round(px / (displayMetrics.xdpi / DisplayMetrics.DENSITY_DEFAULT));
        Log.d("SellerHomeActivity", "px " + px + " dp " + dp);
        return dp;
    }

    private void initDrawer() {
        drawer = new DrawerVariableSeller(this);
        toolbar = new SellerToolbarVariable(this, sellerHomeToolbar);
        toolbar.createToolbarWithDrawer();
        drawer.setToolbar(toolbar);
        drawer.createDrawer();
        drawer.setEnabled(true);

        sellerHomeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("NIS", "CLICK");
                drawer.openDrawer();
            }
        });
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        if (mDrawerToggle != null)
            mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public void onGCMSuccess(final String gcmId) {
        final String userId = this.userId;
        ShopNetworkController.ShopInfoParam shopInfoParam = new ShopNetworkController.ShopInfoParam();
        shopInfoParam.shopId = this.shopId;

        // get all this using this
        shopController.getData(userId, gcmId, shopInfoParam, getShopInfo(),
                getNotif(gcmId, userId), getResCenter(), getDeposit(), getTicker()
        );
    }

    protected ShopController.ListenerGetTicker getTicker(){
        return new ShopController.ListenerGetTicker() {
            @Override
            public void onSuccess(Ticker.Tickers[] tickers) {
                if (tickers != null && tickers.length > 0) {
                    generateTicker(tickers);
                }
            }

            @Override
            public void onError() {
                announcementTicker.setVisibility(View.GONE);
            }
        };
    }

    private void generateTicker(Ticker.Tickers[] tickers) {
        announcementTicker.removeAllViews();
        announcementTicker.setVisibility(View.VISIBLE);
        for(int position = 0; position<tickers.length; position++){

            View view = getLayoutInflater().inflate(R.layout.layout_ticker_announcement, null);

            TextView title = ButterKnife.findById(view, R.id.ticker_title);
            TextView message = ButterKnife.findById(view, R.id.ticker_message);

            if (tickers[position].getTitle() != null && tickers[position].getTitle().length() == 0) {
                title.setVisibility(View.GONE);
            } else {
                title.setVisibility(View.VISIBLE);
                title.setText(tickers[position].getTitle());
            }
            message.setText(tickers[position].getMessage());
            message.setMovementMethod(new SelectableSpannedMovementMethod());

            Spannable sp = (Spannable)message.getText();
            URLSpan[] urls=sp.getSpans(0, message.getText().length(), URLSpan.class);
            SpannableStringBuilder style=new SpannableStringBuilder(message.getText());
            style.clearSpans();
            for(final URLSpan url : urls){
                style.setSpan(new ClickableSpan() {
                    @Override
                    public void onClick(View widget) {
                        Intent intent = new Intent(SellerHomeActivity.this, BannerWebView.class);
                        intent.putExtra("url", url.getURL());
                        startActivity(intent);
                    }
                }, sp.getSpanStart(url), sp.getSpanEnd(url), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            message.setText(style);

            announcementTicker.addView(view);
        }
    }

    @NonNull
    protected DepositNetworkController.DepositListener getDeposit() {
        return new DepositNetworkController.DepositListener() {
            @Override
            public void onSuccess(DepositModel.Data depositModel) {
                if (sellerHomeDeposit != null)
                    sellerHomeDeposit.setText(depositModel.getDepositTotal());
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MessageErrorException) {
                    Snackbar.make(activitySellerHome, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }else{
                    if (snackbarRetryUndefinite != null) {
                        snackbarRetryUndefinite.showRetrySnackbar();
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        };
    }

    @NonNull
    protected InboxResCenterNetworkController.InboxResCenterListener getResCenter() {
        return new InboxResCenterNetworkController.InboxResCenterListener() {
            @Override
            public void onSuccess(ResCenterInboxData resCenterInboxData) {
                if (sellerHomeBuyerComplain != null) {
                    Integer sellerResol = resCenterInboxData.getCount().getSellerResol();
                    Log.d("STUART", "number of seller complain : " + sellerResol);
                    sellerHomeBuyerComplain.setText(Integer.toString(sellerResol));
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MessageErrorException) {
                    Snackbar.make(activitySellerHome, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }else{
                    if (snackbarRetryUndefinite != null) {
                        snackbarRetryUndefinite.showRetrySnackbar();
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        };
    }

    @NonNull
    protected NotifNetworkController.GetNotif getNotif(final String gcmId, final String userId) {
        return new NotifNetworkController.GetNotif() {
            @Override
            public void onSuccess(Notification.Data notification) {
                String sales_new_order = notification.getSales().getSales_new_order();
                int i = Integer.parseInt(sales_new_order);
                cardNewOrderContainer.removeAllViews();
                if (i > 0) {
                    RelativeLayout.LayoutParams layoutParamsHeader =
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                    layoutParamsHeader.addRule(RelativeLayout.ALIGN_PARENT_TOP);
                    NewOrderHeaderView newOrderHeaderView = new NewOrderHeaderView(SellerHomeActivity.this);
                    newOrderHeaderView.setId(R.id.seller_home_new_order_container);

                    RelativeLayout.LayoutParams layoutParams2 =
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.WRAP_CONTENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                    layoutParams2.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                    LinearLayout container = new LinearLayout(SellerHomeActivity.this);
                    container.setOrientation(LinearLayout.VERTICAL);
                    container.setId(R.id.seller_home_latest_new_order);

                    RelativeLayout.LayoutParams layoutParams =
                            new RelativeLayout.LayoutParams(
                                    RelativeLayout.LayoutParams.MATCH_PARENT,
                                    RelativeLayout.LayoutParams.WRAP_CONTENT
                            );
                    layoutParams.addRule(RelativeLayout.ABOVE, container.getId());
                    NewOrderView newOrderView = new NewOrderView(SellerHomeActivity.this);
                    newOrderView.init(notification.getSales());
                    newOrderView.setId(R.id.card_new_order);

                    cardNewOrderContainer.addView(newOrderHeaderView, layoutParamsHeader);
                    cardNewOrderContainer.addView(newOrderView, layoutParams);
                    cardNewOrderContainer.addView(container, layoutParams2);

                    getNewOrder(gcmId, userId, container);
                } else {
                    setDisplayNewOrder();
                }

                if (sellerProductDiscussion != null) {
                    sellerProductDiscussion.setText(notification.getInbox().getInbox_talk());
                }
                sellerHomeNewMessages.setText(notification.getInbox().getInbox_message());

            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MessageErrorException) {
                    Snackbar.make(activitySellerHome, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }else{
                    if (snackbarRetryUndefinite != null) {
                        snackbarRetryUndefinite.showRetrySnackbar();
                    }
                }
            }

            @Override
            public void onFailure() {

            }
        };
    }

    /**
     * set display new order frame when first time load or 0 order
     */
    private void setDisplayNewOrder() {
        RelativeLayout.LayoutParams layoutParamsHeader =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        layoutParamsHeader.addRule(RelativeLayout.ALIGN_PARENT_TOP);
        NewOrderHeaderView newOrderHeaderView = new NewOrderHeaderView(SellerHomeActivity.this);
        newOrderHeaderView.setId(R.id.seller_home_new_order_container);

        RelativeLayout.LayoutParams layoutParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        layoutParams.addRule(RelativeLayout.BELOW, newOrderHeaderView.getId());
//        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        NewOrderView newOrderView = new NewOrderView(SellerHomeActivity.this);
        newOrderView.setId(R.id.card_new_order);
//        newOrderView.setLineBreak(false);

        RelativeLayout.LayoutParams emptyLayoutParams =
                new RelativeLayout.LayoutParams(
                        RelativeLayout.LayoutParams.MATCH_PARENT,
                        RelativeLayout.LayoutParams.WRAP_CONTENT
                );
        emptyLayoutParams.addRule(RelativeLayout.BELOW, newOrderView.getId());
        NoResultView noResultView = new NoResultView(this, null);

        cardNewOrderContainer.addView(newOrderHeaderView, layoutParamsHeader);
        cardNewOrderContainer.addView(newOrderView, layoutParams);
        cardNewOrderContainer.addView(noResultView, emptyLayoutParams);
    }

    @NonNull
    protected ShopNetworkController.GetShopInfo getShopInfo() {
        return new ShopNetworkController.GetShopInfo() {
            @Override
            public void onFailure() {

            }

            @Override
            public void onSuccess(ShopModel shopModel) {
                //[START] for debugging purpose
                SellerHomeActivity.this.shopModel = shopModel;
                Log.d("STUART", shopModel.toString());
                //[END] for debugging purpose

                if (sellerHomeTransactionView != null) {
                    sellerHomeTransactionView.init(shopModel);
                }

                sellerHomeShopname.setText(MethodChecker.fromHtml(shopModel.info.shopName));
                sellerHomeShopPlace.setText(shopModel.info.shopLocation);

                imageHandler.loadImage(sellerHomeShopCover, shopModel.info.shopCover);
                imageHandler.loadImage(sellerHomeShopIcon, shopModel.info.shopAvatar);

                // home badge container need here

                ReputationView.ReputationViewModel reputationViewModel = new ReputationView.ReputationViewModel();
                reputationViewModel.typeMedal = shopModel.stats.shopBadgeLevel.set;
                reputationViewModel.levelMedal = shopModel.stats.shopBadgeLevel.level;
                reputationViewModel.reputationPoints = shopModel.stats.shopReputationScore;
                reputationViewModel.stats = shopModel.stats;

                sellerHomeReputationView.init(reputationViewModel);

                boolean isGold = shopModel.info.shopIsGold == 1;
                String shopLucky = shopModel.info.shopLucky;
                BadgeContainer.BadgeContainerModel badgeContainerModel =
                        new BadgeContainer.BadgeContainerModel();
                badgeContainerModel.luckyUrl = shopLucky;
                badgeContainerModel.isGold = isGold;
                sellerHomeBadgeContainer.init(badgeContainerModel, imageHandler);

                if(isGold){
                    goldMerchantAnnouncementText.setText(R.string.extend_gold_merchant);
                }else{
                    goldMerchantAnnouncementText.setText(R.string.upgrade_gold_merchant);
                }
                goldMerchantAnnouncementImage.setVisibility(View.VISIBLE);
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof MessageErrorException) {
                    if (snackbarRetryUndefinite != null) {
                        snackbarRetryUndefinite.showRetrySnackbar();
                    }
//                    Snackbar.make(activitySellerHome, e.getMessage(), Snackbar.LENGTH_LONG).show();
                } else if (e instanceof ManyRequestErrorException) {
                    if (snackbarRetryUndefinite != null) {
                        snackbarRetryUndefinite.showRetrySnackbar();
                    }
                } else {
                    if (snackbarRetryUndefinite != null) {
                        snackbarRetryUndefinite.showRetrySnackbar();
                    }
                }
            }
        };
    }

    private void getNewOrder(String gcmId, String userId, final LinearLayout container) {
        shopController.getNewOrder(userId, gcmId, new ShopTransactionController.GetNewOrder() {
            @Override
            public void onSuccess(OrderShippingData orderShippingData) {
                List<OrderShippingList> orderShippingList = orderShippingData.getOrderShippingList();

                if (orderShippingList != null && !orderShippingList.isEmpty()) {
                    Observable.from(orderShippingList)
                            .take(3)
                            .observeOn(AndroidSchedulers.mainThread())
                            .subscribe(new Action1<OrderShippingList>() {
                                @Override
                                public void call(OrderShippingList orderShippingList) {

                                    View view = LayoutInflater.from(container.getContext()).inflate(R.layout.seller_home_new_order, container, false);
                                    new SellerHomeNewOrderView(view).initData(orderShippingList);
                                    container.addView(view);
                                }
                            });
                }
            }

            @Override
            public void onError(Throwable e) {
                if (e instanceof ShopNetworkController.MessageErrorException) {
                    Snackbar.make(activitySellerHome, e.getMessage(), Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure() {

            }
        });
    }

    private void initToolbar() {
//        sellerHomeToolbar.setTitle("");
//        sellerHomeToolbar.setNavigationIcon(R.drawable.ic_arrow_back_3x);
        sellerHomeToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        setSupportActionBar(sellerHomeToolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        getSupportActionBar().setHomeButtonEnabled(true);
        collapsingToolbar.setTitleEnabled(true);

        smoothAppBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {

                if (collapsingToolbar.isScrimsShown) {
                    if (shopModel == null || shopModel.info == null || shopModel.info.shopName == null)
                        toolbar.setTitleText("Home");
                    else
                        toolbar.setTitleText(shopModel.info.shopName);
                } else {
                    toolbar.setTitleText(" ");
                }
            }
        });

    }

    @Override
    public void onLogout(Boolean success) {
        finish();
        Intent intent = new Intent(this, WelcomeActivity.class);
        intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
        intent.putExtra(SessionView.MOVE_TO_CART_KEY, SessionView.SELLER_HOME);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    @Override
    public void renderShopScore(ShopScoreViewModel shopScoreViewModel) {
        shopScoreWidget.renderView(shopScoreViewModel);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (!isInit) {
            initBoom();
        }
        isInit = true;
    }

    private void initBoom() {

        Drawable[] drawables = new Drawable[4];
        int[] drawablesResource = new int[]{
                R.drawable.ic_atur_produk,
                R.drawable.ic_ulasan,
                R.drawable.ic_etalase,
                R.drawable.ic_settings
        };
        for (int i = 0; i < drawables.length; i++)
            drawables[i] = AppCompatDrawableManager.get().getDrawable(this, drawablesResource[i]);

        String[] STRINGS = new String[]{
                "Atur Produk",
                "Ulasan",
                "Etalase",
                "Pengaturan"
        };
        String[] strings = new String[drawables.length];
        for (int i = 0; i < drawables.length; i++)
            strings[i] = STRINGS[i];

        String color = "#ffffff";

        int[][] colors = new int[drawables.length][2];
        for (int i = 0; i < drawables.length; i++) {
            colors[i][1] = Color.parseColor(color);
            colors[i][0] = Util.getInstance().getPressedColor(colors[i][1]);
        }

        ButtonType buttonType = ButtonType.CIRCLE;

        // Now with Builder, you can init BMB more convenient
        new SquareMenuButton.Builder()
                .duration(300)
                .subButtons(drawables, colors, strings)
                .button(buttonType)
                .boom(BoomType.LINE)
                .place(PlaceType.CIRCLE_4_1)
                .subButtonsShadow(Util.getInstance().dp2px(2), Util.getInstance().dp2px(2))
                .onSubButtonClick(new BoomMenuButton.OnSubButtonClickListener() {
                    @Override
                    public void onClick(int buttonIndex) {
                        Context context = SellerHomeActivity.this;
                        switch (buttonIndex) {
                            case 0:
                                context.startActivity(new Intent(
                                        context, ManageProduct.class
                                ));
                                break;
                            case 1:
                                context.startActivity(new Intent(
                                        context, InboxReputationActivity.class));
                                break;
                            case 2:
                                context.startActivity(new Intent(
                                        context, EtalaseShopEditor.class));
                                break;
                            case 3:
                                context.startActivity(new Intent(
                                        context, ManageGeneral.class));
                                break;

                        }
                    }
                })
                .animator(new BoomMenuButton.AnimatorListener() {
                    @Override
                    public void toShow() {
                        hideLayout.setVisibility(View.VISIBLE);
                        hideLayout.setClickable(true);
                        isBoomMenuShown = true;
                    }

                    @Override
                    public void showing(float fraction) {

                    }

                    @Override
                    public void showed() {

                    }

                    @Override
                    public void toHide() {

                    }

                    @Override
                    public void hiding(float fraction) {

                    }

                    @Override
                    public void hided() {
                        isBoomMenuShown = false;
                        hideLayout.setVisibility(View.INVISIBLE);
                        hideLayout.setClickable(false);
                    }
                })
                .onGetAnimationRootLayout(new BoomMenuButton.GetAnimationRootLayout() {
                    @Override
                    public ViewGroup getRootView() {
                        return activitySellerHome;
                    }
                })
                .init(sellerHomeBoom);
    }

    @Override
    public void onBackPressed() {
        if (isBoomMenuShown) {
            sellerHomeBoom.dismiss();
        } else if (drawer.isOpened()) {
            drawer.closeDrawer();
        } else {
            showExitDialog();
        }
    }

    private void showExitDialog() {
        LocalCacheHandler handler = new LocalCacheHandler(this, CloseAppsDialogFragment.CLOSE_APPS_CACHE);
        if (handler.getBoolean(CloseAppsDialogFragment.DONT_SHOW_FLAG)) {
            super.onBackPressed();
        } else {
            CloseAppsDialogFragment dialog = CloseAppsDialogFragment.newInstance();
            FragmentManager fm = getFragmentManager();

            dialog.show(fm, "filter_dialog");
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                Log.d(STUART, messageTAG + " R.id.home !!!");
                finish();
                return true;
            case android.R.id.home:
                Log.d(STUART, messageTAG + " android.R.id.home !!!");
                finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setMinimumHeight(View view) {
        DisplayMetrics displaymetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displaymetrics);
        int screenHeight = displaymetrics.heightPixels;

        int actionBarHeight = 0;
        TypedValue tv = new TypedValue();
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        view.setMinimumHeight(screenHeight - actionBarHeight);
    }

    @Override
    protected void onResume() {
        super.onResume();
        shopController.init(this);
        smoothAppBarLayout.setExpanded(true);
        sendToGTM();
        sendToLocalytics();
        presenter.getShopScoreMainData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        shopController.unSubscribe();
    }

    private void sendToGTM() {

        Authenticated authEvent = new Authenticated();
        authEvent.setUserFullName(SessionHandler.getLoginName(this));
        authEvent.setUserID(SessionHandler.getGTMLoginID(this));
        authEvent.setShopID(SessionHandler.getShopID(this));
        authEvent.setUserSeller(SessionHandler.getShopID(this).equals("0") ? 0 : 1);

        CommonUtils.dumper("GAv4 appdata " + new JSONObject(authEvent.getAuthDataLayar()).toString());

        Nishikino.init(this).startAnalytics()
                .eventAuthenticate(authEvent)
                .sendScreen(AppScreen.SCREEN_SELLER_HOME);
    }

    private void sendToLocalytics() {
        Jordan.init(this).getLocalyticsContainer()
                .tagScreen(AppScreen.SCREEN_SELLER_HOME);
    }

    @Override
    public void onStart() {
        super.onStart();
        if(appInstalledOrNot(ARG_TRUECALLER_PACKAGE)){
            UnifyTracking.eventTrueCaller(SessionHandler.getLoginID(this));
        }
    }

    private boolean appInstalledOrNot(String uri) {
        PackageManager pm = getPackageManager();
        boolean app_installed;
        try {
            pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }
        catch (PackageManager.NameNotFoundException e) {
            app_installed = false;
        }
        return app_installed;
    }

    public static class SellerHomeNewOrderView {
        @BindView(R.id.new_order_name)
        TextView newOrderName;

        @BindView(R.id.new_order_date)
        TextView newOrderDate;

        @BindView(R.id.new_order_remaining_days)
        TextView newOrderRemainingDays;
        private OrderShippingList orderShippingList;
        private View itemView;

        public SellerHomeNewOrderView(View itemView) {
            this.itemView = itemView;
            ButterKnife.bind(this, itemView);
        }

        public void initData(OrderShippingList orderShippingList) {
            this.orderShippingList = orderShippingList;

            newOrderName.setText(orderShippingList.getOrderCustomer().getCustomerName());
            newOrderDate.setText(orderShippingList.getOrderDetail().getDetailOrderDate());
            String daysLeft;
            switch (orderShippingList.getOrderPayment().getPaymentProcessDayLeft()) {
                case 0:
                    daysLeft = "Hari ini";
                    newOrderRemainingDays.setBackgroundColor(newOrderRemainingDays.getResources().getColor(R.color.tkpd_status_red));
                    break;
                case 1:
                    daysLeft = "Besok";
                    newOrderRemainingDays.setBackgroundColor(newOrderRemainingDays.getResources().getColor(R.color.tkpd_status_orange));
                    break;
                default:
                    daysLeft = orderShippingList.getOrderPayment().getPaymentProcessDayLeft() + " Hari Lagi";
                    newOrderRemainingDays.setBackgroundColor(newOrderRemainingDays.getResources().getColor(R.color.tkpd_status_blue));
            }
            newOrderRemainingDays.setText(daysLeft);
        }
    }

    public static class SellerHomeNewOrderViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.new_order_name)
        TextView newOrderName;

        @BindView(R.id.new_order_date)
        TextView newOrderDate;

        @BindView(R.id.new_order_remaining_days)
        TextView newOrderRemainingDays;
        private OrderShippingList orderShippingList;

        public SellerHomeNewOrderViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void initData(OrderShippingList orderShippingList) {
            this.orderShippingList = orderShippingList;

            newOrderName.setText(orderShippingList.getOrderCustomer().getCustomerName());
            newOrderDate.setText(orderShippingList.getOrderDetail().getDetailOrderDate());
            newOrderRemainingDays.setText(orderShippingList.getOrderPayment().getPaymentProcessDayLeft() + " Hari Lagi");
        }
    }

    public static class SellerHomeNewOrderAdapter extends RecyclerView.Adapter<SellerHomeNewOrderViewHolder> {

        private List<OrderShippingList> dataList;

        public SellerHomeNewOrderAdapter(List<OrderShippingList> dataList) {
            this.dataList = dataList;
        }

        @Override
        public SellerHomeNewOrderViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.seller_home_new_order, parent, false);
            return new SellerHomeNewOrderViewHolder(view);
        }

        @Override
        public void onBindViewHolder(SellerHomeNewOrderViewHolder holder, int position) {
            holder.initData(dataList.get(position));
        }

        @Override
        public int getItemCount() {
            return dataList.size();
        }
    }
}
