package com.tokopedia.search.result.presentation.model

import com.google.android.gms.tagmanager.DataLayer
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.search.result.presentation.view.typefactory.ProfileListTypeFactory

data class ProfileViewModel (
    var id: String = "",
    var name: String = "",
    var imgUrl: String = "",
    var username: String = "",
    var followed: Boolean = false,
    var isKol: Boolean = false,
    var isAffiliate: Boolean = false,
    var followers: Int = 0,
    var post_count: Int = 0,
    val isRecommendation: Boolean = false
) : Visitable<ProfileListTypeFactory> {

    companion object {
        private const val KEY_ID = "id"
        private const val KEY_NAME = "name"
        private const val KEY_CREATIVE = "creative"
        private const val KEY_POSITION = "position"

        private const val VAL_NAME = "/search result - profile"
        private const val RECOMMENDATION_NAME = "/no search result - top profile"
    }

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

    fun getRecommendationTrackingObject() : Any {
        return DataLayer.mapOf(
                KEY_ID, id,
                KEY_NAME, RECOMMENDATION_NAME,
                KEY_CREATIVE, name.toLowerCase(),
                KEY_POSITION, position.toString()
        )
    }
}