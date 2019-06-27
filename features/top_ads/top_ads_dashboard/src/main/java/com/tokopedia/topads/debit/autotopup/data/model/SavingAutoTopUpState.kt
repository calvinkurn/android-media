package com.tokopedia.topads.debit.autotopup.data.model

sealed class SavingAutoTopUpState
object Loading : SavingAutoTopUpState()
data class ResponseSaving(val isSuccess: Boolean,
                          val throwable: Throwable?): SavingAutoTopUpState()