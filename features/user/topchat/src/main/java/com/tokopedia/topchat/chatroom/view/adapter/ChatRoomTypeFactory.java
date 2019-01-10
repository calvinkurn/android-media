package com.tokopedia.topchat.chatroom.view.adapter;

import android.view.View;

import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder;
import com.tokopedia.topchat.chatroom.view.viewmodel.TimeMachineChatModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.TypingChatModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.fallback.FallbackAttachmentViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement.ImageAnnouncementViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageannouncement
        .ImageDualAnnouncementViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.imageupload.ImageUploadViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.message.MessageViewModel;
import com.tokopedia.topchat.chatroom.view.viewmodel.productattachment.ProductAttachmentViewModel;

/**
 * Created by stevenfredian on 9/27/17.
 */

public interface ChatRoomTypeFactory {

    AbstractViewHolder createViewHolder(View view, int viewType);

    // NEW VERSION

    int type(FallbackAttachmentViewModel fallbackAttachmentViewModel);

    int type(ProductAttachmentViewModel productAttachmentViewModel);

    int type(ImageAnnouncementViewModel imageAnnouncementViewModel);

    int type(ImageUploadViewModel attachImageModel);

    int type(MessageViewModel messageViewModel);

    //OTHER
    int type(TimeMachineChatModel timeMachineChatModel);

    int type(TypingChatModel typingChatModel);

    int type(ImageDualAnnouncementViewModel imageDualAnnouncementViewModel);


}
