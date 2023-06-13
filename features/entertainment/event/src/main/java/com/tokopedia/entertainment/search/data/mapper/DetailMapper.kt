package com.tokopedia.entertainment.search.data.mapper

import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextBubbleAdapter
import com.tokopedia.entertainment.search.adapter.viewholder.EventGridAdapter
import com.tokopedia.entertainment.search.data.EventDetailResponse
import com.tokopedia.kotlin.extensions.view.isZero
import com.tokopedia.kotlin.extensions.view.toIntSafely

object DetailMapper {

    fun mapToCategory(category: EventDetailResponse.Data.EventChildCategory.CategoriesItem) : CategoryTextBubbleAdapter.CategoryTextBubble {
        return CategoryTextBubbleAdapter.CategoryTextBubble(id = category.id, category = category.title)
    }

    fun mapToGrid(event : EventDetailResponse.Data.EventSearch.ProductsItem) : EventGridAdapter.EventGrid {
        return EventGridAdapter.EventGrid(
                id = event.id,
                image_url = event.imageApp,
                location = event.cityName,
                nama_event = event.title,
                harga_start = "Mulai dari",
                harga_now = event.price,
                app_url = event.appUrl,
                category_id = event.childCategoryIds,
                isFree = (event.isFree == 1 && event.salesPrice.toIntSafely().isZero())
        )
    }
}