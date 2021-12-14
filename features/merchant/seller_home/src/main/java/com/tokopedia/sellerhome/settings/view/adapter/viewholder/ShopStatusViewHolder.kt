package com.tokopedia.sellerhome.settings.view.adapter.viewholder

import android.view.View
import androidx.annotation.ColorRes
import androidx.annotation.LayoutRes
import androidx.annotation.StringRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Group
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.seller.menu.common.analytics.NewOtherMenuTracking.sendEventClickShopStatus
import com.tokopedia.seller.menu.common.view.uimodel.base.*
import com.tokopedia.sellerhome.R
import com.tokopedia.sellerhome.settings.view.uimodel.secondaryinfo.widget.ShopStatusWidgetUiModel
import com.tokopedia.unifyprinciples.Typography

class ShopStatusViewHolder(itemView: View?,
                           private val onGoToPowerMerchant: (String?, Boolean) -> Unit,
                           private val onErrorClicked: () -> Unit,
                           private val onShopStatusImpression: (ShopType) -> Unit) :
    AbstractViewHolder<ShopStatusWidgetUiModel>(itemView) {

    companion object {
        @LayoutRes
        val LAYOUT_RES = R.layout.item_sah_new_other_shop_status

        private const val TAB_PM = "pm"
        private const val TAB_PM_PRO = "pm_pro"
    }

    private val successOsLayout: Group? =
        itemView?.findViewById(R.id.group_sah_new_other_shop_status_success_os)
    private val pmIcon: IconUnify? =
        itemView?.findViewById(R.id.ic_sah_new_other_shop_status_pm)
    private val pmProIcon: IconUnify? =
        itemView?.findViewById(R.id.ic_sah_new_other_shop_status_pm_pro)
    private val shopStatusTitleTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_shop_status_title)
    private val warningIcon: IconUnify? =
        itemView?.findViewById(R.id.ic_sah_new_other_shop_status_warning)
    private val shopStatusDescTextView: Typography? =
        itemView?.findViewById(R.id.tv_sah_new_other_shop_status_desc)
    private val loadingLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.shimmer_sah_new_other_shop_status)
    private val errorLayout: ConstraintLayout? =
        itemView?.findViewById(R.id.error_state_sah_new_other_shop_status)

    private var onItemViewClicked: () -> Unit = {}

    override fun bind(element: ShopStatusWidgetUiModel) {
        when(val state = element.state) {
            is SettingResponseState.SettingSuccess -> {
                itemView.addOnImpressionListener(element.impressHolder) {
                    onShopStatusImpression(state.data)
                }
                setShopStatusSuccessLayout(state.data)
            }
            is SettingResponseState.SettingError -> setShopStatusErrorLayout()
            is SettingResponseState.SettingLoading -> setShopStatusLoadingLayout()
        }
    }

    private fun setShopStatusSuccessLayout(shopType: ShopType) {
        shopStatusDescTextView?.run {
            setOnClickListener(null)
            isClickable = false
        }

        when (shopType) {
            is RegularMerchant -> setRegularMerchantLayout()
            is PowerMerchantStatus -> setPowerMerchantLayout(shopType)
            is PowerMerchantProStatus -> setPowerMerchantProLayout(shopType)
            is ShopType.OfficialStore -> setOfficialStoreLayout()
        }

        itemView.setOnClickListener {
            sendEventClickShopStatus(shopType)
            onItemViewClicked()
        }

        loadingLayout?.gone()
        errorLayout?.gone()
    }

    private fun setRegularMerchantLayout() {
        setTitle(com.tokopedia.seller.menu.common.R.string.regular_merchant)
        setDescription(
            R.string.sah_new_other_status_upgrade,
            com.tokopedia.unifyprinciples.R.color.Unify_GN500
        )

        pmIcon?.gone()
        pmProIcon?.gone()
        warningIcon?.gone()
        successOsLayout?.gone()

        onItemViewClicked = {
            onGoToPowerMerchant(TAB_PM, false)
        }
    }

    private fun setPowerMerchantLayout(powerMerchantStatus: PowerMerchantStatus) {
        setTitle(com.tokopedia.seller.menu.common.R.string.power_merchant_upgrade)
        when (powerMerchantStatus) {
            is PowerMerchantStatus.Active -> {
                setDescription(
                    R.string.sah_new_other_status_upgrade,
                    com.tokopedia.unifyprinciples.R.color.Unify_GN500
                )
                warningIcon?.gone()
                shopStatusDescTextView?.run {
                    isClickable = true
                    setOnClickListener {
                        onGoToPowerMerchant(TAB_PM_PRO, true)
                    }
                }
            }
            is PowerMerchantStatus.NotActive -> {
                setDescription(
                    com.tokopedia.seller.menu.common.R.string.setting_not_active,
                    com.tokopedia.unifyprinciples.R.color.Unify_RN500
                )
                warningIcon?.show()
            }
        }
        pmIcon?.show()
        pmProIcon?.gone()
        successOsLayout?.gone()

        onItemViewClicked = {
            onGoToPowerMerchant(TAB_PM_PRO, false)
        }
    }

    private fun setPowerMerchantProLayout(powerMerchantProStatus: PowerMerchantProStatus) {
        setTitle(R.string.sah_new_other_status_pm_pro_title)
        val statusStringRes: Int
        val statusColorRes: Int
        val isStatusActive: Boolean
        when (powerMerchantProStatus) {
            is PowerMerchantProStatus.Advanced -> {
                statusStringRes = R.string.sah_new_other_status_pm_pro_advanced
                statusColorRes = com.tokopedia.unifyprinciples.R.color.Unify_NN600
                isStatusActive = true
            }
            is PowerMerchantProStatus.Expert -> {
                statusStringRes = R.string.sah_new_other_status_pm_pro_expert
                statusColorRes = com.tokopedia.unifyprinciples.R.color.Unify_TN500
                isStatusActive = true
            }
            is PowerMerchantProStatus.Ultimate -> {
                statusStringRes = R.string.sah_new_other_status_pm_pro_ultimate
                statusColorRes = com.tokopedia.unifyprinciples.R.color.Unify_YN500
                isStatusActive = true
            }
            is PowerMerchantProStatus.InActive -> {
                statusStringRes = R.string.setting_not_active
                statusColorRes = com.tokopedia.unifyprinciples.R.color.Unify_RN500
                isStatusActive = false
            }
        }
        setDescription(statusStringRes, statusColorRes)

        warningIcon?.showWithCondition(!isStatusActive)
        pmIcon?.gone()
        pmProIcon?.show()
        successOsLayout?.gone()

        onItemViewClicked = {
            onGoToPowerMerchant(TAB_PM_PRO, false)
        }
    }

    private fun setOfficialStoreLayout() {
        successOsLayout?.show()
        hideAllShopStatusSuccessLayouts()
        loadingLayout?.gone()
        errorLayout?.gone()
        onItemViewClicked = {
            onGoToPowerMerchant(null, false)
        }
    }

    private fun setTitle(@StringRes titleStringRes: Int) {
        shopStatusTitleTextView?.run {
            text = getString(titleStringRes)
            show()
        }
    }

    private fun setDescription(
        @StringRes descStringRes: Int,
        @ColorRes descColorRes: Int) {
        shopStatusDescTextView?.run {
            text = getString(descStringRes)
            setTextColor(
                MethodChecker.getColor(
                    itemView.context,
                    descColorRes
                )
            )
            show()
        }
    }

    private fun setShopStatusLoadingLayout() {
        hideAllShopStatusSuccessLayouts()
        successOsLayout?.gone()
        loadingLayout?.show()
        errorLayout?.gone()
        itemView.setOnClickListener(null)
    }

    private fun setShopStatusErrorLayout() {
        hideAllShopStatusSuccessLayouts()
        successOsLayout?.gone()
        loadingLayout?.gone()
        errorLayout?.run {
            show()
            setOnClickListener {
                onErrorClicked()
            }
        }
        itemView.setOnClickListener(null)
    }

    /**
     * Note:    We should've use group to change multiple views visibility altogether.
     *          However, currently there is a bug that causes the layout become unable to change individual
     *          view's visibility inside a group. We need to upgrade the constraint layout library version
     *          to fix that. However, doing so, could cause some existing layouts to be broken.
     */
    private fun hideAllShopStatusSuccessLayouts() {
        shopStatusTitleTextView?.gone()
        shopStatusDescTextView?.gone()
        pmIcon?.gone()
        pmProIcon?.gone()
        warningIcon?.gone()
    }

}