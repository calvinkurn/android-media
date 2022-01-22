package com.tokopedia.picker.ui.fragment.camera.recyclers.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.tokopedia.picker.R
import com.tokopedia.picker.databinding.ViewItemCameraModeSliderBinding
import com.tokopedia.utils.view.binding.viewBinding

class CameraSliderAdapter(
    private val elements: List<String>,
    private val listener: Listener
) : RecyclerView.Adapter<CameraSliderAdapter.CameraSliderViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CameraSliderViewHolder {
        return CameraSliderViewHolder.create(listener, parent)
    }

    override fun onBindViewHolder(holder: CameraSliderViewHolder, position: Int) {
        holder.bind(elements[position])
    }

    override fun getItemCount(): Int {
        return elements.size
    }

    class CameraSliderViewHolder(
        private val listener: Listener,
        view: View
    ) : RecyclerView.ViewHolder(view) {

        private val binding: ViewItemCameraModeSliderBinding? by viewBinding()
        private val context by lazy { itemView.context }

        fun bind(element: String) {
            binding?.btnMode?.text = element

            binding?.btnMode?.setTextColor(
                ContextCompat.getColor(
                    context,
                    android.R.color.white
                )
            )

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