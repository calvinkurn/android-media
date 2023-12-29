package com.tokopedia.scp_rewards.cabinet.presentation.adapter

import androidx.recyclerview.widget.DiffUtil
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter
import com.tokopedia.scp_rewards.cabinet.presentation.adapter.differ.SeeMoreDiffer
import com.tokopedia.scp_rewards_widgets.medal.SeeMoreMedalTypeFactory

class SeeMoreMedalAdapter(typeFactory: SeeMoreMedalTypeFactory) : BaseAdapter<SeeMoreMedalTypeFactory>(typeFactory) {

    fun submitList(list:List<Visitable<*>>){
        val differ = SeeMoreDiffer(list,visitables)
        val result = DiffUtil.calculateDiff(differ)
        visitables.clear()
        visitables.addAll(list)
        result.dispatchUpdatesTo(this)
    }
}


