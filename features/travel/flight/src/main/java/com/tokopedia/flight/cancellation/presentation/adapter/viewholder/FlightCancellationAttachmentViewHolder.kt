package com.tokopedia.flight.cancellation.presentation.adapter.viewholder

import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.BitmapImageViewTarget
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.flight.R
import com.tokopedia.flight.cancellation.presentation.adapter.FlightCancellationAttachmentAdapterTypeFactory
import com.tokopedia.flight.cancellation.presentation.model.FlightCancellationAttachmentModel
import com.tokopedia.flight.databinding.ItemFlightCancellationAttachmentBinding
import java.io.File

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationAttachmentViewHolder(val binding: ItemFlightCancellationAttachmentBinding,
                                             private val listener: FlightCancellationAttachmentAdapterTypeFactory.AdapterInteractionListener,
                                             private val showChangeButton: Boolean)
    : AbstractViewHolder<FlightCancellationAttachmentModel>(binding.root) {

    override fun bind(element: FlightCancellationAttachmentModel) {
        with(binding) {
            tvFilename.text = element.filename
            tvPassengerName.text = element.passengerName

            if (element.filepath != null && element.filepath.isNotEmpty()) {
                Glide.with(itemView.context)
                        .asBitmap()
                        .load(File((element.filepath)))
                        .centerCrop()
                        .into(getRoundedImageViewTarget(ivAttachment))

                ivAttachment.setOnClickListener {
                    listener.viewImage(element.filepath)
                }

                resizeAttachmentTo60()

                if (showChangeButton) {
                    tvChangeImage.visibility = View.VISIBLE
                    tvViewImage.visibility = View.VISIBLE
                } else {
                    tvChangeImage.visibility = View.GONE
                    tvViewImage.visibility = View.GONE
                }

                tvChangeImage.setOnClickListener {
                    listener.onUploadAttachmentButtonClicked(adapterPosition)
                }

                imageContainer.setBackgroundResource(R.drawable.bg_flight_gray_rounded_stroke)
            } else {
                ivAttachment.setOnClickListener {
                    listener.onUploadAttachmentButtonClicked(adapterPosition)
                }

                tvChangeImage.visibility = View.GONE
                imageContainer.setBackgroundResource(R.drawable.bg_flight_gray_rounded_dashed)

                resizeAttachmentTo40()
            }

            renderFileName(element)
        }
    }

    private fun getRoundedImageViewTarget(imageView: ImageView): BitmapImageViewTarget =
            object : BitmapImageViewTarget(imageView) {
                override fun setResource(resource: Bitmap?) {
                    val circularBitmapDrawable = RoundedBitmapDrawableFactory.create(imageView.context.resources, resource)
                    circularBitmapDrawable.cornerRadius = ROUNDED_RADIUS
                    imageView.setImageDrawable(circularBitmapDrawable)
                }
            }

    private fun resizeAttachmentTo60() {
        with(binding) {
            ivAttachment.layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.attachment_60)
            ivAttachment.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.attachment_60)
            ivAttachment.requestLayout()
        }
    }

    private fun resizeAttachmentTo40() {
        with(binding) {
            ivAttachment.layoutParams.height = itemView.context.resources.getDimensionPixelSize(R.dimen.attachment_40)
            ivAttachment.layoutParams.width = itemView.context.resources.getDimensionPixelSize(R.dimen.attachment_40)
            ivAttachment.requestLayout()
        }
    }

    private fun renderFileName(element: FlightCancellationAttachmentModel) {
        with(binding) {
            if (element.filename.isNotEmpty()) {
                tvFilename.visibility = View.VISIBLE
            } else {
                tvFilename.visibility = View.GONE
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_attachment

        private const val ROUNDED_RADIUS = 5f
    }
}