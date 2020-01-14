package com.tokopedia.tokopoints.view.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
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
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal;
import com.tokopedia.promogamification.common.applink.ApplinkConstant;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.notification.TokoPointsNotificationManager;
import com.tokopedia.tokopoints.notification.model.PopupNotification;
import com.tokopedia.tokopoints.view.activity.CouponListingStackedActivity;
import com.tokopedia.tokopoints.view.pointhistory.PointHistoryActivity;
import com.tokopedia.tokopoints.view.activity.TokoPointsHomeNewActivity;
import com.tokopedia.tokopoints.view.adapter.ExploreSectionPagerAdapter;
import com.tokopedia.tokopoints.view.adapter.SectionCategoryAdapter;
import com.tokopedia.tokopoints.view.adapter.SectionTickerPagerAdapter;
import com.tokopedia.tokopoints.view.contract.TokoPointsHomeContract;
import com.tokopedia.tokopoints.view.customview.CustomViewPager;
import com.tokopedia.tokopoints.view.customview.ServerErrorView;
import com.tokopedia.tokopoints.view.customview.TokoPointToolbar;
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
import com.tokopedia.unifyprinciples.Typography;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import static com.tokopedia.tokopoints.view.util.CommonConstant.BUNDLE_ARGS_USER_IS_LOGGED_IN;

/*
 * Dynamic layout params are applied via
 * function setLayoutParams() because configuration in statusBarHeight
 * */

public class TokoPointsHomeFragmentNew extends BaseDaggerFragment implements TokoPointsHomeContract.View, View.OnClickListener, TokoPointToolbar.OnTokoPointToolbarClickListener {

    private static final String FPM_TOKOPOINT = "ft_tokopoint";
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private ViewFlipper mContainerMain;
    private TextView mTextMembershipValue, mTextMembershipValueBottom, mTextPoints, mTextPointsBottom, mTextLoyalty;
    private TextView mTextMembershipLabel;
    private ImageView mImgEgg, mImgEggBottom, mImgBackground;
    private TabLayout mTabLayoutPromo;
    private CustomViewPager mPagerPromos;
    private LinearLayout bottomViewMembership;
    private AppBarLayout appBarHeader;
    private RecyclerView mRvDynamicLinks;
    @Inject
    public TokoPointsHomePresenterNew mPresenter;

    private int mSumToken;
    private String mValueMembershipDescription;

    private StartPurchaseBottomSheet mStartPurchaseBottomSheet;
    private View tickerContainer;
    private View dynamicLinksContainer;
    private LinearLayout containerEgg;
    private onAppBarCollapseListener appBarCollapseListener;
    private ExploreSectionPagerAdapter mExploreSectionPagerAdapter;
    private PerformanceMonitoring performanceMonitoring;
    private CollapsingToolbarLayout collapsingToolbarLayout;

    private CoordinatorLayout coordinatorLayout;

    private View statusBarBgView;
    private TokoPointToolbar tokoPointToolbar;
    private ServerErrorView serverErrorView;

    private Boolean userLoggedInStatus;
    private AppCompatImageView ivLeaderBoard;
    private AppCompatImageView ivUserCoupon;
    private TextView userCouponCount;
    private CardView rewardsPointLayout;
    private AppCompatImageView ivPointStack;
    private TextView tvPointLabel;
    private View midSeparator;
    private AppCompatImageView ivLoyaltyStack;
    private TextView tvLoyaltyLabel;
    private TextView tvPointsValue;
    private ConstraintLayout pointLayout;
    private TextView emptyTitle;
    private TextView emptySubtitle;
    private TextView tvNonLoginCta;
    private static final int REQUEST_CODE_LOGIN = 1;


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
        if (getArguments() != null) {
            userLoggedInStatus = getArguments().getBoolean(BUNDLE_ARGS_USER_IS_LOGGED_IN);
        }
        View view = inflater.inflate(R.layout.tp_fragment_homepage_new, container, false);
        initViews(view);
        hideStatusBar();
        ((BaseSimpleActivity) getActivity()).setSupportActionBar(tokoPointToolbar);
        collapsingToolbarLayout = view.findViewById(R.id.collapsing_toolbar);
        collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
        collapsingToolbarLayout.setTitle(" ");

