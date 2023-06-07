package com.tokopedia.people.analytic.tracker

import javax.inject.Inject

class UserProfileTracker @Inject constructor(
    userProfileGeneralTracker: UserProfileGeneralTracker,
) : UserProfileGeneralTracker by userProfileGeneralTracker
{

}
