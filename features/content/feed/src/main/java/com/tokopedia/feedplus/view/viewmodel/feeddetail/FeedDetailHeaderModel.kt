package com.tokopedia.feedplus.view.viewmodel.feeddetail

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.feedplus.view.adapter.typefactory.feeddetail.FeedPlusDetailTypeFactory

/**
 * @author by nisie on 5/24/17.
 */
class FeedDetailHeaderModel(var shopId: Int,
                            var shopName: String,
                            var shopAvatar: String,
                            var isGoldMerchant: Boolean,
                            var time: String,
                            var isOfficialStore: Boolean,
                            var shareLinkURL: String,
                            var shareLinkDescription: String,
                            var actionText: String,
                            var activityId: String) : Visitable<FeedPlusDetailTypeFactory> {
    override fun type(typeFactory: FeedPlusDetailTypeFactory): Int {
        return typeFactory.type(this)
    }

}