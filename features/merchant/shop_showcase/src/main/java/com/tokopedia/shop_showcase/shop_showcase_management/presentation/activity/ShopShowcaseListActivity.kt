package com.tokopedia.shop_showcase.shop_showcase_management.presentation.activity

import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.Window
import android.view.WindowManager
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseActivity
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.PageNameConstant
import com.tokopedia.shop_showcase.common.ShopShowcaseFragmentNavigation
import com.tokopedia.shop_showcase.common.ShopShowcaseListParam
import com.tokopedia.shop_showcase.common.ShopType
import com.tokopedia.shop_showcase.shop_showcase_management.data.model.ShowcaseList.ShowcaseItem
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.ShopShowcaseListFragment
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.ShopShowcaseListReorderFragment
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class ShopShowcaseListActivity : BaseActivity(), ShopShowcaseFragmentNavigation {

    companion object {
        @JvmStatic
        fun createIntentListShopShowcase(
                context: Context,
                shopId: String,
                selectedEtalaseId: String? = "",
                isShowDefault : Boolean? = false,
                isShowZeroProduct : Boolean? = false
        ): Intent {
            val intent = Intent(context, ShopShowcaseListActivity::class.java)
            intent.putExtra(ShopShowcaseListParam.EXTRA_SHOP_ID, shopId)
            intent.putExtra(ShopShowcaseListParam.EXTRA_ETALASE_ID, selectedEtalaseId)
            intent.putExtra(ShopShowcaseListParam.EXTRA_IS_SHOW_DEFAULT, isShowDefault ?: false)
            intent.putExtra(ShopShowcaseListParam.EXTRA_IS_SHOW_ZERO_PRODUCT, isShowZeroProduct ?: false)
            return intent
        }

        @JvmStatic
        fun createIntentAddShopShowcase(
                context: Context,
                isNeedToGoToAddShowcase: Boolean
        ): Intent {
            val intent = Intent(context, ShopShowcaseListActivity::class.java)
            intent.putExtra(ShopShowcaseListParam.EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE, isNeedToGoToAddShowcase)
//            intent.putExtra(ShopShowcaseListParam.EXTRA_TOTAL_PRODUCT, totalProduct)
            return intent
        }
    }

    private val userSession: UserSessionInterface by lazy {
        UserSession(this)
    }
    private var shopId: String? = null
    private var selectedEtalaseId: String? = null
    private var isShowDefault: Boolean? = null
    private var isShowZeroProduct: Boolean? = null
    private var shopType = com.tokopedia.shop_showcase.common.ShopType.REGULAR
    private var isNeedToGoToAddShowcase: Boolean? = false
    private var totalProduct: Int? = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        shopId = intent?.getStringExtra(ShopShowcaseListParam.EXTRA_SHOP_ID)
        selectedEtalaseId = intent?.getStringExtra(ShopShowcaseListParam.EXTRA_ETALASE_ID)
        isShowDefault = intent?.getBooleanExtra(ShopShowcaseListParam.EXTRA_IS_SHOW_DEFAULT, false)
        isShowZeroProduct = intent?.getBooleanExtra(ShopShowcaseListParam.EXTRA_IS_SHOW_ZERO_PRODUCT, false)
        isNeedToGoToAddShowcase = intent?.getBooleanExtra(ShopShowcaseListParam.EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE, false)
//        totalProduct = intent?.getIntExtra(ShopShowcaseListParam.EXTRA_TOTAL_PRODUCT, 0)
        setContentView(R.layout.fragment_shop_showcase_list_container)

        if (shopId == null){
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
            val fragmentShowcaseReorder = ShopShowcaseListReorderFragment.createInstance(
                    shopType, showcaseList, isMyShop())
            navigateToOtherFragment(fragmentShowcaseReorder, null)
        }
    }

    private fun setupStatusbar(){
        val window: Window = getWindow()
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            getWindow().statusBarColor = resources.getColor(android.R.color.white)
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
        val isOfficialStore = false // userSession.isShopOfficialStore
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