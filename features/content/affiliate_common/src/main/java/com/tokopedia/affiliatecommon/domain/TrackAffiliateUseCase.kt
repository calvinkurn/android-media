package com.tokopedia.affiliatecommon.domain

import com.tokopedia.usecase.coroutines.UseCase

/**
 * @author by milhamj on 2019-08-20.
 */
class TrackAffiliateUseCase: UseCase<Boolean>() {
    override suspend fun executeOnBackground(): Boolean {

        return false
    }
}