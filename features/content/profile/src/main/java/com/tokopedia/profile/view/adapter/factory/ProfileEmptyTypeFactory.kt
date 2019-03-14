package com.tokopedia.profile.view.adapter.factory

import com.tokopedia.profile.view.viewmodel.ProfileEmptyViewModel

/**
 * @author by yfsx on 14/03/19.
 */
interface ProfileEmptyTypeFactory {
    fun type(viewModel: ProfileEmptyViewModel): Int
}