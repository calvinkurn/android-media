package com.tokopedia.chatbot.view.activity

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.di.ChatbotModule

import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.view.adapter.ContactUsMigrationAdapter
import com.tokopedia.chatbot.view.viewmodel.ChatbotViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.webview.BaseSessionWebViewFragment
import kotlinx.android.synthetic.main.bottom_sheet_go_to_help.view.*
import javax.inject.Inject

class ChatbotInboxBSActivity : BaseSimpleActivity() {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    lateinit var viewModel : ChatbotViewModel

    val URL_HELP = getInstance().WEB + "help?utm_source=android"

    val bottomSheetPage = BottomSheetUnify()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot_inbox_bsactivity)

        initInjector()
        initViewModel()
        startObservingViewModels()

        viewModel.getTicketList()
    }

    private fun startObservingViewModels() {
        viewModel.ticketList.observe(this, Observer {
            when(it) {
                is Success -> handleSuccess(it.data)
                is Fail -> handleGQLError(it.throwable)
            }
        })
    }

    //Check onDetach whether to close the connection to viewModel

    private fun handleGQLError(throwable: Throwable) {

    }

    private fun handleSuccess(data: InboxTicketListResponse) {
        val noticeStatus =  data?.ticket?.TicketData?.notice?.isActive
        if(noticeStatus==true){
            val notice = data?.ticket?.TicketData?.notice
            val title = notice?.title
            val subtitle = notice?.subtitle
            val content = notice?.content
            val contentList = notice?.listOfContent
            showBottomSheet(title,subtitle,content,contentList)
        }
        else{

        }
    }

    private fun showBottomSheet(
        title: String,
        subtitle: String,
        content: String,
        contentList: List<String>
    ) {

        val viewBottomSheetPage =
            View.inflate(this, R.layout.bottom_sheet_go_to_help, null).apply {
                this.text_title.text = title
                this.text_subtitle.text = SpannableString(MethodChecker.fromHtml(subtitle))
                this.text_list_header.text = content
                this.btn_tokopedia_care.setOnClickListener {
                    goToHelpPage()
                }

                this.content_list.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
                val adapter = ContactUsMigrationAdapter()
                setList(contentList,adapter)
                this.content_list.adapter = adapter

            }
        bottomSheetPage.apply {
            showCloseIcon = true
            setChild(viewBottomSheetPage)
            showKnob = false
        }
        supportFragmentManager?.let {
            bottomSheetPage.show(it, "TAG")
        }
    }

    private fun goToHelpPage() {
        RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, URL_HELP)
        finish()
    }

    private fun setList(contentList: List<String>, adapter: ContactUsMigrationAdapter) {
        var newList = mutableListOf<String>()

        contentList.forEachIndexed{ index, content ->
            val spannableString = SpannableString("${index+1}. ")
            spannableString.setSpan(StyleSpan(Typeface.BOLD),0,3,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
            val data = spannableString.toString() + content
            newList.add(data)
        }

        adapter.setList(newList)
    }

    private fun initViewModel() {
        viewModel = ViewModelProvider(this,viewModelFactory).get(ChatbotViewModel::class.java)
    }

    private fun initInjector() {
        val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent)
            .chatbotModule(ChatbotModule(this))
            .build()

        chatbotComponent.inject(this)
    }


    override fun getNewFragment(): Fragment? = null
}