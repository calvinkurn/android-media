package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.MyCouponListingActivity;
import com.tokopedia.tokopoints.view.activity.SendGiftActivity;
import com.tokopedia.tokopoints.view.adapter.CatalogListAdapter;
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration;
import com.tokopedia.tokopoints.view.contract.CatalogListItemContract;
import com.tokopedia.tokopoints.view.model.CatalogStatusItem;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.presenter.CatalogListItemPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import javax.inject.Inject;

import static com.tokopedia.tokopoints.view.util.CommonConstant.ARGS_CATEGORY_ID;
import static com.tokopedia.tokopoints.view.util.CommonConstant.ARGS_SORT_TYPE;

public class CatalogListItemFragment extends BaseDaggerFragment implements CatalogListItemContract.View, View.OnClickListener {

    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private static final int CONTAINER_EMPTY = 3;
    private ViewFlipper mContainer;
    private RecyclerView mRecyclerViewCatalog;
    private CatalogListAdapter mAdapter;
    private long mRefreshTime;
    private Timer mTimer;
    private Handler mHandler = new Handler();

    private Runnable mRunnableUpdateCatalogStatus = new Runnable() {
        @Override
        public void run() {
            List<Integer> items = new ArrayList<>();
            for (CatalogsValueEntity each : mAdapter.getItems()) {
                if (each.getCatalogType() == CommonConstant.CATALOG_TYPE_FLASH_SALE) {
                    items.add(each.getId());
                }
            }

            mPresenter.fetchLatestStatus(items);
        }
    };

    @Inject
    public CatalogListItemPresenter mPresenter;
    private SwipeToRefresh mSwipeToRefresh;

