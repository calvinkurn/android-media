package com.tokopedia.manageaddress.ui.manageaddress.fromfriend

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.applink.internal.ApplinkConstInternalLogistic.PARAM_SOURCE
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.isVisible
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.manageaddress.data.analytics.ShareAddressAnalytics
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressBottomSheet
import com.tokopedia.manageaddress.ui.shareaddress.bottomsheets.ShareAddressBottomSheet.Companion.TAG_SHARE_ADDRESS
import com.tokopedia.manageaddress.di.ManageAddressComponent
import com.tokopedia.manageaddress.ui.uimodel.FromFriendAddressActionState
import com.tokopedia.manageaddress.ui.uimodel.FromFriendAddressListState
import com.tokopedia.manageaddress.ui.manageaddress.ManageAddressItemAdapter
import com.tokopedia.manageaddress.util.ManageAddressConstant
import com.tokopedia.manageaddress.R
import com.tokopedia.manageaddress.databinding.FragmentFromFriendBinding
import com.tokopedia.media.loader.loadImage
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.Toaster
import com.tokopedia.utils.lifecycle.autoClearedNullable
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject

class FromFriendFragment :
    BaseDaggerFragment(),
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
        initArguments()
        bindView()
        observeGetFromFriendAddressList()
        observeSaveAddress()
        observeDeleteAddress()
        viewModel.getFromFriendAddressList()
    }

    private fun initArguments() {
        viewModel.isShareAddressFromNotif =
            arguments?.getBoolean(ManageAddressConstant.EXTRA_SHARE_ADDRESS_FROM_NOTIF, false)
                ?: false
        viewModel.source = arguments?.getString(PARAM_SOURCE, "") ?: ""
    }

    private fun bindView() {
        binding?.apply {
            cbAllAddress.setOnCheckedChangeListener { _, isChecked ->
                if (viewModel.isNeedUpdateAllList) {
                    ShareAddressAnalytics.onCheckAllAddress(isChecked)
                    viewModel.setAllListSelected(isChecked)
                    refreshListAndButton()
                } else {
                    viewModel.isNeedUpdateAllList = true
                }
            }

            cardRequestAddress.clRequestAddress.setOnClickListener {
                ShareAddressAnalytics.onClickRequestAddress()
                showRequestAddressBottomSheet()
            }

            rvAddressList.let {
                adapter.initAddressList(viewModel.addressList)
                it.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                it.adapter = adapter
            }

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

    @SuppressLint("NotifyDataSetChanged")
    private fun observeGetFromFriendAddressList() {
        viewModel.getFromFriendAddressState.observe(viewLifecycleOwner) {
            when (it) {
                is FromFriendAddressListState.Success -> {
                    binding?.apply {
                        totalAddressTicker.apply {
                            it.data?.message?.takeIf { it.isNotBlank() }?.apply {
                                visible()
                                setTextDescription(this)
                            } ?: kotlin.run {
                                gone()
                            }
                        }

                        groupCardAndList.visible()
                        if (viewModel.isShareAddressFromNotif && viewModel.addressList.isEmpty()) {
                            groupBottomView.gone()
                            globalError.apply {
                                visible()
                                setType(GlobalError.MAINTENANCE)
                                errorIllustration.loadImage(IMAGE_SHARE_ADDRESS)
                                errorIllustration.adjustViewBounds = true
                                errorTitle.text =
                                    getString(R.string.title_failed_saved_share_address_from_notif)
                                errorDescription.text =
                                    getString(R.string.description_failed_saved_share_address_from_notif)
                            }
                        } else {
                            globalError.gone()
                            groupBottomView.visible()
                        }
                    }

                    adapter.notifyDataSetChanged()
                    mListener?.updateFromFriendsTabText(it.data?.numberOfRequest ?: 0)
                }
                is FromFriendAddressListState.Fail -> {
                    if (it.throwable != null) {
                        handleError(it.throwable)
                    }
                }
                is FromFriendAddressListState.Loading -> {
                    binding?.apply {
                        swipeRefresh.isRefreshing = it.isShowLoading
                        cbAllAddress.isVisible =
                            viewModel.isHaveAddressList && it.isShowLoading.not()
                    }
                }
            }
        }
    }

    private fun observeSaveAddress() {
        viewModel.saveAddressState.observe(viewLifecycleOwner) {
            when (it) {
                is FromFriendAddressActionState.Success -> {
                    ShareAddressAnalytics.onClickSaveButton(isSuccess = true)
                    showToaster(it.message)
                    mListener?.apply {
                        removeArgumentsFromNotif()
                        onSuccessSaveShareAddress()
                    }
                }
                is FromFriendAddressActionState.Fail -> {
                    ShareAddressAnalytics.onClickSaveButton(isSuccess = false)
                    showToaster(it.errorMessage, Toaster.TYPE_ERROR)
                }
                is FromFriendAddressActionState.Loading -> {
                    binding?.btnSave?.isLoading = it.isShowLoading
                }
            }
        }
    }

    private fun observeDeleteAddress() {
        viewModel.deleteAddressState.observe(viewLifecycleOwner) {
            when (it) {
                is FromFriendAddressActionState.Success -> {
                    ShareAddressAnalytics.onClickDeleteButton(isSuccess = true)
                    if (viewModel.isShareAddressFromNotif) {
                        viewModel.isShareAddressFromNotif = false
                        mListener?.removeArgumentsFromNotif()
                    }
                    viewModel.getFromFriendAddressList()
                }
                is FromFriendAddressActionState.Fail -> {
                    ShareAddressAnalytics.onClickDeleteButton(isSuccess = false)
                    refreshListAndButton()
                    showToaster(it.errorMessage, Toaster.TYPE_ERROR)
                }
                is FromFriendAddressActionState.Loading -> refreshListAndButton()
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun refreshListAndButton() {
        binding?.cbAllAddress?.isVisible = viewModel.isHaveAddressList
        adapter.notifyDataSetChanged()
        updateButton()
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> showGlobalError(
                GlobalError.NO_CONNECTION
            )
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(
                        GlobalError.NO_CONNECTION
                    )
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                        showGlobalError(GlobalError.SERVER_ERROR)
                        showToaster(
                            ManageAddressConstant.DEFAULT_ERROR_MESSAGE,
                            Toaster.TYPE_ERROR
                        )
                    }
                }
            }
            else -> {
                showGlobalError(GlobalError.SERVER_ERROR)
                showToaster(
                    throwable.message ?: ManageAddressConstant.DEFAULT_ERROR_MESSAGE,
                    Toaster.TYPE_ERROR
                )
            }
        }
    }

    private fun showGlobalError(type: Int) {
        binding?.run {
            globalError.setType(type)
            globalError.setActionClickListener {
                context?.let {
                    viewModel.getFromFriendAddressList()
                }
            }
            groupCardAndList.gone()
            globalError.visible()
            groupBottomView.gone()
        }
    }

    private fun showToaster(
        message: String,
        toastType: Int = Toaster.TYPE_NORMAL,
        actionText: String = "",
        onClickListener: (() -> Unit)? = null
    ) {
        view?.let {
            Toaster.build(
                view = it,
                text = message,
                duration = Toaster.LENGTH_SHORT,
                type = toastType,
                actionText = actionText,
                clickListener = {
                    onClickListener?.invoke()
                }
            ).show()
        }
    }

    private fun updateButton() {
        val checkedAddressSize = viewModel.selectedAddressList.size
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

    override fun onCheckedChangeListener(index: Int, isChecked: Boolean) {
        viewModel.onCheckedAddress(index, isChecked)

        binding?.cbAllAddress?.apply {
            if (this.isChecked != viewModel.isAllSelected) {
                viewModel.isNeedUpdateAllList = false
                this.isChecked = viewModel.isAllSelected
            }
        }

        updateButton()
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
            requestAddressListener = requestAddressListener,
            source = viewModel.source
        )
        bottomSheetRequestAddress?.show(parentFragmentManager, TAG_SHARE_ADDRESS)
    }

    interface Listener {
        fun updateFromFriendsTabText(count: Int)

        fun onSuccessSaveShareAddress()

        fun removeArgumentsFromNotif()
    }

    companion object {
        private const val IMAGE_SHARE_ADDRESS =
            "https://images.tokopedia.net/img/android/share_address/share_address_image.png"

        fun newInstance(bundle: Bundle, listener: Listener): FromFriendFragment {
            return FromFriendFragment().apply {
                arguments = bundle
                mListener = listener
            }
        }
    }
}
