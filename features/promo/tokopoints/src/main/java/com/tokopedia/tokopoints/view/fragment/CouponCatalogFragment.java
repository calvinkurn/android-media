package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.AlertDialog;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.analytics.performance.PerformanceMonitoring;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.CouponListingStackedActivity;
import com.tokopedia.tokopoints.view.contract.CouponCatalogContract;
import com.tokopedia.tokopoints.view.customview.ServerErrorView;
import com.tokopedia.tokopoints.view.model.CatalogStatusItem;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.presenter.CouponCatalogPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;
import com.tokopedia.tokopoints.view.util.ImageUtil;
import com.tokopedia.unifyprinciples.Typography;
import com.tokopedia.user.session.UserSession;
import com.tokopedia.webview.TkpdWebView;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


import static com.tokopedia.tokopoints.view.util.CommanUtilsKt.getLessDisplayData;
import static com.tokopedia.tokopoints.view.util.CommonConstant.COUPON_MIME_TYPE;
import static com.tokopedia.tokopoints.view.util.CommonConstant.UTF_ENCODING;

public class CouponCatalogFragment extends BaseDaggerFragment implements CouponCatalogContract.View, View.OnClickListener {
    private static final String FPM_DETAIL_TOKOPOINT = "ft_tokopoint_detail";
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;


    private static final int REQUEST_CODE_LOGIN = 1;
    private ViewFlipper mContainerMain;
    private ServerErrorView serverErrorView;
    private Subscription mSubscriptionCouponTimer;
    private Subscription mSubscriptionCatalogTimer;
    private int mRefreshRepeatCount = 0;
    private String mCouponName;
    public CountDownTimer mTimer;
    private PerformanceMonitoring fpmDetailTokopoint;
    UserSession mUserSession;
    private CatalogsValueEntity catalogsValueEntity;
    private TextView pointValueText;
    Typography pointValue;
    Typography textUserPoint;
    String code;
    String userPoints;

