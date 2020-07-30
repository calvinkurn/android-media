package com.tokopedia.kolcommon.view.listener

/**
 * Created by jegul on 2019-11-13
 */
interface KolPostViewHolderListener {

    fun onGoToKolProfile(rowNumber: Int, userId: String, postId: Int)

    fun onGoToKolProfileUsingApplink(rowNumber: Int, applink: String)

    fun onOpenKolTooltip(rowNumber: Int, uniqueTrackingId: String, url: String)

    fun trackContentClick(hasMultipleContent: Boolean, activityId: String,
                          activityType: String, position: String)

    fun trackTooltipClick(hasMultipleContent: Boolean, activityId: String,
                          activityType: String, position: String)

    fun onFollowKolClicked(rowNumber: Int, id: Int)

    fun onUnfollowKolClicked(rowNumber: Int, id: Int)

    fun onLikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                         activityType: String)

    fun onUnlikeKolClicked(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                           activityType: String)

    fun onGoToKolComment(rowNumber: Int, id: Int, hasMultipleContent: Boolean,
                         activityType: String)

    fun onEditClicked(hasMultipleContent: Boolean, activityId: String,
                      activityType: String)
}
