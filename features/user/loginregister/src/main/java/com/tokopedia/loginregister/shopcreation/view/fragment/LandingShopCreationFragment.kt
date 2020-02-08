package com.tokopedia.loginregister.shopcreation.view.fragment

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.tokopedia.abstraction.common.utils.image.ImageHandler
import com.tokopedia.abstraction.common.utils.snackbar.NetworkErrorHelper
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.kotlin.extensions.view.toIntOrZero
import com.tokopedia.loginregister.R
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics
import com.tokopedia.loginregister.common.analytics.ShopCreationAnalytics.Companion.SCREEN_LANDING_SHOP_CREATION
import com.tokopedia.loginregister.shopcreation.common.IOnBackPressed
import com.tokopedia.loginregister.shopcreation.di.ShopCreationComponent
import com.tokopedia.loginregister.shopcreation.domain.pojo.ShopInfoByID
import com.tokopedia.loginregister.shopcreation.viewmodel.ShopCreationViewModel
import com.tokopedia.profilecommon.domain.pojo.UserProfileCompletionData
import com.tokopedia.sessioncommon.ErrorHandlerSession
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

/**
 * Created by Ade Fulki on 2019-12-09.
 * ade.hadian@tokopedia.com
 */

class LandingShopCreationFragment : BaseShopCreationFragment(), IOnBackPressed {

    lateinit var toolbarShopCreation: Toolbar
    lateinit var buttonOpenShop: UnifyButton
    lateinit var landingImage: ImageView
    lateinit var loading: LoaderUnify
    lateinit var mainView: View

    @Inject
    lateinit var userSession: UserSessionInterface
    @Inject
    lateinit var shopCreationAnalytics: ShopCreationAnalytics
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModelProvider by lazy { ViewModelProviders.of(this, viewModelFactory) }
    private val shopCreationViewModel by lazy { viewModelProvider.get(ShopCreationViewModel::class.java) }

    override fun getScreenName(): String = SCREEN_LANDING_SHOP_CREATION

