package com.tokopedia.play.broadcaster.ui.viewholder

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.kotlin.extensions.view.hide
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.kotlin.extensions.view.showWithCondition
import com.tokopedia.play.broadcaster.ui.model.beautification.FaceFilterUiModel
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.databinding.ItemBeautificationFilterOptionBinding
import com.tokopedia.play.broadcaster.ui.model.beautification.BeautificationAssetStatus
import com.tokopedia.play.broadcaster.ui.model.beautification.PresetFilterUiModel

/**
 * Created By : Jonathan Darwin on February 28, 2023
 */
class BeautificationFilterOptionViewHolder private constructor() {

    class FaceFilter(
        private val binding: ItemBeautificationFilterOptionBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: FaceFilterUiModel) {
            binding.apply {
                tvBeautificationFilter.text = item.name
                imgBeautificationFilter.setImageUrl(item.iconUrl)
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

        private fun setAssetStatus(assetStatus: BeautificationAssetStatus) {
            when(assetStatus) {
                BeautificationAssetStatus.Downloading -> {
                    binding.icLoader.show()
                    binding.icDownload.hide()
                }
                BeautificationAssetStatus.NotDownloaded -> {
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
            ) = FaceFilter(
                ItemBeautificationFilterOptionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener
            )
        }
    }

    class Preset(
        private val binding: ItemBeautificationFilterOptionBinding,
        private val listener: Listener,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: PresetFilterUiModel) {
            binding.apply {
                tvBeautificationFilter.text = item.name
                imgBeautificationFilter.setImageUrl(item.iconUrl)
                icChecked.hide()

                clIcon.setBackgroundResource(
                    if(item.active) R.drawable.bg_face_filter_option_selected
                    else R.drawable.bg_face_filter_option
                )

                setAssetStatus(item.assetStatus)

                root.setOnClickListener {
                    listener.onClick(item)
                }
            }
        }

        private fun setAssetStatus(assetStatus: BeautificationAssetStatus) {
            when(assetStatus) {
                BeautificationAssetStatus.Downloading -> {
                    binding.icLoader.show()
                    binding.icDownload.hide()
                }
                BeautificationAssetStatus.NotDownloaded -> {
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
            fun onClick(item: PresetFilterUiModel)
        }

        companion object {

            fun create(
                parent: ViewGroup,
                listener: Listener,
            ) = Preset(
                ItemBeautificationFilterOptionBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false,
                ),
                listener
            )
        }
    }
}
