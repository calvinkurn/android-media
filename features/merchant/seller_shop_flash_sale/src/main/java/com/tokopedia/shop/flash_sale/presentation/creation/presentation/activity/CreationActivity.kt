package com.tokopedia.shop.flash_sale.presentation.creation.presentation.activity

import com.tokopedia.abstraction.base.view.activity.BaseSimpleActivity
import com.tokopedia.shop.flash_sale.presentation.creation.presentation.fragment.CreationFragment

class CreationActivity: BaseSimpleActivity() {

    override fun getNewFragment() = CreationFragment()

}