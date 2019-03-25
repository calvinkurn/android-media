package com.tokopedia.tokopoints.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.ColorInt;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.gamification.applink.ApplinkConstant;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager;
import com.tokopedia.tokopoints.view.activity.CatalogListingActivity;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.activity.SendGiftActivity;
import com.tokopedia.tokopoints.view.activity.TokoPointsHomeActivity;
import com.tokopedia.tokopoints.view.adapter.ExploreSectionPagerAdapter;
import com.tokopedia.tokopoints.view.adapter.SectionCategoryAdapter;
import com.tokopedia.tokopoints.view.adapter.TickerPagerAdapter;
import com.tokopedia.tokopoints.view.contract.TokoPointsHomeContract;
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.TokoPointEntity;
import com.tokopedia.tokopoints.view.model.TokoPointSumCoupon;
import com.tokopedia.tokopoints.view.model.section.SectionContent;
import com.tokopedia.tokopoints.view.presenter.TokoPointsHomePresenterNew;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.TabUtil;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class TokoPointsHomeFragmentNew extends BaseDaggerFragment implements TokoPointsHomeContract.View, View.OnClickListener {

    private static final String FPM_TOKOPOINT = "ft_tokopoint";
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private static final int TAB_CATALOG = 0;
    private static final int TAB_COUPON = 1;
    private ViewFlipper mContainerMain;
    private TextView mTextMembershipValue, mTextMembershipValueBottom, mTextPoints, mTextPointsBottom, mTextLoyalty;
    private TextView mTextMembershipLabel;
    private ImageView mImgEgg, mImgEggBottom, mImgBackground;
    private TabLayout mTabLayoutPromo;
    private ViewPager mPagerPromos;
    private LinearLayout bottomViewMembership;
    private AppBarLayout appBarHeader;
    private RecyclerView mRvDynamicLinks;
    @Inject
    public TokoPointsHomePresenterNew mPresenter;

    private int mSumToken;
    private int mCouponCount;
    private String mValueMembershipDescription;

    StartPurchaseBottomSheet mStartPurchaseBottomSheet;
    private View tickerContainer;
    private View dynamicLinksContainer;
    private LinearLayout containerEgg;
    private onAppBarCollapseListener appBarCollapseListener;
    private ExploreSectionPagerAdapter mExploreSectionPagerAdapter;
    private PerformanceMonitoring performanceMonitoring;
    private Toolbar toolbar;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private CoordinatorLayout coordinatorLayout;

    private View statusBarBgView;


    public static TokoPointsHomeFragmentNew newInstance() {
        return new TokoPointsHomeFragmentNew();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        performanceMonitoring = PerformanceMonitoring.start(FPM_TOKOPOINT);
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_homepage_new, container, false);

        coordinatorLayout = view.findViewById(R.id.container);
        hideStatusBar();


        toolbar = view.findViewById(R.id.toolbar);
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));

        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(" ");

        initViews(view);

        appBarHeader.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                handleAppBarOffsetChange(verticalOffset);
            }
        });

        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) toolbar.getLayoutParams();
        layoutParams.topMargin = getStatusBarHeight(getActivity());
        toolbar.setLayoutParams(layoutParams);

        return view;
    }

    public static int getStatusBarHeight(Context context) {
        int height = 0;
        int resId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resId > 0) {
            height = context.getResources().getDimensionPixelSize(resId);
        }
        return height;
    }

    private void hideStatusBar() {
        coordinatorLayout.setFitsSystemWindows(false);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT_WATCH) {
            coordinatorLayout.requestApplyInsets();
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            int flags = coordinatorLayout.getSystemUiVisibility();
            flags |= View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR;
            coordinatorLayout.setSystemUiVisibility(flags);
            getActivity().getWindow().setStatusBarColor(Color.WHITE);
        }

        if (Build.VERSION.SDK_INT >= 19 && Build.VERSION.SDK_INT < 21) {
            setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, true);
        }

        if (Build.VERSION.SDK_INT >= 19) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= 21) {
            setWindowFlag(getActivity(), WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS, false);
            getActivity().getWindow().setStatusBarColor(Color.TRANSPARENT);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }


    AppBarLayout.OnOffsetChangedListener offsetChangedListenerBottomView = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            verticalOffset = Math.abs(verticalOffset);
            if (verticalOffset >= appBarLayout.getTotalScrollRange() - tickerContainer.getHeight() - dynamicLinksContainer.getHeight()) {
                slideUp();
            } else {
                slideDown();
            }
        }
    };

    boolean isDarkToolbar;

    private void handleAppBarOffsetChange(int offset) {

        int positiveOffset = offset * -1;

        int searchBarTransitionRange =
                getResources().getDimensionPixelSize(R.dimen.tp_home_top_bg_height) - toolbar.getHeight() - getStatusBarHeight(getActivity());
        float offsetAlpha =
                (255f / searchBarTransitionRange) * (searchBarTransitionRange - positiveOffset);
        if (offsetAlpha < 0) {
            offsetAlpha = 0;
        }

        if (offsetAlpha >= 255) {
            offsetAlpha = 255;
        }

        float alpha = offsetAlpha / 255 - 1;
        if (alpha < 0)
            alpha = alpha * -1;
        statusBarBgView.setAlpha(alpha);
        if (alpha > 0.5)
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back_grey));
        else
            toolbar.setNavigationIcon(ContextCompat.getDrawable(getActivity(), R.drawable.ic_action_back));
        toolbar.setBackgroundColor(
                adjustAlpha(getActivity().getResources().getColor(R.color.white), alpha));
    }

    @ColorInt
    public static int adjustAlpha(@ColorInt int color, float factor) {
        int alpha = Math.round(Color.alpha(color) * factor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        return Color.argb(alpha, red, green, blue);
    }

    AppBarLayout.OnOffsetChangedListener offsetChangedListenerAppBarElevation = new AppBarLayout.OnOffsetChangedListener() {
        @Override
        public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
            if (appBarCollapseListener != null) {
                verticalOffset = Math.abs(verticalOffset);
                if (verticalOffset >= appBarLayout.getTotalScrollRange()) {     //Appbar is hidden now
                    appBarCollapseListener.hideToolbarElevation();
                } else {
                    appBarCollapseListener.showToolbarElevation();
                }
            }
        }
    };

    private void slideUp() {
        if (bottomViewMembership.getVisibility() != View.VISIBLE) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerEgg.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.tp_margin_xxxlarge));
            Animation bottomUp = AnimationUtils.loadAnimation(bottomViewMembership.getContext(),
                    R.anim.tp_bottom_up);
            bottomViewMembership.startAnimation(bottomUp);
            bottomViewMembership.setVisibility(View.VISIBLE);
        }

    }

    private void slideDown() {
        if (bottomViewMembership.getVisibility() != View.GONE) {
            CoordinatorLayout.LayoutParams layoutParams = (CoordinatorLayout.LayoutParams) containerEgg.getLayoutParams();
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(R.dimen.tp_margin_large));
            Animation slideDown = AnimationUtils.loadAnimation(bottomViewMembership.getContext(),
                    R.anim.tp_bottom_down);
            bottomViewMembership.startAnimation(slideDown);
            bottomViewMembership.setVisibility(View.GONE);
        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initListener();
        mPresenter.getTokoPointDetail();
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getAppContext(), CommonConstant.PREF_TOKOPOINTS);
        if (!localCacheHandler.getBoolean(CommonConstant.PREF_KEY_ON_BOARDED)) {
            showOnBoardingTooltip(getString(R.string.tp_label_know_tokopoints), getString(R.string.tp_message_tokopoints_on_boarding));
            localCacheHandler.putBoolean(CommonConstant.PREF_KEY_ON_BOARDED, true);
            localCacheHandler.applyEditor();
        }

        TokoPointsNotificationManager.fetchNotification(getActivity(), "main", getChildFragmentManager());
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        AnalyticsTrackerUtil.sendScreenEvent(getActivity(), getScreenName());
    }

    @Override
    public void showLoading() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void hideLoading() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TokoPointsHomeActivity)
            appBarCollapseListener = (onAppBarCollapseListener) context;
    }

    @Override
    protected String getScreenName() {
        return AnalyticsTrackerUtil.ScreenKeys.HOME_PAGE_SCREEN_NAME;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.text_membership_label || source.getId() == R.id.img_egg || source.getId() == R.id.text_membership_value) {
            ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.MEMBERSHIP, getString(R.string.tp_label_membership));

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_MEMBERSHIP,
                    mValueMembershipDescription);
        } else if (source.getId() == R.id.bottom_view_membership) {
            ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.MEMBERSHIP, getString(R.string.tp_label_membership));

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_MEM_BOTTOM,
                    "");
        } else if (source.getId() == R.id.text_my_points_value_bottom) {
            ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.HISTORY, getString(R.string.tp_history));

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_POINT_SAYA,
                    "");
        } else if (source.getId() == R.id.text_loyalty_value) {
            ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.HISTORY, getString(R.string.tp_history));

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_LOYALTY_SAYA,
                    "");
        } else if (source.getId() == R.id.text_failed_action) {
            mPresenter.getTokoPointDetail();
        }
    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container_main);
        mTextMembershipValue = view.findViewById(R.id.text_membership_value);
        mTextMembershipLabel = view.findViewById(R.id.text_membership_label);
        mTextPoints = view.findViewById(R.id.text_my_points_value);
        mTextLoyalty = view.findViewById(R.id.text_loyalty_value);
        mImgEgg = view.findViewById(R.id.img_egg);
        mTabLayoutPromo = view.findViewById(R.id.tab_layout_promos);
        mPagerPromos = view.findViewById(R.id.view_pager_promos);
        mTextMembershipValueBottom = view.findViewById(R.id.text_membership_value_bottom);
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom);
        mImgEggBottom = view.findViewById(R.id.img_egg_bottom);
        mImgBackground = view.findViewById(R.id.img_bg_header);
        appBarHeader = view.findViewById(R.id.app_bar);
        bottomViewMembership = view.findViewById(R.id.bottom_view_membership);
        tickerContainer = view.findViewById(R.id.cons_ticker_container);
        containerEgg = view.findViewById(R.id.container_fab_egg_token);
        mRvDynamicLinks = view.findViewById(R.id.rv_dynamic_link);
        dynamicLinksContainer = view.findViewById(R.id.container_dynamic_links);
        statusBarBgView = view.findViewById(R.id.status_bar_bg);
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.text_membership_label).setOnClickListener(this);
        getView().findViewById(R.id.img_egg).setOnClickListener(this);
        getView().findViewById(R.id.text_membership_value).setOnClickListener(this);
        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);
        bottomViewMembership.setOnClickListener(this);
        mTextPointsBottom.setOnClickListener(this);
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    @Override
    public void onSuccessTokenDetail(LuckyEggEntity tokenDetail) {
        if (tokenDetail != null) {
            try {
                if (tokenDetail.isOffFlag()) {
                    return;
                }

                containerEgg.setVisibility(View.VISIBLE);
                TextView textCount = getView().findViewById(R.id.text_token_count);
                TextView textMessage = getView().findViewById(R.id.text_token_title);
                ImageView imgToken = getView().findViewById(R.id.img_token);
                textCount.setText(tokenDetail.getSumTokenStr());
                this.mSumToken = tokenDetail.getSumToken();
                textMessage.setText(tokenDetail.getFloating().getTokenClaimCustomText());
                if (tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl().endsWith(".gif")) {
                    ImageHandler.loadGifFromUrl(imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl(), R.color.green_50);
                } else {
                    ImageHandler.loadImageFitCenter(getContext(), imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl());
                }

                if (mSumToken == 0) {
                    textCount.setVisibility(View.GONE);
                    textMessage.setPadding(getResources().getDimensionPixelSize(R.dimen.dp_30),
                            0,
                            0,
                            0);
                } else {
                    textCount.setVisibility(View.VISIBLE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                //to avoid any accidental crash in order to prevent homepage error
            }
        }
    }

    @Override
    public void onError(String error) {
        if (mContainerMain != null) {
            mContainerMain.setDisplayedChild(CONTAINER_ERROR);
        }
    }

    @Override
    public void showRedeemCouponDialog(String cta, String code, String title) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        adb.setTitle(R.string.tp_label_use_coupon);
        StringBuilder messageBuilder = new StringBuilder()
                .append(getString(R.string.tp_label_coupon))
                .append(" ")
                .append("<strong>")
                .append(title)
                .append("</strong>")
                .append(" ")
                .append(getString(R.string.tp_mes_coupon_part_2));
        adb.setMessage(MethodChecker.fromHtml(messageBuilder.toString()));
        adb.setPositiveButton(R.string.tp_label_use, (dialogInterface, i) -> {
            //Call api to validate the coupon
            mPresenter.redeemCoupon(code, cta);

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI_GUNAKAN_KUPON,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                    title);
        });
        adb.setNegativeButton(R.string.tp_label_later, (dialogInterface, i) -> {
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI_GUNAKAN_KUPON,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_NANTI_SAJA,
                    title);
        });
        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    @Override
    public void showConfirmRedeemDialog(String cta, String code, String title) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        adb.setNegativeButton(R.string.tp_label_use, (dialogInterface, i) -> {
            showRedeemCouponDialog(cta, code, title);

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_BERHASIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                    title);
        });

        adb.setPositiveButton(R.string.tp_label_view_coupon, (dialogInterface, i) -> {
            startActivity(MyCouponListingActivity.getCallingIntent(getActivityContext()));

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_BERHASIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_LIHAT_KUPON,
                    "");
        });

        adb.setTitle(R.string.tp_label_successful_exchange);
        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);

        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_BERHASIL,
                AnalyticsTrackerUtil.ActionKeys.VIEW_REDEEM_SUCCESS,
                title);
    }

    @Override
    public void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        String labelPositive;
        String labelNegative = null;

        switch (resCode) {
            case CommonConstant.CouponRedemptionCode.LOW_POINT:
                labelPositive = getString(R.string.tp_label_ok);
                break;
            case CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE:
                labelPositive = getString(R.string.tp_label_complete_profile);
                labelNegative = getString(R.string.tp_label_later);
                break;
            case CommonConstant.CouponRedemptionCode.SUCCESS:
                labelPositive = getString(R.string.tp_label_exchange);
                labelNegative = getString(R.string.tp_label_betal);
                break;
            case CommonConstant.CouponRedemptionCode.QUOTA_LIMIT_REACHED:
                labelPositive = getString(R.string.tp_label_ok);
                break;
            default:
                labelPositive = getString(R.string.tp_label_ok);
        }

        if (title == null || title.isEmpty()) {
            adb.setTitle(R.string.tp_label_exchange_failed);
        } else {
            adb.setTitle(title);
        }

        adb.setMessage(MethodChecker.fromHtml(message));

        if (labelNegative != null && !labelNegative.isEmpty()) {
            adb.setNegativeButton(labelNegative, (dialogInterface, i) -> {
                switch (resCode) {
                    case CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE:
                        AnalyticsTrackerUtil.sendEvent(getContext(),
                                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                                AnalyticsTrackerUtil.CategoryKeys.POPUP_VERIFIED,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_NANTI_SAJA,
                                "");
                        break;
                    case CommonConstant.CouponRedemptionCode.SUCCESS:
                        AnalyticsTrackerUtil.sendEvent(getContext(),
                                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                                AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_BATAL,
                                title);
                        break;
                    default:
                }
            });
        }

        adb.setPositiveButton(labelPositive, (dialogInterface, i) -> {
            switch (resCode) {
                case CommonConstant.CouponRedemptionCode.LOW_POINT:
                    dialogInterface.cancel();
                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_POINT_TIDAK,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_BELANJA,
                            "");
                    break;
                case CommonConstant.CouponRedemptionCode.QUOTA_LIMIT_REACHED:
                    dialogInterface.cancel();

                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_KUOTA_HABIS,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_OK,
                            "");
                    break;
                case CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE:
                    startActivity(new Intent(getAppContext(), ProfileCompletionActivity.class));

                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_VERIFIED,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_INCOMPLETE_PROFILE,
                            "");
                    break;
                case CommonConstant.CouponRedemptionCode.SUCCESS:
                    mPresenter.startSaveCoupon(item);

                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            AnalyticsTrackerUtil.CategoryKeys.POPUP_KONFIRMASI,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                            title);
                    break;
                default:
                    dialogInterface.cancel();
            }
        });

        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    private void decorateDialog(AlertDialog dialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }

        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    R.color.grey_warm));
        }
    }

    private void showOnBoardingTooltip(String title, String content) {
        BottomSheetView mToolTip = new BottomSheetView(getActivityContext());
        mToolTip.renderBottomSheet(new BottomSheetView.BottomSheetField
                .BottomSheetFieldBuilder()
                .setTitle(title)
                .setBody(content)
                .setCloseButton(getString(R.string.tp_label_check_storepoints))
                .build());

        mToolTip.show();

        mToolTip.setBtnCloseOnClick(view -> {
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_CEK,
                    AnalyticsTrackerUtil.EventKeys.TOKOPOINTS_ON_BOARDING_LABEL);
            mToolTip.cancel();
        });

    }

    @Override
    public void showRedeemFullError(CatalogsValueEntity item, String title, String desc) {
        if (getActivity() == null || !isAdded()) {
            return;
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_tp_network_error_large, null, false);

        ImageView img = view.findViewById(R.id.img_error);
        img.setImageResource(R.drawable.ic_tp_error_redeem_full);
        TextView titleText = view.findViewById(R.id.text_title_error);

        if (title == null || title.isEmpty()) {
            titleText.setText(R.string.tp_label_too_many_access);
        } else {
            titleText.setText(title);
        }

        TextView label = view.findViewById(R.id.text_label_error);
        label.setText(desc);

        view.findViewById(R.id.text_failed_action).setOnClickListener(view1 -> mPresenter.startSaveCoupon(item));

        adb.setView(view);
        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    @Override
    public void onPreValidateError(String title, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());

        adb.setTitle(title);
        adb.setMessage(message);

        adb.setPositiveButton(R.string.tp_label_ok, (dialogInterface, i) -> {
                }
        );

        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    public void showStartPurchaseBottomSheet(String title) {
        mStartPurchaseBottomSheet.show(getChildFragmentManager(), title);
    }

    @Override
    public void gotoSendGiftPage(int id, String title, String pointStr) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_COUPON_ID, id);
        bundle.putString(CommonConstant.EXTRA_COUPON_TITLE, title);
        bundle.putString(CommonConstant.EXTRA_COUPON_POINT, pointStr);
        startActivity(SendGiftActivity.getCallingIntent(getActivity(), bundle));
    }

    @Override
    public void showTokoPointCoupon(TokoPointSumCoupon data) {
        TabUtil.wrapTabIndicatorToTitle(mTabLayoutPromo,
                (int) getResources().getDimension(R.dimen.tp_margin_medium),
                (int) getResources().getDimension(R.dimen.tp_margin_regular));

        if (data.getSumCoupon() > 0) {
            TextView counterCoupon = getView().findViewById(R.id.text_count);
            counterCoupon.setVisibility(View.VISIBLE);
            counterCoupon.setText(data.getSumCouponStr());
            mTabLayoutPromo.getTabAt(CommonConstant.MY_COUPON_TAB).setText(R.string.tp_label_my_coupon_space);
            mCouponCount = data.getSumCoupon();
        } else {
            mTabLayoutPromo.getTabAt(CommonConstant.MY_COUPON_TAB).setText(R.string.tp_label_my_coupon);
            TabUtil.removedPaddingAtLast(mTabLayoutPromo,
                    (int) getResources().getDimension(R.dimen.tp_margin_medium));
        }
    }

    @Override
    public void onFinishRendering() {
        if (performanceMonitoring != null)
            performanceMonitoring.stopTrace();
    }


    @Override
    public void renderTicker(SectionContent content) {
        if (getView() == null
                || content == null
                || content.getLayoutTickerAttr() == null
                || content.getLayoutTickerAttr().getTickerList() == null
                || content.getLayoutTickerAttr().getTickerList().isEmpty()) {
            return;
        }

        ViewPager pager = getView().findViewById(R.id.view_pager_ticker);
        pager.setAdapter(new TickerPagerAdapter(getContext(), content.getLayoutTickerAttr().getTickerList()));
        CirclePageIndicator pageIndicator = getView().findViewById(R.id.page_indicator_ticker);
        if (content.getLayoutTickerAttr().getTickerList().size() > 1) {
            //adding bottom dots(Page Indicator)
            pageIndicator.setVisibility(View.VISIBLE);
            pageIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
            pageIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.white_two));
            pageIndicator.setViewPager(pager, 0);
        } else {
            pageIndicator.setVisibility(View.GONE);
        }

        tickerContainer.setVisibility(View.VISIBLE);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                AnalyticsTrackerUtil.sendEvent(getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_TOKOPOINT,
                        AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                        AnalyticsTrackerUtil.ActionKeys.VIEW_TICKER,
                        "");
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public void renderCategory(SectionContent content) {
        if (content == null
                || content.getLayoutCategoryAttr() == null
                || content.getLayoutCategoryAttr().getCategoryTokopointsList() == null
                || content.getLayoutCategoryAttr().getCategoryTokopointsList().isEmpty()) {
            return;
        }

        dynamicLinksContainer.setVisibility(View.VISIBLE);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 5, GridLayoutManager.VERTICAL, false);
        mRvDynamicLinks.setLayoutManager(manager);
        mRvDynamicLinks.setAdapter(new SectionCategoryAdapter(content.getLayoutCategoryAttr().getCategoryTokopointsList()));
    }

    @Override
    public void renderToolbarWithHeader(TokoPointEntity data) {
        //todo lalit

        if (data == null) {
            return; //TODO any error page? Ask from gulfikar
        }

        //init header
        if (data.getStatus() != null) {
            mTextMembershipLabel.setText(data.getStatus().getUserName());

            if (data.getStatus().getTier() != null) {
                mValueMembershipDescription = data.getStatus().getTier().getNameDesc();
                mTextMembershipValue.setText(mValueMembershipDescription);
                mTextMembershipValueBottom.setText(mValueMembershipDescription);
                ImageHandler.loadImageCircle2(getActivityContext(), mImgEgg, data.getStatus().getTier().getEggImageHomepageURL());
                ImageHandler.loadImageCircle2(getActivityContext(), mImgEggBottom, data.getStatus().getTier().getEggImageUrl());
            }

            if (data.getStatus().getPoints() != null) {
                ImageHandler.loadImageCircle2(getActivityContext(), mImgBackground, data.getStatus().getPoints().getBackgroundImgURLMobile());
                mTextPoints.setText(CurrencyFormatUtil.convertPriceValue(data.getStatus().getPoints().getReward(), false));
                mTextPointsBottom.setText(CurrencyFormatUtil.convertPriceValue(data.getStatus().getPoints().getReward(), false));
                mTextLoyalty.setText(CurrencyFormatUtil.convertPriceValue(data.getStatus().getPoints().getLoyalty(), false));
            }
        }

        //init bottom sheet
        renderPurchaseBottomsheet(data.getLobs());
    }


    public void renderPurchaseBottomsheet(LobDetails data) {
        if (data == null || getView() == null) {
            return;
        }

        if (mStartPurchaseBottomSheet == null) {
            mStartPurchaseBottomSheet = new StartPurchaseBottomSheet();
        }

        mStartPurchaseBottomSheet.setData(data);

        getView().findViewById(R.id.img_token).setOnClickListener(view -> {
            if (mSumToken <= 0) {
                showStartPurchaseBottomSheet(data.getTitle());
            } else {
                if (getActivity() != null) {
                    RouteManager.route(getActivity(), ApplinkConstant.GAMIFICATION);
                }
            }

            AnalyticsTrackerUtil.sendEvent(getActivity(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_FLOATING_LUCKY,
                    "");
        });

        getView().findViewById(R.id.text_token_title).setOnClickListener(view -> {
            if (mSumToken <= 0) {
                showStartPurchaseBottomSheet(data.getTitle());
            } else {
                if (getActivity() != null) {
                    RouteManager.route(getActivity(), ApplinkConstant.GAMIFICATION);
                }
            }

            AnalyticsTrackerUtil.sendEvent(view.getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_EGG,
                    "");
        });
    }

    @Override
    public void renderSections(List<SectionContent> sections) {
        if (sections == null) {
            //TODO hide all section container
            return;
        }

        List<SectionContent> exploreSectionItem = new ArrayList<>();
        SectionContent couponSection = null;

        for (SectionContent sectionContent : sections) {
            switch (sectionContent.getLayoutType()) {
                case CommonConstant.SectionLayoutType.TICKER:
                    renderTicker(sectionContent);
                    break;
                case CommonConstant.SectionLayoutType.CATEGORY:
                    renderCategory(sectionContent);
                    break;
                case CommonConstant.SectionLayoutType.COUPON:
                    couponSection = sectionContent;
                    break;
                case CommonConstant.SectionLayoutType.CATALOG:
                case CommonConstant.SectionLayoutType.BANNER:
                    exploreSectionItem.add(sectionContent);
                    break;
                default:
                    break;

            }
        }

        //init explore and kupon-saya tab
        renderExploreSectionTab(exploreSectionItem, couponSection);
    }

    @Override
    public void gotoCoupons() {
        startActivity(MyCouponListingActivity.getCallingIntent(getActivityContext()));
    }

    @Override
    public void gotoCatalog() {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_COUPON_COUNT, mCouponCount);
        startActivity(CatalogListingActivity.getCallingIntent(getActivityContext(), bundle));
    }

    public void renderExploreSectionTab(List<SectionContent> sections, SectionContent couponSection) {
        if (sections.isEmpty()) {
            //TODO hide tab or show empty box
        }

        mExploreSectionPagerAdapter = new ExploreSectionPagerAdapter(getActivityContext(), mPresenter, sections, couponSection);
        mExploreSectionPagerAdapter.setRefreshing(false);
        mPagerPromos.setAdapter(mExploreSectionPagerAdapter);
        mPagerPromos.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayoutPromo));
        mTabLayoutPromo.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPagerPromos));

        mPagerPromos.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    appBarHeader.addOnOffsetChangedListener(offsetChangedListenerBottomView);
                    mPresenter.setPagerSelectedItem(position);

                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_PENUKARAN,
                            "");
                } else {
                    appBarHeader.removeOnOffsetChangedListener(offsetChangedListenerBottomView);
                    slideDown();
                    mPresenter.setPagerSelectedItem(position);

                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_KUPON_SAYA,
                            "");
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        if (couponSection.getLayoutCouponAttr().getCouponList().isEmpty()) {
            mPresenter.setPagerSelectedItem(TAB_CATALOG);
        } else {
            mPresenter.setPagerSelectedItem(TAB_COUPON);
        }

        mPagerPromos.setCurrentItem(mPresenter.getPagerSelectedItem());

    }

    public void onSuccessResponse(TokoPointEntity data, List<SectionContent> sections) {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);

        renderToolbarWithHeader(data);
        renderSections(sections);
    }

}
