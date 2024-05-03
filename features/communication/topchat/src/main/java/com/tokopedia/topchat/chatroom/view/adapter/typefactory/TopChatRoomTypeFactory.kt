package com.tokopedia.topchat.chatroom.view.adapter.typefactory

import android.view.ViewGroup
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingBannerUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.roomsettings.RoomSettingFraudAlertUiModel
import com.tokopedia.topchat.chatroom.domain.pojo.srw.SrwBubbleUiModel
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.ProductCarouselListAttachmentViewHolder
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.common.AdapterListener
import com.tokopedia.topchat.chatroom.view.adapter.viewholder.product_bundling.ProductBundlingCarouselViewHolder
import com.tokopedia.topchat.chatroom.view.uimodel.*
import com.tokopedia.topchat.chatroom.view.uimodel.BroadcastSpamHandlerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ImageDualAnnouncementUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.ReminderTickerUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.MultipleProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.product_bundling.ProductBundlingUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherCarouselUiModel
import com.tokopedia.topchat.chatroom.view.uimodel.voucher.TopChatRoomVoucherUiModel

interface TopChatRoomTypeFactory {

    fun type(imageDualAnnouncementViewModel: ImageDualAnnouncementUiModel): Int

    fun type(voucherViewModel: TopChatRoomVoucherUiModel): Int
    fun type(voucherCarouselUiModel: TopChatRoomVoucherCarouselUiModel): Int

    fun getItemViewType(visitables: List<Visitable<*>>, position: Int, default: Int): Int

    fun createViewHolder(
        parent: ViewGroup,
        type: Int,
        productCarouselListListener: ProductCarouselListAttachmentViewHolder.Listener,
        productBundlingCarouselListener: ProductBundlingCarouselViewHolder.Listener,
        adapterListener: AdapterListener
    ): AbstractViewHolder<*>

    fun type(roomSettingFraudAlertUiModel: RoomSettingFraudAlertUiModel): Int

    fun type(roomSettingBannerUiModel: RoomSettingBannerUiModel): Int

    fun type(productCarouselUiModel: TopChatRoomProductCarouselUiModel): Int

    fun type(headerDateUiModel: HeaderDateUiModel): Int

    fun type(stickerUiModel: StickerUiModel): Int

    fun type(broadcastSpamHandlerUiModel: BroadcastSpamHandlerUiModel): Int

    fun type(broadCastUiModel: TopChatRoomBroadcastUiModel): Int

    fun type(reviewUiModel: ReviewUiModel): Int
    fun type(srwBubbleUiModel: SrwBubbleUiModel): Int
    fun type(getReminderTickerUiModel: ReminderTickerUiModel): Int

    fun type(multipleProductBundlingUiModel: MultipleProductBundlingUiModel): Int
    fun type(productBundlingUiModel: ProductBundlingUiModel): Int

    fun type(orderCancellationUiModel: TopChatRoomOrderCancellationUiModel): Int
}
