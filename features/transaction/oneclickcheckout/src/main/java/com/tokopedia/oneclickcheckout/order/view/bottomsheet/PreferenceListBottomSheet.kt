package com.tokopedia.oneclickcheckout.order.view.bottomsheet

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.globalerror.GlobalError
import com.tokopedia.globalerror.ReponseStatus
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.oneclickcheckout.R
import com.tokopedia.oneclickcheckout.common.domain.GetPreferenceListUseCase
import com.tokopedia.oneclickcheckout.common.idling.OccIdlingResource
import com.tokopedia.oneclickcheckout.common.view.model.preference.PreferenceListResponseModel
import com.tokopedia.oneclickcheckout.common.view.model.preference.ProfilesItemModel
import com.tokopedia.oneclickcheckout.order.view.OrderSummaryPageFragment
import com.tokopedia.oneclickcheckout.preference.edit.view.PreferenceEditActivity
import com.tokopedia.oneclickcheckout.preference.list.view.PreferenceListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifycomponents.ticker.Ticker
import timber.log.Timber
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

class PreferenceListBottomSheet(
        private val isNewFlow: Boolean,
        private val getPreferenceListUseCase: GetPreferenceListUseCase,
        private val paymentProfile: String,
        private val listener: PreferenceListBottomSheetListener) {

    private var bottomSheet: BottomSheetUnify? = null

    private var ticker: Ticker? = null
    private var rvPreferenceList: RecyclerView? = null
    private var btnAddPreference: UnifyButton? = null
    private var progressBar: LoaderUnify? = null
    private var globalError: GlobalError? = null

    private var adapter: PreferenceListAdapter? = null

    private fun getPreferenceList() {
        globalError?.gone()
        ticker?.gone()
        rvPreferenceList?.gone()
        btnAddPreference?.gone()
        progressBar?.visible()
        OccIdlingResource.increment()
        getPreferenceListUseCase.execute({ preferenceListResponseModel: PreferenceListResponseModel ->
            updateList(preferenceListResponseModel)
            OccIdlingResource.decrement()
        }, { throwable: Throwable ->
            Timber.d(throwable)
            handleError(throwable)
            OccIdlingResource.decrement()
        }, getPreferenceListUseCase.generateRequestParams(paymentProfile, PreferenceEditActivity.FROM_FLOW_OSP_STRING))
    }

    private fun handleError(throwable: Throwable) {
        when (throwable) {
            is SocketTimeoutException, is UnknownHostException, is ConnectException -> {
                showGlobalError(GlobalError.NO_CONNECTION)
            }
            is RuntimeException -> {
                when (throwable.localizedMessage?.toIntOrNull()) {
                    ReponseStatus.GATEWAY_TIMEOUT, ReponseStatus.REQUEST_TIMEOUT -> showGlobalError(GlobalError.NO_CONNECTION)
                    ReponseStatus.NOT_FOUND -> showGlobalError(GlobalError.PAGE_NOT_FOUND)
                    ReponseStatus.INTERNAL_SERVER_ERROR -> showGlobalError(GlobalError.SERVER_ERROR)
                    else -> showGlobalError(GlobalError.SERVER_ERROR)
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
        ticker?.gone()
        rvPreferenceList?.gone()
        btnAddPreference?.gone()
        progressBar?.gone()
        globalError?.visible()
    }

    fun show(fragment: OrderSummaryPageFragment, profileId: Int) {
        fragment.context?.let { context ->
            fragment.fragmentManager?.let {
                bottomSheet?.dismiss()
                bottomSheet = BottomSheetUnify().apply {
                    isDragable = true
                    isHideable = true
                    if (isNewFlow) {
                        setTitle(context.getString(R.string.lbl_new_occ_profile_name))
                    } else {
                        setTitle(context.getString(R.string.lbl_osp_secondary_header))
                    }
                    val child = View.inflate(context, R.layout.bottom_sheet_preference_list, null)
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
    }

    private fun setupChild(child: View, profileId: Int) {
        ticker = child.findViewById(R.id.ticker_preference_list)
        rvPreferenceList = child.findViewById(R.id.rv_preference_list)
        btnAddPreference = child.findViewById(R.id.btn_add_preference)
        progressBar = child.findViewById(R.id.progress_bar)
        globalError = child.findViewById(R.id.global_error)

        rvPreferenceList?.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        adapter = PreferenceListAdapter(getListener(), profileId)
        rvPreferenceList?.adapter = adapter
        rvPreferenceList?.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                super.getItemOffsets(outRect, view, parent, state)
                outRect.top = child.context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
                outRect.bottom = child.context?.resources?.getDimension(R.dimen.dp_6)?.toInt() ?: 0
            }
        })
        if (isNewFlow) {
            btnAddPreference?.text = child.context?.getString(R.string.lbl_add_new_occ_profile_name)
        }
        btnAddPreference?.setOnClickListener {
            bottomSheet?.dismiss()
            listener.onAddPreference(adapter?.itemCount ?: 1)
        }
    }

    private fun getListener(): PreferenceListAdapter.PreferenceListAdapterListener = object : PreferenceListAdapter.PreferenceListAdapterListener {
        override fun onPreferenceSelected(preference: ProfilesItemModel, isMainProfile: Boolean) {
            bottomSheet?.dismiss()
            listener.onChangePreference(preference)
        }

        override fun onPreferenceEditClicked(preference: ProfilesItemModel, position: Int, profileSize: Int) {
            bottomSheet?.dismiss()
            listener.onEditPreference(preference, position, profileSize)
        }
    }

    private fun updateList(preferences: PreferenceListResponseModel) {
        adapter?.submitList(preferences.profiles, isNewFlow)
        if (isNewFlow) {
            listener.onShowNewLayout()
        }
        progressBar?.gone()
        val tickerMessage = preferences.ticker
        if (tickerMessage != null) {
            ticker?.setHtmlDescription(tickerMessage)
            ticker?.visible()
        } else {
            ticker?.gone()
        }
        rvPreferenceList?.visible()
        if (preferences.profiles.size >= preferences.maxProfile) {
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

        fun onShowNewLayout()
    }
}