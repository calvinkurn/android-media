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

import com.tokopedia.abstraction.base.app.BaseMainApplication;
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.analytics.AffiliateEventTracking;
import com.tokopedia.affiliate.feature.createpost.view.activity.CreatePostActivity;
import com.tokopedia.affiliate.feature.explore.view.activity.ExploreActivity;
import com.tokopedia.affiliate.feature.onboarding.di.DaggerOnboardingComponent;
import com.tokopedia.affiliate.feature.onboarding.view.activity.RecommendProductActivity;
import com.tokopedia.affiliate.feature.onboarding.view.listener.RecommendProductContract;
import com.tokopedia.affiliate.feature.onboarding.view.viewmodel.RecommendProductViewModel;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.user.session.UserSession;

import javax.inject.Inject;

/**
 * @author by milhamj on 10/4/18.
 */
public class RecommendProductFragment extends BaseDaggerFragment
        implements RecommendProductContract.View {

    private static final String DEFAULT_PRODUCT_ID = "0";

    private ImageView image;
    private TextView name;
    private TextView commission;
    private ButtonCompat recommendBtn;
    private TextView seeOther;
    private View loadingView;

    @Inject
    RecommendProductContract.Presenter presenter;

    @Inject
    AffiliateAnalytics affiliateAnalytics;

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
    public void onStart() {
        super.onStart();
        affiliateAnalytics.getAnalyticTracker().sendScreen(getActivity(), getScreenName());
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        presenter.attachView(this);
        initVar();
        initView();
        presenter.getProductInfo(productId);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
    }

    @Override
    public UserSession getUserSession() {
        return new UserSession(getContext());
    }

    @Override
    public void onSuccessGetProductInfo(RecommendProductViewModel viewModel) {
        ImageHandler.loadImageRounded2(image.getContext(), image, viewModel.getProductImage());
        name.setText(viewModel.getProductName());
        commission.setText(viewModel.getCommission());
        recommendBtn.setOnClickListener(v -> {
            affiliateAnalytics.onDirectRecommRekomendasikanButtonClicked();
            if (getActivity() != null) {
                Intent intent = CreatePostActivity.Companion.getInstanceAffiliate(
                        getActivity(),
                        productId,
                        viewModel.getAdId()
                );
                startActivity(intent);
                getActivity().finish();
            }
        });
    }

    @Override
    public void onErrorGetProductInfo(String message) {
        NetworkErrorHelper.showEmptyState(getContext(), getView(), message,
                () -> presenter.getProductInfo(productId));
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
        BaseAppComponent baseAppComponent
                = ((BaseMainApplication) getActivity().getApplication())
                .getBaseAppComponent();
        DaggerOnboardingComponent.builder()
                .baseAppComponent(baseAppComponent)
                .build()
                .inject(this);
    }

    @Override
    protected String getScreenName() {
        return AffiliateEventTracking.Screen.BYME_ADD_RECOMMENDATION;
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
            affiliateAnalytics.onDirectRecommProdukLainButtonClicked();
            if (getActivity() != null) {
                Intent intent = ExploreActivity.getInstance(getActivity());
                startActivity(intent);
                getActivity().finish();
            }
        });
    }
}
