package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.ViewFlipper;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
import com.tokopedia.design.utils.CurrencyFormatUtil;
import com.tokopedia.library.baseadapter.AdapterCallback;
import com.tokopedia.tokopoints.R;
import com.tokopedia.tokopoints.TokopointRouter;
import com.tokopedia.tokopoints.di.TokoPointComponent;
import com.tokopedia.tokopoints.view.adapter.PointHistoryListAdapter;
import com.tokopedia.tokopoints.view.adapter.SpacesItemDecoration;
import com.tokopedia.tokopoints.view.contract.PointHistoryContract;
import com.tokopedia.tokopoints.view.model.TokoPointStatusPointsEntity;
import com.tokopedia.tokopoints.view.presenter.PointHistoryPresenter;
import com.tokopedia.tokopoints.view.util.CommonConstant;

import javax.inject.Inject;

public class PointHistoryFragment extends BaseDaggerFragment implements PointHistoryContract.View, AdapterCallback, View.OnClickListener {
    private static final int CONTAINER_LOADER = 0;
    private static final int CONTAINER_DATA = 1;
    private static final int CONTAINER_ERROR = 2;
    private ViewFlipper mContainerMain;
    private RecyclerView mRecyclerView;
    private PointHistoryListAdapter mAdapter;
    private String mStrPointExpInfo, mStrLoyaltyExpInfo;

    @Inject
    public PointHistoryPresenter mPresenter;

    public static Fragment newInstance(Bundle extras) {
        Fragment fragment = new PointHistoryFragment();
        fragment.setArguments(extras);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initInjector();
        return inflater.inflate(R.layout.tp_fragment_point_history, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mContainerMain = view.findViewById(R.id.container_main);
        mRecyclerView = view.findViewById(R.id.rv_history_point);
        mContainerMain.setDisplayedChild(1);

        if (mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_2), 0, 0));
        }

        mAdapter = new PointHistoryListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startDataLoading();
        mPresenter.getPointsDetail();
        view.findViewById(R.id.btn_history_info).setOnClickListener(this::onClick);
        view.findViewById(R.id.text_failed_action).setOnClickListener(this::onClick);
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
    protected void initInjector() {
        getComponent(TokoPointComponent.class).inject(this);
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    public void showLoading() {
        if (getView() == null) {
            return;
        }

        mContainerMain.setDisplayedChild(CONTAINER_LOADER);
    }

    @Override
    public void onSuccess(TokoPointStatusPointsEntity data) {
        if (data == null || getActivity() == null || getView() == null) {
            return;
        }

        getView().findViewById(R.id.con_header).setVisibility(View.VISIBLE);
        TextView point = getView().findViewById(R.id.text_my_points_value);
        point.setText(CurrencyFormatUtil.convertPriceValue(data.getReward(), false));
        TextView loyalty = getView().findViewById(R.id.text_loyalty_value);
        loyalty.setText(CurrencyFormatUtil.convertPriceValue(data.getLoyalty(), false));
        mStrPointExpInfo = data.getRewardExpiryInfo();
        mStrLoyaltyExpInfo = data.getLoyaltyExpiryInfo();
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    @Override
    public void onError(String error) {
        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
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
    public void onRetryPageLoad(int pageNumber) {

    }

    @Override
    public void onEmptyList(Object rawObject) {
        if (getView() == null || getActivity() == null) {

        }

        mContainerMain.setDisplayedChild(CONTAINER_ERROR);
        ImageView icon = getView().findViewById(R.id.img_error);
        icon.setImageResource(R.drawable.tp_ic_empty_list);
        ((TextView) getView().findViewById(R.id.text_title_error)).setText(R.string.tp_history_empty_title);
        ((TextView) getView().findViewById(R.id.text_label_error)).setText(R.string.tp_history_empty_desc);
        ((TextView) getView().findViewById(R.id.text_failed_action)).setText(R.string.tp_history_btn_action);
    }

    @Override
    public void onStartFirstPageLoad() {
        showLoading();
    }

    @Override
    public void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject) {
        mContainerMain.setDisplayedChild(CONTAINER_DATA);
    }

    @Override
    public void onStartPageLoad(int pageNumber) {

    }

    @Override
    public void onFinishPageLoad(int itemCount, int pageNumber, @Nullable Object rawObject) {

    }

    @Override
    public void onError(int pageNumber) {
        if (pageNumber == 1) {
            onError("n/a");
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_history_info) {
            showHistoryExpiryBottomSheet(mStrPointExpInfo, mStrLoyaltyExpInfo);
        } else if (v.getId() == R.id.text_failed_action) {
            TextView btnActionFailed = (TextView) v;
            if (btnActionFailed.getText().toString().equalsIgnoreCase(getString(R.string.tp_history_btn_action))) {
                RouteManager.route(v.getContext(), ApplinkConst.HOME);
            } else {
                mPresenter.getPointsDetail();
                mAdapter.startDataLoading();
            }
        }
    }

    private void showHistoryExpiryBottomSheet(String pointInfo, String loyaltyInfo) {
        CloseableBottomSheetDialog dialog = CloseableBottomSheetDialog.createInstanceRounded(getActivity());
        View view = getLayoutInflater().inflate(R.layout.tp_point_history_info, null, false);
        TextView textPoint = view.findViewById(R.id.text_point_exp_info);
        TextView textLoyalty = view.findViewById(R.id.text_loyalty_exp_info);
        textPoint.setText(MethodChecker.fromHtml(pointInfo));
        textLoyalty.setText(MethodChecker.fromHtml(loyaltyInfo));
        view.findViewById(R.id.btn_help_history).setOnClickListener(v -> ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.INFO_EXPIRED_POINTS, getString(R.string.tp_title_tokopoints)));
        dialog.setCustomContentView(view, getString(R.string.tp_title_history_bottomshet), false);
        dialog.show();
        view.findViewById(R.id.close_button).setOnClickListener(v -> dialog.dismiss());
    }
}
