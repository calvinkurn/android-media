package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.localizationchooseaddress.util.ChooseAddressUtils
import com.tokopedia.logisticCommon.ui.shareaddress.ShareAddressBottomSheet
import com.tokopedia.logisticCommon.ui.shareaddress.ShareAddressBottomSheet.Companion.TAG_SHARE_ADDRESS
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressActionState
import com.tokopedia.manageaddress.domain.model.shareaddress.FromFriendAddressListState
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressItemAdapter
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.FragmentFromFriendBinding
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

    private var mListener: Listener? = null

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
        initCbAllAddress()
        initRequestAddressView()
        initRecyclerView()
        initObserver()
        initButtonClickListener()
        doSearchAddressList()
    }

    private fun setChosenAddrId() {
        viewModel.chosenAddrId = getChosenAddrId()
    }

    private fun initCbAllAddress() {
        binding?.cbAllAddress?.setOnCheckedChangeListener { _, isChecked ->
            if (viewModel.isNeedUpdateAllList) {
                viewModel.setAllListSelected(isChecked)
                refreshListAndButton()
            } else {
                viewModel.isNeedUpdateAllList = true
            }
        }
    }

    private fun initRequestAddressView() {
        binding?.cardRequestAddress?.clRequestAddress?.setOnClickListener {
            showRequestAddressBottomSheet()
        }
    }

    private fun initRecyclerView() {
        binding?.rvAddressList?.let {
            adapter.initAddressList(viewModel.addressList)
            it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            it.adapter = adapter
        }
    }

    private fun initObserver() {
        viewModel.getFromFriendAddressState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is FromFriendAddressListState.Success -> onSuccessGetList()
                is FromFriendAddressListState.Fail -> onFailedGetList(it.throwable)
                is FromFriendAddressListState.Loading -> onLoadingGetList(it.isShowLoading)
            }
        })

        viewModel.saveAddressState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is FromFriendAddressActionState.Success -> onSuccessSaveAddress()
                is FromFriendAddressActionState.Fail -> showToaster(it.errorMessage, Toaster.TYPE_ERROR)
                is FromFriendAddressActionState.Loading -> onLoadingSaveAddress(it.isShowLoading)
            }
        })

        viewModel.deleteAddressState.observe(viewLifecycleOwner, Observer {
            when (it) {
                is FromFriendAddressActionState.Success -> onSuccessDeleteAddress()
                is FromFriendAddressActionState.Fail -> onFailedDeleteAddress(it.errorMessage)
                is FromFriendAddressActionState.Loading -> refreshListAndButton()
            }
        })
    }

    private fun refreshListAndButton() {
        refreshList()
        updateButton()
    }

    private fun onSuccessGetList() {
        binding?.apply {
            svAddressList.visible()
            globalError.gone()
        }
        refreshList()
    }

    private fun onFailedGetList(throwable: Throwable?) {
        if (throwable != null) {
            handleError(throwable)
        }
    }

    private fun onLoadingGetList(isShowLoading: Boolean) {
        binding?.apply {
            swipeRefresh.isRefreshing = isShowLoading
            cbAllAddress.isVisible = viewModel.isHaveAddressList && isShowLoading.not()
        }
    }

    private fun onSuccessSaveAddress() {
        showToaster(getString(R.string.succes_save_share_address))
        mListener?.onSuccessSaveShareAddress()
    }

    private fun onLoadingSaveAddress(isLoading: Boolean) {
        binding?.btnSave?.isLoading = isLoading
    }

    private fun onSuccessDeleteAddress() {
        doSearchAddressList()
    }

    private fun onFailedDeleteAddress(errorMessage: String) {
        refreshListAndButton()
        showToaster(errorMessage, Toaster.TYPE_ERROR)
    }

    private fun initButtonClickListener(){
        binding?.apply {
            btnDelete.setOnClickListener {
                viewModel.deleteAddress()
                showToaster(
                    message = getString(R.string.success_delete_share_address),
                    actionText = getString(R.string.action_cancel_delete_address)
                ) {
                    viewModel.onCancelDeleteAddress()
                }
            }

            btnSave.setOnClickListener {
                viewModel.saveAddress()
            }
        }
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
                    doSearchAddressList()
                }
            }
            svAddressList.gone()
            globalError.visible()
        }
    }

    private fun showToaster(
        message: String,
        toastType: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickListener: (() -> Unit)? = null
    ) {
        view?.let {
            Toaster.build(it, message, Toaster.LENGTH_SHORT, type = toastType,
                actionText = actionText, clickListener = {
                    onClickListener?.invoke()
                }).show()
        }
    }

    private fun refreshList() {
        adapter.refreshAdapter()
    }

    private fun updateButton() {
        val checkedAddressSize = viewModel.getSelectedAddressList().size
        val isEnable = checkedAddressSize > 0
        binding?.apply {
            btnDelete.isEnabled = isEnable
            btnSave.isEnabled = isEnable
            btnSave.text = if (isEnable) {
                getString(R.string.btn_save_with_total, checkedAddressSize.toString())
            } else {
                getString(R.string.btn_save)
            }
        }
    }

    private fun doSearchAddressList() {
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

    override fun onCheckedChangeListener(index: Int, isChecked: Boolean) {
        viewModel.onCheckedAddress(index, isChecked)
        updateCbAllAddress()
        updateButton()
    }

    private fun updateCbAllAddress() {
        binding?.cbAllAddress?.apply {
            if (isChecked != viewModel.isAllSelected) {
                viewModel.isNeedUpdateAllList = false
                isChecked = viewModel.isAllSelected
            }
        }
    }

    private fun showRequestAddressBottomSheet() {
        val requestAddressListener = object : ShareAddressBottomSheet.RequestAddressListener {
            override fun onSuccessRequestAddress() {
                bottomSheetRequestAddress?.dismiss()
                showToaster(getString(R.string.success_request_address))
            }
        }

        bottomSheetRequestAddress = ShareAddressBottomSheet.newInstance(
            isRequestAddress = true,
            requestAddressListener = requestAddressListener
        )
        bottomSheetRequestAddress?.show(parentFragmentManager, TAG_SHARE_ADDRESS)
    }

    interface Listener {
        fun onSuccessSaveShareAddress()
    }

    companion object {
        private const val ARG_EXTRA_SEARCH_QUERY = "ARG_EXTRA_SEARCH_QUERY"

        fun newInstance(searchQuery: String?, listener: Listener): FromFriendFragment {
            val bundle = Bundle()
            bundle.putString(ARG_EXTRA_SEARCH_QUERY, searchQuery ?: "")

            return FromFriendFragment().apply {
                arguments = bundle
                mListener = listener
            }
        }
    }
}