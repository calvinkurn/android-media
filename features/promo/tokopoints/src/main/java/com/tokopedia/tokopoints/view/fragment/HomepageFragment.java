package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tkpd.library.utils.ImageHandler;
import com.tkpd.library.utils.LocalCacheHandler;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.core.router.home.HomeRouter;
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
import com.tokopedia.tokopoints.view.adapter.CatalogBannerPagerAdapter;
import com.tokopedia.tokopoints.view.adapter.HomepagePagerAdapter;
import com.tokopedia.tokopoints.view.adapter.TickerPagerAdapter;
import com.tokopedia.tokopoints.view.contract.HomepageContract;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.model.LuckyEggEntity;
import com.tokopedia.tokopoints.view.model.TickerContainer;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;
import com.tokopedia.tokopoints.view.model.TokoPointStatusTierEntity;
import com.tokopedia.tokopoints.view.presenter.HomepagePresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

import javax.inject.Inject;

public class HomepageFragment extends BaseDaggerFragment implements HomepageContract.View, View.OnClickListener {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private ViewFlipper mContainerMain;
    private TextView mTextMembershipValue, mTextPoints, mTextLoyalty;
    private ImageView mImgEgg;
    private TabLayout mTabLayoutPromo;
    private ViewPager mPagerPromos;
    @Inject
    public HomepagePresenter mPresenter;

