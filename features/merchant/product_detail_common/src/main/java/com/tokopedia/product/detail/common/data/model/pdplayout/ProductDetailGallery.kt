package com.tokopedia.product.detail.common.data.model.pdplayout

import android.os.Parcelable
import com.tokopedia.image_gallery.ImageGalleryItem
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize

@Parcelize
data class ProductDetailGallery(
    val page: Page,
    val productId: String = "",
    val userId: String = "",
    val defaultItem: Item? = null,
    val items: List<Item>,
    val selectedId: String? = null
) : Parcelable {

    @IgnoredOnParcel
    val combinedItems: List<Item> = run {
        val items = mutableListOf<Item>()
        defaultItem?.let { items.add(it) }
        items.addAll(this.items)
        items
    }

    /**
     * Combine defaultItem and items, then
     * transform them into ImageGalleryItem list -> requirement for unify's ImageGallery
     */
    fun generateImageGalleryItems(isAutoPlay: Boolean = false): ArrayList<ImageGalleryItem> {
        return ArrayList(
            combinedItems.map { item ->
                ImageGalleryItem(
                    drawable = null,
                    url = item.url,
                    thumbnailUrl = item.thumbnailUrl,
                    tagText = item.tag
                ).setAutoPlay(isAutoPlay)
            }
        )
    }

    /**
     * get selected position to show in ImageGallery
     * 1. defaultItem is optional, only 1, and will add on first index
     * 3. selected position determined from Items (using indexOfFirst)
     * 4. if item found, then:
     * 4a. defaultItem available -> index + 1 (ex: VBS)
     * 4b. defaultItem null -> index (ex: PDP)
     * 5. item not found, pass -1
     */
    fun getSelectedPosition(): Int {
        val itemPosition = items.indexOfFirst { it.id == selectedId }
        return (itemPosition + 1).takeIf {
            it > 0 && defaultItem != null
        } ?: itemPosition
    }

    /**
     * defaultItem will ignore attribute id, you can fill it with anything
     * else it is mandatory
     */
    @Parcelize
    data class Item(
        val id: String,
        val url: String,
        val thumbnailUrl: String = url,
        val tag: String?,
        val type: Type
    ) : Parcelable{
        enum class Type (name: String){
            Image("image"), Video("video")
        }
    }

    enum class Page {
        ProductDetail,
        VariantBottomSheet
    }

}
