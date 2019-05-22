package com.tokopedia.search.result.presentation.view.typefactory

import com.tokopedia.abstraction.base.view.adapter.factory.AdapterTypeFactory
import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.model.TotalSearchCountViewModel

interface ProfileListTypeFactory : AdapterTypeFactory {
    fun type(profileViewModel: ProfileViewModel): Int
    fun type(emptySearchProfileModel: EmptySearchProfileViewModel): Int
    fun type(totalSearchCountViewModel: TotalSearchCountViewModel): Int
}