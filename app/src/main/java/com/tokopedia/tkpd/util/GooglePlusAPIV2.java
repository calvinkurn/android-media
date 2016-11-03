package com.tokopedia.tkpd.util;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.os.Bundle;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

/**
 * Created by hangnadi on 6/17/15.
 */
public class GooglePlusAPIV2 implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    public static final int RC_SIGN_IN = 8844;
    private boolean mIntentInProgress;
    private Activity context;
    private onLoginSuccessListener listener;

    public GooglePlusAPIV2(Activity context) {
        this.context = context;
        this.mGoogleApiClient = (new GoogleApiClient.Builder(context))
                .addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_PROFILE)
                .build();
    }

    public void setOnLoginSuccessListener(onLoginSuccessListener listener) {
        this.listener = listener;
    }

    public void Login() {
        this.mGoogleApiClient = (new GoogleApiClient.Builder(this.context)).addConnectionCallbacks(this).addOnConnectionFailedListener(this).addApi(Plus.API).addScope(Plus.SCOPE_PLUS_LOGIN).build();
        this.mGoogleApiClient.connect();
    }

    public void GetProfile() {
        if(!this.mGoogleApiClient.isConnected()) {
            this.Login();
        } else {
            this.listener.onLoginSuccess(Plus.PeopleApi.getCurrentPerson(this.mGoogleApiClient), Plus.AccountApi.getAccountName(this.mGoogleApiClient));
        }

    }

    public void onConnectionFailed(ConnectionResult result) {
        if(!this.mIntentInProgress && result.hasResolution()) {
            try {
                this.mIntentInProgress = true;
                result.startResolutionForResult(this.context, 8844);
            } catch (IntentSender.SendIntentException var3) {
                this.mIntentInProgress = false;
                this.mGoogleApiClient.connect();
            }
        }

    }

    public void onStop() {
        if(this.mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(this.mGoogleApiClient);
            this.mGoogleApiClient.disconnect();
        }

    }

    public boolean SignOut() {
        System.out.println("Signing out");
        if(this.mGoogleApiClient.isConnected()) {
            System.out.println("Is Signed in");
            Plus.AccountApi.clearDefaultAccount(this.mGoogleApiClient);
            this.mGoogleApiClient.disconnect();
            System.out.println("Singout completed");
            return false;
        } else {
            return true;
        }
    }

    public void onActivityResult(int requestCode, int responseCode, Intent intent) {
        System.out.println("responseCode " + responseCode + " isconnected " + this.mGoogleApiClient.isConnected());
        if(requestCode == 8844) {
            this.mIntentInProgress = false;
            if(responseCode == -1 && this.mGoogleApiClient.isConnected()) {
                this.listener.onLoginSuccess(Plus.PeopleApi.getCurrentPerson(this.mGoogleApiClient), Plus.AccountApi.getAccountName(this.mGoogleApiClient));
            }

            if(!this.mGoogleApiClient.isConnecting() && responseCode == -1) {
                this.mGoogleApiClient.connect();
            } else {
                this.listener.onLoginFailed();
                this.mGoogleApiClient.disconnect();
            }
        }

    }

    public void onConnected(Bundle arg0) {
        this.listener.onLoginSuccess(Plus.PeopleApi.getCurrentPerson(this.mGoogleApiClient), Plus.AccountApi.getAccountName(this.mGoogleApiClient));
    }

    public void onConnectionSuspended(int arg0) {
        this.mGoogleApiClient.connect();
    }

    public interface onLoginSuccessListener {
        void onLoginSuccess(Person var1, String var2);
        void onLoginFailed();
    }
}
