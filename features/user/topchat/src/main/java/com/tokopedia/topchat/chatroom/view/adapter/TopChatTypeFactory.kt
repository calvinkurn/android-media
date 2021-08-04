package com.tokopedia.topchat.chatroom.view.adapter

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBanner
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlert
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselListAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.uimodel.*
import com.tokopedia.topchat.chatroom.view.viewmodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.QuotationUiModel
import com.tokopedia.topchat.chatroom.view.viewmodel.TopChatVoucherUiModel

interface TopChatTypeFactory {

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementUiModel): Int

    fun type(voucherViewModel: TopChatVoucherUiModel): Int

    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int

    fun createViewHolder(
            parent: ViewGroup,
            type: Int,
            productCarouselListListener: ProductCarouselListAttachmentViewHolder.Listener,
            adapterListener: AdapterListener
    ): AbstractViewHolder<*>

    fun type(roomSettingFraudAlert: RoomSettingFraudAlert): Int

    fun type(roomSettingBanner: RoomSettingBanner): Int

    fun type(quotationViewModel: QuotationUiModel): Int

    fun type(productCarouselUiModel: ProductCarouselUiModel): Int

    fun type(headerDateUiModel: HeaderDateUiModel): Int

    fun type(stickerUiModel: StickerUiModel): Int

    fun type(broadcastSpamHandlerUiModel: BroadcastSpamHandlerUiModel): Int

    fun type(broadCastUiModel: BroadCastUiModel): Int

    fun type(reviewUiModel: ReviewUiModel): Int
    fun type(srwBubbleUiModel: SrwBubbleUiModel): Int

}
