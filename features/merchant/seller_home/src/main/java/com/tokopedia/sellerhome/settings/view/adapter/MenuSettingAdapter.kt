package com.tokopedia.sellerhome.settings.view.adapter

import android.content.Context
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListAdapter
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.applink.internal.ApplinkConstInternalMarketplace
import com.tokopedia.applink.internal.ApplinkConstInternalUserPlatform
import com.tokopedia.config.GlobalConfig
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.ONE
import com.tokopedia.seller.menu.common.analytics.SettingTrackingConstant
import com.tokopedia.seller.menu.common.view.typefactory.OtherMenuAdapterTypeFactory
import com.tokopedia.seller.menu.common.view.uimodel.*
import com.tokopedia.seller.menu.common.view.uimodel.base.DividerType
import com.tokopedia.seller.menu.common.view.uimodel.base.SettingUiModel
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.common.SellerHomeConst
import com.tokopedia.sellerhome.settings.analytics.SocialMediaLinksTracker
import com.tokopedia.sellerhome.settings.view.uimodel.menusetting.MenuSettingAccess
import java.util.*

class MenuSettingAdapter(
    private val context: Context?,
    private val listener: Listener,
    private val isShowScreenRecorder: Boolean,
    typeFactory: OtherMenuAdapterTypeFactory
) : BaseListAdapter<SettingUiModel, OtherMenuAdapterTypeFactory>(typeFactory) {

    companion object {
        private const val SHIPPING_SERVICE_ALIAS = "shipping service"
        private const val PASSWORD_ALIAS = "password"

        private const val DEVELOPER_OPTION_INDEX_FROM_LAST = 4
        private const val SCREEN_RECORDER_INDEX_FROM_LAST = 3
        private const val SOCIAL_EXPIRED_DATE = 1646326800000 //04-03-2022
    }

    var menuSetingAccess = MenuSettingAccess()

    private val otherSettingList = listOf(
        SettingTitleMenuUiModel(
            context?.getString(R.string.setting_menu_account_setting).orEmpty(),
            IconUnify.USER
        ),
        MenuItemUiModel(
            context?.getString(R.string.setting_menu_self_profile).orEmpty(),
            clickApplink = ApplinkConst.SETTING_PROFILE,
            settingTypeInfix = SettingTrackingConstant.ACCOUNT_SETTING
        ),
        MenuItemUiModel(
            context?.getString(R.string.setting_menu_bank_account).orEmpty(),
            clickApplink = ApplinkConstInternalGlobal.SETTING_BANK,
            settingTypeInfix = SettingTrackingConstant.ACCOUNT_SETTING
        ),
        MenuItemUiModel(
            context?.getString(R.string.setting_menu_password).orEmpty(),
            settingTypeInfix = SettingTrackingConstant.ACCOUNT_SETTING,
            trackingAlias = PASSWORD_ALIAS
        ) { listener.onAddOrChangePassword() },
        DividerUiModel(DividerType.THICK),
        SettingTitleMenuUiModel(
            context?.getString(R.string.setting_menu_app_setting).orEmpty(),
            IconUnify.PHONE_SETTING
        ),
        MenuItemUiModel(
            context?.getString(R.string.setting_menu_chat_and_notification).orEmpty(),
            clickApplink = ApplinkConstInternalMarketplace.USER_NOTIFICATION_SETTING,
            settingTypeInfix = SettingTrackingConstant.APP_SETTING
        ),
        MenuItemUiModel(
            context?.getString(R.string.setting_notification_troubleshooter).orEmpty(),
            clickApplink = ApplinkConstInternalUserPlatform.PUSH_NOTIFICATION_TROUBLESHOOTER,
            settingTypeInfix = SettingTrackingConstant.APP_SETTING
        ),
        MenuItemUiModel(
            context?.getString(R.string.setting_menu_share_app).orEmpty(),
            settingTypeInfix = SettingTrackingConstant.APP_SETTING
        ) { listener.onShareApplication() },
        MenuItemUiModel(
            context?.getString(R.string.setting_menu_review_app).orEmpty(),
            settingTypeInfix = SettingTrackingConstant.APP_SETTING
        ) { listener.onReviewApplication() },
        MenuItemUiModel(
            title = context?.getString(R.string.setting_menu_give_feedback).orEmpty(),
            settingTypeInfix = SettingTrackingConstant.APP_SETTING
        ) { listener.onGiveFeedback() },
        DividerUiModel(DividerType.THIN_INDENTED),
        MenuItemUiModel(
            title = context?.getString(R.string.sah_social_menu_title).orEmpty(),
            settingTypeInfix = SettingTrackingConstant.APP_SETTING,
            tag = getSocialTag()
        ) {
            listener.onOpenSocialMediaLinks()
        }.apply {
            clickSendTracker = {
                SocialMediaLinksTracker.sendClickEvent()
            }
        },
        DividerUiModel(DividerType.THIN_INDENTED)
    )

    fun populateInitialMenus(isShopOwner: Boolean) {
        val menuList = mutableListOf<SettingUiModel>()
        if (isShopOwner) {
            val shopSettingList = getSettingList()
            menuList.addAll(shopSettingList)
        } else {
            menuList.add(SettingLoadingUiModel)
        }
        menuList.addAll(otherSettingList)
        if (isShowScreenRecorder)
            menuList.add(
                menuList.size - SCREEN_RECORDER_INDEX_FROM_LAST, MenuItemUiModel(
                    context?.getString(R.string.setting_screen_recorder).orEmpty(),
                    clickApplink = ApplinkConstInternalGlobal.SCREEN_RECORDER,
                    settingTypeInfix = SettingTrackingConstant.APP_SETTING
                )
            )
        if (GlobalConfig.isAllowDebuggingTools())
            menuList.add(menuList.size - DEVELOPER_OPTION_INDEX_FROM_LAST, MenuItemUiModel(
                context?.getString(R.string.setting_menu_developer_options).orEmpty(),
                settingTypeInfix = SettingTrackingConstant.APP_SETTING
            ) {
                context?.let {
                    RouteManager.route(it, ApplinkConst.DEVELOPER_OPTIONS)
                }
            })
        clearAllElements()
        addElement(menuList)
    }

    fun removeLoading() {
        visitables.find { it is SettingLoadingUiModel }?.let {
            clearElement(it)
        }
    }

    fun showSuccessAccessMenus(settingAccess: MenuSettingAccess) {
        menuSetingAccess = settingAccess
        val settingList = getSettingList()
        removeLoading()
        visitables.addAll(0, settingList)
        notifyItemRangeChanged(0, settingList.size - 1)
    }

    fun showShopSetting(isMultiLocation: Boolean){
        if (isMultiLocation) {
            if (getCurrentMultiLocMenuIndex() == null) {
                getExpectedMultiLocMenuIndex()?.let { index ->
                    visitables.add(index, getMultiLocationUiModel())
                    notifyItemInserted(index)
                }
            }
        } else {
            getCurrentMultiLocMenuIndex()?.let { currentIndex ->
                visitables.removeAt(currentIndex)
                notifyItemRemoved(currentIndex)
            }
        }
    }

    private fun getSettingList() =
        listOf(
            SettingTitleMenuUiModel(
                context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_setting)
                    .orEmpty(), IconUnify.SHOP_SETTING
            ),
            IndentedSettingTitleUiModel(
                context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_profile)
                    .orEmpty()
            ),
            MenuItemUiModel(
                context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_basic_info)
                    .orEmpty(),
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                clickAction = {
                    goToApplinkWhenAccessAuthorized(
                        menuSetingAccess.isInfoAccessAuthorized,
                        ApplinkConstInternalMarketplace.SHOP_SETTINGS_INFO
                    )
                }),
            MenuItemUiModel(
                context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_notes)
                    .orEmpty(),
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                clickAction = {
                    goToApplinkWhenAccessAuthorized(
                        menuSetingAccess.isNotesAccessAuthorized,
                        ApplinkConstInternalMarketplace.SHOP_SETTINGS_NOTES
                    )
                }),
            MenuItemUiModel(
                context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_shop_working_hours)
                    .orEmpty(),
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                clickAction = {
                    goToApplinkWhenAccessAuthorized(
                        menuSetingAccess.isInfoAccessAuthorized,
                        ApplinkConstInternalMarketplace.SHOP_SETTINGS_OPERATIONAL_HOURS
                    )
                }),
            DividerUiModel(DividerType.THIN_INDENTED),
            IndentedSettingTitleUiModel(
                context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_location_and_shipment)
                    .orEmpty()
            ),
            MenuItemUiModel(
                context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_set_shipment_method)
                    .orEmpty(),
                settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
                trackingAlias = SHIPPING_SERVICE_ALIAS,
                clickAction = {
                    goToApplinkWhenAccessAuthorized(
                        menuSetingAccess.isShipmentAccessAuthorized,
                        ApplinkConst.SELLER_SHIPPING_EDITOR
                    )
                }),
            DividerUiModel(DividerType.THIN_INDENTED),
            MenuItemUiModel(
                context?.getString(R.string.setting_menu_set_activation_page_cod).orEmpty(),
                settingTypeInfix = SettingTrackingConstant.COD_ACTIVATION_SETTING,
                clickAction = {
                    goToApplinkWhenAccessAuthorized(
                        menuSetingAccess.isShipmentAccessAuthorized,
                        ApplinkConst.SELLER_COD_ACTIVATION
                    )

                }),
            DividerUiModel(DividerType.THICK)
        )

    private fun goToApplinkWhenAccessAuthorized(isEligible: Boolean, applink: String) {
        if (isEligible) {
            context?.let {
                RouteManager.route(it, applink)
            }
        } else {
            listener.onNoAccess()
        }
    }

    private fun getSocialTag(): String {
        val expiredDateMillis = SOCIAL_EXPIRED_DATE
        val todayMillis = Date().time
        return if (todayMillis < expiredDateMillis) {
            context?.getString(R.string.setting_new_tag).orEmpty()
        } else {
            SellerHomeConst.EMPTY_STRING
        }
    }

    private fun getExpectedMultiLocMenuIndex(): Int? {
        return visitables.indexOfFirst {
            it is IndentedSettingTitleUiModel && it.settingTitle == context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_location_and_shipment)
                .orEmpty()
        }.takeIf { it != RecyclerView.NO_POSITION }?.plus(Int.ONE)
    }

    private fun getCurrentMultiLocMenuIndex(): Int? {
        return visitables.indexOfFirst {
            it is MenuItemUiModel && it.title == context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_add_and_shop_location)
                .orEmpty()
        }.takeIf { it != RecyclerView.NO_POSITION }?.plus(Int.ONE)
    }

    private fun getMultiLocationUiModel(): MenuItemUiModel {
        return MenuItemUiModel(
            context?.getString(com.tokopedia.seller.menu.common.R.string.setting_menu_add_and_shop_location)
                .orEmpty(),
            settingTypeInfix = SettingTrackingConstant.SHOP_SETTING,
            clickAction = {
                goToApplinkWhenAccessAuthorized(
                    menuSetingAccess.isAddressAccessAuthorized,
                    ApplinkConstInternalMarketplace.SHOP_SETTINGS_ADDRESS
                )
            })
    }

    interface Listener {
        fun onAddOrChangePassword()
        fun onShareApplication()
        fun onReviewApplication()
        fun onGiveFeedback()
        fun onOpenSocialMediaLinks()
        fun onNoAccess()
    }
}
