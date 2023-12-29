package com.tokopedia.scp_rewards_widgets.common.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.scp_rewards_widgets.medal.SeeMoreMedalTypeFactory

class LoadingModel: Visitable<SeeMoreMedalTypeFactory> {

    override fun type(typeFactory: SeeMoreMedalTypeFactory) = typeFactory.type(this)
}
