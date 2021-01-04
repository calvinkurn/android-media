package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.content.Context
import android.content.SharedPreferences
import android.graphics.Typeface.BOLD
import android.text.Spannable
import android.text.style.ForegroundColorSpan
import android.text.style.StyleSpan
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalSellerapp
import com.tokopedia.homenav.R
import com.tokopedia.homenav.common.util.animateProfileBadge
import com.tokopedia.homenav.common.util.animateProfileName
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.iconunify.getIconUnifyDrawable
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.coroutines.*
import java.util.*

class AccountHeaderViewHolder(itemView: View,
                              private val mainNavListener: MainNavListener,
                              private val userSession: UserSessionInterface
): AbstractViewHolder<AccountHeaderViewModel>(itemView), CoroutineScope {


    private val masterJob = SupervisorJob()
    override val coroutineContext = masterJob + Dispatchers.Main

    private lateinit var layoutNonLogin: ConstraintLayout
    private lateinit var layoutLogin: ConstraintLayout
    private lateinit var layoutLoginAs: ConstraintLayout

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_account_header
        const val TEXT_LOGIN_AS = "Masuk Sebagai %s"
        const val TEXT_TOKO_SAYA = "Toko saya:  %s"
        private const val GREETINGS_0_2 = "Selamat tidur~"
        private const val GREETINGS_3_4 =  "Lagi begadang? Kangen, ya?"
        private const val GREETINGS_5_9 =  "Selamat pagi! Semongko!"
        private const val GREETINGS_10_14 =  "Udah makan siang?"
        private const val GREETINGS_15_17 = "Selamat sore! Ngopi, kuy?"
        private const val GREETINGS_18_23 = "Jangan lupa makan malam~"
        private const val GREETINGS_DEFAULT = "Hai Toppers"

        private const val ANIMATION_DURATION_MS: Long = 300
    }

    override fun bind(element: AccountHeaderViewModel, payloads: MutableList<Any>) {
        bind(element)
    }

    override fun bind(element: AccountHeaderViewModel) {
        initViewHolder()
        when(element.loginState) {
            AccountHeaderViewModel.LOGIN_STATE_LOGIN -> renderLoginState(element)
            AccountHeaderViewModel.LOGIN_STATE_LOGIN_AS -> renderLoginAs()
            else -> renderNonLoginState()
        }
    }

    private fun initViewHolder() {
        layoutLogin = itemView.findViewById(R.id.layout_login)
        layoutNonLogin = itemView.findViewById(R.id.layout_nonlogin)
        layoutLoginAs = itemView.findViewById(R.id.layout_login_as)

        layoutNonLogin.visibility = View.GONE
        layoutLoginAs.visibility = View.GONE
        layoutLogin.visibility = View.GONE
    }

    private fun renderLoginState(element: AccountHeaderViewModel) {
        layoutLogin.visibility = View.VISIBLE
        val userImage: ImageUnify = layoutLogin.findViewById(R.id.img_user_login)
        val usrBadge: ImageUnify = layoutLogin.findViewById(R.id.usr_badge)
        val usrOvoBadge: ImageUnify = layoutLogin.findViewById(R.id.usr_ovo_badge)
        val btnSettings: ImageView = layoutLogin.findViewById(R.id.btn_settings)
        val btnTryAgain: ImageView = layoutLogin.findViewById(R.id.btn_try_again)
        val tvName: Typography = layoutLogin.findViewById(R.id.tv_name)
        val tvOvo: Typography = layoutLogin.findViewById(R.id.tv_ovo)
        val tvOvoShimmer: View = layoutLogin.findViewById(R.id.tv_ovo_shimmer)
        val usrOvoBadgeShimmer: View = layoutLogin.findViewById(R.id.usr_ovo_badge_shimmer)
        val tvShopInfo: Typography = layoutLogin.findViewById(R.id.usr_shop_info)
        val tvShopNotif: Typography = layoutLogin.findViewById(R.id.usr_shop_notif)
        val shimmerShopInfo: LoaderUnify = layoutLogin.findViewById(R.id.shimmer_shop_info)
        val btnTryAgainShopInfo: ImageView = layoutLogin.findViewById(R.id.btn_try_again_shop_info)

        btnTryAgain.setOnClickListener{mainNavListener.onErrorProfileRefreshClicked(adapterPosition)}
        btnTryAgain.setImageDrawable(
                getIconUnifyDrawable(
                        itemView.context,
                        IconUnify.REPLAY,
                        ContextCompat.getColor(itemView.context, R.color.Unify_Y400)
                )
        )

        btnTryAgainShopInfo.setOnClickListener{mainNavListener.onErrorShopInfoRefreshClicked(adapterPosition)}
        btnTryAgainShopInfo.setImageDrawable(
                getIconUnifyDrawable(
                        itemView.context,
                        IconUnify.REPLAY,
                        ContextCompat.getColor(itemView.context, R.color.Unify_Y400)
                )
        )

        btnTryAgainShopInfo.gone()
        btnTryAgain.gone()
        shimmerShopInfo.gone()

        userImage.loadImageCircle(element.userImage)
        userImage.isClickable = false
        tvName.isClickable = false
        if (element.isGetUserNameError) {
            tvName.text = MethodChecker.fromHtml(AccountHeaderViewModel.ERROR_TEXT_PROFILE)
        } else {
            configureNameAndBadgeSwitcher(tvName, getCurrentGreetings(), element.userName, usrBadge, getCurrentGreetingsIconStringUrl(), element.badge)
        }
        if (element.isGetUserMembershipError) {
            usrBadge.gone()
        }

        tvOvo.isClickable = false
        usrOvoBadge.visible()
        if (element.isCacheData) {
            tvOvoShimmer.visible()
            usrOvoBadgeShimmer.visible()
            tvOvo.gone()
            usrOvoBadge.gone()
        } else {
            tvOvoShimmer.gone()
            usrOvoBadgeShimmer.gone()
            tvOvo.visible()
            usrOvoBadge.visible()
            if (element.isGetOvoError && element.isGetSaldoError) {
                tvOvo.text = AccountHeaderViewModel.ERROR_TEXT_OVO
                usrOvoBadge.setImageResource(R.drawable.ic_nav_ovo)
            } else if (element.isGetOvoError && !element.isGetSaldoError) {
                tvOvo.text = element.saldo
                usrOvoBadge.setImageResource(R.drawable.ic_saldo)
            } else {
                tvOvo.text = renderOvoText(element.ovoSaldo, element.ovoPoint, element.saldo)
                if (element.ovoSaldo.isNotEmpty()) {
                    usrOvoBadge.setImageResource(R.drawable.ic_nav_ovo)
                } else if (element.saldo.isNotEmpty()) {
                    usrOvoBadge.setImageResource(R.drawable.ic_saldo)
                }

            }
        }

        //shop info error state
        if (!element.isGetShopError && element.shopName.isNotEmpty()) {
            tvShopInfo.visible()
            var subtext = ""
            var fulltext = ""
            if (element.isGetShopError) {
                subtext = MethodChecker.fromHtml(AccountHeaderViewModel.ERROR_TEXT_SHOP_TRY).toString()
                fulltext = String.format(AccountHeaderViewModel.ERROR_TEXT_SHOP, subtext)
            } else {
                subtext = MethodChecker.fromHtml(element.shopName).toString()
                fulltext = String.format(TEXT_TOKO_SAYA, subtext)
                tvShopInfo.setOnClickListener { onShopClicked() }
            }

            tvShopInfo.setText(fulltext, TextView.BufferType.SPANNABLE)
            val str = tvShopInfo.text as Spannable
            val i = fulltext.indexOf(subtext)
            str.setSpan(ForegroundColorSpan(itemView.context.getResColor(R.color.green_shop)), i, i + subtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
            str.setSpan(StyleSpan(BOLD), i, i + subtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
        } else if (element.isGetShopLoading) {
            tvShopInfo.gone()
            btnTryAgainShopInfo.gone()
            shimmerShopInfo.visible()
        } else if (element.isGetShopError) {
            btnTryAgainShopInfo.visible()
            tvShopInfo.visible()
            shimmerShopInfo.gone()

            tvShopInfo.text = getString(R.string.error_state_shop_info)
        }

        layoutLogin.setOnClickListener {
            TrackingProfileSection.onClickProfileSection(userSession.userId)
            mainNavListener.onProfileSectionClicked()
        }

        btnSettings.visible()
        btnTryAgain.gone()
        if (element.isGetUserNameError || (element.isGetOvoError && element.isGetSaldoError && !element.isCacheData)) {
            usrOvoBadge.gone()
            btnTryAgain.visible()
        }
    }

    private fun renderOvoText(ovoString: String, pointString: String, saldoString: String): String {
        return if (ovoString.isNotEmpty()) {
            itemView.context.getString(R.string.text_ovo_saldo, ovoString, pointString)
        } else {
            saldoString
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

    private fun renderLoginAs() {
        layoutLoginAs.visibility = View.VISIBLE
        val imgUserLoginAs: ImageView = layoutLoginAs.findViewById(R.id.img_user_login_as)
        val btnLoginAs: UnifyButton = layoutLoginAs.findViewById(R.id.btn_login_as)
        val name = getSharedPreference().getString(AccountHeaderViewModel.KEY_USER_NAME, "") ?: ""
        val profilePic = getSharedPreference().getString(AccountHeaderViewModel.KEY_PROFILE_PICTURE, "") ?: ""
        imgUserLoginAs.loadImageCircle(profilePic)
        val nameTrimmed = name.split(" ")
        btnLoginAs.text = String.format(TEXT_LOGIN_AS, nameTrimmed[0])

        btnLoginAs.setOnClickListener {
            TrackingProfileSection.onClickLoginReminderButton("")
            mainNavListener.onProfileLoginClicked()
        }
    }

    private fun getSharedPreference(): SharedPreferences {
        return itemView.context.getSharedPreferences(AccountHeaderViewModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }

    private fun getCurrentGreetings() : String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..2 -> GREETINGS_0_2
            in 3..4 -> GREETINGS_3_4
            in 5..9 -> GREETINGS_5_9
            in 10..14 -> GREETINGS_10_14
            in 15..17 -> GREETINGS_15_17
            in 18..23 -> GREETINGS_18_23
            else -> GREETINGS_DEFAULT
        }
    }

    private fun getCurrentGreetingsIconStringUrl() : String {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..2 -> "https://ecs7.tokopedia.net/home-img/greet-sleep.png"
            in 3..4 -> "https://ecs7.tokopedia.net/home-img/greet-confused.png"
            in 5..9 -> "https://ecs7.tokopedia.net/home-img/greet-cloud-sun.png"
            in 10..14 -> "https://ecs7.tokopedia.net/home-img/greet-sun.png"
            in 15..17 -> "https://ecs7.tokopedia.net/home-img/greet-dusk.png"
            in 18..23 -> "https://ecs7.tokopedia.net/home-img/greet-moon.png"
            else -> "https://ecs7.tokopedia.net/home-img/greet-sun.png"
        }
    }

    private fun onShopClicked() {
        TrackingProfileSection.onClickShopProfileSection(userSession.userId)
        RouteManager.route(itemView.context, ApplinkConstInternalSellerapp.SELLER_MENU)
    }

    private var needToSwitchText: Boolean = isFirstTimeUserSeeNameAnimationOnSession()

    private fun configureNameAndBadgeSwitcher(tvName: Typography, greetingString: String, nameString: String, ivBadge: ImageView, badgeGreetingsUrl: String, badgeUrl: String) {
        if (needToSwitchText) {
            tvName.text = greetingString
            ivBadge.loadImage(badgeGreetingsUrl)
            launch {
                delay(1000)
                tvName.animateProfileName(nameString, ANIMATION_DURATION_MS)
                ivBadge.animateProfileBadge(badgeUrl, ANIMATION_DURATION_MS)
                setFirstTimeUserSeeNameAnimationOnSession(false)
            }
        } else {
            tvName.text = nameString
            ivBadge.loadImage(badgeUrl)
        }
    }

    private fun isFirstTimeUserSeeNameAnimationOnSession() = MainNavConst.MainNavState.runAnimation


    private fun setFirstTimeUserSeeNameAnimationOnSession(value: Boolean) {
        MainNavConst.MainNavState.runAnimation = value
    }

    private fun setColor(view: TextView, fulltext: String, subtext: String, color: Int) {
        view.setText(fulltext, TextView.BufferType.SPANNABLE)
        val str = view.text as Spannable
        val i = fulltext.indexOf(subtext)
        str.setSpan(ForegroundColorSpan(color), i, i + subtext.length, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE)
    }
}