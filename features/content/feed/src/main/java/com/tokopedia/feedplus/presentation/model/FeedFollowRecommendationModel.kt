package com.tokopedia.feedplus.presentation.model

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.presentation.adapter.FeedAdapterTypeFactory
import java.net.SocketTimeoutException
import java.net.UnknownHostException

/**
 * Created By : Jonathan Darwin on July 04, 2023
 */
data class FeedFollowRecommendationModel(
    val id: String,
    val title: String,
    val description: String,
    val data: List<Profile>,
    val cursor: String,
    val status: Status,
) : Visitable<FeedAdapterTypeFactory> {

    val hasNext: Boolean
        get() = cursor.isNotEmpty()

    val isError: Boolean
        get() = status == Status.Error || status == Status.NoInternet

    val isLoading: Boolean
        get() = status == Status.Loading

    override fun type(typeFactory: FeedAdapterTypeFactory): Int = typeFactory.type(this)

    enum class Status {
        Unknown,
        Loading,
        Success,
        Error,
        NoInternet;

        companion object {
            fun getErrorStatus(throwable: Throwable): Status {
                return when (throwable) {
                    is UnknownHostException, is SocketTimeoutException -> {
                        FeedFollowRecommendationModel.Status.NoInternet
                    }
                    else -> FeedFollowRecommendationModel.Status.Error
                }
            }
        }
    }

    data class Profile(
        val id: String,
        val encryptedId: String,
        val name: String,
        val badge: String,
        val type: ProfileType,
        val imageUrl: String,
        val thumbnailUrl: String,
        val videoUrl: String,
        val applink: String,
        val isFollowed: Boolean,
    ) {
        val isShop: Boolean
            get() = type == ProfileType.Seller
    }

    enum class ProfileType {
        Seller, Ugc, Unknown;

        companion object {
            fun mapType(type: Int): ProfileType {
                return when (type) {
                    2 -> Seller
                    3 -> Ugc
                    else -> Unknown
                }
            }
        }
    }

    companion object {
        val Empty: FeedFollowRecommendationModel
            get() = FeedFollowRecommendationModel(
                id = "",
                title = "",
                description = "",
                data = emptyList(),
                cursor = "",
                status = Status.Unknown,
            )
    }
}
