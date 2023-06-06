package com.tokopedia.inbox.universalinbox.view.viewmodel

import androidx.lifecycle.DefaultLifecycleObserver
import com.tokopedia.abstraction.base.view.viewmodel.BaseViewModel
import com.tokopedia.abstraction.common.dispatcher.CoroutineDispatchers
import com.tokopedia.iconunify.IconUnify
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuSectionUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxMenuUiModel
import com.tokopedia.inbox.universalinbox.view.uimodel.UniversalInboxShopInfoUiModel
import com.tokopedia.user.session.UserSessionInterface
import javax.inject.Inject

class UniversalInboxViewModel @Inject constructor(
    private val userSession: UserSessionInterface,
    private val dispatcher: CoroutineDispatchers
): BaseViewModel(dispatcher.main), DefaultLifecycleObserver {

    fun dummy(): List<Any> {
        return listOf(
            UniversalInboxMenuSectionUiModel("Percakapan"),
            UniversalInboxMenuUiModel(
                title = "Chat Penjual",
                icon = IconUnify.CHAT,
                counter = 2
            ),
            UniversalInboxMenuUiModel(
                title = "Chat Pembeli",
                icon = IconUnify.SHOP,
                counter = 100,
                additionalInfo = UniversalInboxShopInfoUiModel(
                    avatar = userSession.shopAvatar,
                    shopName = userSession.shopName + " ${userSession.shopName}" + " ${userSession.shopName}" + " ${userSession.shopName}"
                )
            ),
            UniversalInboxMenuSectionUiModel("Lainnya"),
            UniversalInboxMenuUiModel(
                title = "Diskusi Produk",
                icon = IconUnify.DISCUSSION,
                counter = 0
            ),
            UniversalInboxMenuUiModel(
                title = "Ulasan",
                icon = IconUnify.STAR,
                counter = 99
            ),
        )
    }

    fun loadMoreRecommendation() {

    }
}
