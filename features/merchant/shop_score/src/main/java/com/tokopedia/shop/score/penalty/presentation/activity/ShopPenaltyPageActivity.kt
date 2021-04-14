package com.tokopedia.shop.score.penalty.presentation.activity

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.app.BaseMainApplication
import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.abstraction.common.di.component.HasComponent
import com.tokopedia.shop.score.penalty.di.component.DaggerPenaltyComponent
import com.tokopedia.shop.score.penalty.di.component.PenaltyComponent
import com.tokopedia.shop.score.penalty.presentation.fragment.ShopPenaltyPageFragment

class ShopPenaltyPageActivity: BaseSimpleActivity(), HasComponent<PenaltyComponent> {

    override fun getNewFragment(): Fragment = ShopPenaltyPageFragment.newInstance()

    override fun getComponent(): PenaltyComponent {
        return DaggerPenaltyComponent
                .builder()
                .baseAppComponent((application as? BaseMainApplication)?.baseAppComponent)
                .build()
    }
}