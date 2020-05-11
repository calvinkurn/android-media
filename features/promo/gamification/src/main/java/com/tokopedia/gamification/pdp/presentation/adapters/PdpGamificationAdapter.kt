package com.tokopedia.gamification.pdp.presentation.adapters

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.adapter.BaseAdapter

class PdpGamificationAdapter(val visitables: List<Visitable<*>>,
                             val typeFactory: PdpGamificationAdapterTypeFactory) :
        BaseAdapter<PdpGamificationAdapterTypeFactory>(typeFactory, visitables) {

}