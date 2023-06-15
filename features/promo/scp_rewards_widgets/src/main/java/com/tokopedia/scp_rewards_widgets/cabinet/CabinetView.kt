package com.tokopedia.scp_rewards_widgets.cabinet

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeader
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeaderViewTypeFactory
import com.tokopedia.scp_rewards_widgets.medal.MedalData
import com.tokopedia.scp_rewards_widgets.medal.MedalViewTypeFactory

class CabinetView(context: Context,
                  attrs: AttributeSet?) : RecyclerView(context, attrs) {

    private val cabinetMedalSectionAdapter: BaseAdapter<MedalViewTypeFactory> by lazy {
        BaseAdapter(MedalViewTypeFactory())
    }

    private val cabinetHeaderAdapter: BaseAdapter<CabinetHeaderViewTypeFactory> by lazy {
        BaseAdapter(CabinetHeaderViewTypeFactory())
    }


    init {
        layoutManager = LinearLayoutManager(context)
        adapter = ConcatAdapter(cabinetHeaderAdapter, cabinetMedalSectionAdapter)
    }

    fun bindData(cabinetHeader: CabinetHeader, medalList: List<MedalData>) {
        cabinetMedalSectionAdapter.setVisitables(medalList)
        cabinetHeaderAdapter.addElement(cabinetHeader)
    }

}
