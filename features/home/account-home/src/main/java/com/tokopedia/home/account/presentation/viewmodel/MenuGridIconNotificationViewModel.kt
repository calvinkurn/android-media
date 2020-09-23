package com.tokopedia.home.account.presentation.viewmodel

import android.os.Parcel
import android.os.Parcelable
import com.tokopedia.home.account.presentation.adapter.AccountTypeFactory
import com.tokopedia.home.account.presentation.viewmodel.base.ParcelableViewModel
import kotlinx.android.parcel.Parcelize

/**
 * Created by fwidjaja on 15/07/20.
 */
@Parcelize
data class MenuGridIconNotificationViewModel(
        var title: String = "",
        var linkText: String = "",
        var applinkUrl: String = "",
        var items: List<MenuGridIconNotificationItemViewModel> = listOf(),
        var titleTrack : String = "",
        var sectionTrack: String = ""
) : ParcelableViewModel<AccountTypeFactory> {
    override fun type(typeFactory: AccountTypeFactory): Int {
        return typeFactory.type(this)
    }
}