    public static Fragment newInstance(int categoryId, int subCategoryId, boolean isPointsAvailable) {
        Fragment fragment = new CatalogListItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.ARGS_CATEGORY_ID, categoryId);
        bundle.putInt(CommonConstant.ARGS_SUB_CATEGORY_ID, subCategoryId);
        bundle.putBoolean(CommonConstant.ARGS_POINTS_AVAILABILITY, isPointsAvailable);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CatalogListItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initInjector();
        fetchRemoteConfig();
        View rootView = inflater.inflate(R.layout.tp_fragment_catalog_tabs_item, container, false);
        mRecyclerViewCatalog = rootView.findViewById(R.id.list_catalog_item);
        mSwipeToRefresh =rootView.findViewById(R.id.swipe_refresh_layout);
        if(getPointsAvailability()) {           // set padding of recycler view according to membershipdata availability
            mRecyclerViewCatalog.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.tp_margin_bottom_membership_and_egg));
        }else{
            mRecyclerViewCatalog.setPadding(0, 0, 0, getResources().getDimensionPixelSize(R.dimen.tp_margin_bottom_egg));
        }
        mContainer = rootView.findViewById(R.id.container);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        view.findViewById(R.id.text_failed_action).setOnClickListener(this);
        view.findViewById(R.id.text_empty_action).setOnClickListener(this);
        mSwipeToRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mPresenter.getCatalog(getCurrentCategoryId(), getCurrentSubCategoryId(), false);
            }
        });
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.text_failed_action) {
            mPresenter.getCatalog(getCurrentCategoryId(), getCurrentSubCategoryId(), true);
        } else if (view.getId() == R.id.text_empty_action) {
            openWebView(CommonConstant.WebLink.INFO);
        }
    }

    @Override
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoader() {
        mContainer.setDisplayedChild(CONTAINER_LOADER);
        mSwipeToRefresh.setRefreshing(false);
    }

    @Override
    public void showError() {
        mContainer.setDisplayedChild(CONTAINER_ERROR);
        mSwipeToRefresh.setRefreshing(false);
    }

    @Override
    public void onEmptyCatalog() {
        mContainer.setDisplayedChild(CONTAINER_EMPTY);
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    @Override
    public void hideLoader() {
        mContainer.setDisplayedChild(CONTAINER_DATA);
        mSwipeToRefresh.setRefreshing(false);
    }

    @Override
    public void populateCatalog(List<CatalogsValueEntity> items) {
        if (items == null || items.isEmpty()) {
            onEmptyCatalog();
            return;
        }

        hideLoader();
        if (mAdapter != null) {
            mAdapter.updateItems(items);
            mAdapter.notifyDataSetChanged();
        } else {
            mAdapter = new CatalogListAdapter(mPresenter, items);
            mRecyclerViewCatalog.addItemDecoration(new SpacesItemDecoration(getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_10),
                    getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_14),
                    getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_14)));
            mRecyclerViewCatalog.setAdapter(mAdapter);
        }

        if (mTimer == null) {
            startUpdateCatalogStatusTimer();
        }
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public int getCurrentSortType() {
        if (getArguments() != null) {
            return getArguments().getInt(ARGS_SORT_TYPE);
        }

        return CommonConstant.DEFAULT_SORT_TYPE; // default sort id
    }

    @Override
    public int getCurrentCategoryId() {
        if (getArguments() != null) {
            return getArguments().getInt(ARGS_CATEGORY_ID);
        }

        return CommonConstant.DEFAULT_CATEGORY_TYPE; // default category id
    }

    @Override
    public int getCurrentSubCategoryId() {
        if (getArguments() != null) {
            return getArguments().getInt(CommonConstant.ARGS_SUB_CATEGORY_ID);
        }

        return CommonConstant.DEFAULT_CATEGORY_TYPE; // default category id
    }

    public boolean getPointsAvailability(){
        if(getArguments()!=null){
            return getArguments().getBoolean(CommonConstant.ARGS_POINTS_AVAILABILITY, false);
        }
        return false;
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
        AlertDialog.Builder builder = adb.setPositiveButton(R.string.tp_label_use, (dialogInterface, i) -> {
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
    public void refreshCatalog(@NonNull List<CatalogStatusItem> items) {
        if (items == null || items.isEmpty()) {
            return;
        }

        for (CatalogStatusItem each : items) {
            if (each == null) {
                continue;
            }

            for (int i = 0; i < mAdapter.getItemCount(); i++) {
                CatalogsValueEntity item = mAdapter.getItems().get(i);
                if (each.getCatalogID() == item.getId()) {
                    item.setDisabled(each.isDisabled());
                    item.setDisabledButton(each.isDisabledButton());
                    item.setUpperTextDesc(each.getUpperTextDesc());
                    item.setQuota(each.getQuota());
                }
            }
        }

        if (mAdapter != null) {
            mAdapter.notifyDataSetChanged();
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

    public CatalogListItemPresenter getPresenter() {
        return this.mPresenter;
    }

    /*This section is exclusively for handling flash-sale timer*/
    private void startUpdateCatalogStatusTimer() {
        mTimer = new Timer();
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (mHandler != null) {
                    mHandler.post(mRunnableUpdateCatalogStatus);
                }
            }
        }, 0, mRefreshTime > 0 ? mRefreshTime : CommonConstant.DEFAULT_AUTO_REFRESH_S);
    }

    private void fetchRemoteConfig() {
        mRefreshTime = ((TokopointRouter) getAppContext())
                .getLongRemoteConfig(CommonConstant.TOKOPOINTS_CATALOG_STATUS_AUTO_REFRESH_S, CommonConstant.DEFAULT_AUTO_REFRESH_S);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mPresenter.destroyView();

        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }

        if (mHandler != null) {
            mHandler.removeCallbacks(mRunnableUpdateCatalogStatus);
            mHandler = null;
        }

        mRunnableUpdateCatalogStatus = null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    @Override
    public void gotoSendGiftPage(int id, String title, String pointStr) {
        Bundle bundle = new Bundle();
        bundle.putInt(CommonConstant.EXTRA_COUPON_ID, id);
        bundle.putString(CommonConstant.EXTRA_COUPON_TITLE, title);
        bundle.putString(CommonConstant.EXTRA_COUPON_POINT, pointStr);
        startActivity(SendGiftActivity.getCallingIntent(getActivity(), bundle));
    }
}

