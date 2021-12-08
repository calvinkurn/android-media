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
import androidx.lifecycle.ViewModelProviders
import com.google.android.material.snackbar.Snackbar
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.common.utils.image.ImageHandler
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
import com.tokopedia.loginregister.common.di.LoginRegisterComponent
import com.tokopedia.loginregister.common.utils.RegisterUtil.removeErrorCode
import com.tokopedia.loginregister.common.utils.SellerAppWidgetHelper
import com.tokopedia.loginregister.databinding.FragmentSellerSeamlessLoginBinding
import com.tokopedia.loginregister.login.di.DaggerLoginComponent
import com.tokopedia.loginregister.login.router.LoginRouter
import com.tokopedia.loginregister.login.view.constant.SeamlessSellerConstant
import com.tokopedia.loginregister.login.view.viewmodel.SellerSeamlessViewModel
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.utils.view.binding.viewBinding
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
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val seamlessViewModel by lazy { viewModelProvider.get(SellerSeamlessViewModel::class.java) }

    @Inject
    lateinit var analytics: SeamlessLoginAnalytics

    private var getUserTaskId = ""
    private var getKeyTaskId = ""

    private val binding: FragmentSellerSeamlessLoginBinding? by viewBinding()

    override fun getScreenName(): String = SeamlessLoginAnalytics.SCREEN_SEAMLESS_LOGIN

    override fun onStart() {
        super.onStart()
        activity?.let {
            analytics.trackScreen(screenName)
        }
    }

    override fun initInjector() {
        val daggerLoginComponent = DaggerLoginComponent
                .builder().loginRegisterComponent(getComponent(LoginRegisterComponent::class.java))
                .build() as DaggerLoginComponent

        daggerLoginComponent.inject(this)
    }

    companion object {
        private const val KEY_AUTO_LOGIN = "is_auto_login"

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

    private fun handleIntentReceive(taskId: String, bundle: Bundle?) {
        if (bundle != null) {
            if (!bundle.containsKey(SeamlessSellerConstant.KEY_ERROR)) {
                if (taskId == getUserTaskId
                        && bundle.getString(SeamlessSellerConstant.KEY_SHOP_NAME)?.isNotEmpty() == true
                        && bundle.getString(SeamlessSellerConstant.KEY_EMAIL)?.isNotEmpty() == true) {
                    binding?.include?.seamlessFragmentShopName?.run {
                        val drawableLeft = AppCompatResources.getDrawable(this.context, R.drawable.ic_shop_dark_grey)
                        text = bundle.getString(SeamlessSellerConstant.KEY_SHOP_NAME)
                        setCompoundDrawablesWithIntrinsicBounds(drawableLeft, null, null, null)
                    }
                    context?.run {
                        if(binding?.include?.seamlessFragmentAvatar != null) {
                            ImageHandler.loadImageCircle2(context, binding?.include?.seamlessFragmentAvatar, bundle.getString(SeamlessSellerConstant.KEY_SHOP_AVATAR))
                        }
                    }
                    binding?.include?.seamlessFragmentName?.text = bundle.getString(SeamlessSellerConstant.KEY_NAME)
                    binding?.include?.seamlessFragmentEmail?.text = maskEmail(bundle.getString(SeamlessSellerConstant.KEY_EMAIL, ""))
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

    private fun maskEmail(email: String): String =
            email.replace("(?<=.)[^@](?=[^@]*?@)|(?:(?<=@.)|(?!^)\\G(?=[^@]*$)).(?=.*\\.)".toRegex(), "*")

    private fun showFullProgressBar(){

        binding?.sellerSeamlessLoader?.show()
        binding?.sellerSeamlessLoader?.hide()
    }

    private fun showProgressBar(){
        binding?.sellerSeamlessMainView?.alpha = 0.4F
        binding?.sellerSeamlessLoader?.show()
        binding?.sellerSeamlessPositiveBtn?.isEnabled = false
        binding?.sellerSeamlessNegativeBtn?.isEnabled = false
    }

    private fun hideProgressBar(){
        binding?.sellerSeamlessPositiveBtn?.isEnabled = true
        binding?.sellerSeamlessNegativeBtn?.isEnabled = true
        binding?.sellerSeamlessMainView?.alpha = 1.0F
        binding?.sellerSeamlessLoader?.hide()
        binding?.sellerSeamlessMainView?.show()
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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_seller_seamless_login, container, false)

        context?.run {
            binding?.include?.seamlessFragmentShopName?.typeface = com.tokopedia.unifyprinciples.getTypeface(this, "NunitoSansExtraBold.ttf")
            binding?.sellerSeamlessTitle?.typeface = com.tokopedia.unifyprinciples.getTypeface(this, "NunitoSansExtraBold.ttf")
        }
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if(GlobalConfig.isSellerApp()){
            connectService()
        }

        showFullProgressBar()

        binding?.sellerSeamlessPositiveBtn?.setOnClickListener { onPositiveBtnClick() }

        binding?.sellerSeamlessNegativeBtn?.setOnClickListener { onNegativeBtnClick() }

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
            service = RemoteApi.Stub.asInterface(boundService)
            getUserTaskId = UUID.randomUUID().toString()
            getUserProfile(getUserTaskId)
        }

        override fun onServiceDisconnected(name: ComponentName) {
            ServerLogger.log(Priority.P2, "SEAMLESS_SELLER",
                    mapOf("type" to "ErrorBindingService", "reason" to "Service Disconnected", "detail" to name.toString()))
            service = null
            activity?.finish()
        }
    }

}
