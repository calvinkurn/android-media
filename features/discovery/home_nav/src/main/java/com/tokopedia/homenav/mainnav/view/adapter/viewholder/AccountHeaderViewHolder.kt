package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.content.Context
import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.seller.SellerAdapter
import com.tokopedia.homenav.common.util.animateProfileBadge
import com.tokopedia.homenav.common.util.animateProfileName
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.view.adapter.typefactory.SellerTypeFactoryImpl
import com.tokopedia.homenav.mainnav.view.adapter.viewholder.seller.SellerSpacingDecoration
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_LOADING
import com.tokopedia.homenav.mainnav.view.datamodel.account.AccountHeaderDataModel.Companion.NAV_PROFILE_STATE_SUCCESS
import com.tokopedia.homenav.mainnav.view.datamodel.account.ProfileSellerDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.sessioncommon.view.admin.dialog.LocationAdminDialog
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.NotificationUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import java.util.*

class AccountHeaderViewHolder(itemView: View,
                              private val mainNavListener: MainNavListener,
                              private val userSession: UserSessionInterface
): AbstractViewHolder<AccountHeaderDataModel>(itemView), CoroutineScope {


    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main

    private lateinit var layoutNonLogin: ConstraintLayout
    private lateinit var layoutLoginHeader: ConstraintLayout
    private lateinit var layoutLogin: ConstraintLayout
    private var adapter: SellerAdapter? = null

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_account_header
        const val TEXT_LOGIN_AS = "Masuk Sebagai %s"
        private const val GREETINGS_0_2 = "Selamat tidur~"
        private const val GREETINGS_3_4 =  "Lagi begadang? Kangen, ya?"
        private const val GREETINGS_5_9 =  "Selamat pagi! Semongko!"
        private const val GREETINGS_10_14 =  "Udah makan siang?"
        private const val GREETINGS_15_17 = "Selamat sore! Ngopi, kuy?"
        private const val GREETINGS_18_23 = "Jangan lupa makan malam~"
        private const val GREETINGS_DEFAULT = "Hai Toppers"

        private const val HOURS_0 = 0
        private const val HOURS_2 = 2
        private const val HOURS_3 = 3
        private const val HOURS_4 = 4
        private const val HOURS_5 = 5
        private const val HOURS_9 = 9
        private const val HOURS_10 = 10
        private const val HOURS_14 = 14
        private const val HOURS_15 = 15
        private const val HOURS_17 = 17
        private const val HOURS_18 = 18
        private const val HOURS_23 = 23

        private const val ANIMATION_DURATION_MS: Long = 300
        private const val GREETINGS_DELAY = 1000L

        private const val DEFAULT_BALANCE_VALUE = "Rp0"
        private const val DEFAULT_BALANCE_POINTS_VALUE = "0 Coins"
        private const val TOTAL_GRID_SELLER = 2
    }

    override fun bind(element: AccountHeaderDataModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(element: AccountHeaderDataModel) {
        initViewHolder()

        val sectionShimmering: View = itemView.findViewById(R.id.section_shimmering_profile)

        when(element.state) {
            NAV_PROFILE_STATE_LOADING -> {
                sectionShimmering.visible()
            }
            NAV_PROFILE_STATE_SUCCESS -> {
                sectionShimmering.gone()

                when(element.loginState) {
                    AccountHeaderDataModel.LOGIN_STATE_LOGIN -> renderLoginState(element)
                    else -> renderNonLoginState()
                }
            }
        }
    }

    private fun initViewHolder() {
        layoutLoginHeader = itemView.findViewById(R.id.layout_login_header)
        layoutLogin = itemView.findViewById(R.id.layout_login)
        layoutNonLogin = itemView.findViewById(R.id.layout_nonlogin)
        layoutNonLogin.visibility = View.GONE
        layoutLogin.visibility = View.GONE
    }

    private fun renderLoginState(element: AccountHeaderDataModel) {
        layoutLogin.visibility = View.VISIBLE
        val userImage: ImageUnify = layoutLoginHeader.findViewById(R.id.img_user_login)
        val usrBadge: ImageUnify = layoutLoginHeader.findViewById(R.id.usr_badge)
        val usrOvoBadge: ImageUnify = layoutLoginHeader.findViewById(R.id.usr_ovo_badge)
        val btnSettings: IconUnify = layoutLoginHeader.findViewById(R.id.btn_settings)
        val btnTryAgain: CardView = layoutLoginHeader.findViewById(R.id.btn_try_again)
        val usrSaldoBadge: IconUnify = layoutLoginHeader.findViewById(R.id.usr_saldo_badge)
        val tvName: Typography = layoutLoginHeader.findViewById(R.id.tv_name)
        val tvOvo: Typography = layoutLoginHeader.findViewById(R.id.tv_ovo)
        val tvSaldo: Typography = layoutLoginHeader.findViewById(R.id.tv_saldo)
        val usrSaldoBadgeShimmer: View = layoutLoginHeader.findViewById(R.id.usr_saldo_badge_shimmer)
        val usrOvoBadgeShimmer: View = layoutLoginHeader.findViewById(R.id.usr_ovo_badge_shimmer)
        //shop
        val tvShopInfo: Typography = layoutLogin.findViewById(R.id.usr_shop_info)
        val tvShopTitle: Typography = layoutLogin.findViewById(R.id.usr_shop_title)
        val tvShopNotif: NotificationUnify = layoutLogin.findViewById(R.id.usr_shop_notif)
        val shimmerShopInfo: LoaderUnify = layoutLogin.findViewById(R.id.shimmer_shop_info)
        val btnTryAgainShopInfo: CardView = layoutLogin.findViewById(R.id.btn_try_again_shop_info)
        val shimmerTryAgainShopInfo: LoaderUnify = layoutLogin.findViewById(R.id.shimmer_btn_try_again)
        val arrowRight : IconUnify = layoutLogin.findViewById(R.id.image_arrow_right)
        val containerShop : ConstraintLayout = layoutLogin.findViewById(R.id.container_shop)
        val recyclerSeller : RecyclerView = layoutLogin.findViewById(R.id.recycler_seller)

        val sectionSaldo: View = layoutLoginHeader.findViewById(R.id.section_header_saldo)
        val sectionWallet: View = layoutLoginHeader.findViewById(R.id.section_header_wallet)

        /**
         * Initial state
         */
        tvOvo.isClickable = false
        usrOvoBadge.visible()
        usrSaldoBadge.visible()
        sectionWallet.visible()
        btnSettings.visible()

        layoutLoginHeader.setOnClickListener {
            TrackingProfileSection.onClickProfileSection(userSession.userId)
            mainNavListener.onProfileSectionClicked()
        }

        /**
         * Reset button state for error handling
         */
        btnTryAgainShopInfo.gone()
        btnTryAgain.gone()
        shimmerShopInfo.gone()
        shimmerTryAgainShopInfo.gone()

        if (
            !element.isCacheData && (element.profileDataModel.isGetUserNameError ||
                    element.profileSaldoDataModel.isGetSaldoError ||
                    element.profileWalletAppDataModel.isWalletAppFailed)) {
            usrOvoBadge.gone()
            btnTryAgain.visible()
        }

        /**
         * Button for error handling
         */
        btnTryAgain.setOnClickListener{mainNavListener.onErrorProfileRefreshClicked(adapterPosition)}

        btnTryAgainShopInfo.setOnClickListener{mainNavListener.onErrorShopInfoRefreshClicked(adapterPosition)}

        /**
         * Set user profile data
         */
        element.profileDataModel.let { profileData ->
            userImage.setImageUrl(url = profileData.userImage)
            userImage.isClickable = false
            tvName.isClickable = false
            if (profileData.isGetUserNameError) {
                tvName.text = getString(R.string.mainnav_general_error)
            } else {
                configureNameAndBadgeSwitcher(
                    tvName,
                    getCurrentGreetings(),
                    profileData.userName,
                    usrBadge,
                    getCurrentGreetingsIconStringUrl(),
                    element.profileMembershipDataModel.badge
                )
            }
        }

        /**
         * Set saldo data
         */
        element.profileSaldoDataModel.let { profileSaldo ->
            when {
                /**
                 * Handling saldo error state
                 */
                profileSaldo.isGetSaldoError -> {
                    if (element.profileWalletAppDataModel.isWalletAppFailed ||
                            element.profileMembershipDataModel.isTokopointExternalAmountError) {
                        sectionSaldo.visible()
                    } else {
                        tvSaldo.text = itemView.context.getString(R.string.mainnav_general_error)
                        usrSaldoBadge.gone()
                        sectionSaldo.visible()
                    }
                }

                !profileSaldo.isGetSaldoError -> {
                    if (profileSaldo.saldo.isEmpty()) {
                        sectionSaldo.visible()
                    } else {
                        tvSaldo.text = profileSaldo.saldo
                        sectionSaldo.visible()
                    }
                }
            }
        }

        if (element.profileMembershipDataModel.isGetUserMembershipError) {
            usrBadge.gone()
        }

        if (element.isCacheData) {
            usrOvoBadgeShimmer.visible()
            usrSaldoBadgeShimmer.visible()
            tvOvo.invisible()
            usrOvoBadge.invisible()
            usrSaldoBadge.invisible()
        } else {
            /**
             * Remove loading shimmering view
             */
            usrOvoBadgeShimmer.invisible()
            usrSaldoBadgeShimmer.invisible()
            tvOvo.visible()
            usrOvoBadge.visible()

            when {
                /**
                 * User is eligible for wallet app, then render wallet app model
                 */
                element.profileWalletAppDataModel.isEligibleForWalletApp -> {
                    element.profileWalletAppDataModel.let { walletAppModel ->
                        when {
                            walletAppModel.isWalletAppLinked -> {
                                val gopayBalance = if(walletAppModel.gopayBalance.isNotEmpty()) walletAppModel.gopayBalance else DEFAULT_BALANCE_VALUE
                                val gopayPointsBalance = if(walletAppModel.gopayPointsBalance.isNotEmpty()) walletAppModel.gopayPointsBalance else DEFAULT_BALANCE_POINTS_VALUE

                                tvOvo.text = String.format(
                                    itemView.context.getString(R.string.mainnav_wallet_app_format),
                                    gopayBalance,
                                    gopayPointsBalance
                                )
                                usrOvoBadge.loadImage(walletAppModel.walletAppImageUrl)
                            }
                            !walletAppModel.isWalletAppLinked -> {
                                if (walletAppModel.walletAppActivationCta.isNotEmpty()) {
                                    tvOvo.text = walletAppModel.walletAppActivationCta
                                    usrOvoBadge.loadImage(walletAppModel.walletAppImageUrl)
                                } else {
                                    /**
                                     * Handle wallet app error state
                                     */
                                    tvOvo.text = itemView.context.getText(R.string.mainnav_general_error)
                                    usrOvoBadge.gone()
                                    usrOvoBadgeShimmer.gone()
                                    usrSaldoBadgeShimmer.gone()
                                    btnTryAgain.visible()
                                }
                            }
                            walletAppModel.isWalletAppFailed -> {
                                tvOvo.gone()
                                usrOvoBadge.gone()
                            }
                        }
                    }
                }

                !element.profileWalletAppDataModel.isEligibleForWalletApp -> {
                    /**
                     * Handling tokopoint value
                     */
                    if (element.profileMembershipDataModel.tokopointExternalAmount.isNotEmpty()
                        && element.profileMembershipDataModel.tokopointPointAmount.isNotEmpty()) {
                        element.profileMembershipDataModel.let { profileMembership ->
                            val spanText = String.format(
                                itemView.context.getString(R.string.mainnav_tokopoint_format),
                                profileMembership.tokopointExternalAmount,
                                profileMembership.tokopointPointAmount
                            )
                            val span = SpannableString(spanText)
                            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_96)), 0, profileMembership.tokopointExternalAmount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_N700_68)), profileMembership.tokopointExternalAmount.length + 1, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            tvOvo.setText(span, TextView.BufferType.SPANNABLE)
                            usrOvoBadge.setImageUrl(profileMembership.tokopointBadgeUrl)
                        }
                        /**
                         * Handling tokopoint error state
                         */
                    } else if (element.profileMembershipDataModel.tokopointExternalAmount.isEmpty()
                        && element.profileMembershipDataModel.tokopointPointAmount.isEmpty()) {
                        sectionWallet.gone()
                    } else if (element.profileMembershipDataModel.isTokopointExternalAmountError) {
                            tvOvo.text = AccountHeaderDataModel.ERROR_TEXT_TOKOPOINTS
                            usrOvoBadge.clearImage()
                    }
                }
            }
        }

        /**
         * Handling seller and affiliate info value
         */
        setSellerAndAffiliate(element, recyclerSeller)

        /**
         * Handling seller info value
         */
        element.profileSellerDataModel.let { profileSeller ->
            if (profileSeller.isGetShopLoading) {
                tvShopInfo.gone()
                tvShopTitle.gone()
                btnTryAgainShopInfo.gone()
                tvShopNotif.gone()
                arrowRight.gone()
                shimmerShopInfo.visible()
                shimmerTryAgainShopInfo.visible()
            } else if (profileSeller.isGetShopError) {
                btnTryAgainShopInfo.visible()
                tvShopInfo.visible()
                tvShopTitle.gone()
                shimmerShopInfo.gone()
                shimmerTryAgainShopInfo.gone()
                tvShopNotif.gone()
                arrowRight.gone()
                tvShopInfo.text = getString(R.string.error_state_shop_info)
            } else if (!profileSeller.isGetShopError) {
                btnTryAgainShopInfo.gone()
                shimmerShopInfo.gone()
                shimmerTryAgainShopInfo.gone()
                tvShopInfo.visible()
                tvShopTitle.visible()
                tvShopNotif.visible()
                arrowRight.visible()

                val shopTitle: String
                val shopInfo: CharSequence
                if (!profileSeller.hasShop){
                    shopTitle = itemView.context?.getString(R.string.account_header_store_empty_shop).orEmpty()
                    shopInfo = MethodChecker.fromHtml(profileSeller.shopName)
                } else {
                    shopTitle = ""
                    shopInfo = MethodChecker.fromHtml(profileSeller.shopName)
                }
                tvShopTitle.run {
                    visible()
                    text = shopTitle
                }
                tvShopInfo.run {
                    visible()
                    text = shopInfo
                }
                containerShop.setOnClickListener {
                    shopClicked(profileSeller, it.context)
                }
                tvShopInfo.setTextColor(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950))
                tvShopInfo.setWeight(Typography.BOLD)
                if (profileSeller.shopOrderCount > 0) {
                    tvShopNotif.visible()
                    tvShopNotif.setNotification(profileSeller.shopOrderCount.toString(), NotificationUnify.COUNTER_TYPE, NotificationUnify.COLOR_PRIMARY)
                } else {
                    tvShopNotif.gone()
                }
            }
        }
    }

    private fun valuateRecyclerViewDecoration(recyclerSeller: RecyclerView) {
        if (recyclerSeller.itemDecorationCount == 0) recyclerSeller.addItemDecoration(
            SellerSpacingDecoration()
        )
        val layoutManager = GridLayoutManager(itemView.context, TOTAL_GRID_SELLER)
        recyclerSeller.layoutManager = layoutManager
    }

    private fun setSellerAndAffiliate(element: AccountHeaderDataModel, recyclerSeller: RecyclerView) {
        val listSellers = mutableListOf<Visitable<*>>()
        listSellers.add(element.profileSellerDataModel)
        listSellers.add(element.profileAffiliateDataModel)
        valuateRecyclerViewDecoration(recyclerSeller)
        val typeFactoryImpl = SellerTypeFactoryImpl(mainNavListener, userSession)
        adapter = SellerAdapter(listSellers, typeFactoryImpl)
        recyclerSeller.adapter = adapter
    }

    private fun shopClicked(profileSeller: ProfileSellerDataModel, context: Context) {
        if (profileSeller.hasShop)
            onShopClicked(profileSeller.canGoToSellerAccount)
        else {
            RouteManager.route(context, ApplinkConst.CREATE_SHOP)
            TrackingProfileSection.onClickOpenShopSection(mainNavListener.getUserId())
        }
    }

    private fun renderNonLoginState() {
        layoutNonLogin.visibility = View.VISIBLE
        val btnLogin: UnifyButton = layoutNonLogin.findViewById(R.id.btn_login)
        val btnRegister: UnifyButton = layoutNonLogin.findViewById(R.id.btn_register)

        btnLogin.setOnClickListener {
            TrackingProfileSection.onClickLoginButton("")
            mainNavListener.onProfileLoginClicked()
        }

        btnRegister.setOnClickListener {
            TrackingProfileSection.onClickRegisterButton("")
            mainNavListener.onProfileRegisterClicked()
        }
    }

    private fun getCurrentGreetings() : String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in HOURS_0..HOURS_2 -> GREETINGS_0_2
            in HOURS_3..HOURS_4 -> GREETINGS_3_4
            in HOURS_5..HOURS_9 -> GREETINGS_5_9
            in HOURS_10..HOURS_14 -> GREETINGS_10_14
            in HOURS_15..HOURS_17 -> GREETINGS_15_17
            in HOURS_18..HOURS_23 -> GREETINGS_18_23
            else -> GREETINGS_DEFAULT
        }
    }

    private fun getCurrentGreetingsIconStringUrl() : String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in HOURS_0..HOURS_2 -> "https://ecs7.tokopedia.net/home-img/greet-sleep.png"
            in HOURS_3..HOURS_4 -> "https://ecs7.tokopedia.net/home-img/greet-confused.png"
            in HOURS_5..HOURS_9 -> "https://ecs7.tokopedia.net/home-img/greet-cloud-sun.png"
            in HOURS_10..HOURS_14 -> "https://ecs7.tokopedia.net/home-img/greet-sun.png"
            in HOURS_15..HOURS_17 -> "https://ecs7.tokopedia.net/home-img/greet-dusk.png"
            in HOURS_18..HOURS_23 -> "https://ecs7.tokopedia.net/home-img/greet-moon.png"
            else -> "https://ecs7.tokopedia.net/home-img/greet-sun.png"
        }
    }

    private fun onShopClicked(canGoToSellerMenu: Boolean) {
        TrackingProfileSection.onClickShopProfileSection(userSession.userId)
        if (canGoToSellerMenu) {
            RouteManager.route(itemView.context, ApplinkConstInternalSellerapp.SELLER_MENU)
        } else {
            LocationAdminDialog(itemView.context).show()
        }
    }

    private var needToSwitchText: Boolean = isFirstTimeUserSeeNameAnimationOnSession()

    private fun configureNameAndBadgeSwitcher(tvName: Typography, greetingString: String, nameString: String, ivBadge: ImageView, badgeGreetingsUrl: String, badgeUrl: String) {
        if (needToSwitchText) {
            tvName.text = greetingString
            ivBadge.visible()
            ivBadge.loadImage(badgeGreetingsUrl)
            launch {
                delay(GREETINGS_DELAY)
                tvName.animateProfileName(nameString, ANIMATION_DURATION_MS)
                ivBadge.animateProfileBadge(badgeUrl, ANIMATION_DURATION_MS)
                setFirstTimeUserSeeNameAnimationOnSession(false)
            }
        } else {
            tvName.text = nameString
            ivBadge.visible()
            ivBadge.loadImage(badgeUrl)
        }
    }

    private fun isFirstTimeUserSeeNameAnimationOnSession() = MainNavConst.MainNavState.runAnimation


    private fun setFirstTimeUserSeeNameAnimationOnSession(value: Boolean) {
        MainNavConst.MainNavState.runAnimation = value
    }
}