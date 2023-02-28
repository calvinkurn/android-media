package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.databinding.ItemFaceFilterOptionBinding
import com.tokopedia.play.broadcaster.ui.model.FaceFilterUiModel
import com.tokopedia.play.broadcaster.R

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class FaceFilterOptionViewHolder(
    private val binding: ItemFaceFilterOptionBinding,
    private val listener: Listener,
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(item: FaceFilterUiModel) {
        binding.apply {
            tvFaceFilter.text = item.name
            imgFaceFilter.setImageUrl(item.iconUrl)
            icChecked.showWithCondition(item.isChecked)

            clIcon.setBackgroundResource(
                if(item.isSelected) R.drawable.bg_face_filter_option_selected
                else R.drawable.bg_face_filter_option
            )

            setAssetStatus(item.assetStatus)

            root.setOnClickListener {
                listener.onClick(item)
            }
        }
    }

    private fun setAssetStatus(assetStatus: FaceFilterUiModel.AssetStatus) {
        when(assetStatus) {
            FaceFilterUiModel.AssetStatus.Downloading -> {
                binding.icLoader.show()
                binding.icDownload.hide()
            }
            FaceFilterUiModel.AssetStatus.NotDownloaded -> {
                binding.icLoader.hide()
                binding.icDownload.show()
            }
            else -> {
                binding.icLoader.hide()
                binding.icDownload.hide()
            }
        }
    }

    interface Listener {
        fun onClick(item: FaceFilterUiModel)
    }

    companion object {

        fun create(
            parent: ViewGroup,
            listener: Listener,
        ) = FaceFilterOptionViewHolder(
            ItemFaceFilterOptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false,
            ),
            listener
        )
    }
}
