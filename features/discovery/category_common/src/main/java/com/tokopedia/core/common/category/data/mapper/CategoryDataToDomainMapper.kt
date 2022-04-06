package com.tokopedia.core.common.category.data.mapper

import com.tokopedia.core.common.category.data.source.db.CategoryDataBase
import com.tokopedia.core.common.category.domain.model.CategoryDomainModel
import com.tokopedia.kotlin.extensions.view.orZero
import rx.functions.Func1


class CategoryDataToDomainMapper: Func1<List<CategoryDataBase>, List<CategoryDomainModel>>{


    override fun call(t: List<CategoryDataBase>): List<CategoryDomainModel> {
        return mapDomainModels(t)
    }

    companion object{
        fun mapDomainModels(categoryDataBases:List<CategoryDataBase>) : List<CategoryDomainModel> =
            categoryDataBases.map { mapItemToEntity(it) }

        fun mapItemToEntity(item:CategoryDataBase): CategoryDomainModel =
            CategoryDomainModel(
                item.id.orZero(),
                item.name,
                item.identifier
            )
    }

}

