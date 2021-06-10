package com.tokopedia.loginregister.external_register.base.fragment

import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.JsonParser
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.login.service.RegisterPushNotifService
import com.tokopedia.loginregister.registerinitial.view.fragment.RegisterInitialFragment
import com.tokopedia.remoteconfig.FirebaseRemoteConfigImpl
import java.net.URLDecoder

/**
 * Created by Yoris Prayogo on 12/01/21.
 * Copyright (c) 2021 PT. Tokopedia All rights reserved.
 */
open class ExternalRegisterInitialFragment: BaseDaggerFragment() {

    override fun initInjector() {}
    override fun getScreenName(): String = ""

    private lateinit var sharedPrefs: SharedPreferences
    private var isHitRegisterPushNotif: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_external_register, container, false)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        fetchRemoteConfig()
    }

    private fun fetchRemoteConfig() {
        context?.let {
            val firebaseRemoteConfig = FirebaseRemoteConfigImpl(it)
            isHitRegisterPushNotif = firebaseRemoteConfig.getBoolean(RegisterInitialFragment.REMOTE_CONFIG_KEY_REGISTER_PUSH_NOTIF, false)
        }
    }

    private fun registerPushNotif() {
        if (isHitRegisterPushNotif && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            activity?.let {
                RegisterPushNotifService.startService(it.applicationContext)
            }
        }
    }

    fun getAuthCodeFromUrl(url: String): String {
        return try {
            val uri = Uri.parse(URLDecoder.decode(url, Charsets.UTF_8.name()))
            val response = uri.getQueryParameter("response") ?: ""
            val parser = JsonParser()
            parser.parse(response).getAsJsonObject().get("authCode").asString
        } catch (e: Exception) {
            ""
        }
    }

    fun goToAddPin2FA(enableSkip2FA: Boolean) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.ADD_PIN)
        intent.putExtras(Bundle().apply {
            putBoolean(ApplinkConstInternalGlobal.PARAM_ENABLE_SKIP_2FA, enableSkip2FA)
            putBoolean(ApplinkConstInternalGlobal.PARAM_IS_SKIP_OTP, true)
        })
        startActivityForResult(intent, RegisterInitialFragment.REQUEST_ADD_PIN)
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                    RegisterInitialFragment.KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE)
            sharedPrefs.edit().putLong(
                    RegisterInitialFragment.KEY_FIRST_INSTALL_TIME_SEARCH, 0).apply()
        }
    }

    fun onSuccessRegister(){
        registerPushNotif()
        saveFirstInstallTime()
    }
}