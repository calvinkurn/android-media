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
import com.tokopedia.homenav.common.util.animateProfileBadge
import com.tokopedia.homenav.common.util.animateProfileName
import com.tokopedia.homenav.mainnav.MainNavConst
import com.tokopedia.homenav.mainnav.view.analytics.TrackingProfileSection
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
            configureNameAndBadgeSwitcher(tvName, getCurrentGreetings(), element.userName, usrBadge, getCurrentGreetingsIcon(), element.badge)
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

    private fun getCurrentGreetingsIcon() : Int {
        return when (Calendar.getInstance().get(Calendar.HOUR_OF_DAY)) {
            in 0..2 -> R.drawable.emo_0_2
            in 3..4 -> R.drawable.emo_3_4
            in 5..9 -> R.drawable.emo_5_9
            in 10..14 -> R.drawable.emo_10_14
            in 15..17 -> R.drawable.emo_15_17
            in 18..23 -> R.drawable.emo_18_23
            else -> R.drawable.emo_10_14
        }
    }

    private var needToSwitchText: Boolean = isFirstTimeUserSeeNameAnimationOnSession()

    private fun configureNameAndBadgeSwitcher(tvName: Typography, greetingString: String, nameString: String, ivBadge: ImageView, drawableInt: Int, badgeUrl: String) {
        if (needToSwitchText) {
            tvName.text = greetingString
            ivBadge.loadImageDrawable(drawableInt)
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
}