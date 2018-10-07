package com.tokopedia.affiliate.feature.onboarding.view.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.view.activity.RecommendProductActivity;
import com.tokopedia.affiliate.feature.onboarding.view.listener.RecommendProductContract;
import com.tokopedia.affiliate.feature.onboarding.view.viewmodel.RecommendProductViewModel;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ButtonCompat;

/**
 * @author by milhamj on 10/4/18.
 */
public class RecommendProductFragment extends BaseDaggerFragment
        implements RecommendProductContract.View {

    private static final String DEFAULT_PRODUCT_ID = "0";
    private static final String PRODUCT_ID_BRACKET = "{product_id}";
    private static final String AD_ID_BRACKET = "{ad_id}";

    private ImageView image;
    private TextView name;
    private TextView commission;
    private ButtonCompat recommendBtn;
    private TextView seeOther;
    private View loadingView;

    private String productId = DEFAULT_PRODUCT_ID;

    public static RecommendProductFragment newInstance(@NonNull Bundle bundle) {
        RecommendProductFragment fragment = new RecommendProductFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_recommend_product, container, false);
        image = view.findViewById(R.id.image);
        name = view.findViewById(R.id.name);
        commission = view.findViewById(R.id.commission);
        recommendBtn = view.findViewById(R.id.recommendBtn);
        seeOther = view.findViewById(R.id.seeOther);
        loadingView = view.findViewById(R.id.loadingView);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar();
        initView();
    }

    @Override
    public void onSuccessGetProductInfo(RecommendProductViewModel viewModel) {
        ImageHandler.loadImageRounded2(image.getContext(), image, viewModel.getProductImage());
        name.setText(viewModel.getProductName());
        commission.setText(viewModel.getCommission());
        recommendBtn.setOnClickListener(v -> {
            Intent intent = RouteManager.getIntent(
                    getContext(),
                    ApplinkConst.AFFILIATE_CREATE_POST
                            .replace(PRODUCT_ID_BRACKET, productId)
                            .replace(AD_ID_BRACKET, viewModel.getAdId())
            );
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }

    @Override
    public void onErrorGetProductInfo(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), message, () -> {

        });
    }

    @Override
    public void showLoading() {
        loadingView.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoading() {
        loadingView.setVisibility(View.GONE);
    }

    @Override
    protected void initInjector() {

    }

    @Override
    protected String getScreenName() {
        return null;
    }

    private void initVar() {
        if (getArguments() != null) {
            productId = getArguments().getString(
                    RecommendProductActivity.PARAM_PRODUCT_ID,
                    DEFAULT_PRODUCT_ID
            );
        }
    }

    private void initView() {
        seeOther.setOnClickListener(v -> {
            Intent intent = RouteManager.getIntent(getContext(), ApplinkConst.AFFILIATE_EXPLORE);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(intent);
        });
    }
}
