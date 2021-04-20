package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.tokopedia.topads.common.analytics.TopAdsCreateAnalytics
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface
import kotlinx.android.synthetic.main.topads_dash_filter_bottomsheet.*
import kotlinx.android.synthetic.main.topads_dash_filter_bottomsheet.view.*

/**
 * Created by Pika on 3/6/20.
 */
private const val SELECTED_STATUS_0 = 0;
private const val SELECTED_STATUS_1 = 1;
private const val SELECTED_STATUS_2 = 2;
private const val SELECTED_STATUS_3 = 3;
private const val CLICK_TERAPKAN = "click - terapkan"

class TopadsGroupFilterSheet : BottomSheetUnify() {
    private var dialog: BottomSheetDialog? = null
    var onSubmitClick: (() -> Unit)? = null
    private var filterCount = 0
    private var selectedStatus = SELECTED_STATUS_0
    private var contentView: View? = null

    private lateinit var userSession: UserSessionInterface


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        contentView = View.inflate(context, R.layout.topads_dash_filter_bottomsheet, null)
        setChild(contentView)
        setTitle(getString(R.string.topads_dash_filter_sheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userSession = UserSession(context)
        view.status?.visibility = View.VISIBLE
        view.status_title?.visibility = View.VISIBLE

        view.active?.setOnClickListener { v ->
            if (v.active.chipType == ChipsUnify.TYPE_NORMAL) {
                v.active.chipType = ChipsUnify.TYPE_SELECTED
                selectedStatus = SELECTED_STATUS_1
            } else {
                v.active.chipType = ChipsUnify.TYPE_NORMAL
                selectedStatus = SELECTED_STATUS_0
            }
            view.tidak_aktif?.chipType = ChipsUnify.TYPE_NORMAL
            view.tidak_tampil?.chipType = ChipsUnify.TYPE_NORMAL
        }
        view.tidak_tampil?.setOnClickListener { v ->
            if (v.tidak_tampil.chipType == ChipsUnify.TYPE_NORMAL) {
                v.tidak_tampil.chipType = ChipsUnify.TYPE_SELECTED
                selectedStatus = SELECTED_STATUS_2
            } else {
                v.tidak_tampil.chipType = ChipsUnify.TYPE_NORMAL
                selectedStatus = SELECTED_STATUS_0
            }
            view.active?.chipType = ChipsUnify.TYPE_NORMAL
            view.tidak_aktif?.chipType = ChipsUnify.TYPE_NORMAL
        }
        view.tidak_aktif?.setOnClickListener { v ->
            if (v.tidak_aktif.chipType == ChipsUnify.TYPE_NORMAL) {
                v.tidak_aktif.chipType = ChipsUnify.TYPE_SELECTED
                selectedStatus = SELECTED_STATUS_3
            } else {
                v.tidak_aktif.chipType = ChipsUnify.TYPE_NORMAL
                selectedStatus = SELECTED_STATUS_0
            }
            view.active?.chipType = ChipsUnify.TYPE_NORMAL
            view.tidak_tampil?.chipType = ChipsUnify.TYPE_NORMAL
        }
        view.submit.setOnClickListener { _ ->
            context.let {
                var eventLabel = "{${userSession.shopId}}" + "-" + "{${getSelectedText(context)}}" + "-" + "{${getSelectedSortId()}}"
                TopAdsCreateAnalytics.topAdsCreateAnalytics.sendHeadlineAdsEvent(CLICK_TERAPKAN, eventLabel, userSession.userId)
            }
            filterCount = 0
            if (selectedStatus != 0)
                filterCount++
            if (view.sortFilter?.checkedRadioButtonId != -1)
                filterCount++
            onSubmitClick?.invoke()
            dismiss()
        }
        
    }

    fun getFilterCount(): Int {
        return filterCount
    }

    fun removeStatusFilter() {
        dialog.let {
            view?.status?.visibility = View.GONE
            view?.status_title?.visibility = View.GONE
        }
    }

    fun getSelectedSortId(): String {
        return when (dialog?.sortFilter?.checkedRadioButtonId) {
            R.id.filter1 -> list[0]
            R.id.filter2 -> list[1]
            R.id.filter3 -> list[2]
            R.id.filter4 -> list[3]
            R.id.filter5 -> list[4]
            else -> ""
        }
    }

    fun getSelectedStatusId(): Int? {
        return selectedStatus
    }

    fun getSelectedText(context: Context?): String {
        context?.run {
            return when (selectedStatus) {
                SELECTED_STATUS_1 -> {
                    getString(R.string.topads_active)
                }
                SELECTED_STATUS_2 -> {
                    getString(R.string.topads_dash_tidak_tampil)
                }
                SELECTED_STATUS_3 -> {
                    getString(R.string.topads_dash_tidak_active)
                }
                else -> ""
            }
        }
        return ""
    }

    companion object {
        lateinit var list: Array<String>
        fun newInstance(context: Context?): TopadsGroupFilterSheet {
            val fragment = TopadsGroupFilterSheet()
            context?.let {
                list = context.resources.getStringArray(R.array.top_ads_sort_value)
            }
            return fragment
        }
    }
}