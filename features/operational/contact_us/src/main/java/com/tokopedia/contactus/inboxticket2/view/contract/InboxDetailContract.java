package com.tokopedia.contactus.inboxticket2.view.contract;

import android.text.TextWatcher;

import com.tokopedia.contactus.inboxticket2.data.model.Tickets;
import com.tokopedia.contactus.inboxticket2.domain.AttachmentItem;
import com.tokopedia.contactus.inboxticket2.view.customview.CustomEditText;
import com.tokopedia.contactus.inboxticket2.view.utils.Utils;
import com.tokopedia.contactus.orderquery.data.ImageUpload;

import java.util.ArrayList;
import java.util.List;

public interface InboxDetailContract {
    interface InboxDetailView extends InboxBaseContract.InboxBaseView {
        void renderMessageList(Tickets ticketDetail);

        void updateAddComment();

        void addImage(ImageUpload image);

        void setSubmitButtonEnabled(boolean enabled);

        List<ImageUpload> getImageList();

        String getUserMessage();

        String getTicketID();

        void showSendProgress();

        void hideSendProgress();

        void toggleTextToolbar(int visibility);

        void askCustomReason();

        void showIssueClosed();

        void enterSearchMode(String search, int total);

        void exitSearchMode();

        void showImagePreview(int position, ArrayList<String> imagesURL);

        void setCurrentRes(int currentRes);

        void updateClosedStatus(String subject);

        String getCommentID();
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

        void onClickEmoji(int number);
    }
}
