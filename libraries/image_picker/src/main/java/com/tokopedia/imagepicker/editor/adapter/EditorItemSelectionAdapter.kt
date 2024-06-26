package com.tokopedia.imagepicker.editor.adapter

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.editor.data.entity.ItemSelection
import com.tokopedia.imagepicker.videorecorder.utils.hide
import com.tokopedia.imagepicker.videorecorder.utils.show
import com.tokopedia.imagepicker.videorecorder.utils.visible
import com.tokopedia.media.loader.loadImage
import com.tokopedia.media.loader.loadImageRounded

@SuppressLint("NotifyDataSetChanged")
class EditorItemSelectionAdapter constructor(
    val items: MutableList<ItemSelection> = mutableListOf(),
    private var listener: EditorItemSelectionListener? = null
) : RecyclerView.Adapter<EditorItemSelectionAdapter.EditorItemSelectionViewHolder>() {

    private var selectedPosition = 0
    private val listViewHolder = mutableListOf<EditorItemSelectionViewHolder>()

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditorItemSelectionViewHolder {
        return EditorItemSelectionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EditorItemSelectionViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
        listViewHolder.add(holder)

        holder.itemView.setOnClickListener {
            listener?.onItemSelected(position, item.placeholderBitmap, item.itemType)
            onItemSelected(position)
        }

    }

    fun setListener(listenerItem: EditorItemSelectionListener?) {
        listener =  listenerItem
    }

    fun clear() {
        items.forEach {
            it.placeholderBitmap?.recycle()
            it.placeholderBitmap = null
        }
        listViewHolder.forEach {
            it.clearImage()
        }
        listViewHolder.clear()
        items.clear()
        selectedPosition = 0
        listener = null
        notifyDataSetChanged()
    }

    fun resetPosition() {
        selectedPosition = 0
        notifyDataSetChanged()
    }

    fun updateAll(list: List<ItemSelection>) {
        listViewHolder.clear()
        items.clear()
        items.addAll(list)
        notifyDataSetChanged()
    }

    fun onItemSelected(position: Int) {
        if (position < 0) return

        items[selectedPosition].isSelected = false
        items[position].isSelected = true
        selectedPosition = position

        notifyDataSetChanged()
    }

    override fun getItemCount() = items.size

    class EditorItemSelectionViewHolder(
        itemView: View
    ) : RecyclerView.ViewHolder(itemView) {

        private val context by lazy { itemView.context }

        private val imgItemSelection = itemView.findViewById<AppCompatImageView>(R.id.img_item_selection)
        private val imgItemPlaceholder = itemView.findViewById<AppCompatImageView>(R.id.img_item_placeholder)
        private val txtPlaceholder = itemView.findViewById<TextView>(R.id.txt_placeholder)
        private val viewSelection = itemView.findViewById<View>(R.id.view_selection)
        private val txtItem = itemView.findViewById<TextView>(R.id.txt_item)

        //clear image with setbackground to null to clear memory
        fun clearImage() {
            imgItemPlaceholder.loadImageRounded(null, 0f) {
            }
        }

        fun bind(item: ItemSelection?) {
            val radius = context.resources.getDimensionPixelSize(R.dimen.image_editor_rounded)

            if (item == null) return
            if (item.isSelected) viewSelection.show() else viewSelection.hide()

            txtItem.text = item.name

            if (item.name.isNotEmpty() && !item.isSingleLabel) {
                txtItem.visibility = View.VISIBLE
            } else {
                txtItem.visibility = View.GONE
            }

            if (item.preview.isNotEmpty()) {
                imgItemSelection.loadImage(item.preview) {
                    setRoundedRadius(radius.toFloat())
                    centerCrop()
                }
            } else {
                imgItemSelection.loadImage(item.previewWithResId) {
                    setRoundedRadius(radius.toFloat())
                    centerCrop()
                }
            }

            // visible the placeholder bitmap by bitmap or resourceId
            imgItemPlaceholder.visible(
                item.placeholderBitmap != null ||
                        item.placeholderResId != 0
            )

            // handling the placeholder of item
            when {
                item.placeholderBitmap != null -> {
                    imgItemPlaceholder.scaleType = ImageView.ScaleType.CENTER_CROP
                    imgItemPlaceholder.setImageBitmap(item.placeholderBitmap)
                }
                item.placeholderResId != 0 -> {
                    imgItemPlaceholder.loadImage(item.placeholderResId)
                }
                item.placeholderText.isNotEmpty() -> {
                    txtPlaceholder.show()
                    txtPlaceholder.text = item.placeholderText.take(6)
                }
            }
        }

        companion object {
            @LayoutRes
            val LAYOUT = R.layout.view_edit_item_selection

            fun create(viewGroup: ViewGroup): EditorItemSelectionViewHolder {
                val layoutView = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(LAYOUT, viewGroup, false)

                return EditorItemSelectionViewHolder(layoutView)
            }
        }

    }


    interface EditorItemSelectionListener {
        fun onItemSelected(position: Int, bitmap: Bitmap?, type: Int)
    }

}