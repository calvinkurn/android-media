package com.tokopedia.profile.view.viewmodel

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.profile.view.adapter.factory.ProfileTypeFactory

/**
 * @author by milhamj on 9/20/18.
 */
class ProfilePostViewModel(
        val name: String = "",
        val avatar: String = "",
        val time: String = "",
        val productId: Int = 0,
        val images: List<String> = ArrayList(),
        val isOwner: Boolean = false) : Visitable<ProfileTypeFactory> {

    override fun type(typeFactory: ProfileTypeFactory?): Int {
        return typeFactory?.type(this) ?: 0
    }
}