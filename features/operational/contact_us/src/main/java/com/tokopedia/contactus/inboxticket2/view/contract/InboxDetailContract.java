package com.tokopedia.contactus.inboxticket2.view.contract;

import android.text.TextWatcher;

import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.orderquery.data.ImageUpload;

import java.util.List;

/**
 * Created by pranaymohapatra on 22/06/18.
 */

public interface InboxDetailContract {
    interface InboxDetailView extends InboxBaseContract.InboxBaseView {
        void showCollapsedMessages();

        void hideMessages();

        void renderMessageList(Tickets ticketDetail);

        void toggleSearch(int visibility);

        void addimage(ImageUpload image);

        void setSubmitButtonEnabled(boolean enabled);

        List<ImageUpload> getImageList();

        String getUserMessage();

        void showSendProgress();

        void hideSendProgress();

    }

    interface InboxDetailPresenter extends InboxBaseContract.InboxBasePresenter {
        CustomEditText.Listener getSearchListener();

        void onImageSelect(ImageUpload image);

        TextWatcher watcher();

        void sendMessage();
    }
}
