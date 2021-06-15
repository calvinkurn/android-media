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
import kotlinx.android.synthetic.main.item_flight_cancellation_attachment.view.*
import java.io.File

/**
 * @author by furqan on 20/07/2020
 */
class FlightCancellationAttachmentViewHolder(itemView: View,
                                             private val listener: FlightCancellationAttachmentAdapterTypeFactory.AdapterInteractionListener,
                                             private val showChangeButton: Boolean)
    : AbstractViewHolder<FlightCancellationAttachmentModel>(itemView) {

    override fun bind(element: FlightCancellationAttachmentModel) {
        with(itemView) {
            tv_filename.text = element.filename
            tv_passenger_name.text = element.passengerName

            if (element.filepath != null && element.filepath.isNotEmpty()) {
                Glide.with(itemView.context)
                        .asBitmap()
                        .load(File((element.filepath)))
                        .centerCrop()
                        .into(getRoundedImageViewTarget(iv_attachment))

                iv_attachment.setOnClickListener {
                    listener.viewImage(element.filepath)
                }

                resizeAttachmentTo60()

                if (showChangeButton) {
                    tv_change_image.visibility = View.VISIBLE
                    tv_view_image.visibility = View.VISIBLE
                } else {
                    tv_change_image.visibility = View.GONE
                    tv_view_image.visibility = View.GONE
                }

                tv_change_image.setOnClickListener {
                    listener.onUploadAttachmentButtonClicked(adapterPosition)
                }

                image_container.setBackgroundResource(R.drawable.bg_flight_gray_rounded_stroke)
            } else {
                iv_attachment.setOnClickListener {
                    listener.onUploadAttachmentButtonClicked(adapterPosition)
                }

                tv_change_image.visibility = View.GONE
                image_container.setBackgroundResource(R.drawable.bg_flight_gray_rounded_dashed)

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
        with(itemView) {
            iv_attachment.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.attachment_60)
            iv_attachment.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.attachment_60)
            iv_attachment.requestLayout()
        }
    }

    private fun resizeAttachmentTo40() {
        with(itemView) {
            iv_attachment.layoutParams.height = context.resources.getDimensionPixelSize(R.dimen.attachment_40)
            iv_attachment.layoutParams.width = context.resources.getDimensionPixelSize(R.dimen.attachment_40)
            iv_attachment.requestLayout()
        }
    }

    private fun renderFileName(element: FlightCancellationAttachmentModel) {
        with(itemView) {
            if (element.filename.isNotEmpty()) {
                tv_filename.visibility = View.VISIBLE
            } else {
                tv_filename.visibility = View.GONE
            }
        }
    }

    companion object {
        val LAYOUT = R.layout.item_flight_cancellation_attachment

        private const val ROUNDED_RADIUS = 5f
    }
}