package com.tokopedia.shop_showcase.shop_showcase_management.presentation.activity

import android.content.pm.ActivityInfo
import android.os.Bundle
import android.provider.Settings
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.applink.etalase.DeepLinkMapperEtalase
import com.tokopedia.device.info.DeviceScreenInfo
import com.tokopedia.shop.common.constant.ShopShowcaseParamConstant
import com.tokopedia.shop.common.data.model.ShowcaseItemPicker
import com.tokopedia.shop.common.graphql.data.shopetalase.ShopEtalaseModel
import com.tokopedia.shop_showcase.R
import com.tokopedia.shop_showcase.common.PageNameConstant
import com.tokopedia.shop_showcase.common.ShopShowcaseFragmentNavigation
import com.tokopedia.shop_showcase.common.ShopShowcaseListParam
import com.tokopedia.shop_showcase.common.ShopType
import com.tokopedia.shop_showcase.shop_showcase_management.presentation.fragment.*
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.accelerometer.orientation.AccelerometerOrientationListener

class ShopShowcaseListActivity : BaseSimpleActivity(), ShopShowcaseFragmentNavigation {

    companion object {
        const val REQUEST_CODE_ADD_ETALASE = 289

        @LayoutRes
        val ACTIVITY_LAYOUT = R.layout.activity_shop_showcase_list_container

        @IdRes
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
    private var isNeedToOpenCreateShowcase: Boolean = false
    private var isNeedToOpenShowcasePicker: String = ""
    private var isNeedToOpenReorder: Boolean = false
    private var isSellerNeedToHideShowcaseGroupValue: Boolean = false
    private var productId: String = ""
    private var productName: String = ""
    private var listShowcase: ArrayList<ShopEtalaseModel>? = arrayListOf()
    private var preSelectedShowcaseListPicker: ArrayList<ShowcaseItemPicker>? = arrayListOf()
    private val accelerometerOrientationListener: AccelerometerOrientationListener by lazy {
        AccelerometerOrientationListener(contentResolver) {
            setScreenOrientation(it)
        }
    }

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
            isNeedToGoToAddShowcase = bundle.getBoolean(ShopShowcaseParamConstant.EXTRA_IS_NEED_TO_OPEN_CREATE_SHOWCASE, false)
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
        configInitialScreenOrientation()
        super.onCreate(savedInstanceState)
    }

    private fun configInitialScreenOrientation() {
        if(isShowcasePickerFragment()){
            val isAccelerometerRotationEnabled = Settings.System.getInt(
                contentResolver,
                Settings.System.ACCELEROMETER_ROTATION,
                0
            ) == 1
            setScreenOrientation(isAccelerometerRotationEnabled)
        }
    }

    override fun onResume() {
        super.onResume()
        registerAccelerometerOrientationListener()
    }

    private fun registerAccelerometerOrientationListener() {
        if (DeviceScreenInfo.isTablet(this)) {
            accelerometerOrientationListener.register()
        }
    }

    override fun getLayoutRes(): Int {
        return ACTIVITY_LAYOUT
    }

    override fun getParentViewResourceID(): Int {
        return PARENT_VIEW_ACTIVITY
    }

    override fun getNewFragment(): Fragment? {
        return when {
            isShowcasePickerFragment() -> getShowcasePickerFragment()
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

    private fun getShowcasePickerFragment(): Fragment {
        return ShopShowcasePickerFragment.createInstance(
                shopId,
                isMyShop(),
                shopType,
                isNeedToOpenShowcasePicker,
                preSelectedShowcaseListPicker,
                productId,
                productName
        )
    }

    private fun getShowcaseListReorderFragment(): Fragment {
        return ShopShowcaseListReorderFragment.createInstance(shopType, listShowcase, isMyShop())
    }

    private fun getShowcaseListFragment(): Fragment {
        return ShopShowcaseListFragment.createInstance(
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

    private fun isShowcasePickerFragment(): Boolean {
        return isNeedToOpenShowcasePicker.isNotEmpty()
    }

    private fun setScreenOrientation(isEnabled: Boolean) {
        if (DeviceScreenInfo.isTablet(this)) {
            requestedOrientation =
                if (isEnabled) ActivityInfo.SCREEN_ORIENTATION_FULL_SENSOR else ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
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
                androidx.core.content.ContextCompat.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_Background)
        )
    }

}