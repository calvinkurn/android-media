package com.tokopedia.vouchercreation.shop.create.view.viewholder.voucherimage

import android.app.Activity
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.View
import androidx.annotation.LayoutRes
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.signature.ObjectKey
import com.tokopedia.abstraction.base.view.adapter.viewholders.AbstractViewHolder
import com.tokopedia.kotlin.extensions.view.toBitmap
import com.tokopedia.vouchercreation.R
import com.tokopedia.vouchercreation.shop.create.view.painter.SquareVoucherPainter
import com.tokopedia.vouchercreation.shop.create.view.uimodel.voucherimage.PostVoucherUiModel
import kotlinx.android.synthetic.main.mvc_post_image.view.*

class PostVoucherViewHolder(itemView: View?,
                            private val activity: Activity?,
                            private val onSuccessGetPostBitmap: (Bitmap) -> Unit) : AbstractViewHolder<PostVoucherUiModel>(itemView) {

    companion object {
        @LayoutRes
        val RES_LAYOUT = R.layout.mvc_post_image

    }

    override fun bind(element: PostVoucherUiModel) {
        itemView.run {
            Glide.with(context)
                    .asDrawable()
                    .load(element.postBaseUiModel.postBaseUrl)
                    .signature(ObjectKey(System.currentTimeMillis().toString()))
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                            return false
                        }

                        override fun onResourceReady(resource: Drawable, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
                            activity?.runOnUiThread {
                                val bitmap = resource.toBitmap()
                                val painter = SquareVoucherPainter(context, bitmap, ::onSuccessGetBitmap)
                                painter.drawInfo(element)
                            }
                            return false
                        }
                    })
                    .submit()
        }
    }

    private fun onSuccessGetBitmap(bitmap: Bitmap) {
        activity?.runOnUiThread {
            itemView.postVoucherImage?.setImageBitmap(bitmap)
            onSuccessGetPostBitmap(bitmap)
        }
    }
}