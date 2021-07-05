package com.tokopedia.loginregister.loginthirdparty.facebook

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import com.facebook.*
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import com.google.gson.Gson
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.loginthirdparty.facebook.data.FacebookRequestData
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import com.tokopedia.usecase.RequestParams
import com.tokopedia.utils.phonenumber.PhoneNumberUtil.transform
import org.json.JSONObject
import javax.inject.Inject

/**
 * @author by nisie on 10/11/17.
 */
open class GetFacebookCredentialUseCase @Inject constructor() {

    open fun execute(requestParams: RequestParams, subscriber: GetFacebookCredentialSubscriber) {
        val fragment = requestParams.getObject(PARAM_FRAGMENT) as Fragment
        val callbackManager = requestParams.getObject(PARAM_CALLBACK_MANAGER) as CallbackManager
        promptFacebookLogin(fragment, callbackManager, subscriber)
    }

    private fun promptFacebookLogin(fragment: Fragment, callbackManager: CallbackManager, subscriber: GetFacebookCredentialSubscriber) {
        FacebookSdk.fullyInitialize()
        LoginManager.getInstance().logInWithReadPermissions(fragment, READ_PERMISSIONS)
        LoginManager.getInstance().registerCallback(callbackManager, object : FacebookCallback<LoginResult> {
            override fun onSuccess(loginResult: LoginResult) {
                if (fragment.context == null) return
                if (TextUtils.isEmpty(loginResult.accessToken.token)) {
                    LoginManager.getInstance().logOut()
                    subscriber.onError(MessageErrorException(
                            fragment.context?.getString(R.string.facebook_error_empty_token)))
                } else {
                    getFacebookEmail(loginResult.accessToken, subscriber, fragment.activity)
                }
            }

            override fun onCancel() {
                LoginManager.getInstance().logOut()
            }

            override fun onError(e: FacebookException) {
                if (fragment.context == null) return
                LoginManager.getInstance().logOut()
                if (e is FacebookAuthorizationException) {
                    subscriber.onError(MessageErrorException(
                            fragment.context?.getString(R.string.facebook_error_not_authorized)))
                } else {
                    subscriber.onError(MessageErrorException(
                            fragment.context?.getString(R.string.facebook_error_not_authorized)))
                }
            }
        })
    }

    private fun getFacebookEmail(accessToken: AccessToken, subscriber: GetFacebookCredentialSubscriber, context: Context?) {
        val request = GraphRequest.newMeRequest(
                accessToken
        ) { `object`: JSONObject?, response: GraphResponse ->
            if (response.error == null && `object` != null) {
                val gson = Gson()
                val data = gson.fromJson(`object`.toString(), FacebookRequestData::class.java)
                if (data != null) {
                    when {
                        data.email.isNotEmpty() -> {
                            subscriber.onSuccessEmail(accessToken, data.email)
                        }
                        data.phone.isNotEmpty() -> {
                            subscriber.onSuccessPhone(accessToken,
                                    transform(data.phone))
                        }
                        else -> {
                            LoginManager.getInstance().logOut()
                            subscriber.onError(MessageErrorException(
                                    context?.getString(R.string.error_login_using_facebook)))
                        }
                    }
                }
            } else {
                val errorCode = response.error.errorCode
                val subErrorCode = response.error.subErrorCode
                val errorMessage = (context?.getString(R.string.error_login_using_facebook)
                        + " " + errorCode + " " + subErrorCode)
                subscriber.onError(MessageErrorException(errorMessage))
            }
        }
        val parameters = Bundle()
        val permission = getFacebookPermission(context)
        parameters.putString("fields", permission)
        request.parameters = parameters
        request.executeAsync()
    }

    private fun getFacebookPermission(context: Context?): String {
        val firebaseRemoteConfig = FirebaseRemoteConfigImpl(context)
        val remoteConfigValue = firebaseRemoteConfig.getString(REMOTE_CONFIG_KEY_FB_PERMISSION, "")
        return if (remoteConfigValue.isEmpty()) {
            DEFAULT_FB_PERMISSION
        } else {
            remoteConfigValue
        }
    }

    companion object {
        private const val PARAM_CALLBACK_MANAGER = "PARAM_CALLBACK_MANAGER"
        private const val PARAM_FRAGMENT = "PARAM_FRAGMENT"
        private val READ_PERMISSIONS = listOf("public_profile", "email")
        private const val DEFAULT_FB_PERMISSION = "email"
        private const val REMOTE_CONFIG_KEY_FB_PERMISSION = "android_user_fb_permission"
        fun getParam(fragment: Fragment?, callbackManager: CallbackManager?): RequestParams {
            val params = RequestParams.create()
            params.putObject(PARAM_FRAGMENT, fragment)
            params.putObject(PARAM_CALLBACK_MANAGER, callbackManager)
            return params
        }
    }
}