package com.tokopedia.shop.score.penalty.presentation.fragment

import com.tokopedia.abstraction.base.view.fragment.BaseDaggerFragment
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent

class PenaltyPageFragment: BaseDaggerFragment() {

    companion object {
        fun newInstance(): PenaltyPageFragment {
            return PenaltyPageFragment()
        }
    }

    override fun getScreenName(): String = ""

    override fun initInjector() {
        getComponent(PenaltyComponent::class.java).inject(this)
    }
}