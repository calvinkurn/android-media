package com.tokopedia.imagepicker.editor.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.tokopedia.imagepicker.R
import com.tokopedia.imagepicker.editor.widget.ItemSelection
import com.tokopedia.imagepicker.videorecorder.utils.hide
import com.tokopedia.imagepicker.videorecorder.utils.show

class EditorItemSelectionAdapter constructor(
    private val items: List<ItemSelection> = mutableListOf(),
    private val listener: EditorItemSelectionListener? = null
) : RecyclerView.Adapter<EditorItemSelectionAdapter.EditorItemSelectionViewHolder>() {

    private var selectedPosition = 0

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditorItemSelectionViewHolder {
        return EditorItemSelectionViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: EditorItemSelectionViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)

        holder.itemView.setOnClickListener {
            onItemSelected(item, position)
        }
    }

    private fun onItemSelected(item: ItemSelection, position: Int) {
        if (position < 0) return

        items[selectedPosition].isSelected = false
        items[position].isSelected = true
        selectedPosition = position

        listener?.onItemSelected(item.itemType)
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

        fun bind(item: ItemSelection?) {
            if (item == null) return
            if (item.placeholderText.isNotEmpty()) txtPlaceholder.show()
            if (item.isSelected) viewSelection.show() else viewSelection.hide()

            txtPlaceholder.text = item.placeholderText.take(6)
            txtItem.text = item.name

            imgItemSelection.loadImage(item.preview)

            if (item.placeholderResId != 0) {
                imgItemPlaceholder.loadImage(item.placeholderResId)
                imgItemPlaceholder.show()
            }
        }

        private fun AppCompatImageView.loadImage(data: Any) {
            val radius = context.resources.getDimensionPixelSize(R.dimen.image_editor_rounded)

            Glide.with(context)
                .load(data)
                .apply(RequestOptions().transform(
                    CenterCrop(),
                    RoundedCorners(radius)
                )).into(this)
        }

        companion object {
            @LayoutRes val LAYOUT = R.layout.view_edit_item_selection

            fun create(viewGroup: ViewGroup): EditorItemSelectionViewHolder {
                val layoutView = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(LAYOUT, viewGroup, false)

                return EditorItemSelectionViewHolder(layoutView)
            }
        }

    }

    interface EditorItemSelectionListener {
        fun onItemSelected(type: Int)
    }

}