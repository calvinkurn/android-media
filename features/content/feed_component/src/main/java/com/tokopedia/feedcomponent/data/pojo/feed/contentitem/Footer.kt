package com.tokopedia.feedcomponent.data.pojo.feed.contentitem

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Footer (
    @SerializedName("like")
    @Expose
    var like: Like = Like(),
    @SerializedName("comment")
    @Expose
    var comment: Comment = Comment(),
    @SerializedName("buttonCta")
    @Expose
    var buttonCta: ButtonCta = ButtonCta(),
    @SerializedName("share")
    @Expose
    var share: Share = Share()

)