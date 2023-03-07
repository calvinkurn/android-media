package com.tokopedia.people.views

import com.tokopedia.people.views.uimodel.FollowListUiModel

/**
 * Created by meyta.taliti on 07/03/23.
 */
interface TotalFollowListener {
    fun updateFollowCount(followCount: FollowListUiModel.FollowCount)
}
