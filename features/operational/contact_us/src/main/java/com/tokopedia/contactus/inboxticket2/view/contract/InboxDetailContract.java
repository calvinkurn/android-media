package com.tokopedia.contactus.inboxticket2.view.contract;

import android.text.TextWatcher;

import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem;
import com.tokopedia.contactus.inboxticket2.domain.Tickets;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;
import com.tokopedia.contactus.orderquery.data.ImageUpload;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by pranaymohapatra on 22/06/18.
 */

public interface InboxDetailContract {
    interface InboxDetailView extends InboxBaseContract.InboxBaseView {
        void showCollapsedMessages();

        void hideMessages();

        void renderMessageList(Tickets ticketDetail);

        void addimage(ImageUpload image);

        void setSubmitButtonEnabled(boolean enabled);

        List<ImageUpload> getImageList();

        String getUserMessage();

        void showSendProgress();

        void hideSendProgress();

        void toggleTextToolbar(int visibility);

        void askCustomReason();

        void showIssueClosed();

        void enterSearchMode(String search);

        void exitSearchMode();

        void showImagePreview(int position, ArrayList<String> imagesURL);
    }

    interface InboxDetailPresenter extends InboxBaseContract.InboxBasePresenter {
        CustomEditText.Listener getSearchListener();

        void onImageSelect(ImageUpload image);

        TextWatcher watcher();

        void sendMessage();

        void clickRate(int id, String commentID);

        void setBadRating(int position);

        void sendCustomReason(String customReason);

        int getNextResult();

        int getPreviousResult();

        Utils getUtils();

        void showImagePreview(int position, List<AttachmentItem> imagesURL);
    }
}
