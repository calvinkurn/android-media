package com.tokopedia.discovery_component.widgets.automatecoupon

interface IAutomateCouponView {
    fun setModel(couponModel: AutomateCouponModel)

    fun setState(state: ButtonState)

    fun onClick(action: () -> Unit)
}
