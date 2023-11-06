package com.tokopedia.creation.common.upload.model.stories

/**
 * Created By : Jonathan Darwin on October 05, 2023
 */
enum class StoriesStatus(val value: Int) {
    Deleted(-1),
    Draft(0),
    Active(1),
    Upcoming(2),
    Moderated(3),
    Expired(4),
    Transcoding(5),
    TranscodingFailed(6),
}
