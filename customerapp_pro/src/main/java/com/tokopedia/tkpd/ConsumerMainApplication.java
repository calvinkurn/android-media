package com.tokopedia.tkpd;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;

import com.tokopedia.applink.ApplinkConst;
import com.tokopedia.applink.RouteManager;
import com.tokopedia.authentication.AuthHelper;
import com.tokopedia.config.GlobalConfig;
import com.tokopedia.core.network.retrofit.utils.AuthUtil;
import com.tokopedia.device.info.DeviceInfo;
import com.tokopedia.intl.BuildConfig;
import com.tokopedia.navigation.presentation.activity.MainParentActivity;
import com.tokopedia.screenshot_observer.Screenshot;
import com.tokopedia.tkpd.deeplink.DeeplinkHandlerActivity;
import com.tokopedia.tkpd.deeplink.activity.DeepLinkActivity;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import kotlin.Pair;
import timber.log.Timber;

/**
 * Created by ricoharisin on 11/11/16.
 */

public class ConsumerMainApplication extends com.tokopedia.tkpd.app.ConsumerMainApplication {

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    protected void setVersionName() {
        Pair<String, String> versions = AuthHelper.getVersionName(BuildConfig.VERSION_NAME);
        String version = versions.getFirst();
        String suffixVersion = versions.getSecond();

        if (!version.equalsIgnoreCase(AuthHelper.ERROR)) {
            GlobalConfig.VERSION_NAME = version;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = version;
            com.tokopedia.config.GlobalConfig.VERSION_NAME_SUFFIX = suffixVersion;
        } else {
            GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
            com.tokopedia.config.GlobalConfig.VERSION_NAME = BuildConfig.VERSION_NAME;
        }
        com.tokopedia.config.GlobalConfig.RAW_VERSION_NAME = BuildConfig.VERSION_NAME;// save raw version name
    }

    @NonNull
    @Override
    public String versionName() {
        return BuildConfig.VERSION_NAME;
    }

    @Override
    public void registerActivityLifecycleCallbacks() {
        registerActivityLifecycleCallbacks(new Screenshot(getApplicationContext().getContentResolver(), new Screenshot.BottomSheetListener() {
            @Override
            public void onFeedbackClicked(Uri uri, String className, boolean isFromScreenshot) {
                openFeedbackForm(uri, className, isFromScreenshot);
            }
        }));
    }

    public void initConfigValues() {
        setVersionCode();
        setVersionName();

        GlobalConfig.APPLICATION_TYPE = 3;
        GlobalConfig.PACKAGE_APPLICATION = "com.tokopedia.intl";

        GlobalConfig.DEBUG = BuildConfig.DEBUG;
        GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.DEBUG = BuildConfig.DEBUG;
        com.tokopedia.config.GlobalConfig.ENABLE_DISTRIBUTION = BuildConfig.ENABLE_DISTRIBUTION;
        com.tokopedia.config.GlobalConfig.IS_PREINSTALL = BuildConfig.IS_PREINSTALL;
        com.tokopedia.config.GlobalConfig.PREINSTALL_NAME = BuildConfig.PREINSTALL_NAME;
        com.tokopedia.config.GlobalConfig.PREINSTALL_DESC = BuildConfig.PREINSTALL_DESC;
        com.tokopedia.config.GlobalConfig.PREINSTALL_SITE = BuildConfig.PREINSTALL_SITE;
        com.tokopedia.config.GlobalConfig.APPLICATION_ID = BuildConfig.APPLICATION_ID;
        com.tokopedia.config.GlobalConfig.ENABLE_DEBUG_TRACE = BuildConfig.ENABLE_DEBUG_TRACE;
        com.tokopedia.config.GlobalConfig.HOME_ACTIVITY_CLASS_NAME = MainParentActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_HANDLER_ACTIVITY_CLASS_NAME = DeeplinkHandlerActivity.class.getName();
        com.tokopedia.config.GlobalConfig.DEEPLINK_ACTIVITY_CLASS_NAME = DeepLinkActivity.class.getName();
        if (com.tokopedia.config.GlobalConfig.DEBUG) {
            com.tokopedia.config.GlobalConfig.DEVICE_ID = DeviceInfo.getAndroidId(this);
        }
        if (BuildConfig.DEBUG_TRACE_NAME != null) {
            com.tokopedia.config.GlobalConfig.DEBUG_TRACE_NAME = BuildConfig.DEBUG_TRACE_NAME.split(",");
        }
        generateConsumerAppNetworkKeys();
    }

