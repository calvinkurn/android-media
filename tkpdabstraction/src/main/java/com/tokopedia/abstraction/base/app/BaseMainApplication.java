package com.tokopedia.abstraction.base.app;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDexApplication;
import android.util.Base64;
import android.util.Log;


import com.tokopedia.abstraction.common.di.component.BaseAppComponent;
import com.tokopedia.abstraction.common.di.component.DaggerBaseAppComponent;
import com.tokopedia.abstraction.common.di.module.AppModule;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

/**
 * Created by User on 10/24/2017.
 */

public class BaseMainApplication extends MultiDexApplication {

    private BaseAppComponent baseAppComponent;

    public BaseAppComponent reinitBaseAppComponent(AppModule appModule){
        return baseAppComponent = DaggerBaseAppComponent.builder()
                .appModule(appModule).build();
    }

    public BaseAppComponent getBaseAppComponent(){
        if (baseAppComponent == null) {
            DaggerBaseAppComponent.Builder daggerBuilder = DaggerBaseAppComponent.builder()
                    .appModule(new AppModule(this));
            baseAppComponent = daggerBuilder.build();
        }
        return baseAppComponent;
    }

    protected static final int VALID = 0;

    protected static final int INVALID = 1;

    public static final String SIGNATURE = "j7iaQ6MRmax40fHCokI4i6Hbn64=";




    protected static int checkAppSignature(Context context) {

        try {

            PackageInfo packageInfo = context.getPackageManager()

                    .getPackageInfo(context.getPackageName(),

                            PackageManager.GET_SIGNATURES);

            for (Signature signature : packageInfo.signatures) {

                byte[] signatureBytes = signature.toByteArray();

                MessageDigest md = MessageDigest.getInstance("SHA");

                md.update(signature.toByteArray());

                final String currentSignature = Base64.encodeToString(md.digest(), Base64.DEFAULT);

                Log.i("REMOVE_ME", "Include this string as a value for SIGNATURE:" + currentSignature);

                //compare signatures

                if (SIGNATURE.equals(currentSignature)){

                    return VALID;

                };

            }

        } catch (Exception e) {

//assumes an issue in checking signature., but we let the caller decide on what to do.

        }

        return INVALID;

    }
}