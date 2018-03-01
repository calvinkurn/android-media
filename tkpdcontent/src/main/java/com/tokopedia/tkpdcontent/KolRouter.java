package com.tokopedia.tkpdcontent;

import android.content.Context;
import android.content.Intent;

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
}
