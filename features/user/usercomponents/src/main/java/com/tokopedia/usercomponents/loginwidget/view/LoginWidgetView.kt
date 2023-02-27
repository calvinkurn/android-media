package com.tokopedia.usercomponents.loginwidget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.user.session.util.EncoderDecoder
import com.tokopedia.usercomponents.databinding.LayoutLoginWidgetBinding
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.common.helper.getPrefLoginReminder

/**
 * Created by dhaba
 */
class LoginWidgetView : ConstraintLayout {
    private var userSession: UserSessionInterface? = null
    private var listener: LoginWidgetListener? = null

    private var viewBinding = LayoutLoginWidgetBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    companion object {
        private const val DEFAULT_PROFILE_PICTURE = "https://images.tokopedia.net/img/android/user/usercomponents/ic_tokopedia_login_widget.png"
    }

    init {
        userSession = UserSession(context)
        loadContent()
    }

    fun setListener(listener: LoginWidgetListener) {
        this.listener = listener
    }

    private fun isLoginReminder(): Boolean {
        val pref = getPrefLoginReminder(context)
        val name = pref.getString(StickyLoginConstant.KEY_USER_NAME, "") ?: ""
        val picture = pref.getString(StickyLoginConstant.KEY_PROFILE_PICTURE, "") ?: ""
        return name.isNotEmpty() && picture.isNotEmpty()
    }

    private fun loadContent() {
        viewBinding.loginButton.setOnClickListener {
            listener?.goToLogin()
        }
        this.setOnClickListener {
            listener?.goToLogin()
        }
        if (userSession?.isLoggedIn == true) {
            hide()
            return
        } else if (isLoginReminder()) {
            showLoginReminder()
        } else {
            loadDefaultLogin()
        }
    }

    private fun loadDefaultLogin() {
        viewBinding.textGreetings.text = context.getString(com.tokopedia.usercomponents.R.string.login_widget_title)
        viewBinding.textSubtitle.text = context.getString(com.tokopedia.usercomponents.R.string.login_widget_subtitle)
        viewBinding.imageLoginWidget.type = ImageUnify.TYPE_CIRCLE
        viewBinding.imageLoginWidget.setImageUrl(DEFAULT_PROFILE_PICTURE)
        show()
    }

    private fun showLoginReminder() {
        try {
            val encryptedName = getPrefLoginReminder(context).getString(StickyLoginConstant.KEY_USER_NAME, "")
            val encryptedProfilePicture = getPrefLoginReminder(context).getString(
                StickyLoginConstant.KEY_PROFILE_PICTURE,
                ""
            )

            val name = EncoderDecoder.Decrypt(encryptedName ?: "", UserSession.KEY_IV)
            val profilePicture = EncoderDecoder.Decrypt(encryptedProfilePicture ?: "", UserSession.KEY_IV)

            viewBinding.textGreetings.text = "Hi, $name"

            viewBinding.imageLoginWidget.type = ImageUnify.TYPE_CIRCLE
            viewBinding.imageLoginWidget.setImageUrl(profilePicture)
            show()
        } catch (e: Exception) {
            hide()
        }
    }
}
