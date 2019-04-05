package com.tokopedia.loginregister.loginthirdparty.google;

import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.credentials.CredentialRequest;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.CommonStatusCodes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Status;
import com.tokopedia.abstraction.AbstractionRouter;
import com.tokopedia.loginregister.R;
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics;

public class SmartLockActivity extends AppCompatActivity implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {


    private static final String TAG = "SmartLock";
    public static final int RC_SAVE = 1;
    public static final int RC_READ = 3;
    public static final int RC_SAVE_SECURITY_QUESTION = 4;
    private static final String IS_RESOLVING = "is_resolving";
    private static final String IS_REQUESTING = "is_requesting";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String STATE = "state";

    // Add mGoogleApiClient and mIsResolving fields here.
    private boolean mIsResolving;
    private boolean mIsRequesting;

    private GoogleApiClient mGoogleApiClient;
    private static final String TOKOPEDIA_PROVIDER = "https://www.tokopedia.com";
    private LoginRegisterAnalytics analytics;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        analytics = new LoginRegisterAnalytics();

        setContentView(R.layout.activity_smartlock);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .addApi(Auth.CREDENTIALS_API)
                .build();

        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(IS_RESOLVING);
            mIsRequesting = savedInstanceState.getBoolean(IS_REQUESTING);
        }
    }

    private void processBundle(Bundle extras) {
        String username = extras.getString(USERNAME);
        String password = extras.getString(PASSWORD);
        if (isValidCredential(username, password)) {
            Credential credential = new Credential.Builder(username)
                    .setPassword(password)
                    .build();
            saveCredential(credential);
        } else {
            goToContent();
        }
    }

    protected void saveCredential(Credential credential) {
        // Credential is valid so save it.
        Auth.CredentialsApi.save(mGoogleApiClient,
                credential).setResultCallback(status -> {
            if (status.isSuccess()) {
                Log.d(TAG, "Credential saved");
                goToContent();
            } else {
                //                    If the credential is new the user must give permission for the credential to be saved.
                Log.d(TAG, "Attempt to save credential failed " +
                        status.getStatusMessage() + " " +
                        status.getStatusCode());
                resolveResult(status, RC_SAVE);
            }
        });
    }

    private void resolveResult(Status status, int requestCode) {
        // We don't want to fire multiple resolutions at once since that
        // can result in stacked dialogs after rotation or another
        // similar event.
        if (mIsResolving) {
            Log.w(TAG, "resolveResult: already resolving.");
            return;
        }

        Log.d(TAG, "Resolving: " + status);
        if (status.hasResolution()) {
            Log.d(TAG, "STATUS: RESOLVING");
            try {
                status.startResolutionForResult(this, requestCode);
                mIsResolving = true;
            } catch (IntentSender.SendIntentException e) {
                Log.e(TAG, "STATUS: Failed to send resolution.", e);
            }
        } else {
            Log.e(TAG, "STATUS: FAIL");
            goToContent();
        }
    }

    private void requestCredentials() {
        mIsRequesting = true;

        CredentialRequest request = new CredentialRequest.Builder()
                .setAccountTypes(TOKOPEDIA_PROVIDER)
                .setPasswordLoginSupported(true)
                .build();
        if (mGoogleApiClient.isConnected()) {
            Auth.CredentialsApi.request(mGoogleApiClient, request).setResultCallback(
                    credentialRequestResult -> {
                        mIsRequesting = false;
                        Status status = credentialRequestResult.getStatus();
                        if (credentialRequestResult.getStatus().isSuccess()) {
                            // Successfully read the credential without any user interaction, this
                            // means there was only a single credential and the user has auto
                            // sign-in enabled.
                            Credential credential = credentialRequestResult.getCredential();
                            processRetrievedCredential(credential);
                        } else if (status.getStatusCode() == CommonStatusCodes.RESOLUTION_REQUIRED) {
                            // This is most likely the case where the user has multiple saved
                            // credentials and needs to pick one.
                            resolveResult(status, RC_READ);
                        } else if (status.getStatusCode() == CommonStatusCodes.SIGN_IN_REQUIRED) {
                            // This is most likely the case where the user does not currently
                            // have any saved credentials and thus needs to provide a username
                            // and password xto sign in.
                            Log.d(TAG, "Sign in required");
                            goToContent();
                        } else {
                            Log.w(TAG, "Unrecognized status code: " + status.getStatusCode());
                            goToContent();
                        }
                    }
            );
        } else {
            mIsRequesting = false;
            Log.d(TAG, "Google Api Client is not connected yet");
            goToContent();
        }
    }

    private void processRetrievedCredential(Credential credential) {
        Bundle bundle = new Bundle();
        bundle.putString(SmartLockActivity.USERNAME, credential.getId());
        bundle.putString(SmartLockActivity.PASSWORD, credential.getPassword());
        setResult(RESULT_OK, new Intent().putExtras(bundle));
        if (mGoogleApiClient != null && mGoogleApiClient.isConnected()) {
            Auth.CredentialsApi.disableAutoSignIn(mGoogleApiClient);
        }
        finish();
    }

    //    Once the user has responded to the dialog, indicating whether or not they want to save the credential, the Activity must handle that response
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d(TAG, "onActivityResult:" + requestCode + ":" + resultCode + ":" +
                data);

        if (requestCode == RC_READ) {
            if (resultCode == RESULT_OK) {
                Credential credential = data.getParcelableExtra(Credential.EXTRA_KEY);
                processRetrievedCredential(credential);
            } else {
                Log.e(TAG, "Credential Read: NOT OK");
                goToContent();
            }
        } else if (requestCode == RC_SAVE) {
            Log.d(TAG, "Result code: " + resultCode);
            if (resultCode == RESULT_OK) {
                analytics.eventSmartLockSaveCredential();
                Log.d(TAG, "Credential Save: OK");
            } else {
                analytics.eventSmartLockNeverSaveCredential();
                Log.e(TAG, "Credential Save Failed");
            }
            goToContent();
        }
        mIsResolving = false;
    }

    private void goToContent() {
        if (getIntent() != null && getIntent().getExtras() != null) {
            setResult(getIntent().getExtras().getInt(STATE));
            if (mGoogleApiClient.isConnected()) {
                Auth.CredentialsApi.disableAutoSignIn(mGoogleApiClient);
            }
        }
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save the user's current sign in state
        savedInstanceState.putBoolean(IS_RESOLVING, mIsResolving);
        savedInstanceState.putBoolean(IS_REQUESTING, mIsRequesting);

        // Always call the superclass so it can save the view hierarchy state
        super.onSaveInstanceState(savedInstanceState);
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        Auth.CredentialsApi.disableAutoSignIn(mGoogleApiClient);
        if (getIntent() != null && getIntent().getExtras() != null) {
            switch (getIntent().getExtras().getInt(STATE)) {
                case RC_READ:
                    requestCredentials();
                    break;
                case RC_SAVE:
                case RC_SAVE_SECURITY_QUESTION:
                    processBundle(getIntent().getExtras());
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    private boolean isValidCredential(String username, String password) {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password);
    }

}