        appBarHeader.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> handleAppBarOffsetChange(verticalOffset));
        setLayoutParams();
        return view;
    }


    private void setLayoutParams() {
        int statusBarHeight = getStatusBarHeight(getActivity());
        FrameLayout.LayoutParams layoutParams = (FrameLayout.LayoutParams) tokoPointToolbar.getLayoutParams();
        layoutParams.topMargin = statusBarHeight;
        tokoPointToolbar.setLayoutParams(layoutParams);

        RelativeLayout.LayoutParams imageEggLp = (RelativeLayout.LayoutParams) mImgEgg.getLayoutParams();
        imageEggLp.topMargin = (int) (statusBarHeight + getActivity().getResources().getDimension(R.dimen.tp_top_margin_big_image));
        mImgEgg.setLayoutParams(imageEggLp);

        RelativeLayout.LayoutParams imageBigLp = (RelativeLayout.LayoutParams) mImgBackground.getLayoutParams();
        imageBigLp.height = (int) (statusBarHeight + getActivity().getResources().getDimension(R.dimen.tp_home_top_bg_height));
        mImgBackground.setLayoutParams(imageBigLp);

        if (!userLoggedInStatus) {
            RelativeLayout.LayoutParams rewardsPointLayoutLP = (RelativeLayout.LayoutParams) rewardsPointLayout.getLayoutParams();
            rewardsPointLayoutLP.topMargin = (int) (statusBarHeight + getActivity().getResources().getDimension(R.dimen.tp_cta_container_nonlogin));
            rewardsPointLayout.setLayoutParams(rewardsPointLayoutLP);

            RelativeLayout.LayoutParams tvEmptyLP = (RelativeLayout.LayoutParams) emptyTitle.getLayoutParams();
            tvEmptyLP.topMargin = (int) (statusBarHeight + getActivity().getResources().getDimension(com.tokopedia.design.R.dimen.dp_56));
            emptyTitle.setLayoutParams(tvEmptyLP);
        }

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

    private void handleAppBarOffsetChange(int offset) {
        int positiveOffset = offset * -1;
        int toolbarTransitionRange = getResources().getDimensionPixelSize(R.dimen.tp_home_top_bg_height)
                - tokoPointToolbar.getHeight() - getStatusBarHeight(getActivity());
        float offsetAlpha =
                (255f / toolbarTransitionRange) * (toolbarTransitionRange - positiveOffset);
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
            tokoPointToolbar.switchToDarkMode();
        else
            tokoPointToolbar.switchToTransparentMode();
        tokoPointToolbar.applyAlphaToToolbarBackground(alpha);
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
            layoutParams.setMargins(0, 0, 0, getResources().getDimensionPixelOffset(com.tokopedia.design.R.dimen.dp_90));
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

        tokoPointToolbar.setTitle(R.string.tp_title_tokopoints);
        tokoPointToolbar.setOnTokoPointToolbarClickListener(this);
        TokoPointsNotificationManager.fetchNotification(getActivity(), "main", getChildFragmentManager());
        mPresenter.tokopointOnboarding2020();
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
        mPresenter.getCouponCount();
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
        return getActivity();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof TokoPointsHomeNewActivity)
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
            RouteManager.route(getContext(), ApplinkConstInternalGlobal.WEBVIEW_TITLE, CommonConstant.WebLink.MEMBERSHIP, getString(R.string.tp_label_membership));


            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_STATUS_MEMBERSHIP,
                    mValueMembershipDescription);
        } else if (source.getId() == R.id.view_loyalty_bottom) {
            RouteManager.route(getContext(), ApplinkConstInternalGlobal.WEBVIEW_TITLE, CommonConstant.WebLink.MEMBERSHIP, getString(R.string.tp_label_membership));
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_MEM_BOTTOM,
                    "");
        } else if (source.getId() == R.id.view_point_bottom
                || source.getId() == R.id.view_point) {
            if (userLoggedInStatus) {
                startActivity(new Intent(getActivityContext(), PointHistoryActivity.class));
            } else {
                getActivity().startActivityForResult(RouteManager.getIntent(getContext(), ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
            }
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_POINT_SAYA,
                    "");
        } else if (source.getId() == R.id.view_loyalty) {
            if (userLoggedInStatus) {
                startActivity(new Intent(getActivityContext(), PointHistoryActivity.class));
            } else {
                getActivity().startActivityForResult(RouteManager.getIntent(getContext(), ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
            }
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_LOYALTY_SAYA,
                    "");
        } else if (source.getId() == R.id.text_failed_action) {
            mPresenter.getTokoPointDetail();
        } else if (source.getId() == R.id.container_fab_egg_token) {
            if (mSumToken <= 0) {
                if (mStartPurchaseBottomSheet != null) {
                    AddPointsFragment addPointsFragment = new AddPointsFragment();
                    addPointsFragment.show(getChildFragmentManager(), "");
                }
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
        }
    }

    private void initViews(@NonNull View view) {
        coordinatorLayout = view.findViewById(R.id.container);
        mContainerMain = view.findViewById(R.id.container_main);
        mTextMembershipValue = view.findViewById(R.id.text_membership_value);
        if (userLoggedInStatus) {
            mTextMembershipValue.setCompoundDrawablesWithIntrinsicBounds(null, null, MethodChecker.getDrawable
                    (getActivity(), com.tokopedia.design.R.drawable.ic_arrow_right_grey), null);
        }
        mTextMembershipLabel = view.findViewById(R.id.text_membership_label);
        mTextPoints = view.findViewById(R.id.text_my_points_value);
        mTextLoyalty = view.findViewById(R.id.text_loyalty_value);
        mImgEgg = view.findViewById(R.id.img_egg);
        mTabLayoutPromo = view.findViewById(R.id.tab_layout_promos);
        mPagerPromos = view.findViewById(R.id.view_pager_promos);
        mPagerPromos.disableScroll(true);
        mTextMembershipValueBottom = view.findViewById(R.id.text_loyalty_value_bottom);
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom);
        mImgEggBottom = view.findViewById(R.id.img_loyalty_stack_bottom);
        mImgBackground = view.findViewById(R.id.img_bg_header);
        appBarHeader = view.findViewById(R.id.app_bar);
        bottomViewMembership = view.findViewById(R.id.bottom_view_membership);
        tickerContainer = view.findViewById(R.id.cons_ticker_container);
        containerEgg = view.findViewById(R.id.container_fab_egg_token);
        mRvDynamicLinks = view.findViewById(R.id.rv_dynamic_link);
        dynamicLinksContainer = view.findViewById(R.id.container_dynamic_links);
        statusBarBgView = view.findViewById(R.id.status_bar_bg);
        tokoPointToolbar = view.findViewById(R.id.toolbar_tokopoint);
        serverErrorView = view.findViewById(R.id.server_error_view);
        ivLeaderBoard = view.findViewById(R.id.iv_tpToolbar_leaderboard);
        ivUserCoupon = view.findViewById(R.id.iv_tpToolbar_coupon);
        userCouponCount = view.findViewById(R.id.tv_tpToolbar_couponCount);
        rewardsPointLayout = view.findViewById(R.id.card_point);
        ivPointStack = view.findViewById(R.id.img_points_stack);
        tvPointLabel = view.findViewById(R.id.text_my_points_label);
        midSeparator = view.findViewById(R.id.line_separator_points_vertical);
        ivLoyaltyStack = view.findViewById(R.id.img_loyalty_stack);
        pointLayout = view.findViewById(R.id.layout_homepoint);
        tvLoyaltyLabel = view.findViewById(R.id.text_loyalty_label);
        tvNonLoginCta = view.findViewById(R.id.tvNonLoginCta);

        emptyTitle = view.findViewById(R.id.emptyTitle);
        emptySubtitle = view.findViewById(R.id.emptySubtitle);

        setStatusBarViewHeight();
    }

    private void setStatusBarViewHeight() {
        if (getActivity() != null)
            statusBarBgView.getLayoutParams().height = getStatusBarHeight(getActivity());
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.view_loyalty_bottom).setOnClickListener(this);
        getView().findViewById(R.id.view_point_bottom).setOnClickListener(this);
        getView().findViewById(R.id.img_egg).setOnClickListener(this);
        getView().findViewById(R.id.text_membership_value).setOnClickListener(this);
        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);
        getView().findViewById(R.id.view_point).setOnClickListener(this);
        getView().findViewById(R.id.view_loyalty).setOnClickListener(this);
        getView().findViewById(R.id.container_fab_egg_token).setOnClickListener(this);
    }

    @Override
    public void openWebView(String url) {
        RouteManager.route(getContext(), ApplinkConstInternalGlobal.WEBVIEW, url);
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
                    ImageHandler.loadGifFromUrl(imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl(), com.tokopedia.session.R.color.green_50);
                } else {
                    ImageHandler.loadImageFitCenter(getContext(), imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl());
                }

                if (mSumToken == 0) {
                    textCount.setVisibility(View.GONE);
                    textMessage.setPadding(getResources().getDimensionPixelSize(com.tokopedia.design.R.dimen.dp_30),
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
    public void onError(String error, boolean hasInternet) {
        if (mContainerMain != null) {
            mContainerMain.setDisplayedChild(CONTAINER_ERROR);
            serverErrorView.showErrorUi(hasInternet);
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
            startActivity(CouponListingStackedActivity.getCallingIntent(getActivityContext()));

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
                    com.tokopedia.design.R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }

        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    com.tokopedia.design.R.color.grey_warm));
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
    public void showTokoPointCoupon(TokoPointSumCoupon data) {
        if (data == null || tokoPointToolbar == null) {
            return;
        }

        tokoPointToolbar.setCouponCount(data.getSumCoupon(), data.getSumCouponStr());
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
            tickerContainer.setVisibility(View.GONE);
            return;
        }

        ViewPager pager = getView().findViewById(R.id.view_pager_ticker);
        pager.setAdapter(new SectionTickerPagerAdapter(getContext(), content.getLayoutTickerAttr().getTickerList()));
        CirclePageIndicator pageIndicator = getView().findViewById(R.id.page_indicator_ticker);
        View hideTickerView = getView().findViewById(R.id.ic_close_ticker);
        hideTickerView.setVisibility(View.GONE);

        if (content.getLayoutTickerAttr().getTickerList().size() > 1) {
            //adding bottom dots(Page Indicator)
            pageIndicator.setVisibility(View.VISIBLE);
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
        mRvDynamicLinks.setAdapter(new SectionCategoryAdapter(getActivityContext(), content.getLayoutCategoryAttr().getCategoryTokopointsList()));
    }

    @Override
    public void renderToolbarWithHeader(TokoPointEntity data) {
        if (data == null) {
            return; //TODO any error page? Ask from gulfikar
        }
        //init header
        if (data.getStatus() != null) {


            if (!userLoggedInStatus) {
                mImgEgg.setVisibility(View.GONE);

                emptyTitle.setVisibility(View.VISIBLE);
                emptySubtitle.setVisibility(View.VISIBLE);
                emptyTitle.setText(data.getStatus().getEmptyMessage().getTitle());
                emptySubtitle.setText(data.getStatus().getEmptyMessage().getSubTitle());

                ivLeaderBoard.setVisibility(View.GONE);
                ivUserCoupon.setVisibility(View.GONE);
                userCouponCount.setVisibility(View.GONE);
                ivLoyaltyStack.setVisibility(View.GONE);
                ivPointStack.setVisibility(View.GONE);
                midSeparator.setVisibility(View.GONE);
                tvPointLabel.setVisibility(View.GONE);
                tvLoyaltyLabel.setVisibility(View.GONE);
                mTextPoints.setVisibility(View.GONE);
                mTextLoyalty.setVisibility(View.GONE);
                pointLayout.setBackground(getResources().getDrawable(R.drawable.bg_tp_nonlogin_rewards_container));
                tvNonLoginCta.setVisibility(View.VISIBLE);
                tvNonLoginCta.setText(data.getStatus().getCta().getText());

                ImageHandler.loadImageFitCenter(mImgBackground.getContext(), mImgBackground, data.getStatus().getTier().getBackgroundImgURLMobile());
                ImageHandler.loadImageCircle2(getActivityContext(), mImgEggBottom, data.getStatus().getTier().getEggImageHomepageURL());

            } else {
                mTextMembershipLabel.setText(data.getStatus().getUserName());

                if (data.getStatus().getTier() != null) {
                    mValueMembershipDescription = data.getStatus().getTier().getNameDesc();
                    mTextMembershipValue.setText(mValueMembershipDescription);
                    mTextMembershipValueBottom.setText(mValueMembershipDescription);
                    ImageHandler.loadImageFitCenter(mImgBackground.getContext(), mImgBackground, data.getStatus().getTier().getBackgroundImgURLMobile());
                    ImageHandler.loadImageCircle2(getActivityContext(), mImgEgg, data.getStatus().getTier().getEggImageHomepageURL());
                    ImageHandler.loadImageCircle2(getActivityContext(), mImgEggBottom, data.getStatus().getTier().getEggImageHomepageURL());
                }

                if (data.getStatus().getPoints() != null) {
                    mTextPoints.setText(CurrencyFormatUtil.convertPriceValue(data.getStatus().getPoints().getReward(), false));
                    mTextPointsBottom.setText(CurrencyFormatUtil.convertPriceValue(data.getStatus().getPoints().getReward(), false));
                    mTextLoyalty.setText(CurrencyFormatUtil.convertPriceValue(data.getStatus().getPoints().getLoyalty(), false));
                }
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
    }

    @Override
    public void renderSections(List<SectionContent> sections) {
        if (sections == null) {
            //TODO hide all section container
            return;
        }

        List<SectionContent> exploreSectionItem = new ArrayList<>();

        for (SectionContent sectionContent : sections) {
            switch (sectionContent.getLayoutType()) {
                case CommonConstant.SectionLayoutType.TICKER:
                    renderTicker(sectionContent);
                    break;
                case CommonConstant.SectionLayoutType.CATEGORY:
                    renderCategory(sectionContent);
                    break;
                case CommonConstant.SectionLayoutType.COUPON:
                case CommonConstant.SectionLayoutType.CATALOG:
                case CommonConstant.SectionLayoutType.BANNER:
                    exploreSectionItem.add(sectionContent);
                    break;
                default:
                    break;

            }
        }

        //init explore and kupon-saya tab
        renderExploreSectionTab(exploreSectionItem);
    }

    public void renderExploreSectionTab(List<SectionContent> sections) {
        if (sections.isEmpty()) {
            //TODO hide tab or show empty box
        }

        mExploreSectionPagerAdapter = new ExploreSectionPagerAdapter(getActivityContext(), mPresenter, sections);
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
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EXPLORE,
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

    }

    public void onSuccessResponse(TokoPointEntity data, List<SectionContent> sections) {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);

        renderToolbarWithHeader(data);
        renderSections(sections);
    }

    @Override
    public void showTokopoint2020(PopupNotification data) {

        if (data.getTitle().isEmpty() || data.getTitle() == null || data.getAppLink().isEmpty() || data.getAppLink() == null) {
            return;
        }

        ButtonCompat btn;
        Typography titleDialog;
        Typography descDialog;
        ImageView boxImageView;
        AlertDialog.Builder adb = new AlertDialog.Builder(getContext());
        View view = LayoutInflater.from(getContext()).inflate(R.layout.tp_upcoming_feature_dialog, null);
        adb.setView(view);
        btn = view.findViewById(R.id.btn_route);
        titleDialog = view.findViewById(R.id.tv_dialogTitle);
        descDialog = view.findViewById(R.id.tv_dialogDesc);
        boxImageView = view.findViewById(R.id.iv_banner);

        titleDialog.setText(data.getTitle());
        descDialog.setText(data.getText());
        btn.setText(data.getButtonText());
        ImageHandler.loadImageFitCenter(getContext(), boxImageView, data.getImageURL());
        AlertDialog alertDialog = adb.create();
        alertDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        alertDialog.setCancelable(false);
        alertDialog.setCanceledOnTouchOutside(false);
        btn.setOnClickListener(v -> {
            RouteManager.route(getContext(), data.getAppLink());
            alertDialog.dismiss();
        });
        alertDialog.show();
    }

    @Override
    public void onToolbarLeaderboardClick() {
        RouteManager.route(getContext(), ApplinkConstInternalGlobal.WEBVIEW_TITLE, CommonConstant.WebLink.LEADERBOARD, getString(R.string.tp_leader));
        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_LEADERBOARD,
                "");
    }

    @Override
    public void onToolbarMyCouponClick() {
        if (getActivity() == null) {
            return;
        }
        startActivity(CouponListingStackedActivity.getCallingIntent(getActivity()));
        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                AnalyticsTrackerUtil.ActionKeys.CLICK_COUNTER_KUPON_SAYA,
                "");
    }

    @Override
    public void onDestroyView() {
        if (mExploreSectionPagerAdapter != null) {
            mExploreSectionPagerAdapter.onDestroyView();
        }
        super.onDestroyView();
    }
}
