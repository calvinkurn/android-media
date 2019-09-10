package com.tokopedia.promocheckout.detail.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.webview.TkpdWebView;
import com.tokopedia.abstraction.common.di.component.HasComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.promocheckout.R;
import com.tokopedia.promocheckout.detail.di.DaggerPromoCheckoutDetailComponent;
import com.tokopedia.promocheckout.detail.di.PromoCheckoutDetailComponent;
import com.tokopedia.promocheckout.detail.model.detailmodel.HachikoCatalogDetail;
import com.tokopedia.promocheckout.detail.view.presenter.CheckoutCatalogDetailContract;
import com.tokopedia.promocheckout.detail.view.presenter.CheckoutCatalogDetailPresenter;
import com.tokopedia.promocheckout.widget.ImageUtil;
import com.tokopedia.promocheckout.widget.PromoImageView;
import com.tokopedia.unifyprinciples.Typography;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import rx.Observable;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class CheckoutCatalogDetailFragment extends BottomSheetDialogFragment implements CheckoutCatalogDetailContract.View
{
    private static final String FPM_DETAIL_TOKOPOINT = "ft_tokopoint_detail";
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private static final String UTF_ENCODING = "UTF-8";
    private static final String COUPON_MIME_TYPE = "text/html";
    private static final String LIST_TAG_START = "<li>";
    private static final String LIST_TAG_END = "</li>";
    private static final int MAX_POINTS_TO_SHOW = 4;
    private ViewFlipper mContainerMain;
    private int mRefreshRepeatCount = 0;
    private String mCouponName;
    public CountDownTimer mTimer;
    public ProgressBar progressBar;
    //private PerformanceMonitoring fpmDetailTokopoint;
    private PromoCheckoutDetailComponent promoCheckoutDetailComponent;

    @Inject
    public CheckoutCatalogDetailPresenter mPresenter;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new CheckoutCatalogDetailFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        setStyle(BottomSheetDialogFragment.STYLE_NORMAL, R.style.TransparentBottomSheetDialogTheme);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.promo_content_coupon_catalog, container, false);
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

        String code = getArguments().getString("slug");
        int catalog_id=getArguments().getInt("catalog_id");
        mPresenter.getCatalogDetail(code,catalog_id);
    }

    @Override
    public void onDestroy() {
        mPresenter.destroyView();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        super.onDestroy();
    }

    @Override
    public void onResume() {
        super.onResume();
    //    AnalyticsTrackerUtil.sendScreenEvent(getActivity(), getScreenName());
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showLoader() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void showError() {
      //  mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Override
    public void onEmptyCatalog() {
        //mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Override
    public void hideLoader() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void populateDetail(HachikoCatalogDetail data) {
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

//    @Override
    protected String getScreenName() {
//        return AnalyticsTrackerUtil.ScreenKeys.COUPON_CATALOG_SCREEN_NAME;
        return " ";
    }

    protected void initInjector() {
//        super.initInjector();
//        getComponent(PromoCheckoutDetailComponent.class).inject(this)


        DaggerPromoCheckoutDetailComponent.builder()
                .baseAppComponent(((BaseMainApplication)getActivity().getApplication()).getBaseAppComponent())
                .build()
                .inject(this);
    }

    private void initViews(@NonNull View view) {
        //mContainerMain = view.findViewById(R.id.container);
        progressBar=view.findViewById(R.id.progressbar);
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }
    }

    @Override
    public void openWebView(String url) {
        //((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    public void showRedeemCouponDialog(String cta, String code, String title) {
//        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
//        adb.setTitle(R.string.tp_label_use_coupon);
//        StringBuilder messageBuilder = new StringBuilder()
//                .append(getString(R.string.tp_label_coupon))
//                .append(" ")
//                .append("<strong>")
//                .append(title)
//                .append("</strong>")
//                .append(" ")
//                .append(getString(R.string.tp_mes_coupon_part_2));
//        adb.setMessage(MethodChecker.fromHtml(messageBuilder.toString()));
//        adb.setPositiveButton(R.string.tp_label_use, (dialogInterface, i) -> {
//            //Call api to validate the coupon
//            mPresenter.redeemCoupon(code, cta);
//
//
//        });
//        adb.setNegativeButton(R.string.tp_label_later, (dialogInterface, i) -> {
//
//
//        });
//        AlertDialog dialog = adb.create();
//        dialog.show();
//        decorateDialog(dialog);
    }

    @Override
    public void showConfirmRedeemDialog(String cta, String code, String title) {
//        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
//        adb.setNegativeButton(R.string.tp_label_use, (dialogInterface, i) -> {
//            showRedeemCouponDialog(cta, code, title);
//
//        });
//
//        adb.setPositiveButton(R.string.tp_label_view_coupon, (dialogInterface, i) -> {
//                    startActivity(MyCouponListingActivity.getCallingIntent(getActivityContext()));
//
//                }
//        );
//
//        adb.setTitle(R.string.tp_label_successful_exchange);
//        AlertDialog dialog = adb.create();
//        dialog.show();
//        decorateDialog(dialog);

    }

    @Override
    public void showValidationMessageDialog(HachikoCatalogDetail item, String title, String message, int resCode) {
        View viewRedeemCoupon=View.inflate(this.getContext(),R.layout.popup_redeem_coupon,null);
        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
        adb.setView(viewRedeemCoupon);
        String labelPositive;
        String labelNegative = null;

   /*     switch (resCode) {
            case 42020:
                labelPositive = "OK";
                break;
            case 42021:
                labelPositive = "Lengkapi Profil";
                labelNegative = "Nanti Saja";
                break;
            case 200:
                labelPositive = "Tukar";
                labelNegative = "Batal";
                break;
            case 42022:
                labelPositive = "OK";
                break;
            default:
                labelPositive = "OK";
        }

        if (title == null || title.isEmpty()) {
            adb.setTitle("Penukaran Gagal");
        } else {
            adb.setTitle(title);
        }

        adb.setMessage(MethodChecker.fromHtml(message));

        if (labelNegative != null && !labelNegative.isEmpty()) {
            adb.setNegativeButton(labelNegative, (dialogInterface, i) -> {
                switch (resCode) {
                    case 42021:

                        break;
                    case 200:

                        break;
                    default:
                }
            });
        }

        adb.setPositiveButton(labelPositive, (dialogInterface, i) -> {
            switch (resCode) {
                case 42020:
                    dialogInterface.cancel();

                    break;
                case 42022:
                    dialogInterface.cancel();

                    break;
                case 42021:
                  //  startActivity(new Intent(getAppContext(), ProfileCompletionActivity.class));

                    break;
                case 200:
                    mPresenter.startSaveCoupon(item);

                    break;
                default:
                    dialogInterface.cancel();
            }
        });*/



        TextView tvTitlePopup=viewRedeemCoupon.findViewById(R.id.tv_titlepopup);
        TextView tvContentPopup=viewRedeemCoupon.findViewById(R.id.tv_contentpopup);
        TextView button=viewRedeemCoupon.findViewById(R.id.button2);
        tvTitlePopup.setText(title);
        tvContentPopup.setText(message);
        button.setOnClickListener(view->mPresenter.startSaveCoupon(item));

        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

    @Override
    public void showRedeemFullError(HachikoCatalogDetail item, String title, String desc) {

    }

    @Override
    public void onSuccessPoints(String point) {
        if (getView() == null) {
            return;
        }


      //  Typography textUserPoint = getView().findViewById(R.id.text_point_value);
       // textUserPoint.setText(point);
    }

    @Override
    public void onErrorPoint(String errorMessage) {
        //TODO @lavekush need to handle it
    }

    @Override
    public void onPreValidateError(String title, String message) {
//        AlertDialog.Builder adb = new AlertDialog.Builder(getActivityContext());
//
//        adb.setTitle(title);
//        adb.setMessage(message);
//
//        adb.setPositiveButton(R.string.tp_label_ok, (dialogInterface, i) -> {
//                }
//        );
//
//        AlertDialog dialog = adb.create();
//        dialog.show();
//        decorateDialog(dialog);
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
    private void setCatalogToUi(@NonNull HachikoCatalogDetail data) {
   /*     if (getView() == null) {
            return;
        }*/

        mCouponName = data.getTitle();
        Typography quota = getView().findViewById(R.id.text_quota_count);
        Typography description = getView().findViewById(R.id.text_description);
        Typography disabledError = getView().findViewById(R.id.text_disabled_error);
        ConstraintLayout giftSectionMainLayout = getView().findViewById(R.id.gift_section_main_layout);
        Typography giftImage = getView().findViewById(R.id.gift_image);
        Typography giftButton = getView().findViewById(R.id.gift_btn);
        View bottomSeparator = getView().findViewById(R.id.bottom_separator);
        Typography btnAction2 = getView().findViewById(R.id.button_action_2);
        ImageView imgBanner = getView().findViewById(R.id.img_banner);
        Typography labelPoint = getView().findViewById(R.id.text_point_label);
        Typography textDiscount = getView().findViewById(R.id.text_point_discount);
        Typography titleMinTrans=getView().findViewById(R.id.titleMinTrans);
        Typography textMinTrans=getView().findViewById(R.id.textMinTrans);

        btnAction2.setVisibility(View.VISIBLE);
        description.setText(data.getTitle());
        titleMinTrans.setText(data.getMinimumUsageLabel());
        textMinTrans.setText(data.getMinimumUsage());
        btnAction2.setText(data.getButtonStr());
        btnAction2.setBackgroundResource(R.drawable.bg_button_orange_enabled);

        ImageHandler.loadImageFitCenter(imgBanner.getContext(), imgBanner, data.getImageUrlMobile());

        WebView tvTnc = getView().findViewById(R.id.tnc_content);
        Typography tncSeeMore = getView().findViewById(R.id.tnc_see_more);
        Typography howToUseSeeMore = getView().findViewById(R.id.how_to_use_see_more);

        if (data.getTnc()!=null)
        {
            tvTnc.loadData(data.getTnc(), COUPON_MIME_TYPE, UTF_ENCODING);
        }
     /*   tncSeeMore.setOnClickListener(v -> {
            loadWebViewInBottomsheet(data.getTnc(), getString(R.string.tnc_coupon_catalog));
        });
        howToUseSeeMore.setOnClickListener(v -> {
            loadWebViewInBottomsheet(data.getHowToUse(), getString(R.string.how_to_use_coupon_catalog));
        });*/

        Typography pointValue = getView().findViewById(R.id.text_point_value_coupon);
        if (data.getPointsStr() == null || data.getPointsStr().isEmpty()) {
            pointValue.setVisibility(View.GONE);
        } else {
            pointValue.setVisibility(View.VISIBLE);
            pointValue.setText(data.getPointsStr());
        }


        Typography points = getView().findViewById(R.id.text_point_value);
        if (data.getPointsStr() == null || data.getPointsStr().isEmpty()) {
            points.setVisibility(View.GONE);
        } else {
            points.setVisibility(View.VISIBLE);
            points.setText(String.valueOf(data.getPoints()));
        }

        //Quota text handling
    /*    if (data.getUpperTextDesc() == null || data.getUpperTextDesc().isEmpty()) {
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
        }*/

        //Quota text handling
     /*   if (data.getDisableErrorMessage() == null || data.getDisableErrorMessage().isEmpty()) {
            disabledError.setVisibility(View.GONE);
        } else {
            disabledError.setVisibility(View.VISIBLE);
            disabledError.setText(data.getDisableErrorMessage());
        }*/

        //disabling the coupons if not eligible for current membership
    /*    if (data.isDisabled()) {
            ImageUtil.dimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.black_54));
        } else {
            ImageUtil.unDimImage(imgBanner);
            pointValue.setTextColor(ContextCompat.getColor(pointValue.getContext(), R.color.orange_red));
        }

        if (data.isDisabledButton()) {
            giftSectionMainLayout.setVisibility(View.GONE);
            btnAction2.setTextColor(ContextCompat.getColor(btnAction2.getContext(), R.color.black_12));
        } else {
            giftSectionMainLayout.setVisibility(View.VISIBLE);
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
        }*/


        btnAction2.setOnClickListener(v -> {
            //call validate api the show dialog
            mPresenter.startValidateCoupon(data);

        });
    }

    private String getLessDisplayData(String data, Typography seeMore) {
      /*  String[] totalString = data.split(LIST_TAG_START);
        String displayString = totalString[0] + LIST_TAG_START;
        if (totalString.length > MAX_POINTS_TO_SHOW + 1) {
            for (int i = 1; i < MAX_POINTS_TO_SHOW; i++) {
                displayString = displayString.concat(totalString[i] + LIST_TAG_START);
            }
            displayString = displayString.concat(totalString[MAX_POINTS_TO_SHOW]);
            String lastString = totalString[totalString.length - 1];
            if (lastString.contains(LIST_TAG_END)) {
                displayString = displayString + LIST_TAG_END + totalString[totalString.length - 1].split(LIST_TAG_END)[1];
            }
        } else {
           displayString = data;
            seeMore.setVisibility(View.GONE);
        }
       return displayString;*/
      return  " ";
    }

    private void loadWebViewInBottomsheet(String data, String title) {
//        CloseableBottomSheetDialog bottomSheet = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
//        View view = getLayoutInflater().inflate(R.layout.catalog_bottomsheet, null, true);
//        WebView webView = view.findViewById(R.id.catalog_webview);
//        ImageView closeBtn = view.findViewById(R.id.close_button);
//        Typography titleView = view.findViewById(R.id.title_closeable);
//
//        webView.loadData(data, COUPON_MIME_TYPE, UTF_ENCODING);
//        closeBtn.setOnClickListener((v) -> bottomSheet.dismiss());
//        titleView.setText(title);
//        bottomSheet.setCustomContentView(view, title, false);
//        bottomSheet.show();
    }


}
