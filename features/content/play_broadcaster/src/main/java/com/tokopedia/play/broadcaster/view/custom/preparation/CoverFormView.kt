package com.tokopedia.play.broadcaster.view.custom.preparation

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import com.tokopedia.kotlin.extensions.view.loadImageRounded
import com.tokopedia.play.broadcaster.databinding.ViewPlayBroPreparationCoverFormBinding

/**
 * Created By : Jonathan Darwin on January 26, 2022
 */
class CoverFormView : ConstraintLayout {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )
    constructor(
        context: Context,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private val binding = ViewPlayBroPreparationCoverFormBinding.inflate(
        LayoutInflater.from(context),
        this,
        true
    )
    private var mListener: Listener? = null

    private var isCoverAvailable = false

    init {
        binding.icCloseCoverForm.setOnClickListener { mListener?.onCloseCoverForm() }
        binding.clCoverFormPreview.setOnClickListener {
            mListener?.onClickCoverPreview(isCoverAvailable)
        }
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mListener = null
    }

    fun setCover(imageUrl: String) {
        binding.ivCoverImagePlaceholder.visibility = View.GONE
        binding.ivCoverFormPreview.setImageUrl(imageUrl)
        isCoverAvailable = true
    }

    fun setTitle(title: String) {
        binding.tvCoverFormTitle.text = title
    }

    fun setShopName(shopName: String) {
        binding.tvCoverFormShopName.text = shopName
    }

    fun setListener(listener: Listener) {
        mListener = listener
    }

    interface Listener {
        fun onCloseCoverForm()
        fun onClickCoverPreview(isEditCover: Boolean)
    }
}