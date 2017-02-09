package com.tokopedia.sellerapp.truecaller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueClient;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;

public class TruecallerActivity extends Activity implements ITrueCallback{

    private TrueClient mTrueClient;
    private String TAG= "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTrueClient = new TrueClient(this, this);
        mTrueClient.getTruecallerUserProfile(this);

//        Context var1 = this;
//        String var2 = null;
//
//        ApplicationInfo var3 = null;
//        try {
//            var3 = var1.getPackageManager().getApplicationInfo(var1.getPackageName(), PackageManager.GET_META_DATA);
//        } catch (PackageManager.NameNotFoundException var5) {
//
//        }
//
//        if(null != var3 && null != var3.metaData) {
//            Object var4 = var3.metaData.get("com.truecaller.android.sdk.PartnerKey");
//            if(var4 instanceof String) {
//                var2 = (String)var4;
//            }
//
//
//        } else {
//
//        }
    }

    @Override
    public void onSuccesProfileShared(@NonNull TrueProfile trueProfile) {
        setResult(RESULT_OK, new Intent().putExtra("phone",trueProfile.phoneNumber));
        finish();
    }

    @Override
    public void onFailureProfileShared(@NonNull TrueError trueError) {
        setResult(RESULT_OK, null);
        finish();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if(mTrueClient.onActivityResult(requestCode,resultCode,data)){
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }
}
