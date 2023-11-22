package com.tokopedia.discovery2.viewcontrollers.adapter.discoverycomponents.thematicheader

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.tokopedia.discovery2.data.ComponentsItem
import com.tokopedia.discovery2.data.DataItem
import com.tokopedia.discovery2.viewcontrollers.activity.DiscoveryBaseViewModel
import com.tokopedia.kotlin.extensions.view.orZero

class ThematicHeaderViewModel(
    application: Application,
    val component: ComponentsItem,
    val position: Int
) : DiscoveryBaseViewModel() {
    companion object {
        private const val FIRST_DATA_POSITION = 1
        private const val THEMATIC_HEADER_MIN_SIZE = 1
    }

    private val _thematicData: MutableLiveData<Pair<Int, DataItem?>> = MutableLiveData()
    val thematicData: LiveData<Pair<Int, DataItem?>>
        get() = _thematicData

    override fun onAttachToViewHolder() {
        super.onAttachToViewHolder()
        loadThematicHeaderData()
    }

    private fun loadThematicHeaderData() {
        if (component.data?.size.orZero() == THEMATIC_HEADER_MIN_SIZE) {
            _thematicData.value = Pair(
                FIRST_DATA_POSITION,
                component.data?.firstOrNull()
            )
        }
    }

    fun switchThematicHeaderData(
        tabPosition: Int
    ) {
        val item = component.data?.find { it.tabIndex?.contains(tabPosition) == true }
        if (_thematicData.value?.second != item) {
            _thematicData.value = Pair(
                tabPosition,
                item
            )
        }
    }
}
