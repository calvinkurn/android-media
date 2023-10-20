package com.tokopedia.cartrevamp.view.uimodel

import com.tokopedia.cart.data.model.response.shopgroupsimplified.CartOnBoardingData
import com.tokopedia.coachmark.CoachMark2
import com.tokopedia.coachmark.CoachMark2Item

data class CartMainCoachMarkUiModel(
    val coachMark: CoachMark2? = null,
    val coachMarkItems: ArrayList<CoachMark2Item> = arrayListOf(),
    val noteOnBoardingData: CartOnBoardingData = CartOnBoardingData(),
    val wishlistOnBoardingData: CartOnBoardingData = CartOnBoardingData()
)
