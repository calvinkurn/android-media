package com.tokopedia.people.analytic.tracker

import com.tokopedia.people.analytic.tracker.review.UserProfileReviewTracker
import javax.inject.Inject

class UserProfileTracker @Inject constructor(
    userProfileGeneralTracker: UserProfileGeneralTracker,
    userProfileReviewTracker: UserProfileReviewTracker,
) : UserProfileGeneralTracker by userProfileGeneralTracker,
    UserProfileReviewTracker by userProfileReviewTracker
