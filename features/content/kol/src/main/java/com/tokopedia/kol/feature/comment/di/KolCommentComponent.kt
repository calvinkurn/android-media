package com.tokopedia.kol.feature.comment.di

import com.tokopedia.kol.common.di.KolComponent
import com.tokopedia.kol.feature.comment.view.activity.ContentCommentActivity
import com.tokopedia.kol.feature.comment.view.fragment.KolCommentNewFragment
import dagger.Component

/**
 * @author by milhamj on 18/04/18.
 */
@KolCommentScope
@Component(
    modules = [KolCommentModule::class],
    dependencies = [KolComponent::class]
)
interface KolCommentComponent {
    fun inject(kolCommentNewActivity: ContentCommentActivity)
    fun inject(kolCommentNewFragment: KolCommentNewFragment)
}
