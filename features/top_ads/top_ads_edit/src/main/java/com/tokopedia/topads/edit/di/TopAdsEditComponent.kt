package com.tokopedia.topads.edit.di

import com.tokopedia.abstraction.common.di.component.BaseAppComponent
import com.tokopedia.topads.edit.di.module.TopAdEditModule
import com.tokopedia.topads.edit.di.module.ViewModelModule
import com.tokopedia.topads.edit.view.activity.EditAdGroupActivity
import com.tokopedia.topads.edit.view.activity.EditFormAdActivity
import com.tokopedia.topads.edit.view.activity.KeywordSearchActivity
import com.tokopedia.topads.edit.view.fragment.edit.*
import com.tokopedia.topads.edit.view.fragment.select.KeywordAdsListFragment
import com.tokopedia.topads.edit.view.fragment.select.NegKeywordAdsListFragment
import com.tokopedia.topads.edit.view.fragment.select.ProductAdsListFragment
import dagger.Component

/**
 * Created by Pika on 4/4/20.
 */

@EditAdScope
@Component(modules = [TopAdEditModule::class, ViewModelModule::class], dependencies = [BaseAppComponent::class])
interface TopAdsEditComponent {
    fun inject(editGroupAdFragment: EditGroupAdFragment)
    fun inject(baseEditKeywordFragment: BaseEditKeywordFragment)
    fun inject(editProductFragment: EditProductFragment)
    fun inject(keywordAdsListFragment: KeywordAdsListFragment)
    fun inject(productAdsListFragment: ProductAdsListFragment)
    fun inject(editNegativeKeywordsFragment: EditNegativeKeywordsFragment)
    fun inject(editKeywordsFragment: EditKeywordsFragment)
    fun inject(negKeywordAdsListFragment: NegKeywordAdsListFragment)
    fun inject(editFormAdActivity: EditFormAdActivity)
    fun inject(editFormWithoutGroupFragment: EditFormWithoutGroupFragment)
    fun inject(searchActivity: KeywordSearchActivity)

    fun inject(editAdGroupFragment: EditAdGroupActivity)
    fun inject(editAdGroupFragment: EditAdGroupFragment)
}

