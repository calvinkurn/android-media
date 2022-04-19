package com.tokopedia.kol.feature.video.view.adapter

import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.tokopedia.feedcomponent.data.pojo.feed.contentitem.PostTagItem
import com.tokopedia.kol.R
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.unifycomponents.UnifyButton
import kotlinx.android.synthetic.main.item_media_tag.view.*

class MediaTagAdapter(private val tags: MutableList<PostTagItem> = mutableListOf(),
                      private val checkIsMyShop: ((String) -> Boolean),
                      private val onFavoriteProduct: ((Boolean, String, Int) -> Unit)? = null,
                      private val onProductActionClick: ((PostTagItem, Boolean) -> Unit)? = null)
    : RecyclerView.Adapter<MediaTagAdapter.MediaTagViewHolder>() {

    fun updateTags(_tags: List<PostTagItem>){
        tags.clear()
        tags.addAll(_tags)
        notifyDataSetChanged()
    }

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
                picture.loadImageRounded(postTagItem.thumbnail, resources.getDimension(com.tokopedia.unifyprinciples.R.dimen.unify_space_8))
                title.text = postTagItem.text
                original_price.gone()
                price.text = postTagItem.price
                rating.shouldShowWithAction(postTagItem.rating > 0){
                    rating.rating = postTagItem.rating.toFloat()
                }

                val shop = postTagItem.shop.firstOrNull()
                val ctaBtn = postTagItem.buttonCTA.firstOrNull()
                if (shop == null || checkIsMyShop(shop.shopId)) {
                    button_action.text = context.getString(R.string.kol_see_product)
                    favorite.gone()
                    button_action.buttonType = UnifyButton.Type.MAIN
                    button_action.setOnClickListener { onProductActionClick?.invoke(postTagItem, true) }
                } else {
                    favorite.setImageDrawable(ContextCompat.getDrawable(context,
                            if (postTagItem.isWishlisted) com.tokopedia.design.R.drawable.ic_wishlist_checked
                            else com.tokopedia.design.R.drawable.ic_wishlist_unchecked))
                    button_action.buttonType = UnifyButton.Type.TRANSACTION
                    if (ctaBtn == null || ctaBtn.text.isBlank()){
                        button_action.isEnabled = false
                        button_action.text = context.getString(com.tokopedia.feedcomponent.R.string.empty_product)
                    } else {
                        button_action.isEnabled = true
                        button_action.text = context.getString(com.tokopedia.feedcomponent.R.string.string_posttag_buy)
                    }

                    favorite.setOnClickListener {
                        onFavoriteProduct?.invoke(!postTagItem.isWishlisted, postTagItem.id, adapterPosition)
                    }
                    button_action.setOnClickListener {
                        onProductActionClick?.invoke(postTagItem, false)
                    }
                }
            }
        }
    }
}