package com.tokopedia.tokomember_seller_dashboard.domain


import com.tokopedia.tokomember_seller_dashboard.model.MembershipData
import com.tokopedia.tokomember_seller_dashboard.view.adapter.mapper.TokomemberIntroMapper
import com.tokopedia.tokomember_seller_dashboard.view.adapter.model.TokomemberIntroItem
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class TokomemberIntroSectionMapperUsecase @Inject constructor() : UseCase<TokomemberIntroItem>() {
    private lateinit var membershipData: MembershipData

    fun getIntroSectionData(
        membershipData: MembershipData?,
        onSuccess: (TokomemberIntroItem) -> Unit,
        onError: (Throwable) -> Unit
    ) {
        this.membershipData = membershipData ?: return
        execute({
            if (!it.tokoVisitable.isNullOrEmpty())
                onSuccess(it)
        }, { onError(it) })
    }

    override suspend fun executeOnBackground(): TokomemberIntroItem {
        val gyroRecommendation = TokomemberIntroMapper.getTokomemberIntroData(membershipData)
        gyroRecommendation.let { return it }
        return TokomemberIntroItem(arrayListOf())
    }

}



