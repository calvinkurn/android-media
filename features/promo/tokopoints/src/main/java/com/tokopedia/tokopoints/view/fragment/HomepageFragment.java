package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.LocalCacheHandler;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.BottomSheetView;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.design.viewpagerindicator.CirclePageIndicator;
import com.tokopedia.gamification.applink.ApplinkConstant;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.CatalogListingActivity;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.activity.SendGiftActivity;
import com.tokopedia.tokopoints.view.activity.TokoPointsHomeActivity;
import com.tokopedia.tokopoints.view.adapter.DynamicLinkAdapter;
import com.tokopedia.tokopoints.view.adapter.HomepagePagerAdapter;
import com.tokopedia.tokopoints.view.adapter.TickerPagerAdapter;
import com.tokopedia.tokopoints.view.contract.HomepageContract;
import com.tokopedia.tokopoints.view.interfaces.onAppBarCollapseListener;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.model.LobDetails;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.PopupNotification;
import com.tokopedia.tokopoints.view.model.TickerContainer;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusTierEntity;
import com.tokopedia.tokopoints.view.model.TokoPointSumCoupon;
import com.tokopedia.tokopoints.view.model.TokopointsDynamicLinkEntity;
import com.tokopedia.tokopoints.view.presenter.HomepagePresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.TabUtil;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class HomepageFragment extends BaseDaggerFragment implements HomepageContract.View, View.OnClickListener {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private ViewFlipper mContainerMain;
    private TextView mTextMembershipValue, mTextMembershipValueBottom, mTextPoints, mTextPointsBottom, mTextLoyalty;
    private ImageView mImgEgg, mImgEggBottom;
    private TabLayout mTabLayoutPromo;
    private ViewPager mPagerPromos;
    private LinearLayout bottomViewMembership;
    private AppBarLayout appBarHeader;
    private RecyclerView mRvDynamicLinks;
    @Inject
    public HomepagePresenter mPresenter;

    private int mSumToken;
    private int mCouponCount;
    private String mValueMembershipDescription;

    StartPurchaseBottomSheet mStartPurchaseBottomSheet;
    private View tickerContainer;
    private View dynamicLinksContainer;
    private LinearLayout containerEgg;
    private onAppBarCollapseListener appBarCollapseListener;

    public static HomepageFragment newInstance() {
        return new HomepageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_homepage, container, false);
        initViews(view);
        appBarHeader.addOnOffsetChangedListener(offsetChangedListenerBottomView);
        appBarHeader.addOnOffsetChangedListener(offsetChangedListenerAppBarElevation);
        return view;
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
        mPresenter.getPopupNotification();
        LocalCacheHandler localCacheHandler = new LocalCacheHandler(getAppContext(), CommonConstant.PREF_TOKOPOINTS);
        if (!localCacheHandler.getBoolean(CommonConstant.PREF_KEY_ON_BOARDED)) {
            showOnBoardingTooltip(getString(R.string.tp_label_know_tokopoints), getString(R.string.tp_message_tokopoints_on_boarding));
            localCacheHandler.putBoolean(CommonConstant.PREF_KEY_ON_BOARDED, true);
            localCacheHandler.applyEditor();
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();
        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
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
        return null;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.text_membership_label || source.getId() == R.id.img_egg || source.getId() == R.id.text_membership_value
                || source.getId() == R.id.bottom_view_membership) {
            openWebView(CommonConstant.WebLink.MEMBERSHIP);

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_MEMBERSHIP,
                    mValueMembershipDescription);
        } else if (source.getId() == R.id.view_point_saya
                || source.getId() == R.id.text_my_points_value_bottom) {
            openWebView(CommonConstant.WebLink.HISTORY);

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_TOKOPOINT,
                    AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_POINT_SAYA,
                    "");
        } else if (source.getId() == R.id.view_loyalty_saya) {
            openWebView(CommonConstant.WebLink.HISTORY);

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
        mTextPoints = view.findViewById(R.id.text_my_points_value);
        mTextLoyalty = view.findViewById(R.id.text_loyalty_value);
        mImgEgg = view.findViewById(R.id.img_egg);
        mTabLayoutPromo = view.findViewById(R.id.tab_layout_promos);
        mPagerPromos = view.findViewById(R.id.view_pager_promos);
        mTextMembershipValueBottom = view.findViewById(R.id.text_membership_value_bottom);
        mTextPointsBottom = view.findViewById(R.id.text_my_points_value_bottom);
        mImgEggBottom = view.findViewById(R.id.img_egg_bottom);
        appBarHeader = view.findViewById(R.id.app_bar);
        bottomViewMembership = view.findViewById(R.id.bottom_view_membership);
        tickerContainer = view.findViewById(R.id.cons_ticker_container);
        containerEgg = view.findViewById(R.id.container_fab_egg_token);
        mRvDynamicLinks = view.findViewById(R.id.rv_dynamic_link);
        dynamicLinksContainer = view.findViewById(R.id.container_dynamic_links);
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.text_membership_label).setOnClickListener(this);
        getView().findViewById(R.id.img_egg).setOnClickListener(this);
        getView().findViewById(R.id.text_membership_value).setOnClickListener(this);
        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);
        getView().findViewById(R.id.view_point_saya).setOnClickListener(this);
        getView().findViewById(R.id.view_loyalty_saya).setOnClickListener(this);
        bottomViewMembership.setOnClickListener(this);
        mTextPointsBottom.setOnClickListener(this);
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    @Override
    public void gotoCatalog() {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_COUPON_COUNT, mCouponCount);
        startActivity(CatalogListingActivity.getCallingIntent(getActivityContext(), bundle));
    }

    @Override
    public void gotoCoupons() {
        startActivity(MyCouponListingActivity.getCallingIntent(getActivityContext()));
    }

    @Override
    public void onSuccessPromos(@NonNull TokoPointPromosEntity data) {
        initPromoPager(data.getCatalog().getCatalogs(), data.getCoupon().getCoupons(), data.getCoupon().getEmptyMessage());
    }

    @Override
    public void onSuccess(TokoPointStatusTierEntity tierData, TokoPointStatusPointsEntity pointData, LobDetails lobDetails) {
        mValueMembershipDescription = tierData.getNameDesc();
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
        mPresenter.getPromos();
        mTextMembershipValue.setText(String.valueOf(tierData.getNameDesc()));
        mTextMembershipValueBottom.setText(String.valueOf(tierData.getNameDesc()));
        mTextPoints.setText(CurrencyFormatUtil.convertPriceValue(pointData.getReward(), false));
        mTextPointsBottom.setText(CurrencyFormatUtil.convertPriceValue(pointData.getReward(), false));
        mTextLoyalty.setText(CurrencyFormatUtil.convertPriceValue(pointData.getLoyalty(), false));
        ImageHandler.loadImageCircle2(getActivityContext(), mImgEgg, tierData.getEggImageUrl());
        ImageHandler.loadImageCircle2(getActivityContext(), mImgEggBottom, tierData.getEggImageUrl());

        //init bottom sheet
        mStartPurchaseBottomSheet = new StartPurchaseBottomSheet();
        mStartPurchaseBottomSheet.setData(lobDetails);

        if (getView() != null) {
            getView().findViewById(R.id.img_token).setOnClickListener(view -> {
                if (mSumToken <= 0) {
                    showStartPurchaseBottomSheet(lobDetails.getTitle());
                } else {
                    if (getActivity() != null) {
                        RouteManager.route(getActivity(), ApplinkConstant.GAMIFICATION);
                    }
                }
            });

            getView().findViewById(R.id.text_token_title).setOnClickListener(view -> {
                if (mSumToken <= 0) {
                    showStartPurchaseBottomSheet(lobDetails.getTitle());

                    AnalyticsTrackerUtil.sendEvent(view.getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EGG_EMPTY,
                            "");
                } else {
                    if (getActivity() != null) {
                        RouteManager.route(getActivity(), ApplinkConstant.GAMIFICATION);
                    }

                    AnalyticsTrackerUtil.sendEvent(view.getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_LUCKY_EGG,
                            AnalyticsTrackerUtil.CategoryKeys.TOKOPOINTS_EGG,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_EGG,
                            "");
                }
            });
        }
    }

    @Override
    public void onSuccessTokenDetail(LuckyEggEntity tokenDetail) {
        if (tokenDetail != null) {
            try {
                containerEgg.setVisibility(View.VISIBLE);
                TextView textCount = getView().findViewById(R.id.text_token_count);
                TextView textMessage = getView().findViewById(R.id.text_token_title);
                ImageView imgToken = getView().findViewById(R.id.img_token);
                textCount.setText(tokenDetail.getSumTokenStr());
                this.mSumToken = tokenDetail.getSumToken();
                textMessage.setText(tokenDetail.getFloating().getTokenClaimText());
                if(tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl().endsWith(".gif")){
                    ImageHandler.loadGifFromUrl(imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl(), R.color.green_50);
                }else{
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
    public void onSuccessTicker(@NonNull List<TickerContainer> tickers) {
        if (getView() != null && tickers.size() > 0) {
            ViewPager pager = getView().findViewById(R.id.view_pager_ticker);

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

            pager.setAdapter(new TickerPagerAdapter(getContext(), tickers));
            final CirclePageIndicator pageIndicator = getView().findViewById(R.id.page_indicator_ticker);
            if (tickers != null && tickers.size() > 1) {
                //adding bottom dots(Page Indicator)
                pageIndicator.setVisibility(View.VISIBLE);
                pageIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
                pageIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.white_two));
                pageIndicator.setViewPager(pager, 0);
            } else {
                pageIndicator.setVisibility(View.GONE);
            }

            tickerContainer.setVisibility(View.VISIBLE);
        } else {
            tickerContainer.setVisibility(View.GONE);

        }
    }

    @Override
    public void onError(String error) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Override
    public void onErrorTicker(String errorMessage) {
        if (getView() != null) {
            getView().findViewById(R.id.cons_ticker_container).setVisibility(View.GONE);
        }
    }

    @Override
    public void onErrorPromos(String error) {

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
    }

    @Override
    public void showValidationMessageDialog(CatalogsValueEntity item, String title, String message, int resCode) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        String labelPositive;
        String labelNegative = null;

        switch (resCode) {
            case CommonConstant.CouponRedemptionCode.LOW_POINT:
                labelPositive = getString(R.string.tp_label_shopping);
                labelNegative = getString(R.string.tp_label_later);
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
                    case CommonConstant.CouponRedemptionCode.LOW_POINT:
                        AnalyticsTrackerUtil.sendEvent(getContext(),
                                AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                                AnalyticsTrackerUtil.CategoryKeys.POPUP_PENUKARAN_POINT_TIDAK,
                                AnalyticsTrackerUtil.ActionKeys.CLICK_NANTI_SAJA,
                                "");
                        break;
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
                    startActivity(((TokopointRouter) getAppContext()).getHomeIntent(getActivityContext()));

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

    private void initPromoPager(List<CatalogsValueEntity> catalogs, List<CouponValueEntity> coupons, Map<String, String> emptyMessages) {
        HomepagePagerAdapter homepagePagerAdapter = new HomepagePagerAdapter(getActivityContext(), mPresenter, catalogs, coupons);
        homepagePagerAdapter.setEmptyMessages(emptyMessages);
        mPagerPromos.setAdapter(homepagePagerAdapter);
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
                } else {
                    appBarHeader.removeOnOffsetChangedListener(offsetChangedListenerBottomView);
                    slideDown();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
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
    public void showPopupNotification(PopupNotification data) {
        if (data.getTitle() == null || data.getTitle().trim().isEmpty()) {
            return;
        }

        PopupNotificationBottomSheet popupNotificationBottomSheet = new PopupNotificationBottomSheet();
        popupNotificationBottomSheet.setData(data);
        popupNotificationBottomSheet.show(getChildFragmentManager(), data.getTitle());
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
    public void onSuccessDynamicLink(TokopointsDynamicLinkEntity tokopointsDynamicLinkEntity) {
        int length = tokopointsDynamicLinkEntity.getLinks().size();
        if (getView() != null) {
            dynamicLinksContainer.setVisibility(View.VISIBLE);
        }
        final boolean isEven = (length % 2 == 0);
        GridLayoutManager manager = new GridLayoutManager(getContext(), 2, GridLayoutManager.VERTICAL, false);
        manager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                return (isEven || position != length - 1) ? 1 : 2;
            }
        });
        mRvDynamicLinks.setLayoutManager(manager);
        mRvDynamicLinks.setAdapter(new DynamicLinkAdapter(tokopointsDynamicLinkEntity.getLinks()));

    }
}
