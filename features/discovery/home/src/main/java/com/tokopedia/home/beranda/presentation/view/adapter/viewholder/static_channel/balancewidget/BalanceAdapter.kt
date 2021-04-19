package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.app.Activity
import android.content.ContextWrapper
import android.graphics.Color
import android.graphics.PorterDuff
import android.graphics.Typeface
import android.graphics.drawable.GradientDrawable
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_wallet.analytics.CommonWalletAnalytics
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.OvoWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_TOKOPOINT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OVO
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceTagAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceTextAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.helper.isHexColor
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.show
import kotlinx.android.synthetic.main.item_balance_widget.view.*

/**
 * Created by yfsx on 3/1/21.
 */

class BalanceAdapter(val listener: HomeCategoryListener?): RecyclerView.Adapter<BalanceAdapter.Holder>() {

    private var itemMap: HomeBalanceModel = HomeBalanceModel()

    fun setItemMap(itemMap: HomeBalanceModel) {
        this.itemMap = HomeBalanceModel()
        this.itemMap = itemMap
        notifyDataSetChanged()
    }

    fun getItemMap():  HomeBalanceModel {
        return itemMap
    }

    fun getTokopointsDataPosition(): Int {
        getItemMap().let {
            val keys =  it.balanceDrawerItemModels.filterValues { model -> model.drawerItemType == TYPE_TOKOPOINT }.keys
            if (keys.isNotEmpty()) {
                return keys.first()
            }
        }
        return -1
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget, parent, false))
    }

    override fun getItemCount(): Int {
        return itemMap.balanceDrawerItemModels.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(
                itemMap.balanceDrawerItemModels[position],
                listener,
                itemMap.balanceType != HomeBalanceModel.TYPE_STATE_3)
    }

    class Holder(v: View): RecyclerView.ViewHolder(v) {

        private val walletAnalytics: CommonWalletAnalytics = CommonWalletAnalytics()
        private var listener: HomeCategoryListener? = null
        private var isOvoAvailable: Boolean = false
        fun bind(drawerItem: BalanceDrawerItemModel?, listener: HomeCategoryListener?, isOvoAvailable: Boolean) {
            this.listener = listener
            renderTokoPoint(drawerItem)
            this.isOvoAvailable = isOvoAvailable
        }

        private fun renderTokoPoint(element: BalanceDrawerItemModel?) {
            itemView.home_progress_bar_balance_layout.gone()
            itemView.home_container_action_balance.gone()
            when (element?.state) {
                BalanceDrawerItemModel.STATE_LOADING -> itemView.home_progress_bar_balance_layout.show()
                BalanceDrawerItemModel.STATE_SUCCESS -> {
                    itemView.home_container_action_balance.show()
                    renderBalanceText(element?.balanceTitleTextAttribute, element?.balanceTitleTagAttribute, itemView.home_tv_balance)
                    renderBalanceText(element?.balanceSubTitleTextAttribute, element?.balanceSubTitleTagAttribute, itemView.home_tv_btn_action_balance)
                    itemView.home_container_balance.handleItemCLickType(
                            element = element,
                            tokopointsAction = {
                                //handle click for type tokopoints
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )
                                OvoWidgetTracking.sendClickOnTokopointsBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "")

                            },
                            ovoWalletAction = {
                                //handle click for type ovo
                                if (RouteManager.isSupportApplink(itemView.context, element.applinkContainer)) {
                                    OvoWidgetTracking.sendClickOnOVOBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "")
                                    val intentBalanceWallet = RouteManager.getIntent(itemView.context, element.applinkContainer)
                                    itemView.context.startActivity(intentBalanceWallet)
                                }
                            },
                            rewardsAction = {
                                //handle click for type rewards
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )
                                OvoWidgetTracking.sendClickOnRewardsBalanceWidgetTracker(isOvoAvailable, listener?.userId?:"")
                            },
                            couponsAction = {
                                //handle click for type coupon
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )
                                OvoWidgetTracking.sendClickOnCouponBalanceWidgetTracker(isOvoAvailable, listener?.userId?:"")
                            },
                            bboAction = {
                                //handle click for type bbo
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )
                                OvoWidgetTracking.sendClickOnBBOBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "0")
                                //uncomment when we use new tracker
                                //OvoWidgetTracking.sendClickOnBBONewTokopointsWidget(isOvoAvailable, listener?.userId ?: "")
                            },
                            walletTopupAction = {
                                //handle click for type wallet topup
                                if (RouteManager.isSupportApplink(itemView.context, element.applinkContainer)) {
                                    OvoWidgetTracking.sendClickOnOVOBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "")
                                    OvoWidgetTracking.eventTopUpOvo(listener?.userId)
                                    val intentBalanceWallet = RouteManager.getIntent(itemView.context, element.applinkContainer)
                                    itemView.context.startActivity(intentBalanceWallet)
                                }
                            },
                            walletOtherAction = {
                                //handle click for type wallet other

                            },
                            walletPendingAction ={
                                //handle click for type wallet pending

                            })

                    itemView.home_tv_btn_action_balance.handleItemCLickType(
                            element = element,
                            tokopointsAction = {
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )
                                OvoWidgetTracking.sendClickOnTokopointsBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "")
                            },
                            ovoWalletAction = {
                                if (RouteManager.isSupportApplink(itemView.context, element.applinkActionText)) {
                                    OvoWidgetTracking.sendClickOnOVOBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "")
                                    val intentBalanceWallet = RouteManager.getIntent(itemView.context, element.applinkActionText)
                                    itemView.context.startActivity(intentBalanceWallet)
                                }
                            },
                            rewardsAction = {
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )
                                OvoWidgetTracking.sendClickOnRewardsBalanceWidgetTracker(isOvoAvailable, listener?.userId?:"")
                            },
                            couponsAction = {
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )
                                OvoWidgetTracking.sendClickOnCouponBalanceWidgetTracker(isOvoAvailable, listener?.userId?:"")
                            },
                            bboAction = {
                                listener?.actionTokoPointClicked(
                                        element.applinkContainer,
                                        element.redirectUrl,
                                        if (element.mainPageTitle.isEmpty())
                                            TITLE_HEADER_WEBSITE
                                        else
                                            element.mainPageTitle
                                )

                                OvoWidgetTracking.sendClickOnBBOBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "0")
                                //uncomment when we use new tracker
                                //OvoWidgetTracking.sendClickOnBBONewTokopointsWidget(isOvoAvailable, listener?.userId ?: "")
                            },
                            walletTopupAction = {
                                if (RouteManager.isSupportApplink(itemView.context, element.applinkContainer)) {
                                    OvoWidgetTracking.sendClickOnOVOBalanceWidgetTracker(isOvoAvailable, listener?.userId ?: "")
                                    OvoWidgetTracking.eventTopUpOvo(listener?.userId)
                                    val intentBalanceWallet = RouteManager.getIntent(itemView.context, element.applinkContainer)
                                    itemView.context.startActivity(intentBalanceWallet)
                                }
                            },
                            walletOtherAction = {
                                //handle click for type wallet other
                                //handle click for type wallet other

                            },
                            walletPendingAction ={
                                //handle click for type wallet pending
                                if (itemView.context !is Activity && itemView.context is ContextWrapper) {
                                    val context = (itemView.context as ContextWrapper).baseContext
                                    val activity = context as Activity
                                    activity.overridePendingTransition(R.anim.anim_slide_up_in, R.anim.anim_page_stay)
                                }
                                walletAnalytics.eventClickActivationOvoHomepage()
                                val intentBalanceWallet = RouteManager.getIntent(itemView.context, element.applinkActionText)
                                itemView.context.startActivity(intentBalanceWallet)
                            })
                }
                BalanceDrawerItemModel.STATE_ERROR -> {
                    itemView.home_container_action_balance.show()
                    renderBalanceText(element.balanceTitleTextAttribute, element.balanceTitleTagAttribute, itemView.home_tv_balance)
                    renderBalanceText(element.balanceSubTitleTextAttribute, element.balanceSubTitleTagAttribute, itemView.home_tv_btn_action_balance)
                    itemView.home_container_balance.handleItemCLickType(
                            element = element,
                            ovoWalletAction = {listener?.onRefreshTokoCashButtonClicked()},
                            rewardsAction = {listener?.onRefreshTokoPointButtonClicked()},
                            bboAction = {listener?.onRefreshTokoPointButtonClicked()},
                            tokopointsAction = {listener?.onRefreshTokoPointButtonClicked()}
                    )
                    itemView.home_tv_btn_action_balance.handleItemCLickType(
                            element = element,
                            ovoWalletAction = {listener?.onRefreshTokoCashButtonClicked()},
                            rewardsAction = {listener?.onRefreshTokoPointButtonClicked()},
                            bboAction = {listener?.onRefreshTokoPointButtonClicked()},
                            tokopointsAction = {listener?.onRefreshTokoPointButtonClicked()}
                    )
                }
            }

            element?.defaultIconRes?.let {
                itemView.home_iv_logo_balance.setImageDrawable(itemView.context.getDrawable(it))
            }
            element?.iconImageUrl?.let {
                if (it.isNotEmpty()) itemView.home_iv_logo_balance.loadImage(it)
            }
        }

        private fun renderBalanceText(textAttr: BalanceTextAttribute?, tagAttr: BalanceTagAttribute?, textView: TextView, textSize: Int = R.dimen.sp_10) {
            textView.background = null
            textView.text = null
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(textSize))
            if (tagAttr != null && tagAttr.text.isNotEmpty()) {
                renderTagAttribute(tagAttr, textView)
            } else if (textAttr != null && textAttr.text.isNotEmpty()) {
                renderTextAttribute(textAttr, textView)
            }
        }

        private fun renderTagAttribute(tagAttr: BalanceTagAttribute, textView: TextView) {
            if (tagAttr.backgroundColour.isNotEmpty() && tagAttr.backgroundColour.isHexColor()) {
                val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_tokopoints_rounded)
                (drawable as GradientDrawable?)?.let {
                    it.setColorFilter(Color.parseColor(tagAttr.backgroundColour), PorterDuff.Mode.SRC_ATOP)
                    textView.background = it
                    val horizontalPadding = itemView.context.resources.getDimensionPixelSize(R.dimen.dp_2)
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(R.dimen.sp_8))
                    textView.setTypeface(null, Typeface.NORMAL)
                    textView.setPadding(horizontalPadding, 0, horizontalPadding, 0)
                }
                textView.setTextColor(ContextCompat.getColor(itemView.context, R.color.Unify_N0))
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68))
            }
            if (tagAttr.text.isNotEmpty()) {
                textView.text = tagAttr.text
            }
        }

        private fun renderTextAttribute(textAttr: BalanceTextAttribute, textView: TextView) {
            if (textAttr.colour.isNotEmpty() && textAttr.colour.isHexColor()) {
                textView.setTextColor(Color.parseColor(textAttr.colour).invertIfDarkMode(itemView.context))
            } else if (textAttr.colourRef != null) {
                textView.setTextColor(ContextCompat.getColor(itemView.context, textAttr.colourRef))
            } else {
                textView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96))
            }
            if (textAttr.isBold) {
                textView.setTypeface(textView.typeface, Typeface.BOLD)
            } else {
                textView.setTypeface(textView.typeface, Typeface.NORMAL)
            }
            if (textAttr.text.isNotEmpty()) {
                textView.text = textAttr.text
            }
        }

        private fun View.handleItemCLickType(element: BalanceDrawerItemModel,
                                             tokopointsAction: () -> Unit = {},
                                             ovoWalletAction: () -> Unit= {},
                                             rewardsAction: () -> Unit= {},
                                             couponsAction: () -> Unit= {},
                                             bboAction: () -> Unit= {},
                                             walletTopupAction: () -> Unit= {},
                                             walletOtherAction: () -> Unit= {},
                                             walletPendingAction: () -> Unit= {}) {
            setOnClickListener {
                when (element.drawerItemType) {
                    TYPE_TOKOPOINT -> tokopointsAction.invoke()
                    TYPE_FREE_ONGKIR -> bboAction.invoke()
                    TYPE_COUPON -> couponsAction.invoke()
                    TYPE_REWARDS -> rewardsAction.invoke()
                    TYPE_WALLET_OVO -> ovoWalletAction.invoke()
                    TYPE_WALLET_WITH_TOPUP -> walletTopupAction.invoke()
                    TYPE_WALLET_OTHER -> walletOtherAction.invoke()
                    TYPE_WALLET_PENDING_CASHBACK -> walletPendingAction.invoke()
                }
            }
        }

        companion object {
            private const val TITLE_HEADER_WEBSITE = "Tokopedia"
            private const val KUPON_SAYA_URL_PATH = "kupon-saya"
        }
    }
}