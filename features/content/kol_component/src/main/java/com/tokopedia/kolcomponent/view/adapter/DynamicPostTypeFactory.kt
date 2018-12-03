package com.tokopedia.kolcomponent.view.adapter

import com.tokopedia.kolcomponent.view.viewmodel.DynamicPostViewModel

/**
 * @author by milhamj on 03/12/18.
 */
interface DynamicPostTypeFactory {
    fun type(dynamicPostViewModel: DynamicPostViewModel): Int
}