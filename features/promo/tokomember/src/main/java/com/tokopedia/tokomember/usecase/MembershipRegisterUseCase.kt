package com.tokopedia.tokomember.usecase

import com.tokopedia.tokomember.model.MembershipRegister
import com.tokopedia.tokomember.model.MembershipRegisterResponse
import com.tokopedia.tokomember.repository.TokomemberRepository
import com.tokopedia.usecase.coroutines.UseCase
import javax.inject.Inject

class MembershipRegisterUseCase @Inject constructor(private val tokomemberRepository: TokomemberRepository) :
    UseCase<MembershipRegisterResponse>() {

    lateinit var cardId: String
    fun registerMembership(
        cardId: String?,
        success: (MembershipRegister?) -> Unit,
        onFail: (Throwable) -> Unit
    ) {
        if (cardId != null) {
            this.cardId = cardId
        }
        execute({
            success(it.data)
        }, {
            onFail(it)
        })
    }

    override suspend fun executeOnBackground(): MembershipRegisterResponse {
        return tokomemberRepository.registerMembership(cardId)
    }
}

object MembershipRegisterParams {
    const val CARD_ID = "cardID"
}