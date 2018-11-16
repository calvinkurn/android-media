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
import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.common.constant.AffiliateConstant;
import com.tokopedia.affiliate.analytics.AffiliateAnalytics;
import com.tokopedia.affiliate.feature.explore.view.activity.ExploreActivity;
import com.tokopedia.affiliate.feature.onboarding.di.DaggerOnboardingComponent;
import com.tokopedia.affiliate.feature.onboarding.view.activity.OnboardingActivity;
import com.tokopedia.affiliate.feature.onboarding.view.activity.UsernameInputActivity;
import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.design.component.ButtonCompat;
import com.tokopedia.user.session.UserSessionInterface;

import javax.inject.Inject;

public class OnboardingFragment extends BaseDaggerFragment {

    private static final String PATH_FORMAT = "%s/%s/%s/%s.jpg";
    private static final String ANDROID_IMAGE_URL = "https://ecs7.tokopedia.net/img/android";
    private static final String FINISH_IMAGE_NAME = "af_onboarding_finish";
    private static final String START_IMAGE_NAME = "af_onboarding_start";
    private static final int LOGIN_CODE = 13;

    @Inject
    UserSessionInterface userSession;

    @Inject
    AffiliateAnalytics affiliateAnalytics;

    private ImageView image;
    private TextView title;
    private TextView subtitle;
    private ButtonCompat goBtn;
    private TextView commission;

    private String productId = "";
    private boolean isOnboardingFinish = false;

    public static OnboardingFragment newInstance(@NonNull Bundle bundle) {
        OnboardingFragment fragment = new OnboardingFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_af_onboarding, container, false);
        image = view.findViewById(R.id.image);
        title = view.findViewById(R.id.title);
        subtitle = view.findViewById(R.id.subtitle);
        goBtn = view.findViewById(R.id.goBtn);
        commission = view.findViewById(R.id.commission);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initVar(savedInstanceState);
        initView();
        if (!userSession.isLoggedIn()) {
            startActivityForResult(
                    RouteManager.getIntent(
                            getContext(),
                            ApplinkConst.LOGIN
                    ),
                    LOGIN_CODE);
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(OnboardingActivity.PARAM_IS_FINISH, isOnboardingFinish);
        outState.putString(OnboardingActivity.PARAM_PRODUCT_ID, productId);
    }

    @Override
    protected String getScreenName() {
        return null;
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

    private void initVar(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            isOnboardingFinish = savedInstanceState.getBoolean(
                    OnboardingActivity.PARAM_IS_FINISH,
                    false
            );
            productId = savedInstanceState.getString(OnboardingActivity.PARAM_PRODUCT_ID, "");
        } else if (getArguments() != null) {
            isOnboardingFinish = getArguments().getBoolean(
                    OnboardingActivity.PARAM_IS_FINISH,
                    false
            );
            productId = getArguments().getString(OnboardingActivity.PARAM_PRODUCT_ID, "");
        }
    }

    private void initView() {
        if (!isOnboardingFinish) {
            setUpOnboardingStart();
        } else {
            setUpOnboardingFinish();
        }
    }

    private void setUpOnboardingStart() {
        String imageUrl = String.format(
                PATH_FORMAT,
                ANDROID_IMAGE_URL,
                START_IMAGE_NAME,
                DisplayMetricUtils.getScreenDensity(getContext()),
                START_IMAGE_NAME
        );
        ImageHandler.loadImage2(image, imageUrl, R.drawable.ic_loading_image);
        goBtn.setOnClickListener(view1 -> {
                    affiliateAnalytics.onCobaSekarangButtonClicked();
                    startActivity(UsernameInputActivity.createIntent(getContext(), productId));
                }
        );
        commission.setVisibility(View.VISIBLE);
        commission.setOnClickListener(v -> {
                    affiliateAnalytics.onTentangKomisiButtonClicked();
                    RouteManager.route(
                            getContext(),
                            String.format(
                                "%s?url=%s",
                                ApplinkConst.WEBVIEW,
                                AffiliateConstant.ABOUT_COMMISSION_URL
                        )
                    );
                }
        );
    }

    private void setUpOnboardingFinish() {
        String imageUrl = String.format(
                PATH_FORMAT,
                ANDROID_IMAGE_URL,
                FINISH_IMAGE_NAME,
                DisplayMetricUtils.getScreenDensity(getContext()),
                FINISH_IMAGE_NAME
        );
        ImageHandler.loadImage2(image, imageUrl, R.drawable.ic_loading_image);
        title.setText(R.string.af_complete_profile);
        subtitle.setText(R.string.af_select_product_recommendation);
        goBtn.setText(R.string.af_see_product_selection);
        goBtn.setOnClickListener(view -> {
            affiliateAnalytics.onDirectRecommPilihanProdukButtonClicked();
            if (getActivity() != null) {
                Intent intent = ExploreActivity.getInstance(getActivity());
                startActivity(intent);
                getActivity().finish();
            }
        });
        commission.setVisibility(View.INVISIBLE);
    }
}
