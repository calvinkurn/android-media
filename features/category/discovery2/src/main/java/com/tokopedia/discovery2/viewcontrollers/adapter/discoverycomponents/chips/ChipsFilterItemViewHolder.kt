package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ChipsUnify

private const val NORMAL = "0"
private const val SELECTED = "2"

class ChipsFilterItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val chipsFilterItem: ChipsUnify = itemView.findViewById(R.id.chipsFilterItem)
    private lateinit var chipsFilterItemViewModel: ChipsFilterItemViewModel

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        chipsFilterItemViewModel = discoveryBaseViewModel as ChipsFilterItemViewModel
        chipsFilterItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            val itemData = item.data?.get(0)
            itemData?.let {
                it.title?.let { title ->
                    if (title.isNotEmpty()) {
                        chipsFilterItem.chipText = title
                        setChipType(it)
                        setClick(it)
                        chipsFilterItem.show()
                    } else {
                        chipsFilterItem.hide()
                    }
                }
            }
        })
    }

    private fun setChipType(chipData: DataItem) {
        chipsFilterItem.apply {
            chipType = chipData.chipSelectionType
        }
    }

    private fun setClick(chipData: DataItem) {
        chipsFilterItem.setOnClickListener {
            (it as ChipsUnify).apply {
                chipData.chipSelectionType = if (isSelected(this)) {
                    NORMAL
                } else {
                    SELECTED
                }
            }
            setChipType(chipData)
        }
    }

    private fun isSelected(chipsUnify: ChipsUnify): Boolean {
        return chipsUnify.chipType.equals(SELECTED)
    }

}