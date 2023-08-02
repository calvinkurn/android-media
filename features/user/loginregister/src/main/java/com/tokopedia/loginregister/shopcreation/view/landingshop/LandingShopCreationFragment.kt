package com.tokopedia.loginregister.shopcreation.view.landingshop

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.applink.shopadmin.ShopAdminDeepLinkMapper
import com.tokopedia.config.GlobalConfig
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics.Companion.SCREEN_LANDING_SHOP_CREATION
import com.tokopedia.loginregister.databinding.FragmentLandingShopCreationBinding
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.data.ShopInfoByID
import com.tokopedia.loginregister.shopcreation.data.UserProfileCompletionData
import com.tokopedia.loginregister.shopcreation.view.base.BaseShopCreationFragment
import com.tokopedia.loginregister.shopcreation.view.ShopCreationViewModel
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 */

class LandingShopCreationFragment : BaseShopCreationFragment(), IOnBackPressed {

    private lateinit var sharedPrefs: SharedPreferences

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModelProvider by lazy {
        ViewModelProvider(this, viewModelFactory)
    }
    private val shopCreationViewModel by lazy {
        viewModelProvider.get(ShopCreationViewModel::class.java)
    }

    private var viewBinding by autoClearedNullable<FragmentLandingShopCreationBinding>()

