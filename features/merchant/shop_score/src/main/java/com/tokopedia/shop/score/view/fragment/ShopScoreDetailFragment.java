package com.tokopedia.shop.score.view.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.core.widget.NestedScrollView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.view.MethodChecker;
import com.tokopedia.core.network.NetworkErrorHelper;
import com.tokopedia.gm.common.constant.GMCommonConstantKt;
import com.tokopedia.shop.score.R;
import com.tokopedia.shop.score.di.ShopScoreDetailDependencyInjector;
import com.tokopedia.shop.score.view.model.ShopScoreDetailItemViewModel;
import com.tokopedia.shop.score.view.model.ShopScoreDetailStateEnum;
import com.tokopedia.shop.score.view.model.ShopScoreDetailSummaryViewModel;
import com.tokopedia.shop.score.view.presenter.ShopScoreDetailPresenterImpl;
import com.tokopedia.shop.score.view.recyclerview.ShopScoreDetailAdapter;
import com.tokopedia.unifycomponents.LoaderUnify;

import java.util.List;

/**
 * @author sebastianuskh on 2/24/17.
 */
public class ShopScoreDetailFragment extends BaseDaggerFragment implements ShopScoreDetailView {
    public static final String TAG = "ShopScoreDetail";
    public static final String IS_NEW_GOLD_BADGE = "is_new_gold_badge";
    public static final String TRUE = "true";
    private ShopScoreDetailFragmentCallback callback;
    private ShopScoreDetailAdapter adapter;
    private ShopScoreDetailPresenterImpl presenter;
    private LinearLayout containerView;
    private TextView summaryDetailTitle;
    private TextView descriptionGoldBadge;
    private ImageView imageViewGoldBadge;
    private FrameLayout mainFrame;
    private ProgressDialog progressDialog;
    private LoaderUnify loadingUnify;
    private NestedScrollView scrollview;

