package com.tokopedia.media.picker.ui.widget.drawerselector.adapter

import android.annotation.SuppressLint
import android.content.ClipData
import android.content.ClipDescription
import android.graphics.Rect
import android.os.Build
import android.view.DragEvent
import android.view.View
import android.view.ViewGroup
import androidx.annotation.DrawableRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.picker.ui.uimodel.MediaUiModel
import com.tokopedia.media.picker.ui.widget.drawerselector.DrawerSelectionWidget
import com.tokopedia.media.picker.ui.widget.drawerselector.viewholder.PlaceholderViewHolder
import com.tokopedia.media.picker.ui.widget.drawerselector.viewholder.ThumbnailViewHolder
import com.tokopedia.media.picker.utils.ActionType

class DrawerSelectionAdapter(
    mediaPathList: List<MediaUiModel>,
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val medias: MutableList<MediaUiModel> = mediaPathList.toMutableList()
    private val listRect: MutableMap<Int,Rect> = mutableMapOf()

    private var listener: DrawerSelectionWidget.Listener? = null
    private var totalVideo = 0
    private var maxSize = 10

    var backgroundColorPlaceHolder: Int = 0
    var placeholderPreview: Int = 0
    var canReorder = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            PLACEHOLDER_TYPE -> PlaceholderViewHolder.create(parent)
            ITEM_TYPE -> ThumbnailViewHolder.create(parent)
            else -> error("invalid view holder type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (getItemViewType(position)) {
            ITEM_TYPE -> {
                val media = medias[position]

                if (canReorder) {
                    setupAreaRect(holder)
                    setupLongClickListener(holder)
                    setupDragListener(holder, position)
                }

                if (holder is ThumbnailViewHolder) {
                    holder.bind(media) {
                        removeData(position)
                    }
                }
            }
            PLACEHOLDER_TYPE -> {
                if (holder is PlaceholderViewHolder) {
                    holder.bind(backgroundColorPlaceHolder, placeholderPreview)
                }
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (position < medias.size) ITEM_TYPE else PLACEHOLDER_TYPE
    }

    override fun getItemCount(): Int {
        return if (maxSize > 0) maxSize else medias.size
    }

    fun setOnDataChangedListener(listener: DrawerSelectionWidget.Listener) {
        this.listener = listener
    }

    fun removeListener() {
        listener = null
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setPlaceholder(@DrawableRes drawable: Int) {
        placeholderPreview = drawable
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setMaxData(size: Int) {
        maxSize = size
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeData(media: MediaUiModel) {
        this.medias.remove(media)
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(media: MediaUiModel) {
        if (medias.contains(media)) return
        this.medias.add(media)

        listener?.onDataSetChanged(
            ActionType.Add(medias, media, null)
        )
        notifyDataSetChanged()
    }

    @SuppressLint("NotifyDataSetChanged")
    fun setData(medias: MutableList<MediaUiModel>) {
        this.medias.clear()
        this.medias.addAll(medias)
        notifyDataSetChanged()
    }

    fun containsVideoMaxOf(count: Int): Boolean {
        return this.medias.filter { it.isVideo() }.size >= count
    }

    fun getData(): List<MediaUiModel> {
        return medias
    }

    private fun isItemType(position: Int): Boolean {
        return getItemViewType(position) == ITEM_TYPE
    }

    private fun setupAreaRect(holder: RecyclerView.ViewHolder) {
        holder.itemView.viewTreeObserver.addOnGlobalLayoutListener {
            if (isItemType(holder.adapterPosition)) {
                listRect[holder.adapterPosition] = Rect(
                    holder.itemView.x.toInt(), holder.itemView.y.toInt(),
                    (holder.itemView.x + holder.itemView.width).toInt(),
                    (holder.itemView.y + holder.itemView.height).toInt()
                )
            }
        }
    }

    private fun setupLongClickListener(holder: RecyclerView.ViewHolder) {
        holder.itemView.setOnLongClickListener {
            val item = ClipData.Item(holder.adapterPosition.toString())

            val dragData = ClipData(
                holder.adapterPosition.toString(),
                arrayOf(ClipDescription.MIMETYPE_TEXT_PLAIN),
                item
            )

            val dragShadow = View.DragShadowBuilder(holder.itemView)

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                holder.itemView.startDragAndDrop(dragData, dragShadow, null, 0)
            }
            else {
                holder.itemView.startDrag(dragData, dragShadow, null, 0)
            }

            true
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setupDragListener(holder: RecyclerView.ViewHolder, position: Int) {
        holder.itemView.setOnDragListener { _, dragEvent ->
            when(dragEvent.action) {
                DragEvent.ACTION_DRAG_STARTED,
                DragEvent.ACTION_DRAG_ENTERED,
                DragEvent.ACTION_DRAG_LOCATION,
                DragEvent.ACTION_DRAG_ENDED,
                DragEvent.ACTION_DRAG_EXITED -> true
                DragEvent.ACTION_DROP -> {
                    val item: ClipData.Item = dragEvent.clipData.getItemAt(0)
                    val dragPosition = Integer.parseInt(item.text.toString())
                    val draggedImagePath = medias[dragPosition]
                    val targetImagePath = medias[position]

                    medias[dragPosition] = targetImagePath
                    medias[position] = draggedImagePath

                    listener?.onDataSetChanged(
                        ActionType.Reorder(medias, null)
                    )

                    notifyDataSetChanged()
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

    private fun removeData(index: Int) {
        if (index > -1) {
            val mediaToRemove = medias[index]
            if (medias[index].isVideo()) totalVideo -= 1

            medias.removeAt(index)

            listener?.onDataSetChanged(
                ActionType.Remove(medias, mediaToRemove, null)
            )

            notifyItemChanged(index)
            listRect.remove(index)
        }
    }

    companion object {
        const val PLACEHOLDER_TYPE = 0
        const val ITEM_TYPE = 1
    }
}