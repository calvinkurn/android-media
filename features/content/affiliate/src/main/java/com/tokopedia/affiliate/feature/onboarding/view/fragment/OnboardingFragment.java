package com.tokopedia.affiliate.feature.onboarding.view.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tokopedia.abstraction.common.utils.DisplayMetricUtils;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.affiliate.R;
import com.tokopedia.affiliate.feature.onboarding.view.activity.DomainInputActivity;
import com.tokopedia.affiliate.feature.onboarding.view.activity.OnboardingActivity;
import com.tokopedia.design.component.ButtonCompat;

import java.util.Objects;

public class OnboardingFragment extends Fragment {

    private static final String PATH_FORMAT = "%s/%s/%s/%s.jpg";
    private static final String ANDROID_IMAGE_URL = "https://ecs7.tokopedia.net/img/android";
    private static final String FINISH_IMAGE_NAME = "af_onboarding_finish";
    private static final String START_IMAGE_NAME = "af_onboarding_start";

    private ImageView image;
    private TextView title;
    private TextView subtitle;
    private ButtonCompat goBtn;
    private TextView commission;

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
        initVar();
        initView();
    }

    private void initVar() {
        if (getArguments() != null) {
            isOnboardingFinish = getArguments().getBoolean(
                    OnboardingActivity.PARAM_IS_FINISH,
                    false
            );
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
                DisplayMetricUtils.getScreenDensity(Objects.requireNonNull(getContext())),
                START_IMAGE_NAME
        );
        ImageHandler.loadImage2(image, imageUrl, R.drawable.ic_loading_image);
        goBtn.setOnClickListener(view1 ->
                startActivity(DomainInputActivity.createIntent(getContext()))
        );
    }

    private void setUpOnboardingFinish() {
        String imageUrl = String.format(
                PATH_FORMAT,
                ANDROID_IMAGE_URL,
                FINISH_IMAGE_NAME,
                DisplayMetricUtils.getScreenDensity(Objects.requireNonNull(getContext())),
                FINISH_IMAGE_NAME
        );
        ImageHandler.loadImage2(image, imageUrl, R.drawable.ic_loading_image);
        title.setText(R.string.af_complete_profile);
        subtitle.setText(R.string.af_select_product_recommendation);
        goBtn.setText(R.string.af_see_product_selection);
        goBtn.setOnClickListener(view ->
                Toast.makeText(getContext(), "Going to explore~", Toast.LENGTH_SHORT).show()
        );
        commission.setVisibility(View.GONE);
    }
}
