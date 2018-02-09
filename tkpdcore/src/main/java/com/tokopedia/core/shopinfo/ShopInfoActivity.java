package com.tokopedia.core.shopinfo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v7.widget.Toolbar;
import android.text.Spanned;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.deeplinkdispatch.DeepLink;
import com.google.gson.Gson;
import com.tkpd.library.utils.CommonUtils;
import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.SnackbarManager;
import com.tokopedia.core.BuildConfig;
import com.tokopedia.core.R;
import com.tokopedia.core.analytics.AppScreen;
import com.tokopedia.core.analytics.ScreenTracking;
import com.tokopedia.core.analytics.TrackingUtils;
import com.tokopedia.core.analytics.UnifyTracking;
import com.tokopedia.core.app.BaseActivity;
import com.tokopedia.core.app.MainApplication;
import com.tokopedia.core.app.TkpdCoreRouter;
import com.tokopedia.core.gcm.Constants;
import com.tokopedia.core.home.BannerWebView;
import com.tokopedia.core.listener.GlobalMainTabSelectedListener;
import com.tokopedia.core.loyaltysystem.util.LuckyShopImage;
import com.tokopedia.core.loyaltysystem.util.URLGenerator;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.core.product.model.share.ShareData;
import com.tokopedia.core.reputationproduct.util.ReputationLevelUtils;
import com.tokopedia.core.router.InboxRouter;
import com.tokopedia.core.router.OldSessionRouter;
import com.tokopedia.core.router.SellerRouter;
import com.tokopedia.core.router.TkpdInboxRouter;
import com.tokopedia.core.router.productdetail.ProductDetailRouter;
import com.tokopedia.core.router.reactnative.IReactNativeRouter;
import com.tokopedia.core.session.presenter.Session;
import com.tokopedia.core.share.ShareActivity;
import com.tokopedia.core.shopinfo.adapter.ShopTabPagerAdapter;
import com.tokopedia.core.shopinfo.facades.ActionShopInfoRetrofit;
import com.tokopedia.core.shopinfo.facades.GetShopInfoRetrofit;
import com.tokopedia.core.shopinfo.fragment.OfficialShopHomeFragment;
import com.tokopedia.core.shopinfo.fragment.ProductList;
import com.tokopedia.core.shopinfo.models.GetShopProductParam;
import com.tokopedia.core.shopinfo.models.shopmodel.Info;
import com.tokopedia.core.util.MethodChecker;
import com.tokopedia.core.util.SessionHandler;
import com.tokopedia.core.var.Badge;
import com.tokopedia.core.var.TkpdState;
import com.tokopedia.core.widgets.NonSwipeableViewPager;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.List;

import static com.tokopedia.core.gcm.Constants.FROM_APP_SHORTCUTS;
import static com.tokopedia.core.router.InboxRouter.PARAM_OWNER_FULLNAME;
import static com.tokopedia.core.shopinfo.models.shopmodel.Info.SHOP_OFFICIAL_VALUE;

/**
 * Created by UNKNOWN on UNKNOWN DATE TIME
 * Edited by HAFIZH on 23-01-2017
 */

