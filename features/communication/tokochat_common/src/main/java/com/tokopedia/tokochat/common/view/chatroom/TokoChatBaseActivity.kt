package com.tokopedia.tokochat.common.view.chatroom

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import com.google.android.play.core.splitcompat.SplitCompat
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatBaseActivityBinding
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.TYPING_DARK_MODE
import com.tokopedia.tokochat.common.util.TokoChatUrlUtil.TYPING_LIGHT_MODE
import com.tokopedia.tokochat.common.util.TokoChatViewUtil.loadGif
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

abstract class TokoChatBaseActivity<T> : BaseSimpleActivity(), HasComponent<T> {

    protected var tokoChatComponent: T? = null
    protected var bundle: Bundle? = null

    private var viewBinding: TokochatBaseActivityBinding? = null

    private var headerShimmering: View? = null
    private var photoHeaderContainer: View? = null
    private var headerContainer: View? = null
    private var iconHeaderMenu: View? = null
    private var headerSubtitle: Typography? = null
    private var typingLayout: View? = null
    private var typingImage: ImageUnify? = null

    protected var headerCustomView: View? = null

    protected abstract fun setupFragmentFactory()

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(newBase)
        SplitCompat.installActivity(this)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setupViewBinding()
        setBackgroundColor()
        setupTokoChatHeader()
    }

    override fun getParentViewResourceID(): Int {
        return R.id.tokochat_layout_chatroom_fragment
    }

    override fun getLayoutRes(): Int {
        return R.layout.tokochat_base_activity
    }

    override fun onDestroy() {
        super.onDestroy()
        viewBinding = null
    }

    private fun setupViewBinding() {
        viewBinding = TokochatBaseActivityBinding.inflate(layoutInflater)
        setContentView(viewBinding?.root)
    }

    private fun getTokoChatHeaderLayout() = R.layout.tokochat_item_header

    private fun setBackgroundColor() {
        viewBinding?.tokochatLayoutChatroomFragment?.setBackgroundColor(
            MethodChecker.getColor(this, com.tokopedia.unifyprinciples.R.color.Unify_NN50)
        )
    }

    protected open fun setupTokoChatHeader() {
        val mInflater = LayoutInflater.from(this)
        headerCustomView = mInflater.inflate(getTokoChatHeaderLayout(), null)
        headerShimmering = headerCustomView?.findViewById(R.id.tokochat_layout_header_shimmering)
        photoHeaderContainer = headerCustomView?.findViewById(R.id.tokochat_layout_header_photo_container)
        headerContainer = headerCustomView?.findViewById(R.id.tokochat_layout_header_container)
        iconHeaderMenu = headerCustomView?.findViewById(R.id.tokochat_icon_header_menu)
        headerSubtitle = headerCustomView?.findViewById(R.id.tokochat_text_user_subtitle)
        typingLayout = headerCustomView?.findViewById(R.id.tokochat_layout_typing)
        typingImage = headerCustomView?.findViewById(R.id.tokochat_iv_typing)
        typingImage?.let {
            val typingUrl = if (this.isDarkMode()) {
                TYPING_DARK_MODE
            } else {
                TYPING_LIGHT_MODE
            }
            loadGif(it, typingUrl)
        }
    }

    fun getHeaderUnify(): HeaderUnify? {
        return viewBinding?.tokochatToolbar
    }

    fun showInterlocutorTyping() {
        headerSubtitle?.gone()
        typingLayout?.show()
    }

    fun hideInterlocutorTyping() {
        typingLayout?.hide()
        headerSubtitle?.show()
    }

    fun showHeader() {
        hideHeaderShimmering()
        photoHeaderContainer?.show()
        headerContainer?.show()
        iconHeaderMenu?.show()
    }

    fun hideHeaderShimmering() {
        headerShimmering?.hide()
    }

    fun showHeaderShimmering() {
        headerShimmering?.show()
    }
}
