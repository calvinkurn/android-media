package com.tokopedia.buyerorderdetail.di

import com.tokopedia.buyerorderdetail.presentation.fragment.BuyerOrderDetailFragment
import dagger.Component

@Component
interface BuyerOrderDetailComponent {
    fun inject(buyerOrderDetailFragment: BuyerOrderDetailFragment)
}