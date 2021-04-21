package com.tokopedia.shop_showcase.shop_showcase_management.presentation.activity

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.etalase.DeepLinkMapperEtalase
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.PageNameConstant
import com.tokopedia.shop_showcase.common.ShopShowcaseFragmentNavigation
import com.tokopedia.shop_showcase.common.ShopShowcaseListParam
import com.tokopedia.shop_showcase.common.ShopType
import com.tokopedia.shop_showcase.common.util.ShopShowcaseAbTestUtil.isNotRegularMerchant
import com.tokopedia.shop_showcase.common.util.ShopShowcaseAbTestUtil.isShouldCheckShopType
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.*
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

class ShopShowcaseListActivity : BaseSimpleActivity(), ShopShowcaseFragmentNavigation {

    companion object {
        const val REQUEST_CODE_ADD_ETALASE = 289

        val ACTIVITY_LAYOUT = R.layout.activity_shop_showcase_list_container
        val PARENT_VIEW_ACTIVITY = R.id.parent_view
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
    private var isNeedToOpenShowcasePicker: String = ""
    private var isNeedToOpenReorder: Boolean = false
    private var isSellerNeedToHideShowcaseGroupValue: Boolean = false
    private var productId: String = ""
    private var productName: String = ""
    private var listShowcase: ArrayList<ShopEtalaseModel>? = arrayListOf()
    private var preSelectedShowcaseListPicker: ArrayList<ShowcaseItemPicker>? = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        val bundle = intent.getBundleExtra(ShopShowcaseParamConstant.EXTRA_BUNDLE)
        if (bundle != null) {
            shopId = bundle.getString(ShopShowcaseParamConstant.EXTRA_SHOP_ID, "0").toString()
            shopType = bundle.getString(ShopShowcaseParamConstant.EXTRA_SHOP_TYPE, ShopType.REGULAR)
            selectedEtalaseId = bundle.getString(ShopShowcaseParamConstant.EXTRA_SELECTED_ETALASE_ID, "0").toString()
            isShowDefault = bundle.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_DEFAULT, true)
            isShowZeroProduct = bundle.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_SHOW_ZERO_PRODUCT, true)
            isSellerNeedToHideShowcaseGroupValue = bundle.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_SELLER_NEED_TO_HIDE_SHOWCASE_GROUP_VALUE, false)
            isNeedToGoToAddShowcase = bundle.getBoolean(ShopShowcaseListParam.EXTRA_IS_NEED_TO_GOTO_ADD_SHOWCASE, false)
            isNeedToOpenShowcasePicker = bundle.getString(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_SHOWCASE_PICKER, "")
            preSelectedShowcaseListPicker = bundle.getParcelableArrayList(ShopShowcaseParamConstant.EXTRA_PRE_SELECTED_SHOWCASE_PICKER)
            productId = bundle.getString(ShopShowcaseParamConstant.EXTRA_PICKER_PRODUCT_ID, "")
            productName = bundle.getString(ShopShowcaseParamConstant.EXTRA_PICKER_PRODUCT_NAME, "")
        }

        // If there is no shopId  then it's seller view
        if (shopId == "0") {
            shopId = getShopIdFromDeepLink()
        }

        if (isMyShop()) {
            getShopType()
        }

        setBackgroundColor()

        super.onCreate(savedInstanceState)
    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    override fun getParentViewResourceID(): Int {
        return PARENT_VIEW_ACTIVITY
    }

    override fun getNewFragment(): Fragment? {
        return when {
            isNeedToOpenShowcasePicker.isNotEmpty() -> {
                ShopShowcasePickerFragment.createInstance(
                        shopId,
                        isMyShop(),
                        shopType,
                        isNeedToOpenShowcasePicker,
                        preSelectedShowcaseListPicker,
                        productId,
                        productName
                )
            }
            isNeedToOpenReorder -> getShowcaseListReorderFragment()
            else -> getShowcaseListFragment()
        }
    }

    override fun navigateToPage(page: String, tag: String?, showcaseList: ArrayList<ShopEtalaseModel>?) {
        when (page) {
            PageNameConstant.SHOWCASE_LIST_PAGE -> {
                isNeedToGoToAddShowcase = false
                isNeedToOpenReorder = false
                inflateFragment()
            }
            PageNameConstant.SHOWCASE_LIST_REORDER_PAGE -> {
                isNeedToGoToAddShowcase = false
                isNeedToOpenReorder = true
                listShowcase = showcaseList
                inflateFragment()
            }
        }
    }

    private fun getShowcaseListReorderFragment(): Fragment {
        return if (isShouldCheckShopType()) {
            // if ab test on, check shop type
            if (isNotRegularMerchant(shopType)) {
                // return new revamped showcase reorder list
                ShopShowcaseListReorderFragment.createInstance(shopType, listShowcase, isMyShop())
            } else {
                // return old showcase reorder list
                ShopShowcaseListReorderFragmentOld.createInstance(shopType, listShowcase, isMyShop())
            }
        } else {
            // if ab test is off, by default its return new revamped showcase reorder list
            ShopShowcaseListReorderFragment.createInstance(shopType, listShowcase, isMyShop())
        }
    }

    private fun getShowcaseListFragment(): Fragment {
        return if (isShouldCheckShopType()) {
            // if ab test on, check shop type
            if (isNotRegularMerchant(shopType)) {
                // return new revamped showcase list
                ShopShowcaseListFragment.createInstance(
                        shopType = shopType,
                        shopId = shopId,
                        selectedEtalaseId = selectedEtalaseId,
                        isShowDefault = isShowDefault,
                        isShowZeroProduct = isShowZeroProduct,
                        isMyShop = isMyShop(),
                        isNeedToGoToAddShowcase = isNeedToGoToAddShowcase,
                        isSellerNeedToHideShowcaseGroupValue = isSellerNeedToHideShowcaseGroupValue
                )
            } else {
                // return old showcase list
                ShopShowcaseListFragmentOld.createInstance(
                        shopType = shopType,
                        shopId = shopId,
                        selectedEtalaseId = selectedEtalaseId,
                        isShowDefault = isShowDefault,
                        isShowZeroProduct = isShowZeroProduct,
                        isMyShop = isMyShop(),
                        isNeedToGoToAddShowcase = isNeedToGoToAddShowcase,
                        isSellerNeedToHideShowcaseGroupValue = isSellerNeedToHideShowcaseGroupValue
                )
            }
        } else {
            // if ab test is off, by default its return new revamped showcase list
            ShopShowcaseListFragment.createInstance(
                    shopType = shopType,
                    shopId = shopId,
                    selectedEtalaseId = selectedEtalaseId,
                    isShowDefault = isShowDefault,
                    isShowZeroProduct = isShowZeroProduct,
                    isMyShop = isMyShop(),
                    isNeedToGoToAddShowcase = isNeedToGoToAddShowcase,
                    isSellerNeedToHideShowcaseGroupValue = isSellerNeedToHideShowcaseGroupValue
            )
        }
    }

    /**
     * @return shopId from deeplink query param.
     * @return userSession.shopId, if shopId is null or blank or = 0
     * */
    private fun getShopIdFromDeepLink(): String {
        val uri = intent.data
        val shopId = uri?.getQueryParameter(DeepLinkMapperEtalase.PATH_SHOP_ID)
        return if (shopId.isNullOrBlank() || shopId == "0") {
            userSession.shopId
        } else {
            shopId
        }
    }

    private fun isMyShop(): Boolean = shopId == userSession.shopId

    private fun getShopType() {
        val isOfficialStore = userSession.isShopOfficialStore
        val isGoldMerchant = userSession.isGoldMerchant

        shopType = when {
            isOfficialStore -> {
                ShopType.OFFICIAL_STORE
            }
            isGoldMerchant -> {
                ShopType.GOLD_MERCHANT
            }
            else -> {
                ShopType.REGULAR
            }
        }
    }

    private fun setBackgroundColor() {
        window.decorView.setBackgroundColor(
                androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_N0)
        )
    }

}