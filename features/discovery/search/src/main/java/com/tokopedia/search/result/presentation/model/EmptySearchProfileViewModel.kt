package com.tokopedia.search.result.presentation.model

import android.support.annotation.DrawableRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProfileListTypeFactory

class EmptySearchProfileViewModel : Visitable<ProfileListTypeFactory> {

    @DrawableRes
    var imageRes: Int = 0
    var title: String? = ""
    var content: String? = ""
    var buttonText: String? = ""

    override fun type(typeFactory: ProfileListTypeFactory): Int {
        return typeFactory.type(this)
    }
}
