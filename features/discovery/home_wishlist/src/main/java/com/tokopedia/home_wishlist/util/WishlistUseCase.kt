package com.tokopedia.home_wishlist.util

import com.tokopedia.usecase.UseCase
import rx.Subscriber

class WishlistUseCase<T> {
    fun executeUseCase(useCase: UseCase<T>, subscriber: Subscriber<T>) {
        useCase.execute(subscriber)
    }
}