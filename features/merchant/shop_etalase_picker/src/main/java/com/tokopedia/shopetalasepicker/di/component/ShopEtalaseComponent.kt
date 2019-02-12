package com.tokopedia.shopetalasepicker.di.component

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.shopetalasepicker.di.scope.ShopEtalaseScope
import com.tokopedia.shopetalasepicker.view.fragment.ShopEtalaseFragment

import dagger.Component

/**
 * Created by hendry on 18/01/18.
 */
@ShopEtalaseScope
@Component(dependencies = arrayOf(BaseAppComponent::class))
interface ShopEtalaseComponent {

    fun inject(shopEtalaseFragment: ShopEtalaseFragment)

}
