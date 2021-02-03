package com.tokopedia.topupbills.telco.prepaid.bottomsheet

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.topupbills.R
import com.tokopedia.topupbills.telco.common.getColorFromResources
import com.tokopedia.topupbills.telco.data.FilterTagDataCollection
import com.tokopedia.topupbills.telco.prepaid.adapter.TelcoFilterAdapterTypeFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import kotlinx.android.synthetic.main.bottom_sheet_telco_filter.view.*

class DigitalTelcoFilterBottomSheet : BottomSheetUnify(),
        BaseCheckableViewHolder.CheckableInteractionListener,
        BaseListCheckableAdapter.OnCheckableAdapterListener<FilterTagDataCollection> {

    private lateinit var childView: View
    private lateinit var listener: ActionListener

    private lateinit var adapter: BaseListCheckableAdapter<FilterTagDataCollection,
            BaseListCheckableTypeFactory<FilterTagDataCollection>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initBottomSheet()
        initAdapter()
        initView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        if (::listener.isInitialized && arguments != null) {
            arguments?.let {
                val dataCollection = it.getParcelableArrayList<FilterTagDataCollection>(FILTER_TAG)
                adapter.addElement(dataCollection)

                val checkPositionList = HashSet<Int>()
                listener.getFilterSelected().map { tagSelected ->
                    dataCollection?.let { collections ->
                        for (i in 0 until collections.size) {
                            if (collections[i].key == tagSelected) {
                                checkPositionList.add(i)
                            }
                        }
                    }
                }
                adapter.setCheckedPositionList(checkPositionList)
                adapter.notifyDataSetChanged()
            }
        }
    }

    fun setListener(listener: ActionListener) {
        this.listener = listener
    }

    private fun initBottomSheet() {
        showCloseIcon = false
        showKnob = true
        isDragable = true
        isHideable = true
        arguments?.let {
            setTitle(it.getString(TITLE) ?: "")
        }
        setAction(getString(R.string.telco_reset_filter)) {
            if (adapter.totalChecked > 0) resetFilter()
        }

        childView = View.inflate(requireContext(), R.layout.bottom_sheet_telco_filter, null)
        setChild(childView)
    }

    private fun initAdapter() {
        val typeFactory = TelcoFilterAdapterTypeFactory(this)
        adapter = BaseListCheckableAdapter<FilterTagDataCollection,
                BaseListCheckableTypeFactory<FilterTagDataCollection>>(typeFactory, this)
    }

    private fun initView() {
        with(childView) {
            telco_filter_rv.setHasFixedSize(true)
            telco_filter_rv.layoutManager = LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false)
            telco_filter_rv.adapter = adapter

            telco_filter_btn.setOnClickListener {
                saveFilter()
            }
        }
    }

    private fun saveFilter() {
        val checkedFilterList = ArrayList<String>()
        val checkedFilterValues = StringBuilder()
        for ((index, checkedData) in adapter.checkedDataList.withIndex()) {
            checkedFilterList.add(checkedData.key)
            checkedFilterValues.append(checkedData.value.toLowerCase())
            if (index < adapter.checkedDataList.size - 1) {
                checkedFilterValues.append(" - ")
            }
        }
        listener.onTelcoFilterSaved(checkedFilterList, checkedFilterValues.toString())

        if (isAdded) {
            dismiss()
        }
    }

    private fun resetFilter() {
        listener.resetFilter()
        adapter.resetCheckedItemSet()
        adapter.notifyDataSetChanged()
        setVisibilityBtnFilter()
    }

    override fun isChecked(position: Int): Boolean {
        setVisibilityBtnFilter()
        return adapter.isChecked(position)
    }

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        adapter.updateListByCheck(isChecked, position)
    }

    override fun onItemChecked(t: FilterTagDataCollection?, isChecked: Boolean) {
        setVisibilityBtnFilter()
    }

    private fun setVisibilityBtnFilter() {
        val filterChecked = adapter.totalChecked > 0
        if (filterChecked) {
            bottomSheetAction.setTextColor(resources.getColorFromResources(requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_G500))
        } else {
            bottomSheetAction.setTextColor(resources.getColorFromResources(requireContext(),
                    com.tokopedia.unifyprinciples.R.color.Unify_N700))
        }
        bottomSheetAction.isEnabled = filterChecked
    }

    interface ActionListener {
        fun onTelcoFilterSaved(keysFilter: ArrayList<String>, valuesFilter: String)
        fun getFilterSelected(): ArrayList<String>
        fun resetFilter()
    }

    companion object {

        private const val TITLE = "title"
        private const val FILTER_TAG = "filter_tag"
        private const val PARAM_NAME = "param_name"

        fun newInstance(title: String, paramName: String, dataCollections: ArrayList<FilterTagDataCollection>): DigitalTelcoFilterBottomSheet {
            val fragment = DigitalTelcoFilterBottomSheet()
            val bundle = Bundle()
            bundle.putString(TITLE, title)
            bundle.putString(PARAM_NAME, paramName)
            bundle.putParcelableArrayList(FILTER_TAG, dataCollections)
            fragment.arguments = bundle
            return fragment
        }
    }
}