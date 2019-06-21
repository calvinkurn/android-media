package com.tokopedia.search.result.presentation.model

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProfileListTypeFactory

data class ProfileViewModel (
    var id: String,
    var name: String,
    var imgUrl: String,
    var username: String,
    var followed: Boolean,
    var isKol: Boolean,
    var isAffiliate: Boolean,
    var followers: Int,
    var post_count: Int
) : Visitable<ProfileListTypeFactory> {

    val KEY_ID = "id"
    val KEY_NAME = "name"
    val KEY_CREATIVE = "creative"
    val KEY_POSITION = "position"

    val VAL_NAME = "/search result - profile"

    var position = 0

    override fun type(typeFactory: ProfileListTypeFactory): Int {
        return typeFactory.type(this)
    }

    fun getTrackingObject() : Any {
        return DataLayer.mapOf(
            KEY_ID, id,
            KEY_NAME, VAL_NAME,
            KEY_CREATIVE, name.toLowerCase(),
            KEY_POSITION, position.toString()
        )
    }
}