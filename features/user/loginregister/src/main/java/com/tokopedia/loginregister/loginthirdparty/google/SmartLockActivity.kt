package com.tokopedia.loginregister.loginthirdparty.google

import android.app.Activity
import android.content.Intent
import android.content.IntentSender.SendIntentException
import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.credentials.Credential
import com.google.android.gms.auth.api.credentials.CredentialRequest
import com.google.android.gms.auth.api.credentials.CredentialRequestResult
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.common.api.GoogleApiClient
import com.google.android.gms.common.api.Status
import com.tokopedia.iris.util.IrisSession
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.LoginRegisterAnalytics
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import timber.log.Timber


class SmartLockActivity : AppCompatActivity(), GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private var mIsResolving = false
    private var mIsRequesting = false
    private var mGoogleApiClient: GoogleApiClient? = null
    private var analytics: LoginRegisterAnalytics? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userSessionInterface: UserSessionInterface = UserSession(this)
        val irisSession = IrisSession(this)
        analytics = LoginRegisterAnalytics(userSessionInterface, irisSession)
        setContentView(R.layout.activity_smartlock)
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build()
        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
        mGoogleApiClient = GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .enableAutoManage(this, 0, this)
                .addApi(Auth.CREDENTIALS_API)
                .build()
        if (savedInstanceState != null) {
            mIsResolving = savedInstanceState.getBoolean(IS_RESOLVING)
            mIsRequesting = savedInstanceState.getBoolean(IS_REQUESTING)
        }
    }

    private fun processBundle(extras: Bundle?) {
        val username = extras!!.getString(USERNAME)
        val password = extras.getString(PASSWORD)
        if (isValidCredential(username, password)) {
            val credential = Credential.Builder(username)
                    .setPassword(password)
                    .build()
            saveCredential(credential)
        } else {
            goToContent()
        }
    }

    protected fun saveCredential(credential: Credential?) {
        // Credential is valid so save it.
        Auth.CredentialsApi.save(mGoogleApiClient,
                credential).setResultCallback { status: Status ->
            if (status.isSuccess) {
                Timber.tag(TAG).d("Credential saved")
                goToContent()
            } else {
                //                    If the credential is new the user must give permission for the credential to be saved.
                Timber.tag(TAG).d("Attempt to save credential failed ${status.statusMessage} ${status.statusCode}")
                resolveResult(status, RC_SAVE)
            }
        }
    }

    private fun resolveResult(status: Status, requestCode: Int) {
        // We don't want to fire multiple resolutions at once since that
        // can result in stacked dialogs after rotation or another
        // similar event.
        if (mIsResolving) {
            Timber.tag(TAG).d("resolveResult: already resolving.")
            return
        }
        Timber.tag(TAG).d("Resolving: $status")
        if (status.hasResolution()) {
            Timber.tag(TAG).d("STATUS: RESOLVING")
            try {
                status.startResolutionForResult(this, requestCode)
                mIsResolving = true
            } catch (e: SendIntentException) {
                Timber.tag(TAG).e(e, "STATUS: Failed to send resolution.")
            }
        } else {
            Timber.tag(TAG).d("STATUS: FAIL")
            goToContent()
        }
    }

    private fun requestCredentials() {
        mIsRequesting = true
        val request = CredentialRequest.Builder()
                .setAccountTypes(TOKOPEDIA_PROVIDER)
                .setPasswordLoginSupported(true)
                .build()
        if (mGoogleApiClient!!.isConnected) {
            Auth.CredentialsApi.request(mGoogleApiClient, request).setResultCallback { credentialRequestResult: CredentialRequestResult ->
                mIsRequesting = false
                val status = credentialRequestResult.status
                if (credentialRequestResult.status.isSuccess) {
                    // Successfully read the credential without any user interaction, this
                    // means there was only a single credential and the user has auto
                    // sign-in enabled.
                    val credential = credentialRequestResult.credential
                    processRetrievedCredential(credential)
                } else if (status.statusCode == CommonStatusCodes.RESOLUTION_REQUIRED) {
                    // This is most likely the case where the user has multiple saved
                    // credentials and needs to pick one.
                    resolveResult(status, RC_READ)
                } else if (status.statusCode == CommonStatusCodes.SIGN_IN_REQUIRED) {
                    // This is most likely the case where the user does not currently
                    // have any saved credentials and thus needs to provide a username
                    // and password xto sign in.
                    Timber.tag(TAG).d("Sign in required")
                    goToContent()
                } else {
                    Timber.tag(TAG).d("Unrecognized status code: ${status.statusCode}")
                    goToContent()
                }
            }
        } else {
            mIsRequesting = false
            Timber.tag(TAG).d("Google Api Client is not connected yet")
            goToContent()
        }
    }

    private fun processRetrievedCredential(credential: Credential) {
        val bundle = Bundle()
        bundle.putString(USERNAME, credential.id)
        bundle.putString(PASSWORD, credential.password)
        setResult(Activity.RESULT_OK, Intent().putExtras(bundle))
        if (mGoogleApiClient != null && mGoogleApiClient!!.isConnected) {
            Auth.CredentialsApi.disableAutoSignIn(mGoogleApiClient)
        }
        finish()
    }

    //    Once the user has responded to the dialog, indicating whether or not they want to save the credential, the Activity must handle that response
    public override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        Timber.tag(TAG).d("onActivityResult:$requestCode:$resultCode:$data")
        if (requestCode == RC_READ) {
            if (resultCode == Activity.RESULT_OK) {
                val credential: Credential? = data?.getParcelableExtra(Credential.EXTRA_KEY)
                credential?.let { processRetrievedCredential(it) }
            } else {
                Timber.tag(TAG).d("Credential Read: NOT OK")
                goToContent()
            }
        } else if (requestCode == RC_SAVE) {
            Timber.tag(TAG).d("Result code: $resultCode")
            if (resultCode == Activity.RESULT_OK) {
                analytics!!.eventSmartLockSaveCredential()
                Timber.tag(TAG).d("Credential Save: OK")
            } else {
                analytics!!.eventSmartLockNeverSaveCredential()
                Timber.tag(TAG).d("Credential Save Failed")
            }
            goToContent()
        }
        mIsResolving = false
    }

    private fun goToContent() {
        if (intent != null && intent.extras != null) {
            setResult(intent.extras!!.getInt(STATE))
            if (mGoogleApiClient!!.isConnected) {
                Auth.CredentialsApi.disableAutoSignIn(mGoogleApiClient)
            }
        }
        finish()
    }

    public override fun onSaveInstanceState(savedInstanceState: Bundle) {
        savedInstanceState.putBoolean(IS_RESOLVING, mIsResolving)
        savedInstanceState.putBoolean(IS_REQUESTING, mIsRequesting)
        super.onSaveInstanceState(savedInstanceState)
    }

    override fun onConnected(bundle: Bundle?) {
        Auth.CredentialsApi.disableAutoSignIn(mGoogleApiClient)
        if (intent != null && intent.extras != null) {
            when (intent.extras!!.getInt(STATE)) {
                RC_READ -> requestCredentials()
                RC_SAVE, RC_SAVE_SECURITY_QUESTION -> processBundle(intent.extras)
                else -> {
                }
            }
        }
    }

    override fun onConnectionSuspended(i: Int) {}
    override fun onConnectionFailed(connectionResult: ConnectionResult) {}
    private fun isValidCredential(username: String?, password: String?): Boolean {
        return !TextUtils.isEmpty(username) && !TextUtils.isEmpty(password)
    }

    companion object {
        private const val TAG = "SmartLock"
        const val RC_SAVE = 1
        const val RC_READ = 3
        const val RC_SAVE_SECURITY_QUESTION = 4
        private const val IS_RESOLVING = "is_resolving"
        private const val IS_REQUESTING = "is_requesting"
        const val USERNAME = "username"
        const val PASSWORD = "password"
        const val STATE = "state"
        private const val TOKOPEDIA_PROVIDER = "https://www.tokopedia.com"
    }
}