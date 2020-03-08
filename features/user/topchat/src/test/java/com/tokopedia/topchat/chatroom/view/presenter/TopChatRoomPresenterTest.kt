package com.tokopedia.topchat.chatroom.view.presenter

import com.tokopedia.atc_common.domain.usecase.AddToCartUseCase
import com.tokopedia.chatbot.domain.mapper.TopChatRoomWebSocketMessageMapper
import com.tokopedia.imageuploader.domain.UploadImageUseCase
import com.tokopedia.network.interceptor.FingerprintInterceptor
import com.tokopedia.network.interceptor.TkpdAuthInterceptor
import com.tokopedia.seamless_login.domain.usecase.SeamlessLoginUsecase
import com.tokopedia.shop.common.domain.interactor.ToggleFavouriteShopUseCase
import com.tokopedia.topchat.chatlist.domain.usecase.DeleteMessageListUseCase
import com.tokopedia.topchat.chatroom.domain.pojo.TopChatImageUploadPojo
import com.tokopedia.topchat.chatroom.domain.usecase.*
import com.tokopedia.user.session.UserSessionInterface
import com.tokopedia.wishlist.common.usecase.AddWishListUseCase
import com.tokopedia.wishlist.common.usecase.RemoveWishListUseCase
import io.mockk.mockk
import org.spekframework.spek2.Spek
import org.spekframework.spek2.style.gherkin.Feature

class TopChatRoomPresenterTest : Spek({

    Feature("Get MessageId") {

        val getChatUseCase by memoized { mockk<GetChatUseCase>(relaxed = true) }
        val userSession by memoized { mockk<UserSessionInterface>(relaxed = true) }
        val tkpdAuthInterceptor by memoized { mockk<TkpdAuthInterceptor>(relaxed = true) }
        val fingerprintInterceptor by memoized { mockk<FingerprintInterceptor>(relaxed = true) }
        val topChatRoomWebSocketMessageMapper by memoized { mockk<TopChatRoomWebSocketMessageMapper>(relaxed = true) }
        val getTemplateChatRoomUseCase by memoized { mockk<GetTemplateChatRoomUseCase>(relaxed = true) }
        val replyChatUseCase by memoized { mockk<ReplyChatUseCase>(relaxed = true) }
        val getExistingMessageIdUseCase by memoized { mockk<GetExistingMessageIdUseCase>(relaxed = true) }
        val deleteMessageListUseCase by memoized { mockk<DeleteMessageListUseCase>(relaxed = true) }
        val changeChatBlockSettingUseCase by memoized { mockk<ChangeChatBlockSettingUseCase>(relaxed = true) }
        val getShopFollowingUseCase by memoized { mockk<GetShopFollowingUseCase>(relaxed = true) }
        val toggleFavouriteShopUseCase by memoized { mockk<ToggleFavouriteShopUseCase>(relaxed = true) }
        val addToCartUseCase by memoized { mockk<AddToCartUseCase>(relaxed = true) }
        val compressImageUseCase by memoized { mockk<CompressImageUseCase>(relaxed = true) }
        val seamlessLoginUsecase by memoized { mockk<SeamlessLoginUsecase>(relaxed = true) }
        val getChatRoomSettingUseCase by memoized { mockk<GetChatRoomSettingUseCase>(relaxed = true) }
        val addWishListUseCase by memoized { mockk<AddWishListUseCase>(relaxed = true) }
        val removeWishListUseCase by memoized { mockk<RemoveWishListUseCase>(relaxed = true) }

    }
})