    override fun initInjector() = getComponent(ShopCreationComponent::class.java).inject(this)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_landing_shop_creation, container, false)
        toolbarShopCreation = view.findViewById(R.id.toolbar_shop_creation)
        buttonOpenShop = view.findViewById(R.id.btn_continue)
        landingImage = view.findViewById(R.id.landing_shop_creation_image)
        loading = view.findViewById(R.id.loading)
        mainView = view.findViewById(R.id.main_view)
        return view
    }

    override fun getToolbar(): Toolbar = toolbarShopCreation

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initObserver()
        initView()
    }

    override fun onStart() {
        super.onStart()
        shopCreationAnalytics.trackScreen(screenName)
    }

    private fun initView() {
        ImageHandler.LoadImage(landingImage, LANDING_PICT_URL)
        if (userSession.isLoggedIn) {
            buttonOpenShop.setOnClickListener {
                showLoading()
                shopCreationAnalytics.eventClickOpenShopLanding()
                shopCreationViewModel.getUserProfile()
            }
        } else {
            buttonOpenShop.setOnClickListener {
                showLoading()
                shopCreationAnalytics.eventClickOpenShopLanding()
                goToPhoneShopCreation()
            }
        }
    }

    private fun initObserver() {
        shopCreationViewModel.getUserProfileResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetProfileInfo(it.data)
                }
                is Fail -> {
                    onFailedGetProfileInfo(it.throwable)
                }
            }
        })
        shopCreationViewModel.getUserInfoResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetUserInfo()
                }
                is Fail -> {
                    onFailedGetUserInfo(it.throwable)
                }
            }
        })
        shopCreationViewModel.getShopInfoResponse.observe(this, Observer {
            when (it) {
                is Success -> {
                    onSuccessGetShopInfo(it.data)
                }
                is Fail -> {
                    onFailedGetShopInfo(it.throwable)
                }
            }
        })
    }

    private fun onSuccessGetProfileInfo(userProfileCompletionData: UserProfileCompletionData) {
        if (userProfileCompletionData.phone.isNotEmpty()) {
            if (userProfileCompletionData.isPhoneVerified) {
                if (userProfileCompletionData.fullName.contains(CHARACTER_NOT_ALLOWED) ||
                        userProfileCompletionData.fullName.isEmpty()) {
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
                userSession.name.isEmpty()) {
            goToNameShopCreation()
        } else {
            goToShopName()
        }
    }

    private fun onFailedGetUserInfo(throwable: Throwable) {
        view?.run {
            hideLoading()
            throwable.message?.let { Toaster.make(this, it, Toaster.toasterLength, Toaster.TYPE_ERROR) }
        }
    }

    private fun onSuccessGetShopInfo(shopInfoByID: ShopInfoByID) {
        if (!userSession.hasShop()){
            goToShopName()
        }
        else if(shopInfoByID.result.isNotEmpty() && shopInfoByID.result[0].shippingLoc.provinceID < 1) {
            goToShopLogistic()
        }
        else goToShopPage(userSession.shopId)
    }

    private fun onFailedGetShopInfo(throwable: Throwable) {
        view?.run {
            throwable.message?.let { Toaster.make(this, it, Toaster.toasterLength, Toaster.TYPE_ERROR) }
        }
        goToShopPage(userSession.shopId)
    }

    private fun goToNameShopCreation() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.NAME_SHOP_CREATION)
        startActivityForResult(intent, REQUEST_CODE_NAME_SHOP_CREATION)
    }

    private fun goToPhoneShopCreation() {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.PHONE_SHOP_CREATION)
        startActivityForResult(intent, REQUEST_CODE_PHONE_SHOP_CREATION)
    }

    private fun goToPhoneShopCreation(phone: String) {
        val intent = RouteManager.getIntent(context, ApplinkConstInternalGlobal.PHONE_SHOP_CREATION)
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
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            intent.flags = Intent.FLAG_ACTIVITY_FORWARD_RESULT
            it.startActivity(intent)
            it.finish()
        }
    }

    private fun goToShopPage(shopId: String) {
        activity?.let {
            val intent = RouteManager.getIntent(activity,
                    ApplinkConst.SHOP.replace("{shop_id}", shopId))
            it.startActivity(intent)
            it.finish()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (resultCode) {
            Activity.RESULT_OK -> {
                when (requestCode) {
                    REQUEST_CODE_PHONE_SHOP_CREATION -> {
                        if(data != null) {
                            data.extras?.run {
                                if(getBoolean(ApplinkConstInternalGlobal.PARAM_IS_HAVE_STORE, false)) {
                                    goToShopPage(getString(ApplinkConstInternalGlobal.PARAM_SHOP_ID, ""))
                                } else {
                                    shopCreationViewModel.getUserInfo()
                                }
                            }
                        } else {
                            shopCreationViewModel.getUserInfo()
                        }
                    }
                    REQUEST_CODE_NAME_SHOP_CREATION -> {
                        shopCreationViewModel.getUserInfo()
                    }
                }
            }
            else -> {
                super.onActivityResult(requestCode, resultCode, data)
            }
        }
    }

    private fun showLoading() {
        loading.visibility = View.VISIBLE
        mainView.visibility = View.INVISIBLE
    }

    private fun hideLoading() {
        loading.visibility = View.GONE
        mainView.visibility = View.VISIBLE
    }

    override fun onBackPressed(): Boolean {
        shopCreationAnalytics.eventClickBackLanding()
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        shopCreationViewModel.getUserProfileResponse.removeObservers(this)
        shopCreationViewModel.getUserInfoResponse.removeObservers(this)
        shopCreationViewModel.flush()
    }

    companion object {

        private const val REQUEST_CODE_NAME_SHOP_CREATION = 100
        private const val REQUEST_CODE_PHONE_SHOP_CREATION = 101

        private const val CHARACTER_NOT_ALLOWED = "CHARACTER_NOT_ALLOWED"

        private const val LANDING_PICT_URL = "https://ecs7.tokopedia.net/android/others/Illustration_buka_toko@3x.png"

        fun createInstance(bundle: Bundle): LandingShopCreationFragment {
            val fragment = LandingShopCreationFragment()
            fragment.arguments = bundle
            return fragment
        }
    }

}