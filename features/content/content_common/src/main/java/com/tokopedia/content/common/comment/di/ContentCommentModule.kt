package com.tokopedia.content.common.comment.di

import androidx.fragment.app.Fragment
import com.tokopedia.abstraction.base.view.fragment.FragmentKey
import com.tokopedia.content.common.comment.repository.ContentCommentRepository
import com.tokopedia.content.common.comment.repository.ContentCommentRepositoryImpl
import com.tokopedia.content.common.comment.ui.ContentCommentBottomSheet
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

/**
 * @author by astidhiyaa on 09/02/23
 */
@Module
abstract class ContentCommentModule {
    @Binds
    @IntoMap
    @FragmentKey(ContentCommentBottomSheet::class)
    abstract fun bindCommentBottomSheet(fragment: ContentCommentBottomSheet): Fragment

    @Binds
    abstract fun bindCommentRepo(commentRepo: ContentCommentRepositoryImpl): ContentCommentRepository
}
