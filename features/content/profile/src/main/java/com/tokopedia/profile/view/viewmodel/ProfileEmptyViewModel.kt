package com.tokopedia.profile.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactory

/**
 * @author by milhamj on 31/10/18.
 */
class ProfileEmptyViewModel : Visitable<ProfileTypeFactory> {
    override fun type(typeFactory: ProfileTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
