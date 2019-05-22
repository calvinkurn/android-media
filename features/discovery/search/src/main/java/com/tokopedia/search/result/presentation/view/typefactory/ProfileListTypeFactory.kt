package com.tokopedia.search.result.presentation.view.typefactory

import com.tokopedia.search.result.presentation.model.EmptySearchProfileViewModel
import com.tokopedia.search.result.presentation.model.ProfileViewModel
import com.tokopedia.search.result.presentation.model.TotalSearchCountViewModel

interface ProfileListTypeFactory {
    fun type(profileViewModel: ProfileViewModel): Int
    fun type(emptySearchProfileModel: EmptySearchProfileViewModel): Int
    fun type(totalSearchCountViewModel: TotalSearchCountViewModel): Int
}