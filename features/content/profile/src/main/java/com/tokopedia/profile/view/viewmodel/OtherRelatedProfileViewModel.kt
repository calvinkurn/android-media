package com.tokopedia.profile.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactory

/**
 * Model to show the the profile has no post (empty)
 */
class OtherRelatedProfileViewModel : Visitable<ProfileTypeFactory> {
    override fun type(typeFactory: ProfileTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
