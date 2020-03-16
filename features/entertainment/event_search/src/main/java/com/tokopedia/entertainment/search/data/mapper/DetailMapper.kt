package com.tokopedia.entertainment.search.data.mapper

import com.tokopedia.entertainment.search.adapter.viewholder.CategoryTextViewHolder
import com.tokopedia.entertainment.search.adapter.viewholder.SearchEventGridViewHolder
import com.tokopedia.entertainment.search.data.EventDetailResponse

object DetailMapper {

    fun mapToCategory(category: EventDetailResponse.Data.EventChildCategory.CategoriesItem) : CategoryTextViewHolder.CategoryTextBubble{
        return CategoryTextViewHolder.CategoryTextBubble(id = category.id, category = category.title)
    }

    fun mapToGrid(event : EventDetailResponse.Data.EventSearch.ProductsItem) : SearchEventGridViewHolder.EventGrid{
        return SearchEventGridViewHolder.EventGrid(
                id = event.id,
                image_url = event.imageApp,
                location = event.cityName,
                nama_event = event.title,
                isFavorite = event.isLiked,
                harga_start = "Mulai dari",
                harga_now = event.salesPrice,
                app_url = event.appUrl
        )
    }
}