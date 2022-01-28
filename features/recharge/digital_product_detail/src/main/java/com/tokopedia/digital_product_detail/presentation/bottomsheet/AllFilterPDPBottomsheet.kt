package com.tokopedia.digital_product_detail.presentation.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseListCheckableAdapter
import com.tokopedia.abstraction.base.view.adapter.factory.BaseListCheckableTypeFactory
import com.tokopedia.abstraction.base.view.adapter.holder.BaseCheckableViewHolder
import com.tokopedia.digital_product_detail.data.model.data.FilterTagDataCollection
import com.tokopedia.digital_product_detail.databinding.BottomSheetAllFilterBinding
import com.tokopedia.digital_product_detail.presentation.adapter.DigitalPDPFilterAllAdapterTypeFactory
import com.tokopedia.unifycomponents.BottomSheetUnify
import com.tokopedia.utils.lifecycle.autoClearedNullable

class AllFilterPDPBottomsheet(private val title:String, private val filterTagDataCollections: List<FilterTagDataCollection>): BottomSheetUnify(),
    BaseCheckableViewHolder.CheckableInteractionListener,
    BaseListCheckableAdapter.OnCheckableAdapterListener<FilterTagDataCollection> {


    private var binding by autoClearedNullable<BottomSheetAllFilterBinding>()

    private lateinit var checkableAdapter: BaseListCheckableAdapter<FilterTagDataCollection,
            BaseListCheckableTypeFactory<FilterTagDataCollection>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iniBottomSheet()
        initAdapter()
        initView()
    }

    private fun iniBottomSheet(){
        isFullpage = false
        isDragable = false
        showCloseIcon = false
        binding = BottomSheetAllFilterBinding.inflate(LayoutInflater.from(context))
        setTitle(title)
        setChild(binding?.root)
    }

    private fun initView(){
        binding?.let {
            it.rvPdpFilterAll.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
                adapter = checkableAdapter
            }
        }

    }

    private fun initAdapter(){
        val typeFactory = DigitalPDPFilterAllAdapterTypeFactory(this)
        checkableAdapter = object : BaseListCheckableAdapter<FilterTagDataCollection,
                BaseListCheckableTypeFactory<FilterTagDataCollection>>(typeFactory, this) {
            override fun getItemId(position: Int): Long {
                return position.toLong()
            }
        }
        checkableAdapter.setHasStableIds(true)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        checkableAdapter.addElement(filterTagDataCollections)
        val checkPositionList = HashSet<Int>()
        filterTagDataCollections.let { collections ->
            for (i in 0 until collections.size){
                if (collections[i].isSelected){
                    checkPositionList.add(i)
                }
            }
        }
        checkableAdapter.setCheckedPositionList(checkPositionList)
        checkableAdapter.notifyDataSetChanged()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bottomSheetBehaviorKnob(view, true)
    }

    override fun onItemChecked(t: FilterTagDataCollection?, isChecked: Boolean) {
        val filterChecked = checkableAdapter.totalChecked > 0
    }

    override fun isChecked(position: Int): Boolean {
        return checkableAdapter.isChecked(position)
    }

    override fun updateListByCheck(isChecked: Boolean, position: Int) {
        //checkableAdapter.updateListByCheck(isChecked, position)
    }
}