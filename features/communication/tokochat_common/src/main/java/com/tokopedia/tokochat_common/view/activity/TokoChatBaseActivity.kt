package com.tokopedia.tokochat_common.view.activity

import android.os.Bundle
import android.view.View
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.header.HeaderUnify
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.media.loader.loadImage
import com.tokopedia.tokochat_common.R
import com.tokopedia.tokochat_common.databinding.TokochatBaseActivityBinding
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil.TYPING_DARK_MODE
import com.tokopedia.tokochat_common.util.TokoChatUrlUtil.TYPING_LIGHT_MODE
import com.tokopedia.unifycomponents.ImageUnify
import com.tokopedia.utils.view.DarkModeUtil.isDarkMode

abstract class TokoChatBaseActivity<T>: BaseSimpleActivity(), HasComponent<T> {

    protected var tokoChatComponent: T? = null
    protected var bundle: Bundle? = null

    private var viewBinding: TokochatBaseActivityBinding? = null

    private var typingLayout: View? = null
    private var typingImage: ImageUnify? = null

    protected abstract fun setupFragmentFactory()

    override fun onCreate(savedInstanceState: Bundle?) {
        setupFragmentFactory()
        super.onCreate(savedInstanceState)
        setupViewBinding()
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

    protected fun getChatHeaderLayout() = R.layout.item_tokochat_header

    protected open fun setupTokoChatHeader() {
        typingLayout = findViewById(R.id.tokochat_layout_typing)
        typingImage = findViewById(R.id.tokochat_iv_typing)
        typingImage?.let {
            val typingUrl = if (this.isDarkMode()) {
                TYPING_DARK_MODE
            } else {
                TYPING_LIGHT_MODE
            }
            it.loadImage(typingUrl)
        }
    }

    fun getHeaderUnify(): HeaderUnify? {
        return viewBinding?.tokochatToolbar
    }

    fun showInterlocutorTyping() {
        typingLayout?.show()
    }

    fun hideInterlocutorTyping() {
        typingLayout?.hide()
    }
}
