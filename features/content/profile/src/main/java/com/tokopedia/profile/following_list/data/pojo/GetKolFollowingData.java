
package com.tokopedia.profile.following_list.data.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class GetKolFollowingData {

    @SerializedName("get_user_kol_following")
    @Expose
    public GetUserKolFollowing getUserKolFollowing;

}
