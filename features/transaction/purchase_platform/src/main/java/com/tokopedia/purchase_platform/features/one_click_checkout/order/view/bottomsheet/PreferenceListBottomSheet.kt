package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.GetPreferenceListUseCase
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.PreferenceListResponseModel
import com.tokopedia.purchase_platform.features.one_click_checkout.common.domain.model.preference.ProfilesItemModel
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view.PreferenceListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottom_sheet_preference_list.view.*
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PreferenceListBottomSheet(
        private val getPreferenceListUseCase: GetPreferenceListUseCase,
        private val listener: PreferenceListBottomSheetListener) {

    private var bottomSheet: BottomSheetUnify? = null

    private var rvPreferenceList: RecyclerView? = null
    private var btnAddPreference: UnifyButton? = null
    private var progressBar: ProgressBar? = null
    private var globalError: GlobalError? = null

    private var adapter: PreferenceListAdapter? = null

    private fun getPreferenceList() {
        globalError?.gone()
        rvPreferenceList?.gone()
        btnAddPreference?.gone()
        progressBar?.visible()
        getPreferenceListUseCase.execute({ preferenceListResponseModel: PreferenceListResponseModel ->
            updateList(preferenceListResponseModel)
        }, { throwable: Throwable ->
            throwable.printStackTrace()
            handleError(throwable)
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
                    }
                }
            }
            else -> {
                showGlobalError(GlobalError.SERVER_ERROR)
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
                setTitle(BOTTOM_SHEET_TITLE)
                val child = View.inflate(fragment.context, R.layout.bottom_sheet_preference_list, null)
                setupChild(child, profileId)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
                getPreferenceList()
            }
        }
    }

    private fun setupChild(child: View, profileId: Int) {
        rvPreferenceList = child.findViewById(R.id.rv_preference_list)
        btnAddPreference = child.findViewById(R.id.btn_add_preference)
        progressBar = child.findViewById(R.id.progress_bar)
        globalError = child.findViewById(R.id.global_error)

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
            bottomSheet?.dismiss()
            listener.onChangePreference(preference)
        }

        override fun onPreferenceEditClicked(preference: ProfilesItemModel, position: Int, profileSize: Int) {
            bottomSheet?.dismiss()
            listener.onEditPreference(preference, position, profileSize)
        }
    }

    fun dismiss() {
        bottomSheet?.dismiss()
    }

    private fun updateList(preferences: PreferenceListResponseModel) {
        adapter?.submitList(preferences.profiles)
        progressBar?.gone()
        rvPreferenceList?.visible()
        if (preferences.profiles?.size ?: 0 >= preferences.maxProfile) {
            btnAddPreference?.visible()
            btnAddPreference?.isEnabled = false
        } else {
            btnAddPreference?.visible()
            btnAddPreference?.isEnabled = true
        }
    }

    interface PreferenceListBottomSheetListener {

        fun onChangePreference(preference: ProfilesItemModel)

        fun onEditPreference(preference: ProfilesItemModel, position: Int, profileSize: Int)

        fun onAddPreference(itemCount: Int)
    }

    companion object {
        private const val BOTTOM_SHEET_TITLE = "Pengiriman dan pembayaran"
    }
}