package com.tokopedia.tkpdcontent;

import android.app.Activity;
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

    void doLikeKolPost(int id, LikeKolPostSubscriber likeKolPostSubscriber);

    void doUnlikeKolPost(int id, LikeKolPostSubscriber likeKolPostSubscriber);

    void actionApplinkFromActivity(Activity activity, String linkUrl);
}
