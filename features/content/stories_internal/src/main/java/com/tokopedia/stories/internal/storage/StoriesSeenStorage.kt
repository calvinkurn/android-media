package com.tokopedia.stories.internal.storage

/**
 * Created by kenny.hadisaputra on 22/08/23
 */
interface StoriesSeenStorage {

    suspend fun hasSeenAllAuthorStories(key: Author, laterThanMillis: Long): Boolean

    suspend fun setSeenAllAuthorStories(key: Author)

    sealed interface Author {

        abstract val id: String
        @JvmInline
        value class Shop(override val id: String) : Author

        @JvmInline
        value class User(override val id: String) : Author
    }
}
