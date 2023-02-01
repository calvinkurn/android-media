package com.tokopedia.contactus.inboxtickets.view.inbox

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.tkpd.remoteresourcerequest.view.DeferredImageView
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.abstraction.base.view.recyclerview.VerticalRecyclerView
import com.tokopedia.abstraction.base.view.viewmodel.ViewModelFactory
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.applink.RouteManager
import com.tokopedia.contactus.R
import com.tokopedia.contactus.common.analytics.ContactUsTracking
import com.tokopedia.contactus.common.analytics.InboxTicketTracking
import com.tokopedia.contactus.databinding.ContactUsFragmentInboxBinding
import com.tokopedia.contactus.home.view.ContactUsHomeActivity
import com.tokopedia.contactus.inboxtickets.di.DaggerInboxComponent
import com.tokopedia.contactus.inboxtickets.di.InboxModule
import com.tokopedia.contactus.inboxtickets.view.adapter.InboxFilterAdapter
import com.tokopedia.contactus.inboxtickets.view.adapter.TicketListAdapter
import com.tokopedia.contactus.inboxtickets.view.customview.ChatWidgetToolTip
import com.tokopedia.contactus.inboxtickets.view.customview.CustomChatWidgetView
import com.tokopedia.contactus.inboxtickets.view.fragment.InboxBottomSheetFragment
import com.tokopedia.contactus.inboxtickets.view.fragment.ServicePrioritiesBottomSheet
import com.tokopedia.contactus.inboxtickets.view.inbox.delegates.HasPaginatedList
import com.tokopedia.contactus.inboxtickets.view.inbox.delegates.HasPaginatedListImpl
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxFilterSelection
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxUiState
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.InboxUiEffect
import com.tokopedia.contactus.inboxtickets.view.inbox.uimodel.UiObjectMapper.mapToSelectionFilterObject
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailActivity.Companion.getIntent
import com.tokopedia.contactus.inboxtickets.view.inboxdetail.InboxDetailConstanta.RESULT_FINISH
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import com.tokopedia.webview.KEY_TITLE
import kotlinx.coroutines.flow.collect
import javax.inject.Inject

