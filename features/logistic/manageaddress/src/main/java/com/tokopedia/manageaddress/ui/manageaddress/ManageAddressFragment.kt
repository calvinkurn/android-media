package com.tokopedia.manageaddress.ui.manageaddress

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.ApplinkConst
import com.tokopedia.applink.RouteManager
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.data.analytics.ShareAddressAnalytics
import com.tokopedia.manageaddress.databinding.FragmentManageAddressBinding
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.model.TickerModel
import com.tokopedia.manageaddress.ui.manageaddress.fromfriend.FromFriendFragment
import com.tokopedia.manageaddress.ui.manageaddress.mainaddress.MainAddressFragment
import com.tokopedia.manageaddress.ui.uimodel.ValidateShareAddressState
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_QUERY
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.setCustomText
import com.tokopedia.unifycomponents.ticker.TickerData
import com.tokopedia.unifycomponents.ticker.TickerPagerAdapter
import com.tokopedia.unifycomponents.ticker.TickerPagerCallback
import com.tokopedia.usecase.coroutines.Fail
import com.tokopedia.usecase.coroutines.Success
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.utils.lifecycle.autoClearedNullable
import javax.inject.Inject

/**
 * ManageAddressFragment
 * Fragment that hold tab layout and viewpager
 * inside it have MainAddressFragment and FromFriendFragment
 */
