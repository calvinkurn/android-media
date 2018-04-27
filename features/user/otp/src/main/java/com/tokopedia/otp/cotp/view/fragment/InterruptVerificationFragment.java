//package com.tokopedia.otp.cotp.view.fragment;
//
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.text.TextUtils;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.ImageView;
//import android.widget.TextView;
//
//import com.google.gson.Gson;
//import com.tokopedia.abstraction.base.app.BaseMainApplication;
//import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment;
//import com.tokopedia.abstraction.common.data.model.storage.CacheManager;
//import com.tokopedia.abstraction.common.utils.image.ImageHandler;
//import com.tokopedia.abstraction.common.utils.view.MethodChecker;
//import com.tokopedia.otp.R;
//import com.tokopedia.otp.common.OTPAnalytics;
//import com.tokopedia.otp.common.di.OtpComponent;
//import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
//import com.tokopedia.otp.cotp.view.viewmodel.InterruptVerificationViewModel;
//import com.tokopedia.otp.cotp.view.viewmodel.VerificationPassModel;
//
//import com.tokopedia.otp.common.di.DaggerOtpComponent;
//import com.tokopedia.otp.cotp.di.DaggerCotpComponent;
//
//import javax.inject.Inject;
//
///**
// * @author by nisie on 1/4/18.
// */
//
//public class InterruptVerificationFragment extends BaseDaggerFragment {
//
//    InterruptVerificationViewModel viewModel;
//    ImageView icon;
//    TextView message;
//    TextView requestOtpButton;
//    TextView chooseOtherMethod;
//
//    @Inject
//    OTPAnalytics analytics;
//
//    @Inject
//    CacheManager cacheManager;
//
//
//    public static Fragment createInstance(Bundle bundle) {
//        Fragment fragment = new InterruptVerificationFragment();
//        fragment.setArguments(bundle);
//        return fragment;
//    }
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        analytics.sendScreen(getActivity(), getScreenName());
//    }
//
//    @Override
//    protected String getScreenName() {
//        if (viewModel != null && !TextUtils.isEmpty(viewModel.getAppScreenName())) {
//            return viewModel.getAppScreenName();
//        } else
//            return OTPAnalytics.Screen.SCREEN_INTERRUPT_VERIFICATION_DEFAULT;
//    }
//
//    @Override
//    protected void initInjector() {
//        OtpComponent otpComponent = DaggerOtpComponent.builder()
//                .baseAppComponent(((BaseMainApplication) getActivity().getApplication())
//                        .getBaseAppComponent()).build();
//
//        DaggerCotpComponent.builder()
//                .otpComponent(otpComponent)
//                .build().inject(this);
//
//    }
//
//
//    @Override
//    public void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//
//        Gson gson = new Gson();
//        VerificationPassModel passModel = gson.fromJson(cacheManager.get(VerificationActivity
//                .PASS_MODEL), VerificationPassModel.class);
//
//        if (cacheManager != null
//                && passModel != null
//                && passModel.getInterruptModel() != null) {
//            viewModel = passModel.getInterruptModel();
//            viewModel.setHasOtherMethod(passModel.canUseOtherMethod());
//        } else {
//            getActivity().finish();
//        }
//    }
//
//    @Nullable
//    @Override
//    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup parent, @Nullable Bundle
//            savedInstanceState) {
//        View view = inflater.inflate(R.layout.fragment_interrupt_verification, parent, false);
//        icon = view.findViewById(R.id.icon);
//        message = view.findViewById(R.id.message);
//        requestOtpButton = view.findViewById(R.id.request_otp_button);
//        chooseOtherMethod = view.findViewById(R.id.choose_other_method);
//        prepareView();
//        return view;
//    }
//
//    private void prepareView() {
//        ImageHandler.loadImageWithIdWithoutPlaceholder(icon, viewModel.getIconId());
//        message.setText(MethodChecker.fromHtml(viewModel.getPromptText()));
//        requestOtpButton.setText(viewModel.getButtonText());
//
//        requestOtpButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if (getActivity() instanceof VerificationActivity) {
//                    ((VerificationActivity) getActivity()).goToDefaultVerificationPage(viewModel.getMode());
//                }
//            }
//        });
//
//        if (viewModel.isHasOtherMethod()) {
//            chooseOtherMethod.setVisibility(View.VISIBLE);
//            chooseOtherMethod.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    if (getActivity() instanceof VerificationActivity) {
//                        ((VerificationActivity) getActivity()).goToSelectVerificationMethod();
//                    }
//                }
//            });
//        } else {
//            chooseOtherMethod.setVisibility(View.GONE);
//        }
//    }
//}
