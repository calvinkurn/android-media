package com.tokopedia.buyerorder.recharge.presentation.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.buyerorder.recharge.presentation.adapter.RechargeOrderDetailTypeFactory
import com.tokopedia.kotlin.extensions.view.orZero
import kotlinx.android.parcel.Parcelize

/**
 * @author by furqan on 02/11/2021
 */
@Parcelize
data class RechargeOrderDetailStaticButtonModel(
        val iconUrl: String,
        @DrawableRes val iconRes: Int,
        val title: String,
        @StringRes val titleRes: Int,
        val subtitle: String,
        @StringRes val subtitleRes: Int,
        val actionUrl: String
) : Parcelable, Visitable<RechargeOrderDetailTypeFactory> {
    override fun type(typeFactory: RechargeOrderDetailTypeFactory?): Int =
            typeFactory?.type(this).orZero()
}