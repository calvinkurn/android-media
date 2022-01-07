package com.tokopedia.imagepicker.media_picker.adapter

import android.content.ClipData
import android.content.ClipDescription
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import android.os.Build
import android.view.DragEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.utils.image.ImageProcessingUtil.shouldLoadFitCenter
import java.io.File
import java.util.ArrayList

import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.media_picker.utils.MediaUtils
import com.tokopedia.imagepicker.media_picker.utils.MediaUtils.convertMsToTimeFormat
import com.tokopedia.imagepicker.media_picker.widget.MediaPickerPreviewWidget
import com.tokopedia.unifyprinciples.Typography
import kotlin.time.ExperimentalTime


class MediaPickerThumbnailAdapter(
    private val context: Context,
    private var mediaPathList: MutableList<String>,
    private var placeholderDrawableResList: List<Int>?
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    var backgroundColorPlaceHolder: Int = ContextCompat.getColor(context, com.tokopedia.unifyprinciples.R.color.Unify_N0)
    var canReorder = false
    var maxVideo = 1

    private var maxSize = 10
    private var usePrimaryString = false
    private var limitListener: MediaPickerPreviewWidget.MediaPickerPreviewWidgetListener? = null
    private var totalVideo = 0

    private val thumbnailSize: Int = context.resources.getDimensionPixelOffset(com.tokopedia.abstraction.R.dimen.dp_72)
    private val roundedSize: Float = context.resources.getDimension(com.tokopedia.abstraction.R.dimen.dp_6)
    private val listRect: MutableMap<Int,Rect> = mutableMapOf()

    val getMediaPathList: List<String>
        get() = mediaPathList

    inner class MediaPickerThumbnailViewHolder(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.image_view)
        val ivDelete: ImageView = itemView.findViewById(R.id.iv_delete)
        val tvDuration: Typography = itemView.findViewById(R.id.tv_duration)

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
            requestBuilder.into(object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable =
                        RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                    circularBitmapDrawable.cornerRadius = roundedSize
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            })
            val isImage = MediaUtils.isImage(mediaPath)
            if (!isImage) {
                val durationVideo = MediaUtils.getDurationVideoInMillis(mediaPathList[position])
                tvDuration.visibility = View.VISIBLE
                tvDuration.text = durationVideo.convertMsToTimeFormat()
            } else {
                tvDuration.visibility = View.GONE
            }
        }
    }

    class PlaceholderThumbnailViewHolderKt(itemView: View) :
        RecyclerView.ViewHolder(itemView) {
        var ivPlaceholder: ImageView = itemView.findViewById(R.id.image_view_placeholder)
        var vFrameImage: ImageView = itemView.findViewById(R.id.image_view)
        fun bind(@DrawableRes drawableRes: Int, backgroundColor: Int) {
            vFrameImage.setBackgroundColor(backgroundColor)
            ivPlaceholder.setImageDrawable(
                MethodChecker.getDrawable(
                    ivPlaceholder.context,
                    drawableRes
                )
            )
        }
    }

    fun setData(
        imagePathList: MutableList<String>, usePrimaryString: Boolean,
        placeholderDrawableList: List<Int>
    ) {
        this.mediaPathList = imagePathList
        this.usePrimaryString = usePrimaryString
        placeholderDrawableResList = placeholderDrawableList
        notifyDataSetChanged()
    }

    fun addData(imagePath: String) {
        val isImage = MediaUtils.isImage(imagePath)
        if (!isImage && totalVideo >= maxVideo) {
            limitListener?.onLimitVideoListener()
            return
        } else {
            totalVideo+=1
        }
        if (!mediaPathList.contains(imagePath)) {
            mediaPathList.add(imagePath)
            notifyItemChanged(mediaPathList.size - 1)
        }
    }

    fun removeData(imagePath: String): Int {
        val index = mediaPathList.indexOf(imagePath)
        removeData(index)
        return index
    }

    fun removeData(index: Int) {
        if (index > -1) {
            if (!MediaUtils.isImage(mediaPathList[index])) {
                totalVideo-=1
            }
            mediaPathList.removeAt(index)
            notifyDataSetChanged()
            listRect.remove(index)
        }
    }

    fun getImagePathList(): MutableList<String> {
        return mediaPathList
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
                    R.layout.image_picker_placeholder_thumbnail_item,
                    parent,
                    false
                )
                PlaceholderThumbnailViewHolderKt(view)
            }
            else -> {
                view = inflater.inflate(
                    R.layout.image_picker_placeholder_thumbnail_item,
                    parent,
                    false
                )
                PlaceholderThumbnailViewHolderKt(view)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isItemType(position)) {
            if (canReorder) {
                setupAreaRect(holder)
                setupLongClickListener(holder)
                setupDragListener(holder, position)
            }
            val imagePath = mediaPathList[position]
            (holder as MediaPickerThumbnailViewHolder).ivDelete.setOnClickListener {
                removeData(position)
            }
            holder.bind(imagePath, position)
        } else {
            // else draw the empty preview
            if (placeholderDrawableResList != null && placeholderDrawableResList!!.size > position) {
                val drawableRes = placeholderDrawableResList!![position]
                (holder as PlaceholderThumbnailViewHolderKt).bind(drawableRes, backgroundColorPlaceHolder)
            } else {
                (holder as PlaceholderThumbnailViewHolderKt).bind(R.drawable.ic_plus, backgroundColorPlaceHolder)
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < mediaPathList.size) {
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
                    val draggedImagePath = mediaPathList!![dragPosition]
                    val targetImagePath = mediaPathList!![position]
                    mediaPathList[dragPosition] = targetImagePath
                    mediaPathList[position] = draggedImagePath
                    notifyDataSetChanged()
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

    fun setOnLimitListener(listener: MediaPickerPreviewWidget.MediaPickerPreviewWidgetListener) {
        limitListener = listener
    }

    fun removeListener() {
        limitListener = null
    }

    override fun getItemCount(): Int {
        return if (maxSize > 0) {
            maxSize
        } else {
            mediaPathList.size
        }
    }

    fun setMaxData(size: Int) {
        maxSize = size
    }

    companion object {
        const val PLACEHOLDER_TYPE = 0
        const val ITEM_TYPE = 1
    }
}