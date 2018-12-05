package com.tokopedia.kolcomponent.view.adapter

import com.tokopedia.kolcomponent.view.viewmodel.DynamicPostViewModel
import com.tokopedia.kolcomponent.view.viewmodel.ItemCreateContentViewModel
import com.tokopedia.kolcomponent.view.viewmodel.ItemRecommendedViewModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicPostTypeFactory {
    fun type(dynamicPostViewModel: DynamicPostViewModel): Int
    fun type(itemRecommendedViewModel: ItemRecommendedViewModel): Int
    fun type(itemCreateContentViewModel: ItemCreateContentViewModel): Int
}