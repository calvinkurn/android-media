package com.tokopedia.topads.dashboard.view.sheet

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.RadioGroup
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.topads.dashboard.R
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.unifycomponents.ChipsUnify
import com.tokopedia.unifycomponents.UnifyButton
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.user.session.UserSession
import com.tokopedia.user.session.UserSessionInterface

/**
 * Created by Pika on 3/6/20.
 */
private const val SELECTED_STATUS_0 = 0;
private const val SELECTED_STATUS_1 = 1;
private const val SELECTED_STATUS_2 = 2;
private const val SELECTED_STATUS_3 = 3;
private const val SELECTED_placement_type_0 = 0;
private const val SELECTED_placement_type_2 = 2;
private const val SELECTED_placement_type_3 = 3;

class TopadsGroupFilterSheet : BottomSheetUnify() {

    private var adPlacementTitle: Typography? = null
    private var adPlacement: LinearLayout? = null
    private var semua: ChipsUnify? = null
    private var search: ChipsUnify? = null
    private var rekomendation: ChipsUnify? = null
    private var statusTitle: Typography? = null
    private var status: LinearLayout? = null
    private var active: ChipsUnify? = null
    private var tidakTampil: ChipsUnify? = null
    private var tidakAktif: ChipsUnify? = null
    private var sortFilter: RadioGroup? = null
    private var submit: UnifyButton? = null

    var onSubmitClick: (() -> Unit)? = null
    private var filterCount = 0
    private var selectedStatus = SELECTED_STATUS_0
    private var selectedAdPlacement = SELECTED_placement_type_0
    private var showPlacementFilter: Boolean = false

    private lateinit var userSession: UserSessionInterface

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,
    ): View? {
        val contentView = View.inflate(context, R.layout.topads_dash_filter_bottomsheet, null)
        setChild(contentView)
        initView(contentView)
        setTitle(getString(R.string.topads_dash_filter_sheet_title))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun initView(view: View) {
        adPlacementTitle = view.findViewById(R.id.adPlacement_title)
        adPlacement = view.findViewById(R.id.adPlacement)
        semua = view.findViewById(R.id.semua)
        search = view.findViewById(R.id.search)
        rekomendation = view.findViewById(R.id.rekomendation)
        statusTitle = view.findViewById(R.id.status_title)
        status = view.findViewById(R.id.status)
        active = view.findViewById(R.id.active)
        tidakTampil = view.findViewById(R.id.tidak_tampil)
        tidakAktif = view.findViewById(R.id.tidak_aktif)
        sortFilter = view.findViewById(R.id.sortFilter)
        submit = view.findViewById(R.id.submit)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        userSession = UserSession(context)
        status?.visibility = View.VISIBLE
        statusTitle?.visibility = View.VISIBLE


        if (showPlacementFilter) {
            adPlacementTitle?.show()
            adPlacement?.show()
        } else {
            adPlacementTitle?.hide()
            adPlacement?.hide()
        }

        selectedAdPlacement = SELECTED_placement_type_0
        // for ad placement filter
        semua?.setOnClickListener { v ->
            if (semua?.chipType == ChipsUnify.TYPE_NORMAL) {
                semua?.chipType = ChipsUnify.TYPE_SELECTED
                selectedAdPlacement = SELECTED_placement_type_0
            } else {
                semua?.chipType = ChipsUnify.TYPE_NORMAL
                selectedAdPlacement = SELECTED_placement_type_0
            }
            search?.chipType = ChipsUnify.TYPE_NORMAL
            rekomendation?.chipType = ChipsUnify.TYPE_NORMAL
        }
        search?.setOnClickListener { v ->
            if (search?.chipType == ChipsUnify.TYPE_NORMAL) {
                search?.chipType = ChipsUnify.TYPE_SELECTED
                selectedAdPlacement = SELECTED_placement_type_2
            } else {
                search?.chipType = ChipsUnify.TYPE_NORMAL
                selectedAdPlacement = SELECTED_placement_type_0
            }
            semua?.chipType = ChipsUnify.TYPE_NORMAL
            rekomendation?.chipType = ChipsUnify.TYPE_NORMAL
        }
        rekomendation?.setOnClickListener { v ->
            if (rekomendation?.chipType == ChipsUnify.TYPE_NORMAL) {
                rekomendation?.chipType = ChipsUnify.TYPE_SELECTED
                selectedAdPlacement = SELECTED_placement_type_3
            } else {
                rekomendation?.chipType = ChipsUnify.TYPE_NORMAL
                selectedAdPlacement = SELECTED_placement_type_0
            }
            semua?.chipType = ChipsUnify.TYPE_NORMAL
            search?.chipType = ChipsUnify.TYPE_NORMAL
        }

        active?.setOnClickListener { v ->
            if (active?.chipType == ChipsUnify.TYPE_NORMAL) {
                active?.chipType = ChipsUnify.TYPE_SELECTED
                selectedStatus = SELECTED_STATUS_1
            } else {
                active?.chipType = ChipsUnify.TYPE_NORMAL
                selectedStatus = SELECTED_STATUS_0
            }
            tidakAktif?.chipType = ChipsUnify.TYPE_NORMAL
            tidakTampil?.chipType = ChipsUnify.TYPE_NORMAL
        }
        tidakTampil?.setOnClickListener { v ->
            if (tidakTampil?.chipType == ChipsUnify.TYPE_NORMAL) {
                tidakTampil?.chipType = ChipsUnify.TYPE_SELECTED
                selectedStatus = SELECTED_STATUS_2
            } else {
                tidakTampil?.chipType = ChipsUnify.TYPE_NORMAL
                selectedStatus = SELECTED_STATUS_0
            }
            active?.chipType = ChipsUnify.TYPE_NORMAL
            tidakAktif?.chipType = ChipsUnify.TYPE_NORMAL
        }
        tidakAktif?.setOnClickListener { v ->
            if (tidakAktif?.chipType == ChipsUnify.TYPE_NORMAL) {
                tidakAktif?.chipType = ChipsUnify.TYPE_SELECTED
                selectedStatus = SELECTED_STATUS_3
            } else {
                tidakAktif?.chipType = ChipsUnify.TYPE_NORMAL
                selectedStatus = SELECTED_STATUS_0
            }
            active?.chipType = ChipsUnify.TYPE_NORMAL
            tidakTampil?.chipType = ChipsUnify.TYPE_NORMAL
        }
        submit?.setOnClickListener { _ ->
            filterCount = 0
            if (selectedStatus != 0)
                filterCount++
            if (sortFilter?.checkedRadioButtonId != -1)
                filterCount++
            onSubmitClick?.invoke()
            dismiss()
        }

    }


    fun showAdplacementFilter(show: Boolean) {
        this.showPlacementFilter = show
    }

    fun getFilterCount(): Int {
        return filterCount
    }

    fun removeStatusFilter() {
        status?.visibility = View.GONE
        statusTitle?.visibility = View.GONE
    }

    fun getSelectedSortId(): String {
        return when (sortFilter?.checkedRadioButtonId) {
            R.id.filter1 -> list[0]
            R.id.filter2 -> list[1]
            R.id.filter3 -> list[2]
            R.id.filter4 -> list[3]
            R.id.filter5 -> list[4]
            else -> ""
        }
    }

    fun getSelectedAdPlacementType(): Int {
        return selectedAdPlacement
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