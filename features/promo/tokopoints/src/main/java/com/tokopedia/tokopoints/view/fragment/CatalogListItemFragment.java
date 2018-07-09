package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.home.SimpleWebViewWithFilePickerActivity;
import com.tokopedia.core.router.home.HomeRouter;
import com.tokopedia.profilecompletion.view.activity.ProfileCompletionActivity;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.adapter.CatalogListAdapter;
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration;
import com.tokopedia.tokopoints.view.contract.CatalogListItemContract;
import com.tokopedia.tokopoints.view.model.CatalogsValueEntity;
import com.tokopedia.tokopoints.view.presenter.CatalogListItemPresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import java.util.List;

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
    private TextView mTextFailedAction;
    private TextView mTextEmptyAction;

    @Inject
    public CatalogListItemPresenter mPresenter;

    public static Fragment newInstance(int categoryId, int currentSortType) {
        Fragment fragment = new CatalogListItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARGS_CATEGORY_ID, categoryId);
        bundle.putInt(ARGS_SORT_TYPE, currentSortType);
        fragment.setArguments(bundle);
        return fragment;
    }

    public CatalogListItemFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        initInjector();
        View rootView = inflater.inflate(R.layout.tp_fragment_catalog_tabs_item, container, false);
        mRecyclerViewCatalog = rootView.findViewById(R.id.list_catalog_item);
        mContainer = rootView.findViewById(R.id.container);
        mTextFailedAction = rootView.findViewById(R.id.text_failed_action);
        mTextFailedAction = rootView.findViewById(R.id.text_empty_action);
        return rootView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        view.findViewById(R.id.text_failed_action).setOnClickListener(this);
        view.findViewById(R.id.text_empty_action).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.text_failed_action) {
            mPresenter.getCatalog(getCurrentCategoryId(), getCurrentSortType());
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
    }

    @Override
    public void showError() {
        mContainer.setDisplayedChild(CONTAINER_ERROR);
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
            mRecyclerViewCatalog.addItemDecoration(new SpacesItemDecoration(getActivityContext().getResources().getDimensionPixelOffset(R.dimen.tp_padding_small)));
            mRecyclerViewCatalog.setAdapter(mAdapter);
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
        });
        adb.setNegativeButton(R.string.tp_label_later, (dialogInterface, i) -> {

        });
        AlertDialog dialog = adb.create();
        dialog.show();
        decorateDialog(dialog);
    }

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
}
