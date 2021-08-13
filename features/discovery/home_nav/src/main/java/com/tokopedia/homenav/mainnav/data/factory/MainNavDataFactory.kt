package com.tokopedia.homenav.mainnav.data.factory

import com.tokopedia.abstraction.base.view.adapter.Visitable
import com.tokopedia.homenav.mainnav.domain.model.DynamicHomeIconEntity

interface MainNavDataFactory {

    fun buildVisitableList(): MainNavDataFactory

    fun addSeparatorSection(): MainNavDataFactory
    fun addBUListSection(categoryData: List<DynamicHomeIconEntity.Category>?): MainNavDataFactory

    fun build(): List<Visitable<*>>
}