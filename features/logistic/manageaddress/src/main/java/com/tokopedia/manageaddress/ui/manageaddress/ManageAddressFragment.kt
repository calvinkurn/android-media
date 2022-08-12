package com.tokopedia.manageaddress.ui.manageaddress

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.design.text.SearchInputView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.analytics.ChooseAddressTracking
import com.tokopedia.logisticCommon.data.constant.AddressConstant.ANA_REVAMP_FEATURE_ID
import com.tokopedia.logisticCommon.data.entity.address.SaveAddressDataModel
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.FragmentManageAddressBinding
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.ui.manageaddress.fromfriend.FromFriendFragment
import com.tokopedia.manageaddress.ui.manageaddress.mainaddress.MainAddressFragment
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.util.ManageAddressConstant.DEFAULT_ERROR_MESSAGE
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_QUERY
import com.tokopedia.manageaddress.util.ManageAddressConstant.EXTRA_REF
import com.tokopedia.manageaddress.util.ManageAddressConstant.KERO_TOKEN
import com.tokopedia.manageaddress.util.ManageAddressConstant.REQUEST_CODE_PARAM_CREATE
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_CART_EXISTING_USER
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_CHOOSE_ADDRESS_EXISTING_USER
import com.tokopedia.manageaddress.util.ManageAddressConstant.SCREEN_NAME_USER_NEW
import com.tokopedia.purchase_platform.common.constant.CheckoutConstant
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.TabsUnifyMediator
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.unifycomponents.setCustomText
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
class ManageAddressFragment : BaseDaggerFragment(), SearchInputView.Listener,
    FromFriendFragment.Listener {

    companion object {
        private const val MAIN_ADDRESS_FRAGMENT_POSITION = 0
        private const val FROM_FRIEND_FRAGMENT_POSITION = 1

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

    private var isFromCheckoutChangeAddress: Boolean? = false

    private var isLocalization: Boolean? = false

    private var source: String = ""

    var tabAdapter: ManageAddressViewPagerAdapter? = null

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(ManageAddressComponent::class.java).inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentManageAddressBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initHeader()
        initViewModel()

        isFromCheckoutChangeAddress = arguments?.getBoolean(CheckoutConstant.EXTRA_IS_FROM_CHECKOUT_CHANGE_ADDRESS)
        isLocalization = arguments?.getBoolean(ManageAddressConstant.EXTRA_IS_LOCALIZATION)
        viewModel.receiverUserId = arguments?.getString(ManageAddressConstant.QUERY_RECEIVER_USER_ID)
        viewModel.senderUserId = arguments?.getString(ManageAddressConstant.QUERY_SENDER_USER_ID)
        source = arguments?.getString(PARAM_SOURCE) ?: ""
        initSearchView()

        initView()
        setSearchView(viewModel.savedQuery)
    }

    private fun initView() {
        val bundle = Bundle()
        if (arguments != null && arguments != null) {
            bundle.putString(EXTRA_QUERY, viewModel.savedQuery)
            bundle.putAll(arguments)
        }

        binding?.apply {
            tabAdapter = this@ManageAddressFragment?.let { ManageAddressViewPagerAdapter(it, fragmentPage()) }
            vpManageAddress.adapter = tabAdapter
            vpManageAddress.isUserInputEnabled = false
            if (viewModel.isNeedToShareAddress) {
                tlManageAddress.gone()
            } else {
                tlManageAddress.visible()
                TabsUnifyMediator(tlManageAddress, vpManageAddress) { tab, position ->
                    tab.setCustomText(fragmentPage().getOrNull(position)?.first ?: getString(R.string.tablayout_label_main))
                }

                if (viewModel.isReceiveShareAddress) {
                    vpManageAddress.currentItem = FROM_FRIEND_FRAGMENT_POSITION
                }
            }
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
                it, InputMethodManager.SHOW_IMPLICIT
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
        return if (viewModel.isNeedToShareAddress) {
            listOf(Pair(getString(R.string.tablayout_label_main), MainAddressFragment.newInstance(bundleData())))
        } else {
            listOf(
                Pair(getString(R.string.tablayout_label_main), MainAddressFragment.newInstance(bundleData())),
                Pair(getString(R.string.tablayout_label_from_friend), FromFriendFragment.newInstance(bundleData(), this))
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

    private fun initHeader() {
        manageAddressListener?.setAddButtonOnClickListener {
            if (isLocalization == true) ChooseAddressTracking.onClickButtonTambahAlamat(userSession.userId)
            openFormAddAddressView()
        }
    }

    private fun initViewModel() {
        viewModel.eligibleForAddressFeature.observe(viewLifecycleOwner, Observer {
            when (it) {
                is Success -> {
                    when (it.data.featureId) {
                        ANA_REVAMP_FEATURE_ID -> {
                            goToAddAddress(it.data.eligible)
                        }
                    }
                }

                is Fail -> {
                    view?.let { view ->
                        Toaster.build(
                            view, it.throwable.message
                                ?: DEFAULT_ERROR_MESSAGE, Toaster.LENGTH_SHORT, type = Toaster.TYPE_ERROR
                        ).show()
                    }
                }
            }
        })
    }

    private fun goToAddAddress(eligible: Boolean) {
        val token = viewModel.token
        val screenName = if (isFromCheckoutChangeAddress == true && isLocalization == false) {
            SCREEN_NAME_CART_EXISTING_USER
        } else if (isFromCheckoutChangeAddress == false && isLocalization == true) {
            SCREEN_NAME_CHOOSE_ADDRESS_EXISTING_USER
        } else {
            SCREEN_NAME_USER_NEW
        }
        if (eligible) {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V3)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, screenName)
            intent.putExtra(PARAM_SOURCE, source)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        } else {
            val intent = RouteManager.getIntent(context, ApplinkConstInternalLogistic.ADD_ADDRESS_V2)
            intent.putExtra(KERO_TOKEN, token)
            intent.putExtra(EXTRA_REF, screenName)
            startActivityForResult(intent, REQUEST_CODE_PARAM_CREATE)
        }
    }

    fun setSearchView(searchKey: String) {
        binding?.searchInputView?.searchBarTextField?.setText(searchKey)
    }

    private fun initSearchView() {
        binding?.searchInputView?.run {
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
    }

    private fun openFormAddAddressView() {
        viewModel.checkUserEligibilityForAnaRevamp()
    }

    fun setListener(listener: ManageAddressListener) {
        this.manageAddressListener = listener
    }

    interface ManageAddressListener {
        fun setAddButtonOnClickListener(onClick: () -> Unit)
        fun setSearch(query: String, saveAddressDataModel: SaveAddressDataModel?)
    }

    fun searchInputVisibility(show: Boolean) {
        if (show) {
            binding?.searchInputView?.visible()
        } else {
            binding?.searchInputView?.gone()
        }
    }

    override fun onSuccessSaveShareAddress() {
        binding?.vpManageAddress?.currentItem = MAIN_ADDRESS_FRAGMENT_POSITION
        viewModel.savedQuery = ""
        setSearchView(viewModel.savedQuery)
        performSearch(viewModel.savedQuery)
    }
}

