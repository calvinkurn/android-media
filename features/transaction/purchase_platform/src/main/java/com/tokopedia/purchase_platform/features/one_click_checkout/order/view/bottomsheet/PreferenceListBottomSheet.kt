package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.network.exception.MessageErrorException
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.OccGlobalEvent
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.PreferenceListResponseModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccGqlResponse
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccProfileRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.data.UpdateCartOccRequest
import com.tokopedia.purchase_platform.features.one_click_checkout.order.domain.UpdateCartOccUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageViewModel
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view.PreferenceListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottom_sheet_preference_list.view.*
import kotlinx.coroutines.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import kotlin.coroutines.CoroutineContext

class PreferenceListBottomSheet(
        override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate,
        private val viewModel: OrderSummaryPageViewModel,
        private val getPreferenceListUseCase: GetPreferenceListUseCase,
        private val updateCartOccUseCase: UpdateCartOccUseCase,
        private val updateCartOccRequest: UpdateCartOccRequest,
        private val listener: PreferenceListBottomSheetListener
) : CoroutineScope {
    // need get all preference list usecase, update selected preference usecase

    private var bottomSheet: BottomSheetUnify? = null

    private var rvPreferenceList: RecyclerView? = null
    private var btnAddPreference: UnifyButton? = null
    private var progressBar: ProgressBar? = null
    private var globalError: GlobalError? = null

    private var adapter: PreferenceListAdapter? = null

    init {
        //get all preference
//        launch {
//            delay(3000)
//            updateList(listOf(Preference(), Preference(), Preference(), Preference(), Preference()))
//        }
//        useCase.execute({ preferenceListResponseModel: PreferenceListResponseModel ->
//            updateList(preferenceListResponseModel.profiles ?: ArrayList())
//        }, { throwable: Throwable ->
//
//        })
    }

    fun getPreferenceList() {
        globalError?.gone()
        rvPreferenceList?.gone()
        btnAddPreference?.gone()
        progressBar?.visible()
        getPreferenceListUseCase.execute({ preferenceListResponseModel: PreferenceListResponseModel ->
            updateList(preferenceListResponseModel.profiles ?: ArrayList())
        }, { throwable: Throwable ->
            throwable.printStackTrace()
            handleError(throwable)
//            bottomSheet?.dismiss()
        })
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                    showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> {
                            showGlobalError(GlobalError.SERVER_ERROR)
//                            Toaster.make(it, "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
                    }
                }
            }
            else -> {
                    showGlobalError(GlobalError.SERVER_ERROR)
//                    Toaster.make(it, throwable.message
//                            ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi", type = Toaster.TYPE_ERROR)
            }
        }
    }

    private fun showGlobalError(type: Int) {
        globalError?.setType(type)
        globalError?.setActionClickListener {
            getPreferenceList()
        }
        rvPreferenceList?.gone()
        btnAddPreference?.gone()
        progressBar?.gone()
        globalError?.visible()
    }

    fun show(fragment: OrderSummaryPageFragment, profileId: Int) {
        fragment.fragmentManager?.let {
            bottomSheet?.dismiss()
            bottomSheet = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                setTitle("Pengiriman dan pembayaran")
                val child = View.inflate(fragment.context, R.layout.bottom_sheet_preference_list, null)
                setupChild(child, profileId)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
                getPreferenceList()
                setOnDismissListener {
                    onCleared()
                }
            }
        }
    }

    private fun setupChild(child: View, profileId: Int) {
        rvPreferenceList = child.rv_preference_list
        btnAddPreference = child.btn_add_preference
        progressBar = child.progress_bar
        globalError = child.global_error

        rvPreferenceList?.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        adapter = PreferenceListAdapter(getListener(), profileId)
        rvPreferenceList?.adapter = adapter
        btnAddPreference?.setOnClickListener {
            bottomSheet?.dismiss()
            listener.onAddPreference(adapter?.itemCount ?: 1)
        }
    }

    private fun getListener(): PreferenceListAdapter.PreferenceListAdapterListener = object : PreferenceListAdapter.PreferenceListAdapterListener {
        override fun onPreferenceSelected(preference: ProfilesItemModel) {
            changePreference(preference)
        }

        override fun onPreferenceEditClicked(preference: ProfilesItemModel, adapterPosition: Int) {
            bottomSheet?.dismiss()
            listener.onEditPreference(preference, adapterPosition)
        }
    }

    private fun changePreference(preference: ProfilesItemModel) {
        if (preference.profileId != null && preference.addressModel?.addressId != null && preference.shipmentModel?.serviceId != null && preference.paymentModel?.gatewayCode != null) {
            viewModel.globalEvent.value = OccGlobalEvent.Loading
            val param = updateCartOccRequest.copy(profile = UpdateCartOccProfileRequest(
                    profileId = preference.profileId.toString(),
                    addressId = preference.addressModel?.addressId.toString(),
                    serviceId = preference.shipmentModel?.serviceId ?: 0,
                    gatewayCode = preference.paymentModel?.gatewayCode ?: ""
            ))

            updateCartOccUseCase.execute(param, { updateCartOccGqlResponse: UpdateCartOccGqlResponse ->
                viewModel.globalEvent.value = OccGlobalEvent.Normal
                bottomSheet?.dismiss()
                listener.onChangePreference(preference)
            }, { throwable: Throwable ->
                throwable.printStackTrace()
                if (throwable is MessageErrorException && throwable.message != null) {
                    viewModel.globalEvent.value = OccGlobalEvent.Error(errorMessage = throwable.message ?: "Terjadi kesalahan pada server. Ulangi beberapa saat lagi")
                } else {
                    viewModel.globalEvent.value = OccGlobalEvent.Error(throwable)
                }
            })
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
        onCleared()
    }

    fun reload() {
        progressBar?.visible()
        rvPreferenceList?.gone()
        btnAddPreference?.gone()
    }

    private fun updateList(preferences: ArrayList<ProfilesItemModel>) {
        adapter?.submitList(preferences)
        progressBar?.gone()
        rvPreferenceList?.visible()
        if (preferences.size >= 5) {
            btnAddPreference?.gone()
        } else {
            btnAddPreference?.visible()
        }
    }

    private fun onCleared() {
        cancel()
    }

    interface PreferenceListBottomSheetListener {

        fun onChangePreference(preference: ProfilesItemModel)

        fun onEditPreference(preference: ProfilesItemModel, adapterPosition: Int)

        fun onAddPreference(itemCount: Int)
    }
}