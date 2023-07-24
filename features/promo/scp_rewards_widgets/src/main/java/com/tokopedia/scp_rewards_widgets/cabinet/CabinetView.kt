package com.tokopedia.scp_rewards_widgets.cabinet

import android.content.Context
import android.util.AttributeSet
import androidx.recyclerview.widget.ConcatAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeader
import com.tokopedia.scp_rewards_widgets.cabinetHeader.CabinetHeaderViewTypeFactory
import com.tokopedia.scp_rewards_widgets.medal.BannerData
import com.tokopedia.scp_rewards_widgets.medal.MedalCallbackListener
import com.tokopedia.scp_rewards_widgets.medal.MedalData
import com.tokopedia.scp_rewards_widgets.medal.MedalError
import com.tokopedia.scp_rewards_widgets.medal.MedalItem
import com.tokopedia.scp_rewards_widgets.medal.MedalViewTypeFactory

class CabinetView(
    context: Context,
    attrs: AttributeSet?
) : RecyclerView(context, attrs), MedalCallbackListener {

    private var listener: MedalCallbackListener? = null

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
        cabinetHeaderAdapter.clearAllElements()
        cabinetHeaderAdapter.addElement(cabinetHeader)
        bindMedalList(medalList)
    }

    fun attachMedalClickListener(listener: MedalCallbackListener) {
        this.listener = listener
    }

    private fun bindMedalList(medalSections: List<MedalData>) {
        if (medalSections.all { it.medalType.isNotEmpty() and it.medalList.isNullOrEmpty() }) {
            cabinetMedalSectionAdapter.setVisitables(listOf(MedalError(imageUrl = medalSections.firstOrNull()?.bannerData?.imageUrl)))
        } else {
            cabinetMedalSectionAdapter.setVisitables(medalSections)
        }
    }

    override fun onMedalClick(medalItem: MedalItem) {
        listener?.onMedalClick(medalItem)
    }

    override fun onSeeMoreClick(medalData: MedalData) {
        listener?.onSeeMoreClick(medalData)
    }

    override fun onMedalLoad(medalItem: MedalItem) {
        listener?.onMedalLoad(medalItem)
    }

    override fun onMedalFailed(medalItem: MedalItem) {
        listener?.onMedalFailed(medalItem)
    }

    override fun onSeeMoreLoad(medalData: MedalData) {
        listener?.onSeeMoreLoad(medalData)
    }

    override fun onBannerClick(bannerData: BannerData?, position: Int?) {
        listener?.onBannerClick(bannerData, position)
    }
}
