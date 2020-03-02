package com.tokopedia.purchase_platform.features.one_click_checkout.order.view.bottomsheet

import android.view.View
import android.widget.ProgressBar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.purchase_platform.R
import com.tokopedia.purchase_platform.features.one_click_checkout.common.data.Preference
import com.tokopedia.purchase_platform.features.one_click_checkout.order.view.OrderSummaryPageFragment
import com.tokopedia.purchase_platform.features.one_click_checkout.preference.list.view.PreferenceListAdapter
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.bottom_sheet_preference_list.view.*
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

class PreferenceListBottomSheet(override val coroutineContext: CoroutineContext = SupervisorJob() + Dispatchers.Main.immediate, private val listener: PreferenceListBottomSheetListener) : CoroutineScope {
    // need get all preference list usecase, update selected preference usecase

    private var bottomSheet: BottomSheetUnify? = null

    private var rvPreferenceList: RecyclerView? = null
    private var btnAddPreference: UnifyButton? = null
    private var progressBar: ProgressBar? = null

    private var adapter: PreferenceListAdapter? = null

    init {
        //get all preference
        launch {
            delay(3000)
            updateList(listOf(Preference(), Preference(), Preference(), Preference(), Preference()))
        }
    }

    fun show(fragment: OrderSummaryPageFragment) {
        fragment.fragmentManager?.let {
            bottomSheet?.dismiss()
            bottomSheet = BottomSheetUnify().apply {
                isDragable = true
                isHideable = true
                setTitle("Pengiriman dan pembayaran")
                val child = View.inflate(fragment.context, R.layout.bottom_sheet_preference_list, null)
                setupChild(child)
                fragment.view?.height?.div(2)?.let { height ->
                    customPeekHeight = height
                }
                setChild(child)
                show(it, null)
                setOnDismissListener {
                    onCleared()
                }
            }
        }
    }

    private fun setupChild(child: View) {
        rvPreferenceList = child.rv_preference_list
        btnAddPreference = child.btn_add_preference
        progressBar = child.progress_bar

        rvPreferenceList?.layoutManager = LinearLayoutManager(child.context, LinearLayoutManager.VERTICAL, false)
        adapter = PreferenceListAdapter(getListener())
        rvPreferenceList?.adapter = adapter
        btnAddPreference?.setOnClickListener {
            bottomSheet?.dismiss()
            listener.onAddPreference()
        }
    }

    private fun getListener(): PreferenceListAdapter.PreferenceListAdapterListener = object : PreferenceListAdapter.PreferenceListAdapterListener {
        override fun onPreferenceSelected(preference: Preference) {
            //Todo: update selected preference api
            bottomSheet?.dismiss()
            listener.onChangePreference(preference)
        }

        override fun onPreferenceEditClicked(preference: Preference) {
            bottomSheet?.dismiss()
            listener.onEditPreference(preference)
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

    private fun updateList(preferences: List<Preference>) {
        adapter?.submitList(preferences)
        progressBar?.gone()
        rvPreferenceList?.visible()
        btnAddPreference?.visible()
    }

    private fun onCleared() {
        cancel()
    }

    interface PreferenceListBottomSheetListener {

        fun onChangePreference(preference: Preference)

        fun onEditPreference(preference: Preference)

        fun onAddPreference()
    }
}