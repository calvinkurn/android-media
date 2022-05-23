package com.tokopedia.chatbot.view.activity

import android.os.Bundle
import android.text.SpannableString
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chatbot.R
import com.tokopedia.chatbot.analytics.ChatbotAnalytics
import com.tokopedia.chatbot.data.inboxTicketList.InboxTicketListResponse
import com.tokopedia.chatbot.di.ChatbotModule

import com.tokopedia.chatbot.di.DaggerChatbotComponent
import com.tokopedia.chatbot.view.adapter.ContactUsMigrationAdapter
import com.tokopedia.chatbot.view.viewmodel.ChatbotViewModel
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.url.TokopediaUrl.Companion.getInstance
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import kotlinx.android.synthetic.main.bottom_sheet_go_to_help.view.*
import javax.inject.Inject

class ContactUsMigrationActivity : BaseSimpleActivity() {

    @Inject
    lateinit var viewModelFactory : ViewModelProvider.Factory

    lateinit var viewModel : ChatbotViewModel

    private val URL_HELP = getInstance().WEB + "help?utm_source=android"
    private val CONTACT_US_APPLINK = "tokopedia-android-internal://customercare2"

    @Inject
    lateinit var chatbotAnalytics: dagger.Lazy<ChatbotAnalytics>

    private val bottomSheetPage = BottomSheetUnify()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot_inbox_migration)

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

    private fun handleGQLError(throwable: Throwable) {
        goToContactUs()
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
            goToContactUs()
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
                val textTitle : Typography = this.findViewById(R.id.text_title)
                val textSubtitle : Typography = this.findViewById(R.id.text_subtitle)
                val textListHeader : Typography = this.findViewById(R.id.text_list_header)
                val buttonTokopediaCare : UnifyButton = this.findViewById(R.id.btn_tokopedia_care)
                val contentListRV : RecyclerView = this.findViewById(R.id.content_list)

                textTitle.text = title
                textSubtitle.text = SpannableString(MethodChecker.fromHtml(subtitle))
                textListHeader.text = content
                buttonTokopediaCare.setOnClickListener {
                    chatbotAnalytics.get().eventOnClickTokopediaCare()
                    goToHelpPage()
                }
                contentListRV.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
                val adapter = ContactUsMigrationAdapter()
                setList(contentList,adapter)
                contentListRV.adapter = adapter

            }
        bottomSheetPage.apply {
            showCloseIcon = true
            setChild(viewBottomSheetPage)
            showKnob = false
        }
        bottomSheetPage.setCloseClickListener {
            chatbotAnalytics.get().eventOnClickCancelBottomSheet()
            goToContactUs()
        }
        supportFragmentManager?.let {
            bottomSheetPage.show(it, "")
        }
    }

    private fun goToContactUs() {
        RouteManager.route(this, CONTACT_US_APPLINK)
        finish()
    }

    private fun goToHelpPage() {
        RouteManager.route(this, ApplinkConstInternalGlobal.WEBVIEW, URL_HELP)
        finish()
    }

    private fun setList(contentList: List<String>, adapter: ContactUsMigrationAdapter) {
        var newList = mutableListOf<Pair<Int,String>>()

        contentList.forEachIndexed{ index, content ->
            newList.add(Pair(index+1,content))
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