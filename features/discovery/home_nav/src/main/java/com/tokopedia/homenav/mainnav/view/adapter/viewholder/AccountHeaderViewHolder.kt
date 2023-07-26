package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.applink.ApplinkConst
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
import com.tokopedia.homenav.mainnav.view.datamodel.account.TokopediaPlusDataModel
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.usercomponents.tokopediaplus.common.TokopediaPlusListener
import com.tokopedia.usercomponents.tokopediaplus.ui.TokopediaPlusWidget
import kotlinx.coroutines.*
import java.util.*

class AccountHeaderViewHolder(itemView: View,
                              private val mainNavListener: MainNavListener,
                              private val userSession: UserSessionInterface,
                              private val tokopediaPlusListener: TokopediaPlusListener
): AbstractViewHolder<AccountHeaderDataModel>(itemView), CoroutineScope {


    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main

    private lateinit var layoutNonLogin: ConstraintLayout
    private lateinit var layoutLoginHeader: ConstraintLayout
    private lateinit var layoutLogin: ConstraintLayout
    private var adapter: SellerAdapter? = null

    private val tokopediaPlusWidget by lazy { layoutLogin.findViewById<TokopediaPlusWidget>(R.id.tokopedia_plus_widget) }


    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_account_header
        private const val GREETINGS_5_10 = "Pagi! Udah sarapan?"
        private const val GREETINGS_10_16 =  "Semangat jalani hari ini!"
        private const val GREETINGS_16_21 =  "Nyantai sambil belanja, yuk~"
        private const val GREETINGS_22_5 =  " Lagi cari apa nih kamu?"

        private const val HOURS_5 = 5
        private const val HOURS_9 = 9
        private const val HOURS_10 = 10
        private const val HOURS_15 = 15
        private const val HOURS_16 = 16
        private const val HOURS_21 = 21

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
        bindViews(element)
    }

    private fun bindViews(element: AccountHeaderDataModel){
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
        val usrEmoji: Typography = layoutLoginHeader.findViewById(R.id.usr_emoji)
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
            mainNavListener.onProfileSectionClicked(TrackingProfileSection.CLICK_USER_ACCOUNT, ApplinkConst.ACCOUNT)
        }

        /**
         * Reset button state for error handling
         */
        btnTryAgain.gone()

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
                    usrEmoji,
                    getCurrentGreetingsIconStringEmoji(),
                    element.profileMembershipDataModel.badge,
                    usrBadge
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
                            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_96)), 0, profileMembership.tokopointExternalAmount.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
                            span.setSpan(ForegroundColorSpan(ContextCompat.getColor(itemView.context, com.tokopedia.unifyprinciples.R.color.Unify_NN950_68)), profileMembership.tokopointExternalAmount.length + 1, spanText.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
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
         * Handling tokopedia plus
         */
        setTokopediaPlus(element.tokopediaPlusDataModel)
    }

    private fun valuateRecyclerViewDecoration(recyclerSeller: RecyclerView) {
        if (recyclerSeller.itemDecorationCount == 0) recyclerSeller.addItemDecoration(
            SellerSpacingDecoration()
        )
        val layoutManager = GridLayoutManager(itemView.context, TOTAL_GRID_SELLER)
        recyclerSeller.layoutManager = layoutManager
    }

    private fun setPositionSellerAndAffiliate(
        element: AccountHeaderDataModel,
        listSellers: MutableList<Visitable<*>>
    ) {
        if (element.profileAffiliateDataModel.isRegister && !element.profileSellerDataModel.hasShop && !element.profileSellerDataModel.isGetShopError) {
            listSellers.add(element.profileAffiliateDataModel)
            listSellers.add(element.profileSellerDataModel)
        } else {
            listSellers.add(element.profileSellerDataModel)
            listSellers.add(element.profileAffiliateDataModel)
        }
    }

    private fun setSellerAndAffiliate(element: AccountHeaderDataModel, recyclerSeller: RecyclerView) {
        val listSellers = mutableListOf<Visitable<*>>()
        setPositionSellerAndAffiliate(element, listSellers)
        valuateRecyclerViewDecoration(recyclerSeller)
        val typeFactoryImpl = SellerTypeFactoryImpl(mainNavListener)
        adapter = SellerAdapter(listSellers, typeFactoryImpl)
        recyclerSeller.adapter = adapter
    }

    private fun setTokopediaPlus(tokopediaPlusDataModel: TokopediaPlusDataModel){
        tokopediaPlusWidget.listener = tokopediaPlusListener
        tokopediaPlusDataModel.let {
            if(it.isGetTokopediaPlusLoading){
                tokopediaPlusWidget.showLoading()
            } else {
                it.tokopediaPlusError?.let { error ->
                    tokopediaPlusWidget.onError(error)
                } ?: it.tokopediaPlusParam?.let { param ->
                    tokopediaPlusWidget.setContent(param)
                }
            }
        }
    }

    private fun renderNonLoginState() {
        layoutNonLogin.visibility = View.VISIBLE
        val btnLogin: UnifyButton = layoutNonLogin.findViewById(R.id.btn_login)
        val btnRegister: UnifyButton = layoutNonLogin.findViewById(R.id.btn_register)

        btnLogin.setOnClickListener {
            mainNavListener.onProfileLoginClicked()
        }

        btnRegister.setOnClickListener {
            mainNavListener.onProfileRegisterClicked()
        }
    }

    private fun getCurrentGreetings() : String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in HOURS_5..HOURS_9 -> GREETINGS_5_10
            in HOURS_10..HOURS_15 -> GREETINGS_10_16
            in HOURS_16..HOURS_21 -> GREETINGS_16_21
            else -> GREETINGS_22_5
        }
    }

    private fun getCurrentGreetingsIconStringEmoji() : String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in HOURS_5..HOURS_9 -> "\uD83C\uDF5E"
            in HOURS_10..HOURS_15 -> "\uD83D\uDE4C"
            in HOURS_16..HOURS_21 -> "\uD83D\uDECD️"
            else -> "\uD83E\uDD14"
        }
    }

    private var needToSwitchText: Boolean = isFirstTimeUserSeeNameAnimationOnSession()

    private fun configureNameAndBadgeSwitcher(
        tvName: Typography,
        greetingString: String,
        nameString: String,
        tvBadge: Typography,
        badgeEmojiString: String,
        badgeUrl: String,
        imgBadge: ImageUnify
    ) {
        if (needToSwitchText) {
            tvName.text = greetingString
            tvBadge.visible()
            imgBadge.invisible()
            tvBadge.text = badgeEmojiString
            launch {
                delay(GREETINGS_DELAY)
                tvBadge.invisible()
                imgBadge.visible()
                tvName.animateProfileName(nameString, ANIMATION_DURATION_MS)
                imgBadge.animateProfileBadge(badgeUrl, ANIMATION_DURATION_MS)
                setFirstTimeUserSeeNameAnimationOnSession(false)
            }
        } else {
            tvName.text = nameString
            tvBadge.invisible()
            imgBadge.visible()
            imgBadge.loadImage(badgeUrl)
        }
    }

    private fun isFirstTimeUserSeeNameAnimationOnSession() = MainNavConst.MainNavState.runAnimation


    private fun setFirstTimeUserSeeNameAnimationOnSession(value: Boolean) {
        MainNavConst.MainNavState.runAnimation = value
    }
}
