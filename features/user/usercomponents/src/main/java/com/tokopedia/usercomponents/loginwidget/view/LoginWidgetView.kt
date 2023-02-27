package com.tokopedia.usercomponents.loginwidget.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
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
import com.tokopedia.usercomponents.stickylogin.view.StickyLoginView
import javax.inject.Inject

/**
 * Created by dhaba
 */
class LoginWidgetView : ConstraintLayout {
    private var userSession: UserSessionInterface? = null

    private var viewBinding = LayoutLoginWidgetBinding.inflate(LayoutInflater.from(context), this)

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        userSession = UserSession(context)
        loadContent()
    }

    private fun isLoginReminder(): Boolean {
        val pref = getPrefLoginReminder(context)
        val name = pref.getString(StickyLoginConstant.KEY_USER_NAME, "") ?: ""
        val picture = pref.getString(StickyLoginConstant.KEY_PROFILE_PICTURE, "") ?: ""
        return name.isNotEmpty() && picture.isNotEmpty()
    }

    private fun loadContent() {
        if (userSession?.isLoggedIn == true) {
            hide()
            return
        } else if (isLoginReminder()) {
            showLoginReminder()
        } else {

        }
    }

    private fun showLoginReminder() {
        try {
            val encryptedName = getPrefLoginReminder(context).getString(StickyLoginConstant.KEY_USER_NAME, "")
            val encryptedProfilePicture = getPrefLoginReminder(context).getString(
                StickyLoginConstant.KEY_PROFILE_PICTURE, "")

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
