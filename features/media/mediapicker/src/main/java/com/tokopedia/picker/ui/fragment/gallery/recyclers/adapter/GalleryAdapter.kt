package com.tokopedia.picker.ui.fragment.gallery.recyclers.adapter

import android.annotation.SuppressLint
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.ViewItemGalleryPickerBinding
import com.tokopedia.picker.ui.fragment.OnMediaClickListener
import com.tokopedia.picker.ui.fragment.OnMediaSelectedListener
import com.tokopedia.picker.ui.fragment.gallery.recyclers.utils.MediaDiffUtil
import com.tokopedia.picker.ui.uimodel.MediaUiModel
import com.tokopedia.picker.utils.isVideoFormat
import com.tokopedia.picker.utils.pickerLoadImage
import com.tokopedia.picker.utils.videoDurationFromUri
import com.tokopedia.utils.view.binding.viewBinding

class GalleryAdapter(
    selectedMedias: List<MediaUiModel>,
    private val shouldSelectListener: OnMediaClickListener
) : RecyclerView.Adapter<GalleryAdapter.GalleryPickerViewHolder>() {

    private val selectedMedias: MutableList<MediaUiModel> = mutableListOf()

    private val listDiffer by lazy {
        AsyncListDiffer<MediaUiModel>(this, MediaDiffUtil())
    }

    private var itemSelectedListener: OnMediaSelectedListener? = null

    init {
        if (selectedMedias.isNotEmpty()) {
            this.selectedMedias.addAll(selectedMedias)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryPickerViewHolder {
        return GalleryPickerViewHolder.create(parent)
    }

    override fun onBindViewHolder(holder: GalleryPickerViewHolder, position: Int) {
        val media = getItem(position)?: return
        val isSelected = isSelected(media)

        holder.bind(media, isSelected) {
            val shouldSelect = shouldSelectListener(media, isSelected)

            if (isSelected) {
                removeSelected(media, position)
            } else if (shouldSelect) {
                addSelected(media, position)
            }
        }
    }

    override fun getItemCount() = listDiffer.currentList.size

    fun setData(medias: List<MediaUiModel>) {
        listDiffer.submitList(null)
        listDiffer.submitList(medias)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeAllSelectedSingleClick() {
        mutate {
            selectedMedias.clear()
            notifyDataSetChanged()
        }
    }

    fun removeSelected(media: MediaUiModel) {
        mutate {
            val elementIndex = listDiffer.currentList.indexOf(media)
            val selectedIndex = selectedMedias.indexOf(media)

            if (selectedIndex != -1 && elementIndex != -1) {
                selectedMedias.removeAt(selectedIndex)
                notifyItemChanged(elementIndex)
            }
        }
    }

    fun setListener(itemSelectedListener: OnMediaSelectedListener?) {
        this.itemSelectedListener = itemSelectedListener
    }

    private fun getItem(position: Int) = listDiffer.currentList.getOrNull(position)

    private fun isSelected(media: MediaUiModel): Boolean {
        return selectedMedias.any { it.path == media.path }
    }

    private fun addSelected(media: MediaUiModel, position: Int) {
        mutate {
            selectedMedias.add(media)
            notifyItemChanged(position)
        }
    }

    private fun removeSelected(media: MediaUiModel, position: Int) {
        mutate {
            selectedMedias.remove(media)
            notifyItemChanged(position)
        }
    }

    private fun mutate(runnable: Runnable) {
        runnable.run()
        itemSelectedListener?.invoke(selectedMedias)
    }

    class GalleryPickerViewHolder(
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemGalleryPickerBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(element: MediaUiModel, isSelected: Boolean, click: () -> Unit) {
            videoDurationLabel(element)

            binding?.viewAlpha?.alpha = if (isSelected) 0.5f else 0f
            binding?.imgPreview?.pickerLoadImage(element.uri.toString())

            itemView.setOnClickListener {
                click()
            }
        }

        private fun videoDurationLabel(element: MediaUiModel) {
            if (isVideoFormat(element.path)) {
                val uri = Uri.withAppendedPath(
                    MediaStore.Files.getContentUri("external"), "" + element.id
                )

                binding?.viewLabel?.text = videoDurationFromUri(context, uri)
                binding?.viewLabel?.show()
            } else {
                binding?.viewLabel?.hide()
            }
        }

        companion object {
            @LayoutRes val LAYOUT = R.layout.view_item_gallery_picker

            fun create(parent: ViewGroup): GalleryPickerViewHolder {
                val view = LayoutInflater
                    .from(parent.context)
                    .inflate(LAYOUT, parent, false)

                return GalleryPickerViewHolder(view)
            }
        }

    }

}