package com.tokopedia.topchat.chattemplate.view.customview

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topchat.R
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatAdapter
import com.tokopedia.topchat.chattemplate.view.adapter.TemplateChatTypeFactoryImpl
import com.tokopedia.topchat.chattemplate.view.listener.ChatTemplateListener
import com.tokopedia.unifycomponents.BaseCustomView

class TopChatTemplateSeparatedView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0
) : BaseCustomView(context, attributeSet, defStyleAttr) {

    private var view: View? = null

    private var chatTemplateList: List<Visitable<*>> = arrayListOf()
    private var recyclerView: RecyclerView? = null
    private var adapter: TemplateChatAdapter? = null

    init {
        initViewLayout()
        initBindView()
    }

    private fun initViewLayout() {
        view = View.inflate(context, R.layout.layout_chat_template, this)
    }

    private fun initBindView() {
        recyclerView = view?.findViewById(R.id.list_template_separated)
    }

    fun setupSeparatedChatTemplate(chatTemplateListener: ChatTemplateListener) {
        adapter = TemplateChatAdapter(TemplateChatTypeFactoryImpl(chatTemplateListener))
        recyclerView?.setHasFixedSize(true)
        recyclerView?.layoutManager = LinearLayoutManager(
            view?.context, LinearLayoutManager.HORIZONTAL, false)
        recyclerView?.adapter = adapter
        adapter?.list?.clear()
    }

    fun updateTemplate(chatTemplates: List<Visitable<*>>) {
        chatTemplateList = chatTemplates
        adapter?.setList(chatTemplates)
    }
}