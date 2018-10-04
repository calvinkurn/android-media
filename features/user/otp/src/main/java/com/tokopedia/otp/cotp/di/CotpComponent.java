package com.tokopedia.otp.cotp.di;

import android.content.Context;

import com.readystatesoftware.chuck.ChuckInterceptor;
import com.tokopedia.otp.common.di.MethodListQualifier;
import com.tokopedia.otp.common.di.OtpComponent;
import com.tokopedia.otp.cotp.view.activity.VerificationActivity;
import com.tokopedia.otp.cotp.view.fragment.ChooseVerificationMethodFragment;
import com.tokopedia.otp.cotp.view.fragment.VerificationFragment;

import dagger.Component;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;

/**
 * @author by nisie on 4/24/18.
 */

@CotpScope
@Component(modules = {CotpModule.class}, dependencies = OtpComponent.class)
public interface CotpComponent {

    void inject(VerificationActivity activity);

    void inject(ChooseVerificationMethodFragment fragment);

    void inject(VerificationFragment fragment);

}