package com.tokopedia.scp_rewards_widgets.cabinet

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeader
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeaderViewTypeFactory
import com.tokopedia.scp_rewards_widgets.medal.MedalClickListener
import com.tokopedia.scp_rewards_widgets.medal.MedalData
import com.tokopedia.scp_rewards_widgets.medal.MedalItem
import com.tokopedia.scp_rewards_widgets.medal.MedalViewTypeFactory

class CabinetView(
    context: Context,
    attrs: AttributeSet?
) : RecyclerView(context, attrs), MedalClickListener {

    private lateinit var listener: MedalClickListener

    private val cabinetMedalSectionAdapter: BaseAdapter<MedalViewTypeFactory> by lazy {
        BaseAdapter(MedalViewTypeFactory(this))
    }

    private val cabinetHeaderAdapter: BaseAdapter<CabinetHeaderViewTypeFactory> by lazy {
        BaseAdapter(CabinetHeaderViewTypeFactory())
    }

    init {
        layoutManager = LinearLayoutManager(context)
        adapter = ConcatAdapter(cabinetHeaderAdapter, cabinetMedalSectionAdapter)
    }

    fun bindData(cabinetHeader: CabinetHeader, medalList: List<MedalData>) {
        cabinetHeaderAdapter.addElement(cabinetHeader)
        bindMedalList(medalList)
    }

    fun attachMedalClickListener(listener: MedalClickListener) {
        this.listener = listener
    }

    private fun bindMedalList(medalList: List<MedalData>) {
        if (medalList.all { it.medalList.isNullOrEmpty() }) {
            cabinetMedalSectionAdapter.setVisitables(listOf(EmptyModel().apply { urlRes = medalList[0].bannerData?.imageUrl }))
        } else {
            cabinetMedalSectionAdapter.setVisitables(medalList)
        }
    }

    override fun onMedalClick(medalItem: MedalItem) {
        if (::listener.isInitialized) {
            listener.onMedalClick(medalItem)
        }
    }

    override fun onSeeMoreClick(medalData: MedalData) {
        if (::listener.isInitialized) {
            listener.onSeeMoreClick(medalData)
        }
    }
}