    @Inject
    public CouponCatalogPresenter mPresenter;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new CouponCatalogFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        mUserSession = new UserSession(getAppContext());
        fpmDetailTokopoint = PerformanceMonitoring.start(FPM_DETAIL_TOKOPOINT);
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_coupon_catalog, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        int id = item.getItemId();
        if (id == R.id.action_menu_share) {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, CommonConstant.WebLink.DETAIL + getArguments().getString(CommonConstant.EXTRA_CATALOG_CODE));
            startActivity(Intent.createChooser(sharingIntent, null));
            return true;
        }
        return true;
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

        code = getArguments().getString(CommonConstant.EXTRA_CATALOG_CODE);
        mPresenter.getCatalogDetail(code);
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
        return getContext();
    }

    @Override
    public void showLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void showError(boolean hasInternet) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
        serverErrorView.showErrorUi(hasInternet);
    }

    @Override
    public void hideLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    @Override
    public void populateDetail(CatalogsValueEntity data) {
        catalogsValueEntity = data;
        setCatalogToUi(data);
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
        return AnalyticsTrackerUtil.ScreenKeys.COUPON_CATALOG_SCREEN_NAME;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.text_my_coupon) {
            startActivity(CouponListingStackedActivity.getCallingIntent(getActivityContext()));
        }
    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container);
        serverErrorView = view.findViewById(R.id.server_error_view);

    }

    private void initListener() {
        if (getView() == null) {
            return;
        }
        serverErrorView.setErrorButtonClickListener((view)->{
            mPresenter.getCatalogDetail(code);
        });
    }

    @Override
    public void openWebView(String url) {
        RouteManager.route(getContext(),String.format("%s?url=%s", ApplinkConst.WEBVIEW,url));
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
                    startActivity(CouponListingStackedActivity.getCallingIntent(getActivityContext()));

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
    public void onSuccessPoints(String point) {
        if (getView() == null) {
            return;
        }

        userPoints = point;
        textUserPoint = getView().findViewById(R.id.text_point_value);
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

        Typography btnAction2 = getView().findViewById(R.id.button_action_2);

        if (realCode != null && !realCode.isEmpty()) {
            btnAction2.setText(R.string.tp_label_use);
            btnAction2.setEnabled(true);
            mSubscriptionCouponTimer.unsubscribe();
            return;
        }

        if (mRefreshRepeatCount >= CommonConstant.MAX_COUPON_RE_FETCH_COUNT) {
            btnAction2.setText(R.string.tp_label_refresh_repeat);
            btnAction2.setEnabled(true);
            btnAction2.setTextColor(ContextCompat.getColor(getActivityContext(), com.tokopedia.design.R.color.white));
            mSubscriptionCouponTimer.unsubscribe();
        }
    }

    @Override
    public void onRealCodeReFreshError() {
        if (getView() == null || mSubscriptionCouponTimer == null) {
            return;
        }

        Typography btnAction2 = getView().findViewById(R.id.button_action_2);
        btnAction2.setText(R.string.tp_label_refresh_repeat);
        btnAction2.setEnabled(true);
        mSubscriptionCouponTimer.unsubscribe();
    }

    @Override
    public void refreshCatalog(@NonNull CatalogStatusItem data) {
        if (getView() == null) {
            return;
        }

        Typography quota = getView().findViewById(R.id.text_quota_count);
        pointValue = getView().findViewById(R.id.text_point_value_coupon);
        Typography btnAction2 = getView().findViewById(R.id.button_action_2);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);

        btnAction2.setEnabled(!data.isDisabledButton());

        if (data.isDisabledButton()) {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.disabled_color));
        } else {
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), com.tokopedia.design.R.color.white));
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
        } else {
            ImageUtil.unDimImage(imgBanner);
        }

        pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), com.tokopedia.design.R.color.unify_Y500));
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
                    com.tokopedia.design.R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }

        if (dialog.getButton(AlertDialog.BUTTON_NEGATIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setAllCaps(false);
            dialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    com.tokopedia.design.R.color.grey_warm));
        }
    }

    //setting catalog values to ui
    private void setCatalogToUi(@NonNull CatalogsValueEntity data) {
        if (getView() == null) {
            return;
        }

        mCouponName = data.getTitle();
        Typography quota = getView().findViewById(R.id.text_quota_count);
        Typography description = getView().findViewById(R.id.text_description);
        Typography disabledError = getView().findViewById(R.id.text_disabled_error);
        ConstraintLayout giftSectionMainLayout = getView().findViewById(R.id.gift_section_main_layout);
        Typography giftImage = getView().findViewById(R.id.gift_image);
        Typography giftButton = getView().findViewById(R.id.gift_btn);
        View bottomSeparator = getView().findViewById(R.id.tp_bottom_separator);
        giftImage.setCompoundDrawablesWithIntrinsicBounds(MethodChecker.getDrawable
                (getActivity(), R.drawable.ic_catalog_gift_btn), null, null, null);
        Typography btnAction2 = getView().findViewById(R.id.button_action_2);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);
        Typography labelPoint = getView().findViewById(R.id.text_point_label);
        Typography textDiscount = getView().findViewById(R.id.text_point_discount);

        btnAction2.setVisibility(View.VISIBLE);
        btnAction2.setEnabled(!data.isDisabledButton());
        description.setText(data.getTitle());
        btnAction2.setText(data.getButtonStr());
        btnAction2.setBackgroundResource(R.drawable.bg_button_buy_orange_tokopoints);

        ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageUrlMobile());

        TkpdWebView tvHowToUse = getView().findViewById(R.id.how_to_use_content);
        TkpdWebView tvTnc = getView().findViewById(R.id.tnc_content);
        Typography tncSeeMore = getView().findViewById(R.id.tnc_see_more);
        Typography howToUseSeeMore = getView().findViewById(R.id.how_to_use_see_more);
        tvTnc.loadData(getLessDisplayData(data.getTnc(), tncSeeMore), COUPON_MIME_TYPE, UTF_ENCODING);
        tvHowToUse.loadData(getLessDisplayData(data.getHowToUse(), howToUseSeeMore), COUPON_MIME_TYPE, UTF_ENCODING);
        tncSeeMore.setOnClickListener(v -> {
            loadWebViewInBottomsheet(data.getTnc(), getString(R.string.tnc_coupon_catalog));
        });
        howToUseSeeMore.setOnClickListener(v -> {
            loadWebViewInBottomsheet(data.getHowToUse(), getString(R.string.how_to_use_coupon_catalog));
        });

        Typography pointValue = getView().findViewById(R.id.text_point_value_coupon);
        if (data.getPointsStr() == null || data.getPointsStr().isEmpty()) {
            pointValue.setVisibility(View.GONE);
        } else {
            pointValue.setVisibility(View.VISIBLE);
            pointValue.setText(data.getPointsStr());
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
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), com.tokopedia.design.R.color.black_54));
        } else {
            ImageUtil.unDimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), com.tokopedia.design.R.color.orange_red));
        }

        if (data.isDisabledButton()) {
            giftSectionMainLayout.setVisibility(View.GONE);
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), com.tokopedia.abstraction.R.color.black_12));
        } else {
            giftSectionMainLayout.setVisibility(View.VISIBLE);
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), com.tokopedia.design.R.color.white));
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
            if (data.isDisabledButton()) {
                giftSectionMainLayout.setVisibility(View.GONE);
                bottomSeparator.setVisibility(View.GONE);
            } else {
                giftSectionMainLayout.setVisibility(View.VISIBLE);
                bottomSeparator.setVisibility(View.VISIBLE);
                giftButton.setText(R.string.tp_label_send);
                giftButton.setOnClickListener(view -> mPresenter.startSendGift(data.getId(), data.getTitle(), data.getPointsStr(), data.getImageUrlMobile()));
            }
        } else {
            giftSectionMainLayout.setVisibility(View.GONE);
            bottomSeparator.setVisibility(View.GONE);
        }

        //hide gift section when user is in public page
        if (!mUserSession.isLoggedIn()){
            giftSectionMainLayout.setVisibility(View.GONE);
            bottomSeparator.setVisibility(View.GONE);
        }

        btnAction2.setOnClickListener(v -> {
            //call validate api the show dialog
            if (mUserSession.isLoggedIn()) {
                mPresenter.startValidateCoupon(data);
            } else {
                startActivityForResult(RouteManager.getIntent(getContext(), ApplinkConst.LOGIN), REQUEST_CODE_LOGIN);
            }
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_TUKAR,
                    mCouponName);
        });


        pointValueText = getView().findViewById(R.id.text_point_value_label);
        if (!mUserSession.isLoggedIn()) {
            pointValueText.setText("Masuk untuk tukar Points");
        }
        //start catalog status timer
        mSubscriptionCatalogTimer = Observable.interval(CommonConstant.DEFAULT_AUTO_REFRESH_S, CommonConstant.DEFAULT_AUTO_REFRESH_S, TimeUnit.MILLISECONDS)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Long>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onNext(Long aLong) {
                        mPresenter.fetchLatestStatus(Arrays.asList(data.getId()));
                    }
                });

        //Coupon impression ga
        AnalyticsTrackerUtil.sendEvent(getContext(),
                AnalyticsTrackerUtil.EventKeys.EVENT_VIEW_COUPON,
                AnalyticsTrackerUtil.CategoryKeys.PENUKARAN_POINT_DETAIL,
                AnalyticsTrackerUtil.ActionKeys.VIEW_COUPON,
                mCouponName);
    }



    private void loadWebViewInBottomsheet(String data, String title) {
        CloseableBottomSheetDialog bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        View view = getLayoutInflater().inflate(R.layout.catalog_bottomsheet, null, true);
        WebView webView = view.findViewById(R.id.catalog_webview);
        ImageView closeBtn = view.findViewById(R.id.close_button);
        Typography titleView = view.findViewById(R.id.title_closeable);
        webView.loadData(data, COUPON_MIME_TYPE, UTF_ENCODING);
        closeBtn.setOnClickListener((v) -> bottomSheet.dismiss());
        titleView.setText(title);
        bottomSheet.setCustomContentView(view, title, false);
        bottomSheet.show();
    }

    @Override
    public void gotoSendGiftPage(int id, String title, String pointStr, String banner) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_COUPON_ID, id);
        bundle.putString(CommonConstant.EXTRA_COUPON_TITLE, title);
        bundle.putString(CommonConstant.EXTRA_COUPON_POINT, pointStr);
        bundle.putString(CommonConstant.EXTRA_COUPON_BANNER, banner);

        SendGiftFragment sendGiftFragment = new SendGiftFragment();
        sendGiftFragment.setArguments(bundle);
        sendGiftFragment.show(getChildFragmentManager(), CommonConstant.FRAGMENT_DETAIL_TOKOPOINT);

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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOGIN) {
            pointValueText.setText(getResources().getString(R.string.points_saya));
            mPresenter.getCatalogDetail(code);
            if (catalogsValueEntity.isDisabled()) {
                mPresenter.startValidateCoupon(catalogsValueEntity);
            }
        }
    }
}
