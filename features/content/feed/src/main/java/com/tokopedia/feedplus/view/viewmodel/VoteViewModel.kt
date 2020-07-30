package com.tokopedia.feedplus.view.viewmodel

import com.tokopedia.vote.domain.model.VoteStatisticDomainModel

/**
 * @author by yoasfs on 2019-12-09
 */
data class VoteViewModel (
        var voteModel: VoteStatisticDomainModel = VoteStatisticDomainModel(),
        var optionId: String = "",
        var rowNumber: Int = 0,
        var errorMessage: String = "",
        var isSuccess: Boolean = false
)