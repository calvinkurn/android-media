package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.R
import com.tokopedia.home.beranda.helper.glide.loadImage
import com.tokopedia.home.beranda.presentation.view.helper.HomeThematicUtil
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.util.EncoderDecoder
import com.tokopedia.usercomponents.stickylogin.common.StickyLoginConstant
import com.tokopedia.usercomponents.stickylogin.common.helper.getPrefLoginReminder
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

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

    fun bind(listener: HomeCategoryListener?, homeThematicUtil: HomeThematicUtil = HomeThematicUtil()) {
        this.listener = listener
        renderWidget(homeThematicUtil)
    }

    private fun renderWidget(homeThematicUtil: HomeThematicUtil) {
        setClickListener()
        val (name, profilePicture) = getAccountData()
        if(!name.isNullOrEmpty() && !profilePicture.isNullOrEmpty()) {
            renderTitle(name)
            renderSubtitle(R.string.login_widget_subtitle_saved)
        } else {
            renderTitle(context.resources.getString(R.string.login_widget_name_default))
            renderSubtitle(R.string.login_widget_subtitle_non_saved)
        }
        renderImage(profilePicture)
        renderTextColor(homeThematicUtil)
    }

    private fun setClickListener() {
        this.button?.setOnClickListener {
            listener?.onLoginWidgetClick()
        }
    }

    private fun getAccountData(): Pair<String?, String?> {
        val prefLoginReminder = getPrefLoginReminder(context)
        val name = prefLoginReminder.getString(StickyLoginConstant.KEY_USER_NAME, "")?.let {
            EncoderDecoder.Decrypt(it, UserSession.KEY_IV)
        }
        val profilePicture = prefLoginReminder.getString(StickyLoginConstant.KEY_PROFILE_PICTURE, "")?.let {
            EncoderDecoder.Decrypt(it, UserSession.KEY_IV)
        }
        return (name to profilePicture)
    }

    private fun renderTitle(name: String) {
        val titleFormat = context.resources.getString(R.string.login_widget_title)
        this.title?.text = titleFormat.format(name.ifEmpty { name })
    }

    private fun renderSubtitle(subtitleRes: Int) {
        this.subtitle?.text = context.resources.getString(subtitleRes)
    }

    private fun renderImage(profilePicture: String?) {
        if(profilePicture.isNullOrEmpty()) {
            this.image?.loadImage(R.drawable.ic_login_widget_default)
        } else {
            this.image?.loadImage(profilePicture)
        }
    }

    fun renderTextColor(homeThematicUtil: HomeThematicUtil) {
        this.title?.setTextColor(ContextCompat.getColor(context, homeThematicUtil.asThematicColor(unifyprinciplesR.color.Unify_NN950)))
        this.subtitle?.setTextColor(ContextCompat.getColor(context, homeThematicUtil.asThematicColor(unifyprinciplesR.color.Unify_NN600)))
    }
}
