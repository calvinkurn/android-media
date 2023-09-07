package com.tokopedia.stories.creation.view.model

/**
 * Created By : Jonathan Darwin on September 07, 2023
 */
sealed interface StoriesCreationBottomSheetType {

    object TooMuchStories : StoriesCreationBottomSheetType

    object Unknown : StoriesCreationBottomSheetType
}
