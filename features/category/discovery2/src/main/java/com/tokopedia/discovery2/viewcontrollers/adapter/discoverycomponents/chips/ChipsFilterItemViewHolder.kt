package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.chips

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.tokopedia.discovery2.R
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.discovery2.viewcontrollers.adapter.viewholder.AbstractViewHolder
import com.tokopedia.discovery2.viewcontrollers.fragment.DiscoveryFragment
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.unifycomponents.ChipsUnify

const val NORMAL = "0"

const val SELECTED = "2"

class ChipsFilterItemViewHolder(itemView: View, private val fragment: Fragment) : AbstractViewHolder(itemView) {
    private val chipsFilterItem: ChipsUnify = itemView.findViewById(R.id.chipsFilterItem)
    private lateinit var chipsFilterItemViewModel: ChipsFilterItemViewModel
    private var positionForParentAdapter: Int = -1

    override fun bindView(discoveryBaseViewModel: DiscoveryBaseViewModel) {
        chipsFilterItemViewModel = discoveryBaseViewModel as ChipsFilterItemViewModel
        chipsFilterItemViewModel.getComponentLiveData().observe(fragment.viewLifecycleOwner, Observer { item ->
            val itemData = item.data?.get(0)
            positionForParentAdapter = itemData?.positionForParentItem ?: -1
            itemData?.let {
                it.title?.let { title ->
                    if (title.isNotEmpty()) {
                        chipsFilterItem.chipText = title
                        setChipType(it)
                        setClick(item)
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

    private fun setClick(componentsItem: ComponentsItem) {
        chipsFilterItem.setOnClickListener {
            componentsItem?.data?.get(0)?.let { it1 ->

                if (it1.chipSelectionType == NORMAL) {
                    (parentAbstractViewHolder as ChipsFilterViewHolder).onChipSelected(componentsItem.id)
                } else {
                    (parentAbstractViewHolder as ChipsFilterViewHolder).onChipUnSelected(componentsItem.id)
                }
                sendChipClickEvent(it1)
            }
        }
    }

    private fun sendChipClickEvent(chipData: DataItem) {
        (fragment as? DiscoveryFragment)?.getDiscoveryAnalytics()?.trackClickChipsFilter(chipData.title
                ?: "")
    }

}