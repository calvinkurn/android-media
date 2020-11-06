package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.content.Context
import android.content.SharedPreferences
import android.text.Spanned
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.homenav.R
import com.tokopedia.homenav.common.util.animateProfileName
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
import com.tokopedia.homenav.mainnav.view.fragment.MainNavFragment
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
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
        const val TEXT_MY_SHOP = "Toko saya: %s"
        const val TEXT_DOT = "\\u2022"
        const val TEXT_POINTS = " Points"
        private const val NAV_SHARED_NOTIFICATION_KEY_PREF = "NAV_SHARED_NOTIFICATION_KEY_PREF"
        private const val NAV_SHARED_NOTIFICATION_KEY_FIRST_OPEN = "NAV_SHARED_NOTIFICATION_KEY_FIRST_OPEN"
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
        val userImage: AppCompatImageView = layoutLogin.findViewById(R.id.img_user_login)
        val usrBadge: AppCompatImageView = layoutLogin.findViewById(R.id.usr_badge)
        val usrOvoBadge: AppCompatImageView = layoutLogin.findViewById(R.id.usr_ovo_badge)
        val btnSettings: ImageView = layoutLogin.findViewById(R.id.btn_settings)
        val tvName: Typography = layoutLogin.findViewById(R.id.tv_name)
        val tvOvo: Typography = layoutLogin.findViewById(R.id.tv_ovo)
        val tvShopInfo: Typography = layoutLogin.findViewById(R.id.usr_shop_info)
        val tvShopNotif: Typography = layoutLogin.findViewById(R.id.usr_shop_notif)

//        element.userName = AccountHeaderViewModel.ERROR_TEXT
//        element.ovoSaldo = ""
//        element.ovoPoint = ""
//        element.shopName = AccountHeaderViewModel.ERROR_TEXT
        userImage.loadImageCircle(element.userImage)

        if (element.userName.equals(AccountHeaderViewModel.ERROR_TEXT)) {
            renderProfileLoginSection(
                    MethodChecker.fromHtml(AccountHeaderViewModel.ERROR_TEXT),
                    tvName,
                    View.OnClickListener { mainNavListener.onErrorProfileNameClicked(element) }
            )
        } else {
            configureNameSwitcher(tvName, getCurrentGreetings(element.userName), element.userName)
        }
        renderProfileLoginSection(
                MethodChecker.fromHtml(
                        if (element.ovoSaldo.equals(AccountHeaderViewModel.ERROR_TEXT)) element.ovoSaldo
                        else renderOvoText(element.ovoSaldo, element.ovoPoint, element.saldo)),
                tvOvo,
                if (element.ovoSaldo.equals(AccountHeaderViewModel.ERROR_TEXT))
                    View.OnClickListener { mainNavListener.onErrorProfileOVOClicked(element) }
                else null)
        if (element.shopName.isNotEmpty()) {
            tvShopInfo.visibility = View.VISIBLE
            renderProfileLoginSection(
                    MethodChecker.fromHtml(
                            if (element.shopName.equals(AccountHeaderViewModel.ERROR_TEXT)) element.shopName
                            else itemView.context.getString(R.string.account_home_shop_name_card, element.shopName)),
                    tvShopInfo,
                    if (element.shopName.equals(AccountHeaderViewModel.ERROR_TEXT))
                        View.OnClickListener { mainNavListener.onErrorProfileOVOClicked(element) }
                    else null)
        }

        usrBadge.loadImage(element.badge)
        if (element.ovoSaldo.isNotEmpty()) {
            usrOvoBadge.loadImageDrawable(R.drawable.ic_nav_ovo)
        } else if (element.saldo.isNotEmpty()) {
            usrOvoBadge.loadImageDrawable(R.drawable.ic_nav_saldo)
        }
    }

    private fun renderProfileLoginSection(text: Spanned, typography: Typography, clickListener : View.OnClickListener?) {
        typography.setOnClickListener(clickListener)
        typography.text = text
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
        btnLoginAs.text = String.format(TEXT_LOGIN_AS, name)

        btnLoginAs.setOnClickListener {
            TrackingProfileSection.onClickLoginReminderButton("")
            mainNavListener.onProfileLoginClicked()
        }
    }

    private fun getSharedPreference(): SharedPreferences {
        return itemView.context.getSharedPreferences(AccountHeaderViewModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }

    private fun getCurrentGreetings(default: String) : String {
        val hourTime = Calendar.getInstance().get(Calendar.HOUR_OF_DAY)
        return when (hourTime) {
            in 0..2 -> "Selamat tidur~"
            in 3..4 -> "Lagi begadang? Kangen, ya?"
            in 5..9 -> "Selamat pagi! Semongko!"
            in 10..14 -> "Udah makan siang?"
            in 15..17 -> "Selamat sore! Ngopi, kuy?"
            in 18..23 -> "Jangan lupa makan malam~"
            else -> default
        }
    }

    private var needToSwitchText: Boolean = isFirstTimeUserSeeNameAnimationOnSession()

    private fun configureNameSwitcher(tvName: Typography, greetingString: String, nameString: String) {
        if (needToSwitchText) {
            tvName.text = greetingString
            launch {
                delay(1000)
                tvName.animateProfileName(nameString, 300)
                setFirstTimeUserSeeNameAnimationOnSession(false)
            }
        } else {
            tvName.text = nameString
        }
    }

    fun isFirstTimeUserSeeNameAnimationOnSession(): Boolean {
        return MainNavConst.MainNavState.runAnimation
    }

    fun setFirstTimeUserSeeNameAnimationOnSession(value: Boolean) {
        MainNavConst.MainNavState.runAnimation = false
    }
}