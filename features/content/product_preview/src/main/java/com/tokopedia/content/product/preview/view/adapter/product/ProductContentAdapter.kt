package com.tokopedia.content.product.preview.view.adapter.product

import android.view.View
import android.view.ViewGroup
import com.tokopedia.adapterdelegate.BaseDiffUtilAdapter
import com.tokopedia.adapterdelegate.TypedAdapterDelegate
import com.tokopedia.content.product.preview.R
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter.ProductContentModelType
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter.ProductContentModelType.ImageType
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter.ProductContentModelType.Unknown
import com.tokopedia.content.product.preview.view.adapter.product.ProductContentAdapter.ProductContentModelType.VideoType
import com.tokopedia.content.product.preview.view.listener.ProductPreviewListener
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel
import com.tokopedia.content.product.preview.view.uimodel.ContentUiModel.MediaType
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentImageViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentLoadingViewHolder
import com.tokopedia.content.product.preview.view.viewholder.product.ProductContentVideoViewHolder

class ProductContentAdapter(
    listener: ProductPreviewListener
) : BaseDiffUtilAdapter<ProductContentModelType>() {

    init {
        delegatesManager
            .addDelegate(ProductContentDelegate.ProductContentImage(listener))
            .addDelegate(ProductContentDelegate.ProductContentVideo(listener))
            .addDelegate(ProductContentDelegate.ProductContentUnknown())
    }

    fun insertData(data: List<ContentUiModel>) {
        clearAllItems()
        setItems(generateModel(data))
        notifyItemRangeChanged(0, data.size)
    }

    private fun generateModel(data: List<ContentUiModel>): List<ProductContentModelType> {
        return buildList {
            addAll(
                data.map {
                    when (it.type) {
                        MediaType.Video -> VideoType(it)
                        MediaType.Image -> ImageType(it)
                        else -> Unknown
                    }
                }
            )
        }
    }

    override fun areItemsTheSame(
        oldItem: ProductContentModelType,
        newItem: ProductContentModelType
    ): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ProductContentModelType,
        newItem: ProductContentModelType
    ): Boolean {
        return when {
            oldItem as VideoType == newItem as VideoType -> oldItem.video.url == newItem.video.url
            oldItem as ImageType == newItem as ImageType -> oldItem.video.url == newItem.video.url
            else -> oldItem == newItem
        }
    }

    internal class ProductContentDelegate private constructor() {

        internal class ProductContentVideo(private val listener: ProductPreviewListener) :
            TypedAdapterDelegate<
                VideoType,
                ProductContentModelType,
                ProductContentVideoViewHolder>(R.layout.item_product_content_video) {

            override fun onBindViewHolder(
                item: VideoType,
                holder: ProductContentVideoViewHolder
            ) {
                holder.bind(item.video)
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                basicView: View
            ): ProductContentVideoViewHolder {
                return ProductContentVideoViewHolder.create(parent, listener)
            }
        }

        internal class ProductContentImage(private val listener: ProductPreviewListener) :
            TypedAdapterDelegate<
                ImageType,
                ProductContentModelType,
                ProductContentImageViewHolder>(R.layout.item_product_content_image) {

            override fun onBindViewHolder(
                item: ImageType,
                holder: ProductContentImageViewHolder
            ) {
                holder.bind(item.image)
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                basicView: View
            ): ProductContentImageViewHolder {
                return ProductContentImageViewHolder.create(parent, listener)
            }
        }

        internal class ProductContentUnknown :
            TypedAdapterDelegate<
                Unknown,
                ProductContentModelType,
                ProductContentLoadingViewHolder>(R.layout.item_product_content_loading) {

            override fun onBindViewHolder(
                item: Unknown,
                holder: ProductContentLoadingViewHolder
            ) {
                holder.bind()
            }

            override fun onCreateViewHolder(
                parent: ViewGroup,
                basicView: View
            ): ProductContentLoadingViewHolder {
                return ProductContentLoadingViewHolder.create(parent)
            }
        }
    }

    sealed interface ProductContentModelType {
        data class ImageType(val image: ContentUiModel) : ProductContentModelType
        data class VideoType(val video: ContentUiModel) : ProductContentModelType
        object Unknown : ProductContentModelType
    }
}
