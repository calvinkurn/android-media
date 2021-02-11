package com.tokopedia.play.broadcaster.ui.viewholder

import android.graphics.drawable.Drawable
import android.view.View
import android.widget.*
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.abstraction.common.utils.view.MethodChecker
import com.tokopedia.adapterdelegate.BaseViewHolder
import com.tokopedia.kotlin.extensions.view.gone
import com.tokopedia.kotlin.extensions.view.show
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play.broadcaster.type.StockAvailable
import com.tokopedia.play.broadcaster.ui.model.ProductContentUiModel
import com.tokopedia.play.broadcaster.util.extension.loadImageFromUrl
import com.tokopedia.play.broadcaster.view.state.NotSelectable
import com.tokopedia.play.broadcaster.view.state.Selectable
import com.tokopedia.play_common.util.extension.compatTransitionName
import com.tokopedia.unifycomponents.selectioncontrol.CheckboxUnify

/**
 * Created by jegul on 27/05/20
 */
class ProductSelectableViewHolder(
        itemView: View,
        private val listener: Listener? = null,
        showSelection: Boolean = true
) : BaseViewHolder(itemView) {

    private val flImage: FrameLayout = itemView.findViewById(R.id.fl_image)
    private val ivImage: ImageView = itemView.findViewById(R.id.iv_image)
    private val cbSelected: CheckboxUnify = itemView.findViewById(R.id.cb_selected)
    private val tvProductName: TextView = itemView.findViewById(R.id.tv_product_name)
    private val tvProductAmount: TextView = itemView.findViewById(R.id.tv_product_amount)

    private var onCheckedChangeListener: (CompoundButton, Boolean) -> Unit = { _ , _ -> }

    private val imageOverlayDrawable by lazy { MethodChecker.getDrawable(flImage.context, R.drawable.fg_play_image_overlay) }

    private val imageRequestListener = object : RequestListener<Drawable> {
        override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
            listener?.onImageLoaded(position = adapterPosition, isSuccess = false)
            return false
        }

        override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: DataSource?, isFirstResource: Boolean): Boolean {
            listener?.onImageLoaded(position = adapterPosition, isSuccess = true)
            return false
        }
    }

    init {
        if (showSelection) {
            cbSelected.show()
            flImage.foreground = imageOverlayDrawable
        }
        else {
            cbSelected.gone()
            flImage.foreground = null
        }
    }

    fun bind(item: ProductContentUiModel) {
        cbSelected.forceSetCheckbox(item.isSelectedHandler(item.id))
        ivImage.loadImageFromUrl(item.imageUrl, imageRequestListener)
        ivImage.compatTransitionName = item.transitionName

        tvProductName.text = item.name
        tvProductAmount.text = getString(R.string.play_product_stock_amount, if (item.stock is StockAvailable) item.stock.stock else "-")

        itemView.setOnClickListener {
            if (cbSelected.isEnabled) {
                val isSelecting = !cbSelected.isChecked
                when (val state = item.isSelectable(isSelecting)) {
                    Selectable -> triggerCheckbox()
                    is NotSelectable -> listener?.onProductSelectError(state.reason)
                }
            }
        }

        onCheckedChangeListener = { _, isChecked ->
            listener?.onProductSelectStateChanged(item.id, isChecked)
        }
        cbSelected.setOnCheckedChangeListener(onCheckedChangeListener)
    }

    private fun triggerCheckbox() {
        cbSelected.isChecked = !cbSelected.isChecked
    }

    private fun CheckBox.forceSetCheckbox(isChecked: Boolean) {
        setOnCheckedChangeListener(null)
        cbSelected.isChecked = isChecked
        setOnCheckedChangeListener(onCheckedChangeListener)
    }

    companion object {

        val LAYOUT = R.layout.item_play_product_selectable
    }

    interface Listener {

        fun onImageLoaded(position: Int, isSuccess: Boolean) {}
        fun onProductSelectStateChanged(productId: Long, isSelected: Boolean)
        fun onProductSelectError(reason: Throwable)
    }
}