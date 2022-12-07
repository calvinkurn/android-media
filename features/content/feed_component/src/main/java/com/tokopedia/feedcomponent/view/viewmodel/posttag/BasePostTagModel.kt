package com.tokopedia.feedcomponent.view.viewmodel.posttag

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedcomponent.view.adapter.posttag.PostTagTypeFactory

/**
 * @author by yoasfs on 2019-07-18
 */
interface BasePostTagModel: Visitable<PostTagTypeFactory> {
    var postId: String
    var positionInFeed: Int
    var feedType: String
}
