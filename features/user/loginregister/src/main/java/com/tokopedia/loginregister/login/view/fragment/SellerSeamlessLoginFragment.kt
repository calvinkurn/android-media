package com.tokopedia.loginregister.login.view.fragment

import android.app.Activity
import android.content.*
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.logger.ServerLogger
import com.tokopedia.logger.utils.Priority
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.RemoteApi
import com.tokopedia.loginregister.common.analytics.SeamlessLoginAnalytics
import com.tokopedia.loginregister.common.utils.RegisterUtil.removeErrorCode
import com.tokopedia.loginregister.common.utils.SellerAppWidgetHelper
import com.tokopedia.loginregister.databinding.FragmentSellerSeamlessLoginBinding
import com.tokopedia.loginregister.login.di.LoginComponent
import com.tokopedia.loginregister.login.router.LoginRouter
import com.tokopedia.loginregister.login.view.constant.SeamlessSellerConstant
import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import com.tokopedia.media.loader.loadImageCircle
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.util.*
import javax.inject.Inject

/**
 * Created by Yoris Prayogo on 22/04/20.
 * Copyright (c) 2020 PT. Tokopedia All rights reserved.
 */

class SellerSeamlessLoginFragment : BaseDaggerFragment() {

    private var service: RemoteApi? = null
    private var serviceConnection: RemoteServiceConnection? = null

    private var autoLogin = false

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val seamlessViewModel by lazy { viewModelProvider.get(SellerSeamlessViewModel::class.java) }

    @Inject
    lateinit var analytics: SeamlessLoginAnalytics

    private var getUserTaskId = ""
    private var getKeyTaskId = ""
    
    private var viewBinding by autoClearedNullable<FragmentSellerSeamlessLoginBinding>()

    override fun getScreenName(): String = SeamlessLoginAnalytics.SCREEN_SEAMLESS_LOGIN

    override fun onStart() {
        super.onStart()
        activity?.let {
            analytics.trackScreen(screenName)
        }
    }

    override fun initInjector() {
        getComponent(LoginComponent::class.java).inject(this)
    }

    companion object {
        private const val KEY_AUTO_LOGIN = "is_auto_login"
        private const val FONT_NAME = "NunitoSansExtraBold.ttf"

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
                activity?.unregisterReceiver(this)
                handleIntentReceive(intent.action!!, intent.extras)
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        viewBinding = FragmentSellerSeamlessLoginBinding.inflate(inflater, container, false)
        context?.run {
            viewBinding?.include?.seamlessFragmentShopName?.typeface = com.tokopedia.unifyprinciples.getTypeface(this, FONT_NAME)
            viewBinding?.sellerSeamlessTitle?.typeface = com.tokopedia.unifyprinciples.getTypeface(this, FONT_NAME)
        }
        return viewBinding?.root
    }

    private fun handleIntentReceive(taskId: String, bundle: Bundle?) {
        if (bundle != null) {
            if (!bundle.containsKey(SeamlessSellerConstant.KEY_ERROR)) {
                if (taskId == getUserTaskId
                        && bundle.getString(SeamlessSellerConstant.KEY_SHOP_NAME)?.isNotEmpty() == true
                        && bundle.getString(SeamlessSellerConstant.KEY_EMAIL)?.isNotEmpty() == true) {
                    viewBinding?.include?.seamlessFragmentShopName?.run {
                        val drawableLeft = AppCompatResources.getDrawable(this.context, R.drawable.ic_shop_dark_grey)
                        text = bundle.getString(SeamlessSellerConstant.KEY_SHOP_NAME)
                        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                    }
                    context?.run {
                        if(viewBinding?.include?.seamlessFragmentAvatar != null) {
                            viewBinding?.include?.seamlessFragmentAvatar?.loadImageCircle(
                                bundle.getString(SeamlessSellerConstant.KEY_SHOP_AVATAR)
                            )
                        }
                    }

                    viewBinding?.include?.seamlessFragmentName?.text = bundle.getString(SeamlessSellerConstant.KEY_NAME)
                    viewBinding?.include?.seamlessFragmentEmail?.text = maskEmail(bundle.getString(SeamlessSellerConstant.KEY_EMAIL, ""))
                    hideProgressBar()
                    if (autoLogin) {
                        onPositiveBtnClick()
                    }
                } else if (taskId == getKeyTaskId) {
                    seamlessViewModel.loginSeamless(bundle.getString(SeamlessSellerConstant.KEY_TOKEN, ""))
                } else moveToNormalLogin()
            } else moveToNormalLogin()
        } else moveToNormalLogin()
    }

    private fun moveToNormalLogin(){
        RouteManager.route(context, ApplinkConst.LOGIN)
        activity?.finish()
    }

    private fun maskEmail(email: String): String = email.replace(
        "(?<=.)[^@](?=[^@]*?@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?=.*\\.)".toRegex(), 
        "*"
    )

    private fun showFullProgressBar(){
        viewBinding?.sellerSeamlessLoader?.show()
        viewBinding?.sellerSeamlessMainView?.hide()
    }

    private fun showProgressBar(){
        viewBinding?.sellerSeamlessMainView?.alpha = 0.4F
        viewBinding?.sellerSeamlessLoader?.show()
        viewBinding?.sellerSeamlessNegativeBtn?.isEnabled = false
        viewBinding?.sellerSeamlessPositiveBtn?.isEnabled = false
    }

