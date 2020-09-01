package com.tokopedia.shop_showcase.shop_showcase_management.presentation.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalMechant
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.PageNameConstant
import com.tokopedia.shop_showcase.common.ShopShowcaseFragmentNavigation
import com.tokopedia.shop_showcase.common.ShopType
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.ShopShowcaseListFragment
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.ShopShowcaseListReorderFragment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class ShopShowcaseListActivity : BaseActivity(), ShopShowcaseFragmentNavigation {

    companion object {
        const val IS_NEED_TOGO_TO_ADD_PAGE = "isNeedToGoToAddShowcase"
        const val REQUEST_CODE_ADD_ETALASE = 289
    }

    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }

    private var shopId: String = "0"
    private var selectedEtalaseId: String = "0"
    private var isShowDefault: Boolean = true
    private var isShowZeroProduct: Boolean = true
    private var shopType = ShopType.REGULAR
    private var isNeedToGoToAddShowcase: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_shop_showcase_list_container)

        val bundle = intent.getBundleExtra("bundle")
        if (bundle != null) {
            shopId = bundle.getString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, "0").toString()
            selectedEtalaseId = bundle.getString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "0").toString()
            isShowDefault = bundle.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            isShowZeroProduct = bundle.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, true)
            isNeedToGoToAddShowcase = bundle.getBoolean(IS_NEED_TOGO_TO_ADD_PAGE, false)
        }

        // If there is no shopId  then it's seller view
        if (shopId == "0"){
            shopId = userSession.shopId
        }

        getShopType()
        setupInitialFragment()
        setupStatusbar()
    }

    override fun navigateToPage(page: String, tag: String?, showcaseList: ArrayList<ShowcaseItem>?) {
        if (page == PageNameConstant.SHOWCASE_LIST_PAGE) {
            val fragmentShowcaseList = ShopShowcaseListFragment.createInstance(
                    shopType, shopId, selectedEtalaseId, isShowDefault,
                    isShowZeroProduct, isMyShop(), isNeedToGoToAddShowcase)
            navigateToOtherFragment(fragmentShowcaseList, null)
        } else if (page == PageNameConstant.SHOWCASE_LIST_REORDER_PAGE) {
            isNeedToGoToAddShowcase = false
            val fragmentShowcaseReorder = ShopShowcaseListReorderFragment.createInstance(
                    shopType, showcaseList, isMyShop())
            navigateToOtherFragment(fragmentShowcaseReorder, null)
        }
    }

    private fun setupStatusbar(){
        val window: Window = getWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().statusBarColor = ContextCompat.getColor(this, android.R.color.white)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    private fun setupInitialFragment() {
        val fragmentShowcaseList = ShopShowcaseListFragment.createInstance(
                shopType, shopId, selectedEtalaseId, isShowDefault,
                isShowZeroProduct, isMyShop(), isNeedToGoToAddShowcase)
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.shop_showcase_container, fragmentShowcaseList).commit()
    }

    private fun navigateToOtherFragment(fragment: Fragment, tag: String?) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction
                .replace(R.id.shop_showcase_container, fragment)
                .addToBackStack(tag)
                .commit()
    }

    private fun isMyShop(): Boolean = shopId == userSession.shopId

    private fun getShopType() {
        val isOfficialStore = userSession.isShopOfficialStore
        val isGoldMerchant = userSession.isGoldMerchant

        if (isOfficialStore) {
            shopType = ShopType.OFFICIAL_STORE
        } else if (isGoldMerchant) {
            shopType = ShopType.GOLD_MERCHANT
        } else {
            shopType = ShopType.REGULAR
        }
    }

}