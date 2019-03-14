package com.tokopedia.profile.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.profile.view.adapter.factory.ProfileEmptyTypeFactory
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactory

/**
 * @author by milhamj on 31/10/18.
 */
class ProfileEmptyViewModel : Visitable<ProfileEmptyTypeFactory> {
    override fun type(typeFactory: ProfileEmptyTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}