    private fun hideProgressBar(){
        viewBinding?.sellerSeamlessNegativeBtn?.isEnabled = true
        viewBinding?.sellerSeamlessPositiveBtn?.isEnabled = true
        viewBinding?.sellerSeamlessMainView?.alpha = 1.0F
        viewBinding?.sellerSeamlessLoader?.hide()
        viewBinding?.sellerSeamlessMainView?.show()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.run {
            autoLogin = getBoolean(KEY_AUTO_LOGIN, false)
        }
        (context?.applicationContext as? LoginRouter)?.let {
            it.setOnboardingStatus(true)
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(GlobalConfig.isSellerApp()){
            connectService()
        }

        showFullProgressBar()

        viewBinding?.sellerSeamlessNegativeBtn?.setOnClickListener { onNegativeBtnClick() }
        viewBinding?.sellerSeamlessPositiveBtn?.setOnClickListener { onPositiveBtnClick() }

        initObserver()
    }

    private fun onNegativeBtnClick(){
        analytics.eventClickLoginWithOtherAcc()
        context?.run {
            val i = RouteManager.getIntent(this, ApplinkConst.LOGIN)
            startActivity(i)
        }
    }

    private fun onPositiveBtnClick(){
        if(service != null){
            analytics.eventClickLoginSeamless(SeamlessLoginAnalytics.LABEL_CLICK)
            showProgressBar()
            getKeyTaskId = UUID.randomUUID().toString()
            getKey(getKeyTaskId)
        }
    }

    private fun goToSecurityQuestion(){
        activity?.let {
            val intent = Intent().putExtra(ApplinkConstInternalGlobal.PARAM_IS_SQ_CHECK, true)
            it.setResult(Activity.RESULT_OK, intent)
            it.finish()
        }
    }

    private fun finishIntent(){
        activity?.setResult(Activity.RESULT_OK)
        activity?.finish()
    }

    private fun initObserver(){
        seamlessViewModel.goToSecurityQuestion.observe(viewLifecycleOwner, Observer {
            if(it) goToSecurityQuestion()
        })

        seamlessViewModel.loginTokenResponse.observe(viewLifecycleOwner, Observer {
            when(it){
                is Success -> onSuccessLoginToken()
                is Fail -> onErrorLoginToken(it.throwable)
            }
        })
    }


    private fun onSuccessLoginToken(){
        analytics.eventClickLoginSeamless(SeamlessLoginAnalytics.LABEL_SUCCESS)
        hideProgressBar()
        SellerAppWidgetHelper.fetchSellerAppWidgetData(context)

        finishIntent()
    }

    private fun onErrorLoginToken(throwable: Throwable?){
        val errorMessage = ErrorHandler.getErrorMessage(activity, throwable)
        analytics.eventClickLoginSeamless("${SeamlessLoginAnalytics.LABEL_FAILED} ${errorMessage.removeErrorCode()}")
        hideProgressBar()
        view?.run{
            Toaster.showError(this, errorMessage, Snackbar.LENGTH_LONG)
        }
    }

    private fun connectService() {
        if(GlobalConfig.isSellerApp() && serviceConnection == null) {
            try {
                serviceConnection = RemoteServiceConnection()
                val i = Intent().apply {
                    component = ComponentName(SeamlessSellerConstant.MAINAPP_PACKAGE, SeamlessSellerConstant.SERVICE_PACKAGE)
                }
                val success = activity?.bindService(i, serviceConnection!!, Context.BIND_AUTO_CREATE)
                if (success == false) {
                    ServerLogger.log(Priority.P2, "SEAMLESS_SELLER",
                            mapOf("type" to "ErrorBindingService", "reason" to "Connect Service Failed", "detail" to "Bind Service: $success"))
                    moveToNormalLogin()
                }
            } catch(ex: Exception) {
                ServerLogger.log(Priority.P2, "SEAMLESS_SELLER",
                        mapOf("type" to "ErrorBindingService", "reason" to "Exception Thrown While Binding", "detail" to "Exception: ${formatThrowable(ex)}."))
                moveToNormalLogin()
            }
        }
    }

    fun formatThrowable(throwable: Throwable): String {
        return try{
            Log.getStackTraceString(throwable).take(1000)
        } catch (e: Exception){
            e.toString()
        }
    }

    private fun getUserProfile(taskId: String){
        if(service != null && activity?.isDestroyed == false) {
            activity?.registerReceiver(broadcastReceiver, IntentFilter().apply { addAction(taskId) })
            service?.getUserProfile(taskId)
        }
    }

    private fun getKey(taskId: String){
        if(service != null && activity?.isDestroyed == false) {
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

    fun onBackPressedFragment() {
        analytics.eventClickBack()
        if(seamlessViewModel.hasLogin())
            moveToNormalLogin()
        else {
            RouteManager.route(activity, ApplinkConstInternalSellerapp.WELCOME)
            activity?.finish()
        }
    }

    internal inner class RemoteServiceConnection : ServiceConnection {
        override fun onServiceConnected(name: ComponentName, boundService: IBinder) {
            try {
                service = RemoteApi.Stub.asInterface(boundService)
                getUserTaskId = UUID.randomUUID().toString()
                getUserProfile(getUserTaskId)
            } catch (ignored: Exception) {}
        }

        override fun onServiceDisconnected(name: ComponentName) {
            ServerLogger.log(Priority.P2, "SEAMLESS_SELLER",
                    mapOf("type" to "ErrorBindingService", "reason" to "Service Disconnected", "detail" to name.toString()))
            service = null
            activity?.finish()
        }
    }

}
