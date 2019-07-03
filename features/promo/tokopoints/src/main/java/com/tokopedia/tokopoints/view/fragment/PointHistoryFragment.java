package com.tokopedia.tokopoints.view.fragment;

import android.content.Context;
import android.graphics.drawable.Drawable;
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
import com.tokopedia.design.bottomsheet.CloseableBottomSheetDialog;
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
        View view = inflater.inflate(R.layout.tp_fragment_point_history, container, false);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPresenter.attachView(this);
        mContainerMain = view.findViewById(R.id.container_main);
        mRecyclerView = view.findViewById(R.id.rv_history_point);
        mContainerMain.setDisplayedChild(1);

        if (mRecyclerView.getItemDecorationCount() == 0) {
            mRecyclerView.addItemDecoration(new SpacesItemDecoration(getActivityContext().getResources().getDimensionPixelOffset(R.dimen.dp_4), 0, 0));
        }

        mAdapter = new PointHistoryListAdapter(getContext(), this);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.startDataLoading();
        mPresenter.getPointsDetail();
        view.findViewById(R.id.btn_history_info).setOnClickListener(this::onClick);
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
    }

    @Override
    public void hideLoading() {
        if (getView() == null) {
            return;
        }
    }

    @Override
    public void onSuccess(TokoPointStatusPointsEntity data) {
        if (data == null) {
            return;
        }

        TextView point = getView().findViewById(R.id.text_my_points_value);
        point.setText(data.getRewardStr());
        TextView loyalty = getView().findViewById(R.id.text_loyalty_value);
        loyalty.setText(data.getLoyaltyStr());
        mStrPointExpInfo = data.getRewardExpiryInfo();
        mStrLoyaltyExpInfo = data.getLoyaltyExpiryInfo();
    }

    @Override
    public void onError(String error) {

    }

    @Override
    public Context getAppContext() {
        return getActivity().getApplicationContext();
    }

    @Override
    public Context getActivityContext() {
        return getActivity();
    }

    private void decorateDialog(AlertDialog dialog) {
        if (dialog.getButton(AlertDialog.BUTTON_POSITIVE) != null) {
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(ContextCompat.getColor(getActivityContext(),
                    R.color.tkpd_main_green));
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setAllCaps(false);
        }
    }

    @Override
    public void onRetryPageLoad(int pageNumber) {

    }

    @Override
    public void onEmptyList(Object rawObject) {

    }

    @Override
    public void onStartFirstPageLoad() {
        showLoading();
    }

    @Override
    public void onFinishFirstPageLoad(int itemCount, @Nullable Object rawObject) {
        hideLoading();
//        if (itemCount == -1) {
//            showError();
//        } else {
//            if (mTimer == null) {
//                startUpdateCatalogStatusTimer();
//            }
//        }

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
            //showError();
        }

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.btn_history_info) {
            showHistoryExpiryBottomSheet(mStrPointExpInfo, mStrLoyaltyExpInfo);
        }
    }

    private void showHistoryExpiryBottomSheet(String pointInfo, String loyaltyInfo) {
        CloseableBottomSheetDialog dialog = CloseableBottomSheetDialog.createInstance(getActivity());
        View view = getLayoutInflater().inflate(R.layout.tp_point_history_info, null, false);
        TextView textPoint = view.findViewById(R.id.text_point_exp_info);
        TextView textLoyalty = view.findViewById(R.id.text_loyalty_exp_info);
        textPoint.setText(MethodChecker.fromHtml(pointInfo));
        textLoyalty.setText(MethodChecker.fromHtml(loyaltyInfo));
        view.findViewById(R.id.btn_help_history).setOnClickListener(v -> ((TokopointRouter) getAppContext()).openTokopointWebview(getContext(), CommonConstant.WebLink.INFO_EXPIRED_POINTS, getString(R.string.tp_title_tokopoints)));
        dialog.setCustomContentView(view, getString(R.string.tp_title_history_bottomshet), true);
        dialog.show();
    }
}
