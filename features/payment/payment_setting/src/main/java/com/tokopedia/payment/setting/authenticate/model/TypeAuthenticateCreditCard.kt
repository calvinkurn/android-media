package com.tokopedia.payment.setting.authenticate.model

import android.os.Parcelable
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.payment.setting.authenticate.view.adapter.AuthenticateCCAdapterFactory
import kotlinx.parcelize.Parcelize

@Parcelize
data class TypeAuthenticateCreditCard(
    var isSelected: Boolean = false,
    var description: String = "",
    var title: String = "",
    var stateWhenSelected: Int = 0
) :
    Visitable<AuthenticateCCAdapterFactory>, Parcelable {

    override fun type(typeFactory: AuthenticateCCAdapterFactory): Int {
        return typeFactory.type(this)
    }
}
