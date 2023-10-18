package com.tokopedia.explore.view.adapter

import android.annotation.SuppressLint
import android.text.SpannableString
import android.text.method.LinkMovementMethod
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.tokopedia.abstraction.base.view.adapter.model.EmptyModel
import com.tokopedia.abstraction.base.view.adapter.model.ErrorNetworkModel
import com.tokopedia.abstraction.base.view.adapter.model.LoadingMoreModel
import com.tokopedia.abstraction.base.view.adapter.viewholders.EmptyViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.ErrorNetworkViewHolder
import com.tokopedia.abstraction.base.view.adapter.viewholders.LoadingMoreViewHolder
import com.tokopedia.applink.RouteManager
import com.tokopedia.applink.internal.ApplinkConstInternalContent
import com.tokopedia.explore.R
import com.tokopedia.explore.domain.entity.PostKol
import com.tokopedia.explore.domain.entity.Tracking
import com.tokopedia.explore.view.type.ExploreCardType
import com.tokopedia.explore.view.uimodel.PostKolUiModel
import com.tokopedia.feedcomponent.util.TagConverter
import com.tokopedia.kotlin.extensions.view.addOnImpressionListener
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.inflateLayout
import com.tokopedia.kotlin.extensions.view.loadImage
import com.tokopedia.kotlin.extensions.view.loadImageCircle
import com.tokopedia.kotlin.extensions.view.loadImageDrawable
import com.tokopedia.kotlin.extensions.view.shouldShowWithAction
import com.tokopedia.kotlin.extensions.view.visible
import com.tokopedia.unifyprinciples.Typography
import java.net.URLEncoder
import com.tokopedia.affiliatecommon.R as affiliatecommonR
import com.tokopedia.feedcomponent.R as feedcomponentR
import com.tokopedia.globalerror.R as globalerrorR
import com.tokopedia.unifyprinciples.R as unifyprinciplesR

