package com.tokopedia.loginregister.welcomepage;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
import com.tokopedia.abstraction.common.utils.image.ImageHandler;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;
import com.tokopedia.loginregister.common.data.LoginRegisterUrl;
import com.tokopedia.loginregister.common.di.LoginRegisterComponent;
import com.tokopedia.loginregister.welcomepage.di.DaggerWelcomePageComponent;

import javax.inject.Inject;

/**
 * @author by yfsx on 14/03/18.
 */

public class WelcomePageFragment extends BaseDaggerFragment {

    private ImageView background;
    private TextView btnContinue;
    private TextView btnProfileCompletion;

    @Inject
    LoginRegisterAnalytics analytics;

    public static WelcomePageFragment newInstance() {
        return new WelcomePageFragment();
    }

    @Override
    protected String getScreenName() {
        return null;
    }

    @Override
    protected void initInjector() {
        DaggerWelcomePageComponent daggerLoginComponent =
                (DaggerWelcomePageComponent) DaggerWelcomePageComponent
                        .builder().loginRegisterComponent(getComponent(LoginRegisterComponent.class))
                        .build();

        daggerLoginComponent.inject(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_welcome_page, container, false);
        background = view.findViewById(R.id.background);
        btnContinue = view.findViewById(R.id.btn_continue);
        btnProfileCompletion = view.findViewById(R.id.btn_profile_completion);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView();
        initViewListener();
    }

    public void initView() {
        ImageHandler.LoadImage(background, LoginRegisterUrl.URL_BACKGROUND);
        btnContinue.setTypeface(Typeface.create("sans-serif-medium", Typeface.NORMAL));
    }

    public void initViewListener() {
        btnContinue.setOnClickListener(view -> {
            if (getActivity() != null) {
                analytics.eventContinueFromWelcomePage();
                getActivity().finish();
            }
        });

        btnProfileCompletion.setOnClickListener(view -> {
            if (getActivity() != null) {
                analytics.eventClickProfileCompletionFromWelcomePage();

                getActivity().setResult(Activity.RESULT_OK);
                getActivity().finish();
            }
        });
    }

}
