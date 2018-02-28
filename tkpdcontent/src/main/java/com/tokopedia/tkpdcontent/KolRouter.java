package com.tokopedia.tkpdcontent;

import android.content.Context;
import android.content.Intent;

import com.tokopedia.tkpdcontent.feature.profile.view.subscriber.LikeKolPostSubscriber;

/**
 * @author by nisie on 2/6/18.
 */

public interface KolRouter {
    Intent getKolCommentActivity(Context context, String avatarUrl, String name,
                                 String review, String time, String userId,
                                 String productImageUrl, String contentName,
                                 String price, boolean isWishlishted, int postId,
                                 int rowNumber);

    String getKolCommentArgsPosition();

    String getKolCommentArgsTotalComment();

    void doLikeKolPost(int id, int action, LikeKolPostSubscriber likeKolPostSubscriber);

    void doFollowKolPost();
}