    override fun getScreenName(): String = SCREEN_LANDING_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        viewBinding = FragmentLandingShopCreationBinding.inflate(inflater, container, false)
        return viewBinding?.root
    }

    override fun getToolbar(): Toolbar? = viewBinding?.toolbarShopCreation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
    }

    override fun onStart() {
        super.onStart()
        shopCreationAnalytics.trackScreen(screenName)
    }

    override fun onResume() {
        super.onResume()
        initButtonListener()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_PHONE_SHOP_CREATION -> {
                        shopCreationViewModel.getUserInfo()
                    }
                    REQUEST_CODE_NAME_SHOP_CREATION -> {
                        shopCreationViewModel.getUserInfo()
                    }
                    ShopAdminDeepLinkMapper.REQUEST_CODE_ADMIN_REDIRECTION -> {
                        setActionAfterAdminRedirection(data)
                    }
                }
            }
            else -> {
                hideLoading()
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    override fun onBackPressed(): Boolean {
        shopCreationAnalytics.eventClickBackLanding()
        if (GlobalConfig.isSellerApp()) {
            activity?.let {
                if (userSession.isLoggedIn) {
                    RouteManager.route(it, ApplinkConstInternalUserPlatform.LOGOUT)
                    it.finish()
                } else if (it.intent.hasExtra(ApplinkConstInternalGlobal.PARAM_SOURCE)) {
                    RouteManager.route(it, ApplinkConst.LOGIN)
                    it.finish()
                } else {
                    RouteManager.route(it, ApplinkConstInternalSellerapp.WELCOME)
                    it.finish()
                }
            }
        }
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        shopCreationViewModel.getUserProfileResponse.removeObservers(this)
        shopCreationViewModel.getUserInfoResponse.removeObservers(this)
        shopCreationViewModel.getShopInfoResponse.removeObservers(this)
    }

    private fun initView() {
        viewBinding?.landingShopCreationImage?.loadImage(LANDING_PICT_URL)

        viewBinding?.deletedShopInfo2?.setOnClickListener {
            RouteManager.route(context, URL_DELETED_SHOP)
        }
    }

    private fun initButtonListener() {
        viewBinding?.btnContinue?.setOnClickListener {
            shopCreationAnalytics.eventClickOpenShopLanding()
            if (userIsLoggedIn()) {
                goToShopAdminRedirection()
            } else {
                showLoading()
                goToPhoneShopCreation()
            }
        }
    }

    private fun userIsLoggedIn(): Boolean {
        return (userSession.userId != DEFAULT_SHOP_ID_NOT_OPEN && userSession.userId.isNotBlank())
    }

    private fun setActionAfterAdminRedirection(intent: Intent?) {
        val appLink = intent?.getStringExtra(ShopAdminDeepLinkMapper.ARGS_APPLINK_FROM_SHOP_ADMIN).orEmpty()
        if (appLink.isNotEmpty()) {
            setActionAfterSuccessAdminRedirection(appLink)
        } else {
            showToasterAfterFailAdminRedirection(intent)
        }
    }

    private fun setActionAfterSuccessAdminRedirection(appLink: String) {
        if (appLink == ApplinkConstInternalUserPlatform.PHONE_SHOP_CREATION) {
            showLoading()
            if (userSession.isLoggedIn) {
                shopCreationViewModel.getUserProfile()
            } else {
                goToPhoneShopCreation()
            }
        } else {
            RouteManager.route(context, appLink)
        }
    }

    private fun showToasterAfterFailAdminRedirection(intent: Intent?) {
        val errorMessage =
            intent?.getStringExtra(ShopAdminDeepLinkMapper.ARGS_ERROR_MESSAGE_FROM_SHOP_ADMIN)
        if (errorMessage?.isNotEmpty() == true) {
            view?.let {
                Toaster.build(
                    it,
                    text = errorMessage,
                    duration = Toaster.LENGTH_SHORT,
                    type = Toaster.TYPE_ERROR
                ).show()
            }
        }
    }

    private fun initObserver() {
        shopCreationViewModel.getUserProfileResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetProfileInfo(it.data)
                }
                is Fail -> {
                    onFailedGetProfileInfo(it.throwable)
                }
            }
        }

        shopCreationViewModel.getUserInfoResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetUserInfo()
                }
                is Fail -> {
                    onFailedGetUserInfo(it.throwable)
                }
            }
        }

        shopCreationViewModel.getShopInfoResponse.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    onSuccessGetShopInfo(it.data)
                }
                is Fail -> {
                    onFailedGetShopInfo(it.throwable)
                }
            }
        }
    }

    private fun onSuccessGetProfileInfo(userProfileCompletionData: UserProfileCompletionData) {
        if (userProfileCompletionData.phone.isNotEmpty()) {
            if (userProfileCompletionData.isPhoneVerified) {
                if (userProfileCompletionData.fullName.contains(CHARACTER_NOT_ALLOWED) ||
                    userProfileCompletionData.fullName.isEmpty()
                ) {
                    goToNameShopCreation()
                } else {
                    if (userSession.hasShop())
                        shopCreationViewModel.getShopInfo(userSession.shopId.toIntOrZero())
                    else goToShopName()
                }
            } else {
                goToPhoneShopCreation(userProfileCompletionData.phone)
            }
        } else {
            goToPhoneShopCreation()
        }
    }

    private fun onFailedGetProfileInfo(throwable: Throwable) {
        view?.run {
            hideLoading()
            val error = ErrorHandlerSession.getErrorMessage(throwable, context, true)
            NetworkErrorHelper.showEmptyState(context, this, error) {
                shopCreationViewModel.getUserProfile()
            }
        }
    }

    private fun onSuccessGetUserInfo() {
        if (userSession.name.contains(CHARACTER_NOT_ALLOWED) ||
            userSession.name.isEmpty()
        ) {
            goToNameShopCreation()
        } else {
            saveFirstInstallTime()
            if (userSession.hasShop())
                shopCreationViewModel.getShopInfo(userSession.shopId.toIntOrZero())
            else goToShopName()
        }
    }

    private fun onFailedGetUserInfo(throwable: Throwable) {
        view?.run {
            hideLoading()
            throwable.message?.let {
                Toaster.build(
                    this,
                    it,
                    Toaster.toasterLength,
                    Toaster.TYPE_ERROR
                )
            }
        }
    }

    private fun onSuccessGetShopInfo(shopInfoByID: ShopInfoByID) {
        if (!userSession.hasShop()) {
            goToShopName()
        } else if (shopInfoByID.result.isNotEmpty() && shopInfoByID.result[0].shippingLoc.provinceID < 1) {
            goToShopLogistic()
        } else goToShopPage(userSession.shopId)
    }

    private fun onFailedGetShopInfo(throwable: Throwable) {
        view?.run {
            throwable.message?.let {
                Toaster.build(
                    this,
                    it,
                    Toaster.toasterLength,
                    Toaster.TYPE_ERROR
                )
            }
        }
        goToShopPage(userSession.shopId)
    }

    private fun goToNameShopCreation() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.NAME_SHOP_CREATION)
        startActivityForResult(intent, REQUEST_CODE_NAME_SHOP_CREATION)
    }

    private fun goToShopAdminRedirection() {
        val intent =
            RouteManager.getIntent(context, ApplinkConstInternalMarketplace.ADMIN_REDIRECTION)
        startActivityForResult(intent, ShopAdminDeepLinkMapper.REQUEST_CODE_ADMIN_REDIRECTION)
    }

    private fun goToPhoneShopCreation() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.PHONE_SHOP_CREATION)
        startActivityForResult(intent, REQUEST_CODE_PHONE_SHOP_CREATION)
    }

    private fun goToPhoneShopCreation(phone: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalUserPlatform.PHONE_SHOP_CREATION)
        intent.putExtra(ApplinkConstInternalGlobal.PARAM_PHONE, phone)
        startActivityForResult(intent, REQUEST_CODE_PHONE_SHOP_CREATION)
    }

    private fun goToShopName() {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.OPEN_SHOP)
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            it.startActivity(intent)
            it.finish()
        }
    }

    private fun goToShopLogistic() {
        activity?.let {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalMarketplace.OPEN_SHOP)
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            intent.putExtra(ApplinkConstInternalMarketplace.PARAM_IS_NEED_LOC, true)
            it.startActivity(intent)
            it.finish()
        }
    }

    private fun goToShopPage(shopId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(
                activity,
                ApplinkConst.SHOP.replace("{shop_id}", shopId)
            )
            it.startActivity(intent)
            it.finish()
        }
    }

    private fun showLoading() {
        viewBinding?.loading?.show()
        viewBinding?.mainView?.hide()
        context?.let {
            viewBinding?.scrollView?.setBackgroundColor(
                MethodChecker.getColor(
                    context,
                    com.tokopedia.unifyprinciples.R.color.Unify_Background
                )
            )
        }
    }

    private fun hideLoading() {
        viewBinding?.loading?.hide()
        viewBinding?.mainView?.show()
        context?.let {
            viewBinding?.scrollView?.background = ContextCompat.getDrawable(it, R.drawable.bg_landing_shop_creation)
        }
    }

    private fun saveFirstInstallTime() {
        context?.let {
            sharedPrefs = it.getSharedPreferences(
                KEY_FIRST_INSTALL_SEARCH, Context.MODE_PRIVATE
            )
            sharedPrefs.edit().putLong(
                KEY_FIRST_INSTALL_TIME_SEARCH, 0
            ).apply()
        }
    }

    companion object {

        private const val REQUEST_CODE_NAME_SHOP_CREATION = 100
        private const val REQUEST_CODE_PHONE_SHOP_CREATION = 101

        private const val DEFAULT_SHOP_ID_NOT_OPEN = "0"

        private const val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

        private const val LANDING_PICT_URL =
            "https://images.tokopedia.net/android/others/Illustration_buka_toko@3x.png"
        private const val URL_DELETED_SHOP =
            "https://www.tokopedia.com/help/article/kebijakan-penonaktifan-toko-secara-permanen"

        private const val KEY_FIRST_INSTALL_SEARCH = "KEY_FIRST_INSTALL_SEARCH"
        private const val KEY_FIRST_INSTALL_TIME_SEARCH = "KEY_IS_FIRST_INSTALL_TIME_SEARCH"

        fun createInstance(bundle: Bundle): LandingShopCreationFragment {
            val fragment = LandingShopCreationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }
}
