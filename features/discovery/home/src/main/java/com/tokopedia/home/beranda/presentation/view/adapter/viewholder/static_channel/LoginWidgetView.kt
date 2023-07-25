package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.R
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.util.EncoderDecoder
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.common.helper.getPrefLoginReminder
import com.tokopedia.unifyprinciples.Typography

/**
 * Created by Frenzel 06/07/23
 */
class LoginWidgetView : FrameLayout {

    private var itemView: View
    private val itemContext: Context
    private var listener: HomeCategoryListener? = null

    private var image: ImageUnify? = null
    private var title: Typography? = null
    private var subtitle: Typography? = null
    private var button: UnifyButton? = null

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    init {
        val view = LayoutInflater.from(context).inflate(R.layout.layout_login_widget, this)
        this.itemView = view
        this.itemContext = view.context
        this.image = itemView.findViewById(R.id.home_login_widget_image)
        this.title = itemView.findViewById(R.id.home_login_widget_title)
        this.subtitle = itemView.findViewById(R.id.home_login_widget_subtitle)
        this.button = itemView.findViewById(R.id.home_login_widget_button)
    }

    fun bind(listener: HomeCategoryListener?) {
        this.listener = listener
        renderWidget()
    }

    private fun renderWidget() {
        val prefLoginReminder = getPrefLoginReminder(context)

        val name = prefLoginReminder.getString(StickyLoginConstant.KEY_USER_NAME, "")?.let {
            EncoderDecoder.Decrypt(it, UserSession.KEY_IV)
        }
        val profilePicture = prefLoginReminder.getString(StickyLoginConstant.KEY_PROFILE_PICTURE, "")?.let {
            EncoderDecoder.Decrypt(it, UserSession.KEY_IV)
        }

        val titleFormat = context.resources.getString(R.string.login_widget_title)
        if(!name.isNullOrEmpty() && !profilePicture.isNullOrEmpty()) {
            this.image?.setImageUrl(profilePicture)
            this.title?.text = titleFormat.format(name)
            this.subtitle?.text = context.resources.getString(R.string.login_widget_subtitle_saved)
        } else {
            this.image?.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_login_widget_default))
            this.title?.text = titleFormat.format(context.resources.getString(R.string.login_widget_name_default))
            this.subtitle?.text = context.resources.getString(R.string.login_widget_subtitle_non_saved)
        }

        this.button?.setOnClickListener {
            listener?.onLoginWidgetClick()
        }
    }
}