public class ShopInfoActivity extends BaseActivity
        implements OfficialShopHomeFragment.OfficialShopInteractionListener,
        ProductList.ProductListCallback {
    private static final int FAVORITE_LOGIN_REQUEST_CODE = 1020;

    public static final String SHOP_STATUS_IS_FAVORITED = "shopIsFavorited";
    public static final String FAVORITE_STATUS_UPDATED = "favoriteStatusUpdated";

    public static final int REQUEST_CODE_LOGIN = 561;
    private static final String FORMAT_UTF_8 = "UTF-8";
    private static final String URL_RECHARGE_HOST = "pulsa.tokopedia.com";

    public static final String EXTRA_STATE_TAB_POSITION = "EXTRA_STATE_TAB_POSITION";

    public final static int TAB_POSITION_HOME = 1;
    public final static int TAB_POSITION_ETALASE = 2;
    public final static int TAB_POSITION_TALK = 3;
    public final static int TAB_POSITION_REVIEW = 4;
    public final static int TAB_POSITION_NOTE = 5;
    public final static int NAVIGATION_TO_INFO = 6;

    private class ViewHolder {
        NonSwipeableViewPager pager;
        TabLayout indicator;
        AppBarLayout appBarLayout;
        ImageView banner;
        TextView shopName;
        TextView closeNotice;
        ImageView goldShop;
        ImageView shopAvatar;
        ImageView luckyShop;
        ImageView freeReturns;
        TextView location;
        ImageView favorite;
        LinearLayout badges;
        View infoShop;
        View message;
        View setting;
        View progressBar;
        View progressView;
        View more;
        View shareBut;
        CollapsingToolbarLayout collapsingToolbarLayout;
        Toolbar toolbar;
    }

    public static String SHOP_ID = "shop_id";
    public static String SHOP_DOMAIN = "shop_domain";
    public static String SHOP_NAME = "shop_name";
    public static String SHOP_AVATAR = "shop_avatar";
    public static String SHOP_FAVORITE = "shop_favorite";
    public static String SHOP_COVER = "shop_cover";
    public static String SHOP_AD_KEY = "shop_ad_key";
    public static String ETALASE_NAME = "etalase_name";
    public static String KEYWORD = "keyword";
    private final int REQ_RELOAD = 100;
    private ViewHolder holder;
    private com.tokopedia.core.shopinfo.models.shopmodel.ShopModel shopModel;
    private GetShopInfoRetrofit facadeGetRetrofit;
    private ActionShopInfoRetrofit facadeAction;
    private ScaleAnimation animateFav;
    private String shopInfoString;
    private String adKey;
    private ShopTabPagerAdapter adapter;
    private String redirectionUrl;
    public static final String LOGIN_ACTION = BuildConfig.APPLICATION_ID + ".LOGIN_ACTION";
    private BroadcastReceiver loginReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    initFacadeAndLoadShopInfo();
                }
            });
        }
    };

    @Override
    public String getScreenName() {
        return AppScreen.SCREEN_SHOP;
    }

    public static Bundle createBundle(String id, String domain) {
        Bundle bundle = new Bundle();
        bundle.putString(SHOP_ID, id);
        bundle.putString(SHOP_DOMAIN, domain);
        return bundle;
    }

    public static Bundle createBundle(String id, String domain, String menuId) {
        Bundle bundle = createBundle(id, domain);
        return bundle;
    }

    public static Bundle createBundle(String id, String domain, String name, String avatar, int favorite) {
        Bundle bundle = createBundle(id, domain);
        bundle.putString(SHOP_NAME, name);
        bundle.putString(SHOP_AVATAR, avatar);
        bundle.putInt(SHOP_FAVORITE, favorite);
        return bundle;
    }

    public static Bundle createBundle(String id, String domain, String name, String avatar, String cover, int favorite) {
        Bundle bundle = createBundle(id, domain, name, avatar, favorite);
        bundle.putString(SHOP_COVER, cover);
        return bundle;
    }

    public static Intent getCallingIntent(Context context, String shopId) {
        Bundle bundle = ShopInfoActivity.createBundle(shopId, "");
        Intent intent = new Intent(context, ShopInfoActivity.class);
        intent.putExtras(bundle);
        return intent;
    }

    @DeepLink(Constants.Applinks.SHOP)
    public static Intent getCallingIntent(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_HOME)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SHOP_ETALASE)
    public static Intent getCallingIntentEtalaseSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();

        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_ETALASE)
                .putExtras(extras);
    }


    @DeepLink(Constants.Applinks.SHOP_TALK)
    public static Intent getCallingIntentTalkSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_TALK)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SHOP_REVIEW)
    public static Intent getCallingIntentReviewSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_REVIEW)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SHOP_NOTE)
    public static Intent getCallingIntentNoteSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, TAB_POSITION_NOTE)
                .putExtras(extras);
    }

    @DeepLink(Constants.Applinks.SHOP_INFO)
    public static Intent getCallingIntentInfoSelected(Context context, Bundle extras) {
        Uri.Builder uri = Uri.parse(extras.getString(DeepLink.URI)).buildUpon();
        return new Intent(context, ShopInfoActivity.class)
                .setData(uri.build())
                .putExtra(EXTRA_STATE_TAB_POSITION, NAVIGATION_TO_INFO)
                .putExtras(extras);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_info);
        initModel();
        initView();
        initVar();
        if (savedInstanceState == null) {
            initFacadeAndLoadShopInfo();
        } else {
            loadSavedModel(savedInstanceState);
            if (shopModel.info.shopOpenSince == null) {
                initFacadeAndLoadShopInfo();
            } else {
//                updateView();
            }
        }

        if (getIntent() != null &&
                getIntent().getBooleanExtra(FROM_APP_SHORTCUTS, false)) {
            UnifyTracking.eventJualLongClick();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(loginReceiver, new IntentFilter(LOGIN_ACTION));
    }


    private void initFacadeAndLoadShopInfo() {
        initFacadeGet();
        loadShopInfo();
    }

    private void initModel() {
        shopModel = new com.tokopedia.core.shopinfo.models.shopmodel.ShopModel();
        shopModel.info = new Info();
        if (getIntent().getExtras() != null) {
            Bundle extra = getIntent().getExtras();
            shopModel.info.shopDomain = extra.getString(SHOP_DOMAIN, "");
            shopModel.info.shopId = extra.getString(SHOP_ID, "");
            shopModel.info.shopName = extra.getString(SHOP_NAME, "");
            shopModel.info.shopAvatar = extra.getString(SHOP_AVATAR, "");
            shopModel.info.shopIsOwner = (shopModel.info.shopId.equals(SessionHandler.getShopID(this))) ? 1 : 0;
            shopModel.info.shopAlreadyFavorited = extra.getInt(SHOP_FAVORITE, 0);
            shopModel.info.shopCover = extra.getString(SHOP_COVER, "");
        }
    }

    private void loadSavedModel(Bundle savedInstanceState) {
        shopInfoString = savedInstanceState.getParcelable("shop_info");
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("shop_info", shopInfoString);
    }

    @Override
    protected void onDestroy() {
        clearVariable();
        super.onDestroy();
        facadeAction.cancelToggleFav();
        facadeGetRetrofit.cancelGetShopInfo();
        unregisterReceiver(loginReceiver);
    }

    @Override
    public boolean isOfficialStore() {
        if (shopModel != null && shopModel.info != null) {
            return shopModel.info.shopIsOfficial == 1;
        }

        return false;
    }

    private void clearVariable() {
        shopModel = null;
    }

    private void initFacadeGet() {
        facadeGetRetrofit = new GetShopInfoRetrofit(this, shopModel.info.shopId, shopModel.info.shopDomain);
        facadeGetRetrofit.setGetShopInfoListener(onGetShopInfoRetro());
        facadeAction = new ActionShopInfoRetrofit(this, shopModel.info.shopId, shopModel.info.shopDomain, adKey);
        facadeAction.setOnActionToggleFavListener(onActionToggleFav());
    }

    private ActionShopInfoRetrofit.OnActionToggleFavListener onActionToggleFav() {
        return new ActionShopInfoRetrofit.OnActionToggleFavListener() {
            @Override
            public void onSuccess() {
                TrackingUtils.sendMoEngageFavoriteEvent(shopModel);
                if (shopModel.info.shopAlreadyFavorited == 1) {
                    if (getApplication() instanceof IReactNativeRouter) {
                        IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getApplication();
                        reactNativeRouter.sendRemoveFavoriteEmitter(String.valueOf(shopModel.info.shopId), SessionHandler.getLoginID(ShopInfoActivity.this));
                    }
                } else {
                    if (getApplication() instanceof IReactNativeRouter) {
                        IReactNativeRouter reactNativeRouter = (IReactNativeRouter) getApplication();
                        reactNativeRouter.sendAddFavoriteEmitter(String.valueOf(shopModel.info.shopId), SessionHandler.getLoginID(ShopInfoActivity.this));
                    }
                }
                shopModel.info.shopAlreadyFavorited = (shopModel.info.shopAlreadyFavorited + 1) % 2;
                updateIsFavoritedIntent(shopModel.info.shopAlreadyFavorited != 0);
                setShopAlreadyFavorite();
                holder.favorite.clearAnimation();
                showToggleFavoriteSuccess(shopModel.info.shopName, shopModel.info.shopAlreadyFavorited != 0);
            }

            @Override
            public void onFailure(String error) {
                holder.favorite.clearAnimation();
                NetworkErrorHelper.showSnackbar(ShopInfoActivity.this, error);
            }
        };
    }

    @SuppressWarnings("Range")
    public void showToggleFavoriteSuccess(String shopName, boolean favorited) {
        String message;
        if (favorited) {
            message = getResources().getString(R.string.add_favorite_success_message)
                    .replace("$1", shopName);
        } else {
            message = getResources().getString(R.string.remove_favorite_success_message)
                    .replace("$1", shopName);
        }
        SnackbarManager.make(this, message, Snackbar.LENGTH_LONG).show();
    }

    private void updateIsFavoritedIntent(boolean shopAlreadyFavorited) {
        Intent resultIntent = new Intent();
        resultIntent.putExtra(SHOP_STATUS_IS_FAVORITED, shopAlreadyFavorited);
        resultIntent.putExtra(FAVORITE_STATUS_UPDATED, true);
        setResult(RESULT_CANCELED, resultIntent);
    }

    private GetShopInfoRetrofit.OnGetShopInfoListener onGetShopInfoRetro() {
        return new GetShopInfoRetrofit.OnGetShopInfoListener() {
            @Override
            public void onSuccess(String result) {
                showShopInfo();
                shopModel = new Gson().fromJson(result, com.tokopedia.core.shopinfo.models.shopmodel.ShopModel.class);
                shopInfoString = result;
                try {
                    updateView();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ShopInfoActivity.this.getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                ShopInfoActivity.this.finish();
            }

            @SuppressWarnings("Range")
            @Override
            public void onFailure() {
                if (!checkIsShowingInitialData()) {
                    showRetry();
                } else
                    SnackbarManager
                            .make(
                                    ShopInfoActivity.this,
                                    getString(R.string.error_load_shop),
                                    Snackbar.LENGTH_INDEFINITE
                            )
                            .setAction("Coba lagi", onRetryClick())
                            .show();
                holder.progressBar.setVisibility(View.GONE);
            }
        };
    }

    private boolean checkIsShowingInitialData() {
        return holder.shopName.getText().length() > 0;
    }

    private View.OnClickListener onRetryClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showProgress();
                facadeGetRetrofit.getShopInfo();
            }
        };
    }

    private void showRetry() {
        NetworkErrorHelper.showEmptyState(this, holder.progressView, new NetworkErrorHelper.RetryClickedListener() {
            @Override
            public void onRetryClicked() {
                showProgress();
                facadeGetRetrofit.getShopInfo();
            }
        });
        holder.progressBar.setVisibility(View.GONE);
    }

    private void showProgress() {
        NetworkErrorHelper.removeEmptyState(holder.progressView);
        holder.progressBar.setVisibility(View.VISIBLE);
    }

    private void initVar() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null && bundle.getString(SHOP_AD_KEY, "") != null) {
            adKey = bundle.getString(SHOP_AD_KEY, "");
        }

        animateFav = new ScaleAnimation(1, 1.25f, 1, 1.25f, Animation.RELATIVE_TO_SELF, 0.50f, Animation.RELATIVE_TO_SELF, 0.50f);
        animateFav.setDuration(250);
        animateFav.setRepeatCount(Animation.INFINITE);
        animateFav.setRepeatMode(Animation.REVERSE);
        animateFav.setFillAfter(false);
    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {TODO
//        getMenuInflater().inflate(R.menu.menu_shop_info, menu);
//        return true;
//    case R2.shopId.action_share:
//            ShareSocmedHandler.ShareIntent(getActivity(), getString(R.string.message_share_shop), ShopUrl);
//    return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void initView() {
        holder = new ViewHolder();
        holder.pager = (NonSwipeableViewPager) findViewById(R.id.view_pager);
        holder.indicator = (TabLayout) findViewById(R.id.indicator);
        holder.banner = (ImageView) findViewById(R.id.banner);
        holder.shopName = (TextView) findViewById(R.id.shop_name);
        holder.goldShop = (ImageView) findViewById(R.id.gold_shop);
        holder.shopAvatar = (ImageView) findViewById(R.id.shop_avatar);
        holder.luckyShop = (ImageView) findViewById(R.id.lucky_shop);
        holder.freeReturns = (ImageView) findViewById(R.id.free_return_shop);
        holder.favorite = (ImageView) findViewById(R.id.favorite);
        holder.badges = (LinearLayout) findViewById(R.id.badges);
        holder.closeNotice = (TextView) findViewById(R.id.close_notice_title);
        holder.message = findViewById(R.id.message);
//        holder.more = findViewById(R.id.more);
        holder.setting = findViewById(R.id.setting);
        holder.collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar);
        holder.toolbar = (Toolbar) findViewById(R.id.toolbar);
        holder.appBarLayout = (AppBarLayout) findViewById(R.id.app_bar_layout);
        holder.progressBar = findViewById(R.id.progress_bar);
        holder.progressView = findViewById(R.id.progress_view);
        holder.infoShop = findViewById(R.id.info_shop);
        holder.more = findViewById(R.id.more);
        holder.shareBut = findViewById(R.id.share_but);
        holder.location = (TextView) findViewById(R.id.location);
        setSupportActionBar(holder.toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        initToolbar();
        initPager();
        initInitialData();
    }

    private void initToolbar() {
        if (Build.VERSION.SDK_INT >= 21) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                getWindow().setStatusBarColor(getResources().getColor(R.color.green_600));
            }
        }
    }

    private boolean isShopValid() {
        return shopModel != null &&
                shopModel.info != null &&
                shopModel.info.shopName != null &&
                !TextUtils.isEmpty(shopModel.info.shopName);
    }

    private void initInitialData() {
        if (isShopValid()) {
            showShopInfo();
            shopModel.info.shopName = MethodChecker.fromHtml(shopModel.info.shopName).toString();
            holder.shopName.setText(shopModel.info.shopName);
            ImageHandler.loadImageCircle2(this, holder.shopAvatar, shopModel.info.shopAvatar);
            if (!shopModel.info.shopCover.isEmpty()) {
                holder.goldShop.setVisibility(View.VISIBLE);
                if (shopModel.info.shopIsOfficial == 1) {
                    holder.goldShop.setImageResource(R.drawable.ic_badge_official);
                } else {
                    holder.goldShop.setImageResource(R.drawable.ic_shop_gold);
                }
                ImageHandler.loadImageCover2(holder.banner, shopModel.info.shopCover);
            } else
                holder.infoShop.setBackgroundResource(0);
            showShopAction();
            setShopAlreadyFavorite();
        }
    }

    private void initPager() {
//        ShopTabPagerAdapter adapter = ShopTabPagerAdapter.createAdapter(getFragmentManager(), this, shopModel);
        adapter = new ShopTabPagerAdapter(getSupportFragmentManager(), this, shopModel);
        holder.pager.setAdapter(adapter);
    }

    private void loadShopInfo() {
        facadeGetRetrofit.getShopInfo();
    }

    private void updateView() throws Exception {
        facadeAction.setShopModel(shopModel);
        if (shopModel.info.shopIsOfficial == 1) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                adapter.initOfficialShop(shopModel);
            } else {
                adapter.initRegularShop(shopModel);
            }
        } else {
            adapter.initRegularShop(shopModel);
        }
        for (String title : ShopTabPagerAdapter.TITLES)
            holder.indicator.addTab(holder.indicator.newTab().setText(title));
        holder.indicator.setupWithViewPager(holder.pager);
        holder.pager.addOnPageChangeListener(new
                TabLayout.TabLayoutOnPageChangeListener(holder.indicator));
        holder.indicator.setOnTabSelectedListener(new GlobalMainTabSelectedListener(this, holder.pager));
        shopModel.info.shopName = MethodChecker.fromHtml(shopModel.info.shopName).toString();
        setListener();
        holder.collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);
        holder.collapsingToolbarLayout.setExpandedTitleColor(Color.TRANSPARENT);

        if (holder.shopAvatar.getDrawable() == null)
            ImageHandler.loadImageCircle2(this, holder.shopAvatar, shopModel.info.shopAvatar);
        ImageHandler.loadImageLucky2(this, holder.luckyShop, shopModel.info.shopLucky);
        setFreeReturn(holder, shopModel.info);
        holder.shopName.setText(MethodChecker.fromHtml(shopModel.info.shopName));
        if (shopModel.info.shopIsOfficial == SHOP_OFFICIAL_VALUE) {
            holder.location.setText(getResources().getString(R.string.authorized));
        } else {
            holder.location.setText(shopModel.info.shopLocation);
        }
        holder.location.setVisibility(View.VISIBLE);
        holder.collapsingToolbarLayout.setTitle(" ");
        if (shopModel.info.shopIsOfficial == 1) {
            showOfficialCover();
            holder.indicator.setTabMode(TabLayout.MODE_SCROLLABLE);
        } else {
            showGoldCover();
            holder.indicator.setTabMode(TabLayout.MODE_FIXED);
        }
        showShopAction();
        setShopAlreadyFavorite();
        showNotice();
        ReputationLevelUtils.setReputationMedals(this, holder.badges, shopModel.stats.shopBadgeLevel.set, shopModel.stats.shopBadgeLevel.level, shopModel.stats.shopReputationScore);
        getIntent().putExtra(SHOP_AVATAR, shopModel.info.shopAvatar);

        Intent intent = getIntent();
        if (intent.getBooleanExtra(DeepLink.IS_DEEP_LINK, false)) {
            actionFirstLaunched(intent.getExtras());
        }

        if (shopModel.info.shopIsOfficial == 1) {
            ScreenTracking.eventOfficialStoreScreenAuth(shopModel.info.shopId, AppScreen.SCREEN_OFFICIAL_STORE);
        }

        // switch to product tab if ETALASE_NAME not empty
        if (intent.getStringExtra(ETALASE_NAME) != null) {
            holder.pager.setCurrentItem(shopModel.info.shopIsOfficial == 1 ? 1 : 0, true);
        }

        if (intent.getStringExtra(KEYWORD) != null) {
            ProductList productListFragment = (ProductList) adapter.getItem(shopModel.info.shopIsOfficial == 1 ? 1 : 0);
            productListFragment.refreshProductListByKeyword(getIntent().getStringExtra(KEYWORD));

            holder.pager.setCurrentItem(shopModel.info.shopIsOfficial == 1 ? 1 : 0, true);
        }

        if (shopModel.info.shopIsOfficial == 1) {
            ScreenTracking.eventOfficialStoreScreenAuth(shopModel.info.shopId, AppScreen.SCREEN_OFFICIAL_STORE);
        }
    }

    private void setFreeReturn(ViewHolder holder, Info data) {
        if (data.badges != null) {
            List<Badge> badges = data.badges;
            for (int i = 0; i < badges.size(); i++) {
                Badge badge = badges.get(i);
                if (badge.getTitle().equals("Free Returns")) {
                    LuckyShopImage.loadImage(holder.freeReturns, badge.getImageUrl());
                }
            }
        }
    }

    private void showNotice() {
        switch (shopModel.info.shopStatus) {
            case 0:
                holder.closeNotice.setVisibility(View.VISIBLE);
                holder.closeNotice.setText(compileShopStatus());
                break;
            case 2:
                holder.closeNotice.setVisibility(View.VISIBLE);
                holder.closeNotice.setText(compileCloseNote());
                break;
            case 3:
                holder.closeNotice.setVisibility(View.VISIBLE);
                holder.closeNotice.setText(compileShopStatus());
                break;
            case 4:
                holder.closeNotice.setVisibility(View.VISIBLE);
                holder.closeNotice.setText(compileShopStatus());
                break;
            case 5:
                holder.closeNotice.setVisibility(View.VISIBLE);
                holder.closeNotice.setText(compileShopStatus());
                break;
        }
    }

    private void showGoldCover() {
        if (shopModel.info.shopIsGold == 0) {
            holder.infoShop.setBackgroundResource(0);
        } else {
            ImageHandler.loadImageCover2(holder.banner, shopModel.info.shopCover);
            holder.infoShop.setBackgroundResource(R.drawable.cover_shader);
            if (shopModel.info.shopIsGoldBadge)
                holder.goldShop.setVisibility(View.VISIBLE);
        }

    }

    private void showOfficialCover() {
        if (shopModel.info.shopIsOfficial == 0) {
            holder.goldShop.setVisibility(View.INVISIBLE);
            holder.infoShop.setBackgroundResource(0);
        } else {
            holder.goldShop.setVisibility(View.VISIBLE);
            holder.goldShop.setImageResource(R.drawable.ic_badge_official);
            ImageHandler.loadImageCover2(holder.banner, shopModel.info.shopCover);
            holder.infoShop.setBackgroundResource(R.drawable.cover_shader);
        }
    }

    private void setShopAlreadyFavorite() {
        if (shopModel.info.shopAlreadyFavorited == 0) {
            holder.favorite.setImageResource(R.drawable.icon_favorite_white);
        } else {
            holder.favorite.setImageResource(R.drawable.icon_favorite_white_full);
        }
    }

    private void showShopAction() {
        if (shopModel.info.shopIsOwner == 0) {
            holder.setting.setVisibility(View.GONE);
            holder.favorite.setVisibility(View.VISIBLE);
            holder.message.setVisibility(View.VISIBLE);
            holder.setting.setOnClickListener(null);
        } else {
            holder.setting.setVisibility(View.VISIBLE);
            holder.favorite.setVisibility(View.GONE);
            holder.message.setVisibility(View.GONE);
            holder.favorite.setOnClickListener(null);
        }
    }

    private void showShopInfo() {
        holder.appBarLayout.setVisibility(View.VISIBLE);
        holder.pager.setVisibility(View.VISIBLE);
        holder.progressView.setVisibility(View.GONE);
    }

    private Spanned compileCloseNote() {
        String closeNote = "<b>" + getResources().getString(R.string.title_currently_closed) + "</b>" + "<br>"
                + getResources().getString(R.string.title_until) + " <b>" + shopModel.closedInfo.until + "</b>" + "<br>"
                + getResources().getString(R.string.title_reason) + " <b>" + shopModel.closedInfo.note + "</b>";
        return MethodChecker.fromHtml(closeNote);
    }

    private Spanned compileShopStatus() {
        String inactiveNote = "<b>" + shopModel.info.shopStatusTitle + "</b>" + "<br>"
                + shopModel.info.shopStatusMessage;
        return MethodChecker.fromHtml(inactiveNote);
    }

    private void setListener() {
        holder.favorite.setOnClickListener(onActionFavorite());
        holder.message.setOnClickListener(onActionSendMessage());
        holder.more.setOnClickListener(onActionMore());
        holder.shopName.setOnClickListener(onActionMore());
        holder.setting.setOnClickListener(onSettingClick());
        holder.shareBut.setOnClickListener(onShareShop());
        holder.appBarLayout.addOnOffsetChangedListener(onAppbarOffsetChange());
    }

    private View.OnClickListener onShareShop() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShareData shareData = ShareData.Builder.aShareData()
                        .setType(ShareData.SHOP_TYPE)
                        .setName(getString(R.string.message_share_shop))
                        .setTextContent(compileShare())
                        .setUri(shopModel.info.shopUrl)
                        .setId(shopModel.info.shopId)
                        .build();
                startActivity(ShareActivity.createIntent(ShopInfoActivity.this, shareData));
            }
        };
    }

    private String compileShare() {
        String share = "";
        share = shopModel.info.shopName + " - " + shopModel.info.shopLocation + " | Tokopedia \n";
        return share;
    }

    private AppBarLayout.OnOffsetChangedListener onAppbarOffsetChange() {
        return new AppBarLayout.OnOffsetChangedListener() {
            boolean isShow = false;
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (shopModel == null)
                    return;
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                if (scrollRange + verticalOffset == 0) {
                    holder.collapsingToolbarLayout.setTitle(shopModel.info.shopName);
                    isShow = true;
                } else if (isShow) {
                    holder.collapsingToolbarLayout.setTitle("");
                    isShow = false;
                }
            }
        };
    }

    private View.OnClickListener onSettingClick() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionSettingShop();
            }
        };
    }

    private void actionSettingShop() {
        Intent intent = SellerRouter.getActivityManageShop(this);
        startActivity(intent);
    }

    private View.OnClickListener onActionMore() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                actionViewMore();
            }
        };
    }

    private View.OnClickListener onActionFavorite() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (SessionHandler.isV4Login(ShopInfoActivity.this)) {
                    holder.favorite.startAnimation(animateFav);
                    facadeAction.actionToggleFav();
                } else {
                    Intent intent = OldSessionRouter.getLoginActivityIntent(ShopInfoActivity.this);
                    intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                            TkpdState.DrawerPosition.LOGIN);
                    startActivityForResult(intent, ShopInfoActivity.FAVORITE_LOGIN_REQUEST_CODE);
                }
            }
        };
    }

    private View.OnClickListener onActionSendMessage() {
        return new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startSendMessageIntent();
            }
        };
    }

    private void actionViewMore() {
        Intent intent = new Intent(this, ShopInfoMore.class);
        intent.putExtra("shop_info", shopInfoString);
        startActivity(intent);
    }

    private void startSendMessageIntent() {
        Intent intent;
        Bundle bundle = new Bundle();
        if (SessionHandler.isV4Login(this)) {
            if (MainApplication.getAppContext() instanceof TkpdInboxRouter) {
                UnifyTracking.eventShopSendChat();
                intent = ((TkpdInboxRouter) MainApplication.getAppContext())
                        .getAskSellerIntent(this,
                                shopModel.info.shopId,
                                shopModel.info.shopName,
                                TkpdInboxRouter.SHOP,
                                shopModel.getInfo().getShopAvatar());
                startActivity(intent);
            }
        } else {
            bundle.putBoolean("login", true);
            intent = OldSessionRouter.getLoginActivityIntent(this);
            intent.putExtra(Session.WHICH_FRAGMENT_KEY, TkpdState.DrawerPosition.LOGIN);
            bundle.putString(InboxRouter.PARAM_SHOP_ID, shopModel.info.shopId);
            bundle.putString(PARAM_OWNER_FULLNAME, shopModel.info.shopName);
            intent.putExtras(bundle);
            startActivityForResult(intent, REQ_RELOAD);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(getClass().getSimpleName(), "onActivityResult requestCode " + requestCode + " resultCode " + resultCode);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case REQ_RELOAD:
                    initFacadeAndLoadShopInfo();
                    break;
                case REQUEST_CODE_LOGIN:
                    if (!TextUtils.isEmpty(redirectionUrl)) {
                        String encodedUrl = encodeUrl(redirectionUrl);
                        if (encodedUrl != null) {
                            openWebView(URLGenerator.generateURLSessionLoginV4(encodedUrl, this));
                        }
                    }
                    break;
            }
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
    }

    public void setToolbarCollapse() {
        holder.appBarLayout.setExpanded(false, true);
    }

    @Override
    public void OnProductListPageRedirected(GetShopProductParam productParam) {
        if (!productParam.getEtalaseId().equalsIgnoreCase("all")) {
            ProductList productListFragment = (ProductList) adapter.getItem(1);
            productListFragment.refreshProductList(productParam);
        } else {
            ProductList productListFragment = (ProductList) adapter.getItem(1);
            productListFragment.refreshProductListFromOffStore();
        }
        holder.pager.setCurrentItem(1, true);
    }

    @Override
    public void OnProductInfoPageRedirected(String productId) {
        Intent intent = ProductDetailRouter
                .createInstanceProductDetailInfoActivity(this, productId);
        startActivity(intent);
    }

    @Override
    public void OnWebViewPageRedirected(String url) {
        if (isNeededToLogin(url)) {
            if (SessionHandler.isV4Login(this)) {
                String encodedUrl = encodeUrl(url);
                if (encodedUrl != null) {
                    openWebView(URLGenerator.generateURLSessionLoginV4(encodedUrl, this));
                }
            } else {
                redirectionUrl = url;
                Intent intent = OldSessionRouter.getLoginActivityIntent(this);
                intent.putExtra(Session.WHICH_FRAGMENT_KEY,
                        TkpdState.DrawerPosition.LOGIN);
                startActivityForResult(intent, REQUEST_CODE_LOGIN);
            }
        } else {
            openWebView(url);
        }


    }

    @Override
    public void onProductListCompleted() {
        if (shopModel.info.shopIsOfficial == 1) {
            OfficialShopHomeFragment fragment = (OfficialShopHomeFragment) adapter.getItem(0);
            fragment.onRefreshProductData();
        }
    }

    private String encodeUrl(String url) {
        String encodedUrl;
        try {
            encodedUrl = URLEncoder.encode(url, FORMAT_UTF_8);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return null;
        }
        return encodedUrl;
    }

    private boolean isNeededToLogin(String url) {
        switch (Uri.parse(url).getHost()) {
            case URL_RECHARGE_HOST:
                return true;
        }
        return false;
    }

    private void openWebView(String url) {
        CommonUtils.dumper(url);
        Intent intent = new Intent(this, BannerWebView.class);
        intent.putExtra(BannerWebView.EXTRA_URL, url);
        startActivity(intent);
    }

    private void actionFirstLaunched(Bundle extras) {
        if (shopModel.info.shopIsOfficial == 1) {
            switch (extras.getInt(EXTRA_STATE_TAB_POSITION, 0)) {
                case TAB_POSITION_HOME:
                    holder.pager.setCurrentItem(0, true);
                    break;
                case TAB_POSITION_ETALASE:
                    holder.pager.setCurrentItem(1, true);
                    break;
                case TAB_POSITION_TALK:
                    holder.pager.setCurrentItem(2, true);
                    break;
                case TAB_POSITION_REVIEW:
                    holder.pager.setCurrentItem(3, true);
                    break;
                case TAB_POSITION_NOTE:
                    holder.pager.setCurrentItem(4, true);
                    break;
                case NAVIGATION_TO_INFO:
                    actionViewMore();
                    break;
                default:
                    break;
            }
        } else {
            switch (extras.getInt(EXTRA_STATE_TAB_POSITION, 0)) {
                case TAB_POSITION_HOME:
                case TAB_POSITION_ETALASE:
                    holder.pager.setCurrentItem(0, true);
                    break;
                case TAB_POSITION_TALK:
                    holder.pager.setCurrentItem(1, true);
                    break;
                case TAB_POSITION_REVIEW:
                    holder.pager.setCurrentItem(2, true);
                    break;
                case TAB_POSITION_NOTE:
                    holder.pager.setCurrentItem(3, true);
                    break;
                case NAVIGATION_TO_INFO:
                    actionViewMore();
                    break;
            }
        }

    }

    @Override
    public void onBackPressed() {
        if (isTaskRoot() ||
                (getIntent().getExtras() != null &&
                        getIntent().getExtras().getBoolean(Constants.EXTRA_APPLINK_FROM_PUSH, false))) {
            Intent homeIntent = ((TkpdCoreRouter) getApplication()).getHomeIntent(this);
            startActivity(homeIntent);
            finish();
        } else {
            super.onBackPressed();
        }
    }

    public void swipeAble(boolean able) {
        if (holder != null && holder.pager != null) {
            holder.pager.setSwipeAble(able);
        }
    }

}
