package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel.balancewidget

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.applink.RouteManager
import com.tokopedia.common_wallet.analytics.CommonWalletAnalytics
import com.tokopedia.home.R
import com.tokopedia.home.analytics.v2.OvoWidgetTracking
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.STATE_LOADING
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_COUPON
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_FREE_ONGKIR
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_REWARDS
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_TOKOPOINT
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_APP_NOT_LINKED
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OTHER
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.util.HomeServerLogger.TYPE_ERROR_SUBMIT_WALLET
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.kotlin.extensions.view.invisible
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlin.coroutines.CoroutineContext

/**
 * Created by yfsx on 3/1/21.
 */

class BalanceAdapter(
    val listener: HomeCategoryListener?,
    diffUtil: DiffUtil.ItemCallback<BalanceDrawerItemModel>
): ListAdapter<BalanceDrawerItemModel, BalanceAdapter.Holder>(diffUtil) {

    var attachedRecyclerView: RecyclerView? = null
    private var itemMap: HomeBalanceModel = HomeBalanceModel()

    companion object {
        var disableAnimation: Boolean = false
        private const val FIRST_POSITION = 0
    }

    @Suppress("TooGenericExceptionCaught")
    fun setItemMap(itemMap: HomeBalanceModel) {
        this.itemMap = itemMap

        val balanceModelList = mutableListOf<BalanceDrawerItemModel>()
        try {
            itemMap.balanceDrawerItemModels.mapValues {
                balanceModelList.add(it.key, it.value)
            }
            submitList(balanceModelList.toMutableList())
        } catch (e: Exception) {
            HomeServerLogger.logWarning(
                type = TYPE_ERROR_SUBMIT_WALLET,
                throwable = e,
                reason = e.message?:""
            )
            e.printStackTrace()
        }
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.attachedRecyclerView = recyclerView
    }

    fun getItemMap():  HomeBalanceModel {
        return itemMap
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(LayoutInflater.from(parent.context).inflate(R.layout.item_balance_widget_new, parent, false))
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

    class Holder(v: View): RecyclerView.ViewHolder(v), CoroutineScope {
        private var alternateDrawerItem: List<BalanceDrawerItemModel>? = null
        private var element: BalanceDrawerItemModel? = null
        override val coroutineContext: CoroutineContext
            get() = Dispatchers.Main

        var animationJob: Job? = null

        private val walletAnalytics: CommonWalletAnalytics = CommonWalletAnalytics()
        private var listener: HomeCategoryListener? = null
        private var isOvoAvailable: Boolean = false
        private var home_container_balance: ConstraintLayout? = itemView.findViewById(R.id.home_container_balance)
        private var home_iv_logo_shimmering: LoaderUnify? = itemView.findViewById(R.id.home_iv_logo_shimmering)
        private var home_progress_bar_balance_layout: ConstraintLayout? = itemView.findViewById(R.id.home_progress_bar_balance_layout)
        private var home_iv_logo_balance: ImageUnify? = itemView.findViewById<ImageUnify>(R.id.home_iv_logo_balance)
        private var home_tv_balance: TextView = itemView.findViewById(R.id.home_tv_balance)
        private var home_container_action_balance: ConstraintLayout? = itemView.findViewById(R.id.home_container_action_balance)
        private var home_tv_reserve_balance: Typography? = itemView.findViewById(R.id.home_tv_reserve_balance)
        private var divider: View? = itemView.findViewById(R.id.divider_balance)

        fun bind(drawerItem: BalanceDrawerItemModel?,
                 listener: HomeCategoryListener?,
                 isOvoAvailable: Boolean
        ) {
            this.listener = listener
            renderDrawerItem(drawerItem)
            this.itemView.tag = String.format(
                itemView.context.getString(R.string.tag_balance_widget), drawerItem?.drawerItemType.toString()
            )
            this.isOvoAvailable = isOvoAvailable
        }

        private fun renderDrawerItem(element: BalanceDrawerItemModel?) {
            /**
             * Initial state
             */
            if (!disableAnimation) {
                home_iv_logo_shimmering?.show()
                home_progress_bar_balance_layout?.show()
            } else {
                home_iv_logo_shimmering?.gone()
                home_progress_bar_balance_layout?.gone()
            }

            if (adapterPosition == FIRST_POSITION) {
                divider?.invisible()
            } else {
                divider?.show()
            }

            animationJob?.cancel()

            when (element?.state) {
                BalanceDrawerItemModel.STATE_LOADING -> {
                    home_iv_logo_balance?.invisible()

                    home_tv_balance?.invisible()

                    if (!disableAnimation) {
                        home_iv_logo_shimmering?.show()
                        home_progress_bar_balance_layout?.show()
                    }
                }
                BalanceDrawerItemModel.STATE_SUCCESS -> {
                    home_progress_bar_balance_layout?.gone()

                    home_iv_logo_balance?.show()
                    home_container_action_balance?.show()

                    home_tv_balance.show()

                    val balanceText = element.balanceTitleTextAttribute?.text ?: ""

                    home_tv_balance.text = balanceText

                    val reserveBalance = element.balanceSubTitleTextAttribute?.text ?: ""
                    if (reserveBalance.isNotEmpty()) {
                        home_tv_reserve_balance?.visible()
                        home_tv_reserve_balance?.text = reserveBalance
                    } else {
                        home_tv_reserve_balance?.gone()
                    }

                    home_container_balance?.handleItemCLickType(
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

                            },
                            walletAppAction = {
                                OvoWidgetTracking.sendClickOnNewWalletAppBalanceWidgetTracker(
                                        subtitle = element.balanceSubTitleTextAttribute?.text?:"",
                                        userId = listener?.userId?:""
                                )
                                listener?.onSectionItemClicked(element.redirectUrl)
                            }
                    )


                }
                BalanceDrawerItemModel.STATE_ERROR -> {
                    home_progress_bar_balance_layout?.gone()
                    home_container_action_balance?.show()
                    home_container_balance?.handleItemCLickType(
                            element = element,
                            ovoWalletAction = {listener?.onRetryWalletApp()},
                            rewardsAction = {listener?.onRetryMembership()},
                            bboAction = {listener?.onRetryMembership()},
                            tokopointsAction = {listener?.onRetryMembership()},
                            walletAppAction = { listener?.onRetryWalletApp() }
                    )
                }
            }

            if (element?.state != STATE_LOADING) {
                //error state using shimmering
                element?.defaultIconRes?.let {
                    if (element.drawerItemType == TYPE_WALLET_PENDING_CASHBACK ||
                        element.drawerItemType == TYPE_WALLET_WITH_TOPUP ||
                        element.drawerItemType == TYPE_WALLET_OTHER
                    ) {
                        home_iv_logo_balance?.visible()
                        home_iv_logo_shimmering?.invisible()
                        home_iv_logo_balance?.setImageDrawable(itemView.context.getDrawable(it))
                    } else {
                        home_iv_logo_balance?.invisible()
                        if (!disableAnimation) {
                            home_iv_logo_shimmering?.visible()
                        } else {
                            home_iv_logo_shimmering?.gone()
                        }
                    }
                }
                element?.iconImageUrl?.let {
                    home_iv_logo_balance?.visible()
                    home_iv_logo_shimmering?.invisible()

                    if (it.isNotEmpty()) home_iv_logo_balance?.setImageUrl(it)
                }
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
                                             walletPendingAction: () -> Unit= {},
                                             walletAppAction: (isLinked: Boolean) -> Unit = {}
        ) {
            setOnClickListener {
                when (element.drawerItemType) {
                    TYPE_TOKOPOINT -> tokopointsAction.invoke()
                    TYPE_FREE_ONGKIR -> bboAction.invoke()
                    TYPE_COUPON -> couponsAction.invoke()
                    TYPE_REWARDS -> rewardsAction.invoke()
                    TYPE_WALLET_WITH_TOPUP -> walletTopupAction.invoke()
                    TYPE_WALLET_OTHER -> walletOtherAction.invoke()
                    TYPE_WALLET_PENDING_CASHBACK -> walletPendingAction.invoke()
                    TYPE_WALLET_APP_LINKED -> walletAppAction.invoke(true)
                    TYPE_WALLET_APP_NOT_LINKED -> walletAppAction.invoke(false)
                }
            }
        }

        companion object {
            private const val TITLE_HEADER_WEBSITE = "Tokopedia"
            private const val KUPON_SAYA_URL_PATH = "kupon-saya"
            private const val DIRECTION_UP = 0
            private const val DIRECTION_DOWN = 1
            private const val DRAWER_DELAY_ANIMATION = 1000L
        }
    }
}