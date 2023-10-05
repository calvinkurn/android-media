package com.tokopedia.contactus.inboxtickets.view.inbox

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.crashlytics.FirebaseCrashlytics
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
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.kotlin.extensions.orFalse
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.network.utils.ErrorHandler
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
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
    private val viewModel by lazy { viewModelProvider[InboxContactUsViewModel::class.java] }

    private val toTicketFeedBack = getInboxDetailResultActivityLauncher()

    private var isFromTokopediaHelp = false

    companion object {
        const val FLAG_FROM_TOKOPEDIA_HELP = "isFromTokopediaHelp"
        private const val RAISE_TICKET_TAG = "raiseTicket"
        private const val PAGE_SIZE = 10

        @JvmStatic
        fun newInstance(isFromInboxPage: Boolean = false): InboxContactUsFragment {
            val bundle = Bundle()
            bundle.putBoolean(FLAG_FROM_TOKOPEDIA_HELP, isFromInboxPage)
            return InboxContactUsFragment().apply {
                arguments = bundle
            }
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
        isFromTokopediaHelp = arguments?.getBoolean(FLAG_FROM_TOKOPEDIA_HELP, false).orFalse()
    }

    override fun onResume() {
        super.onResume()
        showProgressBar()
        resetPaging()
        viewModel.restartPageOfList()
        viewModel.getTicketItems()
        viewModel.getTopBotStatus()
    }

    private fun initView() {
        setupViewBinding()
        setupPaging()
        setOptionsFilter()
        settingOnClickListener()
        settingClickListenerOfErrorPage()
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
            val appLink = String.format(applinkPrefix, uiState.idMessage, uiState.isChatbotActive)
            val welcomeMessage = MethodChecker.fromHtmlWithoutExtraSpace(uiState.welcomeMessage)
            showChatBotWidget(welcomeMessage.toString(), uiState.unReadNotification, appLink)
        } else {
            hideChatBotWidget()
            showErrorTopChatStatus(uiState.errorMessageChatBotWidget)
            sendRecordToFirebase(uiState.exception)
        }
    }

    private fun sendRecordToFirebase(e : Exception?){
        e?.let {
            FirebaseCrashlytics.getInstance().recordException(e)
        }
    }

    private fun showErrorTopChatStatus(errorMessage: String){
        if(errorMessage.isNotEmpty()) {
            binding?.rvEmailList.showToasterErrorWithCta(
                errorMessage,
                context?.getString(R.string.contact_us_ok).orEmpty()
            )
        }
    }

    private fun handleEffect(uiEffect: InboxUiEffect) {
        hideErrorPage()
        when (uiEffect) {
            is InboxUiEffect.EmptyTicket -> {
                hideList()
                hideProgressBar()
                if (uiEffect.isFilteredData) {
                    toggleEmptyLayout(View.VISIBLE)
                } else {
                    hideFilter()
                    toggleNoTicketLayout(uiEffect.name)
                }
            }

            is InboxUiEffect.LoadNextPageSuccess -> {
                when {
                    uiEffect.isFirstPage && !uiEffect.isHasNext -> {
                        loadDataIntoRecyclerView(uiEffect)
                        notifyLoadResult(uiEffect.isHasNext)
                    }
                    uiEffect.isFirstPage -> {
                        loadDataIntoRecyclerView(uiEffect)
                        notifyLoadResult(uiEffect.isHasNext)
                    }
                    else -> {
                        notifyLoadResult(uiEffect.isHasNext)
                        mAdapter.addAll(uiEffect.currentPageItems)
                    }
                }
            }
            is InboxUiEffect.FetchInboxError -> {
                hideProgressBar()
                showErrorPage(uiEffect.throwable)
                showToastWhenNeeded(uiEffect.throwable)
            }
        }
    }

    private fun showToastWhenNeeded(throwable: Throwable) {
        if(!throwable.isInternetException() || !mAdapter.isEmpty) {
            val errorMessage = ErrorHandler.getErrorMessage(context, throwable)
            binding?.rvEmailList.showToasterErrorWithCta(
                errorMessage,
                context?.getString(R.string.contact_us_ok).orEmpty()
            )
        }
    }

    private fun showErrorPage(throwable: Throwable) {
        if(mAdapter.isEmpty) {
            binding?.viewOfContent?.hide()
            binding?.layoutErrorGlobal?.show()
            binding?.homeGlobalError?.run {
                setType(getTypeOfErrorGlobal(throwable))
            }
        }
    }

    private fun getTypeOfErrorGlobal(throwable: Throwable): Int {
        return if(throwable.isInternetException()){
            GlobalError.NO_CONNECTION
        } else {
            GlobalError.SERVER_ERROR
        }
    }

    private fun Throwable.isInternetException() : Boolean {
        return  this is SocketException || this is SocketTimeoutException || this is UnknownHostException
    }

    private fun hideErrorPage(){
        binding?.viewOfContent?.show()
        binding?.layoutErrorGlobal?.hide()
    }

    private fun loadDataIntoRecyclerView(data : InboxUiEffect.LoadNextPageSuccess){
        showFilter()
        toggleEmptyLayout(View.GONE)
        mAdapter.clear()
        mAdapter.addAll(data.currentPageItems)
        hideProgressBar()
        showList()
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
                    rvEmailList?.scrollBy(0, 0)
                }

                override fun showSerVicePriorityBottomSheet() {
                    servicePriorityBottomSheet()
                }

                override fun onClickTicket(index: Int, isOfficialStore: Boolean) {
                    val itemTicket = viewModel.getItemTicketOnPosition(index)
                    val ticketId = itemTicket.id.orEmpty()
                    sendTrackingClickToDetailTicketMessage(index)
                    goToInboxDetail(ticketId, isOfficialStore)
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
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventTicketClick,
            itemTicket.caseNumber.orEmpty()
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

    private fun settingClickListenerOfErrorPage(){
        binding?.layoutErrorGlobal?.run {
            binding?.homeGlobalError?.run {
                errorSecondaryAction.hide()
                setActionClickListener {
                    showProgressBar()
                    viewModel.getTopBotStatus()
                    viewModel.getTicketItems()
                }
            }
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
        servicePrioritiesBottomSheet = ServicePrioritiesBottomSheet()
        servicePrioritiesBottomSheet?.setCloseButtonListener(this)
        servicePrioritiesBottomSheet?.show(parentFragmentManager, "servicePrioritiesBottomSheet")
    }

    override fun onClickClose() {
        servicePrioritiesBottomSheet?.dismiss()
    }

    private fun toggleNoTicketLayout(name: String) {
        ivNoTicket?.loadRemoteImageDrawable("no_messages.png")
        ivNoTicket?.show()
        tvNoTicket?.text = getString(R.string.contact_us_no_ticket_message)
        tvNoTicket?.show()
        tvRaiseTicket?.text = getString(R.string.contact_us_tokopedia_care)
        tvRaiseTicket?.tag = RAISE_TICKET_TAG
        tvRaiseTicket?.show()
        tvGreetNoTicket?.text = String.format(getString(R.string.contact_us_greet_user), name)
        tvGreetNoTicket?.show()
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
        val tolTipMessage = getWelcomeMessage(welcomeMessage)
        chatWidget?.setToolTipDescription(tolTipMessage)
        chatWidget?.setToolTipButtonLister(object : ChatWidgetToolTip.ChatWidgetToolTipListener {
            override fun onClickToolTipButton() {
                sendGTMClickChatButton()
                RouteManager.route(context ?: return, applink)
            }
        })
        showChatBotWidgetNotification(isHasUnReadNotification)
    }

    private fun getWelcomeMessage(message : String) : String {
        return message.ifEmpty {
            getString(R.string.tool_tip_tanya_default_value)
        }
    }

    private fun showChatBotWidgetNotification(isShowNotification: Boolean) {
        chatWidgetNotification?.showWithCondition(isShowNotification)
    }

    private fun hideChatBotWidget() {
        chatWidget?.hide()
    }

    private fun raiseTicket() {
        if (tvRaiseTicket?.tag == RAISE_TICKET_TAG) {
            routeOnEmptyPage()
        } else {
            viewModel.autoPickShowAllOptionsFilter()
            viewModel.restartPageOfList()
            viewModel.getTicketItems()
        }
    }

    private fun routeOnEmptyPage(){
        ContactUsTracking.sendGTMInboxTicket(
            "",
            InboxTicketTracking.Category.EventInboxTicket,
            InboxTicketTracking.Action.EventClickHubungi,
            InboxTicketTracking.Label.InboxEmpty
        )
        if(isFromTokopediaHelp) {
            activity?.finish()
        } else {
            ContactUsHomeActivity.start(context?:requireContext())
        }
    }

    private fun sendGTMClickChatButton() {
        ContactUsTracking.sendGTMInboxTicket(
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventClickChatbotButton,
            ""
        )
    }

    private fun sendGtmClickTicketFilter(selected: String) {
        ContactUsTracking.sendGTMInboxTicket(
            InboxTicketTracking.Event.Event,
            InboxTicketTracking.Category.EventCategoryInbox,
            InboxTicketTracking.Action.EventClickTicketFilter,
            selected
        )
    }

    private fun showProgressBar() {
        binding?.progressBarLayout?.show()
    }

    private fun hideProgressBar() {
        binding?.progressBarLayout?.gone()
    }

    private fun goToInboxDetail(ticketId : String, isOfficialStore: Boolean) {
        val detailIntent =
            getIntent(context ?: return, ticketId, isOfficialStore)
        toTicketFeedBack.launch(detailIntent)
    }

    private fun getInboxDetailResultActivityLauncher() : ActivityResultLauncher<Intent> {
        return registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            if (it.resultCode == RESULT_FINISH) {
                routeToHomeContactUs()
            }
        }
    }

    private fun routeToHomeContactUs(){
        activity?.startActivity(
            Intent(
                context,
                ContactUsHomeActivity::class.java
            ).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        )
        activity?.finish()
    }

    override fun onDestroy() {
        viewModel.flush()
        super.onDestroy()
    }
}
