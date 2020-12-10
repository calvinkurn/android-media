package com.tokopedia.homenav.mainnav.data.factory

import android.content.Context
import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.data.mapper.toVisitable
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity
import com.tokopedia.user.session.UserSessionInterface

class MainNavDataFactoryImpl(
        private val context: Context,
        private val userSession: UserSessionInterface
): MainNavDataFactory {

    private var visitableList: MutableList<Visitable<*>> = mutableListOf()

    override fun buildVisitableList(): MainNavDataFactory {
        visitableList.clear()
        return this
    }

    override fun addSeparatorSection(): MainNavDataFactory {

        return this
    }

    override fun addBUListSection(categoryData: List<DynamicHomeIconEntity.Category>?): MainNavDataFactory {
        categoryData?.toVisitable()?.let { visitableList.addAll(it) }
        return this
    }

    override fun build(): List<Visitable<*>> {
        return visitableList
    }

}