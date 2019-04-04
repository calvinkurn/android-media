package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.base.view.widget.SwipeToRefresh;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.CatalogListingActivity;
import com.tokopedia.tokopoints.view.adapter.CouponInStackBaseAdapter;
import com.tokopedia.tokopoints.view.adapter.CouponListBaseAdapter;
import com.tokopedia.tokopoints.view.adapter.CouponListStackedBaseAdapter;
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration;
import com.tokopedia.tokopoints.view.contract.CouponListingStackedContract;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.model.TokoPointPromosEntity;
import com.tokopedia.tokopoints.view.presenter.CouponListingStackedPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class CouponListingStackedFragment extends BaseDaggerFragment implements CouponListingStackedContract.View, View.OnClickListener, AdapterCallback {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private static final int CONTAINER_EMPTY = 3;
    private ViewFlipper mContainerMain;
    private RecyclerView mRecyclerView;
    private CouponListStackedBaseAdapter mAdapter;
    private SpacesItemDecoration mItemDecoration;

    @Inject
    public CouponListingStackedPresenter mPresenter;
    private SwipeToRefresh mSwipeToRefresh;

    public static CouponListingStackedFragment newInstance() {
        return new CouponListingStackedFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_stacked_coupon_listing, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initListener();
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
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
        mSwipeToRefresh.setRefreshing(false);
    }

    @Override
    public void showError(String errorMeassage) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
        mSwipeToRefresh.setRefreshing(false);
    }

    @Override
    public void hideLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
        mSwipeToRefresh.setRefreshing(false);
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
        return AnalyticsTrackerUtil.ScreenKeys.MY_COUPON_LISTING_SCREEN_NAME;
    }

    @Override
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    public void onClick(View source) {
        if (source.getId() == R.id.text_failed_action) {
            showLoader();
            mAdapter.loadData(mAdapter.getCurrentPageIndex());
        }
    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container);
        mRecyclerView = view.findViewById(R.id.recycler_view_coupons);
        mSwipeToRefresh = view.findViewById(R.id.swipe_refresh_layout);
        mItemDecoration = new SpacesItemDecoration(getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_12),
                getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_12),
                getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_12));
    }

    private void initListener() {
        if (getView() == null) {
            return;
        }

        getView().findViewById(R.id.text_failed_action).setOnClickListener(this);
        getView().findViewById(R.id.button_continue).setOnClickListener(view12 -> {
            Bundle bundle = new Bundle();
            bundle.putInt(CommonConstant.EXTRA_COUPON_COUNT, 0);
            startActivity(CatalogListingActivity.getCallingIntent(getActivityContext(), bundle));
        });
        getView().findViewById(R.id.text_empty_action).setOnClickListener(v ->
                ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), CommonConstant.WebLink.INFO));

        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getCoupons(mPresenter.getCategoryId());
            }
        });
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    @Override
    public void populateCoupons(int categoryId) {
        mAdapter = new CouponListStackedBaseAdapter(mPresenter, this, getAppContext(), categoryId);

        if (mRecyclerView.getItemDecorationCount() > 0) {
            mRecyclerView.removeItemDecoration(mItemDecoration);
        }

        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startDataLoading();
    }

    @Override
    public void onErrorCoupons(String errorMessage) {

    }

    @Override
    public void emptyCoupons(Map<String, String> errors) {
        if (getView() == null || errors == null) {
            return;
        }

        ((ImageView) getView().findViewById(R.id.img_error2)).setImageResource(R.drawable.ic_tp_empty_pages);
        ((TextView) getView().findViewById(R.id.text_title_error2)).setText(errors.get(CommonConstant.CouponMapKeys.TITLE));
        ((TextView) getView().findViewById(R.id.text_label_error2)).setText(errors.get(CommonConstant.CouponMapKeys.SUB_TITLE));
        getView().findViewById(R.id.button_continue).setVisibility(View.VISIBLE);

        mContainerMain.setDisplayedChild(CONTAINER_EMPTY);
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
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_GUNAKAN,
                    title);
        });
        adb.setNegativeButton(R.string.tp_label_later, (dialogInterface, i) -> {
            AnalyticsTrackerUtil.sendEvent(getContext(),
                    AnalyticsTrackerUtil.EventKeys.EVENT_CLICK_COUPON,
                    AnalyticsTrackerUtil.CategoryKeys.KUPON_MILIK_SAYA,
                    AnalyticsTrackerUtil.ActionKeys.CLICK_NANTI_SAJA,
                    title);
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

    @Override
    public void onRetryPageLoad(int pageNumber) {
    }

    @Override
    public void onEmptyList(Object rawObject) {
        hideLoader();
        emptyCoupons(((TokoPointPromosEntity) rawObject).getCoupon().getEmptyMessage());
    }

    @Override
    public void onStartFirstPageLoad() {
        showLoader();
    }

    @Override
    public void onFinishFirstPageLoad(int count, @Nullable Object rawObject) {
        getView().postDelayed(() -> hideLoader(), CommonConstant.UI_SETTLING_DELAY_MS);
    }

    @Override
    public void onStartPageLoad(int pageNumber) {
    }

    @Override
    public void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject) {
        mSwipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onError(int pageNumber) {
        if (pageNumber == 1) {
            mContainerMain.setDisplayedChild(CONTAINER_ERROR);
        }
        mSwipeToRefresh.setRefreshing(false);
    }

    public CouponListingStackedPresenter getPresenter() {
        return this.mPresenter;
    }

    public void showCouponInStackBottomSheet(String stackId) {
        CloseableBottomSheetDialog closeableBottomSheetDialog = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        View view = getLayoutInflater().inflate(R.layout.tp_bottosheet_coupon_in_stack, null, false);
        RecyclerView recyclerView = view.findViewById(R.id.rv_coupon_in_stack);

        if (mItemDecoration != null) {
            recyclerView.addItemDecoration(mItemDecoration);
        }

        CouponInStackBaseAdapter adapter = new CouponInStackBaseAdapter(new AdapterCallback() {
            @Override
            public void onRetryPageLoad(int pageNumber) {

            }

            @Override
            public void onEmptyList(Object rawObject) {

            }

            @Override
            public void onStartFirstPageLoad() {

            }

            @Override
            public void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject) {
                closeableBottomSheetDialog.show();
            }

            @Override
            public void onStartPageLoad(int pageNumber) {

            }

            @Override
            public void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject) {

            }

            @Override
            public void onError(int pageNumber) {

            }
        }, getContext(), stackId);

        recyclerView.setAdapter(adapter);
        adapter.startDataLoading();
        closeableBottomSheetDialog.setContentView(view);
    }

    public static String getJson() {
        return "{\n" +
                "  \"coupons\": {\n" +
                "    \"coupons\": [\n" +
                "      {\n" +
                "        \"id\": 994462900,\n" +
                "        \"promoID\": 210426,\n" +
                "        \"code\": \"OVONGKIRMCKOGAQGN17\",\n" +
                "        \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/576x192.jpg\",\n" +
                "        \"imageURLMobile\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/576x192.jpg\",\n" +
                "        \"redirectURL\": \"https://www.tokopedia.com/tokopoints\",\n" +
                "        \"redirectAppLink\": \"tokopedia://tokopoints\",\n" +
                "        \"usage\": {\n" +
                "          \"activeCountdown\": 0,\n" +
                "          \"expiredCountdown\": 1535680,\n" +
                "          \"text\": \"Berlaku hingga\",\n" +
                "          \"usageStr\": \"31 Mar 2019\",\n" +
                "          \"buttonUsage\": {\n" +
                "            \"text\": \"Gunakan\",\n" +
                "            \"url\": \"https://www.tokopedia.com/cart/\",\n" +
                "            \"appLink\": \"tokopedia://cart\",\n" +
                "            \"type\": \"redirect\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"minimumUsage\": \"Rp100.000\",\n" +
                "        \"minimumUsageLabel\": \"Min. transaksi\",\n" +
                "        \"isNewCoupon\": true,\n" +
                "        \"isStacked\": true,\n" +
                "        \"stackID\": \"catalogID:1:expired_at:20190320\",\n" +
                "        \"upperLeftSection\": {\n" +
                "          \"textAttributes\": [\n" +
                "            {\n" +
                "              \"text\": \"3 Kupon\",\n" +
                "              \"color\": \"#C53B3D\",\n" +
                "              \"isBold\": true\n" +
                "            }\n" +
                "          ],\n" +
                "          \"backgroundColor\": \"#FAECE8\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 994462895,\n" +
                "        \"promoID\": 211037,\n" +
                "        \"code\": \"TRVTPDMCKOGQLRCDXL3\",\n" +
                "        \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/TRVTPD-Travel-576x192 .jpg\",\n" +
                "        \"imageURLMobile\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/TRVTPD-Travel-576x192 .jpg\",\n" +
                "        \"redirectURL\": \"https://www.tokopedia.com/tokopoints/kupon-saya/detail/TRVTPDMCKOGQLRCD XL3\",\n" +
                "        \"redirectAppLink\": \"https://www.tokopedia.com/tokopoints/kupon-saya/detail/TRVTPDMCKOGQLRCD XL3\",\n" +
                "        \"usage\": {\n" +
                "          \"activeCountdown\": 0,\n" +
                "          \"expiredCountdown\": 1535680,\n" +
                "          \"text\": \"Berlaku hingga\",\n" +
                "          \"usageStr\": \"31 Mar 2019\",\n" +
                "          \"buttonUsage\": {\n" +
                "            \"text\": \"Gunakan\",\n" +
                "            \"url\": \"https://www.tokopedia.com/cart/\",\n" +
                "            \"appLink\": \"tokopedia://cart\",\n" +
                "            \"type\": \"redirect\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"minimumUsage\": \"Rp100.000\",\n" +
                "        \"minimumUsageLabel\": \"Min. transaksi\",\n" +
                "        \"isNewCoupon\": true,\n" +
                "        \"isStacked\": false,\n" +
                "        \"stackID\": \"\",\n" +
                "        \"upperLeftSection\": {\n" +
                "          \"textAttributes\": [\n" +
                "            {\n" +
                "              \"text\": \"\",\n" +
                "              \"color\": \"\",\n" +
                "              \"isBold\": false\n" +
                "            }\n" +
                "          ],\n" +
                "          \"backgroundColor\": \"\"\n" +
                "        }\n" +
                "      },\n" +
                "      {\n" +
                "        \"id\": 994462894,\n" +
                "        \"promoID\": 211035,\n" +
                "        \"code\": \"TUTPDMCKOG34Q9X8JKQ\",\n" +
                "        \"imageURL\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/TUTPD-Topup-576x192.j pg\",\n" +
                "        \"imageURLMobile\": \"https://ecs7.tokopedia.net/img/blog/promo/2019/02/TUTPD-Topup-576x192.j pg\",\n" +
                "        \"redirectURL\": \"https://www.tokopedia.com/tokopoints\",\n" +
                "        \"redirectAppLink\": \"tokopedia://tokopoints\",\n" +
                "        \"usage\": {\n" +
                "          \"activeCountdown\": 0,\n" +
                "          \"expiredCountdown\": 1535680,\n" +
                "          \"text\": \"Berlaku hingga\",\n" +
                "          \"usageStr\": \"31 Mar 2019\",\n" +
                "          \"buttonUsage\": {\n" +
                "            \"text\": \"Gunakan\",\n" +
                "            \"url\": \"https://www.tokopedia.com/cart/\",\n" +
                "            \"appLink\": \"tokopedia://cart\",\n" +
                "            \"type\": \"redirect\"\n" +
                "          }\n" +
                "        },\n" +
                "        \"minimumUsage\": \"\",\n" +
                "        \"minimumUsageLabel\": \"Tanpa minimum transaksi\",\n" +
                "        \"isNewCoupon\": false,\n" +
                "        \"isStacked\": false,\n" +
                "        \"stackID\": \"\",\n" +
                "        \"upperLeftSection\": {\n" +
                "          \"textAttributes\": [\n" +
                "            {\n" +
                "              \"text\": \"\",\n" +
                "              \"color\": \"\",\n" +
                "              \"isBold\": false\n" +
                "            }\n" +
                "          ],\n" +
                "          \"backgroundColor\": \"\"\n" +
                "        }\n" +
                "      }\n" +
                "    ],\n" +
                "    \"tokopointsPaging\": {\n" +
                "      \"hasNext\": false\n" +
                "    },\n" +
                "    \"tokopointsExtraInfo\": [\n" +
                "      \n" +
                "    ],\n" +
                "    \"tokopointsEmptyMessage\": {\n" +
                "      \"title\": \"\",\n" +
                "      \"subTitle\": \"\"\n" +
                "    }\n" +
                "  }\n" +
                "}";
    }

}
