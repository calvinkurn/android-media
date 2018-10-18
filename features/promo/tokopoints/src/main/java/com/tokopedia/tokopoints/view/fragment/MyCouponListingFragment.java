package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.activity.CatalogListingActivity;
import com.tokopedia.tokopoints.view.adapter.CouponListAdapter;
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration;
import com.tokopedia.tokopoints.view.contract.MyCouponListingContract;
import com.tokopedia.tokopoints.view.model.CouponValueEntity;
import com.tokopedia.tokopoints.view.presenter.MyCouponListingPresenter;
import com.tokopedia.tokopoints.view.util.AnalyticsTrackerUtil;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;
import java.util.Map;

import javax.inject.Inject;

public class MyCouponListingFragment extends BaseDaggerFragment implements MyCouponListingContract.View, View.OnClickListener {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private static final int CONTAINER_EMPTY = 3;
    private ViewFlipper mContainerMain;
    private RecyclerView mRecyclerView;
    private CouponListAdapter mAdapter;

    @Inject
    public MyCouponListingPresenter mPresenter;

    public static MyCouponListingFragment newInstance() {
        return new MyCouponListingFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        View view = inflater.inflate(R.layout.tp_fragment_my_coupon_listing, container, false);
        initViews(view);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        initListener();
        mPresenter.getCoupons();
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
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public void showLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void showError(String errorMeassage) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
    }

    @Override
    public void hideLoader() {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
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
        } else if (source.getId() == R.id.text_failed_action) {
            mPresenter.getCoupons();
        }
    }

    private void initViews(@NonNull View view) {
        mContainerMain = view.findViewById(R.id.container);
        mRecyclerView = view.findViewById(R.id.recycler_view_coupons);

        ((ImageView) view.findViewById(R.id.img_error)).setImageResource(R.drawable.ic_tp_empty_pages);
        ((TextView) view.findViewById(R.id.text_title_error)).setText(getString(R.string.tp_default_empty_coupons_title));
        ((TextView) view.findViewById(R.id.text_label_error)).setText(getString(R.string.tp_default_empty_coupons_subtitle));
        view.findViewById(R.id.button_continue).setVisibility(View.VISIBLE);
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
    }

    @Override
    public void openWebView(String url) {
        ((TokopointRouter) getAppContext()).openTokoPoint(getContext(), url);
    }

    @Override
    public void populateCoupons(List<CouponValueEntity> coupons) {
        hideLoader();
        mAdapter = new CouponListAdapter(mPresenter, coupons);
        mRecyclerView.addItemDecoration(new SpacesItemDecoration(getActivityContext().getResources().getDimensionPixelOffset(R.dimen.tp_padding_small)));
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onErrorCoupons(String errorMessage) {

    }

    @Override
    public void emptyCoupons(Map<String,String> errors) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);

        if(getView() == null || errors == null){
            return;
        }

        ((ImageView) getView().findViewById(R.id.img_error)).setImageResource(R.drawable.ic_tp_empty_pages);
        ((TextView) getView().findViewById(R.id.text_title_error)).setText(errors.get(CommonConstant.CouponMapKeys.TITLE));
        ((TextView) getView().findViewById(R.id.text_label_error)).setText(errors.get(CommonConstant.CouponMapKeys.SUB_TITLE));
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
}
