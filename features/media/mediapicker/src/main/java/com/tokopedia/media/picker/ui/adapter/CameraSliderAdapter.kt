package com.tokopedia.media.picker.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.media.R
import com.tokopedia.media.databinding.ViewItemCameraModeSliderBinding
import com.tokopedia.media.picker.ui.fragment.camera.CameraSelectorUiModel
import com.tokopedia.unifyprinciples.Typography
import com.tokopedia.utils.view.binding.viewBinding

class CameraSliderAdapter(
    private val elements: MutableList<CameraSelectorUiModel>,
    private val listener: Listener
) : RecyclerView.Adapter<CameraSliderAdapter.CameraSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraSliderViewHolder {
        return CameraSliderViewHolder.create(listener, parent)
    }

    override fun onBindViewHolder(holder: CameraSliderViewHolder, position: Int) {
        holder.bind(elements[position])
    }

    override fun getItemCount() = elements.size

    class CameraSliderViewHolder(
        private val listener: Listener,
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemCameraModeSliderBinding? by viewBinding()

        fun bind(element: CameraSelectorUiModel) {
            binding?.btnMode?.text = itemView.context.getString(element.name)
            binding?.btnMode?.setWeight(if(element.isSelected) Typography.BOLD else Typography.REGULAR)

            itemView.setOnClickListener {
                listener.onCameraSliderItemClicked(it)
            }
        }

        companion object {
            @LayoutRes val LAYOUT = R.layout.view_item_camera_mode_slider

            fun create(listener: Listener, viewGroup: ViewGroup): CameraSliderViewHolder {
                val view = LayoutInflater
                    .from(viewGroup.context)
                    .inflate(LAYOUT, viewGroup, false)

                return CameraSliderViewHolder(listener, view)
            }
        }

    }

    interface Listener {
        fun onCameraSliderItemClicked(view: View)
    }

}
