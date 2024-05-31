package com.tokopedia.home.beranda.presentation.view.adapter.viewholder.static_channel

import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.tokopedia.home.beranda.listener.HomeCategoryListener
import com.tokopedia.home.R as homeR
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

    private var container: ConstraintLayout? = null
    private var image: ImageUnify? = null
    private var title: Typography? = null
    private var subtitle: Typography? = null
    private var button: UnifyButton? = null

    private val defaultHomeThematicUtil by lazy { HomeThematicUtil(context) }

    private var type: Int = TYPE_CLEAR

    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        initWithAttrs(attrs)
    }
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initWithAttrs(attrs)
    }

    private fun initWithAttrs(attrs: AttributeSet?) {
        val attributes: TypedArray = context.obtainStyledAttributes(attrs, homeR.styleable.LoginWidgetView)
        try {
            type = attributes.getInt(
                homeR.styleable.LoginWidgetView_backgroundType,
                TYPE_CLEAR
            )
        } finally {
            attributes.recycle()
        }
    }

    init {
        val view = LayoutInflater.from(context).inflate(homeR.layout.layout_login_widget, this)
        this.itemView = view
        this.itemContext = view.context
        this.container = itemView.findViewById(homeR.id.containerLoginWidget)
        this.image = itemView.findViewById(homeR.id.home_login_widget_image)
        this.title = itemView.findViewById(homeR.id.home_login_widget_title)
        this.subtitle = itemView.findViewById(homeR.id.home_login_widget_subtitle)
        this.button = itemView.findViewById(homeR.id.home_login_widget_button)
    }

    fun bind(listener: HomeCategoryListener?, homeThematicUtil: HomeThematicUtil? = null) {
        this.listener = listener
        renderWidget(homeThematicUtil ?: defaultHomeThematicUtil)
    }

    private fun renderWidget(homeThematicUtil: HomeThematicUtil) {
        setClickListener()
        setupBackground()
        val (name, profilePicture) = getAccountData()
        if(!name.isNullOrEmpty() && !profilePicture.isNullOrEmpty()) {
            renderTitle(name)
            renderSubtitle(homeR.string.login_widget_subtitle_saved)
        } else {
            renderTitle(context.resources.getString(homeR.string.login_widget_name_default))
            renderSubtitle(homeR.string.login_widget_subtitle_non_saved)
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

    private fun setupBackground() {
        when(type) {
            TYPE_CLEAR -> container?.backgroundTintList = ContextCompat.getColorStateList(context, android.R.color.transparent)
            TYPE_FILLED -> container?.backgroundTintList = ContextCompat.getColorStateList(context, homeR.color.home_dms_login_widget_background)
        }
    }

    private fun renderTitle(name: String) {
        val titleFormat = context.resources.getString(homeR.string.login_widget_title)
        this.title?.text = titleFormat.format(name.ifEmpty { name })
    }

    private fun renderSubtitle(subtitleRes: Int) {
        this.subtitle?.text = context.resources.getString(subtitleRes)
    }

    private fun renderImage(profilePicture: String?) {
        if(profilePicture.isNullOrEmpty()) {
            this.image?.loadImage(homeR.drawable.ic_login_widget_default)
        } else {
            this.image?.loadImage(profilePicture)
        }
    }

    fun renderTextColor(homeThematicUtil: HomeThematicUtil) {
        this.title?.setTextColor(homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN950, itemView.context))
        this.subtitle?.setTextColor(homeThematicUtil.getThematicColor(unifyprinciplesR.color.Unify_NN600, itemView.context))
    }

    companion object {
        private const val TYPE_CLEAR = 0
        private const val TYPE_FILLED = 1
    }
}
