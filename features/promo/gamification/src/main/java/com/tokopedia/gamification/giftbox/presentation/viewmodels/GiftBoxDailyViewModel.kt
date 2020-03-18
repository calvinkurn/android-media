package com.tokopedia.gamification.giftbox.presentation.viewmodels

import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.gamification.giftbox.data.di.IO
import com.tokopedia.gamification.giftbox.data.di.MAIN
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyRewardUseCase
import com.tokopedia.gamification.giftbox.domain.GiftBoxDailyUseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Named

class GiftBoxDailyViewModel @Inject constructor(@Named(MAIN) uiDispatcher: CoroutineDispatcher,
                                                @Named(IO) workerDispatcher: CoroutineDispatcher,
                                                giftBoxDailyUseCase: GiftBoxDailyUseCase,
                                                giftBoxDailyRewardUseCase: GiftBoxDailyRewardUseCase)
    : BaseViewModel(uiDispatcher) {
}