class InboxContactUsFragment :
    BaseDaggerFragment(),
    ServicePrioritiesBottomSheet.CloseServicePrioritiesBottomSheet,
    HasPaginatedList by HasPaginatedListImpl() {

    private var binding by autoClearedNullable<ContactUsFragmentInboxBinding>()
    private var ivNoTicket: DeferredImageView? = null
    private var tvNoTicket: Typography? = null
    private var tvRaiseTicket: Typography? = null
    private var tvGreetNoTicket: Typography? = null
    private var rvEmailList: VerticalRecyclerView? = null
    private var btnFilter: View? = null
    private val mAdapter: TicketListAdapter by lazy {
        TicketListAdapter(arrayListOf())
    }
    private var btnFilterTv: TextView? = null
    private var chatWidget: CustomChatWidgetView? = null
    private var chatWidgetNotification: View? = null
    private var bottomFragment: BottomSheetDialogFragment? = null
    private var servicePrioritiesBottomSheet: ServicePrioritiesBottomSheet? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private val viewModelProvider by lazy { ViewModelProvider(this, viewModelFactory) }
    private val viewModel by lazy { viewModelProvider.get(InboxContactUsViewModel::class.java) }

    companion object {
        private const val RAISE_TICKET_TAG = "raiseTicket"
        private const val PAGE_SIZE = 10
        const val REQUEST_DETAILS = 204

        @JvmStatic
        fun newInstance(): InboxContactUsFragment {
            return InboxContactUsFragment()
        }
    }

    override fun getScreenName(): String =
        InboxContactUsFragment::class.java.canonicalName.orEmpty()

    override fun initInjector() {
        DaggerInboxComponent.builder()
            .baseAppComponent(
                (activity?.applicationContext as? BaseMainApplication)?.baseAppComponent
            )
            .inboxModule(activity?.applicationContext?.let { InboxModule(it) })
            .build().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = ContactUsFragmentInboxBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        setObserver()
        setObserverUIEffect()
    }

    override fun onResume() {
        super.onResume()
        showProgressBar()
        viewModel.getTopBotStatus()
        viewModel.restartPageOfList()
        viewModel.getTicketItems()
    }

    private fun initView() {
        setupViewBinding()
        setupPaging()
        setOptionsFilter()
        settingOnClickListener()
        btnFilterTv?.setCompoundDrawablesWithIntrinsicBounds(
            MethodChecker.getDrawable(
                context ?: return,
                R.drawable.contactus_ic_filter_list
            ),
            null,
            null,
            null
        )
    }

    private fun setObserver() {
        viewLifecycleOwner.lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { state ->
                handleUIState(state)
            }
        }
    }

    private fun setObserverUIEffect() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiEffect.collect { event -> handleEffect(event) }
        }
    }

    private fun handleUIState(uiState: InboxUiState) {
        if (uiState.showChatBotWidget) {
            val applinkPrefix =
                context?.resources?.getString(R.string.contactus_chat_bot_applink).orEmpty()
            val appLink = String.format(applinkPrefix, uiState.idMessage)
            showChatBotWidget(uiState.welcomeMessage, uiState.unReadNotification, appLink)
        } else {
            hideChatBotWidget()
        }
    }

    private fun handleEffect(uiEffect: InboxUiEffect) {
        when (uiEffect) {
            is InboxUiEffect.EmptyTicket -> {
                hideList()
                hideProgressBar()
                if (uiEffect.isFilteredData) {
                    toggleEmptyLayout(View.VISIBLE)
                } else {
                    hideFilter()
                    toggleNoTicketLayout(View.VISIBLE, uiEffect.name)
                }
            }

            is InboxUiEffect.LoadNextPageSuccess -> {
                if (uiEffect.isFirstPage) {
                    showFilter()
                    toggleEmptyLayout(View.GONE)
                    mAdapter.clear()
                    mAdapter.addAll(uiEffect.currentPageItems)
                    hideProgressBar()
                    showList()
                } else {
                    notifyLoadResult(uiEffect.isHasNext)
                    mAdapter.addAll(uiEffect.currentPageItems)
                }
            }
            is InboxUiEffect.FetchInboxError -> {
                val errorMessage = ErrorHandler.getErrorMessage(context, uiEffect.throwable)
                binding?.rvEmailList.showToasterErrorWithCta(
                    errorMessage,
                    context?.getString(R.string.contact_us_ok).orEmpty()
                )
            }
        }
    }

    private fun View?.showToasterErrorWithCta(errorMessage: String, ctaText: String) {
        Toaster.build(
            this ?: return,
            errorMessage,
            Toaster.LENGTH_SHORT,
            Toaster.TYPE_ERROR,
            ctaText
        ).apply {
            show()
        }
    }

    private fun setupPaging() {
        val pagingConfig = HasPaginatedList.Config(
            pageSize = PAGE_SIZE,
            onLoadNextPage = {
                mAdapter.addFooter()
            },
            onLoadNextPageFinished = {
                mAdapter.removeFooter()
            }
        )

        binding?.rvEmailList?.apply {
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
            mAdapter.setListener(object : TicketListAdapter.TicketAdapterListener {
                override fun scrollList() {
                }

                override fun showSerVicePriorityBottomSheet() {
                    servicePriorityBottomSheet()
                }

                override fun onClickTicket(index: Int, isOfficialStore: Boolean) {
                    val itemTicket = viewModel.getItemTicketOnPosition(index)
                    val ticketId = itemTicket.id.orEmpty()
                    val detailIntent =
                        getIntent(context ?: return, ticketId, isOfficialStore)
                    startActivityForResult(detailIntent, REQUEST_DETAILS)
                    sendTrackingClickToDetailTicketMessage(index)
                }
            })
            adapter = mAdapter

            attachPaging(this, pagingConfig) { _, _ ->
                viewModel.getTicketItems()
            }
        }
    }

    fun sendTrackingClickToDetailTicketMessage(positionItem: Int) {
        val itemTicket = viewModel.getItemTicketOnPosition(positionItem)
        ContactUsTracking.sendGTMInboxTicket(
            context,
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventTicketClick,
            itemTicket.caseNumber
        )
    }

    private fun setOptionsFilter() {
        val optionsFilterFromAsset =
            context?.resources?.getStringArray(R.array.contact_us_filter_array).orEmpty().asList()
        viewModel.setOptionsFilter(mapToSelectionFilterObject(optionsFilterFromAsset))
    }

    private fun setupViewBinding() {
        binding?.run {
            header.setNavigationOnClickListener { activity?.finish() }
            this@InboxContactUsFragment.ivNoTicket = ivNoTicket
            this@InboxContactUsFragment.tvNoTicket = tvNoTicket
            this@InboxContactUsFragment.tvRaiseTicket = tvRaiseTicket
            this@InboxContactUsFragment.tvGreetNoTicket = tvGreeting
            this@InboxContactUsFragment.rvEmailList = rvEmailList
            this@InboxContactUsFragment.btnFilter = btnFilter
            this@InboxContactUsFragment.btnFilterTv = btnFilterTv
            this@InboxContactUsFragment.chatWidget = chatWidget
            this@InboxContactUsFragment.chatWidgetNotification = chatWidgetNotificationIndicator
        }
    }

    private fun settingOnClickListener() {
        tvRaiseTicket?.setOnClickListener {
            raiseTicket()
        }
        btnFilter?.setOnClickListener {
            showBottomFragment(viewModel.getOptionsFilter())
        }
    }

    private fun showBottomFragment(options: List<InboxFilterSelection>) {
        val BOTTOM_FRAGMENT = "Bottom_Sheet_Fragment"
        bottomFragment =
            parentFragmentManager.findFragmentByTag(BOTTOM_FRAGMENT) as? BottomSheetDialogFragment?
        if (bottomFragment == null) bottomFragment =
            getBottomFragment(getBottomSheetLayoutRes(), options)
        bottomFragment?.show(parentFragmentManager, BOTTOM_FRAGMENT)
    }

    private fun getBottomSheetLayoutRes(): Int {
        return R.layout.layout_bottom_sheet_fragment
    }

    private fun getBottomFragment(
        resID: Int,
        options: List<InboxFilterSelection>
    ): BottomSheetDialogFragment? {
        val bottomFragment = InboxBottomSheetFragment.getBottomSheetFragment(resID)
        bottomFragment?.setAdapter(getFilterAdapter(options))
        return bottomFragment
    }

    private fun getFilterAdapter(options: List<InboxFilterSelection>): InboxFilterAdapter {
        val adapter: InboxFilterAdapter by lazy {
            InboxFilterAdapter(options) { optionSelected ->
                viewModel.markOptionsFilterWithSelected(optionSelected)
                showProgressBar()
                viewModel.restartPageOfList()
                viewModel.getTicketItems()
                sendGtmClickTicketFilter(viewModel.findMarkOptions().name)
                bottomFragment?.dismiss()
            }
        }
        return adapter
    }

    fun servicePriorityBottomSheet() {
        servicePrioritiesBottomSheet = ServicePrioritiesBottomSheet(context ?: return, this)
        servicePrioritiesBottomSheet?.show(parentFragmentManager, "servicePrioritiesBottomSheet")
    }

    override fun onClickClose() {
        servicePrioritiesBottomSheet?.dismiss()
    }

    private fun toggleNoTicketLayout(visibility: Int, name: String) {
        ivNoTicket?.loadRemoteImageDrawable("no_messages.png")
        ivNoTicket?.visibility = visibility
        tvNoTicket?.text = getString(R.string.contact_us_no_ticket_message)
        tvNoTicket?.visibility = visibility
        tvRaiseTicket?.text = getString(R.string.contact_us_tokopedia_care)
        tvRaiseTicket?.tag = RAISE_TICKET_TAG
        tvRaiseTicket?.visibility = visibility
        tvGreetNoTicket?.text = String.format(getString(R.string.contact_us_greet_user), name)
        tvGreetNoTicket?.visibility = visibility
    }

    private fun toggleEmptyLayout(visibility: Int) {
        ivNoTicket?.visibility = visibility
        tvNoTicket?.visibility = visibility
        tvRaiseTicket?.visibility = visibility
    }

    private fun hideList() {
        rvEmailList?.hide()
    }

    private fun showList() {
        rvEmailList?.show()
    }

    private fun hideFilter() {
        btnFilter?.hide()
    }

    private fun showFilter() {
        btnFilter?.show()
    }

    private fun showChatBotWidget(
        welcomeMessage: String,
        isHasUnReadNotification: Boolean,
        applink: String
    ) {
        chatWidget?.show()
        chatWidget?.setToolTipDescription(welcomeMessage)
        chatWidget?.setToolTipButtonLister(object : ChatWidgetToolTip.ChatWidgetToolTipListener {
            override fun onClickToolTipButton() {
                sendGTMClickChatButton()
                RouteManager.route(context ?: return, applink)
            }
        })
        showChatBotWidgetNotification(isHasUnReadNotification)
    }

    private fun showChatBotWidgetNotification(isShowNotification: Boolean) {
        chatWidgetNotification?.showWithCondition(isShowNotification)
    }

    private fun hideChatBotWidget() {
        chatWidget?.hide()
    }

    @SuppressLint("DeprecatedMethod")
    private fun raiseTicket() {
        if (tvRaiseTicket?.tag == RAISE_TICKET_TAG) {
            val contactUsHome = Intent(context ?: return, InboxContactUsActivity::class.java)
            contactUsHome.putExtra(KEY_TITLE, getString(R.string.contact_us_title_home))
            contactUsHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(contactUsHome)
            ContactUsTracking.sendGTMInboxTicket(
                context ?: return,
                "",
                InboxTicketTracking.Category.EventInboxTicket,
                InboxTicketTracking.Action.EventClickHubungi,
                InboxTicketTracking.Label.InboxEmpty
            )
            activity?.finish()
        } else {
            viewModel.autoPickShowAllOptionsFilter()
            viewModel.restartPageOfList()
            viewModel.getTicketItems()
        }
    }

    @SuppressLint("DeprecatedMethod")
    private fun sendGTMClickChatButton() {
        ContactUsTracking.sendGTMInboxTicket(
            activity,
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventClickChatbotButton,
            ""
        )
    }

    @SuppressLint("DeprecatedMethod")
    private fun sendGtmClickTicketFilter(selected: String) {
        ContactUsTracking.sendGTMInboxTicket(
            activity,
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventClickTicketFilter,
            selected
        )
    }

    private fun showProgressBar() {
        binding?.progressBarLayout?.visibility = View.VISIBLE
    }

    private fun hideProgressBar() {
        binding?.progressBarLayout?.visibility = View.GONE
    }

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (resultCode != Activity.RESULT_CANCELED && requestCode == REQUEST_DETAILS) {
            if (resultCode == RESULT_FINISH) {
                activity?.startActivityForResult(
                    Intent(
                        context,
                        ContactUsHomeActivity::class.java
                    ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP),
                    100
                )
                activity?.finish()
            }
        }
    }
}
