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
