package com.tokopedia.kol.feature.video.view.adapter

import android.support.v4.content.ContextCompat
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.kol.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import kotlinx.android.synthetic.main.item_media_tag.view.*

class MediaTagAdapter(private val tags: List<PostTagItem> = listOf(),
                      private val onFavoriteProduct: ((String) -> Unit)? = null,
                      private val onProductActionClick: ((String) -> Unit)? = null)
    : RecyclerView.Adapter<MediaTagAdapter.MediaTagViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MediaTagViewHolder {
        return MediaTagViewHolder(parent.inflateLayout(R.layout.item_media_tag))
    }

    override fun getItemCount(): Int = tags.size

    override fun onBindViewHolder(holder: MediaTagViewHolder, position: Int) {
        holder.bind(tags[position])
    }

    inner class MediaTagViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        fun bind(postTagItem: PostTagItem) {
            with(itemView){
                picture.loadImageRounded(postTagItem.thumbnail, resources.getDimension(R.dimen.dp_8))
                title.text = postTagItem.text
                original_price.gone()
                price.text = postTagItem.price
                rating.shouldShowWithAction(postTagItem.rating > 0){
                    rating.rating = postTagItem.rating.toFloat()
                }
                favorite.setImageDrawable(ContextCompat.getDrawable(context,
                        if (postTagItem.isWishlisted) R.drawable.ic_wishlist_checked
                        else R.drawable.ic_wishlist_unchecked))
                button_action.text = context.getString(R.string.string_posttag_buy)

                favorite.setOnClickListener {
                    onFavoriteProduct?.invoke(postTagItem.id)
                }
                button_action.setOnClickListener {
                    onProductActionClick?.invoke(postTagItem.id)
                }
            }
        }
    }
}