    public static HomepageFragment newInstance() {
        return new HomepageFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_homepage, container, false);
        initViews(view);
        return view;
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
        if (source.getId() == R.id.text_see_membership_status) {
            openWebView(CommonConstant.WebLink.MEMBERSHIP);
        } else if (source.getId() == R.id.text_my_points_label
                || source.getId() == R.id.img_points_stack
                || source.getId() == R.id.text_my_points_value
                || source.getId() == R.id.img_loyalty_stack
                || source.getId() == R.id.text_loyalty_label
                || source.getId() == R.id.text_loyalty_value) {
            openWebView(CommonConstant.WebLink.HISTORY);
        } else if (source.getId() == R.id.text_failed_action) {
            mPresenter.getTokoPointDetail();
        } else if (source.getId() == R.id.container_fab_egg_token) {
            RouteManager.route(getActivity(), ApplinkConstant.GAMIFICATION);
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
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.text_see_membership_status).setOnClickListener(this);
        getView().findViewById(R.id.text_my_points_label).setOnClickListener(this);
        getView().findViewById(R.id.img_points_stack).setOnClickListener(this);
        getView().findViewById(R.id.text_my_points_value).setOnClickListener(this);
        getView().findViewById(R.id.img_loyalty_stack).setOnClickListener(this);
        getView().findViewById(R.id.text_loyalty_label).setOnClickListener(this);
        getView().findViewById(R.id.text_loyalty_value).setOnClickListener(this);
        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);
        getView().findViewById(R.id.container_fab_egg_token).setOnClickListener(this);
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    @Override
    public void gotoCatalog() {
        startActivity(CatalogListingActivity.getCallingIntent(getActivityContext()));
    }

    @Override
    public void gotoCoupons() {
        startActivity(MyCouponListingActivity.getCallingIntent(getActivityContext()));
    }

    @Override
    public void onSuccessPromos(TokoPointPromosEntity data) {
        initPromoPager(data.getCatalog().getCatalogs(), data.getCoupon().getCoupons());
    }

    @Override
    public void onSuccess(TokoPointStatusTierEntity tierData, TokoPointStatusPointsEntity pointData) {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
        mPresenter.getPromos();
        mTextMembershipValue.setText(String.valueOf(tierData.getNameDesc()));
        mTextPoints.setText(CurrencyFormatUtil.convertPriceValue(pointData.getReward(), false));
        mTextLoyalty.setText(CurrencyFormatUtil.convertPriceValue(pointData.getLoyalty(), false));
        ImageHandler.loadImageFitCenter(getActivityContext(), mImgEgg, tierData.getEggImageUrl());
    }

    @Override
    public void onSuccessTokenDetail(LuckyEggEntity tokenDetail) {
        if (tokenDetail != null) {
            try {
                getView().findViewById(R.id.container_fab_egg_token).setVisibility(View.VISIBLE);
                TextView textCount = getView().findViewById(R.id.text_token_count);
                TextView textMessage = getView().findViewById(R.id.text_token_title);
                ImageView imgToken = getView().findViewById(R.id.img_token);
                textCount.setText(String.valueOf(tokenDetail.getSumToken()));
                textMessage.setText(tokenDetail.getFloating().getTokenClaimText());
                ImageHandler.loadImageFit2(getContext(), imgToken, tokenDetail.getFloating().getTokenAsset().getFloatingImgUrl());

                if (tokenDetail.getSumToken() == 0) {
                    getView().findViewById(R.id.text_token_count).setVisibility(View.GONE);
                    getView().findViewById(R.id.text_token_title).setPadding(getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                            getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                            getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                            getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall));
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
            pager.setAdapter(new TickerPagerAdapter(getContext(), tickers));
            //adding bottom dots(Page Indicator)
            final CirclePageIndicator pageIndicator = getView().findViewById(R.id.page_indicator_ticker);
            pageIndicator.setFillColor(ContextCompat.getColor(getContext(), R.color.tkpd_main_green));
            pageIndicator.setPageColor(ContextCompat.getColor(getContext(), R.color.white_two));
            pageIndicator.setViewPager(pager, 0);
            getView().findViewById(R.id.cons_ticker_container).setVisibility(View.VISIBLE);
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
        });
        adb.setNegativeButton(R.string.tp_label_later, (dialogInterface, i) -> {

        });
        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    @Override
    public void showConfirmRedeemDialog(String cta, String code, String title) {
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        adb.setNegativeButton(R.string.tp_label_use, (dialogInterface, i) -> showRedeemCouponDialog(cta, code, title));

        adb.setPositiveButton(R.string.tp_label_view_coupon, (dialogInterface, i) -> {
            //Open webview with lihat kupon
            openWebView(CommonConstant.WebLink.SEE_COUPON);
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

            });
        }

        adb.setPositiveButton(labelPositive, (dialogInterface, i) -> {
            switch (resCode) {
                case CommonConstant.CouponRedemptionCode.LOW_POINT:
                    startActivity(HomeRouter.getHomeActivityInterfaceRouter(
                            getAppContext()));
                    break;
                case CommonConstant.CouponRedemptionCode.QUOTA_LIMIT_REACHED:
                    dialogInterface.cancel();
                    break;
                case CommonConstant.CouponRedemptionCode.PROFILE_INCOMPLETE:
                    startActivity(new Intent(getAppContext(), ProfileCompletionActivity.class));
                    break;
                case CommonConstant.CouponRedemptionCode.SUCCESS:
                    mPresenter.startSaveCoupon(item);
                    break;
                default:
                    dialogInterface.cancel();
            }
        });

        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    private void initPromoPager(List<CatalogsValueEntity> catalogs, List<CouponValueEntity> coupons) {
        mPagerPromos.setAdapter(new HomepagePagerAdapter(getActivityContext(), mPresenter, catalogs, coupons));
        mPagerPromos.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(mTabLayoutPromo));
        mTabLayoutPromo.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(mPagerPromos));

        //Check for coupons and make sure user coupon get selected if he has any number.
        if (coupons != null && !coupons.isEmpty()) {
            mPagerPromos.setCurrentItem(1);
        }
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
    }

    @Override
    public void showRedeemFullError(CatalogsValueEntity item, String title) {
        if (getActivity() == null || !isAdded()) {
            return;
        }

        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        View view = LayoutInflater.from(getContext())
                .inflate(R.layout.layout_tp_network_error_large, null, false);

        ImageView img = view.findViewById(R.id.img_error);
        img.setImageResource(R.drawable.ic_tp_error_redeem_full);
        TextView titleText = view.findViewById(R.id.text_title_error);
        titleText.setText(R.string.tp_label_too_many_access);
        TextView label = view.findViewById(R.id.text_label_error);
        label.setText(R.string.tp_label_wait);

        view.findViewById(R.id.text_failed_action).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mPresenter.startSaveCoupon(item);
            }
        });

        adb.setView(view);
        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }
}