    private View.OnClickListener goToSellerCenter = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callback.goToSellerCenter();
        }
    };
    private View.OnClickListener goToCompleteInformation = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            callback.goToCompleteInformation();
        }
    };
    private NetworkErrorHelper.RetryClickedListener retryLoadShopScore = new NetworkErrorHelper.RetryClickedListener() {
        @Override
        public void onRetryClicked() {
            presenter.getShopScoreDetail();
        }
    };


    public static Fragment createFragment() {
        return new ShopScoreDetailFragment();
    }

    @Override
    protected void initInjector() {
        presenter = ShopScoreDetailDependencyInjector.getPresenter(getActivity());
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View parentView = inflater.inflate(R.layout.fragment_shop_score_detail, container, false);

        setupRecyclerView(parentView);
        progressDialog = new ProgressDialog(getActivity());

        containerView = (LinearLayout) parentView.findViewById(R.id.container_view);

        mainFrame = (FrameLayout) parentView.findViewById(R.id.main_frame);

        summaryDetailTitle = (TextView) parentView.findViewById(R.id.text_view_shop_score_summary_detail_tittle);
        descriptionGoldBadge = (TextView) parentView.findViewById(R.id.description_shop_score_detail_gold_badge_info);
        imageViewGoldBadge = (ImageView) parentView.findViewById(R.id.image_view_gold_badge);
        loadingUnify = (LoaderUnify) parentView.findViewById(R.id.loading);
        scrollview = (NestedScrollView) parentView.findViewById(R.id.scrollview);

        parentView.findViewById(R.id.button_go_to_seller_center).setOnClickListener(goToSellerCenter);
        parentView.findViewById(R.id.button_go_to_complete_information).setOnClickListener(goToCompleteInformation);

        TextView textView = parentView.findViewById(R.id.description_shop_score_detail_gold_badge_info);
        textView.setText(getString(R.string.description_shop_score_gold_badge_state,
                GMCommonConstantKt.GM_BADGE_TITLE));

        presenter.attachView(this);
        presenter.getShopScoreDetail();
        return parentView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.unsubscribe();

    }

    private void setupRecyclerView(View parentView) {
        RecyclerView recyclerView = (RecyclerView) parentView.findViewById(R.id.recycler_view_shop_score_detail);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        adapter = new ShopScoreDetailAdapter();
        recyclerView.setAdapter(adapter);
        recyclerView.setNestedScrollingEnabled(false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof ShopScoreDetailFragmentCallback) {
            this.callback = (ShopScoreDetailFragmentCallback) context;
        } else {
            throw new RuntimeException("Please implement ShopScoreDetailFragmentCallback in the Activity");
        }
    }


    @Override
    protected String getScreenName() {
        return null;
    }


    @Override
    public void renderShopScoreDetail(List<ShopScoreDetailItemViewModel> viewModel) {
        adapter.updateData(viewModel);
    }

    @Override
    public void renderShopScoreSummary(ShopScoreDetailSummaryViewModel viewModel) {
        setNoGravity();
        String stringConcat = buildStringSummary(viewModel);
        summaryDetailTitle.setText(Html.fromHtml(stringConcat));
    }

    @Override
    public void renderShopScoreState(ShopScoreDetailStateEnum shopScoreDetailStateEnum) {
        Drawable icon;
        switch (shopScoreDetailStateEnum) {
            case GOLD_MERCHANT_QUALIFIED_BADGE:
            case GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                icon = MethodChecker.getDrawable(getContext(), com.tokopedia.gm.common.R.drawable.ic_power_merchant);
                break;
            case NOT_GOLD_MERCHANT_QUALIFIED_BADGE:
            case NOT_GOLD_MERCHANT_NOT_QUALIFIED_BADGE:
                icon = MethodChecker.getDrawable(getContext(), com.tokopedia.gm.common.R.drawable.ic_pm_badge_shop_regular);
                break;
            default:
                icon = MethodChecker.getDrawable(getContext(), com.tokopedia.gm.common.R.drawable.ic_pm_badge_shop_regular);
                break;
        }
        setShopScoreGoldBadgeState(icon);

    }

    @Override
    public void emptyState() {
        scrollview.setBackgroundColor(ContextCompat.getColor(getContext(), android.R.color.white));
        containerView.setVisibility(View.GONE);
        setGravityCenter();
        NetworkErrorHelper
                .showEmptyState(
                        getActivity(),
                        mainFrame,
                        getString(R.string.error_title_shop_score_failed),
                        getString(R.string.error_subtitle_shop_score_failed),
                        getString(R.string.label_try_again),
                        com.tokopedia.globalerror.R.drawable.unify_globalerrors_connection,
                        retryLoadShopScore
                );
    }

    @Override
    public void showLoading() {
        setGravityCenter();
        loadingUnify.setVisibility(View.VISIBLE);
        containerView.setVisibility(View.GONE);
    }

    @Override
    public void dismissLoading() {
        loadingUnify.setVisibility(View.GONE);
        containerView.setVisibility(View.VISIBLE);
    }

    private void setGravityCenter() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        params.gravity = Gravity.CENTER;
        mainFrame.setLayoutParams(params);
    }

    private void setNoGravity() {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
        );
        mainFrame.setLayoutParams(params);
    }

    private void setShopScoreGoldBadgeState(Drawable icon) {
        imageViewGoldBadge.setImageDrawable(icon);
    }

    private String buildStringSummary(ShopScoreDetailSummaryViewModel viewModel) {
        return getString(R.string.subtitle_first_shop_score_detail_summary)
                + " "
                + "<font color=#"
                + Integer.toHexString(viewModel.getColor())
                + "><strong>"
                + viewModel.getText()
                + "</strong></font>"
                + " "
                + getString(R.string.subtitle_second_shop_score_detail_summary)
                + " "
                + "<strong>"
                + viewModel.getValue()
                + "</strong>"
                + ".";
    }


}
