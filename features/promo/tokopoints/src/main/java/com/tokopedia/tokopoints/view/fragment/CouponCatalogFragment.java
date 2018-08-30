package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.adapter.CouponCatalogInfoPagerAdapter;
import com.tokopedia.tokopoints.view.contract.CouponCatalogContract;
import com.tokopedia.tokopoints.view.model.CatalogStatusItem;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.presenter.CouponCatalogPresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.ImageUtil;
import com.tokopedia.tokopoints.view.util.TabUtil;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponCatalogFragment extends BaseDaggerFragment implements CouponCatalogContract.View, View.OnClickListener {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private ViewFlipper mContainerMain;
    private Subscription mSubscriptionCouponTimer;
    private Subscription mSubscriptionCatalogTimer;
    private int mRefreshRepeatCount = 0;
    private String mCouponRealCode;

    @Inject
    public CouponCatalogPresenter mPresenter;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new CouponCatalogFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_coupon_catalog, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initListener();

        if (getArguments() == null) {
            if (getActivity() != null) {
                getActivity().finish();
            }
            return;
        }

        String code = getArguments().getString(CommonConstant.EXTRA_CATALOG_CODE);
        if (code == null || code.isEmpty()) {
            code = getArguments().getString(CommonConstant.EXTRA_COUPON_CODE);
            if (code == null || code.isEmpty()) {
                if (getActivity() != null) {
                    getActivity().finish();
                }

                return;
            }
            mPresenter.getCouponDetail(code);
        } else {
            mPresenter.getCatalogDetail(code);
        }
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();

        if (mSubscriptionCatalogTimer != null && !mSubscriptionCatalogTimer.isUnsubscribed()) {
            mSubscriptionCatalogTimer.unsubscribe();
        }

        if (mSubscriptionCouponTimer != null && !mSubscriptionCouponTimer.isUnsubscribed()) {
            mSubscriptionCouponTimer.unsubscribe();
        }

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void showError() {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Override
    public void onEmptyCatalog() {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Override
    public void hideLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    @Override
    public void populateDetail(CatalogsValueEntity data) {
        setCatalogToUi(data);
    }

    @Override
    public void populateDetail(CouponValueEntity data) {
        setCouponToUi(data);
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
        if (source.getId() == R.id.text_my_coupon) {
            startActivity(MyCouponListingActivity.getCallingIntent(getActivityContext()));
        }
    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container);
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);
        getView().findViewById(R.id.text_my_coupon).setOnClickListener(this);
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

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
        adb.setNegativeButton(R.string.tp_label_use, (dialogInterface, i) ->
                showRedeemCouponDialog(cta, code, title)
        );

        adb.setPositiveButton(R.string.tp_label_view_coupon, (dialogInterface, i) ->
                startActivity(MyCouponListingActivity.getCallingIntent(getActivityContext()))
        );

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

    @Override
    public void showRedeemFullError(CatalogsValueEntity item, String title, String desc) {

    }

    @Override
    public void onSuccessPoints(String point) {
        if (getView() == null) {
            return;
        }

        TextView textUserPoint = getView().findViewById(R.id.text_point_value);
        textUserPoint.setText(point);
    }

    @Override
    public void onErrorPoint(String errorMessage) {
        //TODO @lavekush need to handle it
    }

    @Override
    public void onRealCodeReFresh(String realCode) {
        if (getView() == null || mSubscriptionCouponTimer == null) {
            return;
        }

        TextView btnAction2 = getView().findViewById(R.id.button_action_2);
        ProgressBar progressBar = getView().findViewById(R.id.progress_refetch_code);

        if (realCode != null && !realCode.isEmpty()) {
            btnAction2.setText(R.string.tp_label_use);
            btnAction2.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            mSubscriptionCouponTimer.unsubscribe();
            return;
        }

        if (mRefreshRepeatCount >= CommonConstant.MAX_COUPON_RE_FETCH_COUNT) {
            btnAction2.setText(R.string.tp_label_refresh_repeat);
            btnAction2.setEnabled(true);
            progressBar.setVisibility(View.GONE);
            btnAction2.setTextColor(ContextCompat.getColor(getActivityContext(), R.color.white));
            mSubscriptionCouponTimer.unsubscribe();
        }
    }

    @Override
    public void onRealCodeReFreshError() {
        if (getView() == null || mSubscriptionCouponTimer == null) {
            return;
        }

        TextView btnAction2 = getView().findViewById(R.id.button_action_2);
        ProgressBar progressBar = getView().findViewById(R.id.progress_refetch_code);
        btnAction2.setText(R.string.tp_label_refresh_repeat);
        btnAction2.setEnabled(true);
        progressBar.setVisibility(View.GONE);
        mSubscriptionCouponTimer.unsubscribe();
    }

    @Override
    public void refreshCatalog(@NonNull CatalogStatusItem data) {
        if (getView() == null) {
            return;
        }

        TextView quota = getView().findViewById(R.id.text_quota_count);
        TextView pointValue = getView().findViewById(R.id.text_point_value_coupon);
        TextView btnAction1 = getView().findViewById(R.id.button_action_1);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);

        btnAction1.setEnabled(!data.isDisabledButton());

        if (data.isDisabledButton()) {
            btnAction1.setTextColor(ContextCompat.getColor(btnAction1.getContext(), R.color.black_12));
        } else {
            btnAction1.setTextColor(ContextCompat.getColor(btnAction1.getContext(), R.color.white));
        }

        //Quota text handling
        if (data.getUpperTextDesc() == null || data.getUpperTextDesc().isEmpty()) {
            quota.setVisibility(View.GONE);
        } else {
            quota.setVisibility(View.VISIBLE);
            StringBuilder upperText = new StringBuilder();
            for (int i = 0; i < data.getUpperTextDesc().size(); i++) {
                if (i == 1) {
                    //exclusive case for handling font color of second index.
                    upperText.append("<font color='#ff5722'>" + data.getUpperTextDesc().get(i) + "</font>");
                } else {
                    upperText.append(data.getUpperTextDesc().get(i)).append(" ");
                }
            }
            quota.setText(MethodChecker.fromHtml(upperText.toString()));
        }

        //disabling the coupons if not eligible for current membership
        if (data.isDisabled()) {
            ImageUtil.dimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.black_54));
        } else {
            ImageUtil.unDimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.orange_red));
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

    //setting catalog values to ui
    private void setCatalogToUi(@NonNull CatalogsValueEntity data) {
        if (getView() == null) {
            return;
        }

        TextView quota = getView().findViewById(R.id.text_quota_count);
        TextView description = getView().findViewById(R.id.text_description);
        TextView pointValue = getView().findViewById(R.id.text_point_value_coupon);
        TextView timeLabel = getView().findViewById(R.id.text_time_label);
        TextView timeValue = getView().findViewById(R.id.text_time_value);
        TextView disabledError = getView().findViewById(R.id.text_disabled_error);
        TextView btnAction1 = getView().findViewById(R.id.button_action_1);
        TextView btnAction2 = getView().findViewById(R.id.button_action_2);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);
        ImageView imgTime = getView().findViewById(R.id.img_time);
        ImageView imgPoint = getView().findViewById(R.id.img_points_stack_coupon);
        TextView labelPoint = getView().findViewById(R.id.text_point_label);
        TextView textDiscount = getView().findViewById(R.id.text_point_discount);

        btnAction1.setVisibility(View.VISIBLE);
        btnAction2.setVisibility(View.GONE);
        btnAction1.setEnabled(!data.isDisabledButton());
        description.setText(data.getTitle());
        btnAction1.setText(data.getButtonStr());
        ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageUrlMobile());
        //setting points info if exist in response
        if (data.getPointsStr() == null || data.getPointsStr().isEmpty()) {
            pointValue.setVisibility(View.GONE);
            imgPoint.setVisibility(View.GONE);
        } else {
            imgPoint.setVisibility(View.VISIBLE);
            pointValue.setVisibility(View.VISIBLE);
            pointValue.setText(data.getPointsStr());
        }

        //setting expiry time info if exist in response
        if (data.getExpiredLabel() == null || data.getExpiredLabel().isEmpty()) {
            timeLabel.setVisibility(View.GONE);
            timeValue.setVisibility(View.GONE);
            imgTime.setVisibility(View.GONE);
        } else {
            timeLabel.setVisibility(View.VISIBLE);
            timeValue.setVisibility(View.VISIBLE);
            imgTime.setVisibility(View.VISIBLE);
            timeLabel.setText(data.getExpiredLabel());
            timeValue.setText(data.getExpiredStr());
        }

        //Quota text handling
        if (data.getUpperTextDesc() == null || data.getUpperTextDesc().isEmpty()) {
            quota.setVisibility(View.GONE);
        } else {
            quota.setVisibility(View.VISIBLE);
            StringBuilder upperText = new StringBuilder();
            for (int i = 0; i < data.getUpperTextDesc().size(); i++) {
                if (i == 1) {
                    //exclusive case for handling font color of second index.
                    upperText.append("<font color='#ff5722'>" + data.getUpperTextDesc().get(i) + "</font>");
                } else {
                    upperText.append(data.getUpperTextDesc().get(i)).append(" ");
                }
            }
            quota.setText(MethodChecker.fromHtml(upperText.toString()));
        }

        //Quota text handling
        if (data.getDisableErrorMessage() == null || data.getDisableErrorMessage().isEmpty()) {
            disabledError.setVisibility(View.GONE);
        } else {
            disabledError.setVisibility(View.VISIBLE);
            disabledError.setText(data.getDisableErrorMessage());
        }

        //disabling the coupons if not eligible for current membership
        if (data.isDisabled()) {
            ImageUtil.dimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.black_54));
        } else {
            ImageUtil.unDimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.orange_red));
        }

        if (data.isDisabledButton()) {
            btnAction1.setTextColor(ContextCompat.getColor(btnAction1.getContext(), R.color.black_12));
        } else {
            btnAction1.setTextColor(ContextCompat.getColor(btnAction1.getContext(), R.color.white));
        }

        if (data.getPointsSlash() <= 0) {
            labelPoint.setVisibility(View.GONE);
        } else {
            labelPoint.setVisibility(View.VISIBLE);
            labelPoint.setText(data.getPointsSlashStr());
            labelPoint.setPaintFlags(labelPoint.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }

        if (data.getDiscountPercentage() <= 0) {
            textDiscount.setVisibility(View.GONE);
        } else {
            textDiscount.setVisibility(View.VISIBLE);
            textDiscount.setText(data.getDiscountPercentageStr());
        }

        btnAction1.setOnClickListener(v -> {
            //call validate api the show dialog
            mPresenter.startValidateCoupon(data);
        });

        setupInfoPager(data.getHowToUse(), data.getTnc());

        //start catalog status timer
        mSubscriptionCatalogTimer = Observable.interval(CommonConstant.DEFAULT_AUTO_REFRESH_S, CommonConstant.DEFAULT_AUTO_REFRESH_S, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong ->
                        mPresenter.fetchLatestStatus(Arrays.asList(data.getId()))
                );
    }

    private void setCouponToUi(CouponValueEntity data) {
        if (getView() == null) {
            return;
        }

        TextView description = getView().findViewById(R.id.text_description);
        TextView label = getView().findViewById(R.id.text_time_label);
        TextView value = getView().findViewById(R.id.text_time_value);
        TextView btnAction1 = getView().findViewById(R.id.button_action_1);
        TextView btnAction2 = getView().findViewById(R.id.button_action_2);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);
        ImageView imgLabel = getView().findViewById(R.id.img_time);
        TextView textMinExchange = getView().findViewById(R.id.text_min_exchange);
        TextView textMinExchangeValue = getView().findViewById(R.id.text_min_exchange_value);
        ImageView imgMinExchange = getView().findViewById(R.id.img_min_exchange);
        ProgressBar progressBar = getView().findViewById(R.id.progress_refetch_code);

        description.setText(data.getTitle());
        ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageUrlMobile());

        btnAction1.setVisibility(View.GONE);

        if (data.getUsage() != null) {
            imgLabel.setImageResource(R.drawable.ic_tp_time);
            label.setVisibility(View.VISIBLE);
            value.setVisibility(View.VISIBLE);
            imgLabel.setVisibility(View.VISIBLE);
            btnAction2.setVisibility(View.VISIBLE);
            value.setText(data.getUsage().getUsageStr());
        }

        if (data.getMinimumUsage() != null && !data.getMinimumUsage().isEmpty()) {
            imgMinExchange.setVisibility(View.VISIBLE);
            textMinExchange.setVisibility(View.VISIBLE);
            textMinExchangeValue.setVisibility(View.VISIBLE);
            textMinExchangeValue.setText(data.getMinimumUsage());
        }

        value.setTextColor(ContextCompat.getColor(btnAction1.getContext(), R.color.medium_green));
        imgLabel.setVisibility(View.VISIBLE);
        imgLabel.setImageResource(R.drawable.bg_tp_time_greeen);
        btnAction2.setOnClickListener(v -> {
            if (btnAction2.getText().toString().equalsIgnoreCase(getString(R.string.tp_label_use))) {
                mPresenter.showRedeemCouponDialog(data.getCta(), mCouponRealCode, data.getTitle());
            } else {
                if (getArguments() != null && getArguments().getString(CommonConstant.EXTRA_COUPON_CODE) != null) {
                    btnAction2.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    btnAction2.setText("");
                    mPresenter.reFetchRealCode(getArguments().getString(CommonConstant.EXTRA_COUPON_CODE));
                }
            }
        });
        setupInfoPager(data.getHowToUse(), data.getTnc());

        if (data.getRealCode() != null && !data.getRealCode().isEmpty()) {
            mCouponRealCode = data.getRealCode();
            btnAction2.setText(R.string.tp_label_use);
            btnAction2.setEnabled(true);
            progressBar.setVisibility(View.GONE);
        } else {
            //check for real_code and start rxjava-timer
            btnAction2.setEnabled(false);
            progressBar.setVisibility(View.VISIBLE);

            mSubscriptionCouponTimer = Observable.interval(CommonConstant.COUPON_RE_FETCH_DELAY_S, CommonConstant.COUPON_RE_FETCH_DELAY_S, TimeUnit.SECONDS)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(aLong -> {
                        if (getArguments() != null && getArguments().getString(CommonConstant.EXTRA_COUPON_CODE) != null) {
                            mPresenter.reFetchRealCode(getArguments().getString(CommonConstant.EXTRA_COUPON_CODE));
                            mRefreshRepeatCount++;
                        }
                    });
        }
    }

    private void setupInfoPager(String info, String tnc) {
        if (getView() == null) {
            return;
        }

        CouponCatalogInfoPagerAdapter adapter = new CouponCatalogInfoPagerAdapter(getActivityContext(), info, tnc);
        ViewPager pager = getView().findViewById(R.id.view_pager_info);
        TabLayout tabs = getView().findViewById(R.id.tab_layout_info);
        pager.setAdapter(adapter);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));

        //excluding extra padding from tabs
        TabUtil.wrapTabIndicatorToTitle(tabs,
                (int) getResources().getDimension(R.dimen.tp_margin_medium),
                (int) getResources().getDimension(R.dimen.tp_margin_regular));
    }
}
