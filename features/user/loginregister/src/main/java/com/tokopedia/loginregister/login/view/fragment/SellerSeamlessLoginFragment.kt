package com.tokopedia.loginregister.login.view.fragment

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.RemoteApi
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.login.di.DaggerLoginComponent
import com.tokopedia.loginregister.login.view.constant.SeamlessSellerConstant
import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.fragment_seller_seamless_login.*
import kotlinx.android.synthetic.main.fragment_seller_seamless_login.view.*
import kotlinx.android.synthetic.main.item_account_with_shop.*
import java.util.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SellerSeamlessLoginFragment : BaseDaggerFragment() {

    private var service: RemoteApi? = null
    private var serviceConnection: RemoteServiceConnection? = null

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val seamlessViewModel by lazy { viewModelProvider.get(SellerSeamlessViewModel::class.java) }

    private var getUserTaskId = ""
    private var getKeyTaskId = ""

    override fun getScreenName(): String = ""
    
    override fun initInjector() {
        val daggerLoginComponent = DaggerLoginComponent
                .builder().loginRegisterComponent(getComponent(LoginRegisterComponent::class.java))
                .build() as DaggerLoginComponent

        daggerLoginComponent.inject(this)
    }

    companion object {
        fun createInstance(bundle: Bundle): Fragment {
            val fragment = SellerSeamlessLoginFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

    private val broadcastReceiver = object: BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if ((intent?.action == getUserTaskId && getUserTaskId.isNotEmpty())
                    || (intent?.action == getKeyTaskId && getKeyTaskId.isNotEmpty())){
                handleIntentReceive(intent?.action, intent.extras)
                activity?.unregisterReceiver(this)
            }
        }
    }

    private fun handleIntentReceive(taskId: String, bundle: Bundle?) {
        bundle?.run {
            hideProgressBar()
            if(!bundle.containsKey(SeamlessSellerConstant.KEY_ERROR)) {
                if (taskId == getUserTaskId) {
                    ImageHandler.loadImageCircle2(activity, seamless_fragment_avatar, getString(SeamlessSellerConstant.KEY_SHOP_AVATAR))
                    seamless_fragment_shop_name.text = getString(SeamlessSellerConstant.KEY_SHOP_NAME)
                    seamless_fragment_name.text = getString(SeamlessSellerConstant.KEY_NAME)
                    seamless_fragment_email.text = maskEmail(getString(SeamlessSellerConstant.KEY_EMAIL))
                    hideProgressBar()
                } else if (taskId == getKeyTaskId) {
                    seamlessViewModel.loginSeamless(getString(SeamlessSellerConstant.KEY_TOKEN))
                } else onErrorLoginToken(null)
            }  else activity?.finish()
        }
    }

    private fun maskEmail(email: String):
            String = email.replace("(?<=.)[^@](?=[^@]*?@)|(?:(?<=@.)|(?!^)\\\\G(?=[^@]*\$)).(?=.*\\\\.)".toRegex(), "*")

    private fun showProgressBar(){
        seller_seamless_main_view?.alpha = 0.4F
        seller_seamless_loader?.show()
        view?.seller_seamless_positive_btn?.isEnabled = false
        view?.seller_seamless_negative_btn?.isEnabled = false
    }

    private fun hideProgressBar(){
        view?.seller_seamless_positive_btn?.isEnabled = true
        view?.seller_seamless_negative_btn?.isEnabled = true
        seller_seamless_main_view?.alpha = 1.0F
        seller_seamless_loader?.hide()
        seller_seamless_main_view?.show()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_seller_seamless_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        showProgressBar()
        if(GlobalConfig.isSellerApp()){
            connectService()
        }

        view?.seller_seamless_positive_btn.setOnClickListener {
            if(service != null){
                showProgressBar()
                getKeyTaskId = UUID.randomUUID().toString()
                getKey(getKeyTaskId)
            }
        }

        view?.seller_seamless_negative_btn.setOnClickListener {
            activity?.finish()
        }

        initObserver()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == LoginEmailPhoneFragment.REQUEST_SECURITY_QUESTION
                && resultCode == Activity.RESULT_OK
                && data != null
                && data.extras != null) {
            onSuccessLoginToken()
        }else super.onActivityResult(requestCode, resultCode, data)
    }

    private fun goToSecurityQuestion(){
        activity?.let {
            it.setResult(Activity.RESULT_OK, Intent().putExtra(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, true))
            it.finish()
        }
    }

    private fun initObserver(){
        seamlessViewModel.goToSecurityQuestion.observe(this, Observer {
            if(it) goToSecurityQuestion()
        })

        seamlessViewModel.loginTokenResponse.observe(this, Observer {
            when(it){
                is Success -> onSuccessLoginToken()
                is Fail -> onErrorLoginToken(it.throwable)
            }
        })
    }

    private fun onSuccessLoginToken(){
        hideProgressBar()
        Toaster.showNormal(view!!, "Success Login Token", Snackbar.LENGTH_LONG)
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun onErrorLoginToken(throwable: Throwable?){
        hideProgressBar()
        view?.run{
            val errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun connectService() {
        if(GlobalConfig.isSellerApp()) {
            serviceConnection = RemoteServiceConnection()
            val i = Intent().apply {
                component = ComponentName(SeamlessSellerConstant.MAINAPP_PACKAGE, SeamlessSellerConstant.SERVICE_PACKAGE)
            }
            val success = activity?.bindService(i, serviceConnection, Context.BIND_AUTO_CREATE)
            if(success == false) activity?.finish()
        }
    }

    private fun getUserProfile(taskId: String){
        if(service != null) {
            activity?.registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(taskId) })
            service?.getUserProfile(taskId)
        }
    }

    private fun getKey(taskId: String){
        if(service != null) {
            activity?.registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(taskId) })
            service?.getDummyKey(taskId)
        }
    }

    override fun onDestroy() {
        serviceConnection?.run {
            activity?.unbindService(this)
        }
        super.onDestroy()
    }

    internal inner class RemoteServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, boundService: IBinder) {
            service = RemoteApi.Stub.asInterface(boundService)
            getUserTaskId = UUID.randomUUID().toString()
            getUserProfile(getUserTaskId)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            service = null
            activity?.finish()
        }
    }

}