class ManageAddressFragment :
    BaseDaggerFragment(),
    SearchInputView.Listener,
    FromFriendFragment.Listener,
    MainAddressFragment.MainAddressListener {

    companion object {
        private const val MAIN_ADDRESS_FRAGMENT_POSITION = 0
        private const val FROM_FRIEND_FRAGMENT_POSITION = 1
        private const val DELAY_SWIPE_VIEW_PAGER = 50L

        fun newInstance(bundle: Bundle): ManageAddressFragment {
            return ManageAddressFragment().apply {
                arguments = bundle
            }
        }
    }

    @Inject
    lateinit var userSession: UserSessionInterface

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: ManageAddressViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[ManageAddressViewModel::class.java]
    }

    private var binding by autoClearedNullable<FragmentManageAddressBinding>()
    private var bottomSheetLainnya: BottomSheetUnify? = null

    private var manageAddressListener: ManageAddressListener? = null

    private var tabAdapter: ManageAddressViewPagerAdapter? = null

    private var isFirstLoad = true

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ManageAddressComponent::class.java).inject(this)
    }

    override fun onAttachFragment(fragment: Fragment) {
        when (fragment) {
            is MainAddressFragment -> fragment.setListener(this)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentManageAddressBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setupDataFromArgument(arguments)
        observeTickerState()
        viewModel.setupTicker()
        if (viewModel.isNeedValidateShareAddress) {
            observerValidateShareAddress()
            viewModel.doValidateShareAddress()
        } else {
            bindView()
        }
    }

    private fun observeTickerState() {
        viewModel.tickerState.observe(viewLifecycleOwner) {
            when (it) {
                is Success -> {
                    showTicker(it.data.item)
                }
                is Fail -> {
                    binding?.tickerManageAddress?.gone()
                }
            }
        }
    }

    private fun showTicker(tickerItem: List<TickerModel.TickerItem>) {
        if (tickerItem.isNotEmpty()) {
            val message = ArrayList<TickerData>()
            for (item in tickerItem) {
                message.add(TickerData(item.title, item.content, item.type, true, item.linkUrl))
            }
            val tickerPageAdapter = TickerPagerAdapter(context, message)
            tickerPageAdapter.setPagerDescriptionClickEvent(object : TickerPagerCallback {
                override fun onPageDescriptionViewClick(linkUrl: CharSequence, itemData: Any?) {
                    val appLink = linkUrl.toString()
                    if (appLink.startsWith("tokopedia")) {
                        startActivity(RouteManager.getIntent(context, appLink))
                    } else {
                        RouteManager.route(
                            context,
                            String.format("%s?url=%s", ApplinkConst.WEBVIEW, appLink)
                        )
                    }
                }
            })
            binding?.tickerManageAddress?.addPagerView(tickerPageAdapter, message)
            binding?.tickerManageAddress?.visible()
        } else {
            binding?.tickerManageAddress?.gone()
        }
    }

    private fun observerValidateShareAddress() {
        viewModel.validateShareAddressState.observe(viewLifecycleOwner) {
            when (it) {
                is ValidateShareAddressState.Success -> {
                    it.receiverUserName?.takeIf { receiverUserName -> receiverUserName.isNotBlank() }
                        ?.apply {
                            arguments?.putString(
                                ManageAddressConstant.EXTRA_RECEIVER_USER_NAME,
                                this
                            )
                        }
                    bindView()
                }
                is ValidateShareAddressState.Fail -> {
                    if (viewModel.isNeedToShareAddress) {
                        viewModel.receiverUserId = null
                        arguments?.putString(ManageAddressConstant.QUERY_PARAM_RUID, null)
                    } else {
                        arguments?.putBoolean(
                            ManageAddressConstant.EXTRA_SHARE_ADDRESS_FROM_NOTIF,
                            true
                        )
                    }
                    bindView()
                }
                is ValidateShareAddressState.Loading -> {
                    binding?.apply {
                        if (it.isShowLoading) {
                            llMainView.gone()
                            progressBar.visible()
                        } else {
                            llMainView.visible()
                            progressBar.gone()
                        }
                    }
                }
            }
        }
    }

    private fun bindView() {
        binding?.apply {
            searchInputView.run {
                searchBarTextField.setOnClickListener {
                    searchBarTextField.isCursorVisible = true
                    openSoftKeyboard()
                }

                searchBarTextField.setOnEditorActionListener { _, actionId, event ->
                    if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                        clearFocus()
                        performSearch(searchBarTextField.text.toString() ?: "")
                        return@setOnEditorActionListener true
                    }
                    return@setOnEditorActionListener false
                }

                clearListener = { performSearch("") }

                searchBarPlaceholder = getString(R.string.label_find_address)
            }

            val fragments = fragmentPage()
            tabAdapter = ManageAddressViewPagerAdapter(this@ManageAddressFragment, fragments)
            vpManageAddress.adapter = tabAdapter
            vpManageAddress.offscreenPageLimit = fragments.size
            vpManageAddress.isUserInputEnabled = false
            if (viewModel.isEligibleShareAddress.not()) {
                tlManageAddress.gone()
            } else if (viewModel.isNeedToShareAddress) {
                tlManageAddress.gone()
                manageAddressListener?.setToolbarTitle(
                    getString(R.string.title_select_share_address),
                    false
                )
            } else {
                tlManageAddress.visible()
                TabsUnifyMediator(tlManageAddress, vpManageAddress) { tab, position ->
                    tab.setCustomText(
                        fragments.getOrNull(position)?.first
                            ?: getString(R.string.tablayout_label_main)
                    )
                }

                vpManageAddress.registerOnPageChangeCallback(object :
                        ViewPager2.OnPageChangeCallback() {
                        override fun onPageSelected(position: Int) {
                            if (isFirstLoad) {
                                isFirstLoad = false
                            } else {
                                if (position == MAIN_ADDRESS_FRAGMENT_POSITION) {
                                    ShareAddressAnalytics.onClickMainTab()
                                } else {
                                    ShareAddressAnalytics.onClickFromFriendTab()
                                }
                            }
                        }
                    })

                if (viewModel.isReceiveShareAddress) {
                    Handler(Looper.getMainLooper()).postDelayed({
                        binding?.vpManageAddress?.currentItem = FROM_FRIEND_FRAGMENT_POSITION
                    }, DELAY_SWIPE_VIEW_PAGER)
                }
            }

            searchInputView.searchBarTextField.setText(viewModel.savedQuery)
        }
    }

    override fun onSearchSubmitted(text: String) {
        performSearch(text)
    }

    override fun onSearchTextChanged(text: String?) {
        openSoftKeyboard()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        bottomSheetLainnya = null
    }

    private fun openSoftKeyboard() {
        binding?.searchInputView?.searchBarTextField?.let {
            (activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager)?.showSoftInput(
                it,
                InputMethodManager.SHOW_IMPLICIT
            )
        }
    }

    /**
     *  call to main address fragment by Activity with callback
     */
    private fun performSearch(query: String) {
        viewModel.savedQuery = query
        tabAdapter?.updateData(fragmentPage())
    }

    private fun fragmentPage(): List<Pair<String, Fragment>> {
        return if (viewModel.isEligibleShareAddress.not() || viewModel.isNeedToShareAddress) {
            listOf(
                Pair(
                    getString(R.string.tablayout_label_main),
                    MainAddressFragment.newInstance(bundleData())
                )
            )
        } else {
            listOf(
                Pair(
                    getString(R.string.tablayout_label_main),
                    MainAddressFragment.newInstance(bundleData())
                ),
                Pair(
                    getString(R.string.tablayout_label_from_friend),
                    FromFriendFragment.newInstance(bundleData(), this)
                )
            )
        }
    }

    private fun bundleData(): Bundle {
        val bundle = Bundle()
        if (arguments != null) {
            bundle.putString(EXTRA_QUERY, viewModel.savedQuery)
            bundle.putAll(arguments)
        }
        return bundle
    }

    fun setListener(listener: ManageAddressListener) {
        this.manageAddressListener = listener
    }

    interface ManageAddressListener {
        fun setAddButtonOnClickListener(onClick: () -> Unit)
        fun setSearch(query: String, saveAddressDataModel: SaveAddressDataModel?)
        fun setToolbarTitle(title: String, isBtnAddVisible: Boolean)
    }

    fun searchInputVisibility(show: Boolean) {
        if (show) {
            binding?.searchInputView?.visible()
        } else {
            binding?.searchInputView?.gone()
        }
    }

    override fun removeArgumentsFromNotif() {
        arguments?.putBoolean(ManageAddressConstant.EXTRA_SHARE_ADDRESS_FROM_NOTIF, false)
    }

    override fun onSuccessSaveShareAddress() {
        binding?.apply {
            vpManageAddress.currentItem = MAIN_ADDRESS_FRAGMENT_POSITION
            viewModel.savedQuery = ""
            searchInputView.searchBarTextField.setText(viewModel.savedQuery)
        }

        performSearch(viewModel.savedQuery)
    }

    override fun updateFromFriendsTabText(count: Int) {
        binding?.tlManageAddress?.tabLayout?.getTabAt(FROM_FRIEND_FRAGMENT_POSITION)?.apply {
            customView?.findViewById<TextView>(com.tokopedia.unifycomponents.R.id.tab_item_text_id)
                ?.apply {
                    text = if (count > 0) {
                        getString(R.string.tablayout_label_from_friend_with_value, count.toString())
                    } else {
                        getString(R.string.tablayout_label_from_friend)
                    }
                    ellipsize = null
                }
        }
    }

    override fun setAddButtonOnClickListener(onClick: () -> Unit) {
        manageAddressListener?.setAddButtonOnClickListener {
            onClick()
        }
    }
}
