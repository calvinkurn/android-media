package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.SnackbarManager;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.activity.SendGiftActivity;
import com.tokopedia.tokopoints.view.adapter.CouponCatalogInfoPagerAdapter;
import com.tokopedia.tokopoints.view.contract.CouponCatalogContract;
import com.tokopedia.tokopoints.view.customview.SwipeCardView;
import com.tokopedia.tokopoints.view.model.CatalogStatusItem;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.model.CouponSwipeUpdate;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.presenter.CouponCatalogPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.ImageUtil;
import com.tokopedia.tokopoints.view.util.TabUtil;
import com.tokopedia.tokopoints.view.util.WrapContentHeightViewPager;

import java.util.Arrays;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CouponCatalogFragment extends BaseDaggerFragment implements CouponCatalogContract.View, View.OnClickListener {
    private static final String FPM_DETAIL_TOKOPOINT = "ft_tokopoint_detail";
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private static final int CONTAINER_SWIPE = 1;
    private ViewFlipper mContainerMain;
    private Subscription mSubscriptionCouponTimer;
    private Subscription mSubscriptionCatalogTimer;
    private int mRefreshRepeatCount = 0;
    private String mCouponRealCode;
    private String mCouponName;
    public CountDownTimer mTimer;
    private SwipeCardView mSwipeCardView;
    private TextView mBtnQrCode;
    private TextView mBtnBarCode;
    private View mViewCodeSeparator;
    private TextView mTextSwipeNote;
    private PerformanceMonitoring fpmDetailTokopoint;
    private final static String SCREEN_NAME="/tokopoints/tukar-point/detail";

    @Inject
    public CouponCatalogPresenter mPresenter;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new CouponCatalogFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        fpmDetailTokopoint = PerformanceMonitoring.start(FPM_DETAIL_TOKOPOINT);
        super.onCreate(savedInstanceState);
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

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

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
        AnalyticsTrackerUtil.sendScreenEvent(getActivity(), getScreenName());
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
        return SCREEN_NAME;
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
                }
        );

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
        TextView btnAction2 = getView().findViewById(R.id.button_action_2);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);

        btnAction2.setEnabled(!data.isDisabledButton());

        if (data.isDisabledButton()) {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.black_12));
        } else {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.white));
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

        mCouponName = data.getTitle();
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

        btnAction2.setVisibility(View.VISIBLE);
        btnAction2.setEnabled(!data.isDisabledButton());
        description.setText(data.getTitle());
        btnAction2.setText(data.getButtonStr());
        btnAction2.setBackgroundResource(R.drawable.bg_button_orange);

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
            timeValue.setTextColor(ContextCompat.getColor(getAppContext(), R.color.black_70));
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
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.black_12));
        } else {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.white));
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

        if (data.getIsGift() == 1) {
            LinearLayout.LayoutParams layoutParams = (LinearLayout.LayoutParams) btnAction1.getLayoutParams();
            layoutParams.rightMargin = getResources().getDimensionPixelOffset(R.dimen.tp_margin_medium);
            btnAction1.setVisibility(View.VISIBLE);
            btnAction1.setText(R.string.tp_label_send);
            btnAction1.setOnClickListener(view -> mPresenter.startSendGift(data.getId(), data.getTitle(), data.getPointsStr()));
        } else {
            btnAction1.setVisibility(View.GONE);
        }

        btnAction2.setOnClickListener(v -> {
            //call validate api the show dialog
            mPresenter.startValidateCoupon(data);

            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                    mCouponName);
        });

        setupInfoPager(data.getHowToUse(), data.getTnc(), true);

        //start catalog status timer
        mSubscriptionCatalogTimer = Observable.interval(CommonConstant.DEFAULT_AUTO_REFRESH_S, CommonConstant.DEFAULT_AUTO_REFRESH_S, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(aLong ->
                        mPresenter.fetchLatestStatus(Arrays.asList(data.getId()))
                );

        //Coupon impression ga
        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.VIEW_COUPON,
                mCouponName);
    }

    private void setCouponToUi(CouponValueEntity data) {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.layout_point).setVisibility(View.GONE);

        mCouponName = data.getTitle();
        TextView description = getView().findViewById(R.id.text_description);
        TextView label = getView().findViewById(R.id.text_time_label);
        TextView value = getView().findViewById(R.id.text_time_value);
        TextView btnAction1 = getView().findViewById(R.id.button_action_1);
        TextView btnAction2 = getView().findViewById(R.id.button_action_2);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);
        ImageView imgLabel = getView().findViewById(R.id.img_time);
        TextView textMinExchangeValue = getView().findViewById(R.id.text_min_exchange_value);
        TextView textMinExchangeLavel = getView().findViewById(R.id.text_min_exchange);
        ImageView imgMinExchange = getView().findViewById(R.id.img_min_exchange);
        ProgressBar progressBar = getView().findViewById(R.id.progress_refetch_code);
        ViewFlipper actionContainer = getView().findViewById(R.id.lin_container_button);

        description.setText(data.getTitle());
        ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageUrlMobile());

        btnAction1.setVisibility(View.GONE);
        btnAction2.setBackgroundResource(R.drawable.bg_button_green);

        if (data.getUsage() != null) {
            imgLabel.setImageResource(R.drawable.ic_tp_time);
            label.setVisibility(View.VISIBLE);
            label.setText(data.getUsage().getText());
            value.setVisibility(View.VISIBLE);
            imgLabel.setVisibility(View.VISIBLE);
            value.setText(data.getUsage().getUsageStr());

            if (data.getUsage().getBtnUsage() != null) {
                if (data.getUsage().getBtnUsage().getType().equalsIgnoreCase("invisible")) {
                    btnAction2.setVisibility(View.GONE);
                } else {
                    btnAction2.setVisibility(View.VISIBLE);
                }
            }
        }

        if (TextUtils.isEmpty(data.getMinimumUsageLabel())) {
            textMinExchangeLavel.setVisibility(View.GONE);
            imgMinExchange.setVisibility(View.GONE);
        } else {
            imgMinExchange.setVisibility(View.VISIBLE);
            textMinExchangeLavel.setVisibility(View.VISIBLE);
            textMinExchangeLavel.setText(data.getMinimumUsageLabel());
        }

        if (TextUtils.isEmpty(data.getMinimumUsage())) {
            textMinExchangeValue.setVisibility(View.GONE);
        } else {
            textMinExchangeValue.setVisibility(View.VISIBLE);
            textMinExchangeValue.setText(data.getMinimumUsage());
        }

        imgLabel.setVisibility(View.VISIBLE);
        imgLabel.setImageResource(R.drawable.bg_tp_time_greeen);
        btnAction2.setOnClickListener(v -> {
            if (btnAction2.getText().toString().equalsIgnoreCase(getString(R.string.tp_label_use))) {
                mPresenter.showRedeemCouponDialog(data.getCta(), mCouponRealCode, data.getTitle());

                AnalyticsTrackerUtil.sendEvent(getContext(),
                        AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                        AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                        AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                        mCouponName);
            } else {
                if (getArguments() != null && getArguments().getString(CommonConstant.EXTRA_COUPON_CODE) != null) {
                    btnAction2.setEnabled(false);
                    progressBar.setVisibility(View.VISIBLE);
                    btnAction2.setText("");
                    mPresenter.reFetchRealCode(getArguments().getString(CommonConstant.EXTRA_COUPON_CODE));
                }
            }
        });
        setupInfoPager(data.getHowToUse(), data.getTnc(), false);

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

        addCountDownTimer(data, value, btnAction2);

        if (data.getSwipe() != null && data.getSwipe().isNeedSwipe()) {
            actionContainer.setDisplayedChild(CONTAINER_SWIPE);

            mSwipeCardView = getView().findViewById(R.id.card_swipe);
            mBtnBarCode = getView().findViewById(R.id.btn_barcode);
            mBtnQrCode = getView().findViewById(R.id.btn_qrcode);
            mViewCodeSeparator = getView().findViewById(R.id.view_code_separator);
            mTextSwipeNote = getView().findViewById(R.id.text_swipe_note);
            mSwipeCardView.setTitle(data.getSwipe().getText());
            mSwipeCardView.setOnSwipeListener(new SwipeCardView.OnSwipeListener() {
                @Override
                public void onComplete() {
                    if (data.getSwipe().getPin().isPinRequire()) {
                        showPinPage(data.getRealCode(), data.getSwipe().getPin().getText());
                    } else {
                        mPresenter.swipeMyCoupon(data.getRealCode(), ""); //Empty for online partner
                    }
                }

                @Override
                public void onPartialSwipe() {

                }
            });

            if (!data.getSwipe().getNote().isEmpty()) {
                mTextSwipeNote.setText(data.getSwipe().getNote());
                mTextSwipeNote.setVisibility(View.VISIBLE);
                mTextSwipeNote.setTextColor(ContextCompat.getColor(getActivityContext(), R.color.black_70));
            }

            if (data.getSwipe().getPartnerCode() != null
                    && !data.getSwipe().getPartnerCode().isEmpty()) {
                mSwipeCardView.setCouponCode(data.getSwipe().getPartnerCode());
            }
        }

        //Coupon impression ga
        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.VIEW_MY_COUPON_DETAIL,
                mCouponName);
    }

    private void setupInfoPager(String info, String tnc, boolean isCatalog) {
        if (getView() == null) {
            return;
        }

        CouponCatalogInfoPagerAdapter adapter = new CouponCatalogInfoPagerAdapter(getActivityContext(), info, tnc);
        WrapContentHeightViewPager pager = getView().findViewById(R.id.view_pager_info);

        pager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 0) {
                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            isCatalog ? AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL : AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_KETENTUAN,
                            mCouponName);
                } else {
                    AnalyticsTrackerUtil.sendEvent(getContext(),
                            AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                            isCatalog ? AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL : AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA_DETAIL,
                            AnalyticsTrackerUtil.ActionKeys.CLICK_CARA_PAKAI,
                            mCouponName);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        TabLayout tabs = getView().findViewById(R.id.tab_layout_info);
        pager.setAdapter(adapter);
        pager.measure(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        pager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabs));
        tabs.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(pager));

        //excluding extra padding from tabs
        TabUtil.wrapTabIndicatorToTitle(tabs,
                (int) getResources().getDimension(R.dimen.tp_margin_medium),
                (int) getResources().getDimension(R.dimen.tp_margin_regular));
    }

    @Override
    public void gotoSendGiftPage(int id, String title, String pointStr) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_COUPON_ID, id);
        bundle.putString(CommonConstant.EXTRA_COUPON_TITLE, title);
        bundle.putString(CommonConstant.EXTRA_COUPON_POINT, pointStr);
        startActivity(SendGiftActivity.getCallingIntent(getActivity(), bundle));
    }

    private void addCountDownTimer(CouponValueEntity item, TextView label, TextView btnContinue) {
        if (mTimer != null || getView() == null) {
            mTimer.cancel();
        }

        if (item.getUsage().getActiveCountDown() < 1) {
            if (item.getUsage().getExpiredCountDown() > 0
                    && item.getUsage().getExpiredCountDown() <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                ProgressBar progressBar = getView().findViewById(R.id.progress_timer);
                progressBar.setVisibility(View.VISIBLE);
                progressBar.setMax((int) CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S);
                mTimer = new CountDownTimer(item.getUsage().getExpiredCountDown() * 1000, 1000) {
                    @Override
                    public void onTick(long l) {
                        label.setPadding(getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                                getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall),
                                getResources().getDimensionPixelSize(R.dimen.tp_padding_regular),
                                getResources().getDimensionPixelSize(R.dimen.tp_padding_xsmall));
                        item.getUsage().setExpiredCountDown(l / 1000);
                        int seconds = (int) (l / 1000) % 60;
                        int minutes = (int) ((l / (1000 * 60)) % 60);
                        int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                        label.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                        progressBar.setProgress((int) l / 1000);
                    }

                    @Override
                    public void onFinish() {
                        progressBar.setVisibility(View.GONE);
                        label.setText("00 : 00 : 00");
                        btnContinue.setText("Expired");
                        btnContinue.setEnabled(false);
                        btnContinue.setTextColor(ContextCompat.getColor(btnContinue.getContext(), R.color.black_12));
                    }
                }.start();
            } else {
                btnContinue.setText(item.getUsage().getBtnUsage().getText());
                btnContinue.setEnabled(true);
                btnContinue.setTextColor(ContextCompat.getColor(btnContinue.getContext(), R.color.white));
            }
        } else {
            if (item.getUsage().getActiveCountDown() > 0) {
                btnContinue.setEnabled(false);
                if (item.getUsage().getActiveCountDown() <= CommonConstant.COUPON_SHOW_COUNTDOWN_MAX_LIMIT_S) {
                    mTimer = new CountDownTimer(item.getUsage().getActiveCountDown() * 1000, 1000) {
                        @Override
                        public void onTick(long l) {
                            item.getUsage().setActiveCountDown(l / 1000);
                            int seconds = (int) (l / 1000) % 60;
                            int minutes = (int) ((l / (1000 * 60)) % 60);
                            int hours = (int) ((l / (1000 * 60 * 60)) % 24);
                            btnContinue.setText(String.format(Locale.ENGLISH, "%02d : %02d : %02d", hours, minutes, seconds));
                        }

                        @Override
                        public void onFinish() {
                            btnContinue.setEnabled(true);
                            btnContinue.setText(item.getUsage().getBtnUsage().getText());
                        }
                    }.start();
                }
            } else {
                btnContinue.setText(item.getUsage().getUsageStr());
                btnContinue.setEnabled(true);
            }
        }
    }

    public void onSwipeResponse(CouponSwipeUpdate data, String qrCodeLink, String barCodeLink) {
        mSwipeCardView.setCouponCode(data.getPartnerCode());

        if (qrCodeLink != null && !qrCodeLink.isEmpty()) {
            mBtnQrCode.setVisibility(View.VISIBLE);
            mViewCodeSeparator.setVisibility(View.VISIBLE);
        }

        if (barCodeLink != null && !barCodeLink.isEmpty()) {
            mBtnBarCode.setVisibility(View.VISIBLE);
        }

        if (data.getNote() != null && !data.getNote().isEmpty()) {
            mTextSwipeNote.setVisibility(View.VISIBLE);
            mTextSwipeNote.setText(data.getNote());
            mTextSwipeNote.setTextColor(ContextCompat.getColor(getActivityContext(), R.color.black_38));
        } else {
            mTextSwipeNote.setVisibility(View.GONE);
        }
    }

    public void onSwipeError(String errorMessage) {
        mSwipeCardView.reset();
        SnackbarManager.make(mSwipeCardView, errorMessage, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void onFinishRendering() {
        if (fpmDetailTokopoint != null)
            fpmDetailTokopoint.stopTrace();
    }

    public void showPinPage(String code, String pinInfo) {
        if (getActivity() == null || getActivity().isFinishing()) {
            return;
        }

        Fragment fragment = new ValidateMerchantPinFragment();
        Bundle bundle = new Bundle();
        bundle.putString(CommonConstant.EXTRA_PIN_INFO, pinInfo);
        bundle.putString(CommonConstant.EXTRA_COUPON_ID, code);
        fragment.setArguments(bundle);

        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(com.tokopedia.abstraction.R.id.parent_view, fragment, ValidateMerchantPinFragment.class.getCanonicalName())
                .addToBackStack(ValidateMerchantPinFragment.class.getCanonicalName())
                .commit();
    }
}
