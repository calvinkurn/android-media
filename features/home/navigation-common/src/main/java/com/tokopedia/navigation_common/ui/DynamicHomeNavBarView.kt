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
import com.tokopedia.navigation_common.util.inDarkMode
import com.tokopedia.navigation_common.util.isDeviceAnimationDisabled
import com.tokopedia.unifyprinciples.NestShadow.isDarkMode
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

    private var mIsForceDarkMode = false
    private val isDarkMode: Boolean
        get() = if (mIsForceDarkMode) true else context.isDarkMode()

    private val darkModeContext: Context = context.inDarkMode

    private val uiModeAwareContext
        get() = if (isDarkMode) darkModeContext else context

    private var mSelectedItemId: BottomNavItemId? = null

    private val modelMap = mutableMapOf<BottomNavItemId, BottomNavBarUiModel>()

    init {
        super.setOrientation(HORIZONTAL)
        refresh()
    }

    override fun setOrientation(orientation: Int) {
        error("Orientation is not allowed to be changed")
    }

    fun setModelList(modelList: List<BottomNavBarUiModel>) {
        modelMap.clear()
        modelMap.putAll(modelList.associateBy { it.uniqueId })
        buildMenu(modelList)

        val firstModel = modelList.firstOrNull() ?: return
        select(mSelectedItemId ?: firstModel.uniqueId)
    }

    fun select(itemId: BottomNavItemId) {
        val model = modelMap[itemId] ?: return
        mListener?.onItemSelected(this, model, mSelectedItemId == itemId)
        mSelectedItemId = itemId
        rebindAllItems(itemId)
    }

    fun forceDarkMode(shouldDarkMode: Boolean) {
        mIsForceDarkMode = shouldDarkMode
        refresh()
    }

    fun setListener(listener: Listener?) {
        mListener = listener
    }

    private fun refresh() {
        bindBackground()
        rebindAllItems()
    }

    private fun rebindAllItems(selectedItemId: BottomNavItemId? = mSelectedItemId) {
        children.forEach {
            val stateHolder = it.stateHolder ?: return@forEach
            val binding = runCatching { ItemBottomNavbarBinding.bind(it) }.getOrNull() ?: return@forEach
            binding.bindModel(stateHolder, selectedItemId == stateHolder.model.uniqueId)
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

        root.setOnClickListener { select(model.uniqueId) }

        updateState(stateHolder.copy(isSelected = isSelected))
    }

    private fun ItemBottomNavbarBinding.updateState(newState: StateHolder) {
        root.tag = newState
    }

    private val View.stateHolder: StateHolder?
        get() = tag as? StateHolder

    //TODO("Handle dark mode in a better way")
    private fun LottieAnimationView.bindAsset(
        model: BottomNavBarUiModel,
        isSelected: Boolean?,
        prevIsSelected: Boolean?
    ) {
        val isDarkMode = this@DynamicHomeNavBarView.isDarkMode
        val asset = when {
            isSelected == null -> model.assets[if (isDarkMode) ASSET_STATIC_INACTIVE_DARK else ASSET_STATIC_INACTIVE_LIGHT]
            context.isDeviceAnimationDisabled() || isSelected == prevIsSelected -> {
                model.assets[
                    if (isSelected) {
                        if (isDarkMode) ASSET_STATIC_ACTIVE_DARK else ASSET_STATIC_ACTIVE_LIGHT
                    }
                    else {
                        if (isDarkMode) ASSET_STATIC_INACTIVE_DARK else ASSET_STATIC_INACTIVE_LIGHT
                    }
                ]
            }
            else -> {
                run {
                    val assetId = if (isSelected) {
                        if (isDarkMode) ASSET_ANIM_ACTIVE_DARK else ASSET_ANIM_ACTIVE_LIGHT
                    } else {
                        if (isDarkMode) ASSET_ANIM_INACTIVE_DARK else ASSET_ANIM_INACTIVE_LIGHT
                    }
                    val asset = model.assets[assetId]
                    val isCached = asset?.let { cacheManager.isUrlCached(it.url) } ?: false

                    if (isCached) asset
                    else {
                        asset?.let { cacheManager.cacheFromUrl(it.url) }

                        model.assets[
                            if (isSelected) {
                                if (isDarkMode) ASSET_STATIC_ACTIVE_DARK else ASSET_STATIC_ACTIVE_LIGHT
                            }
                            else {
                                if (isDarkMode) ASSET_STATIC_INACTIVE_DARK else ASSET_STATIC_INACTIVE_LIGHT
                            }
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
                uiModeAwareContext,
                if (isSelected == true) unifyprinciplesR.color.Unify_GN500
                else navigation_commonR.color.dynamic_bottom_navbar_item_title
            )
        )
    }

    private fun bindBackground() {
        setBackgroundColor(
            ContextCompat.getColor(uiModeAwareContext, unifyprinciplesR.color.Unify_NN0)
        )
    }

    companion object {
        private const val ASSET_STATIC_INACTIVE_LIGHT = "unselected_icon_light_mode"
        private const val ASSET_STATIC_ACTIVE_LIGHT = "selected_icon_light_mode"
        private const val ASSET_ANIM_INACTIVE_LIGHT = "inactive_icon_light_mode"
        private const val ASSET_ANIM_ACTIVE_LIGHT = "active_icon_light_mode"

        private const val ASSET_STATIC_INACTIVE_DARK = "unselected_icon_dark_mode"
        private const val ASSET_STATIC_ACTIVE_DARK = "selected_icon_dark_mode"
        private const val ASSET_ANIM_INACTIVE_DARK = "inactive_icon_dark_mode"
        private const val ASSET_ANIM_ACTIVE_DARK = "active_icon_dark_mode"
    }

    interface Listener {
        fun onItemSelected(view: DynamicHomeNavBarView, model: BottomNavBarUiModel, isReselected: Boolean)
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
