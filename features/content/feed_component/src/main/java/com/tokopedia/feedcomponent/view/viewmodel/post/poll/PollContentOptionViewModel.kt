package com.tokopedia.feedcomponent.view.viewmodel.post.poll

/**
 * @author by milhamj on 12/12/18.
 */
data class PollContentOptionViewModel(
        var optionId: String = "0",
        var option: String = "",
        var imageUrl: String = "",
        var redirectLink: String = "",
        var percentage: Int = 0,
        var selected: Int = 0
) {
    companion object {
        const val DEFAULT = 0
        const val UNSELECTED = 1
        const val SELECTED = 2
    }
}