package com.tokopedia.feedcomponent.view.viewmodel.post.video

import com.tokopedia.feedcomponent.view.viewmodel.data.FooterViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.HeaderViewModel
import com.tokopedia.feedcomponent.view.viewmodel.data.template.TemplateFooterViewModel
import com.tokopedia.feedcomponent.view.viewmodel.post.BasePostViewModel

/**
 * @author by yfsx on 20/03/19.
 */
data class VideoViewModel(
        var id: String = "",
        var thumbnail: String = "",
        var url: String = "",
        var header: HeaderViewModel = HeaderViewModel(),
        var footer: FooterViewModel = FooterViewModel(),
        var templateFooter: TemplateFooterViewModel = TemplateFooterViewModel(),
        override var postId: Int = 0,
        override var positionInFeed: Int = 0
) : BasePostViewModel {
}