class HashtagLandingItemAdapter(var listener: OnHashtagPostClick? = null)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val list = mutableListOf<HashtagItemType>()
    private val isLoading: Boolean
        get() {
            val lastIndex = itemCount - 1
            return if (lastIndex > -1){
                list[lastIndex] is Loading
            } else
                false
        }

    private val emptyModel: EmptyModel by lazy {
        EmptyModel().apply {
            iconRes = affiliatecommonR.drawable.ic_af_empty
            contentRes = R.string.af_empty_hashtag_post
        }
    }

    private val errorModel: ErrorNetworkModel by lazy {
        ErrorNetworkModel().apply {
            this.iconDrawableRes = globalerrorR.drawable.unify_globalerrors_connection
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType){
            TYPE_EMPTY -> {
                val v = parent.inflateLayout(EmptyViewHolder.LAYOUT)
                EmptyViewHolder(v)
            }
            TYPE_DATA -> {
                val v = parent.inflateLayout(R.layout.item_explore_hashtag_landing_item)
                HashtagLandingItemViewHolder(v)
            }
            TYPE_ERROR -> {
                val v = parent.inflateLayout(ErrorNetworkViewHolder.LAYOUT)
                ErrorNetworkViewHolder(v)
            }
            else -> {
                val v = parent.inflateLayout(LoadingMoreViewHolder.LAYOUT)
                LoadingMoreViewHolder(v)
            }
        }
    }

    override fun getItemCount(): Int = list.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(val item = list[position]){
            is Loading -> {
                val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                params.isFullSpan = true
                holder.itemView.layoutParams = params
                (holder as LoadingMoreViewHolder).bind(LoadingMoreModel())
            }
            is Empty -> {
                val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                params.isFullSpan = true
                holder.itemView.layoutParams = params
                (holder as EmptyViewHolder).bind(item.empty)
            }
            is Error -> {
                val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                params.isFullSpan = true
                holder.itemView.layoutParams = params
                (holder as ErrorNetworkViewHolder).bind(item.error)
            }
            is Data -> {
                val params = (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams)
                params.isFullSpan = false
                holder.itemView.layoutParams = params
                (holder as HashtagLandingItemViewHolder).bind(item.data, position)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(list[position]){
            is Loading -> TYPE_LOADING
            is Empty -> TYPE_EMPTY
            is Data -> TYPE_DATA
            is Error -> TYPE_ERROR
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(data: List<PostKolUiModel>) {
        val dataNew = data.map { Data(it) }
        list.clear()
        list.addAll(dataNew)
        notifyDataSetChanged()
    }

    fun showLoadMore() {
        list.add(Loading)
        notifyItemInserted(list.size)
    }

    fun hideLoadMore() {
        if (isLoading){
            val lastIndex = itemCount - 1
            list.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    fun addData(data: List<PostKolUiModel>) {
        val startInsert = itemCount
        list.addAll(data.map { Data(it) })
        notifyItemRangeInserted(startInsert, itemCount)
    }

    fun showEmpty() {
        list.add(Empty(emptyModel))
        notifyItemInserted(itemCount)
    }

    fun showError(message: String, onRetry: (()-> Unit)? = null){
        errorModel.errorMessage = message
        errorModel.setOnRetryListener(onRetry)
        list.add(Error(errorModel))
        notifyItemInserted(itemCount)
    }

    fun hideError(){
        val lastIndex = itemCount - 1
        if (lastIndex > -1 && list[lastIndex] is Error){
            list.removeAt(lastIndex)
            notifyItemRemoved(lastIndex)
        }
    }

    companion object {
        private const val TYPE_LOADING = 1
        private const val TYPE_EMPTY = 2
        private const val TYPE_DATA = 3
        private const val TYPE_ERROR = 4
    }

    inner class HashtagLandingItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        private val postThumbnail: ImageView = itemView.findViewById(R.id.post_thumbnail)
        private val badge: ImageView = itemView.findViewById(R.id.badge)
        private val creatorImage: ImageView = itemView.findViewById(R.id.creator_img)
        private val postDescription: Typography = itemView.findViewById(R.id.post_descr)
        private val creatorName: Typography = itemView.findViewById(R.id.creator_name)

        fun bind(uiModel: PostKolUiModel, position: Int){
            val item = uiModel.postKol
            with(itemView){
                if (item.content.isEmpty()){
                    postThumbnail.gone()
                    badge.gone()
                } else {
                    val thumbnail = item.content.first()
                    postThumbnail.loadImage(thumbnail.imageurl)
                    postThumbnail.visible()
                    postThumbnail.addOnImpressionListener(uiModel.impressHolder) {
                        listener?.onImageFirstTimeSeen(item, position)
                        listener?.onAffiliateTrack(uiModel.postKol.tracking, false)
                    }
                    when {
                        item.content.size > 1 -> {
                            badge.loadImageDrawable(feedcomponentR.drawable.ic_affiliate_multi)
                            badge.visible()
                        }
                        thumbnail.type == ExploreCardType.Video.typeString -> {
                            badge.loadImageDrawable(feedcomponentR.drawable.ic_affiliate_video)
                            badge.visible()
                        }
                        else -> badge.gone()
                    }
                    postThumbnail.setOnClickListener {
                        listener?.onImageClick(item, position)
                        listener?.onAffiliateTrack(uiModel.postKol.tracking, true)
                    }
                }

                creatorImage.shouldShowWithAction(!item.userPhoto.isBlank()){
                    creatorImage.loadImageCircle(item.userPhoto)
                    creatorImage.setOnClickListener { listener?.onUserImageClick(item) }
                }
                creatorName.text = item.userName
                creatorName.setOnClickListener { listener?.onUserNameClick(item) }
                val tagHConverter = TagConverter()
                postDescription.text = tagHConverter.convertToLinkifyHashtag(SpannableString(item.description),
                        ContextCompat.getColor(context, unifyprinciplesR.color.Unify_GN500)){
                    val encodeHashtag = URLEncoder.encode(it, "UTF-8")
                    RouteManager.route(context, ApplinkConstInternalContent.HASHTAG_PAGE, encodeHashtag)
                }
                postDescription.movementMethod = LinkMovementMethod.getInstance()
            }
        }
    }

    interface OnHashtagPostClick{
        fun onImageFirstTimeSeen(post: PostKol, position: Int)
        fun onImageClick(post: PostKol, position: Int)
        fun onUserImageClick(post: PostKol)
        fun onUserNameClick(post: PostKol)
        fun onAffiliateTrack(trackingList: List<Tracking>, isClick: Boolean)
    }
}

sealed class HashtagItemType
object Loading: HashtagItemType()
class Empty(val empty: EmptyModel): HashtagItemType()
class Data(val data: PostKolUiModel): HashtagItemType()
class Error(val error: ErrorNetworkModel): HashtagItemType()
