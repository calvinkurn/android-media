package com.tokopedia.chatbot.view.activity

import android.content.Context
import android.os.Bundle
import android.text.SpannableString
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalGlobal
import com.tokopedia.chatbot.ChatbotConstant.CONTACT_US_APPLINK
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

    private val viewModel : ChatbotViewModel by lazy(LazyThreadSafetyMode.NONE){
        ViewModelProvider(this,viewModelFactory).get(ChatbotViewModel::class.java)
    }

    private val URL_HELP = getInstance().WEB + "help?utm_source=android"

    private lateinit var textSubtitle : Typography
    private lateinit var buttonTokopediaCare : UnifyButton
    private lateinit var contentListRV : RecyclerView

    @Inject
    lateinit var chatbotAnalytics: dagger.Lazy<ChatbotAnalytics>

    private val bottomSheetPage = BottomSheetUnify()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot_inbox_migration)

        initInjector()
        startObservingViewModels()

        viewModel.getTicketList()
    }

    private fun startObservingViewModels() {
        viewModel.ticketList.observe(this) {
            when (it) {
                is Success -> handleSuccess(it.data)
                is Fail -> handleGQLError(it.throwable)
            }
        }
    }

    private fun handleGQLError(throwable: Throwable) {
        goToContactUs()
    }

    private fun handleSuccess(data: InboxTicketListResponse) {
        val showBottomSheet =  data?.ticket?.TicketData?.notice?.isActive
        if(showBottomSheet==true){
            val noticeData = data?.ticket?.TicketData?.notice
            val subtitle = noticeData?.subtitle
            showBottomSheet(subtitle)
        }
        else{
            goToContactUs()
        }
    }

    private fun showBottomSheet(
        subtitle: String
    ) {
        val viewBottomSheetPage = initBottomSheet(subtitle)

        bottomSheetPage.apply {
            showCloseIcon = true
            setChild(viewBottomSheetPage)
            showKnob = false
        }

        supportFragmentManager?.let {
            bottomSheetPage.show(it, "")
        }
    }

    private fun initBottomSheet(subtitle: String): View? {
        val view = View.inflate(this, R.layout.bottom_sheet_go_to_help, null).apply {
            initViews(this)
            setSubtitle(subtitle)
            initRecyclerViewFromContentList(context,contentListRV)
            setOnClickListener()
        }
        return view
    }

    private fun initViews(view : View) {
        textSubtitle = view.findViewById(R.id.text_subtitle)
        buttonTokopediaCare = view.findViewById(R.id.btn_tokopedia_care)
        contentListRV= view.findViewById(R.id.content_list)
    }

    private fun setOnClickListener() {
        buttonTokopediaCare.setOnClickListener {
            chatbotAnalytics.get().eventOnClickTokopediaCare()
            goToHelpPage()
        }

        bottomSheetPage.setCloseClickListener {
            chatbotAnalytics.get().eventOnClickCancelBottomSheet()
            goToContactUs()
        }
    }

    private fun setSubtitle(subtitle: String) {
        textSubtitle.text = SpannableString(MethodChecker.fromHtml(subtitle))
    }

    private fun initRecyclerViewFromContentList(
        context: Context,
        contentListRV: RecyclerView
    ) {
        contentListRV.layoutManager = LinearLayoutManager(context,LinearLayoutManager.VERTICAL, false)
        val adapter = ContactUsMigrationAdapter()
        val contentList = getList()
        setList(contentList,adapter)
        contentListRV.adapter = adapter
    }

    private fun getList(): List<String> {
        val list = mutableListOf<String>()
        list.add(getString(R.string.chatbot_migration_list_item_1))
        list.add(getString(R.string.chatbot_migration_list_item_2))
        return list
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


    private fun initInjector() {
        val chatbotComponent = DaggerChatbotComponent.builder().baseAppComponent(
            (application as BaseMainApplication).baseAppComponent)
            .chatbotModule(ChatbotModule(this))
            .build()

        chatbotComponent.inject(this)
    }

    override fun getNewFragment(): Fragment? = null
}