package com.tokopedia.imagepicker.media_picker.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.net.Uri
import android.os.Build
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.picker.R
import com.tokopedia.picker.data.entity.Media
import com.tokopedia.picker.databinding.MediaPickerPlaceholderThumbnailItemBinding
import com.tokopedia.picker.databinding.MediaPickerThumbnailItemBinding
import com.tokopedia.utils.image.ImageProcessingUtil.shouldLoadFitCenter
import java.io.File

import com.tokopedia.picker.ui.widget.bottomsheet.MediaPickerPreviewWidget
import com.tokopedia.picker.utils.ActionType
import com.tokopedia.picker.utils.getVideoDurationLabel
import com.tokopedia.picker.utils.isVideoFormat
import com.tokopedia.utils.view.binding.viewBinding


class MediaPickerThumbnailAdapter(
    private val context: Context,
    private val mediaPathList: List<Media>,
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val _mediaPathList: MutableList<Media> = mediaPathList.toMutableList()
    var backgroundColorPlaceHolder: Int = 0
    var canReorder = false
    var maxVideo = 1
    var placeholderPreview: Int = 0

    private var maxSize = 10
    private var listener: MediaPickerPreviewWidget.MediaPickerPreviewWidgetListener? = null
    private var totalVideo = 0

    private val thumbnailSize: Int = context.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_72)
    private val roundedSize: Float = context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_6)
    private val listRect: MutableMap<Int,Rect> = mutableMapOf()

    val getMediaPathList: List<Media>
        get() = _mediaPathList

    inner class MediaPickerThumbnailViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding: MediaPickerThumbnailItemBinding? by viewBinding()

        fun bind(mediaPath: String, position: Int) {
            val file = File(mediaPath)
            var loadFitCenter = false
            if (file.exists()) {
                loadFitCenter = shouldLoadFitCenter(file)
            }
            var requestBuilder = Glide.with(context)
                .asBitmap()
                .load(mediaPath)
                .override(thumbnailSize, thumbnailSize)
            requestBuilder = if (loadFitCenter) {
                requestBuilder.fitCenter()
            } else {
                requestBuilder.centerCrop()
            }
            requestBuilder.into(object : BitmapImageViewTarget(binding?.imageView) {
                override fun setResource(resource: Bitmap?) {
                    binding?.let {
                        val circularBitmapDrawable =
                            RoundedBitmapDrawableFactory.create(it.imageView.context.resources, resource)
                        circularBitmapDrawable.cornerRadius = roundedSize
                        it.imageView.setImageDrawable(circularBitmapDrawable)
                    }

                }
            })
            val isVideo = isVideoFormat(mediaPath)
            if (isVideo) {
                val durationVideo = getVideoDurationLabel(context, Uri.parse(_mediaPathList[position].path))
                binding?.tvDuration?.visibility = View.VISIBLE
                binding?.tvDuration?.text = durationVideo
            } else {
                binding?.tvDuration?.visibility = View.GONE
            }
        }
    }

    class PlaceholderThumbnailViewHolderKt(itemView: View) :
        RecyclerView.ViewHolder(itemView) {

        val binding: MediaPickerPlaceholderThumbnailItemBinding? by viewBinding()

        fun bind(@DrawableRes drawableRes: Int, backgroundColor: Int) {
            binding?.imageView?.setBackgroundColor(backgroundColor)
            binding?.imageViewPlaceholder?.setImageDrawable(
                MethodChecker.getDrawable(
                    binding?.imageViewPlaceholder?.context,
                    drawableRes
                )
            )
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(
        medias: MutableList<Media>,
    ) {
        _mediaPathList.clear()
        _mediaPathList.addAll(medias)
        notifyDataSetChanged()
    }

    fun addData(media: Media) {
        val isVideo = isVideoFormat(media.path)
        if (isVideo && totalVideo >= maxVideo) {
            listener?.onDataSetChanged(ActionType.Add(_mediaPathList, context.getString(R.string.error_reached_limit)))
            return
        } else if(isVideo && totalVideo < maxVideo) {
            totalVideo+=1
        }
        if (!_mediaPathList.contains(media)) {
            _mediaPathList.add(media)
            notifyItemChanged(_mediaPathList.size - 1)
            listener?.onDataSetChanged(ActionType.Add(_mediaPathList, null))
        }
    }

    fun removeData(media: Media): Int {
        val index = _mediaPathList.indexOf(media)
        removeData(index)
        return index
    }

    fun removeData(index: Int) {
        if (index > -1) {
            if (isVideoFormat(_mediaPathList[index].path)) {
                totalVideo-=1
            }
            _mediaPathList.removeAt(index)
            listener?.onDataSetChanged(ActionType.Remove(_mediaPathList, null))
            notifyItemChanged(index)
            listRect.remove(index)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(context)
        val view: View
        return when (viewType) {
            ITEM_TYPE -> {
                view = inflater.inflate(R.layout.media_picker_thumbnail_item, parent, false)
                MediaPickerThumbnailViewHolder(view)
            }
            PLACEHOLDER_TYPE -> {
                view = inflater.inflate(
                    R.layout.media_picker_placeholder_thumbnail_item,
                    parent,
                    false
                )
                PlaceholderThumbnailViewHolderKt(view)
            }
            else -> {
                view = inflater.inflate(
                    R.layout.media_picker_placeholder_thumbnail_item,
                    parent,
                    false
                )
                PlaceholderThumbnailViewHolderKt(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE -> {
                if (canReorder) {
                    setupAreaRect(holder)
                    setupLongClickListener(holder)
                    setupDragListener(holder, position)
                }
                val media = _mediaPathList[position].path
                (holder as MediaPickerThumbnailViewHolder).binding?.ivDelete?.setOnClickListener {
                    removeData(position)
                }
                holder.bind(media, position)
            }
            else -> {
                (holder as PlaceholderThumbnailViewHolderKt).bind(placeholderPreview, backgroundColorPlaceHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < _mediaPathList.size) {
            ITEM_TYPE
        } else PLACEHOLDER_TYPE
    }

    private fun isItemType(position: Int): Boolean {
        return getItemViewType(position) == ITEM_TYPE
    }

    private fun setupAreaRect(holder: RecyclerView.ViewHolder) {
        holder.itemView.viewTreeObserver.addOnGlobalLayoutListener {
            if (isItemType(holder.position)) {
                listRect[holder.position] =
                    Rect(holder.itemView.x.toInt(), holder.itemView.y.toInt(),
                        (holder.itemView.x + holder.itemView.width).toInt(),
                        (holder.itemView.y + holder.itemView.height).toInt(),)
            }
        }
    }

    private fun setupLongClickListener(holder: RecyclerView.ViewHolder) {
        holder.itemView.setOnLongClickListener {
            val item = ClipData.Item(holder.position.toString())
            val dragData = ClipData(
                holder.position.toString(),
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.itemView.startDragAndDrop(dragData, View.DragShadowBuilder(holder.itemView), null, 0)
            }
            else {
                holder.itemView.startDrag(dragData, View.DragShadowBuilder(holder.itemView), null, 0)
            }
            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupDragListener(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnDragListener { _, dragEvent ->
            when(dragEvent.action) {

                DragEvent.ACTION_DRAG_STARTED -> {
                    true
                }
                DragEvent.ACTION_DRAG_ENTERED -> {
                    true
                }
                DragEvent.ACTION_DRAG_LOCATION -> {
                    true
                }
                DragEvent.ACTION_DRAG_EXITED -> {
                    true
                }
                DragEvent.ACTION_DROP -> {
                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    val dragPosition = Integer.parseInt(item.text.toString())
                    val draggedImagePath = _mediaPathList[dragPosition]
                    val targetImagePath = _mediaPathList[position]
                    _mediaPathList[dragPosition] = targetImagePath
                    _mediaPathList[position] = draggedImagePath
                    notifyDataSetChanged()
                    listener?.onDataSetChanged(ActionType.Reorder(_mediaPathList, null))
                    true
                }
                DragEvent.ACTION_DRAG_ENDED -> {
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    fun setOnDataChangedListener(listener: MediaPickerPreviewWidget.MediaPickerPreviewWidgetListener) {
        this.listener = listener
    }

    fun removeListener() {
        listener = null
    }

    fun setPlaceholder(@DrawableRes drawable: Int) {
        placeholderPreview = drawable
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return if (maxSize > 0) {
            maxSize
        } else {
            _mediaPathList.size
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMaxData(size: Int) {
        maxSize = size
        notifyDataSetChanged()
    }

    companion object {
        const val PLACEHOLDER_TYPE = 0
        const val ITEM_TYPE = 1
    }
}