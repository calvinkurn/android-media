package com.tokopedia.navigation_common.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.children
import com.airbnb.lottie.LottieAnimationView
import com.tokopedia.media.loader.loadImage
import com.tokopedia.navigation_common.databinding.ItemBottomNavbarBinding
import com.tokopedia.navigation_common.util.LottieCacheManager
import com.tokopedia.navigation_common.util.isDeviceAnimationDisabled
import com.tokopedia.unifyprinciples.R as unifyprinciplesR
import com.tokopedia.navigation_common.R as navigation_commonR

class DynamicHomeNavBarView : LinearLayout {

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    constructor(
        context: Context?,
        attrs: AttributeSet?,
        defStyleAttr: Int,
        defStyleRes: Int
    ) : super(context, attrs, defStyleAttr, defStyleRes)

    private var mListener: Listener? = null

    private val cacheManager = LottieCacheManager(context)

    init {
        super.setOrientation(HORIZONTAL)
    }

    override fun setOrientation(orientation: Int) {
        error("Orientation is not allowed to be changed")
    }

    fun setModelList(modelList: List<BottomNavBarUiModel>) {
        buildMenu(modelList)
        select(BottomNavBarItemType.Home)
    }

    fun select(type: BottomNavBarItemType) {
        var childToBeSelected: View? = null

        run selectChild@ {
            children.forEach {
                val stateHolder = it.stateHolder ?: return@forEach
                if (stateHolder.model.type != type) return@forEach
                childToBeSelected = it
                return@selectChild
            }
        }

        childToBeSelected?.let { selectInternal(it) }
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun selectInternal(view: View) {
        children.forEach {
            val stateHolder = it.stateHolder ?: return@forEach
            val binding = runCatching { ItemBottomNavbarBinding.bind(it) }.getOrNull() ?: return@forEach
            binding.bindModel(stateHolder, it == view)
        }
    }

    private fun buildMenu(modelList: List<BottomNavBarUiModel>) {
        removeAllViews()
        modelList.map(::buildMenuView).forEach(::addToParent)
    }

    private fun buildMenuView(model: BottomNavBarUiModel): View {
        val binding = ItemBottomNavbarBinding.inflate(
            LayoutInflater.from(context),
            this,
            false
        )

        val stateHolder = StateHolder.init(model)
        binding.root.tag = stateHolder
        binding.bindModel(stateHolder, null)

        return binding.root
    }

    private fun addToParent(view: View) {
        val lParams = LayoutParams(
            0,
            LayoutParams.MATCH_PARENT
        )
        lParams.weight = 1f
        view.layoutParams = lParams
        addView(view)
    }

    private fun ItemBottomNavbarBinding.bindModel(
        stateHolder: StateHolder,
        isSelected: Boolean?,
    ) {
        val (model, prevIsSelected) = stateHolder
        ivIcon.bindAsset(model, isSelected, prevIsSelected)
        tvTitle.bindText(model, isSelected)

        mListener?.onItemSelected(root, model, isSelected == true && prevIsSelected == true)
        root.setOnClickListener { select(model.type) }

        updateState(stateHolder.copy(isSelected = isSelected))
    }

    private fun ItemBottomNavbarBinding.updateState(newState: StateHolder) {
        root.tag = newState
    }

    private val View.stateHolder: StateHolder?
        get() = tag as? StateHolder

    private fun LottieAnimationView.bindAsset(
        model: BottomNavBarUiModel,
        isSelected: Boolean?,
        prevIsSelected: Boolean?
    ) {
        val asset = when {
            isSelected == null -> model.assets[ASSET_STATIC_INACTIVE_LIGHT]
            isSelected == prevIsSelected -> null
            context.isDeviceAnimationDisabled() -> {
                model.assets[
                    if (isSelected) ASSET_STATIC_ACTIVE_LIGHT
                    else ASSET_STATIC_INACTIVE_LIGHT
                ]
            }
            else -> {
                run {
                    val assetId = if (isSelected) ASSET_ANIM_ACTIVE_LIGHT else ASSET_ANIM_INACTIVE_LIGHT
                    val asset = model.assets[assetId]
                    val isCached = asset?.let { cacheManager.isUrlCached(it.url) } ?: false

                    if (isCached) asset
                    else {
                        asset?.let { cacheManager.cacheFromUrl(it.url) }

                        model.assets[
                            if (isSelected) ASSET_STATIC_ACTIVE_LIGHT
                            else ASSET_STATIC_INACTIVE_LIGHT
                        ]
                    }
                }
            }
        } ?: return

        pauseAnimation()

        when (asset) {
            is BottomNavBarAsset.Image -> loadImage(asset.url)
            is BottomNavBarAsset.Lottie -> {
                setAnimationFromUrl(asset.url)
                playAnimation()
            }
        }
    }

    private fun TextView.bindText(model: BottomNavBarUiModel, isSelected: Boolean?) {
        text = model.title
        setTextColor(
            ContextCompat.getColor(
                context,
                if (isSelected == true) unifyprinciplesR.color.Unify_GN500
                else navigation_commonR.color.dynamic_bottom_navbar_item_title
            )
        )
    }

    companion object {
        private const val ASSET_STATIC_INACTIVE_LIGHT = "unselected_icon_light_mode"
        private const val ASSET_STATIC_ACTIVE_LIGHT = "selected_icon_light_mode"
        private const val ASSET_ANIM_INACTIVE_LIGHT = "inactive_icon_light_mode"
        private const val ASSET_ANIM_ACTIVE_LIGHT = "active_icon_light_mode"
    }

    interface Listener {
        fun onItemSelected(view: View, model: BottomNavBarUiModel, isReselected: Boolean)
    }

    data class StateHolder(
        val model: BottomNavBarUiModel,
        val isSelected: Boolean?,
    ) {
        companion object {
            fun init(model: BottomNavBarUiModel): StateHolder {
                return StateHolder(model, null)
            }
        }
    }
}
