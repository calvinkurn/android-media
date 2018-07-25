package com.tokopedia.navigation.data.mapper;

import com.tokopedia.graphql.data.model.GraphqlResponse;
import com.tokopedia.navigation.R;
import com.tokopedia.navigation.data.entity.ChatEntity;
import com.tokopedia.navigation.data.entity.InboxEntity;
import com.tokopedia.navigation.data.entity.NotificationEntity;
import com.tokopedia.navigation.domain.model.Inbox;
import com.tokopedia.navigation.domain.model.Notification;

import java.util.ArrayList;
import java.util.List;

import rx.functions.Func1;

/**
 * Created by meta on 25/07/18.
 */
public class NotificationMapper implements Func1<GraphqlResponse, NotificationEntity> {

    @Override
    public NotificationEntity call(GraphqlResponse graphqlResponse) {
        return graphqlResponse.getData(NotificationEntity.class);
    }

    public static Notification notificationMapper(NotificationEntity.Notification entity) {
        Notification data = new Notification();
        try {
            data.setTotalCart(Integer.getInteger(entity.getTotalCart()));
            data.setTotalInbox(totalInbox(entity));
            data.setTotalNotif(Integer.getInteger(entity.getTotalNotif()));
            data.setTotalWishlist(Integer.getInteger(entity.getInbox().getWishlist()));
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return data;
    }

    public static Integer totalInbox(NotificationEntity.Notification entity) {
        Integer total = 0;
        try {
            total += Integer.getInteger(entity.getChat().getUnreads());
            total += Integer.getInteger(entity.getInbox().getTalk());
            total += Integer.getInteger(entity.getInbox().getReview());
            total += Integer.getInteger(entity.getInbox().getTicket());
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return total;
    }

    public static List<Inbox> inboxMapper(InboxEntity inboxEntity, ChatEntity chatEntity) {
        List<Inbox> inboxList = new ArrayList<>();

        Inbox chat = new Inbox(R.drawable.ic_topchat, R.string.chat, R.string.chat_desc);
        if (chatEntity.getUnreads() != null) {
            chat.setTotalBadge(chatEntity.getUnreads());
        }
        inboxList.add(chat);

        Inbox discuss = new Inbox(R.drawable.ic_tanyajawab, R.string.diskusi, R.string.diskusi_desc);
        if (inboxEntity.getTalk() != null) {
            discuss.setTotalBadge(inboxEntity.getTalk());
        }
        inboxList.add(discuss);

        Inbox review = new Inbox(R.drawable.ic_ulasan, R.string.ulasan, R.string.ulasan_desc);
        if (inboxEntity.getReview() != null) {
            review.setTotalBadge(inboxEntity.getReview());
        }
        inboxList.add(review);

        Inbox ticket = new Inbox(R.drawable.ic_pesan_bantuan, R.string.pesan_bantuan, R.string.pesan_bantuan_desc);
        if (inboxEntity.getTicket() != null) {
            ticket.setTotalBadge(inboxEntity.getTicket());
        }
        inboxList.add(ticket);

        return inboxList;
    }
}
