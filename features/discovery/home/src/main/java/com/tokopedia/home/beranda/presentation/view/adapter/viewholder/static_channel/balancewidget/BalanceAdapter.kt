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
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
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
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_OVO
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_PENDING_CASHBACK
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceDrawerItemModel.Companion.TYPE_WALLET_WITH_TOPUP
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceTagAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.BalanceTextAttribute
import com.tokopedia.home.beranda.presentation.view.adapter.datamodel.balance.HomeBalanceModel
import com.tokopedia.home.beranda.presentation.view.helper.isHexColor
import com.tokopedia.home.util.HomeServerLogger
import com.tokopedia.home.util.HomeServerLogger.TYPE_ERROR_SUBMIT_WALLET
import com.tokopedia.home_component.util.invertIfDarkMode
import com.tokopedia.home_component.util.toDpInt
import com.tokopedia.home_component.util.toSp
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.searchbar.helper.Ease
import com.tokopedia.searchbar.helper.EasingInterpolator
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifyprinciples.Typography
import kotlinx.coroutines.*
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
        private var home_tv_btn_action_balance: TextView = itemView.findViewById(R.id.home_tv_btn_action_balance)
        private var home_iv_logo_balance: ImageView? = itemView.findViewById<ImageView>(R.id.home_iv_logo_balance)
        private var home_tv_balance: TextView = itemView.findViewById(R.id.home_tv_balance)
        private var home_container_action_balance: ConstraintLayout? = itemView.findViewById(R.id.home_container_action_balance)
        private var home_tv_reserve_balance: Typography? = itemView.findViewById(R.id.home_tv_reserve_balance)

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
            home_tv_btn_action_balance?.show()

            animationJob?.cancel()

            when (element?.state) {
                BalanceDrawerItemModel.STATE_LOADING -> {
                    home_iv_logo_balance?.invisible()

                    home_tv_balance?.invisible()
                    home_tv_btn_action_balance?.invisible()

                    if (!disableAnimation) {
                        home_iv_logo_shimmering?.show()
                        home_progress_bar_balance_layout?.show()
                    }
                }
                BalanceDrawerItemModel.STATE_SUCCESS -> {
                    home_progress_bar_balance_layout?.gone()

                    home_iv_logo_balance?.show()
                    home_container_action_balance?.show()

                    home_tv_balance?.show()
                    home_tv_btn_action_balance?.show()

                    renderBalanceText(element?.balanceTitleTextAttribute, element?.balanceTitleTagAttribute, home_tv_balance)
                    renderBalanceText(element?.balanceSubTitleTextAttribute, element?.balanceSubTitleTagAttribute, home_tv_btn_action_balance)

                    if (element.reserveBalance.isNotEmpty()) {
                        home_tv_reserve_balance?.visible()
                        home_tv_reserve_balance?.text = element.reserveBalance
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

                    home_tv_btn_action_balance?.handleItemCLickType(
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
                            },
                            walletAppAction = {
                                OvoWidgetTracking.sendClickOnNewWalletAppBalanceWidgetTracker(
                                        subtitle = element.balanceSubTitleTextAttribute?.text?:"",
                                        userId = listener?.userId?:""
                                )
                                listener?.onSectionItemClicked(element.redirectUrl)
                            }
                    )

                    //interpolator
                    element?.alternateBalanceDrawerItem?.let {
                        this.element = element
                        this.alternateDrawerItem = it
                        if (!disableAnimation) {
                            setDrawerItemWithAnimation()
                        }
                    }
                }
                BalanceDrawerItemModel.STATE_ERROR -> {
                    home_progress_bar_balance_layout?.gone()
                    home_container_action_balance?.show()
                    renderBalanceText(element.balanceTitleTextAttribute, element.balanceTitleTagAttribute, home_tv_balance)
                    renderBalanceText(element.balanceSubTitleTextAttribute, element.balanceSubTitleTagAttribute, home_tv_btn_action_balance)
                    home_container_balance?.handleItemCLickType(
                            element = element,
                            ovoWalletAction = {listener?.onRetryWalletApp()},
                            rewardsAction = {listener?.onRetryMembership()},
                            bboAction = {listener?.onRetryMembership()},
                            tokopointsAction = {listener?.onRetryMembership()},
                            walletAppAction = { listener?.onRetryWalletApp() }
                    )
                    home_tv_btn_action_balance?.handleItemCLickType(
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
                    if (element.drawerItemType == TYPE_WALLET_OVO ||
                        element.drawerItemType == TYPE_WALLET_PENDING_CASHBACK ||
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

                    if (it.isNotEmpty()) home_iv_logo_balance?.loadImage(it)
                }
            }
        }

        fun setDrawerItemWithAnimation() {
            if (listener?.needToRotateTokopoints() == true) {
                animationJob?.cancel()
                if (animationJob == null || animationJob?.isActive == false) {
                    animationJob = launch {
                        alternateDrawerItem?.forEach { alternateItem ->
                            delay(DRAWER_DELAY_ANIMATION)
                            renderItemAnimation(alternateItem, slideDirection = DIRECTION_UP)
                            delay(DRAWER_DELAY_ANIMATION)
                        }
                        delay(DRAWER_DELAY_ANIMATION)
                        element?.let {
                            element?.let { renderItemAnimation(it, slideDirection = DIRECTION_DOWN) }
                        }
                    }
                }
                listener?.setRotateTokopointsDone(true)
            }
        }

        private suspend fun renderItemAnimation(item: BalanceDrawerItemModel, slideDirection: Int = DIRECTION_DOWN) {
            var title: BalanceTextAttribute? = null
            var subtitle: BalanceTextAttribute? = null
            var titleTag: BalanceTagAttribute? = null
            var subtitleTag: BalanceTagAttribute? = null
            val slideIn =
                if (slideDirection == DIRECTION_DOWN) AnimationUtils.loadAnimation(itemView.context, R.anim.search_bar_slide_down_in) else
                    AnimationUtils.loadAnimation(itemView.context, R.anim.search_bar_slide_up_in)
            slideIn.interpolator = EasingInterpolator(Ease.QUART_OUT)
            val slideOut =
                if (slideDirection == DIRECTION_DOWN) AnimationUtils.loadAnimation(itemView.context, R.anim.slide_out_down) else
                    AnimationUtils.loadAnimation(itemView.context, R.anim.slide_out_up)
            slideOut.interpolator = EasingInterpolator(Ease.QUART_IN)
            slideOut.setAnimationListener(object : Animation.AnimationListener {
                override fun onAnimationRepeat(animation: Animation?) {}
                override fun onAnimationEnd(animation: Animation?) {
                    title = item.balanceTitleTextAttribute
                    subtitle = item.balanceSubTitleTextAttribute
                    titleTag = item.balanceTitleTagAttribute
                    subtitleTag = item.balanceSubTitleTagAttribute
                    setItemText(title, titleTag, subtitle, subtitleTag)
                    home_container_action_balance?.startAnimation(slideIn)
                }

                override fun onAnimationStart(animation: Animation?) {}
            })
            home_container_action_balance?.startAnimation(slideOut)

            home_container_action_balance?.addOnAttachStateChangeListener(object:
                View.OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View?) {

                }

                override fun onViewDetachedFromWindow(v: View?) {
                    animationJob?.cancel()
                    title = element?.balanceTitleTextAttribute
                    subtitle = element?.balanceSubTitleTextAttribute
                    titleTag = element?.balanceTitleTagAttribute
                    subtitleTag = element?.balanceSubTitleTagAttribute
                    setItemText(title, titleTag, subtitle, subtitleTag)
                    home_container_action_balance?.removeOnAttachStateChangeListener(this)
                }
            })
        }

        private fun setItemText(
            title: BalanceTextAttribute?,
            titleTag: BalanceTagAttribute?,
            subtitle: BalanceTextAttribute?,
            subtitleTag: BalanceTagAttribute?
        ) {
            renderBalanceText(
                textAttr = title,
                textView = home_tv_balance,
                tagAttr = titleTag
            )
            renderBalanceText(
                textAttr = subtitle,
                textView = home_tv_btn_action_balance,
                tagAttr = subtitleTag
            )
        }

        private fun renderBalanceText(textAttr: BalanceTextAttribute?, tagAttr: BalanceTagAttribute?, textView: TextView, textSize: Int = R.dimen.home_balance_default_text_size) {
            textView.setTypeface(null, Typeface.NORMAL)

            textView.background = null
            textView.text = null
            textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, itemView.context.resources.getDimension(textSize))
            if (tagAttr != null && tagAttr.text.isNotEmpty()) {
                renderTagAttribute(tagAttr, textView)
            } else if (textAttr != null && textAttr.text.isNotEmpty()) {
                renderTextAttribute(textAttr, textView)
            } else if ((tagAttr == null && textAttr == null) || (tagAttr != null && tagAttr.text.isEmpty()) || (textAttr != null && textAttr.text.isEmpty())) {
                textView.gone()
            }
        }

        private fun renderTagAttribute(tagAttr: BalanceTagAttribute, textView: TextView) {
            if (tagAttr.backgroundColour.isNotEmpty() && tagAttr.backgroundColour.isHexColor()) {
                val drawable = ContextCompat.getDrawable(itemView.context, R.drawable.bg_tokopoints_rounded)
                (drawable as GradientDrawable?)?.let {
                    it.setColorFilter(Color.parseColor(tagAttr.backgroundColour), PorterDuff.Mode.SRC_ATOP)
                    textView.background = it
                    val horizontalPadding = 2f.toDpInt()
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_PX, 8f.toSp())
                    textView.setTypeface(null, Typeface.NORMAL)
                    textView.setPadding(horizontalPadding, 0, horizontalPadding, 0)
                }
                textView.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N0))
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
                                             walletPendingAction: () -> Unit= {},
                                             walletAppAction: (isLinked: Boolean) -> Unit = {}
        ) {
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