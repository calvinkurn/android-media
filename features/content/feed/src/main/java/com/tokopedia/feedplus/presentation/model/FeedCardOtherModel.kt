package com.tokopedia.feedplus.presentation.model

/**
 * Created By : Muhammad Furqan on 28/02/23
 */
data class FeedViewModel(
    val label: String = "",
    val count: Int = 0,
    val countFmt: String = ""
)

data class FeedLikeModel(
    val label: String = "",
    val count: Int = 0,
    val countFmt: String = "",
    val likedBy: List<String> = emptyList(),
    val isLiked: Boolean = false
)

data class FeedCommentModel(
    val label: String = "",
    val count: Int = 0,
    val countFmt: String = "",
    val items: List<FeedCommentItemModel> = emptyList()
)

data class FeedCommentItemModel(
    val id: String,
    val author: FeedAuthorModel,
    val text: String
)

data class FeedShareModel(
    val contentId: String,
    val author: FeedAuthorModel,
    val appLink: String,
    val webLink: String,
    val mediaUrl: String
)

data class FeedFollowModel(
    val label: String = "",
    val count: Int = 0,
    val countFmt: String = "",
    val isFollowed: Boolean = false
)

data class FeedScoreModel(
    val label: String = "",
    val value: String = ""
) {

    val isContentScore: Boolean
        get() = label == TOTAL_SCORE

    companion object {
        private const val TOTAL_SCORE = "TotalScore"
    }
}

data class FeedCardCtaModel(
    val texts: List<String> = emptyList(),
    val subtitles: List<String> = emptyList(),
    val color: String = "",
    val colorGradient: List<FeedCardCtaGradientModel> = emptyList()
)

data class FeedCardCtaGradientModel(
    val color: String,
    val position: Double
)

data class FeedCardCampaignModel(
    val id: String = "",
    val status: String = "",
    val name: String = "",
    val shortName: String = "",
    val startTime: String = "",
    val endTime: String = "",
    val restrictions: List<FeedCardCampaignRestrictionModel> = emptyList(),
    val isReminderActive: Boolean = false
) {

    val isExclusiveForMember: Boolean
        get() = restrictions.firstOrNull { it.label == LABEL_FOLLOWERS_ONLY && it.isActive } != null

    val isOngoing: Boolean
        get() = status == ONGOING

    val isUpcoming: Boolean
        get() = status == UPCOMING

    companion object {
        private const val LABEL_FOLLOWERS_ONLY = "followers_only"

        const val NO = "no"
        const val ONGOING = "ongoing"
        const val UPCOMING = "upcoming"
    }
}

data class FeedCardCampaignRestrictionModel(
    val isActive: Boolean,
    val label: String
)
