package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.data.entity.address.RecipientAddressModel
import com.tokopedia.logisticCommon.ui.shareaddress.ShareAddressBottomSheet
import com.tokopedia.logisticCommon.ui.shareaddress.ShareAddressBottomSheet.Companion.TAG_SHARE_ADDRESS
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressActionState
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressListState
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressItemAdapter
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.FragmentFromFriendBinding
import com.tokopedia.manageaddress.ui.manageaddress.fromfriend.FromFriendViewModel.Companion.FIRST_PAGE
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FromFriendFragment : BaseDaggerFragment(),
    ManageAddressItemAdapter.FromFriendAddressItemAdapterListener {

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: FromFriendViewModel by lazy {
        ViewModelProvider(this, viewModelFactory)[FromFriendViewModel::class.java]
    }

    private val adapter = ManageAddressItemAdapter().apply {
        setFromFriendAddressListener(this@FromFriendFragment)
    }

    private var bottomSheetRequestAddress: BottomSheetUnify? = null

    private var binding by autoClearedNullable<FragmentFromFriendBinding>()

    override fun initInjector() {
        getComponent(ManageAddressComponent::class.java).inject(this)
    }

    override fun getScreenName(): String = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentFromFriendBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setChosenAddrId()
        initRequestAddressView()
        initRecyclerView()
        initObserver()
        searchFromFriendAddressList()
    }

    private fun setChosenAddrId() {
        viewModel.chosenAddrId = getChosenAddrId()
    }

    private fun initRequestAddressView() {
        binding?.cardRequestAddress?.clRequestAddress?.setOnClickListener {
            showRequestAddressBottomSheet()
        }
    }

    private fun initRecyclerView() {
        binding?.rvAddressList?.let {
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }

        binding?.svAddressList?.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { v, _, scrollY, _, _ ->
                v?.let {
                    val currentHeight = (v.getChildAt(0).measuredHeight - v.measuredHeight)
                    if (scrollY == currentHeight) {
                        viewModel.onLoadMore("")
                    }
                }
            })
    }

    private fun initObserver() {
        viewModel.getFromFriendAddressState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is FromFriendAddressListState.Success -> {
                    binding?.run {
                        globalError.gone()
                    }
                    if (viewModel.page == FIRST_PAGE) {
                        adapter.clearData()
                        updateSaveAllButton(it.data.listAddress.isNotEmpty())
                    }
                    updateData(it.data.listAddress)
                }
                is FromFriendAddressListState.Fail -> {
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }
                is FromFriendAddressListState.Loading -> {
                    viewModel.isOnLoadingGetAddress = it.isShowLoading
                    binding?.swipeRefresh?.isRefreshing = it.isShowLoading
                }
            }
        })

        viewModel.saveAddressState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is FromFriendAddressActionState.Success -> {
                }
                is FromFriendAddressActionState.Fail -> {
                    showToaster(
                        it.errorMessage,
                        Toaster.TYPE_ERROR
                    )
                }
                is FromFriendAddressActionState.Loading -> {
                    binding?.swipeRefresh?.isRefreshing = it.isShowLoading
                }
            }
        })

        viewModel.deleteAddressState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is FromFriendAddressActionState.Success -> {
                }
                is FromFriendAddressActionState.Fail -> {
                    showToaster(
                        it.errorMessage,
                        Toaster.TYPE_ERROR
                    )
                }
                is FromFriendAddressActionState.Loading -> {
                    binding?.swipeRefresh?.isRefreshing = it.isShowLoading
                }
            }
        })
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                view?.let {
                    showGlobalError(GlobalError.NO_CONNECTION)
                }
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                        GlobalError.NO_CONNECTION
                    )
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)

                    else -> {
                        view?.let {
                            showGlobalError(GlobalError.SERVER_ERROR)
                            showToaster(
                                ManageAddressConstant.DEFAULT_ERROR_MESSAGE,
                                Toaster.TYPE_ERROR
                            )
                        }
                    }
                }
            }
            else -> {
                view?.let {
                    showGlobalError(GlobalError.SERVER_ERROR)
                    showToaster(
                        throwable.message ?: ManageAddressConstant.DEFAULT_ERROR_MESSAGE,
                        Toaster.TYPE_ERROR
                    )
                }
            }
        }
    }

    private fun showGlobalError(type: Int) {
        binding?.run {
            globalError.setType(type)
            globalError.setActionClickListener {
                context?.let {
                    searchFromFriendAddressList()
                }
            }
            rvAddressList.gone()
            globalError.visible()
        }
    }

    private fun showToaster(
        errorMessage: String,
        toastType: Int,
        actionText: String = "",
        onClickListener: (() -> Unit)? = null
    ) {
        view?.let {
            Toaster.build(it, errorMessage, Toaster.LENGTH_SHORT, type = toastType,
                actionText = actionText, clickListener = {
                    onClickListener?.invoke()
                }).show()
        }
    }

    private fun updateData(data: List<RecipientAddressModel>) {
        adapter.addList(data)
    }

    private fun updateSaveAllButton(isVisible: Boolean) {
        binding?.viewBtnSaveAll?.apply {
            if (isVisible) {
                visible()
            } else {
                gone()
            }
        }
    }

    private fun searchFromFriendAddressList() {
        if(arguments != null) {
            val searchQuery = requireArguments().getString(ARG_EXTRA_SEARCH_QUERY, "")
            viewModel.onSearchAdrress(searchQuery)
        }
    }

    private fun getChosenAddrId(): Long {
        val localChosenAddr = context?.let { ChooseAddressUtils.getLocalizingAddressData(it) }
        var chosenAddrId: Long = 0
        localChosenAddr?.address_id?.let { localAddrId ->
            if (localAddrId.isNotEmpty()) {
                localChosenAddr.address_id.toLong().let { id ->
                    chosenAddrId = id
                }
            }
        }
        return chosenAddrId
    }

    override fun onSaveAddressSharedClicked(peopleAddress: RecipientAddressModel) {
        viewModel.saveAddress(peopleAddress.id)
    }

    override fun onDeleteAddressSharedClicked(peopleAddress: RecipientAddressModel) {
        viewModel.deleteAddress(peopleAddress.id)
        showToaster(
            getString(R.string.success_delete_address),
            Toaster.TYPE_NORMAL,
            getString(R.string.action_cancel_delete_address)
        ) {
            viewModel.isCancelDelete = true
        }
    }

    private fun showRequestAddressBottomSheet() {
        val requestAddressListener = object : ShareAddressBottomSheet.RequestAddressListener {
            override fun onSuccessRequestAddress() {
                bottomSheetRequestAddress?.dismiss()
            }
        }

        bottomSheetRequestAddress = ShareAddressBottomSheet.newInstance(
            isRequestAddress = true,
            requestAddressListener = requestAddressListener
        )
        bottomSheetRequestAddress?.show(parentFragmentManager, TAG_SHARE_ADDRESS)
    }

    companion object {
        private const val ARG_EXTRA_SEARCH_QUERY = "ARG_EXTRA_SEARCH_QUERY"

        fun newInstance(searchQuery: String?): FromFriendFragment {
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_SEARCH_QUERY, searchQuery ?: "")

            return FromFriendFragment().apply {
                arguments = bundle
            }
        }
    }
}