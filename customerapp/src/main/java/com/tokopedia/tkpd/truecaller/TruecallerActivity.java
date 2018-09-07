package com.tokopedia.tkpd.truecaller;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import com.tokopedia.tkpd.R;
import com.truecaller.android.sdk.ITrueCallback;
import com.truecaller.android.sdk.TrueClient;
import com.truecaller.android.sdk.TrueError;
import com.truecaller.android.sdk.TrueProfile;

public class TruecallerActivity extends Activity implements ITrueCallback {

    private TrueClient mTrueClient;
    private String TAG = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_truecaller);
        mTrueClient = new TrueClient(this, this);
        mTrueClient.getTruecallerUserProfile(this);
    }

    @Override
    public void onSuccesProfileShared(@NonNull TrueProfile trueProfile) {
        setResult(RESULT_OK, new Intent().putExtra("phone", trueProfile.phoneNumber));
        finish();
    }

    @Override
    public void onFailureProfileShared(@NonNull TrueError trueError) {
        switch (trueError.getErrorType()) {
            case TrueError.ERROR_TYPE_USER_DENIED:
            case TrueError.ERROR_TYPE_UNAUTHORIZED_USER:
                setResult(RESULT_OK, new Intent().putExtra("error", getString(R.string.error_user_truecaller)));
                finish();
                break;
            default:
                setResult(RESULT_OK, new Intent().putExtra("error", String.format("%s (%s)", getString(R.string.error_fetch_truecaller)
                        , getString(R.string.error_code_truecaller, trueError.getErrorType()))));
                finish();
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mTrueClient.onActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public static Intent getCallingIntent(Context context) {
        return new Intent(context, TruecallerActivity.class);
    }
}
