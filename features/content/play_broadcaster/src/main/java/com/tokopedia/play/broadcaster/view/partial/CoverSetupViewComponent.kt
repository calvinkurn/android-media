package com.tokopedia.play.broadcaster.view.partial

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.net.Uri
import android.view.ViewGroup
import android.widget.*
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.OnLifecycleEvent
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.tokopedia.kotlin.extensions.view.*
import com.tokopedia.play.broadcaster.R
import com.tokopedia.play_common.util.extension.doOnLayout
import com.tokopedia.play_common.util.extension.doOnPreDraw
import com.tokopedia.play_common.util.extension.isLocal
import com.tokopedia.play_common.view.addKeyboardInsetsListener
import com.tokopedia.play_common.viewcomponent.ViewComponent
import com.tokopedia.play_common.viewcomponent.ViewComponentListener
import com.tokopedia.unifycomponents.LoaderUnify
import com.tokopedia.unifycomponents.UnifyButton


class CoverSetupViewComponent(
        container: ViewGroup,
        private val dataSource: DataSource,
        private val listener: Listener
) : ViewComponent(container, R.id.cl_cover_setup) {

    private val ivCoverImage: ImageView = findViewById(R.id.iv_cover_image)
    private val loaderImage: LoaderUnify = findViewById(R.id.loader_image)
    private val llChangeCover: LinearLayout = findViewById(R.id.ll_change_cover)
    private val tvAddChangeCover: TextView = findViewById(R.id.tv_add_change_cover)
    private val btnNext: UnifyButton = findViewById(R.id.btn_next)
    private val clCropButton: ConstraintLayout = findViewById(R.id.cl_crop_button)
    private val flCropSize: FrameLayout = findViewById(R.id.fl_crop_size)
    private val clCropParent: ConstraintLayout = findViewById(R.id.cl_crop_parent)
    private val slCropParent: ScrollView = findViewById(R.id.sl_crop_parent)

    init {
        llChangeCover.setOnClickListener { listener.onImageAreaClicked(this) }
        ivCoverImage.setOnClickListener { listener.onImageAreaClicked(this) }
        btnNext.setOnClickListener {
            if (btnNext.isLoading) return@setOnClickListener
            listener.onNextButtonClicked(this)
        }

        setupScrollView()

        updateViewState()

        flCropSize.doOnLayout { flCrop ->
            clCropParent.layoutParams.height = flCrop.measuredHeight

            ivCoverImage.doOnPreDraw {
                ivCoverImage.layoutParams.height = flCrop.measuredHeight
                ivCoverImage.layoutParams.width = flCrop.measuredWidth

                ivCoverImage.invalidate()
                ivCoverImage.requestLayout()
            }
        }

        rootView.addKeyboardInsetsListener(triggerOnAttached = false) { isVisible, _ ->
            stabilizeScroll(!isVisible)
            clCropButton.showWithCondition(!isVisible)
        }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        rootView.setOnApplyWindowInsetsListener(null)
        listener.onViewDestroyed(this)
    }

    fun getBottomActionView() = clCropButton

    fun setLoading(isLoading: Boolean) {
        btnNext.isLoading = isLoading
    }

    fun setImage(uri: Uri?) {
        if (uri != null) {
            if (uri.isLocal()) ivCoverImage.setImageURI(uri)
            else {
                loaderImage.show()
                Glide.with(ivCoverImage.context)
                        .load(uri)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(e: GlideException?, model: Any?, target: Target<Drawable>?, isFirstResource: Boolean): Boolean {
                                loaderImage.hide()
                                return false
                            }

                            override fun onResourceReady(resource: Drawable?, model: Any?, target: Target<Drawable>?, dataSource: com.bumptech.glide.load.DataSource?, isFirstResource: Boolean): Boolean {
                                loaderImage.hide()
                                return false
                            }
                        })
                        .into(ivCoverImage)
            }
        }
        else {
            ivCoverImage.setImageDrawable(null)
            loaderImage.hide()
        }

        updateViewState()
    }

    fun updateViewState() {
        updateAddChangeCover()
        updateButtonState()
    }

    private fun updateButtonState() {
        btnNext.isEnabled = dataSource.getCurrentCoverUri() != null
    }

    fun clickNext() {
        btnNext.performClick()
    }

    private fun updateAddChangeCover() {
        tvAddChangeCover.text = getString(
                if (dataSource.getCurrentCoverUri() != null) R.string.play_prepare_cover_title_change_cover_label
                else R.string.play_prepare_cover_title_add_cover_label
        )
    }

    private fun stabilizeScroll(shouldStabilize: Boolean) {
        if (shouldStabilize) slCropParent.smoothScrollTo(0, 0)
        else slCropParent.smoothScrollTo(0, slCropParent.bottom)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setupScrollView() {
        slCropParent.setOnTouchListener { _, _ -> true }
    }

    interface Listener : ViewComponentListener<CoverSetupViewComponent> {

        fun onImageAreaClicked(view: CoverSetupViewComponent)
        fun onNextButtonClicked(view: CoverSetupViewComponent)
        fun onTitleAreaHasFocus()
    }

    interface DataSource {

        fun getMaxTitleCharacters(): Int
        fun isValidCoverTitle(coverTitle: String): Boolean
        fun getCurrentCoverUri(): Uri?
    }
}