    protected void setVersionCode() {
        try {
            PackageInfo pInfo = this.getPackageManager().getPackageInfo(this.getPackageName(), 0);
            GlobalConfig.VERSION_CODE = pInfo.versionCode;
            com.tokopedia.config.GlobalConfig.VERSION_CODE = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
            com.tokopedia.config.GlobalConfig.VERSION_CODE = BuildConfig.VERSION_CODE;
        }
    }

    public void generateConsumerAppNetworkKeys() {
        AuthUtil.KEY.KEY_CREDIT_CARD_VAULT = ConsumerAppNetworkKeys.CREDIT_CARD_VAULT_AUTH_KEY;
        AuthUtil.KEY.ZEUS_WHITELIST = ConsumerAppNetworkKeys.ZEUS_WHITELIST;
    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    private native byte[] bytesFromJNI();

    // Used to load the 'native-lib' library on application startup.
    static {
        System.loadLibrary("native-lib");
    }

    public boolean checkAppSignature() {
        try {
            PackageInfo info;
            boolean signatureValid = false;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNING_CERTIFICATES);
                if (null != info && info.signingInfo.getApkContentsSigners().length > 0) {
                    byte[] rawCertNative = bytesFromJNI();
                    // handle if the library is failing
                    if (rawCertNative == null) {
                        return true;
                    }
                    byte[] rawCertJava = info.signingInfo.getApkContentsSigners()[0].toByteArray();
                    signatureValid = getInfoFromBytes(rawCertJava).equals(getInfoFromBytes(rawCertNative));
                }
            } else {
                info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
                if (null != info && info.signatures.length > 0) {
                    byte[] rawCertNative = bytesFromJNI();
                    // handle if the library is failing
                    if (rawCertNative == null) {
                        return true;
                    }
                    byte[] rawCertJava = info.signatures[0].toByteArray();
                    signatureValid = getInfoFromBytes(rawCertJava).equals(getInfoFromBytes(rawCertNative));
                }
            }
            if (!signatureValid) {
                Timber.w("P1#APP_SIGNATURE_FAILED#'certJava!=certNative'");
            }
            return signatureValid;
        } catch (PackageManager.NameNotFoundException e) {
            Timber.w("P1#APP_SIGNATURE_FAILED#'PackageManager.NameNotFoundException'");
            return false;
        }
    }

    private String getInfoFromBytes(byte[] bytes) {
        if (null == bytes) {
            return "null";
        }

        /*
         * Get the X.509 certificate.
         */
        InputStream certStream = new ByteArrayInputStream(bytes);
        StringBuilder sb = new StringBuilder();
        try {
            CertificateFactory certFactory = CertificateFactory.getInstance("X509");
            X509Certificate x509Cert = (X509Certificate) certFactory.generateCertificate(certStream);

            sb.append("Certificate subject: ").append(x509Cert.getSubjectDN()).append("\n");
            sb.append("Certificate issuer: ").append(x509Cert.getIssuerDN()).append("\n");
            sb.append("Certificate serial number: ").append(x509Cert.getSerialNumber()).append("\n");
            MessageDigest md;
            try {
                md = MessageDigest.getInstance("MD5");
                md.update(bytes);
                byte[] byteArray = md.digest();
                sb.append("MD5: ").append(bytesToString(byteArray)).append("\n");
                md.reset();
                md = MessageDigest.getInstance("SHA");
                md.update(bytes);
                byteArray = md.digest();
                sb.append("SHA1: ").append(bytesToString(byteArray)).append("\n");
                md.reset();
                md = MessageDigest.getInstance("SHA256");
                md.update(bytes);
                byteArray = md.digest();
                sb.append("SHA256: ").append(bytesToString(byteArray)).append("\n");
            } catch (NoSuchAlgorithmException e) {
                e.printStackTrace();
            }


            sb.append("\n");
        } catch (CertificateException e) {

        }
        return sb.toString();
    }


    private String bytesToString(byte[] bytes) {
        StringBuilder md5StrBuff = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            if (Integer.toHexString(0xFF & bytes[i]).length() == 1) {
                md5StrBuff.append("0").append(Integer.toHexString(0xFF & bytes[i]));
            } else {
                md5StrBuff.append(Integer.toHexString(0xFF & bytes[i]));
            }
            if (bytes.length - 1 != i) {
                md5StrBuff.append(":");
            }
        }
        return md5StrBuff.toString();
    }


    private void openFeedbackForm(Uri uri, String className, boolean isFromScreenshot) {
        Intent intent = RouteManager.getIntent(getApplicationContext(), ApplinkConst.FEEDBACK_FORM);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.putExtra("EXTRA_URI_IMAGE", uri);
        intent.putExtra("EXTRA_IS_CLASS_NAME", className);
        intent.putExtra("EXTRA_IS_FROM_SCREENSHOT", isFromScreenshot);
        getApplicationContext().startActivity(intent);
    }
}
