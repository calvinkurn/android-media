package com.tokopedia.homenav.mainnav.view.adapter.viewholder

import android.content.Context
import android.content.SharedPreferences
import android.view.View
import android.widget.ImageView
import androidx.annotation.LayoutRes
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.homenav.R
import com.tokopedia.homenav.mainnav.view.interactor.MainNavListener
import com.tokopedia.homenav.mainnav.view.viewmodel.AccountHeaderViewModel
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSessionInterface

class AccountHeaderViewHolder(itemView: View,
                               private val mainNavListener: MainNavListener,
                               private val userSession: UserSessionInterface
): AbstractViewHolder<AccountHeaderViewModel>(itemView) {

    private lateinit var layoutNonLogin: ConstraintLayout
    private lateinit var layoutLogin: ConstraintLayout
    private lateinit var layoutLoginAs: ConstraintLayout

    companion object {
        @LayoutRes
        val LAYOUT = R.layout.holder_account_header
        const val TEXT_LOGIN_AS = "Masuk Sebagai %s"
        const val TEXT_MY_SHOP = "Toko saya: %s"
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
        val userImage: ImageView = layoutLogin.findViewById(R.id.img_user_login)
        val usrBadge: ImageView = layoutLogin.findViewById(R.id.usr_badge)
        val usrOvoBadge: ImageView = layoutLogin.findViewById(R.id.usr_ovo_badge)
        val btnSettings: ImageView = layoutLogin.findViewById(R.id.btn_settings)
        val tvName: Typography = layoutLogin.findViewById(R.id.tv_name)
        val tvOvo: Typography = layoutLogin.findViewById(R.id.tv_ovo)
        val tvShopInfo: Typography = layoutLogin.findViewById(R.id.usr_shop_info)
        val tvShopNotif: Typography = layoutLogin.findViewById(R.id.usr_shop_notif)

        userImage.loadImageCircle(element.userImage)
        tvName.text = element.userName
        tvOvo.text = element.ovoSaldo
        usrBadge.loadImage(element.badge)

        if (element.shopName.isNotEmpty()) {
            tvShopInfo.visibility = View.VISIBLE
            tvShopInfo.text = String.format(TEXT_MY_SHOP, element.shopName)
        }
    }

    private fun renderNonLoginState() {
        layoutNonLogin.visibility = View.VISIBLE
        val btnLogin: UnifyButton = layoutNonLogin.findViewById(R.id.btn_login)
        val btnRegister: UnifyButton = layoutNonLogin.findViewById(R.id.btn_register)

        btnLogin.setOnClickListener {
            //applink login, handle callback
        }

        btnRegister.setOnClickListener {
            //applink register, handle callback
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
            //applink login with, handle callback
        }
    }

    private fun getSharedPreference(): SharedPreferences {
        return itemView.context.getSharedPreferences(AccountHeaderViewModel.STICKY_LOGIN_REMINDER_PREF, Context.MODE_PRIVATE)
    }
}